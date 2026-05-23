/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.flowcentraltech.flowcentral.studio.web.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Entity editor object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EntityEditor {

    private final EntityDef entityDef;

    private Design design;

    private boolean readOnly;

    private EntityEditor(EntityDef entityDef, Design design) {
        this.entityDef = entityDef;
        this.design = design;
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public Design getDesign() {
        return design;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public enum Type {
        PARENT,
        ROOT,
        CHILD;

        public boolean isParent() {
            return PARENT.equals(this);
        }

        public boolean isRoot() {
            return ROOT.equals(this);
        }

        public boolean isChild() {
            return CHILD.equals(this);
        }
    }

    public static Builder newBuilder(AppletUtilities au, EntityDef entityDef) throws UnifyException {
        return new Builder(au, entityDef);
    }

    public static class Builder {

        private AppletUtilities au;

        private EntityDef entityDef;

        private List<DesignEntity> parents;

        private List<DesignEntity> children;

        private Set<String> building;

        public Builder(AppletUtilities au, EntityDef entityDef) throws UnifyException {
            this.au = au;
            this.entityDef = entityDef;
            this.parents = new ArrayList<DesignEntity>();
            this.children = new ArrayList<DesignEntity>();
            this.building = new HashSet<String>();
        }

        public EntityEditor build() throws UnifyException {
            building.add(entityDef.getLongName());
            final DesignEntity entity = createEntity(entityDef, Type.ROOT);
            return new EntityEditor(entityDef, new Design(entity, parents, children));
        }

        private DesignEntity createEntity(EntityDef _entityDef, final Type mode) throws UnifyException {
            return new DesignEntity(_entityDef.getLongName(), au.resolveSessionMessage(_entityDef.getLabel()),
                    getDesignFields(_entityDef, _entityDef.getBaseFieldDefList(), mode),
                    getDesignFields(_entityDef, _entityDef.getActFieldDefList(), mode),
                    getDesignFields(_entityDef, _entityDef.getChildFieldDefList(), mode),
                    getDesignFields(_entityDef, _entityDef.getChildListFieldDefList(), mode),
                    getDesignFields(_entityDef, _entityDef.getListOnlyFieldDefList(), mode));
        }

        private List<DesignField> getDesignFields(EntityDef __entityDef, List<EntityFieldDef> entityFields, final Type mode)
                throws UnifyException {
            if (!DataUtils.isBlank(entityFields)) {
                List<DesignField> fields = new ArrayList<DesignField>();
                DesignField pk = null;
                for (EntityFieldDef entityFieldDef : entityFields) {
                    final Optional<String> optional = au.generateFieldTypeSql(__entityDef.getLongName(), entityFieldDef);
                    final String type = entityFieldDef.isPrimaryKey() ? " [PK]"
                            : (entityFieldDef.isForeignKey() ? " [FK]"
                                    : (entityFieldDef.isChild() ? " [CH]"
                                            : (entityFieldDef.isChildList() ? " [CL]"
                                                    : (entityFieldDef.isListOnly() ? " [LO]" : ""))));
                    if (entityFieldDef.isPrimaryKey()) {
                        pk = new DesignField(type, entityFieldDef.getFieldName(),
                                optional.isPresent() ? optional.get() : "", !mode.isChild() ? __entityDef.getLongName() : null, null,
                                entityFieldDef.getDataType().displayIndex());
                    } else {
                        String fk = null;
                        if (!mode.isParent() && entityFieldDef.isForeignKey()) {
                            fk = entityFieldDef.isNonEnumForeignKey()
                                    ? au.getRefDef(entityFieldDef.getReferences()).getEntity()
                                    : entityFieldDef.getReferences();
                            if (mode.isChild() && !entityDef.getLongName().equals(fk)) {
                                fk = null;
                            }
                        }

                        fields.add(new DesignField(type, entityFieldDef.getFieldName(),
                                optional.isPresent() ? optional.get() : "", null, fk,
                                entityFieldDef.getDataType().displayIndex()));
                    }

                    if (mode.isRoot()) {
                        if (entityFieldDef.isForeignKey()) {
                            if (entityFieldDef.isNonEnumForeignKey()) {
                                final EntityDef _entityDef = au
                                        .getEntityDef(au.getRefDef(entityFieldDef.getReferences()).getEntity());
                                if (!building.contains(_entityDef.getLongName())) {
                                    building.add(_entityDef.getLongName());
                                    parents.add(createEntity(_entityDef, Type.PARENT));
                                }
                            } else {
                                if (!building.contains(entityFieldDef.getReferences())) {
                                    building.add(entityFieldDef.getReferences());
                                    final String name = au.getStaticListEnumType(entityFieldDef.getReferences())
                                            .getSimpleName();
                                    parents.add(new DesignEntity(entityFieldDef.getReferences(), name,
                                            Collections.emptyList(),
                                            Arrays.asList(
                                                    new DesignField(" [PK]", "code", "VARCHAR(32) NOT NULL", entityFieldDef.getReferences(), null,
                                                            0),
                                                    new DesignField("", "description", "VARCHAR(64) NOT NULL", null, null,
                                                            1)),
                                            Collections.emptyList(), Collections.emptyList(), Collections.emptyList()));
                                }
                            }
                        } else if (entityFieldDef.isChildRef()) {
                            final EntityDef _entityDef = au
                                    .getEntityDef(au.getRefDef(entityFieldDef.getRefLongName()).getEntity());
                            if (!building.contains(_entityDef.getLongName())) {
                                building.add(_entityDef.getLongName());
                                children.add(createEntity(_entityDef, Type.CHILD));
                            }
                        }
                    }
                }

                DataUtils.sortAscending(fields, DesignField.class, "name");
                DataUtils.sortAscending(fields, DesignField.class, "index");

                if (pk != null) {
                    fields.add(0, pk);
                }

                return fields;
            }

            return Collections.emptyList();
        }
    }

    public static class DesignField {

        private String type;

        private String name;

        private String schema;

        private String pk;

        private String fk;

        private int index;

        public DesignField(String type, String name, String schema, String pk, String fk, int index) {
            this.type = type;
            this.name = name;
            this.schema = schema;
            this.pk = pk;
            this.fk = fk;
            this.index = index;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public String getSchema() {
            return schema;
        }

        public String getPk() {
            return pk;
        }

        public String getFk() {
            return fk;
        }

        public int getIndex() {
            return index;
        }

    }

    public static class DesignEntity {

        private String name;

        private String caption;

        private List<DesignField> bfields;

        private List<DesignField> afields;

        private List<DesignField> cfields;

        private List<DesignField> clfields;

        private List<DesignField> lfields;

        public DesignEntity(String name, String caption, List<DesignField> bfields, List<DesignField> afields,
                List<DesignField> cfields, List<DesignField> clfields, List<DesignField> lfields) {
            this.name = name;
            this.caption = caption;
            this.bfields = bfields;
            this.afields = afields;
            this.cfields = cfields;
            this.clfields = clfields;
            this.lfields = lfields;
        }

        public DesignEntity() {

        }

        public String getName() {
            return name;
        }

        public String getCaption() {
            return caption;
        }

        public List<DesignField> getBfields() {
            return bfields;
        }

        public List<DesignField> getAfields() {
            return afields;
        }

        public List<DesignField> getCfields() {
            return cfields;
        }

        public List<DesignField> getClfields() {
            return clfields;
        }

        public List<DesignField> getLfields() {
            return lfields;
        }

    }

    public static class Design {

        private DesignEntity entity;

        private List<DesignEntity> parents;

        private List<DesignEntity> children;

        public Design(DesignEntity entity, List<DesignEntity> parents, List<DesignEntity> children) {
            this.entity = entity;
            this.parents = parents;
            this.children = children;
        }

        public Design(DesignEntity entity) {
            this.entity = entity;
        }

        public Design() {

        }

        public DesignEntity getEntity() {
            return entity;
        }

        public List<DesignEntity> getParents() {
            return parents;
        }

        public List<DesignEntity> getChildren() {
            return children;
        }

    }

}

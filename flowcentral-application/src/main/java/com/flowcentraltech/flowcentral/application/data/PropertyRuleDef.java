/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.application.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Property rule definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class PropertyRuleDef extends BaseApplicationEntityDef {

    private EntityDef entityDef;

    private String choiceField;

    private String listField;

    private String propNameField;

    private String propValField;

    private PropertyListDef defaultList;

    private boolean ignoreCase;

    private Map<String, PropertyRuleChoiceDef> choiceDefMap;

    private List<PropertyRuleChoiceDef> choiceDefList;

    private PropertyRuleDef(EntityDef entityDef, String choiceField, String listField, String propNameField,
            String propValField, PropertyListDef defaultList, boolean ignoreCase,
            Map<String, PropertyRuleChoiceDef> choiceDefMap, List<PropertyRuleChoiceDef> choiceDefList,
            ApplicationEntityNameParts nameParts, String description, Long id, long version) {
        super(nameParts, description, id, version);
        this.entityDef = entityDef;
        this.choiceField = choiceField;
        this.listField = listField;
        this.propNameField = propNameField;
        this.propValField = propValField;
        this.defaultList = defaultList;
        this.ignoreCase = ignoreCase;
        this.choiceDefMap = choiceDefMap;
        this.choiceDefList = choiceDefList;
    };

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public String getChoiceField() {
        return choiceField;
    }

    public String getListField() {
        return listField;
    }

    public String getPropNameField() {
        return propNameField;
    }

    public String getPropValField() {
        return propValField;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public Map<String, PropertyRuleChoiceDef> getChoiceDefMap() {
        return choiceDefMap;
    }

    public List<PropertyRuleChoiceDef> getChoiceDefList() {
        return choiceDefList;
    }

    public PropertyListDef getPropertyListDef(Entity entityInst) throws UnifyException {
        String choice = DataUtils.convert(String.class, ReflectUtils.getBeanProperty(entityInst, choiceField));
        return getPropertyListDef(choice);
    }

    public PropertyListDef getPropertyListDef(String choice) {
        if (!StringUtils.isBlank(choice)) {
            if (ignoreCase) {
                choice = choice.toLowerCase();
            }

            PropertyRuleChoiceDef choiceDef = choiceDefMap.get(choice);
            if (choiceDef != null) {
                return choiceDef.getPropertyListDef();
            }
        }

        return defaultList;
    }

    public static Builder newBuilder(EntityDef entityDef, String choiceField, String listField, String propNameField,
            String propValField, PropertyListDef defaultList, boolean ignoreCase, String longName, String description,
            Long id, long version) {
        return new Builder(entityDef, choiceField, listField, propNameField, propValField, defaultList, ignoreCase,
                longName, description, id, version);
    }

    public static class Builder {

        private Map<String, PropertyRuleChoiceDef> choiceDefMap;

        private EntityDef entityDef;

        private String choiceField;

        private String listField;

        private String propNameField;

        private String propValField;

        private PropertyListDef defaultList;

        private boolean ignoreCase;

        private String longName;

        private String description;

        private Long id;

        private long version;

        public Builder(EntityDef entityDef, String choiceField, String listField, String propNameField,
                String propValField, PropertyListDef defaultList, boolean ignoreCase, String longName,
                String description, Long id, long version) {
            this.entityDef = entityDef;
            this.choiceField = choiceField;
            this.listField = listField;
            this.propNameField = propNameField;
            this.propValField = propValField;
            this.defaultList = defaultList;
            this.ignoreCase = ignoreCase;
            this.longName = longName;
            this.description = description;
            this.id = id;
            this.version = version;
            this.choiceDefMap = new LinkedHashMap<String, PropertyRuleChoiceDef>();
        }

        public Builder addChoiceDef(PropertyListDef propertyListDef, String choice) {
            String actChoice = choice;
            if (ignoreCase) {
                actChoice = choice.toLowerCase();
            }

            if (choiceDefMap.containsKey(actChoice)) {
                throw new RuntimeException("Property rule choice [" + choice + "] already exists in this definition.");
            }

            choiceDefMap.put(actChoice, new PropertyRuleChoiceDef(propertyListDef, actChoice));
            return this;
        }

        public PropertyRuleDef build() throws UnifyException {
            ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
            return new PropertyRuleDef(entityDef, choiceField, listField, propNameField, propValField, defaultList,
                    ignoreCase, DataUtils.unmodifiableMap(choiceDefMap),
                    DataUtils.unmodifiableList(new ArrayList<PropertyRuleChoiceDef>(choiceDefMap.values())), nameParts,
                    description, id, version);
        }
    }

}

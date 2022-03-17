/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.GeneratorNameUtils;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityBaseType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.batch.ConstraintAction;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.constant.TextCase;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Entity definition
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityDef extends BaseApplicationEntityDef {

    private EntityBaseType baseType;

    private ConfigType type;

    private List<EntityFieldDef> fieldDefList;

    private List<EntityFieldDef> filterFieldDefList;

    private List<EntityFieldDef> filterStringFieldDefList;

    private List<EntityFieldDef> setValueFieldDefList;

    private List<EntityFieldDef> descriptiveFieldDefList;

    private List<EntityFieldDef> stringFieldDefList;

    private List<EntityFieldDef> fkFieldDefList;

    private List<EntityFieldDef> listOnlyFieldDefList;

    private List<EntityFieldDef> childListFieldDefList;

    private List<EntityFieldDef> basicSearchFieldDefList;

    private List<EntityFieldDef> autoFormatFieldDefList;

    private List<EntityFieldDef> suggestionFieldDefList;

    private Map<String, EntityFieldDef> fieldDefMap;

    private Map<String, String> fieldLabelMap;

    private Map<String, EntityFieldDef> refFieldDefMap;

    private Map<String, EntityExpressionDef> expressionDefMap;

    private List<EntityAttachmentDef> attachmentList;

    private List<UniqueConstraintDef> uniqueConstraintList;

    private List<IndexDef> indexList;

    private Map<String, EntityUploadDef> uploadMap;

    private List<Listable> expressionListableList;

    private EntityFieldDef fosterParentIdDef;

    private EntityFieldDef fosterParentTypeDef;

    private EntityFieldDef categoryColumnDef;

    private String blobFieldName;

    private String originClassName;

    private String tableName;

    private String delegate;

    private String addPrivilege;

    private String editPrivilege;

    private String deletePrivilege;

    private String reportPrivilege;

    private String attachPrivilege;

    private String label;

    private boolean auditable;

    private boolean reportable;

    private boolean withListOnly;

    private boolean withCustomFields;

    private boolean withChildFields;

    private boolean withDescriptionField;

    private boolean withSuggestionFields;

    private boolean listOnlyTypesResolved;

    private EntityDef(EntityBaseType baseType, ConfigType type, Map<String, EntityFieldDef> fieldDefMap,
            List<EntityFieldDef> fieldDefList, List<EntityAttachmentDef> attachmentList,
            Map<String, EntityExpressionDef> expressionDefMap, List<UniqueConstraintDef> uniqueConstraintList,
            List<IndexDef> indexList, List<EntityUploadDef> uploadList, ApplicationEntityNameParts nameParts,
            String originClassName, String tableName, String label, String delegate, boolean auditable,
            boolean reportable, String description, Long id, long version) {
        super(nameParts, description, id, version);
        this.baseType = baseType;
        this.type = type;
        this.originClassName = originClassName;
        this.tableName = tableName;
        this.label = label;
        this.delegate = delegate;
        this.auditable = auditable;
        this.reportable = reportable;
        this.fieldDefList = fieldDefList;
        this.fieldDefMap = fieldDefMap;
        this.expressionDefMap = expressionDefMap;
        this.attachmentList = attachmentList;
        this.uniqueConstraintList = uniqueConstraintList;
        this.indexList = indexList;

        this.uploadMap = Collections.emptyMap();
        if (!DataUtils.isBlank(uploadList)) {
            this.uploadMap = new HashMap<String, EntityUploadDef>();
            for (EntityUploadDef entityUploadDef : uploadList) {
                this.uploadMap.put(entityUploadDef.getName(), entityUploadDef);
            }

            this.uploadMap = Collections.unmodifiableMap(this.uploadMap);
        }

        this.fieldLabelMap = new HashMap<String, String>();
        for (EntityFieldDef entityFieldDef : this.fieldDefList) {
            this.withSuggestionFields |= entityFieldDef.isWithSuggestionType();
            this.withListOnly |= entityFieldDef.isListOnly();
            this.withCustomFields |= entityFieldDef.isCustom();
            this.withChildFields |= entityFieldDef.isChildRef();
            this.withDescriptionField |= "description".equals(entityFieldDef.getFieldName());
            if (entityFieldDef.isDescriptive()) {
                if (this.descriptiveFieldDefList == null) {
                    this.descriptiveFieldDefList = new ArrayList<EntityFieldDef>();
                }

                this.descriptiveFieldDefList.add(entityFieldDef);
            }

            if (EntityFieldDataType.REF.equals(entityFieldDef.getDataType())
                    || EntityFieldDataType.REF_UNLINKABLE.equals(entityFieldDef.getDataType())) {
                if (refFieldDefMap == null) {
                    refFieldDefMap = new HashMap<String, EntityFieldDef>();
                }

                refFieldDefMap.put(entityFieldDef.getRefDef().getEntity(), entityFieldDef);
            } else if (EntityFieldDataType.FOSTER_PARENT_ID.equals(entityFieldDef.getDataType())) {
                fosterParentIdDef = entityFieldDef;
            } else if (EntityFieldDataType.FOSTER_PARENT_TYPE.equals(entityFieldDef.getDataType())) {
                fosterParentTypeDef = entityFieldDef;
            } else if (EntityFieldDataType.CATEGORY_COLUMN.equals(entityFieldDef.getDataType())) {
                categoryColumnDef = entityFieldDef;
            }

            this.fieldLabelMap.put(entityFieldDef.getFieldName(), entityFieldDef.getFieldLabel());
        }

        this.descriptiveFieldDefList = DataUtils.unmodifiableList(this.descriptiveFieldDefList);
        this.fieldLabelMap = DataUtils.unmodifiableMap(this.fieldLabelMap);
        this.refFieldDefMap = DataUtils.unmodifiableMap(refFieldDefMap);
        this.addPrivilege = PrivilegeNameUtils.getAddPrivilegeName(nameParts.getLongName());
        this.editPrivilege = PrivilegeNameUtils.getEditPrivilegeName(nameParts.getLongName());
        this.deletePrivilege = PrivilegeNameUtils.getDeletePrivilegeName(nameParts.getLongName());
        this.reportPrivilege = PrivilegeNameUtils.getReportablePrivilegeName(nameParts.getLongName());
        this.attachPrivilege = PrivilegeNameUtils.getAttachPrivilegeName(nameParts.getLongName());
    }

    public EntityBaseType getBaseType() {
        return baseType;
    }

    public ConfigType getType() {
        return type;
    }

    public boolean isWithCustomFields() {
        return withCustomFields;
    }

    public boolean isWithListOnly() {
        return withListOnly;
    }

    public boolean isWithChildFields() {
        return withChildFields;
    }

    public boolean isWithDescriptionField() {
        return withDescriptionField;
    }

    public boolean isWithSuggestionFields() {
        return withSuggestionFields;
    }

    public String getDelegate() {
        return delegate;
    }

    public boolean isStatic() {
        return type.isStatic();
    }

    public boolean isCustom() {
        return type.isCustom();
    }

    public void setListOnlyTypesResolved() {
        this.listOnlyTypesResolved = true;
    }

    public boolean isListOnlyTypesResolved() {
        return listOnlyTypesResolved;
    }

    public List<EntityFieldDef> getFieldDefList() {
        return fieldDefList;
    }

    public List<? extends Listable> getFilterFieldListables(LabelSuggestionDef labelSuggestionDef)
            throws UnifyException {
        if (!listOnlyTypesResolved) {
            throw new RuntimeException("List-only types are unresolved for entity definition [" + getLongName() + "].");
        }

        if (labelSuggestionDef != null) {
            List<ListData> list = new ArrayList<ListData>();
            for (EntityFieldDef entityFieldDef : fieldDefList) {
                if (entityFieldDef.isWithInputWidget() && entityFieldDef.isSupportFilter()) {
                    String suggestedLabel = labelSuggestionDef.getSuggestedLabel(entityFieldDef.getFieldName());
                    ListData item = StringUtils.isBlank(suggestedLabel)
                            ? new ListData(entityFieldDef.getListKey(), entityFieldDef.getListDescription())
                            : new ListData(entityFieldDef.getListKey(), suggestedLabel);
                    list.add(item);
                }
            }

            DataUtils.sortAscending(list, ListData.class, "listDescription");
            return list;
        }

        if (filterFieldDefList == null) {
            synchronized (this) {
                if (filterFieldDefList == null) {
                    List<EntityFieldDef> list = new ArrayList<EntityFieldDef>();
                    for (EntityFieldDef entityFieldDef : fieldDefList) {
                        if (entityFieldDef.isWithInputWidget() && entityFieldDef.isSupportFilter()) {
                            list.add(entityFieldDef);
                        }
                    }

                    list.add(getFieldDef("id"));
                    DataUtils.sortAscending(list, EntityFieldDef.class, "listDescription");
                    filterFieldDefList = DataUtils.unmodifiableList(list);
                }
            }
        }

        return filterFieldDefList;
    }

    public List<EntityFieldDef> getFilterStringFieldDefList() throws UnifyException {
        if (!listOnlyTypesResolved) {
            throw new RuntimeException("List-only types are unresolved for entity definition [" + getLongName() + "].");
        }

        if (filterStringFieldDefList == null) {
            synchronized (this) {
                if (filterStringFieldDefList == null) {
                    List<EntityFieldDef> list = new ArrayList<EntityFieldDef>();
                    for (EntityFieldDef entityFieldDef : fieldDefList) {
                        if (entityFieldDef.isString()
                                || (EntityFieldDataType.LIST_ONLY.equals(entityFieldDef.getDataType())
                                        && entityFieldDef.isWithResolvedTypeFieldDef() && EntityFieldDataType.STRING
                                                .equals(entityFieldDef.getResolvedTypeFieldDef().getDataType()))) {
                            list.add(entityFieldDef);
                        }
                    }

                    DataUtils.sortAscending(list, EntityFieldDef.class, "listDescription");
                    filterStringFieldDefList = DataUtils.unmodifiableList(list);
                }
            }
        }

        return filterStringFieldDefList;
    }

    public List<EntityFieldDef> getAutoFormatFieldDefList() {
        if (autoFormatFieldDefList == null) {
            synchronized (this) {
                if (autoFormatFieldDefList == null) {
                    List<EntityFieldDef> list = new ArrayList<EntityFieldDef>();
                    for (EntityFieldDef entityFieldDef : fieldDefList) {
                        if (entityFieldDef.isWithAutoFormat()) {
                            list.add(entityFieldDef);
                        }
                    }

                    autoFormatFieldDefList = DataUtils.unmodifiableList(list);
                }
            }
        }

        return autoFormatFieldDefList;
    }

    public boolean isWithAutoFormatFields() {
        return !getAutoFormatFieldDefList().isEmpty();
    }
    
    public List<EntityFieldDef> getSuggestionFieldDefList() {
        if (suggestionFieldDefList == null) {
            synchronized (this) {
                if (suggestionFieldDefList == null) {
                    List<EntityFieldDef> list = new ArrayList<EntityFieldDef>();
                    for (EntityFieldDef entityFieldDef : fieldDefList) {
                        if (entityFieldDef.isWithSuggestionType()) {
                            list.add(entityFieldDef);
                        }
                    }

                    suggestionFieldDefList = DataUtils.unmodifiableList(list);
                }
            }
        }

        return suggestionFieldDefList;
    }

    private static final Set<String> RESERVED_SETVALUE_FIELDS = Collections
            .unmodifiableSet(new HashSet<String>(Arrays.asList("inWorkflow")));

    public List<EntityFieldDef> getSetValueFieldDefList() throws UnifyException {
        if (setValueFieldDefList == null) {
            synchronized (this) {
                if (setValueFieldDefList == null) {
                    List<EntityFieldDef> list = new ArrayList<EntityFieldDef>();
                    for (EntityFieldDef entityFieldDef : fieldDefList) {
                        if (entityFieldDef.isWithInputWidget() && entityFieldDef.isSupportSetValue()
                                && !RESERVED_SETVALUE_FIELDS.contains(entityFieldDef.getFieldName())) {
                            list.add(entityFieldDef);
                        }
                    }

                    DataUtils.sortAscending(list, EntityFieldDef.class, "listDescription");
                    setValueFieldDefList = DataUtils.unmodifiableList(list);
                }
            }
        }

        return setValueFieldDefList;
    }

    public List<EntityFieldDef> getDescriptiveFieldDefList() {
        return descriptiveFieldDefList;
    }

    public List<EntityFieldDef> getFkFieldDefList() {
        if (fkFieldDefList == null) {
            fkFieldDefList = new ArrayList<EntityFieldDef>();
            for (EntityFieldDef entityFieldDef : fieldDefList) {
                if (entityFieldDef.isForeignKey()) {
                    fkFieldDefList.add(entityFieldDef);
                }
            }

            fkFieldDefList = DataUtils.unmodifiableList(fkFieldDefList);
        }

        return fkFieldDefList;
    }

    public List<EntityFieldDef> getListOnlyFieldDefList() {
        if (listOnlyFieldDefList == null) {
            listOnlyFieldDefList = new ArrayList<EntityFieldDef>();
            for (EntityFieldDef entityFieldDef : fieldDefList) {
                if (entityFieldDef.isListOnly()) {
                    listOnlyFieldDefList.add(entityFieldDef);
                }
            }

            listOnlyFieldDefList = DataUtils.unmodifiableList(listOnlyFieldDefList);
        }

        return listOnlyFieldDefList;
    }

    public List<EntityFieldDef> getChildListFieldDefList() {
        if (childListFieldDefList == null) {
            childListFieldDefList = new ArrayList<EntityFieldDef>();
            for (EntityFieldDef entityFieldDef : fieldDefList) {
                if (entityFieldDef.isChildList()) {
                    childListFieldDefList.add(entityFieldDef);
                }
            }

            childListFieldDefList = DataUtils.unmodifiableList(childListFieldDefList);
        }

        return childListFieldDefList;
    }

    public List<EntityFieldDef> getStringFieldDefList() {
        if (stringFieldDefList == null) {
            stringFieldDefList = new ArrayList<EntityFieldDef>();
            for (EntityFieldDef entityFieldDef : fieldDefList) {
                if (entityFieldDef.isString()) {
                    stringFieldDefList.add(entityFieldDef);
                }
            }

            stringFieldDefList = DataUtils.unmodifiableList(stringFieldDefList);
        }

        return stringFieldDefList;
    }

    public List<EntityFieldDef> getBasicSearchFieldDefList() {
        if (basicSearchFieldDefList == null) {
            basicSearchFieldDefList = new ArrayList<EntityFieldDef>();
            for (EntityFieldDef entityFieldDef : fieldDefList) {
                if (entityFieldDef.isBasicSearch()) {
                    basicSearchFieldDefList.add(entityFieldDef);
                }
            }

            basicSearchFieldDefList = DataUtils.unmodifiableList(basicSearchFieldDefList);
        }

        return basicSearchFieldDefList;
    }

    public boolean isWithDescriptiveFieldList() {
        return !descriptiveFieldDefList.isEmpty();
    }

    public Map<String, EntityFieldDef> getFieldDefMap() {
        return fieldDefMap;
    }

    public EntityFieldDef getRefEntityFieldDef(String entity) {
        return refFieldDefMap.get(entity);
    }

    public Map<String, String> getFieldLabelMap() {
        return fieldLabelMap;
    }

    public boolean isWithFieldDef(String fieldName) {
        return fieldDefMap.containsKey(fieldName);
    }

    public List<EntityAttachmentDef> getAttachmentList() {
        return attachmentList;
    }

    public List<UniqueConstraintDef> getUniqueConstraintList() {
        return uniqueConstraintList;
    }

    public List<IndexDef> getIndexList() {
        return indexList;
    }

    public EntityUploadDef getUploadDef(String name) {
        return uploadMap.get(name);
    }

    public EntityFieldDef getFosterParentIdDef() {
        return fosterParentIdDef;
    }

    public EntityFieldDef getFosterParentTypeDef() {
        return fosterParentTypeDef;
    }

    public EntityFieldDef getCategoryColumnDef() {
        return categoryColumnDef;
    }

    public String getOriginClassName() {
        return originClassName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getLabel() {
        return label;
    }

    public boolean isAuditable() {
        return auditable;
    }

    public boolean isReportable() {
        return reportable;
    }

    public boolean delegated() {
        return !StringUtils.isBlank(delegate);
    }

    public boolean isWithFosterParent() {
        return fosterParentIdDef != null && fosterParentTypeDef != null;
    }

    public boolean isWithCategoryColumn() {
        return categoryColumnDef != null;
    }

    public boolean isWithAttachments() {
        return !attachmentList.isEmpty();
    }

    public boolean isWithExpressions() {
        return !expressionDefMap.isEmpty();
    }

    public boolean isWithUniqueConstraints() {
        return !uniqueConstraintList.isEmpty();
    }

    public String getAddPrivilege() {
        return addPrivilege;
    }

    public String getEditPrivilege() {
        return editPrivilege;
    }

    public String getDeletePrivilege() {
        return deletePrivilege;
    }

    public String getReportPrivilege() {
        return reportPrivilege;
    }

    public String getAttachPrivilege() {
        return attachPrivilege;
    }

    public boolean isSingleFieldUniqueConstraint(String fieldName) {
        for (UniqueConstraintDef uniqueConstraintDef : uniqueConstraintList) {
            if (uniqueConstraintDef.isSingleFieldConstraint(fieldName)) {
                return true;
            }
        }

        return false;
    }

    public EntityFieldDef getFieldDef(String fieldName) {
        EntityFieldDef entityFieldDef = fieldDefMap.get(fieldName);
        if (entityFieldDef == null) {
            throw new RuntimeException(
                    "Field with name [" + fieldName + "] is unknown for entity definition [" + getLongName() + "].");
        }

        return entityFieldDef;
    }

    public Map<String, Object> extractValues(Entity inst) throws UnifyException {
        Map<String, Object> map = new HashMap<String, Object>();
        for (EntityFieldDef entityFieldDef : fieldDefList) {
            Object val = ReflectUtils.getBeanProperty(inst, entityFieldDef.getFieldName());
            map.put(entityFieldDef.getFieldName(), val);
        }

        return map;
    }

    public Map<String, Object> extractValues(ValueStore valueStore) throws UnifyException {
        Map<String, Object> map = new HashMap<String, Object>();
        for (EntityFieldDef entityFieldDef : fieldDefList) {
            Object val = valueStore.retrieve(entityFieldDef.getFieldName());
            map.put(entityFieldDef.getFieldName(), val);
        }

        return map;
    }

    public String getBlobFieldName() {
        if (blobFieldName == null) {
            for (EntityFieldDef entityFieldDef : fieldDefList) {
                if (entityFieldDef.isBlob()) {
                    blobFieldName = entityFieldDef.getFieldName();
                    break;
                }
            }
        }

        return blobFieldName;
    }

    public String getBlobDescFieldName() {
        List<EntityFieldDef> list = getStringFieldDefList();
        if (!list.isEmpty()) {
            return list.get(0).getFieldName();
        }

        return null;
    }

    public List<Listable> getExpressionsListables() {
        if (expressionListableList == null) {
            synchronized (this) {
                if (expressionListableList == null) {
                    if (!expressionDefMap.isEmpty()) {
                        expressionListableList = new ArrayList<Listable>();
                        for (EntityExpressionDef entityExpressionDef : expressionDefMap.values()) {
                            String fullGenName = GeneratorNameUtils.getGeneratorFullName(
                                    ApplicationModuleNameConstants.ENTITYEXPRESSION_SETVALUE_GENERATOR,
                                    entityExpressionDef.getName());
                            expressionListableList.add(new ListData(fullGenName, entityExpressionDef.getDescription()));
                        }

                        expressionListableList = Collections.unmodifiableList(expressionListableList);
                    } else {
                        expressionListableList = Collections.emptyList();
                    }
                }
            }
        }

        return expressionListableList;
    }

    public EntityExpressionDef getExpressionDef(String expressionName) {
        EntityExpressionDef entityExpressionDef = expressionDefMap.get(expressionName);
        if (entityExpressionDef == null) {
            throw new RuntimeException("Expression with name [" + expressionName
                    + "] is unknown for entity definition [" + getLongName() + "].");
        }

        return entityExpressionDef;
    }

    public static Builder newBuilder(ConfigType type, String originClassName, String label, String delegate,
            boolean auditable, boolean reportable, String longName, String description, Long id, long version) {
        return new Builder(null, type, originClassName, null, label, delegate, auditable, reportable, longName,
                description, id, version);
    }

    public static Builder newBuilder(EntityBaseType baseType, ConfigType type, String originClassName, String tableName,
            String label, String delegate, boolean auditable, boolean reportable, String longName, String description,
            Long id, long version) {
        return new Builder(baseType, type, originClassName, tableName, label, delegate, auditable, reportable, longName,
                description, id, version);
    }

    public static class Builder {

        private EntityBaseType baseType;

        private ConfigType type;

        private Map<String, EntityFieldDef> fieldDefMap;

        private Map<String, EntityAttachmentDef> attachmentDefMap;

        private Map<String, EntityExpressionDef> expressionDefMap;

        private List<UniqueConstraintDef> uniqueConstraintList;

        private List<IndexDef> indexList;

        private List<EntityUploadDef> uploadList;

        private String originClassName;

        private String tableName;

        private String label;

        private String delegate;

        private boolean auditable;

        private boolean reportable;

        private String longName;

        private String description;

        private Long id;

        private long version;

        public Builder(EntityBaseType baseType, ConfigType type, String originClassName, String tableName, String label,
                String delegate, boolean auditable, boolean reportable, String longName, String description, Long id,
                long version) {
            this.baseType = baseType;
            this.type = type;
            this.fieldDefMap = new LinkedHashMap<String, EntityFieldDef>();
            this.originClassName = originClassName;
            this.tableName = tableName;
            this.label = label;
            this.delegate = delegate;
            this.auditable = auditable;
            this.reportable = reportable;
            this.longName = longName;
            this.description = description;
            this.id = id;
            this.version = version;
        }

        public Builder addFieldDef(WidgetTypeDef textWidgetTypeDef, WidgetTypeDef inputWidgetTypeDef,
                EntityFieldDataType dataType, EntityFieldType type, String fieldName, String fieldLabel)
                throws UnifyException {
            return addFieldDef(textWidgetTypeDef, inputWidgetTypeDef, null, dataType, type, null, fieldName, fieldLabel,
                    null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                    false, false, false, false, false, false);
        }

        public Builder addFieldDef(WidgetTypeDef textWidgetTypeDef, WidgetTypeDef inputWidgetTypeDef,
                WidgetTypeDef lingualWidgetTypeDef, EntityFieldDataType dataType, EntityFieldType type,
                TextCase textCase, String fieldName, String fieldLabel, String columnName, String category,String suggestionType,
                String inputLabel, String inputListKey, String lingualListKey, String autoFormat, String defaultVal,
                String references, String key, String property, Integer rows, Integer columns, Integer minLen,
                Integer maxLen, Integer precision, Integer scale, boolean nullable, boolean auditable,
                boolean reportable, boolean maintainLink, boolean basicSearch, boolean descriptive)
                throws UnifyException {
            return addFieldDef(textWidgetTypeDef, inputWidgetTypeDef, lingualWidgetTypeDef, null, dataType, type,
                    textCase, fieldName, fieldLabel, columnName, category, suggestionType, inputLabel, inputListKey, lingualListKey,
                    autoFormat, defaultVal, references, key, property, rows, columns, minLen, maxLen, precision, scale,
                    nullable, auditable, reportable, maintainLink, basicSearch, descriptive);
        }

        public Builder addFieldDef(WidgetTypeDef textWidgetTypeDef, WidgetTypeDef inputWidgetTypeDef,
                WidgetTypeDef lingualWidgetTypeDef, RefDef refDef, EntityFieldDataType dataType, EntityFieldType type,
                TextCase textCase, String fieldName, String fieldLabel, String columnName, String category, String suggestionType,
                String inputLabel, String inputListKey, String lingualListKey, String autoFormat, String defaultVal,
                String references, String key, String property, Integer rows, Integer columns, Integer minLen,
                Integer maxLen, Integer precision, Integer scale, boolean nullable, boolean auditable,
                boolean reportable, boolean maintainLink, boolean basicSearch, boolean descriptive)
                throws UnifyException {
            if (fieldDefMap.containsKey(fieldName)) {
                throw new RuntimeException("Field with name [" + fieldName + "] already exists in this definition.");
            }

            fieldDefMap.put(fieldName,
                    new EntityFieldDef(textWidgetTypeDef, inputWidgetTypeDef, lingualWidgetTypeDef, refDef, dataType,
                            type, textCase, longName, fieldName, fieldLabel, columnName, references, category, suggestionType,
                            inputLabel, inputListKey, lingualListKey, autoFormat, defaultVal, key, property,
                            DataUtils.convert(int.class, rows), DataUtils.convert(int.class, columns),
                            DataUtils.convert(int.class, minLen), DataUtils.convert(int.class, maxLen),
                            DataUtils.convert(int.class, precision), DataUtils.convert(int.class, scale), nullable,
                            auditable, reportable, maintainLink, basicSearch, descriptive));
            return this;
        }

        public Builder addAttachmentDef(FileAttachmentType type, String name, String description) {
            if (attachmentDefMap == null) {
                attachmentDefMap = new LinkedHashMap<String, EntityAttachmentDef>();
            }

            if (attachmentDefMap.containsKey(name)) {
                throw new RuntimeException("Attachment with name [" + name + "] already exists in this definition.");
            }

            attachmentDefMap.put(name, new EntityAttachmentDef(type, name, description));
            return this;
        }

        public Builder addExpressionDef(String name, String description, String expression) {
            if (expressionDefMap == null) {
                expressionDefMap = new LinkedHashMap<String, EntityExpressionDef>();
            }

            if (expressionDefMap.containsKey(name)) {
                throw new RuntimeException("Expression with name [" + name + "] already exists in this definition.");
            }

            expressionDefMap.put(name, new EntityExpressionDef(name, description, expression));
            return this;
        }

        public Builder addUniqueConstraintDef(String name, String description, List<String> fieldList,
                boolean caseInsensitive) {
            if (uniqueConstraintList == null) {
                uniqueConstraintList = new ArrayList<UniqueConstraintDef>();
            }

            uniqueConstraintList.add(new UniqueConstraintDef(name, description, fieldList, caseInsensitive));
            return this;
        }

        public Builder addIndexDef(String name, String description, List<String> fieldList) {
            if (indexList == null) {
                indexList = new ArrayList<IndexDef>();
            }

            indexList.add(new IndexDef(name, description, fieldList));
            return this;
        }

        public Builder addUploadDef(String name, String description, FieldSequenceDef fieldSequenceDef,
                ConstraintAction constraintAction) {
            if (uploadList == null) {
                uploadList = new ArrayList<EntityUploadDef>();
            }

            uploadList.add(new EntityUploadDef(name, description, fieldSequenceDef, constraintAction));
            return this;
        }

        public EntityDef build() throws UnifyException {
            ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
            List<EntityFieldDef> fieldDefList = new ArrayList<EntityFieldDef>(fieldDefMap.values());
            DataUtils.sortDescending(fieldDefList, EntityFieldDef.class, "sortIndex");
            return new EntityDef(baseType, type, DataUtils.unmodifiableMap(fieldDefMap),
                    DataUtils.unmodifiableList(fieldDefList), DataUtils.unmodifiableValuesList(attachmentDefMap),
                    DataUtils.unmodifiableMap(expressionDefMap), DataUtils.unmodifiableList(uniqueConstraintList),
                    DataUtils.unmodifiableList(indexList), DataUtils.unmodifiableList(uploadList), nameParts,
                    originClassName, tableName, label, delegate, auditable, reportable, description, id, version);
        }
    }

}

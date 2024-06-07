/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.common.data.EntityFieldAttributes;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.TextCase;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Entity field definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityFieldDef implements Listable, EntityFieldAttributes {

    private WidgetTypeDef textWidgetTypeDef;

    private WidgetTypeDef inputWidgetTypeDef;

    private WidgetTypeDef ligualWidgetTypeDef;

    private EntityFieldDef resolvedTypeFieldDef;

    private RefDef refDef;

    private EntityFieldDataType dataType;

    private EntityFieldType type;

    private TextCase textCase;

    private String entityLongName;

    private String fieldName;

    private String fieldLabel;

    private String columnName;

    private String references;

    private String key;

    private String property;

    private String category;

    private String suggestionType;

    private String inputLabel;

    private String inputListKey;

    private String lingualListKey;

    private String autoFormat;

    private String defaultVal;

    private String mapped;

    private int rows;

    private int columns;

    private int minLen;

    private int maxLen;

    private int precision;

    private int scale;

    private boolean trim;

    private boolean allowNegative;

    private boolean editable;

    private boolean nullable;

    private boolean auditable;

    private boolean reportable;

    private boolean descriptive;

    private boolean maintainLink;

    private boolean basicSearch;

    public EntityFieldDef(WidgetTypeDef textWidgetTypeDef, WidgetTypeDef inputWidgetTypeDef, RefDef refDef,
            String entityLongName, String fieldName, String mapped, String references, String inputListKey) {
        this.textWidgetTypeDef = textWidgetTypeDef;
        this.inputWidgetTypeDef = inputWidgetTypeDef;
        this.refDef = refDef;
        this.entityLongName = entityLongName;
        this.fieldName = fieldName;
        this.mapped = mapped;
        this.references = references;
        this.inputListKey = inputListKey;
    }

    public EntityFieldDef(WidgetTypeDef textWidgetTypeDef, WidgetTypeDef inputWidgetTypeDef,
            WidgetTypeDef ligualWidgetTypeDef, RefDef refDef, EntityFieldDataType dataType, EntityFieldType type,
            TextCase textCase, String entityLongName, String fieldName, String mapped, String fieldLabel,
            String columnName, String references, String category, String suggestionType, String inputLabel,
            String inputListKey, String lingualListKey, String autoFormat, String defaultVal, String key,
            String property, int rows, int columns, int minLen, int maxLen, int precision, int scale,
            boolean trim, boolean allowNegative, boolean editable, boolean nullable, boolean auditable, boolean reportable,
            boolean maintainLink, boolean basicSearch, boolean descriptive) {
        this.textWidgetTypeDef = textWidgetTypeDef;
        this.inputWidgetTypeDef = inputWidgetTypeDef;
        this.ligualWidgetTypeDef = ligualWidgetTypeDef;
        this.refDef = refDef;
        this.dataType = dataType;
        this.type = type;
        this.textCase = textCase;
        this.entityLongName = entityLongName;
        this.fieldName = fieldName;
        this.mapped = mapped;
        this.fieldLabel = fieldLabel;
        this.columnName = columnName;
        this.references = references;
        this.category = category;
        this.suggestionType = suggestionType;
        this.inputLabel = inputLabel;
        this.inputListKey = inputListKey;
        this.lingualListKey = lingualListKey;
        this.autoFormat = autoFormat;
        this.defaultVal = defaultVal;
        this.key = key;
        this.property = property;
        this.rows = rows;
        this.columns = columns;
        this.minLen = minLen;
        this.maxLen = maxLen > 0 ? maxLen : 0;
        this.precision = precision;
        this.scale = scale;
        this.trim = trim;
        this.allowNegative = allowNegative;
        this.editable = editable;
        this.nullable = nullable;
        this.auditable = auditable;
        this.reportable = reportable;
        this.maintainLink = maintainLink;
        this.basicSearch = basicSearch;
        this.descriptive = descriptive;
    }

    @Override
    public String getListDescription() {
        return inputLabel != null ? inputLabel : fieldLabel;
    }

    @Override
    public String getListKey() {
        return fieldName;
    }

    public WidgetTypeDef getTextWidgetTypeDef() {
        return textWidgetTypeDef;
    }

    public WidgetTypeDef getInputWidgetTypeDef() {
        return inputWidgetTypeDef != null ? inputWidgetTypeDef
                : (resolvedTypeFieldDef != null ? resolvedTypeFieldDef.getInputWidgetTypeDef() : null);
    }

    public WidgetTypeDef getLigualWidgetTypeDef() {
        return ligualWidgetTypeDef != null ? ligualWidgetTypeDef : getInputWidgetTypeDef();
    }

    public RefDef getRefDef() {
        return refDef;
    }

    public EntityFieldDataType getDataType() {
        return dataType;
    }

    public EntityFieldType getType() {
        return type;
    }

    public TextCase getTextCase() {
        return textCase;
    }

    public int getSortIndex() {
        return type.index();
    }

    public String getEntityLongName() {
        return entityLongName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public String getColumnName() {
        return columnName;
    }

    public boolean isWithColumnName() {
        return !StringUtils.isBlank(columnName);
    }

    public boolean isWithInputLabel() {
        return !StringUtils.isBlank(inputLabel);
    }

    @Override
    public String getReferences() {
        return references;
    }

    public String getCategory() {
        return category;
    }

    public String getSuggestionType() {
        return suggestionType;
    }

    public boolean isWithSuggestionType() {
        return inputWidgetTypeDef != null
                && "application.suggestiontextsearch".equals(inputWidgetTypeDef.getLongName());
    }

    public EntityFieldDef getResolvedTypeFieldDef() {
        return resolvedTypeFieldDef;
    }

    public void setResolvedTypeFieldDef(EntityFieldDef resolvedTypeFieldDef) {
        if (this.resolvedTypeFieldDef == null && EntityFieldDataType.LIST_ONLY.equals(dataType)) {
            this.resolvedTypeFieldDef = resolvedTypeFieldDef;
        }
    }

    public EntityFieldDataType getResolvedDataType() {
        return isWithResolvedTypeFieldDef() ? resolvedTypeFieldDef.getDataType() : dataType;
    }

    public boolean isWithResolvedTypeFieldDef() {
        return resolvedTypeFieldDef != null;
    }

    public boolean isWithCategory() {
        return !StringUtils.isBlank(category);
    }

    public String getInputLabel() {
        return inputLabel;
    }

    public String getInputListKey() {
        return inputListKey;
    }

    public String getLingualListKey() {
        return lingualListKey;
    }

    public String getAutoFormat() {
        return autoFormat;
    }

    public boolean isWithAutoFormat() {
        return !StringUtils.isBlank(autoFormat);
    }

    public boolean isStringAutoFormat() {
        return EntityFieldDataType.STRING.equals(dataType) || !StringUtils.isBlank(autoFormat);
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public boolean isWithDefaultVal() {
        return !StringUtils.isBlank(defaultVal);
    }

    public String getMapping() {
        return mapped;
    }

    public boolean isWithMapping() {
        return !StringUtils.isBlank(mapped);
    }

    public boolean isWithUnresolvedInputWidget() {
        return inputWidgetTypeDef != null;
    }

    public boolean isWithInputWidget() {
        return inputWidgetTypeDef != null || (resolvedTypeFieldDef != null && resolvedTypeFieldDef.isWithInputWidget());
    }

    public String getFieldLongName() {
        return StringUtils.dotify(entityLongName, fieldName);
    }

    public String getKey() {
        return key;
    }

    public String getProperty() {
        return property;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    @Override
    public int getMinLen() {
        return minLen;
    }

    @Override
    public int getMaxLen() {
        return maxLen;
    }

    @Override
    public int getPrecision() {
        return precision;
    }

    @Override
    public int getScale() {
        return scale;
    }

    @Override
    public boolean isTrim() {
        return trim;
    }

    @Override
    public boolean isAllowNegative() {
        return allowNegative;
    }

    public boolean isLenLimits() {
        return minLen > 0 || maxLen > 0;
    }

    public boolean isPrimaryKey() {
        return "id".equals(fieldName);
    }

    public boolean isEditable() {
        return editable;
    }

    public boolean isNullable() {
        return nullable;
    }

    public boolean isAuditable() {
        return auditable;
    }

    public boolean isReportable() {
        return reportable;
    }

    public boolean isDescriptive() {
        return descriptive;
    }

    public boolean isMaintainLink() {
        return maintainLink;
    }

    public boolean isBasicSearch() {
        return basicSearch;
    }

    public boolean isTableViewable() {
        return dataType.isTableViewable();
    }

    public boolean isFormViewable() {
        return dataType.isFormViewable();
    }

    public boolean isSupportFilter() {
        return dataType.isSupportFilter();
    }

    public boolean isSupportSetValue() {
        return dataType.isSupportSetValue() && !isListOnly();
    }

    public boolean isPrimitive() {
        return dataType.isPrimitive();
    }

    public boolean isScratch() {
        return dataType.isScratch();
    }

    public boolean isDate() {
        return dataType.isDate();
    }

    public boolean isTimestamp() {
        return dataType.isTimestamp();
    }

    public boolean isTime() {
        return dataType.isDate() || dataType.isTimestamp();
    }

    public boolean isBoolean() {
        return dataType.isBoolean();
    }

    public boolean isDecimal() {
        return dataType.isDecimal();
    }

    public boolean isNumber() {
        return dataType.isNumber();
    }

    public boolean isEnumDataType() {
        return dataType.isEnumDataType();
    }

    public boolean isEnumDynamic() {
        return dataType.isEnumDynamic();
    }

    public boolean isEnumGroup() {
        return dataType.isEnumGroup();
    }

    public boolean isRefDataType() {
        return dataType.isRefDataType();
    }

    public boolean isRefFileUpload() {
        return dataType.isRefFileUpload();
    }

    public boolean isString() {
        return dataType.isString();
    }

    public boolean isTenantId() {
        return dataType.isTenantId();
    }

    public boolean isBase() {
        return type.isBase();
    }

    public boolean isStatic() {
        return type.isStatic();
    }

    public boolean isCustom() {
        return type.isCustom();
    }

    public boolean isEntityRef() {
        return refDef != null;
    }

    public boolean isForeignKey() {
        return dataType.isForeignKey();
    }

    public boolean isChildRef() {
        return dataType.isChild() || dataType.isChildList();
    }

    public boolean isChild() {
        return dataType.isChild();
    }

    public boolean isChildList() {
        return dataType.isChildList();
    }

    public boolean isListOnly() {
        return dataType != null && dataType.isListOnly();
    }

    public boolean isBlob() {
        return EntityFieldDataType.BLOB.equals(dataType);
    }

    @Override
    public String toString() {
        return StringUtils.toXmlString(this);
    }

    public static EntityFieldDef createForString(AppletUtilities au, String fieldName, String fieldLabel,
            Integer minLen, Integer maxLen, boolean nullable) throws UnifyException {
        return EntityFieldDef.newBuilderForString(au, fieldName, fieldLabel).minLen(minLen).maxLen(maxLen)
                .nullable(nullable).build();
    }

    public static EntityFieldDef createForAmount(AppletUtilities au, String fieldName, String fieldLabel,
            Integer precision, Integer scale, boolean nullable) throws UnifyException {
        return EntityFieldDef.newBuilderForAmount(au, fieldName, fieldLabel).precision(precision).scale(scale)
                .nullable(nullable).build();
    }

    public static EntityFieldDef createForDate(AppletUtilities au, String fieldName, String fieldLabel,
            boolean nullable) throws UnifyException {
        return EntityFieldDef.newBuilderForDate(au, fieldName, fieldLabel).nullable(nullable).build();
    }

    public static EntityFieldDef createForDateTime(AppletUtilities au, String fieldName, String fieldLabel,
            boolean nullable) throws UnifyException {
        return EntityFieldDef.newBuilderForDateTime(au, fieldName, fieldLabel).nullable(nullable).build();
    }

    public static EntityFieldDef createForDateTimeUTC(AppletUtilities au, String fieldName, String fieldLabel,
            boolean nullable) throws UnifyException {
        return EntityFieldDef.newBuilderForDateTimeUTC(au, fieldName, fieldLabel).nullable(nullable).build();
    }

    public static EntityFieldDef createForEnum(AppletUtilities au, String fieldName, String fieldLabel, String list,
            boolean nullable) throws UnifyException {
        return EntityFieldDef.newBuilderForEnum(au, fieldName, fieldLabel, list).nullable(nullable).build();
    }

    public static EntityFieldDef createForEnumRef(AppletUtilities au, String fieldName, String fieldLabel, String list,
            boolean nullable) throws UnifyException {
        return EntityFieldDef.newBuilderForEnumRef(au, fieldName, fieldLabel, list).nullable(nullable).build();
    }

    public static EntityFieldDef createForEnumDynamic(AppletUtilities au, String fieldName, String fieldLabel, String list,
            boolean nullable) throws UnifyException {
        return EntityFieldDef.newBuilderForEnumDynamic(au, fieldName, fieldLabel, list).nullable(nullable).build();
    }

    public static Builder newBuilderForString(AppletUtilities au, String fieldName, String fieldLabel)
            throws UnifyException {
        return EntityFieldDef.newBuilder(au, EntityFieldDataType.STRING, EntityFieldType.STATIC, fieldName, fieldLabel)
                .textWidget("application.text").inputWidget("application.text");
    }

    public static Builder newBuilderForAmount(AppletUtilities au, String fieldName, String fieldLabel)
            throws UnifyException {
        return EntityFieldDef.newBuilder(au, EntityFieldDataType.DECIMAL, EntityFieldType.STATIC, fieldName, fieldLabel)
                .textWidget("application.text").inputWidget("application.amount");
    }

    public static Builder newBuilderForDate(AppletUtilities au, String fieldName, String fieldLabel)
            throws UnifyException {
        return EntityFieldDef.newBuilder(au, EntityFieldDataType.DATE, EntityFieldType.STATIC, fieldName, fieldLabel)
                .textWidget("application.text").inputWidget("application.date");
    }

    public static Builder newBuilderForDateTime(AppletUtilities au, String fieldName, String fieldLabel)
            throws UnifyException {
        return EntityFieldDef
                .newBuilder(au, EntityFieldDataType.TIMESTAMP, EntityFieldType.STATIC, fieldName, fieldLabel)
                .textWidget("application.text").inputWidget("application.datetime");
    }

    public static Builder newBuilderForDateTimeUTC(AppletUtilities au, String fieldName, String fieldLabel)
            throws UnifyException {
        return EntityFieldDef
                .newBuilder(au, EntityFieldDataType.TIMESTAMP_UTC, EntityFieldType.STATIC, fieldName, fieldLabel)
                .textWidget("application.text").inputWidget("application.datetime");
    }

    public static Builder newBuilderForEnum(AppletUtilities au, String fieldName, String fieldLabel, String list)
            throws UnifyException {
        return EntityFieldDef.newBuilder(au, EntityFieldDataType.ENUM, EntityFieldType.STATIC, fieldName, fieldLabel)
                .inputWidget("application.enumlist").references(list);
    }

    public static Builder newBuilderForEnumRef(AppletUtilities au, String fieldName, String fieldLabel, String list)
            throws UnifyException {
        return EntityFieldDef
                .newBuilder(au, EntityFieldDataType.ENUM_REF, EntityFieldType.STATIC, fieldName, fieldLabel)
                .inputWidget("application.enumlist").references(list);
    }

    public static Builder newBuilderForEnumDynamic(AppletUtilities au, String fieldName, String fieldLabel, String list)
            throws UnifyException {
        return EntityFieldDef.newBuilder(au, EntityFieldDataType.ENUM_DYN, EntityFieldType.STATIC, fieldName, fieldLabel)
                .inputWidget("application.enumlist").references(list);
    }

    public static Builder newBuilder(AppletUtilities au, EntityFieldDataType dataType, EntityFieldType type,
            String fieldName, String fieldLabel) {
        return new Builder(au, dataType, type, fieldName, fieldLabel);
    }

    public static class Builder {

        private AppletUtilities au;

        private WidgetTypeDef textWidgetTypeDef;

        private WidgetTypeDef inputWidgetTypeDef;

        private WidgetTypeDef ligualWidgetTypeDef;

        private RefDef refDef;

        private EntityFieldDataType dataType;

        private EntityFieldType type;

        private TextCase textCase;

        private String entityLongName;

        private String fieldName;

        private String mapped;

        private String fieldLabel;

        private String columnName;

        private String references;

        private String key;

        private String property;

        private String category;

        private String suggestionType;

        private String inputLabel;

        private String inputListKey;

        private String lingualListKey;

        private String autoFormat;

        private String defaultVal;

        private int rows;

        private int columns;

        private int minLen;

        private int maxLen;

        private int precision;

        private int scale;

        private boolean trim;

        private boolean allowNegative;

        private boolean editable;

        private boolean nullable;

        private boolean auditable;

        private boolean reportable;

        private boolean descriptive;

        private boolean maintainLink;

        private boolean basicSearch;

        public Builder(AppletUtilities au, EntityFieldDataType dataType, EntityFieldType type, String fieldName,
                String fieldLabel) {
            this.au = au;
            this.dataType = dataType;
            this.type = type;
            this.fieldName = fieldName;
            this.fieldLabel = fieldLabel;
        }

        public Builder textWidget(String widgetName) throws UnifyException {
            this.textWidgetTypeDef = au.getWidgetTypeDef(widgetName);
            return this;
        }

        public Builder inputWidget(String widgetName) throws UnifyException {
            this.inputWidgetTypeDef = au.getWidgetTypeDef(widgetName);
            return this;
        }

        public Builder lingualWidget(String widgetName) throws UnifyException {
            this.ligualWidgetTypeDef = au.getWidgetTypeDef(widgetName);
            return this;
        }

        public Builder refDef(String refName) throws UnifyException {
            this.refDef = au.getRefDef(refName);
            return this;
        }

        public Builder textCase(TextCase textCase) throws UnifyException {
            this.textCase = textCase;
            return this;
        }

        public Builder entityLongName(String entityLongName) throws UnifyException {
            this.entityLongName = entityLongName;
            return this;
        }

        public Builder columnName(String columnName) throws UnifyException {
            this.columnName = columnName;
            return this;
        }

        public Builder mapped(String mapped) throws UnifyException {
            this.mapped = mapped;
            return this;
        }

        public Builder references(String references) throws UnifyException {
            this.references = references;
            return this;
        }

        public Builder key(String key) throws UnifyException {
            this.key = key;
            return this;
        }

        public Builder property(String property) throws UnifyException {
            this.property = property;
            return this;
        }

        public Builder category(String category) throws UnifyException {
            this.category = category;
            return this;
        }

        public Builder suggestionType(String suggestionType) throws UnifyException {
            this.suggestionType = suggestionType;
            return this;
        }

        public Builder inputLabel(String inputLabel) throws UnifyException {
            this.inputLabel = inputLabel;
            return this;
        }

        public Builder inputListKey(String inputListKey) throws UnifyException {
            this.inputListKey = inputListKey;
            return this;
        }

        public Builder lingualListKey(String lingualListKey) throws UnifyException {
            this.lingualListKey = lingualListKey;
            return this;
        }

        public Builder autoFormat(String autoFormat) throws UnifyException {
            this.autoFormat = autoFormat;
            return this;
        }

        public Builder defaultVal(String defaultVal) throws UnifyException {
            this.defaultVal = defaultVal;
            return this;
        }

        public Builder rows(Integer rows) throws UnifyException {
            this.rows = DataUtils.convert(int.class, rows);
            return this;
        }

        public Builder columns(Integer columns) throws UnifyException {
            this.columns = DataUtils.convert(int.class, columns);
            return this;
        }

        public Builder minLen(Integer minLen) throws UnifyException {
            this.minLen = DataUtils.convert(int.class, minLen);
            return this;
        }

        public Builder maxLen(Integer maxLen) throws UnifyException {
            this.maxLen = DataUtils.convert(int.class, maxLen);
            return this;
        }

        public Builder precision(Integer precision) throws UnifyException {
            this.precision = DataUtils.convert(int.class, precision);
            return this;
        }

        public Builder scale(Integer scale) throws UnifyException {
            this.scale = DataUtils.convert(int.class, scale);
            return this;
        }

        public Builder trim(boolean trim) throws UnifyException {
            this.trim = trim;
            return this;
        }

        public Builder allowNegative(boolean allowNegative) throws UnifyException {
            this.allowNegative = allowNegative;
            return this;
        }

        public Builder editable(boolean editable) throws UnifyException {
            this.editable = editable;
            return this;
        }

        public Builder nullable(boolean nullable) throws UnifyException {
            this.nullable = nullable;
            return this;
        }

        public Builder auditable(boolean auditable) throws UnifyException {
            this.auditable = auditable;
            return this;
        }

        public Builder reportable(boolean reportable) throws UnifyException {
            this.reportable = reportable;
            return this;
        }

        public Builder descriptive(boolean descriptive) throws UnifyException {
            this.descriptive = descriptive;
            return this;
        }

        public Builder maintainLink(boolean maintainLink) throws UnifyException {
            this.maintainLink = maintainLink;
            return this;
        }

        public Builder basicSearch(boolean basicSearch) throws UnifyException {
            this.basicSearch = basicSearch;
            return this;
        }

        public EntityFieldDef build() throws UnifyException {
            if (inputWidgetTypeDef == null) {
                throw new RuntimeException("Input widget is required.");
            }

            if (StringUtils.isBlank(fieldName)) {
                throw new RuntimeException("Field name is blank.");
            }

            if (StringUtils.isBlank(fieldLabel)) {
                throw new RuntimeException("Field label is blank.");
            }

            return new EntityFieldDef(textWidgetTypeDef, inputWidgetTypeDef, ligualWidgetTypeDef, refDef, dataType,
                    type, textCase, entityLongName, fieldName, mapped, fieldLabel, columnName, references, category,
                    suggestionType, inputLabel, inputListKey, lingualListKey, autoFormat, defaultVal, key, property,
                    rows, columns, minLen, maxLen, precision, scale, trim, allowNegative, editable, nullable, auditable,
                    reportable, maintainLink, basicSearch, descriptive);
        }
    }

}

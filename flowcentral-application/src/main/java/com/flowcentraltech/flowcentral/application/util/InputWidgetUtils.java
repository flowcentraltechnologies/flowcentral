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
package com.flowcentraltech.flowcentral.application.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldAttributes;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.FieldSequenceDef;
import com.flowcentraltech.flowcentral.application.data.FieldSequenceEntryDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.FilterRestrictionDef;
import com.flowcentraltech.flowcentral.application.data.SetValueDef;
import com.flowcentraltech.flowcentral.application.data.SetValuesDef;
import com.flowcentraltech.flowcentral.application.data.WidgetTypeDef;
import com.flowcentraltech.flowcentral.application.entities.AppAppletFilter;
import com.flowcentraltech.flowcentral.application.entities.AppFieldSequence;
import com.flowcentraltech.flowcentral.application.entities.AppFilter;
import com.flowcentraltech.flowcentral.application.entities.AppSetValues;
import com.flowcentraltech.flowcentral.application.entities.AppTableFilter;
import com.flowcentraltech.flowcentral.common.business.SpecialParamProvider;
import com.flowcentraltech.flowcentral.common.data.DateRange;
import com.flowcentraltech.flowcentral.common.input.AbstractInput;
import com.flowcentraltech.flowcentral.common.input.StringInput;
import com.flowcentraltech.flowcentral.common.util.CommonInputUtils;
import com.flowcentraltech.flowcentral.common.util.LingualDateUtils;
import com.flowcentraltech.flowcentral.configuration.constants.ChildListActionType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.LingualDateType;
import com.flowcentraltech.flowcentral.configuration.constants.LingualStringType;
import com.flowcentraltech.flowcentral.configuration.constants.SetValueType;
import com.flowcentraltech.flowcentral.configuration.constants.WidgetColor;
import com.flowcentraltech.flowcentral.configuration.xml.FieldSequenceConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FieldSequenceEntryConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FilterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FilterRestrictionConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SetValueConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SetValuesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.TableFilterConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UnifyOperationException;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.constant.TextCase;
import com.tcdng.unify.core.criterion.CompoundRestriction;
import com.tcdng.unify.core.criterion.CriteriaBuilder;
import com.tcdng.unify.core.criterion.DoubleParamRestriction;
import com.tcdng.unify.core.criterion.FilterConditionListType;
import com.tcdng.unify.core.criterion.FilterConditionType;
import com.tcdng.unify.core.criterion.MultipleParamRestriction;
import com.tcdng.unify.core.criterion.Order;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.criterion.SimpleRestriction;
import com.tcdng.unify.core.criterion.SingleParamRestriction;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.FilterUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Input widget utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class InputWidgetUtils {

    private static final Set<String> ENUMERATION_WIDGETS = Collections.unmodifiableSet(new HashSet<String>(
            Arrays.asList("application.enumlist", "application.enumlistlabel", "application.enumreadonlytext")));

    private static final Class<?>[] NEW_INPUT_PARAMS = new Class<?>[] { String.class };

    private static final Map<EntityFieldDataType, String> defaultFormInputWidgets;

    static {
        Map<EntityFieldDataType, String> map = new EnumMap<EntityFieldDataType, String>(EntityFieldDataType.class);
        map.put(EntityFieldDataType.CHAR, "application.text");
        map.put(EntityFieldDataType.BOOLEAN, "application.booleanlist");
        map.put(EntityFieldDataType.SHORT, "application.integer");
        map.put(EntityFieldDataType.INTEGER, "application.integer");
        map.put(EntityFieldDataType.LONG, "application.integer");
        map.put(EntityFieldDataType.FLOAT, "application.decimal");
        map.put(EntityFieldDataType.DOUBLE, "application.decimal");
        map.put(EntityFieldDataType.DECIMAL, "application.decimal");
        map.put(EntityFieldDataType.DATE, "application.date");
        map.put(EntityFieldDataType.TIMESTAMP_UTC, "application.datetime");
        map.put(EntityFieldDataType.TIMESTAMP, "application.datetime");
        map.put(EntityFieldDataType.STRING, "application.text");
        map.put(EntityFieldDataType.ENUM, "application.enumlist");
        map.put(EntityFieldDataType.ENUM_REF, "application.enumlist");
        map.put(EntityFieldDataType.REF, "application.entitylist");
        map.put(EntityFieldDataType.REF_UNLINKABLE, "application.entitylist");
        map.put(EntityFieldDataType.REF_FILEUPLOAD, "application.fileupload");
        defaultFormInputWidgets = Collections.unmodifiableMap(map);
    }

    private InputWidgetUtils() {

    }

    public static boolean isEnumerationWidget(String widgetName) {
        return ENUMERATION_WIDGETS.contains(widgetName);
    }

    public static String getDefaultEntityFieldWidget(EntityFieldDataType type) {
        return defaultFormInputWidgets.get(type);
    }

    public static AbstractInput<?> newMultiInput(final EntityFieldDef entityFieldDef) throws UnifyException {
        final EntityFieldDef _entityFieldDef = entityFieldDef.isWithResolvedTypeFieldDef()
                ? entityFieldDef.getResolvedTypeFieldDef()
                : entityFieldDef;
        if (entityFieldDef.isEnumDataType()) {
            String editor = String.format("!ui-dropdownchecklist list:%s columns:3 formatter:$d{!pipearrayformat}",
                    _entityFieldDef.getReferences());
            return (AbstractInput<?>) ReflectUtils.newInstance(StringInput.class, NEW_INPUT_PARAMS, editor);
        }

        return null;
    }

    public static AbstractInput<?> newInput(final EntityFieldDef entityFieldDef, boolean lingual, boolean search)
            throws UnifyException {
        final EntityFieldDef _entityFieldDef = entityFieldDef.isWithResolvedTypeFieldDef()
                ? entityFieldDef.getResolvedTypeFieldDef()
                : entityFieldDef;
        WidgetTypeDef widgetTypeDef = lingual ? _entityFieldDef.getLigualWidgetTypeDef()
                : _entityFieldDef.getInputWidgetTypeDef();
        if (widgetTypeDef == null || (search && "application.textarea".equals(widgetTypeDef.getLongName()))) {
            widgetTypeDef = _entityFieldDef.getTextWidgetTypeDef();
        }

        Class<? extends AbstractInput<?>> inputClass = lingual ? StringInput.class
                : CommonInputUtils.getInputClass(widgetTypeDef.getInputType());
        String editor = InputWidgetUtils.constructEditor(widgetTypeDef, _entityFieldDef);
        return (AbstractInput<?>) ReflectUtils.newInstance(inputClass, NEW_INPUT_PARAMS, editor);
    }

    public static String constructEditor(WidgetTypeDef widgetTypeDef, EntityFieldDef entityFieldDef) {
        String editor = InputWidgetUtils.constructEditor(widgetTypeDef, entityFieldDef, null, false);
        if (widgetTypeDef.isStretch()) {
            StringBuilder esb = new StringBuilder(editor);
            esb.append(" style:$s{width:100%;}");
            return esb.toString();
        }

        return editor;
    }

    public static String constructEditorWithBinding(WidgetTypeDef widgetTypeDef, EntityFieldDef entityFieldDef) {
        return InputWidgetUtils.constructEditorWithBinding(widgetTypeDef, entityFieldDef, null, null);
    }

    public static String constructEditorWithBinding(WidgetTypeDef widgetTypeDef, EntityFieldDef entityFieldDef,
            String reference, WidgetColor color) {
        String editor = InputWidgetUtils.constructEditor(widgetTypeDef, entityFieldDef, reference, false);
        StringBuilder esb = new StringBuilder(editor);
        esb.append(" binding:").append(entityFieldDef.getFieldName());
        boolean isWithStyling = widgetTypeDef.isStretch() || color != null;
        if (isWithStyling) {
            esb.append(" style:$s{");
            if (widgetTypeDef.isStretch()) {
                esb.append("width:100%;");
            }

            if (color != null) {
                esb.append("background-color:").append(color.hex()).append(";");
            }

            esb.append("}");
        }

        return esb.toString();
    }

    public static String constructRenderer(WidgetTypeDef widgetTypeDef, EntityFieldDef entityFieldDef) {
        return InputWidgetUtils.constructEditor(widgetTypeDef, entityFieldDef, null, true);
    }

    private static String constructEditor(WidgetTypeDef widgetTypeDef, EntityFieldDef entityFieldDef, String reference,
            boolean renderer) {
        final EntityFieldAttributes efa = entityFieldDef.isWithResolvedTypeFieldDef()
                ? entityFieldDef.getResolvedTypeFieldDef()
                : entityFieldDef;
        String editor = renderer ? widgetTypeDef.getRenderer() : widgetTypeDef.getEditor();
        switch (widgetTypeDef.getLongName()) {
            case "application.textarea":
            case "application.textareamedium":
            case "application.textarealarge":
                // Deprecated? entityFieldDef.getRows()
                editor = String.format(editor, efa.getMinLen(), efa.getMaxLen());
                break;
            case "application.password":
                editor = String.format(editor, efa.getMinLen(), efa.getMaxLen());
                break;
            case "application.mobile":
            case "application.integertext":
                editor = String.format(editor, efa.getMinLen(), efa.getMaxLen());
                break;
            case "application.suggestiontextsearch":
                String type = !StringUtils.isBlank(efa.getSuggestionType()) ? efa.getSuggestionType() : "";
                editor = String.format(editor, type);
                break;
            case "application.text":
            case "application.name":
            case "application.alphanumeric":
            case "application.alphanumericwithspace":
            case "application.alphanumericwithspecial":
            case "application.wildname":
            case "application.email":
            case "application.emailset":
            case "application.mobileset":
            case "application.website":
            case "application.domain": {
                String textCase = entityFieldDef.getTextCase() != null
                        ? entityFieldDef.getTextCase().toString().toLowerCase()
                        : "";
                editor = String.format(editor, efa.getMinLen(), efa.getMaxLen(), textCase);
            }
                break;
            case "application.textwithupper":
            case "application.namewithupper":
            case "application.alphanumericwithupper":
            case "application.alphanumericwithspaceupper":
            case "application.alphanumericwithspecialupper": {
                editor = String.format(editor, efa.getMinLen(), efa.getMaxLen(),
                        TextCase.UPPER.toString().toLowerCase());
            }
                break;
            case "application.alphanumericwithspecialcamel": {
                editor = String.format(editor, efa.getMinLen(), efa.getMaxLen(),
                        TextCase.CAMEL.toString().toLowerCase());
            }
                break;
            case "application.series": {
                editor = String.format(editor, efa.getMinLen(), efa.getMaxLen(), TextCase.UPPER.toString());
            }
                break;
            case "application.fullname":
            case "application.fullnamewithspecial": {
                String textCase = entityFieldDef.getTextCase() != null
                        ? entityFieldDef.getTextCase().toString().toLowerCase()
                        : TextCase.CAMEL.toString().toLowerCase();
                editor = String.format(editor, efa.getMinLen(), efa.getMaxLen(), textCase);
            }
                break;
            case "application.javafieldname":
                editor = String.format(editor, efa.getMinLen(), efa.getMaxLen());
                break;
            case "application.integer":
            case "application.integerformatless":
                editor = String.format(editor, efa.getPrecision());
                break;
            case "application.amountwhole":
                int precision = efa.getScale() > 0 ? efa.getPrecision() - efa.getScale() : efa.getPrecision();
                editor = String.format(editor, precision);
                break;
            case "application.amount":
            case "application.decimal":
            case "application.postingentry":
            case "application.postingentryneg":
            case "application.postingdebitonly":
            case "application.postingdebitonlyneg":
            case "application.postingcreditonly":
            case "application.postingcreditonlyneg":
                editor = String.format(editor, efa.getPrecision(), efa.getScale());
                break;
            case "application.enumlist":
            case "application.enumreadonlytext":
            case "application.enumlistlabel":
                editor = String.format(editor, entityFieldDef.getReferences());
                break;
            case "application.entitylist":
            case "application.entitysearch":
            case "application.entityselect":
            case "application.caseentitysearch":
                if (StringUtils.isBlank(reference)) {
                    reference = entityFieldDef.isEntityRef() ? entityFieldDef.getRefDef().getLongName()
                            : entityFieldDef.getReferences();
                }

                editor = String.format(editor, reference,
                        StringUtils.toNonNullString(entityFieldDef.getInputListKey(), ""));
                break;
            case "application.workappentitylist":
            case "application.workappentitysearch":
                editor = String.format(editor, StringUtils.toNonNullString(entityFieldDef.getInputListKey(), ""));
                break;
            case "application.fileupload":
                editor = String.format(editor, entityFieldDef.getRefDef().getLongName(),
                        entityFieldDef.getEntityLongName(), entityFieldDef.getFieldName());
                break;
            default:
                break;
        }

        return editor;
    }

    public static String getFilterConditionTypeSelectDescriptior(EntityFieldDef entityFieldDef,
            FilterConditionListType listType) throws UnifyException {
        EntityFieldDef _entityFieldDef = entityFieldDef.isWithResolvedTypeFieldDef()
                ? entityFieldDef.getResolvedTypeFieldDef()
                : entityFieldDef;
        EntityFieldDataType type = _entityFieldDef.getDataType();
        if (type.isPrimitive()) {
            String listCommand = null;
            if (_entityFieldDef.isPrimaryKey()) {
                listCommand = "pkconditionlist";
            } else if (_entityFieldDef.isEntityRef()) {
                if (type.isEnumDataType()) {
                    listCommand = "enumconstconditionlist";
                } else {
                    listCommand = "refconditionlist";
                }
            } else {
                listCommand = FilterUtils.getFilterConditionTypeListCommand(
                        _entityFieldDef.getDataType().dataType().javaClass(), listType);
            }

            return String.format("!ui-select list:%s extStyleClass:$s{tcread} blankOption:$m{blank.none}", listCommand);
        }

        return null;
    }

    public static boolean isSupportedFilterConditionType(EntityFieldDef entityFieldDef, FilterConditionType type,
            FilterConditionListType listType) throws UnifyException {
        EntityFieldDef _entityFieldDef = entityFieldDef.isWithResolvedTypeFieldDef()
                ? entityFieldDef.getResolvedTypeFieldDef()
                : entityFieldDef;
        return _entityFieldDef.getDataType().isPrimitive() && FilterUtils.isFilterConditionSupportedForType(
                _entityFieldDef.getDataType().dataType().javaClass(), type, listType);
    }

    public static SetValuesConfig getSetValuesConfig(String valueGenerator, AppSetValues appSetValues)
            throws UnifyException {
        SetValuesDef setValuesDef = InputWidgetUtils.getSetValuesDef(null, null, valueGenerator, appSetValues);
        if (setValuesDef != null) {
            SetValuesConfig setValuesConfig = new SetValuesConfig();
            List<SetValueConfig> setValueConfigList = new ArrayList<SetValueConfig>();
            for (SetValueDef setValueDef : setValuesDef.getSetValueList()) {
                SetValueConfig setValueConfig = new SetValueConfig();
                setValueConfig.setType(setValueDef.getType());
                setValueConfig.setFieldName(setValueDef.getFieldName());
                setValueConfig.setValue(setValueDef.getParam());
                setValueConfigList.add(setValueConfig);
            }

            setValuesConfig.setSetValueList(setValueConfigList);
            return setValuesConfig;
        }

        return null;
    }

    public static SetValuesDef getSetValuesDef(String valueGenerator, AppSetValues appSetValues) throws UnifyException {
        return InputWidgetUtils.getSetValuesDef(null, null, valueGenerator, appSetValues);
    }

    public static SetValuesDef getSetValuesDef(String name, String description, String valueGenerator,
            AppSetValues appSetValues) throws UnifyException {
        if (!StringUtils.isBlank(valueGenerator) || appSetValues != null) {
            SetValuesDef.Builder svdb = SetValuesDef.newBuilder();
            svdb.name(name).description(description).valueGenerator(valueGenerator);
            if (appSetValues != null) {
                try (BufferedReader reader = new BufferedReader(new StringReader(appSetValues.getDefinition()))) {
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        String[] p = line.split("]");
                        SetValueType type = p.length > 1 ? SetValueType.fromCode(p[1]) : null;
                        String fieldName = p[0];
                        String param = p.length > 2 ? p[2] : null;
                        svdb.addSetValueDef(type, fieldName, param);
                    }
                } catch (IOException e) {
                    throw new UnifyOperationException(e);
                }
            }

            return svdb.build();
        }

        return null;
    }

    public static AppSetValues newAppSetValues(SetValuesConfig setValuesConfig) throws UnifyException {
        if (setValuesConfig != null) {
            return new AppSetValues(InputWidgetUtils.getSetValuesDefinition(setValuesConfig));
        }

        return null;
    }

    public static String getSetValuesDefinition(SetValuesConfig setValuesConfig) throws UnifyException {
        String result = null;
        try (StringWriter sw = new StringWriter(); BufferedWriter bw = new BufferedWriter(sw)) {
            if (!DataUtils.isBlank(setValuesConfig.getSetValueList())) {
                for (SetValueConfig setValueConfig : setValuesConfig.getSetValueList()) {
                    bw.write(setValueConfig.getFieldName());
                    bw.write(']');
                    if (setValueConfig.getType() != null) {
                        bw.write(setValueConfig.getType().code());
                        bw.write(']');

                        if (setValueConfig.getValue() != null) {
                            bw.write(setValueConfig.getValue());
                            bw.write(']');
                        }
                    }

                    bw.newLine();
                }
            }

            bw.flush();
            result = sw.toString();
        } catch (IOException e) {
            throw new UnifyOperationException(e);
        }

        return result;
    }

    public static String getSetValuesDefinition(SetValuesDef setValuesDef) throws UnifyException {
        String result = null;
        try (StringWriter sw = new StringWriter(); BufferedWriter bw = new BufferedWriter(sw)) {
            if (!DataUtils.isBlank(setValuesDef.getSetValueList())) {
                for (SetValueDef setValueDef : setValuesDef.getSetValueList()) {
                    bw.write(setValueDef.getFieldName());
                    bw.write(']');
                    if (setValueDef.getType() != null) {
                        bw.write(setValueDef.getType().code());
                        bw.write(']');

                        if (setValueDef.getParam() != null) {
                            bw.write(setValueDef.getParam());
                            bw.write(']');
                        }
                    }

                    bw.newLine();
                }
            }

            bw.flush();
            result = sw.toString();
        } catch (IOException e) {
            throw new UnifyOperationException(e);
        }

        return result;
    }

    public static FieldSequenceDef getFieldSequenceDef(AppFieldSequence appFieldSequence) throws UnifyException {
        return InputWidgetUtils.getFieldSequenceDef(null, null, appFieldSequence);
    }

    public static FieldSequenceDef getFieldSequenceDef(String name, String description,
            AppFieldSequence appFieldSequence) throws UnifyException {
        if (appFieldSequence != null) {
            FieldSequenceDef.Builder svdb = FieldSequenceDef.newBuilder();
            svdb.name(name).description(description);
            try (BufferedReader reader = new BufferedReader(new StringReader(appFieldSequence.getDefinition()))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] p = line.split("]");
                    String fieldName = p[0];
                    String formatter = p.length > 1 ? p[1] : null;
                    svdb.addFieldSequenceEntryDef(fieldName, formatter);
                }
            } catch (IOException e) {
                throw new UnifyOperationException(e);
            }

            return svdb.build();
        }

        return null;
    }

    public static FieldSequenceConfig getFieldSequenceConfig(AppFieldSequence appFieldSequence) throws UnifyException {
        FieldSequenceDef fieldSequenceDef = InputWidgetUtils.getFieldSequenceDef(appFieldSequence);
        return InputWidgetUtils.getFieldSequenceConfig(fieldSequenceDef);
    }

    public static FieldSequenceConfig getFieldSequenceConfig(FieldSequenceDef fieldSequenceDef) throws UnifyException {
        List<FieldSequenceEntryConfig> entryList = new ArrayList<FieldSequenceEntryConfig>();
        for (FieldSequenceEntryDef fieldSequenceEntryDef : fieldSequenceDef.getFieldSequenceList()) {
            entryList.add(new FieldSequenceEntryConfig(fieldSequenceEntryDef.getFieldName(),
                    fieldSequenceEntryDef.getFormatter()));
        }

        return new FieldSequenceConfig(entryList);
    }

    public static AppFieldSequence newAppFieldSequence(FieldSequenceConfig fieldSequenceConfig) throws UnifyException {
        if (fieldSequenceConfig != null) {
            return new AppFieldSequence(InputWidgetUtils.getFieldSequenceDefinition(fieldSequenceConfig));
        }

        return null;
    }

    public static String getFieldSequenceDefinition(FieldSequenceConfig fieldSequenceConfig) throws UnifyException {
        String result = null;
        try (StringWriter sw = new StringWriter(); BufferedWriter bw = new BufferedWriter(sw)) {
            for (FieldSequenceEntryConfig entry : fieldSequenceConfig.getEntryList()) {
                bw.write(entry.getFieldName());
                bw.write(']');
                if (entry.getFormatter() != null) {
                    bw.write(entry.getFormatter());
                    bw.write(']');
                }

                bw.newLine();
            }

            bw.flush();
            result = sw.toString();
        } catch (IOException e) {
            throw new UnifyOperationException(e);
        }

        return result;
    }

    public static String getFieldSequenceDefinition(FieldSequenceDef fieldSequenceDef) throws UnifyException {
        String result = null;
        try (StringWriter sw = new StringWriter(); BufferedWriter bw = new BufferedWriter(sw)) {
            for (FieldSequenceEntryDef fieldSequenceEntryDef : fieldSequenceDef.getFieldSequenceList()) {
                bw.write(fieldSequenceEntryDef.getFieldName());
                bw.write(']');
                if (fieldSequenceEntryDef.isWithFormatter()) {
                    bw.write(fieldSequenceEntryDef.getFormatter());
                    bw.write(']');
                }

                bw.newLine();
            }

            bw.flush();
            result = sw.toString();
        } catch (IOException e) {
            throw new UnifyOperationException(e);
        }

        return result;
    }

    public static String getOrderDefinition(Order order) throws UnifyException {
        if (order != null) {
            StringBuilder sb = new StringBuilder();
            for (Order.Part part : order.getParts()) {
                sb.append(part.getField()).append("]").append(part.getType().name()).append("\r\n");
            }

            return sb.toString();
        }

        return null;
    }

    public static Order getOrder(String definition) throws UnifyException {
        if (!StringUtils.isBlank(definition)) {
            Order order = new Order();
            try (BufferedReader reader = new BufferedReader(new StringReader(definition));) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] p = line.split("]");
                    String fieldName = p[0];
                    OrderType type = OrderType.fromName(p[1]);
                    order.add(fieldName, type);
                }
            } catch (IOException e) {
                throw new UnifyOperationException(e);
            }

            return order;
        }

        return null;
    }

    public static FilterConfig getFilterConfig(AppAppletFilter appAppletFilter) throws UnifyException {
        FilterConfig filterConfig = InputWidgetUtils.getFilterConfig(appAppletFilter.getName(),
                appAppletFilter.getDescription(), appAppletFilter.getPreferredForm(),
                appAppletFilter.getPreferredChildListApplet(), appAppletFilter.getChildListActionType(),
                appAppletFilter.getFilter());
        filterConfig.setPreferredForm(appAppletFilter.getPreferredForm());
        filterConfig.setPreferredChildListApplet(appAppletFilter.getPreferredChildListApplet());
        filterConfig.setQuickFilter(appAppletFilter.isQuickFilter());
        filterConfig.setChildListActionType(appAppletFilter.getChildListActionType());
        return filterConfig;
    }

    public static TableFilterConfig getFilterConfig(AppTableFilter appTableFilter) throws UnifyException {
        if (appTableFilter != null) {
            TableFilterConfig tableFilterConfig = new TableFilterConfig();
            InputWidgetUtils.getFilterConfig(tableFilterConfig, appTableFilter.getName(),
                    appTableFilter.getDescription(), null, null, null, appTableFilter.getFilter());
            tableFilterConfig.setRowColor(appTableFilter.getRowColor());
            return tableFilterConfig;
        }

        return null;
    }

    public static FilterConfig getFilterConfig(AppFilter appFilter) throws UnifyException {
        return InputWidgetUtils.getFilterConfig(null, null, null, null, null, appFilter);
    }

    public static FilterConfig getFilterConfig(String name, String description, String preferredForm,
            String preferredChildListApplet, ChildListActionType childListActionType, AppFilter appFilter)
            throws UnifyException {
        if (appFilter != null) {
            FilterConfig filterConfig = new FilterConfig();
            getFilterConfig(filterConfig, name, description, preferredForm, preferredChildListApplet,
                    childListActionType, appFilter);
            return filterConfig;
        }

        return null;
    }

    public static void getFilterConfig(FilterConfig filterConfig, String name, String description, String preferredForm,
            String preferredChildListApplet, ChildListActionType childListActionType, AppFilter appFilter)
            throws UnifyException {
        if (appFilter != null) {
            FilterDef filterDef = InputWidgetUtils.getFilterDef(name, description, preferredForm,
                    preferredChildListApplet, childListActionType, appFilter);
            filterConfig.setName(name);
            filterConfig.setDescription(description);
            List<FilterRestrictionConfig> restrictionList = new ArrayList<FilterRestrictionConfig>();
            FilterRestrictionConfig restrConfig = null;
            List<FilterRestrictionDef> defList = filterDef.getFilterRestrictionDefList();
            final int len = defList.size();
            for (int i = 0; i < len;) {
                FilterRestrictionDef nextRestrDef = defList.get(i);
                if (nextRestrDef.getDepth() > 0) {
                    i = setSubRestrictions(restrConfig, defList, 0, i);
                } else {
                    restrConfig = new FilterRestrictionConfig();
                    restrConfig.setType(nextRestrDef.getType());
                    restrConfig.setField(nextRestrDef.getFieldName());
                    restrConfig.setParamA(nextRestrDef.getParamA());
                    restrConfig.setParamB(nextRestrDef.getParamB());
                    restrictionList.add(restrConfig);
                    i++;
                }

            }

            filterConfig.setRestrictionList(restrictionList);
        }
    }

    private static int setSubRestrictions(FilterRestrictionConfig restrConfig, List<FilterRestrictionDef> defList,
            int depth, int index) {
        List<FilterRestrictionConfig> restrictionList = new ArrayList<FilterRestrictionConfig>();
        final int len = defList.size();
        depth++;
        FilterRestrictionConfig nextRestrConfig = null;
        while (index < len) {
            FilterRestrictionDef nextRestrDef = defList.get(index);
            if (nextRestrDef.getDepth() == depth) {
                nextRestrConfig = new FilterRestrictionConfig();
                nextRestrConfig.setType(nextRestrDef.getType());
                nextRestrConfig.setField(nextRestrDef.getFieldName());
                nextRestrConfig.setParamA(nextRestrDef.getParamA());
                nextRestrConfig.setParamB(nextRestrDef.getParamB());
                restrictionList.add(nextRestrConfig);
                index++;
            } else if (nextRestrDef.getDepth() > depth) {
                index = setSubRestrictions(nextRestrConfig, defList, depth, index);
            } else {
                break;
            }
        }

        restrConfig.setRestrictionList(restrictionList);
        return index;
    }

    public static FilterDef getFilterDef(AppFilter appFilter) throws UnifyException {
        return InputWidgetUtils.getFilterDef(null, null, null, null, null, appFilter);
    }

    public static FilterDef getFilterDef(String name, String description, String preferredForm,
            String preferredChildListApplet, ChildListActionType childListActionType, AppFilter appFilter)
            throws UnifyException {
        if (appFilter != null) {
            FilterDef.Builder fdb = FilterDef.newBuilder();
            fdb.name(name).description(description).preferredForm(preferredForm)
                    .preferredChildListApplet(preferredChildListApplet).childListActionType(childListActionType);
            addFilterDefinition(fdb, appFilter.getDefinition());
            return fdb.build();
        }

        return null;
    }

    public static FilterDef getFilterDef(String filterDefinition) throws UnifyException {
        if (filterDefinition != null) {
            FilterDef.Builder fdb = FilterDef.newBuilder();
            addFilterDefinition(fdb, filterDefinition);
            return fdb.build();
        }

        return null;
    }

    private static void addFilterDefinition(FilterDef.Builder fdb, String filterDefinition) throws UnifyException {
        if (filterDefinition != null) {
            try (BufferedReader reader = new BufferedReader(new StringReader(filterDefinition));) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] p = line.split("]");
                    FilterConditionType type = FilterConditionType.fromCode(p[0]);
                    String fieldName = p.length > 2 ? p[2] : null;
                    String paramA = p.length > 3 ? p[3] : null;
                    String paramB = p.length > 4 ? p[4] : null;
                    int depth = Integer.parseInt(p[1]);
                    fdb.addRestrictionDef(type, fieldName, paramA, paramB, depth);
                }
            } catch (IOException e) {
                throw new UnifyOperationException(e);
            }
        }
    }

    public static FilterDef getFilterDef(Restriction restriction) throws UnifyException {
        return InputWidgetUtils.getFilterDef(null, null, null, null, null, restriction);
    }

    public static FilterDef getFilterDef(String name, String description, String preferredForm,
            String preferredChildListApplet, ChildListActionType childListActionType, Restriction restriction)
            throws UnifyException {
        if (restriction != null) {
            FilterDef.Builder fdb = FilterDef.newBuilder();
            fdb.name(name).description(description).preferredForm(preferredForm)
                    .preferredChildListApplet(preferredChildListApplet).childListActionType(childListActionType);
            InputWidgetUtils.addRestriction(fdb, restriction, 0);
            return fdb.build();
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private static void addRestriction(FilterDef.Builder fdb, Restriction restriction, int depth)
            throws UnifyException {
        final FilterConditionType conditionType = restriction.getConditionType();
        if (conditionType.isCompound()) {
            CompoundRestriction compoundRestriction = (CompoundRestriction) restriction;
            fdb.addRestrictionDef(conditionType, null, null, null, depth);
            int subDepth = depth + 1;
            for (Restriction subRestriction : compoundRestriction.getRestrictionList()) {
                InputWidgetUtils.addRestriction(fdb, subRestriction, subDepth);
            }
        } else {
            String paramA = null;
            String paramB = null;
            if (conditionType.isSingleParam()) {
                paramA = DataUtils.convert(String.class, ((SingleParamRestriction) restriction).getParam());
            } else if (conditionType.isRange()) {
                DoubleParamRestriction doubleParamRestriction = (DoubleParamRestriction) restriction;
                paramA = DataUtils.convert(String.class, doubleParamRestriction.getFirstParam());
                paramB = DataUtils.convert(String.class, doubleParamRestriction.getSecondParam());
            } else if (conditionType.isAmongst()) {
                MultipleParamRestriction multipleParamRestriction = (MultipleParamRestriction) restriction;
                paramA = CommonInputUtils.buildCollectionString(
                        DataUtils.convert(List.class, String.class, multipleParamRestriction.getParams()));
            }
            fdb.addRestrictionDef(conditionType, ((SimpleRestriction) restriction).getFieldName(), paramA, paramB,
                    depth);
        }
    }

    public static String getFilterDefinition(Restriction restriction) throws UnifyException {
        return InputWidgetUtils.getFilterDefinition(InputWidgetUtils.getFilterDef(restriction));
    }

    public static String getFilterDefinition(FilterDef filterDef) throws UnifyException {
        String result = null;
        try (StringWriter sw = new StringWriter(); BufferedWriter bw = new BufferedWriter(sw);) {
            for (FilterRestrictionDef filterRestrictionDef : filterDef.getFilterRestrictionDefList()) {
                bw.write(filterRestrictionDef.getType().code());
                bw.write(']');
                bw.write(String.valueOf(filterRestrictionDef.getDepth()));
                bw.write(']');
                if (filterRestrictionDef.getFieldName() != null) {
                    bw.write(filterRestrictionDef.getFieldName());
                    bw.write(']');
                    if (filterRestrictionDef.getParamA() != null) {
                        bw.write(filterRestrictionDef.getParamA());
                        bw.write(']');
                        if (filterRestrictionDef.getParamB() != null) {
                            bw.write(filterRestrictionDef.getParamB());
                            bw.write(']');
                        }
                    }
                }
                bw.newLine();
            }

            bw.flush();
            result = sw.toString();
        } catch (IOException e) {
            throw new UnifyOperationException(e);
        }

        return result;
    }

    public static String getUpdateDefinition(Update update) throws UnifyException {
        String result = null;
        try (StringWriter sw = new StringWriter(); BufferedWriter bw = new BufferedWriter(sw)) {
            for (Map.Entry<String, Object> entry : update.entrySet()) {
                bw.write(entry.getKey());
                bw.write(']');
                if (entry.getValue() != null) {
                    bw.write(DataUtils.convert(String.class, entry.getValue()));
                    bw.write(']');
                }
                bw.newLine();
            }

            bw.flush();
            result = sw.toString();
        } catch (IOException e) {
            throw new UnifyOperationException(e);
        }

        return result;
    }

    public static AppFilter newAppFilter(FilterConfig filterConfig) throws UnifyException {
        if (filterConfig != null) {
            return new AppFilter(InputWidgetUtils.getFilterDefinition(filterConfig));
        }

        return null;
    }

    public static String getFilterDefinition(FilterConfig filterConfig) throws UnifyException {
        return InputWidgetUtils.writeFilterConfigRestrictions(filterConfig.getRestrictionList(), 0);
    }

    private static String writeFilterConfigRestrictions(List<FilterRestrictionConfig> restrictionConfigList, int depth)
            throws UnifyException {
        String result = null;
        try (StringWriter sw = new StringWriter(); BufferedWriter bw = new BufferedWriter(sw);) {
            for (FilterRestrictionConfig filterRestrictionConfig : restrictionConfigList) {
                bw.write(filterRestrictionConfig.getType().code());
                bw.write(']');
                bw.write(String.valueOf(depth));
                bw.write(']');
                if (filterRestrictionConfig.getField() != null) {
                    bw.write(filterRestrictionConfig.getField());
                    bw.write(']');
                    if (filterRestrictionConfig.getParamA() != null) {
                        bw.write(filterRestrictionConfig.getParamA());
                        bw.write(']');
                        if (filterRestrictionConfig.getParamB() != null) {
                            bw.write(filterRestrictionConfig.getParamB());
                            bw.write(']');
                        }
                    }
                }
                bw.newLine();

                if (!DataUtils.isBlank(filterRestrictionConfig.getRestrictionList())) {
                    String _result = InputWidgetUtils
                            .writeFilterConfigRestrictions(filterRestrictionConfig.getRestrictionList(), depth + 1);
                    bw.write(_result);
                }
            }

            bw.flush();
            result = sw.toString();
        } catch (IOException e) {
            throw new UnifyOperationException(e);
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public static Restriction getRestriction(EntityDef entityDef, FilterDef filterDef,
            SpecialParamProvider specialParamProvider, Date now) throws UnifyException {
        List<FilterRestrictionDef> conditionList = filterDef.getFilterRestrictionDefList();
        if (!conditionList.isEmpty()) {
            FilterRestrictionDef fo = conditionList.get(0);
            if (conditionList.size() == 1 && !fo.getType().isCompound()) {
                Object paramA = specialParamProvider.resolveSpecialParameter(fo.getParamA());
                Object paramB = specialParamProvider.resolveSpecialParameter(fo.getParamB());
                FilterConditionType type = fo.getType();
                if (!type.isFieldVal() && !type.isParameterVal()) {
                    EntityFieldDef entityFieldDef = entityDef.getFieldDef(fo.getFieldName());
                    if (type.isLingual() && entityFieldDef.isString()) {
                        ResolvedCondition resolved = InputWidgetUtils.resolveLingualStringCondition(entityFieldDef, now,
                                type, paramA, paramB);
                        type = resolved.getType();
                        paramA = resolved.getParamA();
                        paramB = resolved.getParamB();
                    } else if (entityFieldDef.isDate() || entityFieldDef.isTimestamp()) {
                        ResolvedCondition resolved = InputWidgetUtils.resolveDateCondition(entityFieldDef, now, type,
                                paramA, paramB);
                        type = resolved.getType();
                        paramA = resolved.getParamA();
                        paramB = resolved.getParamB();
                    } else {
                        Class<?> dataType = entityFieldDef.getDataType().dataType().javaClass();
                        if (paramA != null) {
                            if (type.isAmongst()) {
                                paramA = DataUtils.convert(List.class, dataType,
                                        Arrays.asList(CommonInputUtils.breakdownCollectionString((String) paramA)));
                            } else {
                                paramA = DataUtils.convert(dataType, paramA);
                            }
                        }

                        if (paramB != null) {
                            paramB = DataUtils.convert(dataType, paramB);
                        }
                    }
                }

                return type.createSimpleCriteria(fo.getFieldName(), paramA, paramB);
            }

            CriteriaBuilder cb = new CriteriaBuilder();
            addCompoundCriteria(cb, entityDef, filterDef, fo, 1, specialParamProvider, now);
            return cb.build();
        }

        return null;
    }

    public static ResolvedCondition resolveLingualStringCondition(EntityFieldDef entityFieldDef, Date now,
            FilterConditionType type, Object paramA, Object paramB) throws UnifyException {
        if (type.isLingual()) {
            LingualStringType lingualType = DataUtils.convert(LingualStringType.class, (String) paramA);
            if (lingualType != null) {
                switch (type) {
                    case EQUALS_LINGUAL:
                        paramA = lingualType.value();
                        type = FilterConditionType.EQUALS;
                        break;
                    case NOT_EQUALS_LINGUAL:
                        paramA = lingualType.value();
                        type = FilterConditionType.NOT_EQUALS;
                        break;
                    default:
                        break;
                }
            } else {
                type = null;
            }
        }

        return new ResolvedCondition(type, paramA, paramB);
    }

    public static ResolvedCondition resolveDateCondition(EntityFieldDef entityFieldDef, Date now,
            FilterConditionType type, Object paramA, Object paramB) throws UnifyException {
        if (type.isLingual()) {
            LingualDateType lingualType = DataUtils.convert(LingualDateType.class, (String) paramA);
            if (lingualType != null) {
                DateRange range = LingualDateUtils.getDateRangeFromNow(now, lingualType);
                switch (type) {
                    case EQUALS_LINGUAL:
                        paramA = range.getFrom();
                        paramB = range.getTo();
                        type = FilterConditionType.BETWEEN;
                        break;
                    case NOT_EQUALS_LINGUAL:
                        paramA = range.getFrom();
                        paramB = range.getTo();
                        type = FilterConditionType.NOT_BETWEEN;
                        break;
                    case GREATER_OR_EQUAL_LINGUAL:
                        paramA = CalendarUtils.getMidnightDate(range.getFrom());
                        type = FilterConditionType.GREATER_OR_EQUAL;
                        break;
                    case GREATER_THAN_LINGUAL:
                        paramA = CalendarUtils.getLastSecondDate(range.getFrom());
                        type = FilterConditionType.GREATER_THAN;
                        break;
                    case LESS_OR_EQUAL_LINGUAL:
                        paramA = CalendarUtils.getLastSecondDate(range.getFrom());
                        type = FilterConditionType.LESS_OR_EQUAL;
                        break;
                    case LESS_THAN_LINGUAL:
                        paramA = CalendarUtils.getMidnightDate(range.getFrom());
                        type = FilterConditionType.LESS_THAN;
                        break;
                    default:
                        break;
                }
            } else {
                type = null;
            }
        } else {
            if (entityFieldDef.isTimestamp()) {
                switch (type) {
                    case EQUALS:
                        paramA = CalendarUtils.getMidnightDate((Date) paramA);
                        paramB = CalendarUtils.getLastSecondDate((Date) paramA);
                        type = FilterConditionType.BETWEEN;
                        break;
                    case BETWEEN:
                        paramA = CalendarUtils.getMidnightDate((Date) paramA);
                        paramB = CalendarUtils.getLastSecondDate((Date) paramB);
                        break;
                    case GREATER_OR_EQUAL:
                        paramA = CalendarUtils.getMidnightDate((Date) paramA);
                        break;
                    case GREATER_THAN:
                        paramA = CalendarUtils.getLastSecondDate((Date) paramA);
                        break;
                    case LESS_OR_EQUAL:
                        paramA = CalendarUtils.getLastSecondDate((Date) paramA);
                        break;
                    case LESS_THAN:
                        paramA = CalendarUtils.getMidnightDate((Date) paramA);
                        break;
                    case NOT_EQUALS:
                        paramA = CalendarUtils.getMidnightDate((Date) paramA);
                        paramB = CalendarUtils.getLastSecondDate((Date) paramA);
                        type = FilterConditionType.NOT_BETWEEN;
                        break;
                    case NOT_BETWEEN:
                        paramA = CalendarUtils.getMidnightDate((Date) paramA);
                        paramB = CalendarUtils.getLastSecondDate((Date) paramB);
                        break;
                    default:
                        break;
                }
            }
        }

        return new ResolvedCondition(type, paramA, paramB);
    }

    private static int addCompoundCriteria(CriteriaBuilder cb, EntityDef entityDef, FilterDef filterDef,
            FilterRestrictionDef fo, int nIndex, SpecialParamProvider specialParamProvider, Date now)
            throws UnifyException {
        if (FilterConditionType.AND.equals(fo.getType())) {
            cb.beginAnd();
        } else {
            cb.beginOr();
        }

        List<FilterRestrictionDef> conditionList = filterDef.getFilterRestrictionDefList();
        int len = conditionList.size();
        int depth = fo.getDepth();
        int i = nIndex;
        for (; i < len; i++) {
            FilterRestrictionDef sfo = conditionList.get(i);
            if (sfo.getDepth() > depth) {
                if (sfo.getType().isCompound()) {
                    i = addCompoundCriteria(cb, entityDef, filterDef, sfo, i + 1, specialParamProvider, now) - 1;
                } else {
                    addSimpleCriteria(cb, entityDef, filterDef, sfo, specialParamProvider, now);
                }
            } else {
                break;
            }
        }

        cb.endCompound();
        return i;
    }

    @SuppressWarnings("unchecked")
    private static void addSimpleCriteria(CriteriaBuilder cb, EntityDef entityDef, FilterDef filterDef,
            FilterRestrictionDef fo, SpecialParamProvider specialParamProvider, Date now) throws UnifyException {
        FilterConditionType type = fo.getType();
        Object paramA = specialParamProvider.resolveSpecialParameter(fo.getParamA());
        Object paramB = specialParamProvider.resolveSpecialParameter(fo.getParamB());
        if (!type.isFieldVal() && !type.isParameterVal()) {
            EntityFieldDef _entityFieldDef = entityDef.getFieldDef(fo.getFieldName());
            if (_entityFieldDef.isWithResolvedTypeFieldDef()) {
                _entityFieldDef = _entityFieldDef.getResolvedTypeFieldDef();
            }

            if (type.isLingual() && _entityFieldDef.isString()) {
                ResolvedCondition resolved = InputWidgetUtils.resolveLingualStringCondition(_entityFieldDef, now, type,
                        paramA, paramB);
                type = resolved.getType();
                paramA = resolved.getParamA();
                paramB = resolved.getParamB();
            } else if (_entityFieldDef.isDate() || _entityFieldDef.isTimestamp()) {
                ResolvedCondition resolved = InputWidgetUtils.resolveDateCondition(_entityFieldDef, now, type, paramA,
                        paramB);
                type = resolved.getType();
                paramA = resolved.getParamA();
                paramB = resolved.getParamB();
            } else {
                EntityFieldDataType fieldDataType = _entityFieldDef.getDataType();
                Class<?> dataType = fieldDataType.dataType().javaClass();
                if (paramA != null) {
                    if (type.isAmongst()) {
                        paramA = DataUtils.convert(List.class, dataType,
                                Arrays.asList(CommonInputUtils.breakdownCollectionString((String) paramA)));
                    } else {
                        paramA = DataUtils.convert(dataType, paramA);
                    }
                }

                if (paramB != null) {
                    paramB = DataUtils.convert(dataType, paramB);
                }
            }
        }

        type.addSimpleCriteria(cb, fo.getFieldName(), paramA, paramB);
    }

}

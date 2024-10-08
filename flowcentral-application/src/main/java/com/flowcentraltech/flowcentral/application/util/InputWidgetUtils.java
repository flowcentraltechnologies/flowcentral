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

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.FieldSequenceDef;
import com.flowcentraltech.flowcentral.application.data.FieldSequenceEntryDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.FilterRestrictionDef;
import com.flowcentraltech.flowcentral.application.data.PropertySequenceDef;
import com.flowcentraltech.flowcentral.application.data.PropertySequenceEntryDef;
import com.flowcentraltech.flowcentral.application.data.SearchInputDef;
import com.flowcentraltech.flowcentral.application.data.SearchInputsDef;
import com.flowcentraltech.flowcentral.application.data.SetValueDef;
import com.flowcentraltech.flowcentral.application.data.SetValuesDef;
import com.flowcentraltech.flowcentral.application.data.TableLoadingDef;
import com.flowcentraltech.flowcentral.application.data.WidgetRuleEntryDef;
import com.flowcentraltech.flowcentral.application.data.WidgetRulesDef;
import com.flowcentraltech.flowcentral.application.data.WidgetTypeDef;
import com.flowcentraltech.flowcentral.application.entities.AppAppletFilter;
import com.flowcentraltech.flowcentral.application.entities.AppEntitySearchInput;
import com.flowcentraltech.flowcentral.application.entities.AppFieldSequence;
import com.flowcentraltech.flowcentral.application.entities.AppFilter;
import com.flowcentraltech.flowcentral.application.entities.AppFormFilter;
import com.flowcentraltech.flowcentral.application.entities.AppPropertySequence;
import com.flowcentraltech.flowcentral.application.entities.AppSearchInput;
import com.flowcentraltech.flowcentral.application.entities.AppSetValues;
import com.flowcentraltech.flowcentral.application.entities.AppTableFilter;
import com.flowcentraltech.flowcentral.application.entities.AppWidgetRules;
import com.flowcentraltech.flowcentral.common.business.SpecialParamProvider;
import com.flowcentraltech.flowcentral.common.constants.SessionParamType;
import com.flowcentraltech.flowcentral.common.data.DateRange;
import com.flowcentraltech.flowcentral.common.data.EntityFieldAttributes;
import com.flowcentraltech.flowcentral.common.input.AbstractInput;
import com.flowcentraltech.flowcentral.common.input.StringInput;
import com.flowcentraltech.flowcentral.common.util.CommonInputUtils;
import com.flowcentraltech.flowcentral.common.util.LingualDateUtils;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.LingualDateType;
import com.flowcentraltech.flowcentral.configuration.constants.LingualStringType;
import com.flowcentraltech.flowcentral.configuration.constants.SearchConditionType;
import com.flowcentraltech.flowcentral.configuration.constants.SetValueType;
import com.flowcentraltech.flowcentral.configuration.constants.WidgetColor;
import com.flowcentraltech.flowcentral.configuration.xml.AppletFilterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntitySearchInputConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FieldSequenceConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FieldSequenceEntryConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FilterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FilterRestrictionConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormFilterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.PropertySequenceConfig;
import com.flowcentraltech.flowcentral.configuration.xml.PropertySequenceEntryConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SearchInputConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SearchInputsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SetValueConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SetValuesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.TableFilterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WidgetRuleEntryConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WidgetRulesConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UnifyOperationException;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.constant.TextCase;
import com.tcdng.unify.core.criterion.And;
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
import com.tcdng.unify.web.ui.widget.data.ButtonInfo;

/**
 * Input widget utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class InputWidgetUtils {

    private static final Set<String> ENUMERATION_WIDGETS = Collections.unmodifiableSet(new HashSet<String>(
            Arrays.asList("application.enumlist", "application.enumlistlabel", "application.enumreadonlytext")));

    private static final Class<?>[] NEW_INPUT_PARAMS = new Class<?>[] { String.class, String.class };

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
        map.put(EntityFieldDataType.ENUM_DYN, "application.enumlist");
        map.put(EntityFieldDataType.REF, "application.entitylist");
        map.put(EntityFieldDataType.REF_UNLINKABLE, "application.entitylist");
        map.put(EntityFieldDataType.REF_FILEUPLOAD, "application.fileupload");
        defaultFormInputWidgets = Collections.unmodifiableMap(map);
    }

    private static final Map<EntityFieldDataType, String> defaultSyncFormInputWidgets;

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
        map.put(EntityFieldDataType.ENUM, "application.text");
        map.put(EntityFieldDataType.ENUM_REF, "application.text");
        map.put(EntityFieldDataType.ENUM_DYN, "application.text");
        map.put(EntityFieldDataType.REF, "application.entitylist");
        map.put(EntityFieldDataType.REF_UNLINKABLE, "application.entitylist");
        map.put(EntityFieldDataType.REF_FILEUPLOAD, "application.fileupload");
        defaultSyncFormInputWidgets = Collections.unmodifiableMap(map);
    }

    private InputWidgetUtils() {

    }

    public static List<ButtonInfo> getButtonInfos(List<TableLoadingDef> loadingDefList) {
        if (!DataUtils.isBlank(loadingDefList)) {
            Set<String> used = new HashSet<String>();
            List<ButtonInfo> _actionBtnInfos = new ArrayList<ButtonInfo>();
            for (TableLoadingDef tableLoadingDef : loadingDefList) {
                if (tableLoadingDef.isWithActionBtnInfos()) {
                    for (ButtonInfo buttonInfo : tableLoadingDef.getActionBtnInfos()) {
                        if (used.add(buttonInfo.getValue())) {
                            _actionBtnInfos.add(buttonInfo);
                        }
                    }
                }
            }

            return Collections.unmodifiableList(_actionBtnInfos);
        }

        return Collections.emptyList();
    }

    public static boolean isEnumerationWidget(String widgetName) {
        return ENUMERATION_WIDGETS.contains(widgetName);
    }

    public static String resolveEntityFieldWidget(final EntityFieldDef entityFieldDef) throws UnifyException {
        if (entityFieldDef.isWithInputWidget()) {
            return entityFieldDef.getInputWidget();
        } else {
            String widget = InputWidgetUtils.getDefaultEntityFieldWidget(entityFieldDef.getDataType());
            if (widget != null) {
                return widget;
            }
        }

        return "application.text";
    }

    public static String getDefaultEntityFieldWidget(EntityFieldDataType type) {
        return defaultFormInputWidgets.get(type);
    }

    public static String getDefaultSyncEntityFieldWidget(EntityFieldDataType type) {
        return defaultSyncFormInputWidgets.get(type);
    }

    public static AbstractInput<?> newMultiInput(final EntityFieldDef entityFieldDef) throws UnifyException {
        final EntityFieldDef _entityFieldDef = entityFieldDef.isWithResolvedTypeFieldDef()
                ? entityFieldDef.getResolvedTypeFieldDef()
                : entityFieldDef;
        if (_entityFieldDef.isEnumGroup()) {
            String editor = String.format("!ui-dropdownchecklist list:%s columns:3 formatter:$d{!pipearrayformat}",
                    _entityFieldDef.getReferences());
            return (AbstractInput<?>) ReflectUtils.newInstance(StringInput.class, NEW_INPUT_PARAMS, editor,
                    "!ui-label");
        }

        return null;
    }

    public static AbstractInput<?> newInput(final AppletUtilities au, final EntityFieldDef entityFieldDef, boolean lingual, boolean search)
            throws UnifyException {
        final EntityFieldDef _entityFieldDef = entityFieldDef.isWithResolvedTypeFieldDef()
                ? entityFieldDef.getResolvedTypeFieldDef()
                : entityFieldDef;
        String widget = lingual ? _entityFieldDef.getLigualWidget()
                : _entityFieldDef.getInputWidget();
        if (widget == null || (search && "application.textarea".equals(widget))) {
            widget = _entityFieldDef.getTextWidget();
        }

        final WidgetTypeDef widgetTypeDef = au.getWidgetTypeDef(widget);
        Class<? extends AbstractInput<?>> inputClass = lingual ? StringInput.class
                : CommonInputUtils.getInputClass(widgetTypeDef.getInputType());
        String editor = InputWidgetUtils.constructEditor(widgetTypeDef, _entityFieldDef);
        String renderer = InputWidgetUtils.constructRenderer(widgetTypeDef, _entityFieldDef);
        return (AbstractInput<?>) ReflectUtils.newInstance(inputClass, NEW_INPUT_PARAMS, editor, renderer);
    }

    public static AbstractInput<?> newInput(WidgetTypeDef widgetTypeDef, EntityFieldDef entityFieldDef)
            throws UnifyException {
        Class<? extends AbstractInput<?>> inputClass = CommonInputUtils.getInputClass(widgetTypeDef.getInputType());
        String editor = InputWidgetUtils.constructEditor(widgetTypeDef, entityFieldDef);
        String renderer = InputWidgetUtils.constructRenderer(widgetTypeDef, entityFieldDef);
        return (AbstractInput<?>) ReflectUtils.newInstance(inputClass, NEW_INPUT_PARAMS, editor, renderer);
    }

    public static AbstractInput<?> newInput(WidgetTypeDef widgetTypeDef, EntityFieldAttributes efa)
            throws UnifyException {
        Class<? extends AbstractInput<?>> inputClass = CommonInputUtils.getInputClass(widgetTypeDef.getInputType());
        String editor = InputWidgetUtils.constructEditor(widgetTypeDef, efa);
        String renderer = InputWidgetUtils.constructRenderer(widgetTypeDef, efa);
        return (AbstractInput<?>) ReflectUtils.newInstance(inputClass, NEW_INPUT_PARAMS, editor, renderer);
    }

    public static String constructEditor(WidgetTypeDef widgetTypeDef, EntityFieldAttributes efa) throws UnifyException {
        String editor = InputWidgetUtils.resolveEditor(widgetTypeDef.getEditor(), widgetTypeDef, efa, null, null);
        if (widgetTypeDef.isStretch()) {
            StringBuilder esb = new StringBuilder(editor);
            esb.append(" style:$s{width:100%;}");
            return esb.toString();
        }

        return editor;
    }

    public static String constructRenderer(WidgetTypeDef widgetTypeDef, EntityFieldAttributes efa)
            throws UnifyException {
        String renderer = InputWidgetUtils.resolveEditor(widgetTypeDef.getRenderer(), widgetTypeDef, efa, null, null);
        if (widgetTypeDef.isStretch()) {
            StringBuilder esb = new StringBuilder(renderer);
            esb.append(" style:$s{width:100%;}");
            return esb.toString();
        }

        return renderer;
    }

    public static String constructEditor(WidgetTypeDef widgetTypeDef, EntityFieldDef entityFieldDef)
            throws UnifyException {
        String editor = InputWidgetUtils.constructEditor(widgetTypeDef, entityFieldDef, null, false);
        if (widgetTypeDef.isStretch()) {
            StringBuilder esb = new StringBuilder(editor);
            esb.append(" style:$s{width:100%;");

            if ("application.entitysearch".equals(widgetTypeDef.getLongName())) {
                esb.append("background-color:").append(WidgetColor.WHITE.hex()).append(";");
            }

            esb.append("}");
            return esb.toString();
        }

        return editor;
    }

    public static String constructEditorWithBinding(WidgetTypeDef widgetTypeDef, EntityFieldDef entityFieldDef)
            throws UnifyException {
        return InputWidgetUtils.constructEditorWithBinding(widgetTypeDef, entityFieldDef, null, null);
    }

    public static String constructEditorWithBinding(WidgetTypeDef widgetTypeDef, EntityFieldDef entityFieldDef,
            String reference, WidgetColor color) throws UnifyException {
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

    public static String constructRenderer(WidgetTypeDef widgetTypeDef, EntityFieldDef entityFieldDef)
            throws UnifyException {
        return InputWidgetUtils.constructEditor(widgetTypeDef, entityFieldDef, null, true);
    }

    private static String constructEditor(WidgetTypeDef widgetTypeDef, EntityFieldDef entityFieldDef, String reference,
            boolean renderer) throws UnifyException {
        final EntityFieldAttributes efa = entityFieldDef.isWithResolvedTypeFieldDef()
                ? entityFieldDef.getResolvedTypeFieldDef()
                : entityFieldDef;
        String editor = renderer ? widgetTypeDef.getRenderer() : widgetTypeDef.getEditor();
        editor = InputWidgetUtils.resolveEditor(editor, widgetTypeDef, efa, entityFieldDef, reference);
        return editor;
    }

    private static String resolveEditor(String editor, WidgetTypeDef widgetTypeDef, EntityFieldAttributes efa,
            EntityFieldDef entityFieldDef, String reference) throws UnifyException {
        switch (widgetTypeDef.getLongName()) {
            case "application.richtexteditor":
            case "application.richtexteditormedium":
            case "application.richtexteditorlarge":
            case "application.richtexteditorxlarge":
            case "application.richtexteditorxxlarge":
                break;
            case "application.emailset":
            case "application.mobileset":
            case "application.textarea":
            case "application.textareamedium":
            case "application.textarealarge":
            case "application.textareaxlarge":
            case "application.textareaxxlarge":
                editor = String.format(editor, efa.getMinLen(), efa.getMaxLen(), efa.isTrim());
                break;
            case "application.password":
                editor = String.format(editor, efa.getMinLen(), efa.getMaxLen(), efa.isTrim());
                break;
            case "application.mobile":
            case "application.integertext":
                editor = String.format(editor, efa.getMinLen(), efa.getMaxLen(), efa.isTrim());
                break;
            case "application.suggestiontextsearch":
                String type = !StringUtils.isBlank(efa.getSuggestionType()) ? efa.getSuggestionType()
                        : entityFieldDef.getFieldLongName();
                editor = String.format(editor, type);
                break;
            case "application.text":
            case "application.name":
            case "application.alphanumeric":
            case "application.alphanumericwithspace":
            case "application.alphanumericwithspecial":
            case "application.wildname":
            case "application.email":
            case "application.website":
            case "application.domain": {
                String textCase = entityFieldDef != null
                        ? (entityFieldDef.getTextCase() != null ? entityFieldDef.getTextCase().toString().toLowerCase()
                                : "")
                        : "";
                editor = String.format(editor, efa.getMinLen(), efa.getMaxLen(), efa.isTrim(), textCase);
            }
                break;
            case "application.textwithupper":
            case "application.namewithupper":
            case "application.alphanumericwithupper":
            case "application.alphanumericwithspaceupper":
            case "application.fullnamewithupper":
            case "application.wordwithupper":
            case "application.alphanumericwithspecialupper": {
                editor = String.format(editor, efa.getMinLen(), efa.getMaxLen(), efa.isTrim(),
                        TextCase.UPPER.toString().toLowerCase());
            }
                break;
            case "application.alphanumericwithspecialcamel": {
                editor = String.format(editor, efa.getMinLen(), efa.getMaxLen(), efa.isTrim(),
                        TextCase.CAMEL.toString().toLowerCase());
            }
                break;
            case "application.series": {
                editor = String.format(editor, efa.getMinLen(), efa.getMaxLen(), efa.isTrim(), TextCase.UPPER.toString());
            }
                break;
            case "application.fullname":
            case "application.fullnamewithspecial": {
                String textCase = entityFieldDef != null
                        ? (entityFieldDef.getTextCase() != null ? entityFieldDef.getTextCase().toString().toLowerCase()
                                : TextCase.CAMEL.toString().toLowerCase())
                        : TextCase.CAMEL.toString().toLowerCase();
                editor = String.format(editor, efa.getMinLen(), efa.getMaxLen(), efa.isTrim(), textCase);
            }
                break;
            case "application.javafieldname":
            case "application.columnname":
            case "application.word":
                editor = String.format(editor, efa.getMinLen(), efa.getMaxLen(), efa.isTrim());
                break;
            case "application.integer":
            case "application.integerformatless":
                editor = String.format(editor, efa.getPrecision(), efa.isAllowNegative());
                break;
            case "application.amountwhole":
            case "application.amountaccountingwhole":
                int precision = efa.getScale() > 0 ? efa.getPrecision() - efa.getScale() : efa.getPrecision();
                editor = String.format(editor, precision, efa.isAllowNegative());
                break;
            case "application.amount":
            case "application.amountaccounting":
            case "application.decimal":
                editor = String.format(editor, efa.getPrecision(), efa.getScale(), efa.isAllowNegative());
                break;
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
                String _references = entityFieldDef != null ? entityFieldDef.getReferences() : efa.getReferences();
                editor = String.format(editor, _references);
                break;
            case "application.entitylist":
            case "application.entitysearch":
            case "application.entityselect":
            case "application.caseentitysearch":
                if (entityFieldDef != null) {
                    if (StringUtils.isBlank(reference)) {
                        reference = entityFieldDef.isEntityRef() ? entityFieldDef.getRefDef().getLongName()
                                : entityFieldDef.getReferences();
                    }

                    editor = String.format(editor, reference,
                            StringUtils.toNonNullString(entityFieldDef.getInputListKey(), ""));
                }
                break;
            case "application.workappentitylist":
            case "application.workappentitysearch":
                if (entityFieldDef != null) {
                    editor = String.format(editor, StringUtils.toNonNullString(entityFieldDef.getInputListKey(), ""));
                }
                break;
            case "application.fileupload":
                if (entityFieldDef != null) {
                    editor = String.format(editor, entityFieldDef.getRefDef().getLongName(),
                            entityFieldDef.getEntityLongName(), entityFieldDef.getFieldName());
                }
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
                if (type.isEnumGroup()) {
                    listCommand = "enumconstconditionlist";
                } else {
                    listCommand = "refconditionlist";
                }
            } else {
                listCommand = FilterUtils.getFilterConditionTypeListCommand(
                        _entityFieldDef.getDataType().dataType().javaClass(), listType,_entityFieldDef.isEnumGroup());
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
            if (appSetValues != null && !StringUtils.isBlank(appSetValues.getDefinition())) {
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
        if (setValuesDef != null) {
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
        if (fieldSequenceDef != null) {
            List<FieldSequenceEntryConfig> entryList = new ArrayList<FieldSequenceEntryConfig>();
            for (FieldSequenceEntryDef fieldSequenceEntryDef : fieldSequenceDef.getFieldSequenceList()) {
                entryList.add(new FieldSequenceEntryConfig(fieldSequenceEntryDef.getFieldName(),
                        fieldSequenceEntryDef.getStandardFormatCode()));
            }

            return new FieldSequenceConfig(entryList);
        }

        return null;
    }

    public static String getFieldSequenceDefinition(FieldSequenceDef fieldSequenceDef) throws UnifyException {
        String result = null;
        if (fieldSequenceDef != null) {
        try (StringWriter sw = new StringWriter(); BufferedWriter bw = new BufferedWriter(sw)) {
            for (FieldSequenceEntryDef fieldSequenceEntryDef : fieldSequenceDef.getFieldSequenceList()) {
                bw.write(fieldSequenceEntryDef.getFieldName());
                bw.write(']');
                if (fieldSequenceEntryDef.isWithStandardFormatCode()) {
                    bw.write(fieldSequenceEntryDef.getStandardFormatCode());
                    bw.write(']');
                }

                bw.newLine();
            }

            bw.flush();
            result = sw.toString();
        } catch (IOException e) {
            throw new UnifyOperationException(e);
        }
        }
        
        return result;
    }

    public static AppFieldSequence newAppFieldSequence(FieldSequenceConfig fieldSequenceConfig) throws UnifyException {
        if (fieldSequenceConfig != null) {
            return new AppFieldSequence(InputWidgetUtils.getFieldSequenceDefinition(fieldSequenceConfig));
        }

        return null;
    }

    public static String getFieldSequenceDefinition(FieldSequenceConfig fieldSequenceConfig) throws UnifyException {
        String result = null;
        if (fieldSequenceConfig != null) {
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
        }
        
        return result;
    }

    public static PropertySequenceDef getPropertySequenceDef(AppPropertySequence appPropertySequence)
            throws UnifyException {
        return InputWidgetUtils.getPropertySequenceDef(null, null, appPropertySequence);
    }

    public static PropertySequenceDef getPropertySequenceDef(String name, String description,
            AppPropertySequence appPropertySequence) throws UnifyException {
        if (appPropertySequence != null) {
            PropertySequenceDef.Builder svdb = PropertySequenceDef.newBuilder();
            svdb.name(name).description(description);
            try (BufferedReader reader = new BufferedReader(new StringReader(appPropertySequence.getDefinition()))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] p = line.split("]");
                    String property = p[0];
                    String label = p.length > 1 ? p[1] : null;
                    svdb.addSequenceEntryDef(property, label);
                }
            } catch (IOException e) {
                throw new UnifyOperationException(e);
            }

            return svdb.build();
        }

        return null;
    }

    public static PropertySequenceConfig getPropertySequenceConfig(AppPropertySequence appPropertySequence)
            throws UnifyException {
        PropertySequenceDef propertySequenceDef = InputWidgetUtils.getPropertySequenceDef(appPropertySequence);
        return InputWidgetUtils.getPropertySequenceConfig(propertySequenceDef);
    }

    public static PropertySequenceConfig getPropertySequenceConfig(PropertySequenceDef propertySequenceDef)
            throws UnifyException {
        if (propertySequenceDef != null) {
            List<PropertySequenceEntryConfig> entryList = new ArrayList<PropertySequenceEntryConfig>();
            for (PropertySequenceEntryDef propertySequenceEntryDef : propertySequenceDef.getSequenceList()) {
                entryList.add(new PropertySequenceEntryConfig(propertySequenceEntryDef.getProperty(),
                        propertySequenceEntryDef.getLabel()));
            }

            return new PropertySequenceConfig(entryList);
        }

        return null;
    }

    public static String getPropertySequenceDefinition(PropertySequenceDef propertySequenceDef) throws UnifyException {
        String result = null;
        if (propertySequenceDef != null) {
            try (StringWriter sw = new StringWriter(); BufferedWriter bw = new BufferedWriter(sw)) {
                for (PropertySequenceEntryDef propertySequenceEntryDef : propertySequenceDef.getSequenceList()) {
                    bw.write(propertySequenceEntryDef.getProperty());
                    bw.write(']');
                    if (!StringUtils.isBlank(propertySequenceEntryDef.getLabel())) {
                        bw.write(propertySequenceEntryDef.getLabel());
                        bw.write(']');
                    }
                    bw.newLine();
                }

                bw.flush();
                result = sw.toString();
            } catch (IOException e) {
                throw new UnifyOperationException(e);
            }
        }

        return result;
    }

    public static AppPropertySequence newAppPropertySequence(PropertySequenceConfig propertySequenceConfig)
            throws UnifyException {
        if (propertySequenceConfig != null) {
            return new AppPropertySequence(InputWidgetUtils.getPropertySequenceDefinition(propertySequenceConfig));
        }

        return null;
    }

    public static String getPropertySequenceDefinition(PropertySequenceConfig propertySequenceConfig)
            throws UnifyException {
        String result = null;
        if (propertySequenceConfig != null) {
            try (StringWriter sw = new StringWriter(); BufferedWriter bw = new BufferedWriter(sw)) {
                for (PropertySequenceEntryConfig entry : propertySequenceConfig.getEntryList()) {
                    bw.write(entry.getProperty());
                    bw.write(']');
                    if (entry.getLabel() != null) {
                        bw.write(entry.getLabel());
                        bw.write(']');
                    }

                    bw.newLine();
                }

                bw.flush();
                result = sw.toString();
            } catch (IOException e) {
                throw new UnifyOperationException(e);
            }
        }

        return result;
    }

    public static WidgetRulesDef getWidgetRulesDef(AppWidgetRules appWidgetRules) throws UnifyException {
        return InputWidgetUtils.getWidgetRulesDef(null, null, appWidgetRules);
    }

    public static WidgetRulesDef getWidgetRulesDef(String name, String description, AppWidgetRules appWidgetRules)
            throws UnifyException {
        if (appWidgetRules != null) {
            WidgetRulesDef.Builder svdb = WidgetRulesDef.newBuilder();
            svdb.name(name).description(description);
            try (BufferedReader reader = new BufferedReader(new StringReader(appWidgetRules.getDefinition()))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    String[] p = line.split("]");
                    String fieldName = p[0];
                    String widget = p.length > 1 ? p[1] : null;
                    svdb.addWidgetRuleEntryDef(fieldName, widget);
                }
            } catch (IOException e) {
                throw new UnifyOperationException(e);
            }

            return svdb.build();
        }

        return null;
    }

    public static WidgetRulesConfig getWidgetRulesConfig(AppWidgetRules appWidgetRules) throws UnifyException {
        WidgetRulesDef widgetRulesDef = InputWidgetUtils.getWidgetRulesDef(appWidgetRules);
        return InputWidgetUtils.getWidgetRulesConfig(widgetRulesDef);
    }

    public static WidgetRulesConfig getWidgetRulesConfig(WidgetRulesDef widgetRulesDef) throws UnifyException {
        if (widgetRulesDef != null) {
        List<WidgetRuleEntryConfig> entryList = new ArrayList<WidgetRuleEntryConfig>();
        for (WidgetRuleEntryDef widgetRuleEntryDef : widgetRulesDef.getWidgetRuleEntryList()) {
            entryList.add(new WidgetRuleEntryConfig(widgetRuleEntryDef.getFieldName(), widgetRuleEntryDef.getWidget()));
        }

        return new WidgetRulesConfig(entryList);
        }
        
        return null;
    }

    public static AppWidgetRules newAppWidgetRules(WidgetRulesConfig widgetRulesConfig) throws UnifyException {
        if (widgetRulesConfig != null) {
            return new AppWidgetRules(InputWidgetUtils.getWidgetRulesDefinition(widgetRulesConfig));
        }

        return null;
    }

    public static String getWidgetRulesDefinition(WidgetRulesConfig widgetRulesConfig) throws UnifyException {
        String result = null;
        if (widgetRulesConfig != null) {
        try (StringWriter sw = new StringWriter(); BufferedWriter bw = new BufferedWriter(sw)) {
            for (WidgetRuleEntryConfig entry : widgetRulesConfig.getEntryList()) {
                bw.write(entry.getFieldName());
                bw.write(']');
                if (entry.getWidget() != null) {
                    bw.write(entry.getWidget());
                    bw.write(']');
                }

                bw.newLine();
            }

            bw.flush();
            result = sw.toString();
        } catch (IOException e) {
            throw new UnifyOperationException(e);
        }
        }
        
        return result;
    }

    public static String getWidgetRulesDefinition(WidgetRulesDef widgetRulesDef) throws UnifyException {
        String result = null;
        if (widgetRulesDef != null) {
        try (StringWriter sw = new StringWriter(); BufferedWriter bw = new BufferedWriter(sw)) {
            for (WidgetRuleEntryDef widgetRuleEntryDef : widgetRulesDef.getWidgetRuleEntryList()) {
                bw.write(widgetRuleEntryDef.getFieldName());
                bw.write(']');
                if (widgetRuleEntryDef.isWithWidget()) {
                    bw.write(widgetRuleEntryDef.getWidget());
                    bw.write(']');
                }

                bw.newLine();
            }

            bw.flush();
            result = sw.toString();
        } catch (IOException e) {
            throw new UnifyOperationException(e);
        }
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

    public static EntitySearchInputConfig getEntitySearchInputConfig(AppEntitySearchInput appEntitySearchInput)
            throws UnifyException {
        if (appEntitySearchInput != null) {
            EntitySearchInputConfig entitySearchInputConfig = new EntitySearchInputConfig();
            InputWidgetUtils.getSearchInputsConfig(entitySearchInputConfig, appEntitySearchInput.getName(),
                    appEntitySearchInput.getDescription(), appEntitySearchInput.getRestrictionResolver(),
                    appEntitySearchInput.getSearchInput());
            return entitySearchInputConfig;
        }

        return null;
    }

    public static SearchInputsConfig getSearchInputConfig(AppEntitySearchInput appEntitySearchInput)
            throws UnifyException {
        if (appEntitySearchInput != null && appEntitySearchInput.getSearchInput() != null) {
            SearchInputsConfig searchInputConfig = new SearchInputsConfig();
            InputWidgetUtils.getSearchInputsConfig(searchInputConfig, appEntitySearchInput.getName(),
                    appEntitySearchInput.getDescription(), appEntitySearchInput.getRestrictionResolver(),
                    appEntitySearchInput.getSearchInput());
            return searchInputConfig;
        }

        return null;
    }

    public static void getSearchInputsConfig(SearchInputsConfig searchInputsConfig, String name, String description,
            String restrictionResolver, AppSearchInput appSearchInput) throws UnifyException {
        if (appSearchInput != null) {
            SearchInputsDef searchInputsDef = InputWidgetUtils.getSearchInputsDef(name, description,
                    restrictionResolver, appSearchInput);
            searchInputsConfig.setName(name);
            searchInputsConfig.setDescription(description);
            List<SearchInputConfig> inputList = new ArrayList<SearchInputConfig>();
            for (SearchInputDef searchInputDef : searchInputsDef.getSearchInputDefList()) {
                SearchInputConfig searchInputConfig = new SearchInputConfig();
                searchInputConfig.setType(searchInputDef.getType());
                searchInputConfig.setField(searchInputDef.getFieldName());
                searchInputConfig.setLabel(searchInputDef.getLabel());
                searchInputConfig.setWidget(searchInputDef.getWidget());
                inputList.add(searchInputConfig);
            }

            searchInputsConfig.setInputList(inputList);
        }
    }

    public static SearchInputsDef getSearchInputsDef(String name, String description, String restrictionResolver,
            AppSearchInput appSearchInput) throws UnifyException {
        if (appSearchInput != null) {
            SearchInputsDef.Builder sidb = SearchInputsDef.newBuilder();
            sidb.name(name).description(description).restrictionResolverName(restrictionResolver);
            if (appSearchInput.getDefinition() != null) {
                try (BufferedReader reader = new BufferedReader(new StringReader(appSearchInput.getDefinition()));) {
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        String[] p = line.split("]");
                        String label = p[0];
                        String field = p[1];
                        String widget = p[2];
                        SearchConditionType type = null;
                        if (p.length > 3) {
                            type = SearchConditionType.fromCode(p[3]);
                        }
                        sidb.addSearchInputDef(type, field, widget, label);
                    }
                } catch (IOException e) {
                    throw new UnifyOperationException(e);
                }
            }

            return sidb.build();
        }

        return null;
    }

    public static AppletFilterConfig getFilterConfig(AppletUtilities au, AppAppletFilter appAppletFilter)
            throws UnifyException {
        if (appAppletFilter != null) {
            AppletFilterConfig appletFilterConfig = new AppletFilterConfig();
            InputWidgetUtils.getFilterConfig(au, appletFilterConfig, appAppletFilter.getName(),
                    appAppletFilter.getDescription(), appAppletFilter.getFilter());
            appletFilterConfig.setPreferredForm(appAppletFilter.getPreferredForm());
            appletFilterConfig.setPreferredChildListApplet(appAppletFilter.getPreferredChildListApplet());
            appletFilterConfig.setQuickFilter(appAppletFilter.isQuickFilter());
            appletFilterConfig.setChildListActionType(appAppletFilter.getChildListActionType());
            appletFilterConfig.setFilterGenerator(appAppletFilter.getFilterGenerator());
            appletFilterConfig.setFilterGeneratorRule(appAppletFilter.getFilterGeneratorRule());
            return appletFilterConfig;
        }

        return null;
    }

    public static FormFilterConfig getFilterConfig(AppletUtilities au, AppFormFilter appFormFilter)
            throws UnifyException {
        if (appFormFilter != null) {
            FormFilterConfig formFilterConfig = new FormFilterConfig();
            InputWidgetUtils.getFilterConfig(au, formFilterConfig, appFormFilter.getName(),
                    appFormFilter.getDescription(), appFormFilter.getFilter());
            formFilterConfig.setFilterGenerator(appFormFilter.getFilterGenerator());
            formFilterConfig.setFilterGeneratorRule(appFormFilter.getFilterGeneratorRule());
            return formFilterConfig;
        }

        return null;
    }

    public static TableFilterConfig getFilterConfig(AppletUtilities au, AppTableFilter appTableFilter)
            throws UnifyException {
        if (appTableFilter != null) {
            TableFilterConfig tableFilterConfig = new TableFilterConfig();
            InputWidgetUtils.getFilterConfig(au, tableFilterConfig, appTableFilter.getName(),
                    appTableFilter.getDescription(), appTableFilter.getFilter());
            tableFilterConfig.setRowColor(appTableFilter.getRowColor());
            tableFilterConfig.setLegendLabel(appTableFilter.getLegendLabel());
            tableFilterConfig.setFilterGenerator(appTableFilter.getFilterGenerator());
            tableFilterConfig.setFilterGeneratorRule(appTableFilter.getFilterGeneratorRule());
            return tableFilterConfig;
        }

        return null;
    }

    public static FilterConfig getFilterConfig(AppletUtilities au, AppFilter appFilter) throws UnifyException {
        return InputWidgetUtils.getFilterConfig(au, null, null, appFilter);
    }

    public static FilterConfig getFilterConfig(AppletUtilities au, String name, String description, AppFilter appFilter)
            throws UnifyException {
        if (appFilter != null) {
            FilterConfig filterConfig = new FilterConfig();
            getFilterConfig(au, filterConfig, name, description, appFilter);
            return filterConfig;
        }

        return null;
    }

    public static void getFilterConfig(AppletUtilities au, FilterConfig filterConfig, String name, String description,
            AppFilter appFilter) throws UnifyException {
        if (appFilter != null) {
            FilterDef filterDef = InputWidgetUtils.getFilterDef(au, name, description,
                    filterConfig.getFilterGenerator(), appFilter);
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

    public static FilterDef getFilterDef(AppletUtilities au, String filterGenerator, AppFilter appFilter)
            throws UnifyException {
        return InputWidgetUtils.getFilterDef(au, null, null, filterGenerator, appFilter);
    }

    public static FilterDef getFilterDef(AppletUtilities au, String name, String description, String filterGenerator,
            AppFilter appFilter) throws UnifyException {
        if (appFilter != null || !StringUtils.isBlank(filterGenerator)) {
            FilterDef.Builder fdb = FilterDef.newBuilder(au);
            fdb.name(name).description(description).filterGenerator(filterGenerator);
            if (appFilter != null) {
                addFilterDefinition(fdb, appFilter.getDefinition());
            }

            return fdb.build();
        }

        return null;
    }

    public static FilterDef getFilterDef(AppletUtilities au, String filterDefinition) throws UnifyException {
        if (filterDefinition != null) {
            FilterDef.Builder fdb = FilterDef.newBuilder(au);
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

    public static FilterDef getFilterDef(AppletUtilities au, Restriction restriction) throws UnifyException {
        return InputWidgetUtils.getFilterDef(au, null, null, restriction);
    }

    public static FilterDef getFilterDef(AppletUtilities au, AppletDef appletDef, FilterDef filterDef,
            Restriction restriction, Date now) throws UnifyException {
        EntityDef entityDef = au.getEntityDef(appletDef.getEntity());
        return InputWidgetUtils.getFilterDef(au, entityDef, filterDef, restriction, now);
    }

    public static FilterDef getFilterDef(AppletUtilities au, EntityDef entityDef, FilterDef filterDef,
            Restriction restriction, Date now) throws UnifyException {
        if (restriction == null) {
            return new FilterDef(filterDef);
        }

        return InputWidgetUtils.getFilterDef(au, null, null,
                new And().add(InputWidgetUtils.getRestriction(au, entityDef, filterDef, now)).add(restriction));
    }

    public static FilterDef getFilterDef(AppletUtilities au, String name, String description, Restriction restriction)
            throws UnifyException {
        if (restriction != null) {
            FilterDef.Builder fdb = FilterDef.newBuilder(au);
            fdb.name(name).description(description);
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

    public static String getFilterDefinition(AppletUtilities au, Restriction restriction) throws UnifyException {
        return InputWidgetUtils.getFilterDefinition(InputWidgetUtils.getFilterDef(au, restriction));
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
        if (update != null) {
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
        }
        
        return result;
    }

    public static AppSearchInput newAppSearchInput(SearchInputsConfig searchInputsConfig) throws UnifyException {
        if (searchInputsConfig != null) {
            return new AppSearchInput(InputWidgetUtils.getSearchInputDefinition(searchInputsConfig));
        }

        return null;
    }

    public static String getSearchInputDefinition(SearchInputsConfig searchInputsConfig) throws UnifyException {
        String result = null;
        if (searchInputsConfig != null) {
        try (StringWriter sw = new StringWriter(); BufferedWriter bw = new BufferedWriter(sw);) {
            if (!DataUtils.isBlank(searchInputsConfig.getInputList())) {
                for (SearchInputConfig searchInputConfig : searchInputsConfig.getInputList()) {
                    bw.write(searchInputConfig.getLabel());
                    bw.write(']');
                    bw.write(searchInputConfig.getField());
                    bw.write(']');
                    bw.write(searchInputConfig.getWidget());
                    bw.write(']');
                    if (searchInputConfig.getType() != null) {
                        bw.write(searchInputConfig.getType().code());
                        bw.write(']');
                    }
                    bw.newLine();
                }
            }

            bw.flush();
            result = sw.toString();
        } catch (IOException e) {
            throw new UnifyOperationException(e);
        }
        }
        
        return result;
    }

    public static String getSearchInputDefinition(SearchInputsDef searchInputsDef) throws UnifyException {
        String result = null;
        if (searchInputsDef != null) {
        try (StringWriter sw = new StringWriter(); BufferedWriter bw = new BufferedWriter(sw);) {
            if (!searchInputsDef.isBlank()) {
                for (SearchInputDef searchInputDef : searchInputsDef.getSearchInputDefList()) {
                    bw.write(searchInputDef.getLabel());
                    bw.write(']');
                    bw.write(searchInputDef.getFieldName());
                    bw.write(']');
                    bw.write(searchInputDef.getWidget());
                    bw.write(']');
                    if (searchInputDef.getType() != null) {
                        bw.write(searchInputDef.getType().code());
                        bw.write(']');
                    }
                    bw.newLine();
                }
            }

            bw.flush();
            result = sw.toString();
        } catch (IOException e) {
            throw new UnifyOperationException(e);
        }
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
            if (!DataUtils.isBlank(restrictionConfigList)) {
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
            }

            bw.flush();
            result = sw.toString();
        } catch (IOException e) {
            throw new UnifyOperationException(e);
        }

        return result;
    }

    public static Restriction getRestriction(AppletUtilities au, EntityDef entityDef, FilterDef filterDef, Date now)
            throws UnifyException {
        return InputWidgetUtils.getRestriction(au, entityDef, filterDef, now, null);
    }

    public static Restriction getRestriction(AppletUtilities au, EntityDef entityDef, FilterDef filterDef, Date now,
            Map<String, Object> parameters) throws UnifyException {
        if (filterDef != null) {
            List<FilterRestrictionDef> conditionList = filterDef.getFilterRestrictionDefList();
            if (!conditionList.isEmpty()) {
                FilterRestrictionDef fo = conditionList.get(0);
                if (conditionList.size() == 1 && !fo.getType().isCompound()) {
                    ResolvedCondition resolved = InputWidgetUtils.resolveFieldParam(au, entityDef, fo,
                            au.specialParamProvider(), now, parameters);
                    return resolved.createSimpleCriteria();
                }

                CriteriaBuilder cb = new CriteriaBuilder();
                addCompoundCriteria(au, cb, entityDef, filterDef, fo, 1, au.specialParamProvider(), now, parameters);
                return cb.build();
            }
        }

        return null;
    }

    public static ResolvedCondition resolveParamCondition(String fieldName, FilterConditionType type, Object paramA,
            Object paramB, Map<String, Object> parameters) throws UnifyException {
        if (parameters != null && type.isParameterVal()) {
            if (paramA != null) {
                paramA = parameters.get((String) paramA);
            }

            if (paramB != null) {
                paramB = parameters.get((String) paramB);
            }

            switch (type) {
                case BEGINS_WITH_PARAM:
                    type = FilterConditionType.BEGINS_WITH;
                    break;
                case BETWEEN_PARAM:
                    type = FilterConditionType.BETWEEN;
                    break;
                case ENDS_WITH_PARAM:
                    type = FilterConditionType.ENDS_WITH;
                    break;
                case EQUALS_PARAM:
                    type = FilterConditionType.EQUALS;
                    break;
                case GREATER_OR_EQUAL_PARAM:
                    type = FilterConditionType.GREATER_OR_EQUAL;
                    break;
                case GREATER_THAN_PARAM:
                    type = FilterConditionType.GREATER_THAN;
                    break;
                case IBEGINS_WITH_PARAM:
                    type = FilterConditionType.IBEGINS_WITH;
                    break;
                case IENDS_WITH_PARAM:
                    type = FilterConditionType.IENDS_WITH;
                    break;
                case ILIKE_PARAM:
                    type = FilterConditionType.ILIKE;
                    break;
                case LESS_OR_EQUAL_PARAM:
                    type = FilterConditionType.LESS_OR_EQUAL;
                    break;
                case LESS_THAN_PARAM:
                    type = FilterConditionType.LESS_THAN;
                    break;
                case LIKE_PARAM:
                    type = FilterConditionType.LIKE;
                    break;
                case NOT_BEGIN_WITH_PARAM:
                    type = FilterConditionType.NOT_BEGIN_WITH;
                    break;
                case NOT_BETWEEN_PARAM:
                    type = FilterConditionType.NOT_BETWEEN;
                    break;
                case NOT_END_WITH_PARAM:
                    type = FilterConditionType.NOT_END_WITH;
                    break;
                case NOT_EQUALS_PARAM:
                    type = FilterConditionType.NOT_EQUALS;
                    break;
                case NOT_LIKE_PARAM:
                    type = FilterConditionType.NOT_LIKE;
                    break;
                case OR:
                    break;
                default:
                    break;
            }
        }

        return new ResolvedCondition(fieldName, type, paramA, paramB);
    }

    public static ResolvedCondition resolveLingualStringCondition(String fieldName, EntityFieldDef entityFieldDef,
            Date now, FilterConditionType type, Object paramA, Object paramB) throws UnifyException {
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

        return new ResolvedCondition(fieldName, type, paramA, paramB);
    }

    public static ResolvedCondition resolveDateCondition(String fieldName, EntityFieldDef entityFieldDef, Date now,
            FilterConditionType type, Object paramA, Object paramB) throws UnifyException {
        return InputWidgetUtils.resolveDateCondition(fieldName, now, type, paramA, paramB,
                entityFieldDef.isTimestamp());
    }

    public static ResolvedCondition resolveDateCondition(String fieldName, Date now, FilterConditionType type,
            Object paramA, Object paramB, boolean timestamp) throws UnifyException {
        if (type.isLingual()) {
            if (type.isRange()) {
                paramA = LingualDateUtils.getDateFromNow(now, (String) paramA);
                paramB = LingualDateUtils.getDateFromNow(now, (String) paramB);
                paramA = CalendarUtils.getMidnightDate((Date) paramA);
                paramB = CalendarUtils.getLastSecondDate((Date) paramB);
            } else {
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
            }
        } else {
            if (timestamp) {
                switch (type) {
                    case EQUALS:
                    case EQUALS_PARAM:
                        paramA = CalendarUtils.getMidnightDate((Date) paramA);
                        paramB = CalendarUtils.getLastSecondDate((Date) paramA);
                        type = FilterConditionType.BETWEEN;
                        break;
                    case BETWEEN:
                    case BETWEEN_PARAM:
                        paramA = CalendarUtils.getMidnightDate((Date) paramA);
                        paramB = CalendarUtils.getLastSecondDate((Date) paramB);
                        type = FilterConditionType.BETWEEN;
                        break;
                    case GREATER_OR_EQUAL:
                    case GREATER_OR_EQUAL_PARAM:
                        paramA = CalendarUtils.getMidnightDate((Date) paramA);
                        type = FilterConditionType.GREATER_OR_EQUAL;
                        break;
                    case GREATER_THAN:
                    case GREATER_THAN_PARAM:
                        paramA = CalendarUtils.getLastSecondDate((Date) paramA);
                        type = FilterConditionType.GREATER_THAN;
                        break;
                    case LESS_OR_EQUAL:
                    case LESS_OR_EQUAL_PARAM:
                        paramA = CalendarUtils.getLastSecondDate((Date) paramA);
                        type = FilterConditionType.LESS_OR_EQUAL;
                        break;
                    case LESS_THAN:
                    case LESS_THAN_PARAM:
                        paramA = CalendarUtils.getMidnightDate((Date) paramA);
                        type = FilterConditionType.LESS_THAN;
                        break;
                    case NOT_EQUALS:
                    case NOT_EQUALS_PARAM:
                        paramA = CalendarUtils.getMidnightDate((Date) paramA);
                        paramB = CalendarUtils.getLastSecondDate((Date) paramA);
                        type = FilterConditionType.NOT_BETWEEN;
                        break;
                    case NOT_BETWEEN:
                    case NOT_BETWEEN_PARAM:
                        paramA = CalendarUtils.getMidnightDate((Date) paramA);
                        paramB = CalendarUtils.getLastSecondDate((Date) paramB);
                        type = FilterConditionType.NOT_BETWEEN;
                        break;
                    default:
                        break;
                }
            }
        }

        return new ResolvedCondition(fieldName, type, paramA, paramB);
    }

    private static int addCompoundCriteria(AppletUtilities au, CriteriaBuilder cb, EntityDef entityDef,
            FilterDef filterDef, FilterRestrictionDef fo, int nIndex, SpecialParamProvider specialParamProvider,
            Date now, Map<String, Object> parameters) throws UnifyException {
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
                    i = addCompoundCriteria(au, cb, entityDef, filterDef, sfo, i + 1, specialParamProvider, now,
                            parameters) - 1;
                } else {
                    addSimpleCriteria(au, cb, entityDef, filterDef, sfo, specialParamProvider, now, parameters);
                }
            } else {
                break;
            }
        }

        cb.endCompound();
        return i;
    }

    private static void addSimpleCriteria(AppletUtilities au, CriteriaBuilder cb, EntityDef entityDef,
            FilterDef filterDef, FilterRestrictionDef fo, SpecialParamProvider specialParamProvider, Date now,
            Map<String, Object> parameters) throws UnifyException {
        ResolvedCondition condition = InputWidgetUtils.resolveFieldParam(au, entityDef, fo, specialParamProvider, now,
                parameters);
        condition.addSimpleCriteria(cb);
    }

    @SuppressWarnings("unchecked")
    private static ResolvedCondition resolveFieldParam(AppletUtilities au, EntityDef entityDef, FilterRestrictionDef fo,
            SpecialParamProvider specialParamProvider, Date now, Map<String, Object> parameters) throws UnifyException {
        FilterConditionType type = fo.getType();
        Object paramA = specialParamProvider.resolveSpecialParameter(fo.getParamA());
        Object paramB = specialParamProvider.resolveSpecialParameter(fo.getParamB());
        if (type.isSessionParameterVal()) {
            if (paramA != null) {
                SessionParamType sessionParamtype = SessionParamType.fromCode((String) paramA);
                if (sessionParamtype != null) {
                    paramA = au.getSessionAttribute(String.class, sessionParamtype.attribute());
                }
            }
        } 
        
        if (!type.isFieldVal() && !type.isParameterVal()) {
            EntityFieldDef _entityFieldDef = entityDef.getFieldDef(fo.getFieldName());
            if (_entityFieldDef.isWithResolvedTypeFieldDef()) {
                _entityFieldDef = _entityFieldDef.getResolvedTypeFieldDef();
            }

            if (type.isLingual() && _entityFieldDef.isString()) {
                return InputWidgetUtils.resolveLingualStringCondition(fo.getFieldName(), _entityFieldDef, now, type,
                        paramA, paramB);
            } else if (_entityFieldDef.isTime()) {
                return InputWidgetUtils.resolveDateCondition(fo.getFieldName(), _entityFieldDef, now, type, paramA,
                        paramB);
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

        if (parameters != null && type.isParameterVal()) {
            return InputWidgetUtils.resolveParamCondition(fo.getFieldName(), type, paramA, paramB, parameters);
        }

        return new ResolvedCondition(fo.getFieldName(), type, paramA, paramB);
    }
}

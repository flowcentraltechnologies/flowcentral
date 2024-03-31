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

package com.flowcentraltech.flowcentral.codegeneration.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.entities.AppApplet;
import com.flowcentraltech.flowcentral.application.entities.AppAppletAlert;
import com.flowcentraltech.flowcentral.application.entities.AppAppletFilter;
import com.flowcentraltech.flowcentral.application.entities.AppAppletProp;
import com.flowcentraltech.flowcentral.application.entities.AppAppletSetValues;
import com.flowcentraltech.flowcentral.application.entities.AppAssignmentPage;
import com.flowcentraltech.flowcentral.application.entities.AppEntity;
import com.flowcentraltech.flowcentral.application.entities.AppEntityAttachment;
import com.flowcentraltech.flowcentral.application.entities.AppEntityCategory;
import com.flowcentraltech.flowcentral.application.entities.AppEntityExpression;
import com.flowcentraltech.flowcentral.application.entities.AppEntityField;
import com.flowcentraltech.flowcentral.application.entities.AppEntityIndex;
import com.flowcentraltech.flowcentral.application.entities.AppEntitySearchInput;
import com.flowcentraltech.flowcentral.application.entities.AppEntitySeries;
import com.flowcentraltech.flowcentral.application.entities.AppEntityUniqueCondition;
import com.flowcentraltech.flowcentral.application.entities.AppEntityUniqueConstraint;
import com.flowcentraltech.flowcentral.application.entities.AppEntityUpload;
import com.flowcentraltech.flowcentral.application.entities.AppForm;
import com.flowcentraltech.flowcentral.application.entities.AppFormAction;
import com.flowcentraltech.flowcentral.application.entities.AppFormAnnotation;
import com.flowcentraltech.flowcentral.application.entities.AppFormElement;
import com.flowcentraltech.flowcentral.application.entities.AppFormFieldValidationPolicy;
import com.flowcentraltech.flowcentral.application.entities.AppFormFilter;
import com.flowcentraltech.flowcentral.application.entities.AppFormRelatedList;
import com.flowcentraltech.flowcentral.application.entities.AppFormReviewPolicy;
import com.flowcentraltech.flowcentral.application.entities.AppFormSetState;
import com.flowcentraltech.flowcentral.application.entities.AppFormStatePolicy;
import com.flowcentraltech.flowcentral.application.entities.AppFormValidationPolicy;
import com.flowcentraltech.flowcentral.application.entities.AppFormWidgetRulesPolicy;
import com.flowcentraltech.flowcentral.application.entities.AppPropertyList;
import com.flowcentraltech.flowcentral.application.entities.AppPropertyListItem;
import com.flowcentraltech.flowcentral.application.entities.AppPropertyRule;
import com.flowcentraltech.flowcentral.application.entities.AppPropertyRuleChoice;
import com.flowcentraltech.flowcentral.application.entities.AppPropertySet;
import com.flowcentraltech.flowcentral.application.entities.AppRef;
import com.flowcentraltech.flowcentral.application.entities.AppSuggestionType;
import com.flowcentraltech.flowcentral.application.entities.AppTable;
import com.flowcentraltech.flowcentral.application.entities.AppTableAction;
import com.flowcentraltech.flowcentral.application.entities.AppTableColumn;
import com.flowcentraltech.flowcentral.application.entities.AppTableFilter;
import com.flowcentraltech.flowcentral.application.entities.AppTableLoading;
import com.flowcentraltech.flowcentral.application.entities.AppWidgetType;
import com.flowcentraltech.flowcentral.application.entities.Application;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldType;
import com.flowcentraltech.flowcentral.configuration.constants.FormElementType;
import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.flowcentraltech.flowcentral.configuration.xml.AppAssignmentPageConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppAssignmentPagesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppEntitiesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppEntityConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppFormConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppFormsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppTableConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppTablesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletAlertConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletFilterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletPropConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletSetValuesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ChoiceConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityAttachmentConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityCategoryConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityExpressionConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityFieldConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityIndexConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntitySearchInputConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntitySeriesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityUniqueConditionConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityUniqueConstraintConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityUploadConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FieldValidationPolicyConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FilterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormActionConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormAnnotationConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormFieldConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormFilterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormReviewPolicyConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormSectionConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormStatePolicyConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormTabConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormValidationPolicyConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormWidgetRulesPolicyConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ModuleAppConfig;
import com.flowcentraltech.flowcentral.configuration.xml.PropertyListConfig;
import com.flowcentraltech.flowcentral.configuration.xml.PropertyListPropConfig;
import com.flowcentraltech.flowcentral.configuration.xml.PropertyListsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.PropertyRuleConfig;
import com.flowcentraltech.flowcentral.configuration.xml.PropertyRulesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.PropertySetConfig;
import com.flowcentraltech.flowcentral.configuration.xml.RefConfig;
import com.flowcentraltech.flowcentral.configuration.xml.RefsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.RelatedListConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SetStateConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SetStatesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SuggestionTypeConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SuggestionTypesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.TableActionConfig;
import com.flowcentraltech.flowcentral.configuration.xml.TableColumnConfig;
import com.flowcentraltech.flowcentral.configuration.xml.TableFilterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.TableLoadingConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WidgetTypeConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WidgetTypesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.util.ConfigurationUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Application XML generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("application-xml-generator")
public class ApplicationXmlGenerator extends AbstractStaticArtifactGenerator {

    private static final String APPS_FOLDER = "apps/";

    @Configurable
    private ApplicationModuleService applicationModuleService;

    public ApplicationXmlGenerator() {
        super("src/main/resources/apps/");
    }

    @Override
    protected void doGenerate(ExtensionModuleStaticFileBuilderContext ctx, String applicationName, ZipOutputStream zos)
            throws UnifyException {
        final String filename = "extension-" + applicationName.toLowerCase() + "-application.xml";
        openEntry(filename, zos);

        final AppConfig appConfig = new AppConfig();
        Application application = applicationModuleService.findApplication(applicationName);
        final String lowerCaseApplicationName = application.getName().toLowerCase();
        String descKey = lowerCaseApplicationName + ".short.description";
        String labelKey = lowerCaseApplicationName + ".label";
        ctx.addMessage(StaticMessageCategoryType.HEADER, descKey, application.getDescription());
        ctx.addMessage(StaticMessageCategoryType.HEADER, labelKey, application.getLabel());
        appConfig.setModule(ctx.getModuleName());
        appConfig.setName(application.getName());
        appConfig.setDescription("$m{" + descKey + "}");
        appConfig.setLabel("$m{" + labelKey + "}");
        appConfig.setDisplayIndex(application.getDisplayIndex());
        appConfig.setDevelopable(application.isDevelopable());
        appConfig.setMenuAccess(application.isMenuAccess());
        appConfig.setAllowSecondaryTenants(application.isAllowSecondaryTenants());

        // Module application configuration
        ModuleAppConfig moduleAppConfig = new ModuleAppConfig();
        moduleAppConfig.setName(applicationName);
        moduleAppConfig.setLongDescription("$m{" + descKey + "}");
        moduleAppConfig.setShortDescription("$m{" + descKey + "}");
        moduleAppConfig.setConfigFile(APPS_FOLDER + filename);
        moduleAppConfig.setAutoInstall(true);
        ctx.addModuleAppConfig(moduleAppConfig);

        // Ancillary
        appConfig.setChartsConfig(ctx.getChartsConfig());
        appConfig.setChartDataSourcesConfig(ctx.getChartDataSourcesConfig());
        appConfig.setDashboardsConfig(ctx.getDashboardsConfig());
        appConfig.setNotifTemplatesConfig(ctx.getNotifTemplatesConfig());
        appConfig.setNotifLargeTextsConfig(ctx.getNotifLargeTextsConfig());
        appConfig.setReportsConfig(ctx.getReportsConfig());
        appConfig.setWorkflowsConfig(ctx.getWorkflowsConfig());
        appConfig.setWorkflowWizardsConfig(ctx.getWorkflowWizardsConfig());
        appConfig.setWfChannelsConfig(ctx.getWfChannelsConfig());

        // Applets
        List<Long> appletIdList = applicationModuleService.findCustomAppComponentIdList(applicationName,
                AppApplet.class);
        if (!DataUtils.isBlank(appletIdList)) {
            AppletsConfig appletsConfig = new AppletsConfig();
            List<AppletConfig> appletList = new ArrayList<AppletConfig>();
            for (Long appletId : appletIdList) {
                AppletConfig appletConfig = new AppletConfig();
                AppApplet appApplet = applicationModuleService.findAppApplet(appletId);
                descKey = getDescriptionKey(lowerCaseApplicationName, "applet", appApplet.getName());
                labelKey = descKey + ".label";
                ctx.addMessage(StaticMessageCategoryType.APPLET, descKey, appApplet.getDescription());
                ctx.addMessage(StaticMessageCategoryType.APPLET, labelKey, appApplet.getLabel());
                appletConfig.setName(appApplet.getName());
                appletConfig.setDescription("$m{" + descKey + "}");
                appletConfig.setLabel("$m{" + labelKey + "}");
                appletConfig.setType(appApplet.getType());
                appletConfig.setEntity(appApplet.getEntity());
                appletConfig.setIcon(appApplet.getIcon());
                appletConfig.setMenuAccess(appApplet.isMenuAccess());
                appletConfig.setSupportOpenInNewWindow(appApplet.isSupportOpenInNewWindow());
                appletConfig.setAllowSecondaryTenants(appApplet.isAllowSecondaryTenants());
                appletConfig.setDisplayIndex(appApplet.getDisplayIndex());
                appletConfig.setRouteToApplet(appApplet.getRouteToApplet());
                appletConfig.setPath(appApplet.getPath());
                appletConfig.setBaseField(appApplet.getBaseField());
                appletConfig.setAssignField(appApplet.getAssignField());
                appletConfig.setAssignDescField(appApplet.getAssignDescField());
                appletConfig.setPseudoDeleteField(appApplet.getPseudoDeleteField());
                appletConfig.setTitleFormat(appApplet.getTitleFormat());

                // Properties
                if (!DataUtils.isBlank(appApplet.getPropList())) {
                    List<AppletPropConfig> propList = new ArrayList<AppletPropConfig>();
                    for (AppAppletProp prop : appApplet.getPropList()) {
                        AppletPropConfig propConfig = new AppletPropConfig();
                        propConfig.setName(prop.getName());
                        propConfig.setValue(prop.getValue());
                        propList.add(propConfig);
                    }

                    appletConfig.setPropList(propList);
                }

                // Filters
                if (!DataUtils.isBlank(appApplet.getFilterList())) {
                    List<AppletFilterConfig> filterList = new ArrayList<AppletFilterConfig>();
                    for (AppAppletFilter appAppletFilter : appApplet.getFilterList()) {
                        AppletFilterConfig filterConfig = InputWidgetUtils.getFilterConfig(au(), appAppletFilter);
                        String filterKey = getDescriptionKey(descKey, "appletfilter", appAppletFilter.getName());
                        ctx.addMessage(StaticMessageCategoryType.APPLET, filterKey, appAppletFilter.getDescription());
                        filterConfig.setName(appAppletFilter.getName());
                        filterConfig.setDescription("$m{" + filterKey + "}");
                        filterList.add(filterConfig);
                    }

                    appletConfig.setFilterList(filterList);
                }

                // Set values
                if (!DataUtils.isBlank(appApplet.getSetValuesList())) {
                    List<AppletSetValuesConfig> valuesList = new ArrayList<AppletSetValuesConfig>();
                    for (AppAppletSetValues appAppletSetValues : appApplet.getSetValuesList()) {
                        AppletSetValuesConfig appletSetValuesConfig = new AppletSetValuesConfig();
                        appletSetValuesConfig.setName(appAppletSetValues.getName());
                        appletSetValuesConfig.setDescription(appAppletSetValues.getDescription());
                        appletSetValuesConfig.setValueGenerator(appAppletSetValues.getValueGenerator());
                        appletSetValuesConfig.setSetValues(InputWidgetUtils.getSetValuesConfig(
                                appAppletSetValues.getValueGenerator(), appAppletSetValues.getSetValues()));
                        valuesList.add(appletSetValuesConfig);
                    }

                    appletConfig.setValuesList(valuesList);
                }

                // Alerts
                if (!DataUtils.isBlank(appApplet.getAlertList())) {
                    List<AppletAlertConfig> alertList = new ArrayList<AppletAlertConfig>();
                    for (AppAppletAlert appAppletAlert : appApplet.getAlertList()) {
                        AppletAlertConfig appletAlertConfig = new AppletAlertConfig();
                        appletAlertConfig.setName(appAppletAlert.getName());
                        appletAlertConfig.setDescription(appAppletAlert.getDescription());
                        appletAlertConfig.setSender(appAppletAlert.getSender());
                        appletAlertConfig.setRecipientContactRule(appAppletAlert.getRecipientContactRule());
                        appletAlertConfig.setRecipientNameRule(appAppletAlert.getRecipientNameRule());
                        appletAlertConfig.setRecipientPolicy(appAppletAlert.getRecipientPolicy());
                        alertList.add(appletAlertConfig);
                    }

                    appletConfig.setAlertList(alertList);
                }

                appletList.add(appletConfig);
            }

            appletsConfig.setAppletList(appletList);
            appConfig.setAppletsConfig(appletsConfig);
        }

        // Widgets
        List<Long> widgetIdList = applicationModuleService.findCustomAppComponentIdList(applicationName,
                AppWidgetType.class);
        if (!DataUtils.isBlank(widgetIdList)) {
            WidgetTypesConfig widgetTypesConfig = new WidgetTypesConfig();
            List<WidgetTypeConfig> widgetTypeList = new ArrayList<WidgetTypeConfig>();
            for (Long widgetId : widgetIdList) {
                WidgetTypeConfig widgetTypeConfig = new WidgetTypeConfig();
                AppWidgetType appWidgetType = applicationModuleService.findAppWidgetType(widgetId);
                descKey = getDescriptionKey(lowerCaseApplicationName, "widgettype", appWidgetType.getName());
                ctx.addMessage(StaticMessageCategoryType.WIDGET, descKey, appWidgetType.getDescription());
                widgetTypeConfig.setDataType(appWidgetType.getDataType());
                widgetTypeConfig.setInputType(appWidgetType.getInputType());
                widgetTypeConfig.setName(appWidgetType.getName());
                widgetTypeConfig.setDescription("$m{" + descKey + "}");
                widgetTypeConfig.setEditor(appWidgetType.getEditor());
                widgetTypeConfig.setRenderer(appWidgetType.getRenderer());
                widgetTypeConfig.setStretch(appWidgetType.isStretch());
                widgetTypeConfig.setListOption(appWidgetType.isListOption());
                widgetTypeConfig.setEnumOption(appWidgetType.isEnumOption());
                widgetTypeList.add(widgetTypeConfig);
            }

            widgetTypesConfig.setWidgetTypeList(widgetTypeList);
            appConfig.setWidgetTypesConfig(widgetTypesConfig);
        }

        // References
        List<Long> refIdList = applicationModuleService.findCustomAppComponentIdList(applicationName, AppRef.class);
        if (!DataUtils.isBlank(refIdList)) {
            RefsConfig refsConfig = new RefsConfig();
            List<RefConfig> refList = new ArrayList<RefConfig>();
            for (Long refId : refIdList) {
                RefConfig refConfig = new RefConfig();
                AppRef appRef = applicationModuleService.findAppRef(refId);
                descKey = getDescriptionKey(lowerCaseApplicationName, "reference", appRef.getName());
                ctx.addMessage(StaticMessageCategoryType.REF, descKey, appRef.getDescription());
                refConfig.setName(appRef.getName());
                refConfig.setDescription("$m{" + descKey + "}");
                refConfig.setEntity(appRef.getEntity());
                refConfig.setSearchField(appRef.getSearchField());
                refConfig.setSearchTable(appRef.getSearchTable());
                refConfig.setSelectHandler(appRef.getSelectHandler());
                refConfig.setListFormat(appRef.getListFormat());
                refConfig.setFilterGenerator(appRef.getFilterGenerator());
                refConfig.setFilterGeneratorRule(appRef.getFilterGeneratorRule());
                refConfig.setFilter(InputWidgetUtils.getFilterConfig(au(), appRef.getFilter()));
                refList.add(refConfig);
            }

            refsConfig.setRefList(refList);
            appConfig.setRefsConfig(refsConfig);
        }

        // Entities
        List<Long> entityIdList = applicationModuleService.findCustomAppComponentIdList(applicationName,
                AppEntity.class);
        if (!DataUtils.isBlank(entityIdList)) {
            AppEntitiesConfig entitiesConfig = new AppEntitiesConfig();
            List<AppEntityConfig> entityList = new ArrayList<AppEntityConfig>();
            for (Long entityId : entityIdList) {
                AppEntityConfig appEntityConfig = new AppEntityConfig();
                AppEntity appEntity = applicationModuleService.findAppEntity(entityId);
                descKey = getDescriptionKey(lowerCaseApplicationName, "entity", appEntity.getName());
                labelKey = descKey + ".label";
                final String entityDescKey = descKey;
                ctx.addMessage(StaticMessageCategoryType.ENTITY, descKey, appEntity.getDescription());
                ctx.addMessage(StaticMessageCategoryType.ENTITY, labelKey, appEntity.getLabel());
                ctx.addEntity(ApplicationNameUtils.getApplicationEntityLongName(applicationName, appEntity.getName()));
                appEntityConfig.setType(ctx.getExtensionEntityClassName(appEntity));
                appEntityConfig.setName(appEntity.getName());
                appEntityConfig.setDescription("$m{" + descKey + "}");
                appEntityConfig.setLabel("$m{" + labelKey + "}");
                appEntityConfig.setEmailProducerConsumer(appEntity.getEmailProducerConsumer());
                appEntityConfig.setDelegate(appEntity.getDelegate());
                appEntityConfig.setTable(appEntity.getTableName());
                appEntityConfig.setDataSourceName(appEntity.getDataSourceName());
                appEntityConfig.setMapped(appEntity.isMapped());
                appEntityConfig.setAuditable(appEntity.isAuditable());
                appEntityConfig.setReportable(appEntity.isReportable());
                appEntityConfig.setActionPolicy(appEntity.isActionPolicy());

                // Fields
                if (!DataUtils.isBlank(appEntity.getFieldList())) {
                    List<EntityFieldConfig> entityFieldConfigList = new ArrayList<EntityFieldConfig>();
                    for (AppEntityField appEntityField : appEntity.getFieldList()) {
                        if (!EntityFieldType.BASE.equals(appEntityField.getType())) {
                            EntityFieldConfig entityFieldConfig = new EntityFieldConfig();
                            labelKey = getDescriptionKey(entityDescKey, "field.label", appEntityField.getName());
                            ctx.addMessage(StaticMessageCategoryType.ENTITY, labelKey, appEntityField.getLabel());
                            entityFieldConfig.setType(appEntityField.getDataType());
                            entityFieldConfig.setName(appEntityField.getName());
                            entityFieldConfig.setLabel("$m{" + labelKey + "}");
                            entityFieldConfig.setColumnName(appEntityField.getColumnName());
                            entityFieldConfig.setReferences(appEntityField.getReferences());
                            entityFieldConfig.setKey(appEntityField.getKey());
                            entityFieldConfig.setProperty(appEntityField.getProperty());
                            entityFieldConfig.setCategory(appEntityField.getCategory());
                            if (appEntityField.getInputLabel() != null) {
                                String inLabelKey = getDescriptionKey(entityDescKey, "field.inputlabel",
                                        appEntityField.getInputLabel());
                                ctx.addMessage(StaticMessageCategoryType.ENTITY, inLabelKey,
                                        appEntityField.getInputLabel());
                                entityFieldConfig.setInputLabel("$m{" + inLabelKey + "}");
                            }

                            entityFieldConfig.setInputWidget(appEntityField.getInputWidget());
                            entityFieldConfig.setSuggestionType(appEntityField.getSuggestionType());
                            entityFieldConfig.setInputListKey(appEntityField.getInputListKey());
                            entityFieldConfig.setLingualWidget(appEntityField.getLingualWidget());
                            entityFieldConfig.setLingualListKey(appEntityField.getLingualListKey());
                            entityFieldConfig.setAutoFormat(appEntityField.getAutoFormat());
                            entityFieldConfig.setDefaultVal(appEntityField.getDefaultVal());
                            entityFieldConfig.setMapped(appEntityField.getMapped());
                            entityFieldConfig.setTextCase(appEntityField.getTextCase());
                            entityFieldConfig.setColumns(appEntityField.getColumns());
                            entityFieldConfig.setRows(appEntityField.getRows());
                            entityFieldConfig.setMinLen(appEntityField.getMinLen());
                            entityFieldConfig.setMaxLen(appEntityField.getMaxLen());
                            entityFieldConfig.setPrecision(appEntityField.getPrecision());
                            entityFieldConfig.setScale(appEntityField.getScale());
                            entityFieldConfig.setAllowNegative(appEntityField.isAllowNegative());
                            entityFieldConfig.setReadOnly(appEntityField.isReadOnly());
                            entityFieldConfig.setNullable(appEntityField.isNullable());
                            entityFieldConfig.setAuditable(appEntityField.isAuditable());
                            entityFieldConfig.setReportable(appEntityField.isReportable());
                            entityFieldConfig.setDescriptive(appEntityField.isDescriptive());
                            entityFieldConfig.setMaintainLink(appEntityField.isMaintainLink());
                            entityFieldConfig.setBasicSearch(appEntityField.isBasicSearch());
                            entityFieldConfigList.add(entityFieldConfig);
                        }
                    }

                    appEntityConfig.setEntityFieldList(entityFieldConfigList);
                }

                // Series
                if (!DataUtils.isBlank(appEntity.getSeriesList())) {
                    List<EntitySeriesConfig> seriesList = new ArrayList<EntitySeriesConfig>();
                    for (AppEntitySeries appEntitySeries : appEntity.getSeriesList()) {
                        EntitySeriesConfig entitySeriesConfig = new EntitySeriesConfig();
                        descKey = getDescriptionKey(entityDescKey, "series", appEntitySeries.getName());
                        labelKey = getDescriptionKey(entityDescKey, "series.label", appEntitySeries.getName());
                        ctx.addMessage(StaticMessageCategoryType.ENTITY, descKey, appEntitySeries.getDescription());
                        ctx.addMessage(StaticMessageCategoryType.ENTITY, labelKey, appEntitySeries.getLabel());
                        entitySeriesConfig.setType(appEntitySeries.getType());
                        entitySeriesConfig.setName(appEntitySeries.getName());
                        entitySeriesConfig.setDescription("$m{" + descKey + "}");
                        entitySeriesConfig.setLabel("$m{" + labelKey + "}");
                        entitySeriesConfig.setFieldName(appEntitySeries.getFieldName());
                        seriesList.add(entitySeriesConfig);
                    }

                    appEntityConfig.setSeriesList(seriesList);
                }

                // Categories
                if (!DataUtils.isBlank(appEntity.getCategoryList())) {
                    List<EntityCategoryConfig> categoryList = new ArrayList<EntityCategoryConfig>();
                    for (AppEntityCategory appEntityCategory : appEntity.getCategoryList()) {
                        EntityCategoryConfig entityCategoryConfig = new EntityCategoryConfig();
                        descKey = getDescriptionKey(entityDescKey, "category", appEntityCategory.getName());
                        labelKey = getDescriptionKey(entityDescKey, "category.label", appEntityCategory.getName());
                        ctx.addMessage(StaticMessageCategoryType.ENTITY, descKey, appEntityCategory.getDescription());
                        ctx.addMessage(StaticMessageCategoryType.ENTITY, labelKey, appEntityCategory.getLabel());
                        entityCategoryConfig.setName(appEntityCategory.getName());
                        entityCategoryConfig.setDescription("$m{" + descKey + "}");
                        entityCategoryConfig.setLabel("$m{" + labelKey + "}");
                        FilterConfig filterConfig = InputWidgetUtils.getFilterConfig(au(),
                                appEntityCategory.getFilter());
                        entityCategoryConfig
                                .setRestrictionList(filterConfig != null ? filterConfig.getRestrictionList() : null);
                        categoryList.add(entityCategoryConfig);
                    }

                    appEntityConfig.setCategoryList(categoryList);
                }

                // Attachments
                if (!DataUtils.isBlank(appEntity.getAttachmentList())) {
                    List<EntityAttachmentConfig> attachmentConfigList = new ArrayList<EntityAttachmentConfig>();
                    for (AppEntityAttachment appEntityAttachment : appEntity.getAttachmentList()) {
                        EntityAttachmentConfig entityAttachmentConfig = new EntityAttachmentConfig();
                        descKey = getDescriptionKey(entityDescKey, "attachment", appEntityAttachment.getName());
                        ctx.addMessage(StaticMessageCategoryType.ENTITY, descKey, appEntityAttachment.getDescription());
                        entityAttachmentConfig.setType(appEntityAttachment.getType());
                        entityAttachmentConfig.setName(appEntityAttachment.getName());
                        entityAttachmentConfig.setDescription("$m{" + descKey + "}");
                        attachmentConfigList.add(entityAttachmentConfig);
                    }

                    appEntityConfig.setAttachmentList(attachmentConfigList);
                }

                // Expressions
                if (!DataUtils.isBlank(appEntity.getExpressionList())) {
                    List<EntityExpressionConfig> expressionList = new ArrayList<EntityExpressionConfig>();
                    for (AppEntityExpression appEntityExpression : appEntity.getExpressionList()) {
                        EntityExpressionConfig entityExpressionConfig = new EntityExpressionConfig();
                        descKey = getDescriptionKey(entityDescKey, "expression", appEntityExpression.getName());
                        ctx.addMessage(StaticMessageCategoryType.ENTITY, descKey, appEntityExpression.getDescription());
                        entityExpressionConfig.setName(appEntityExpression.getName());
                        entityExpressionConfig.setDescription("$m{" + descKey + "}");
                        entityExpressionConfig.setExpression(appEntityExpression.getExpression());
                        expressionList.add(entityExpressionConfig);
                    }

                    appEntityConfig.setExpressionList(expressionList);
                }

                // Unique Constraints
                if (!DataUtils.isBlank(appEntity.getUniqueConstraintList())) {
                    List<EntityUniqueConstraintConfig> uniqueConstraintList = new ArrayList<EntityUniqueConstraintConfig>();
                    for (AppEntityUniqueConstraint appEntityUniqueConstraint : appEntity.getUniqueConstraintList()) {
                        EntityUniqueConstraintConfig entityUniqueConstraintConfig = new EntityUniqueConstraintConfig();
                        descKey = getDescriptionKey(entityDescKey, "uniqueconstraint",
                                appEntityUniqueConstraint.getName());
                        ctx.addMessage(StaticMessageCategoryType.ENTITY, descKey,
                                appEntityUniqueConstraint.getDescription());
                        entityUniqueConstraintConfig.setName(appEntityUniqueConstraint.getName());
                        entityUniqueConstraintConfig.setDescription("$m{" + descKey + "}");
                        entityUniqueConstraintConfig.setFieldList(appEntityUniqueConstraint.getFieldList());
                        if (!DataUtils.isBlank(appEntityUniqueConstraint.getConditionList())) {
                            List<EntityUniqueConditionConfig> conditionConfigList = new ArrayList<EntityUniqueConditionConfig>();
                            for (AppEntityUniqueCondition entityUniqueCondition : appEntityUniqueConstraint
                                    .getConditionList()) {
                                EntityUniqueConditionConfig entityUniqueConditionConfig = new EntityUniqueConditionConfig();
                                entityUniqueConditionConfig.setField(entityUniqueCondition.getField());
                                entityUniqueConditionConfig.setValue(entityUniqueCondition.getValue());
                                conditionConfigList.add(entityUniqueConditionConfig);
                            }

                            entityUniqueConstraintConfig.setConditionList(conditionConfigList);
                        }

                        uniqueConstraintList.add(entityUniqueConstraintConfig);
                    }

                    appEntityConfig.setUniqueConstraintList(uniqueConstraintList);
                }

                // Indexes
                if (!DataUtils.isBlank(appEntity.getIndexList())) {
                    List<EntityIndexConfig> indexList = new ArrayList<EntityIndexConfig>();
                    for (AppEntityIndex appEntityIndex : appEntity.getIndexList()) {
                        EntityIndexConfig entityIndexConfig = new EntityIndexConfig();
                        descKey = getDescriptionKey(entityDescKey, "index", appEntityIndex.getName());
                        ctx.addMessage(StaticMessageCategoryType.ENTITY, descKey, appEntityIndex.getDescription());
                        entityIndexConfig.setName(appEntityIndex.getName());
                        entityIndexConfig.setDescription("$m{" + descKey + "}");
                        entityIndexConfig.setFieldList(appEntityIndex.getFieldList());
                        indexList.add(entityIndexConfig);
                    }

                    appEntityConfig.setIndexList(indexList);
                }

                // Upload configuration
                if (!DataUtils.isBlank(appEntity.getUploadList())) {
                    List<EntityUploadConfig> uploadList = new ArrayList<EntityUploadConfig>();
                    for (AppEntityUpload appEntityUpload : appEntity.getUploadList()) {
                        EntityUploadConfig entityUploadConfig = new EntityUploadConfig();
                        descKey = getDescriptionKey(entityDescKey, "upload", appEntityUpload.getName());
                        ctx.addMessage(StaticMessageCategoryType.ENTITY, descKey, appEntityUpload.getDescription());
                        entityUploadConfig.setName(appEntityUpload.getName());
                        entityUploadConfig.setDescription("$m{" + descKey + "}");
                        entityUploadConfig.setConstraintAction(appEntityUpload.getConstraintAction());
                        entityUploadConfig.setFieldSequence( 
                                InputWidgetUtils.getFieldSequenceConfig(appEntityUpload.getFieldSequence()));
                        uploadList.add(entityUploadConfig);
                    }

                    appEntityConfig.setUploadList(uploadList);
                }

                // Search Inputs
                if (!DataUtils.isBlank(appEntity.getSearchInputList())) {
                    List<EntitySearchInputConfig> searchInputList = new ArrayList<EntitySearchInputConfig>();
                    for (AppEntitySearchInput appEntitySearchInput : appEntity.getSearchInputList()) {
                        EntitySearchInputConfig entitySearchInputConfig = InputWidgetUtils
                                .getEntitySearchInputConfig(appEntitySearchInput);
                        String searchInputKey = getDescriptionKey(descKey, "entitysearchinput",
                                appEntitySearchInput.getName());
                        ctx.addMessage(StaticMessageCategoryType.ENTITY, searchInputKey,
                                appEntitySearchInput.getDescription());
                        entitySearchInputConfig.setName(appEntitySearchInput.getName());
                        entitySearchInputConfig.setDescription("$m{" + searchInputKey + "}");
                        entitySearchInputConfig.setRestrictionResolver(appEntitySearchInput.getRestrictionResolver());
                        searchInputList.add(entitySearchInputConfig);
                    }

                    appEntityConfig.setSearchInputList(searchInputList);
                }

                entityList.add(appEntityConfig);
                ctx.addMessageGap(StaticMessageCategoryType.ENTITY);
            }

            entitiesConfig.setEntityList(entityList);
            appConfig.setEntitiesConfig(entitiesConfig);
        }

        // Tables
        List<Long> tableIdList = applicationModuleService.findCustomAppComponentIdList(applicationName, AppTable.class);
        if (!DataUtils.isBlank(tableIdList)) {
            AppTablesConfig tablesConfig = new AppTablesConfig();
            List<AppTableConfig> tableConfigList = new ArrayList<AppTableConfig>();
            for (Long tableId : tableIdList) {
                AppTableConfig appTableConfig = new AppTableConfig();
                AppTable appTable = applicationModuleService.findAppTable(tableId);
                descKey = getDescriptionKey(lowerCaseApplicationName, "table", appTable.getName());
                labelKey = descKey + ".label";
                final String tableDescKey = descKey;
                ctx.addMessage(StaticMessageCategoryType.TABLE, descKey, appTable.getDescription());
                ctx.addMessage(StaticMessageCategoryType.TABLE, labelKey, appTable.getLabel());
                appTableConfig.setName(appTable.getName());
                appTableConfig.setDescription("$m{" + descKey + "}");
                appTableConfig.setLabel("$m{" + labelKey + "}");
                appTableConfig.setEntity(appTable.getEntity());
                appTableConfig.setDetailsPanelName(appTable.getDetailsPanelName());
                appTableConfig.setLoadingFilterGen(appTable.getLoadingFilterGen());
                appTableConfig.setLoadingSearchInput(appTable.getLoadingSearchInput());
                appTableConfig.setSortHistory(appTable.getSortHistory());
                appTableConfig.setItemsPerPage(appTable.getItemsPerPage());
                appTableConfig.setSummaryTitleColumns(appTable.getSummaryTitleColumns());
                appTableConfig.setSerialNo(appTable.isSerialNo());
                appTableConfig.setSortable(appTable.isSortable());
                appTableConfig.setShowLabelHeader(appTable.isShowLabelHeader());
                appTableConfig.setHeaderToUpperCase(appTable.isHeaderToUpperCase());
                appTableConfig.setHeaderCenterAlign(appTable.isHeaderCenterAlign());
                appTableConfig.setBasicSearch(appTable.isBasicSearch());
                appTableConfig.setTotalSummary(appTable.isTotalSummary());
                appTableConfig.setHeaderless(appTable.isHeaderless());
                appTableConfig.setMultiSelect(appTable.isMultiSelect());
                appTableConfig.setNonConforming(appTable.isNonConforming());
                appTableConfig.setFixedRows(appTable.isFixedRows());
                appTableConfig.setLimitSelectToColumns(appTable.isLimitSelectToColumns());

                // Columns
                if (!DataUtils.isBlank(appTable.getColumnList())) {
                    List<TableColumnConfig> columnList = new ArrayList<TableColumnConfig>();
                    for (AppTableColumn appTableColumn : appTable.getColumnList()) {
                        TableColumnConfig tableColumnConfig = new TableColumnConfig();
                        tableColumnConfig.setField(appTableColumn.getField());
                        if (appTableColumn.getLabel() != null) {
                            labelKey = getDescriptionKey(tableDescKey, "column.label", appTableColumn.getLabel());
                            ctx.addMessage(StaticMessageCategoryType.TABLE, labelKey, appTableColumn.getLabel());
                            tableColumnConfig.setLabel("$m{" + labelKey + "}");
                        }

                        tableColumnConfig.setRenderWidget(appTableColumn.getRenderWidget());
                        tableColumnConfig.setLinkAct(appTableColumn.getLinkAct());
                        tableColumnConfig.setSymbol(appTableColumn.getSymbol());
                        tableColumnConfig.setOrder(appTableColumn.getOrder());
                        tableColumnConfig.setWidthRatio(appTableColumn.getWidthRatio());
                        tableColumnConfig.setSwitchOnChange(appTableColumn.isSwitchOnChange());
                        tableColumnConfig.setHiddenOnNull(appTableColumn.isHiddenOnNull());
                        tableColumnConfig.setHidden(appTableColumn.isHidden());
                        tableColumnConfig.setDisabled(appTableColumn.isDisabled());
                        tableColumnConfig.setEditable(appTableColumn.isEditable());
                        tableColumnConfig.setSortable(appTableColumn.isSortable());
                        tableColumnConfig.setSummary(appTableColumn.isSummary());
                        columnList.add(tableColumnConfig);
                    }

                    appTableConfig.setColumnList(columnList);
                }

                // Filters
                if (!DataUtils.isBlank(appTable.getFilterList())) {
                    List<TableFilterConfig> filterList = new ArrayList<TableFilterConfig>();
                    for (AppTableFilter appTableFilter : appTable.getFilterList()) {
                        TableFilterConfig tableFilterConfig = InputWidgetUtils.getFilterConfig(au(), appTableFilter);
                        String filterKey = getDescriptionKey(descKey, "tablefilter", appTableFilter.getName());
                        ctx.addMessage(StaticMessageCategoryType.TABLE, filterKey, appTableFilter.getDescription());
                        tableFilterConfig.setName(appTableFilter.getName());
                        tableFilterConfig.setDescription("$m{" + filterKey + "}");
                        filterList.add(tableFilterConfig);
                    }

                    appTableConfig.setFilterList(filterList);
                }

                // Actions
                if (!DataUtils.isBlank(appTable.getActionList())) {
                    List<TableActionConfig> actionList = new ArrayList<TableActionConfig>();
                    for (AppTableAction appTableAction : appTable.getActionList()) {
                        TableActionConfig tableActionConfig = new TableActionConfig();
                        descKey = getDescriptionKey(tableDescKey, "action", appTableAction.getName());
                        labelKey = getDescriptionKey(tableDescKey, "action.label", appTableAction.getLabel());
                        ctx.addMessage(StaticMessageCategoryType.TABLE, descKey, appTableAction.getDescription());
                        ctx.addMessage(StaticMessageCategoryType.TABLE, labelKey, appTableAction.getLabel());
                        tableActionConfig.setName(appTableAction.getName());
                        tableActionConfig.setDescription("$m{" + descKey + "}");
                        tableActionConfig.setLabel("$m{" + labelKey + "}");
                        tableActionConfig.setPolicy(appTableAction.getPolicy());
                        tableActionConfig.setOrderIndex(appTableAction.getOrderIndex());
                        actionList.add(tableActionConfig);
                    }

                    appTableConfig.setActionList(actionList);
                }

                // Loading
                if (!DataUtils.isBlank(appTable.getLoadingList())) {
                    List<TableLoadingConfig> loadingList = new ArrayList<TableLoadingConfig>();
                    for (AppTableLoading appTableLoading : appTable.getLoadingList()) {
                        TableLoadingConfig tableLoadingConfig = new TableLoadingConfig();
                        descKey = getDescriptionKey(tableDescKey, "loading", appTableLoading.getName());
                        labelKey = getDescriptionKey(tableDescKey, "loading.label", appTableLoading.getLabel());
                        ctx.addMessage(StaticMessageCategoryType.TABLE, descKey, appTableLoading.getDescription());
                        ctx.addMessage(StaticMessageCategoryType.TABLE, labelKey, appTableLoading.getLabel());
                        tableLoadingConfig.setName(appTableLoading.getName());
                        tableLoadingConfig.setDescription("$m{" + descKey + "}");
                        tableLoadingConfig.setLabel("$m{" + labelKey + "}");
                        tableLoadingConfig.setProvider(appTableLoading.getProvider());
                        tableLoadingConfig.setOrderIndex(appTableLoading.getOrderIndex());
                        loadingList.add(tableLoadingConfig);
                    }

                    appTableConfig.setLoadingList(loadingList);
                }

                tableConfigList.add(appTableConfig);
                ctx.addMessageGap(StaticMessageCategoryType.TABLE);
            }

            tablesConfig.setTableList(tableConfigList);
            appConfig.setTablesConfig(tablesConfig);
        }

        // Forms
        List<Long> formIdList = applicationModuleService.findCustomAppComponentIdList(applicationName, AppForm.class);
        if (!DataUtils.isBlank(formIdList)) {
            AppFormsConfig formsConfig = new AppFormsConfig();
            List<AppFormConfig> formConfigList = new ArrayList<AppFormConfig>();
            for (Long formId : formIdList) {
                AppFormConfig appFormConfig = new AppFormConfig();
                AppForm appForm = applicationModuleService.findAppForm(formId);
                descKey = getDescriptionKey(lowerCaseApplicationName, "form", appForm.getName());
                final String formDescKey = descKey;
                ctx.addMessage(StaticMessageCategoryType.FORM, descKey, appForm.getDescription());
                appFormConfig.setType(appForm.getType());
                appFormConfig.setName(appForm.getName());
                appFormConfig.setDescription("$m{" + descKey + "}");
                appFormConfig.setEntity(appForm.getEntity());
                appFormConfig.setConsolidatedReview(appForm.getConsolidatedReview());
                appFormConfig.setConsolidatedValidation(appForm.getConsolidatedValidation());
                appFormConfig.setConsolidatedState(appForm.getConsolidatedState());
                appFormConfig.setListingGenerator(appForm.getListingGenerator());
                appFormConfig.setTitleFormat(appForm.getTitleFormat());

                // Filters
                if (!DataUtils.isBlank(appForm.getFilterList())) {
                    List<FormFilterConfig> filterList = new ArrayList<FormFilterConfig>();
                    for (AppFormFilter appFormFilter : appForm.getFilterList()) {
                        FormFilterConfig filterConfig = InputWidgetUtils.getFilterConfig(au(), appFormFilter);
                        descKey = getDescriptionKey(formDescKey, "formfilter", appFormFilter.getName());
                        ctx.addMessage(StaticMessageCategoryType.FORM, descKey, appFormFilter.getDescription());
                        filterConfig.setName(appFormFilter.getName());
                        filterConfig.setDescription("$m{" + descKey + "}");
                        filterList.add(filterConfig);
                    }

                    appFormConfig.setFilterList(filterList);
                }

                // Annotations
                if (!DataUtils.isBlank(appForm.getAnnotationList())) {
                    List<FormAnnotationConfig> annotationConfigList = new ArrayList<FormAnnotationConfig>();
                    for (AppFormAnnotation appFormAnnotation : appForm.getAnnotationList()) {
                        FormAnnotationConfig formAnnotationConfig = new FormAnnotationConfig();
                        descKey = getDescriptionKey(formDescKey, "annotation", appFormAnnotation.getName());
                        String msgKey = getDescriptionKey(formDescKey, "annotation.message",
                                appFormAnnotation.getMessage());
                        ctx.addMessage(StaticMessageCategoryType.FORM, descKey, appFormAnnotation.getDescription());
                        ctx.addMessage(StaticMessageCategoryType.FORM, msgKey, appFormAnnotation.getMessage());
                        formAnnotationConfig.setType(appFormAnnotation.getType());
                        formAnnotationConfig.setName(appFormAnnotation.getName());
                        formAnnotationConfig.setDescription("$m{" + descKey + "}");
                        formAnnotationConfig.setMessage("$m{" + msgKey + "}");
                        formAnnotationConfig.setHtml(appFormAnnotation.isHtml());
                        annotationConfigList.add(formAnnotationConfig);
                    }

                    appFormConfig.setAnnotationList(annotationConfigList);
                }

                // Form actions
                if (!DataUtils.isBlank(appForm.getActionList())) {
                    List<FormActionConfig> actionConfigList = new ArrayList<FormActionConfig>();
                    for (AppFormAction appFormAction : appForm.getActionList()) {
                        FormActionConfig formActionConfig = new FormActionConfig();
                        descKey = getDescriptionKey(formDescKey, "action", appFormAction.getName());
                        labelKey = getDescriptionKey(formDescKey, "action.label", appFormAction.getLabel());
                        ctx.addMessage(StaticMessageCategoryType.FORM, descKey, appFormAction.getDescription());
                        ctx.addMessage(StaticMessageCategoryType.FORM, labelKey, appFormAction.getLabel());
                        formActionConfig.setName(appFormAction.getName());
                        formActionConfig.setDescription("$m{" + descKey + "}");
                        formActionConfig.setLabel("$m{" + labelKey + "}");
                        formActionConfig.setSymbol(appFormAction.getSymbol());
                        formActionConfig.setStyleClass(appFormAction.getStyleClass());
                        formActionConfig.setPolicy(appFormAction.getPolicy());
                        formActionConfig.setShowOnCreate(appFormAction.isShowOnCreate());
                        formActionConfig.setShowOnMaintain(appFormAction.isShowOnMaintain());
                        formActionConfig.setValidateForm(appFormAction.isValidateForm());
                        formActionConfig.setOrderIndex(appFormAction.getOrderIndex());
                        formActionConfig.setType(appFormAction.getType());
                        formActionConfig.setHighlightType(appFormAction.getHighlightType());
                        formActionConfig
                                .setOnCondition(InputWidgetUtils.getFilterConfig(au(), appFormAction.getOnCondition()));
                        actionConfigList.add(formActionConfig);
                    }

                    appFormConfig.setActionList(actionConfigList);
                }

                // Form elements
                if (!DataUtils.isBlank(appForm.getElementList())) {
                    List<FormTabConfig> tabConfigList = new ArrayList<FormTabConfig>();
                    List<AppFormElement> elementList = appForm.getElementList();
                    final int len = elementList.size();
                    for (int i = 0; i < len;) {
                        FormTabConfig formTabConfig = new FormTabConfig();
                        AppFormElement appFormElement = elementList.get(i);
                        formTabConfig.setName(appFormElement.getElementName());
                        formTabConfig.setContentType(appFormElement.getTabContentType());
                        if (appFormElement.getLabel() != null) {
                            labelKey = getDescriptionKey(formDescKey, "tab.label", appFormElement.getLabel());
                            ctx.addMessage(StaticMessageCategoryType.FORM, labelKey, appFormElement.getLabel());
                            formTabConfig.setLabel("$m{" + labelKey + "}");
                        }

                        formTabConfig.setApplet(appFormElement.getTabApplet());
                        formTabConfig.setReference(appFormElement.getTabReference());
                        formTabConfig.setFilter(appFormElement.getFilter());
                        formTabConfig.setEditAction(appFormElement.getEditAction());
                        formTabConfig.setEditFormless(appFormElement.getEditFormless());
                        formTabConfig.setEditFixedRows(appFormElement.getEditFixedRows());
                        formTabConfig.setEditAllowAddition(appFormElement.getEditAllowAddition());
                        formTabConfig.setIgnoreParentCondition(appFormElement.isIgnoreParentCondition());
                        formTabConfig.setShowSearch(appFormElement.isShowSearch());
                        formTabConfig.setQuickEdit(appFormElement.isQuickEdit());
                        formTabConfig.setQuickOrder(appFormElement.isQuickOrder());
                        formTabConfig.setVisible(appFormElement.isVisible());
                        formTabConfig.setEditable(appFormElement.isEditable());
                        formTabConfig.setDisabled(appFormElement.isDisabled());
                        formTabConfig.setMappedForm(appFormElement.getTabMappedForm());
                        formTabConfig.setMappedFieldName(appFormElement.getMappedFieldName());

                        final boolean isChangeLog = TabContentType.MINIFORM_CHANGELOG
                                .equals(appFormElement.getTabContentType());
                        List<FormSectionConfig> sectionConfigList = new ArrayList<FormSectionConfig>();
                        for (i++; i < len;) {
                            appFormElement = elementList.get(i);
                            if (FormElementType.SECTION.equals(appFormElement.getType())) {
                                FormSectionConfig formSectionConfig = new FormSectionConfig();
                                formSectionConfig.setName(appFormElement.getElementName());
                                formSectionConfig.setColumns(appFormElement.getSectionColumns());
                                if (appFormElement.getLabel() != null) {
                                    labelKey = getDescriptionKey(formDescKey, "section.label",
                                            appFormElement.getLabel());
                                    ctx.addMessage(StaticMessageCategoryType.FORM, labelKey, appFormElement.getLabel());
                                    formSectionConfig.setLabel("$m{" + labelKey + "}");
                                }

                                formSectionConfig.setVisible(appFormElement.isVisible());
                                formSectionConfig.setEditable(appFormElement.isEditable());
                                formSectionConfig.setDisabled(appFormElement.isDisabled());

                                List<FormFieldConfig> fieldConfigList = new ArrayList<FormFieldConfig>();
                                for (i++; i < len; i++) {
                                    appFormElement = elementList.get(i);
                                    if (FormElementType.FIELD.equals(appFormElement.getType())) {
                                        FormFieldConfig formFieldConfig = new FormFieldConfig();
                                        formFieldConfig.setName(appFormElement.getElementName());
                                        if (appFormElement.getLabel() != null) {
                                            labelKey = getDescriptionKey(formDescKey, "field.label",
                                                    appFormElement.getLabel());
                                            ctx.addMessage(StaticMessageCategoryType.FORM, labelKey,
                                                    appFormElement.getLabel());
                                            formFieldConfig.setLabel("$m{" + labelKey + "}");
                                        }

                                        formFieldConfig.setInputWidget(appFormElement.getInputWidget());
                                        formFieldConfig.setReference(appFormElement.getInputReference());
                                        formFieldConfig.setColumn(appFormElement.getFieldColumn());
                                        formFieldConfig.setSwitchOnChange(appFormElement.isSwitchOnChange());
                                        formFieldConfig.setSaveAs(appFormElement.isSaveAs());
                                        formFieldConfig.setRequired(appFormElement.isRequired());
                                        formFieldConfig.setVisible(appFormElement.isVisible());
                                        formFieldConfig.setColor(appFormElement.getColor());
                                        formFieldConfig.setEditable(appFormElement.isEditable());
                                        formFieldConfig.setDisabled(appFormElement.isDisabled());
                                        fieldConfigList.add(formFieldConfig);
                                    } else {
                                        break;
                                    }
                                }

                                formSectionConfig.setFieldList(fieldConfigList);
                                sectionConfigList.add(formSectionConfig);
                            } else {
                                break;
                            }
                        }

                        if (!isChangeLog) {
                            formTabConfig.setSectionList(sectionConfigList);
                        }

                        tabConfigList.add(formTabConfig);
                    }

                    appFormConfig.setTabList(tabConfigList);
                }

                // Related Lists
                if (!DataUtils.isBlank(appForm.getRelatedList())) {
                    List<RelatedListConfig> relatedConfigList = new ArrayList<RelatedListConfig>();
                    for (AppFormRelatedList appFormRelatedList : appForm.getRelatedList()) {
                        RelatedListConfig relatedListConfig = new RelatedListConfig();
                        relatedListConfig.setName(appFormRelatedList.getName());
                        descKey = getDescriptionKey(formDescKey, "relatedlist", appFormRelatedList.getName());
                        labelKey = getDescriptionKey(formDescKey, "relatedlist.label", appFormRelatedList.getLabel());
                        ctx.addMessage(StaticMessageCategoryType.FORM, descKey, appFormRelatedList.getDescription());
                        ctx.addMessage(StaticMessageCategoryType.FORM, labelKey, appFormRelatedList.getLabel());
                        relatedListConfig.setDescription("$m{" + descKey + "}");
                        relatedListConfig.setLabel("$m{" + labelKey + "}");
                        relatedListConfig.setApplet(appFormRelatedList.getApplet());
                        relatedListConfig.setFilter(appFormRelatedList.getFilter());
                        relatedListConfig.setEditAction(appFormRelatedList.getEditAction());
                        relatedConfigList.add(relatedListConfig);
                    }

                    appFormConfig.setRelatedList(relatedConfigList);
                }

                // Form State Policies
                if (!DataUtils.isBlank(appForm.getFieldStateList())) {
                    List<FormStatePolicyConfig> formStatePolicyConfigList = new ArrayList<FormStatePolicyConfig>();
                    for (AppFormStatePolicy appFormStatePolicy : appForm.getFieldStateList()) {
                        FormStatePolicyConfig formStatePolicyConfig = new FormStatePolicyConfig();
                        formStatePolicyConfig.setName(appFormStatePolicy.getName());
                        descKey = getDescriptionKey(formDescKey, "statepolicy", appFormStatePolicy.getDescription());
                        ctx.addMessage(StaticMessageCategoryType.FORM, descKey, appFormStatePolicy.getDescription());
                        formStatePolicyConfig.setDescription("$m{" + descKey + "}");
                        formStatePolicyConfig.setValueGenerator(appFormStatePolicy.getValueGenerator());
                        formStatePolicyConfig.setTrigger(appFormStatePolicy.getTrigger());
                        formStatePolicyConfig.setExecutionIndex(appFormStatePolicy.getExecutionIndex());
                        formStatePolicyConfig.setType(appFormStatePolicy.getType());
                        formStatePolicyConfig.setOnCondition(
                                InputWidgetUtils.getFilterConfig(au(), appFormStatePolicy.getOnCondition()));
                        formStatePolicyConfig.setSetValues(InputWidgetUtils.getSetValuesConfig(
                                appFormStatePolicy.getValueGenerator(), appFormStatePolicy.getSetValues()));

                        if (!DataUtils.isBlank(appFormStatePolicy.getSetStateList())) {
                            SetStatesConfig setStates = new SetStatesConfig();
                            List<SetStateConfig> setStateConfigList = new ArrayList<SetStateConfig>();
                            for (AppFormSetState appFormSetState : appFormStatePolicy.getSetStateList()) {
                                SetStateConfig setStateConfig = new SetStateConfig();
                                setStateConfig.setType(appFormSetState.getType());
                                setStateConfig.setTarget(appFormSetState.getTarget());
                                setStateConfig.setDisabled(appFormSetState.getDisabled());
                                setStateConfig.setEditable(appFormSetState.getEditable());
                                setStateConfig.setRequired(appFormSetState.getRequired());
                                setStateConfig.setVisible(appFormSetState.getVisible());
                                setStateConfigList.add(setStateConfig);
                            }

                            setStates.setSetStateList(setStateConfigList);
                            formStatePolicyConfig.setSetStates(setStates);
                        }

                        formStatePolicyConfigList.add(formStatePolicyConfig);
                    }

                    appFormConfig.setFormStatePolicyList(formStatePolicyConfigList);
                }

                // Form widget rule policies
                if (!DataUtils.isBlank(appForm.getFieldStateList())) {
                    List<FormWidgetRulesPolicyConfig> widgetRulesPolicyList = new ArrayList<FormWidgetRulesPolicyConfig>();
                    for (AppFormWidgetRulesPolicy appFormWidgetRulesPolicy : appForm.getWidgetRulesList()) {
                        FormWidgetRulesPolicyConfig formWidgetRulesPolicyConfig = new FormWidgetRulesPolicyConfig();
                        formWidgetRulesPolicyConfig.setName(appFormWidgetRulesPolicy.getName());
                        descKey = getDescriptionKey(formDescKey, "widgetrulespolicy",
                                appFormWidgetRulesPolicy.getDescription());
                        ctx.addMessage(StaticMessageCategoryType.FORM, descKey,
                                appFormWidgetRulesPolicy.getDescription());
                        formWidgetRulesPolicyConfig.setDescription("$m{" + descKey + "}");
                        formWidgetRulesPolicyConfig.setExecutionIndex(appFormWidgetRulesPolicy.getExecutionIndex());
                        formWidgetRulesPolicyConfig.setOnCondition(
                                InputWidgetUtils.getFilterConfig(au(), appFormWidgetRulesPolicy.getOnCondition()));
                        formWidgetRulesPolicyConfig.setWidgetRules(
                                InputWidgetUtils.getWidgetRulesConfig(appFormWidgetRulesPolicy.getWidgetRules()));
                        widgetRulesPolicyList.add(formWidgetRulesPolicyConfig);
                    }

                    appFormConfig.setWidgetRulesPolicyList(widgetRulesPolicyList);
                }

                // Form field validation policies
                if (!DataUtils.isBlank(appForm.getFieldValidationList())) {
                    List<FieldValidationPolicyConfig> fieldValidationPolicyConfigList = new ArrayList<FieldValidationPolicyConfig>();
                    for (AppFormFieldValidationPolicy appFormFieldValidationPolicy : appForm.getFieldValidationList()) {
                        FieldValidationPolicyConfig fieldValidationPolicyConfig = new FieldValidationPolicyConfig();
                        fieldValidationPolicyConfig.setName(appFormFieldValidationPolicy.getName());
                        descKey = getDescriptionKey(formDescKey, "fieldvalidationpolicy",
                                appFormFieldValidationPolicy.getName());
                        ctx.addMessage(StaticMessageCategoryType.FORM, descKey,
                                appFormFieldValidationPolicy.getDescription());
                        fieldValidationPolicyConfig.setDescription("$m{" + descKey + "}");
                        fieldValidationPolicyConfig.setFieldName(appFormFieldValidationPolicy.getFieldName());
                        fieldValidationPolicyConfig.setValidator(appFormFieldValidationPolicy.getValidation());
                        fieldValidationPolicyConfig.setRule(appFormFieldValidationPolicy.getRule());
                        fieldValidationPolicyConfig.setExecutionIndex(appFormFieldValidationPolicy.getExecutionIndex());
                        fieldValidationPolicyConfigList.add(fieldValidationPolicyConfig);
                    }

                    appFormConfig.setFieldValidationPolicyList(fieldValidationPolicyConfigList);
                }

                // Form validation policies
                if (!DataUtils.isBlank(appForm.getFormValidationList())) {
                    List<FormValidationPolicyConfig> formValidationPolicyList = new ArrayList<FormValidationPolicyConfig>();
                    for (AppFormValidationPolicy appFormValidationPolicy : appForm.getFormValidationList()) {
                        FormValidationPolicyConfig formValidationPolicyConfig = new FormValidationPolicyConfig();
                        formValidationPolicyConfig.setName(appFormValidationPolicy.getName());
                        descKey = getDescriptionKey(formDescKey, "validationpolicy", appFormValidationPolicy.getName());
                        String msgKey = descKey + ".message";
                        ctx.addMessage(StaticMessageCategoryType.FORM, descKey,
                                appFormValidationPolicy.getDescription());
                        ctx.addMessage(StaticMessageCategoryType.FORM, msgKey, appFormValidationPolicy.getMessage());
                        formValidationPolicyConfig.setDescription("$m{" + descKey + "}");
                        formValidationPolicyConfig.setMessage("$m{" + msgKey + "}");
                        formValidationPolicyConfig.setTarget(appFormValidationPolicy.getTarget());
                        formValidationPolicyConfig.setExecutionIndex(appFormValidationPolicy.getExecutionIndex());
                        formValidationPolicyConfig.setErrorMatcher(appFormValidationPolicy.getErrorMatcher());
                        formValidationPolicyConfig.setErrorCondition(
                                InputWidgetUtils.getFilterConfig(au(), appFormValidationPolicy.getErrorCondition()));
                        formValidationPolicyList.add(formValidationPolicyConfig);
                    }

                    appFormConfig.setFormValidationPolicyList(formValidationPolicyList);
                }

                // Form review policies
                if (!DataUtils.isBlank(appForm.getFormReviewList())) {
                    List<FormReviewPolicyConfig> formReviewPolicyList = new ArrayList<FormReviewPolicyConfig>();
                    for (AppFormReviewPolicy appFormReviewPolicy : appForm.getFormReviewList()) {
                        FormReviewPolicyConfig formReviewPolicyConfig = new FormReviewPolicyConfig();
                        formReviewPolicyConfig.setName(appFormReviewPolicy.getName());
                        descKey = getDescriptionKey(formDescKey, "reviewpolicy", appFormReviewPolicy.getName());
                        String msgKey = descKey + ".message";
                        ctx.addMessage(StaticMessageCategoryType.FORM, descKey, appFormReviewPolicy.getDescription());
                        ctx.addMessage(StaticMessageCategoryType.FORM, msgKey, appFormReviewPolicy.getMessage());
                        formReviewPolicyConfig.setDescription("$m{" + descKey + "}");
                        formReviewPolicyConfig.setMessage("$m{" + msgKey + "}");
                        formReviewPolicyConfig.setMessageType(appFormReviewPolicy.getMessageType());
                        formReviewPolicyConfig.setEvents(appFormReviewPolicy.getFormEvents());
                        formReviewPolicyConfig.setTarget(appFormReviewPolicy.getTarget());
                        formReviewPolicyConfig.setSkippable(appFormReviewPolicy.isSkippable());
                        formReviewPolicyConfig.setExecutionIndex(appFormReviewPolicy.getExecutionIndex());
                        formReviewPolicyConfig.setErrorMatcher(appFormReviewPolicy.getErrorMatcher());
                        formReviewPolicyConfig.setErrorCondition(
                                InputWidgetUtils.getFilterConfig(au(), appFormReviewPolicy.getErrorCondition()));
                        formReviewPolicyList.add(formReviewPolicyConfig);
                    }

                    appFormConfig.setFormReviewPolicyList(formReviewPolicyList);
                }

                formConfigList.add(appFormConfig);
                ctx.addMessageGap(StaticMessageCategoryType.FORM);
            }

            formsConfig.setFormList(formConfigList);
            appConfig.setFormsConfig(formsConfig);
        }

        // Property lists
        List<Long> propertyListIdList = applicationModuleService.findCustomAppComponentIdList(applicationName,
                AppPropertyList.class);
        if (!DataUtils.isBlank(propertyListIdList)) {
            PropertyListsConfig propertyListsConfig = new PropertyListsConfig();
            List<PropertyListConfig> propertyConfigList = new ArrayList<PropertyListConfig>();
            for (Long appPropertyId : propertyListIdList) {
                PropertyListConfig propertyListConfig = new PropertyListConfig();
                AppPropertyList appPropertyList = applicationModuleService.findAppPropertyList(appPropertyId);
                descKey = getDescriptionKey(lowerCaseApplicationName, "propertylist", appPropertyList.getName());
                final String propertyDescKey = descKey;
                ctx.addMessage(StaticMessageCategoryType.PROPERTY_LIST, descKey, appPropertyList.getDescription());
                propertyListConfig.setName(appPropertyList.getName());
                propertyListConfig.setDescription("$m{" + descKey + "}");

                // Property sets
                if (!DataUtils.isBlank(appPropertyList.getItemSet())) {
                    List<PropertySetConfig> propSetList = new ArrayList<PropertySetConfig>();
                    for (AppPropertySet appPropertySet : appPropertyList.getItemSet()) {
                        PropertySetConfig propertySetConfig = new PropertySetConfig();
                        labelKey = getDescriptionKey(propertyDescKey, "set.label", appPropertySet.getLabel());
                        ctx.addMessage(StaticMessageCategoryType.PROPERTY_LIST, labelKey, appPropertySet.getLabel());
                        propertySetConfig.setLabel("$m{" + labelKey + "}");

                        // Property items
                        if (!DataUtils.isBlank(appPropertySet.getItemList())) {
                            List<PropertyListPropConfig> propList = new ArrayList<PropertyListPropConfig>();
                            for (AppPropertyListItem appPropertyListItem : appPropertySet.getItemList()) {
                                PropertyListPropConfig propertyListPropConfig = new PropertyListPropConfig();
                                descKey = getDescriptionKey(propertyDescKey, "item",
                                        appPropertyListItem.getDescription());
                                ctx.addMessage(StaticMessageCategoryType.PROPERTY_LIST, descKey,
                                        appPropertyListItem.getDescription());
                                propertyListPropConfig.setName(appPropertyListItem.getName());
                                propertyListPropConfig.setDescription("$m{" + descKey + "}");
                                propertyListPropConfig.setInputWidget(appPropertyListItem.getInputWidget());
                                propertyListPropConfig.setReferences(appPropertyListItem.getReferences());
                                propertyListPropConfig.setDefaultVal(appPropertyListItem.getDefaultVal());
                                propertyListPropConfig.setRequired(appPropertyListItem.isRequired());
                                propertyListPropConfig.setMask(appPropertyListItem.isMask());
                                propertyListPropConfig.setEncrypt(appPropertyListItem.isEncrypt());
                                propList.add(propertyListPropConfig);
                            }

                            propertySetConfig.setPropList(propList);
                        }

                        propSetList.add(propertySetConfig);
                    }

                    propertyListConfig.setPropSetList(propSetList);
                }

                propertyConfigList.add(propertyListConfig);
                ctx.addMessageGap(StaticMessageCategoryType.PROPERTY_LIST);
            }

            propertyListsConfig.setPropertyConfigList(propertyConfigList);
            appConfig.setPropertyListsConfig(propertyListsConfig);
        }

        // Property rules
        List<Long> propertyRuleIdRule = applicationModuleService.findCustomAppComponentIdList(applicationName,
                AppPropertyRule.class);
        if (!DataUtils.isBlank(propertyRuleIdRule)) {
            PropertyRulesConfig propertyRulesConfig = new PropertyRulesConfig();
            List<PropertyRuleConfig> propertyConfigRule = new ArrayList<PropertyRuleConfig>();
            for (Long appPropertyId : propertyRuleIdRule) {
                PropertyRuleConfig propertyRuleConfig = new PropertyRuleConfig();
                AppPropertyRule appPropertyRule = applicationModuleService.findAppPropertyRule(appPropertyId);
                descKey = getDescriptionKey(lowerCaseApplicationName, "propertyrule", appPropertyRule.getName());
                ctx.addMessage(StaticMessageCategoryType.PROPERTY_RULE, descKey, appPropertyRule.getDescription());
                propertyRuleConfig.setName(appPropertyRule.getName());
                propertyRuleConfig.setDescription("$m{" + descKey + "}");
                propertyRuleConfig.setEntity(appPropertyRule.getEntity());
                propertyRuleConfig.setChoiceField(appPropertyRule.getChoiceField());
                propertyRuleConfig.setListField(appPropertyRule.getListField());
                propertyRuleConfig.setPropNameField(appPropertyRule.getPropNameField());
                propertyRuleConfig.setPropValField(appPropertyRule.getPropValField());
                propertyRuleConfig.setDefaultList(appPropertyRule.getDefaultList());
                propertyRuleConfig.setIgnoreCase(appPropertyRule.isIgnoreCase());

                // Property choice
                if (!DataUtils.isBlank(appPropertyRule.getChoiceList())) {
                    List<ChoiceConfig> choiceList = new ArrayList<ChoiceConfig>();
                    for (AppPropertyRuleChoice ruleChoice : appPropertyRule.getChoiceList()) {
                        ChoiceConfig choiceConfig = new ChoiceConfig();
                        choiceConfig.setName(ruleChoice.getName());
                        choiceConfig.setVal(ruleChoice.getList());
                        choiceList.add(choiceConfig);
                    }

                    propertyRuleConfig.setChoiceList(choiceList);
                }

                propertyConfigRule.add(propertyRuleConfig);
                ctx.addMessageGap(StaticMessageCategoryType.PROPERTY_RULE);
            }

            propertyRulesConfig.setPropertyRuleConfigList(propertyConfigRule);
            appConfig.setPropertyRulesConfig(propertyRulesConfig);
        }

        // Assignment page
        List<Long> assignPageIdList = applicationModuleService.findCustomAppComponentIdList(applicationName,
                AppAssignmentPage.class);
        if (!DataUtils.isBlank(assignPageIdList)) {
            AppAssignmentPagesConfig assignPagesConfig = new AppAssignmentPagesConfig();
            List<AppAssignmentPageConfig> assignmentPageList = new ArrayList<AppAssignmentPageConfig>();
            for (Long assignPageId : assignPageIdList) {
                AppAssignmentPageConfig appAssignmentPageConfig = new AppAssignmentPageConfig();
                AppAssignmentPage appAssignmentPage = applicationModuleService.findAppAssignmentPage(assignPageId);
                descKey = getDescriptionKey(lowerCaseApplicationName, "assignmentpage", appAssignmentPage.getName());
                labelKey = descKey + ".label";
                ctx.addMessage(StaticMessageCategoryType.ASSIGNMENT_PAGE, descKey, appAssignmentPage.getDescription());
                ctx.addMessage(StaticMessageCategoryType.ASSIGNMENT_PAGE, labelKey, appAssignmentPage.getLabel());
                appAssignmentPageConfig.setName(appAssignmentPage.getName());
                appAssignmentPageConfig.setDescription("$m{" + descKey + "}");
                appAssignmentPageConfig.setLabel("$m{" + labelKey + "}");
                appAssignmentPageConfig.setEntity(appAssignmentPage.getEntity());
                if (appAssignmentPage.getFilterCaption1() != null) {
                    labelKey = descKey + ".filtercaption1";
                    ctx.addMessage(StaticMessageCategoryType.ASSIGNMENT_PAGE, labelKey,
                            appAssignmentPage.getFilterCaption1());
                    appAssignmentPageConfig.setFilterCaption1("$m{" + labelKey + "}");
                }

                if (appAssignmentPage.getFilterCaption2() != null) {
                    labelKey = descKey + ".filtercaption2";
                    ctx.addMessage(StaticMessageCategoryType.ASSIGNMENT_PAGE, labelKey,
                            appAssignmentPage.getFilterCaption2());
                    appAssignmentPageConfig.setFilterCaption2("$m{" + labelKey + "}");
                }

                appAssignmentPageConfig.setFilterList1(appAssignmentPage.getFilterList1());
                appAssignmentPageConfig.setFilterList2(appAssignmentPage.getFilterList2());

                if (appAssignmentPage.getAssignCaption() != null) {
                    labelKey = descKey + ".assigncaption";
                    ctx.addMessage(StaticMessageCategoryType.ASSIGNMENT_PAGE, labelKey,
                            appAssignmentPage.getAssignCaption());
                    appAssignmentPageConfig.setAssignCaption("$m{" + labelKey + "}");
                }

                appAssignmentPageConfig.setAssignList(appAssignmentPage.getAssignList());

                if (appAssignmentPage.getUnassignCaption() != null) {
                    labelKey = descKey + ".unassigncaption";
                    ctx.addMessage(StaticMessageCategoryType.ASSIGNMENT_PAGE, labelKey,
                            appAssignmentPage.getUnassignCaption());
                    appAssignmentPageConfig.setUnassignCaption("$m{" + labelKey + "}");
                }

                appAssignmentPageConfig.setUnassignList(appAssignmentPage.getUnassignList());
                appAssignmentPageConfig.setEntity(appAssignmentPage.getEntity());
                appAssignmentPageConfig.setCommitPolicy(appAssignmentPage.getCommitPolicy());
                appAssignmentPageConfig.setAssignField(appAssignmentPage.getAssignField());
                appAssignmentPageConfig.setBaseField(appAssignmentPage.getBaseField());
                appAssignmentPageConfig.setRuleDescField(appAssignmentPage.getRuleDescField());
                assignmentPageList.add(appAssignmentPageConfig);
                ctx.addMessageGap(StaticMessageCategoryType.ASSIGNMENT_PAGE);
            }

            assignPagesConfig.setAssignmentPageList(assignmentPageList);
            appConfig.setAssignmentPagesConfig(assignPagesConfig);
        }

        // Suggestions
        List<Long> suggestionIdList = applicationModuleService.findCustomAppComponentIdList(applicationName,
                AppSuggestionType.class);
        if (!DataUtils.isBlank(suggestionIdList)) {
            SuggestionTypesConfig suggestionTypesConfig = new SuggestionTypesConfig();
            List<SuggestionTypeConfig> suggestionTypeList = new ArrayList<SuggestionTypeConfig>();
            for (Long suggestionTypeId : suggestionIdList) {
                SuggestionTypeConfig suggestionTypeConfig = new SuggestionTypeConfig();
                AppSuggestionType appSuggestionType = applicationModuleService.findAppSuggestionType(suggestionTypeId);
                descKey = getDescriptionKey(lowerCaseApplicationName, "suggestiontype", appSuggestionType.getName());
                ctx.addMessage(StaticMessageCategoryType.SUGGESTION, descKey, appSuggestionType.getDescription());
                suggestionTypeConfig.setName(appSuggestionType.getName());
                suggestionTypeConfig.setDescription("$m{" + descKey + "}");
                suggestionTypeConfig.setParent(appSuggestionType.getParent());
                suggestionTypeList.add(suggestionTypeConfig);
            }

            suggestionTypesConfig.setSuggestionTypeList(suggestionTypeList);
            appConfig.setSuggestionTypesConfig(suggestionTypesConfig);
        }

        ConfigurationUtils.writeConfig(appConfig, zos);
        closeEntry(zos);
    }
}

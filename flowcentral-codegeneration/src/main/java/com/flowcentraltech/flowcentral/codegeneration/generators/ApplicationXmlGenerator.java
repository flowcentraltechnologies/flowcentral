/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
import com.flowcentraltech.flowcentral.application.entities.AppAPI;
import com.flowcentraltech.flowcentral.application.entities.AppApplet;
import com.flowcentraltech.flowcentral.application.entities.AppAppletAlert;
import com.flowcentraltech.flowcentral.application.entities.AppAppletFilter;
import com.flowcentraltech.flowcentral.application.entities.AppAppletProp;
import com.flowcentraltech.flowcentral.application.entities.AppAppletRouteToApplet;
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
import com.flowcentraltech.flowcentral.application.entities.AppEnumeration;
import com.flowcentraltech.flowcentral.application.entities.AppEnumerationItem;
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
import com.flowcentraltech.flowcentral.application.util.ApplicationCodeGenUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldType;
import com.flowcentraltech.flowcentral.configuration.constants.FormElementType;
import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.flowcentraltech.flowcentral.configuration.xml.APIConfig;
import com.flowcentraltech.flowcentral.configuration.xml.APIsConfig;
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
import com.flowcentraltech.flowcentral.configuration.xml.AppletAlertsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletFilterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletFiltersConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletPropConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletPropsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletRouteToAppletConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletRouteToAppletsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletSetValuesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletValuesSetConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ChoiceConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityAttachmentConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityAttachmentsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityCategoriesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityCategoryConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityExpressionConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityExpressionsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityFieldConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityFieldsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityIndexConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityIndexesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntitySearchInputConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntitySearchInputsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntitySeriesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntitySeriesSetConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityUniqueConditionConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityUniqueConstraintConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityUniqueConstraintsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityUploadConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityUploadsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EnumerationConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EnumerationItemConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EnumerationsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FieldValidationPoliciesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FieldValidationPolicyConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FilterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormActionConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormActionsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormAnnotationConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormAnnotationsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormFieldConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormFilterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormFiltersConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormReviewPoliciesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormReviewPolicyConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormSectionConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormStatePoliciesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormStatePolicyConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormTabConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormTabsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormValidationPoliciesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormValidationPolicyConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FormWidgetRulesPoliciesConfig;
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
import com.flowcentraltech.flowcentral.configuration.xml.RelatedListsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SetStateConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SetStatesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SuggestionTypeConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SuggestionTypesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.TableActionConfig;
import com.flowcentraltech.flowcentral.configuration.xml.TableActionsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.TableColumnConfig;
import com.flowcentraltech.flowcentral.configuration.xml.TableColumnsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.TableFilterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.TableFiltersConfig;
import com.flowcentraltech.flowcentral.configuration.xml.TableLoadingConfig;
import com.flowcentraltech.flowcentral.configuration.xml.TableLoadingsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WidgetTypeConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WidgetTypesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.util.ConfigurationUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Application XML generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("application-xml-generator")
public class ApplicationXmlGenerator extends AbstractResourcesArtifactGenerator {

    private static final String APPS_FOLDER = "apps/";

    @Configurable
    private ApplicationModuleService applicationModuleService;

    public ApplicationXmlGenerator() {
        super(APPS_FOLDER);
    }

    @Override
    protected void doGenerate(ExtensionModuleStaticFileBuilderContext ctx, String applicationName, ZipOutputStream zos)
            throws UnifyException {
        final String filename = "extension-" + applicationName.toLowerCase() + "-application.xml";
        openEntry(ctx, filename, zos);

        final AppConfig appConfig = new AppConfig();
        Application application = applicationModuleService.findApplication(applicationName);
        final String lowerCaseApplicationName = application.getName().toLowerCase();
        String descKey = lowerCaseApplicationName + ".short.description";
        String labelKey = lowerCaseApplicationName + ".label";
        ctx.addMessage(StaticMessageCategoryType.HEADER, descKey, application.getDescription());
        ctx.addMessage(StaticMessageCategoryType.HEADER, labelKey, application.getLabel());
        appConfig.setModule(ctx.getModuleName());
        appConfig.setName(application.getName());
        appConfig.setDescription(ctx.isSnapshotMode() ? application.getDescription() : "$m{" + descKey + "}");
        appConfig.setLabel(ctx.isSnapshotMode() ? application.getLabel() : "$m{" + labelKey + "}");
        appConfig.setDisplayIndex(application.getDisplayIndex());
        appConfig.setDevelopable(true);
        appConfig.setMenuAccess(application.isMenuAccess());
        appConfig.setAllowSecondaryTenants(application.isAllowSecondaryTenants());
        appConfig.setCustom(application.getConfigType().isCustom());

        // Module application configuration
        ModuleAppConfig moduleAppConfig = new ModuleAppConfig();
        moduleAppConfig.setName(applicationName);
        moduleAppConfig.setLongDescription(ctx.isSnapshotMode() ? application.getDescription() : "$m{" + descKey + "}");
        moduleAppConfig
                .setShortDescription(ctx.isSnapshotMode() ? application.getDescription() : "$m{" + descKey + "}");
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
        appConfig.setHelpSheetsConfig(ctx.getHelpSheetsConfig());
        appConfig.setWfChannelsConfig(ctx.getWfChannelsConfig());

        // APIs
        List<Long> apiIdList = applicationModuleService.findCustomAppComponentIdList(applicationName, AppAPI.class);
        if (!DataUtils.isBlank(apiIdList)) {
            APIsConfig apisConfig = new APIsConfig();
            List<APIConfig> apiList = new ArrayList<APIConfig>();
            for (Long apiId : apiIdList) {
                APIConfig apiConfig = new APIConfig();
                AppAPI appAPI = applicationModuleService.findAppAPI(apiId);
                descKey = getDescriptionKey(lowerCaseApplicationName, "api", appAPI.getName());
                labelKey = descKey + ".label";
                ctx.addMessage(StaticMessageCategoryType.API, descKey, appAPI.getDescription());
                apiConfig.setName(appAPI.getName());
                apiConfig.setDescription(ctx.isSnapshotMode() ? appAPI.getDescription() : "$m{" + descKey + "}");
                apiConfig.setType(appAPI.getType());
                apiConfig.setEntity(appAPI.getEntity());
                apiConfig.setApplet(appAPI.getApplet());
                apiConfig.setSupportCreate(appAPI.getSupportCreate());
                apiConfig.setSupportDelete(appAPI.getSupportDelete());
                apiConfig.setSupportRead(appAPI.getSupportRead());
                apiConfig.setSupportUpdate(appAPI.getSupportUpdate());

                apiList.add(apiConfig);
            }

            apisConfig.setApiList(apiList);
            appConfig.setApisConfig(apisConfig);
        }

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
                appletConfig.setDescription(ctx.isSnapshotMode() ? appApplet.getDescription() : "$m{" + descKey + "}");
                appletConfig.setLabel(ctx.isSnapshotMode() ? appApplet.getLabel() : "$m{" + labelKey + "}");
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

                    appletConfig.setProperties(new AppletPropsConfig(propList));
                }

                // Route to applets
                if (!DataUtils.isBlank(appApplet.getRouteToAppletList())) {
                    List<AppletRouteToAppletConfig> routeToAppletList = new ArrayList<AppletRouteToAppletConfig>();
                    for (AppAppletRouteToApplet appAppletRouteToApplet : appApplet.getRouteToAppletList()) {
                        AppletRouteToAppletConfig appletRouteToAppletConfig = new AppletRouteToAppletConfig();
                        appletRouteToAppletConfig.setRouteToApplet(appAppletRouteToApplet.getRouteToApplet());
                        routeToAppletList.add(appletRouteToAppletConfig);
                    }

                    appletConfig.setRouteToAppletItems(new AppletRouteToAppletsConfig(routeToAppletList));
                }

                // Filters
                if (!DataUtils.isBlank(appApplet.getFilterList())) {
                    List<AppletFilterConfig> filterList = new ArrayList<AppletFilterConfig>();
                    for (AppAppletFilter appAppletFilter : appApplet.getFilterList()) {
                        AppletFilterConfig filterConfig = InputWidgetUtils.getFilterConfig(au(), appAppletFilter);
                        String filterKey = getDescriptionKey(descKey, "appletfilter", appAppletFilter.getName());
                        ctx.addMessage(StaticMessageCategoryType.APPLET, filterKey, appAppletFilter.getDescription());
                        filterConfig.setName(appAppletFilter.getName());
                        filterConfig.setDescription(
                                ctx.isSnapshotMode() ? appAppletFilter.getDescription() : "$m{" + filterKey + "}");
                        filterList.add(filterConfig);
                    }

                    appletConfig.setFilters(new AppletFiltersConfig(filterList));
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

                    appletConfig.setValuesSet(new AppletValuesSetConfig(valuesList));
                }

                // Alerts
                if (!DataUtils.isBlank(appApplet.getAlertList())) {
                    List<AppletAlertConfig> alertList = new ArrayList<AppletAlertConfig>();
                    for (AppAppletAlert appAppletAlert : appApplet.getAlertList()) {
                        AppletAlertConfig appletAlertConfig = new AppletAlertConfig();
                        appletAlertConfig.setName(appAppletAlert.getName());
                        appletAlertConfig.setDescription(appAppletAlert.getDescription());
                        appletAlertConfig.setSender(appAppletAlert.getSender());
                        appletAlertConfig.setTemplate(appAppletAlert.getTemplate());
                        appletAlertConfig.setRecipientContactRule(appAppletAlert.getRecipientContactRule());
                        appletAlertConfig.setRecipientNameRule(appAppletAlert.getRecipientNameRule());
                        appletAlertConfig.setRecipientPolicy(appAppletAlert.getRecipientPolicy());
                        alertList.add(appletAlertConfig);
                    }

                    appletConfig.setAlerts(new AppletAlertsConfig(alertList));
                }

                appletList.add(appletConfig);
            }

            appletsConfig.setAppletList(appletList);
            appConfig.setAppletsConfig(appletsConfig);
        }

        // Enumerations
        List<Long> enumIdList = applicationModuleService.findCustomAppComponentIdList(applicationName,
                AppEnumeration.class);
        if (!DataUtils.isBlank(enumIdList)) {
            EnumerationsConfig enumerationsConfig = new EnumerationsConfig();
            List<EnumerationConfig> enumerationList = new ArrayList<EnumerationConfig>();
            for (Long enumId : enumIdList) {
                EnumerationConfig enumerationConfig = new EnumerationConfig();
                AppEnumeration appEnumeration = applicationModuleService.findAppEnumeration(enumId);
                descKey = getDescriptionKey(lowerCaseApplicationName, "enumeration", appEnumeration.getName());
                labelKey = descKey + ".label";
                ctx.addMessage(StaticMessageCategoryType.ENUMERATION, descKey, appEnumeration.getDescription());
                ctx.addMessage(StaticMessageCategoryType.ENUMERATION, labelKey, appEnumeration.getLabel());
                enumerationConfig.setName(appEnumeration.getName());
                enumerationConfig
                        .setDescription(ctx.isSnapshotMode() ? appEnumeration.getDescription() : "$m{" + descKey + "}");
                enumerationConfig.setLabel(ctx.isSnapshotMode() ? appEnumeration.getLabel() : "$m{" + labelKey + "}");

                List<EnumerationItemConfig> itemList = new ArrayList<EnumerationItemConfig>();
                for (AppEnumerationItem appEnumerationItem : appEnumeration.getItemList()) {
                    EnumerationItemConfig enumerationItemConfig = new EnumerationItemConfig();
                    enumerationItemConfig.setCode(appEnumerationItem.getCode());
                    enumerationItemConfig.setLabel(appEnumerationItem.getLabel());
                    enumerationItemConfig.setDisplayIndex(appEnumerationItem.getDisplayIndex());
                    itemList.add(enumerationItemConfig);
                }

                enumerationConfig.setItemList(itemList);
                enumerationList.add(enumerationConfig);
            }

            enumerationsConfig.setEnumList(enumerationList);
            appConfig.setEnumerationsConfig(enumerationsConfig);
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
                widgetTypeConfig
                        .setDescription(ctx.isSnapshotMode() ? appWidgetType.getDescription() : "$m{" + descKey + "}");
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
                refConfig.setDescription(ctx.isSnapshotMode() ? appRef.getDescription() : "$m{" + descKey + "}");
                refConfig.setEntity(appRef.getEntity());
                refConfig.setOrderField(appRef.getOrderField());
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
                final boolean isDirectDelegate = !StringUtils.isBlank(appEntity.getDelegate())
                        && !ApplicationCodeGenUtils.isCustomClass(appEntity.getEntityClass());
                descKey = getDescriptionKey(lowerCaseApplicationName, "entity", appEntity.getName());
                labelKey = descKey + ".label";
                final String entityDescKey = descKey;
                ctx.addMessage(StaticMessageCategoryType.ENTITY, descKey, appEntity.getDescription());
                ctx.addMessage(StaticMessageCategoryType.ENTITY, labelKey, appEntity.getLabel());
                if (!isDirectDelegate) {
                    ctx.addEntity(
                            ApplicationNameUtils.getApplicationEntityLongName(applicationName, appEntity.getName()));
                }

                appEntityConfig.setBaseType(ctx.isSnapshotMode() || isDirectDelegate ? appEntity.getBaseType() : null);
                appEntityConfig.setType(ctx.isSnapshotMode() || isDirectDelegate ? appEntity.getEntityClass()
                        : ctx.getExtensionEntityClassName(appEntity));
                appEntityConfig.setName(appEntity.getName());
                appEntityConfig
                        .setDescription(ctx.isSnapshotMode() ? appEntity.getDescription() : "$m{" + descKey + "}");
                appEntityConfig.setLabel(ctx.isSnapshotMode() ? appEntity.getLabel() : "$m{" + labelKey + "}");
                appEntityConfig.setEmailProducerConsumer(appEntity.getEmailProducerConsumer());
                appEntityConfig.setDelegate(appEntity.getDelegate());
                appEntityConfig.setTable(appEntity.getTableName());
                appEntityConfig.setDataSourceName(appEntity.getDataSourceName());
                appEntityConfig.setDateFormatter(appEntity.getDateFormatter());
                appEntityConfig.setDateTimeFormatter(appEntity.getDateTimeFormatter());
                appEntityConfig.setMapped(appEntity.isMapped());
                appEntityConfig.setSupportsChangeEvents(appEntity.isSupportsChangeEvents());
                appEntityConfig.setAuditable(appEntity.isAuditable());
                appEntityConfig.setReportable(appEntity.isReportable());
                appEntityConfig.setActionPolicy(appEntity.isActionPolicy());

                // Fields
                if (!DataUtils.isBlank(appEntity.getFieldList())) {
                    List<EntityFieldConfig> entityFieldConfigList = new ArrayList<EntityFieldConfig>();
                    for (AppEntityField appEntityField : appEntity.getFieldList()) {
                        if (ctx.isSnapshotMode() || !EntityFieldType.BASE.equals(appEntityField.getType())) {
                            EntityFieldConfig entityFieldConfig = new EntityFieldConfig();
                            labelKey = getDescriptionKey(entityDescKey, "field.label", appEntityField.getName());
                            ctx.addMessage(StaticMessageCategoryType.ENTITY, labelKey, appEntityField.getLabel());
                            entityFieldConfig.setType(appEntityField.getDataType());
                            entityFieldConfig.setName(appEntityField.getName());
                            entityFieldConfig.setLabel(
                                    ctx.isSnapshotMode() ? appEntityField.getLabel() : "$m{" + labelKey + "}");
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
                                entityFieldConfig.setInputLabel(ctx.isSnapshotMode() ? appEntityField.getInputLabel()
                                        : "$m{" + inLabelKey + "}");
                            }

                            entityFieldConfig.setInputWidget(appEntityField.getInputWidget());
                            entityFieldConfig.setSuggestionType(appEntityField.getSuggestionType());
                            entityFieldConfig.setInputListKey(appEntityField.getInputListKey());
                            entityFieldConfig.setLingualWidget(appEntityField.getLingualWidget());
                            entityFieldConfig.setLingualListKey(appEntityField.getLingualListKey());
                            entityFieldConfig.setAutoFormat(appEntityField.getAutoFormat());
                            entityFieldConfig.setDefaultVal(appEntityField.getDefaultVal());
                            entityFieldConfig.setJsonName(appEntityField.getJsonName());
                            entityFieldConfig.setJsonFormatter(appEntityField.getJsonFormatter());
                            entityFieldConfig.setMapped(appEntityField.getMapped());
                            entityFieldConfig.setTextCase(appEntityField.getTextCase());
                            entityFieldConfig.setColumns(appEntityField.getColumns());
                            entityFieldConfig.setRows(appEntityField.getRows());
                            entityFieldConfig.setMinLen(appEntityField.getMinLen());
                            entityFieldConfig.setMaxLen(appEntityField.getMaxLen());
                            entityFieldConfig.setPrecision(appEntityField.getPrecision());
                            entityFieldConfig.setScale(appEntityField.getScale());
                            entityFieldConfig.setBranchScoping(appEntityField.isBranchScoping());
                            entityFieldConfig.setTrim(appEntityField.isTrim());
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

                    appEntityConfig.setFields(new EntityFieldsConfig(entityFieldConfigList));
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
                        entitySeriesConfig.setDescription(
                                ctx.isSnapshotMode() ? appEntitySeries.getDescription() : "$m{" + descKey + "}");
                        entitySeriesConfig
                                .setLabel(ctx.isSnapshotMode() ? appEntitySeries.getLabel() : "$m{" + labelKey + "}");
                        entitySeriesConfig.setFieldName(appEntitySeries.getFieldName());
                        seriesList.add(entitySeriesConfig);
                    }

                    appEntityConfig.setSeriesSet(new EntitySeriesSetConfig(seriesList));
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
                        entityCategoryConfig.setDescription(
                                ctx.isSnapshotMode() ? appEntityCategory.getDescription() : "$m{" + descKey + "}");
                        entityCategoryConfig
                                .setLabel(ctx.isSnapshotMode() ? appEntityCategory.getLabel() : "$m{" + labelKey + "}");
                        FilterConfig filterConfig = InputWidgetUtils.getFilterConfig(au(),
                                appEntityCategory.getFilter());
                        entityCategoryConfig
                                .setRestrictionList(filterConfig != null ? filterConfig.getRestrictionList() : null);
                        categoryList.add(entityCategoryConfig);
                    }

                    appEntityConfig.setCategories(new EntityCategoriesConfig(categoryList));
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
                        entityAttachmentConfig.setDescription(
                                ctx.isSnapshotMode() ? appEntityAttachment.getDescription() : "$m{" + descKey + "}");
                        attachmentConfigList.add(entityAttachmentConfig);
                    }

                    appEntityConfig.setAttachments(new EntityAttachmentsConfig(attachmentConfigList));
                }

                // Expressions
                if (!DataUtils.isBlank(appEntity.getExpressionList())) {
                    List<EntityExpressionConfig> expressionList = new ArrayList<EntityExpressionConfig>();
                    for (AppEntityExpression appEntityExpression : appEntity.getExpressionList()) {
                        EntityExpressionConfig entityExpressionConfig = new EntityExpressionConfig();
                        descKey = getDescriptionKey(entityDescKey, "expression", appEntityExpression.getName());
                        ctx.addMessage(StaticMessageCategoryType.ENTITY, descKey, appEntityExpression.getDescription());
                        entityExpressionConfig.setName(appEntityExpression.getName());
                        entityExpressionConfig.setDescription(
                                ctx.isSnapshotMode() ? appEntityExpression.getDescription() : "$m{" + descKey + "}");
                        entityExpressionConfig.setExpression(appEntityExpression.getExpression());
                        expressionList.add(entityExpressionConfig);
                    }

                    appEntityConfig.setExpressions(new EntityExpressionsConfig(expressionList));
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
                        entityUniqueConstraintConfig
                                .setDescription(ctx.isSnapshotMode() ? appEntityUniqueConstraint.getDescription()
                                        : "$m{" + descKey + "}");
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

                    appEntityConfig.setUniqueConstraints(new EntityUniqueConstraintsConfig(uniqueConstraintList));
                }

                // Indexes
                if (!DataUtils.isBlank(appEntity.getIndexList())) {
                    List<EntityIndexConfig> indexList = new ArrayList<EntityIndexConfig>();
                    for (AppEntityIndex appEntityIndex : appEntity.getIndexList()) {
                        EntityIndexConfig entityIndexConfig = new EntityIndexConfig();
                        descKey = getDescriptionKey(entityDescKey, "index", appEntityIndex.getName());
                        ctx.addMessage(StaticMessageCategoryType.ENTITY, descKey, appEntityIndex.getDescription());
                        entityIndexConfig.setName(appEntityIndex.getName());
                        entityIndexConfig.setDescription(
                                ctx.isSnapshotMode() ? appEntityIndex.getDescription() : "$m{" + descKey + "}");
                        entityIndexConfig.setFieldList(appEntityIndex.getFieldList());
                        indexList.add(entityIndexConfig);
                    }

                    appEntityConfig.setIndexes(new EntityIndexesConfig(indexList));
                }

                // Upload configuration
                if (!DataUtils.isBlank(appEntity.getUploadList())) {
                    List<EntityUploadConfig> uploadList = new ArrayList<EntityUploadConfig>();
                    for (AppEntityUpload appEntityUpload : appEntity.getUploadList()) {
                        EntityUploadConfig entityUploadConfig = new EntityUploadConfig();
                        descKey = getDescriptionKey(entityDescKey, "upload", appEntityUpload.getName());
                        ctx.addMessage(StaticMessageCategoryType.ENTITY, descKey, appEntityUpload.getDescription());
                        entityUploadConfig.setName(appEntityUpload.getName());
                        entityUploadConfig.setDescription(
                                ctx.isSnapshotMode() ? appEntityUpload.getDescription() : "$m{" + descKey + "}");
                        entityUploadConfig.setConstraintAction(appEntityUpload.getConstraintAction());
                        entityUploadConfig.setFieldSequence(
                                InputWidgetUtils.getFieldSequenceConfig(appEntityUpload.getFieldSequence()));
                        uploadList.add(entityUploadConfig);
                    }

                    appEntityConfig.setUploads(new EntityUploadsConfig(uploadList));
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
                        entitySearchInputConfig
                                .setDescription(ctx.isSnapshotMode() ? appEntitySearchInput.getDescription()
                                        : "$m{" + searchInputKey + "}");
                        entitySearchInputConfig.setRestrictionResolver(appEntitySearchInput.getRestrictionResolver());
                        searchInputList.add(entitySearchInputConfig);
                    }

                    appEntityConfig.setSearchInputs(new EntitySearchInputsConfig(searchInputList));
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
                appTableConfig.setDescription(ctx.isSnapshotMode() ? appTable.getDescription() : "$m{" + descKey + "}");
                appTableConfig.setLabel(ctx.isSnapshotMode() ? appTable.getLabel() : "$m{" + labelKey + "}");
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
                            tableColumnConfig.setLabel(
                                    ctx.isSnapshotMode() ? appTableColumn.getLabel() : "$m{" + labelKey + "}");
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

                    appTableConfig.setColumns(new TableColumnsConfig(columnList));
                }

                // Filters
                if (!DataUtils.isBlank(appTable.getFilterList())) {
                    List<TableFilterConfig> filterList = new ArrayList<TableFilterConfig>();
                    for (AppTableFilter appTableFilter : appTable.getFilterList()) {
                        TableFilterConfig tableFilterConfig = InputWidgetUtils.getFilterConfig(au(), appTableFilter);
                        String filterKey = getDescriptionKey(descKey, "tablefilter", appTableFilter.getName());
                        ctx.addMessage(StaticMessageCategoryType.TABLE, filterKey, appTableFilter.getDescription());
                        tableFilterConfig.setName(appTableFilter.getName());
                        tableFilterConfig.setDescription(
                                ctx.isSnapshotMode() ? appTableFilter.getDescription() : "$m{" + filterKey + "}");
                        filterList.add(tableFilterConfig);
                    }

                    appTableConfig.setFilters(new TableFiltersConfig(filterList));
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
                        tableActionConfig.setDescription(
                                ctx.isSnapshotMode() ? appTableAction.getDescription() : "$m{" + descKey + "}");
                        tableActionConfig
                                .setLabel(ctx.isSnapshotMode() ? appTableAction.getLabel() : "$m{" + labelKey + "}");
                        tableActionConfig.setPolicy(appTableAction.getPolicy());
                        tableActionConfig.setOrderIndex(appTableAction.getOrderIndex());
                        actionList.add(tableActionConfig);
                    }

                    appTableConfig.setActions(new TableActionsConfig(actionList));
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
                        tableLoadingConfig.setDescription(
                                ctx.isSnapshotMode() ? appTableLoading.getDescription() : "$m{" + descKey + "}");
                        tableLoadingConfig
                                .setLabel(ctx.isSnapshotMode() ? appTableLoading.getLabel() : "$m{" + labelKey + "}");
                        tableLoadingConfig.setProvider(appTableLoading.getProvider());
                        tableLoadingConfig.setOrderIndex(appTableLoading.getOrderIndex());
                        loadingList.add(tableLoadingConfig);
                    }

                    appTableConfig.setLoadings(new TableLoadingsConfig(loadingList));
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
                appFormConfig.setDescription(ctx.isSnapshotMode() ? appForm.getDescription() : "$m{" + descKey + "}");
                appFormConfig.setEntity(appForm.getEntity());
                appFormConfig.setLabel(appForm.getLabel());
                appFormConfig.setHelpSheet(appForm.getHelpSheet());
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
                        filterConfig.setDescription(ctx.isSnapshotMode() ? appFormFilter.getDescription() : "$m{" + descKey + "}");
                        filterList.add(filterConfig);
                    }

                    appFormConfig.setFilters(new FormFiltersConfig(filterList));
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
                        formAnnotationConfig.setVisibility(appFormAnnotation.getVisibility());
                        formAnnotationConfig.setName(appFormAnnotation.getName());
                        formAnnotationConfig.setDescription(
                                ctx.isSnapshotMode() ? appFormAnnotation.getDescription() : "$m{" + descKey + "}");
                        formAnnotationConfig.setMessage(
                                ctx.isSnapshotMode() ? appFormAnnotation.getMessage() : "$m{" + msgKey + "}");
                        formAnnotationConfig.setHtml(appFormAnnotation.isHtml());
                        formAnnotationConfig.setDirectPlacement(appFormAnnotation.isDirectPlacement());
                        formAnnotationConfig.setOnCondition(
                                InputWidgetUtils.getFilterConfig(au(), appFormAnnotation.getOnCondition()));
                        annotationConfigList.add(formAnnotationConfig);
                    }

                    appFormConfig.setAnnotations(new FormAnnotationsConfig(annotationConfigList));
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
                        formActionConfig.setDescription(
                                ctx.isSnapshotMode() ? appFormAction.getDescription() : "$m{" + descKey + "}");
                        formActionConfig
                                .setLabel(ctx.isSnapshotMode() ? appFormAction.getLabel() : "$m{" + labelKey + "}");
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

                    appFormConfig.setActions(new FormActionsConfig(actionConfigList));
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
                            formTabConfig.setLabel(
                                    ctx.isSnapshotMode() ? appFormElement.getLabel() : "$m{" + labelKey + "}");
                        }

                        formTabConfig.setApplet(appFormElement.getTabApplet());
                        formTabConfig.setReference(appFormElement.getTabReference());
                        formTabConfig.setFilter(appFormElement.getFilter());
                        formTabConfig.setEditAction(appFormElement.getEditAction());
                        formTabConfig.setEditFormless(appFormElement.getEditFormless());
                        formTabConfig.setEditFixedRows(appFormElement.getEditFixedRows());
                        formTabConfig.setEditAllowAddition(appFormElement.getEditAllowAddition());
                        formTabConfig.setIgnoreParentCondition(appFormElement.isIgnoreParentCondition());
                        formTabConfig.setIncludeSysParam(appFormElement.isIncludeSysParam());
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
                                    formSectionConfig.setLabel(
                                            ctx.isSnapshotMode() ? appFormElement.getLabel() : "$m{" + labelKey + "}");
                                }

                                formSectionConfig.setIcon(appFormElement.getIcon());
                                formSectionConfig.setPanel(appFormElement.getPanel());
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
                                            formFieldConfig.setLabel(ctx.isSnapshotMode() ? appFormElement.getLabel()
                                                    : "$m{" + labelKey + "}");
                                        }

                                        formFieldConfig.setInputWidget(appFormElement.getInputWidget());
                                        formFieldConfig.setReference(appFormElement.getInputReference());
                                        formFieldConfig.setPreviewForm(appFormElement.getPreviewForm());
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

                    appFormConfig.setTabs(new FormTabsConfig(tabConfigList));
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
                        relatedListConfig.setDescription(
                                ctx.isSnapshotMode() ? appFormRelatedList.getDescription() : "$m{" + descKey + "}");
                        relatedListConfig.setLabel(
                                ctx.isSnapshotMode() ? appFormRelatedList.getLabel() : "$m{" + labelKey + "}");
                        relatedListConfig.setApplet(appFormRelatedList.getApplet());
                        relatedListConfig.setFilter(appFormRelatedList.getFilter());
                        relatedListConfig.setEditAction(appFormRelatedList.getEditAction());
                        relatedConfigList.add(relatedListConfig);
                    }

                    appFormConfig.setRelatedLists(new RelatedListsConfig(relatedConfigList));
                }

                // Form State Policies
                if (!DataUtils.isBlank(appForm.getFieldStateList())) {
                    List<FormStatePolicyConfig> formStatePolicyConfigList = new ArrayList<FormStatePolicyConfig>();
                    for (AppFormStatePolicy appFormStatePolicy : appForm.getFieldStateList()) {
                        FormStatePolicyConfig formStatePolicyConfig = new FormStatePolicyConfig();
                        formStatePolicyConfig.setName(appFormStatePolicy.getName());
                        descKey = getDescriptionKey(formDescKey, "statepolicy", appFormStatePolicy.getDescription());
                        ctx.addMessage(StaticMessageCategoryType.FORM, descKey, appFormStatePolicy.getDescription());
                        formStatePolicyConfig.setDescription(
                                ctx.isSnapshotMode() ? appFormStatePolicy.getDescription() : "$m{" + descKey + "}");
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

                    appFormConfig.setFormStatePolicies(new FormStatePoliciesConfig(formStatePolicyConfigList));
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
                        formWidgetRulesPolicyConfig
                                .setDescription(ctx.isSnapshotMode() ? appFormWidgetRulesPolicy.getDescription()
                                        : "$m{" + descKey + "}");
                        formWidgetRulesPolicyConfig.setExecutionIndex(appFormWidgetRulesPolicy.getExecutionIndex());
                        formWidgetRulesPolicyConfig.setOnCondition(
                                InputWidgetUtils.getFilterConfig(au(), appFormWidgetRulesPolicy.getOnCondition()));
                        formWidgetRulesPolicyConfig.setWidgetRules(
                                InputWidgetUtils.getWidgetRulesConfig(appFormWidgetRulesPolicy.getWidgetRules()));
                        widgetRulesPolicyList.add(formWidgetRulesPolicyConfig);
                    }

                    appFormConfig.setFormWidgetRulesPolicies(new FormWidgetRulesPoliciesConfig(widgetRulesPolicyList));
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
                        fieldValidationPolicyConfig
                                .setDescription(ctx.isSnapshotMode() ? appFormFieldValidationPolicy.getDescription()
                                        : "$m{" + descKey + "}");
                        fieldValidationPolicyConfig.setFieldName(appFormFieldValidationPolicy.getFieldName());
                        fieldValidationPolicyConfig.setValidator(appFormFieldValidationPolicy.getValidation());
                        fieldValidationPolicyConfig.setRule(appFormFieldValidationPolicy.getRule());
                        fieldValidationPolicyConfig.setExecutionIndex(appFormFieldValidationPolicy.getExecutionIndex());
                        fieldValidationPolicyConfigList.add(fieldValidationPolicyConfig);
                    }

                    appFormConfig.setFieldValidationPolicies(
                            new FieldValidationPoliciesConfig(fieldValidationPolicyConfigList));
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
                        formValidationPolicyConfig
                                .setDescription(ctx.isSnapshotMode() ? appFormValidationPolicy.getDescription()
                                        : "$m{" + descKey + "}");
                        formValidationPolicyConfig.setMessage(
                                ctx.isSnapshotMode() ? appFormValidationPolicy.getMessage() : "$m{" + msgKey + "}");
                        formValidationPolicyConfig.setTarget(appFormValidationPolicy.getTarget());
                        formValidationPolicyConfig.setExecutionIndex(appFormValidationPolicy.getExecutionIndex());
                        formValidationPolicyConfig.setErrorMatcher(appFormValidationPolicy.getErrorMatcher());
                        formValidationPolicyConfig.setErrorCondition(
                                InputWidgetUtils.getFilterConfig(au(), appFormValidationPolicy.getErrorCondition()));
                        formValidationPolicyList.add(formValidationPolicyConfig);
                    }

                    appFormConfig.setFormValidationPolicies(new FormValidationPoliciesConfig(formValidationPolicyList));
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
                        formReviewPolicyConfig.setDescription(
                                ctx.isSnapshotMode() ? appFormReviewPolicy.getDescription() : "$m{" + descKey + "}");
                        formReviewPolicyConfig.setMessage(
                                ctx.isSnapshotMode() ? appFormReviewPolicy.getMessage() : "$m{" + msgKey + "}");
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

                    appFormConfig.setFormReviewPolicies(new FormReviewPoliciesConfig(formReviewPolicyList));
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
                propertyListConfig.setDescription(
                        ctx.isSnapshotMode() ? appPropertyList.getDescription() : "$m{" + descKey + "}");

                // Property sets
                if (!DataUtils.isBlank(appPropertyList.getItemSet())) {
                    List<PropertySetConfig> propSetList = new ArrayList<PropertySetConfig>();
                    for (AppPropertySet appPropertySet : appPropertyList.getItemSet()) {
                        PropertySetConfig propertySetConfig = new PropertySetConfig();
                        labelKey = getDescriptionKey(propertyDescKey, "set.label", appPropertySet.getLabel());
                        ctx.addMessage(StaticMessageCategoryType.PROPERTY_LIST, labelKey, appPropertySet.getLabel());
                        propertySetConfig
                                .setLabel(ctx.isSnapshotMode() ? appPropertySet.getLabel() : "$m{" + labelKey + "}");

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
                                propertyListPropConfig
                                        .setDescription(ctx.isSnapshotMode() ? appPropertyListItem.getDescription()
                                                : "$m{" + descKey + "}");
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
                propertyRuleConfig.setDescription(
                        ctx.isSnapshotMode() ? appPropertyRule.getDescription() : "$m{" + descKey + "}");
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
                appAssignmentPageConfig.setDescription(
                        ctx.isSnapshotMode() ? appAssignmentPage.getDescription() : "$m{" + descKey + "}");
                appAssignmentPageConfig
                        .setLabel(ctx.isSnapshotMode() ? appAssignmentPage.getLabel() : "$m{" + labelKey + "}");
                appAssignmentPageConfig.setEntity(appAssignmentPage.getEntity());
                if (appAssignmentPage.getFilterCaption1() != null) {
                    labelKey = descKey + ".filtercaption1";
                    ctx.addMessage(StaticMessageCategoryType.ASSIGNMENT_PAGE, labelKey,
                            appAssignmentPage.getFilterCaption1());
                    appAssignmentPageConfig.setFilterCaption1(
                            ctx.isSnapshotMode() ? appAssignmentPage.getFilterCaption1() : "$m{" + labelKey + "}");
                }

                if (appAssignmentPage.getFilterCaption2() != null) {
                    labelKey = descKey + ".filtercaption2";
                    ctx.addMessage(StaticMessageCategoryType.ASSIGNMENT_PAGE, labelKey,
                            appAssignmentPage.getFilterCaption2());
                    appAssignmentPageConfig.setFilterCaption2(
                            ctx.isSnapshotMode() ? appAssignmentPage.getFilterCaption2() : "$m{" + labelKey + "}");
                }

                appAssignmentPageConfig.setFilterList1(appAssignmentPage.getFilterList1());
                appAssignmentPageConfig.setFilterList2(appAssignmentPage.getFilterList2());

                if (appAssignmentPage.getAssignCaption() != null) {
                    labelKey = descKey + ".assigncaption";
                    ctx.addMessage(StaticMessageCategoryType.ASSIGNMENT_PAGE, labelKey,
                            appAssignmentPage.getAssignCaption());
                    appAssignmentPageConfig.setAssignCaption(
                            ctx.isSnapshotMode() ? appAssignmentPage.getAssignCaption() : "$m{" + labelKey + "}");
                }

                appAssignmentPageConfig.setAssignList(appAssignmentPage.getAssignList());

                if (appAssignmentPage.getUnassignCaption() != null) {
                    labelKey = descKey + ".unassigncaption";
                    ctx.addMessage(StaticMessageCategoryType.ASSIGNMENT_PAGE, labelKey,
                            appAssignmentPage.getUnassignCaption());
                    appAssignmentPageConfig.setUnassignCaption(
                            ctx.isSnapshotMode() ? appAssignmentPage.getUnassignCaption() : "$m{" + labelKey + "}");
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
                suggestionTypeConfig.setDescription(
                        ctx.isSnapshotMode() ? appSuggestionType.getDescription() : "$m{" + descKey + "}");
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

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
package com.flowcentraltech.flowcentral.application.business;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationFeatureConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationImportDataTaskConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleErrorConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.AppletFilterDef;
import com.flowcentraltech.flowcentral.application.data.ApplicationDef;
import com.flowcentraltech.flowcentral.application.data.ApplicationMenuDef;
import com.flowcentraltech.flowcentral.application.data.AssignmentPageDef;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.EntityUploadDef;
import com.flowcentraltech.flowcentral.application.data.FieldSequenceDef;
import com.flowcentraltech.flowcentral.application.data.FieldSequenceEntryDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef;
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef.FilterType;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormFilterDef;
import com.flowcentraltech.flowcentral.application.data.PropertyListDef;
import com.flowcentraltech.flowcentral.application.data.PropertyListItem;
import com.flowcentraltech.flowcentral.application.data.PropertyListItemDef;
import com.flowcentraltech.flowcentral.application.data.PropertyRuleDef;
import com.flowcentraltech.flowcentral.application.data.RecLoadInfo;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.data.SetStatesDef;
import com.flowcentraltech.flowcentral.application.data.SetValuesDef;
import com.flowcentraltech.flowcentral.application.data.SuggestionTypeDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.data.TableFilterDef;
import com.flowcentraltech.flowcentral.application.data.TableLoadingDef;
import com.flowcentraltech.flowcentral.application.data.UniqueConstraintDef;
import com.flowcentraltech.flowcentral.application.data.WidgetRuleEntryDef;
import com.flowcentraltech.flowcentral.application.data.WidgetRulesDef;
import com.flowcentraltech.flowcentral.application.data.WidgetTypeDef;
import com.flowcentraltech.flowcentral.application.entities.AppApplet;
import com.flowcentraltech.flowcentral.application.entities.AppAppletFilter;
import com.flowcentraltech.flowcentral.application.entities.AppAppletFilterQuery;
import com.flowcentraltech.flowcentral.application.entities.AppAppletProp;
import com.flowcentraltech.flowcentral.application.entities.AppAppletPropQuery;
import com.flowcentraltech.flowcentral.application.entities.AppAppletQuery;
import com.flowcentraltech.flowcentral.application.entities.AppAppletSetValues;
import com.flowcentraltech.flowcentral.application.entities.AppAppletSetValuesQuery;
import com.flowcentraltech.flowcentral.application.entities.AppAssignmentPage;
import com.flowcentraltech.flowcentral.application.entities.AppAssignmentPageQuery;
import com.flowcentraltech.flowcentral.application.entities.AppEntity;
import com.flowcentraltech.flowcentral.application.entities.AppEntityAttachment;
import com.flowcentraltech.flowcentral.application.entities.AppEntityAttachmentQuery;
import com.flowcentraltech.flowcentral.application.entities.AppEntityExpression;
import com.flowcentraltech.flowcentral.application.entities.AppEntityExpressionQuery;
import com.flowcentraltech.flowcentral.application.entities.AppEntityField;
import com.flowcentraltech.flowcentral.application.entities.AppEntityFieldQuery;
import com.flowcentraltech.flowcentral.application.entities.AppEntityIndex;
import com.flowcentraltech.flowcentral.application.entities.AppEntityIndexQuery;
import com.flowcentraltech.flowcentral.application.entities.AppEntityQuery;
import com.flowcentraltech.flowcentral.application.entities.AppEntityUniqueConstraint;
import com.flowcentraltech.flowcentral.application.entities.AppEntityUniqueConstraintQuery;
import com.flowcentraltech.flowcentral.application.entities.AppEntityUpload;
import com.flowcentraltech.flowcentral.application.entities.AppEntityUploadQuery;
import com.flowcentraltech.flowcentral.application.entities.AppFieldSequence;
import com.flowcentraltech.flowcentral.application.entities.AppFieldSequenceQuery;
import com.flowcentraltech.flowcentral.application.entities.AppFilter;
import com.flowcentraltech.flowcentral.application.entities.AppFilterQuery;
import com.flowcentraltech.flowcentral.application.entities.AppForm;
import com.flowcentraltech.flowcentral.application.entities.AppFormAction;
import com.flowcentraltech.flowcentral.application.entities.AppFormActionQuery;
import com.flowcentraltech.flowcentral.application.entities.AppFormAnnotation;
import com.flowcentraltech.flowcentral.application.entities.AppFormAnnotationQuery;
import com.flowcentraltech.flowcentral.application.entities.AppFormElement;
import com.flowcentraltech.flowcentral.application.entities.AppFormElementQuery;
import com.flowcentraltech.flowcentral.application.entities.AppFormFieldValidationPolicy;
import com.flowcentraltech.flowcentral.application.entities.AppFormFieldValidationPolicyQuery;
import com.flowcentraltech.flowcentral.application.entities.AppFormFilter;
import com.flowcentraltech.flowcentral.application.entities.AppFormFilterQuery;
import com.flowcentraltech.flowcentral.application.entities.AppFormQuery;
import com.flowcentraltech.flowcentral.application.entities.AppFormRelatedList;
import com.flowcentraltech.flowcentral.application.entities.AppFormRelatedListQuery;
import com.flowcentraltech.flowcentral.application.entities.AppFormReviewPolicy;
import com.flowcentraltech.flowcentral.application.entities.AppFormReviewPolicyQuery;
import com.flowcentraltech.flowcentral.application.entities.AppFormSetState;
import com.flowcentraltech.flowcentral.application.entities.AppFormStatePolicy;
import com.flowcentraltech.flowcentral.application.entities.AppFormStatePolicyQuery;
import com.flowcentraltech.flowcentral.application.entities.AppFormValidationPolicy;
import com.flowcentraltech.flowcentral.application.entities.AppFormValidationPolicyQuery;
import com.flowcentraltech.flowcentral.application.entities.AppFormWidgetRulesPolicy;
import com.flowcentraltech.flowcentral.application.entities.AppFormWidgetRulesPolicyQuery;
import com.flowcentraltech.flowcentral.application.entities.AppPropertyList;
import com.flowcentraltech.flowcentral.application.entities.AppPropertyListItem;
import com.flowcentraltech.flowcentral.application.entities.AppPropertyListQuery;
import com.flowcentraltech.flowcentral.application.entities.AppPropertyRule;
import com.flowcentraltech.flowcentral.application.entities.AppPropertyRuleChoice;
import com.flowcentraltech.flowcentral.application.entities.AppPropertyRuleChoiceQuery;
import com.flowcentraltech.flowcentral.application.entities.AppPropertyRuleQuery;
import com.flowcentraltech.flowcentral.application.entities.AppPropertySet;
import com.flowcentraltech.flowcentral.application.entities.AppRef;
import com.flowcentraltech.flowcentral.application.entities.AppRefQuery;
import com.flowcentraltech.flowcentral.application.entities.AppSetValues;
import com.flowcentraltech.flowcentral.application.entities.AppSetValuesQuery;
import com.flowcentraltech.flowcentral.application.entities.AppSuggestion;
import com.flowcentraltech.flowcentral.application.entities.AppSuggestionQuery;
import com.flowcentraltech.flowcentral.application.entities.AppSuggestionType;
import com.flowcentraltech.flowcentral.application.entities.AppSuggestionTypeQuery;
import com.flowcentraltech.flowcentral.application.entities.AppTable;
import com.flowcentraltech.flowcentral.application.entities.AppTableAction;
import com.flowcentraltech.flowcentral.application.entities.AppTableActionQuery;
import com.flowcentraltech.flowcentral.application.entities.AppTableColumn;
import com.flowcentraltech.flowcentral.application.entities.AppTableFilter;
import com.flowcentraltech.flowcentral.application.entities.AppTableFilterQuery;
import com.flowcentraltech.flowcentral.application.entities.AppTableLoading;
import com.flowcentraltech.flowcentral.application.entities.AppTableLoadingQuery;
import com.flowcentraltech.flowcentral.application.entities.AppTableQuery;
import com.flowcentraltech.flowcentral.application.entities.AppWidgetRules;
import com.flowcentraltech.flowcentral.application.entities.AppWidgetRulesQuery;
import com.flowcentraltech.flowcentral.application.entities.AppWidgetType;
import com.flowcentraltech.flowcentral.application.entities.AppWidgetTypeQuery;
import com.flowcentraltech.flowcentral.application.entities.Application;
import com.flowcentraltech.flowcentral.application.entities.ApplicationQuery;
import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.application.util.ApplicationCodeGenUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityUtils.EntityBaseTypeFieldSet;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationPageUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.application.web.widgets.EntitySearchWidget;
import com.flowcentraltech.flowcentral.common.annotation.LongName;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.business.ApplicationArtifactInstaller;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.EntityAuditInfoProvider;
import com.flowcentraltech.flowcentral.common.business.FileAttachmentProvider;
import com.flowcentraltech.flowcentral.common.business.SuggestionProvider;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.constants.OwnershipType;
import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.EntityAuditInfo;
import com.flowcentraltech.flowcentral.common.data.ParamValuesDef;
import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.flowcentraltech.flowcentral.common.entities.FileAttachment;
import com.flowcentraltech.flowcentral.common.entities.FileAttachmentDoc;
import com.flowcentraltech.flowcentral.common.entities.FileAttachmentQuery;
import com.flowcentraltech.flowcentral.common.entities.ParamValues;
import com.flowcentraltech.flowcentral.common.entities.ParamValuesQuery;
import com.flowcentraltech.flowcentral.common.util.CommonInputUtils;
import com.flowcentraltech.flowcentral.common.util.ConfigUtils;
import com.flowcentraltech.flowcentral.configuration.business.ConfigurationLoader;
import com.flowcentraltech.flowcentral.configuration.constants.AppletType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityBaseType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldType;
import com.flowcentraltech.flowcentral.configuration.constants.FormElementType;
import com.flowcentraltech.flowcentral.configuration.constants.FormReviewType;
import com.flowcentraltech.flowcentral.configuration.constants.RecordActionType;
import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.flowcentraltech.flowcentral.configuration.constants.WidgetColor;
import com.flowcentraltech.flowcentral.configuration.data.ApplicationInstall;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.configuration.xml.AppAssignmentPageConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppEntityConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppFormConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppTableConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletFilterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletPropConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletSetValuesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ChoiceConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityAttachmentConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityExpressionConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityFieldConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityIndexConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityUniqueConstraintConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EntityUploadConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FieldSequenceConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FieldValidationPolicyConfig;
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
import com.flowcentraltech.flowcentral.configuration.xml.ModuleConfig;
import com.flowcentraltech.flowcentral.configuration.xml.PropertyListConfig;
import com.flowcentraltech.flowcentral.configuration.xml.PropertyListPropConfig;
import com.flowcentraltech.flowcentral.configuration.xml.PropertyRuleConfig;
import com.flowcentraltech.flowcentral.configuration.xml.PropertySetConfig;
import com.flowcentraltech.flowcentral.configuration.xml.RefConfig;
import com.flowcentraltech.flowcentral.configuration.xml.RelatedListConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SetStateConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SetValuesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SuggestionTypeConfig;
import com.flowcentraltech.flowcentral.configuration.xml.TableActionConfig;
import com.flowcentraltech.flowcentral.configuration.xml.TableColumnConfig;
import com.flowcentraltech.flowcentral.configuration.xml.TableFilterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.TableLoadingConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WidgetRulesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WidgetTypeConfig;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.flowcentraltech.flowcentral.system.constants.SystemModuleSysParamConstants;
import com.flowcentraltech.flowcentral.system.entities.Module;
import com.tcdng.unify.core.UnifyComponentConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.DynamicEntityType;
import com.tcdng.unify.core.annotation.DynamicFieldType;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Taskable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.data.MapValues;
import com.tcdng.unify.core.data.ParamConfig;
import com.tcdng.unify.core.data.StringToken;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.database.dynamic.DynamicEntityInfo;
import com.tcdng.unify.core.database.dynamic.sql.DynamicSqlEntityLoader;
import com.tcdng.unify.core.database.sql.SqlDataSourceDialect;
import com.tcdng.unify.core.database.sql.SqlDatabase;
import com.tcdng.unify.core.format.Formatter;
import com.tcdng.unify.core.list.ListManager;
import com.tcdng.unify.core.message.MessageResolver;
import com.tcdng.unify.core.security.TwoWayStringCryptograph;
import com.tcdng.unify.core.task.TaskExecLimit;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.ArgumentTypeInfo;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Default implementation of application module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(ApplicationModuleNameConstants.APPLICATION_MODULE_SERVICE)
public class ApplicationModuleServiceImpl extends AbstractFlowCentralService
        implements ApplicationModuleService, FileAttachmentProvider, EntityAuditInfoProvider, SuggestionProvider {

    private final Set<String> refProperties = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
            AppletPropertyConstants.SEARCH_TABLE, AppletPropertyConstants.CREATE_FORM,
            AppletPropertyConstants.CREATE_FORM_SUBMIT_WORKFLOW_CHANNEL, AppletPropertyConstants.MAINTAIN_FORM,
            AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_WORKFLOW_CHANNEL, AppletPropertyConstants.ASSIGNMENT_PAGE,
            AppletPropertyConstants.PROPERTY_LIST_RULE, AppletPropertyConstants.IMPORTDATA_ROUTETO_APPLETNAME)));

    private static final int MAX_LIST_DEPTH = 8;

    @Configurable
    private SystemModuleService systemModuleService;

    @Configurable
    private ApplicationPrivilegeManager applicationPrivilegeManager;

    @Configurable
    private MessageResolver messageResolver;

    @Configurable
    private ConfigurationLoader configurationLoader;

    @Configurable
    private DynamicSqlEntityLoader dynamicSqlEntityLoader;

    @Configurable
    private ListManager listManager;

    @Configurable
    private AppletUtilities appletUtilities;

    @Configurable
    private TwoWayStringCryptograph twoWayStringCryptograph;

    private List<ApplicationArtifactInstaller> applicationArtifactInstallerList;

    private List<ApplicationAppletDefProvider> applicationAppletDefProviderList;

    private FactoryMap<String, ApplicationDef> applicationDefFactoryMap;

    private FactoryMap<String, AppletDef> appletDefFactoryMap;

    private FactoryMap<String, WidgetTypeDef> widgetDefFactoryMap;

    private FactoryMap<String, SuggestionTypeDef> suggestionDefFactoryMap;

    private FactoryMap<String, EntityClassDef> entityClassDefFactoryMap;

    private FactoryMap<String, EntityDef> entityDefFactoryMap;

    private FactoryMap<String, EntityDef> entityDefByClassFactoryMap;

    private FactoryMap<String, RefDef> refDefFactoryMap;

    private FactoryMap<String, TableDef> tableDefFactoryMap;

    private FactoryMap<String, FormDef> formDefFactoryMap;

    private FactoryMap<String, AssignmentPageDef> assignmentPageDefFactoryMap;

    private FactoryMap<String, PropertyListDef> propertyListDefMap;

    private FactoryMap<String, PropertyRuleDef> propertyRuleDefMap;

    private Set<String> entitySearchTypes;

    public ApplicationModuleServiceImpl() {
        this.entitySearchTypes = new HashSet<String>();
        this.applicationDefFactoryMap = new FactoryMap<String, ApplicationDef>(true)
            {
                @Override
                protected boolean stale(String name, ApplicationDef applicationDef) throws Exception {
                    return (environment().value(long.class, "versionNo",
                            new ApplicationQuery().id(applicationDef.getId())) > applicationDef.getVersion());
                }

                @Override
                protected ApplicationDef create(String name, Object... arg1) throws Exception {
                    Application application = environment().list(new ApplicationQuery().name(name));
                    return new ApplicationDef(application.getName(), application.getDescription(), application.getId(),
                            application.getVersionNo(), application.isDevelopable(), application.isMenuAccess(),
                            application.getModuleName(), application.getModuleDesc(), application.getModuleLabel(),
                            application.getModuleShortCode(), application.getSectorShortCode(),
                            application.getSectorColor());
                }

            };

        this.appletDefFactoryMap = new FactoryMap<String, AppletDef>(true)
            {
                @Override
                protected boolean stale(String longName, AppletDef appletDef) throws Exception {
                    return (environment().value(long.class, "versionNo",
                            new AppAppletQuery().id(appletDef.getId())) > appletDef.getVersion());
                }

                @Override
                protected AppletDef create(String longName, Object... arg1) throws Exception {
                    String _actLongName = ApplicationNameUtils.removeVestigialNamePart(longName);
                    AppApplet appApplet = getApplicationEntity(AppApplet.class, _actLongName);
                    final boolean descriptiveButtons = systemModuleService.getSysParameterValue(boolean.class,
                            SystemModuleSysParamConstants.SYSTEM_DESCRIPTIVE_BUTTONS_ENABLED);
                    AppletDef.Builder adb = AppletDef.newBuilder(appApplet.getType(), appApplet.getEntity(),
                            appApplet.getLabel(), appApplet.getIcon(), appApplet.getAssignDescField(),
                            appApplet.getDisplayIndex(), appApplet.isMenuAccess(), descriptiveButtons, _actLongName,
                            appApplet.getDescription(), appApplet.getId(), appApplet.getVersionNo());
                    for (AppAppletProp appAppletProp : appApplet.getPropList()) {
                        adb.addPropDef(appAppletProp.getName(), appAppletProp.getValue());
                    }

                    for (AppAppletFilter appAppletFilter : appApplet.getFilterList()) {
                        FilterDef filterDef = InputWidgetUtils.getFilterDef(appletUtilities, appAppletFilter.getName(),
                                appAppletFilter.getDescription(), appAppletFilter.getFilterGenerator(),
                                appAppletFilter.getFilter());
                        if (filterDef != null) {
                            adb.addFilterDef(new AppletFilterDef(filterDef, appAppletFilter.getPreferredForm(),
                                    appAppletFilter.getPreferredChildListApplet(),
                                    appAppletFilter.getChildListActionType()));
                        }
                    }

                    for (AppAppletSetValues appAppletSetValues : appApplet.getSetValuesList()) {
                        adb.addSetValuesDef(appAppletSetValues.getName(), appAppletSetValues.getDescription(),
                                InputWidgetUtils.getSetValuesDef(appAppletSetValues.getName(),
                                        appAppletSetValues.getDescription(), appAppletSetValues.getValueGenerator(),
                                        appAppletSetValues.getSetValues()));
                    }

                    adb.routeToApplet(appApplet.getRouteToApplet());
                    if (!StringUtils.isBlank(appApplet.getPath())) {
                        adb.openPath(appApplet.getPath());
                    } else {
                        adb.openPath(ApplicationPageUtils.constructAppletOpenPagePath(appApplet.getType(), longName));
                    }

                    adb.openWindow(appApplet.getType().isOpenWindow());
                    return adb.build();
                }

            };

        this.widgetDefFactoryMap = new FactoryMap<String, WidgetTypeDef>(true)
            {
                @Override
                protected boolean stale(String longName, WidgetTypeDef widgetTypeDef) throws Exception {
                    return environment().value(long.class, "versionNo",
                            new AppWidgetTypeQuery().id(widgetTypeDef.getId())) > widgetTypeDef.getVersion();
                }

                @Override
                protected WidgetTypeDef create(String longName, Object... arg1) throws Exception {
                    AppWidgetType appWidgetType = getApplicationEntity(AppWidgetType.class, longName);
                    return new WidgetTypeDef(appWidgetType.getDataType(), appWidgetType.getInputType(), longName,
                            appWidgetType.getDescription(), appWidgetType.getId(), appWidgetType.getVersionNo(),
                            appWidgetType.getEditor(), appWidgetType.getRenderer(), appWidgetType.isStretch());
                }
            };

        this.suggestionDefFactoryMap = new FactoryMap<String, SuggestionTypeDef>(true)
            {
                @Override
                protected boolean stale(String longName, SuggestionTypeDef suggestionTypeDef) throws Exception {
                    return environment().value(long.class, "versionNo", new AppSuggestionTypeQuery()
                            .id(suggestionTypeDef.getId())) > suggestionTypeDef.getVersion();
                }

                @Override
                protected SuggestionTypeDef create(String longName, Object... args) throws Exception {
                    ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
                    AppSuggestionType appSuggestionType = environment()
                            .list(Query.of(AppSuggestionType.class).addEquals("name", nameParts.getEntityName())
                                    .addEquals("applicationName", nameParts.getApplicationName()));
                    if (appSuggestionType == null) {
                        appSuggestionType = new AppSuggestionType();
                        Long applicationId = getApplicationDef(nameParts.getApplicationName()).getId();
                        appSuggestionType.setApplicationId(applicationId);
                        appSuggestionType.setConfigType(ConfigType.STATIC);
                        appSuggestionType.setName(nameParts.getEntityName());
                        appSuggestionType.setDescription(nameParts.getEntityName());
                        environment().create(appSuggestionType);
                    }

                    return new SuggestionTypeDef(appSuggestionType.getParent(), longName,
                            appSuggestionType.getDescription(), appSuggestionType.getId(),
                            appSuggestionType.getVersionNo());
                }
            };

        this.entityClassDefFactoryMap = new FactoryMap<String, EntityClassDef>(true)
            {
                @Override
                protected boolean stale(String longName, EntityClassDef entityClassDef) throws Exception {
                    if (!"application.propertyItem".equals(longName)) {
                        return environment().value(long.class, "versionNo",
                                new AppEntityQuery().id(entityClassDef.getId())) > entityClassDef.getVersion();
                    }

                    return false;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected EntityClassDef create(String longName, Object... arg1) throws Exception {
                    final EntityDef entityDef = getEntityDef(longName);
                    if ("application.propertyItem".equals(longName)) {
                        return new EntityClassDef(entityDef, PropertyListItem.class);
                    }

                    logDebug("Building dynamic entity information...");
                    Map<String, DynamicEntityInfo> dynamicEntityInfoMap = new HashMap<String, DynamicEntityInfo>();
                    DynamicEntityInfo _originDynamicEntityInfo = buildDynamicEntityInfo(entityDef, dynamicEntityInfoMap,
                            null);

                    // Consider all dependants
                    List<String> entityNameList = getAllDependantEntities(longName);
                    for (String entity : entityNameList) {
                        buildDynamicEntityInfo(getEntityDef(entity), dynamicEntityInfoMap, null);
                    }

                    // Get entity definitions for type that need to be generated
                    List<DynamicEntityInfo> _dynamicEntityInfos = new ArrayList<DynamicEntityInfo>();
                    for (Map.Entry<String, DynamicEntityInfo> entry : dynamicEntityInfoMap.entrySet()) {
                        DynamicEntityInfo info = entry.getValue();
                        info.finalizeResolution();
                        if (info.isGeneration()) {
                            _dynamicEntityInfos.add(entry.getValue());
                        }
                    }

                    logDebug("Dynamic entity information successfully built. [{0}] entities resolved for generation.",
                            _dynamicEntityInfos.size());

                    // Compile and load class if necessary
                    if (!_dynamicEntityInfos.isEmpty()) {
                        dynamicSqlEntityLoader.loadDynamicSqlEntities((SqlDatabase) environment().getDatabase(),
                                _dynamicEntityInfos);
                    }

                    // Load related entity class definitions
                    for (Map.Entry<String, DynamicEntityInfo> entry : dynamicEntityInfoMap.entrySet()) {
                        final String _longName = entry.getKey();
                        if (!_longName.equals(longName)) {
                            EntityDef _entityDef = getEntityDef(_longName);
                            Class<? extends Entity> _entityClass = (Class<? extends Entity>) ReflectUtils
                                    .classForName(entry.getValue().getClassName());
                            registerDelegate(_entityDef, _entityClass);
                            put(_longName, new EntityClassDef(_entityDef, _entityClass));
                        }
                    }

                    Class<? extends Entity> entityClass = (Class<? extends Entity>) ReflectUtils
                            .classForName(_originDynamicEntityInfo.getClassName());
                    registerDelegate(entityDef, entityClass);
                    return new EntityClassDef(entityDef, entityClass);
                }

                @Override
                protected boolean onCreate(EntityClassDef entityClassDef) throws Exception {
                    if (entityClassDef.getEntityDef().delegated()) {
                        List<ArgumentTypeInfo> childListArgs = new ArrayList<ArgumentTypeInfo>();
                        for (EntityFieldDef entityFieldDef : entityClassDef.getEntityDef().getFieldDefList()) {
                            if (entityFieldDef.isChildRef()) {
                                EntityClassDef _refEntityClassDef = get(entityFieldDef.getRefDef().getEntity());
                                onCreate(_refEntityClassDef);
                                if (entityFieldDef.isChildList()) {
                                    childListArgs.add(new ArgumentTypeInfo(entityFieldDef.getFieldName(),
                                            _refEntityClassDef.getEntityClass().getName()));
                                }
                            }
                        }

                        if (!DataUtils.isBlank(childListArgs)) {
                            ReflectUtils.updateTypePropertyArgumentTypes(entityClassDef.getEntityClass().getName(),
                                    childListArgs);
                        }
                        // TODO Clear all utilities class information caches
                    }

                    return true;
                }

            };

        this.entityDefFactoryMap = new FactoryMap<String, EntityDef>(true)
            {
                @Override
                protected boolean stale(String longName, EntityDef entityDef) throws Exception {
                    if (!"application.propertyItem".equals(longName)) {
                        return environment().value(long.class, "versionNo",
                                new AppEntityQuery().id(entityDef.getId())) > entityDef.getVersion();
                    }

                    return false;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected EntityDef create(String longName, Object... arg1) throws Exception {
                    final WidgetTypeDef textWidgetTypeDef = widgetDefFactoryMap.get("application.text");
                    if ("application.propertyItem".equals(longName)) {
                        EntityDef.Builder edb = EntityDef.newBuilder(ConfigType.STATIC,
                                PropertyListItem.class.getName(),
                                getApplicationMessage("application.propertyitem.label"), null, null, false, false,
                                "application.propertyItem", getApplicationMessage("application.propertyitem"), 0L, 1L);
                        edb.addFieldDef(textWidgetTypeDef, textWidgetTypeDef, EntityFieldDataType.STRING,
                                EntityFieldType.STATIC, "name", getApplicationMessage("application.propertyitem.name"));
                        edb.addFieldDef(textWidgetTypeDef, textWidgetTypeDef, EntityFieldDataType.STRING,
                                EntityFieldType.STATIC, "description",
                                getApplicationMessage("application.propertyitem.description"));
                        edb.addFieldDef(textWidgetTypeDef, textWidgetTypeDef, EntityFieldDataType.STRING,
                                EntityFieldType.STATIC, "value",
                                getApplicationMessage("application.propertyitem.value"));
                        edb.addFieldDef(textWidgetTypeDef, textWidgetTypeDef, EntityFieldDataType.STRING,
                                EntityFieldType.STATIC, "displayValue",
                                getApplicationMessage("application.propertyitem.displayvalue"));
                        return edb.build();
                    }

                    AppEntity appEntity = getApplicationEntity(AppEntity.class, longName);
                    EntityDef.Builder edb = EntityDef.newBuilder(appEntity.getBaseType(), appEntity.getConfigType(),
                            appEntity.getEntityClass(), appEntity.getTableName(), appEntity.getLabel(),
                            appEntity.getEmailProducerConsumer(), appEntity.getDelegate(), appEntity.getAuditable(),
                            appEntity.getReportable(), longName, appEntity.getDescription(), appEntity.getId(),
                            appEntity.getVersionNo());

                    for (AppEntityField appEntityField : appEntity.getFieldList()) {
                        WidgetTypeDef inputWidgetTypeDef = null;
                        if (!StringUtils.isBlank(appEntityField.getInputWidget())) {
                            inputWidgetTypeDef = getWidgetTypeDef(appEntityField.getInputWidget());
                        }

                        WidgetTypeDef lingualWidgetTypeDef = null;
                        EntityFieldDataType type = appEntityField.getDataType();
                        if (!StringUtils.isBlank(appEntityField.getLingualWidget())) {
                            lingualWidgetTypeDef = getWidgetTypeDef(appEntityField.getLingualWidget());
                        } else {
                            if (EntityFieldDataType.STRING.equals(type)) {
                                lingualWidgetTypeDef = getWidgetTypeDef("application.lingualstringtypelist");
                            } else if (type.isDate() || type.isTimestamp()) {
                                lingualWidgetTypeDef = getWidgetTypeDef("application.lingualdatetypelist");
                            }
                        }

                        String references = appEntityField.getReferences();
                        if (type.isEntityRef() || (!appEntityField.getDataType().isEnumDataType()
                                && !StringUtils.isBlank(references))) {
                            edb.addFieldDef(textWidgetTypeDef, inputWidgetTypeDef, lingualWidgetTypeDef,
                                    getRefDef(references), appEntityField.getDataType(), appEntityField.getType(),
                                    appEntityField.getTextCase(), appEntityField.getName(), appEntityField.getLabel(),
                                    appEntityField.getColumnName(), appEntityField.getCategory(),
                                    appEntityField.getSuggestionType(), appEntityField.getInputLabel(),
                                    appEntityField.getInputListKey(), appEntityField.getLingualListKey(),
                                    appEntityField.getAutoFormat(), appEntityField.getDefaultVal(), references,
                                    appEntityField.getKey(), appEntityField.getProperty(), appEntityField.getRows(),
                                    appEntityField.getColumns(), appEntityField.getMinLen(), appEntityField.getMaxLen(),
                                    appEntityField.getPrecision(), appEntityField.getScale(),
                                    appEntityField.isAllowNegative(), appEntityField.isNullable(),
                                    appEntityField.isAuditable(), appEntityField.isReportable(),
                                    appEntityField.isMaintainLink(), appEntityField.isBasicSearch(),
                                    appEntityField.isDescriptive());
                        } else {
                            edb.addFieldDef(textWidgetTypeDef, inputWidgetTypeDef, lingualWidgetTypeDef,
                                    appEntityField.getDataType(), appEntityField.getType(),
                                    appEntityField.getTextCase(), appEntityField.getName(), appEntityField.getLabel(),
                                    appEntityField.getColumnName(), appEntityField.getCategory(),
                                    appEntityField.getSuggestionType(), appEntityField.getInputLabel(),
                                    appEntityField.getInputListKey(), appEntityField.getLingualListKey(),
                                    appEntityField.getAutoFormat(), appEntityField.getDefaultVal(), references,
                                    appEntityField.getKey(), appEntityField.getProperty(), appEntityField.getRows(),
                                    appEntityField.getColumns(), appEntityField.getMinLen(), appEntityField.getMaxLen(),
                                    appEntityField.getPrecision(), appEntityField.getScale(),
                                    appEntityField.isAllowNegative(), appEntityField.isNullable(),
                                    appEntityField.isAuditable(), appEntityField.isReportable(),
                                    appEntityField.isMaintainLink(), appEntityField.isBasicSearch(),
                                    appEntityField.isDescriptive());
                        }

                    }

                    for (AppEntityAttachment appEntityAttachment : appEntity.getAttachmentList()) {
                        edb.addAttachmentDef(appEntityAttachment.getType(), appEntityAttachment.getName(),
                                appEntityAttachment.getDescription());
                    }

                    for (AppEntityExpression appEntityExpression : appEntity.getExpressionList()) {
                        edb.addExpressionDef(appEntityExpression.getName(), appEntityExpression.getDescription(),
                                appEntityExpression.getExpression());
                    }

                    for (AppEntityUniqueConstraint appUniqueConstraint : appEntity.getUniqueConstraintList()) {
                        boolean caseInsensitive = true; // TODO Get from appUniqueConstraint
                        edb.addUniqueConstraintDef(appUniqueConstraint.getName(), appUniqueConstraint.getDescription(),
                                DataUtils.convert(List.class, String.class, appUniqueConstraint.getFieldList(), null),
                                caseInsensitive);
                    }

                    for (AppEntityIndex appIndex : appEntity.getIndexList()) {
                        edb.addIndexDef(appIndex.getName(), appIndex.getDescription(),
                                DataUtils.convert(List.class, String.class, appIndex.getFieldList(), null));
                    }

                    for (AppEntityUpload appUpload : appEntity.getUploadList()) {
                        edb.addUploadDef(appUpload.getName(), appUpload.getDescription(),
                                InputWidgetUtils.getFieldSequenceDef(appUpload.getFieldSequence()),
                                appUpload.getConstraintAction());
                    }

                    return edb.build();
                }

            };

        this.entityDefByClassFactoryMap = new FactoryMap<String, EntityDef>(true)
            {
                @Override
                protected boolean stale(String entityClass, EntityDef entityDef) throws Exception {
                    if (!PropertyListItem.class.getName().equals(entityClass)) {
                        return (environment().value(long.class, "versionNo",
                                new AppEntityQuery().id(entityDef.getId())) > entityDef.getVersion());
                    }

                    return false;
                }

                @Override
                protected EntityDef create(String entityClass, Object... arg1) throws Exception {
                    AppEntity appEntity = environment().find(new AppEntityQuery().entityClass(entityClass));
                    return getEntityDef(ApplicationNameUtils
                            .getApplicationEntityLongName(appEntity.getApplicationName(), appEntity.getName()));
                }

            };

        this.refDefFactoryMap = new FactoryMap<String, RefDef>(true)
            {
                @Override
                protected boolean stale(String longName, RefDef refDef) throws Exception {
                    return (environment().value(long.class, "versionNo", new AppRefQuery().id(refDef.getId())) > refDef
                            .getVersion());
                }

                @Override
                protected RefDef create(String longName, Object... arg1) throws Exception {
                    AppRef appRef = getApplicationEntity(AppRef.class, longName);
                    FilterDef filterDef = InputWidgetUtils.getFilterDef(appletUtilities, appRef.getFilterGenerator(),
                            appRef.getFilter());
                    List<StringToken> listFormat = !StringUtils.isBlank(appRef.getListFormat())
                            ? StringUtils.breakdownParameterizedString(appRef.getListFormat())
                            : null;
                    return new RefDef(appRef.getEntity(), appRef.getSearchField(), appRef.getSearchTable(),
                            appRef.getSelectHandler(), filterDef, listFormat, longName, appRef.getDescription(),
                            appRef.getId(), appRef.getVersionNo());
                }

            };

        this.tableDefFactoryMap = new FactoryMap<String, TableDef>(true)
            {

                @Override
                protected boolean stale(String longName, TableDef tableDef) throws Exception {
                    if (!"application.propertyItemTable".equals(longName)) {
                        return (environment().value(long.class, "versionNo",
                                new AppTableQuery().id(tableDef.getId())) > tableDef.getVersion())
                                || (tableDef.getEntityDef()
                                        .getVersion() != getEntityDef(tableDef.getEntityDef().getLongName())
                                                .getVersion());
                    }

                    return false;
                }

                @Override
                protected TableDef create(String longName, Object... arg1) throws Exception {
                    if ("application.propertyItemTable".equals(longName)) {
                        TableDef.Builder tdb = TableDef.newBuilder(getEntityDef("application.propertyItem"),
                                getApplicationMessage("application.propertyitem.table.label"), false, false,
                                "application.propertyItemTable",
                                getApplicationMessage("application.propertyitem.table.description"), 0L, 1L);
                        WidgetTypeDef widgetTypeDef = getWidgetTypeDef("application.text");
                        String renderer = widgetTypeDef.getRenderer();
                        tdb.addColumnDef("name", renderer, null, 2, false, false, false, true, false, false);
                        tdb.addColumnDef("description", renderer, null, 2, false, false, false, true, false, false);
                        tdb.addColumnDef(getApplicationMessage("application.propertyitem.value"), "displayValue",
                                renderer, null, null, 2, false, false, false, true, false, false);

                        tdb.itemsPerPage(-1);
                        return tdb.build();
                    }

                    AppTable appTable = getApplicationEntity(AppTable.class, longName);
                    final EntityDef entityDef = getEntityDef(appTable.getEntity());
                    TableDef.Builder tdb = TableDef.newBuilder(entityDef, appTable.getLabel(), appTable.isSerialNo(),
                            appTable.isSortable(), longName, appTable.getDescription(), appTable.getId(),
                            appTable.getVersionNo());

                    for (AppTableFilter appTableFilter : appTable.getFilterList()) {
                        FilterDef _filterDef = InputWidgetUtils.getFilterDef(appletUtilities, appTableFilter.getName(),
                                appTableFilter.getDescription(), appTableFilter.getFilterGenerator(),
                                appTableFilter.getFilter());
                        if (_filterDef != null) {
                            tdb.addFilterDef(new TableFilterDef(_filterDef, appTableFilter.getRowColor(),
                                    appTableFilter.getLegendLabel()));
                        }
                    }

                    for (AppTableColumn appTableColumn : appTable.getColumnList()) {
                        final EntityFieldDef entityFieldDef = entityDef.getFieldDef(appTableColumn.getField());
                        String renderer = InputWidgetUtils
                                .constructRenderer(getWidgetTypeDef(appTableColumn.getRenderWidget()), entityFieldDef);
                        String editor = !entityFieldDef.isListOnly()
                                ? InputWidgetUtils.constructEditor(getWidgetTypeDef(appTableColumn.getRenderWidget()),
                                        entityFieldDef)
                                : null;
                        OrderType order = OrderType.fromCode(appTableColumn.getOrder());
                        tdb.addColumnDef(appTableColumn.getLabel(), appTableColumn.getField(), renderer, editor,
                                appTableColumn.getLinkAct(), order, appTableColumn.getWidthRatio(),
                                appTableColumn.isSwitchOnChange(), appTableColumn.isHidden(),
                                appTableColumn.isDisabled(), appTableColumn.isEditable(), appTableColumn.isSortable(),
                                appTableColumn.isSummary());
                    }

                    for (AppTableAction appTableAction : appTable.getActionList()) {
                        tdb.addTableAction(appTableAction.getPolicy(), appTableAction.getLabel());
                    }

                    for (AppTableLoading appTableLoading : appTable.getLoadingList()) {
                        tdb.addTableLoadingDef(new TableLoadingDef(appTableLoading.getName(),
                                appTableLoading.getDescription(), appTableLoading.getLabel(),
                                appTableLoading.getProvider(), appTableLoading.getOrderIndex()));
                    }

                    tdb.detailsPanelName(appTable.getDetailsPanelName());
                    tdb.sortHistory(appTable.getSortHistory());
                    tdb.itemsPerPage(appTable.getItemsPerPage());
                    tdb.headerToUpperCase(appTable.isHeaderToUpperCase());
                    tdb.headerCenterAlign(appTable.isHeaderCenterAlign());
                    tdb.basicSearch(appTable.isBasicSearch());
                    tdb.totalSummary(appTable.isTotalSummary());
                    tdb.headerless(appTable.isHeaderless());
                    tdb.multiSelect(appTable.isMultiSelect());
                    tdb.nonConforming(appTable.isNonConforming());
                    tdb.fixedRows(appTable.isFixedRows());
                    tdb.limitSelectToColumns(appTable.isLimitSelectToColumns());
                    return tdb.build();
                }

            };

        this.formDefFactoryMap = new FactoryMap<String, FormDef>(true)
            {

                @Override
                protected boolean stale(String longName, FormDef formDef) throws Exception {
                    return (environment().value(long.class, "versionNo",
                            new AppFormQuery().id(formDef.getId())) > formDef.getVersion())
                            || (formDef.getEntityDef()
                                    .getVersion() != getEntityDef(formDef.getEntityDef().getLongName()).getVersion());
                }

                @SuppressWarnings("unchecked")
                @Override
                protected FormDef create(String longName, Object... arg1) throws Exception {
                    AppForm appForm = getApplicationEntity(AppForm.class, longName);
                    final boolean useFormColorScheme = systemModuleService.getSysParameterValue(boolean.class,
                            ApplicationModuleSysParamConstants.USE_APPLICATION_FORM_COLOR_SCHEME);
                    final EntityDef entityDef = getEntityDef(appForm.getEntity());
                    FormDef.Builder fdb = FormDef.newBuilder(appForm.getType(), entityDef,
                            appForm.getConsolidatedValidation(), appForm.getConsolidatedReview(),
                            appForm.getConsolidatedState(), appForm.getListingGenerator(), longName,
                            appForm.getDescription(), appForm.getId(), appForm.getVersionNo());
                    Map<String, FieldRenderInfo> fieldRenderInfos = new HashMap<String, FieldRenderInfo>();
                    int tabIndex = -1;
                    int sectionIndex = -1;
                    for (AppFormElement appFormElement : appForm.getElementList()) {
                        if (FormElementType.TAB.equals(appFormElement.getType())) {
                            tabIndex++;
                            sectionIndex = -1;
                            FilterGroupDef filterGroupDef = getFilterGroupDef(appFormElement.getTabApplet(),
                                    appFormElement.getFilter());
                            fdb.addFormTab(appFormElement.getTabContentType(), filterGroupDef,
                                    appFormElement.getElementName(), appFormElement.getLabel(),
                                    appFormElement.getTabApplet(), appFormElement.getTabReference(),
                                    appFormElement.getEditAction(), appFormElement.getEditFormless(),
                                    appFormElement.getEditFixedRows(), appFormElement.isIgnoreParentCondition(),
                                    appFormElement.isShowSearch(), appFormElement.isVisible(),
                                    appFormElement.isEditable(), appFormElement.isDisabled());
                        } else if (FormElementType.SECTION.equals(appFormElement.getType())) {
                            sectionIndex++;
                            fdb.addFormSection(tabIndex, appFormElement.getElementName(), appFormElement.getLabel(),
                                    appFormElement.getSectionColumns(), appFormElement.isVisible(),
                                    appFormElement.isEditable(), appFormElement.isDisabled());
                        } else {
                            // FIELD
                            final String fieldName = appFormElement.getElementName();
                            if (entityDef.isWithFieldDef(fieldName)) {
                                EntityFieldDef entityFieldDef = entityDef.getFieldDef(fieldName);
                                WidgetTypeDef widgetTypeDef = !StringUtils.isBlank(appFormElement.getInputWidget())
                                        ? getWidgetTypeDef(appFormElement.getInputWidget())
                                        : entityFieldDef.getInputWidgetTypeDef();
                                widgetTypeDef = widgetTypeDef != null ? widgetTypeDef
                                        : getWidgetTypeDef("application.text");
                                WidgetColor color = appFormElement.getColor();
                                if (useFormColorScheme) {
                                    color = entitySearchTypes.contains(widgetTypeDef.getLongName()) ? WidgetColor.PURPLE
                                            : (entityFieldDef.isWithAutoFormat() ? WidgetColor.NAVY_GRAY : color);
                                }

                                fieldRenderInfos.put(fieldName,
                                        new FieldRenderInfo(appFormElement.getInputReference(), color));
                                String renderer = InputWidgetUtils.constructEditorWithBinding(widgetTypeDef,
                                        entityFieldDef, appFormElement.getInputReference(), color);
                                RefDef inputRefDef = !StringUtils.isBlank(appFormElement.getInputReference())
                                        ? getRefDef(appFormElement.getInputReference())
                                        : entityFieldDef.getRefDef();

                                String label = appFormElement.getLabel();
                                if (label == null) {
                                    label = entityFieldDef.getFieldLabel();
                                }

                                fdb.addFormField(tabIndex, sectionIndex, entityFieldDef, widgetTypeDef, inputRefDef,
                                        label, renderer, appFormElement.getFieldColumn(),
                                        appFormElement.isSwitchOnChange(), appFormElement.isSaveAs(),
                                        appFormElement.isRequired(), appFormElement.isVisible(),
                                        appFormElement.isEditable(), appFormElement.isDisabled());
                            }
                        }
                    }

                    List<StringToken> titleFormat = !StringUtils.isBlank(appForm.getTitleFormat())
                            ? StringUtils.breakdownParameterizedString(appForm.getTitleFormat())
                            : null;
                    fdb.titleFormat(titleFormat);

                    for (AppFormFilter appFormFilter : appForm.getFilterList()) {
                        FilterDef filterDef = InputWidgetUtils.getFilterDef(appletUtilities, appFormFilter.getName(),
                                appFormFilter.getDescription(), appFormFilter.getFilterGenerator(),
                                appFormFilter.getFilter());
                        if (filterDef != null) {
                            fdb.addFilterDef(new FormFilterDef(filterDef));
                        }
                    }

                    for (AppFormAnnotation appFormAnnotation : appForm.getAnnotationList()) {
                        fdb.addFormAnnotation(appFormAnnotation.getType(), appFormAnnotation.getName(),
                                appFormAnnotation.getDescription(), appFormAnnotation.getMessage(),
                                appFormAnnotation.isHtml());
                    }

                    for (AppFormAction appFormAction : appForm.getActionList()) {
                        fdb.addFormAction(appFormAction.getType(), appFormAction.getHighlightType(),
                                appFormAction.getName(), appFormAction.getDescription(),
                                resolveApplicationMessage(appFormAction.getLabel()), appFormAction.getSymbol(),
                                appFormAction.getStyleClass(), appFormAction.getPolicy(), appFormAction.getOrderIndex(),
                                appFormAction.isShowOnCreate(), appFormAction.isShowOnMaintain(),
                                appFormAction.isValidateForm(),
                                InputWidgetUtils.getFilterDef(appletUtilities, null, appFormAction.getOnCondition()));
                    }

                    for (AppFormRelatedList appFormRelatedList : appForm.getRelatedList()) {
                        FilterGroupDef _filterGroupDef = getFilterGroupDef(appFormRelatedList.getApplet(),
                                appFormRelatedList.getFilter());
                        fdb.addRelatedList(_filterGroupDef, appFormRelatedList.getName(),
                                appFormRelatedList.getDescription(), appFormRelatedList.getLabel(),
                                appFormRelatedList.getApplet(), appFormRelatedList.getEditAction());
                    }

                    for (AppFormStatePolicy appFormStatePolicy : appForm.getFieldStateList()) {
                        SetStatesDef.Builder ssdb = SetStatesDef.newBuilder();
                        for (AppFormSetState appFormSetState : appFormStatePolicy.getSetStateList()) {
                            ssdb.addSetStateDef(appFormSetState.getType(),
                                    DataUtils.convert(List.class, String.class, appFormSetState.getTarget(), null),
                                    appFormSetState.getRequired(), appFormSetState.getVisible(),
                                    appFormSetState.getEditable(), appFormSetState.getDisabled());
                        }

                        fdb.addFormStatePolicy(appFormStatePolicy.getName(), appFormStatePolicy.getDescription(),
                                appFormStatePolicy.getType(),
                                InputWidgetUtils
                                        .getFilterDef(appletUtilities, null, appFormStatePolicy.getOnCondition()),
                                ssdb.build(),
                                InputWidgetUtils.getSetValuesDef(appFormStatePolicy.getValueGenerator(),
                                        appFormStatePolicy.getSetValues()),
                                DataUtils.convert(List.class, String.class, appFormStatePolicy.getTrigger(), null));
                    }

                    for (AppFormWidgetRulesPolicy appFormWidgetRulesPolicy : appForm.getWidgetRulesList()) {
                        WidgetRulesDef widgetRulesDef = InputWidgetUtils
                                .getWidgetRulesDef(appFormWidgetRulesPolicy.getWidgetRules());
                        Map<String, String> ruleEditors = new HashMap<String, String>();
                        for (WidgetRuleEntryDef widgetRuleEntryDef : widgetRulesDef.getWidgetRuleEntryList()) {
                            if (widgetRuleEntryDef.isPresent()) {
                                final String fieldName = widgetRuleEntryDef.getFieldName();
                                FieldRenderInfo fieldRenderInfo = fieldRenderInfos.get(fieldName);
                                WidgetTypeDef widgetTypeDef = getWidgetTypeDef(widgetRuleEntryDef.getWidget());
                                EntityFieldDef entityFieldDef = entityDef.getFieldDef(fieldName);
                                String renderer = InputWidgetUtils.constructEditorWithBinding(widgetTypeDef,
                                        entityFieldDef, fieldRenderInfo.getReference(), fieldRenderInfo.getColor());
                                ruleEditors.put(fieldName, renderer);
                            }
                        }

                        fdb.addFormWidgetRulesPolicy(appFormWidgetRulesPolicy.getName(),
                                appFormWidgetRulesPolicy.getDescription(), InputWidgetUtils
                                        .getFilterDef(appletUtilities, null, appFormWidgetRulesPolicy.getOnCondition()),
                                widgetRulesDef, ruleEditors);
                    }

                    for (AppFormFieldValidationPolicy appFormFieldValidationPolicy : appForm.getFieldValidationList()) {
                        fdb.addFieldValidationPolicy(appFormFieldValidationPolicy.getName(),
                                appFormFieldValidationPolicy.getDescription(),
                                appFormFieldValidationPolicy.getFieldName(),
                                appFormFieldValidationPolicy.getValidation(), appFormFieldValidationPolicy.getRule());
                    }

                    for (AppFormValidationPolicy appFormValidationPolicy : appForm.getFormValidationList()) {
                        fdb.addFormValidationPolicy(
                                InputWidgetUtils.getFilterDef(appletUtilities, null,
                                        appFormValidationPolicy.getErrorCondition()),
                                appFormValidationPolicy.getName(), appFormValidationPolicy.getDescription(),
                                appFormValidationPolicy.getMessage(), appFormValidationPolicy.getErrorMatcher(),
                                DataUtils.convert(List.class, String.class, appFormValidationPolicy.getTarget(), null));
                    }

                    for (AppFormReviewPolicy appFormReviewPolicy : appForm.getFormReviewList()) {
                        fdb.addFormReviewPolicy(
                                DataUtils.convert(List.class, FormReviewType.class, appFormReviewPolicy.getFormEvents(),
                                        null),
                                InputWidgetUtils.getFilterDef(appletUtilities, null,
                                        appFormReviewPolicy.getErrorCondition()),
                                appFormReviewPolicy.getName(), appFormReviewPolicy.getDescription(),
                                appFormReviewPolicy.getMessageType(), appFormReviewPolicy.getMessage(),
                                appFormReviewPolicy.getErrorMatcher(),
                                DataUtils.convert(List.class, String.class, appFormReviewPolicy.getTarget(), null),
                                appFormReviewPolicy.isSkippable());
                    }

                    return fdb.build();
                }

            };

        this.assignmentPageDefFactoryMap = new FactoryMap<String, AssignmentPageDef>(true)
            {

                @Override
                protected boolean stale(String longName, AssignmentPageDef assignmentPageDef) throws Exception {
                    return (environment().value(long.class, "versionNo", new AppAssignmentPageQuery()
                            .id(assignmentPageDef.getId())) > assignmentPageDef.getVersion());
                }

                @Override
                protected AssignmentPageDef create(String longName, Object... arg1) throws Exception {
                    AppAssignmentPage appAssignmentPage = getApplicationEntity(AppAssignmentPage.class, longName);
                    return new AssignmentPageDef(longName, appAssignmentPage.getDescription(),
                            appAssignmentPage.getId(), appAssignmentPage.getVersionNo(), appAssignmentPage.getLabel(),
                            appAssignmentPage.getEntity(), appAssignmentPage.getCommitPolicy(),
                            appAssignmentPage.getBaseField(), appAssignmentPage.getAssignField(),
                            appAssignmentPage.getFilterCaption1(), appAssignmentPage.getFilterCaption2(),
                            appAssignmentPage.getFilterList1(), appAssignmentPage.getFilterList2(),
                            appAssignmentPage.getAssignCaption(), appAssignmentPage.getUnassignCaption(),
                            appAssignmentPage.getAssignList(), appAssignmentPage.getUnassignList(),
                            appAssignmentPage.getRuleDescField());
                }

            };

        this.propertyListDefMap = new FactoryMap<String, PropertyListDef>()
            {

                @Override
                protected boolean stale(String longName, PropertyListDef propertyListDef) throws Exception {
                    return (environment().value(long.class, "versionNo",
                            new AppPropertyListQuery().id(propertyListDef.getId())) > propertyListDef.getVersion());
                }

                @Override
                protected PropertyListDef create(String longName, Object... params) throws Exception {
                    AppPropertyList appPropertyList = getApplicationEntity(AppPropertyList.class, longName);
                    PropertyListDef.Builder pldb = PropertyListDef.newBuilder(longName,
                            appPropertyList.getDescription(), appPropertyList.getId(), appPropertyList.getVersionNo());

                    final WidgetTypeDef textWidgetTypeDef = widgetDefFactoryMap.get("application.text");
                    for (AppPropertySet set : appPropertyList.getItemSet()) {
                        pldb.addSetDef(set.getLabel());
                        for (AppPropertyListItem listItem : set.getItemList()) {
                            WidgetTypeDef widgetTypeDef = getWidgetTypeDef(listItem.getInputWidget());
                            RefDef refDef = null; // TODO
                            String filterListKey = null; // TODO
                            EntityFieldDef entityFieldDef = new EntityFieldDef(textWidgetTypeDef, widgetTypeDef, refDef,
                                    longName, listItem.getName(), listItem.getReferences(), filterListKey);
                            String renderer = InputWidgetUtils.constructEditorWithBinding(widgetTypeDef,
                                    entityFieldDef);
                            pldb.addItemDef(entityFieldDef, widgetTypeDef, set.getLabel(), listItem.getDescription(),
                                    renderer, listItem.getDefaultVal(), listItem.isRequired(), listItem.isMask(),
                                    listItem.isEncrypt());
                        }
                    }

                    return pldb.build();
                }

            };

        this.propertyRuleDefMap = new FactoryMap<String, PropertyRuleDef>()
            {

                @Override
                protected boolean stale(String longName, PropertyRuleDef propertyRuleDef) throws Exception {
                    return (environment().value(long.class, "versionNo",
                            new AppPropertyRuleQuery().id(propertyRuleDef.getId())) > propertyRuleDef.getVersion());
                }

                @Override
                protected PropertyRuleDef create(String longName, Object... params) throws Exception {
                    AppPropertyRule appPropertyRule = getApplicationEntity(AppPropertyRule.class, longName);
                    PropertyListDef defaultList = null;
                    if (!StringUtils.isBlank(appPropertyRule.getDefaultList())) {
                        defaultList = getPropertyListDef(appPropertyRule.getDefaultList());
                    }

                    PropertyRuleDef.Builder prdb = PropertyRuleDef.newBuilder(getEntityDef(appPropertyRule.getEntity()),
                            appPropertyRule.getChoiceField(), appPropertyRule.getListField(),
                            appPropertyRule.getPropNameField(), appPropertyRule.getPropValField(), defaultList,
                            appPropertyRule.isIgnoreCase(), longName, appPropertyRule.getDescription(),
                            appPropertyRule.getId(), appPropertyRule.getVersionNo());

                    for (AppPropertyRuleChoice appPropertyRuleChoice : appPropertyRule.getChoiceList()) {
                        prdb.addChoiceDef(getPropertyListDef(appPropertyRuleChoice.getList()),
                                appPropertyRuleChoice.getName());
                    }

                    return prdb.build();
                }
            };
    }

    public final void setSystemModuleService(SystemModuleService systemModuleService) {
        this.systemModuleService = systemModuleService;
    }

    public final void setApplicationPrivilegeManager(ApplicationPrivilegeManager applicationPrivilegeManager) {
        this.applicationPrivilegeManager = applicationPrivilegeManager;
    }

    public final void setMessageResolver(MessageResolver messageResolver) {
        this.messageResolver = messageResolver;
    }

    public final void setConfigurationLoader(ConfigurationLoader configurationLoader) {
        this.configurationLoader = configurationLoader;
    }

    public final void setDynamicSqlEntityLoader(DynamicSqlEntityLoader dynamicSqlEntityLoader) {
        this.dynamicSqlEntityLoader = dynamicSqlEntityLoader;
    }

    public final void setListManager(ListManager listManager) {
        this.listManager = listManager;
    }

    public final void setAppletUtilities(AppletUtilities appletUtilities) {
        this.appletUtilities = appletUtilities;
    }

    public final void setTwoWayStringCryptograph(TwoWayStringCryptograph twoWayStringCryptograph) {
        this.twoWayStringCryptograph = twoWayStringCryptograph;
    }

    @Override
    public FilterGroupDef getFilterGroupDef(String appletName, String tabFilter) throws UnifyException {
        if (!StringUtils.isBlank(appletName)) {
            AppletDef _appletDef = getAppletDef(appletName);
            EntityDef _entityDef = getEntityDef(_appletDef.getEntity());
            FilterGroupDef.Builder fgdb = FilterGroupDef.newBuilder(_entityDef);
            if (!StringUtils.isBlank(tabFilter)) {
                fgdb.addFilter(FilterType.TAB, _appletDef.getFilterDef(tabFilter));
            }

            String baseCondition = _appletDef.getPropValue(String.class, AppletPropertyConstants.BASE_RESTRICTION);
            if (!StringUtils.isBlank(baseCondition)) {
                fgdb.addFilter(FilterType.BASE, _appletDef.getFilterDef(baseCondition));
            }

            String updateCondition = _appletDef.getPropValue(String.class,
                    AppletPropertyConstants.MAINTAIN_FORM_UPDATE_CONDITION);
            if (!StringUtils.isBlank(updateCondition)) {
                fgdb.addFilter(FilterType.MAINTAIN_UPDATE, _appletDef.getFilterDef(updateCondition));
            }

            String deleteCondition = _appletDef.getPropValue(String.class,
                    AppletPropertyConstants.MAINTAIN_FORM_DELETE_CONDITION);
            if (!StringUtils.isBlank(deleteCondition)) {
                fgdb.addFilter(FilterType.MAINTAIN_DELETE, _appletDef.getFilterDef(deleteCondition));
            }

            return fgdb.build();
        }

        return null;
    }

    @Override
    public EntityAuditInfo getEntityAuditInfo(Object entityDef) throws UnifyException {
        EntityDef _entityDef = (EntityDef) entityDef;
        if (_entityDef.isAuditable()) {
            return new EntityAuditInfo(_entityDef.getAuditFieldNames(), true);
        }

        return new EntityAuditInfo();
    }

    @Override
    public void saveSuggestions(Object entityDef, Entity inst) throws UnifyException {
        EntityDef _entityDef = (EntityDef) entityDef;
        if (_entityDef.isWithSuggestionFields()) {
            if (systemModuleService.getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.AUTOSAVE_SUGGESTIONS)) {
                for (EntityFieldDef entityFieldDef : _entityDef.getSuggestionFieldDefList()) {
                    String suggestion = DataUtils.getBeanProperty(String.class, inst, entityFieldDef.getFieldName());
                    if (!StringUtils.isBlank(suggestion)) {
                        final String suggestionTypeLongName = entityFieldDef.getSuggestionType() != null
                                ? entityFieldDef.getSuggestionType()
                                : entityFieldDef.getFieldLongName();
                        ApplicationEntityNameParts parts = ApplicationNameUtils
                                .getApplicationEntityNameParts(suggestionTypeLongName);
                        suggestion = suggestion.trim();
                        AppSuggestionQuery query = (AppSuggestionQuery) new AppSuggestionQuery()
                                .addEquals("applicationName", parts.getApplicationName())
                                .addEquals("suggestionTypeName", parts.getEntityName())
                                .addIEquals("suggestion", suggestion);
                        if (parts.isWithFieldName()) {
                            query.addEquals("fieldName", entityFieldDef.getFieldName());
                        }

                        if (environment().countAll(query) == 0) {
                            SuggestionTypeDef suggestionTypeDef = getSuggestionTypeDef(suggestionTypeLongName);
                            AppSuggestion appSuggestion = new AppSuggestion();
                            appSuggestion.setSuggestionTypeId(suggestionTypeDef.getId());
                            appSuggestion.setSuggestion(suggestion);
                            appSuggestion.setFieldName(entityFieldDef.getFieldName());
                            environment().create(appSuggestion);
                        }

                    }
                }
            }
        }
    }

    @Override
    public boolean isEntitySearchWidget(String widgetLongName) throws UnifyException {
        return entitySearchTypes.contains(widgetLongName);
    }

    @Override
    public boolean isApplicationDevelopable(Long applicationId) throws UnifyException {
        return environment().value(boolean.class, "developable", new ApplicationQuery().id(applicationId));
    }

    @Override
    public String getApplicationName(Long applicationId) throws UnifyException {
        return environment().value(String.class, "name", new ApplicationQuery().id(applicationId));
    }

    @Override
    public String getAppEntity(Long appEntityId) throws UnifyException {
        AppEntity appEntity = environment()
                .listLean(new AppEntityQuery().id(appEntityId).addSelect("applicationName", "name"));
        return ApplicationNameUtils.getApplicationEntityLongName(appEntity.getApplicationName(), appEntity.getName());
    }

    @Override
    public List<AppEntityUpload> findAppEntityUploads(AppEntityUploadQuery query) throws UnifyException {
        return environment().findAll(query);
    }

    @Override
    public List<AppEntityField> getEntityBaseTypeFieldList(EntityBaseType type, ConfigType configType)
            throws UnifyException {
        return ApplicationEntityUtils.getEntityBaseTypeFieldList(messageResolver, type, configType);
    }

    @Override
    public Long createAppRef(AppRef appRef) throws UnifyException {
        return (Long) environment().create(appRef);
    }

    @Override
    public Long createApplication(Application application, Module module) throws UnifyException {
        logDebug("Creating application [{0}]...", application.getDescription());
        if (module != null) {
            Long moduleId = systemModuleService.createModule(module);
            application.setModuleId(moduleId);
        }

        Long applicationId = (Long) environment().create(application);
        final String applicationPrivilegeCode = PrivilegeNameUtils.getApplicationPrivilegeName(application.getName());
        applicationPrivilegeManager.registerPrivilege(applicationId,
                ApplicationPrivilegeConstants.APPLICATION_CATEGORY_CODE, applicationPrivilegeCode,
                application.getDescription());
        UserToken userToken = getUserToken();
        if (!userToken.isReservedUser() && systemModuleService.getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.ASSIGN_APPLICATIONENTITY_ONCREATE)) {
            applicationPrivilegeManager.assignPrivilegeToRole(userToken.getRoleCode(), applicationPrivilegeCode);
        }

        return applicationId;
    }

    @Override
    public List<Application> findApplications(ApplicationQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public Application findApplication(String applicationName) throws UnifyException {
        return environment().list(new ApplicationQuery().name(applicationName));
    }

    @Override
    public List<AppApplet> findManageEntityListApplets(String entity) throws UnifyException {
        return environment().listAll(new AppAppletQuery().typeIn(AppletType.MANAGE_ENTITY_LIST_TYPES).entity(entity));
    }

    @Override
    public List<AppApplet> findAppApplets(AppAppletQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public List<AppApplet> findFormRelatedAppApplets(Long formId) throws UnifyException {
        logDebug("Finding related application applets for form [{0}]...", formId);
        String entityName = environment().value(String.class, "entity", new AppFormQuery().id(formId));
        List<AppRef> refList = environment()
                .listAll(new AppRefQuery().entity(entityName).addSelect("name", "applicationName"));
        if (!DataUtils.isBlank(refList)) {
            List<String> refNameList = new ArrayList<String>();
            for (AppRef appRef : refList) {
                refNameList.add(ApplicationNameUtils.getApplicationEntityLongName(appRef.getApplicationName(),
                        appRef.getName()));
            }

            List<AppEntityField> fieldList = environment()
                    .listAll(new AppEntityFieldQuery().dataType(EntityFieldDataType.REF).referencesIn(refNameList)
                            .addSelect("appEntityName", "applicationName"));
            if (!DataUtils.isBlank(fieldList)) {
                Set<String> entityNameList = new HashSet<String>();
                for (AppEntityField appEntityField : fieldList) {
                    entityNameList.add(ApplicationNameUtils.getApplicationEntityLongName(
                            appEntityField.getApplicationName(), appEntityField.getAppEntityName()));
                }

                return environment().listAll(
                        new AppAppletQuery().typeIn(AppletType.MANAGE_ENTITY_LIST_TYPES).entityIn(entityNameList));
            }
        }

        return Collections.emptyList();
    }

    @Override
    public AppApplet findAppApplet(Long appletId) throws UnifyException {
        return environment().find(AppApplet.class, appletId);
    }

    @Override
    public String getAppAppletEntity(Long appAppletId) throws UnifyException {
        return environment().value(String.class, "entity", new AppAppletQuery().id(appAppletId));
    }

    @Override
    public String getAppTableEntity(Long appTableId) throws UnifyException {
        return environment().value(String.class, "entity", new AppTableQuery().id(appTableId));
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getAppComponentEntity(String componentEntityName, Long appComponentId) throws UnifyException {
        EntityClassDef entityClassDef = getEntityClassDef(componentEntityName);
        return environment().value(String.class, "entity",
                Query.of((Class<? extends Entity>) entityClassDef.getEntityClass()).addEquals("id", appComponentId));
    }

    @Override
    public List<AppAppletSetValues> findAppAppletSetValues(Long appAppletId) throws UnifyException {
        return environment().findAll(new AppAppletSetValuesQuery().appAppletId(appAppletId));
    }

    @Override
    public List<AppAppletFilter> findAppAppletFilters(Long appAppletId) throws UnifyException {
        return environment().findAll(new AppAppletFilterQuery().appAppletId(appAppletId));
    }

    @Override
    public List<AppAppletFilter> findAppAppletQuickFilters(Long appAppletId) throws UnifyException {
        return environment().findAll(new AppAppletFilterQuery().appAppletId(appAppletId).quickFilter(true));
    }

    @Override
    public List<AppAppletFilter> findAppAppletFilters(String appAppletName) throws UnifyException {
        ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(appAppletName);
        Long appAppletId = environment().value(Long.class, "id",
                new AppAppletQuery().applicationName(nameParts.getApplicationName()).name(nameParts.getEntityName()));
        return findAppAppletFilters(appAppletId);
    }

    @Override
    public List<AppAppletFilter> findAppAppletFilters(AppAppletFilterQuery query) throws UnifyException {
        return environment().findAll(query);
    }

    @Override
    public AppRef findAppRef(Long refId) throws UnifyException {
        return environment().find(AppRef.class, refId);
    }

    @Override
    public List<AppEntity> findAppEntities(AppEntityQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public AppEntity findAppEntity(Long entityId) throws UnifyException {
        return environment().find(AppEntity.class, entityId);
    }

    @Override
    public List<AppEntityField> findAppEntityFields(AppEntityFieldQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public AppTable findAppTable(Long tableId) throws UnifyException {
        return environment().find(AppTable.class, tableId);
    }

    @Override
    public AppEntityField findAppEntityField(AppEntityFieldQuery query) throws UnifyException {
        return environment().find(query);
    }

    @Override
    public List<AppEntityField> findFormRelatedAppEntityFields(Long formId, String appletName) throws UnifyException {
        logDebug("Finding related application entity fields for form [{0}] and applet [{1}]...", formId, appletName);
        String entityName = environment().value(String.class, "entity", new AppFormQuery().id(formId));
        List<AppRef> refList = environment()
                .listAll(new AppRefQuery().entity(entityName).addSelect("name", "applicationName"));
        if (!DataUtils.isBlank(refList)) {
            List<String> refNameList = new ArrayList<String>();
            for (AppRef appRef : refList) {
                refNameList.add(ApplicationNameUtils.getApplicationEntityLongName(appRef.getApplicationName(),
                        appRef.getName()));
            }

            ApplicationEntityNameParts anp = ApplicationNameUtils.getApplicationEntityNameParts(appletName);
            String appletEntityName = environment().value(String.class, "entity",
                    new AppAppletQuery().applicationName(anp.getApplicationName()).name(anp.getEntityName()));
            ApplicationEntityNameParts enp = ApplicationNameUtils.getApplicationEntityNameParts(appletEntityName);
            return environment()
                    .listAll(new AppEntityFieldQuery().dataType(EntityFieldDataType.REF).referencesIn(refNameList)
                            .applicationName(enp.getApplicationName()).appEntityName(enp.getEntityName()));
        }

        return Collections.emptyList();
    }

    @Override
    public List<AppEntityField> findFormAppEntityFields(Long formId) throws UnifyException {
        String entityName = environment().value(String.class, "entity", new AppFormQuery().id(formId));
        ApplicationEntityNameParts np = ApplicationNameUtils.getApplicationEntityNameParts(entityName);
        return environment().listAll(
                new AppEntityFieldQuery().applicationName(np.getApplicationName()).appEntityName(np.getEntityName()));
    }

    @Override
    public Set<String> findChildAppEntities(String entityName) throws UnifyException {
        logDebug("Finding child entities for [{0}]...", entityName);
        List<AppRef> refList = environment()
                .listAll(new AppRefQuery().entity(entityName).addSelect("name", "applicationName"));
        if (!DataUtils.isBlank(refList)) {
            List<String> refNames = new ArrayList<String>();
            for (AppRef appRef : refList) {
                refNames.add(ApplicationNameUtils.getApplicationEntityLongName(appRef.getApplicationName(),
                        appRef.getName()));
            }

            List<AppEntityField> fieldList = environment()
                    .listAll(new AppEntityFieldQuery().dataType(EntityFieldDataType.REF).referencesIn(refNames)
                            .addSelect("appEntityName", "applicationName"));
            if (!DataUtils.isBlank(fieldList)) {
                Set<String> result = new HashSet<String>();
                for (AppEntityField appEntityField : fieldList) {
                    result.add(ApplicationNameUtils.getApplicationEntityLongName(appEntityField.getApplicationName(),
                            appEntityField.getAppEntityName()));
                }

                return result;
            }
        }

        return Collections.emptySet();
    }

    @Override
    public Set<String> findBlobEntities(String entityName) throws UnifyException {
        logDebug("Finding BLOB entities for [{0}]...", entityName);
        List<AppEntityField> fieldList = environment().listAll(new AppEntityFieldQuery()
                .dataType(EntityFieldDataType.BLOB).addSelect("appEntityName", "applicationName"));
        if (!DataUtils.isBlank(fieldList)) {
            Set<String> result = new HashSet<String>();
            for (AppEntityField appEntityField : fieldList) {
                result.add(ApplicationNameUtils.getApplicationEntityLongName(appEntityField.getApplicationName(),
                        appEntityField.getAppEntityName()));
            }

            if (!result.isEmpty()) {
                List<AppRef> refList = environment()
                        .listAll(new AppRefQuery().entity(entityName).addSelect("name", "applicationName"));

                List<String> refNames = new ArrayList<String>();
                for (AppRef appRef : refList) {
                    refNames.add(ApplicationNameUtils.getApplicationEntityLongName(appRef.getApplicationName(),
                            appRef.getName()));
                }

                fieldList = environment().listAll(new AppEntityFieldQuery().dataType(EntityFieldDataType.REF)
                        .referencesIn(refNames).addSelect("appEntityName", "applicationName"));
                if (!DataUtils.isBlank(fieldList)) {
                    for (AppEntityField appEntityField : fieldList) {
                        String refEntityName = ApplicationNameUtils.getApplicationEntityLongName(
                                appEntityField.getApplicationName(), appEntityField.getAppEntityName());
                        result.remove(refEntityName);
                    }
                }

                return result;
            }
        }

        return Collections.emptySet();
    }

    @Override
    public void resolveEntityReferences(Entity inst, String entity) throws UnifyException {
        EntityDef entityDef = getEntityDef(entity);
        for (EntityFieldDef listOnlyFieldDef : entityDef.getListOnlyFieldDefList()) {
            String val = DataUtils.getBeanProperty(String.class, inst, listOnlyFieldDef.getFieldName());
            if (val != null) {
                RecLoadInfo recLoadInfo = resolveListOnlyRecordLoadInformation(entityDef,
                        listOnlyFieldDef.getFieldName(), val, null);
                if (recLoadInfo != null) {
                    DataUtils.setBeanProperty(inst, recLoadInfo.getFieldName(), recLoadInfo.getVal(),
                            recLoadInfo.getFormatter());
                }
            }
        }
    }

    @Override
    public EntityFieldDataType resolveListOnlyEntityDataType(AppEntityField appEntityField) throws UnifyException {
        logDebug("Resolving list-only entity data type for field [{1}] in entity [{0}]...",
                appEntityField.getAppEntityName(), appEntityField.getName());
        EntityFieldDataType type = EntityFieldDataType.LIST_ONLY;
        int depth = 0;
        while (appEntityField != null && depth < MAX_LIST_DEPTH) {
            AppEntityField keyField = findAppEntityField(new AppEntityFieldQuery()
                    .appEntityId(appEntityField.getAppEntityId()).name(appEntityField.getKey()));
            if (keyField != null) {
                if (EntityFieldDataType.ENUM_REF.equals(keyField.getDataType())) {
                    type = EntityFieldDataType.STRING;
                } else if (EntityFieldDataType.REF.equals(keyField.getDataType())
                        || EntityFieldDataType.REF_UNLINKABLE.equals(keyField.getDataType())) {
                    RefDef refDef = getRefDef(keyField.getReferences());
                    ApplicationEntityNameParts np = ApplicationNameUtils
                            .getApplicationEntityNameParts(refDef.getEntity());
                    if (appEntityField.getProperty() != null) {
                        List<AppEntityField> propertyFieldList = findAppEntityFields(
                                (AppEntityFieldQuery) new AppEntityFieldQuery().applicationName(np.getApplicationName())
                                        .appEntityName(np.getEntityName()).name(appEntityField.getProperty()));
                        if (!DataUtils.isBlank(propertyFieldList)) {
                            appEntityField = propertyFieldList.get(0);
                            type = appEntityField.getDataType();
                            if ((EntityFieldDataType.LIST_ONLY.equals(type))) {
                                depth++;
                                continue;
                            }
                        }
                    }
                }
            }

            appEntityField = null;
        }

        return type;
    }

    @Override
    public AppForm findAppForm(Long formId) throws UnifyException {
        return environment().find(AppForm.class, formId);
    }

    @Override
    public List<String> findEntityAppForms(String entity) throws UnifyException {
        List<AppForm> formList = environment()
                .listAll(new AppFormQuery().entity(entity).addSelect("name", "applicationName"));
        if (!DataUtils.isBlank(formList)) {
            List<String> resultList = new ArrayList<String>();
            for (AppForm appForm : formList) {
                resultList.add(ApplicationNameUtils.getApplicationEntityLongName(appForm.getApplicationName(),
                        appForm.getName()));
            }

            return resultList;
        }

        return Collections.emptyList();
    }

    @Override
    public Long createAppForm(AppForm appForm) throws UnifyException {
        return (Long) environment().create(appForm);
    }

    @Override
    public List<AppForm> findAppForms(AppFormQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public List<AppFormFilter> findAppFormFilters(String appFormName) throws UnifyException {
        ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(appFormName);
        Long appFormId = environment().value(Long.class, "id",
                new AppFormQuery().applicationName(nameParts.getApplicationName()).name(nameParts.getEntityName()));
        return environment().findAll(new AppFormFilterQuery().appFormId(appFormId));
    }

    @Override
    public List<AppFormAnnotation> findAppFormAnnotations(AppFormAnnotationQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public List<AppFormElement> findAppFormElementsByFormId(Long appFormId, FormElementType type)
            throws UnifyException {
        return environment().listAll(new AppFormElementQuery().appFormId(appFormId).type(type));
    }

    @Override
    public String findAppFormEntityLongName(Long appFormId) throws UnifyException {
        return environment().value(String.class, "entity", new AppFormQuery().id(appFormId));
    }

    @Override
    public List<AppFormElement> findAppFormElementsByStatePolicy(Long appFormStatePolicyId, FormElementType type)
            throws UnifyException {
        Long appFormId = environment().value(Long.class, "appFormId",
                new AppFormStatePolicyQuery().id(appFormStatePolicyId));
        return environment().listAll(new AppFormElementQuery().appFormId(appFormId).type(type));
    }

    @Override
    public Long createAppTable(AppTable appTable) throws UnifyException {
        return (Long) environment().create(appTable);
    }

    @Override
    public Long createAppAssignmentPage(AppAssignmentPage appAssignmentPage) throws UnifyException {
        return (Long) environment().create(appAssignmentPage);
    }

    @Override
    public List<AppTable> findAppTables(AppTableQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public List<String> findEntityAppTables(String entity) throws UnifyException {
        logDebug("Finding tables for entity [{0}]...", entity);
        List<AppTable> tableList = environment()
                .listAll(new AppTableQuery().entity(entity).addSelect("name", "applicationName"));
        if (!DataUtils.isBlank(tableList)) {
            List<String> resultList = new ArrayList<String>();
            for (AppTable appTable : tableList) {
                resultList.add(ApplicationNameUtils.getApplicationEntityLongName(appTable.getApplicationName(),
                        appTable.getName()));
            }

            return resultList;
        }

        return Collections.emptyList();
    }

    @Override
    public List<AppTableAction> findTableActions(AppTableActionQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public List<AppAssignmentPage> findAppAssignmentPages(AppAssignmentPageQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public <T extends BaseApplicationEntity> List<Long> findAppComponentIdList(String applicationName,
            Class<T> componentClazz) throws UnifyException {
        return environment().valueList(Long.class, "id",
                Query.of(componentClazz).addEquals("applicationName", applicationName).addOrder("id"));
    }

    @Override
    public <T extends BaseApplicationEntity> List<Long> findCustomAppComponentIdList(String applicationName,
            Class<T> componentClazz) throws UnifyException {
        return environment().valueList(Long.class, "id",
                Query.of(componentClazz).addEquals("applicationName", applicationName)
                        .addAmongst("configType", Arrays.asList(ConfigType.CUSTOMIZED, ConfigType.CUSTOM))
                        .addOrder("id"));
    }

    @Override
    public ApplicationDef getApplicationDef(String applicationName) throws UnifyException {
        return applicationDefFactoryMap.get(applicationName);
    }

    @Override
    public List<ApplicationMenuDef> getApplicationMenuDefs(String appletFilter) throws UnifyException {
        final boolean indicateMenuSectors = systemModuleService.getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.SECTOR_INDICATION_ON_MENU);
        List<Application> applicationList = indicateMenuSectors
                ? environment().listAll(
                        new ApplicationQuery().isMenuAccess().addOrder("sectorShortCode", "displayIndex", "label"))
                : environment().listAll(new ApplicationQuery().isMenuAccess().addOrder("displayIndex", "label"));
        if (!DataUtils.isBlank(applicationList)) {
            List<ApplicationMenuDef> resultList = new ArrayList<ApplicationMenuDef>();
            final String importApplicationName = systemModuleService.getSysParameterValue(String.class,
                    ApplicationModuleSysParamConstants.IMPORT_APPLICATION_NAME);
            for (Application application : applicationList) {
                List<AppletDef> appletDefList = importApplicationName.equals(application.getName())
                        ? getImportDataAppletDefs(appletFilter)
                        : getUnreservedMenuAppletDefs(application.getName(), appletFilter);
                DataUtils.sortAscending(appletDefList, AppletDef.class, "label");
                DataUtils.sortAscending(appletDefList, AppletDef.class, "displayIndex");
                resultList.add(new ApplicationMenuDef(application.getLabel(), application.getName(),
                        application.getDescription(), application.getId(), application.getVersionNo(),
                        application.getSectorShortCode(), application.getSectorColor(), appletDefList));
            }

            return resultList;
        }

        return Collections.emptyList();
    }

    @Override
    public List<AppletDef> getAppletDefs(String applicationName) throws UnifyException {
        List<String> appAppletList = environment().valueList(String.class, "name",
                new AppAppletQuery().applicationName(applicationName));
        if (!DataUtils.isBlank(appAppletList)) {
            List<AppletDef> resultList = new ArrayList<AppletDef>();
            for (String appletName : appAppletList) {
                resultList.add(appletDefFactoryMap
                        .get(ApplicationNameUtils.getApplicationEntityLongName(applicationName, appletName)));
            }

            return resultList;
        }

        return Collections.emptyList();
    }

    @Override
    public List<AppWidgetType> findAppWidgetTypes(AppWidgetTypeQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public AppPropertyList findAppPropertyList(Long appPropertyId) throws UnifyException {
        return environment().find(AppPropertyList.class, appPropertyId);
    }

    @Override
    public AppPropertyRule findAppPropertyRule(Long appPropertyRuleId) throws UnifyException {
        return environment().find(AppPropertyRule.class, appPropertyRuleId);
    }

    @Override
    public AppAssignmentPage findAppAssignmentPage(Long appAssignmentPageId) throws UnifyException {
        return environment().find(AppAssignmentPage.class, appAssignmentPageId);
    }

    @Override
    public AppletDef getAppletDef(String appletName) throws UnifyException {
        if (appletName.startsWith(ApplicationNameUtils.RESERVED_FC_PREFIX)) {
            for (ApplicationAppletDefProvider aadp : applicationAppletDefProviderList) {
                if (aadp.providesApplet(appletName)) {
                    return aadp.getAppletDef(appletName);
                }
            }

            throw new UnifyException(ApplicationModuleErrorConstants.UNABLE_TO_RESOLVE_RESERVED_APPLET, appletName);
        }

        return appletDefFactoryMap.get(appletName);
    }

    @Override
    public AppletDef getAppletDef(Long appAppletId) throws UnifyException {
        AppApplet appApplet = environment()
                .listLean(new AppAppletQuery().id(appAppletId).addSelect("applicationName", "name"));
        String appletName = ApplicationNameUtils.getApplicationEntityLongName(appApplet.getApplicationName(),
                appApplet.getName());
        return appletDefFactoryMap.get(appletName);
    }

    @Override
    public WidgetTypeDef getWidgetTypeDef(String widgetName) throws UnifyException {
        return widgetDefFactoryMap.get(widgetName);
    }

    @Override
    public AppWidgetType findAppWidgetType(Long widgetTypeId) throws UnifyException {
        return environment().find(AppWidgetType.class, widgetTypeId);
    }

    @Override
    public SuggestionTypeDef getSuggestionTypeDef(String suggestionTypeName) throws UnifyException {
        return suggestionDefFactoryMap.get(suggestionTypeName);
    }

    @Override
    public AppSuggestionType findAppSuggestionType(Long suggestionTypeId) throws UnifyException {
        return environment().find(AppSuggestionType.class, suggestionTypeId);
    }

    @Override
    public List<? extends Listable> getRelatedWidgetTypes(String applicationName) throws UnifyException {
        List<ListData> list = new ArrayList<ListData>();
        fetchWidgetTypeList(list, "application");
        if (!"application".equals(applicationName)) {
            fetchWidgetTypeList(list, applicationName);
        }

        return list;
    }

    @Override
    public List<Class<?>> getDelegateEntitiesByDataSource(String dataSourceName) throws UnifyException {
        List<AppEntity> entityList = environment()
                .listAll(new AppEntityQuery().isDelegated().addSelect("applicationName", "name", "delegate"));
        if (!entityList.isEmpty()) {
            List<Class<?>> delegateList = new ArrayList<Class<?>>();
            for (AppEntity entity : entityList) {
                String entityLongName = ApplicationNameUtils.getApplicationEntityLongName(entity.getApplicationName(),
                        entity.getName());
                EntityClassDef entityClassDef = getEntityClassDef(entityLongName);
                if (dataSourceName.equals(environment().getEntityDataSourceName(entityLongName))) {
                    delegateList.add(entityClassDef.getEntityClass());
                }
            }

            return delegateList;
        }

        return Collections.emptyList();
    }

    @Override
    public EntityClassDef getEntityClassDef(String entityName) throws UnifyException {
        return entityClassDefFactoryMap.get(entityName);
    }

    @Override
    public EntityDef getEntityDef(String entityName) throws UnifyException {
        EntityDef entityDef = entityDefFactoryMap.get(entityName);
        if (!entityDef.isListOnlyTypesResolved()) {
            synchronized (entityDef) {
                if (!entityDef.isListOnlyTypesResolved()) {
                    resolveListOnlyFieldTypes(entityDef);
                }
            }
        }

        return entityDef;
    }

    @Override
    public EntityDef getEntityDefByClass(String entityClass) throws UnifyException {
        return entityDefByClassFactoryMap.get(entityClass);
    }

    @Override
    public List<AppRef> findAppRefs(AppRefQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public RefDef getRefDef(String refName) throws UnifyException {
        return refDefFactoryMap.get(refName);
    }

    @Override
    public TableDef getTableDef(String tableName) throws UnifyException {
        return tableDefFactoryMap.get(tableName);
    }

    @Override
    public FormDef getFormDef(String formName) throws UnifyException {
        return formDefFactoryMap.get(formName);
    }

    @Override
    public AssignmentPageDef getAssignmentPageDef(String assigmentPageName) throws UnifyException {
        return assignmentPageDefFactoryMap.get(assigmentPageName);
    }

    @Override
    public PropertyListDef getPropertyListDef(String propertyListName) throws UnifyException {
        return propertyListDefMap.get(propertyListName);
    }

    @Override
    public PropertyRuleDef getPropertyRuleDef(String propertyRuleName) throws UnifyException {
        return propertyRuleDefMap.get(propertyRuleName);
    }

    @Override
    public List<PropertyListItem> getPropertyListItems(Entity entityInst, String childFkFieldName,
            PropertyRuleDef propertyRuleDef) throws UnifyException {
        PropertyListDef propertyListDef = propertyRuleDef.getPropertyListDef(entityInst);
        if (propertyListDef != null) {
            Map<String, String> valueMap = localGetPropertyListValues(entityInst, childFkFieldName, propertyRuleDef);
            List<PropertyListItem> resultList = new ArrayList<PropertyListItem>();
            for (PropertyListItemDef propertyListItemDef : propertyListDef.getItemDefList()) {
                String value = valueMap.get(propertyListItemDef.getName());
                String displayValue = null;
                if (value != null && propertyListItemDef.isEncrypt()) {
                    value = twoWayStringCryptograph.decrypt(value);
                }

                if (value != null) {
                    if (propertyListItemDef.isMask()) {
                        displayValue = StringUtils.MASK;
                    } else if (!StringUtils.isBlank(propertyListItemDef.getReferences())) {
                        displayValue = getListItemByKey(LocaleType.SESSION, propertyListItemDef.getReferences(), value)
                                .getListDescription();
                    } else {
                        displayValue = value;
                    }
                }

                resultList.add(new PropertyListItem(propertyListItemDef.getName(), propertyListItemDef.getDescription(),
                        value, displayValue, propertyListItemDef.getWidgetTypeDef().getLongName()));
            }

            return resultList;
        }

        return Collections.emptyList();
    }

    @Override
    public MapValues getPropertyListValues(Entity entityInst, String childFkFieldName, PropertyRuleDef propertyRuleDef)
            throws UnifyException {
        MapValues values = new MapValues();
        PropertyListDef propertyListDef = propertyRuleDef.getPropertyListDef(entityInst);
        if (propertyListDef != null) {
            Map<String, String> valueMap = localGetPropertyListValues(entityInst, childFkFieldName, propertyRuleDef);
            for (PropertyListItemDef propertyListItemDef : propertyListDef.getItemDefList()) {
                String value = valueMap.get(propertyListItemDef.getName());
                if (value != null && propertyListItemDef.isEncrypt()) {
                    value = twoWayStringCryptograph.decrypt(value);
                }
                values.addValue(propertyListItemDef.getName(), String.class, value);
            }
        }

        values.addValue(ApplicationModuleNameConstants.PROPERTYLIST_OWNERID_PROPERTY, Long.class, entityInst.getId());
        return values;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void savePropertyListValues(SweepingCommitPolicy sweepingCommitPolicy, Entity entityInst,
            String childFkFieldName, PropertyRuleDef propertyRuleDef, MapValues values) throws UnifyException {
        EntityFieldDef childListFieldDef = propertyRuleDef.getEntityDef().getFieldDef(propertyRuleDef.getListField());
        EntityClassDef childEntityClassDef = getEntityClassDef(childListFieldDef.getRefDef().getEntity());
        Class<? extends Entity> childEntityClass = (Class<? extends Entity>) childEntityClassDef.getEntityClass();
        // Delete older child records
        environment().deleteAll(Query.of(childEntityClass).addEquals(childFkFieldName, entityInst.getId()));
        PropertyListDef propertyListDef = propertyRuleDef.getPropertyListDef(entityInst);
        if (propertyListDef != null) {
            // Create new child records
            Entity childInst = ReflectUtils.newInstance(childEntityClass);
            DataUtils.setBeanProperty(childInst, childFkFieldName, entityInst.getId());
            for (PropertyListItemDef propertyListItemDef : propertyListDef.getItemDefList()) {
                String propName = propertyListItemDef.getName();
                DataUtils.setBeanProperty(childInst, propertyRuleDef.getPropNameField(), propName);
                String value = null;
                if (values.isMapValue(propName)) {
                    value = DataUtils.convert(String.class, values.getValue(propName));
                }

                if (value != null && propertyListItemDef.isEncrypt()) {
                    value = twoWayStringCryptograph.encrypt(value);
                }

                DataUtils.setBeanProperty(childInst, propertyRuleDef.getPropValField(), value);
                environment().create(childInst);
            }
        }

        if (sweepingCommitPolicy != null) {
            sweepingCommitPolicy.bumpAllParentVersions(db(), RecordActionType.UPDATE);
        }
    }

    @Override
    public int countFileAttachments(String category, String ownerEntityName, Long ownerInstId) throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        return environment().countAll(new FileAttachmentQuery().category(category).entity(entityDef.getTableName())
                .entityInstId(ownerInstId));
    }

    @Override
    public boolean saveFileAttachment(String category, String ownerEntityName, Long ownerInstId, Attachment attachment)
            throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        FileAttachment oldFileAttachment = environment().findLean(new FileAttachmentQuery().category(category)
                .entity(entityDef.getTableName()).entityInstId(ownerInstId).name(attachment.getName()));
        if (oldFileAttachment == null) {
            FileAttachment fileAttachment = new FileAttachment();
            fileAttachment.setType(attachment.getType());
            fileAttachment.setEntityInstId(ownerInstId);
            fileAttachment.setEntity(entityDef.getTableName());
            fileAttachment.setCategory(category);
            fileAttachment.setName(attachment.getName());
            fileAttachment.setTitle(attachment.getTitle());
            fileAttachment.setFileName(attachment.getFileName());
            fileAttachment.setFile(new FileAttachmentDoc(attachment.getData()));
            environment().create(fileAttachment);
            return true;
        }

        oldFileAttachment.setType(attachment.getType());
        oldFileAttachment.setTitle(attachment.getTitle());
        oldFileAttachment.setFileName(attachment.getFileName());
        oldFileAttachment.setFile(new FileAttachmentDoc(attachment.getData()));
        environment().updateByIdVersion(oldFileAttachment);
        return false;
    }

    @Override
    public Map<String, String> retrieveAllAttachmentFileNames(String category, String ownerEntityName, Long ownerInstId)
            throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        return environment().valueMap(String.class, "name", String.class, "fileName", new FileAttachmentQuery()
                .category(category).entity(entityDef.getTableName()).entityInstId(ownerInstId));
    }

    @Override
    public List<Attachment> retrieveAllFileAttachments(String category, String ownerEntityName, Long ownerInstId)
            throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        List<Long> fileAttachmentIdList = environment().valueList(Long.class, "id", new FileAttachmentQuery()
                .category(category).entity(entityDef.getTableName()).entityInstId(ownerInstId));
        if (!DataUtils.isBlank(fileAttachmentIdList)) {
            List<Attachment> list = new ArrayList<Attachment>();
            for (Long fileAttachmentId : fileAttachmentIdList) {
                FileAttachment fileAttachment = environment().find(FileAttachment.class, fileAttachmentId);
                list.add(Attachment
                        .newBuilder(fileAttachment.getId(), fileAttachment.getType(), fileAttachment.getVersionNo())
                        .name(fileAttachment.getName()).title(fileAttachment.getTitle())
                        .fileName(fileAttachment.getFileName()).data(fileAttachment.getFile().getData()).build());
                return list;
            }
        }

        return Collections.emptyList();
    }

    @Override
    public Attachment retrieveFileAttachment(String category, String ownerEntityName, Long ownerInstId,
            String attachmentName) throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        FileAttachment fileAttachment = environment().find(new FileAttachmentQuery().category(category)
                .entity(entityDef.getTableName()).entityInstId(ownerInstId).name(attachmentName));
        if (fileAttachment != null) {
            return Attachment
                    .newBuilder(fileAttachment.getId(), fileAttachment.getType(), fileAttachment.getVersionNo())
                    .name(fileAttachment.getName()).title(fileAttachment.getTitle())
                    .fileName(fileAttachment.getFileName()).data(fileAttachment.getFile().getData()).build();
        }

        return null;
    }

    @Override
    public boolean deleteFileAttachment(String category, String ownerEntityName, Long ownerInstId,
            String attachmentName) throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        return environment().deleteAll(new FileAttachmentQuery().category(category).entity(entityDef.getTableName())
                .entityInstId(ownerInstId).name(attachmentName)) > 0;
    }

    @Override
    public boolean sychFileAttachments(String category, String ownerEntityName, Long destEntityInstId,
            Long srcEntityInstId) throws UnifyException {
        if (!destEntityInstId.equals(srcEntityInstId)) {
            final EntityDef entityDef = getEntityDef(ownerEntityName);
            environment().deleteAll(new FileAttachmentQuery().category(category).entity(entityDef.getTableName())
                    .entityInstId(destEntityInstId));
            List<Long> fileAttachmentIdList = environment().valueList(Long.class, "id", new FileAttachmentQuery()
                    .category(category).entity(entityDef.getTableName()).entityInstId(srcEntityInstId));
            if (!DataUtils.isBlank(fileAttachmentIdList)) {
                for (Long fileAttachmentId : fileAttachmentIdList) {
                    FileAttachment fileAttachment = environment().find(FileAttachment.class, fileAttachmentId);
                    fileAttachment.setEntityInstId(destEntityInstId);
                    environment().create(fileAttachment);
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public FilterDef retrieveFilterDef(String category, String ownerEntityName, Long ownerInstId,
            String filterGenerator) throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        return InputWidgetUtils.getFilterDef(appletUtilities, filterGenerator, environment().find(
                new AppFilterQuery().category(category).entity(entityDef.getTableName()).entityInstId(ownerInstId)));
    }

    @Override
    public void saveFilterDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, FilterDef filterDef) throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        environment().deleteAll(
                new AppFilterQuery().category(category).entity(entityDef.getTableName()).entityInstId(ownerInstId));
        if (filterDef != null && !filterDef.isBlank()) {
            AppFilter appFilter = new AppFilter();
            appFilter.setEntityInstId(ownerInstId);
            appFilter.setEntity(entityDef.getTableName());
            appFilter.setCategory(category);
            appFilter.setDefinition(InputWidgetUtils.getFilterDefinition(filterDef));
            environment().create(appFilter);
        }

        if (sweepingCommitPolicy != null) {
            sweepingCommitPolicy.bumpAllParentVersions(db(), RecordActionType.UPDATE);
        }
    }

    @Override
    public void saveAppletQuickFilterDef(SweepingCommitPolicy sweepingCommitPolicy, Long appAppletId, String name,
            String description, OwnershipType ownershipType, FilterDef filterDef) throws UnifyException {
        Long ownerInstId = null;
        AppAppletFilter appAppletFilter = environment()
                .find(new AppAppletFilterQuery().appAppletId(appAppletId).name(name));
        if (appAppletFilter == null) {
            appAppletFilter = new AppAppletFilter();
            appAppletFilter.setAppAppletId(appAppletId);
            appAppletFilter.setName(name);
            appAppletFilter.setDescription(description);
            appAppletFilter.setOwnershipType(ownershipType);
            if (OwnershipType.USER.equals(ownershipType)) {
                appAppletFilter.setOwner(getUserToken().getUserLoginId());
            }

            appAppletFilter.setQuickFilter(true);
            ownerInstId = (Long) environment().create(appAppletFilter);
        } else {
            appAppletFilter.setQuickFilter(true);
            ownerInstId = appAppletFilter.getId();
        }

        saveFilterDef(sweepingCommitPolicy, "applet", "application.appAppletFilter", ownerInstId, filterDef);
    }

    @Override
    public FieldSequenceDef retrieveFieldSequenceDef(String category, String ownerEntityName, Long ownerInstId)
            throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        return InputWidgetUtils.getFieldSequenceDef(environment().find(new AppFieldSequenceQuery().category(category)
                .entity(entityDef.getTableName()).entityInstId(ownerInstId)));
    }

    @Override
    public void saveFieldSequenceDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, FieldSequenceDef fieldSequenceDef) throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        environment().deleteAll(new AppFieldSequenceQuery().category(category).entity(entityDef.getTableName())
                .entityInstId(ownerInstId));
        if (fieldSequenceDef != null && !fieldSequenceDef.isBlank()) {
            AppFieldSequence appFieldSequence = new AppFieldSequence();
            appFieldSequence.setEntityInstId(ownerInstId);
            appFieldSequence.setEntity(entityDef.getTableName());
            appFieldSequence.setCategory(category);
            appFieldSequence.setDefinition(InputWidgetUtils.getFieldSequenceDefinition(fieldSequenceDef));
            environment().create(appFieldSequence);
        }

        if (sweepingCommitPolicy != null) {
            sweepingCommitPolicy.bumpAllParentVersions(db(), RecordActionType.UPDATE);
        }
    }

    @Override
    public WidgetRulesDef retrieveWidgetRulesDef(String category, String ownerEntityName, Long ownerInstId)
            throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        return InputWidgetUtils.getWidgetRulesDef(environment().find(new AppWidgetRulesQuery().category(category)
                .entity(entityDef.getTableName()).entityInstId(ownerInstId)));
    }

    @Override
    public void saveWidgetRulesDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, WidgetRulesDef widgetRulesDef) throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        environment().deleteAll(new AppWidgetRulesQuery().category(category).entity(entityDef.getTableName())
                .entityInstId(ownerInstId));
        if (widgetRulesDef != null && !widgetRulesDef.isBlank()) {
            AppWidgetRules appWidgetRules = new AppWidgetRules();
            appWidgetRules.setEntityInstId(ownerInstId);
            appWidgetRules.setEntity(entityDef.getTableName());
            appWidgetRules.setCategory(category);
            appWidgetRules.setDefinition(InputWidgetUtils.getWidgetRulesDefinition(widgetRulesDef));
            environment().create(appWidgetRules);
        }

        if (sweepingCommitPolicy != null) {
            sweepingCommitPolicy.bumpAllParentVersions(db(), RecordActionType.UPDATE);
        }
    }

    @Override
    public SetValuesDef retrieveSetValuesDef(String category, String ownerEntityName, Long ownerInstId)
            throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        return InputWidgetUtils.getSetValuesDef(null, environment().find(
                new AppSetValuesQuery().category(category).entity(entityDef.getTableName()).entityInstId(ownerInstId)));
    }

    @Override
    public void saveSetValuesDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, SetValuesDef setValuesDef) throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        environment().deleteAll(
                new AppSetValuesQuery().category(category).entity(entityDef.getTableName()).entityInstId(ownerInstId));
        if (setValuesDef != null && !setValuesDef.isBlank()) {
            AppSetValues appSetValues = new AppSetValues();
            appSetValues.setEntityInstId(ownerInstId);
            appSetValues.setEntity(entityDef.getTableName());
            appSetValues.setCategory(category);
            appSetValues.setDefinition(InputWidgetUtils.getSetValuesDefinition(setValuesDef));
            environment().create(appSetValues);
        }

        if (sweepingCommitPolicy != null) {
            sweepingCommitPolicy.bumpAllParentVersions(db(), RecordActionType.UPDATE);
        }
    }

    @Override
    public ParamValuesDef retrieveParamValuesDef(String category, String ownerEntityName, Long ownerInstId,
            List<ParamConfig> paramConfigList) throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        return CommonInputUtils.getParamValuesDef(paramConfigList, environment().find(
                new ParamValuesQuery().category(category).entity(entityDef.getTableName()).entityInstId(ownerInstId)));
    }

    @Override
    public void saveParamValuesDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, ParamValuesDef paramValuesDef) throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        environment().deleteAll(
                new ParamValuesQuery().category(category).entity(entityDef.getTableName()).entityInstId(ownerInstId));
        if (paramValuesDef != null && !paramValuesDef.isBlank()) {
            ParamValues paramValues = new ParamValues();
            paramValues.setEntityInstId(ownerInstId);
            paramValues.setEntity(entityDef.getTableName());
            paramValues.setCategory(category);
            paramValues.setDefinition(CommonInputUtils.getParamValuesDefinition(paramValuesDef));
            environment().create(paramValues);
        }

        if (sweepingCommitPolicy != null) {
            sweepingCommitPolicy.bumpAllParentVersions(db(), RecordActionType.UPDATE);
        }
    }

    @Override
    public String getEntityDescriptionByRef(String refLongName, Long entityId) throws UnifyException {
        RefDef refDef = getRefDef(refLongName);
        EntityClassDef entityClassDef = getEntityClassDef(refDef.getEntity());
        return getEntityDescription(entityClassDef, entityId, refDef.getSearchField());
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getEntityDescription(EntityClassDef entityClassDef, Long entityId, String fieldName)
            throws UnifyException {
        final EntityDef entityDef = entityClassDef.getEntityDef();
        if (entityDef.isWithDescriptiveFieldList()) {
            Query<?> query = Query.of((Class<? extends Entity>) entityClassDef.getEntityClass()).addEquals("id",
                    entityId);
            for (EntityFieldDef entityFieldDef : entityDef.getDescriptiveFieldDefList()) {
                query.addSelect(entityFieldDef.getFieldName());
            }

            Entity inst = environment().list(query);
            return buildEntityDescription(entityDef, inst);
        }

        fieldName = entityDef.isWithDescriptionField() ? "description" : fieldName;
        return environment().value(String.class, fieldName,
                Query.of((Class<? extends Entity>) entityClassDef.getEntityClass()).addEquals("id", entityId));
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getEntityDescription(EntityClassDef entityClassDef, Entity inst, String fieldName)
            throws UnifyException {
        final EntityDef entityDef = entityClassDef.getEntityDef();
        if (entityDef.isWithDescriptiveFieldList()) {
            return buildEntityDescription(entityDef, inst);
        }

        fieldName = fieldName == null ? "description" : fieldName;
        if (fieldName != null && entityDef.isWithFieldDef(fieldName)) {
            return environment().value(String.class, fieldName,
                    Query.of((Class<? extends Entity>) entityClassDef.getEntityClass()).addEquals("id", inst.getId()));
        }

        return "";
    }

    private String buildEntityDescription(EntityDef entityDef, Entity inst) throws UnifyException {
        StringBuilder sb = new StringBuilder();
        boolean appendSym = false;
        for (EntityFieldDef entityFieldDef : entityDef.getDescriptiveFieldDefList()) {
            if (appendSym) {
                sb.append(" ");
            } else {
                appendSym = true;
            }

            Object val = ReflectUtils.getBeanProperty(inst, entityFieldDef.getFieldName());
            if (val != null) {
                sb.append(val);
            }
        }

        return sb.toString();
    }

    @Override
    public List<String> findForeignEntityStringFields(String entityName, String fkFieldName) throws UnifyException {
        EntityDef entityDef = getEntityDef(entityName);
        EntityFieldDef entityFieldDef = entityDef.getFieldDef(fkFieldName);
        RefDef refDef = getRefDef(entityFieldDef.getReferences());
        return findEntityStringFields(refDef.getEntity());
    }

    @Override
    public List<String> findEntityStringFields(String entityName) throws UnifyException {
        List<String> fieldList = new ArrayList<String>();
        EntityDef entityDef = getEntityDef(entityName);
        for (EntityFieldDef _entityFieldDef : entityDef.getFieldDefList()) {
            if (_entityFieldDef.isListOnly()) {
                if (EntityFieldDataType.STRING
                        .equals(resolveListOnlyFieldDataType(entityDef, _entityFieldDef.getFieldName()))) {
                    fieldList.add(_entityFieldDef.getFieldName());
                }
            } else {
                if (_entityFieldDef.isString()) {
                    fieldList.add(_entityFieldDef.getFieldName());
                }
            }
        }

        return fieldList;
    }

    @Override
    public List<DynamicEntityInfo> generateDynamicEntityInfos(List<String> entityNames, String basePackage)
            throws UnifyException {
        Map<String, DynamicEntityInfo> workingMap = new HashMap<String, DynamicEntityInfo>();
        for (String entityName : entityNames) {
            EntityDef entityDef = getEntityDef(entityName);
            buildDynamicEntityInfo(entityDef, workingMap, basePackage);
        }

        List<DynamicEntityInfo> resultList = new ArrayList<DynamicEntityInfo>();
        for (String entityName : entityNames) {
            DynamicEntityInfo info = workingMap.get(entityName);
            info.finalizeResolution();
            resultList.add(info);
        }
        return resultList;
    }

    @Override
    public Restriction getChildRestriction(EntityDef parentEntityDef, String childFieldName, Entity parentInst)
            throws UnifyException {
        EntityFieldDef childListFieldDef = parentEntityDef.getFieldDef(childFieldName);
        EntityDef _childEntityDef = getEntityDef(getRefDef(childListFieldDef.getReferences()).getEntity());
        if (childListFieldDef.isWithCategory()) {
            return new And().add(new Equals(_childEntityDef.getFosterParentIdDef().getFieldName(), parentInst.getId()))
                    .add(new Equals(_childEntityDef.getFosterParentTypeDef().getFieldName(),
                            parentEntityDef.getTableName()))
                    .add(new Equals(_childEntityDef.getCategoryColumnDef().getFieldName(),
                            childListFieldDef.getCategory()));
        }

        return new Equals(_childEntityDef.getRefEntityFieldDef(parentEntityDef.getLongName()).getFieldName(),
                parentInst.getId());
    }

    @SuppressWarnings("unchecked")
    @Taskable(name = ApplicationImportDataTaskConstants.IMPORTDATA_TASK_NAME, description = "Import Data Task",
            parameters = { @Parameter(name = ApplicationImportDataTaskConstants.IMPORTDATA_ENTITY,
                    description = "$m{dataimportappletpanel.dataimport.entity}", type = String.class, mandatory = true),
                    @Parameter(name = ApplicationImportDataTaskConstants.IMPORTDATA_UPLOADCONFIG,
                            description = "$m{dataimportappletpanel.dataimport.uploadconfig}", type = String.class,
                            mandatory = true),
                    @Parameter(name = ApplicationImportDataTaskConstants.IMPORTDATA_UPLOAD_FILE,
                            description = "$m{dataimportappletpanel.dataimport.selectfile}", type = byte[].class,
                            mandatory = true),
                    @Parameter(name = ApplicationImportDataTaskConstants.IMPORTDATA_WITH_HEADER_FLAG,
                            description = "$m{dataimportappletpanel.dataimport.hasheader}", type = boolean.class,
                            mandatory = true) },
            limit = TaskExecLimit.ALLOW_MULTIPLE, schedulable = false)
    public int executeImportDataTask(TaskMonitor taskMonitor, String entity, String uploadConfig, byte[] uploadFile,
            boolean withHeaderFlag) throws UnifyException {
        logDebug(taskMonitor, "Importing data to entity [{0}] table...", entity);
        int totalRecords = 0;
        int updatedRecords = 0;
        int skippedRecords = 0;
        CSVParser csvFileParser = null;
        try {
            final EntityClassDef entityClassDef = getEntityClassDef(entity);
            final EntityDef entityDef = entityClassDef.getEntityDef();
            EntityUploadDef entityUploadDef = entityClassDef.getEntityDef().getUploadDef(uploadConfig);
            List<FieldSequenceEntryDef> fieldSequenceList = entityUploadDef.getFieldSequenceDef()
                    .getFieldSequenceList();
            Entity inst = entityClassDef.newInst();
            Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(uploadFile)));
            CSVFormat csvFormat = CSVFormat.RFC4180
                    .withHeader(DataUtils.toArray(String.class, entityUploadDef.getFieldNameList()))
                    .withIgnoreHeaderCase().withIgnoreEmptyLines().withTrim().withIgnoreSurroundingSpaces()
                    .withNullString("");
            if (withHeaderFlag) {
                csvFormat = csvFormat.withSkipHeaderRecord();
            }

            csvFileParser = new CSVParser(reader, csvFormat);
            Locale locale = getApplicationLocale();
            Map<String, RecLoadInfo> recMap = new LinkedHashMap<String, RecLoadInfo>();
            for (CSVRecord csvRecord : csvFileParser) {
                recMap.clear();
                for (FieldSequenceEntryDef fieldSequenceEntryDef : fieldSequenceList) {
                    Formatter<?> formatter = fieldSequenceEntryDef.isWithFormatter()
                            ? (Formatter<?>) getUplComponent(locale, fieldSequenceEntryDef.getFormatter(), true)
                            : null;
                    String fieldName = fieldSequenceEntryDef.getFieldName();
                    String listVal = csvRecord.get(fieldName);
                    RecLoadInfo recLoadInfo = resolveListOnlyRecordLoadInformation(entityDef, fieldName, listVal,
                            formatter);
                    if (recLoadInfo != null) {
                        recMap.put(recLoadInfo.getFieldName(), recLoadInfo);
                    } else {
                        recMap.put(fieldName, new RecLoadInfo(fieldName, listVal, formatter));
                    }
                }

                UniqueConstraintDef uniqueConstriantDef = null;
                Entity _inst = null;
                boolean createRecord = true;
                if (entityDef.isWithUniqueConstraints()) {
                    for (UniqueConstraintDef constDef : entityDef.getUniqueConstraintList()) {
                        Query<?> query = Query.of((Class<? extends Entity>) entityClassDef.getEntityClass());
                        for (String fieldName : constDef.getFieldList()) {
                            query.addEquals(fieldName, recMap.get(fieldName).getVal());
                        }

                        _inst = environment().findLean(query);
                        if (_inst != null) {
                            uniqueConstriantDef = constDef;
                            createRecord = false;
                            break;
                        }
                    }
                }

                if (createRecord) {
                    for (RecLoadInfo recLoadInfo : recMap.values()) {
                        DataUtils.setBeanProperty(inst, recLoadInfo.getFieldName(), recLoadInfo.getVal(),
                                recLoadInfo.getFormatter());
                    }

                    environment().create(inst);
                } else {
                    switch (entityUploadDef.getConstraintAction()) {
                        case SKIP: {
                            skippedRecords++;
                        }
                            break;
                        case UPDATE: {
                            for (RecLoadInfo recLoadInfo : recMap.values()) {
                                DataUtils.setBeanProperty(_inst, recLoadInfo.getFieldName(), recLoadInfo.getVal(),
                                        recLoadInfo.getFormatter());
                            }

                            environment().updateLeanByIdVersion(_inst);
                            updatedRecords++;
                        }
                            break;
                        case FAIL:
                        default:
                            throw new UnifyException(
                                    ApplicationModuleErrorConstants.UNABLE_LOAD_DATA_WITH_EXISTING_UNIQUE_RECORD,
                                    entityUploadDef.getDescription(), uniqueConstriantDef.getDescription());
                    }
                }

                totalRecords++;
            }

            logDebug(taskMonitor, "Loading data to entity [{0}] table successful.", entity);
            logDebug(taskMonitor, "Total records = {0}, skipped records = {1}, updated records = {2}.", totalRecords,
                    skippedRecords, updatedRecords);
        } catch (IOException e) {
            this.throwOperationErrorException(e);
        } finally {
            if (csvFileParser != null) {
                try {
                    csvFileParser.close();
                } catch (IOException e) {
                }
            }
        }

        return 0;
    }

    @SuppressWarnings("unchecked")
    private RecLoadInfo resolveListOnlyRecordLoadInformation(EntityDef entityDef, String fieldName, String listVal,
            Formatter<?> formatter) throws UnifyException {
        EntityFieldDef entityFieldDef = entityDef.getFieldDef(fieldName);
        if (entityFieldDef.isListOnly()) {
            EntityFieldDef refEntityFieldDef = entityDef.getFieldDef(entityFieldDef.getKey());
            if (refEntityFieldDef.isEnumDataType()) {
                String keyVal = listVal != null
                        ? getListItemByDescription(LocaleType.APPLICATION, refEntityFieldDef.getReferences(), listVal)
                                .getListKey()
                        : null;
                return new RecLoadInfo(entityFieldDef.getKey(), keyVal, formatter);
            } else {
                EntityClassDef refEntityClassDef = getEntityClassDef(refEntityFieldDef.getRefDef().getEntity());
                if (refEntityClassDef.getEntityDef().isSingleFieldUniqueConstraint(entityFieldDef.getProperty())) {
                    Long refId = null;
                    if (listVal != null) {
                        EntityFieldDataType listOnlyDataType = resolveListOnlyFieldDataType(
                                refEntityClassDef.getEntityDef(), entityFieldDef.getProperty());
                        Object cListVal = DataUtils.convert(listOnlyDataType.dataType().javaClass(), listVal,
                                formatter);
                        refId = environment().value(Long.class, "id",
                                Query.of((Class<? extends Entity>) refEntityClassDef.getEntityClass())
                                        .addEquals(entityFieldDef.getProperty(), cListVal));
                    }

                    return new RecLoadInfo(entityFieldDef.getKey(), refId, null);
                }
            }
        }

        return null;
    }

    private EntityFieldDataType resolveListOnlyFieldDataType(EntityDef refEntityDef, String property)
            throws UnifyException {
        EntityFieldDef propEntityFieldDef = refEntityDef.getFieldDef(property);
        if (propEntityFieldDef.getDataType().isListOnly()) {
            EntityFieldDef _refEntityFieldDef = refEntityDef.getFieldDef(propEntityFieldDef.getKey());
            if (_refEntityFieldDef.isEnumDataType()) {
                return EntityFieldDataType.STRING;
            }

            EntityDef _refEntityDef = getEntityDef(_refEntityFieldDef.getRefDef().getEntity());
            return resolveListOnlyFieldDataType(_refEntityDef, propEntityFieldDef.getProperty());
        }

        return propEntityFieldDef.getDataType();
    }

    @Override
    protected void onInitialize() throws UnifyException {
        super.onInitialize();
        applicationArtifactInstallerList = getComponents(ApplicationArtifactInstaller.class);
        applicationAppletDefProviderList = getComponents(ApplicationAppletDefProvider.class);
        List<UnifyComponentConfig> searchWidgets = getComponentConfigs(EntitySearchWidget.class);
        for (UnifyComponentConfig unifyComponentConfig : searchWidgets) {
            LongName lna = unifyComponentConfig.getType().getAnnotation(LongName.class);
            if (lna != null) {
                entitySearchTypes.add(lna.value());
            }
        }
    }

    @Override
    protected void doInstallModuleFeatures(final ModuleInstall moduleInstall) throws UnifyException {
        installAutoInstallApplications(moduleInstall);
    }

    private void installAutoInstallApplications(final ModuleInstall moduleInstall) throws UnifyException {
        final ModuleConfig moduleConfig = moduleInstall.getModuleConfig();
        if (moduleConfig.getModuleAppsConfig() != null
                && !DataUtils.isBlank(moduleConfig.getModuleAppsConfig().getModuleAppList())) {
            for (ModuleAppConfig moduleAppConfig : moduleConfig.getModuleAppsConfig().getModuleAppList()) {
                if (moduleAppConfig.isAutoInstall()) {
                    ApplicationInstall applicationInstall = configurationLoader
                            .loadApplicationInstallation(moduleAppConfig.getConfigFile());
                    installApplication(moduleInstall.getTaskMonitor(), applicationInstall);
                }
            }
        }
    }

    private List<AppletDef> getImportDataAppletDefs(String appletFilter) throws UnifyException {
        Query<AppApplet> query = new AppAppletQuery().type(AppletType.DATA_IMPORT).menuAccess(true)
                .addSelect("applicationName", "name");
        if (!StringUtils.isBlank(appletFilter)) {
            query.addILike("label", appletFilter);
        }

        List<AppApplet> appAppletList = environment().listAll(query);
        if (!DataUtils.isBlank(appAppletList)) {
            List<AppletDef> resultList = new ArrayList<AppletDef>();
            for (AppApplet appApplet : appAppletList) {
                resultList.add(appletDefFactoryMap.get(ApplicationNameUtils
                        .getApplicationEntityLongName(appApplet.getApplicationName(), appApplet.getName())));
            }

            return resultList;
        }

        return Collections.emptyList();
    }

    private List<AppletDef> getUnreservedMenuAppletDefs(String applicationName, String appletFilter)
            throws UnifyException {
        Query<AppApplet> query = new AppAppletQuery().menuAccess(true).unreserved().applicationName(applicationName);
        if (!StringUtils.isBlank(appletFilter)) {
            query.addILike("label", appletFilter);
        }

        List<String> appAppletList = environment().valueList(String.class, "name", query);
        if (!DataUtils.isBlank(appAppletList)) {
            List<AppletDef> resultList = new ArrayList<AppletDef>();
            for (String appletName : appAppletList) {
                AppletDef _appletDef = appletDefFactoryMap
                        .get(ApplicationNameUtils.getApplicationEntityLongName(applicationName, appletName));
                if (_appletDef.isFacade()) {
                    _appletDef = appletDefFactoryMap.get(_appletDef.getRouteToApplet()).facade(_appletDef);
                }

                resultList.add(_appletDef);
            }

            return resultList;
        }

        return Collections.emptyList();
    }

    private List<String> getAllDependantEntities(String entity) throws Exception {
        List<AppRef> refList = environment()
                .listAll(new AppRefQuery().entity(entity).addSelect("applicationName", "name"));
        if (!DataUtils.isBlank(refList)) {
            List<String> refNameList = ApplicationNameUtils.getApplicationEntityLongNames(refList);

            Set<Long> appEntityList = environment().valueSet(Long.class, "appEntityId", new AppEntityFieldQuery()
                    .addEquals("dataType", EntityFieldDataType.REF).addAmongst("references", refNameList));
            if (!DataUtils.isBlank(appEntityList)) {
                List<AppEntity> entityList = environment()
                        .listAll(new AppEntityQuery().idIn(appEntityList).addSelect("applicationName", "name"));
                return ApplicationNameUtils.getApplicationEntityLongNames(entityList);
            }
        }

        return Collections.emptyList();
    }

    private void fetchWidgetTypeList(final List<ListData> list, final String applicationName) throws UnifyException {
        Map<String, String> map = environment().valueMap(String.class, "name", String.class, "description",
                new AppWidgetTypeQuery().applicationName("application"));
        for (Map.Entry<String, String> entry : map.entrySet()) {
            list.add(new ListData(ApplicationNameUtils.getApplicationEntityLongName("application", entry.getKey()),
                    entry.getValue()));
        }
    }

    private void resolveListOnlyFieldTypes(final EntityDef entityDef) throws UnifyException {
        for (EntityFieldDef listOnlyFieldDef : entityDef.getListOnlyFieldDefList()) {
            EntityDef _entityDef = entityDef;
            EntityFieldDef _listOnlyFieldDef = listOnlyFieldDef;
            int depth = 0;
            while (depth < MAX_LIST_DEPTH) {
                EntityFieldDef keyFieldDef = _entityDef.getFieldDef(_listOnlyFieldDef.getKey());
                if (EntityFieldDataType.ENUM_REF.equals(keyFieldDef.getDataType())) {
                    // TODO
                    break;
                } else if (EntityFieldDataType.REF.equals(keyFieldDef.getDataType())
                        || EntityFieldDataType.REF_UNLINKABLE.equals(keyFieldDef.getDataType())) {
                    _entityDef = entityDefFactoryMap.get(keyFieldDef.getRefDef().getEntity());
                    EntityFieldDef _resolvedTypeFieldDef = _entityDef.getFieldDef(_listOnlyFieldDef.getProperty());
                    if (_resolvedTypeFieldDef.isListOnly()) {
                        _listOnlyFieldDef = _resolvedTypeFieldDef;
                        depth++;
                    } else {
                        listOnlyFieldDef.setResolvedTypeFieldDef(_resolvedTypeFieldDef);
                        break;
                    }
                } else {
                    throw new UnifyException(ApplicationModuleErrorConstants.INVALID_LIST_ONLY_FIELD,
                            _entityDef.getLongName(), _listOnlyFieldDef.getFieldName());
                }
            }

        }

        entityDef.setListOnlyTypesResolved();
    }

    @SuppressWarnings("unchecked")
    private boolean installApplication(final TaskMonitor taskMonitor, final ApplicationInstall applicationInstall)
            throws UnifyException {
        final AppConfig applicationConfig = applicationInstall.getApplicationConfig();
        final Long moduleId = systemModuleService.getModuleId(applicationConfig.getModule());
        String description = resolveApplicationMessage(applicationConfig.getDescription());

        // Applications
        logDebug(taskMonitor, "Installing application [{0}]...", description);
        Application oldApplication = environment().find(new ApplicationQuery().name(applicationConfig.getName()));
        Long applicationId = null;
        if (oldApplication == null) {
            logDebug(taskMonitor, "Performing new application [{0}] installation...", description);
            Application application = new Application();
            application.setModuleId(moduleId);
            application.setName(applicationConfig.getName());
            application.setDescription(description);
            application.setLabel(resolveApplicationMessage(applicationConfig.getLabel()));
            application.setDisplayIndex(applicationConfig.getDisplayIndex());
            application.setDevelopable(applicationConfig.isDevelopable());
            application.setMenuAccess(applicationConfig.isMenuAccess());
            application.setConfigType(ConfigType.STATIC_INSTALL);
            applicationId = (Long) environment().create(application);
        } else {
            logDebug(taskMonitor, "Upgrading application [{0}]...", description);
            oldApplication.setModuleId(moduleId);
            if (ConfigUtils.isSetInstall(oldApplication)) {
                oldApplication.setDescription(description);
                oldApplication.setLabel(resolveApplicationMessage(applicationConfig.getLabel()));
                oldApplication.setDevelopable(applicationConfig.isDevelopable());
                oldApplication.setMenuAccess(applicationConfig.isMenuAccess());
                oldApplication.setDisplayIndex(applicationConfig.getDisplayIndex());
            }

            environment().updateByIdVersion(oldApplication);
            applicationId = oldApplication.getId();
        }
        applicationInstall.setApplicationId(applicationId);
        final String applicationName = applicationConfig.getName();
        applicationPrivilegeManager.registerPrivilege(applicationId,
                ApplicationPrivilegeConstants.APPLICATION_CATEGORY_CODE,
                PrivilegeNameUtils.getApplicationPrivilegeName(applicationName), description);
        if (ApplicationModuleNameConstants.APPLICATION_APPLICATION_NAME.equals(applicationName)) {
            applicationPrivilegeManager.registerPrivilege(applicationId,
                    ApplicationPrivilegeConstants.APPLICATION_FEATURE_CATEGORY_CODE,
                    PrivilegeNameUtils
                            .getFeaturePrivilegeName(ApplicationFeatureConstants.SAVE_GLOBAL_TABLE_QUICK_FILTER),
                    resolveApplicationMessage("$m{application.privilege.saveglobaltablefilter}"));
        }

        // Applets
        logDebug(taskMonitor, "Installing application applets...");
        if (applicationConfig.getAppletsConfig() != null
                && !DataUtils.isBlank(applicationConfig.getAppletsConfig().getAppletList())) {
            AppApplet appApplet = new AppApplet();
            appApplet.setApplicationId(applicationId);
            for (AppletConfig appletConfig : applicationConfig.getAppletsConfig().getAppletList()) {
                AppApplet oldAppApplet = environment()
                        .findLean(new AppAppletQuery().applicationId(applicationId).name(appletConfig.getName()));
                description = resolveApplicationMessage(appletConfig.getDescription());
                String label = resolveApplicationMessage(appletConfig.getLabel());
                String entity = ApplicationNameUtils.ensureLongNameReference(applicationName, appletConfig.getEntity());
                if (oldAppApplet == null) {
                    logDebug("Installing new application applet [{0}]...", appletConfig.getName());
                    appApplet.setId(null);
                    appApplet.setName(appletConfig.getName());
                    appApplet.setDescription(description);
                    appApplet.setType(appletConfig.getType());
                    appApplet.setEntity(entity);
                    appApplet.setLabel(label);
                    appApplet.setIcon(appletConfig.getIcon());
                    appApplet.setRouteToApplet(appletConfig.getRouteToApplet());
                    appApplet.setPath(appletConfig.getPath());
                    appApplet.setMenuAccess(appletConfig.isMenuAccess());
                    appApplet.setDisplayIndex(appletConfig.getDisplayIndex());
                    appApplet.setBaseField(appletConfig.getBaseField());
                    appApplet.setAssignField(appletConfig.getAssignField());
                    appApplet.setAssignDescField(appletConfig.getAssignDescField());
                    appApplet.setConfigType(ConfigType.STATIC_INSTALL);
                    populateChildList(appApplet, applicationName, appletConfig);
                    environment().create(appApplet);
                } else {
                    logDebug("Upgrading application applet [{0}]...", appletConfig.getName());
                    if (ConfigUtils.isSetInstall(oldAppApplet)) {
                        oldAppApplet.setDescription(description);
                        oldAppApplet.setType(appletConfig.getType());
                        oldAppApplet.setEntity(entity);
                        oldAppApplet.setLabel(label);
                        oldAppApplet.setIcon(appletConfig.getIcon());
                        oldAppApplet.setRouteToApplet(appletConfig.getRouteToApplet());
                        oldAppApplet.setPath(appletConfig.getPath());
                        oldAppApplet.setMenuAccess(appletConfig.isMenuAccess());
                        oldAppApplet.setDisplayIndex(appletConfig.getDisplayIndex());
                        oldAppApplet.setBaseField(appletConfig.getBaseField());
                        oldAppApplet.setAssignField(appletConfig.getAssignField());
                        oldAppApplet.setAssignDescField(appletConfig.getAssignDescField());
                    }

                    populateChildList(oldAppApplet, applicationName, appletConfig);
                    environment().updateByIdVersion(oldAppApplet);
                }

                applicationPrivilegeManager
                        .registerPrivilege(applicationId,
                                ApplicationPrivilegeConstants.APPLICATION_APPLET_CATEGORY_CODE,
                                PrivilegeNameUtils.getAppletPrivilegeName(ApplicationNameUtils
                                        .getApplicationEntityLongName(applicationName, appletConfig.getName())),
                                description);
            }

            logDebug(taskMonitor, "Installed [{0}] application applets...",
                    applicationConfig.getAppletsConfig().getAppletList().size());
        }

        // Widgets
        logDebug(taskMonitor, "Installing application widget types...");
        if (applicationConfig.getWidgetTypesConfig() != null
                && !DataUtils.isBlank(applicationConfig.getWidgetTypesConfig().getWidgetTypeList())) {
            AppWidgetType appWidgetType = new AppWidgetType();
            appWidgetType.setApplicationId(applicationId);
            for (WidgetTypeConfig widgetTypeConfig : applicationConfig.getWidgetTypesConfig().getWidgetTypeList()) {
                AppWidgetType oldWidgetType = environment().findLean(
                        new AppWidgetTypeQuery().applicationId(applicationId).name(widgetTypeConfig.getName()));
                description = resolveApplicationMessage(widgetTypeConfig.getDescription());
                if (oldWidgetType == null) {
                    logDebug("Installing new application widget [{0}]...", widgetTypeConfig.getName());
                    appWidgetType.setId(null);
                    appWidgetType.setDataType(widgetTypeConfig.getDataType());
                    appWidgetType.setInputType(widgetTypeConfig.getInputType());
                    appWidgetType.setName(widgetTypeConfig.getName());
                    appWidgetType.setDescription(description);
                    appWidgetType.setEditor(widgetTypeConfig.getEditor());
                    appWidgetType.setRenderer(widgetTypeConfig.getRenderer());
                    appWidgetType.setStretch(widgetTypeConfig.isStretch());
                    appWidgetType.setListOption(widgetTypeConfig.isListOption());
                    appWidgetType.setEnumOption(widgetTypeConfig.isEnumOption());
                    appWidgetType.setConfigType(ConfigType.STATIC_INSTALL);
                    environment().create(appWidgetType);
                } else {
                    logDebug("Upgrading application widget [{0}]...", widgetTypeConfig.getName());
                    if (ConfigUtils.isSetInstall(oldWidgetType)) {
                        oldWidgetType.setDataType(widgetTypeConfig.getDataType());
                        oldWidgetType.setInputType(widgetTypeConfig.getInputType());
                        oldWidgetType.setDescription(description);
                        oldWidgetType.setEditor(widgetTypeConfig.getEditor());
                        oldWidgetType.setRenderer(widgetTypeConfig.getRenderer());
                        oldWidgetType.setStretch(widgetTypeConfig.isStretch());
                        oldWidgetType.setListOption(widgetTypeConfig.isListOption());
                        oldWidgetType.setEnumOption(widgetTypeConfig.isEnumOption());
                        environment().updateByIdVersion(oldWidgetType);
                    }
                }
            }
        }

        // References
        logDebug(taskMonitor, "Installing application references...");
        if (applicationConfig.getRefsConfig() != null
                && !DataUtils.isBlank(applicationConfig.getRefsConfig().getRefList())) {
            AppRef appRef = new AppRef();
            appRef.setApplicationId(applicationId);
            for (RefConfig refConfig : applicationConfig.getRefsConfig().getRefList()) {
                AppRef oldAppRef = environment()
                        .findLean(new AppRefQuery().applicationId(applicationId).name(refConfig.getName()));
                description = resolveApplicationMessage(refConfig.getDescription());
                if (oldAppRef == null) {
                    logDebug("Installing new application reference [{0}] ...", refConfig.getName());
                    appRef.setId(null);
                    appRef.setName(refConfig.getName());
                    appRef.setDescription(description);
                    appRef.setEntity(
                            ApplicationNameUtils.ensureLongNameReference(applicationName, refConfig.getEntity()));
                    appRef.setSearchField(refConfig.getSearchField());
                    appRef.setSearchTable(refConfig.getSearchTable());
                    appRef.setSelectHandler(refConfig.getSelectHandler());
                    appRef.setListFormat(refConfig.getListFormat());
                    appRef.setFilterGenerator(refConfig.getFilterGenerator());
                    appRef.setFilterGeneratorRule(refConfig.getFilterGeneratorRule());
                    appRef.setFilter(InputWidgetUtils.newAppFilter(refConfig.getFilter()));
                    appRef.setConfigType(ConfigType.STATIC_INSTALL);
                    environment().create(appRef);
                } else {
                    logDebug("Upgrading application reference [{0}] ...", refConfig.getName());
                    if (ConfigUtils.isSetInstall(oldAppRef)) {
                        oldAppRef.setDescription(description);
                        oldAppRef.setEntity(
                                ApplicationNameUtils.ensureLongNameReference(applicationName, refConfig.getEntity()));
                        oldAppRef.setSearchField(refConfig.getSearchField());
                        oldAppRef.setSearchTable(refConfig.getSearchTable());
                        oldAppRef.setSelectHandler(refConfig.getSelectHandler());
                        oldAppRef.setListFormat(refConfig.getListFormat());
                        oldAppRef.setFilterGenerator(refConfig.getFilterGenerator());
                        oldAppRef.setFilterGeneratorRule(refConfig.getFilterGeneratorRule());
                        oldAppRef.setFilter(InputWidgetUtils.newAppFilter(refConfig.getFilter()));
                    } else {
                        environment().findChildren(oldAppRef);
                    }

                    environment().updateByIdVersion(oldAppRef);
                }
            }

            logDebug(taskMonitor, "Installed [{0}] application references...",
                    applicationConfig.getRefsConfig().getRefList().size());
        }

        // Entities
        logDebug(taskMonitor, "Installing application entities...");
        Map<String, Long> entityIdByNameMap = new HashMap<String, Long>();
        if (applicationConfig.getEntitiesConfig() != null
                && !DataUtils.isBlank(applicationConfig.getEntitiesConfig().getEntityList())) {
            AppEntity appEntity = new AppEntity();
            appEntity.setApplicationId(applicationId);
            EntityBaseTypeFieldSet entityBaseTypeFieldSet = ApplicationEntityUtils
                    .getEntityBaseTypeFieldSet(messageResolver);
            for (AppEntityConfig appEntityConfig : applicationConfig.getEntitiesConfig().getEntityList()) {
                AppEntity oldAppEntity = environment()
                        .findLean(new AppEntityQuery().applicationId(applicationId).name(appEntityConfig.getName()));
                description = resolveApplicationMessage(appEntityConfig.getDescription());
                String label = resolveApplicationMessage(appEntityConfig.getLabel());
                Class<? extends BaseEntity> entityClass = (Class<? extends BaseEntity>) ReflectUtils
                        .classForName(appEntityConfig.getType());
                String tableName = !StringUtils.isBlank(appEntityConfig.getDelegate()) ? appEntityConfig.getTable()
                        : ((SqlDataSourceDialect) db().getDataSource().getDialect()).findSqlEntityInfo(entityClass)
                                .getTableName();
                EntityBaseType baseType = ApplicationEntityUtils.getEntityBaseType(entityClass);
                Long entityId = null;
                if (oldAppEntity == null) {
                    logDebug("Installing new application entity [{0}]...", appEntityConfig.getName());
                    appEntity.setId(null);
                    appEntity.setBaseType(baseType);
                    appEntity.setName(appEntityConfig.getName());
                    appEntity.setDescription(description);
                    appEntity.setLabel(label);
                    appEntity.setEmailProducerConsumer(appEntityConfig.getEmailProducerConsumer());
                    appEntity.setDelegate(appEntityConfig.getDelegate());
                    appEntity.setEntityClass(appEntityConfig.getType());
                    appEntity.setTableName(tableName);
                    appEntity.setAuditable(appEntityConfig.isAuditable());
                    appEntity.setReportable(appEntityConfig.isReportable());
                    appEntity.setConfigType(ConfigType.STATIC_INSTALL);
                    populateChildList(appEntity, entityBaseTypeFieldSet, applicationName, appEntityConfig);
                    entityId = (Long) environment().create(appEntity);
                } else {
                    logDebug("Upgrading application entity [{0}]...", appEntityConfig.getName());
                    if (!oldAppEntity.getConfigType().isStatic()) {
                        // TODO Clash with custom entity
                    }

                    if (ConfigUtils.isSetInstall(oldAppEntity)) {
                        oldAppEntity.setBaseType(baseType);
                        oldAppEntity.setDescription(description);
                        oldAppEntity.setLabel(label);
                        oldAppEntity.setEmailProducerConsumer(appEntityConfig.getEmailProducerConsumer());
                        oldAppEntity.setDelegate(appEntityConfig.getDelegate());
                        oldAppEntity.setEntityClass(appEntityConfig.getType());
                        oldAppEntity.setTableName(tableName);
                        oldAppEntity.setAuditable(appEntityConfig.isAuditable());
                        oldAppEntity.setReportable(appEntityConfig.isReportable());
                    }

                    populateChildList(oldAppEntity, entityBaseTypeFieldSet, applicationName, appEntityConfig);
                    environment().updateByIdVersion(oldAppEntity);
                    entityId = oldAppEntity.getId();
                }

                final String entityLongName = ApplicationNameUtils.getApplicationEntityLongName(applicationName,
                        appEntityConfig.getName());
                entityIdByNameMap.put(entityLongName, entityId);
                applicationPrivilegeManager.registerPrivilege(applicationId,
                        ApplicationPrivilegeConstants.APPLICATION_ENTITY_CATEGORY_CODE,
                        PrivilegeNameUtils.getAddPrivilegeName(entityLongName),
                        getApplicationMessage("application.entity.privilege.add", description));
                applicationPrivilegeManager.registerPrivilege(applicationId,
                        ApplicationPrivilegeConstants.APPLICATION_ENTITY_CATEGORY_CODE,
                        PrivilegeNameUtils.getEditPrivilegeName(entityLongName),
                        getApplicationMessage("application.entity.privilege.edit", description));
                applicationPrivilegeManager.registerPrivilege(applicationId,
                        ApplicationPrivilegeConstants.APPLICATION_ENTITY_CATEGORY_CODE,
                        PrivilegeNameUtils.getDeletePrivilegeName(entityLongName),
                        getApplicationMessage("application.entity.privilege.delete", description));
                if (baseType.isWorkEntityType()) {
                    applicationPrivilegeManager.registerPrivilege(applicationId,
                            ApplicationPrivilegeConstants.APPLICATION_ENTITY_CATEGORY_CODE,
                            PrivilegeNameUtils.getAttachPrivilegeName(entityLongName),
                            getApplicationMessage("application.entity.privilege.attach", description));
                }
            }

            logDebug(taskMonitor, "Installed [{0}] application entities...",
                    applicationConfig.getEntitiesConfig().getEntityList().size());
        }

        // Tables
        logDebug(taskMonitor, "Installing application tables...");
        if (applicationConfig.getTablesConfig() != null
                && !DataUtils.isBlank(applicationConfig.getTablesConfig().getTableList())) {
            AppTable appTable = new AppTable();
            appTable.setApplicationId(applicationId);
            for (AppTableConfig appTableConfig : applicationConfig.getTablesConfig().getTableList()) {
                description = resolveApplicationMessage(appTableConfig.getDescription());
                String label = resolveApplicationMessage(appTableConfig.getLabel());
                AppTable oldAppTable = environment()
                        .findLean(new AppTableQuery().applicationId(applicationId).name(appTableConfig.getName()));
                if (oldAppTable == null) {
                    logDebug("Installing new application table [{0}]...", appTableConfig.getName());
                    appTable.setId(null);
                    appTable.setEntity(
                            ApplicationNameUtils.ensureLongNameReference(applicationName, appTableConfig.getEntity()));
                    appTable.setName(appTableConfig.getName());
                    appTable.setDescription(description);
                    appTable.setLabel(label);
                    appTable.setDetailsPanelName(appTableConfig.getDetailsPanelName());
                    appTable.setSortHistory(appTableConfig.getSortHistory());
                    appTable.setItemsPerPage(appTableConfig.getItemsPerPage());
                    appTable.setSerialNo(appTableConfig.isSerialNo());
                    appTable.setSortable(appTableConfig.isSortable());
                    appTable.setHeaderToUpperCase(appTableConfig.isHeaderToUpperCase());
                    appTable.setHeaderCenterAlign(appTableConfig.isHeaderCenterAlign());
                    appTable.setBasicSearch(appTableConfig.isBasicSearch());
                    appTable.setTotalSummary(appTableConfig.isTotalSummary());
                    appTable.setHeaderless(appTableConfig.isHeaderless());
                    appTable.setMultiSelect(appTableConfig.isMultiSelect());
                    appTable.setNonConforming(appTableConfig.isNonConforming());
                    appTable.setFixedRows(appTableConfig.isFixedRows());
                    appTable.setLimitSelectToColumns(appTableConfig.isLimitSelectToColumns());
                    appTable.setConfigType(ConfigType.MUTABLE_INSTALL);
                    populateChildList(appTable, applicationName, appTableConfig);
                    environment().create(appTable);
                } else {
                    logDebug("Upgrading application table [{0}]...", appTableConfig.getName());
                    if (ConfigUtils.isSetInstall(oldAppTable)) {
                        oldAppTable.setEntity(ApplicationNameUtils.ensureLongNameReference(applicationName,
                                appTableConfig.getEntity()));
                        oldAppTable.setDescription(description);
                        oldAppTable.setLabel(label);
                        oldAppTable.setDetailsPanelName(appTableConfig.getDetailsPanelName());
                        oldAppTable.setSortHistory(appTableConfig.getSortHistory());
                        oldAppTable.setItemsPerPage(appTableConfig.getItemsPerPage());
                        oldAppTable.setSerialNo(appTableConfig.isSerialNo());
                        oldAppTable.setSortable(appTableConfig.isSortable());
                        oldAppTable.setHeaderToUpperCase(appTableConfig.isHeaderToUpperCase());
                        oldAppTable.setHeaderCenterAlign(appTableConfig.isHeaderCenterAlign());
                        oldAppTable.setBasicSearch(appTableConfig.isBasicSearch());
                        oldAppTable.setTotalSummary(appTableConfig.isTotalSummary());
                        oldAppTable.setHeaderless(appTableConfig.isHeaderless());
                        oldAppTable.setMultiSelect(appTableConfig.isMultiSelect());
                        oldAppTable.setNonConforming(appTableConfig.isNonConforming());
                        oldAppTable.setFixedRows(appTableConfig.isFixedRows());
                        oldAppTable.setLimitSelectToColumns(appTableConfig.isLimitSelectToColumns());
                    }

                    populateChildList(oldAppTable, applicationName, appTableConfig);
                    environment().updateByIdVersion(oldAppTable);
                }
            }

            logDebug(taskMonitor, "Installed [{0}] application tables...",
                    applicationConfig.getTablesConfig().getTableList().size());
        }

        // Forms
        logDebug(taskMonitor, "Installing application forms...");
        if (applicationConfig.getFormsConfig() != null
                && !DataUtils.isBlank(applicationConfig.getFormsConfig().getFormList())) {
            AppForm appForm = new AppForm();
            appForm.setApplicationId(applicationId);
            for (AppFormConfig appFormConfig : applicationConfig.getFormsConfig().getFormList()) {
                description = resolveApplicationMessage(appFormConfig.getDescription());
                AppForm oldAppForm = environment()
                        .findLean(new AppFormQuery().applicationId(applicationId).name(appFormConfig.getName()));
                if (oldAppForm == null) {
                    logDebug("Installing new application form [{0}]...", appFormConfig.getName());
                    appForm.setId(null);
                    appForm.setType(appFormConfig.getType());
                    appForm.setEntity(
                            ApplicationNameUtils.ensureLongNameReference(applicationName, appFormConfig.getEntity()));
                    appForm.setConsolidatedReview(appFormConfig.getConsolidatedReview());
                    appForm.setConsolidatedValidation(appFormConfig.getConsolidatedValidation());
                    appForm.setConsolidatedState(appFormConfig.getConsolidatedState());
                    appForm.setListingGenerator(appFormConfig.getListingGenerator());
                    appForm.setTitleFormat(appFormConfig.getTitleFormat());
                    appForm.setName(appFormConfig.getName());
                    appForm.setDescription(description);
                    appForm.setConfigType(ConfigType.MUTABLE_INSTALL);
                    populateChildList(appForm, appFormConfig, applicationId, applicationName);
                    environment().create(appForm);
                } else {
                    logDebug("Upgrading application form [{0}]...", appFormConfig.getName());
                    if (ConfigUtils.isSetInstall(oldAppForm)) {
                        oldAppForm.setType(appFormConfig.getType());
                        oldAppForm.setEntity(ApplicationNameUtils.ensureLongNameReference(applicationName,
                                appFormConfig.getEntity()));
                        oldAppForm.setConsolidatedReview(appFormConfig.getConsolidatedReview());
                        oldAppForm.setConsolidatedValidation(appFormConfig.getConsolidatedValidation());
                        oldAppForm.setConsolidatedState(appFormConfig.getConsolidatedState());
                        oldAppForm.setListingGenerator(appFormConfig.getListingGenerator());
                        oldAppForm.setTitleFormat(appFormConfig.getTitleFormat());
                        oldAppForm.setDescription(description);
                    }

                    populateChildList(oldAppForm, appFormConfig, applicationId, applicationName);
                    environment().updateByIdVersion(oldAppForm);
                }
            }

            logDebug(taskMonitor, "Installed [{0}] application forms...",
                    applicationConfig.getFormsConfig().getFormList().size());
        }

        // Property lists
        logDebug(taskMonitor, "Installing application property lists...");
        if (applicationConfig.getPropertyListsConfig() != null
                && !DataUtils.isBlank(applicationConfig.getPropertyListsConfig().getPropertyConfigList())) {
            AppPropertyList appPropertyList = new AppPropertyList();
            appPropertyList.setApplicationId(applicationId);
            for (PropertyListConfig propertyListConfig : applicationConfig.getPropertyListsConfig()
                    .getPropertyConfigList()) {
                description = resolveApplicationMessage(propertyListConfig.getDescription());
                AppPropertyList oldAppPropertyList = environment().findLean(
                        new AppPropertyListQuery().applicationId(applicationId).name(propertyListConfig.getName()));
                if (oldAppPropertyList == null) {
                    logDebug("Installing new application property list [{0}]...", propertyListConfig.getName());
                    appPropertyList.setId(null);
                    appPropertyList.setName(propertyListConfig.getName());
                    appPropertyList.setDescription(description);
                    appPropertyList.setConfigType(ConfigType.STATIC_INSTALL);
                    populateChildList(appPropertyList, applicationName, propertyListConfig);
                    environment().create(appPropertyList);
                } else {
                    logDebug("Upgrading application property list [{0}]...", propertyListConfig.getName());
                    if (ConfigUtils.isSetInstall(oldAppPropertyList)) {
                        oldAppPropertyList.setDescription(description);
                    }

                    populateChildList(oldAppPropertyList, applicationName, propertyListConfig);
                    environment().updateByIdVersion(oldAppPropertyList);
                }
            }

            logDebug(taskMonitor, "Installed [{0}] application property lists...",
                    applicationConfig.getPropertyListsConfig().getPropertyConfigList().size());
        }

        // Property rules
        logDebug(taskMonitor, "Installing application property rules...");
        if (applicationConfig.getPropertyRulesConfig() != null
                && !DataUtils.isBlank(applicationConfig.getPropertyRulesConfig().getPropertyRuleConfigList())) {
            AppPropertyRule appPropertyRule = new AppPropertyRule();
            appPropertyRule.setApplicationId(applicationId);
            for (PropertyRuleConfig propertyRuleConfig : applicationConfig.getPropertyRulesConfig()
                    .getPropertyRuleConfigList()) {
                description = resolveApplicationMessage(propertyRuleConfig.getDescription());
                AppPropertyRule oldAppPropertyRule = environment().findLean(
                        new AppPropertyRuleQuery().applicationId(applicationId).name(propertyRuleConfig.getName()));
                if (oldAppPropertyRule == null) {
                    logDebug("Installing new application property rule [{0}]...", propertyRuleConfig.getName());
                    appPropertyRule.setId(null);
                    appPropertyRule.setName(propertyRuleConfig.getName());
                    appPropertyRule.setDescription(description);
                    appPropertyRule.setEntity(ApplicationNameUtils.ensureLongNameReference(applicationName,
                            propertyRuleConfig.getEntity()));
                    appPropertyRule.setChoiceField(propertyRuleConfig.getChoiceField());
                    appPropertyRule.setListField(propertyRuleConfig.getListField());
                    appPropertyRule.setPropNameField(propertyRuleConfig.getPropNameField());
                    appPropertyRule.setPropValField(propertyRuleConfig.getPropValField());
                    appPropertyRule.setDefaultList(ApplicationNameUtils.ensureLongNameReference(applicationName,
                            propertyRuleConfig.getDefaultList()));
                    appPropertyRule.setIgnoreCase(propertyRuleConfig.isIgnoreCase());
                    appPropertyRule.setConfigType(ConfigType.STATIC_INSTALL);
                    populateChildList(appPropertyRule, applicationName, propertyRuleConfig);
                    environment().create(appPropertyRule);
                } else {
                    logDebug("Upgrading application property rule [{0}]...", propertyRuleConfig.getName());
                    if (ConfigUtils.isSetInstall(oldAppPropertyRule)) {
                        oldAppPropertyRule.setDescription(description);
                        oldAppPropertyRule.setEntity(ApplicationNameUtils.ensureLongNameReference(applicationName,
                                propertyRuleConfig.getEntity()));
                        oldAppPropertyRule.setChoiceField(propertyRuleConfig.getChoiceField());
                        oldAppPropertyRule.setListField(propertyRuleConfig.getListField());
                        oldAppPropertyRule.setPropNameField(propertyRuleConfig.getPropNameField());
                        oldAppPropertyRule.setPropValField(propertyRuleConfig.getPropValField());
                        oldAppPropertyRule.setDefaultList(ApplicationNameUtils.ensureLongNameReference(applicationName,
                                propertyRuleConfig.getDefaultList()));
                        oldAppPropertyRule.setIgnoreCase(propertyRuleConfig.isIgnoreCase());
                    }

                    populateChildList(oldAppPropertyRule, applicationName, propertyRuleConfig);
                    environment().updateByIdVersion(oldAppPropertyRule);
                }
            }

            logDebug(taskMonitor, "Installed [{0}] application property rules...",
                    applicationConfig.getPropertyRulesConfig().getPropertyRuleConfigList().size());
        }

        // Assignment pages
        logDebug(taskMonitor, "Installing application assignment pages...");
        if (applicationConfig.getAssignmentPagesConfig() != null
                && !DataUtils.isBlank(applicationConfig.getAssignmentPagesConfig().getAssignmentPageList())) {
            AppAssignmentPage appAssignmentPage = new AppAssignmentPage();
            appAssignmentPage.setApplicationId(applicationId);
            for (AppAssignmentPageConfig appAssignmentPageConfig : applicationConfig.getAssignmentPagesConfig()
                    .getAssignmentPageList()) {
                AppAssignmentPage oldAppAssignmentPage = environment().findLean(new AppAssignmentPageQuery()
                        .applicationId(applicationId).name(appAssignmentPageConfig.getName()));
                description = resolveApplicationMessage(appAssignmentPageConfig.getDescription());
                String label = resolveApplicationMessage(appAssignmentPageConfig.getLabel());
                if (oldAppAssignmentPage == null) {
                    logDebug("Installing new application assignment page [{0}]...", appAssignmentPageConfig.getName());
                    appAssignmentPage.setId(null);
                    appAssignmentPage.setName(appAssignmentPageConfig.getName());
                    appAssignmentPage.setDescription(description);
                    appAssignmentPage.setLabel(label);
                    if (appAssignmentPageConfig.getFilterCaption1() != null) {
                        appAssignmentPage.setFilterCaption1(
                                resolveApplicationMessage(appAssignmentPageConfig.getFilterCaption1()));
                    }

                    if (appAssignmentPageConfig.getFilterCaption2() != null) {
                        appAssignmentPage.setFilterCaption2(
                                resolveApplicationMessage(appAssignmentPageConfig.getFilterCaption2()));
                    }

                    appAssignmentPage.setFilterList1(appAssignmentPageConfig.getFilterList1());
                    appAssignmentPage.setFilterList2(appAssignmentPageConfig.getFilterList2());

                    appAssignmentPage
                            .setAssignCaption(resolveApplicationMessage(appAssignmentPageConfig.getAssignCaption()));
                    appAssignmentPage.setAssignList(appAssignmentPageConfig.getAssignList());
                    appAssignmentPage.setUnassignCaption(
                            resolveApplicationMessage(appAssignmentPageConfig.getUnassignCaption()));
                    appAssignmentPage.setUnassignList(appAssignmentPageConfig.getUnassignList());
                    appAssignmentPage.setEntity(ApplicationNameUtils.ensureLongNameReference(applicationName,
                            appAssignmentPageConfig.getEntity()));
                    appAssignmentPage.setCommitPolicy(appAssignmentPageConfig.getCommitPolicy());
                    appAssignmentPage.setAssignField(appAssignmentPageConfig.getAssignField());
                    appAssignmentPage.setBaseField(appAssignmentPageConfig.getBaseField());
                    appAssignmentPage.setRuleDescField(appAssignmentPageConfig.getRuleDescField());
                    appAssignmentPage.setConfigType(ConfigType.STATIC_INSTALL);
                    environment().create(appAssignmentPage);
                } else {
                    logDebug("Upgrading application assignment page [{0}]...", appAssignmentPageConfig.getName());
                    if (ConfigUtils.isSetInstall(oldAppAssignmentPage)) {
                        oldAppAssignmentPage.setDescription(description);
                        oldAppAssignmentPage.setLabel(label);
                        if (appAssignmentPageConfig.getFilterCaption1() != null) {
                            oldAppAssignmentPage.setFilterCaption1(
                                    resolveApplicationMessage(appAssignmentPageConfig.getFilterCaption1()));
                        }

                        if (appAssignmentPageConfig.getFilterCaption2() != null) {
                            oldAppAssignmentPage.setFilterCaption2(
                                    resolveApplicationMessage(appAssignmentPageConfig.getFilterCaption2()));
                        }

                        oldAppAssignmentPage.setFilterList1(appAssignmentPageConfig.getFilterList1());
                        oldAppAssignmentPage.setFilterList2(appAssignmentPageConfig.getFilterList2());
                        oldAppAssignmentPage.setAssignCaption(
                                resolveApplicationMessage(appAssignmentPageConfig.getAssignCaption()));
                        oldAppAssignmentPage.setAssignList(appAssignmentPageConfig.getAssignList());
                        oldAppAssignmentPage.setUnassignCaption(
                                resolveApplicationMessage(appAssignmentPageConfig.getUnassignCaption()));
                        oldAppAssignmentPage.setUnassignList(appAssignmentPageConfig.getUnassignList());
                        oldAppAssignmentPage.setEntity(ApplicationNameUtils.ensureLongNameReference(applicationName,
                                appAssignmentPageConfig.getEntity()));
                        oldAppAssignmentPage.setCommitPolicy(appAssignmentPageConfig.getCommitPolicy());
                        oldAppAssignmentPage.setAssignField(appAssignmentPageConfig.getAssignField());
                        oldAppAssignmentPage.setBaseField(appAssignmentPageConfig.getBaseField());
                        oldAppAssignmentPage.setRuleDescField(appAssignmentPageConfig.getRuleDescField());
                    }

                    environment().updateByIdVersion(oldAppAssignmentPage);
                }
            }

            logDebug(taskMonitor, "Installed [{0}] application assignment pages...",
                    applicationConfig.getAssignmentPagesConfig().getAssignmentPageList().size());
        }

        // Suggestions
        logDebug(taskMonitor, "Installing application suggestion types...");
        if (applicationConfig.getSuggestionTypesConfig() != null
                && !DataUtils.isBlank(applicationConfig.getSuggestionTypesConfig().getSuggestionTypeList())) {
            AppSuggestionType appSuggestionType = new AppSuggestionType();
            appSuggestionType.setApplicationId(applicationId);
            for (SuggestionTypeConfig suggestionTypeConfig : applicationConfig.getSuggestionTypesConfig()
                    .getSuggestionTypeList()) {
                AppSuggestionType oldSuggestionType = environment().findLean(
                        new AppSuggestionTypeQuery().applicationId(applicationId).name(suggestionTypeConfig.getName()));
                description = resolveApplicationMessage(suggestionTypeConfig.getDescription());
                if (oldSuggestionType == null) {
                    logDebug("Installing new application suggestion [{0}]...", suggestionTypeConfig.getName());
                    appSuggestionType.setId(null);
                    appSuggestionType.setName(suggestionTypeConfig.getName());
                    appSuggestionType.setDescription(description);
                    appSuggestionType.setParent(ApplicationNameUtils.ensureLongNameReference(applicationName,
                            suggestionTypeConfig.getParent()));
                    appSuggestionType.setConfigType(ConfigType.STATIC_INSTALL);
                    environment().create(appSuggestionType);
                } else {
                    logDebug("Upgrading application suggestion [{0}]...", suggestionTypeConfig.getName());
                    if (ConfigUtils.isSetInstall(oldSuggestionType)) {
                        oldSuggestionType.setDescription(description);
                        oldSuggestionType.setParent(ApplicationNameUtils.ensureLongNameReference(applicationName,
                                suggestionTypeConfig.getParent()));
                        environment().updateByIdVersion(oldSuggestionType);
                    }
                }
            }
        }

        logDebug(taskMonitor, "Installing other application artifacts...");
        if (!DataUtils.isBlank(applicationArtifactInstallerList)) {
            for (ApplicationArtifactInstaller applicationArtifactInstaller : applicationArtifactInstallerList) {
                applicationArtifactInstaller.installApplicationArtifacts(taskMonitor, applicationInstall);
            }
        }

        return true;
    }

    private void populateChildList(AppApplet appApplet, String applicationName, AppletConfig appletConfig)
            throws UnifyException {
        List<AppAppletProp> propList = null;
        if (!DataUtils.isBlank(appletConfig.getPropList())) {
            propList = new ArrayList<AppAppletProp>();
            Map<String, AppAppletProp> map = appApplet.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppAppletPropQuery().appAppletId(appApplet.getId()));
            for (AppletPropConfig appletPropConfig : appletConfig.getPropList()) {
                AppAppletProp oldAppAppletProp = map.get(appletPropConfig.getName());
                if (oldAppAppletProp == null) {
                    AppAppletProp appAppletProp = new AppAppletProp();
                    appAppletProp.setName(appletPropConfig.getName());
                    if (refProperties.contains(appletPropConfig.getName())) {
                        appAppletProp.setValue(ApplicationNameUtils.ensureLongNameReference(applicationName,
                                appletPropConfig.getValue()));
                    } else {
                        appAppletProp.setValue(appletPropConfig.getValue());
                    }

                    appAppletProp.setConfigType(ConfigType.MUTABLE_INSTALL);
                    propList.add(appAppletProp);
                } else {
                    if (ConfigUtils.isSetInstall(oldAppAppletProp)) {
                        if (refProperties.contains(appletPropConfig.getName())) {
                            oldAppAppletProp.setValue(ApplicationNameUtils.ensureLongNameReference(applicationName,
                                    appletPropConfig.getValue()));
                        } else {
                            oldAppAppletProp.setValue(appletPropConfig.getValue());
                        }
                    }

                    propList.add(oldAppAppletProp);
                }
            }
        }
        appApplet.setPropList(propList);

        List<AppAppletSetValues> setValuesList = null;
        if (!DataUtils.isBlank(appletConfig.getSetValuesList())) {
            setValuesList = new ArrayList<AppAppletSetValues>();
            Map<String, AppAppletSetValues> map = appApplet.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppAppletSetValuesQuery().appAppletId(appApplet.getId()));
            for (AppletSetValuesConfig appletSetValuesConfig : appletConfig.getSetValuesList()) {
                AppAppletSetValues oldAppAppletSetValues = map.get(appletSetValuesConfig.getName());
                if (oldAppAppletSetValues == null) {
                    AppAppletSetValues appAppletSetValues = new AppAppletSetValues();
                    appAppletSetValues.setName(appletSetValuesConfig.getName());
                    appAppletSetValues.setDescription(appletSetValuesConfig.getDescription());
                    appAppletSetValues.setValueGenerator(appletSetValuesConfig.getValueGenerator());
                    appAppletSetValues.setSetValues(newAppSetValues(appletSetValuesConfig.getSetValues()));
                    appAppletSetValues.setConfigType(ConfigType.MUTABLE_INSTALL);
                    setValuesList.add(appAppletSetValues);
                } else {
                    if (ConfigUtils.isSetInstall(oldAppAppletSetValues)) {
                        oldAppAppletSetValues
                                .setDescription(resolveApplicationMessage(appletSetValuesConfig.getDescription()));
                        oldAppAppletSetValues.setValueGenerator(appletSetValuesConfig.getValueGenerator());
                        oldAppAppletSetValues.setSetValues(newAppSetValues(appletSetValuesConfig.getSetValues()));
                    } else {
                        environment().findChildren(oldAppAppletSetValues);
                    }

                    setValuesList.add(oldAppAppletSetValues);
                }

            }
        }

        appApplet.setSetValuesList(setValuesList);

        List<AppAppletFilter> filterList = null;
        if (!DataUtils.isBlank(appletConfig.getFilterList())) {
            filterList = new ArrayList<AppAppletFilter>();
            Map<String, AppAppletFilter> map = appApplet.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppAppletFilterQuery().appAppletId(appApplet.getId()));
            for (AppletFilterConfig filterConfig : appletConfig.getFilterList()) {
                AppAppletFilter oldAppAppletFilter = map.get(filterConfig.getName());
                if (oldAppAppletFilter == null) {
                    AppAppletFilter appAppletFilter = new AppAppletFilter();
                    appAppletFilter.setOwnershipType(OwnershipType.GLOBAL);
                    appAppletFilter.setName(filterConfig.getName());
                    appAppletFilter.setDescription(resolveApplicationMessage(filterConfig.getDescription()));
                    appAppletFilter.setFilter(InputWidgetUtils.newAppFilter(filterConfig));
                    appAppletFilter.setPreferredForm(filterConfig.getPreferredForm());
                    appAppletFilter.setPreferredChildListApplet(filterConfig.getPreferredChildListApplet());
                    appAppletFilter.setQuickFilter(filterConfig.isQuickFilter());
                    appAppletFilter.setChildListActionType(filterConfig.getChildListActionType());
                    appAppletFilter.setFilterGenerator(filterConfig.getFilterGenerator());
                    appAppletFilter.setFilterGeneratorRule(filterConfig.getFilterGeneratorRule());
                    appAppletFilter.setConfigType(ConfigType.MUTABLE_INSTALL);
                    filterList.add(appAppletFilter);
                } else {
                    if (ConfigUtils.isSetInstall(oldAppAppletFilter)) {
                        oldAppAppletFilter.setOwnershipType(OwnershipType.GLOBAL);
                        oldAppAppletFilter.setDescription(resolveApplicationMessage(filterConfig.getDescription()));
                        oldAppAppletFilter.setFilter(InputWidgetUtils.newAppFilter(filterConfig));
                        oldAppAppletFilter.setPreferredForm(filterConfig.getPreferredForm());
                        oldAppAppletFilter.setPreferredChildListApplet(filterConfig.getPreferredChildListApplet());
                        oldAppAppletFilter.setQuickFilter(filterConfig.isQuickFilter());
                        oldAppAppletFilter.setChildListActionType(filterConfig.getChildListActionType());
                        oldAppAppletFilter.setFilterGenerator(filterConfig.getFilterGenerator());
                        oldAppAppletFilter.setFilterGeneratorRule(filterConfig.getFilterGeneratorRule());
                    } else {
                        environment().findChildren(oldAppAppletFilter);
                    }

                    filterList.add(oldAppAppletFilter);
                }

            }
        }
        appApplet.setFilterList(filterList);
    }

    private AppFieldSequence newAppFieldSequence(FieldSequenceConfig fieldSequenceConfig) throws UnifyException {
        if (fieldSequenceConfig != null) {
            return new AppFieldSequence(InputWidgetUtils.getFieldSequenceDefinition(fieldSequenceConfig));
        }

        return null;
    }

    private AppSetValues newAppSetValues(SetValuesConfig setValuesConfig) throws UnifyException {
        if (setValuesConfig != null) {
            return new AppSetValues(InputWidgetUtils.getSetValuesDefinition(setValuesConfig));
        }

        return null;
    }

    private AppWidgetRules newAppWidgetRules(WidgetRulesConfig widgetRulesConfig) throws UnifyException {
        if (widgetRulesConfig != null) {
            return new AppWidgetRules(InputWidgetUtils.getWidgetRulesDefinition(widgetRulesConfig));
        }

        return null;
    }

    private void populateChildList(AppEntity appEntity, EntityBaseTypeFieldSet entityBaseTypeFieldSet,
            String applicationName, AppEntityConfig appEntityConfig) throws UnifyException {
        List<AppEntityField> fieldList = new ArrayList<AppEntityField>();
        fieldList.addAll(entityBaseTypeFieldSet.getBaseFieldList(appEntity.getBaseType()));
        Map<String, AppEntityField> map = appEntity.isIdBlank() ? Collections.emptyMap()
                : environment().findAllMap(String.class, "name",
                        new AppEntityFieldQuery().appEntityId(appEntity.getId()));
        if (!DataUtils.isBlank(appEntityConfig.getEntityFieldList())) {
            for (EntityFieldConfig entityFieldConfig : appEntityConfig.getEntityFieldList()) {
                AppEntityField oldAppEntityField = map.remove(entityFieldConfig.getName());
                if (oldAppEntityField == null) {
                    AppEntityField appEntityField = new AppEntityField();
                    appEntityField.setDataType(entityFieldConfig.getType());
                    appEntityField.setType(EntityFieldType.STATIC);
                    appEntityField.setName(entityFieldConfig.getName());
                    appEntityField.setLabel(resolveApplicationMessage(entityFieldConfig.getLabel()));
                    String references = entityFieldConfig.getReferences();
                    if (entityFieldConfig.getType().isEntityRef()
                            || (!entityFieldConfig.getType().isEnumDataType() && !StringUtils.isBlank(references))) {
                        references = ApplicationNameUtils.ensureLongNameReference(applicationName, references);
                    }

                    appEntityField.setColumnName(entityFieldConfig.getColumnName());
                    appEntityField.setReferences(references);
                    appEntityField.setKey(entityFieldConfig.getKey());
                    appEntityField.setProperty(entityFieldConfig.getProperty());
                    appEntityField.setCategory(entityFieldConfig.getCategory());
                    String inputLabel = entityFieldConfig.getInputLabel() == null ? null
                            : resolveApplicationMessage(entityFieldConfig.getInputLabel());
                    appEntityField.setInputLabel(inputLabel);
                    appEntityField.setInputWidget(ApplicationNameUtils.ensureLongNameReference(applicationName,
                            entityFieldConfig.getInputWidget()));
                    appEntityField.setSuggestionType(ApplicationNameUtils.ensureLongNameReference(applicationName,
                            entityFieldConfig.getSuggestionType()));
                    appEntityField.setInputListKey(entityFieldConfig.getInputListKey());
                    appEntityField.setLingualWidget(ApplicationNameUtils.ensureLongNameReference(applicationName,
                            entityFieldConfig.getLingualWidget()));
                    appEntityField.setLingualListKey(entityFieldConfig.getLingualListKey());
                    appEntityField.setAutoFormat(entityFieldConfig.getAutoFormat());
                    appEntityField.setDefaultVal(entityFieldConfig.getDefaultVal());
                    appEntityField.setTextCase(entityFieldConfig.getTextCase());
                    appEntityField.setColumns(entityFieldConfig.getColumns());
                    appEntityField.setRows(entityFieldConfig.getRows());
                    appEntityField.setMinLen(entityFieldConfig.getMinLen());
                    appEntityField.setMaxLen(entityFieldConfig.getMaxLen());
                    appEntityField.setPrecision(entityFieldConfig.getPrecision());
                    appEntityField.setScale(entityFieldConfig.getScale());
                    appEntityField.setAllowNegative(entityFieldConfig.isAllowNegative());
                    appEntityField.setNullable(entityFieldConfig.isNullable());
                    appEntityField.setAuditable(entityFieldConfig.isAuditable());
                    appEntityField.setReportable(entityFieldConfig.isReportable());
                    appEntityField.setDescriptive(entityFieldConfig.isDescriptive());
                    appEntityField.setMaintainLink(entityFieldConfig.isMaintainLink());
                    appEntityField.setBasicSearch(entityFieldConfig.isBasicSearch());
                    appEntityField.setConfigType(ConfigType.STATIC_INSTALL);
                    fieldList.add(appEntityField);
                } else {
                    if (ConfigUtils.isSetInstall(oldAppEntityField)) {
                        oldAppEntityField.setDataType(entityFieldConfig.getType());
                        oldAppEntityField.setType(EntityFieldType.STATIC);
                        oldAppEntityField.setLabel(resolveApplicationMessage(entityFieldConfig.getLabel()));
                        String references = entityFieldConfig.getReferences();
                        if (entityFieldConfig.getType().isEntityRef() || (!entityFieldConfig.getType().isEnumDataType()
                                && !StringUtils.isBlank(references))) {
                            references = ApplicationNameUtils.ensureLongNameReference(applicationName, references);
                        }

                        oldAppEntityField.setColumnName(entityFieldConfig.getColumnName());
                        oldAppEntityField.setReferences(references);
                        oldAppEntityField.setKey(entityFieldConfig.getKey());
                        oldAppEntityField.setProperty(entityFieldConfig.getProperty());
                        oldAppEntityField.setCategory(entityFieldConfig.getCategory());
                        String inputLabel = entityFieldConfig.getInputLabel() == null ? null
                                : resolveApplicationMessage(entityFieldConfig.getInputLabel());
                        oldAppEntityField.setInputLabel(inputLabel);
                        oldAppEntityField.setInputWidget(ApplicationNameUtils.ensureLongNameReference(applicationName,
                                entityFieldConfig.getInputWidget()));
                        oldAppEntityField.setSuggestionType(ApplicationNameUtils
                                .ensureLongNameReference(applicationName, entityFieldConfig.getSuggestionType()));
                        oldAppEntityField.setInputListKey(entityFieldConfig.getInputListKey());
                        oldAppEntityField.setLingualWidget(ApplicationNameUtils.ensureLongNameReference(applicationName,
                                entityFieldConfig.getLingualWidget()));
                        oldAppEntityField.setLingualListKey(entityFieldConfig.getLingualListKey());
                        oldAppEntityField.setAutoFormat(entityFieldConfig.getAutoFormat());
                        oldAppEntityField.setDefaultVal(entityFieldConfig.getDefaultVal());
                        oldAppEntityField.setTextCase(entityFieldConfig.getTextCase());
                        oldAppEntityField.setColumns(entityFieldConfig.getColumns());
                        oldAppEntityField.setRows(entityFieldConfig.getRows());
                        oldAppEntityField.setMinLen(entityFieldConfig.getMinLen());
                        oldAppEntityField.setMaxLen(entityFieldConfig.getMaxLen());
                        oldAppEntityField.setPrecision(entityFieldConfig.getPrecision());
                        oldAppEntityField.setScale(entityFieldConfig.getScale());
                        oldAppEntityField.setAllowNegative(entityFieldConfig.isAllowNegative());
                        oldAppEntityField.setNullable(entityFieldConfig.isNullable());
                        oldAppEntityField.setAuditable(entityFieldConfig.isAuditable());
                        oldAppEntityField.setReportable(entityFieldConfig.isReportable());
                        oldAppEntityField.setDescriptive(entityFieldConfig.isDescriptive());
                        oldAppEntityField.setMaintainLink(entityFieldConfig.isMaintainLink());
                        oldAppEntityField.setBasicSearch(entityFieldConfig.isBasicSearch());
                    }

                    fieldList.add(oldAppEntityField);
                }
            }
        }

        if (!map.isEmpty()) {
            for (AppEntityField appEntityField : map.values()) {
                if (EntityFieldType.CUSTOM.equals(appEntityField.getType())) {
                    fieldList.add(appEntityField);
                }
            }
        }

        appEntity.setFieldList(fieldList);

        List<AppEntityAttachment> attachmentList = null;
        if (!DataUtils.isBlank(appEntityConfig.getAttachmentList())) {
            attachmentList = new ArrayList<AppEntityAttachment>();
            Map<String, AppEntityAttachment> map2 = appEntity.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppEntityAttachmentQuery().appEntityId(appEntity.getId()));
            for (EntityAttachmentConfig entityAttachmentConfig : appEntityConfig.getAttachmentList()) {
                AppEntityAttachment oldAppEntityAttachment = map2.get(entityAttachmentConfig.getName());
                if (oldAppEntityAttachment == null) {
                    AppEntityAttachment appEntityAttachment = new AppEntityAttachment();
                    appEntityAttachment.setType(entityAttachmentConfig.getType());
                    appEntityAttachment.setName(entityAttachmentConfig.getName());
                    appEntityAttachment
                            .setDescription(resolveApplicationMessage(entityAttachmentConfig.getDescription()));
                    appEntityAttachment.setConfigType(ConfigType.STATIC_INSTALL);
                    attachmentList.add(appEntityAttachment);
                } else {
                    if (ConfigUtils.isSetInstall(oldAppEntityAttachment)) {
                        oldAppEntityAttachment.setType(entityAttachmentConfig.getType());
                        oldAppEntityAttachment
                                .setDescription(resolveApplicationMessage(entityAttachmentConfig.getDescription()));
                    }

                    attachmentList.add(oldAppEntityAttachment);
                }
            }
        }

        appEntity.setAttachmentList(attachmentList);

        List<AppEntityExpression> expressionList = null;
        if (!DataUtils.isBlank(appEntityConfig.getExpressionList())) {
            expressionList = new ArrayList<AppEntityExpression>();
            Map<String, AppEntityExpression> map2 = appEntity.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppEntityExpressionQuery().appEntityId(appEntity.getId()));
            for (EntityExpressionConfig entityExpressionConfig : appEntityConfig.getExpressionList()) {
                AppEntityExpression oldAppEntityExpression = map2.get(entityExpressionConfig.getName());
                if (oldAppEntityExpression == null) {
                    AppEntityExpression appEntityExpression = new AppEntityExpression();
                    appEntityExpression.setName(entityExpressionConfig.getName());
                    appEntityExpression
                            .setDescription(resolveApplicationMessage(entityExpressionConfig.getDescription()));
                    appEntityExpression.setExpression(entityExpressionConfig.getExpression());
                    appEntityExpression.setConfigType(ConfigType.MUTABLE_INSTALL);
                    expressionList.add(appEntityExpression);
                } else {
                    if (ConfigUtils.isSetInstall(oldAppEntityExpression)) {
                        oldAppEntityExpression
                                .setDescription(resolveApplicationMessage(entityExpressionConfig.getDescription()));
                        oldAppEntityExpression.setExpression(entityExpressionConfig.getExpression());
                    }

                    expressionList.add(oldAppEntityExpression);
                }
            }
        }

        appEntity.setExpressionList(expressionList);

        List<AppEntityUniqueConstraint> uniqueConstraintList = null;
        if (!DataUtils.isBlank(appEntityConfig.getUniqueConstraintList())) {
            uniqueConstraintList = new ArrayList<AppEntityUniqueConstraint>();
            Map<String, AppEntityUniqueConstraint> map2 = appEntity.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppEntityUniqueConstraintQuery().appEntityId(appEntity.getId()));
            for (EntityUniqueConstraintConfig uniqueConstraintConfig : appEntityConfig.getUniqueConstraintList()) {
                AppEntityUniqueConstraint oldAppEntityUniqueConstraint = map2.get(uniqueConstraintConfig.getName());
                if (oldAppEntityUniqueConstraint == null) {
                    AppEntityUniqueConstraint appUniqueConstraint = new AppEntityUniqueConstraint();
                    appUniqueConstraint.setName(uniqueConstraintConfig.getName());
                    appUniqueConstraint
                            .setDescription(resolveApplicationMessage(uniqueConstraintConfig.getDescription()));
                    appUniqueConstraint.setFieldList(uniqueConstraintConfig.getFieldList());
                    appUniqueConstraint.setConfigType(ConfigType.STATIC_INSTALL);
                    uniqueConstraintList.add(appUniqueConstraint);
                } else {
                    if (ConfigUtils.isSetInstall(oldAppEntityUniqueConstraint)) {
                        oldAppEntityUniqueConstraint
                                .setDescription(resolveApplicationMessage(uniqueConstraintConfig.getDescription()));
                        oldAppEntityUniqueConstraint.setFieldList(uniqueConstraintConfig.getFieldList());
                    }

                    uniqueConstraintList.add(oldAppEntityUniqueConstraint);
                }

            }
        }

        appEntity.setUniqueConstraintList(uniqueConstraintList);

        List<AppEntityIndex> indexList = null;
        if (!DataUtils.isBlank(appEntityConfig.getIndexList())) {
            indexList = new ArrayList<AppEntityIndex>();
            Map<String, AppEntityIndex> map2 = appEntity.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppEntityIndexQuery().appEntityId(appEntity.getId()));
            for (EntityIndexConfig indexConfig : appEntityConfig.getIndexList()) {
                AppEntityIndex oldAppEntityIndex = map2.get(indexConfig.getName());
                if (oldAppEntityIndex == null) {
                    AppEntityIndex appIndex = new AppEntityIndex();
                    appIndex.setName(indexConfig.getName());
                    appIndex.setDescription(resolveApplicationMessage(indexConfig.getDescription()));
                    appIndex.setFieldList(indexConfig.getFieldList());
                    appIndex.setConfigType(ConfigType.STATIC_INSTALL);
                    indexList.add(appIndex);
                } else {
                    if (ConfigUtils.isSetInstall(oldAppEntityIndex)) {
                        oldAppEntityIndex.setDescription(resolveApplicationMessage(indexConfig.getDescription()));
                        oldAppEntityIndex.setFieldList(indexConfig.getFieldList());
                    }

                    indexList.add(oldAppEntityIndex);
                }

            }
        }

        appEntity.setIndexList(indexList);

        List<AppEntityUpload> uploadList = null;
        if (!DataUtils.isBlank(appEntityConfig.getUploadList())) {
            uploadList = new ArrayList<AppEntityUpload>();
            Map<String, AppEntityUpload> map2 = appEntity.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppEntityUploadQuery().appEntityId(appEntity.getId()));
            for (EntityUploadConfig uploadConfig : appEntityConfig.getUploadList()) {
                AppEntityUpload oldAppEntityUpload = map2.get(uploadConfig.getName());
                if (oldAppEntityUpload == null) {
                    AppEntityUpload appEntityUpload = new AppEntityUpload();
                    appEntityUpload.setName(uploadConfig.getName());
                    appEntityUpload.setDescription(resolveApplicationMessage(uploadConfig.getDescription()));
                    appEntityUpload.setConstraintAction(uploadConfig.getConstraintAction());
                    appEntityUpload.setFieldSequence(newAppFieldSequence(uploadConfig.getFieldSequence()));
                    appEntityUpload.setConfigType(ConfigType.STATIC_INSTALL);
                    uploadList.add(appEntityUpload);
                } else {
                    if (ConfigUtils.isSetInstall(oldAppEntityUpload)) {
                        oldAppEntityUpload.setDescription(resolveApplicationMessage(uploadConfig.getDescription()));
                        oldAppEntityUpload.setConstraintAction(uploadConfig.getConstraintAction());
                        oldAppEntityUpload.setFieldSequence(newAppFieldSequence(uploadConfig.getFieldSequence()));
                    }

                    uploadList.add(oldAppEntityUpload);
                }

            }
        }

        appEntity.setUploadList(uploadList);
    }

    private void populateChildList(AppTable appTable, String applicationName, AppTableConfig appTableConfig)
            throws UnifyException {
        List<AppTableColumn> columnList = new ArrayList<AppTableColumn>();
        for (TableColumnConfig tableColumnConfig : appTableConfig.getColumnList()) {
            AppTableColumn appTableColumn = new AppTableColumn();
            appTableColumn.setField(tableColumnConfig.getField());
            appTableColumn.setLabel(resolveApplicationMessage(tableColumnConfig.getLabel()));
            appTableColumn.setRenderWidget(
                    ApplicationNameUtils.ensureLongNameReference(applicationName, tableColumnConfig.getRenderWidget()));
            appTableColumn.setLinkAct(tableColumnConfig.getLinkAct());
            appTableColumn.setOrder(tableColumnConfig.getOrder());
            appTableColumn.setWidthRatio(tableColumnConfig.getWidthRatio());
            appTableColumn.setSwitchOnChange(tableColumnConfig.isSwitchOnChange());
            appTableColumn.setEditable(tableColumnConfig.isEditable());
            appTableColumn.setHidden(tableColumnConfig.isHidden());
            appTableColumn.setDisabled(tableColumnConfig.isDisabled());
            appTableColumn.setSortable(tableColumnConfig.isSortable());
            appTableColumn.setSummary(tableColumnConfig.isSummary());
            appTableColumn.setConfigType(ConfigType.MUTABLE_INSTALL);
            columnList.add(appTableColumn);
        }

        appTable.setColumnList(columnList);

        List<AppTableFilter> filterList = null;
        if (!DataUtils.isBlank(appTableConfig.getFilterList())) {
            filterList = new ArrayList<AppTableFilter>();
            Map<String, AppTableFilter> map = appTable.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppTableFilterQuery().appTableId(appTable.getId()));
            for (TableFilterConfig filterConfig : appTableConfig.getFilterList()) {
                if (!DataUtils.isBlank(filterConfig.getRestrictionList())) {
                    AppTableFilter oldAppTableFilter = map.get(filterConfig.getName());
                    if (oldAppTableFilter == null) {
                        AppTableFilter appTableFilter = new AppTableFilter();
                        appTableFilter.setName(filterConfig.getName());
                        appTableFilter.setDescription(resolveApplicationMessage(filterConfig.getDescription()));
                        appTableFilter.setFilterGenerator(filterConfig.getFilterGenerator());
                        appTableFilter.setFilterGeneratorRule(filterConfig.getFilterGeneratorRule());
                        appTableFilter.setFilter(InputWidgetUtils.newAppFilter(filterConfig));
                        appTableFilter.setRowColor(filterConfig.getRowColor());
                        appTableFilter.setLegendLabel(filterConfig.getLegendLabel());
                        appTableFilter.setConfigType(ConfigType.MUTABLE_INSTALL);
                        filterList.add(appTableFilter);
                    } else {
                        if (ConfigUtils.isSetInstall(oldAppTableFilter)) {
                            oldAppTableFilter.setDescription(resolveApplicationMessage(filterConfig.getDescription()));
                            oldAppTableFilter.setFilterGenerator(filterConfig.getFilterGenerator());
                            oldAppTableFilter.setFilterGeneratorRule(filterConfig.getFilterGeneratorRule());
                            oldAppTableFilter.setFilter(InputWidgetUtils.newAppFilter(filterConfig));
                            oldAppTableFilter.setRowColor(filterConfig.getRowColor());
                            oldAppTableFilter.setLegendLabel(filterConfig.getLegendLabel());
                        } else {
                            environment().findChildren(oldAppTableFilter);
                        }

                        filterList.add(oldAppTableFilter);
                    }
                }
            }
        }
        appTable.setFilterList(filterList);

        List<AppTableAction> actionList = null;
        if (!DataUtils.isBlank(appTableConfig.getActionList())) {
            actionList = new ArrayList<AppTableAction>();
            Map<String, AppTableAction> map = appTable.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppTableActionQuery().appTableId(appTable.getId()));
            for (TableActionConfig tableActionConfig : appTableConfig.getActionList()) {
                AppTableAction oldAppTableAction = map.get(tableActionConfig.getName());
                if (oldAppTableAction == null) {
                    AppTableAction appTableAction = new AppTableAction();
                    appTableAction.setName(tableActionConfig.getName());
                    appTableAction.setDescription(resolveApplicationMessage(tableActionConfig.getDescription()));
                    appTableAction.setLabel(resolveApplicationMessage(tableActionConfig.getLabel()));
                    appTableAction.setPolicy(tableActionConfig.getPolicy());
                    appTableAction.setOrderIndex(tableActionConfig.getOrderIndex());
                    appTableAction.setConfigType(ConfigType.MUTABLE_INSTALL);
                    actionList.add(appTableAction);
                } else {
                    if (ConfigUtils.isSetInstall(oldAppTableAction)) {
                        oldAppTableAction.setDescription(resolveApplicationMessage(tableActionConfig.getDescription()));
                        oldAppTableAction.setLabel(resolveApplicationMessage(tableActionConfig.getLabel()));
                        oldAppTableAction.setPolicy(tableActionConfig.getPolicy());
                        oldAppTableAction.setOrderIndex(tableActionConfig.getOrderIndex());
                    } else {
                        environment().findChildren(oldAppTableAction);
                    }

                    actionList.add(oldAppTableAction);
                }
            }
        }

        appTable.setActionList(actionList);

        List<AppTableLoading> loadingList = null;
        if (!DataUtils.isBlank(appTableConfig.getLoadingList())) {
            loadingList = new ArrayList<AppTableLoading>();
            Map<String, AppTableLoading> map = appTable.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppTableLoadingQuery().appTableId(appTable.getId()));
            for (TableLoadingConfig tableLoadingConfig : appTableConfig.getLoadingList()) {
                AppTableLoading oldAppTableLoading = map.get(tableLoadingConfig.getName());
                if (oldAppTableLoading == null) {
                    AppTableLoading appTableLoading = new AppTableLoading();
                    appTableLoading.setName(tableLoadingConfig.getName());
                    appTableLoading.setDescription(resolveApplicationMessage(tableLoadingConfig.getDescription()));
                    appTableLoading.setLabel(resolveApplicationMessage(tableLoadingConfig.getLabel()));
                    appTableLoading.setProvider(tableLoadingConfig.getProvider());
                    appTableLoading.setOrderIndex(tableLoadingConfig.getOrderIndex());
                    appTableLoading.setConfigType(ConfigType.MUTABLE_INSTALL);
                    loadingList.add(appTableLoading);
                } else {
                    if (ConfigUtils.isSetInstall(oldAppTableLoading)) {
                        oldAppTableLoading
                                .setDescription(resolveApplicationMessage(tableLoadingConfig.getDescription()));
                        oldAppTableLoading.setLabel(resolveApplicationMessage(tableLoadingConfig.getLabel()));
                        oldAppTableLoading.setProvider(tableLoadingConfig.getProvider());
                        oldAppTableLoading.setOrderIndex(tableLoadingConfig.getOrderIndex());
                    } else {
                        environment().findChildren(oldAppTableLoading);
                    }

                    loadingList.add(oldAppTableLoading);
                }
            }
        }

        appTable.setLoadingList(loadingList);
    }

    private void populateChildList(final AppForm appForm, AppFormConfig appFormConfig, final Long applicationId,
            String applicationName) throws UnifyException {
        // Filters
        List<AppFormFilter> filterList = null;
        if (!DataUtils.isBlank(appFormConfig.getFilterList())) {
            filterList = new ArrayList<AppFormFilter>();
            Map<String, AppFormFilter> map = appForm.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppFormFilterQuery().appFormId(appForm.getId()));
            for (FormFilterConfig filterConfig : appFormConfig.getFilterList()) {
                AppFormFilter oldAppFormFilter = map.get(filterConfig.getName());
                if (oldAppFormFilter == null) {
                    AppFormFilter appFormFilter = new AppFormFilter();
                    appFormFilter.setName(filterConfig.getName());
                    appFormFilter.setDescription(resolveApplicationMessage(filterConfig.getDescription()));
                    appFormFilter.setFilterGenerator(filterConfig.getFilterGenerator());
                    appFormFilter.setFilterGeneratorRule(filterConfig.getFilterGeneratorRule());
                    appFormFilter.setFilter(InputWidgetUtils.newAppFilter(filterConfig));
                    appFormFilter.setConfigType(ConfigType.MUTABLE_INSTALL);
                    filterList.add(appFormFilter);
                } else {
                    if (ConfigUtils.isSetInstall(oldAppFormFilter)) {
                        oldAppFormFilter.setDescription(resolveApplicationMessage(filterConfig.getDescription()));
                        oldAppFormFilter.setFilterGenerator(filterConfig.getFilterGenerator());
                        oldAppFormFilter.setFilterGeneratorRule(filterConfig.getFilterGeneratorRule());
                        oldAppFormFilter.setFilter(InputWidgetUtils.newAppFilter(filterConfig));
                    } else {
                        environment().findChildren(oldAppFormFilter);
                    }

                    filterList.add(oldAppFormFilter);
                }

            }
        }
        appForm.setFilterList(filterList);

        // Form annotations
        List<AppFormAnnotation> annotationList = null;
        if (!DataUtils.isBlank(appFormConfig.getAnnotationList())) {
            annotationList = new ArrayList<AppFormAnnotation>();
            Map<String, AppFormAnnotation> map = appForm.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppFormAnnotationQuery().appFormId(appForm.getId()));
            for (FormAnnotationConfig formAnnotationConfig : appFormConfig.getAnnotationList()) {
                AppFormAnnotation oldAppFormAnnotation = map.get(formAnnotationConfig.getName());
                String description = resolveApplicationMessage(formAnnotationConfig.getDescription());
                String message = resolveApplicationMessage(formAnnotationConfig.getMessage());
                if (oldAppFormAnnotation == null) {
                    AppFormAnnotation appFormAnnotation = new AppFormAnnotation();
                    appFormAnnotation.setType(formAnnotationConfig.getType());
                    appFormAnnotation.setName(formAnnotationConfig.getName());
                    appFormAnnotation.setDescription(description);
                    appFormAnnotation.setMessage(message);
                    appFormAnnotation.setHtml(formAnnotationConfig.isHtml());
                    appFormAnnotation.setConfigType(ConfigType.MUTABLE_INSTALL);
                    annotationList.add(appFormAnnotation);
                } else {
                    if (ConfigUtils.isSetInstall(oldAppFormAnnotation)) {
                        oldAppFormAnnotation.setType(formAnnotationConfig.getType());
                        oldAppFormAnnotation.setDescription(description);
                        oldAppFormAnnotation.setMessage(message);
                        oldAppFormAnnotation.setHtml(formAnnotationConfig.isHtml());
                    }

                    annotationList.add(oldAppFormAnnotation);
                }
            }
        }

        appForm.setAnnotationList(annotationList);

        // Form actions
        List<AppFormAction> actionList = null;
        if (!DataUtils.isBlank(appFormConfig.getActionList())) {
            actionList = new ArrayList<AppFormAction>();
            Map<String, AppFormAction> map = appForm.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppFormActionQuery().appFormId(appForm.getId()));
            for (FormActionConfig formActionConfig : appFormConfig.getActionList()) {
                AppFormAction oldAppFormAction = map.get(formActionConfig.getName());
                String description = resolveApplicationMessage(formActionConfig.getDescription());
                String label = resolveApplicationMessage(formActionConfig.getLabel());
                if (oldAppFormAction == null) {
                    AppFormAction appFormAction = new AppFormAction();
                    appFormAction.setType(formActionConfig.getType());
                    appFormAction.setHighlightType(formActionConfig.getHighlightType());
                    appFormAction.setName(formActionConfig.getName());
                    appFormAction.setDescription(description);
                    appFormAction.setLabel(label);
                    appFormAction.setSymbol(formActionConfig.getSymbol());
                    appFormAction.setStyleClass(formActionConfig.getStyleClass());
                    appFormAction.setPolicy(formActionConfig.getPolicy());
                    appFormAction.setShowOnCreate(formActionConfig.isShowOnCreate());
                    appFormAction.setShowOnMaintain(formActionConfig.isShowOnMaintain());
                    appFormAction.setValidateForm(formActionConfig.isValidateForm());
                    appFormAction.setOrderIndex(formActionConfig.getOrderIndex());
                    appFormAction.setOnCondition(InputWidgetUtils.newAppFilter(formActionConfig.getOnCondition()));
                    appFormAction.setConfigType(ConfigType.MUTABLE_INSTALL);
                    actionList.add(appFormAction);
                } else {
                    if (ConfigUtils.isSetInstall(oldAppFormAction)) {
                        oldAppFormAction.setType(formActionConfig.getType());
                        oldAppFormAction.setHighlightType(formActionConfig.getHighlightType());
                        oldAppFormAction.setDescription(description);
                        oldAppFormAction.setLabel(label);
                        oldAppFormAction.setSymbol(formActionConfig.getSymbol());
                        oldAppFormAction.setStyleClass(formActionConfig.getStyleClass());
                        oldAppFormAction.setPolicy(formActionConfig.getPolicy());
                        oldAppFormAction.setShowOnCreate(formActionConfig.isShowOnCreate());
                        oldAppFormAction.setShowOnMaintain(formActionConfig.isShowOnMaintain());
                        oldAppFormAction.setValidateForm(formActionConfig.isValidateForm());
                        oldAppFormAction.setOrderIndex(formActionConfig.getOrderIndex());
                        oldAppFormAction
                                .setOnCondition(InputWidgetUtils.newAppFilter(formActionConfig.getOnCondition()));
                    }

                    actionList.add(oldAppFormAction);
                }

                if (!applicationPrivilegeManager.isRegisteredPrivilege(
                        ApplicationPrivilegeConstants.APPLICATION_FORMACTION_CATEGORY_CODE,
                        PrivilegeNameUtils.getFormActionPrivilegeName(formActionConfig.getName()))) {
                    applicationPrivilegeManager.registerPrivilege(applicationId,
                            ApplicationPrivilegeConstants.APPLICATION_FORMACTION_CATEGORY_CODE,
                            PrivilegeNameUtils.getFormActionPrivilegeName(formActionConfig.getName()), description);
                }
            }
        }

        appForm.setActionList(actionList);

        // Form elements
        List<AppFormElement> elementList = new ArrayList<AppFormElement>();
        for (FormTabConfig formTabConfig : appFormConfig.getTabList()) {
            // Tab
            AppFormElement appFormElement = new AppFormElement();
            appFormElement.setType(FormElementType.TAB);
            appFormElement.setElementName(formTabConfig.getName());
            appFormElement.setTabContentType(formTabConfig.getContentType());
            appFormElement.setLabel(resolveApplicationMessage(formTabConfig.getLabel()));
            appFormElement.setTabApplet(
                    ApplicationNameUtils.ensureLongNameReference(applicationName, formTabConfig.getApplet()));
            appFormElement.setTabReference(formTabConfig.getReference());
            appFormElement.setFilter(formTabConfig.getFilter());
            appFormElement.setEditAction(formTabConfig.getEditAction());
            appFormElement.setEditFormless(formTabConfig.getEditFormless());
            appFormElement.setEditFixedRows(formTabConfig.getEditFixedRows());
            appFormElement.setIgnoreParentCondition(formTabConfig.isIgnoreParentCondition());
            appFormElement.setShowSearch(formTabConfig.isShowSearch());
            appFormElement.setVisible(formTabConfig.isVisible());
            appFormElement.setEditable(formTabConfig.isEditable());
            appFormElement.setDisabled(formTabConfig.isDisabled());
            appFormElement.setConfigType(ConfigType.MUTABLE_INSTALL);
            elementList.add(appFormElement);

            if (TabContentType.MINIFORM.equals(formTabConfig.getContentType())) {
                for (FormSectionConfig formSectionConfig : formTabConfig.getSectionList()) {
                    // Section
                    appFormElement = new AppFormElement();
                    appFormElement.setType(FormElementType.SECTION);
                    appFormElement.setElementName(formSectionConfig.getName());
                    appFormElement.setSectionColumns(formSectionConfig.getColumns());
                    appFormElement.setLabel(resolveApplicationMessage(formSectionConfig.getLabel()));
                    appFormElement.setVisible(formSectionConfig.isVisible());
                    appFormElement.setEditable(formSectionConfig.isEditable());
                    appFormElement.setDisabled(formSectionConfig.isDisabled());
                    appFormElement.setConfigType(ConfigType.MUTABLE_INSTALL);
                    elementList.add(appFormElement);

                    if (!DataUtils.isBlank(formSectionConfig.getFieldList())) {
                        for (FormFieldConfig formFieldConfig : formSectionConfig.getFieldList()) {
                            // Field
                            appFormElement = new AppFormElement();
                            appFormElement.setType(FormElementType.FIELD);
                            appFormElement.setElementName(formFieldConfig.getName());
                            appFormElement.setLabel(resolveApplicationMessage(formFieldConfig.getLabel()));
                            appFormElement.setInputWidget(ApplicationNameUtils.ensureLongNameReference(applicationName,
                                    formFieldConfig.getInputWidget()));
                            appFormElement.setInputReference(ApplicationNameUtils
                                    .ensureLongNameReference(applicationName, formFieldConfig.getReference()));
                            appFormElement.setFieldColumn(formFieldConfig.getColumn());
                            appFormElement.setSwitchOnChange(formFieldConfig.isSwitchOnChange());
                            appFormElement.setSaveAs(formFieldConfig.isSaveAs());
                            appFormElement.setRequired(formFieldConfig.isRequired());
                            appFormElement.setVisible(formFieldConfig.isVisible());
                            appFormElement.setColor(formFieldConfig.getColor());
                            appFormElement.setEditable(formFieldConfig.isEditable());
                            appFormElement.setDisabled(formFieldConfig.isDisabled());
                            appFormElement.setConfigType(ConfigType.MUTABLE_INSTALL);
                            elementList.add(appFormElement);
                        }
                    }
                }
            } else if (TabContentType.MINIFORM_CHANGELOG.equals(formTabConfig.getContentType())) {
                ApplicationEntityUtils.addChangeLogFormElements(elementList);
            }
        }

        appForm.setElementList(elementList);

        // Related lists
        List<AppFormRelatedList> relatedList = null;
        if (!DataUtils.isBlank(appFormConfig.getRelatedList())) {
            relatedList = new ArrayList<AppFormRelatedList>();
            Map<String, AppFormRelatedList> map = appForm.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppFormRelatedListQuery().appFormId(appForm.getId()));
            for (RelatedListConfig relatedListConfig : appFormConfig.getRelatedList()) {
                AppFormRelatedList oldAppFormRelatedList = map.get(relatedListConfig.getName());
                if (oldAppFormRelatedList == null) {
                    AppFormRelatedList appFormRelatedList = new AppFormRelatedList();
                    appFormRelatedList.setName(relatedListConfig.getName());
                    appFormRelatedList.setDescription(resolveApplicationMessage(relatedListConfig.getDescription()));
                    appFormRelatedList.setLabel(resolveApplicationMessage(relatedListConfig.getLabel()));
                    appFormRelatedList.setApplet(ApplicationNameUtils.ensureLongNameReference(applicationName,
                            relatedListConfig.getApplet()));
                    appFormRelatedList.setFilter(relatedListConfig.getFilter());
                    appFormRelatedList.setEditAction(relatedListConfig.getEditAction());
                    appFormRelatedList.setConfigType(ConfigType.MUTABLE_INSTALL);
                    relatedList.add(appFormRelatedList);
                } else {
                    if (ConfigUtils.isSetInstall(oldAppFormRelatedList)) {
                        oldAppFormRelatedList
                                .setDescription(resolveApplicationMessage(relatedListConfig.getDescription()));
                        oldAppFormRelatedList.setLabel(resolveApplicationMessage(relatedListConfig.getLabel()));
                        oldAppFormRelatedList.setApplet(ApplicationNameUtils.ensureLongNameReference(applicationName,
                                relatedListConfig.getApplet()));
                        oldAppFormRelatedList.setFilter(relatedListConfig.getFilter());
                        oldAppFormRelatedList.setEditAction(relatedListConfig.getEditAction());
                    }

                    relatedList.add(oldAppFormRelatedList);
                }

            }
        }

        appForm.setRelatedList(relatedList);

        // Form state policies
        List<AppFormStatePolicy> fieldStateList = null;
        if (!DataUtils.isBlank(appFormConfig.getFormStatePolicyList())) {
            fieldStateList = new ArrayList<AppFormStatePolicy>();
            Map<String, AppFormStatePolicy> map = appForm.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppFormStatePolicyQuery().appFormId(appForm.getId()));
            for (FormStatePolicyConfig formStatePolicyConfig : appFormConfig.getFormStatePolicyList()) {
                AppFormStatePolicy oldAppFormStatePolicy = map.get(formStatePolicyConfig.getName());
                if (oldAppFormStatePolicy == null) {
                    AppFormStatePolicy appFormStatePolicy = new AppFormStatePolicy();
                    appFormStatePolicy.setType(formStatePolicyConfig.getType());
                    appFormStatePolicy.setName(formStatePolicyConfig.getName());
                    appFormStatePolicy
                            .setDescription(resolveApplicationMessage(formStatePolicyConfig.getDescription()));
                    appFormStatePolicy.setValueGenerator(formStatePolicyConfig.getValueGenerator());
                    appFormStatePolicy.setTrigger(formStatePolicyConfig.getTrigger());
                    appFormStatePolicy
                            .setOnCondition(InputWidgetUtils.newAppFilter(formStatePolicyConfig.getOnCondition()));
                    appFormStatePolicy.setSetValues(newAppSetValues(formStatePolicyConfig.getSetValues()));
                    appFormStatePolicy.setConfigType(ConfigType.MUTABLE_INSTALL);
                    populateChildList(appFormStatePolicy, formStatePolicyConfig);
                    fieldStateList.add(appFormStatePolicy);
                } else {
                    if (ConfigUtils.isSetInstall(oldAppFormStatePolicy)) {
                        oldAppFormStatePolicy.setType(formStatePolicyConfig.getType());
                        oldAppFormStatePolicy
                                .setDescription(resolveApplicationMessage(formStatePolicyConfig.getDescription()));
                        oldAppFormStatePolicy.setValueGenerator(formStatePolicyConfig.getValueGenerator());
                        oldAppFormStatePolicy.setTrigger(formStatePolicyConfig.getTrigger());
                        oldAppFormStatePolicy
                                .setOnCondition(InputWidgetUtils.newAppFilter(formStatePolicyConfig.getOnCondition()));
                        oldAppFormStatePolicy.setSetValues(newAppSetValues(formStatePolicyConfig.getSetValues()));
                        populateChildList(oldAppFormStatePolicy, formStatePolicyConfig);
                    } else {
                        environment().findChildren(oldAppFormStatePolicy);
                    }

                    fieldStateList.add(oldAppFormStatePolicy);
                }
            }
        }

        appForm.setFieldStateList(fieldStateList);

        // Form widget rule policies
        List<AppFormWidgetRulesPolicy> widgetRulesList = null;
        if (!DataUtils.isBlank(appFormConfig.getWidgetRulesPolicyList())) {
            widgetRulesList = new ArrayList<AppFormWidgetRulesPolicy>();
            Map<String, AppFormWidgetRulesPolicy> map = appForm.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppFormWidgetRulesPolicyQuery().appFormId(appForm.getId()));
            for (FormWidgetRulesPolicyConfig formWidgetRulesPolicyConfig : appFormConfig.getWidgetRulesPolicyList()) {
                AppFormWidgetRulesPolicy oldAppFormWidgetRulesPolicy = map.get(formWidgetRulesPolicyConfig.getName());
                if (oldAppFormWidgetRulesPolicy == null) {
                    AppFormWidgetRulesPolicy appFormWidgetRulesPolicy = new AppFormWidgetRulesPolicy();
                    appFormWidgetRulesPolicy.setName(formWidgetRulesPolicyConfig.getName());
                    appFormWidgetRulesPolicy
                            .setDescription(resolveApplicationMessage(formWidgetRulesPolicyConfig.getDescription()));
                    appFormWidgetRulesPolicy.setOnCondition(
                            InputWidgetUtils.newAppFilter(formWidgetRulesPolicyConfig.getOnCondition()));
                    appFormWidgetRulesPolicy
                            .setWidgetRules(newAppWidgetRules(formWidgetRulesPolicyConfig.getWidgetRules()));
                    appFormWidgetRulesPolicy.setConfigType(ConfigType.MUTABLE_INSTALL);
                    widgetRulesList.add(appFormWidgetRulesPolicy);
                } else {
                    if (ConfigUtils.isSetInstall(oldAppFormWidgetRulesPolicy)) {
                        oldAppFormWidgetRulesPolicy.setDescription(
                                resolveApplicationMessage(formWidgetRulesPolicyConfig.getDescription()));
                        oldAppFormWidgetRulesPolicy.setOnCondition(
                                InputWidgetUtils.newAppFilter(formWidgetRulesPolicyConfig.getOnCondition()));
                        oldAppFormWidgetRulesPolicy
                                .setWidgetRules(newAppWidgetRules(formWidgetRulesPolicyConfig.getWidgetRules()));
                    } else {
                        environment().findChildren(oldAppFormWidgetRulesPolicy);
                    }

                    widgetRulesList.add(oldAppFormWidgetRulesPolicy);
                }
            }
        }

        appForm.setWidgetRulesList(widgetRulesList);

        // Form field validation policies
        List<AppFormFieldValidationPolicy> fieldValidationList = null;
        if (!DataUtils.isBlank(appFormConfig.getFieldValidationPolicyList())) {
            fieldValidationList = new ArrayList<AppFormFieldValidationPolicy>();
            Map<String, AppFormFieldValidationPolicy> map = appForm.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppFormFieldValidationPolicyQuery().appFormId(appForm.getId()));
            for (FieldValidationPolicyConfig fieldValidationPolicyConfig : appFormConfig
                    .getFieldValidationPolicyList()) {
                AppFormFieldValidationPolicy oldAppFormFieldValidationPolicy = map
                        .get(fieldValidationPolicyConfig.getName());
                if (oldAppFormFieldValidationPolicy == null) {
                    AppFormFieldValidationPolicy appFormFieldValidationPolicy = new AppFormFieldValidationPolicy();
                    appFormFieldValidationPolicy.setName(fieldValidationPolicyConfig.getName());
                    appFormFieldValidationPolicy
                            .setDescription(resolveApplicationMessage(fieldValidationPolicyConfig.getDescription()));
                    appFormFieldValidationPolicy.setFieldName(fieldValidationPolicyConfig.getFieldName());
                    appFormFieldValidationPolicy.setValidation(fieldValidationPolicyConfig.getValidator());
                    appFormFieldValidationPolicy.setRule(fieldValidationPolicyConfig.getRule());
                    appFormFieldValidationPolicy.setConfigType(ConfigType.MUTABLE_INSTALL);
                    fieldValidationList.add(appFormFieldValidationPolicy);
                } else {
                    if (ConfigUtils.isSetInstall(oldAppFormFieldValidationPolicy)) {
                        oldAppFormFieldValidationPolicy.setDescription(
                                resolveApplicationMessage(fieldValidationPolicyConfig.getDescription()));
                        oldAppFormFieldValidationPolicy.setFieldName(fieldValidationPolicyConfig.getFieldName());
                        oldAppFormFieldValidationPolicy.setValidation(fieldValidationPolicyConfig.getValidator());
                        oldAppFormFieldValidationPolicy.setRule(fieldValidationPolicyConfig.getRule());
                    }

                    fieldValidationList.add(oldAppFormFieldValidationPolicy);
                }
            }
        }

        appForm.setFieldValidationList(fieldValidationList);

        // Form validation policies
        List<AppFormValidationPolicy> formValidationList = null;
        if (!DataUtils.isBlank(appFormConfig.getFormValidationPolicyList())) {
            formValidationList = new ArrayList<AppFormValidationPolicy>();
            Map<String, AppFormValidationPolicy> map = appForm.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppFormValidationPolicyQuery().appFormId(appForm.getId()));
            for (FormValidationPolicyConfig formValidationPolicyConfig : appFormConfig.getFormValidationPolicyList()) {
                AppFormValidationPolicy oldAppFormValidationPolicy = map.get(formValidationPolicyConfig.getName());
                if (oldAppFormValidationPolicy == null) {
                    AppFormValidationPolicy appFormValidationPolicy = new AppFormValidationPolicy();
                    appFormValidationPolicy.setName(formValidationPolicyConfig.getName());
                    appFormValidationPolicy
                            .setDescription(resolveApplicationMessage(formValidationPolicyConfig.getDescription()));
                    appFormValidationPolicy.setErrorMatcher(formValidationPolicyConfig.getErrorMatcher());
                    appFormValidationPolicy.setErrorCondition(
                            InputWidgetUtils.newAppFilter(formValidationPolicyConfig.getErrorCondition()));
                    appFormValidationPolicy
                            .setMessage(resolveApplicationMessage(formValidationPolicyConfig.getMessage()));
                    appFormValidationPolicy.setTarget(formValidationPolicyConfig.getTarget());
                    appFormValidationPolicy.setConfigType(ConfigType.MUTABLE_INSTALL);
                    formValidationList.add(appFormValidationPolicy);
                } else {
                    if (ConfigUtils.isSetInstall(oldAppFormValidationPolicy)) {
                        oldAppFormValidationPolicy
                                .setDescription(resolveApplicationMessage(formValidationPolicyConfig.getDescription()));
                        oldAppFormValidationPolicy.setErrorMatcher(formValidationPolicyConfig.getErrorMatcher());
                        oldAppFormValidationPolicy.setErrorCondition(
                                InputWidgetUtils.newAppFilter(formValidationPolicyConfig.getErrorCondition()));
                        oldAppFormValidationPolicy
                                .setMessage(resolveApplicationMessage(formValidationPolicyConfig.getMessage()));
                        oldAppFormValidationPolicy.setTarget(formValidationPolicyConfig.getTarget());
                    } else {
                        environment().findChildren(oldAppFormValidationPolicy);
                    }

                    formValidationList.add(oldAppFormValidationPolicy);
                }
            }
        }

        appForm.setFormValidationList(formValidationList);

        // Form review policies
        List<AppFormReviewPolicy> formReviewList = null;
        if (!DataUtils.isBlank(appFormConfig.getFormReviewPolicyList())) {
            formReviewList = new ArrayList<AppFormReviewPolicy>();
            Map<String, AppFormReviewPolicy> map = appForm.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppFormReviewPolicyQuery().appFormId(appForm.getId()));
            for (FormReviewPolicyConfig formReviewPolicyConfig : appFormConfig.getFormReviewPolicyList()) {
                AppFormReviewPolicy oldAppFormReviewPolicy = map.get(formReviewPolicyConfig.getName());
                if (oldAppFormReviewPolicy == null) {
                    AppFormReviewPolicy appFormReviewPolicy = new AppFormReviewPolicy();
                    appFormReviewPolicy.setName(formReviewPolicyConfig.getName());
                    appFormReviewPolicy
                            .setDescription(resolveApplicationMessage(formReviewPolicyConfig.getDescription()));
                    appFormReviewPolicy.setErrorMatcher(formReviewPolicyConfig.getErrorMatcher());
                    appFormReviewPolicy.setErrorCondition(
                            InputWidgetUtils.newAppFilter(formReviewPolicyConfig.getErrorCondition()));
                    appFormReviewPolicy.setMessage(resolveApplicationMessage(formReviewPolicyConfig.getMessage()));
                    appFormReviewPolicy.setMessageType(formReviewPolicyConfig.getMessageType());
                    appFormReviewPolicy.setFormEvents(formReviewPolicyConfig.getEvents());
                    appFormReviewPolicy.setTarget(formReviewPolicyConfig.getTarget());
                    appFormReviewPolicy.setSkippable(formReviewPolicyConfig.isSkippable());
                    appFormReviewPolicy.setConfigType(ConfigType.MUTABLE_INSTALL);
                    formReviewList.add(appFormReviewPolicy);
                } else {
                    if (ConfigUtils.isSetInstall(oldAppFormReviewPolicy)) {
                        oldAppFormReviewPolicy
                                .setDescription(resolveApplicationMessage(formReviewPolicyConfig.getDescription()));
                        oldAppFormReviewPolicy.setErrorMatcher(formReviewPolicyConfig.getErrorMatcher());
                        oldAppFormReviewPolicy.setErrorCondition(
                                InputWidgetUtils.newAppFilter(formReviewPolicyConfig.getErrorCondition()));
                        oldAppFormReviewPolicy
                                .setMessage(resolveApplicationMessage(formReviewPolicyConfig.getMessage()));
                        oldAppFormReviewPolicy.setMessageType(formReviewPolicyConfig.getMessageType());
                        oldAppFormReviewPolicy.setFormEvents(formReviewPolicyConfig.getEvents());
                        oldAppFormReviewPolicy.setTarget(formReviewPolicyConfig.getTarget());
                        oldAppFormReviewPolicy.setSkippable(formReviewPolicyConfig.isSkippable());
                    } else {
                        environment().findChildren(oldAppFormReviewPolicy);
                    }

                    formReviewList.add(oldAppFormReviewPolicy);
                }
            }
        }

        appForm.setFormReviewList(formReviewList);
    }

    private void populateChildList(AppFormStatePolicy appFormStatePolicy, FormStatePolicyConfig formStatePolicyConfig)
            throws UnifyException {
        List<AppFormSetState> setStateList = null;
        if (formStatePolicyConfig.getSetStates() != null
                && !DataUtils.isBlank(formStatePolicyConfig.getSetStates().getSetStateList())) {
            setStateList = new ArrayList<AppFormSetState>();
            for (SetStateConfig setStateConfig : formStatePolicyConfig.getSetStates().getSetStateList()) {
                AppFormSetState appFormSetState = new AppFormSetState();
                appFormSetState.setType(setStateConfig.getType());
                appFormSetState.setTarget(setStateConfig.getTarget());
                appFormSetState.setRequired(setStateConfig.getRequired());
                appFormSetState.setVisible(setStateConfig.getVisible());
                appFormSetState.setEditable(setStateConfig.getEditable());
                appFormSetState.setDisabled(setStateConfig.getDisabled());
                appFormSetState.setConfigType(ConfigType.MUTABLE_INSTALL);
                setStateList.add(appFormSetState);
            }
        }

        appFormStatePolicy.setSetStateList(setStateList);
    }

    private void populateChildList(AppPropertyRule appPropertyRule, String applicationName,
            PropertyRuleConfig propertyRuleConfig) throws UnifyException {
        List<AppPropertyRuleChoice> choiceList = Collections.emptyList();
        if (!DataUtils.isBlank(propertyRuleConfig.getChoiceList())) {
            choiceList = new ArrayList<AppPropertyRuleChoice>();
            Map<String, AppPropertyRuleChoice> map = appPropertyRule.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppPropertyRuleChoiceQuery().appPropertyRuleId(appPropertyRule.getId()));
            for (ChoiceConfig choiceConfig : propertyRuleConfig.getChoiceList()) {
                AppPropertyRuleChoice oldAppPropertyRuleChoice = map.get(choiceConfig.getName());
                if (oldAppPropertyRuleChoice == null) {
                    AppPropertyRuleChoice appPropertyRuleChoice = new AppPropertyRuleChoice();
                    appPropertyRuleChoice.setName(choiceConfig.getName());
                    appPropertyRuleChoice.setList(
                            ApplicationNameUtils.ensureLongNameReference(applicationName, choiceConfig.getVal()));
                    appPropertyRuleChoice.setConfigType(ConfigType.STATIC_INSTALL);
                    choiceList.add(appPropertyRuleChoice);
                } else {
                    if (ConfigUtils.isSetInstall(oldAppPropertyRuleChoice)) {
                        oldAppPropertyRuleChoice.setList(
                                ApplicationNameUtils.ensureLongNameReference(applicationName, choiceConfig.getVal()));
                    }

                    choiceList.add(oldAppPropertyRuleChoice);
                }
            }
        }

        appPropertyRule.setChoiceList(choiceList);
    }

    private void populateChildList(AppPropertyList appPropertyList, String applicationName,
            PropertyListConfig propertyListConfig) throws UnifyException {
        List<AppPropertySet> itemSet = Collections.emptyList();
        if (!DataUtils.isBlank(propertyListConfig.getPropSetList())) {
            itemSet = new ArrayList<AppPropertySet>();
            for (PropertySetConfig propertySetConfig : propertyListConfig.getPropSetList()) {
                AppPropertySet set = new AppPropertySet();
                set.setLabel(resolveApplicationMessage(propertySetConfig.getLabel()));
                List<AppPropertyListItem> itemList = Collections.emptyList();
                if (!DataUtils.isBlank(propertySetConfig.getPropList())) {
                    itemList = new ArrayList<AppPropertyListItem>();
                    for (PropertyListPropConfig propConfig : propertySetConfig.getPropList()) {
                        AppPropertyListItem item = new AppPropertyListItem();
                        item.setName(propConfig.getName());
                        item.setDescription(resolveApplicationMessage(propConfig.getDescription()));
                        String inputWidget = ApplicationNameUtils.ensureLongNameReference(applicationName,
                                propConfig.getInputWidget());
                        item.setInputWidget(inputWidget);

                        String references = propConfig.getReferences();
                        if (references != null && !InputWidgetUtils.isEnumerationWidget(inputWidget)) {
                            references = ApplicationNameUtils.ensureLongNameReference(applicationName, references);
                        }

                        item.setReferences(references);
                        item.setDefaultVal(propConfig.getDefaultVal());
                        item.setRequired(propConfig.isRequired());
                        item.setMask(propConfig.isMask());
                        item.setEncrypt(propConfig.isEncrypt());
                        itemList.add(item);
                    }
                }

                set.setItemList(itemList);
                itemSet.add(set);
            }
        }

        appPropertyList.setItemSet(itemSet);

    }

    private void registerDelegate(EntityDef entityDef, Class<? extends Entity> entityClass) throws UnifyException {
        if (entityDef.delegated()) {
            environment().registerDelegate(entityClass, entityDef.getLongName(), entityDef.getDelegate());
        } else {
            environment().unregisterDelegate(entityDef.getLongName());
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> localGetPropertyListValues(Entity entityInst, String childFkFieldName,
            PropertyRuleDef propertyRuleDef) throws UnifyException {
        EntityFieldDef childListFieldDef = propertyRuleDef.getEntityDef().getFieldDef(propertyRuleDef.getListField());
        EntityClassDef childEntityClassDef = getEntityClassDef(childListFieldDef.getRefDef().getEntity());
        return environment().valueMap(String.class, propertyRuleDef.getPropNameField(), String.class,
                propertyRuleDef.getPropValField(),
                Query.of((Class<? extends Entity>) childEntityClassDef.getEntityClass()).addEquals(childFkFieldName,
                        entityInst.getId()));
    }

    private <T extends BaseApplicationEntity> T getApplicationEntity(Class<T> entityClazz, String longName)
            throws UnifyException {
        ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
        T inst = environment().list(Query.of(entityClazz).addEquals("name", nameParts.getEntityName())
                .addEquals("applicationName", nameParts.getApplicationName()));
        if (inst == null) {
            throw new UnifyException(ApplicationModuleErrorConstants.CANNOT_FIND_APPLICATION_ENTITY,
                    nameParts.getEntityName(), entityClazz, nameParts.getApplicationName());
        }
        return inst;
    }

    private DynamicEntityInfo buildDynamicEntityInfo(final EntityDef entityDef,
            Map<String, DynamicEntityInfo> dynamicEntityInfoMap, String basePackage) throws UnifyException {
        logDebug("Constructing dynamic information for entity [{0}]...", entityDef.getLongName());
        DynamicEntityInfo _dynamicEntityInfo = dynamicEntityInfoMap.get(entityDef.getLongName());
        if (_dynamicEntityInfo == null) {
            DynamicEntityType dynamicEntityType = DynamicEntityType.INFO_ONLY;
            String className = entityDef.getOriginClassName();
            String baseClassName = ApplicationEntityUtils.getBaseEntityClass(entityDef.getBaseType()).getName();
            if (entityDef.isCustom() || entityDef.isWithCustomFields()) {
                dynamicEntityType = DynamicEntityType.TABLE;
                if (entityDef.isStatic()) {
                    dynamicEntityType = DynamicEntityType.TABLE_EXT;
                    baseClassName = className;
                    className = ApplicationCodeGenUtils.generateCustomEntityClassName(entityDef.getType(),
                            entityDef.getApplicationName(), entityDef.getName());
                }
            }

            if (!DynamicEntityType.INFO_ONLY.equals(dynamicEntityType) && !StringUtils.isBlank(basePackage)) {
                String moduleName = environment().value(String.class, "moduleName",
                        new ApplicationQuery().name(entityDef.getApplicationName()));
                className = ApplicationCodeGenUtils.generateExtensionEntityClassName(basePackage, moduleName,
                        entityDef.getName());
            }

            // Construct dynamic entity information and load dynamic class
            DynamicEntityInfo.ManagedType managedType = entityDef.delegated()
                    ? DynamicEntityInfo.ManagedType.NOT_MANAGED
                    : DynamicEntityInfo.ManagedType.MANAGED;
            DynamicEntityInfo.Builder deib = DynamicEntityInfo.newBuilder(dynamicEntityType, className, managedType)
                    .baseClassName(baseClassName).tableName(entityDef.getTableName()).version(1L);
            _dynamicEntityInfo = deib.prefetch();
            dynamicEntityInfoMap.put(entityDef.getLongName(), _dynamicEntityInfo);

            List<EntityFieldDef> listOnlyFieldList = null;
            for (EntityFieldDef entityFieldDef : entityDef.getFieldDefList()) {
                DynamicFieldType type = entityFieldDef.isCustom() ? DynamicFieldType.GENERATION
                        : DynamicFieldType.INFO_ONLY;
                if (entityFieldDef.isRefDataType()) {
                    if (entityFieldDef.isEnumDataType()) {
                        deib.addForeignKeyField(type,
                                listManager.getStaticListEnumType(entityFieldDef.getReferences()).getName(),
                                entityFieldDef.getColumnName(), entityFieldDef.getFieldName(),
                                entityFieldDef.getDefaultVal(), entityFieldDef.isNullable());
                    } else if (entityFieldDef.isChildRef()) {
                        DynamicFieldType fieldType = entityFieldDef.isCustom() ? DynamicFieldType.GENERATION
                                : DynamicFieldType.INFO_ONLY;
                        EntityDef _refEntityDef = getEntityDef(entityFieldDef.getRefDef().getEntity());
                        DynamicEntityInfo _childDynamicEntityInfo = buildDynamicEntityInfo(_refEntityDef,
                                dynamicEntityInfoMap, basePackage);
                        if (entityFieldDef.isChild() || entityFieldDef.isRefFileUpload()) {
                            deib.addChildField(fieldType, _childDynamicEntityInfo, entityFieldDef.getFieldName());
                        } else {// Child list
                            deib.addChildListField(fieldType, _childDynamicEntityInfo, entityFieldDef.getFieldName());
                        }
                    } else {
                        EntityDef _refEntityDef = getEntityDef(entityFieldDef.getRefDef().getEntity());
                        logDebug("Constructing dynamic information for entity [{0}] referenced by [{1}]...",
                                _refEntityDef.getLongName(),
                                entityDef.getLongName() + "." + entityFieldDef.getFieldName());
                        DynamicEntityInfo _refDynamicEntityInfo = entityDef.getLongName()
                                .equals(_refEntityDef.getLongName()) ? DynamicEntityInfo.SELF_REFERENCE
                                        : buildDynamicEntityInfo(_refEntityDef, dynamicEntityInfoMap, basePackage);
                        deib.addForeignKeyField(type, _refDynamicEntityInfo, entityFieldDef.getColumnName(),
                                entityFieldDef.getFieldName(), entityFieldDef.getDefaultVal(),
                                entityFieldDef.isNullable());
                    }
                } else if (entityFieldDef.isListOnly()) {
                    if (listOnlyFieldList == null) {
                        listOnlyFieldList = new ArrayList<EntityFieldDef>();
                    }

                    listOnlyFieldList.add(entityFieldDef);
                } else {
                    if (entityFieldDef.isEnumDataType()) {
                        deib.addField(type, listManager.getStaticListEnumType(entityFieldDef.getReferences()).getName(),
                                entityFieldDef.getColumnName(), entityFieldDef.getFieldName(),
                                entityFieldDef.getDefaultVal(), entityFieldDef.isNullable(),
                                entityFieldDef.isDescriptive());
                    } else {
                        if (!entityFieldDef.isChildRef()) {
                            deib.addField(type, entityFieldDef.getDataType().dataType(), entityFieldDef.getColumnName(),
                                    entityFieldDef.getFieldName(), entityFieldDef.getDefaultVal(),
                                    entityFieldDef.getMaxLen(), entityFieldDef.getPrecision(),
                                    entityFieldDef.getScale(), entityFieldDef.isNullable(),
                                    entityFieldDef.isDescriptive());
                        }
                    }
                }
            }

            if (listOnlyFieldList != null) {
                for (EntityFieldDef entityFieldDef : listOnlyFieldList) {
                    DynamicFieldType type = entityFieldDef.isCustom() ? DynamicFieldType.GENERATION
                            : DynamicFieldType.INFO_ONLY;
                    deib.addListOnlyField(type, entityFieldDef.getColumnName(), entityFieldDef.getFieldName(),
                            entityFieldDef.getKey(), entityFieldDef.getProperty(), entityFieldDef.isDescriptive());
                }
            }

            deib.build();
        }

        return _dynamicEntityInfo;
    }

    private class FieldRenderInfo {

        private final String reference;

        private final WidgetColor color;

        public FieldRenderInfo(String reference, WidgetColor color) {
            this.reference = reference;
            this.color = color;
        }

        public String getReference() {
            return reference;
        }

        public WidgetColor getColor() {
            return color;
        }
    }
}

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
package com.flowcentraltech.flowcentral.application.business;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.AppletRequestAttributeConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationDeletionTaskConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationFeatureConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationImportDataTaskConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleErrorConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationPredefinedEntityConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationPredefinedTableConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationReplicationTaskConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.AppletFilterDef;
import com.flowcentraltech.flowcentral.application.data.AppletWorkflowCopyInfo;
import com.flowcentraltech.flowcentral.application.data.AppletWorkflowCopyInfo.EventType;
import com.flowcentraltech.flowcentral.application.data.AppletWorkflowCopyInfo.WorkflowCopyType;
import com.flowcentraltech.flowcentral.application.data.ApplicationDef;
import com.flowcentraltech.flowcentral.application.data.ApplicationMenuDef;
import com.flowcentraltech.flowcentral.application.data.AssignmentPageDef;
import com.flowcentraltech.flowcentral.application.data.DelegateEntityInfo;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.EntitySearchInputDef;
import com.flowcentraltech.flowcentral.application.data.EntityUploadDef;
import com.flowcentraltech.flowcentral.application.data.FieldSequenceDef;
import com.flowcentraltech.flowcentral.application.data.FieldSequenceEntryDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef;
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef.FilterType;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormFilterDef;
import com.flowcentraltech.flowcentral.application.data.HelpSheetDef;
import com.flowcentraltech.flowcentral.application.data.IndexDef;
import com.flowcentraltech.flowcentral.application.data.PropertyListDef;
import com.flowcentraltech.flowcentral.application.data.PropertyListItem;
import com.flowcentraltech.flowcentral.application.data.PropertyListItemDef;
import com.flowcentraltech.flowcentral.application.data.PropertyRuleDef;
import com.flowcentraltech.flowcentral.application.data.PropertySequenceDef;
import com.flowcentraltech.flowcentral.application.data.RecLoadInfo;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.data.SearchInputsDef;
import com.flowcentraltech.flowcentral.application.data.SetStatesDef;
import com.flowcentraltech.flowcentral.application.data.SetValuesDef;
import com.flowcentraltech.flowcentral.application.data.SnapshotDetails;
import com.flowcentraltech.flowcentral.application.data.StandardAppletDef;
import com.flowcentraltech.flowcentral.application.data.SuggestionTypeDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.data.TableFilterDef;
import com.flowcentraltech.flowcentral.application.data.TableLoadingDef;
import com.flowcentraltech.flowcentral.application.data.UniqueConditionDef;
import com.flowcentraltech.flowcentral.application.data.UniqueConstraintDef;
import com.flowcentraltech.flowcentral.application.data.Usage;
import com.flowcentraltech.flowcentral.application.data.WidgetRuleEntryDef;
import com.flowcentraltech.flowcentral.application.data.WidgetRulesDef;
import com.flowcentraltech.flowcentral.application.data.WidgetTypeDef;
import com.flowcentraltech.flowcentral.application.entities.*;
import com.flowcentraltech.flowcentral.application.util.ApplicationCodeGenUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationPageUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationReplicationContext;
import com.flowcentraltech.flowcentral.application.util.ApplicationReplicationUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.application.web.widgets.EntitySearchWidget;
import com.flowcentraltech.flowcentral.common.annotation.LongName;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.EnvironmentDelegate;
import com.flowcentraltech.flowcentral.common.business.EnvironmentDelegateHolder;
import com.flowcentraltech.flowcentral.common.business.EnvironmentDelegateRegistrar;
import com.flowcentraltech.flowcentral.common.business.EnvironmentDelegateUtilities;
import com.flowcentraltech.flowcentral.common.business.FileAttachmentProvider;
import com.flowcentraltech.flowcentral.common.business.PostBootSetup;
import com.flowcentraltech.flowcentral.common.business.PreInstallationSetup;
import com.flowcentraltech.flowcentral.common.business.SuggestionProvider;
import com.flowcentraltech.flowcentral.common.business.SystemDefinitionsCache;
import com.flowcentraltech.flowcentral.common.business.SystemRestoreService;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.constants.FileAttachmentCategoryType;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.flowcentraltech.flowcentral.common.constants.OwnershipType;
import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.common.constants.WfItemVersionType;
import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.AttachmentDetails;
import com.flowcentraltech.flowcentral.common.data.ParamValuesDef;
import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseVersionEntity;
import com.flowcentraltech.flowcentral.common.entities.EntityWrapper;
import com.flowcentraltech.flowcentral.common.entities.FileAttachment;
import com.flowcentraltech.flowcentral.common.entities.FileAttachmentDoc;
import com.flowcentraltech.flowcentral.common.entities.FileAttachmentQuery;
import com.flowcentraltech.flowcentral.common.entities.ParamValues;
import com.flowcentraltech.flowcentral.common.entities.ParamValuesQuery;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.flowcentraltech.flowcentral.common.util.CommonInputUtils;
import com.flowcentraltech.flowcentral.common.util.EntityUtils;
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
import com.flowcentraltech.flowcentral.configuration.data.ApplicationRestore;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.configuration.data.ModuleRestore;
import com.flowcentraltech.flowcentral.configuration.data.SystemRestore;
import com.flowcentraltech.flowcentral.configuration.xml.AppAssignmentPageConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppEntityConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppFormConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppTableConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletAlertConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletFilterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletPropConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletRouteToAppletConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppletSetValuesConfig;
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
import com.flowcentraltech.flowcentral.configuration.xml.EnumerationConfig;
import com.flowcentraltech.flowcentral.configuration.xml.EnumerationItemConfig;
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
import com.flowcentraltech.flowcentral.system.constants.SystemModuleSysParamConstants;
import com.flowcentraltech.flowcentral.system.entities.MappedTenant;
import com.flowcentraltech.flowcentral.system.entities.MappedTenantQuery;
import com.flowcentraltech.flowcentral.system.entities.Module;
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.core.UnifyComponentConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Broadcast;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.DynamicEntityType;
import com.tcdng.unify.core.annotation.DynamicFieldType;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Synchronized;
import com.tcdng.unify.core.annotation.Taskable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.data.MapValues;
import com.tcdng.unify.core.data.ParamConfig;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.data.ValueStoreWriter;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.NativeUpdate;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.database.dynamic.DynamicEntityInfo;
import com.tcdng.unify.core.database.dynamic.sql.DynamicSqlEntityLoader;
import com.tcdng.unify.core.database.sql.SqlDataSourceDialect;
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
        implements ApplicationModuleService, SystemRestoreService, FileAttachmentProvider, SuggestionProvider,
        PreInstallationSetup, PostBootSetup, EnvironmentDelegateRegistrar {

    private static final String PRE_INSTALLATION_SETUP_LOCK = "app::preinstallationsetup";

    private static final String POST_BOOT_SETUP_LOCK = "app::postbootsetup";

    private static final String DYNAMIC_ENTITY_BUILD_LOCK = "app:dynamicentitybuild";

    private final Set<String> refProperties = Collections
            .unmodifiableSet(new HashSet<String>(Arrays.asList(AppletPropertyConstants.SEARCH_TABLE,
                    AppletPropertyConstants.CREATE_FORM, AppletPropertyConstants.LOADING_TABLE,
                    AppletPropertyConstants.CREATE_FORM_SUBMIT_WORKFLOW_CHANNEL, AppletPropertyConstants.MAINTAIN_FORM,
                    AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_WORKFLOW_CHANNEL,
                    AppletPropertyConstants.ASSIGNMENT_PAGE, AppletPropertyConstants.PROPERTY_LIST_RULE,
                    AppletPropertyConstants.IMPORTDATA_ROUTETO_APPLETNAME, AppletPropertyConstants.QUICK_EDIT_TABLE,
                    AppletPropertyConstants.QUICK_EDIT_FORM)));

    private final Set<String> RESERVED_TABLES = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(
            ApplicationPredefinedTableConstants.PROPERTYITEM_TABLE, ApplicationPredefinedTableConstants.USAGE_TABLE,
            ApplicationPredefinedTableConstants.ATTACHMENT_TABLE, ApplicationPredefinedTableConstants.SNAPSHOT_TABLE)));

    private final Set<String> RESERVED_ENTITIES = Collections
            .unmodifiableSet(new HashSet<String>(Arrays.asList(ApplicationPredefinedEntityConstants.PROPERTYITEM_ENTITY,
                    ApplicationPredefinedEntityConstants.USAGE_ENTITY,
                    ApplicationPredefinedEntityConstants.ATTACHMENT_ENTITY,
                    ApplicationPredefinedEntityConstants.SNAPSHOT_ENTITY)));

    private static final int MAX_LIST_DEPTH = 8;

    private static final long CLEAR_SYSTEM_CACHE_WAIT_MILLISEC = 2500;

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

    @Configurable("application-usagelistprovider")
    private UsageListProvider usageListProvider;

    @Configurable
    private EnvironmentDelegateUtilities environmentDelegateUtilities;

    private Map<String, EnvironmentDelegateHolder> delegateHolderByEntityClass;

    private Map<String, EnvironmentDelegateHolder> delegateHolderByLongName;

    private List<ApplicationArtifactInstaller> applicationArtifactInstallerList;

    private List<ApplicationAppletDefProvider> applicationAppletDefProviderList;

    private FactoryMap<String, ApplicationDef> applicationDefFactoryMap;

    private FactoryMap<String, AppletDef> appletDefFactoryMap;

    private FactoryMap<String, WidgetTypeDef> widgetDefFactoryMap;

    private FactoryMap<String, SuggestionTypeDef> suggestionDefFactoryMap;

    private EntityClassDefFactoryMap entityClassDefFactoryMap;

    private FactoryMap<String, EntityDef> entityDefFactoryMap;

    private FactoryMap<String, EntityDef> entityDefByClassFactoryMap;

    private FactoryMap<String, RefDef> refDefFactoryMap;

    private FactoryMap<String, TableDef> tableDefFactoryMap;

    private FactoryMap<String, FormDef> formDefFactoryMap;

    private FactoryMap<String, HelpSheetDef> helpSheetDefFactoryMap;

    private FactoryMap<String, AssignmentPageDef> assignmentPageDefFactoryMap;

    private FactoryMap<String, PropertyListDef> propertyListDefMap;

    private FactoryMap<String, PropertyRuleDef> propertyRuleDefMap;

    private Set<String> entitySearchTypes;

    public ApplicationModuleServiceImpl() {
        this.entitySearchTypes = new HashSet<String>();
        this.delegateHolderByEntityClass = new ConcurrentHashMap<String, EnvironmentDelegateHolder>();
        this.delegateHolderByLongName = new ConcurrentHashMap<String, EnvironmentDelegateHolder>();
        this.applicationDefFactoryMap = new FactoryMap<String, ApplicationDef>(true)
            {
                @Override
                protected boolean pause() throws Exception {
                    return isInSystemRestoreMode();
                }

                @Override
                protected boolean stale(String name, ApplicationDef applicationDef) throws Exception {
                    return isStale(new ApplicationQuery(), applicationDef);
                }

                @Override
                protected ApplicationDef create(String name, Object... params) throws Exception {
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
                protected boolean pause() throws Exception {
                    return isInSystemRestoreMode();
                }

                @Override
                protected boolean stale(String longName, AppletDef appletDef) throws Exception {
                    return isStale(new AppAppletQuery(), appletDef);
                }

                @Override
                protected AppletDef create(String longName, Object... params) throws Exception {
                    String _actLongName = ApplicationNameUtils.getAppletNameParts(longName).getAppletName();
                    AppApplet appApplet = getApplicationEntity(AppApplet.class, _actLongName);
                    final boolean descriptiveButtons = appletUtilities.system().getSysParameterValue(boolean.class,
                            SystemModuleSysParamConstants.SYSTEM_DESCRIPTIVE_BUTTONS_ENABLED);
                    final AppletType type = appApplet.getType();
                    StandardAppletDef.Builder adb = StandardAppletDef.newBuilder(type, appApplet.getEntity(),
                            appApplet.getLabel(), appApplet.getIcon(), appApplet.getAssignDescField(),
                            appApplet.getPseudoDeleteField(), appApplet.getDisplayIndex(), appApplet.isMenuAccess(),
                            appApplet.isSupportOpenInNewWindow(), appApplet.isAllowSecondaryTenants(),
                            descriptiveButtons, _actLongName, appApplet.getDescription(), appApplet.getId(),
                            appApplet.getVersionNo());

                    List<StringToken> titleFormat = !StringUtils.isBlank(appApplet.getTitleFormat())
                            ? StringUtils.breakdownParameterizedString(appApplet.getTitleFormat())
                            : null;
                    adb.titleFormat(titleFormat);

                    for (AppAppletProp appAppletProp : appApplet.getPropList()) {
                        adb.addPropDef(appAppletProp.getName(), appAppletProp.getValue());
                    }

                    if (type.isMultiFacade()) {
                        List<String> subAppletList = new ArrayList<String>();
                        for (AppAppletRouteToApplet appAppletRouteToApplet : appApplet.getRouteToAppletList()) {
                            subAppletList.add(appAppletRouteToApplet.getRouteToApplet());
                        }

                        adb.subAppletList(subAppletList);
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
                        SetValuesDef setValuesDef = InputWidgetUtils.getSetValuesDef(appAppletSetValues.getName(),
                                appAppletSetValues.getDescription(), appAppletSetValues.getValueGenerator(),
                                appAppletSetValues.getSetValues());
                        adb.addSetValuesDef(appAppletSetValues.getName(), appAppletSetValues.getDescription(),
                                setValuesDef);
                    }

                    adb.routeToApplet(appApplet.getRouteToApplet());
                    if (!StringUtils.isBlank(appApplet.getPath())) {
                        adb.openPath(appApplet.getPath());
                    } else {
                        adb.openPath(ApplicationPageUtils.constructAppletOpenPagePath(appApplet.getType(), longName)
                                .getOpenPath());
                        if (type.isEntityList()) {
                            adb.maintainOpenPath(ApplicationPageUtils
                                    .constructAppletOpenPagePath(AppletType.CREATE_ENTITY, longName).getOpenPath());
                            adb.listingOpenPath(ApplicationPageUtils
                                    .constructAppletOpenPagePath(AppletType.LISTING, longName).getOpenPath());

                            if (adb.getPropValue(boolean.class, AppletPropertyConstants.WORKFLOWCOPY)) {
                                adb.openDraftPath(ApplicationPageUtils
                                        .constructAppletOpenPagePath(type, longName,
                                                ApplicationNameUtils.WORKFLOW_COPY_UPDATE_DRAFT_PATH_SUFFIX)
                                        .getOpenPath());
                                adb.openDraftWorkflow(ApplicationNameUtils.getWorkflowCopyUpdateWorkflowName(longName));
                                appletUtilities.ensureWorkflowCopyWorkflows(longName, false);
                            }
                        }
                    }

                    adb.openWindow(appApplet.getType().isOpenWindow());
                    return adb.build();
                }

            };

        this.widgetDefFactoryMap = new FactoryMap<String, WidgetTypeDef>(true)
            {
                @Override
                protected boolean pause() throws Exception {
                    return isInSystemRestoreMode();
                }

                @Override
                protected boolean stale(String longName, WidgetTypeDef widgetTypeDef) throws Exception {
                    return isStale(new AppWidgetTypeQuery(), widgetTypeDef);
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
                protected boolean pause() throws Exception {
                    return isInSystemRestoreMode();
                }

                @Override
                protected boolean stale(String longName, SuggestionTypeDef suggestionTypeDef) throws Exception {
                    return isStale(new AppSuggestionTypeQuery(), suggestionTypeDef);
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

        this.entityClassDefFactoryMap = new EntityClassDefFactoryMap()
            {

                @Override
                protected boolean pause() throws Exception {
                    return isInSystemRestoreMode();
                }

                @Override
                protected boolean stale(String longName, EntityClassDef entityClassDef) throws Exception {
                    if (!RESERVED_ENTITIES.contains(longName)) {
                        return isStale(new AppEntityQuery(), entityClassDef);
                    }

                    return false;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected EntityClassDef create(String longName, Object... arg1) throws Exception {
                    final EntityDef entityDef = getEntityDef(longName);
                    if (ApplicationPredefinedEntityConstants.PROPERTYITEM_ENTITY.equals(longName)) {
                        return new EntityClassDef(entityDef, PropertyListItem.class);
                    }

                    if (ApplicationPredefinedEntityConstants.USAGE_ENTITY.equals(longName)) {
                        return new EntityClassDef(entityDef, Usage.class);
                    }

                    if (ApplicationPredefinedEntityConstants.ATTACHMENT_ENTITY.equals(longName)) {
                        return new EntityClassDef(entityDef,
                                com.flowcentraltech.flowcentral.application.data.Attachment.class);
                    }

                    if (ApplicationPredefinedEntityConstants.SNAPSHOT_ENTITY.equals(longName)) {
                        return new EntityClassDef(entityDef, SnapshotDetails.class);
                    }

                    if (!entityDef.isCustom()) {
                        Class<? extends Entity> entityClass = (Class<? extends Entity>) ReflectUtils
                                .classForName(entityDef.getOriginClassName());
                        registerDelegate(entityDef, entityClass);
                        return new EntityClassDef(entityDef, entityClass);
                    }

                    return performDynamicEntityBuild(longName);
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
                protected boolean pause() throws Exception {
                    return isInSystemRestoreMode();
                }

                @Override
                protected boolean stale(String longName, EntityDef entityDef) throws Exception {
                    if (!RESERVED_ENTITIES.contains(longName)) {
                        return isStale(new AppEntityQuery(), entityDef);
                    }

                    return false;
                }

                @SuppressWarnings("unchecked")
                @Override
                protected EntityDef create(String longName, Object... arg1) throws Exception {
                    final WidgetTypeDef textWidgetTypeDef = widgetDefFactoryMap.get("application.text");
                    final WidgetTypeDef intWidgetTypeDef = widgetDefFactoryMap.get("application.integer");
                    final WidgetTypeDef manLabelWidgetTypeDef = widgetDefFactoryMap.get("application.mandatorylabel");
                    if (ApplicationPredefinedEntityConstants.PROPERTYITEM_ENTITY.equals(longName)) {
                        EntityDef.Builder edb = EntityDef.newBuilder(ConfigType.STATIC,
                                PropertyListItem.class.getName(),
                                getApplicationMessage("application.propertyitem.label"), null, null, null, false, false,
                                false, false, ApplicationPredefinedEntityConstants.PROPERTYITEM_ENTITY,
                                getApplicationMessage("application.propertyitem"), 0L, 1L);
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

                    if (ApplicationPredefinedEntityConstants.USAGE_ENTITY.equals(longName)) {
                        EntityDef.Builder edb = EntityDef.newBuilder(ConfigType.STATIC, Usage.class.getName(),
                                getApplicationMessage("application.usage.label"), null, null, null, false, false, false,
                                false, ApplicationPredefinedEntityConstants.USAGE_ENTITY,
                                getApplicationMessage("application.usage"), 0L, 1L);
                        edb.addFieldDef(textWidgetTypeDef, textWidgetTypeDef, EntityFieldDataType.STRING,
                                EntityFieldType.STATIC, "type", getApplicationMessage("application.usage.type"));
                        edb.addFieldDef(textWidgetTypeDef, textWidgetTypeDef, EntityFieldDataType.STRING,
                                EntityFieldType.STATIC, "usedByType",
                                getApplicationMessage("application.usage.usedbytype"));
                        edb.addFieldDef(textWidgetTypeDef, textWidgetTypeDef, EntityFieldDataType.STRING,
                                EntityFieldType.STATIC, "usedBy", getApplicationMessage("application.usage.usedby"));
                        edb.addFieldDef(textWidgetTypeDef, textWidgetTypeDef, EntityFieldDataType.STRING,
                                EntityFieldType.STATIC, "usedFor", getApplicationMessage("application.usage.usedfor"));
                        edb.addFieldDef(textWidgetTypeDef, textWidgetTypeDef, EntityFieldDataType.STRING,
                                EntityFieldType.STATIC, "usage", getApplicationMessage("application.usage.usage"));
                        return edb.build();
                    }

                    if (ApplicationPredefinedEntityConstants.ATTACHMENT_ENTITY.equals(longName)) {
                        EntityDef.Builder edb = EntityDef.newBuilder(ConfigType.STATIC,
                                com.flowcentraltech.flowcentral.application.data.Attachment.class.getName(),
                                getApplicationMessage("application.attachment.label"), null, null, null, false, false,
                                false, false, ApplicationPredefinedEntityConstants.ATTACHMENT_ENTITY,
                                getApplicationMessage("application.attachment"), 0L, 1L);
                        edb.addFieldDef(textWidgetTypeDef, textWidgetTypeDef, EntityFieldDataType.STRING,
                                EntityFieldType.STATIC, "name", getApplicationMessage("application.attachment.name"));
                        edb.addFieldDef(textWidgetTypeDef, textWidgetTypeDef, EntityFieldDataType.STRING,
                                EntityFieldType.STATIC, "description",
                                getApplicationMessage("application.attachment.description"));
                        edb.addFieldDef(textWidgetTypeDef, textWidgetTypeDef, EntityFieldDataType.STRING,
                                EntityFieldType.STATIC, "format",
                                getApplicationMessage("application.attachment.format"));
                        edb.addFieldDef(manLabelWidgetTypeDef, manLabelWidgetTypeDef, EntityFieldDataType.BOOLEAN,
                                EntityFieldType.STATIC, "mandatory",
                                getApplicationMessage("application.attachment.mandatory"));
                        edb.addFieldDef(textWidgetTypeDef, textWidgetTypeDef, EntityFieldDataType.TIMESTAMP,
                                EntityFieldType.STATIC, "createdOn",
                                getApplicationMessage("application.attachment.createdon"));
                        return edb.build();
                    }

                    if (ApplicationPredefinedEntityConstants.SNAPSHOT_ENTITY.equals(longName)) {
                        EntityDef.Builder edb = EntityDef.newBuilder(ConfigType.STATIC, SnapshotDetails.class.getName(),
                                getApplicationMessage("application.snapshotdetails.label"), null, null, null, false,
                                false, false, false, ApplicationPredefinedEntityConstants.SNAPSHOT_ENTITY,
                                getApplicationMessage("application.snapshotdetails"), 0L, 1L);
                        edb.addFieldDef(intWidgetTypeDef, intWidgetTypeDef, EntityFieldDataType.LONG,
                                EntityFieldType.STATIC, "snapshotId",
                                getApplicationMessage("application.snapshotdetails.snapshotid"));
                        edb.addFieldDef(textWidgetTypeDef, textWidgetTypeDef, EntityFieldDataType.STRING,
                                EntityFieldType.STATIC, "name",
                                getApplicationMessage("application.snapshotdetails.name"));
                        edb.addFieldDef(textWidgetTypeDef, textWidgetTypeDef, EntityFieldDataType.STRING,
                                EntityFieldType.STATIC, "filename",
                                getApplicationMessage("application.snapshotdetails.filename"));
                        edb.addFieldDef(textWidgetTypeDef, textWidgetTypeDef, EntityFieldDataType.STRING,
                                EntityFieldType.STATIC, "type",
                                getApplicationMessage("application.snapshotdetails.type"));
                        edb.addFieldDef(textWidgetTypeDef, textWidgetTypeDef, EntityFieldDataType.STRING,
                                EntityFieldType.STATIC, "message",
                                getApplicationMessage("application.snapshotdetails.message"));
                        edb.addFieldDef(textWidgetTypeDef, textWidgetTypeDef, EntityFieldDataType.TIMESTAMP,
                                EntityFieldType.STATIC, "snapshotDate",
                                getApplicationMessage("application.snapshotdetails.snapshotdate"));
                        edb.addFieldDef(textWidgetTypeDef, textWidgetTypeDef, EntityFieldDataType.STRING,
                                EntityFieldType.STATIC, "snapshotBy",
                                getApplicationMessage("application.snapshotdetails.snapshotby"));
                        return edb.build();
                    }

                    AppEntity appEntity = getApplicationEntity(AppEntity.class, longName);
                    EntityDef.Builder edb = EntityDef.newBuilder(appEntity.getBaseType(), appEntity.getConfigType(),
                            appEntity.getEntityClass(), appEntity.getTableName(), appEntity.getLabel(),
                            appEntity.getEmailProducerConsumer(), appEntity.getDelegate(),
                            appEntity.getDataSourceName(), appEntity.isMapped(), appEntity.isAuditable(),
                            appEntity.isReportable(), appEntity.isActionPolicy(), longName, appEntity.getDescription(),
                            appEntity.getId(), appEntity.getVersionNo());

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
                        if (type.isEntityRef()
                                || (!appEntityField.getDataType().isEnumGroup() && !StringUtils.isBlank(references))) {
                            edb.addFieldDef(textWidgetTypeDef, inputWidgetTypeDef, lingualWidgetTypeDef,
                                    getRefDef(references), appEntityField.getDataType(), appEntityField.getType(),
                                    appEntityField.getTextCase(), appEntityField.getName(), appEntityField.getMapped(),
                                    appEntityField.getLabel(), appEntityField.getColumnName(),
                                    appEntityField.getCategory(), appEntityField.getSuggestionType(),
                                    appEntityField.getInputLabel(), appEntityField.getInputListKey(),
                                    appEntityField.getLingualListKey(), appEntityField.getAutoFormat(),
                                    appEntityField.getDefaultVal(), references, appEntityField.getKey(),
                                    appEntityField.getProperty(), appEntityField.getRows(), appEntityField.getColumns(),
                                    appEntityField.getMinLen(), appEntityField.getMaxLen(),
                                    appEntityField.getPrecision(), appEntityField.getScale(), appEntityField.isTrim(),
                                    appEntityField.isAllowNegative(), !appEntityField.isReadOnly(),
                                    appEntityField.isNullable(), appEntityField.isAuditable(),
                                    appEntityField.isReportable(), appEntityField.isMaintainLink(),
                                    appEntityField.isBasicSearch(), appEntityField.isDescriptive());
                        } else {
                            edb.addFieldDef(textWidgetTypeDef, inputWidgetTypeDef, lingualWidgetTypeDef,
                                    appEntityField.getDataType(), appEntityField.getType(),
                                    appEntityField.getTextCase(), appEntityField.getName(), appEntityField.getMapped(),
                                    appEntityField.getLabel(), appEntityField.getColumnName(),
                                    appEntityField.getCategory(), appEntityField.getSuggestionType(),
                                    appEntityField.getInputLabel(), appEntityField.getInputListKey(),
                                    appEntityField.getLingualListKey(), appEntityField.getAutoFormat(),
                                    appEntityField.getDefaultVal(), references, appEntityField.getKey(),
                                    appEntityField.getProperty(), appEntityField.getRows(), appEntityField.getColumns(),
                                    appEntityField.getMinLen(), appEntityField.getMaxLen(),
                                    appEntityField.getPrecision(), appEntityField.getScale(), appEntityField.isTrim(),
                                    appEntityField.isAllowNegative(), !appEntityField.isReadOnly(),
                                    appEntityField.isNullable(), appEntityField.isAuditable(),
                                    appEntityField.isReportable(), appEntityField.isMaintainLink(),
                                    appEntityField.isBasicSearch(), appEntityField.isDescriptive());
                        }

                    }

                    for (AppEntityAttachment appEntityAttachment : appEntity.getAttachmentList()) {
                        edb.addAttachmentDef(appEntityAttachment.getType(), appEntityAttachment.getName(),
                                appEntityAttachment.getDescription());
                    }

                    for (AppEntitySeries appEntitySeries : appEntity.getSeriesList()) {
                        edb.addSeriesDef(appEntitySeries.getType(), appEntitySeries.getName(),
                                appEntitySeries.getDescription(), appEntitySeries.getLabel(),
                                appEntitySeries.getFieldName());
                    }

                    for (AppEntityCategory appEntityCategory : appEntity.getCategoryList()) {
                        FilterDef filterDef = InputWidgetUtils.getFilterDef(appletUtilities,
                                appEntityCategory.getName(), appEntityCategory.getDescription(), null,
                                appEntityCategory.getFilter());
                        edb.addCategoryDef(appEntityCategory.getName(), appEntityCategory.getDescription(),
                                appEntityCategory.getLabel(), filterDef);
                    }

                    for (AppEntityExpression appEntityExpression : appEntity.getExpressionList()) {
                        edb.addExpressionDef(appEntityExpression.getName(), appEntityExpression.getDescription(),
                                appEntityExpression.getExpression());
                    }

                    for (AppEntityUniqueConstraint appUniqueConstraint : appEntity.getUniqueConstraintList()) {
                        boolean caseInsensitive = true; // TODO Get from appUniqueConstraint
                        List<UniqueConditionDef> conditionList = null;
                        if (!DataUtils.isBlank(appUniqueConstraint.getConditionList())) {
                            conditionList = new ArrayList<UniqueConditionDef>();
                            for (AppEntityUniqueCondition appEntityUniqueCondition : appUniqueConstraint
                                    .getConditionList()) {
                                EntityFieldDef entityFieldDef = edb
                                        .getEntityFieldDef(appEntityUniqueCondition.getField());
                                Object val = DataUtils.convert(entityFieldDef.getDataType().dataType().javaClass(),
                                        appEntityUniqueCondition.getValue());
                                conditionList.add(new UniqueConditionDef(appEntityUniqueCondition.getField(),
                                        new Equals(appEntityUniqueCondition.getField(), val)));
                            }
                        }

                        edb.addUniqueConstraintDef(appUniqueConstraint.getName(), appUniqueConstraint.getDescription(),
                                DataUtils.convert(List.class, String.class, appUniqueConstraint.getFieldList(), null),
                                conditionList, caseInsensitive);
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

                    for (AppEntitySearchInput appEntitySearchInput : appEntity.getSearchInputList()) {
                        SearchInputsDef searchInputsDef = InputWidgetUtils.getSearchInputsDef(
                                appEntitySearchInput.getName(), appEntitySearchInput.getDescription(),
                                appEntitySearchInput.getRestrictionResolver(), appEntitySearchInput.getSearchInput());
                        if (searchInputsDef != null) {
                            edb.addEntitySearchInputDef(new EntitySearchInputDef(searchInputsDef));
                        }
                    }

                    return edb.build();
                }

            };

        this.entityDefByClassFactoryMap = new FactoryMap<String, EntityDef>(true)
            {
                @Override
                protected boolean pause() throws Exception {
                    return isInSystemRestoreMode();
                }

                @Override
                protected boolean stale(String entityClass, EntityDef entityDef) throws Exception {
                    if (!PropertyListItem.class.getName().equals(entityClass)) {
                        return isStale(new AppEntityQuery(), entityDef);
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
                protected boolean pause() throws Exception {
                    return isInSystemRestoreMode();
                }

                @Override
                protected boolean stale(String longName, RefDef refDef) throws Exception {
                    return isStale(new AppRefQuery(), refDef);
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
                protected boolean pause() throws Exception {
                    return isInSystemRestoreMode();
                }

                @Override
                protected boolean stale(String longName, TableDef tableDef) throws Exception {
                    if (!RESERVED_TABLES.contains(longName)) {
                        return isStale(new AppTableQuery(), tableDef) || (tableDef.getEntityDef()
                                .getVersion() != getEntityDef(tableDef.getEntityDef().getLongName()).getVersion());
                    }

                    return false;
                }

                @Override
                protected TableDef create(String longName, Object... arg1) throws Exception {
                    final boolean classicLink = appletUtilities.system().getSysParameterValue(boolean.class,
                            ApplicationModuleSysParamConstants.ENABLE_CLASSIC_LINK_DECORATION);
                    if (ApplicationPredefinedTableConstants.PROPERTYITEM_TABLE.equals(longName)) {
                        TableDef.Builder tdb = TableDef.newBuilder(
                                getEntityDef(ApplicationPredefinedEntityConstants.PROPERTYITEM_ENTITY),
                                getApplicationMessage("application.propertyitem.table.label"), false, false,
                                ApplicationPredefinedTableConstants.PROPERTYITEM_TABLE,
                                getApplicationMessage("application.propertyitem.table.description"), 0L, 1L);
                        tdb.classicLink(classicLink);
                        WidgetTypeDef widgetTypeDef = getWidgetTypeDef("application.text");
                        String renderer = widgetTypeDef.getRenderer();
                        tdb.addColumnDef("name", renderer, 2, false);
                        tdb.addColumnDef("description", renderer, 2, false);
                        tdb.addColumnDef(getApplicationMessage("application.propertyitem.value"), "displayValue",
                                renderer, 2, false);
                        tdb.itemsPerPage(-1);
                        return tdb.build();
                    }

                    if (ApplicationPredefinedTableConstants.USAGE_TABLE.equals(longName)) {
                        TableDef.Builder tdb = TableDef.newBuilder(
                                getEntityDef(ApplicationPredefinedEntityConstants.USAGE_ENTITY),
                                getApplicationMessage("application.usage.table.label"), true, true,
                                ApplicationPredefinedTableConstants.USAGE_TABLE,
                                getApplicationMessage("application.usage.table.description"), 0L, 1L);
                        tdb.classicLink(classicLink);
                        WidgetTypeDef widgetTypeDef = getWidgetTypeDef("application.text");
                        String renderer = widgetTypeDef.getRenderer();
                        tdb.addColumnDef("type", renderer, 2, false);
                        tdb.addColumnDef("usedByType", renderer, 2, true);
                        tdb.addColumnDef("usedBy", renderer, 3, true);
                        tdb.addColumnDef("usedFor", renderer, 3, true);
                        tdb.addColumnDef("usage", renderer, 3, true);
                        tdb.itemsPerPage(25); // TODO
                        return tdb.build();
                    }

                    if (ApplicationPredefinedTableConstants.ATTACHMENT_TABLE.equals(longName)) {
                        TableDef.Builder tdb = TableDef
                                .newBuilder(getEntityDef(ApplicationPredefinedEntityConstants.ATTACHMENT_ENTITY),
                                        getApplicationMessage("application.attachment.table.label"), false, false,
                                        ApplicationPredefinedTableConstants.ATTACHMENT_TABLE,
                                        getApplicationMessage("application.attachment.table.description"), 0L, 1L)
                                .serialNo(true);
                        tdb.classicLink(classicLink);
                        tdb.addColumnDef("name", "!ui-label", 2, false);
                        tdb.addColumnDef("description", "!ui-label", 3, false);
                        tdb.addColumnDef("format", "!ui-label", 2, false);
                        tdb.addColumnDef("mandatory",
                                "!ui-booleanlabel onTrue:$m{label.mandatory} onFalse:$m{label.optional}", 2, false);
                        tdb.addColumnDef("createdOn", "!ui-label", 2, false);
                        tdb.itemsPerPage(-1);
                        return tdb.build();
                    }

                    if (ApplicationPredefinedTableConstants.SNAPSHOT_TABLE.equals(longName)) {
                        TableDef.Builder tdb = TableDef
                                .newBuilder(getEntityDef(ApplicationPredefinedEntityConstants.SNAPSHOT_ENTITY),
                                        getApplicationMessage("application.snapshotdetails.table.label"), false, false,
                                        ApplicationPredefinedTableConstants.SNAPSHOT_TABLE,
                                        getApplicationMessage("application.snapshotdetails.table.description"), 0L, 1L)
                                .serialNo(true);
                        tdb.classicLink(classicLink);
                        tdb.addColumnDef("name", "!ui-label", 2, false);
                        tdb.addColumnDef("message", "!ui-label", 3, false);
                        tdb.addColumnDef("filename", "!ui-label", 2, false);
                        tdb.addColumnDef("type", "!ui-label", 2, false);
                        tdb.addColumnDef("snapshotDate", "!ui-label", 2, false);
                        tdb.addColumnDef("snapshotBy", "!ui-label", 2, false);
                        tdb.serialNo(true);
                        tdb.sortable(false);
                        tdb.headerToUpperCase(true);
                        tdb.headerCenterAlign(true);
                        tdb.itemsPerPage(-1);
                        return tdb.build();
                    }

                    AppTable appTable = getApplicationEntity(AppTable.class, longName);
                    final EntityDef entityDef = getEntityDef(appTable.getEntity());
                    TableDef.Builder tdb = TableDef.newBuilder(entityDef, appTable.getLabel(), appTable.isSerialNo(),
                            appTable.isSortable(), longName, appTable.getDescription(), appTable.getId(),
                            appTable.getVersionNo());
                    tdb.classicLink(classicLink);

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
                                appTableColumn.getLinkAct(), appTableColumn.getSymbol(), order,
                                appTableColumn.getWidthRatio(), appTableColumn.isSwitchOnChange(),
                                appTableColumn.isHiddenOnNull(), appTableColumn.isHidden(), appTableColumn.isDisabled(),
                                appTableColumn.isEditable(), appTableColumn.isSortable(), appTableColumn.isSummary());
                    }

                    for (AppTableAction appTableAction : appTable.getActionList()) {
                        tdb.addTableAction(appTableAction.getPolicy(), appTableAction.getLabel());
                    }

                    for (AppTableLoading appTableLoading : appTable.getLoadingList()) {
                        tdb.addTableLoadingDef(
                                new TableLoadingDef(appTableLoading.getName(), appTableLoading.getDescription(),
                                        appTableLoading.getLabel(), appTableLoading.getProvider(),
                                        appTableLoading.getOrderIndex(), Collections.emptyList()));
                    }

                    tdb.detailsPanelName(appTable.getDetailsPanelName());
                    tdb.loadingSearchInput(appTable.getLoadingSearchInput());
                    tdb.loadingFilterGen(appTable.getLoadingFilterGen());
                    tdb.sortHistory(appTable.getSortHistory());
                    tdb.itemsPerPage(appTable.getItemsPerPage());
                    tdb.summaryTitleColumns(appTable.getSummaryTitleColumns());
                    tdb.showLabelHeader(appTable.isShowLabelHeader());
                    tdb.headerToUpperCase(appTable.isHeaderToUpperCase());
                    tdb.headerCenterAlign(appTable.isHeaderCenterAlign());
                    tdb.basicSearch(appTable.isBasicSearch());
                    tdb.totalSummary(appTable.isTotalSummary());
                    tdb.headerless(appTable.isHeaderless());
                    tdb.multiSelect(appTable.isMultiSelect());
                    tdb.nonConforming(appTable.isNonConforming());
                    tdb.fixedRows(appTable.isFixedRows());
                    tdb.limitSelectToColumns(appTable.isLimitSelectToColumns());

                    appletUtilities.ensureWorkflowUserInteractionLoadingApplet(longName, false);
                    return tdb.build();
                }

            };

        this.formDefFactoryMap = new FactoryMap<String, FormDef>(true)
            {

                @Override
                protected boolean pause() throws Exception {
                    return isInSystemRestoreMode();
                }

                @Override
                protected boolean stale(String longName, FormDef formDef) throws Exception {
                    return isStale(new AppFormQuery(), formDef) || (formDef.getEntityDef()
                            .getVersion() != getEntityDef(formDef.getEntityDef().getLongName()).getVersion());
                }

                @SuppressWarnings("unchecked")
                @Override
                protected FormDef create(String longName, Object... arg1) throws Exception {
                    AppForm appForm = getApplicationEntity(AppForm.class, longName);
                    final boolean useFormColorScheme = appletUtilities.system().getSysParameterValue(boolean.class,
                            ApplicationModuleSysParamConstants.USE_APPLICATION_FORM_COLOR_SCHEME);
                    final EntityDef entityDef = getEntityDef(appForm.getEntity());
                    FormDef.Builder fdb = FormDef.newBuilder(appForm.getType(), entityDef, appForm.getLabel(),
                            appForm.getHelpSheet(), appForm.getConsolidatedValidation(),
                            appForm.getConsolidatedReview(), appForm.getConsolidatedState(),
                            appForm.getListingGenerator(), longName, appForm.getDescription(), appForm.getId(),
                            appForm.getVersionNo());
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
                                    appFormElement.getMappedFieldName(), appFormElement.getTabMappedForm(),
                                    appFormElement.getEditAction(), appFormElement.getEditFormless(),
                                    appFormElement.getEditAllowAddition(), appFormElement.getEditFixedRows(),
                                    appFormElement.isIgnoreParentCondition(), appFormElement.isShowSearch(),
                                    appFormElement.isQuickEdit(), appFormElement.isQuickOrder(),
                                    appFormElement.isVisible(), appFormElement.isEditable(),
                                    appFormElement.isDisabled());
                        } else if (FormElementType.SECTION.equals(appFormElement.getType())) {
                            sectionIndex++;
                            fdb.addFormSection(tabIndex, appFormElement.getElementName(), appFormElement.getLabel(),
                                    appFormElement.getSectionColumns(), appFormElement.getPanel(),
                                    appFormElement.isVisible(), appFormElement.isEditable(),
                                    appFormElement.isDisabled());
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
                                WidgetColor color = entityFieldDef.isWithAutoFormat() ? WidgetColor.NAVY_GRAY
                                        : appFormElement.getColor();
                                if (useFormColorScheme) {
                                    color = entitySearchTypes.contains(widgetTypeDef.getLongName()) ? WidgetColor.PURPLE
                                            : color;
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
                                        appFormElement.getPreviewForm(), label, renderer,
                                        appFormElement.getFieldColumn(), appFormElement.isSwitchOnChange(),
                                        appFormElement.isSaveAs(), appFormElement.isRequired(),
                                        appFormElement.isVisible(),
                                        appFormElement.isEditable() && !entityFieldDef.isWithAutoFormat(),
                                        appFormElement.isDisabled());
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
                        fdb.addFormAnnotation(appFormAnnotation.getType(), appFormAnnotation.getVisibility(),
                                appFormAnnotation.getName(), appFormAnnotation.getDescription(),
                                appFormAnnotation.getMessage(), appFormAnnotation.isHtml(),
                                appFormAnnotation.isDirectPlacement(), InputWidgetUtils.getFilterDef(appletUtilities,
                                        null, appFormAnnotation.getOnCondition()));
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

                    DataUtils.sortAscending(appForm.getFieldStateList(), AppFormStatePolicy.class, "executionIndex");
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

                    DataUtils.sortAscending(appForm.getWidgetRulesList(), AppFormWidgetRulesPolicy.class,
                            "executionIndex");
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

                    DataUtils.sortAscending(appForm.getFieldValidationList(), AppFormFieldValidationPolicy.class,
                            "executionIndex");
                    for (AppFormFieldValidationPolicy appFormFieldValidationPolicy : appForm.getFieldValidationList()) {
                        fdb.addFieldValidationPolicy(appFormFieldValidationPolicy.getName(),
                                appFormFieldValidationPolicy.getDescription(),
                                appFormFieldValidationPolicy.getFieldName(),
                                appFormFieldValidationPolicy.getValidation(), appFormFieldValidationPolicy.getRule());
                    }

                    DataUtils.sortAscending(appForm.getFormValidationList(), AppFormValidationPolicy.class,
                            "executionIndex");
                    for (AppFormValidationPolicy appFormValidationPolicy : appForm.getFormValidationList()) {
                        fdb.addFormValidationPolicy(
                                InputWidgetUtils.getFilterDef(appletUtilities, null,
                                        appFormValidationPolicy.getErrorCondition()),
                                appFormValidationPolicy.getName(), appFormValidationPolicy.getDescription(),
                                appFormValidationPolicy.getMessage(), appFormValidationPolicy.getErrorMatcher(),
                                DataUtils.convert(List.class, String.class, appFormValidationPolicy.getTarget(), null));
                    }

                    DataUtils.sortAscending(appForm.getFormReviewList(), AppFormReviewPolicy.class, "executionIndex");
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

        this.helpSheetDefFactoryMap = new FactoryMap<String, HelpSheetDef>(true)
            {
                @Override
                protected boolean stale(String longName, HelpSheetDef helpSheetDef) throws Exception {
                    return isStale(new AppHelpSheetQuery(), helpSheetDef);
                }

                @Override
                protected HelpSheetDef create(String longName, Object... params) throws Exception {
                    AppHelpSheet appHelpSheet = getApplicationEntity(AppHelpSheet.class, longName);
                    HelpSheetDef.Builder hsdb = HelpSheetDef.newBuilder(appHelpSheet.getLabel(),
                            appHelpSheet.getHelpOverview(), longName, appHelpSheet.getDescription(),
                            appHelpSheet.getId(), appHelpSheet.getVersionNo());

                    List<AppHelpEntry> entryList = environment()
                            .listAll(new AppHelpEntryQuery().appHelpSheetId(appHelpSheet.getId()));
                    for (AppHelpEntry entry : entryList) {
                        hsdb.addEntry(entry.getFieldName(), entry.getHelpContent());
                    }

                    return hsdb.build();
                }

            };

        this.assignmentPageDefFactoryMap = new FactoryMap<String, AssignmentPageDef>(true)
            {

                @Override
                protected boolean pause() throws Exception {
                    return isInSystemRestoreMode();
                }

                @Override
                protected boolean stale(String longName, AssignmentPageDef assignmentPageDef) throws Exception {
                    return isStale(new AppAssignmentPageQuery(), assignmentPageDef);
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
                protected boolean pause() throws Exception {
                    return isInSystemRestoreMode();
                }

                @Override
                protected boolean stale(String longName, PropertyListDef propertyListDef) throws Exception {
                    return isStale(new AppPropertyListQuery(), propertyListDef);
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
                                    longName, listItem.getName(), null, listItem.getReferences(), filterListKey);
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
                protected boolean pause() throws Exception {
                    return isInSystemRestoreMode();
                }

                @Override
                protected boolean stale(String longName, PropertyRuleDef propertyRuleDef) throws Exception {
                    return isStale(new AppPropertyRuleQuery(), propertyRuleDef);
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

    @Override
    public void restoreSystem(TaskMonitor taskMonitor, SystemRestore systemRestore) throws UnifyException {
        logDebug(taskMonitor, "Attempting to execute system restore...");
        if (enterSystemRestoreMode()) {
            try {
                logDebug(taskMonitor, "Executing system restore...");

                // Restore modules
                for (ModuleRestore moduleRestore : systemRestore.getModuleList()) {
                    restoreModule(taskMonitor, moduleRestore);
                }

                // All system cache
                clearAllSystemDefinitionsCache();

                // Wait a while for propagation
                logDebug(taskMonitor, "Waiting for clear system definitions cache propagation...");
                pause(CLEAR_SYSTEM_CACHE_WAIT_MILLISEC);

                logDebug(taskMonitor, "System restore successfully completed.");
            } finally {
                exitSystemRestoreMode();
            }
        } else {
            logDebug(taskMonitor, "Some other process is executing system restore.");
        }

        logDebug(taskMonitor, "Ensuring system generated components...");
        ensureAllWorkflowCopyComponents(true);
        logDebug(taskMonitor, "System generated components resolved.");
    }

    @Broadcast
    public void clearAllSystemDefinitionsCache() throws UnifyException {
        logDebug("Clearing all system definitions cache...");
        List<SystemDefinitionsCache> caches = getComponents(SystemDefinitionsCache.class);
        for (SystemDefinitionsCache cache : caches) {
            cache.clearDefinitionsCache();
        }

        logDebug("All system definitions cache clearing successfully completed.");
    }

    @Override
    public void clearDefinitionsCache() throws UnifyException {
        logDebug("Clearing definitions cache...");
        propertyRuleDefMap.clear();
        propertyListDefMap.clear();
        assignmentPageDefFactoryMap.clear();
        formDefFactoryMap.clear();
        tableDefFactoryMap.clear();
        refDefFactoryMap.clear();
        entityDefByClassFactoryMap.clear();
        entityClassDefFactoryMap.clear();
        suggestionDefFactoryMap.clear();
        widgetDefFactoryMap.clear();
        appletDefFactoryMap.clear();
        applicationDefFactoryMap.clear();
        logDebug("Definitions cache clearing successfully completed.");
    }

    @Override
    public EnvironmentDelegateHolder getEnvironmentDelegateInfo(String entityLongName) throws UnifyException {
        return delegateHolderByLongName.get(entityLongName);
    }

    @Override
    public EnvironmentDelegateHolder getEnvironmentDelegateInfo(Class<? extends Entity> entityClass)
            throws UnifyException {
        return delegateHolderByEntityClass.get(entityClass.getName());
    }

    @Override
    public String resolveLongName(Class<? extends Entity> entityClass) throws UnifyException {
        EnvironmentDelegateHolder delegateHolder = getEnvironmentDelegateInfo(entityClass);
        if (delegateHolder == null) {
            // TODO Throw exception
        }

        return delegateHolder.getEntityLongName();
    }

    private static final Class<?>[] WRAPPER_PARAMS_0 = { EntityClassDef.class };

    private static final Class<?>[] WRAPPER_PARAMS_1 = { EntityClassDef.class, Entity.class };

    private static final Class<?>[] WRAPPER_PARAMS_2 = { EntityClassDef.class, List.class };

    private static final Class<?>[] WRAPPER_PARAMS_3 = { EntityClassDef.class, ValueStore.class };

    @Override
    public boolean isEntityComponentPresent(Class<? extends EntityWrapper> wrapperType) throws UnifyException {
        final String entityName = ReflectUtils.getPublicStaticStringConstant(wrapperType,
                ApplicationCodeGenUtils.ENTITY_NAME);
        return isComponentPresent(AppEntity.class, entityName);
    }

    @Override
    public boolean isComponentPresent(Class<? extends BaseApplicationEntity> type, String longName)
            throws UnifyException {
        ApplicationEntityNameParts parts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
        return environment().countAll(Query.of(type).addEquals("applicationName", parts.getApplicationName())
                .addEquals("name", parts.getEntityName())) > 0;
    }

    @Override
    public <T extends EntityWrapper> T wrapperOf(Class<T> wrapperType) throws UnifyException {
        final String entityName = ReflectUtils.getPublicStaticStringConstant(wrapperType,
                ApplicationCodeGenUtils.ENTITY_NAME);
        final EntityClassDef entityClassDef = getEntityClassDef(entityName);
        return ReflectUtils.newInstance(wrapperType, WRAPPER_PARAMS_0, entityClassDef);
    }

    @Override
    public <T extends EntityWrapper> T wrapperOf(Class<T> wrapperType, Long Id) throws UnifyException {
        Entity inst = environment().listLean(queryOf(wrapperType).addEquals("id", Id));
        return wrapperOf(wrapperType, inst);
    }

    @Override
    public <T extends EntityWrapper> T wrapperOf(Class<T> wrapperType, Entity inst) throws UnifyException {
        if (inst != null) {
            final String entityName = ReflectUtils.getPublicStaticStringConstant(wrapperType,
                    ApplicationCodeGenUtils.ENTITY_NAME);
            final EntityClassDef entityClassDef = getEntityClassDef(entityName);
            return ReflectUtils.newInstance(wrapperType, WRAPPER_PARAMS_1, entityClassDef, inst);
        }

        return null;
    }

    @Override
    public <T extends EntityWrapper> T wrapperOf(Class<T> wrapperType, Query<? extends Entity> query)
            throws UnifyException {
        List<? extends Entity> instList = environment().listAll(query);
        return wrapperOf(wrapperType, instList);
    }

    @Override
    public <T extends EntityWrapper> T wrapperOf(Class<T> wrapperType, List<? extends Entity> instList)
            throws UnifyException {
        if (instList != null) {
            final String entityName = ReflectUtils.getPublicStaticStringConstant(wrapperType,
                    ApplicationCodeGenUtils.ENTITY_NAME);
            final EntityClassDef entityClassDef = getEntityClassDef(entityName);
            return ReflectUtils.newInstance(wrapperType, WRAPPER_PARAMS_2, entityClassDef, instList);
        }

        return null;
    }

    @Override
    public <T extends EntityWrapper> T wrapperOf(Class<T> wrapperType, ValueStore valueStore) throws UnifyException {
        if (valueStore != null) {
            final String entityName = ReflectUtils.getPublicStaticStringConstant(wrapperType,
                    ApplicationCodeGenUtils.ENTITY_NAME);
            final EntityClassDef entityClassDef = getEntityClassDef(entityName);
            return ReflectUtils.newInstance(wrapperType, WRAPPER_PARAMS_3, entityClassDef, valueStore);
        }

        return null;
    }

    @Override
    public <T extends EntityWrapper> T wrapperOf(Class<T> wrapperType, ValueStoreReader valueStoreReader)
            throws UnifyException {
        return this.wrapperOf(wrapperType, valueStoreReader.getValueStore());
    }

    @Override
    public <T extends EntityWrapper> T wrapperOf(Class<T> wrapperType, ValueStoreWriter valueStoreWriter)
            throws UnifyException {
        return this.wrapperOf(wrapperType, valueStoreWriter.getValueStore());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Query<? extends Entity> queryOf(String entityName) throws UnifyException {
        final EntityClassDef entityClassDef = getEntityClassDef(entityName);
        return Query.of((Class<? extends Entity>) entityClassDef.getEntityClass());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Query<? extends Entity> queryOf(Class<? extends EntityWrapper> wrapperType) throws UnifyException {
        final String entityName = ReflectUtils.getPublicStaticStringConstant(wrapperType,
                ApplicationCodeGenUtils.ENTITY_NAME);
        final EntityClassDef entityClassDef = getEntityClassDef(entityName);
        return Query.of((Class<? extends Entity>) entityClassDef.getEntityClass());
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

            addFilterType(fgdb, FilterType.BASE, _appletDef, AppletPropertyConstants.BASE_RESTRICTION);
            addFilterType(fgdb, FilterType.MAINTAIN_UPDATE, _appletDef,
                    AppletPropertyConstants.MAINTAIN_FORM_UPDATE_CONDITION);
            addFilterType(fgdb, FilterType.MAINTAIN_DELETE, _appletDef,
                    AppletPropertyConstants.MAINTAIN_FORM_DELETE_CONDITION);
            return fgdb.build();
        }

        return null;
    }

    private void addFilterType(FilterGroupDef.Builder fgdb, FilterType filterType, AppletDef _appletDef,
            String propertyName) throws UnifyException {
        final String condition = _appletDef.getPropValue(String.class, propertyName);
        if (!StringUtils.isBlank(condition)) {
            fgdb.addFilter(filterType, _appletDef.getFilterDef(condition));
        }
    }

    @Override
    public void saveSuggestions(Object entityDef, Entity inst) throws UnifyException {
        EntityDef _entityDef = (EntityDef) entityDef;
        if (_entityDef.isWithSuggestionFields()) {
            if (appletUtilities.system().getSysParameterValue(boolean.class,
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
                        if (parts.isWithMinorName()) {
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
    public boolean isApplicationDevelopable(String applicationName) throws UnifyException {
        return environment().value(boolean.class, "developable", new ApplicationQuery().name(applicationName));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void bumpVersion(EntityDef entityDef, Long id) throws UnifyException {
        final Class<?> entityClass = getEntityClassDef(entityDef.getLongName()).getEntityClass();
        if (BaseVersionEntity.class.isAssignableFrom(entityClass)) {
            final boolean isClearMergeVersion = BaseApplicationEntity.class.isAssignableFrom(entityClass)
                    && !isEnterprise();
            Query<?> query = Query.of((Class<? extends Entity>) entityClass).addEquals("id", id);
            long bumpedVersionNo = environment().value(long.class, "versionNo", query) + 1L;
            Update update = isClearMergeVersion
                    ? new Update().add("versionNo", bumpedVersionNo).add("devMergeVersionNo", null)
                    : new Update().add("versionNo", bumpedVersionNo);

            if (AppEntity.class.equals(entityClass)) {
                query.addEquals("configType", ConfigType.CUSTOM).addIsNull("delegate");
                update.add("schemaUpdateRequired", true);
            }

            environment().updateAll(query, update);
        }
    }

    @Override
    public String getApplicationName(Long applicationId) throws UnifyException {
        return environment().value(String.class, "name", new ApplicationQuery().id(applicationId));
    }

    @Override
    public Long getApplicationId(String applicationName) throws UnifyException {
        return environment().value(Long.class, "id", new ApplicationQuery().name(applicationName));
    }

    @Override
    public Long getAppAppletApplicationId(Long appAppletId) throws UnifyException {
        return environment().value(Long.class, "applicationId", new AppAppletQuery().id(appAppletId));
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
            Long moduleId = appletUtilities.system().createModule(module);
            application.setModuleId(moduleId);
        }

        Long applicationId = (Long) environment().create(application);
        final String applicationPrivilegeCode = PrivilegeNameUtils.getApplicationPrivilegeName(application.getName());
        applicationPrivilegeManager.registerPrivilege(application.getConfigType(), applicationId,
                ApplicationPrivilegeConstants.APPLICATION_CATEGORY_CODE, applicationPrivilegeCode,
                application.getDescription());
        UserToken userToken = getUserToken();
        if (!userToken.isReservedUser() && appletUtilities.system().getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.ASSIGN_APPLICATIONENTITY_ONCREATE)) {
            applicationPrivilegeManager.assignPrivilegeToRole(userToken.getRoleCode(), applicationPrivilegeCode);
        }

        return applicationId;
    }

    @Override
    public List<String> getApplicationNames(String moduleName) throws UnifyException {
        return environment().valueList(String.class, "name",
                new ApplicationQuery().moduleName(moduleName).addOrder("name"));
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
    public Application findApplication(Long applicationId) throws UnifyException {
        return environment().list(Application.class, applicationId);
    }

    @Override
    public List<AppApplet> findManageEntityListApplets(String entity) throws UnifyException {
        return environment().listAll(new AppAppletQuery().typeIn(AppletType.MANAGE_ENTITY_LIST_TYPES).entity(entity));
    }

    @Override
    public List<AppApplet> findCreateEntityApplets(String entity) throws UnifyException {
        return environment().listAll(new AppAppletQuery().type(AppletType.CREATE_ENTITY).entity(entity));
    }

    @Override
    public List<? extends Listable> findAppEntityUniqueConstraints(String entity) throws UnifyException {
        ApplicationEntityNameParts parts = ApplicationNameUtils.getApplicationEntityNameParts(entity);
        List<? extends Entity> list = environment()
                .findAll(new AppEntityUniqueConstraintQuery().applicationName(parts.getApplicationName())
                        .entityName(parts.getEntityName()).addSelect("name", "description"));
        return EntityUtils.getListablesFromEntityList(list, "name", "description");
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
    public boolean isAppletWithWorkflowCopy(String appletName) throws UnifyException {
        ApplicationEntityNameParts np = ApplicationNameUtils.getApplicationEntityNameParts(appletName);
        return environment().countAll(new AppAppletPropQuery().applicationName(np.getApplicationName())
                .appletName(np.getEntityName()).name(AppletPropertyConstants.WORKFLOWCOPY).value("true")) > 0;
    }

    @Override
    public boolean isWorkEntityWithPendingDraft(Class<? extends WorkEntity> entityClass, Long id)
            throws UnifyException {
        return environment().countAll(Query.of(entityClass).addEquals("originalCopyId", id)
                .addEquals("wfItemVersionType", WfItemVersionType.DRAFT)) > 0;
    }

    @Override
    public AppletWorkflowCopyInfo getAppletWorkflowCopyInfo(String appletName) throws UnifyException {
        final ApplicationEntityNameParts np = ApplicationNameUtils.getApplicationEntityNameParts(appletName);
        final long appletVersionNo = environment().value(long.class, "versionNo",
                new AppAppletQuery().applicationName(np.getApplicationName()).name(np.getEntityName()));
        final boolean supportMultiItemAction = appletUtilities.system().getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.ENABLE_DRAFT_WORKFLOW_MULTIITEM_ACTION);

        final int noOfCreateApprovals = DataUtils.convert(int.class,
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_CREATE_APPROVAL_LEVELS));
        final int noOfUpdateApprovals = DataUtils.convert(int.class,
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_UPDATE_APPROVAL_LEVELS));
        final int noOfDeleteApprovals = DataUtils.convert(int.class,
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_DELETE_APPROVAL_LEVELS));

        final AppletWorkflowCopyInfo.Builder awcb = AppletWorkflowCopyInfo.newBuilder(appletName,
                getAppletProperty(np, AppletPropertyConstants.SEARCH_TABLE),
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_ATTACHMENT_PROVIDER), noOfCreateApprovals,
                noOfUpdateApprovals, noOfDeleteApprovals, appletVersionNo, supportMultiItemAction);
        // Creation workflow
        awcb.withEvent(WorkflowCopyType.CREATION, EventType.ON_SUBMIT,
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_CREATE_SUBMIT_ALERT),
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_CREATE_SUBMIT_SETVALUES));
        awcb.withEvent(WorkflowCopyType.CREATION, EventType.ON_APPROVE,
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_CREATE_APPROVAL_ALERT),
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_CREATE_APPROVAL_SETVALUES));
        awcb.withEvent(WorkflowCopyType.CREATION, EventType.ON_REJECT,
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_CREATE_REJECTION_ALERT),
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_CREATE_REJECTION_SETVALUES));
        awcb.withEvent(WorkflowCopyType.CREATION, EventType.ON_RESUBMIT,
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_CREATE_RESUBMIT_ALERT),
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_CREATE_RESUBMIT_SETVALUES));
        awcb.withEvent(WorkflowCopyType.CREATION, EventType.ON_DISCARD,
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_CREATE_DISCARD_ALERT),
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_CREATE_DISCARD_SETVALUES));
        awcb.withEvent(WorkflowCopyType.CREATION, EventType.ON_ABORT,
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_CREATE_ABORT_ALERT),
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_CREATE_ABORT_SETVALUES));

        // Update workflow
        awcb.withEvent(WorkflowCopyType.UPDATE, EventType.ON_SUBMIT,
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_UPDATE_SUBMIT_ALERT),
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_UPDATE_SUBMIT_SETVALUES));
        awcb.withEvent(WorkflowCopyType.UPDATE, EventType.ON_APPROVE,
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_UPDATE_APPROVAL_ALERT),
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_UPDATE_APPROVAL_SETVALUES));
        awcb.withEvent(WorkflowCopyType.UPDATE, EventType.ON_REJECT,
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_UPDATE_REJECTION_ALERT),
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_UPDATE_REJECTION_SETVALUES));
        awcb.withEvent(WorkflowCopyType.UPDATE, EventType.ON_RESUBMIT,
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_UPDATE_RESUBMIT_ALERT),
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_UPDATE_RESUBMIT_SETVALUES));
        awcb.withEvent(WorkflowCopyType.UPDATE, EventType.ON_DISCARD,
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_UPDATE_DISCARD_ALERT),
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_UPDATE_DISCARD_SETVALUES));
        awcb.withEvent(WorkflowCopyType.UPDATE, EventType.ON_ABORT,
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_UPDATE_ABORT_ALERT),
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_UPDATE_ABORT_SETVALUES));

        // Deletion workflow
        awcb.withEvent(WorkflowCopyType.DELETION, EventType.ON_SUBMIT,
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_DELETE_SUBMIT_ALERT),
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_DELETE_SUBMIT_SETVALUES));
        awcb.withEvent(WorkflowCopyType.DELETION, EventType.ON_APPROVE,
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_DELETE_APPROVAL_ALERT),
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_DELETE_APPROVAL_SETVALUES));
        awcb.withEvent(WorkflowCopyType.DELETION, EventType.ON_REJECT,
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_DELETE_REJECTION_ALERT),
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_DELETE_REJECTION_SETVALUES));
        awcb.withEvent(WorkflowCopyType.DELETION, EventType.ON_ABORT,
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_DELETE_ABORT_ALERT),
                getAppletProperty(np, AppletPropertyConstants.WORKFLOWCOPY_DELETE_ABORT_SETVALUES));

        // Alert
        final List<AppAppletAlert> alerts = environment().findAll(
                new AppAppletAlertQuery().applicationName(np.getApplicationName()).appletName(np.getEntityName()));
        for (AppAppletAlert appAppletAlert : alerts) {
            awcb.withAlert(appAppletAlert.getName(), appAppletAlert.getDescription(),
                    appAppletAlert.getRecipientPolicy(), appAppletAlert.getRecipientNameRule(),
                    appAppletAlert.getRecipientContactRule(), appAppletAlert.getSender(), appAppletAlert.getTemplate());
        }

        return awcb.build();
    }

    private String getAppletProperty(ApplicationEntityNameParts np, String propertyName) throws UnifyException {
        Optional<String> property = environment().valueOptional(String.class, "value", new AppAppletPropQuery()
                .applicationName(np.getApplicationName()).appletName(np.getEntityName()).name(propertyName));
        return property.isPresent() ? property.get() : null;
    }

    @Override
    public int getWorkitemCategoryParticipationCount(String role) throws UnifyException {
        return appletUtilities.workItemUtilities().countWorkflowLoadingTableInfoByRole(role);
    }

    @Override
    public String getAppAppletEntity(Long appAppletId) throws UnifyException {
        return environment().value(String.class, "entity", new AppAppletQuery().id(appAppletId));
    }

    @Override
    public String getAppAppletEntity(String appletName) throws UnifyException {
        ApplicationEntityNameParts np = ApplicationNameUtils.getApplicationEntityNameParts(appletName);
        return environment().value(String.class, "entity",
                new AppAppletQuery().applicationName(np.getApplicationName()).name(np.getEntityName()));
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
    public List<AppAppletSetValues> findAppAppletSetValues(String appletName) throws UnifyException {
        ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(appletName);
        Long appAppletId = environment().value(Long.class, "id",
                new AppAppletQuery().applicationName(nameParts.getApplicationName()).name(nameParts.getEntityName()));
        return environment().findAll(new AppAppletSetValuesQuery().appAppletId(appAppletId));
    }

    @Override
    public List<AppAppletAlert> findAppAppletAlerts(Long appAppletId) throws UnifyException {
        return environment().findAll(new AppAppletAlertQuery().appAppletId(appAppletId));
    }

    @Override
    public List<? extends Listable> findAppAppletSearchInputsListable(Long appAppletId) throws UnifyException {
        String entity = environment().value(String.class, "entity", new AppAppletQuery().id(appAppletId));
        return findAppEntitySearchInputs(entity);
    }

    @Override
    public List<? extends Listable> findAppEntitySearchInputs(String entity) throws UnifyException {
        ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(entity);
        Long appEntityId = environment().value(Long.class, "id",
                new AppEntityQuery().applicationName(nameParts.getApplicationName()).name(nameParts.getEntityName()));
        List<AppEntitySearchInput> entitySearchInputList = environment()
                .findAll(new AppEntitySearchInputQuery().appEntityId(appEntityId).addSelect("name", "description"));
        return EntityUtils.getListablesFromEntityList(entitySearchInputList, "name", "description");
    }

    @Override
    public List<AppAppletFilter> findAppAppletQuickFilters(Long appAppletId) throws UnifyException {
        return environment().findAll(new AppAppletFilterQuery().appAppletId(appAppletId).quickFilter(true));
    }

    @Override
    public List<? extends Listable> findAppAppletFiltersListable(String appAppletName) throws UnifyException {
        ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(appAppletName);
        Long appAppletId = environment().value(Long.class, "id",
                new AppAppletQuery().applicationName(nameParts.getApplicationName()).name(nameParts.getEntityName()));
        return findAppAppletFiltersListable(appAppletId);
    }

    @Override
    public List<? extends Listable> findAppAppletFiltersListable(Long appAppletId) throws UnifyException {
        List<AppAppletFilter> filterList = environment()
                .findAll(new AppAppletFilterQuery().appAppletId(appAppletId).addSelect("name", "description"));
        return EntityUtils.getListablesFromEntityList(filterList, "name", "description");
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
    public String findAppEntityFieldReferences(AppEntityFieldQuery query) throws UnifyException {
        Optional<String> optional = environment().valueOptional(String.class, "references", query);
        return optional.isPresent() ? optional.get() : null;
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
            Class<T> componentClazz, String filterBy, String filter) throws UnifyException {
        return StringUtils.isBlank(filter)
                ? environment().valueList(Long.class, "id",
                        Query.of(componentClazz).addEquals("applicationName", applicationName).addOrder("id"))
                : environment().valueList(Long.class, "id", Query.of(componentClazz)
                        .addEquals("applicationName", applicationName).addILike(filterBy, filter).addOrder("id"));
    }

    @Override
    public <T extends BaseApplicationEntity> List<Long> findNonClassifiedAppComponentIdList(String applicationName,
            Class<T> componentClazz, String filterBy, String filter) throws UnifyException {
        return StringUtils.isBlank(filter)
                ? environment().valueList(Long.class, "id",
                        Query.of(componentClazz).addEquals("applicationName", applicationName)
                                .addNotEquals("classified", true).addOrder("id"))
                : environment().valueList(Long.class, "id",
                        Query.of(componentClazz).addEquals("applicationName", applicationName)
                                .addILike(filterBy, filter).addNotEquals("classified", true).addOrder("id"));
    }

    @Override
    public <T extends BaseApplicationEntity> List<String> findAppComponentNames(String applicationName,
            Class<T> componentClazz) throws UnifyException {
        return environment().valueList(String.class, "name",
                Query.of(componentClazz).addEquals("applicationName", applicationName).addOrder("name"));
    }

    @Override
    public <T extends BaseApplicationEntity> List<Long> findCustomAppComponentIdList(String applicationName,
            Class<T> componentClazz) throws UnifyException {
        return environment().valueList(Long.class, "id",
                Query.of(componentClazz).addEquals("applicationName", applicationName)
                        .addEquals("configType", ConfigType.CUSTOM).addOrder("id"));
    }

    @Override
    public ApplicationDef getApplicationDef(String applicationName) throws UnifyException {
        return applicationDefFactoryMap.get(applicationName);
    }

    @Override
    public List<ApplicationMenuDef> getApplicationMenuDefs(String appletFilter) throws UnifyException {
        final boolean indicateMenuSectorLabels = appletUtilities.system().getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.SECTOR_LABEL_INDICATION_ON_MENU);
        final boolean indicateSectorColors = appletUtilities.system().getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.SECTOR_COLOR_INDICATION_ON_MENU);
        final boolean sectorSortOnMenu = appletUtilities.system().getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.SECTOR_SORT_ON_MENU);
        List<Application> applicationList = (indicateMenuSectorLabels || indicateSectorColors) && sectorSortOnMenu
                ? environment().listAll(
                        new ApplicationQuery().isMenuAccess().addOrder("sectorShortCode", "displayIndex", "label"))
                : environment().listAll(new ApplicationQuery().isMenuAccess().addOrder("displayIndex", "label"));
        if (!DataUtils.isBlank(applicationList)) {
            List<ApplicationMenuDef> resultList = new ArrayList<ApplicationMenuDef>();
            final String importApplicationName = appletUtilities.system().getSysParameterValue(String.class,
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
    public HelpSheetDef getHelpSheetDef(String helpSheetName) throws UnifyException {
        return helpSheetDefFactoryMap.get(helpSheetName);
    }

    @Override
    public WidgetTypeDef getWidgetTypeDef(String widgetName) throws UnifyException {
        return widgetDefFactoryMap.get(widgetName);
    }

    @Override
    public AppEnumeration findAppEnumeration(Long enumerationId) throws UnifyException {
        return environment().find(AppEnumeration.class, enumerationId);
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
    public Listable getDynamicEnumList(String longName) throws UnifyException {
        ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
        String label = environment().value(String.class, "label", new AppEnumerationQuery()
                .applicationName(nameParts.getApplicationName()).name(nameParts.getEntityName()));
        return new ListData(longName, label);
    }

    @Override
    public List<? extends Listable> getDynamicEnumLists(String label, int limit) throws UnifyException {
        AppEnumerationQuery query = (AppEnumerationQuery) new AppEnumerationQuery()
                .addSelect("applicationName", "name", "label").addOrder("label");
        if (!StringUtils.isBlank(label)) {
            query.labelLike(label);
        }

        query.setLimit(limit);

        List<AppEnumeration> enumerations = environment().listAll(query);
        List<ListData> list = new ArrayList<ListData>();
        for (AppEnumeration enumeration : enumerations) {
            String longName = ApplicationNameUtils.getApplicationEntityLongName(enumeration.getApplicationName(),
                    enumeration.getName());
            list.add(new ListData(longName, enumeration.getLabel()));
        }

        return list;
    }

    @Override
    public List<? extends Listable> getRelatedWidgetTypes(String applicationName) throws UnifyException {
        Collection<String> applicationNames = new HashSet<String>();
        applicationNames.add("application");
        applicationNames.add(applicationName);
        List<AppWidgetType> widgets = environment().listAll(new AppWidgetTypeQuery().applicationNameIn(applicationNames)
                .addSelect("applicationName", "name", "description").addOrder("description"));
        List<ListData> list = new ArrayList<ListData>();
        for (AppWidgetType widget : widgets) {
            String longName = ApplicationNameUtils.getApplicationEntityLongName(widget.getApplicationName(),
                    widget.getName());
            list.add(new ListData(longName, widget.getDescription()));
        }

        return list;
    }

    @Override
    public List<? extends Listable> findAppHelpSheetsByEntity(String entity) throws UnifyException {
        List<AppHelpSheet> sheets = environment().listAll(new AppHelpSheetQuery().entity(entity));
        return ApplicationNameUtils.getListableList(sheets);
    }

    @Override
    public List<Class<?>> getDelegateEntities(List<String> entityLongNames) throws UnifyException {
        List<AppEntity> entityList = environment()
                .listAll(new AppEntityQuery().isDelegated().addSelect("applicationName", "name", "delegate"));
        if (!entityList.isEmpty()) {
            logDebug("Resolving delegate entities...");
            Set<String> entityAliases = new HashSet<String>(entityLongNames);
            List<Class<?>> delegateList = new ArrayList<Class<?>>();
            for (String entityLongName : ApplicationNameUtils.getApplicationEntityLongNames(entityList)) {
                if (entityAliases.contains(entityLongName)) {
                    EntityClassDef entityClassDef = getEntityClassDef(entityLongName);
                    delegateList.add(entityClassDef.getEntityClass());
                }
            }

            return delegateList;
        }

        return Collections.emptyList();
    }

    @Override
    public boolean isEntityDef(String entityName) throws UnifyException {
        ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(entityName);
        return environment().countAll(new AppEntityQuery().applicationName(nameParts.getApplicationName())
                .name(nameParts.getEntityName())) > 0;
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
    public EntityDef getAppletEntityDef(String appletName) throws UnifyException {
        return getEntityDef(getAppAppletEntity(appletName));
    }

    @Override
    public EntityClassDef getAppletEntityClassDef(String appletName) throws UnifyException {
        return getEntityClassDef(getAppAppletEntity(appletName));
    }

    @Override
    public List<AppRef> findAppRefs(AppRefQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public int countAppRefs(AppRefQuery query) throws UnifyException {
        return environment().countAll(query);
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
    public int countFileAttachments(FileAttachmentCategoryType category, String ownerEntityName, Long ownerInstId)
            throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        return environment().countAll(new FileAttachmentQuery().category(category.code())
                .entity(entityDef.getTableName()).entityInstId(ownerInstId));
    }

    @Override
    public boolean saveFileAttachment(FileAttachmentCategoryType category, String ownerEntityName, Long ownerInstId,
            Attachment attachment) throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        FileAttachment oldFileAttachment = environment().findLean(new FileAttachmentQuery().category(category.code())
                .entity(entityDef.getTableName()).entityInstId(ownerInstId).name(attachment.getName()));
        if (oldFileAttachment == null) {
            FileAttachment fileAttachment = new FileAttachment();
            fileAttachment.setType(attachment.getType());
            fileAttachment.setEntityInstId(ownerInstId);
            fileAttachment.setEntity(entityDef.getTableName());
            fileAttachment.setCategory(category.code());
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
    public Map<String, AttachmentDetails> retrieveAllFileAttachmentsByName(FileAttachmentCategoryType category,
            String ownerEntityName, Long ownerInstId) throws UnifyException {
        Map<String, AttachmentDetails> map = Collections.emptyMap();
        List<AttachmentDetails> detailList = retrieveAllFileAttachments(category, ownerEntityName, ownerInstId);
        if (!DataUtils.isBlank(detailList)) {
            map = new LinkedHashMap<String, AttachmentDetails>();
            for (AttachmentDetails details : detailList) {
                map.put(details.getName(), details);
            }
        }

        return map;
    }

    @Override
    public List<AttachmentDetails> retrieveAllFileAttachments(FileAttachmentCategoryType category,
            String ownerEntityName, Long ownerInstId) throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        List<FileAttachment> fileAttachmentList = environment()
                .findAll(new FileAttachmentQuery().category(category.code()).entity(entityDef.getTableName())
                        .entityInstId(ownerInstId).addSelect("id", "type", "versionNo", "name", "title", "fileName"));
        if (!DataUtils.isBlank(fileAttachmentList)) {
            List<AttachmentDetails> list = new ArrayList<AttachmentDetails>();
            for (FileAttachment fileAttachment : fileAttachmentList) {
                list.add(new AttachmentDetails(fileAttachment.getId(), fileAttachment.getType(),
                        fileAttachment.getName(), fileAttachment.getTitle(), fileAttachment.getFileName(),
                        fileAttachment.getVersionNo()));
            }

            return list;
        }

        return Collections.emptyList();
    }

    @Override
    public Attachment retrieveFileAttachment(FileAttachmentCategoryType category, String ownerEntityName,
            Long ownerInstId, String attachmentName) throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        FileAttachment fileAttachment = environment().find(new FileAttachmentQuery().category(category.code())
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
    public boolean deleteFileAttachment(FileAttachmentCategoryType category, String ownerEntityName, Long ownerInstId,
            String attachmentName) throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        return environment().deleteAll(new FileAttachmentQuery().category(category.code())
                .entity(entityDef.getTableName()).entityInstId(ownerInstId).name(attachmentName)) > 0;
    }

    @Override
    public boolean sychFileAttachments(FileAttachmentCategoryType category, String ownerEntityName,
            Long destEntityInstId, Long srcEntityInstId) throws UnifyException {
        if (!destEntityInstId.equals(srcEntityInstId)) {
            final EntityDef entityDef = getEntityDef(ownerEntityName);
            environment().deleteAll(new FileAttachmentQuery().category(category.code()).entity(entityDef.getTableName())
                    .entityInstId(destEntityInstId));
            List<Long> fileAttachmentIdList = environment().valueList(Long.class, "id", new FileAttachmentQuery()
                    .category(category.code()).entity(entityDef.getTableName()).entityInstId(srcEntityInstId));
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
    public PropertySequenceDef retrieveSequenceDef(String category, String ownerEntityName, Long ownerInstId)
            throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        return InputWidgetUtils.getPropertySequenceDef(environment().find(new AppPropertySequenceQuery()
                .category(category).entity(entityDef.getTableName()).entityInstId(ownerInstId)));
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
    public void saveSequenceDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, PropertySequenceDef propertySequenceDef) throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        environment().deleteAll(new AppPropertySequenceQuery().category(category).entity(entityDef.getTableName())
                .entityInstId(ownerInstId));
        if (propertySequenceDef != null && !propertySequenceDef.isBlank()) {
            AppPropertySequence appPropertySequence = new AppPropertySequence();
            appPropertySequence.setEntityInstId(ownerInstId);
            appPropertySequence.setEntity(entityDef.getTableName());
            appPropertySequence.setCategory(category);
            appPropertySequence.setDefinition(InputWidgetUtils.getPropertySequenceDefinition(propertySequenceDef));
            environment().create(appPropertySequence);
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
    public SearchInputsDef retrieveSearchInputsDef(String category, String ownerEntityName, Long ownerInstId)
            throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        return InputWidgetUtils.getSearchInputsDef(null, null, null, environment().find(new AppSearchInputQuery()
                .category(category).entity(entityDef.getTableName()).entityInstId(ownerInstId)));
    }

    @Override
    public void saveSearchInputsDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, SearchInputsDef searchInputsDef) throws UnifyException {
        final EntityDef entityDef = getEntityDef(ownerEntityName);
        environment().deleteAll(new AppSearchInputQuery().category(category).entity(entityDef.getTableName())
                .entityInstId(ownerInstId));
        if (searchInputsDef != null && !searchInputsDef.isBlank()) {
            AppSearchInput appSearchInput = new AppSearchInput();
            appSearchInput.setEntityInstId(ownerInstId);
            appSearchInput.setEntity(entityDef.getTableName());
            appSearchInput.setCategory(category);
            appSearchInput.setDefinition(InputWidgetUtils.getSearchInputDefinition(searchInputsDef));
            environment().create(appSearchInput);
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
        if (fieldName != null) {
            List<String> descList = environment().valueList(String.class, fieldName,
                    Query.of((Class<? extends Entity>) entityClassDef.getEntityClass()).addEquals("id", entityId));
            if (descList.size() == 1) {
                return descList.get(0);
            }
        }

        return null;
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

    @Override
    public DelegateEntityInfo getDelegateEntity(String entityName) throws UnifyException {
        ApplicationEntityNameParts parts = ApplicationNameUtils.getApplicationEntityNameParts(entityName);
        AppEntity appEntity = environment().find(new AppEntityQuery().applicationName(parts.getApplicationName())
                .name(parts.getEntityName()).addSelect("delegate", "dataSourceName"));
        return appEntity != null
                ? new DelegateEntityInfo(appEntity.getDelegate(), entityName, appEntity.getDataSourceName())
                : new DelegateEntityInfo(entityName);
    }

    @Override
    public List<DelegateEntityInfo> getDelegateEntitiesByDelegate(String delegate) throws UnifyException {
        List<AppEntity> entityList = environment().listAll(new AppEntityQuery().addEquals("delegate", delegate)
                .addSelect("applicationName", "name", "dataSourceName"));
        if (!DataUtils.isBlank(entityList)) {
            List<DelegateEntityInfo> resultList = new ArrayList<DelegateEntityInfo>(entityList.size());
            for (AppEntity appEntity : entityList) {
                final String entityName = ApplicationNameUtils
                        .getApplicationEntityLongName(appEntity.getApplicationName(), appEntity.getName());
                resultList.add(new DelegateEntityInfo(delegate, entityName, appEntity.getDataSourceName()));
            }

            return resultList;

        }

        return Collections.emptyList();
    }

    @Override
    public List<DelegateEntityInfo> getDelegateEntitiesByDataSource(String dataSourceName) throws UnifyException {
        List<AppEntity> entityList = environment().listAll(new AppEntityQuery()
                .addEquals("dataSourceName", dataSourceName).addSelect("applicationName", "name", "delegate"));
        if (!DataUtils.isBlank(entityList)) {
            List<DelegateEntityInfo> resultList = new ArrayList<DelegateEntityInfo>(entityList.size());
            for (AppEntity appEntity : entityList) {
                final String entityName = ApplicationNameUtils
                        .getApplicationEntityLongName(appEntity.getApplicationName(), appEntity.getName());
                resultList.add(
                        new DelegateEntityInfo(appEntity.getDelegate(), entityName, appEntity.getDataSourceName()));
            }

            return resultList;

        }

        return Collections.emptyList();
    }

    @Override
    public List<DelegateEntityInfo> getDelegateEntities() throws UnifyException {
        List<AppEntity> entityList = environment().listAll(
                new AppEntityQuery().addIsNotNull("delegate").addSelect("applicationName", "name", "dataSourceName"));
        if (!DataUtils.isBlank(entityList)) {
            List<DelegateEntityInfo> resultList = new ArrayList<DelegateEntityInfo>(entityList.size());
            for (AppEntity appEntity : entityList) {
                final String entityName = ApplicationNameUtils
                        .getApplicationEntityLongName(appEntity.getApplicationName(), appEntity.getName());
                resultList.add(
                        new DelegateEntityInfo(appEntity.getDelegate(), entityName, appEntity.getDataSourceName()));
            }

            return resultList;

        }

        return Collections.emptyList();
    }

    private String buildEntityDescription(EntityDef entityDef, Entity inst) throws UnifyException {
        if (inst != null) {
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

        return "";
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
    public List<DynamicEntityInfo> generateDynamicEntityInfos(List<String> entityNames, String basePackage,
            boolean extension) throws UnifyException {
        Map<String, DynamicEntityInfo> workingMap = new HashMap<String, DynamicEntityInfo>();
        for (String entityName : entityNames) {
            EntityDef entityDef = getEntityDef(entityName);
            buildDynamicEntityInfo(entityDef, workingMap, basePackage, extension);
        }

        List<DynamicEntityInfo> resultList = new ArrayList<DynamicEntityInfo>();
        for (String entityName : entityNames) {
            DynamicEntityInfo info = workingMap.get(entityName);
            info.finalizeResolution();
            info.setAlias(entityName);
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

    @Override
    public void setReloadOnSwitch() throws UnifyException {
        setRequestAttribute(AppletRequestAttributeConstants.RELOAD_ONSWITCH, Boolean.TRUE);
    }

    @Override
    public boolean clearReloadOnSwitch() throws UnifyException {
        return Boolean.TRUE.equals(removeRequestAttribute(AppletRequestAttributeConstants.RELOAD_ONSWITCH));
    }

    @Override
    public boolean isReloadOnSwitch() throws UnifyException {
        return getRequestAttribute(boolean.class, AppletRequestAttributeConstants.RELOAD_ONSWITCH);
    }

    @Override
    public AppHelpSheet findHelpSheet(Long helpSheetId) throws UnifyException {
        return environment().list(AppHelpSheet.class, helpSheetId);
    }

    @Override
    public List<Long> findCustomHelpSheetIdList(String applicationName) throws UnifyException {
        return environment().valueList(Long.class, "id",
                new AppHelpSheetQuery().applicationName(applicationName).isCustom());
    }

    @Taskable(name = ApplicationReplicationTaskConstants.APPLICATION_REPLICATION_TASK_NAME,
            description = "Application Replication Task",
            parameters = {
                    @Parameter(name = ApplicationReplicationTaskConstants.SOURCE_APPLICATION_NAME,
                            description = "$m{applicationreplicationtask.sourceapplicationname}", type = String.class,
                            mandatory = true),
                    @Parameter(name = ApplicationReplicationTaskConstants.DESTINATION_APPLICATION_NAME,
                            description = "$m{applicationreplicationtask.destinationapplicationname}",
                            type = String.class, mandatory = true),
                    @Parameter(name = ApplicationReplicationTaskConstants.DESTINATION_MODULE_NAME,
                            description = "$m{applicationreplicationtask.destinationmodulename}", type = String.class,
                            mandatory = true),
                    @Parameter(name = ApplicationReplicationTaskConstants.REPLICATION_RULES_FILE,
                            description = "$m{applicationreplicationtask.replicationrulesfile}", type = byte[].class,
                            mandatory = true) },
            limit = TaskExecLimit.ALLOW_SINGLE, schedulable = false)
    public int executeApplicationReplicationTask(TaskMonitor taskMonitor, String srcApplicationName,
            String destApplicationName, String destModuleName, byte[] replicationRulesFile) throws UnifyException {
        logDebug(taskMonitor,
                "Executing application replication from source application [{0}] to destination application [{1}] and module [{2}]...",
                srcApplicationName, destApplicationName, destModuleName);
        taskMonitor.getTaskOutput().setResult(ApplicationReplicationTaskConstants.TASK_SUCCESS, Boolean.FALSE);
        logDebug(taskMonitor, "Checking if source application exists...");
        final Application srcApplication = environment().list(new ApplicationQuery().name(srcApplicationName));
        if (srcApplication == null) {
            logDebug(taskMonitor, "Source application does not exist. Replication terminated.");
            taskMonitor.cancel();
            return 0;
        }

        if (!srcApplication.isDevelopable()) {
            logDebug(taskMonitor, "Source application is not developable. Replication terminated.");
            taskMonitor.cancel();
            return 0;
        }

        if (environment().countAll(new AppEntityQuery().applicationId(srcApplication.getId()).isStatic()) > 0) {
            logDebug(taskMonitor, "Some source application entities are static. Replication terminated.");
            taskMonitor.cancel();
            return 0;

        }

        logDebug(taskMonitor, "Checking if destination application exists...");
        if (environment().countAll(new ApplicationQuery().name(destApplicationName)) > 0) {
            logDebug(taskMonitor, "Destination application already exists. Replication terminated.");
            taskMonitor.cancel();
            return 0;
        }

        logDebug(taskMonitor, "Checking if destination module exists...");
        Optional<Long> destModuleId = appletUtilities.system().getModuleId(destModuleName);

        logDebug(taskMonitor, "Creating application replication context...");
        ApplicationReplicationContext ctx = ApplicationReplicationUtils.createApplicationReplicationContext(
                appletUtilities, srcApplicationName, destApplicationName, replicationRulesFile);

        // Application
        logDebug(taskMonitor, "Replicating application...");
        final Long srcApplicationId = srcApplication.getId();
        srcApplication.setModuleId(destModuleId.get());
        srcApplication.setName(destApplicationName);
        srcApplication.setDescription(ctx.messageSwap(srcApplication.getDescription()));
        srcApplication.setLabel(ctx.messageSwap(srcApplication.getLabel()));
        final Long destApplicationId = (Long) environment().create(srcApplication);
        applicationPrivilegeManager.registerPrivilege(ConfigType.CUSTOM, destApplicationId,
                ApplicationPrivilegeConstants.APPLICATION_CATEGORY_CODE,
                PrivilegeNameUtils.getApplicationPrivilegeName(destApplicationName), srcApplication.getDescription());

        // Applets
        logDebug(taskMonitor, "Replicating applets...");
        List<Long> appletIdList = environment().valueList(Long.class, "id",
                new AppAppletQuery().applicationId(srcApplicationId));
        for (Long appletId : appletIdList) {
            AppApplet srcAppApplet = environment().find(AppApplet.class, appletId);
            if (srcAppApplet.getType().isSupportReplication()) {
                String oldDescription = srcAppApplet.getDescription();
                srcAppApplet.setId(null);
                srcAppApplet.setApplicationId(destApplicationId);
                srcAppApplet.setName(ctx.nameSwap(srcAppApplet.getName()));
                srcAppApplet.setDescription(ctx.messageSwap(srcAppApplet.getDescription()));
                srcAppApplet.setLabel(ctx.messageSwap(srcAppApplet.getLabel()));
                srcAppApplet.setEntity(ctx.entitySwap(srcAppApplet.getEntity()));
                srcAppApplet.setRouteToApplet(ctx.entitySwap(srcAppApplet.getRouteToApplet()));
                srcAppApplet.setAssignField(ctx.fieldSwap(srcAppApplet.getAssignField()));
                srcAppApplet.setAssignDescField(ctx.fieldSwap(srcAppApplet.getAssignDescField()));
                srcAppApplet.setBaseField(ctx.fieldSwap(srcAppApplet.getBaseField()));
                srcAppApplet.setPseudoDeleteField(ctx.fieldSwap(srcAppApplet.getPseudoDeleteField()));

                // Applet properties
                for (AppAppletProp appAppletProp : srcAppApplet.getPropList()) {
                    if (refProperties.contains(appAppletProp.getName())) {
                        appAppletProp.setValue(ctx.entitySwap(appAppletProp.getValue()));
                    }
                }

                // Applet set values
                for (AppAppletSetValues appAppletSetValues : srcAppApplet.getSetValuesList()) {
                    appAppletSetValues.setDescription(ctx.messageSwap(appAppletSetValues.getDescription()));
                    appAppletSetValues.setValueGenerator(ctx.componentSwap(appAppletSetValues.getValueGenerator()));
                    ApplicationReplicationUtils.applyReplicationRules(ctx, appAppletSetValues.getValueGenerator(),
                            appAppletSetValues.getSetValues());
                }

                // Applet filters
                for (AppAppletFilter appAppletFilter : srcAppApplet.getFilterList()) {
                    appAppletFilter.setDescription(ctx.messageSwap(appAppletFilter.getDescription()));
                    ApplicationReplicationUtils.applyReplicationRules(ctx, appAppletFilter.getFilter());
                    appAppletFilter.setPreferredForm(ctx.entitySwap(appAppletFilter.getPreferredForm()));
                    appAppletFilter
                            .setPreferredChildListApplet(ctx.entitySwap(appAppletFilter.getPreferredChildListApplet()));
                    appAppletFilter.setFilterGenerator(ctx.componentSwap(appAppletFilter.getFilterGenerator()));
                }

                srcAppApplet.setDeprecated(false);
                srcAppApplet.setConfigType(ConfigType.CUSTOM);
                environment().create(srcAppApplet);
                logDebug(taskMonitor, "Applet [{0}] -> [{1}]...", oldDescription, srcAppApplet.getDescription());

                applicationPrivilegeManager.registerPrivilege(ConfigType.CUSTOM, destApplicationId,
                        ApplicationPrivilegeConstants.APPLICATION_APPLET_CATEGORY_CODE,
                        PrivilegeNameUtils.getAppletPrivilegeName(ApplicationNameUtils
                                .getApplicationEntityLongName(destApplicationName, srcAppApplet.getName())),
                        srcAppApplet.getDescription());
            }
        }

        // Widgets
        logDebug(taskMonitor, "Replicating widgets...");
        List<Long> widgetIdList = environment().valueList(Long.class, "id",
                new AppWidgetTypeQuery().applicationId(srcApplicationId));
        for (Long widgetId : widgetIdList) {
            AppWidgetType srcWidgetType = environment().find(AppWidgetType.class, widgetId);
            String oldDescription = srcWidgetType.getDescription();
            srcWidgetType.setId(null);
            srcWidgetType.setApplicationId(destApplicationId);
            srcWidgetType.setName(ctx.nameSwap(srcWidgetType.getName()));
            srcWidgetType.setDescription(ctx.messageSwap(srcWidgetType.getDescription()));
            srcWidgetType.setConfigType(ConfigType.CUSTOM);
            environment().create(srcWidgetType);
            logDebug(taskMonitor, "Widget [{0}] -> [{1}]...", oldDescription, srcWidgetType.getDescription());
        }

        // References
        logDebug(taskMonitor, "Replicating references...");
        List<Long> referenceIdList = environment().valueList(Long.class, "id",
                new AppRefQuery().applicationId(srcApplicationId));
        for (Long referenceId : referenceIdList) {
            AppRef srcAppRef = environment().find(AppRef.class, referenceId);
            String oldDescription = srcAppRef.getDescription();
            srcAppRef.setId(null);
            srcAppRef.setApplicationId(destApplicationId);
            srcAppRef.setName(ctx.nameSwap(srcAppRef.getName()));
            srcAppRef.setDescription(ctx.messageSwap(srcAppRef.getDescription()));
            srcAppRef.setEntity(ctx.entitySwap(srcAppRef.getEntity()));
            srcAppRef.setSearchField(ctx.fieldSwap(srcAppRef.getSearchField()));
            srcAppRef.setSearchTable(ctx.entitySwap(srcAppRef.getSearchTable()));
            srcAppRef.setSelectHandler(ctx.componentSwap(srcAppRef.getSelectHandler()));
            srcAppRef.setFilterGenerator(ctx.componentSwap(srcAppRef.getFilterGenerator()));
            srcAppRef.setConfigType(ConfigType.CUSTOM);
            ApplicationReplicationUtils.applyReplicationRules(ctx, srcAppRef.getFilter());
            environment().create(srcAppRef);
            logDebug(taskMonitor, "Reference [{0}] -> [{1}]...", oldDescription, srcAppRef.getDescription());
        }

        // Entities
        logDebug(taskMonitor, "Replicating entities...");
        List<Long> entityIdList = environment().valueList(Long.class, "id",
                new AppEntityQuery().applicationId(srcApplicationId));
        for (Long entityId : entityIdList) {
            AppEntity srcAppEntity = environment().find(AppEntity.class, entityId);
            String oldDescription = srcAppEntity.getDescription();
            srcAppEntity.setId(null);
            srcAppEntity.setApplicationId(destApplicationId);
            srcAppEntity.setName(ctx.nameSwap(srcAppEntity.getName()));
            srcAppEntity.setDescription(ctx.messageSwap(srcAppEntity.getDescription()));
            srcAppEntity.setLabel(ctx.messageSwap(srcAppEntity.getLabel()));
            srcAppEntity.setEmailProducerConsumer(ctx.componentSwap(srcAppEntity.getEmailProducerConsumer()));
            srcAppEntity.setEntityClass(ctx.classSwap(srcAppEntity.getEntityClass()));
            if (StringUtils.isBlank(srcAppEntity.getDelegate())) {
                srcAppEntity.setTableName(ctx.tableSwap(srcAppEntity.getTableName()));
            }

            Map<String, AppEntityField> map = new HashMap<String, AppEntityField>();
            for (AppEntityField appEntityField : srcAppEntity.getFieldList()) {
                map.put(appEntityField.getName(), appEntityField);
                appEntityField.setName(ctx.fieldSwap(appEntityField.getName()));
                appEntityField.setLabel(ctx.fieldSwap(appEntityField.getLabel()));
                appEntityField.setColumnName(ctx.fieldSwap(appEntityField.getColumnName()));
                appEntityField.setInputWidget(ctx.entitySwap(appEntityField.getInputWidget()));
                appEntityField.setSuggestionType(ctx.entitySwap(appEntityField.getSuggestionType()));
                appEntityField.setLingualWidget(ctx.entitySwap(appEntityField.getLingualWidget()));
                appEntityField.setAutoFormat(ctx.autoFormatSwap(appEntityField.getAutoFormat()));
                appEntityField.setInputListKey(ctx.fieldSwap(appEntityField.getInputListKey()));
                appEntityField.setReferences(ctx.entitySwap(appEntityField.getReferences()));
            }

            for (AppEntityField appEntityField : srcAppEntity.getFieldList()) {
                if (appEntityField.getDataType().isListOnly()) {
                    AppEntityField refAppEntityField = map.get(appEntityField.getKey());
                    if (refAppEntityField.getReferences() == null
                            || !ApplicationNameUtils.isLongName(refAppEntityField.getReferences())
                            || refAppEntityField.getReferences().startsWith(destApplicationName)) {
                        appEntityField.setKey(ctx.fieldSwap(appEntityField.getKey()));
                        appEntityField.setProperty(ctx.fieldSwap(appEntityField.getProperty()));
                    }
                }
            }

            for (AppEntitySearchInput appEntitySearchInput : srcAppEntity.getSearchInputList()) {
                ApplicationReplicationUtils.applyReplicationRules(ctx, appEntitySearchInput);
            }

            for (AppEntityUpload appEntityUpload : srcAppEntity.getUploadList()) {
                ApplicationReplicationUtils.applyReplicationRules(ctx, appEntityUpload.getFieldSequence());
            }

            for (AppEntityUniqueConstraint constraint : srcAppEntity.getUniqueConstraintList()) {
                constraint.setFieldList(ctx.fieldSwap(constraint.getFieldList()));
                if (!DataUtils.isBlank(constraint.getConditionList())) {
                    for (AppEntityUniqueCondition appEntityUniqueCondition : constraint.getConditionList()) {
                        appEntityUniqueCondition.setField(ctx.fieldSwap(appEntityUniqueCondition.getField()));
                    }
                }
            }

            for (AppEntityIndex index : srcAppEntity.getIndexList()) {
                index.setFieldList(ctx.fieldSwap(index.getFieldList()));
            }

            srcAppEntity.setConfigType(ConfigType.CUSTOM);
            environment().create(srcAppEntity);

            final String entityLongName = ApplicationNameUtils.getApplicationEntityLongName(destApplicationName,
                    srcAppEntity.getName());
            applicationPrivilegeManager.registerPrivilege(ConfigType.CUSTOM, destApplicationId,
                    ApplicationPrivilegeConstants.APPLICATION_ENTITY_CATEGORY_CODE,
                    PrivilegeNameUtils.getAddPrivilegeName(entityLongName),
                    getApplicationMessage("application.entity.privilege.add", srcAppEntity.getDescription()));
            applicationPrivilegeManager.registerPrivilege(ConfigType.CUSTOM, destApplicationId,
                    ApplicationPrivilegeConstants.APPLICATION_ENTITY_CATEGORY_CODE,
                    PrivilegeNameUtils.getEditPrivilegeName(entityLongName),
                    getApplicationMessage("application.entity.privilege.edit", srcAppEntity.getDescription()));
            applicationPrivilegeManager.registerPrivilege(ConfigType.CUSTOM, destApplicationId,
                    ApplicationPrivilegeConstants.APPLICATION_ENTITY_CATEGORY_CODE,
                    PrivilegeNameUtils.getDeletePrivilegeName(entityLongName),
                    getApplicationMessage("application.entity.privilege.delete", srcAppEntity.getDescription()));

            logDebug(taskMonitor, "Entity [{0}] -> [{1}]...", oldDescription, srcAppEntity.getDescription());
        }

        // Tables
        logDebug(taskMonitor, "Replicating tables...");
        List<Long> tableIdList = environment().valueList(Long.class, "id",
                new AppTableQuery().applicationId(srcApplicationId));
        for (Long tableId : tableIdList) {
            AppTable srcAppTable = environment().find(AppTable.class, tableId);
            String oldDescription = srcAppTable.getDescription();
            srcAppTable.setId(null);
            srcAppTable.setApplicationId(destApplicationId);
            srcAppTable.setName(ctx.nameSwap(srcAppTable.getName()));
            srcAppTable.setDescription(ctx.messageSwap(srcAppTable.getDescription()));
            srcAppTable.setLabel(ctx.messageSwap(srcAppTable.getLabel()));
            srcAppTable.setEntity(ctx.entitySwap(srcAppTable.getEntity()));
            srcAppTable.setDetailsPanelName(ctx.componentSwap(srcAppTable.getDetailsPanelName()));
            srcAppTable.setLoadingFilterGen(ctx.componentSwap(srcAppTable.getLoadingFilterGen()));
            srcAppTable.setLoadingSearchInput(ctx.componentSwap(srcAppTable.getLoadingSearchInput()));

            // Columns
            for (AppTableColumn appTableColumn : srcAppTable.getColumnList()) {
                appTableColumn.setField(ctx.fieldSwap(appTableColumn.getField()));
                appTableColumn.setLabel(ctx.messageSwap(appTableColumn.getLabel()));
                appTableColumn.setRenderWidget(ctx.entitySwap(appTableColumn.getRenderWidget()));
            }

            // Filters
            for (AppTableFilter appTableFilter : srcAppTable.getFilterList()) {
                appTableFilter.setFilterGenerator(ctx.componentSwap(appTableFilter.getFilterGenerator()));
                ApplicationReplicationUtils.applyReplicationRules(ctx, appTableFilter.getFilter());
            }

            // Table Loading
            for (AppTableLoading appTableLoading : srcAppTable.getLoadingList()) {
                appTableLoading.setProvider(ctx.componentSwap(appTableLoading.getProvider()));
            }

            srcAppTable.setConfigType(ConfigType.CUSTOM);
            environment().create(srcAppTable);
            logDebug(taskMonitor, "Table [{0}] -> [{1}]...", oldDescription, srcAppTable.getDescription());
        }

        // Forms
        logDebug(taskMonitor, "Replicating forms...");
        List<Long> formIdList = environment().valueList(Long.class, "id",
                new AppFormQuery().applicationId(srcApplicationId));
        for (Long formId : formIdList) {
            AppForm srcAppForm = environment().find(AppForm.class, formId);
            String oldDescription = srcAppForm.getDescription();
            srcAppForm.setId(null);
            srcAppForm.setApplicationId(destApplicationId);
            srcAppForm.setEntity(ctx.entitySwap(srcAppForm.getEntity()));
            srcAppForm.setName(ctx.nameSwap(srcAppForm.getName()));
            srcAppForm.setDescription(ctx.messageSwap(srcAppForm.getDescription()));
            srcAppForm.setConsolidatedReview(ctx.componentSwap(srcAppForm.getConsolidatedReview()));
            srcAppForm.setConsolidatedValidation(ctx.componentSwap(srcAppForm.getConsolidatedValidation()));
            srcAppForm.setConsolidatedState(ctx.componentSwap(srcAppForm.getConsolidatedState()));
            srcAppForm.setListingGenerator(ctx.componentSwap(srcAppForm.getListingGenerator()));

            // Filters
            for (AppFormFilter appFormFilter : srcAppForm.getFilterList()) {
                appFormFilter.setFilterGenerator(ctx.componentSwap(appFormFilter.getFilterGenerator()));
                ApplicationReplicationUtils.applyReplicationRules(ctx, appFormFilter.getFilter());
            }

            // Actions
            for (AppFormAction appFormAction : srcAppForm.getActionList()) {
                appFormAction.setName(ctx.nameSwap(appFormAction.getName()));
                appFormAction.setPolicy(ctx.componentSwap(appFormAction.getPolicy()));
                ApplicationReplicationUtils.applyReplicationRules(ctx, appFormAction.getOnCondition());

                applicationPrivilegeManager.registerPrivilege(ConfigType.CUSTOM, destApplicationId,
                        ApplicationPrivilegeConstants.APPLICATION_FORMACTION_CATEGORY_CODE,
                        PrivilegeNameUtils.getFormActionPrivilegeName(appFormAction.getName()),
                        appFormAction.getDescription());
            }

            // Elements
            for (AppFormElement appFormElement : srcAppForm.getElementList()) {
                if (appFormElement.getType().isFieldType()) {
                    appFormElement.setElementName(ctx.fieldSwap(appFormElement.getElementName()));
                }

                appFormElement.setTabApplet(ctx.entitySwap(appFormElement.getTabApplet()));
                appFormElement.setTabReference(ctx.entitySwap(appFormElement.getTabReference()));
                appFormElement.setTabMappedForm(ctx.entitySwap(appFormElement.getTabMappedForm()));
                appFormElement.setInputWidget(ctx.entitySwap(appFormElement.getInputWidget()));
                appFormElement.setInputReference(ctx.entitySwap(appFormElement.getInputReference()));
            }

            // Related Lists
            for (AppFormRelatedList appFormRelatedList : srcAppForm.getRelatedList()) {
                appFormRelatedList.setApplet(ctx.entitySwap(appFormRelatedList.getApplet()));
            }

            // Annotation Policies
            for (AppFormAnnotation appFormAnnotation : srcAppForm.getAnnotationList()) {
                ApplicationReplicationUtils.applyReplicationRules(ctx, appFormAnnotation.getOnCondition());
            }

            // State Policies
            for (AppFormStatePolicy appFormStatePolicy : srcAppForm.getFieldStateList()) {
                appFormStatePolicy.setValueGenerator(ctx.componentSwap(appFormStatePolicy.getValueGenerator()));
                ApplicationReplicationUtils.applyReplicationRules(ctx, appFormStatePolicy.getOnCondition());
                ApplicationReplicationUtils.applyReplicationRules(ctx, appFormStatePolicy.getValueGenerator(),
                        appFormStatePolicy.getSetValues());
                for (AppFormSetState appFormSetState : appFormStatePolicy.getSetStateList()) {
                    if (appFormSetState.getType().isFieldType()) {
                        appFormSetState.setTarget(ctx.fieldSwap(appFormSetState.getTarget()));
                    }
                }
            }

            // Widget Rule Policies
            for (AppFormWidgetRulesPolicy appFormWidgetRulesPolicy : srcAppForm.getWidgetRulesList()) {
                ApplicationReplicationUtils.applyReplicationRules(ctx, appFormWidgetRulesPolicy.getOnCondition());
                ApplicationReplicationUtils.applyReplicationRules(ctx, appFormWidgetRulesPolicy.getWidgetRules());
            }

            // Field Validation Policies
            for (AppFormFieldValidationPolicy appFormFieldValidationPolicy : srcAppForm.getFieldValidationList()) {
                appFormFieldValidationPolicy.setFieldName(ctx.fieldSwap(appFormFieldValidationPolicy.getFieldName()));
                appFormFieldValidationPolicy
                        .setValidation(ctx.componentSwap(appFormFieldValidationPolicy.getValidation()));
            }

            // Form Validation Policies
            for (AppFormValidationPolicy appFormValidationPolicy : srcAppForm.getFormValidationList()) {
                appFormValidationPolicy.setErrorMatcher(ctx.componentSwap(appFormValidationPolicy.getErrorMatcher()));
                ApplicationReplicationUtils.applyReplicationRules(ctx, appFormValidationPolicy.getErrorCondition());
            }

            // Form Review Policies
            for (AppFormReviewPolicy appFormReviewPolicy : srcAppForm.getFormReviewList()) {
                appFormReviewPolicy.setErrorMatcher(ctx.componentSwap(appFormReviewPolicy.getErrorMatcher()));
                ApplicationReplicationUtils.applyReplicationRules(ctx, appFormReviewPolicy.getErrorCondition());
            }

            srcAppForm.setConfigType(ConfigType.CUSTOM);
            environment().create(srcAppForm);
            logDebug(taskMonitor, "Form [{0}] -> [{1}]...", oldDescription, srcAppForm.getDescription());
        }

        // Property Lists
        logDebug(taskMonitor, "Replicating property lists...");
        List<Long> propertyListIdList = environment().valueList(Long.class, "id",
                new AppPropertyListQuery().applicationId(srcApplicationId));
        for (Long propertyListId : propertyListIdList) {
            AppPropertyList srcAppPropertyList = environment().find(AppPropertyList.class, propertyListId);
            String oldDescription = srcAppPropertyList.getDescription();
            srcAppPropertyList.setId(null);
            srcAppPropertyList.setApplicationId(destApplicationId);
            srcAppPropertyList.setName(ctx.nameSwap(srcAppPropertyList.getName()));
            srcAppPropertyList.setDescription(ctx.messageSwap(srcAppPropertyList.getDescription()));

            for (AppPropertySet appPropertySet : srcAppPropertyList.getItemSet()) {
                for (AppPropertyListItem appPropertyListItem : appPropertySet.getItemList()) {
                    appPropertyListItem.setInputWidget(ctx.entitySwap(appPropertyListItem.getInputWidget()));
                    appPropertyListItem.setReferences(ctx.entitySwap(appPropertyListItem.getReferences()));
                }
            }

            srcAppPropertyList.setConfigType(ConfigType.CUSTOM);
            environment().create(srcAppPropertyList);
            logDebug(taskMonitor, "Property list [{0}] -> [{1}]...", oldDescription,
                    srcAppPropertyList.getDescription());
        }

        // Property Rules
        logDebug(taskMonitor, "Replicating property rules...");
        List<Long> propertyRuleIdList = environment().valueList(Long.class, "id",
                new AppPropertyRuleQuery().applicationId(srcApplicationId));
        for (Long propertyRuleId : propertyRuleIdList) {
            AppPropertyRule srcAppPropertyRule = environment().find(AppPropertyRule.class, propertyRuleId);
            String oldDescription = srcAppPropertyRule.getDescription();
            srcAppPropertyRule.setId(null);
            srcAppPropertyRule.setApplicationId(destApplicationId);
            srcAppPropertyRule.setName(ctx.nameSwap(srcAppPropertyRule.getName()));
            srcAppPropertyRule.setDescription(ctx.messageSwap(srcAppPropertyRule.getDescription()));
            srcAppPropertyRule.setEntity(ctx.entitySwap(srcAppPropertyRule.getEntity()));
            srcAppPropertyRule.setDefaultList(ctx.entitySwap(srcAppPropertyRule.getDefaultList()));

            for (AppPropertyRuleChoice appPropertyRuleChoice : srcAppPropertyRule.getChoiceList()) {
                appPropertyRuleChoice.setList(ctx.entitySwap(appPropertyRuleChoice.getList()));
            }

            srcAppPropertyRule.setConfigType(ConfigType.CUSTOM);
            environment().create(srcAppPropertyRule);
            logDebug(taskMonitor, "Property rule [{0}] -> [{1}]...", oldDescription,
                    srcAppPropertyRule.getDescription());
        }

        // Assignment Pages
        logDebug(taskMonitor, "Replicating assignment pages...");
        List<Long> assignmentPageIdList = environment().valueList(Long.class, "id",
                new AppAssignmentPageQuery().applicationId(srcApplicationId));
        for (Long assignmentPageId : assignmentPageIdList) {
            AppAssignmentPage srcAppAssignmentPage = environment().find(AppAssignmentPage.class, assignmentPageId);
            String oldDescription = srcAppAssignmentPage.getDescription();
            srcAppAssignmentPage.setId(null);
            srcAppAssignmentPage.setApplicationId(destApplicationId);
            srcAppAssignmentPage.setName(ctx.nameSwap(srcAppAssignmentPage.getName()));
            srcAppAssignmentPage.setDescription(ctx.messageSwap(srcAppAssignmentPage.getDescription()));
            srcAppAssignmentPage.setLabel(ctx.messageSwap(srcAppAssignmentPage.getLabel()));
            srcAppAssignmentPage.setEntity(ctx.entitySwap(srcAppAssignmentPage.getEntity()));
            srcAppAssignmentPage.setCommitPolicy(ctx.componentSwap(srcAppAssignmentPage.getCommitPolicy()));
            srcAppAssignmentPage.setBaseField(ctx.fieldSwap(srcAppAssignmentPage.getBaseField()));
            srcAppAssignmentPage.setAssignField(ctx.fieldSwap(srcAppAssignmentPage.getAssignField()));

            srcAppAssignmentPage.setConfigType(ConfigType.CUSTOM);
            environment().create(srcAppAssignmentPage);
            logDebug(taskMonitor, "Assignment page [{0}] -> [{1}]...", oldDescription,
                    srcAppAssignmentPage.getDescription());
        }

        // Suggestions
        logDebug(taskMonitor, "Replicating suggestion types...");
        List<Long> suggestionTypeIdList = environment().valueList(Long.class, "id",
                new AppSuggestionTypeQuery().applicationId(srcApplicationId));
        for (Long suggestionTypeId : suggestionTypeIdList) {
            AppSuggestionType srcAppSuggestionType = environment().find(AppSuggestionType.class, suggestionTypeId);
            String oldDescription = srcAppSuggestionType.getDescription();
            srcAppSuggestionType.setId(null);
            srcAppSuggestionType.setApplicationId(destApplicationId);
            srcAppSuggestionType.setName(ctx.nameSwap(srcAppSuggestionType.getName()));
            srcAppSuggestionType.setDescription(ctx.messageSwap(srcAppSuggestionType.getDescription()));
            srcAppSuggestionType.setParent(ctx.entitySwap(srcAppSuggestionType.getParent()));

            for (AppSuggestion suggestion : srcAppSuggestionType.getSuggestionList()) {
                suggestion.setFieldName(ctx.fieldSwap(suggestion.getFieldName()));
            }

            srcAppSuggestionType.setConfigType(ConfigType.CUSTOM);
            environment().create(srcAppSuggestionType);
            logDebug(taskMonitor, "Suggestion type [{0}] -> [{1}]...", oldDescription,
                    srcAppSuggestionType.getDescription());
        }

        logDebug(taskMonitor, "Replicating other application artifacts...");
        if (!DataUtils.isBlank(applicationArtifactInstallerList)) {
            for (ApplicationArtifactInstaller applicationArtifactInstaller : applicationArtifactInstallerList) {
                applicationArtifactInstaller.replicateApplicationArtifacts(taskMonitor, srcApplicationId,
                        destApplicationId, ctx);
            }
        }

        logDebug(taskMonitor, "Application successfully replicated.");

        taskMonitor.getTaskOutput().setResult(ApplicationReplicationTaskConstants.TASK_SUCCESS, Boolean.TRUE);
        return 0;
    }

    @Taskable(name = ApplicationDeletionTaskConstants.APPLICATION_DELETION_TASK_NAME,
            description = "Application Deletion Task",
            parameters = { @Parameter(name = ApplicationDeletionTaskConstants.APPLICATION_NAME,
                    description = "$m{applicationdeletiontask.applicationname}", type = String.class,
                    mandatory = true) },
            limit = TaskExecLimit.ALLOW_SINGLE, schedulable = false)
    public int executeApplicationDeletionTask(TaskMonitor taskMonitor, String applicationName) throws UnifyException {
        logDebug(taskMonitor, "Executing deletion on application [{0}] ...", applicationName);
        logDebug(taskMonitor, "Checking if application developable...");
        taskMonitor.getTaskOutput().setResult(ApplicationDeletionTaskConstants.TASK_SUCCESS, Boolean.FALSE);
        final Application application = environment().list(new ApplicationQuery().name(applicationName));
        if (!application.isDevelopable()) {
            logDebug(taskMonitor, "Application is non-developable. Deletion terminated.");
            return 0;
        }

        logDebug(taskMonitor, "Application is developable.");
        logDebug(taskMonitor, "Checking for application dependants...");
        if (usageListProvider.countUsages(new BeanValueStore(application).getReader(), null) != 0L) {
            logDebug(taskMonitor,
                    "Application is in use or referenced by one or more components belonging to some other application. Deletion terminated.");
            return 0;
        }

        logDebug(taskMonitor, "No application dependants found. Proceeding with deletion...");
        int deletionCount = deleteApplication(taskMonitor, application.getId());
        taskMonitor.getTaskOutput().setResult(ApplicationDeletionTaskConstants.TASK_SUCCESS, Boolean.TRUE);
        return deletionCount;
    }

    private int deleteApplication(TaskMonitor taskMonitor, Long applicationId) throws UnifyException {
        logDebug(taskMonitor, "Deleting application with ID [{0}]...", applicationId);
        int deletionCount = 0;
        if (!DataUtils.isBlank(applicationArtifactInstallerList)) {
            for (ApplicationArtifactInstaller applicationArtifactInstaller : applicationArtifactInstallerList) {
                deletionCount += applicationArtifactInstaller.deleteApplicationArtifacts(taskMonitor, applicationId);
            }
        }

        applicationPrivilegeManager.unregisterApplicationPrivileges(applicationId);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "suggestion types", new AppSuggestionTypeQuery(),
                applicationId, false);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "assignment pages", new AppAssignmentPageQuery(),
                applicationId, false);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "property rules", new AppPropertyRuleQuery(),
                applicationId, false);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "property lists", new AppPropertyListQuery(),
                applicationId, false);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "forms", new AppFormQuery(), applicationId, false);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "tables", new AppTableQuery(), applicationId, false);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "entities", new AppEntityQuery(), applicationId,
                false);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "references", new AppRefQuery(), applicationId, false);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "enumerations", new AppEnumerationQuery(),
                applicationId, false);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "widget types", new AppWidgetTypeQuery(),
                applicationId, false);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "applets", new AppAppletQuery(), applicationId, false);

        environment().delete(Application.class, applicationId);
        logDebug(taskMonitor, "Application with ID [{0}] successfully deleted.", applicationId);
        return deletionCount;
    }

    private int deleteCustomApplication(TaskMonitor taskMonitor, Long applicationId) throws UnifyException {
        logDebug(taskMonitor, "Deleting custom application with ID [{0}]...", applicationId);
        int deletionCount = 0;
        if (!DataUtils.isBlank(applicationArtifactInstallerList)) {
            for (ApplicationArtifactInstaller applicationArtifactInstaller : applicationArtifactInstallerList) {
                deletionCount += applicationArtifactInstaller.deleteCustomApplicationArtifacts(taskMonitor,
                        applicationId);
            }
        }

        applicationPrivilegeManager.unregisterCustonApplicationPrivileges(applicationId);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "suggestion types", new AppSuggestionTypeQuery(),
                applicationId, true);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "assignment pages", new AppAssignmentPageQuery(),
                applicationId, true);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "property rules", new AppPropertyRuleQuery(),
                applicationId, true);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "property lists", new AppPropertyListQuery(),
                applicationId, true);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "forms", new AppFormQuery(), applicationId, true);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "tables", new AppTableQuery(), applicationId, true);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "entities", new AppEntityQuery(), applicationId, true);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "references", new AppRefQuery(), applicationId, true);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "enumerations", new AppEnumerationQuery(),
                applicationId, true);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "widget types", new AppWidgetTypeQuery(),
                applicationId, true);
        deletionCount += deleteApplicationArtifacts(taskMonitor, "applets", new AppAppletQuery(), applicationId, true);

        environment().updateAll(new ApplicationQuery().addEquals("id", applicationId),
                new Update().add("menuAccess", false));
        logDebug(taskMonitor, "Application with ID [{0}] successfully deleted.", applicationId);
        return deletionCount;
    }

    private int deleteApplicationArtifacts(TaskMonitor taskMonitor, String name, BaseApplicationEntityQuery<?> query,
            Long applicationId, boolean customOnly) throws UnifyException {
        logDebug(taskMonitor, "Deleting application {0}...", name);
        int deletion = environment().deleteAll(
                customOnly ? query.applicationId(applicationId).isCustom() : query.applicationId(applicationId));
        logDebug(taskMonitor, "[{1}] application {0} deleted.", name, deletion);
        return deletion;
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
            Map<String, RecLoadInfo> recMap = new LinkedHashMap<String, RecLoadInfo>();
            for (CSVRecord csvRecord : csvFileParser) {
                recMap.clear();
                for (FieldSequenceEntryDef fieldSequenceEntryDef : fieldSequenceList) {
                    Formatter<?> formatter = fieldSequenceEntryDef.isWithStandardFormatCode()
                            ? appletUtilities.formatHelper().newFormatter(fieldSequenceEntryDef.getStandardFormatCode())
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
                        if (recMap.keySet().containsAll(constDef.getFieldList())) {
                            Query<?> query = Query.of((Class<? extends Entity>) entityClassDef.getEntityClass());
                            for (String fieldName : constDef.getFieldList()) {
                                query.addEquals(fieldName, recMap.get(fieldName).getVal());
                            }

                            if (constDef.isWithConditionList()) {
                                for (UniqueConditionDef ucd : constDef.getConditionList()) {
                                    query.addRestriction(ucd.getRestriction());
                                }
                            }

                            _inst = environment().findLean(query);
                            if (_inst != null) {
                                uniqueConstriantDef = constDef;
                                createRecord = false;
                                break;
                            }
                        }
                    }
                }

                if (createRecord) {
                    for (RecLoadInfo recLoadInfo : recMap.values()) {
                        DataUtils.setBeanProperty(inst, recLoadInfo.getFieldName(), recLoadInfo.getVal(),
                                recLoadInfo.getFormatter());
                    }

                    appletUtilities.populateAutoFormatFields(entityDef, inst);
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

    @Override
    @Synchronized(PRE_INSTALLATION_SETUP_LOCK)
    public void performPreInstallationSetup() throws UnifyException {
        // Force schema change for custom entities (studio)
        db().update(new NativeUpdate(
                "UPDATE fc_entity SET schema_update_required_fg = ? WHERE config_type = ? AND delegate IS NULL")
                        .setParam(0, true).setParam(1, "C"));
    }

    @SuppressWarnings("unchecked")
    @Override
    @Synchronized(POST_BOOT_SETUP_LOCK)
    public void performPostBootSetup(final boolean isInstallationPerformed) throws UnifyException {
        if (isTenancyEnabled()) {
            // Detect primary tenant and also possible improper primary tenant change
            final Long actualPrimaryTenantId = appletUtilities.system().getSysParameterValue(Long.class,
                    SystemModuleSysParamConstants.SYSTEM_ACTUAL_PRIMARY_TENANT_ID);
            boolean primaryTenantResolved = false;
            EntityClassDef tenantEntityClassDef = getEntityClassDef("system.tenant");
            MappedEntityProvider<? extends BaseMappedEntityProviderContext> provider = appletUtilities
                    .getMappingProvider((Class<? extends Entity>) tenantEntityClassDef.getEntityClass());
            if (isEntityDef(provider.srcEntity())) {
                getEntityClassDef(provider.srcEntity());
                List<MappedTenant> tenantList = appletUtilities.system()
                        .findTenants((MappedTenantQuery) new MappedTenantQuery().ignoreEmptyCriteria(true));
                for (MappedTenant mappedTenant : tenantList) {
                    if (Boolean.TRUE.equals(mappedTenant.getPrimary())) {
                        if (primaryTenantResolved) {
                            throwOperationErrorException(
                                    new IllegalArgumentException("Multiple primary tenants defined in system."));
                        }

                        if (actualPrimaryTenantId == null) {
                            appletUtilities.system().setSysParameterValue(
                                    SystemModuleSysParamConstants.SYSTEM_ACTUAL_PRIMARY_TENANT_ID,
                                    mappedTenant.getId());
                        } else if (!actualPrimaryTenantId.equals(mappedTenant.getId())) {
                            throwOperationErrorException(
                                    new IllegalArgumentException("Primary tenant has been improperly changed from ["
                                            + actualPrimaryTenantId + "]  to [" + mappedTenant.getId() + "]"));
                        }

                        primaryTenantResolved = true;
                    }
                }
            }

        }

        resolveMappedEntities();

        ensureAllWorkflowCopyComponents(isInstallationPerformed);
    }

    @SuppressWarnings("unchecked")
    @Synchronized(DYNAMIC_ENTITY_BUILD_LOCK)
    public EntityClassDef performDynamicEntityBuild(final String longName) throws UnifyException {
        logInfo("Building dynamic entity information...");
        Map<String, DynamicEntityInfo> dynamicEntityInfoMap = new HashMap<String, DynamicEntityInfo>();
        Set<Long> ids = buildDynamicEntityInfos(longName, dynamicEntityInfoMap);

        // If any schema has changed, pull other entities that have schema changed
        final boolean schemaUpdated = db()
                .countAll(new AppEntityQuery().isSchemaUpdateRequired().isCustom().addAmongst("id", ids)) > 0;
        List<String> otherSchemaUpdateEntities = schemaUpdated ? appletUtilities.getApplicationEntitiesLongNames(
                (AppEntityQuery) new AppEntityQuery().isSchemaUpdateRequired().isCustom().addNotAmongst("id", ids))
                : Collections.emptyList();
        for (String entity : otherSchemaUpdateEntities) {
            buildDynamicEntityInfos(entity, dynamicEntityInfoMap);
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

        logInfo("Dynamic entity information successfully built. [{0}] entities resolved for generation.",
                _dynamicEntityInfos.size());

        // Compile and load class if necessary
        if (!_dynamicEntityInfos.isEmpty()) {
            dynamicSqlEntityLoader.loadDynamicSqlEntities(_dynamicEntityInfos);
        }

        if (schemaUpdated) {
            db().updateAll(new AppEntityQuery().isSchemaUpdateRequired().isCustom(),
                    new Update().add("schemaUpdateRequired", false));
        }

        // Load related entity class definitions
        for (Map.Entry<String, DynamicEntityInfo> entry : dynamicEntityInfoMap.entrySet()) {
            final String _longName = entry.getKey();
            if (!_longName.equals(longName)) {
                EntityDef _entityDef = getEntityDef(_longName);
                Class<? extends Entity> _entityClass = (Class<? extends Entity>) ReflectUtils
                        .classForName(entry.getValue().getClassName());
                registerDelegate(_entityDef, _entityClass);
                try {
                    entityClassDefFactoryMap.putEntityClassDef(_longName, new EntityClassDef(_entityDef, _entityClass));
                } catch (Exception e) {
                    throwOperationErrorException(e);
                }
            }
        }

        final EntityDef entityDef = getEntityDef(longName);
        Class<? extends Entity> entityClass = (Class<? extends Entity>) ReflectUtils
                .classForName(entityDef.getOriginClassName());
        registerDelegate(entityDef, entityClass);
        return new EntityClassDef(entityDef, entityClass);
    }

    private Set<Long> buildDynamicEntityInfos(final String longName,
            Map<String, DynamicEntityInfo> dynamicEntityInfoMap) throws UnifyException {
        Set<Long> _set = new HashSet<Long>();
        EntityDef _entityDef = getEntityDef(longName);
        _set.add(_entityDef.getId());
        buildDynamicEntityInfo(_entityDef, dynamicEntityInfoMap, null, true);

        // Consider all dependents
        List<String> entityNameList = getDependentEntities(longName);
        for (String _entity : entityNameList) {
            _entityDef = getEntityDef(_entity);
            _set.add(_entityDef.getId());
            buildDynamicEntityInfo(_entityDef, dynamicEntityInfoMap, null, true);
        }

        return _set;
    }

    private abstract class EntityClassDefFactoryMap extends FactoryMap<String, EntityClassDef> {

        public EntityClassDefFactoryMap() {
            super(true);
        }

        public void putEntityClassDef(String longName, EntityClassDef entityClassDef) throws Exception {
            put(longName, entityClassDef);
        }

    }

    private List<String> getDependentEntities(String entity) throws UnifyException {
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
            if (_refEntityFieldDef.isEnumGroup()) {
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

    private void restoreModule(TaskMonitor taskMonitor, final ModuleRestore moduleRestore) throws UnifyException {
        setSessionAttribute(FlowCentralSessionAttributeConstants.ALTERNATIVE_RESOURCES_BUNDLE,
                moduleRestore.getMessages());
        try {
            ModuleConfig moduleConfig = moduleRestore.getModuleConfig();
            Optional<Long> moduleIdOpt = appletUtilities.system().getModuleId(moduleConfig.getName());
            Long moduleId = null;
            if (moduleIdOpt.isPresent()) {
                moduleId = moduleIdOpt.get();
            } else {
                Module module = new Module();
                module.setShortCode(moduleConfig.getShortCode());
                module.setName(moduleConfig.getName());
                module.setDescription(resolveApplicationMessage(moduleConfig.getDescription()));
                module.setLabel(resolveApplicationMessage(moduleConfig.getLabel()));
                module.setStatus(RecordStatus.ACTIVE);
                moduleId = (Long) environment().create(module);
            }

            List<Long> applicationIdList = environment().valueList(Long.class, "id",
                    new ApplicationQuery().moduleId(moduleId).isDevelopable());
            for (Long applicationId : applicationIdList) {
                // Backup role privileges
                applicationPrivilegeManager.backupApplicationRolePrivileges(applicationId);

                // Delete old custom applications
                deleteCustomApplication(taskMonitor, applicationId);
            }

            // Restore applications
            for (ApplicationRestore applicationRestore : moduleRestore.getApplicationList()) {
                restoreApplication(taskMonitor, applicationRestore);
            }

            // Restore role privileges
            applicationPrivilegeManager.restoreApplicationRolePrivileges();
        } finally {
            removeSessionAttribute(FlowCentralSessionAttributeConstants.ALTERNATIVE_RESOURCES_BUNDLE);
        }
    }

    private List<AppletDef> getImportDataAppletDefs(String appletFilter) throws UnifyException {
        Query<AppApplet> query = new AppAppletQuery().type(AppletType.DATA_IMPORT).menuAccess(true)
                .addSelect("applicationName", "name");
        if (isTenancyEnabled() && !getUserToken().isPrimaryTenant()) {
            query.addEquals("allowSecondaryTenants", Boolean.TRUE);
        }

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
        if (isTenancyEnabled() && !getUserToken().isPrimaryTenant()) {
            query.addEquals("allowSecondaryTenants", Boolean.TRUE);
        }

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

    private void resolveMappedEntities() throws UnifyException {
        List<AppEntity> entityList = environment()
                .listAll(new AppEntityQuery().delegate(ApplicationModuleNameConstants.MAPPEDENTITY_ENVIRONMENT_DELEGATE)
                        .addSelect("applicationName", "name"));
        for (String entityLongName : ApplicationNameUtils.getApplicationEntityLongNames(entityList)) {
            getEntityClassDef(entityLongName);
        }
    }

    private void ensureAllWorkflowCopyComponents(boolean isInstallationPerformed) throws UnifyException {
        List<String> updateDraftApplets = new ArrayList<String>();
        List<AppAppletProp> appAppletPropList = environment().listAll(new AppAppletPropQuery()
                .name(AppletPropertyConstants.WORKFLOWCOPY).value("true").addSelect("applicationName", "appletName"));
        for (AppAppletProp appAppletProp : appAppletPropList) {
            updateDraftApplets.add(ApplicationNameUtils.getApplicationEntityLongName(appAppletProp.getApplicationName(),
                    appAppletProp.getAppletName()));
        }

        for (String appletName : updateDraftApplets) {
            appletUtilities.ensureWorkflowCopyWorkflows(appletName, isInstallationPerformed);
        }

        appletUtilities.ensureWorkflowUserInteractionLoadingApplets(isInstallationPerformed);
    }

    @SuppressWarnings("unchecked")
    private boolean installApplication(final TaskMonitor taskMonitor, final ApplicationInstall applicationInstall)
            throws UnifyException {
        final AppConfig applicationConfig = applicationInstall.getApplicationConfig();
        final Optional<Long> moduleId = appletUtilities.system().getModuleId(applicationConfig.getModule());
        String description = resolveApplicationMessage(applicationConfig.getDescription());

        // Applications
        logDebug(taskMonitor, "Installing application [{0}]...", description);
        Application oldApplication = environment().find(new ApplicationQuery().name(applicationConfig.getName()));
        Long applicationId = null;
        if (oldApplication == null) {
            logDebug(taskMonitor, "Performing new application [{0}] installation...", description);
            Application application = new Application();
            application.setModuleId(moduleId.get());
            application.setName(applicationConfig.getName());
            application.setDescription(description);
            application.setLabel(resolveApplicationMessage(applicationConfig.getLabel()));
            application.setDisplayIndex(applicationConfig.getDisplayIndex());
            application.setDevelopable(applicationConfig.getDevelopable());
            application.setMenuAccess(applicationConfig.getMenuAccess());
            application.setAllowSecondaryTenants(applicationConfig.getAllowSecondaryTenants());
            application.setConfigType(ConfigType.STATIC);
            applicationId = (Long) environment().create(application);
        } else {
            logDebug(taskMonitor, "Upgrading application [{0}]...", description);
            oldApplication.setModuleId(moduleId.get());
            oldApplication.setDescription(description);
            oldApplication.setLabel(resolveApplicationMessage(applicationConfig.getLabel()));
            oldApplication.setDevelopable(applicationConfig.getDevelopable());
            oldApplication.setMenuAccess(applicationConfig.getMenuAccess());
            oldApplication.setAllowSecondaryTenants(applicationConfig.getAllowSecondaryTenants());
            oldApplication.setDisplayIndex(applicationConfig.getDisplayIndex());
            oldApplication.setConfigType(ConfigType.STATIC);

            environment().updateByIdVersion(oldApplication);
            applicationId = oldApplication.getId();
        }
        applicationInstall.setApplicationId(applicationId);

        final String applicationName = applicationConfig.getName();
        applicationPrivilegeManager.registerPrivilege(ConfigType.STATIC, applicationId,
                ApplicationPrivilegeConstants.APPLICATION_CATEGORY_CODE,
                PrivilegeNameUtils.getApplicationPrivilegeName(applicationName), description);
        if (ApplicationModuleNameConstants.APPLICATION_APPLICATION_NAME.equals(applicationName)) {
            applicationPrivilegeManager.registerPrivilege(ConfigType.STATIC, applicationId,
                    ApplicationPrivilegeConstants.APPLICATION_FEATURE_CATEGORY_CODE,
                    PrivilegeNameUtils
                            .getFeaturePrivilegeName(ApplicationFeatureConstants.SAVE_GLOBAL_TABLE_QUICK_FILTER),
                    resolveApplicationMessage("$m{application.privilege.saveglobaltablefilter}"));
        }

        // Applets
        logDebug(taskMonitor, "Installing application applets...");
        environment().updateAll(new AppAppletQuery().applicationId(applicationId).isStatic(),
                new Update().add("deprecated", Boolean.TRUE));
        if (applicationConfig.getAppletsConfig() != null
                && !DataUtils.isBlank(applicationConfig.getAppletsConfig().getAppletList())) {
            AppApplet appApplet = new AppApplet();
            appApplet.setApplicationId(applicationId);
            List<String> appletNames = new ArrayList<String>();
            for (AppletConfig appletConfig : applicationConfig.getAppletsConfig().getAppletList()) {
                appletNames.add(appletConfig.getName());
            }

            environment().updateAll(
                    new AppAppletQuery().applicationId(applicationId).configType(ConfigType.STATIC).nameIn(appletNames),
                    new Update().add("menuAccess", Boolean.FALSE));

            for (AppletConfig appletConfig : applicationConfig.getAppletsConfig().getAppletList()) {
                AppApplet oldAppApplet = environment()
                        .findLean(new AppAppletQuery().applicationId(applicationId).name(appletConfig.getName()));
                description = resolveApplicationMessage(appletConfig.getDescription());
                String label = resolveApplicationMessage(appletConfig.getLabel());
                String entity = ApplicationNameUtils.ensureLongNameReference(applicationName, appletConfig.getEntity());
                if (oldAppApplet == null) {
                    logDebug("Installing new application applet [{0}]. Access = [{1}]...", appletConfig.getName(),
                            appletConfig.getMenuAccess());
                    appApplet.setId(null);
                    appApplet.setName(appletConfig.getName());
                    appApplet.setDescription(description);
                    appApplet.setType(appletConfig.getType());
                    appApplet.setEntity(entity);
                    appApplet.setLabel(label);
                    appApplet.setIcon(appletConfig.getIcon());
                    appApplet.setRouteToApplet(appletConfig.getRouteToApplet());
                    appApplet.setPath(appletConfig.getPath());
                    appApplet.setMenuAccess(appletConfig.getMenuAccess());
                    appApplet.setSupportOpenInNewWindow(appletConfig.getSupportOpenInNewWindow());
                    appApplet.setAllowSecondaryTenants(appletConfig.getAllowSecondaryTenants());
                    appApplet.setDisplayIndex(appletConfig.getDisplayIndex());
                    appApplet.setBaseField(appletConfig.getBaseField());
                    appApplet.setAssignField(appletConfig.getAssignField());
                    appApplet.setAssignDescField(appletConfig.getAssignDescField());
                    appApplet.setPseudoDeleteField(appletConfig.getPseudoDeleteField());
                    appApplet.setTitleFormat(appletConfig.getTitleFormat());
                    appApplet.setDeprecated(false);
                    appApplet.setConfigType(ConfigType.STATIC);
                    populateChildList(appApplet, applicationName, appletConfig, false);
                    environment().create(appApplet);
                } else {
                    logDebug("Upgrading application applet [{0}]. Access = [{1}]...", appletConfig.getName(),
                            appletConfig.getMenuAccess());
                    oldAppApplet.setDescription(description);
                    oldAppApplet.setType(appletConfig.getType());
                    oldAppApplet.setEntity(entity);
                    oldAppApplet.setLabel(label);
                    oldAppApplet.setIcon(appletConfig.getIcon());
                    oldAppApplet.setRouteToApplet(appletConfig.getRouteToApplet());
                    oldAppApplet.setPath(appletConfig.getPath());
                    oldAppApplet.setMenuAccess(appletConfig.getMenuAccess());
                    oldAppApplet.setSupportOpenInNewWindow(appletConfig.getSupportOpenInNewWindow());
                    oldAppApplet.setAllowSecondaryTenants(appletConfig.getAllowSecondaryTenants());
                    oldAppApplet.setDisplayIndex(appletConfig.getDisplayIndex());
                    oldAppApplet.setBaseField(appletConfig.getBaseField());
                    oldAppApplet.setAssignField(appletConfig.getAssignField());
                    oldAppApplet.setAssignDescField(appletConfig.getAssignDescField());
                    oldAppApplet.setPseudoDeleteField(appletConfig.getPseudoDeleteField());
                    oldAppApplet.setTitleFormat(appletConfig.getTitleFormat());
                    oldAppApplet.setConfigType(ConfigType.STATIC);
                    oldAppApplet.setDeprecated(false);
                    populateChildList(oldAppApplet, applicationName, appletConfig, false);
                    environment().updateByIdVersion(oldAppApplet);
                }

                applicationPrivilegeManager
                        .registerPrivilege(ConfigType.STATIC, applicationId,
                                ApplicationPrivilegeConstants.APPLICATION_APPLET_CATEGORY_CODE,
                                PrivilegeNameUtils.getAppletPrivilegeName(ApplicationNameUtils
                                        .getApplicationEntityLongName(applicationName, appletConfig.getName())),
                                description);
            }

            logDebug(taskMonitor, "Installed [{0}] application applets...",
                    applicationConfig.getAppletsConfig().getAppletList().size());
        }

        // Enumerations
        logDebug(taskMonitor, "Installing application enumerations...");
        environment().updateAll(new AppEnumerationQuery().applicationId(applicationId).isStatic(),
                new Update().add("deprecated", Boolean.TRUE));
        if (applicationConfig.getEnumerationsConfig() != null
                && !DataUtils.isBlank(applicationConfig.getEnumerationsConfig().getEnumList())) {
            AppEnumeration appEnumeration = new AppEnumeration();
            appEnumeration.setApplicationId(applicationId);
            for (EnumerationConfig enumerationConfig : applicationConfig.getEnumerationsConfig().getEnumList()) {
                AppEnumeration oldAppEnumeration = environment().findLean(
                        new AppEnumerationQuery().applicationId(applicationId).name(enumerationConfig.getName()));
                description = resolveApplicationMessage(enumerationConfig.getDescription());
                if (oldAppEnumeration == null) {
                    logDebug("Installing new application enumeration [{0}]...", enumerationConfig.getName());
                    appEnumeration.setId(null);
                    appEnumeration.setName(enumerationConfig.getName());
                    appEnumeration.setDescription(resolveApplicationMessage(enumerationConfig.getDescription()));
                    appEnumeration.setLabel(resolveApplicationMessage(enumerationConfig.getLabel()));
                    appEnumeration.setDeprecated(false);
                    appEnumeration.setConfigType(ConfigType.STATIC);
                    populateChildList(appEnumeration, enumerationConfig);
                    environment().create(appEnumeration);
                } else {
                    logDebug("Upgrading application enumeration [{0}]...", enumerationConfig.getName());
                    oldAppEnumeration.setDescription(resolveApplicationMessage(enumerationConfig.getDescription()));
                    oldAppEnumeration.setLabel(resolveApplicationMessage(enumerationConfig.getLabel()));
                    oldAppEnumeration.setDeprecated(false);
                    oldAppEnumeration.setConfigType(ConfigType.STATIC);
                    populateChildList(oldAppEnumeration, enumerationConfig);
                    environment().updateByIdVersion(oldAppEnumeration);
                }
            }
        }

        // Widgets
        logDebug(taskMonitor, "Installing application widget types...");
        environment().updateAll(new AppWidgetTypeQuery().applicationId(applicationId).isStatic(),
                new Update().add("deprecated", Boolean.TRUE));
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
                    appWidgetType.setDeprecated(false);
                    appWidgetType.setConfigType(ConfigType.STATIC);
                    environment().create(appWidgetType);
                } else {
                    logDebug("Upgrading application widget [{0}]...", widgetTypeConfig.getName());
                    oldWidgetType.setDataType(widgetTypeConfig.getDataType());
                    oldWidgetType.setInputType(widgetTypeConfig.getInputType());
                    oldWidgetType.setDescription(description);
                    oldWidgetType.setEditor(widgetTypeConfig.getEditor());
                    oldWidgetType.setRenderer(widgetTypeConfig.getRenderer());
                    oldWidgetType.setStretch(widgetTypeConfig.isStretch());
                    oldWidgetType.setListOption(widgetTypeConfig.isListOption());
                    oldWidgetType.setEnumOption(widgetTypeConfig.isEnumOption());
                    oldWidgetType.setDeprecated(false);
                    oldWidgetType.setConfigType(ConfigType.STATIC);
                    environment().updateByIdVersion(oldWidgetType);
                }
            }
        }

        // References
        logDebug(taskMonitor, "Installing application references...");
        environment().updateAll(new AppRefQuery().applicationId(applicationId).isStatic(),
                new Update().add("deprecated", Boolean.TRUE));
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
                    appRef.setDeprecated(false);
                    appRef.setConfigType(ConfigType.STATIC);
                    environment().create(appRef);
                } else {
                    logDebug("Upgrading application reference [{0}] ...", refConfig.getName());
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
                    oldAppRef.setConfigType(ConfigType.STATIC);
                    oldAppRef.setDeprecated(false);
                    environment().updateByIdVersion(oldAppRef);
                }
            }

            logDebug(taskMonitor, "Installed [{0}] application references...",
                    applicationConfig.getRefsConfig().getRefList().size());
        }

        // Entities
        logDebug(taskMonitor, "Installing application entities...");
        Map<String, Long> entityIdByNameMap = new HashMap<String, Long>();
        environment().updateAll(new AppEntityQuery().applicationId(applicationId).isStatic(),
                new Update().add("deprecated", Boolean.TRUE));
        if (applicationConfig.getEntitiesConfig() != null
                && !DataUtils.isBlank(applicationConfig.getEntitiesConfig().getEntityList())) {
            AppEntity appEntity = new AppEntity();
            appEntity.setApplicationId(applicationId);
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
                    appEntity.setDataSourceName(appEntityConfig.getDataSourceName());
                    appEntity.setMapped(appEntityConfig.getMapped());
                    appEntity.setAuditable(appEntityConfig.getAuditable());
                    appEntity.setReportable(appEntityConfig.getReportable());
                    appEntity.setActionPolicy(appEntityConfig.getActionPolicy());
                    appEntity.setDeprecated(false);
                    appEntity.setConfigType(ConfigType.STATIC);
                    populateChildList(appEntity, applicationName, appEntityConfig, false);
                    entityId = (Long) environment().create(appEntity);
                } else {
                    logDebug("Upgrading application entity [{0}]...", appEntityConfig.getName());
                    oldAppEntity.setBaseType(baseType);
                    oldAppEntity.setDescription(description);
                    oldAppEntity.setLabel(label);
                    oldAppEntity.setEmailProducerConsumer(appEntityConfig.getEmailProducerConsumer());
                    oldAppEntity.setDelegate(appEntityConfig.getDelegate());
                    oldAppEntity.setEntityClass(appEntityConfig.getType());
                    oldAppEntity.setTableName(tableName);
                    oldAppEntity.setDataSourceName(appEntityConfig.getDataSourceName());
                    oldAppEntity.setMapped(appEntityConfig.getMapped());
                    oldAppEntity.setAuditable(appEntityConfig.getAuditable());
                    oldAppEntity.setReportable(appEntityConfig.getReportable());
                    oldAppEntity.setActionPolicy(appEntityConfig.getActionPolicy());
                    oldAppEntity.setConfigType(ConfigType.STATIC);
                    oldAppEntity.setDeprecated(false);
                    populateChildList(oldAppEntity, applicationName, appEntityConfig, false);
                    environment().updateByIdVersion(oldAppEntity);
                    entityId = oldAppEntity.getId();
                }

                appletUtilities.sequence().ensureCachedBlockSequence(appEntityConfig.getType());

                final String entityLongName = ApplicationNameUtils.getApplicationEntityLongName(applicationName,
                        appEntityConfig.getName());
                entityIdByNameMap.put(entityLongName, entityId);
                applicationPrivilegeManager.registerPrivilege(ConfigType.STATIC, applicationId,
                        ApplicationPrivilegeConstants.APPLICATION_ENTITY_CATEGORY_CODE,
                        PrivilegeNameUtils.getAddPrivilegeName(entityLongName),
                        getApplicationMessage("application.entity.privilege.add", description));
                applicationPrivilegeManager.registerPrivilege(ConfigType.STATIC, applicationId,
                        ApplicationPrivilegeConstants.APPLICATION_ENTITY_CATEGORY_CODE,
                        PrivilegeNameUtils.getEditPrivilegeName(entityLongName),
                        getApplicationMessage("application.entity.privilege.edit", description));
                applicationPrivilegeManager.registerPrivilege(ConfigType.STATIC, applicationId,
                        ApplicationPrivilegeConstants.APPLICATION_ENTITY_CATEGORY_CODE,
                        PrivilegeNameUtils.getDeletePrivilegeName(entityLongName),
                        getApplicationMessage("application.entity.privilege.delete", description));
                if (baseType.isWorkEntityType()) {
                    applicationPrivilegeManager.registerPrivilege(ConfigType.STATIC, applicationId,
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
        environment().updateAll(new AppTableQuery().applicationId(applicationId).isStatic(),
                new Update().add("deprecated", Boolean.TRUE));
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
                    appTable.setLoadingFilterGen(appTableConfig.getLoadingFilterGen());
                    appTable.setLoadingSearchInput(appTableConfig.getLoadingSearchInput());
                    appTable.setSortHistory(appTableConfig.getSortHistory());
                    appTable.setItemsPerPage(appTableConfig.getItemsPerPage());
                    appTable.setSummaryTitleColumns(appTableConfig.getSummaryTitleColumns());
                    appTable.setSerialNo(appTableConfig.getSerialNo());
                    appTable.setSortable(appTableConfig.getSortable());
                    appTable.setShowLabelHeader(appTableConfig.getShowLabelHeader());
                    appTable.setHeaderToUpperCase(appTableConfig.getHeaderToUpperCase());
                    appTable.setHeaderCenterAlign(appTableConfig.getHeaderCenterAlign());
                    appTable.setBasicSearch(appTableConfig.getBasicSearch());
                    appTable.setTotalSummary(appTableConfig.getTotalSummary());
                    appTable.setHeaderless(appTableConfig.getHeaderless());
                    appTable.setMultiSelect(appTableConfig.getMultiSelect());
                    appTable.setNonConforming(appTableConfig.getNonConforming());
                    appTable.setFixedRows(appTableConfig.getFixedRows());
                    appTable.setLimitSelectToColumns(appTableConfig.getLimitSelectToColumns());
                    appTable.setDeprecated(false);
                    appTable.setConfigType(ConfigType.STATIC);
                    populateChildList(appTable, applicationName, appTableConfig, false);
                    environment().create(appTable);
                } else {
                    logDebug("Upgrading application table [{0}]...", appTableConfig.getName());
                    oldAppTable.setEntity(
                            ApplicationNameUtils.ensureLongNameReference(applicationName, appTableConfig.getEntity()));
                    oldAppTable.setDescription(description);
                    oldAppTable.setLabel(label);
                    oldAppTable.setDetailsPanelName(appTableConfig.getDetailsPanelName());
                    oldAppTable.setLoadingFilterGen(appTableConfig.getLoadingFilterGen());
                    oldAppTable.setLoadingSearchInput(appTableConfig.getLoadingSearchInput());
                    oldAppTable.setSortHistory(appTableConfig.getSortHistory());
                    oldAppTable.setItemsPerPage(appTableConfig.getItemsPerPage());
                    oldAppTable.setSummaryTitleColumns(appTableConfig.getSummaryTitleColumns());
                    oldAppTable.setSerialNo(appTableConfig.getSerialNo());
                    oldAppTable.setSortable(appTableConfig.getSortable());
                    oldAppTable.setShowLabelHeader(appTableConfig.getShowLabelHeader());
                    oldAppTable.setHeaderToUpperCase(appTableConfig.getHeaderToUpperCase());
                    oldAppTable.setHeaderCenterAlign(appTableConfig.getHeaderCenterAlign());
                    oldAppTable.setBasicSearch(appTableConfig.getBasicSearch());
                    oldAppTable.setTotalSummary(appTableConfig.getTotalSummary());
                    oldAppTable.setHeaderless(appTableConfig.getHeaderless());
                    oldAppTable.setMultiSelect(appTableConfig.getMultiSelect());
                    oldAppTable.setNonConforming(appTableConfig.getNonConforming());
                    oldAppTable.setFixedRows(appTableConfig.getFixedRows());
                    oldAppTable.setLimitSelectToColumns(appTableConfig.getLimitSelectToColumns());
                    oldAppTable.setConfigType(ConfigType.STATIC);
                    oldAppTable.setDeprecated(false);
                    populateChildList(oldAppTable, applicationName, appTableConfig, false);
                    environment().updateByIdVersion(oldAppTable);
                }
            }

            logDebug(taskMonitor, "Installed [{0}] application tables...",
                    applicationConfig.getTablesConfig().getTableList().size());
        }

        // Forms
        logDebug(taskMonitor, "Installing application forms...");
        environment().updateAll(new AppFormQuery().applicationId(applicationId).isStatic(),
                new Update().add("deprecated", Boolean.TRUE));
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
                    appForm.setLabel(appFormConfig.getLabel());
                    appForm.setHelpSheet(appFormConfig.getHelpSheet());
                    appForm.setConsolidatedReview(appFormConfig.getConsolidatedReview());
                    appForm.setConsolidatedValidation(appFormConfig.getConsolidatedValidation());
                    appForm.setConsolidatedState(appFormConfig.getConsolidatedState());
                    appForm.setListingGenerator(appFormConfig.getListingGenerator());
                    appForm.setTitleFormat(appFormConfig.getTitleFormat());
                    appForm.setName(appFormConfig.getName());
                    appForm.setDescription(description);
                    appForm.setDeprecated(false);
                    appForm.setConfigType(ConfigType.STATIC);
                    populateChildList(appForm, appFormConfig, applicationId, applicationName, false);
                    environment().create(appForm);
                } else {
                    logDebug("Upgrading application form [{0}]...", appFormConfig.getName());
                    oldAppForm.setType(appFormConfig.getType());
                    oldAppForm.setEntity(
                            ApplicationNameUtils.ensureLongNameReference(applicationName, appFormConfig.getEntity()));
                    oldAppForm.setLabel(appFormConfig.getLabel());
                    oldAppForm.setHelpSheet(appFormConfig.getHelpSheet());
                    oldAppForm.setConsolidatedReview(appFormConfig.getConsolidatedReview());
                    oldAppForm.setConsolidatedValidation(appFormConfig.getConsolidatedValidation());
                    oldAppForm.setConsolidatedState(appFormConfig.getConsolidatedState());
                    oldAppForm.setListingGenerator(appFormConfig.getListingGenerator());
                    oldAppForm.setTitleFormat(appFormConfig.getTitleFormat());
                    oldAppForm.setDescription(description);
                    oldAppForm.setConfigType(ConfigType.STATIC);
                    oldAppForm.setDeprecated(false);
                    populateChildList(oldAppForm, appFormConfig, applicationId, applicationName, false);
                    environment().updateByIdVersion(oldAppForm);
                }
            }

            logDebug(taskMonitor, "Installed [{0}] application forms...",
                    applicationConfig.getFormsConfig().getFormList().size());
        }

        // Property lists
        logDebug(taskMonitor, "Installing application property lists...");
        environment().updateAll(new AppPropertyListQuery().applicationId(applicationId).isStatic(),
                new Update().add("deprecated", Boolean.TRUE));
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
                    appPropertyList.setDeprecated(false);
                    appPropertyList.setConfigType(ConfigType.STATIC);
                    populateChildList(appPropertyList, applicationName, propertyListConfig);
                    environment().create(appPropertyList);
                } else {
                    logDebug("Upgrading application property list [{0}]...", propertyListConfig.getName());
                    oldAppPropertyList.setDescription(description);
                    oldAppPropertyList.setConfigType(ConfigType.STATIC);
                    oldAppPropertyList.setDeprecated(false);
                    populateChildList(oldAppPropertyList, applicationName, propertyListConfig);
                    environment().updateByIdVersion(oldAppPropertyList);
                }
            }

            logDebug(taskMonitor, "Installed [{0}] application property lists...",
                    applicationConfig.getPropertyListsConfig().getPropertyConfigList().size());
        }

        // Property rules
        logDebug(taskMonitor, "Installing application property rules...");
        environment().updateAll(new AppPropertyRuleQuery().applicationId(applicationId).isStatic(),
                new Update().add("deprecated", Boolean.TRUE));
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
                    appPropertyRule.setDeprecated(false);
                    appPropertyRule.setConfigType(ConfigType.STATIC);
                    populateChildList(appPropertyRule, applicationName, propertyRuleConfig, false);
                    environment().create(appPropertyRule);
                } else {
                    logDebug("Upgrading application property rule [{0}]...", propertyRuleConfig.getName());
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
                    oldAppPropertyRule.setConfigType(ConfigType.STATIC);
                    oldAppPropertyRule.setDeprecated(false);
                    populateChildList(oldAppPropertyRule, applicationName, propertyRuleConfig, false);
                    environment().updateByIdVersion(oldAppPropertyRule);
                }
            }

            logDebug(taskMonitor, "Installed [{0}] application property rules...",
                    applicationConfig.getPropertyRulesConfig().getPropertyRuleConfigList().size());
        }

        // Assignment pages
        logDebug(taskMonitor, "Installing application assignment pages...");
        environment().updateAll(new AppAssignmentPageQuery().applicationId(applicationId).isStatic(),
                new Update().add("deprecated", Boolean.TRUE));
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
                    appAssignmentPage.setDeprecated(false);
                    appAssignmentPage.setConfigType(ConfigType.STATIC);
                    environment().create(appAssignmentPage);
                } else {
                    logDebug("Upgrading application assignment page [{0}]...", appAssignmentPageConfig.getName());
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
                    oldAppAssignmentPage
                            .setAssignCaption(resolveApplicationMessage(appAssignmentPageConfig.getAssignCaption()));
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
                    oldAppAssignmentPage.setConfigType(ConfigType.STATIC);
                    oldAppAssignmentPage.setDeprecated(false);
                    environment().updateByIdVersion(oldAppAssignmentPage);
                }
            }

            logDebug(taskMonitor, "Installed [{0}] application assignment pages...",
                    applicationConfig.getAssignmentPagesConfig().getAssignmentPageList().size());
        }

        // Suggestions
        logDebug(taskMonitor, "Installing application suggestion types...");
        environment().updateAll(new AppSuggestionTypeQuery().applicationId(applicationId).isStatic(),
                new Update().add("deprecated", Boolean.TRUE));
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
                    appSuggestionType.setDeprecated(false);
                    appSuggestionType.setConfigType(ConfigType.STATIC);
                    environment().create(appSuggestionType);
                } else {
                    logDebug("Upgrading application suggestion [{0}]...", suggestionTypeConfig.getName());
                    oldSuggestionType.setDescription(description);
                    oldSuggestionType.setParent(ApplicationNameUtils.ensureLongNameReference(applicationName,
                            suggestionTypeConfig.getParent()));
                    oldSuggestionType.setDeprecated(false);
                    oldSuggestionType.setConfigType(ConfigType.STATIC);
                    environment().updateByIdVersion(oldSuggestionType);
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

    private boolean restoreApplication(final TaskMonitor taskMonitor, final ApplicationRestore applicationRestore)
            throws UnifyException {
        final AppConfig applicationConfig = applicationRestore.getApplicationConfig();
        final Optional<Long> moduleId = appletUtilities.system().getModuleId(applicationConfig.getModule());
        String description = resolveApplicationMessage(applicationConfig.getDescription());

        // Application
        Long applicationId = null;
        ConfigType appConfigType = null;
        if (environment().updateAll(new ApplicationQuery().name(applicationConfig.getName()),
                new Update().add("menuAccess", true)) == 0) {
            logDebug(taskMonitor, "Restoring application [{0}]...", description);
            Application application = new Application();
            application.setModuleId(moduleId.get());
            application.setName(applicationConfig.getName());
            application.setDescription(description);
            application.setLabel(resolveApplicationMessage(applicationConfig.getLabel()));
            application.setDisplayIndex(applicationConfig.getDisplayIndex());
            application.setDevelopable(applicationConfig.getDevelopable());
            application.setMenuAccess(applicationConfig.getMenuAccess());
            application.setAllowSecondaryTenants(applicationConfig.getAllowSecondaryTenants());
            application.setConfigType(ConfigType.CUSTOM);
            applicationId = (Long) environment().create(application);
            appConfigType = ConfigType.CUSTOM;
        } else {
            applicationId = getApplicationId(applicationConfig.getName());
            appConfigType = environment().value(ConfigType.class, "configType",
                    new ApplicationQuery().name(applicationConfig.getName()));
        }

        applicationRestore.setApplicationId(applicationId);

        final String applicationName = applicationConfig.getName();
        applicationPrivilegeManager.registerPrivilege(appConfigType, applicationId,
                ApplicationPrivilegeConstants.APPLICATION_CATEGORY_CODE,
                PrivilegeNameUtils.getApplicationPrivilegeName(applicationName), description);
        if (ApplicationModuleNameConstants.APPLICATION_APPLICATION_NAME.equals(applicationName)) {
            applicationPrivilegeManager.registerPrivilege(appConfigType, applicationId,
                    ApplicationPrivilegeConstants.APPLICATION_FEATURE_CATEGORY_CODE,
                    PrivilegeNameUtils
                            .getFeaturePrivilegeName(ApplicationFeatureConstants.SAVE_GLOBAL_TABLE_QUICK_FILTER),
                    resolveApplicationMessage("$m{application.privilege.saveglobaltablefilter}"));
        }

        // Applets
        logDebug(taskMonitor, "Restoring custom application applets...");
        if (applicationConfig.getAppletsConfig() != null
                && !DataUtils.isBlank(applicationConfig.getAppletsConfig().getAppletList())) {
            AppApplet appApplet = new AppApplet();
            appApplet.setApplicationId(applicationId);
            List<String> appletNames = new ArrayList<String>();
            for (AppletConfig appletConfig : applicationConfig.getAppletsConfig().getAppletList()) {
                appletNames.add(appletConfig.getName());
            }

            for (AppletConfig appletConfig : applicationConfig.getAppletsConfig().getAppletList()) {
                description = resolveApplicationMessage(appletConfig.getDescription());
                String label = resolveApplicationMessage(appletConfig.getLabel());
                String entity = ApplicationNameUtils.ensureLongNameReference(applicationName, appletConfig.getEntity());
                logDebug("Restoring custom application applet [{0}]. Menu access = [{1}]...", appletConfig.getName(),
                        appletConfig.getMenuAccess());
                appApplet.setId(null);
                appApplet.setName(appletConfig.getName());
                appApplet.setDescription(description);
                appApplet.setType(appletConfig.getType());
                appApplet.setEntity(entity);
                appApplet.setLabel(label);
                appApplet.setIcon(appletConfig.getIcon());
                appApplet.setRouteToApplet(appletConfig.getRouteToApplet());
                appApplet.setPath(appletConfig.getPath());
                appApplet.setMenuAccess(appletConfig.getMenuAccess());
                appApplet.setSupportOpenInNewWindow(appletConfig.getSupportOpenInNewWindow());
                appApplet.setAllowSecondaryTenants(appletConfig.getAllowSecondaryTenants());
                appApplet.setDisplayIndex(appletConfig.getDisplayIndex());
                appApplet.setBaseField(appletConfig.getBaseField());
                appApplet.setAssignField(appletConfig.getAssignField());
                appApplet.setAssignDescField(appletConfig.getAssignDescField());
                appApplet.setPseudoDeleteField(appletConfig.getPseudoDeleteField());
                appApplet.setTitleFormat(appletConfig.getTitleFormat());
                appApplet.setDeprecated(false);
                appApplet.setConfigType(ConfigType.CUSTOM);
                populateChildList(appApplet, applicationName, appletConfig, true);
                environment().create(appApplet);

                applicationPrivilegeManager
                        .registerPrivilege(ConfigType.CUSTOM, applicationId,
                                ApplicationPrivilegeConstants.APPLICATION_APPLET_CATEGORY_CODE,
                                PrivilegeNameUtils.getAppletPrivilegeName(ApplicationNameUtils
                                        .getApplicationEntityLongName(applicationName, appletConfig.getName())),
                                description);
            }

            logDebug(taskMonitor, "Restored [{0}] custom application applets...",
                    applicationConfig.getAppletsConfig().getAppletList().size());
        }

        // Enumerations
        logDebug(taskMonitor, "Restoring custom application enumerations...");
        if (applicationConfig.getEnumerationsConfig() != null
                && !DataUtils.isBlank(applicationConfig.getEnumerationsConfig().getEnumList())) {
            AppEnumeration appEnumeration = new AppEnumeration();
            appEnumeration.setApplicationId(applicationId);
            for (EnumerationConfig enumerationConfig : applicationConfig.getEnumerationsConfig().getEnumList()) {
                description = resolveApplicationMessage(enumerationConfig.getDescription());
                logDebug("Restoring application custom enumeration [{0}]...", enumerationConfig.getName());
                appEnumeration.setId(null);
                appEnumeration.setName(enumerationConfig.getName());
                appEnumeration.setDescription(resolveApplicationMessage(enumerationConfig.getDescription()));
                appEnumeration.setLabel(resolveApplicationMessage(enumerationConfig.getLabel()));
                appEnumeration.setConfigType(ConfigType.CUSTOM);
                populateChildList(appEnumeration, enumerationConfig);
                environment().create(appEnumeration);
            }

            logDebug(taskMonitor, "Restored [{0}] custom application enumerations...",
                    applicationConfig.getEnumerationsConfig().getEnumList().size());
        }

        // Widgets
        logDebug(taskMonitor, "Restoring custom application widget types...");
        if (applicationConfig.getWidgetTypesConfig() != null
                && !DataUtils.isBlank(applicationConfig.getWidgetTypesConfig().getWidgetTypeList())) {
            AppWidgetType appWidgetType = new AppWidgetType();
            appWidgetType.setApplicationId(applicationId);
            for (WidgetTypeConfig widgetTypeConfig : applicationConfig.getWidgetTypesConfig().getWidgetTypeList()) {
                description = resolveApplicationMessage(widgetTypeConfig.getDescription());
                logDebug("Restoring application custom widget [{0}]...", widgetTypeConfig.getName());
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
                appWidgetType.setDeprecated(false);
                appWidgetType.setConfigType(ConfigType.CUSTOM);
                environment().create(appWidgetType);
            }

            logDebug(taskMonitor, "Restored [{0}] custom application widgets...",
                    applicationConfig.getWidgetTypesConfig().getWidgetTypeList().size());
        }

        // References
        logDebug(taskMonitor, "Restoring custom application references...");
        if (applicationConfig.getRefsConfig() != null
                && !DataUtils.isBlank(applicationConfig.getRefsConfig().getRefList())) {
            AppRef appRef = new AppRef();
            appRef.setApplicationId(applicationId);
            for (RefConfig refConfig : applicationConfig.getRefsConfig().getRefList()) {
                description = resolveApplicationMessage(refConfig.getDescription());
                logDebug("Restoring custom application reference [{0}] ...", refConfig.getName());
                appRef.setId(null);
                appRef.setName(refConfig.getName());
                appRef.setDescription(description);
                appRef.setEntity(ApplicationNameUtils.ensureLongNameReference(applicationName, refConfig.getEntity()));
                appRef.setSearchField(refConfig.getSearchField());
                appRef.setSearchTable(refConfig.getSearchTable());
                appRef.setSelectHandler(refConfig.getSelectHandler());
                appRef.setListFormat(refConfig.getListFormat());
                appRef.setFilterGenerator(refConfig.getFilterGenerator());
                appRef.setFilterGeneratorRule(refConfig.getFilterGeneratorRule());
                appRef.setFilter(InputWidgetUtils.newAppFilter(refConfig.getFilter()));
                appRef.setDeprecated(false);
                appRef.setConfigType(ConfigType.CUSTOM);
                environment().create(appRef);
            }

            logDebug(taskMonitor, "Restored [{0}] custom application references...",
                    applicationConfig.getRefsConfig().getRefList().size());
        }

        // Entities
        logDebug(taskMonitor, "Restoring custom application entities...");
        Map<String, Long> entityIdByNameMap = new HashMap<String, Long>();
        if (applicationConfig.getEntitiesConfig() != null
                && !DataUtils.isBlank(applicationConfig.getEntitiesConfig().getEntityList())) {
            AppEntity appEntity = new AppEntity();
            appEntity.setApplicationId(applicationId);
            for (AppEntityConfig appEntityConfig : applicationConfig.getEntitiesConfig().getEntityList()) {
                description = resolveApplicationMessage(appEntityConfig.getDescription());
                String label = resolveApplicationMessage(appEntityConfig.getLabel());
                final EntityBaseType baseType = appEntityConfig.getBaseType();
                logDebug("Restoring custom application entity [{0}]...", appEntityConfig.getName());
                appEntity.setId(null);
                appEntity.setBaseType(baseType);
                appEntity.setName(appEntityConfig.getName());
                appEntity.setDescription(description);
                appEntity.setLabel(label);
                appEntity.setEmailProducerConsumer(appEntityConfig.getEmailProducerConsumer());
                appEntity.setDelegate(appEntityConfig.getDelegate());
                appEntity.setEntityClass(appEntityConfig.getType());
                appEntity.setTableName(appEntityConfig.getTable());
                appEntity.setDataSourceName(appEntityConfig.getDataSourceName());
                appEntity.setMapped(appEntityConfig.getMapped());
                appEntity.setAuditable(appEntityConfig.getAuditable());
                appEntity.setReportable(appEntityConfig.getReportable());
                appEntity.setActionPolicy(appEntityConfig.getActionPolicy());
                appEntity.setDeprecated(false);
                appEntity.setSchemaUpdateRequired(true);
                appEntity.setConfigType(ConfigType.CUSTOM);
                populateChildList(appEntity, applicationName, appEntityConfig, true);
                Long entityId = (Long) environment().create(appEntity);

                appletUtilities.sequence().ensureCachedBlockSequence(appEntityConfig.getType());

                final String entityLongName = ApplicationNameUtils.getApplicationEntityLongName(applicationName,
                        appEntityConfig.getName());
                entityIdByNameMap.put(entityLongName, entityId);
                applicationPrivilegeManager.registerPrivilege(ConfigType.CUSTOM, applicationId,
                        ApplicationPrivilegeConstants.APPLICATION_ENTITY_CATEGORY_CODE,
                        PrivilegeNameUtils.getAddPrivilegeName(entityLongName),
                        getApplicationMessage("application.entity.privilege.add", description));
                applicationPrivilegeManager.registerPrivilege(ConfigType.CUSTOM, applicationId,
                        ApplicationPrivilegeConstants.APPLICATION_ENTITY_CATEGORY_CODE,
                        PrivilegeNameUtils.getEditPrivilegeName(entityLongName),
                        getApplicationMessage("application.entity.privilege.edit", description));
                applicationPrivilegeManager.registerPrivilege(ConfigType.CUSTOM, applicationId,
                        ApplicationPrivilegeConstants.APPLICATION_ENTITY_CATEGORY_CODE,
                        PrivilegeNameUtils.getDeletePrivilegeName(entityLongName),
                        getApplicationMessage("application.entity.privilege.delete", description));
                if (baseType.isWorkEntityType()) {
                    applicationPrivilegeManager.registerPrivilege(ConfigType.CUSTOM, applicationId,
                            ApplicationPrivilegeConstants.APPLICATION_ENTITY_CATEGORY_CODE,
                            PrivilegeNameUtils.getAttachPrivilegeName(entityLongName),
                            getApplicationMessage("application.entity.privilege.attach", description));
                }
            }

            logDebug(taskMonitor, "Restored [{0}] custom application entities...",
                    applicationConfig.getEntitiesConfig().getEntityList().size());
        }

        // Tables
        logDebug(taskMonitor, "Restoring custom application tables...");
        if (applicationConfig.getTablesConfig() != null
                && !DataUtils.isBlank(applicationConfig.getTablesConfig().getTableList())) {
            AppTable appTable = new AppTable();
            appTable.setApplicationId(applicationId);
            for (AppTableConfig appTableConfig : applicationConfig.getTablesConfig().getTableList()) {
                description = resolveApplicationMessage(appTableConfig.getDescription());
                String label = resolveApplicationMessage(appTableConfig.getLabel());
                logDebug("Restoring custom application table [{0}]...", appTableConfig.getName());
                appTable.setId(null);
                appTable.setEntity(
                        ApplicationNameUtils.ensureLongNameReference(applicationName, appTableConfig.getEntity()));
                appTable.setName(appTableConfig.getName());
                appTable.setDescription(description);
                appTable.setLabel(label);
                appTable.setDetailsPanelName(appTableConfig.getDetailsPanelName());
                appTable.setLoadingFilterGen(appTableConfig.getLoadingFilterGen());
                appTable.setLoadingSearchInput(appTableConfig.getLoadingSearchInput());
                appTable.setSortHistory(appTableConfig.getSortHistory());
                appTable.setItemsPerPage(appTableConfig.getItemsPerPage());
                appTable.setSummaryTitleColumns(appTableConfig.getSummaryTitleColumns());
                appTable.setSerialNo(appTableConfig.getSerialNo());
                appTable.setSortable(appTableConfig.getSortable());
                appTable.setShowLabelHeader(appTableConfig.getShowLabelHeader());
                appTable.setHeaderToUpperCase(appTableConfig.getHeaderToUpperCase());
                appTable.setHeaderCenterAlign(appTableConfig.getHeaderCenterAlign());
                appTable.setBasicSearch(appTableConfig.getBasicSearch());
                appTable.setTotalSummary(appTableConfig.getTotalSummary());
                appTable.setHeaderless(appTableConfig.getHeaderless());
                appTable.setMultiSelect(appTableConfig.getMultiSelect());
                appTable.setNonConforming(appTableConfig.getNonConforming());
                appTable.setFixedRows(appTableConfig.getFixedRows());
                appTable.setLimitSelectToColumns(appTableConfig.getLimitSelectToColumns());
                appTable.setDeprecated(false);
                appTable.setConfigType(ConfigType.CUSTOM);
                populateChildList(appTable, applicationName, appTableConfig, true);
                environment().create(appTable);
            }

            logDebug(taskMonitor, "Restored [{0}] custom application tables...",
                    applicationConfig.getTablesConfig().getTableList().size());
        }

        // Forms
        logDebug(taskMonitor, "Restoring custom application forms...");
        if (applicationConfig.getFormsConfig() != null
                && !DataUtils.isBlank(applicationConfig.getFormsConfig().getFormList())) {
            AppForm appForm = new AppForm();
            appForm.setApplicationId(applicationId);
            for (AppFormConfig appFormConfig : applicationConfig.getFormsConfig().getFormList()) {
                description = resolveApplicationMessage(appFormConfig.getDescription());
                logDebug("Restoring custom application form [{0}]...", appFormConfig.getName());
                appForm.setId(null);
                appForm.setType(appFormConfig.getType());
                appForm.setEntity(
                        ApplicationNameUtils.ensureLongNameReference(applicationName, appFormConfig.getEntity()));
                appForm.setLabel(appFormConfig.getLabel());
                appForm.setHelpSheet(appFormConfig.getHelpSheet());
                appForm.setConsolidatedReview(appFormConfig.getConsolidatedReview());
                appForm.setConsolidatedValidation(appFormConfig.getConsolidatedValidation());
                appForm.setConsolidatedState(appFormConfig.getConsolidatedState());
                appForm.setListingGenerator(appFormConfig.getListingGenerator());
                appForm.setTitleFormat(appFormConfig.getTitleFormat());
                appForm.setName(appFormConfig.getName());
                appForm.setDescription(description);
                appForm.setDeprecated(false);
                appForm.setConfigType(ConfigType.CUSTOM);
                populateChildList(appForm, appFormConfig, applicationId, applicationName, true);
                environment().create(appForm);
            }

            logDebug(taskMonitor, "Restored [{0}] custom application forms...",
                    applicationConfig.getFormsConfig().getFormList().size());
        }

        // Property lists
        logDebug(taskMonitor, "Restoring custom application property lists...");
        if (applicationConfig.getPropertyListsConfig() != null
                && !DataUtils.isBlank(applicationConfig.getPropertyListsConfig().getPropertyConfigList())) {
            AppPropertyList appPropertyList = new AppPropertyList();
            appPropertyList.setApplicationId(applicationId);
            for (PropertyListConfig propertyListConfig : applicationConfig.getPropertyListsConfig()
                    .getPropertyConfigList()) {
                description = resolveApplicationMessage(propertyListConfig.getDescription());
                logDebug("Restoring custom application property list [{0}]...", propertyListConfig.getName());
                appPropertyList.setId(null);
                appPropertyList.setName(propertyListConfig.getName());
                appPropertyList.setDescription(description);
                appPropertyList.setDeprecated(false);
                appPropertyList.setConfigType(ConfigType.CUSTOM);
                populateChildList(appPropertyList, applicationName, propertyListConfig);
                environment().create(appPropertyList);
            }

            logDebug(taskMonitor, "Restoring [{0}] custom application property lists...",
                    applicationConfig.getPropertyListsConfig().getPropertyConfigList().size());
        }

        // Property rules
        logDebug(taskMonitor, "Restoring custom application property rules...");
        if (applicationConfig.getPropertyRulesConfig() != null
                && !DataUtils.isBlank(applicationConfig.getPropertyRulesConfig().getPropertyRuleConfigList())) {
            AppPropertyRule appPropertyRule = new AppPropertyRule();
            appPropertyRule.setApplicationId(applicationId);
            for (PropertyRuleConfig propertyRuleConfig : applicationConfig.getPropertyRulesConfig()
                    .getPropertyRuleConfigList()) {
                description = resolveApplicationMessage(propertyRuleConfig.getDescription());
                logDebug("Restoring custom application property rule [{0}]...", propertyRuleConfig.getName());
                appPropertyRule.setId(null);
                appPropertyRule.setName(propertyRuleConfig.getName());
                appPropertyRule.setDescription(description);
                appPropertyRule.setEntity(
                        ApplicationNameUtils.ensureLongNameReference(applicationName, propertyRuleConfig.getEntity()));
                appPropertyRule.setChoiceField(propertyRuleConfig.getChoiceField());
                appPropertyRule.setListField(propertyRuleConfig.getListField());
                appPropertyRule.setPropNameField(propertyRuleConfig.getPropNameField());
                appPropertyRule.setPropValField(propertyRuleConfig.getPropValField());
                appPropertyRule.setDefaultList(ApplicationNameUtils.ensureLongNameReference(applicationName,
                        propertyRuleConfig.getDefaultList()));
                appPropertyRule.setIgnoreCase(propertyRuleConfig.isIgnoreCase());
                appPropertyRule.setDeprecated(false);
                appPropertyRule.setConfigType(ConfigType.CUSTOM);
                populateChildList(appPropertyRule, applicationName, propertyRuleConfig, true);
                environment().create(appPropertyRule);
            }

            logDebug(taskMonitor, "Restored [{0}] custom application property rules...",
                    applicationConfig.getPropertyRulesConfig().getPropertyRuleConfigList().size());
        }

        // Assignment pages
        logDebug(taskMonitor, "Restoring custom application assignment pages...");
        if (applicationConfig.getAssignmentPagesConfig() != null
                && !DataUtils.isBlank(applicationConfig.getAssignmentPagesConfig().getAssignmentPageList())) {
            AppAssignmentPage appAssignmentPage = new AppAssignmentPage();
            appAssignmentPage.setApplicationId(applicationId);
            for (AppAssignmentPageConfig appAssignmentPageConfig : applicationConfig.getAssignmentPagesConfig()
                    .getAssignmentPageList()) {
                description = resolveApplicationMessage(appAssignmentPageConfig.getDescription());
                String label = resolveApplicationMessage(appAssignmentPageConfig.getLabel());
                logDebug("Restoring custom application assignment page [{0}]...", appAssignmentPageConfig.getName());
                appAssignmentPage.setId(null);
                appAssignmentPage.setName(appAssignmentPageConfig.getName());
                appAssignmentPage.setDescription(description);
                appAssignmentPage.setLabel(label);
                if (appAssignmentPageConfig.getFilterCaption1() != null) {
                    appAssignmentPage
                            .setFilterCaption1(resolveApplicationMessage(appAssignmentPageConfig.getFilterCaption1()));
                }

                if (appAssignmentPageConfig.getFilterCaption2() != null) {
                    appAssignmentPage
                            .setFilterCaption2(resolveApplicationMessage(appAssignmentPageConfig.getFilterCaption2()));
                }

                appAssignmentPage.setFilterList1(appAssignmentPageConfig.getFilterList1());
                appAssignmentPage.setFilterList2(appAssignmentPageConfig.getFilterList2());

                appAssignmentPage
                        .setAssignCaption(resolveApplicationMessage(appAssignmentPageConfig.getAssignCaption()));
                appAssignmentPage.setAssignList(appAssignmentPageConfig.getAssignList());
                appAssignmentPage
                        .setUnassignCaption(resolveApplicationMessage(appAssignmentPageConfig.getUnassignCaption()));
                appAssignmentPage.setUnassignList(appAssignmentPageConfig.getUnassignList());
                appAssignmentPage.setEntity(ApplicationNameUtils.ensureLongNameReference(applicationName,
                        appAssignmentPageConfig.getEntity()));
                appAssignmentPage.setCommitPolicy(appAssignmentPageConfig.getCommitPolicy());
                appAssignmentPage.setAssignField(appAssignmentPageConfig.getAssignField());
                appAssignmentPage.setBaseField(appAssignmentPageConfig.getBaseField());
                appAssignmentPage.setRuleDescField(appAssignmentPageConfig.getRuleDescField());
                appAssignmentPage.setDeprecated(false);
                appAssignmentPage.setConfigType(ConfigType.CUSTOM);
                environment().create(appAssignmentPage);
            }

            logDebug(taskMonitor, "Restored [{0}] custom application assignment pages...",
                    applicationConfig.getAssignmentPagesConfig().getAssignmentPageList().size());
        }

        // Suggestions
        logDebug(taskMonitor, "Restoring custom application suggestion types...");
        if (applicationConfig.getSuggestionTypesConfig() != null
                && !DataUtils.isBlank(applicationConfig.getSuggestionTypesConfig().getSuggestionTypeList())) {
            AppSuggestionType appSuggestionType = new AppSuggestionType();
            appSuggestionType.setApplicationId(applicationId);
            for (SuggestionTypeConfig suggestionTypeConfig : applicationConfig.getSuggestionTypesConfig()
                    .getSuggestionTypeList()) {
                description = resolveApplicationMessage(suggestionTypeConfig.getDescription());
                logDebug("Restoring custom application suggestion [{0}]...", suggestionTypeConfig.getName());
                appSuggestionType.setId(null);
                appSuggestionType.setName(suggestionTypeConfig.getName());
                appSuggestionType.setDescription(description);
                appSuggestionType.setParent(ApplicationNameUtils.ensureLongNameReference(applicationName,
                        suggestionTypeConfig.getParent()));
                appSuggestionType.setDeprecated(false);
                appSuggestionType.setConfigType(ConfigType.CUSTOM);
                environment().create(appSuggestionType);
            }

            logDebug(taskMonitor, "Restored [{0}] custom application suggestions...",
                    applicationConfig.getSuggestionTypesConfig().getSuggestionTypeList().size());
        }

        logDebug(taskMonitor, "Restoring other custom application artifacts...");
        if (!DataUtils.isBlank(applicationArtifactInstallerList)) {
            for (ApplicationArtifactInstaller applicationArtifactInstaller : applicationArtifactInstallerList) {
                applicationArtifactInstaller.restoreCustomApplicationArtifacts(taskMonitor, applicationRestore);
            }
        }

        return true;
    }

    private void populateChildList(AppEnumeration appEnumeration, EnumerationConfig enumerationConfig)
            throws UnifyException {
        List<AppEnumerationItem> itemList = null;
        if (!DataUtils.isBlank(enumerationConfig.getItemList())) {
            itemList = new ArrayList<AppEnumerationItem>();
            for (EnumerationItemConfig itemConfig : enumerationConfig.getItemList()) {
                AppEnumerationItem appEnumerationItem = new AppEnumerationItem();
                appEnumerationItem.setCode(itemConfig.getCode());
                appEnumerationItem.setLabel(itemConfig.getLabel());
                itemList.add(appEnumerationItem);
            }
        }

        appEnumeration.setItemList(itemList);
    }

    private void populateChildList(AppApplet appApplet, String applicationName, AppletConfig appletConfig,
            boolean restore) throws UnifyException {
        List<AppAppletProp> propList = null;
        if (!DataUtils.isBlank(appletConfig.getPropList())) {
            propList = new ArrayList<AppAppletProp>();
            Map<String, AppAppletProp> map = restore || appApplet.isIdBlank() ? Collections.emptyMap()
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

                    appAppletProp.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    propList.add(appAppletProp);
                } else {
                    if (refProperties.contains(appletPropConfig.getName())) {
                        oldAppAppletProp.setValue(ApplicationNameUtils.ensureLongNameReference(applicationName,
                                appletPropConfig.getValue()));
                    } else {
                        oldAppAppletProp.setValue(appletPropConfig.getValue());
                    }

                    oldAppAppletProp.setConfigType(ConfigType.STATIC);
                    propList.add(oldAppAppletProp);
                }
            }
        }
        appApplet.setPropList(propList);

        List<AppAppletSetValues> valuesList = null;
        if (!DataUtils.isBlank(appletConfig.getValuesList())) {
            valuesList = new ArrayList<AppAppletSetValues>();
            Map<String, AppAppletSetValues> map = restore || appApplet.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppAppletSetValuesQuery().appAppletId(appApplet.getId()));
            for (AppletSetValuesConfig appletSetValuesConfig : appletConfig.getValuesList()) {
                AppAppletSetValues oldAppAppletSetValues = map.get(appletSetValuesConfig.getName());
                String description = resolveApplicationMessage(appletSetValuesConfig.getDescription());
                if (oldAppAppletSetValues == null) {
                    AppAppletSetValues appAppletSetValues = new AppAppletSetValues();
                    appAppletSetValues.setName(appletSetValuesConfig.getName());
                    appAppletSetValues.setDescription(description);
                    appAppletSetValues.setValueGenerator(appletSetValuesConfig.getValueGenerator());
                    appAppletSetValues.setSetValues(newAppSetValues(appletSetValuesConfig.getSetValues()));
                    appAppletSetValues.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    valuesList.add(appAppletSetValues);
                } else {
                    oldAppAppletSetValues.setDescription(resolveApplicationMessage(description));
                    oldAppAppletSetValues.setValueGenerator(appletSetValuesConfig.getValueGenerator());
                    oldAppAppletSetValues.setSetValues(newAppSetValues(appletSetValuesConfig.getSetValues()));
                    oldAppAppletSetValues.setConfigType(ConfigType.STATIC);
                    valuesList.add(oldAppAppletSetValues);
                }

            }
        }

        appApplet.setSetValuesList(valuesList);

        List<AppAppletAlert> alertList = null;
        if (!DataUtils.isBlank(appletConfig.getAlertList())) {
            alertList = new ArrayList<AppAppletAlert>();
            Map<String, AppAppletAlert> map = restore || appApplet.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppAppletAlertQuery().appAppletId(appApplet.getId()));
            for (AppletAlertConfig alertConfig : appletConfig.getAlertList()) {
                AppAppletAlert oldAppAppletAlert = map.get(alertConfig.getName());
                if (oldAppAppletAlert == null) {
                    AppAppletAlert appAppletAlert = new AppAppletAlert();
                    appAppletAlert.setName(alertConfig.getName());
                    appAppletAlert.setDescription(resolveApplicationMessage(alertConfig.getDescription()));
                    appAppletAlert.setSender(alertConfig.getSender());
                    appAppletAlert.setTemplate(
                            ApplicationNameUtils.ensureLongNameReference(applicationName, alertConfig.getTemplate()));
                    appAppletAlert.setRecipientContactRule(alertConfig.getRecipientContactRule());
                    appAppletAlert.setRecipientNameRule(alertConfig.getRecipientNameRule());
                    appAppletAlert.setRecipientPolicy(alertConfig.getRecipientPolicy());
                    appAppletAlert.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    alertList.add(appAppletAlert);
                } else {
                    oldAppAppletAlert.setDescription(resolveApplicationMessage(alertConfig.getDescription()));
                    oldAppAppletAlert.setSender(alertConfig.getSender());
                    oldAppAppletAlert.setTemplate(
                            ApplicationNameUtils.ensureLongNameReference(applicationName, alertConfig.getTemplate()));
                    oldAppAppletAlert.setRecipientContactRule(alertConfig.getRecipientContactRule());
                    oldAppAppletAlert.setRecipientNameRule(alertConfig.getRecipientNameRule());
                    oldAppAppletAlert.setRecipientPolicy(alertConfig.getRecipientPolicy());
                    oldAppAppletAlert.setConfigType(ConfigType.STATIC);
                    alertList.add(oldAppAppletAlert);
                }

            }
        }

        appApplet.setAlertList(alertList);

        List<AppAppletRouteToApplet> routeToAppletList = null;
        if (!DataUtils.isBlank(appletConfig.getRouteToAppletList())) {
            routeToAppletList = new ArrayList<AppAppletRouteToApplet>();
            for (AppletRouteToAppletConfig appletRouteToAppletConfig : appletConfig.getRouteToAppletList()) {
                AppAppletRouteToApplet appAppletRouteToApplet = new AppAppletRouteToApplet();
                appAppletRouteToApplet.setRouteToApplet(appletRouteToAppletConfig.getRouteToApplet());
                routeToAppletList.add(appAppletRouteToApplet);
            }
        }

        appApplet.setRouteToAppletList(routeToAppletList);

        List<AppAppletFilter> filterList = null;
        if (!DataUtils.isBlank(appletConfig.getFilterList())) {
            filterList = new ArrayList<AppAppletFilter>();
            Map<String, AppAppletFilter> map = restore || appApplet.isIdBlank() ? Collections.emptyMap()
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
                    appAppletFilter.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    filterList.add(appAppletFilter);
                } else {
                    oldAppAppletFilter.setOwnershipType(OwnershipType.GLOBAL);
                    oldAppAppletFilter.setDescription(resolveApplicationMessage(filterConfig.getDescription()));
                    oldAppAppletFilter.setFilter(InputWidgetUtils.newAppFilter(filterConfig));
                    oldAppAppletFilter.setPreferredForm(filterConfig.getPreferredForm());
                    oldAppAppletFilter.setPreferredChildListApplet(filterConfig.getPreferredChildListApplet());
                    oldAppAppletFilter.setQuickFilter(filterConfig.isQuickFilter());
                    oldAppAppletFilter.setChildListActionType(filterConfig.getChildListActionType());
                    oldAppAppletFilter.setFilterGenerator(filterConfig.getFilterGenerator());
                    oldAppAppletFilter.setFilterGeneratorRule(filterConfig.getFilterGeneratorRule());
                    oldAppAppletFilter.setConfigType(ConfigType.STATIC);
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

    private void populateChildList(AppEntity appEntity, String applicationName, AppEntityConfig appEntityConfig,
            boolean restore) throws UnifyException {
        List<AppEntityField> fieldList = new ArrayList<AppEntityField>();
        fieldList.addAll(ApplicationEntityUtils.getEntityBaseTypeFieldList(messageResolver, appEntity.getBaseType(),
                ConfigType.STATIC));
        Map<String, AppEntityField> base = Collections.emptyMap();
        if (restore) {
            base = new HashMap<String, AppEntityField>();
            for (AppEntityField appEntityField : fieldList) {
                base.put(appEntityField.getName(), appEntityField);
            }
        }

        Map<String, AppEntityField> map = restore || appEntity.isIdBlank() ? Collections.emptyMap()
                : environment().findAllMap(String.class, "name",
                        new AppEntityFieldQuery().appEntityId(appEntity.getId()));
        if (!DataUtils.isBlank(appEntityConfig.getEntityFieldList())) {
            for (EntityFieldConfig entityFieldConfig : appEntityConfig.getEntityFieldList()) {
                if (restore) {
                    AppEntityField appEntityField = base.get(entityFieldConfig.getName());
                    if (appEntityField != null) {
                        if (!StringUtils.isBlank(entityFieldConfig.getColumnName())) {
                            appEntityField.setColumnName(entityFieldConfig.getColumnName());
                        }

                        if (!StringUtils.isBlank(entityFieldConfig.getLabel())) {
                            appEntityField.setLabel(resolveApplicationMessage(entityFieldConfig.getLabel()));
                        }

                        continue;
                    }
                }

                AppEntityField oldAppEntityField = map.remove(entityFieldConfig.getName());
                if (oldAppEntityField == null) {
                    AppEntityField appEntityField = new AppEntityField();
                    appEntityField.setDataType(entityFieldConfig.getType());
                    appEntityField.setType(restore ? EntityFieldType.CUSTOM : EntityFieldType.STATIC);
                    appEntityField.setName(entityFieldConfig.getName());
                    appEntityField.setLabel(resolveApplicationMessage(entityFieldConfig.getLabel()));
                    String references = entityFieldConfig.getReferences();
                    if (entityFieldConfig.getType().isEntityRef()
                            || (!entityFieldConfig.getType().isEnumGroup() && !StringUtils.isBlank(references))) {
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
                    appEntityField.setMapped(entityFieldConfig.getMapped());
                    appEntityField.setTextCase(entityFieldConfig.getTextCase());
                    appEntityField.setColumns(entityFieldConfig.getColumns());
                    appEntityField.setRows(entityFieldConfig.getRows());
                    appEntityField.setMinLen(entityFieldConfig.getMinLen());
                    appEntityField.setMaxLen(entityFieldConfig.getMaxLen());
                    appEntityField.setPrecision(entityFieldConfig.getPrecision());
                    appEntityField.setScale(entityFieldConfig.getScale());
                    appEntityField.setTrim(entityFieldConfig.getTrim());
                    appEntityField.setAllowNegative(entityFieldConfig.getAllowNegative());
                    appEntityField.setReadOnly(entityFieldConfig.getReadOnly());
                    appEntityField.setNullable(entityFieldConfig.getNullable());
                    appEntityField.setAuditable(entityFieldConfig.getAuditable());
                    appEntityField.setReportable(
                            entityFieldConfig.getType().isReportable() ? entityFieldConfig.getReportable() : false);
                    appEntityField.setDescriptive(entityFieldConfig.getDescriptive());
                    appEntityField.setMaintainLink(entityFieldConfig.getMaintainLink());
                    appEntityField.setBasicSearch(entityFieldConfig.getBasicSearch());
                    appEntityField.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    fieldList.add(appEntityField);
                } else {
                    oldAppEntityField.setDataType(entityFieldConfig.getType());
                    oldAppEntityField.setType(restore ? EntityFieldType.CUSTOM : EntityFieldType.STATIC);
                    oldAppEntityField.setLabel(resolveApplicationMessage(entityFieldConfig.getLabel()));
                    String references = entityFieldConfig.getReferences();
                    if (entityFieldConfig.getType().isEntityRef()
                            || (!entityFieldConfig.getType().isEnumGroup() && !StringUtils.isBlank(references))) {
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
                    oldAppEntityField.setSuggestionType(ApplicationNameUtils.ensureLongNameReference(applicationName,
                            entityFieldConfig.getSuggestionType()));
                    oldAppEntityField.setInputListKey(entityFieldConfig.getInputListKey());
                    oldAppEntityField.setLingualWidget(ApplicationNameUtils.ensureLongNameReference(applicationName,
                            entityFieldConfig.getLingualWidget()));
                    oldAppEntityField.setLingualListKey(entityFieldConfig.getLingualListKey());
                    oldAppEntityField.setAutoFormat(entityFieldConfig.getAutoFormat());
                    oldAppEntityField.setDefaultVal(entityFieldConfig.getDefaultVal());
                    oldAppEntityField.setMapped(entityFieldConfig.getMapped());
                    oldAppEntityField.setTextCase(entityFieldConfig.getTextCase());
                    oldAppEntityField.setColumns(entityFieldConfig.getColumns());
                    oldAppEntityField.setRows(entityFieldConfig.getRows());
                    oldAppEntityField.setMinLen(entityFieldConfig.getMinLen());
                    oldAppEntityField.setMaxLen(entityFieldConfig.getMaxLen());
                    oldAppEntityField.setPrecision(entityFieldConfig.getPrecision());
                    oldAppEntityField.setScale(entityFieldConfig.getScale());
                    oldAppEntityField.setTrim(entityFieldConfig.getTrim());
                    oldAppEntityField.setAllowNegative(entityFieldConfig.getAllowNegative());
                    oldAppEntityField.setReadOnly(entityFieldConfig.getReadOnly());
                    oldAppEntityField.setNullable(entityFieldConfig.getNullable());
                    oldAppEntityField.setAuditable(entityFieldConfig.getAuditable());
                    oldAppEntityField.setReportable(
                            entityFieldConfig.getType().isReportable() ? entityFieldConfig.getReportable() : false);
                    oldAppEntityField.setDescriptive(entityFieldConfig.getDescriptive());
                    oldAppEntityField.setMaintainLink(entityFieldConfig.getMaintainLink());
                    oldAppEntityField.setBasicSearch(entityFieldConfig.getBasicSearch());
                    oldAppEntityField.setConfigType(ConfigType.STATIC);
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

        List<AppEntitySeries> seriesList = null;
        if (!DataUtils.isBlank(appEntityConfig.getSeriesList())) {
            seriesList = new ArrayList<AppEntitySeries>();
            Map<String, AppEntitySeries> map2 = restore || appEntity.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppEntitySeriesQuery().appEntityId(appEntity.getId()));
            for (EntitySeriesConfig entitySeriesConfig : appEntityConfig.getSeriesList()) {
                AppEntitySeries oldAppEntitySeries = map2.remove(entitySeriesConfig.getName());
                if (oldAppEntitySeries == null) {
                    AppEntitySeries appEntitySeries = new AppEntitySeries();
                    appEntitySeries.setType(entitySeriesConfig.getType());
                    appEntitySeries.setName(entitySeriesConfig.getName());
                    appEntitySeries.setDescription(resolveApplicationMessage(entitySeriesConfig.getDescription()));
                    appEntitySeries.setLabel(resolveApplicationMessage(entitySeriesConfig.getLabel()));
                    appEntitySeries.setFieldName(entitySeriesConfig.getFieldName());
                    appEntitySeries.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    seriesList.add(appEntitySeries);
                } else {
                    oldAppEntitySeries.setType(entitySeriesConfig.getType());
                    oldAppEntitySeries.setDescription(resolveApplicationMessage(entitySeriesConfig.getDescription()));
                    oldAppEntitySeries.setLabel(resolveApplicationMessage(entitySeriesConfig.getLabel()));
                    oldAppEntitySeries.setFieldName(entitySeriesConfig.getFieldName());
                    oldAppEntitySeries.setConfigType(ConfigType.STATIC);
                    seriesList.add(oldAppEntitySeries);
                }
            }
        }

        appEntity.setSeriesList(seriesList);

        List<AppEntityCategory> categoryList = null;
        if (!DataUtils.isBlank(appEntityConfig.getCategoryList())) {
            categoryList = new ArrayList<AppEntityCategory>();
            Map<String, AppEntityCategory> map2 = restore || appEntity.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppEntityCategoryQuery().appEntityId(appEntity.getId()));
            for (EntityCategoryConfig entityCategoryConfig : appEntityConfig.getCategoryList()) {
                AppEntityCategory oldAppEntityCategory = map2.remove(entityCategoryConfig.getName());
                if (oldAppEntityCategory == null) {
                    AppEntityCategory appEntityCategory = new AppEntityCategory();
                    appEntityCategory.setName(entityCategoryConfig.getName());
                    appEntityCategory.setDescription(resolveApplicationMessage(entityCategoryConfig.getDescription()));
                    appEntityCategory.setLabel(resolveApplicationMessage(entityCategoryConfig.getLabel()));
                    appEntityCategory.setFilter(InputWidgetUtils.newAppFilter(entityCategoryConfig));
                    appEntityCategory.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    categoryList.add(appEntityCategory);
                } else {
                    oldAppEntityCategory
                            .setDescription(resolveApplicationMessage(entityCategoryConfig.getDescription()));
                    oldAppEntityCategory.setLabel(resolveApplicationMessage(entityCategoryConfig.getLabel()));
                    oldAppEntityCategory.setFilter(InputWidgetUtils.newAppFilter(entityCategoryConfig));
                    oldAppEntityCategory.setConfigType(ConfigType.STATIC);
                    categoryList.add(oldAppEntityCategory);
                }
            }
        }

        appEntity.setCategoryList(categoryList);

        List<AppEntityAttachment> attachmentList = null;
        if (!DataUtils.isBlank(appEntityConfig.getAttachmentList())) {
            attachmentList = new ArrayList<AppEntityAttachment>();
            Map<String, AppEntityAttachment> map2 = restore || appEntity.isIdBlank() ? Collections.emptyMap()
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
                    appEntityAttachment.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    attachmentList.add(appEntityAttachment);
                } else {
                    oldAppEntityAttachment.setType(entityAttachmentConfig.getType());
                    oldAppEntityAttachment
                            .setDescription(resolveApplicationMessage(entityAttachmentConfig.getDescription()));
                    oldAppEntityAttachment.setConfigType(ConfigType.STATIC);
                    attachmentList.add(oldAppEntityAttachment);
                }
            }
        }

        appEntity.setAttachmentList(attachmentList);

        List<AppEntityExpression> expressionList = null;
        if (!DataUtils.isBlank(appEntityConfig.getExpressionList())) {
            expressionList = new ArrayList<AppEntityExpression>();
            Map<String, AppEntityExpression> map2 = restore || appEntity.isIdBlank() ? Collections.emptyMap()
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
                    appEntityExpression.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    expressionList.add(appEntityExpression);
                } else {
                    oldAppEntityExpression
                            .setDescription(resolveApplicationMessage(entityExpressionConfig.getDescription()));
                    oldAppEntityExpression.setExpression(entityExpressionConfig.getExpression());
                    oldAppEntityExpression.setConfigType(ConfigType.STATIC);
                    expressionList.add(oldAppEntityExpression);
                }
            }
        }

        appEntity.setExpressionList(expressionList);

        List<AppEntityUniqueConstraint> uniqueConstraintList = null;
        if (!DataUtils.isBlank(appEntityConfig.getUniqueConstraintList())) {
            uniqueConstraintList = new ArrayList<AppEntityUniqueConstraint>();
            Map<String, AppEntityUniqueConstraint> map2 = restore || appEntity.isIdBlank() ? Collections.emptyMap()
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
                    populateChildList(appUniqueConstraint, applicationName, uniqueConstraintConfig);
                    appUniqueConstraint.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    uniqueConstraintList.add(appUniqueConstraint);
                } else {
                    oldAppEntityUniqueConstraint
                            .setDescription(resolveApplicationMessage(uniqueConstraintConfig.getDescription()));
                    oldAppEntityUniqueConstraint.setFieldList(uniqueConstraintConfig.getFieldList());
                    populateChildList(oldAppEntityUniqueConstraint, applicationName, uniqueConstraintConfig);
                    oldAppEntityUniqueConstraint.setConfigType(ConfigType.STATIC);
                    uniqueConstraintList.add(oldAppEntityUniqueConstraint);
                }

            }
        }

        appEntity.setUniqueConstraintList(uniqueConstraintList);

        List<AppEntityIndex> indexList = null;
        if (!DataUtils.isBlank(appEntityConfig.getIndexList())) {
            indexList = new ArrayList<AppEntityIndex>();
            Map<String, AppEntityIndex> map2 = restore || appEntity.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppEntityIndexQuery().appEntityId(appEntity.getId()));
            for (EntityIndexConfig indexConfig : appEntityConfig.getIndexList()) {
                AppEntityIndex oldAppEntityIndex = map2.get(indexConfig.getName());
                if (oldAppEntityIndex == null) {
                    AppEntityIndex appIndex = new AppEntityIndex();
                    appIndex.setName(indexConfig.getName());
                    appIndex.setDescription(resolveApplicationMessage(indexConfig.getDescription()));
                    appIndex.setFieldList(indexConfig.getFieldList());
                    appIndex.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    indexList.add(appIndex);
                } else {
                    oldAppEntityIndex.setDescription(resolveApplicationMessage(indexConfig.getDescription()));
                    oldAppEntityIndex.setFieldList(indexConfig.getFieldList());
                    oldAppEntityIndex.setConfigType(ConfigType.STATIC);
                    indexList.add(oldAppEntityIndex);
                }

            }
        }

        appEntity.setIndexList(indexList);

        List<AppEntityUpload> uploadList = null;
        if (!DataUtils.isBlank(appEntityConfig.getUploadList())) {
            uploadList = new ArrayList<AppEntityUpload>();
            Map<String, AppEntityUpload> map2 = restore || appEntity.isIdBlank() ? Collections.emptyMap()
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
                    appEntityUpload.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    uploadList.add(appEntityUpload);
                } else {
                    oldAppEntityUpload.setDescription(resolveApplicationMessage(uploadConfig.getDescription()));
                    oldAppEntityUpload.setConstraintAction(uploadConfig.getConstraintAction());
                    oldAppEntityUpload.setFieldSequence(newAppFieldSequence(uploadConfig.getFieldSequence()));
                    oldAppEntityUpload.setConfigType(ConfigType.STATIC);
                    uploadList.add(oldAppEntityUpload);
                }

            }
        }

        appEntity.setUploadList(uploadList);

        List<AppEntitySearchInput> searchInputList = null;
        if (!DataUtils.isBlank(appEntityConfig.getSearchInputList())) {
            searchInputList = new ArrayList<AppEntitySearchInput>();
            Map<String, AppEntitySearchInput> _emap = restore || appEntity.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppEntitySearchInputQuery().appEntityId(appEntity.getId()));
            for (EntitySearchInputConfig searchInputConfig : appEntityConfig.getSearchInputList()) {
                if (!DataUtils.isBlank(searchInputConfig.getInputList())) {
                    AppEntitySearchInput oldAppEntitySearchInput = _emap.get(searchInputConfig.getName());
                    if (oldAppEntitySearchInput == null) {
                        AppEntitySearchInput appEntitySearchInput = new AppEntitySearchInput();
                        appEntitySearchInput.setName(searchInputConfig.getName());
                        appEntitySearchInput
                                .setDescription(resolveApplicationMessage(searchInputConfig.getDescription()));
                        appEntitySearchInput.setRestrictionResolver(searchInputConfig.getRestrictionResolver());
                        appEntitySearchInput.setSearchInput(InputWidgetUtils.newAppSearchInput(searchInputConfig));
                        appEntitySearchInput.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                        searchInputList.add(appEntitySearchInput);
                    } else {
                        oldAppEntitySearchInput
                                .setDescription(resolveApplicationMessage(searchInputConfig.getDescription()));
                        oldAppEntitySearchInput.setRestrictionResolver(searchInputConfig.getRestrictionResolver());
                        oldAppEntitySearchInput.setSearchInput(InputWidgetUtils.newAppSearchInput(searchInputConfig));
                        oldAppEntitySearchInput.setConfigType(ConfigType.STATIC);
                        searchInputList.add(oldAppEntitySearchInput);
                    }
                }
            }
        }
        appEntity.setSearchInputList(searchInputList);
    }

    private void populateChildList(AppEntityUniqueConstraint appUniqueConstraint, String applicationName,
            EntityUniqueConstraintConfig uniqueConstraintConfig) throws UnifyException {
        List<AppEntityUniqueCondition> conditionList = new ArrayList<AppEntityUniqueCondition>();
        if (!DataUtils.isBlank(uniqueConstraintConfig.getConditionList())) {
            for (EntityUniqueConditionConfig entityUniqueConditionConfig : uniqueConstraintConfig.getConditionList()) {
                AppEntityUniqueCondition entityUniqueCondition = new AppEntityUniqueCondition();
                entityUniqueCondition.setField(entityUniqueConditionConfig.getField());
                entityUniqueCondition.setValue(entityUniqueConditionConfig.getValue());
                conditionList.add(entityUniqueCondition);
            }
        }

        appUniqueConstraint.setConditionList(conditionList);
    }

    private void populateChildList(AppTable appTable, String applicationName, AppTableConfig appTableConfig,
            boolean restore) throws UnifyException {
        List<AppTableColumn> columnList = new ArrayList<AppTableColumn>();
        for (TableColumnConfig tableColumnConfig : appTableConfig.getColumnList()) {
            AppTableColumn appTableColumn = new AppTableColumn();
            appTableColumn.setField(tableColumnConfig.getField());
            appTableColumn.setLabel(resolveApplicationMessage(tableColumnConfig.getLabel()));
            appTableColumn.setRenderWidget(
                    ApplicationNameUtils.ensureLongNameReference(applicationName, tableColumnConfig.getRenderWidget()));
            appTableColumn.setLinkAct(tableColumnConfig.getLinkAct());
            appTableColumn.setSymbol(tableColumnConfig.getSymbol());
            appTableColumn.setOrder(tableColumnConfig.getOrder());
            appTableColumn.setWidthRatio(tableColumnConfig.getWidthRatio());
            appTableColumn.setSwitchOnChange(tableColumnConfig.getSwitchOnChange());
            appTableColumn.setEditable(tableColumnConfig.getEditable());
            appTableColumn.setHiddenOnNull(tableColumnConfig.getHiddenOnNull());
            appTableColumn.setHidden(tableColumnConfig.getHidden());
            appTableColumn.setDisabled(tableColumnConfig.getDisabled());
            appTableColumn.setSortable(tableColumnConfig.getSortable());
            appTableColumn.setSummary(tableColumnConfig.getSummary());
            appTableColumn.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
            columnList.add(appTableColumn);
        }

        appTable.setColumnList(columnList);

        List<AppTableFilter> filterList = null;
        if (!DataUtils.isBlank(appTableConfig.getFilterList())) {
            filterList = new ArrayList<AppTableFilter>();
            Map<String, AppTableFilter> map = restore || appTable.isIdBlank() ? Collections.emptyMap()
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
                        appTableFilter.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                        filterList.add(appTableFilter);
                    } else {
                        oldAppTableFilter.setDescription(resolveApplicationMessage(filterConfig.getDescription()));
                        oldAppTableFilter.setFilterGenerator(filterConfig.getFilterGenerator());
                        oldAppTableFilter.setFilterGeneratorRule(filterConfig.getFilterGeneratorRule());
                        oldAppTableFilter.setFilter(InputWidgetUtils.newAppFilter(filterConfig));
                        oldAppTableFilter.setRowColor(filterConfig.getRowColor());
                        oldAppTableFilter.setLegendLabel(filterConfig.getLegendLabel());
                        oldAppTableFilter.setConfigType(ConfigType.STATIC);
                        filterList.add(oldAppTableFilter);
                    }
                }
            }
        }
        appTable.setFilterList(filterList);

        List<AppTableAction> actionList = null;
        if (!DataUtils.isBlank(appTableConfig.getActionList())) {
            actionList = new ArrayList<AppTableAction>();
            Map<String, AppTableAction> map = restore || appTable.isIdBlank() ? Collections.emptyMap()
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
                    appTableAction.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    actionList.add(appTableAction);
                } else {
                    oldAppTableAction.setDescription(resolveApplicationMessage(tableActionConfig.getDescription()));
                    oldAppTableAction.setLabel(resolveApplicationMessage(tableActionConfig.getLabel()));
                    oldAppTableAction.setPolicy(tableActionConfig.getPolicy());
                    oldAppTableAction.setOrderIndex(tableActionConfig.getOrderIndex());
                    oldAppTableAction.setConfigType(ConfigType.STATIC);
                    actionList.add(oldAppTableAction);
                }
            }
        }

        appTable.setActionList(actionList);

        List<AppTableLoading> loadingList = null;
        if (!DataUtils.isBlank(appTableConfig.getLoadingList())) {
            loadingList = new ArrayList<AppTableLoading>();
            Map<String, AppTableLoading> map = restore || appTable.isIdBlank() ? Collections.emptyMap()
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
                    appTableLoading.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    loadingList.add(appTableLoading);
                } else {
                    oldAppTableLoading.setDescription(resolveApplicationMessage(tableLoadingConfig.getDescription()));
                    oldAppTableLoading.setLabel(resolveApplicationMessage(tableLoadingConfig.getLabel()));
                    oldAppTableLoading.setProvider(tableLoadingConfig.getProvider());
                    oldAppTableLoading.setOrderIndex(tableLoadingConfig.getOrderIndex());
                    oldAppTableLoading.setConfigType(ConfigType.STATIC);
                    loadingList.add(oldAppTableLoading);
                }
            }
        }
        appTable.setLoadingList(loadingList);
    }

    private void populateChildList(final AppForm appForm, AppFormConfig appFormConfig, final Long applicationId,
            final String applicationName, final boolean restore) throws UnifyException {
        // Filters
        List<AppFormFilter> filterList = null;
        if (!DataUtils.isBlank(appFormConfig.getFilterList())) {
            filterList = new ArrayList<AppFormFilter>();
            Map<String, AppFormFilter> map = restore || appForm.isIdBlank() ? Collections.emptyMap()
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
                    appFormFilter.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    filterList.add(appFormFilter);
                } else {
                    oldAppFormFilter.setDescription(resolveApplicationMessage(filterConfig.getDescription()));
                    oldAppFormFilter.setFilterGenerator(filterConfig.getFilterGenerator());
                    oldAppFormFilter.setFilterGeneratorRule(filterConfig.getFilterGeneratorRule());
                    oldAppFormFilter.setFilter(InputWidgetUtils.newAppFilter(filterConfig));
                    oldAppFormFilter.setConfigType(ConfigType.STATIC);
                    filterList.add(oldAppFormFilter);
                }

            }
        }
        appForm.setFilterList(filterList);

        // Form annotations
        List<AppFormAnnotation> annotationList = null;
        if (!DataUtils.isBlank(appFormConfig.getAnnotationList())) {
            annotationList = new ArrayList<AppFormAnnotation>();
            Map<String, AppFormAnnotation> map = restore || appForm.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppFormAnnotationQuery().appFormId(appForm.getId()));
            for (FormAnnotationConfig formAnnotationConfig : appFormConfig.getAnnotationList()) {
                AppFormAnnotation oldAppFormAnnotation = map.get(formAnnotationConfig.getName());
                String description = resolveApplicationMessage(formAnnotationConfig.getDescription());
                String message = resolveApplicationMessage(formAnnotationConfig.getMessage());
                if (oldAppFormAnnotation == null) {
                    AppFormAnnotation appFormAnnotation = new AppFormAnnotation();
                    appFormAnnotation.setType(formAnnotationConfig.getType());
                    appFormAnnotation.setVisibility(formAnnotationConfig.getVisibility());
                    appFormAnnotation.setName(formAnnotationConfig.getName());
                    appFormAnnotation.setDescription(description);
                    appFormAnnotation.setMessage(message);
                    appFormAnnotation.setHtml(formAnnotationConfig.isHtml());
                    appFormAnnotation.setDirectPlacement(formAnnotationConfig.isDirectPlacement());
                    appFormAnnotation
                            .setOnCondition(InputWidgetUtils.newAppFilter(formAnnotationConfig.getOnCondition()));
                    appFormAnnotation.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    annotationList.add(appFormAnnotation);
                } else {
                    oldAppFormAnnotation.setType(formAnnotationConfig.getType());
                    oldAppFormAnnotation.setVisibility(formAnnotationConfig.getVisibility());
                    oldAppFormAnnotation.setDescription(description);
                    oldAppFormAnnotation.setMessage(message);
                    oldAppFormAnnotation.setHtml(formAnnotationConfig.isHtml());
                    oldAppFormAnnotation.setDirectPlacement(formAnnotationConfig.isDirectPlacement());
                    oldAppFormAnnotation
                            .setOnCondition(InputWidgetUtils.newAppFilter(formAnnotationConfig.getOnCondition()));
                    oldAppFormAnnotation.setConfigType(ConfigType.STATIC);
                    annotationList.add(oldAppFormAnnotation);
                }
            }
        }

        appForm.setAnnotationList(annotationList);

        // Form actions
        List<AppFormAction> actionList = null;
        if (!DataUtils.isBlank(appFormConfig.getActionList())) {
            actionList = new ArrayList<AppFormAction>();
            Map<String, AppFormAction> map = restore || appForm.isIdBlank() ? Collections.emptyMap()
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
                    appFormAction.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    actionList.add(appFormAction);
                } else {
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
                    oldAppFormAction.setOnCondition(InputWidgetUtils.newAppFilter(formActionConfig.getOnCondition()));
                    oldAppFormAction.setConfigType(ConfigType.STATIC);
                    actionList.add(oldAppFormAction);
                }

                if (!applicationPrivilegeManager.isRegisteredPrivilege(
                        ApplicationPrivilegeConstants.APPLICATION_FORMACTION_CATEGORY_CODE,
                        PrivilegeNameUtils.getFormActionPrivilegeName(formActionConfig.getName()))) {
                    applicationPrivilegeManager.registerPrivilege(restore ? ConfigType.CUSTOM : ConfigType.STATIC,
                            applicationId, ApplicationPrivilegeConstants.APPLICATION_FORMACTION_CATEGORY_CODE,
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
            appFormElement.setTabMappedForm(formTabConfig.getMappedForm());
            appFormElement.setMappedFieldName(formTabConfig.getMappedFieldName());
            appFormElement.setFilter(formTabConfig.getFilter());
            appFormElement.setEditAction(formTabConfig.getEditAction());
            appFormElement.setEditFormless(formTabConfig.getEditFormless());
            appFormElement.setEditFixedRows(formTabConfig.getEditFixedRows());
            appFormElement.setEditAllowAddition(formTabConfig.getEditAllowAddition());
            appFormElement.setIgnoreParentCondition(formTabConfig.getIgnoreParentCondition());
            appFormElement.setShowSearch(formTabConfig.getShowSearch());
            appFormElement.setQuickEdit(formTabConfig.getQuickEdit());
            appFormElement.setQuickOrder(formTabConfig.getQuickOrder());
            appFormElement.setVisible(formTabConfig.getVisible());
            appFormElement.setEditable(formTabConfig.getEditable());
            appFormElement.setDisabled(formTabConfig.getDisabled());
            appFormElement.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
            elementList.add(appFormElement);

            if (TabContentType.MINIFORM.equals(formTabConfig.getContentType())) {
                for (FormSectionConfig formSectionConfig : formTabConfig.getSectionList()) {
                    // Section
                    appFormElement = new AppFormElement();
                    appFormElement.setType(FormElementType.SECTION);
                    appFormElement.setElementName(formSectionConfig.getName());
                    appFormElement.setSectionColumns(formSectionConfig.getColumns());
                    appFormElement.setLabel(resolveApplicationMessage(formSectionConfig.getLabel()));
                    appFormElement.setPanel(formSectionConfig.getPanel());
                    appFormElement.setVisible(formSectionConfig.getVisible());
                    appFormElement.setEditable(formSectionConfig.getEditable());
                    appFormElement.setDisabled(formSectionConfig.getDisabled());
                    appFormElement.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
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
                            appFormElement.setPreviewForm(formFieldConfig.getPreviewForm());
                            appFormElement.setFieldColumn(formFieldConfig.getColumn());
                            appFormElement.setSwitchOnChange(formFieldConfig.getSwitchOnChange());
                            appFormElement.setSaveAs(formFieldConfig.getSaveAs());
                            appFormElement.setRequired(formFieldConfig.getRequired());
                            appFormElement.setVisible(formFieldConfig.getVisible());
                            appFormElement.setColor(formFieldConfig.getColor());
                            appFormElement.setEditable(formFieldConfig.getEditable());
                            appFormElement.setDisabled(formFieldConfig.getDisabled());
                            appFormElement.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
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
            Map<String, AppFormRelatedList> map = restore || appForm.isIdBlank() ? Collections.emptyMap()
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
                    appFormRelatedList.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    relatedList.add(appFormRelatedList);
                } else {
                    oldAppFormRelatedList.setDescription(resolveApplicationMessage(relatedListConfig.getDescription()));
                    oldAppFormRelatedList.setLabel(resolveApplicationMessage(relatedListConfig.getLabel()));
                    oldAppFormRelatedList.setApplet(ApplicationNameUtils.ensureLongNameReference(applicationName,
                            relatedListConfig.getApplet()));
                    oldAppFormRelatedList.setFilter(relatedListConfig.getFilter());
                    oldAppFormRelatedList.setEditAction(relatedListConfig.getEditAction());
                    oldAppFormRelatedList.setConfigType(ConfigType.STATIC);
                    relatedList.add(oldAppFormRelatedList);
                }

            }
        }

        appForm.setRelatedList(relatedList);

        // Form state policies
        List<AppFormStatePolicy> fieldStateList = null;
        if (!DataUtils.isBlank(appFormConfig.getFormStatePolicyList())) {
            fieldStateList = new ArrayList<AppFormStatePolicy>();
            Map<String, AppFormStatePolicy> map = restore || appForm.isIdBlank() ? Collections.emptyMap()
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
                    appFormStatePolicy.setExecutionIndex(formStatePolicyConfig.getExecutionIndex());
                    appFormStatePolicy
                            .setOnCondition(InputWidgetUtils.newAppFilter(formStatePolicyConfig.getOnCondition()));
                    appFormStatePolicy.setSetValues(newAppSetValues(formStatePolicyConfig.getSetValues()));
                    appFormStatePolicy.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    populateChildList(appFormStatePolicy, formStatePolicyConfig);
                    fieldStateList.add(appFormStatePolicy);
                } else {
                    oldAppFormStatePolicy.setType(formStatePolicyConfig.getType());
                    oldAppFormStatePolicy
                            .setDescription(resolveApplicationMessage(formStatePolicyConfig.getDescription()));
                    oldAppFormStatePolicy.setValueGenerator(formStatePolicyConfig.getValueGenerator());
                    oldAppFormStatePolicy.setTrigger(formStatePolicyConfig.getTrigger());
                    oldAppFormStatePolicy.setExecutionIndex(formStatePolicyConfig.getExecutionIndex());
                    oldAppFormStatePolicy
                            .setOnCondition(InputWidgetUtils.newAppFilter(formStatePolicyConfig.getOnCondition()));
                    oldAppFormStatePolicy.setSetValues(newAppSetValues(formStatePolicyConfig.getSetValues()));
                    populateChildList(oldAppFormStatePolicy, formStatePolicyConfig);
                    oldAppFormStatePolicy.setConfigType(ConfigType.STATIC);
                    fieldStateList.add(oldAppFormStatePolicy);
                }
            }
        }

        appForm.setFieldStateList(fieldStateList);

        // Form widget rule policies
        List<AppFormWidgetRulesPolicy> widgetRulesList = null;
        if (!DataUtils.isBlank(appFormConfig.getWidgetRulesPolicyList())) {
            widgetRulesList = new ArrayList<AppFormWidgetRulesPolicy>();
            Map<String, AppFormWidgetRulesPolicy> map = restore || appForm.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppFormWidgetRulesPolicyQuery().appFormId(appForm.getId()));
            for (FormWidgetRulesPolicyConfig formWidgetRulesPolicyConfig : appFormConfig.getWidgetRulesPolicyList()) {
                AppFormWidgetRulesPolicy oldAppFormWidgetRulesPolicy = map.get(formWidgetRulesPolicyConfig.getName());
                if (oldAppFormWidgetRulesPolicy == null) {
                    AppFormWidgetRulesPolicy appFormWidgetRulesPolicy = new AppFormWidgetRulesPolicy();
                    appFormWidgetRulesPolicy.setName(formWidgetRulesPolicyConfig.getName());
                    appFormWidgetRulesPolicy.setExecutionIndex(formWidgetRulesPolicyConfig.getExecutionIndex());
                    appFormWidgetRulesPolicy
                            .setDescription(resolveApplicationMessage(formWidgetRulesPolicyConfig.getDescription()));
                    appFormWidgetRulesPolicy.setOnCondition(
                            InputWidgetUtils.newAppFilter(formWidgetRulesPolicyConfig.getOnCondition()));
                    appFormWidgetRulesPolicy
                            .setWidgetRules(newAppWidgetRules(formWidgetRulesPolicyConfig.getWidgetRules()));
                    appFormWidgetRulesPolicy.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    widgetRulesList.add(appFormWidgetRulesPolicy);
                } else {
                    oldAppFormWidgetRulesPolicy.setExecutionIndex(formWidgetRulesPolicyConfig.getExecutionIndex());
                    oldAppFormWidgetRulesPolicy
                            .setDescription(resolveApplicationMessage(formWidgetRulesPolicyConfig.getDescription()));
                    oldAppFormWidgetRulesPolicy.setOnCondition(
                            InputWidgetUtils.newAppFilter(formWidgetRulesPolicyConfig.getOnCondition()));
                    oldAppFormWidgetRulesPolicy
                            .setWidgetRules(newAppWidgetRules(formWidgetRulesPolicyConfig.getWidgetRules()));
                    oldAppFormWidgetRulesPolicy.setConfigType(ConfigType.STATIC);
                    widgetRulesList.add(oldAppFormWidgetRulesPolicy);
                }
            }
        }

        appForm.setWidgetRulesList(widgetRulesList);

        // Form field validation policies
        List<AppFormFieldValidationPolicy> fieldValidationList = null;
        if (!DataUtils.isBlank(appFormConfig.getFieldValidationPolicyList())) {
            fieldValidationList = new ArrayList<AppFormFieldValidationPolicy>();
            Map<String, AppFormFieldValidationPolicy> map = restore || appForm.isIdBlank() ? Collections.emptyMap()
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
                    appFormFieldValidationPolicy.setExecutionIndex(fieldValidationPolicyConfig.getExecutionIndex());
                    appFormFieldValidationPolicy.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    fieldValidationList.add(appFormFieldValidationPolicy);
                } else {
                    oldAppFormFieldValidationPolicy
                            .setDescription(resolveApplicationMessage(fieldValidationPolicyConfig.getDescription()));
                    oldAppFormFieldValidationPolicy.setFieldName(fieldValidationPolicyConfig.getFieldName());
                    oldAppFormFieldValidationPolicy.setValidation(fieldValidationPolicyConfig.getValidator());
                    oldAppFormFieldValidationPolicy.setExecutionIndex(fieldValidationPolicyConfig.getExecutionIndex());
                    oldAppFormFieldValidationPolicy.setRule(fieldValidationPolicyConfig.getRule());
                    oldAppFormFieldValidationPolicy.setConfigType(ConfigType.STATIC);
                    fieldValidationList.add(oldAppFormFieldValidationPolicy);
                }
            }
        }

        appForm.setFieldValidationList(fieldValidationList);

        // Form validation policies
        List<AppFormValidationPolicy> formValidationList = null;
        if (!DataUtils.isBlank(appFormConfig.getFormValidationPolicyList())) {
            formValidationList = new ArrayList<AppFormValidationPolicy>();
            Map<String, AppFormValidationPolicy> map = restore || appForm.isIdBlank() ? Collections.emptyMap()
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
                    appFormValidationPolicy.setExecutionIndex(formValidationPolicyConfig.getExecutionIndex());
                    appFormValidationPolicy.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    formValidationList.add(appFormValidationPolicy);
                } else {
                    oldAppFormValidationPolicy
                            .setDescription(resolveApplicationMessage(formValidationPolicyConfig.getDescription()));
                    oldAppFormValidationPolicy.setErrorMatcher(formValidationPolicyConfig.getErrorMatcher());
                    oldAppFormValidationPolicy.setErrorCondition(
                            InputWidgetUtils.newAppFilter(formValidationPolicyConfig.getErrorCondition()));
                    oldAppFormValidationPolicy
                            .setMessage(resolveApplicationMessage(formValidationPolicyConfig.getMessage()));
                    oldAppFormValidationPolicy.setTarget(formValidationPolicyConfig.getTarget());
                    oldAppFormValidationPolicy.setExecutionIndex(formValidationPolicyConfig.getExecutionIndex());
                    oldAppFormValidationPolicy.setConfigType(ConfigType.STATIC);
                    formValidationList.add(oldAppFormValidationPolicy);
                }
            }
        }

        appForm.setFormValidationList(formValidationList);

        // Form review policies
        List<AppFormReviewPolicy> formReviewList = null;
        if (!DataUtils.isBlank(appFormConfig.getFormReviewPolicyList())) {
            formReviewList = new ArrayList<AppFormReviewPolicy>();
            Map<String, AppFormReviewPolicy> map = restore || appForm.isIdBlank() ? Collections.emptyMap()
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
                    appFormReviewPolicy.setExecutionIndex(formReviewPolicyConfig.getExecutionIndex());
                    appFormReviewPolicy.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    formReviewList.add(appFormReviewPolicy);
                } else {
                    oldAppFormReviewPolicy
                            .setDescription(resolveApplicationMessage(formReviewPolicyConfig.getDescription()));
                    oldAppFormReviewPolicy.setErrorMatcher(formReviewPolicyConfig.getErrorMatcher());
                    oldAppFormReviewPolicy.setErrorCondition(
                            InputWidgetUtils.newAppFilter(formReviewPolicyConfig.getErrorCondition()));
                    oldAppFormReviewPolicy.setMessage(resolveApplicationMessage(formReviewPolicyConfig.getMessage()));
                    oldAppFormReviewPolicy.setMessageType(formReviewPolicyConfig.getMessageType());
                    oldAppFormReviewPolicy.setFormEvents(formReviewPolicyConfig.getEvents());
                    oldAppFormReviewPolicy.setTarget(formReviewPolicyConfig.getTarget());
                    oldAppFormReviewPolicy.setSkippable(formReviewPolicyConfig.isSkippable());
                    oldAppFormReviewPolicy.setExecutionIndex(formReviewPolicyConfig.getExecutionIndex());
                    oldAppFormReviewPolicy.setConfigType(ConfigType.STATIC);
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
                appFormSetState.setConfigType(appFormStatePolicy.getConfigType());
                setStateList.add(appFormSetState);
            }
        }

        appFormStatePolicy.setSetStateList(setStateList);
    }

    private void populateChildList(AppPropertyRule appPropertyRule, String applicationName,
            PropertyRuleConfig propertyRuleConfig, boolean restore) throws UnifyException {
        List<AppPropertyRuleChoice> choiceList = Collections.emptyList();
        if (!DataUtils.isBlank(propertyRuleConfig.getChoiceList())) {
            choiceList = new ArrayList<AppPropertyRuleChoice>();
            Map<String, AppPropertyRuleChoice> map = restore || appPropertyRule.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new AppPropertyRuleChoiceQuery().appPropertyRuleId(appPropertyRule.getId()));
            for (ChoiceConfig choiceConfig : propertyRuleConfig.getChoiceList()) {
                AppPropertyRuleChoice oldAppPropertyRuleChoice = map.get(choiceConfig.getName());
                if (oldAppPropertyRuleChoice == null) {
                    AppPropertyRuleChoice appPropertyRuleChoice = new AppPropertyRuleChoice();
                    appPropertyRuleChoice.setName(choiceConfig.getName());
                    appPropertyRuleChoice.setList(
                            ApplicationNameUtils.ensureLongNameReference(applicationName, choiceConfig.getVal()));
                    appPropertyRuleChoice.setConfigType(restore ? ConfigType.CUSTOM : ConfigType.STATIC);
                    choiceList.add(appPropertyRuleChoice);
                } else {
                    oldAppPropertyRuleChoice.setList(
                            ApplicationNameUtils.ensureLongNameReference(applicationName, choiceConfig.getVal()));
                    oldAppPropertyRuleChoice.setConfigType(ConfigType.STATIC);
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
            final String entityClassName = entityClass.getName();
            unregisterDelegate(entityDef.getLongName());
            EnvironmentDelegate environmentDelegate = isComponent(entityDef.getDelegate())
                    ? (EnvironmentDelegate) getComponent(entityDef.getDelegate())
                    : (EnvironmentDelegate) getComponent(ApplicationModuleNameConstants.MISSING_ENVIRONMENT_DELEGATE);
            EnvironmentDelegateHolder delegateInfo = new EnvironmentDelegateHolder(entityDef.getLongName(),
                    entityClassName, environmentDelegate);
            delegateHolderByEntityClass.put(entityClassName, delegateInfo);
            delegateHolderByLongName.put(entityDef.getLongName(), delegateInfo);
        } else {
            unregisterDelegate(entityDef.getLongName());
        }
    }

    private void unregisterDelegate(String entityLongName) throws UnifyException {
        EnvironmentDelegateHolder delegateInfo = delegateHolderByLongName.remove(entityLongName);
        if (delegateInfo != null) {
            delegateHolderByEntityClass.remove(delegateInfo.getEntityClassName());
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
            Map<String, DynamicEntityInfo> dynamicEntityInfoMap, String basePackage, boolean extension)
            throws UnifyException {
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
                className = extension
                        ? ApplicationCodeGenUtils.generateExtensionEntityClassName(basePackage, moduleName,
                                entityDef.getName())
                        : ApplicationCodeGenUtils.generateUtilitiesEntityWrapperClassName(basePackage, moduleName,
                                entityDef.getName());
            }

            // Construct dynamic entity information and load dynamic class
            final DynamicEntityInfo.ManagedType managedType = entityDef.delegated()
                    ? DynamicEntityInfo.ManagedType.NOT_MANAGED
                    : DynamicEntityInfo.ManagedType.MANAGED;
            final boolean schemaUpdateRequired = db().value(boolean.class, "schemaUpdateRequired",
                    new AppEntityQuery().addEquals("id", entityDef.getId()));
            DynamicEntityInfo.Builder deib = DynamicEntityInfo
                    .newBuilder(dynamicEntityType, className, managedType, schemaUpdateRequired)
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
                                entityFieldDef.getDefaultVal(), entityFieldDef.isUnlinked(),
                                entityFieldDef.isNullable());
                    } else if (entityFieldDef.isChildRef()) {
                        DynamicFieldType fieldType = entityFieldDef.isCustom() ? DynamicFieldType.GENERATION
                                : DynamicFieldType.INFO_ONLY;
                        EntityDef _refEntityDef = getEntityDef(entityFieldDef.getRefDef().getEntity());
                        DynamicEntityInfo _childDynamicEntityInfo = buildDynamicEntityInfo(_refEntityDef,
                                dynamicEntityInfoMap, basePackage, extension);
                        if (entityFieldDef.isChild() || entityFieldDef.isRefFileUpload()) {
                            deib.addChildField(fieldType, _childDynamicEntityInfo, entityFieldDef.getFieldName(),
                                    entityFieldDef.isEditable());
                        } else {// Child list
                            deib.addChildListField(fieldType, _childDynamicEntityInfo, entityFieldDef.getFieldName(),
                                    entityFieldDef.isEditable());
                        }
                    } else {
                        EntityDef _refEntityDef = getEntityDef(entityFieldDef.getRefDef().getEntity());
                        DynamicEntityInfo _refDynamicEntityInfo = entityDef.getLongName()
                                .equals(_refEntityDef.getLongName()) ? DynamicEntityInfo.SELF_REFERENCE
                                        : buildDynamicEntityInfo(_refEntityDef, dynamicEntityInfoMap, basePackage,
                                                extension);
                        deib.addForeignKeyField(type, _refDynamicEntityInfo, entityFieldDef.getColumnName(),
                                entityFieldDef.getFieldName(), entityFieldDef.getDefaultVal(),
                                entityFieldDef.isUnlinked(), entityFieldDef.isNullable());
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
                                entityFieldDef.getMapping(), entityFieldDef.getDefaultVal(),
                                entityFieldDef.isNullable(), entityFieldDef.isDescriptive());
                    } else {
                        if (!entityFieldDef.isChildRef()) {
                            if (entityFieldDef.isTenantId()) {
                                deib.addTenantIdField(type, entityFieldDef.getColumnName(),
                                        entityFieldDef.getFieldName(), entityFieldDef.getMapping(),
                                        entityFieldDef.getPrecision(), entityFieldDef.getScale());
                            } else {
                                deib.addField(type, entityFieldDef.getDataType().dataType(),
                                        entityFieldDef.getColumnName(), entityFieldDef.getFieldName(),
                                        entityFieldDef.getMapping(), entityFieldDef.getDefaultVal(),
                                        entityFieldDef.getMaxLen(), entityFieldDef.getPrecision(),
                                        entityFieldDef.getScale(), entityFieldDef.isNullable(),
                                        entityFieldDef.isDescriptive());
                            }
                        }
                    }
                }
            }

            if (entityDef.isWithIndexes()) {
                for (IndexDef indexDef : entityDef.getIndexList()) {
                    deib.addIndex(indexDef.getFieldList());
                }
            }

            if (entityDef.isWithUniqueConstraints()) {
                for (UniqueConstraintDef uniqueConstraintDef : entityDef.getUniqueConstraintList()) {
                    deib.addUniqueConstraint(uniqueConstraintDef.getFieldList());
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

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

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.AppletRequestAttributeConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.AppletFilterDef;
import com.flowcentraltech.flowcentral.application.data.AppletSetValuesDef;
import com.flowcentraltech.flowcentral.application.data.ApplicationDef;
import com.flowcentraltech.flowcentral.application.data.AssignmentPageDef;
import com.flowcentraltech.flowcentral.application.data.DelegateEntityInfo;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.data.FieldSequenceDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef;
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef.FilterType;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormFieldDef;
import com.flowcentraltech.flowcentral.application.data.FormRelatedListDef;
import com.flowcentraltech.flowcentral.application.data.FormStatePolicyDef;
import com.flowcentraltech.flowcentral.application.data.FormTabDef;
import com.flowcentraltech.flowcentral.application.data.PropertyListItem;
import com.flowcentraltech.flowcentral.application.data.PropertyRuleDef;
import com.flowcentraltech.flowcentral.application.data.PropertySequenceDef;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.data.SearchInputsDef;
import com.flowcentraltech.flowcentral.application.data.SetValuesDef;
import com.flowcentraltech.flowcentral.application.data.TabSheetDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.data.UsageType;
import com.flowcentraltech.flowcentral.application.data.WidgetRulesDef;
import com.flowcentraltech.flowcentral.application.data.WidgetTypeDef;
import com.flowcentraltech.flowcentral.application.entities.AppEntity;
import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.application.listing.DetailsFormListingGenerator;
import com.flowcentraltech.flowcentral.application.listing.FormListingGenerator;
import com.flowcentraltech.flowcentral.application.listing.LetterFormListingGenerator;
import com.flowcentraltech.flowcentral.application.util.ApplicationCollaborationUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.validation.FormContextEvaluator;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.application.web.data.DetailsCase;
import com.flowcentraltech.flowcentral.application.web.data.DetailsFormListing;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.data.LetterFormListing;
import com.flowcentraltech.flowcentral.application.web.data.Summary;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.flowcentraltech.flowcentral.application.web.panels.EntityCRUD;
import com.flowcentraltech.flowcentral.application.web.panels.EntityChild;
import com.flowcentraltech.flowcentral.application.web.panels.EntityFieldSequence;
import com.flowcentraltech.flowcentral.application.web.panels.EntityFilter;
import com.flowcentraltech.flowcentral.application.web.panels.EntityParamValues;
import com.flowcentraltech.flowcentral.application.web.panels.EntityPropertySequence;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySearch;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySearchInput;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySelect;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySetValues;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySingleForm;
import com.flowcentraltech.flowcentral.application.web.panels.EntityWidgetRules;
import com.flowcentraltech.flowcentral.application.web.panels.HeaderWithTabsForm;
import com.flowcentraltech.flowcentral.application.web.panels.HeadlessTabsForm;
import com.flowcentraltech.flowcentral.application.web.panels.ListingForm;
import com.flowcentraltech.flowcentral.application.web.panels.LoadingSearch;
import com.flowcentraltech.flowcentral.application.web.panels.PropertySearch;
import com.flowcentraltech.flowcentral.application.web.panels.SingleFormBean;
import com.flowcentraltech.flowcentral.application.web.panels.UsageSearch;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractApplet;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractEntityFormApplet;
import com.flowcentraltech.flowcentral.application.web.widgets.BeanListTable;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTable;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniForm;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniFormScope;
import com.flowcentraltech.flowcentral.application.web.widgets.SectorIcon;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet.TabSheetItem;
import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.flowcentraltech.flowcentral.common.annotation.BeanBinding;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.AuditLogger;
import com.flowcentraltech.flowcentral.common.business.CollaborationProvider;
import com.flowcentraltech.flowcentral.common.business.EnvironmentDelegateRegistrar;
import com.flowcentraltech.flowcentral.common.business.EnvironmentDelegateUtilities;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.business.ReportProvider;
import com.flowcentraltech.flowcentral.common.business.SequenceCodeGenerator;
import com.flowcentraltech.flowcentral.common.business.SpecialParamProvider;
import com.flowcentraltech.flowcentral.common.business.policies.ChildListEditPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.ConsolidatedFormStatePolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.ParamConfigListProvider;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.TableSummaryLine;
import com.flowcentraltech.flowcentral.common.constants.CollaborationType;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralApplicationAttributeConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.flowcentraltech.flowcentral.common.constants.OwnershipType;
import com.flowcentraltech.flowcentral.common.constants.WfItemVersionType;
import com.flowcentraltech.flowcentral.common.data.AuditSnapshot;
import com.flowcentraltech.flowcentral.common.data.EntityAuditInfo;
import com.flowcentraltech.flowcentral.common.data.EntityAuditSnapshot;
import com.flowcentraltech.flowcentral.common.data.EntityFieldAudit;
import com.flowcentraltech.flowcentral.common.data.FormListingOptions;
import com.flowcentraltech.flowcentral.common.data.FormattedAudit;
import com.flowcentraltech.flowcentral.common.data.FormattedEntityAudit;
import com.flowcentraltech.flowcentral.common.data.FormattedFieldAudit;
import com.flowcentraltech.flowcentral.common.data.FormatterOptions;
import com.flowcentraltech.flowcentral.common.data.GenerateListingReportOptions;
import com.flowcentraltech.flowcentral.common.data.ParamValuesDef;
import com.flowcentraltech.flowcentral.common.entities.BaseVersionEntity;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.flowcentraltech.flowcentral.common.util.RestrictionUtils;
import com.flowcentraltech.flowcentral.common.web.util.EntityConfigurationUtils;
import com.flowcentraltech.flowcentral.configuration.constants.EntityChildCategoryType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.InputType;
import com.flowcentraltech.flowcentral.configuration.constants.RecordActionType;
import com.flowcentraltech.flowcentral.configuration.constants.RendererType;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyComponentConfig;
import com.tcdng.unify.core.UnifyCoreErrorConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Preferred;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.criterion.AbstractRestrictionTranslatorMapper;
import com.tcdng.unify.core.criterion.Amongst;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.criterion.RestrictionTranslator;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.Formats;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.data.MapValues;
import com.tcdng.unify.core.data.ParamConfig;
import com.tcdng.unify.core.data.ParameterizedStringGenerator;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.database.Database;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.filter.ObjectFilter;
import com.tcdng.unify.core.format.FormatHelper;
import com.tcdng.unify.core.message.MessageResolver;
import com.tcdng.unify.core.report.Report;
import com.tcdng.unify.core.report.ReportLayoutType;
import com.tcdng.unify.core.system.SequenceNumberService;
import com.tcdng.unify.core.task.TaskLauncher;
import com.tcdng.unify.core.upl.UplComponent;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.constant.ResultMappingConstants;
import com.tcdng.unify.web.font.FontSymbolManager;
import com.tcdng.unify.web.ui.PageRequestContextUtil;
import com.tcdng.unify.web.ui.widget.Panel;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;

/**
 * Applet utilities implementation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(ApplicationModuleNameConstants.APPLET_UTILITIES)
public class AppletUtilitiesImpl extends AbstractFlowCentralComponent implements AppletUtilities {

    @Configurable
    private RestrictionTranslator restrictionTranslator;

    @Configurable
    private SpecialParamProvider specialParamProvider;

    @Configurable
    private FormContextEvaluator formContextEvaluator;

    @Configurable
    private SequenceCodeGenerator sequenceCodeGenerator;

    @Configurable
    private EnvironmentService environmentService;

    @Configurable
    private AuditLogger auditLogger;

    @Configurable
    private ApplicationModuleService applicationModuleService;

    @Configurable
    private SystemModuleService systemModuleService;

    @Configurable
    private ApplicationWorkItemUtilities applicationWorkItemUtilies;

    @Configurable
    private EnvironmentDelegateUtilities environmentDelegateUtilities;

    @Configurable
    private EnvironmentDelegateRegistrar environmentDelegateRegistrar;

    @Configurable
    private FontSymbolManager fontSymbolManager;

    @Configurable
    private CollaborationProvider collaborationProvider;

    @Configurable
    private PageRequestContextUtil pageRequestContextUtil;

    @Configurable
    private FormatHelper formatHelper;

    @Configurable
    private ReportProvider reportProvider;

    @Configurable
    private MessageResolver messageResolver;

    @Configurable
    private TaskLauncher taskLauncher;

    @Configurable
    private ApplicationPrivilegeManager applicationPrivilegeManager;

    @Configurable
    private SequenceNumberService sequenceNumberService;

    private final FactoryMap<String, Class<? extends SingleFormBean>> singleFormBeanClassByPanelName;

    private final Map<String, WidgetTypeDef> adhocWidgets;

    private MappedEntityProviderInfo mappedEntityProviderInfo;

    public AppletUtilitiesImpl() {
        this.singleFormBeanClassByPanelName = new FactoryMap<String, Class<? extends SingleFormBean>>()
            {
                @SuppressWarnings("unchecked")
                @Override
                protected Class<? extends SingleFormBean> create(String panelName, Object... arg1) throws Exception {
                    try {
                        BeanBinding sa = getComponentType(panelName).getAnnotation(BeanBinding.class);
                        return (Class<? extends SingleFormBean>) sa.value();
                    } catch (Exception e) {
                        throw new RuntimeException("Get single form bean class error: panelName = " + panelName, e);
                    }
                }

            };

        this.adhocWidgets = new HashMap<String, WidgetTypeDef>();
    }

    @Override
    public Long getMappedDestTenantId(Long srcTenantId) throws UnifyException {
        return systemModuleService.getMappedDestTenantId(srcTenantId);
    }

    @Override
    public Long getUnmappedSrcTenantId(Long destTenantId) throws UnifyException {
        return systemModuleService.getUnmappedSrcTenantId(destTenantId);
    }

    @Override
    public boolean isMappingProviderPresent(Query<? extends Entity> query) throws UnifyException {
        return mappedEntityProviderInfo.isProviderPresent(query.getEntityClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends BaseMappedEntityProviderContext> MappedEntityProvider<T> getMappingProvider(
            Query<? extends Entity> query) throws UnifyException {
        return (MappedEntityProvider<T>) mappedEntityProviderInfo.getProvider(query.getEntityClass());
    }

    @Override
    public boolean isMappingProviderPresent(Class<? extends Entity> entityClass) throws UnifyException {
        return mappedEntityProviderInfo.isProviderPresent(entityClass);
    }

    @Override
    public MappedEntityProvider<? extends BaseMappedEntityProviderContext> getMappingProvider(
            Class<? extends Entity> entityClass) throws UnifyException {
        return mappedEntityProviderInfo.getProvider(entityClass);
    }

    @Override
    public String getProviderSrcEntity(String destEntity) {
        return mappedEntityProviderInfo.isProviderPresent(destEntity)
                ? mappedEntityProviderInfo.getProvider(destEntity).srcEntity()
                : null;
    }

    @Override
    public void consumeExceptionAndGenerateSilentError(Exception e, boolean tableResult) {
        try {
            logError(e);
            final String errorMsg = e instanceof UnifyException
                    ? getErrorMessage(LocaleType.SESSION, ((UnifyException) e).getUnifyError())
                    : getSessionMessage(UnifyCoreErrorConstants.COMPONENT_OPERATION_ERROR, e.getMessage());
            if (tableResult) {
                setRequestAttribute(AppletRequestAttributeConstants.SILENT_MULTIRECORD_SEARCH_ERROR_MSG, errorMsg);
            } else {
                setRequestAttribute(AppletRequestAttributeConstants.SILENT_FORM_ERROR_MSG, errorMsg);
            }
        } catch (Exception e1) {
            logError(e);
        }
    }

    @Override
    public void consumeExceptionAndGenerateHint(Exception e, String hint, Object... params) {
        try {
            logError(e);
            hintUser(MODE.ERROR, hint, params);
        } catch (UnifyException e1) {
            logError(e);
        }
    }

    @Override
    public void setReloadOnSwitch() throws UnifyException {
        applicationModuleService.setReloadOnSwitch();
    }

    @Override
    public boolean clearReloadOnSwitch() throws UnifyException {
        return applicationModuleService.clearReloadOnSwitch();
    }

    @Override
    public boolean isReloadOnSwitch() throws UnifyException {
        return applicationModuleService.isReloadOnSwitch();
    }

    @Override
    public FilterGroupDef getFilterGroupDef(String appletName, String tabFilter) throws UnifyException {
        return applicationModuleService.getFilterGroupDef(appletName, tabFilter);
    }

    @Override
    public String getTriggerWidgetId() throws UnifyException {
        return pageRequestContextUtil.getTriggerWidgetId();
    }

    @Override
    public void hintUser(String messageKey, Object... params) throws UnifyException {
        pageRequestContextUtil.hintUser(MODE.INFO, messageKey, params);
    }

    @Override
    public void hintUser(MODE mode, String messageKey, Object... params) throws UnifyException {
        pageRequestContextUtil.hintUser(mode, messageKey, params);
    }

    @Override
    public FormattedAudit formatAudit(AuditSnapshot auditSnapshot) throws UnifyException {
        return formatAudit(auditSnapshot, FormatterOptions.DEFAULT);
    }

    @Override
    public FormattedAudit formatAudit(AuditSnapshot auditSnapshot, FormatterOptions options) throws UnifyException {
        final FormatterOptions.Instance foi = options.createInstance(getUnifyComponentContext());
        final List<EntityAuditSnapshot> snapshots = auditSnapshot.getSnapshots();
        final FormattedEntityAudit[] _formattedSnapshots = new FormattedEntityAudit[snapshots.size()];
        for (int i = 0; i < _formattedSnapshots.length; i++) {
            EntityAuditSnapshot snapshot = snapshots.get(i);
            List<EntityFieldAudit> fieldAudits = snapshot.getFieldAudits();
            EntityDef entityDef = applicationModuleService.getEntityDef(snapshot.getEntity());
            FormattedFieldAudit[] fields = snapshot.getEventType().isPhantom() ? new FormattedFieldAudit[0]
                    : new FormattedFieldAudit[fieldAudits.size()];
            for (int j = 0; j < fields.length; j++) {
                EntityFieldAudit fieldAudit = fieldAudits.get(j);
                EntityFieldDef entityFieldDef = entityDef.getFieldDef(fieldAudit.getFieldName());
                EntityFieldDataType dataType = entityFieldDef.isWithResolvedTypeFieldDef()
                        ? entityFieldDef.getResolvedDataType()
                        : entityFieldDef.getDataType();
                String[] fmtOldValue = foi.format(dataType, fieldAudit.getOldValue());
                String[] fmtNewValue = foi.format(dataType, fieldAudit.getNewValue());
                fields[j] = new FormattedFieldAudit(fieldAudit.getFieldName(), fmtOldValue, fmtNewValue);
            }

            FormattedEntityAudit _formattedSnapshot = new FormattedEntityAudit(snapshot.getEventType().name(),
                    snapshot.getEntity(), fields);
            _formattedSnapshots[i] = _formattedSnapshot;
        }

        return new FormattedAudit(_formattedSnapshots);
    }

    @Override
    public EntityAuditInfo getEntityAuditInfo(String entityName) throws UnifyException {
        EntityDef entityDef = applicationModuleService.getEntityDef(entityName);
        if (entityDef.isAuditable()) {
            return new EntityAuditInfo(entityName, entityDef.getAuditFieldNames());
        }

        return new EntityAuditInfo(entityName);
    }

    @Override
    public List<String> getApplicationEntitiesLongNames(Query<? extends BaseApplicationEntity> query)
            throws UnifyException {
        if (!query.isEmptyCriteria() || query.isIgnoreEmptyCriteria()) {
            final Query<? extends BaseApplicationEntity> _query = query.copy();
            _query.addSelect("applicationName", "name", "description");
            List<? extends BaseApplicationEntity> list = environment().listAll(_query);
            return ApplicationNameUtils.getApplicationEntityLongNames(list);
        }

        return Collections.emptyList();
    }

    @Override
    public List<? extends Listable> getApplicationEntityListables(final Query<? extends BaseApplicationEntity> query)
            throws UnifyException {
        if (!query.isEmptyCriteria() || query.isIgnoreEmptyCriteria()) {
            final Query<? extends BaseApplicationEntity> _query = query.copy();
            _query.addSelect("applicationName", "name", "description");
            List<? extends BaseApplicationEntity> list = environment().listAll(_query);
            return ApplicationNameUtils.getListableList(list);
        }

        return Collections.emptyList();
    }

    @Override
    public List<? extends Listable> getEntityComponents(Class<? extends UnifyComponent> componentType, String entity,
            boolean acceptNonReferenced) throws UnifyException {
        List<UnifyComponentConfig> components = DataUtils.unmodifiableList(getComponentConfigs(componentType));
        if (!DataUtils.isBlank(components)) {
            return acceptNonReferenced
                    ? EntityConfigurationUtils.getConfigListableByEntityAcceptNonReferenced(components, entity,
                            messageResolver)
                    : EntityConfigurationUtils.getConfigListableByEntity(components, entity, messageResolver);
        }

        return Collections.emptyList();
    }

    @Override
    public ParameterizedStringGenerator getStringGenerator(ValueStoreReader paramReader, List<StringToken> tokenList)
            throws UnifyException {
        return specialParamProvider.getStringGenerator(paramReader, tokenList);
    }

    @Override
    public ParameterizedStringGenerator getStringGenerator(ValueStoreReader paramReader,
            ValueStoreReader generatorReader, List<StringToken> tokenList) throws UnifyException {
        return specialParamProvider.getStringGenerator(paramReader, generatorReader, tokenList);
    }

    @Override
    public boolean isLowLatencyRequest() throws UnifyException {
        return pageRequestContextUtil.isLowLatencyRequest();
    }

    @Override
    public int getSearchMinimumItemsPerPage() throws UnifyException {
        int minimumItemsPerPage = systemModuleService.getSysParameterValue(int.class,
                ApplicationModuleSysParamConstants.SEARCH_MINIMUM_ITEMS_PER_PAGE);
        return minimumItemsPerPage > 0 ? minimumItemsPerPage : -1;
    }

    @Override
    public SectorIcon getPageSectorIconByApplication(String applicationName) throws UnifyException {
        if (!applicationName.startsWith(ApplicationNameUtils.RESERVED_FC_PREFIX)) {
            boolean indicatePageSectors = systemModuleService.getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.SECTOR_INDICATION_ON_PAGE);
            if (indicatePageSectors) {
                ApplicationDef applicationDef = applicationModuleService.getApplicationDef(applicationName);
                return new SectorIcon(applicationDef.getSectorShortCode(), applicationDef.getSectorColor());
            }
        }

        return null;
    }

    @Override
    public boolean isEntitySearchWidget(String widgetLongName) throws UnifyException {
        return applicationModuleService.isEntitySearchWidget(widgetLongName);
    }

    @Override
    public <T> T getSysParameterValue(Class<T> clazz, String code) throws UnifyException {
        return systemModuleService.getSysParameterValue(clazz, code);
    }

    @Override
    public SpecialParamProvider specialParamProvider() {
        return specialParamProvider;
    }

    @Override
    public FormContextEvaluator formContextEvaluator() {
        return formContextEvaluator;
    }

    @Override
    public SequenceCodeGenerator sequenceCodeGenerator() {
        return sequenceCodeGenerator;
    }

    @Override
    public SequenceNumberService sequence() {
        return sequenceNumberService;
    }

    @Override
    public ReportProvider reportProvider() {
        return reportProvider;
    }

    @Override
    public String getNextSequenceCode(String ownerId, String sequenceDefintion, ValueStoreReader valueStoreReader)
            throws UnifyException {
        return sequenceCodeGenerator.getNextSequenceCode(ownerId, sequenceDefintion, valueStoreReader);
    }

    @Override
    public long getNextSequenceNumber(String sequence) throws UnifyException {
        return sequenceNumberService.getNextSequenceNumber(sequence);
    }

    @Override
    public boolean isApplicationDevelopable(Long applicationId) throws UnifyException {
        return applicationModuleService.isApplicationDevelopable(applicationId);
    }

    @Override
    public UserToken getSessionUserToken() throws UnifyException {
        return getSessionContext().getUserToken();
    }

    @Override
    public String getSessionUserLoginId() throws UnifyException {
        return getSessionContext().getUserToken().getUserLoginId();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Long> getSessionBranchScope() throws UnifyException {
        List<Long> branchIdList = (List<Long>) getSessionAttribute(FlowCentralSessionAttributeConstants.BRANCH_SCOPING);
        return branchIdList == null ? Collections.emptyList() : branchIdList;
    }

    @Override
    public Restriction getSessionBranchScopeRestriction(EntityDef entityDef) throws UnifyException {
        Restriction branchScopeRestriction = null;
        if (entityDef.isWithBranchScoping()) {
            final List<Long> branchList = getSessionBranchScope();
            if (!DataUtils.isBlank(branchList)) {
                List<EntityFieldDef> scopeFieldList = entityDef.getBranchScopingFieldDefList();
                if (scopeFieldList.size() == 1) {
                    if (branchList.size() == 1) {
                        branchScopeRestriction = new Equals(scopeFieldList.get(0).getFieldName(), branchList.get(0));
                    } else {
                        branchScopeRestriction = new Amongst(scopeFieldList.get(0).getFieldName(), branchList);
                    }
                } else {
                    And and = new And();
                    final Long branchId = branchList.size() == 1 ? branchList.get(0) : null;
                    for (EntityFieldDef entityFieldDef : scopeFieldList) {
                        if (branchId != null) {
                            and.add(new Equals(entityFieldDef.getFieldName(), branchId));
                        } else {
                            and.add(new Amongst(entityFieldDef.getFieldName(), branchList));
                        }
                    }

                    branchScopeRestriction = and;
                }
            }
        }

        return branchScopeRestriction;
    }

    @Override
    public <T> T getSessionAttribute(Class<T> clazz, String attributeName) throws UnifyException {
        return DataUtils.convert(clazz, getSessionAttribute(attributeName));
    }

    @Override
    public void setSessionAttribute(String name, Object value) throws UnifyException {
        super.setSessionAttribute(name, value);
    }

    @Override
    public Date getNow() throws UnifyException {
        return applicationModuleService.getNow();
    }

    @Override
    public Date getToday() throws UnifyException {
        return applicationModuleService.getToday();
    }

    @Override
    public <T extends UnifyComponent> T getComponent(Class<T> componentType) throws UnifyException {
        return super.getComponent(componentType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends UnifyComponent> T getComponent(Class<T> componentClazz, String componentName)
            throws UnifyException {
        return (T) getComponent(componentName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends UplComponent> T getUplComponent(Class<T> componentClazz, String descriptor)
            throws UnifyException {
        return (T) getUplComponent(getSessionLocale(), descriptor, true);
    }

    @Override
    public String getSymbolUnicode(String symbolName) throws UnifyException {
        return fontSymbolManager.resolveSymbolUnicode(symbolName);
    }

    @Override
    public String resolveSessionMessage(String message, Object... params) throws UnifyException {
        return super.resolveSessionMessage(message, params);
    }

    @Override
    public Listable getListItemByKey(String listName, String itemKey) throws UnifyException {
        return getListItemByKey(LocaleType.SESSION, listName, itemKey);
    }

    @Override
    public AuditLogger audit() {
        return auditLogger;
    }

    @Override
    public ApplicationModuleService application() {
        return applicationModuleService;
    }

    @Override
    public ApplicationPrivilegeManager applicationPrivilegeManager() {
        return applicationPrivilegeManager;
    }

    @Override
    public TaskLauncher taskLauncher() {
        return taskLauncher;
    }

    @Override
    public SystemModuleService system() {
        return systemModuleService;
    }

    @Override
    public EnvironmentService environment() {
        return environmentService;
    }

    @Override
    public ApplicationWorkItemUtilities workItemUtilities() {
        return applicationWorkItemUtilies;
    }

    @Override
    public EnvironmentDelegateUtilities delegateUtilities() {
        return environmentDelegateUtilities;
    }

    @Override
    public EnvironmentDelegateRegistrar delegateRegistrar() {
        return environmentDelegateRegistrar;
    }

    @Override
    public CollaborationProvider collaborationProvider() {
        return collaborationProvider;
    }

    @Override
    public FormatHelper formatHelper() {
        return formatHelper;
    }

    @Override
    public String translate(Restriction restriction) throws UnifyException {
        return restrictionTranslator.translate(restriction);
    }

    @Override
    public String translate(Restriction restriction, EntityDef entityDef) throws UnifyException {
        return restrictionTranslator.translate(restriction, new AuRestrictionTranslatorMapper(entityDef));
    }

    @Override
    public AppletDef getAppletDef(String appletName) throws UnifyException {
        return applicationModuleService.getAppletDef(appletName);
    }

    @Override
    public AppletDef getAppletDef(Long appAppletId) throws UnifyException {
        return applicationModuleService.getAppletDef(appAppletId);
    }

    @Override
    public EntityDef getAppletEntityDef(String appletName) throws UnifyException {
        return applicationModuleService.getAppletEntityDef(appletName);
    }

    @Override
    public EntityClassDef getAppletEntityClassDef(String appletName) throws UnifyException {
        return applicationModuleService.getAppletEntityClassDef(appletName);
    }

    @Override
    public void ensureWorkflowCopyWorkflows(String appletName, boolean forceUpdate) throws UnifyException {
        if (applicationWorkItemUtilies != null) {
            applicationWorkItemUtilies.ensureWorkflowCopyWorkflows(appletName, forceUpdate);
        }
    }

    @Override
    public void ensureWorkflowUserInteractionLoadingApplets(boolean forceUpdate) throws UnifyException {
        if (applicationWorkItemUtilies != null) {
            applicationWorkItemUtilies.ensureWorkflowUserInteractionLoadingApplets(forceUpdate);
        }
    }

    @Override
    public void ensureWorkflowUserInteractionLoadingApplet(String loadingTableName, boolean forceUpdate)
            throws UnifyException {
        if (applicationWorkItemUtilies != null) {
            applicationWorkItemUtilies.ensureWorkflowUserInteractionLoadingApplet(loadingTableName, forceUpdate);
        }
    }

    @Override
    public boolean isAppletWithWorkflowCopy(String appletName) throws UnifyException {
        return applicationModuleService.isAppletWithWorkflowCopy(appletName);
    }

    @Override
    public boolean isWorkEntityWithPendingDraft(Class<? extends WorkEntity> entityClass, Long id)
            throws UnifyException {
        return applicationModuleService.isWorkEntityWithPendingDraft(entityClass, id);
    }

    @Override
    public WidgetTypeDef getWidgetTypeDef(String widgetName) throws UnifyException {
        return adhocWidgets.containsKey(widgetName) ? adhocWidgets.get(widgetName)
                : applicationModuleService.getWidgetTypeDef(widgetName);
    }

    @Override
    public String setAdhocWidgetTypeDef(DataType dataType, InputType type, String longName, String description,
            String editor, String renderer) throws UnifyException {
        WidgetTypeDef widgetTypeDef = new WidgetTypeDef(dataType, type, longName, description, 1L, 1L, editor,
                renderer);
        adhocWidgets.put(longName, widgetTypeDef);
        return longName;
    }

    @Override
    public TableDef getTableDef(String tableName) throws UnifyException {
        return applicationModuleService.getTableDef(tableName);
    }

    @Override
    public FormDef getFormDef(String formName) throws UnifyException {
        return applicationModuleService.getFormDef(formName);
    }

    @Override
    public EntityClassDef getEntityClassDef(String entityName) throws UnifyException {
        return applicationModuleService.getEntityClassDef(entityName);
    }

    @Override
    public boolean isEntityDef(String entityName) throws UnifyException {
        return applicationModuleService.isEntityDef(entityName);
    }

    @Override
    public EntityDef getEntityDef(String entityName) throws UnifyException {
        return applicationModuleService.getEntityDef(entityName);
    }

    @Override
    public RefDef getRefDef(String refName) throws UnifyException {
        return applicationModuleService.getRefDef(refName);
    }

    @Override
    public AssignmentPageDef getAssignmentPageDef(String assignPageName) throws UnifyException {
        return applicationModuleService.getAssignmentPageDef(assignPageName);
    }

    @Override
    public PropertyRuleDef getPropertyRuleDef(String propertyRuleName) throws UnifyException {
        return applicationModuleService.getPropertyRuleDef(propertyRuleName);
    }

    @Override
    public List<PropertyListItem> getPropertyListItems(Entity entityInst, String childFkFieldName,
            PropertyRuleDef propertyRuleDef) throws UnifyException {
        return applicationModuleService.getPropertyListItems(entityInst, childFkFieldName, propertyRuleDef);
    }

    @Override
    public MapValues getPropertyListValues(Entity entityInst, String childFkFieldName, PropertyRuleDef propertyRuleDef)
            throws UnifyException {
        return applicationModuleService.getPropertyListValues(entityInst, childFkFieldName, propertyRuleDef);
    }

    @Override
    public void savePropertyListValues(SweepingCommitPolicy sweepingCommitPolicy, Entity entityInst,
            String childFkFieldName, PropertyRuleDef propertyRuleDef, MapValues values) throws UnifyException {
        applicationModuleService.savePropertyListValues(sweepingCommitPolicy, entityInst, childFkFieldName,
                propertyRuleDef, values);
    }

    @Override
    public FilterDef retrieveFilterDef(String category, String ownerEntityName, Long ownerInstId,
            String filterGenerator) throws UnifyException {
        return applicationModuleService.retrieveFilterDef(category, ownerEntityName, ownerInstId, filterGenerator);
    }

    @Override
    public void saveFilterDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, FilterDef filterDef) throws UnifyException {
        applicationModuleService.saveFilterDef(sweepingCommitPolicy, category, ownerEntityName, ownerInstId, filterDef);
    }

    @Override
    public void saveAppletQuickFilterDef(SweepingCommitPolicy sweepingCommitPolicy, Long appAppletId, String name,
            String description, OwnershipType ownershipType, FilterDef filterDef) throws UnifyException {
        applicationModuleService.saveAppletQuickFilterDef(sweepingCommitPolicy, appAppletId, name, description,
                ownershipType, filterDef);
    }

    @Override
    public void commandShowPopup(Panel panel) throws UnifyException {
        pageRequestContextUtil.setRequestPopupPanel(panel);
        pageRequestContextUtil.setCommandResultMapping(ResultMappingConstants.SHOW_POPUP);
    }

    @Override
    public void commandShowPopup(String panelName) throws UnifyException {
        pageRequestContextUtil.setRequestPopupName(panelName);
        pageRequestContextUtil.setCommandResultMapping(ResultMappingConstants.SHOW_POPUP);
    }

    @Override
    public void commandHidePopup() throws UnifyException {
        pageRequestContextUtil.setCommandResultMapping(ResultMappingConstants.HIDE_POPUP);
    }

    @Override
    public void commandRefreshPanels(String... panelLongName) throws UnifyException {
        pageRequestContextUtil.setResponseRefreshPanels(panelLongName);
        pageRequestContextUtil.setCommandResultMapping(ResultMappingConstants.REFRESH_PANELS);
    }

    @Override
    public void commandRefreshPanels(Panel... panels) throws UnifyException {
        if (pageRequestContextUtil.setResponseRefreshPanels(panels)) {
            pageRequestContextUtil.setCommandResultMapping(ResultMappingConstants.REFRESH_PANELS);
        }
    }

    @Override
    public void commandRefreshPanelsAndHidePopup(String... panelLongName) throws UnifyException {
        pageRequestContextUtil.setResponseRefreshPanels(panelLongName);
        pageRequestContextUtil.setCommandResultMapping(ResultMappingConstants.REFRESH_HIDE_POPUP);
    }

    @Override
    public void commandRefreshPanelsAndHidePopup(Panel... panels) throws UnifyException {
        pageRequestContextUtil.setResponseRefreshPanels(panels);
        pageRequestContextUtil.setCommandResultMapping(ResultMappingConstants.REFRESH_HIDE_POPUP);
    }

    @Override
    public FieldSequenceDef retrieveFieldSequenceDef(String category, String ownerEntityName, Long ownerInstId)
            throws UnifyException {
        return applicationModuleService.retrieveFieldSequenceDef(category, ownerEntityName, ownerInstId);
    }

    @Override
    public PropertySequenceDef retrieveSequenceDef(String category, String ownerEntityName, Long ownerInstId)
            throws UnifyException {
        return applicationModuleService.retrieveSequenceDef(category, ownerEntityName, ownerInstId);
    }

    @Override
    public void saveFieldSequenceDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, FieldSequenceDef fieldSequenceDef) throws UnifyException {
        applicationModuleService.saveFieldSequenceDef(sweepingCommitPolicy, category, ownerEntityName, ownerInstId,
                fieldSequenceDef);
    }

    @Override
    public void saveSequenceDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, PropertySequenceDef propertySequenceDef) throws UnifyException {
        applicationModuleService.saveSequenceDef(sweepingCommitPolicy, category, ownerEntityName, ownerInstId,
                propertySequenceDef);
    }

    @Override
    public WidgetRulesDef retrieveWidgetRulesDef(String category, String ownerEntityName, Long ownerInstId)
            throws UnifyException {
        return applicationModuleService.retrieveWidgetRulesDef(category, ownerEntityName, ownerInstId);
    }

    @Override
    public void saveWidgetRulesDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, WidgetRulesDef widgetRulesDef) throws UnifyException {
        applicationModuleService.saveWidgetRulesDef(sweepingCommitPolicy, category, ownerEntityName, ownerInstId,
                widgetRulesDef);
    }

    @Override
    public SetValuesDef retrieveSetValuesDef(String category, String ownerEntityName, Long ownerInstId)
            throws UnifyException {
        return applicationModuleService.retrieveSetValuesDef(category, ownerEntityName, ownerInstId);
    }

    @Override
    public void saveSetValuesDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, SetValuesDef setValuesDef) throws UnifyException {
        applicationModuleService.saveSetValuesDef(sweepingCommitPolicy, category, ownerEntityName, ownerInstId,
                setValuesDef);
    }

    @Override
    public ParamValuesDef retrieveParamValuesDef(String category, String ownerEntityName, Long ownerInstId,
            List<ParamConfig> paramConfigList) throws UnifyException {
        return applicationModuleService.retrieveParamValuesDef(category, ownerEntityName, ownerInstId, paramConfigList);
    }

    @Override
    public void saveParamValuesDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, ParamValuesDef paramValuesDef) throws UnifyException {
        applicationModuleService.saveParamValuesDef(sweepingCommitPolicy, category, ownerEntityName, ownerInstId,
                paramValuesDef);
    }

    @Override
    public SearchInputsDef retrieveSearchInputsDef(String category, String ownerEntityName, Long ownerInstId)
            throws UnifyException {
        return applicationModuleService.retrieveSearchInputsDef(category, ownerEntityName, ownerInstId);
    }

    @Override
    public void saveSearchInputsDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, SearchInputsDef searchInputsDef) throws UnifyException {
        applicationModuleService.saveSearchInputsDef(sweepingCommitPolicy, category, ownerEntityName, ownerInstId,
                searchInputsDef);
    }

    @Override
    public ListingForm constructListingForm(AbstractApplet applet, String rootTitle, String beanTitle, FormDef formDef,
            Entity inst, BreadCrumbs breadCrumbs) throws UnifyException {
        logDebug("Constructing listing form for bean [{0}] using form definition [{1}]...", beanTitle,
                formDef.getLongName());
        final AppletContext appletContext = applet != null ? applet.getCtx() : new AppletContext(null, applet, this);
        final FormContext formContext = new FormContext(appletContext, formDef, null, inst);
        final SectorIcon sectorIcon = applet != null
                ? getPageSectorIconByApplication(applet.getRootAppletDef().getApplicationName())
                : null;
        final ListingForm form = new ListingForm(formContext, sectorIcon, breadCrumbs);
        form.setBeanTitle(beanTitle);
        form.setFormMode(FormMode.LISTING);
        return form;
    }

    @SuppressWarnings("unchecked")
    @Override
    public HeaderWithTabsForm constructHeaderWithTabsForm(AbstractEntityFormApplet applet, String rootTitle,
            String beanTitle, FormDef formDef, Entity inst, FormMode formMode, BreadCrumbs breadCrumbs,
            EntityFormEventHandlers formEventHandlers) throws UnifyException {
        logDebug("Constructing header with tabs form for bean [{0}] using form definition [{1}]...", beanTitle,
                formDef.getLongName());
        final AppletContext appletContext = applet != null ? applet.getCtx() : new AppletContext(null, applet, this);
        final SweepingCommitPolicy sweepingCommitPolicy = applet;
        final FormContext formContext = new FormContext(appletContext, formDef, formEventHandlers, inst);
        final SectorIcon sectorIcon = applet != null
                ? getPageSectorIconByApplication(applet.getRootAppletDef().getApplicationName())
                : null;
        final HeaderWithTabsForm form = new HeaderWithTabsForm(formContext, sectorIcon, breadCrumbs);
        final Date now = getNow();
        form.setBeanTitle(beanTitle);
        form.setFormMode(formMode);
        if (applet != null && applet.isCollaboration() && applet.isNoForm()) {
            setCollaborationContext(form);
        }

        final AppletDef _formAppletDef = applet != null ? applet.getFormAppletDef() : null;
        if (_formAppletDef != null && !_formAppletDef.isStudioComponent()) {
            boolean conditionalDisabled = !formBeanMatchAppletPropertyCondition(_formAppletDef, form,
                    AppletPropertyConstants.MAINTAIN_FORM_UPDATE_CONDITION);
            formContext.setConditionalDisabled(conditionalDisabled);
        }

        // Tabs
        final EntityDef entityDef = formDef.getEntityDef();
        final boolean isCreateMode = formMode.isCreate();
        if (formDef.getTabCount() > 1) {
            TabSheetDef.Builder tsdb = TabSheetDef.newBuilder(formContext, 1L);
            List<TabSheetItem> tabSheetItemList = new ArrayList<TabSheetItem>();
            final int len = formDef.getTabCount();
            formContext.evaluateTabStates();
            for (int tabIndex = 1; tabIndex < len; tabIndex++) {
                FormTabDef formTabDef = formDef.getFormTabDef(tabIndex);
                switch (formTabDef.getContentType()) {
                    case MINIFORM:
                    case MINIFORM_CHANGELOG: {
                        logDebug("Constructing mini form for tab [{0}]...", formTabDef.getName());
                        tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "!fc-miniform",
                                RendererType.SIMPLE_WIDGET);
                        tabSheetItemList.add(new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(),
                                new MiniForm(MiniFormScope.CHILD_FORM, formContext, formTabDef), tabIndex,
                                formContext.getFormTab(formTabDef.getName()).isVisible()));
                    }
                        break;
                    case MINIFORM_MAPPED: {
                        logDebug("Constructing mini form for tab [{0}]...", formTabDef.getName());
                        tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "!fc-miniform",
                                RendererType.SIMPLE_WIDGET);
                        final String _mappedFieldName = formTabDef.getMappedFieldName();
                        final FormDef _mappedFormDef = getFormDef(formTabDef.getMappedForm());
                        final Long _mappedInstId = DataUtils.getBeanProperty(Long.class, inst, _mappedFieldName);
                        EntityClassDef mappedEntityClassDef = getEntityClassDef(
                                _mappedFormDef.getEntityDef().getLongName());
                        Entity _mappedInst = _mappedInstId != null
                                ? environment().list((Class<? extends Entity>) mappedEntityClassDef.getEntityClass(),
                                        _mappedInstId)
                                : (Entity) ReflectUtils.newInstance(mappedEntityClassDef.getEntityClass());
                        FormContext _mappedFormContext = new FormContext(appletContext, _mappedFormDef,
                                formEventHandlers, _mappedInst);
                        tabSheetItemList.add(new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(),
                                new MiniForm(MiniFormScope.CHILD_FORM, _mappedFormContext,
                                        _mappedFormDef.getFormTabDef(0)),
                                tabIndex, formContext.getFormTab(formTabDef.getName()).isVisible()));
                    }
                        break;
                    case PROPERTY_LIST: {
                        logDebug("Constructing property list tab [{0}] using applet [{1}]...", formTabDef.getName(),
                                formTabDef.getApplet());
                        AppletDef _appletDef = getAppletDef(formTabDef.getApplet());
                        String childFkFieldName = getChildFkFieldName(entityDef, formTabDef.getReference());
                        PropertySearch _propertySearch = constructPropertySearch(formContext, sweepingCommitPolicy,
                                formTabDef.getName(), inst, _appletDef, formTabDef.isIgnoreParentCondition());
                        _propertySearch.setChildTabIndex(tabIndex);
                        _propertySearch.applyEntityToSearch(inst, childFkFieldName);

                        tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "fc-propertylistpanel",
                                RendererType.STANDALONE_PANEL);
                        tabSheetItemList.add(new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(),
                                _propertySearch, tabIndex,
                                !isCreateMode && formContext.getFormTab(formTabDef.getName()).isVisible()));
                    }
                        break;
                    case USAGE_LIST: {
                        logDebug("Constructing usage list tab [{0}] using applet [{1}]...", formTabDef.getName(),
                                formTabDef.getApplet());
                        final String usageListProvider = formTabDef.getReference();
                        UsageSearch _usageSearch = new UsageSearch(formContext, sweepingCommitPolicy,
                                formTabDef.getName(), usageListProvider, 0, true);
                        _usageSearch.setChildTabIndex(tabIndex);
                        _usageSearch.setSearchUsageType(UsageType.ENTITY);
                        _usageSearch.setPaginationLabel(resolveSessionMessage("$m{usagesearch.display.label}"));
                        _usageSearch.applyEntityToSearch(inst);

                        tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "fc-usagelistpanel",
                                RendererType.STANDALONE_PANEL);
                        tabSheetItemList.add(
                                new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(), _usageSearch, tabIndex,
                                        !isCreateMode && formContext.getFormTab(formTabDef.getName()).isVisible()));
                    }
                        break;
                    case CHILD: {
                        logDebug("Constructing child tab [{0}] using applet [{1}]...", formTabDef.getName(),
                                formTabDef.getApplet());
                        AppletDef _appletDef = getAppletDef(formTabDef.getApplet());
                        EntityChild _entityChild = constructEntityChild(formContext, sweepingCommitPolicy,
                                formTabDef.getName(), rootTitle, _appletDef, formTabDef.isQuickEdit(),
                                formTabDef.isIgnoreParentCondition());
                        final boolean canCreate = _appletDef.getPropValue(boolean.class,
                                AppletPropertyConstants.SEARCH_TABLE_NEW);
                        _entityChild.setCanCreate(canCreate);
                        Restriction childRestriction = getChildRestriction(entityDef, formTabDef.getReference(), inst);
                        _entityChild.setChildTabIndex(tabIndex);
                        _entityChild.load(formContext, childRestriction);
                        tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "fc-childpanel",
                                RendererType.STANDALONE_PANEL);
                        tabSheetItemList.add(
                                new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(), _entityChild, tabIndex,
                                        !isCreateMode && formContext.getFormTab(formTabDef.getName()).isVisible()));
                    }
                        break;
                    case CHILD_LIST: {
                        logDebug("Constructing child list tab [{0}] using applet [{1}]...", formTabDef.getName(),
                                formTabDef.getApplet());
                        AppletDef _appletDef = getAppletDef(formTabDef.getApplet());
                        final boolean newButtonVisible = applet == null
                                || !hideAddActionButton(form, applet.getFormAppletDef(), formTabDef.getApplet());
                        final String editAction = formTabDef.getEditAction() == null ? "/assignToChildItem"
                                : formTabDef.getEditAction();
                        int mode = formTabDef.isShowSearch() ? EntitySearch.ENABLE_ALL
                                : EntitySearch.ENABLE_ALL & ~EntitySearch.SHOW_SEARCH;
                        if (formTabDef.isQuickEdit()) {
                            mode |= EntitySearch.SHOW_QUICK_EDIT;
                        }

                        if (formTabDef.isQuickOrder()) {
                            mode |= EntitySearch.SHOW_QUICK_ORDER;
                        }

                        final boolean isIgnoreReport = true;
                        EntitySearch _entitySearch = constructEntitySearch(formContext, sweepingCommitPolicy,
                                formTabDef.getName(), rootTitle, _appletDef, editAction, mode, isIgnoreReport,
                                formTabDef.isIgnoreParentCondition());
                        _entitySearch.setNewButtonVisible(newButtonVisible);
                        if (_appletDef.isPropWithValue(AppletPropertyConstants.BASE_RESTRICTION)) {
                            AppletFilterDef appletFilterDef = _appletDef.getFilterDef(
                                    _appletDef.getPropValue(String.class, AppletPropertyConstants.BASE_RESTRICTION));
                            _entitySearch.setBaseFilter(appletFilterDef != null ? appletFilterDef.getFilterDef() : null,
                                    specialParamProvider);
                        }

                        Restriction childRestriction = getChildRestriction(entityDef, formTabDef.getReference(), inst);
                        Restriction tabRestriction = formTabDef.getRestriction(FilterType.TAB,
                                form.getFormValueStoreReader(), now);
                        childRestriction = RestrictionUtils.and(childRestriction, tabRestriction);

                        _entitySearch.setChildTabIndex(tabIndex);
                        _entitySearch.setRelatedList(formTabDef.getApplet());
                        _entitySearch.setBaseRestriction(childRestriction, specialParamProvider);
                        _entitySearch.applyFilterToSearch();

                        tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "fc-childlistpanel",
                                RendererType.STANDALONE_PANEL);
                        tabSheetItemList.add(
                                new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(), _entitySearch, tabIndex,
                                        !isCreateMode && formContext.getFormTab(formTabDef.getName()).isVisible()));
                    }
                        break;
                    case FILTER_CONDITION: {
                        logDebug("Constructing filter condition tab [{0}] using reference [{1}]...",
                                formTabDef.getName(), formTabDef.getReference());
                        EntityChildCategoryType categoryType = EntityChildCategoryType
                                .fromName(formTabDef.getReference());
                        EntityDef _entityDef = getEntityDef(appletContext.getReference(categoryType));
                        EntityFilter _entityFilter = constructEntityFilter(formContext, sweepingCommitPolicy,
                                formTabDef.getName(), formDef.getEntityDef(), EntityFilter.ENABLE_ALL,
                                formTabDef.isIgnoreParentCondition());
                        _entityFilter.setListType(categoryType.listType());
                        _entityFilter.setParamList(categoryType.paramList());
                        _entityFilter.setCategory(categoryType.category());
                        _entityFilter.setOwnerInstId((Long) inst.getId());
                        _entityFilter.load(_entityDef);
                        tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "fc-entityfilterpanel",
                                RendererType.STANDALONE_PANEL);
                        tabSheetItemList.add(
                                new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(), _entityFilter, tabIndex,
                                        !isCreateMode && formContext.getFormTab(formTabDef.getName()).isVisible()));
                    }
                        break;
                    case SEARCH_INPUT: {
                        logDebug("Constructing search input tab [{0}] using reference [{1}]...", formTabDef.getName(),
                                formTabDef.getReference());
                        EntityChildCategoryType categoryType = EntityChildCategoryType
                                .fromName(formTabDef.getReference());
                        EntityDef _entityDef = getEntityDef(appletContext.getReference(categoryType));
                        EntitySearchInput _entitySearchInput = constructEntitySearchInput(formContext,
                                sweepingCommitPolicy, formTabDef.getName(), formDef.getEntityDef(),
                                EntitySearchInput.ENABLE_ALL, formTabDef.isIgnoreParentCondition());
                        _entitySearchInput.setCategory(categoryType.category());
                        _entitySearchInput.setOwnerInstId((Long) inst.getId());
                        _entitySearchInput.load(_entityDef);
                        tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "fc-entitysearchinputpanel",
                                RendererType.STANDALONE_PANEL);
                        tabSheetItemList.add(new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(),
                                _entitySearchInput, tabIndex,
                                !isCreateMode && formContext.getFormTab(formTabDef.getName()).isVisible()));
                    }
                        break;
                    case PARAM_VALUES: {
                        logDebug("Constructing parameter values tab [{0}] using reference [{1}]...",
                                formTabDef.getName(), formTabDef.getReference());
                        ParamConfigListProvider pclProvider = (ParamConfigListProvider) getComponent(
                                formTabDef.getReference());
                        EntityParamValues _entityParamValues = constructEntityParamValues(formContext,
                                sweepingCommitPolicy, formTabDef.getName(), formDef.getEntityDef(),
                                EntityParamValues.ENABLE_ALL, formTabDef.isIgnoreParentCondition());
                        _entityParamValues.setCategory(pclProvider.getCategory(inst));
                        _entityParamValues.setOwnerInstId((Long) inst.getId());
                        _entityParamValues.load(pclProvider.getParamConfigList(inst));
                        tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "fc-entityparamvaluespanel",
                                RendererType.STANDALONE_PANEL);
                        tabSheetItemList.add(new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(),
                                _entityParamValues, tabIndex,
                                !isCreateMode && formContext.getFormTab(formTabDef.getName()).isVisible()));
                    }
                        break;
                    case FIELD_SEQUENCE: {
                        logDebug("Constructing field sequence tab [{0}] using reference [{1}]...", formTabDef.getName(),
                                formTabDef.getReference());
                        EntityChildCategoryType categoryType = EntityChildCategoryType
                                .fromName(formTabDef.getReference());
                        EntityDef _entityDef = getEntityDef(appletContext.getReference(categoryType));
                        EntityFieldSequence _entityFieldSequence = constructEntityFieldSequence(formContext,
                                sweepingCommitPolicy, formTabDef.getName(), formDef.getEntityDef(),
                                EntityFieldSequence.ENABLE_ALL, formTabDef.isIgnoreParentCondition());
                        _entityFieldSequence.setCategory(categoryType.category());
                        _entityFieldSequence.setOwnerInstId((Long) inst.getId());
                        _entityFieldSequence.load(_entityDef);
                        tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "fc-entityfieldsequencepanel",
                                RendererType.STANDALONE_PANEL);
                        tabSheetItemList.add(new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(),
                                _entityFieldSequence, tabIndex,
                                !isCreateMode && formContext.getFormTab(formTabDef.getName()).isVisible()));
                    }
                        break;
                    case PROPERTY_SEQUENCE: {
                        logDebug("Constructing property sequence tab [{0}] using reference [{1}]...",
                                formTabDef.getName(), formTabDef.getReference());
                        EntityChildCategoryType categoryType = EntityChildCategoryType
                                .fromName(formTabDef.getReference());
                        EntityDef _entityDef = getEntityDef(appletContext.getReference(categoryType));
                        EntityPropertySequence _entityPropertySequence = constructEntityPropertySequence(formContext,
                                sweepingCommitPolicy, formTabDef.getName(), formDef.getEntityDef(),
                                EntityPropertySequence.ENABLE_ALL, formTabDef.isIgnoreParentCondition());
                        _entityPropertySequence.setCategory(categoryType.category());
                        _entityPropertySequence.setOwnerInstId((Long) inst.getId());
                        _entityPropertySequence.load(_entityDef);
                        tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "fc-entitypropertysequencepanel",
                                RendererType.STANDALONE_PANEL);
                        tabSheetItemList.add(new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(),
                                _entityPropertySequence, tabIndex,
                                !isCreateMode && formContext.getFormTab(formTabDef.getName()).isVisible()));
                    }
                        break;
                    case SET_VALUES: {
                        logDebug("Constructing set values tab [{0}] using reference [{1}]...", formTabDef.getName(),
                                formTabDef.getReference());
                        EntityChildCategoryType categoryType = EntityChildCategoryType
                                .fromName(formTabDef.getReference());
                        EntityDef _entityDef = getEntityDef(appletContext.getReference(categoryType));
                        EntitySetValues _entitySetValues = constructEntitySetValues(formContext, sweepingCommitPolicy,
                                formTabDef.getName(), formDef.getEntityDef(), EntitySetValues.ENABLE_ALL,
                                formTabDef.isIgnoreParentCondition());
                        _entitySetValues.setCategory(categoryType.category());
                        _entitySetValues.setOwnerInstId((Long) inst.getId());
                        _entitySetValues.load(_entityDef);
                        tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "fc-entitysetvaluespanel",
                                RendererType.STANDALONE_PANEL);
                        tabSheetItemList.add(new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(),
                                _entitySetValues, tabIndex,
                                !isCreateMode && formContext.getFormTab(formTabDef.getName()).isVisible()));
                    }
                        break;
                    case WIDGET_RULES: {
                        logDebug("Constructing widget rules tab [{0}] using reference [{1}]...", formTabDef.getName(),
                                formTabDef.getReference());
                        EntityChildCategoryType categoryType = EntityChildCategoryType
                                .fromName(formTabDef.getReference());
                        EntityDef _entityDef = getEntityDef(appletContext.getReference(categoryType));
                        EntityWidgetRules _entityWidgetRules = constructEntityWidgetRules(formContext,
                                sweepingCommitPolicy, formTabDef.getName(), formDef.getEntityDef(),
                                EntityWidgetRules.ENABLE_ALL, formTabDef.isIgnoreParentCondition());
                        _entityWidgetRules.setCategory(categoryType.category());
                        _entityWidgetRules.setOwnerInstId((Long) inst.getId());
                        _entityWidgetRules.load(_entityDef);
                        tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "fc-entitywidgetrulespanel",
                                RendererType.STANDALONE_PANEL);
                        tabSheetItemList.add(new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(),
                                _entityWidgetRules, tabIndex,
                                !isCreateMode && formContext.getFormTab(formTabDef.getName()).isVisible()));
                    }
                        break;
                    default:
                        break;
                }
            }

            form.setTabSheet(new TabSheet(tsdb.build(), tabSheetItemList));
        }

        // Related lists
        List<FormRelatedListDef> formRelatedListDefList = formDef.getFormRelatedListDefList();
        if (!DataUtils.isBlank(formRelatedListDefList)) {
            TabSheetDef.Builder tsdb = TabSheetDef.newBuilder(null, 1L);
            List<TabSheetItem> tabSheetItemList = new ArrayList<TabSheetItem>();
            final int len = formRelatedListDefList.size();
            for (int i = 0; i < len; i++) {
                FormRelatedListDef formRelatedListDef = formRelatedListDefList.get(i);
                AppletDef _appletDef = getAppletDef(formRelatedListDef.getApplet());
                final String editAction = formRelatedListDef.getEditAction() == null ? "/assignToRelatedItem"
                        : formRelatedListDef.getEditAction();
                EntitySearch _entitySearch = constructEntitySearch(formContext, sweepingCommitPolicy,
                        formRelatedListDef.getName(), rootTitle, _appletDef, editAction,
                        EntitySearch.ENABLE_ALL & ~(EntitySearch.SHOW_NEW_BUTTON | EntitySearch.SHOW_EDIT_BUTTON
                                | EntitySearch.SHOW_SEARCH),
                        true, false);
                if (_appletDef.isPropWithValue(AppletPropertyConstants.BASE_RESTRICTION)) {
                    AppletFilterDef appletFilterDef = _appletDef.getFilterDef(
                            _appletDef.getPropValue(String.class, AppletPropertyConstants.BASE_RESTRICTION));
                    _entitySearch.setBaseFilter(appletFilterDef != null ? appletFilterDef.getFilterDef() : null,
                            specialParamProvider);
                }

                String fkFieldName = getChildFkFieldName(entityDef.getLongName(), _appletDef.getEntity());
                Restriction baseRestriction = new Equals(fkFieldName, inst.getId());
                _entitySearch.setRelatedList(formRelatedListDef.getName());
                _entitySearch.setBaseRestriction(baseRestriction, specialParamProvider);
                _entitySearch.applyFilterToSearch();

                tsdb.addTabDef(formRelatedListDef.getName(),
                        formRelatedListDef.getLabel() + " (" + _entitySearch.getTotalItemCount() + ")",
                        "fc-relatedlistpanel", RendererType.STANDALONE_PANEL);
                tabSheetItemList.add(new TabSheetItem(formRelatedListDef.getName(), formRelatedListDef.getApplet(),
                        _entitySearch, i, !isCreateMode));
            }

            form.setRelatedListTabSheet(new TabSheet(tsdb.build(), tabSheetItemList, !isCreateMode));
        }

        return form;
    }

    @Override
    public HeadlessTabsForm constructHeadlessTabsForm(AppletContext appletContext,
            SweepingCommitPolicy sweepingCommitPolicy, String rootTitle, AppletDef appletDef) throws UnifyException {
        logDebug("Constructing headerless tabs form for [{0}] using applet definition [{1}]...", rootTitle,
                appletDef.getLongName());
        TabSheetDef.Builder tsdb = TabSheetDef.newBuilder(null, 1L);
        List<TabSheetItem> tabSheetItemList = new ArrayList<TabSheetItem>();
        final List<String> appletList = appletDef.getPropDef(AppletPropertyConstants.HEADLESS_TABS_APPLETS)
                .getValueList();
        final int len = appletList.size();
        final boolean isCreateMode = false;
        for (int i = 0; i < len; i++) {
            final String appletName = appletList.get(i);
            AppletDef _appletDef = getAppletDef(appletName);
            final String editAction = null;
            EntitySearch _entitySearch = constructEntitySearch(new FormContext(appletContext), sweepingCommitPolicy,
                    _appletDef.getName(), rootTitle, _appletDef, editAction, EntitySearch.ENABLE_ALL, false, false);
            if (_appletDef.isPropWithValue(AppletPropertyConstants.BASE_RESTRICTION)) {
                AppletFilterDef appletFilterDef = _appletDef
                        .getFilterDef(_appletDef.getPropValue(String.class, AppletPropertyConstants.BASE_RESTRICTION));
                _entitySearch.setBaseFilter(appletFilterDef != null ? appletFilterDef.getFilterDef() : null,
                        specialParamProvider);
            }

            _entitySearch.setChildTabIndex(i);
            _entitySearch.setHeadlessList(appletName);
            _entitySearch.applyFilterToSearch();

            tsdb.addTabDef(_appletDef.getName(), _appletDef.getLabel(), "fc-headlesslistpanel",
                    RendererType.STANDALONE_PANEL);
            tabSheetItemList.add(new TabSheetItem(_appletDef.getName(), appletName, _entitySearch, i, !isCreateMode));
        }

        return new HeadlessTabsForm(this, new TabSheet(tsdb.build(), tabSheetItemList, !isCreateMode));
    }

    @Override
    public EntitySingleForm constructEntitySingleForm(AbstractApplet applet, String rootTitle, String beanTitle,
            Entity inst, FormMode formMode, BreadCrumbs breadCrumbs) throws UnifyException {
        final AppletContext appletContext = applet != null ? applet.getCtx() : new AppletContext(null, applet, this);
        final FormContext formContext = new FormContext(appletContext, applet.getEntityDef(), inst);
        final SectorIcon sectorIcon = applet != null
                ? getPageSectorIconByApplication(applet.getRootAppletDef().getApplicationName())
                : null;
        final String panelName = applet.getSingleFormAppletDef().getPropValue(String.class,
                AppletPropertyConstants.SINGLE_FORM_PANEL);
        logDebug("Constructing entity single form for [{0}] using panel [{1}]...", rootTitle, panelName);
        final SingleFormBean bean = createSingleFormBeanForPanel(panelName);
        bean.init(this);

        final EntitySingleForm form = new EntitySingleForm(formContext, sectorIcon, breadCrumbs, panelName, bean);
        form.setBeanTitle(beanTitle);
        form.setFormMode(formMode);

        boolean conditionalDisabled = !formBeanMatchAppletPropertyCondition(applet.getSingleFormAppletDef(), form,
                AppletPropertyConstants.MAINTAIN_FORM_UPDATE_CONDITION);
        formContext.setConditionalDisabled(conditionalDisabled);
        return form;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void updateHeaderWithTabsForm(HeaderWithTabsForm form, Entity inst) throws UnifyException {
        final FormDef formDef = form.getFormDef();
        final EntityDef entityDef = formDef.getEntityDef();
        final FormContext formContext = form.getCtx();
        final AbstractEntityFormApplet applet = (AbstractEntityFormApplet) formContext.getAppletContext().applet();
        final Date now = getNow();
        logDebug("Updating header with tabs form for [{0}] using form definition [{1}]...", inst.getId(),
                formDef.getLongName());
        boolean isCreateMode = form.getFormMode().isCreate();
        if (!isCreateMode) {
            String beanTitle = getEntityDescription(getEntityClassDef(entityDef.getLongName()), inst, null);
            form.setBeanTitle(beanTitle);
        }

        FormTabDef _formTabDef = formDef.getFormTabDef(0);
        // Clear unsatisfactory foreign key fields
        if (HeaderWithTabsForm.UpdateType.SWITCH_ON_CHANGE.equals(form.getUpdateType())
                && _formTabDef.isWithCondRefDefFormFields()) {
            clearUnsatisfactoryRefs(_formTabDef, entityDef, form.getCtx().getFormValueStore().getReader(), inst);
        }

        // Populate entity list-only fields
        populateListOnlyFields(entityDef, inst);

        form.setFormBean(inst);
        if (applet.isCollaboration() && applet.isRootForm()) {
            setCollaborationContext(form);
        }

        final AppletDef _formAppletDef = applet != null ? applet.getFormAppletDef() : null;
        if (_formAppletDef != null && !_formAppletDef.isStudioComponent()) {
            boolean conditionalDisabled = !formBeanMatchAppletPropertyCondition(_formAppletDef, form,
                    AppletPropertyConstants.MAINTAIN_FORM_UPDATE_CONDITION);
            formContext.setConditionalDisabled(conditionalDisabled);
        }

        // Update tabs
        if (form.isWithTabSheet() && formDef.getTabCount() > 1) {
            formContext.evaluateTabStates();
            TabSheet tabSheet = form.getTabSheet();
            for (TabSheetItem tabSheetItem : tabSheet.getTabSheetItemList()) {
                FormTabDef formTabDef = formDef.getFormTabDef(tabSheetItem.getIndex());
                switch (formTabDef.getContentType()) {
                    case MINIFORM:
                    case MINIFORM_CHANGELOG: {
                        tabSheetItem.setVisible(formContext.getFormTab(tabSheetItem.getName()).isVisible());
                    }
                        break;
                    case MINIFORM_MAPPED: {
                        logDebug("Updating mini form for tab [{0}]...", formTabDef.getName());
                        final String _mappedFieldName = formTabDef.getMappedFieldName();
                        final FormDef _mappedFormDef = getFormDef(formTabDef.getMappedForm());
                        final Long _mappedInstId = DataUtils.getBeanProperty(Long.class, inst, _mappedFieldName);
                        EntityClassDef mappedEntityClassDef = getEntityClassDef(
                                _mappedFormDef.getEntityDef().getLongName());
                        Entity _mappedInst = _mappedInstId != null
                                ? environment().list((Class<? extends Entity>) mappedEntityClassDef.getEntityClass(),
                                        _mappedInstId)
                                : (Entity) ReflectUtils.newInstance(mappedEntityClassDef.getEntityClass());
                        MiniForm miniForm = (MiniForm) tabSheetItem.getValObject();
                        miniForm.getCtx().setInst(_mappedInst);
                        tabSheetItem.setVisible(formContext.getFormTab(formTabDef.getName()).isVisible());
                    }
                        break;
                    case PROPERTY_LIST: {
                        logDebug("Updating property list tab [{0}] using applet [{1}]...", formTabDef.getName(),
                                formTabDef.getApplet());
                        String childFkFieldName = getChildFkFieldName(entityDef, formTabDef.getReference());
                        PropertySearch _propertySearch = (PropertySearch) tabSheetItem.getValObject();
                        _propertySearch.applyEntityToSearch(inst, childFkFieldName);
                        tabSheetItem.setVisible(
                                !isCreateMode && formContext.getFormTab(tabSheetItem.getName()).isVisible());
                    }
                        break;
                    case USAGE_LIST: {
                        logDebug("Updating usage list tab [{0}] using applet [{1}]...", formTabDef.getName(),
                                formTabDef.getApplet());
                        UsageSearch _usageSearch = (UsageSearch) tabSheetItem.getValObject();
                        _usageSearch.applyEntityToSearch(inst);
                        tabSheetItem.setVisible(
                                !isCreateMode && formContext.getFormTab(tabSheetItem.getName()).isVisible());
                    }
                        break;
                    case CHILD: {
                        logDebug("Updating child tab [{0}] using reference [{1}]...", formTabDef.getName(),
                                formTabDef.getReference());
                        Restriction childRestriction = getChildRestriction(entityDef, formTabDef.getReference(), inst);
                        EntityChild _entityChild = (EntityChild) tabSheetItem.getValObject();
                        _entityChild.load(formContext, childRestriction);
                        tabSheetItem.setVisible(
                                !isCreateMode && formContext.getFormTab(tabSheetItem.getName()).isVisible());
                    }
                        break;
                    case CHILD_LIST: {
                        logDebug("Updating child list tab [{0}] using reference [{1}]...", formTabDef.getName(),
                                formTabDef.getReference());
                        final boolean newButtonVisible = !hideAddActionButton(form, applet.getFormAppletDef(),
                                formTabDef.getApplet());
                        EntitySearch _entitySearch = (EntitySearch) tabSheetItem.getValObject();
                        Restriction childRestriction = getChildRestriction(entityDef, formTabDef.getReference(), inst);
                        Restriction tabRestriction = formTabDef.getRestriction(FilterType.TAB,
                                formContext.getFormValueStore().getReader(), now);
                        childRestriction = RestrictionUtils.and(childRestriction, tabRestriction);

                        _entitySearch.setNewButtonVisible(newButtonVisible);
                        _entitySearch.setBaseRestriction(childRestriction, specialParamProvider);
                        _entitySearch.applyFilterToSearch();
                        tabSheetItem.setVisible(
                                !isCreateMode && formContext.getFormTab(tabSheetItem.getName()).isVisible());
                    }
                        break;
                    case FILTER_CONDITION: {
                        logDebug("Updating filter condition tab [{0}] using reference [{1}]...", formTabDef.getName(),
                                formTabDef.getReference());
                        EntityChildCategoryType categoryType = EntityChildCategoryType
                                .fromName(formTabDef.getReference());
                        EntityDef _entityDef = getEntityDef(formContext.getAppletContext().getReference(categoryType));
                        EntityFilter _entityFilter = (EntityFilter) tabSheetItem.getValObject();
                        _entityFilter.setListType(categoryType.listType());
                        _entityFilter.setParamList(categoryType.paramList());
                        _entityFilter.setCategory(categoryType.category());
                        _entityFilter.setOwnerInstId((Long) inst.getId());
                        _entityFilter.load(_entityDef);
                        tabSheetItem.setVisible(
                                !isCreateMode && formContext.getFormTab(tabSheetItem.getName()).isVisible());
                    }
                        break;
                    case SEARCH_INPUT: {
                        logDebug("Updating search input tab [{0}] using reference [{1}]...", formTabDef.getName(),
                                formTabDef.getReference());
                        EntityChildCategoryType categoryType = EntityChildCategoryType
                                .fromName(formTabDef.getReference());
                        EntityDef _entityDef = getEntityDef(formContext.getAppletContext().getReference(categoryType));
                        EntitySearchInput _entitySearchInput = (EntitySearchInput) tabSheetItem.getValObject();
                        _entitySearchInput.setCategory(categoryType.category());
                        _entitySearchInput.setOwnerInstId((Long) inst.getId());
                        _entitySearchInput.load(_entityDef);
                        tabSheetItem.setVisible(
                                !isCreateMode && formContext.getFormTab(tabSheetItem.getName()).isVisible());
                    }
                        break;
                    case PARAM_VALUES: {
                        logDebug("Updating parameter values tab [{0}] using reference [{1}]...", formTabDef.getName(),
                                formTabDef.getReference());
                        ParamConfigListProvider pclProvider = getComponent(ParamConfigListProvider.class,
                                formTabDef.getReference());
                        EntityParamValues _entityParamValues = (EntityParamValues) tabSheetItem.getValObject();
                        _entityParamValues.setCategory(pclProvider.getCategory(inst));
                        _entityParamValues.setOwnerInstId((Long) inst.getId());
                        _entityParamValues.load(pclProvider.getParamConfigList(inst));
                        tabSheetItem.setVisible(
                                !isCreateMode && formContext.getFormTab(tabSheetItem.getName()).isVisible());
                    }
                        break;
                    case FIELD_SEQUENCE: {
                        logDebug("Updating field sequence tab [{0}] using reference [{1}]...", formTabDef.getName(),
                                formTabDef.getReference());
                        EntityChildCategoryType categoryType = EntityChildCategoryType
                                .fromName(formTabDef.getReference());
                        EntityDef _entityDef = getEntityDef(formContext.getAppletContext().getReference(categoryType));
                        EntityFieldSequence _entityFieldSequence = (EntityFieldSequence) tabSheetItem.getValObject();
                        _entityFieldSequence.setCategory(categoryType.category());
                        _entityFieldSequence.setOwnerInstId((Long) inst.getId());
                        _entityFieldSequence.load(_entityDef);
                        tabSheetItem.setVisible(
                                !isCreateMode && formContext.getFormTab(tabSheetItem.getName()).isVisible());
                    }
                        break;
                    case PROPERTY_SEQUENCE: {
                        logDebug("Updating property sequence tab [{0}] using reference [{1}]...", formTabDef.getName(),
                                formTabDef.getReference());
                        EntityChildCategoryType categoryType = EntityChildCategoryType
                                .fromName(formTabDef.getReference());
                        EntityDef _entityDef = getEntityDef(formContext.getAppletContext().getReference(categoryType));
                        EntityPropertySequence _entityPropertySequence = (EntityPropertySequence) tabSheetItem
                                .getValObject();
                        _entityPropertySequence.setCategory(categoryType.category());
                        _entityPropertySequence.setOwnerInstId((Long) inst.getId());
                        _entityPropertySequence.load(_entityDef);
                        tabSheetItem.setVisible(
                                !isCreateMode && formContext.getFormTab(tabSheetItem.getName()).isVisible());
                    }
                        break;
                    case SET_VALUES: {
                        logDebug("Updating set values tab [{0}] using reference [{1}]...", formTabDef.getName(),
                                formTabDef.getReference());
                        EntityChildCategoryType categoryType = EntityChildCategoryType
                                .fromName(formTabDef.getReference());
                        EntityDef _entityDef = getEntityDef(formContext.getAppletContext().getReference(categoryType));
                        EntitySetValues _entitySetValues = (EntitySetValues) tabSheetItem.getValObject();
                        _entitySetValues.setCategory(categoryType.category());
                        _entitySetValues.setOwnerInstId((Long) inst.getId());
                        _entitySetValues.load(_entityDef);
                        tabSheetItem.setVisible(
                                !isCreateMode && formContext.getFormTab(tabSheetItem.getName()).isVisible());
                    }
                        break;
                    case WIDGET_RULES: {
                        logDebug("Updating widget rules tab [{0}] using reference [{1}]...", formTabDef.getName(),
                                formTabDef.getReference());
                        EntityChildCategoryType categoryType = EntityChildCategoryType
                                .fromName(formTabDef.getReference());
                        EntityDef _entityDef = getEntityDef(formContext.getAppletContext().getReference(categoryType));
                        EntityWidgetRules _entityWidgetRules = (EntityWidgetRules) tabSheetItem.getValObject();
                        _entityWidgetRules.setCategory(categoryType.category());
                        _entityWidgetRules.setOwnerInstId((Long) inst.getId());
                        _entityWidgetRules.load(_entityDef);
                        tabSheetItem.setVisible(
                                !isCreateMode && formContext.getFormTab(tabSheetItem.getName()).isVisible());
                    }
                        break;
                    default:
                        break;

                }
            }

            tabSheet.pushTabIndexToActiveItem();
            tabSheet.setActive(true);
        }

        // Update related lists
        if (form.isWithRelatedListTabSheet()) {
            TabSheet tabSheet = form.getRelatedListTabSheet();
            if (!isCreateMode) {
                List<FormRelatedListDef> formRelatedListDefList = formDef.getFormRelatedListDefList();
                if (!DataUtils.isBlank(formRelatedListDefList)) {
                    for (TabSheetItem tabSheetItem : tabSheet.getTabSheetItemList()) {
                        FormRelatedListDef formRelatedListDef = formRelatedListDefList.get(tabSheetItem.getIndex());
                        AppletDef _appletDef = getAppletDef(formRelatedListDef.getApplet());
                        String fkFieldName = getChildFkFieldName(entityDef.getLongName(), _appletDef.getEntity());
                        EntitySearch _entitySearch = (EntitySearch) tabSheetItem.getValObject();
                        _entitySearch.setBaseRestriction(new Equals(fkFieldName, inst.getId()), specialParamProvider);
                        _entitySearch.applyFilterToSearch();

                        form.getRelatedListTabSheet().getTabDef(tabSheetItem.getIndex()).setTabLabel(
                                formRelatedListDef.getLabel() + " (" + _entitySearch.getTotalItemCount() + ")");
                    }
                }
            }

            tabSheet.setActive(!isCreateMode);
        }
    }

    @Override
    public void updateEntitySingleForm(AbstractApplet applet, EntitySingleForm form, Entity inst)
            throws UnifyException {
        if (!form.getFormMode().isCreate()) {
            form.setBeanTitle(inst.getDescription());
        }

        form.setFormBean(inst);
    }

    @Override
    public String getChildFkFieldName(String entity, String childEntity) throws UnifyException {
        final EntityDef entityDef = getEntityDef(childEntity);
        EntityFieldDef entityFieldDef = entityDef.getRefEntityFieldDef(entity);
        if (entityFieldDef != null) {
            return entityFieldDef.getFieldName();
        }

        entityFieldDef = entityDef.getMappedFieldDefByEntity(this, entity);
        return entityFieldDef != null ? entityFieldDef.getFieldName() : null;
    }

    @Override
    public String getChildFkFieldName(EntityDef parentEntityDef, String childFieldName) throws UnifyException {
        EntityFieldDef childListFieldDef = parentEntityDef.getFieldDef(childFieldName);
        EntityDef _childEntityDef = getEntityDef(getRefDef(childListFieldDef.getReferences()).getEntity());
        EntityFieldDef refEntityFieldDef = _childEntityDef.getRefEntityFieldDef(parentEntityDef.getLongName());
        return refEntityFieldDef != null ? refEntityFieldDef.getFieldName() : null;
    }

    @Override
    public Restriction getChildRestriction(EntityDef parentEntityDef, String childFieldName, Entity parentInst)
            throws UnifyException {
        return applicationModuleService.getChildRestriction(parentEntityDef, childFieldName, parentInst);
    }

    @Override
    public void populateNewChildReferenceFields(EntityDef parentEntityDef, String childFieldName, Entity parentInst,
            Entity childInst) throws UnifyException {
        EntityFieldDef childListFieldDef = parentEntityDef.getFieldDef(childFieldName);
        EntityDef _childEntityDef = getEntityDef(getRefDef(childListFieldDef.getReferences()).getEntity());
        if (childListFieldDef.isWithCategory()) {
            DataUtils.setBeanProperty(childInst, _childEntityDef.getFosterParentIdDef().getFieldName(),
                    parentInst.getId());
            DataUtils.setBeanProperty(childInst, _childEntityDef.getFosterParentTypeDef().getFieldName(),
                    parentEntityDef.getTableName());
            DataUtils.setBeanProperty(childInst, _childEntityDef.getCategoryColumnDef().getFieldName(),
                    childListFieldDef.getCategory());
            return;
        }

        final String fkFieldName = _childEntityDef.getRefEntityFieldDef(parentEntityDef.getLongName()).getFieldName();
        DataUtils.setBeanProperty(childInst, fkFieldName, parentInst.getId());
        copyListOnlyFieldsFromForeignEntity(_childEntityDef, fkFieldName, childInst, parentInst);
    }

    @Override
    public Long getOverrideTenantId(EntityDef parentEntityDef, Entity parentInst) throws UnifyException {
        if (parentEntityDef != null && isTenancyEnabled() && parentEntityDef.getLongName().equals(
                getApplicationAttribute(String.class, FlowCentralApplicationAttributeConstants.TENANT_SOURCE_ENTITY))) {
            return getMappedDestTenantId((Long) parentInst.getId());
        }

        return null;
    }

    @Override
    public PropertySearch constructPropertySearch(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy,
            String tabName, Entity inst, AppletDef _appletDef, boolean isIgnoreParentCondition) throws UnifyException {
        PropertyRuleDef _propertyRuleDef = getPropertyRuleDef(
                _appletDef.getPropValue(String.class, AppletPropertyConstants.PROPERTY_LIST_RULE));
        int propertySearchMode = PropertySearch.ENABLE_ALL;
        if (!_appletDef.getPropValue(boolean.class, AppletPropertyConstants.PROPERTY_LIST_UPDATE, false)) {
            propertySearchMode = propertySearchMode & ~PropertySearch.SHOW_EDIT_BUTTON;
        }

        PropertySearch _propertySearch = new PropertySearch(ctx, sweepingCommitPolicy, tabName, _propertyRuleDef,
                propertySearchMode, isIgnoreParentCondition);
        _propertySearch.setEntitySubTitle(inst.getDescription());
        return _propertySearch;
    }

    @Override
    public EntitySearch constructEntitySearch(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy,
            String tabName, String rootTitle, AppletDef _appletDef, String editAction, int entitySearchMode,
            boolean isIgnoreReport, boolean isIgnoreParentCondition) throws UnifyException {
        logDebug("Constructing entity search for [{0}] using applet definition [{1}]...", rootTitle,
                _appletDef.getLongName());
        TableDef _tableDef = getTableDef(_appletDef.getPropValue(String.class, AppletPropertyConstants.SEARCH_TABLE));
        if (!_appletDef.getPropValue(boolean.class, AppletPropertyConstants.SEARCH_TABLE_NEW, false)) {
            entitySearchMode = entitySearchMode & ~EntitySearch.SHOW_NEW_BUTTON;
        }

        if (!_appletDef.getPropValue(boolean.class, AppletPropertyConstants.SEARCH_TABLE_EDIT, false)) {
            entitySearchMode = entitySearchMode & ~EntitySearch.SHOW_EDIT_BUTTON;
        }

        if (!_appletDef.getPropValue(boolean.class, AppletPropertyConstants.SEARCH_TABLE_QUICKFILTER, false)) {
            entitySearchMode = entitySearchMode & ~EntitySearch.SHOW_QUICKFILTER;
        }

        if (!_appletDef.getPropValue(boolean.class, AppletPropertyConstants.SEARCH_TABLE_ACTIONFOOTER, false)) {
            entitySearchMode = entitySearchMode & ~EntitySearch.SHOW_ACTIONFOOTER;
        }

        if (_tableDef.isWithDetailsPanelName() && _appletDef.getPropValue(boolean.class,
                AppletPropertyConstants.SEARCH_TABLE_SHOW_EXPANDED_DETAILS, false)) {
            entitySearchMode |= EntitySearch.SHOW_EXPAND_DETAILS;
        }

        if (!isIgnoreReport
                && _appletDef.getPropValue(boolean.class, AppletPropertyConstants.SEARCH_TABLE_REPORT, false)
                && systemModuleService.getSysParameterValue(boolean.class,
                        ApplicationModuleSysParamConstants.ENABLE_QUICK_REPORT)) {
            entitySearchMode |= EntitySearch.SHOW_REPORT;
        }

        final boolean viewItemsInSeparateTabs = _appletDef.getPropValue(boolean.class,
                AppletPropertyConstants.SEARCH_TABLE_VIEW_ITEM_SEPARATE_TAB)
                && systemModuleService.getSysParameterValue(boolean.class,
                        ApplicationModuleSysParamConstants.ENABLE_VIEW_ENTITY_IN_SEPARATE_TAB);

        if (_appletDef.getPropValue(boolean.class, AppletPropertyConstants.SEARCH_TABLE_SEARCH_ON_CRITERIA_ONLY,
                false)) {
            entitySearchMode |= EntitySearch.SEARCH_ON_CRITERIA_ONLY;
        }

        final boolean basicSearchOnly = _appletDef.getPropValue(boolean.class,
                AppletPropertyConstants.SEARCH_TABLE_BASICSEARCHONLY, false);

        final String defaultQuickFilter = _appletDef.getPropValue(String.class,
                AppletPropertyConstants.SEARCH_TABLE_QUICKFILTER_DEFAULT);

        final String searchConfigName = _appletDef.getPropValue(String.class,
                AppletPropertyConstants.SEARCH_TABLE_SEARCHINPUT);

        final boolean showBaseRestriction = systemModuleService.getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.SHOW_RESOLVED_BASE_RESTRICTION);

        final int appletSearchColumns = _appletDef.getPropValue(int.class,
                AppletPropertyConstants.SEARCH_TABLE_SEARCH_COLUMNS);

        final boolean showConditions = systemModuleService.getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.SEARCH_ENTRY_SHOW_CONDITIONS);

        final int systemSearchColumns = systemModuleService.getSysParameterValue(int.class,
                ApplicationModuleSysParamConstants.SEARCH_ENTRY_COLUMNS);
        final int searchColumns = appletSearchColumns > 0 ? appletSearchColumns
                : (systemSearchColumns > 0 ? systemSearchColumns : 1);
        final String preferredEvent = systemModuleService.getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.ENABLE_SEARCH_ON_SEARCH_INPUT_CHANGE) ? "onchange" : null;
        
        SectorIcon sectorIcon = getPageSectorIconByApplication(_appletDef.getApplicationName());
        EntitySearch _entitySearch = new EntitySearch(ctx, sectorIcon, sweepingCommitPolicy, tabName, _tableDef,
                _appletDef.getId(), editAction, defaultQuickFilter, searchConfigName, preferredEvent, searchColumns,
                entitySearchMode, showConditions, isIgnoreParentCondition, viewItemsInSeparateTabs);
        _entitySearch.setPaginationLabel(resolveSessionMessage("$m{entitysearch.display.label}"));
        _entitySearch.setBasicSearchOnly(basicSearchOnly);
        _entitySearch.setShowBaseRestriction(showBaseRestriction);
        if (_appletDef.isDescriptiveButtons()) {
            String addCaption = _appletDef.getPropValue(String.class, AppletPropertyConstants.SEARCH_TABLE_NEW_CAPTION);
            if (!StringUtils.isBlank(addCaption)) {
                _entitySearch.setEntityNewLabel(resolveSessionMessage(addCaption));
            } else {
                _entitySearch.setEntityNewLabel(
                        resolveSessionMessage("$m{entitysearch.button.new}", _tableDef.getEntityDef().getLabel()));
            }
        }

        _entitySearch.setEntitySubTitle(rootTitle);
        return _entitySearch;
    }

    @Override
    public LoadingSearch constructLoadingSearch(AppletContext ctx, int loadingSearchMode) throws UnifyException {
        logDebug("Constructing loading search using applet definition [{0}]...", ctx.getRootAppletName());
        final AppletDef _rootAppletDef = ctx.getRootAppletDef();
        TableDef _tableDef = getTableDef(
                _rootAppletDef.getPropValue(String.class, AppletPropertyConstants.LOADING_TABLE));
        if (!_rootAppletDef.getPropValue(boolean.class, AppletPropertyConstants.LOADING_TABLE_ACTIONFOOTER, false)) {
            loadingSearchMode = loadingSearchMode & ~LoadingSearch.SHOW_ACTIONFOOTER;
        }

        final String searchConfigName = _rootAppletDef.getPropValue(String.class,
                AppletPropertyConstants.SEARCH_TABLE_SEARCHINPUT);
        final int appletSearchColumns = _rootAppletDef.getPropValue(int.class,
                AppletPropertyConstants.SEARCH_TABLE_SEARCH_COLUMNS);

        final boolean showConditions = systemModuleService.getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.SEARCH_ENTRY_SHOW_CONDITIONS);

        final int systemSearchColumns = systemModuleService.getSysParameterValue(int.class,
                ApplicationModuleSysParamConstants.SEARCH_ENTRY_COLUMNS);
        final int searchColumns = appletSearchColumns > 0 ? appletSearchColumns
                : (systemSearchColumns > 0 ? systemSearchColumns : 1);
        final String preferredEvent = systemModuleService.getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.ENABLE_SEARCH_ON_SEARCH_INPUT_CHANGE) ? "onchange" : null;

        SectorIcon sectorIcon = getPageSectorIconByApplication(_rootAppletDef.getApplicationName());
        LoadingSearch loadingSearch = new LoadingSearch(ctx, sectorIcon, _tableDef, _rootAppletDef.getId(),
                searchConfigName, preferredEvent, searchColumns, loadingSearchMode, showConditions);
        loadingSearch.setEntitySubTitle(_rootAppletDef.getLabel());
        return loadingSearch;
    }

    @Override
    public EntitySelect constructEntitySelect(RefDef refDef, ValueStore valueStore, String fieldNameA,
            String fieldNameB, String filter, int limit) throws UnifyException {
        logDebug("Constructing entity select using reference definition [{0}]...", refDef.getLongName());
        TableDef tableDef = applicationModuleService.getTableDef(refDef.getSearchTable());
        EntitySelect entitySelect = new EntitySelect(this, tableDef, refDef.getSearchField(), fieldNameA, fieldNameB,
                valueStore, refDef.getSelectHandler(), refDef.getOrderField(), limit);
        entitySelect.setEnableFilter(true);
        String label = tableDef.getEntityDef().getFieldDef(refDef.getSearchField()).getFieldLabel() + ":";
        String labelA = !StringUtils.isBlank(fieldNameA)
                ? tableDef.getEntityDef().getFieldDef(fieldNameA).getFieldLabel() + ":"
                : null;
        String labelB = !StringUtils.isBlank(fieldNameB)
                ? tableDef.getEntityDef().getFieldDef(fieldNameB).getFieldLabel() + ":"
                : null;
        entitySelect.setLabel(label);
        entitySelect.setLabelA(labelA);
        entitySelect.setLabelB(labelB);
        if (!StringUtils.isBlank(filter)) {
            entitySelect.setFilter(filter);
        }

        EntityClassDef entityClassDef = applicationModuleService.getEntityClassDef(refDef.getEntity());
        Restriction br = refDef.isWithFilter()
                ? refDef.getFilter().getRestriction(entityClassDef.getEntityDef(), valueStore.getReader(), getNow())
                : null;

        entitySelect.setBaseRestriction(br);
        return entitySelect;
    }

    @Override
    public EntityChild constructEntityChild(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy, String tabName,
            String rootTitle, AppletDef _appletDef, boolean quickEdit, boolean isIgnoreParentCondition)
            throws UnifyException {
        logDebug("Constructing entity child for [{0}] using applet definition [{1}]...", rootTitle,
                _appletDef.getLongName());
        FormDef childFormDef = getFormDef(_appletDef.getPropValue(String.class, AppletPropertyConstants.MAINTAIN_FORM));
        EntityChild _entityChild = new EntityChild(ctx, sweepingCommitPolicy, tabName, childFormDef, quickEdit,
                isIgnoreParentCondition);
        _entityChild.setEntitySubTitle(rootTitle);
        return _entityChild;
    }

    @Override
    public EntityFilter constructEntityFilter(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy,
            String tabName, EntityDef ownerEntityDef, int entityFilterMode, boolean isIgnoreParentCondition)
            throws UnifyException {
        logDebug("Constructing entity filter for [{0}] using entity definition [{1}]...", tabName,
                ownerEntityDef.getLongName());
        return new EntityFilter(ctx, sweepingCommitPolicy, tabName, ownerEntityDef, entityFilterMode,
                isIgnoreParentCondition);
    }

    @Override
    public EntitySearchInput constructEntitySearchInput(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy,
            String tabName, EntityDef ownerEntityDef, int entitySearchInputMode, boolean isIgnoreParentCondition)
            throws UnifyException {
        logDebug("Constructing entity search input for [{0}] using entity definition [{1}]...", tabName,
                ownerEntityDef.getLongName());
        return new EntitySearchInput(this, ctx, sweepingCommitPolicy, tabName, ownerEntityDef, entitySearchInputMode,
                isIgnoreParentCondition);
    }

    @Override
    public EntityFieldSequence constructEntityFieldSequence(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy,
            String tabName, EntityDef ownerEntityDef, int entityFieldSequenceMode, boolean isIgnoreParentCondition)
            throws UnifyException {
        logDebug("Constructing entity field sequence for [{0}] using entity definition [{1}]...", tabName,
                ownerEntityDef.getLongName());
        return new EntityFieldSequence(ctx, sweepingCommitPolicy, tabName, ownerEntityDef, entityFieldSequenceMode,
                isIgnoreParentCondition);
    }

    @Override
    public EntityPropertySequence constructEntityPropertySequence(FormContext ctx,
            SweepingCommitPolicy sweepingCommitPolicy, String tabName, EntityDef ownerEntityDef,
            int entityPropertySequenceMode, boolean isIgnoreParentCondition) throws UnifyException {
        logDebug("Constructing entity property sequence for [{0}] using entity definition [{1}]...", tabName,
                ownerEntityDef.getLongName());
        return new EntityPropertySequence(ctx, sweepingCommitPolicy, tabName, ownerEntityDef,
                entityPropertySequenceMode, isIgnoreParentCondition);
    }

    @Override
    public EntityWidgetRules constructEntityWidgetRules(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy,
            String tabName, EntityDef ownerEntityDef, int mode, boolean isIgnoreParentCondition) throws UnifyException {
        logDebug("Constructing entity widget rules for [{0}] using entity definition [{1}]...", tabName,
                ownerEntityDef.getLongName());
        return new EntityWidgetRules(ctx, sweepingCommitPolicy, tabName, ownerEntityDef, mode, isIgnoreParentCondition);
    }

    @Override
    public EntitySetValues constructEntitySetValues(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy,
            String tabName, EntityDef ownerEntityDef, int entitySetValuesMode, boolean isIgnoreParentCondition)
            throws UnifyException {
        logDebug("Constructing entity set values for [{0}] using entity definition [{1}]...", tabName,
                ownerEntityDef.getLongName());
        return new EntitySetValues(ctx, sweepingCommitPolicy, tabName, ownerEntityDef, entitySetValuesMode,
                isIgnoreParentCondition);
    }

    @Override
    public EntityParamValues constructEntityParamValues(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy,
            String tabName, EntityDef ownerEntityDef, int entityParamValuesMode, boolean isIgnoreParentCondition)
            throws UnifyException {
        logDebug("Constructing entity parameter values for [{0}] using entity definition [{1}]...", tabName,
                ownerEntityDef.getLongName());
        return new EntityParamValues(ctx, sweepingCommitPolicy, tabName, ownerEntityDef, entityParamValuesMode,
                isIgnoreParentCondition);
    }

    @Override
    public BeanListTable constructEntryBeanTable(String tableName, String entryEditPolicy) throws UnifyException {
        return constructEntryBeanTable(tableName, null, entryEditPolicy);
    }

    @Override
    public BeanListTable constructEntryBeanTable(String tableName, FilterGroupDef filterGroupDef,
            String entryEditPolicy) throws UnifyException {
        BeanListTable entryBeanTable = new BeanListTable(this, getTableDef(tableName), filterGroupDef,
                BeanListTable.ENTRY_ENABLED);
        if (!StringUtils.isBlank(entryEditPolicy)) {
            ChildListEditPolicy policy = getComponent(ChildListEditPolicy.class, entryEditPolicy);
            entryBeanTable.setPolicy(policy);
        }

        return entryBeanTable;
    }

    @Override
    public EntityCRUD constructEntityCRUD(AppletContext ctx, String appletName,
            EntityFormEventHandlers formEventHandlers, String baseField, Object baseId) throws UnifyException {
        return constructEntityCRUD(ctx, appletName, formEventHandlers, null, null, null, baseField, baseId, null, null,
                false, true, false);
    }

    @Override
    public EntityCRUD constructEntityCRUD(AppletContext ctx, String appletName,
            EntityFormEventHandlers formEventHandlers, SweepingCommitPolicy sweepingCommitPolicy,
            EntityDef parentEntityDef, Entity parentInst, String baseField, Object baseId, String childListName,
            FilterGroupDef filterGroupDef, final boolean viewOnly, final boolean allowAddition, final boolean fixedRows)
            throws UnifyException {
        final AppletDef formAppletDef = getAppletDef(appletName);
        final String tableName = formAppletDef.isPropWithValue(AppletPropertyConstants.ENTRY_TABLE)
                ? formAppletDef.getPropValue(String.class, AppletPropertyConstants.ENTRY_TABLE)
                : formAppletDef.getPropValue(String.class, AppletPropertyConstants.SEARCH_TABLE);
        final String entryTablePolicy = formAppletDef.getPropValue(String.class,
                AppletPropertyConstants.ENTRY_TABLE_POLICY);
        final String createFormName = viewOnly ? null
                : formAppletDef.getPropValue(String.class, AppletPropertyConstants.CREATE_FORM);
        final String maintainFormName = formAppletDef.getPropValue(String.class, AppletPropertyConstants.MAINTAIN_FORM);
        final EntityClassDef entityClassDef = ctx.au().getEntityClassDef(formAppletDef.getEntity());
        TableDef tableDef = ctx.au().getTableDef(tableName);
        EntityTable entityTable = new EntityTable(ctx.au(), tableDef, filterGroupDef);
        entityTable.setCrudMode(EntityTable.CrudMode.SIMPLE);
        entityTable.setFixedRows(fixedRows);
        if (!StringUtils.isBlank(entryTablePolicy)) {
            ChildListEditPolicy policy = ctx.au().getComponent(ChildListEditPolicy.class, entryTablePolicy);
            entityTable.setPolicy(policy);
        }

        entityTable.setViewOnly(viewOnly);
        MiniForm createForm = null;
        if (!viewOnly) {
            FormContext createFrmCtx = new FormContext(ctx, ctx.au().getFormDef(createFormName), formEventHandlers);
            createFrmCtx.setCrudMode();
            createFrmCtx.setParentEntityDef(parentEntityDef);
            createFrmCtx.setParentInst(parentInst);

            createForm = new MiniForm(MiniFormScope.MAIN_FORM, createFrmCtx,
                    createFrmCtx.getFormDef().getFormTabDef(0));
        }

        FormContext maintainFrmCtx = new FormContext(ctx, ctx.au().getFormDef(maintainFormName), formEventHandlers);
        maintainFrmCtx.setCrudMode();
        maintainFrmCtx.setParentEntityDef(parentEntityDef);
        maintainFrmCtx.setParentInst(parentInst);

        MiniForm maintainForm = new MiniForm(MiniFormScope.MAIN_FORM, maintainFrmCtx,
                maintainFrmCtx.getFormDef().getFormTabDef(0));
        return new EntityCRUD(ctx.au(), sweepingCommitPolicy, formAppletDef, entityClassDef, baseField, baseId,
                entityTable, createForm, maintainForm, childListName, allowAddition);
    }

    @Override
    public boolean formBeanMatchAppletPropertyCondition(AppletDef appletDef, AbstractForm form,
            String conditionPropName) throws UnifyException {
        String condFilterName = appletDef.getPropValue(String.class, conditionPropName, null);
        if (condFilterName != null) {
            return appletDef.getFilterDef(condFilterName).getFilterDef()
                    .getObjectFilter(getEntityClassDef(appletDef.getEntity()).getEntityDef(),
                            form.getFormValueStoreReader(), getNow())
                    .matchObject(form.getFormBean());
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void bumpVersion(Database db, EntityDef entityDef, Entity inst) throws UnifyException {
        if (inst != null) {
            final Class<?> entityClass = getEntityClassDef(entityDef.getLongName()).getEntityClass();
            if (BaseVersionEntity.class.isAssignableFrom(entityClass)) {
                final boolean isClearMergeVersion = BaseApplicationEntity.class.isAssignableFrom(entityClass)
                        && !isEnterprise();
                Query<?> query = Query.of((Class<? extends Entity>) entityClass).addEquals("id", inst.getId());
                long bumpedVersionNo = db.value(long.class, "versionNo", query) + 1L;
                Update update = isClearMergeVersion
                        ? new Update().add("versionNo", bumpedVersionNo).add("devMergeVersionNo", null)
                        : new Update().add("versionNo", bumpedVersionNo);

                if (AppEntity.class.equals(entityClass)) {
                    AppEntity appEntity = (AppEntity) inst;
                    if (appEntity.getConfigType().isCustom() && StringUtils.isBlank(appEntity.getDelegate())) {
                        appEntity.setSchemaUpdateRequired(true);
                        update.add("schemaUpdateRequired", true);
                    }
                }

                db.updateAll(query, update);

                ((BaseVersionEntity) inst).setVersionNo(bumpedVersionNo);
                if (isClearMergeVersion) {
                    ((BaseApplicationEntity) inst).setDevMergeVersionNo(null);
                }
            }
        }
    }

    @Override
    public void bumpVersion(EntityDef entityDef, Long id) throws UnifyException {
        applicationModuleService.bumpVersion(entityDef, id);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected void onInitialize() throws UnifyException {
        Map<String, MappedEntityProvider<? extends BaseMappedEntityProviderContext>> providers = new HashMap<String, MappedEntityProvider<? extends BaseMappedEntityProviderContext>>();
        List<MappedEntityProvider> _providers = getComponents(MappedEntityProvider.class);
        for (MappedEntityProvider _provider : _providers) {
            if (providers.containsKey(_provider.destEntity())) {
                final MappedEntityProvider existingMappedEntityProvider = providers.get(_provider.destEntity());
                final boolean currentPreferred = _provider.getClass().isAnnotationPresent(Preferred.class);
                final boolean existingPreferred = existingMappedEntityProvider.getClass()
                        .isAnnotationPresent(Preferred.class);
                if (existingPreferred == currentPreferred) {
                    throwOperationErrorException(
                            new IllegalArgumentException("Multiple mapped entity providers found entity class ["
                                    + _provider.destEntity() + "]. Found [" + _provider.getName() + "] and ["
                                    + existingMappedEntityProvider.getName() + "]"));
                }

                if (existingPreferred && !currentPreferred) {
                    continue;
                }
            }

            providers.put(_provider.destEntity(), _provider);
        }

        mappedEntityProviderInfo = new MappedEntityProviderInfo(providers);
    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    private boolean hideAddActionButton(HeaderWithTabsForm form, AppletDef _currFormAppletDef, String childAppletName)
            throws UnifyException {
        List<AppletFilterDef> filterList = _currFormAppletDef.getChildListFilterDefs(childAppletName);
        if (!filterList.isEmpty()) {
            final Date now = getNow();
            final EntityDef entityDef = form.getFormDef().getEntityDef();
            final ValueStoreReader reader = form.getFormValueStoreReader();
            for (AppletFilterDef filterDef : filterList) {
                if (filterDef.isHideAddWidgetChildListAction()) {
                    ObjectFilter filter = filterDef.getFilterDef().getObjectFilter(entityDef, reader, now);
                    if (filter.matchReader(reader)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void onMiniformSwitchOnChange(MiniForm form) throws UnifyException {
        form.getCtx().resetTabIndex();

        final EntityDef entityDef = form.getCtx().getEntityDef();
        final Entity inst = (Entity) form.getFormBean();
        final FormTabDef formTabDef = form.getFormTabDef();
        // Clear unsatisfactory foreign key fields
        if (formTabDef.isWithCondRefDefFormFields()) {
            clearUnsatisfactoryRefs(formTabDef, entityDef, form.getCtx().getFormValueStore().getReader(), inst);
        }

        // Populate entity list-only fields
        populateListOnlyFields(entityDef, inst);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void clearUnsatisfactoryRefs(FormTabDef _formTabDef, EntityDef entityDef, ValueStoreReader _reader,
            Entity inst) throws UnifyException {
        final Date now = getNow();
        int depth = 4;
        boolean change = true;
        while ((--depth >= 0) && change) {
            change = false;
            for (FormFieldDef _formFieldDef : _formTabDef.getCondRefDefFormFieldDefList()) {
                Long currVal = DataUtils.getBeanProperty(Long.class, inst, _formFieldDef.getFieldName());
                if (currVal != null) {
                    RefDef refDef = _formFieldDef.getInputRefDef();
                    EntityClassDef _entityClassDef = applicationModuleService.getEntityClassDef(refDef.getEntity());
                    Restriction br = refDef.isWithFilter()
                            ? refDef.getFilter().getRestriction(_entityClassDef.getEntityDef(), _reader, now)
                            : null;

                    Query<? extends Entity> query = br != null
                            ? Query.ofDefaultingToAnd((Class<? extends Entity>) _entityClassDef.getEntityClass(), br)
                            : Query.of((Class<? extends Entity>) _entityClassDef.getEntityClass());
                    query.addEquals("id", currVal);
                    if (environmentService.countAll(query) == 0) {
                        DataUtils.setBeanProperty(inst, _formFieldDef.getFieldName(), null);
                        change = true;
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void populateListOnlyFields(EntityDef entityDef, Entity inst) throws UnifyException {
        for (EntityFieldDef entityFieldDef : entityDef.getFkFieldDefList()) {
            final String fkFieldName = entityFieldDef.getFieldName();
            if (entityFieldDef.isEnumGroup()) {
                String key = DataUtils.getBeanProperty(String.class, inst, fkFieldName);
                if (key == null) {
                    clearListOnlyFields(entityDef, fkFieldName, inst);
                } else {
                    Listable listable = getListItemByKey(entityFieldDef.getReferences(), key);
                    for (EntityFieldDef _entityFieldDef : entityDef.getListOnlyFieldDefList()) {
                        if (_entityFieldDef.getKey().equals(fkFieldName)) {
                            String val = "description".equals(_entityFieldDef.getProperty())
                                    ? listable.getListDescription()
                                    : listable.getListKey();
                            DataUtils.setBeanProperty(inst, _entityFieldDef.getFieldName(), val);
                        }
                    }
                }
            } else {
                Long id = DataUtils.getBeanProperty(Long.class, inst, fkFieldName);
                if (id == null) {
                    clearListOnlyFields(entityDef, fkFieldName, inst);
                } else {
                    final RefDef refDef = applicationModuleService.getRefDef(entityFieldDef.getRefLongName());
                    EntityClassDef _parentEntityClassDef = getEntityClassDef(refDef.getEntity());
                    Entity parentInst = environmentService
                            .listLean((Class<? extends Entity>) _parentEntityClassDef.getEntityClass(), id);
                    copyListOnlyFieldsFromForeignEntity(entityDef, fkFieldName, inst, parentInst);
                }
            }
        }
    }

    @Override
    public String getEntityDescription(EntityClassDef entityClassDef, Entity inst, String fieldName)
            throws UnifyException {
        return applicationModuleService.getEntityDescription(entityClassDef, inst, fieldName);
    }

    @Override
    public DelegateEntityInfo getEntityDelegate(String entityName) throws UnifyException {
        return applicationModuleService.getDelegateEntity(entityName);
    }

    @Override
    public List<DelegateEntityInfo> getDelegateEntitiesByDelegate(String delegate) throws UnifyException {
        return applicationModuleService.getDelegateEntitiesByDelegate(delegate);
    }

    @Override
    public List<DelegateEntityInfo> getDelegateEntitiesByDataSource(String dataSourceName) throws UnifyException {
        return applicationModuleService.getDelegateEntitiesByDataSource(dataSourceName);
    }

    @Override
    public List<DelegateEntityInfo> getDelegateEntities() throws UnifyException {
        return applicationModuleService.getDelegateEntities();
    }

    @Override
    public EntityActionResult createEntityInstByFormContext(final AppletDef formAppletDef,
            final FormContext formContext, final SweepingCommitPolicy scp) throws UnifyException {
        final Entity inst = (Entity) formContext.getInst();
        final FormDef _formDef = formContext.getFormDef();
        final EntityDef _entityDef = _formDef.getEntityDef();

        if (formAppletDef.getPropValue(boolean.class, AppletPropertyConstants.WORKFLOWCOPY)) {
            final String wfCopyCreateSetValuesName = formAppletDef.getPropValue(String.class,
                    AppletPropertyConstants.WORKFLOWCOPY_CREATE_COPY_SETVALUES);
            if (!StringUtils.isBlank(wfCopyCreateSetValuesName)) {
                AppletSetValuesDef appletSetValuesDef = formAppletDef.getSetValues(wfCopyCreateSetValuesName);
                appletSetValuesDef.getSetValuesDef().apply(this, _entityDef, getNow(), inst, Collections.emptyMap(),
                        null);
            }
        }

        final String createPolicy = formAppletDef != null
                ? formAppletDef.getPropValue(String.class, AppletPropertyConstants.CREATE_FORM_NEW_POLICY)
                : formContext.getAttribute(String.class, AppletPropertyConstants.CREATE_FORM_NEW_POLICY);
        EntityActionContext eCtx = new EntityActionContext(_entityDef, inst, RecordActionType.CREATE, scp,
                createPolicy);
        eCtx.setAll(formContext);

        // Populate values for auto-format fields
        populateAutoFormatFields(_entityDef, inst);

        // Apply delayed set values here since set values can depend on auto-format
        // fields
        applyDelayedSetValues(formContext);

        return create(formContext, eCtx);
    }

    @Override
    public void populateAutoFormatFields(EntityDef _entityDef, Entity inst) throws UnifyException {
        SequenceCodeGenerator gen = sequenceCodeGenerator();
        if (_entityDef.isWithAutoFormatFields()) {
            final ValueStoreReader reader = new BeanValueStore(inst).getReader();
            for (EntityFieldDef entityFieldDef : _entityDef.getAutoFormatFieldDefList()) {
                if (entityFieldDef.isStringAutoFormat()) {
                    String skeleton = gen.getCodeSkeleton(entityFieldDef.getAutoFormat());
                    final String _val = DataUtils.getBeanProperty(String.class, inst, entityFieldDef.getFieldName());
                    if (_val == null || skeleton.equals(_val)) {
                        DataUtils.setBeanProperty(inst, entityFieldDef.getFieldName(), gen
                                .getNextSequenceCode(_entityDef.getLongName(), entityFieldDef.getAutoFormat(), reader));
                    }
                }
            }
        }
    }

    @Override
    public void revertAutoFormatFields(EntityDef _entityDef, Entity inst) throws UnifyException {
        SequenceCodeGenerator gen = sequenceCodeGenerator();
        if (_entityDef.isWithAutoFormatFields()) {
            for (EntityFieldDef entityFieldDef : _entityDef.getAutoFormatFieldDefList()) {
                if (entityFieldDef.isStringAutoFormat()) {
                    DataUtils.setBeanProperty(inst, entityFieldDef.getFieldName(),
                            gen.getCodeSkeleton(entityFieldDef.getAutoFormat()));
                }
            }
        }
    }

    @Override
    public EntityActionResult createEntityInstWorkflowDraftByFormContext(AppletDef formAppletDef,
            FormContext formContext, SweepingCommitPolicy scp) throws UnifyException {
        Entity inst = (Entity) formContext.getInst();
        // Editable child records should be replicated
        environment().findEditableChildren(inst);

        // Create workflow copy
        EntityClassDef entityClassDef = getEntityClassDef(formAppletDef.getEntity());
        ValueStore wfCopyValueStore = new BeanValueStore(entityClassDef.newInst());
        wfCopyValueStore.copyWithExclusions(new BeanValueStore(inst), ApplicationEntityUtils.RESERVED_BASE_FIELDS);
        final String wfCopyUpdateSetValuesName = formAppletDef.getPropValue(String.class,
                AppletPropertyConstants.WORKFLOWCOPY_UPDATE_COPY_SETVALUES);
        if (!StringUtils.isBlank(wfCopyUpdateSetValuesName)) {
            AppletSetValuesDef appletSetValuesDef = formAppletDef.getSetValues(wfCopyUpdateSetValuesName);
            appletSetValuesDef.getSetValuesDef().apply(this, entityClassDef.getEntityDef(), getNow(), wfCopyValueStore,
                    Collections.emptyMap(), null);
        }

        WorkEntity copyInst = (WorkEntity) wfCopyValueStore.getValueObject();
        copyInst.setWfItemVersionType(WfItemVersionType.DRAFT);
        copyInst.setOriginalCopyId((Long) inst.getId());

        final String createPolicy = formAppletDef != null
                ? formAppletDef.getPropValue(String.class, AppletPropertyConstants.CREATE_FORM_NEW_POLICY)
                : formContext.getAttribute(String.class, AppletPropertyConstants.CREATE_FORM_NEW_POLICY);
        EntityActionContext eCtx = new EntityActionContext(entityClassDef.getEntityDef(), copyInst,
                RecordActionType.CREATE, scp, createPolicy);
        eCtx.setAll(formContext);
        eCtx.setWorkflowCopied(true);
        return create(formContext, eCtx);
    }

    @Override
    public EntityActionResult updateEntityInstByFormContext(AppletDef formAppletDef, FormContext formContext,
            SweepingCommitPolicy scp) throws UnifyException {
        Entity inst = (Entity) formContext.getInst();
        String updatePolicy = formAppletDef != null
                ? formAppletDef.getPropValue(String.class, AppletPropertyConstants.MAINTAIN_FORM_UPDATE_POLICY)
                : formContext.getAttribute(String.class, AppletPropertyConstants.MAINTAIN_FORM_UPDATE_POLICY);
        final EntityDef entityDef = formContext.getFormDef().getEntityDef();
        EntityActionContext eCtx = new EntityActionContext(entityDef, inst, RecordActionType.UPDATE, scp, updatePolicy);
        eCtx.setAll(formContext);

        // Ensure values for auto-format fields
        ensureAutoFormatFields(entityDef, inst);

        applyDelayedSetValues(formContext);

        return updateLean(formContext, eCtx);
    }

    @Override
    public EntityActionResult deleteEntityInstByFormContext(AppletDef formAppletDef, FormContext formContext,
            SweepingCommitPolicy scp) throws UnifyException {
        Entity inst = (Entity) formContext.getInst();
        final EntityDef _entityDef = formContext.getFormDef().getEntityDef();
        boolean pseudoDelete = formAppletDef.getPropValue(boolean.class,
                AppletPropertyConstants.MAINTAIN_FORM_DELETE_PSEUDO, false);
        EntityActionResult entityActionResult = null;
        final String deletePolicy = formAppletDef != null
                ? formAppletDef.getPropValue(String.class, AppletPropertyConstants.MAINTAIN_FORM_DELETE_POLICY)
                : formContext.getAttribute(String.class, AppletPropertyConstants.MAINTAIN_FORM_DELETE_POLICY);
        if (pseudoDelete) {
            final String pseudoDeleteSetValuesName = formAppletDef.getPropValue(String.class,
                    AppletPropertyConstants.MAINTAIN_FORM_DELETE_PSEUDO_SETVALUES);
            if (!StringUtils.isBlank(pseudoDeleteSetValuesName)) {
                AppletSetValuesDef appletSetValuesDef = formAppletDef.getSetValues(pseudoDeleteSetValuesName);
                appletSetValuesDef.getSetValuesDef().apply(this, _entityDef, getNow(), inst, Collections.emptyMap(),
                        null);
            }

            EntityActionContext eCtx = new EntityActionContext(_entityDef, inst, RecordActionType.UPDATE, scp,
                    deletePolicy);
            eCtx.setAll(formContext);
            entityActionResult = updateLean(formContext, eCtx);
        } else {
            EntityActionContext eCtx = new EntityActionContext(_entityDef, inst, RecordActionType.DELETE, scp,
                    deletePolicy);
            eCtx.setAll(formContext);
            entityActionResult = delete(formContext, eCtx);
        }

        return entityActionResult;
    }

    @Override
    public void onFormConstruct(AppletDef formAppletDef, FormContext formContext, String baseField, boolean create)
            throws UnifyException {
        final ValueStore formValueStore = formContext.getFormValueStore();
        final FormDef formDef = formContext.getFormDef();
        final EntityDef entityDef = formDef.getEntityDef();
        formContext.setFixedReference(baseField);
        logDebug("Executing on-form-construct [{0}] using applet [{1}] and base field [{2}]...", formDef.getLongName(),
                formAppletDef.getLongName(), baseField);

        final Entity inst = (Entity) formContext.getInst();
        final Date now = getNow();
        if (create) {
            // Apply create state policy
            String onCreateStatePolicy = formAppletDef.getPropValue(String.class,
                    AppletPropertyConstants.CREATE_FORM_STATE_POLICY);
            if (!StringUtils.isBlank(onCreateStatePolicy)) {
                logDebug("Applying on-create state policy [{0}] on form [{1}]...", onCreateStatePolicy, inst.getId());
                FormStatePolicyDef onCreateFormStatePolicyDef = formDef
                        .getOnCreateFormStatePolicyDef(onCreateStatePolicy);
                if (onCreateFormStatePolicyDef.isSetValues()) {
                    applySetValues(onCreateFormStatePolicyDef, entityDef, formValueStore, now);
                }
            }

            // Populate skeleton for auto-format fields
            if (entityDef.isWithAutoFormatFields()) {
                logDebug("Populating auto-format fields for form [{0}]...", inst.getId());
                final SequenceCodeGenerator gen = sequenceCodeGenerator();
                for (EntityFieldDef entityFieldDef : entityDef.getAutoFormatFieldDefList()) {
                    if (entityFieldDef.isStringAutoFormat()) {
                        DataUtils.setBeanProperty(inst, entityFieldDef.getFieldName(),
                                gen.getCodeSkeleton(entityFieldDef.getAutoFormat()));
                    }
                }
            }
        }

        if (formDef.isWithConsolidatedFormState()) {
            ConsolidatedFormStatePolicy policy = getComponent(ConsolidatedFormStatePolicy.class,
                    formDef.getConsolidatedFormState());
            logDebug("Applying consolidated form state policy [{0}] on form [{1}]...", policy.getName(), inst.getId());
            policy.onFormConstruct(formValueStore);
        }

        // Fire on form construct value generators
        logDebug("Populating on-form-construct set values for form [{0}]...", inst.getId());
        for (FormStatePolicyDef formStatePolicyDef : formDef.getOnFormConstructSetValuesFormStatePolicyDefList()) {
            applySetValues(formStatePolicyDef, entityDef, formValueStore, now);
        }
    }

    private void applySetValues(FormStatePolicyDef formStatePolicyDef, EntityDef entityDef, ValueStore valueStore,
            Date now) throws UnifyException {
        if (formStatePolicyDef.isSetValues()) {
            ObjectFilter objectFilter = formStatePolicyDef.isWithCondition()
                    ? formStatePolicyDef.getOnCondition().getObjectFilter(entityDef, valueStore.getReader(), now)
                    : null;
            if (objectFilter == null || objectFilter.matchReader(valueStore.getReader())) {
                formStatePolicyDef.getSetValuesDef().apply(this, entityDef, now, valueStore, Collections.emptyMap(),
                        null);
            }
        }
    }

    @Override
    public byte[] generateViewListingReportAsByteArray(ValueStoreReader reader,
            List<GenerateListingReportOptions> options) throws UnifyException {
        final Report report = generateViewListingReport(reader, options);
        return reportProvider.generateReportAsByteArray(report);
    }

    @Override
    public byte[] generateViewListingReportAsByteArray(ValueStoreReader reader, String generator,
            FormListingOptions options) throws UnifyException {
        final Report report = generateViewListingReport(reader, generator, options);
        return reportProvider.generateReportAsByteArray(report);
    }

    @Override
    public byte[] generateDetailListingReportAsByteArray(ValueStoreReader reader, String tableName,
            List<? extends Entity> dataList, String generator, Map<String, Object> properties, Formats formats,
            boolean asSpreadSheet) throws UnifyException {
        return generateDetailListingReportAsByteArray(reader, tableName, dataList, generator, properties, formats,
                asSpreadSheet, Collections.emptyList(), Collections.emptyList(), 0);
    }

    @Override
    public byte[] generateDetailListingReportAsByteArray(ValueStoreReader reader, String tableName,
            List<? extends Entity> dataList, String generator, Map<String, Object> properties, Formats formats,
            boolean asSpreadSheet, List<TableSummaryLine> preSummaryLines, List<TableSummaryLine> postSummaryLines,
            int summaryTitleColumn) throws UnifyException {
        final Report report = generateDetailListingReport(reader, tableName, dataList, generator, properties, formats,
                asSpreadSheet, preSummaryLines, postSummaryLines, summaryTitleColumn);
        return reportProvider.generateReportAsByteArray(report);
    }

    @Override
    public byte[] generateDetailListingReportAsByteArray(ValueStoreReader reader, List<DetailsCase> caseList,
            String generator, Map<String, Object> properties, int columns, boolean asSpreadSheet)
            throws UnifyException {
        final Report report = generateDetailListingReport(reader, caseList, generator, properties, columns,
                asSpreadSheet);
        return reportProvider.generateReportAsByteArray(report);
    }

    @Override
    public byte[] generateLetterListingReportAsByteArray(ValueStoreReader reader, String letterGenerator)
            throws UnifyException {
        final Report report = generateLetterListingReport(reader, letterGenerator);
        return reportProvider.generateReportAsByteArray(report);
    }

    @Override
    public void generateViewListingReportToOutputStream(OutputStream outputStream, ValueStoreReader reader,
            List<GenerateListingReportOptions> options) throws UnifyException {
        final Report report = generateViewListingReport(reader, options);
        reportProvider.generateReport(report, outputStream);
    }

    @Override
    public void generateViewListingReportToOutputStream(OutputStream outputStream, ValueStoreReader reader,
            String generator, FormListingOptions options) throws UnifyException {
        final Report report = generateViewListingReport(reader, generator, options);
        reportProvider.generateReport(report, outputStream);
    }

    @Override
    public void generateDetailListingReportToOutputStream(OutputStream outputStream, ValueStoreReader reader,
            String tableName, List<? extends Entity> dataList, String generator, Map<String, Object> properties,
            Formats formats, boolean asSpreadSheet) throws UnifyException {
        generateDetailListingReportToOutputStream(outputStream, reader, tableName, dataList, generator, properties,
                formats, asSpreadSheet, Collections.emptyList(), Collections.emptyList(), 0);
    }

    @Override
    public void generateDetailListingReportToOutputStream(OutputStream outputStream, ValueStoreReader reader,
            String tableName, List<? extends Entity> dataList, String generator, Map<String, Object> properties,
            Formats formats, boolean asSpreadSheet, List<TableSummaryLine> preSummaryLines,
            List<TableSummaryLine> postSummaryLines, int summaryTitleColumn) throws UnifyException {
        final Report report = generateDetailListingReport(reader, tableName, dataList, generator, properties, formats,
                asSpreadSheet, preSummaryLines, postSummaryLines, summaryTitleColumn);
        reportProvider.generateReport(report, outputStream);
    }

    @Override
    public void generateDetailListingReportToOutputStream(OutputStream outputStream, ValueStoreReader reader,
            List<DetailsCase> caseList, String generator, Map<String, Object> properties, int columns,
            boolean asSpreadSheet) throws UnifyException {
        final Report report = generateDetailListingReport(reader, caseList, generator, properties, columns,
                asSpreadSheet);
        reportProvider.generateReport(report, outputStream);
    }

    @Override
    public void generateLetterListingReportToOutputStream(OutputStream outputStream, ValueStoreReader reader,
            String letterGenerator) throws UnifyException {
        final Report report = generateLetterListingReport(reader, letterGenerator);
        reportProvider.generateReport(report, outputStream);
    }

    @Override
    public Report generateViewListingReport(ValueStoreReader reader, List<GenerateListingReportOptions> goptions)
            throws UnifyException {
        Report.Builder rb = Report.newBuilder(ReportLayoutType.MULTIDOCHTML_PDF).title("listingReport");
        for (GenerateListingReportOptions _goptions : goptions) {
            final FormListingOptions options = _goptions.getFormListingOptions();
            logDebug("Generating view listing report using generator [{0}] and options [{1}]...",
                    _goptions.getGenerator(), options);
            final FormListingGenerator _generator = (FormListingGenerator) getComponent(_goptions.getGenerator());
            final int optionFlags = options.isImportant() ? 0 : _generator.getOptionFlagsOverride(reader);
            FormListingOptions _options = optionFlags == 0 ? options
                    : new FormListingOptions(options.getFormActionName(), optionFlags);
            _options.setOptionsName(_goptions.getOptionsName());
            logDebug("Using resolved option [{0}]...", _options);
            if (reader != null) {
                reader.setFormats(_generator.getFormats());
            }

            _generator.generateHtmlReport(rb, reader, _options);
        }

        return rb.build();
    }

    @Override
    public Report generateViewListingReport(ValueStoreReader reader, String generator, FormListingOptions options)
            throws UnifyException {
        logDebug("Generating view listing report using generator [{0}] and options [{1}]...", generator, options);
        final FormListingGenerator _generator = (FormListingGenerator) getComponent(generator);
        final int optionFlags = options.isImportant() ? 0 : _generator.getOptionFlagsOverride(reader);
        FormListingOptions _options = optionFlags == 0 ? options
                : new FormListingOptions(options.getFormActionName(), optionFlags);
        logDebug("Using resolved option [{0}]...", _options);
        if (reader != null) {
            reader.setFormats(_generator.getFormats());
        }

        return _generator.generateHtmlReport(reader, _options);
    }

    @Override
    public Report generateDetailListingReport(ValueStoreReader reader, String tableName,
            List<? extends Entity> dataList, String generator, Map<String, Object> properties, Formats formats,
            boolean asSpreadSheet) throws UnifyException {
        return generateDetailListingReport(reader, tableName, dataList, generator, properties, formats, asSpreadSheet,
                Collections.emptyList(), Collections.emptyList(), 0);
    }

    @Override
    public Report generateDetailListingReport(ValueStoreReader reader, String tableName,
            List<? extends Entity> dataList, String generator, Map<String, Object> properties, Formats formats,
            boolean asSpreadSheet, List<TableSummaryLine> preSummaryLines, List<TableSummaryLine> postSummaryLines,
            int summaryTitleColumn) throws UnifyException {
        DetailsFormListing.Builder lb = DetailsFormListing.newBuilder().usingGenerator(generator)
                .addProperties(properties).asSpreadSheet(asSpreadSheet);
        lb.beginCase();
        lb.tableName(tableName);
        lb.withContent(dataList);
        lb.usingFormats(formats);
        if (preSummaryLines != null) {
            for (TableSummaryLine line : preSummaryLines) {
                lb.addPreSummary(new Summary(line.getLabel(), line.values()));
            }
        }

        if (postSummaryLines != null) {
            for (TableSummaryLine line : postSummaryLines) {
                lb.addPostSummary(new Summary(line.getLabel(), line.values()));
            }
        }

        lb.summaryTitleColumn(summaryTitleColumn);
        lb.endCase();

        final DetailsFormListing listing = lb.build();
        final DetailsFormListingGenerator _generator = (DetailsFormListingGenerator) getComponent(
                listing.getGenerator());
        if (reader != null) {
            reader.setFormats(_generator.getFormats());
        }

        final FormListingOptions options = new FormListingOptions(listing);
        return listing.isSpreadSheet() ? _generator.generateExcelReport(reader, options)
                : _generator.generateHtmlReport(reader, options);
    }

    @Override
    public Report generateDetailListingReport(ValueStoreReader reader, List<DetailsCase> caseList, String generator,
            Map<String, Object> properties, int columns, boolean asSpreadSheet) throws UnifyException {
        DetailsFormListing.Builder lb = DetailsFormListing.newBuilder().usingGenerator(generator)
                .addProperties(properties).addCases(caseList).columns(columns).asSpreadSheet(asSpreadSheet);
        final DetailsFormListing listing = lb.build();
        final DetailsFormListingGenerator _generator = (DetailsFormListingGenerator) getComponent(
                listing.getGenerator());
        if (reader != null) {
            reader.setFormats(_generator.getFormats());
        }

        final FormListingOptions options = new FormListingOptions(listing);
        return listing.isSpreadSheet() ? _generator.generateExcelReport(reader, options)
                : _generator.generateHtmlReport(reader, options);
    }

    @Override
    public Report generateLetterListingReport(ValueStoreReader reader, String letterGenerator) throws UnifyException {
        final LetterFormListingGenerator _generator = (LetterFormListingGenerator) getComponent(letterGenerator);
        final LetterFormListing letterFormListing = new LetterFormListing(letterGenerator);
        if (reader != null) {
            reader.setFormats(_generator.getFormats());
        }

        return _generator.generateHtmlReport(reader, new FormListingOptions(letterFormListing));
    }

    private void ensureAutoFormatFields(EntityDef _entityDef, Entity inst) throws UnifyException {
        SequenceCodeGenerator gen = sequenceCodeGenerator();
        if (_entityDef.isWithAutoFormatFields()) {
            final ValueStoreReader reader = new BeanValueStore(inst).getReader();
            for (EntityFieldDef entityFieldDef : _entityDef.getAutoFormatFieldDefList()) {
                if (entityFieldDef.isStringAutoFormat()) {
                    final String val = DataUtils.getBeanProperty(String.class, inst, entityFieldDef.getFieldName());
                    String skeleton = gen.getCodeSkeleton(entityFieldDef.getAutoFormat());
                    if (val == null || skeleton.equals(val)) {
                        DataUtils.setBeanProperty(inst, entityFieldDef.getFieldName(), gen
                                .getNextSequenceCode(_entityDef.getLongName(), entityFieldDef.getAutoFormat(), reader));
                    }
                }
            }
        }
    }

    private void applyDelayedSetValues(final FormContext formContext) throws UnifyException {
        final FormDef _formDef = formContext.getFormDef();
        final ValueStore _formValueStore = formContext.getFormValueStore();
        final ValueStoreReader _formValueStoreReader = formContext.getFormValueStore().getReader();
        final Date now = getNow();
        logDebug("Applying delayed set values on [{0}] using form definition [{1}] ...",
                _formValueStoreReader.read("id"), _formDef.getLongName());
        // Execute delayed set values
        final Map<String, Object> variables = Collections.emptyMap();
        for (FormStatePolicyDef formStatePolicyDef : _formDef.getOnDelayedSetValuesFormStatePolicyDefList()) {
            ObjectFilter objectFilter = formStatePolicyDef.isWithCondition()
                    ? formStatePolicyDef.getOnCondition().getObjectFilter(_formDef.getEntityDef(),
                            _formValueStoreReader, now)
                    : null;
            if (objectFilter == null || objectFilter.matchReader(_formValueStoreReader)) {
                if (formStatePolicyDef.isSetValues()) {
                    formStatePolicyDef.getSetValuesDef().apply(this, _formDef.getEntityDef(), now, _formValueStore,
                            variables, null);
                }
            }
        }
    }

    private EntityActionResult create(FormContext formContext, EntityActionContext eCtx) throws UnifyException {
        EntityActionResult entityActionResult = null;
        try {
            entityActionResult = environment().create(eCtx);
            entityActionResult.setWorkflowCopied(eCtx.isWorkflowCopied());
        } catch (UnifyException e) {
            // Revert to skeleton values
            revertAutoFormatFields(eCtx.getEntityDef(EntityDef.class), eCtx.getInst());

            if (!eCtx.isWithFormMessages()) {
                throw e;
            }
        }

        formContext.addValidationErrors(eCtx.getFormMessages());
        return entityActionResult == null ? new EntityActionResult(eCtx) : entityActionResult;
    }

    private EntityActionResult updateLean(FormContext formContext, EntityActionContext eCtx) throws UnifyException {
        EntityActionResult entityActionResult = null;
        try {
            entityActionResult = environment().updateLean(eCtx);
        } catch (UnifyException e) {
            if (!eCtx.isWithFormMessages()) {
                throw e;
            }
        }

        formContext.addValidationErrors(eCtx.getFormMessages());
        return entityActionResult == null ? new EntityActionResult(eCtx) : entityActionResult;
    }

    private EntityActionResult delete(FormContext formContext, EntityActionContext eCtx) throws UnifyException {
        EntityActionResult entityActionResult = null;
        try {
            entityActionResult = environment().delete(eCtx);
        } catch (UnifyException e) {
            if (!eCtx.isWithFormMessages()) {
                throw e;
            }
        }

        formContext.addValidationErrors(eCtx.getFormMessages());
        return entityActionResult == null ? new EntityActionResult(eCtx) : entityActionResult;
    }

    private SingleFormBean createSingleFormBeanForPanel(String panelName) throws UnifyException {
        Class<? extends SingleFormBean> beanClass = singleFormBeanClassByPanelName.get(panelName);
        return ReflectUtils.newInstance(beanClass);
    }

    private void clearListOnlyFields(EntityDef entityDef, String fkFieldName, Entity inst) throws UnifyException {
        for (EntityFieldDef entityFieldDef : entityDef.getListOnlyFieldDefList()) {
            if (entityFieldDef.getKey().equals(fkFieldName)) {
                DataUtils.setBeanProperty(inst, entityFieldDef.getFieldName(), null);
            }
        }
    }

    private void copyListOnlyFieldsFromForeignEntity(EntityDef entityDef, String fkFieldName, Entity inst,
            Entity foreignInst) throws UnifyException {
        for (EntityFieldDef entityFieldDef : entityDef.getListOnlyFieldDefList()) {
            if (entityFieldDef.getKey().equals(fkFieldName)) {
                DataUtils.setBeanProperty(inst, entityFieldDef.getFieldName(),
                        DataUtils.getBeanProperty(Object.class, foreignInst, entityFieldDef.getProperty()));
            }
        }
    }

    private void setCollaborationContext(HeaderWithTabsForm form) throws UnifyException {
        boolean enterReadOnlyMode = false;
        if (collaborationProvider != null) {
            final BaseApplicationEntity _appInst = (BaseApplicationEntity) ((AbstractForm) form).getCtx().getInst();
            final CollaborationType type = ApplicationCollaborationUtils.getCollaborationType(_appInst.getClass());
            if (type != null) {
                final String resourceName = ApplicationNameUtils
                        .getApplicationEntityLongName(_appInst.getApplicationName(), _appInst.getName());
                enterReadOnlyMode = !collaborationProvider.isLockedBy(type, resourceName,
                        getUserToken().getUserLoginId());
            }
        }

        form.getCtx().getAppletContext().setReadOnly(enterReadOnlyMode);
    }

    private class MappedEntityProviderInfo {

        private Map<String, MappedEntityProvider<? extends BaseMappedEntityProviderContext>> providersByName;

        private Map<String, MappedEntityProvider<? extends BaseMappedEntityProviderContext>> providersByType;

        public MappedEntityProviderInfo(
                Map<String, MappedEntityProvider<? extends BaseMappedEntityProviderContext>> providers)
                throws UnifyException {
            this.providersByName = providers;
        }

        public boolean isProviderPresent(String destEntity) {
            return providersByName.containsKey(destEntity);
        }

        public MappedEntityProvider<? extends BaseMappedEntityProviderContext> getProvider(String destEntity) {
            return providersByName.get(destEntity);
        }

        public boolean isProviderPresent(Class<? extends Entity> destEntityClass) throws UnifyException {
            MappedEntityProvider<? extends BaseMappedEntityProviderContext> provider = getProvidersbyType()
                    .get(destEntityClass.getName());
            return provider != null && provider.isEntitiesDef();
        }

        public MappedEntityProvider<? extends BaseMappedEntityProviderContext> getProvider(
                Class<? extends Entity> destEntityClass) throws UnifyException {
            return getProvidersbyType().get(destEntityClass.getName());
        }

        private Map<String, MappedEntityProvider<? extends BaseMappedEntityProviderContext>> getProvidersbyType()
                throws UnifyException {
            if (providersByType == null) {
                synchronized (this) {
                    if (providersByType == null) {
                        providersByType = new HashMap<String, MappedEntityProvider<? extends BaseMappedEntityProviderContext>>();
                        for (String destEntity : providersByName.keySet()) {
                            EntityClassDef entityClassDef = getEntityClassDef(destEntity);
                            providersByType.put(entityClassDef.getEntityClass().getName(),
                                    providersByName.get(destEntity));
                        }

                        providersByType = Collections.unmodifiableMap(providersByType);
                    }
                }
            }

            return providersByType;
        }
    }

    private class AuRestrictionTranslatorMapper extends AbstractRestrictionTranslatorMapper {

        private EntityDef entityDef;

        public AuRestrictionTranslatorMapper(EntityDef entityDef) {
            this.entityDef = entityDef;
        }

        @Override
        public String getLabelTranslation(String fieldName) throws UnifyException {
            return entityDef.getFieldLabelMap().get(fieldName);
        }

        @Override
        public String getValueTranslation(String fieldName, Object val) throws UnifyException {
            EntityFieldDef entityFieldDef = entityDef.getFieldDef(fieldName);
            switch (entityFieldDef.getDataType()) {
                case ENUM:
                case ENUM_REF:
                    String key = val instanceof String ? (String) val : null;
                    if (key == null) {
                        key = val instanceof EnumConst ? ((EnumConst) val).code() : null;
                    }

                    if (key != null) {
                        return getListItemByKey(LocaleType.SESSION, entityFieldDef.getReferences(), key)
                                .getListDescription();
                    }
                    break;
                case ENUM_DYN:
                    break;
                case REF:
                case REF_UNLINKABLE:
                    RefDef refDef = applicationModuleService.getRefDef(entityFieldDef.getReferences());
                    val = val instanceof String ? Long.valueOf((String) val) : val;
                    return val != null ? applicationModuleService.getEntityDescription(
                            applicationModuleService.getEntityClassDef(refDef.getEntity()), (Long) val,
                            refDef.getSearchField()) : "";
                case LIST_ONLY:
                case BLOB:
                case BOOLEAN:
                case CHAR:
                case CHILD:
                case CHILD_LIST:
                case REF_FILEUPLOAD:
                case CLOB:
                case DATE:
                case DECIMAL:
                case DOUBLE:
                case FLOAT:
                case INTEGER:
                case LONG:
                case TENANT_ID:
                case MAPPED:
                case SCRATCH:
                case SHORT:
                case STRING:
                case TIMESTAMP:
                case TIMESTAMP_UTC:
                default:
                    break;

            }

            return null;
        }

    }
}

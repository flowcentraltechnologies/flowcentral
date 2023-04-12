/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.workflow.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.ApplicationAppletDefProvider;
import com.flowcentraltech.flowcentral.application.business.EmailListProducerConsumer;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleErrorConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.application.constants.ProcessVariable;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.Comments;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.Errors;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.InputValue;
import com.flowcentraltech.flowcentral.application.data.SetValuesDef;
import com.flowcentraltech.flowcentral.application.data.StandardAppletDef;
import com.flowcentraltech.flowcentral.application.data.WidgetTypeDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationPageUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameParts;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.application.validation.Validator;
import com.flowcentraltech.flowcentral.application.web.widgets.InputArrayEntries;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.business.FileAttachmentProvider;
import com.flowcentraltech.flowcentral.common.business.NotificationRecipientProvider;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.WfBinaryPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.WfEnrichmentPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.WfProcessPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.WfRecipientPolicy;
import com.flowcentraltech.flowcentral.common.constants.CommonTempValueNameConstants;
import com.flowcentraltech.flowcentral.common.constants.ProcessErrorConstants;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.common.data.WfEntityInst;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.flowcentraltech.flowcentral.configuration.constants.AppletType;
import com.flowcentraltech.flowcentral.configuration.constants.DefaultApplicationConstants;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepType;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.notification.senders.NotificationAlertSender;
import com.flowcentraltech.flowcentral.organization.business.OrganizationModuleService;
import com.flowcentraltech.flowcentral.system.constants.SystemModuleSysParamConstants;
import com.flowcentraltech.flowcentral.workflow.constants.WfAppletPropertyConstants;
import com.flowcentraltech.flowcentral.workflow.constants.WfChannelErrorConstants;
import com.flowcentraltech.flowcentral.workflow.constants.WfChannelStatus;
import com.flowcentraltech.flowcentral.workflow.constants.WfReviewMode;
import com.flowcentraltech.flowcentral.workflow.constants.WfWizardAppletPropertyConstants;
import com.flowcentraltech.flowcentral.workflow.constants.WorkflowModuleErrorConstants;
import com.flowcentraltech.flowcentral.workflow.constants.WorkflowModuleNameConstants;
import com.flowcentraltech.flowcentral.workflow.constants.WorkflowModuleSysParamConstants;
import com.flowcentraltech.flowcentral.workflow.data.WfAlertDef;
import com.flowcentraltech.flowcentral.workflow.data.WfChannelDef;
import com.flowcentraltech.flowcentral.workflow.data.WfDef;
import com.flowcentraltech.flowcentral.workflow.data.WfFilterDef;
import com.flowcentraltech.flowcentral.workflow.data.WfRoutingDef;
import com.flowcentraltech.flowcentral.workflow.data.WfSetValuesDef;
import com.flowcentraltech.flowcentral.workflow.data.WfStepDef;
import com.flowcentraltech.flowcentral.workflow.data.WfStepSetValuesDef;
import com.flowcentraltech.flowcentral.workflow.data.WfUserActionDef;
import com.flowcentraltech.flowcentral.workflow.data.WfWizardDef;
import com.flowcentraltech.flowcentral.workflow.data.WorkEntityItem;
import com.flowcentraltech.flowcentral.workflow.data.WorkEntitySingleFormItem;
import com.flowcentraltech.flowcentral.workflow.entities.WfChannel;
import com.flowcentraltech.flowcentral.workflow.entities.WfChannelQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WfItem;
import com.flowcentraltech.flowcentral.workflow.entities.WfItemEvent;
import com.flowcentraltech.flowcentral.workflow.entities.WfItemEventQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WfItemHist;
import com.flowcentraltech.flowcentral.workflow.entities.WfStep;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepAlert;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepRole;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepRoleQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepRouting;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepUserAction;
import com.flowcentraltech.flowcentral.workflow.entities.WfTransitionQueue;
import com.flowcentraltech.flowcentral.workflow.entities.WfTransitionQueueQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WfWizard;
import com.flowcentraltech.flowcentral.workflow.entities.WfWizardItemQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WfWizardQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WfWizardStep;
import com.flowcentraltech.flowcentral.workflow.entities.Workflow;
import com.flowcentraltech.flowcentral.workflow.entities.WorkflowFilter;
import com.flowcentraltech.flowcentral.workflow.entities.WorkflowFilterQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WorkflowQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WorkflowSetValues;
import com.flowcentraltech.flowcentral.workflow.entities.WorkflowSetValuesQuery;
import com.flowcentraltech.flowcentral.workflow.util.WorkflowEntityUtils;
import com.flowcentraltech.flowcentral.workflow.util.WorkflowNameUtils;
import com.flowcentraltech.flowcentral.workflow.util.WorkflowNameUtils.WfAppletNameParts;
import com.flowcentraltech.flowcentral.workflow.util.WorkflowNameUtils.WfWizardAppletNameParts;
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Periodic;
import com.tcdng.unify.core.annotation.PeriodicType;
import com.tcdng.unify.core.annotation.TransactionAttribute;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.constant.FrequencyUnit;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.IsNull;
import com.tcdng.unify.core.criterion.Or;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.ParameterizedStringGenerator;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.data.ValueStoreWriter;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.database.sql.SqlDataSourceDialect;
import com.tcdng.unify.core.database.sql.SqlEntityInfo;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Default workflow business service implementation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(WorkflowModuleNameConstants.WORKFLOW_MODULE_SERVICE)
public class WorkflowModuleServiceImpl extends AbstractFlowCentralService
        implements WorkflowModuleService, ApplicationAppletDefProvider {

    private static final String WFTRANSITION_QUEUE_LOCK = "wf::transitionqueue-lock";

    private static final String WFAUTOLOADING_LOCK = "wf::autoloading-lock";

    private static final String WORKFLOW_APPLICATION = "workflow";

    private static final String WORK_CATEGORY = "work";

    @Configurable
    private OrganizationModuleService organizationModuleService;

    @Configurable
    private AppletUtilities appletUtil;

    @Configurable
    private NotificationRecipientProvider notifRecipientProvider;

    @Configurable
    private FileAttachmentProvider fileAttachmentProvider;

    private final FactoryMap<String, WfDef> wfDefFactoryMap;

    private final FactoryMap<String, WfWizardDef> wfWizardDefFactoryMap;

    private final FactoryMap<String, WfChannelDef> wfChannelDefFactoryMap;

    public WorkflowModuleServiceImpl() {
        this.wfDefFactoryMap = new FactoryMap<String, WfDef>(true)
            {
                @Override
                protected boolean stale(String wfName, WfDef wfDef) throws Exception {
                    return (environment().value(long.class, "versionNo", new WorkflowQuery().id(wfDef.getId())) > wfDef
                            .getVersion())
                            || (wfDef.getEntityDef().getVersion() < appletUtil.application()
                                    .getEntityDef(wfDef.getEntity()).getVersion());
                }

                @Override
                protected WfDef create(String longName, Object... arg1) throws Exception {
                    ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
                    Workflow workflow = environment().list(new WorkflowQuery()
                            .applicationName(nameParts.getApplicationName()).name(nameParts.getEntityName()));
                    if (workflow == null) {
                        throw new UnifyException(WorkflowModuleErrorConstants.CANNOT_FIND_APPLICATION_WORKFLOW,
                                longName);
                    }

                    EntityClassDef entityClassDef = appletUtil.application().getEntityClassDef(workflow.getEntity());
                    List<StringToken> descFormat = !StringUtils.isBlank(workflow.getDescFormat())
                            ? StringUtils.breakdownParameterizedString(workflow.getDescFormat())
                            : Collections.emptyList();
                    WfDef.Builder wdb = WfDef.newBuilder(entityClassDef, descFormat, longName,
                            workflow.getDescription(), workflow.getId(), workflow.getVersionNo());

                    Set<String> filterNames = new HashSet<String>();
                    for (WorkflowFilter workflowFilter : workflow.getFilterList()) {
                        filterNames.add(workflowFilter.getName());
                        wdb.addFilterDef(new WfFilterDef(InputWidgetUtils.getFilterDef(appletUtil,
                                workflowFilter.getName(), workflowFilter.getDescription(),
                                workflowFilter.getFilterGenerator(), workflowFilter.getFilter())));
                    }

                    for (WorkflowSetValues workflowSetValues : workflow.getSetValuesList()) {
                        FilterDef onCondition = InputWidgetUtils.getFilterDef(appletUtil, workflowSetValues.getName(),
                                workflowSetValues.getDescription(), null, workflowSetValues.getOnCondition());
                        SetValuesDef setValues = InputWidgetUtils.getSetValuesDef(workflowSetValues.getName(),
                                workflowSetValues.getDescription(), workflowSetValues.getValueGenerator(),
                                workflowSetValues.getSetValues());
                        WfSetValuesDef wfSetValuesDef = new WfSetValuesDef(workflowSetValues.getType(),
                                workflowSetValues.getName(), workflowSetValues.getDescription(), onCondition,
                                setValues);
                        wdb.addWfSetValuesDef(wfSetValuesDef);
                    }

                    final boolean descriptiveButtons = appletUtil.system().getSysParameterValue(boolean.class,
                            SystemModuleSysParamConstants.SYSTEM_DESCRIPTIVE_BUTTONS_ENABLED);
                    for (WfStep wfStep : workflow.getStepList()) {
                        AppletDef appletDef = null;
                        if (wfStep.getType().isInteractive() && !StringUtils.isBlank(wfStep.getAppletName())) {
                            final boolean useraction = wfStep.getType().isUserAction();
                            AppletDef _stepAppletDef = appletUtil.application().getAppletDef(wfStep.getAppletName());
                            AppletType _reviewAppletType = _stepAppletDef.getType().isSingleForm()
                                    ? AppletType.REVIEW_SINGLEFORMWORKITEMS
                                    : AppletType.REVIEW_WORKITEMS;
                            final String appletName = WorkflowNameUtils.getWfAppletName(longName, wfStep.getName());
                            final String label = getApplicationMessage("workflow.applet.label", workflow.getLabel(),
                                    wfStep.getLabel());
                            final String assignDescField = null;
                            final String pseudoDeleteField = null;
                            StandardAppletDef.Builder adb = StandardAppletDef.newBuilder(_reviewAppletType, null, label,
                                    "tasks", assignDescField, pseudoDeleteField, 0, true, true, descriptiveButtons,
                                    appletName, label);
                            final String table = useraction ? "workflow.wfItemReviewTable"
                                    : "workflow.wfItemRecoveryTable";
                            final String update = useraction ? "true" : "false";
                            adb.addPropDef(AppletPropertyConstants.SEARCH_TABLE, table);
                            adb.addPropDef(AppletPropertyConstants.MAINTAIN_FORM_UPDATE, update);
                            adb.addPropDef(AppletPropertyConstants.MAINTAIN_FORM_DELETE, "false");
                            adb.addPropDef(WfAppletPropertyConstants.WORKFLOW, longName);
                            adb.addPropDef(WfAppletPropertyConstants.WORKFLOW_STEP, wfStep.getName());
                            adb.addPropDef(WfAppletPropertyConstants.WORKFLOW_STEP_APPLET, wfStep.getAppletName());

                            adb.openPath(
                                    ApplicationPageUtils.constructAppletOpenPagePath(_reviewAppletType, appletName));
                            adb.originApplicationName(nameParts.getApplicationName());
                            appletDef = adb.build();
                        }

                        WfStepDef.Builder wsdb = WfStepDef.newBuilder(appletDef, wfStep.getType(), wfStep.getPriority(),
                                wfStep.getRecordActionType(), wfStep.getAppletName(), wfStep.getNextStepName(),
                                wfStep.getAltNextStepName(), wfStep.getBinaryConditionName(),
                                wfStep.getReadOnlyConditionName(), wfStep.getAutoLoadConditionName(),
                                wfStep.getNewCommentCaption(), wfStep.getPolicy(), wfStep.getRule(), wfStep.getName(),
                                wfStep.getDescription(), wfStep.getLabel(),
                                DataUtils.convert(int.class, wfStep.getCriticalMinutes()),
                                DataUtils.convert(int.class, wfStep.getExpiryMinutes()), wfStep.isAudit(),
                                wfStep.isBranchOnly(), wfStep.isIncludeForwarder(), wfStep.isForwarderPreffered(),
                                wfStep.getEmails(), wfStep.getComments());

                        if (wfStep.getSetValues() != null || !StringUtils.isBlank(wfStep.getValueGenerator())) {
                            wsdb.addWfSetValuesDef(new WfStepSetValuesDef(InputWidgetUtils.getSetValuesDef(
                                    wfStep.getValueGenerator(),
                                    wfStep.getSetValues() != null ? wfStep.getSetValues().getSetValues() : null)));
                        }

                        for (WfStepRouting wfStepRouting : wfStep.getRoutingList()) {
                            if (!StringUtils.isBlank(wfStepRouting.getConditionName())
                                    && !filterNames.contains(wfStepRouting.getConditionName())) {
                                throw new RuntimeException("Workflow with name [" + workflow.getName()
                                        + "] does not contain filter with name [" + wfStepRouting.getConditionName()
                                        + "].");
                            }

                            wsdb.addWfRoutingDef(wfStepRouting.getName(), wfStepRouting.getDescription(),
                                    wfStepRouting.getConditionName(), wfStepRouting.getNextStepName());
                        }

                        for (WfStepUserAction wfStepUserAction : wfStep.getUserActionList()) {
                            wsdb.addWfUserActionDef(wfStepUserAction.getCommentRequirement(),
                                    wfStepUserAction.getHighlightType(), wfStepUserAction.getName(),
                                    wfStepUserAction.getDescription(), wfStepUserAction.getLabel(),
                                    wfStepUserAction.getSymbol(), wfStepUserAction.getStyleClass(),
                                    wfStepUserAction.getNextStepName(), wfStepUserAction.getSetValuesName(),
                                    wfStepUserAction.getOrderIndex(), wfStepUserAction.isFormReview(),
                                    wfStepUserAction.isValidatePage(), wfStepUserAction.isForwarderPreferred());
                        }

                        for (WfStepAlert wfStepAlert : wfStep.getAlertList()) {
                            if (!StringUtils.isBlank(wfStepAlert.getFireOnConditionName())
                                    && !filterNames.contains(wfStepAlert.getFireOnConditionName())) {
                                throw new RuntimeException("Workflow with name [" + workflow.getName()
                                        + "] does not contain filter with name [" + wfStepAlert.getFireOnConditionName()
                                        + "].");
                            }

                            wsdb.addWfAlertDef(wfStepAlert.getType(), wfStepAlert.getName(),
                                    wfStepAlert.getDescription(), wfStepAlert.getRecipientPolicy(),
                                    wfStepAlert.getRecipientNameRule(), wfStepAlert.getRecipientContactRule(),
                                    wfStepAlert.getGenerator(), wfStepAlert.getFireOnPrevStepName(),
                                    wfStepAlert.getFireOnConditionName());
                        }

                        for (WfStepRole wfStepRole : wfStep.getRoleList()) {
                            wsdb.addParticipatingRole(wfStepRole.getRoleCode());
                        }

                        wdb.addWfStepDef(wsdb.build());
                    }

                    return wdb.build();
                }

            };

        this.wfWizardDefFactoryMap = new FactoryMap<String, WfWizardDef>(true)
            {

                @Override
                protected boolean stale(String longName, WfWizardDef formWizardDef) throws Exception {
                    return (environment().value(long.class, "versionNo",
                            new WfWizardQuery().id(formWizardDef.getId())) > formWizardDef.getVersion());
                }

                @Override
                protected WfWizardDef create(String longName, Object... arg1) throws Exception {
                    ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
                    WfWizard wfWizard = environment().list(new WfWizardQuery()
                            .applicationName(nameParts.getApplicationName()).name(nameParts.getEntityName()));
                    WfWizardDef.Builder wwdb = WfWizardDef.newBuilder(wfWizard.getEntity(),
                            wfWizard.getSubmitWorkflow(), longName, wfWizard.getDescription(), wfWizard.getId(),
                            wfWizard.getVersionNo());
                    final String appletName = WorkflowNameUtils.getWfWizardAppletName(longName);
                    final String label = getApplicationMessage("workflow.wizardapplet.label", wfWizard.getLabel());
                    final boolean descriptiveButtons = appletUtil.system().getSysParameterValue(boolean.class,
                            SystemModuleSysParamConstants.SYSTEM_DESCRIPTIVE_BUTTONS_ENABLED);
                    final String assignDescField = null;
                    final String pseudoDeleteField = null;
                    StandardAppletDef.Builder adb = StandardAppletDef.newBuilder(AppletType.REVIEW_WIZARDWORKITEMS,
                            null, label, "magic", assignDescField, pseudoDeleteField, 0, true, true, descriptiveButtons,
                            appletName, label);
                    adb.addPropDef(AppletPropertyConstants.SEARCH_TABLE, "workflow.wfWizardItemReviewTable");
                    adb.addPropDef(AppletPropertyConstants.SEARCH_TABLE_NEW, "true");
                    adb.addPropDef(WfWizardAppletPropertyConstants.WORKFLOW_WIZARD, longName);

                    adb.openPath(ApplicationPageUtils.constructAppletOpenPagePath(AppletType.REVIEW_WIZARDWORKITEMS,
                            appletName));
                    adb.originApplicationName(nameParts.getApplicationName());
                    AppletDef appletDef = adb.build();
                    wwdb.applet(appletDef);
                    for (WfWizardStep wfWizardStep : wfWizard.getStepList()) {
                        wwdb.addStep(wfWizardStep.getName(), wfWizardStep.getDescription(), wfWizardStep.getLabel(),
                                wfWizardStep.getForm(), wfWizardStep.getReference());
                    }
                    return wwdb.build();
                }

            };

        this.wfChannelDefFactoryMap = new FactoryMap<String, WfChannelDef>(true)
            {

                @Override
                protected boolean stale(String longName, WfChannelDef wfChannelDef) throws Exception {
                    return (environment().value(long.class, "versionNo",
                            new WfChannelQuery().id(wfChannelDef.getId())) > wfChannelDef.getVersion());
                }

                @Override
                protected WfChannelDef create(String longName, Object... arg1) throws Exception {
                    ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
                    WfChannel wfChannel = environment().list(new WfChannelQuery()
                            .applicationName(nameParts.getApplicationName()).name(nameParts.getEntityName()));
                    if (wfChannel == null) {
                        throw new UnifyException(ApplicationModuleErrorConstants.CANNOT_FIND_APPLICATION_ENTITY,
                                nameParts.getEntityName(), WfChannel.class, nameParts.getApplicationName());
                    }

                    WfChannelDef.Builder wwdb = WfChannelDef.newBuilder(wfChannel.getDirection(), wfChannel.getStatus(),
                            wfChannel.getLabel(), wfChannel.getEntity(), wfChannel.getDestination(),
                            wfChannel.getRule(), longName, wfChannel.getDescription(), wfChannel.getId(),
                            wfChannel.getVersionNo());
                    return wwdb.build();
                }

            };

    }

    public final void setOrganizationModuleService(OrganizationModuleService organizationModuleService) {
        this.organizationModuleService = organizationModuleService;
    }

    public final void setAppletUtil(AppletUtilities appletUtil) {
        this.appletUtil = appletUtil;
    }

    public final void setNotifRecipientProvider(NotificationRecipientProvider notifRecipientProvider) {
        this.notifRecipientProvider = notifRecipientProvider;
    }

    public final void setFileAttachmentProvider(FileAttachmentProvider fileAttachmentProvider) {
        this.fileAttachmentProvider = fileAttachmentProvider;
    }

    @Override
    public EntityActionResult submitToWorkflowChannel(EntityDef entityDef, String workflowChannelName, WorkEntity inst,
            String policyName) throws UnifyException {
        EntityActionContext ctx = new EntityActionContext(entityDef, inst, policyName);
        executeEntityPreActionPolicy(ctx);
        int resultCode = submitToWorkflowByChannel(workflowChannelName, inst);
        if (WfChannelErrorConstants.WORKFLOW_CHANNEL_CLOSED == resultCode) {
            throw new UnifyException(WorkflowModuleErrorConstants.WORKFLOW_CHANNEL_IS_CLOSED, workflowChannelName);
        }

        return executeEntityPostActionPolicy(ctx);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void submitToWorkflowByName(String workflowName, String entity, Long id) throws UnifyException {
        EntityClassDef entityClassDef = appletUtil.getEntityClassDef(entity);
        WorkEntity inst = (WorkEntity) environment().list((Class<? extends Entity>) entityClassDef.getEntityClass(),
                id);
        submitToWorkflowByName(workflowName, inst);
    }

    @Override
    public void submitToWorkflowByName(String workflowName, WorkEntity inst) throws UnifyException {
        submitToWorkflow(getWfDef(workflowName), inst);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int submitToWorkflowByChannel(String wfDocChannelName, String entity, Long id) throws UnifyException {
        EntityClassDef entityClassDef = appletUtil.getEntityClassDef(entity);
        WorkEntity inst = (WorkEntity) environment().list((Class<? extends Entity>) entityClassDef.getEntityClass(),
                id);
        return submitToWorkflowByChannel(wfDocChannelName, inst);
    }

    @Override
    public int submitToWorkflowByChannel(String workflowChannelName, WorkEntity inst) throws UnifyException {
        WfChannelDef wfChannelDef = getWfChannelDef(workflowChannelName);
        if (WfChannelStatus.CLOSED.equals(wfChannelDef.getStatus())) {
            return WfChannelErrorConstants.WORKFLOW_CHANNEL_CLOSED;
        }

        if (WfChannelStatus.SUSPENDED.equals(wfChannelDef.getStatus())) {
            // TODO Put in suspension queue
            return WfChannelErrorConstants.WORKFLOW_CHANNEL_SUSPENDED;
        }

        submitToWorkflowByName(wfChannelDef.getDestination(), inst);
        return WfChannelErrorConstants.NO_ERROR;
    }

    @Override
    public int submitToWorkflowByChannel(String wfDocChannelName, List<WorkEntity> entityList) throws UnifyException {
        WfChannelDef wfChannelDef = getWfChannelDef(wfDocChannelName);
        if (WfChannelStatus.CLOSED.equals(wfChannelDef.getStatus())) {
            return WfChannelErrorConstants.WORKFLOW_CHANNEL_CLOSED;
        }

        for (WorkEntity workEntity : entityList) {
            submitToWorkflowByName(wfChannelDef.getDestination(), workEntity);
        }

        return WfChannelErrorConstants.NO_ERROR;
    }

    @Override
    public int submitToWorkflowByChannel(String wfDocChannelName, String branchCode, String departmentCode,
            List<WorkEntity> entityList) throws UnifyException {
        if (!StringUtils.isBlank(branchCode)) {
            try {
                organizationModuleService.getBranchId(branchCode);
            } catch (UnifyException e) {
                return WfChannelErrorConstants.WORKFLOW_UNKNOWN_BRANCH;
            }
        }

        if (!StringUtils.isBlank(departmentCode)) {
            try {
                organizationModuleService.getDepartmentId(departmentCode);
            } catch (UnifyException e) {
                return WfChannelErrorConstants.WORKFLOW_UNKNOWN_DEPARTMENT;
            }
        }

        for (WorkEntity workEntity : entityList) {
            workEntity.setWorkBranchCode(branchCode);
            workEntity.setWorkDepartmentCode(departmentCode);
        }

        return submitToWorkflowByChannel(wfDocChannelName, entityList);
    }

    @Override
    public Workflow findWorkflow(Long workflowId) throws UnifyException {
        return environment().find(Workflow.class, workflowId);
    }

    @Override
    public List<Long> findWorkflowIdList(String applicationName) throws UnifyException {
        return environment().valueList(Long.class, "id", new WorkflowQuery().applicationName(applicationName));
    }

    @Override
    public WfChannel findWfChannel(Long wfChannelId) throws UnifyException {
        return environment().find(WfChannel.class, wfChannelId);
    }

    @Override
    public List<Long> findWfChannelIdList(String applicationName) throws UnifyException {
        return environment().valueList(Long.class, "id", new WfChannelQuery().applicationName(applicationName));
    }

    @Override
    public WfWizard findWfWizard(Long wfWizardId) throws UnifyException {
        return environment().find(WfWizard.class, wfWizardId);
    }

    @Override
    public List<Long> findWfWizardIdList(String applicationName) throws UnifyException {
        return environment().valueList(Long.class, "id", new WfWizardQuery().applicationName(applicationName));
    }

    @Override
    public Workflow findLeanWorkflowById(Long workflowId) throws UnifyException {
        return environment().findLean(Workflow.class, workflowId);
    }

    @Override
    public List<Workflow> findWorkflows(WorkflowQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public List<WorkflowFilter> findWorkflowFilters(WorkflowFilterQuery query) throws UnifyException {
        return environment().findAll(query);
    }

    @Override
    public List<WorkflowSetValues> findWorkflowSetValues(WorkflowSetValuesQuery query) throws UnifyException {
        return environment().findAll(query);
    }

    @Override
    public WfStep findLeanWorkflowStepById(Long wfStepId) throws UnifyException {
        return environment().listLean(WfStep.class, wfStepId);
    }

    @Override
    public List<WfStep> findWorkflowSteps(WfStepQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public WfDef getWfDef(String workflowName) throws UnifyException {
        return wfDefFactoryMap.get(workflowName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public WorkEntityItem getWfItemWorkEntity(Long wfItemId, WfReviewMode wfReviewMode) throws UnifyException {
        final WfItem wfItem = environment().list(WfItem.class, wfItemId);
        final WfDef wfDef = getWfDef(wfItem.getWorkflowName());
        WorkEntity workEntity = null;
        if (wfReviewMode.lean()) {
            workEntity = environment().listLean(
                    (Class<? extends WorkEntity>) wfDef.getEntityClassDef().getEntityClass(), wfItem.getWorkRecId());
        } else {
            workEntity = environment().list((Class<? extends WorkEntity>) wfDef.getEntityClassDef().getEntityClass(),
                    wfItem.getWorkRecId());
        }

        final ValueStore valueStore = new BeanValueStore(workEntity);
        final ValueStoreReader reader = valueStore.getReader();
        WfStepDef wfStepDef = wfDef.getWfStepDef(wfItem.getWfStepName());
        InputArrayEntries emails = null;
        final boolean isEmails = WorkflowEntityUtils.isWorkflowConditionMatched(appletUtil, reader, wfDef,
                wfStepDef.getEmails());
        if (isEmails && wfDef.getEntityDef().emailProducerConsumer()) {
            EmailListProducerConsumer emailProducerConsumer = (EmailListProducerConsumer) getComponent(
                    wfDef.getEntityDef().getEmailProducerConsumer());
            WidgetTypeDef widgetTypeDef = appletUtil.application().getWidgetTypeDef("application.email");
            Validator validator = (Validator) getComponent("fc-emailvalidator");
            InputArrayEntries.Builder ieb = InputArrayEntries.newBuilder(widgetTypeDef);
            ieb.columns(3); // TODO Get from system parameter
            ieb.validator(validator);
            if (wfReviewMode.lean()) {
                environment().findChildren(workEntity);
            }
            List<InputValue> emailList = emailProducerConsumer.produce(wfDef.getEntityDef(), valueStore);
            ieb.addEntries(emailList);
            emails = ieb.build();
        }

        Comments comments = null;
        final boolean isComments = WorkflowEntityUtils.isWorkflowConditionMatched(appletUtil, reader, wfDef,
                wfStepDef.getComments());
        if (isComments) {
            Comments.Builder cmb = Comments.newBuilder();//
            List<WfItemEvent> events = environment().findAll(new WfItemEventQuery()
                    .wfItemHistId(wfItem.getWfItemHistId()).commentsOnly()
                    .addSelect("comment", "actor", "wfAction", "actionDt").addOrder(OrderType.DESCENDING, "actionDt"));
            for (WfItemEvent wfItemEvent : events) {
                cmb.addOldComment(wfItemEvent.getComment(), wfItemEvent.getActor(), wfItemEvent.getWfAction(),
                        wfItemEvent.getActionDt());
            }

            comments = cmb.build();
        }

        Errors errors = null;
        if (wfStepDef.isError()) {
            errors = new Errors(wfItem.getErrorMsg(), wfItem.getErrorTrace(), wfItem.getErrorDoc());
        }

        return WfReviewMode.SINGLEFORM.equals(wfReviewMode)
                ? new WorkEntitySingleFormItem(workEntity, emails, comments, errors)
                : new WorkEntityItem(workEntity, emails, comments, errors);
    }

    @Override
    public boolean applyUserAction(final WorkEntity wfEntityInst, final Long wfItemId, final String stepName,
            final String userAction, final String comment, InputArrayEntries emails, WfReviewMode wfReviewMode)
            throws UnifyException {
        final WfItem wfItem = environment().list(WfItem.class, wfItemId);
        if (wfItem.getWfStepName().equals(stepName)) {
            final WfDef wfDef = getWfDef(wfItem.getWorkflowName());
            WfStepDef prevWfStepDef = wfDef.getWfStepDef(stepName);
            WfUserActionDef userActionDef = prevWfStepDef.getUserActionDef(userAction);
            // Update current event
            environment().updateAll(new WfItemEventQuery().id(wfItem.getWfItemEventId()),
                    new Update().add("actor", getUserToken().getUserLoginId()).add("actionDt", getNow())
                            .add("comment", comment).add("wfAction", userActionDef.getLabel()));

            // Prepare event for next step
            WfStepDef nextWfStepDef = wfDef.getWfStepDef(userActionDef.getNextStepName());
            final Long wfItemEventId = createWfItemEvent(nextWfStepDef, wfItem.getWfItemHistId(), stepName, null, null,
                    null, null);

            final String forwardTo = userActionDef.isForwarderPreferred() ? wfItem.getForwardedBy() : null;
            wfItem.setWfItemEventId(wfItemEventId);
            wfItem.setForwardedBy(getUserToken().getUserLoginId());
            wfItem.setForwardTo(forwardTo);
            wfItem.setHeldBy(getUserToken().getUserLoginId());
            environment().updateByIdVersion(wfItem);

            wfEntityInst.setProcessingStatus(nextWfStepDef.getProcessingStatus());
            final ValueStore wfEntityInstValueStore = new BeanValueStore(wfEntityInst);
            if (userActionDef.isWithSetValues()) {
                final EntityDef entityDef = wfDef.getEntityDef();
                final Date now = getNow();
                final WfSetValuesDef wfSetValuesDef = wfDef.getSetValuesDef(userActionDef.getSetValuesName());
                if (!wfSetValuesDef.isWithOnCondition() || wfSetValuesDef.getOnCondition()
                        .getObjectFilter(entityDef, wfEntityInstValueStore.getReader(), now)
                        .matchReader(wfEntityInstValueStore.getReader())) {
                    wfSetValuesDef.getSetValues().apply(appletUtil, entityDef, now, wfEntityInst,
                            Collections.emptyMap(), null);
                }
            }

            if (wfReviewMode.lean()) {
                if (emails != null) {
                    environment().findChildren(wfEntityInst);
                    updateEmails(wfDef, wfEntityInst, emails);
                    environment().updateByIdVersion(wfEntityInst);
                } else {
                    environment().updateLeanByIdVersion(wfEntityInst);
                }
            } else {
                updateEmails(wfDef, wfEntityInst, emails);
                environment().updateByIdVersion(wfEntityInst);
            }

            pushToWfTransitionQueue(wfDef, wfItemId);
            return true;
        }

        return false;
    }

    @Override
    public WfWizardDef getWfWizardDef(String wfWizardName) throws UnifyException {
        return wfWizardDefFactoryMap.get(wfWizardName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void graduateWfWizardItem(String wfWizardName, Long workEntityId) throws UnifyException {
        WfWizardDef wfWizardDef = getWfWizardDef(wfWizardName);
        EntityClassDef entityClassDef = appletUtil.application().getEntityClassDef(wfWizardDef.getEntity());
        WorkEntity we = environment().findLean((Class<? extends WorkEntity>) entityClassDef.getEntityClass(),
                workEntityId);
        we.setInWorkflow(false);
        environment().updateLeanByIdVersion(we);
        environment()
                .deleteAll(new WfWizardItemQuery().wizard(wfWizardDef.getLongName()).primaryEntityId(workEntityId));
    }

    @Override
    public Long createWorkflowChannel(WfChannel wfChannel) throws UnifyException {
        return (Long) environment().create(wfChannel);
    }

    @Override
    public List<WfChannel> findWorkflowChannels(WfChannelQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public int updateWorkflowChannel(WfChannel wfChannel) throws UnifyException {
        return environment().updateByIdVersion(wfChannel);
    }

    @Override
    public WfChannelDef getWfChannelDef(String workflowChannelName) throws UnifyException {
        return wfChannelDefFactoryMap.get(workflowChannelName);
    }

    @Override
    public boolean providesApplet(String appletName) {
        return appletName.startsWith(WorkflowNameUtils.RESERVED_WORKFLOW_APPLET_PREFIX)
                || appletName.startsWith(WorkflowNameUtils.RESERVED_WORKFLOW_WIZARD_PREFIX);
    }

    @Override
    public AppletDef getAppletDef(String appletName) throws UnifyException {
        if (WorkflowNameUtils.isWfWizardAppletName(appletName)) {
            WfWizardAppletNameParts nameParts = WorkflowNameUtils.getWfWizardAppletNameParts(appletName);
            return getWfWizardDef(nameParts.getWizard()).getAppletDef();
        }

        WfAppletNameParts nameParts = WorkflowNameUtils.getWfAppletNameParts(appletName);
        WfDef wfDef = getWfDef(nameParts.getWorkflow());
        return wfDef.getAppletDef(nameParts.getStepName());
    }

    @Override
    public List<AppletDef> getAppletDefsByRole(String applicationName, String roleCode, String appletFilter)
            throws UnifyException {
        if (WORKFLOW_APPLICATION.equals(applicationName)) {
            List<AppletDef> appletDefList = new ArrayList<AppletDef>();
            if (roleCode == null && getUserToken().isReservedUser()) {
                // Workflow interactive steps
                for (Workflow workflow : environment()
                        .listAll(new WorkflowQuery().ignoreEmptyCriteria(true).addSelect("applicationName", "name"))) {
                    WfDef wfDef = getWfDef(ApplicationNameUtils.ensureLongNameReference(workflow.getApplicationName(),
                            workflow.getName()));
                    appletDefList.addAll(wfDef.getAppletDefs());
                }

                // Workflow for wizard applets
                for (WfWizard wizard : environment()
                        .listAll(new WfWizardQuery().ignoreEmptyCriteria(true).addSelect("applicationName", "name"))) {
                    appletDefList.add(wfWizardDefFactoryMap.get(
                            ApplicationNameUtils.ensureLongNameReference(wizard.getApplicationName(), wizard.getName()))
                            .getAppletDef());
                }
            } else {
                // Workflow interactive steps
                List<WfStepRole> wfRoleList = environment().listAll(new WfStepRoleQuery().roleCode(roleCode));
                if (!DataUtils.isBlank(wfRoleList)) {
                    for (WfStepRole wfRole : wfRoleList) {
                        WfDef wfDef = getWfDef(ApplicationNameUtils.ensureLongNameReference(wfRole.getApplicationName(),
                                wfRole.getWorkflowName()));
                        AppletDef _appletDef = wfDef.getAppletDef(wfRole.getWfStepName());
                        if (_appletDef != null) {
                            appletDefList.add(_appletDef);
                        }
                    }
                }

                // Workflow for wizard applets
                List<String> wfWizardPrivList = appletUtil.applicationPrivilegeManager().findRolePrivileges(
                        ApplicationPrivilegeConstants.APPLICATION_WORKFLOW_WIZARD_CATEGORY_CODE, roleCode);
                if (!DataUtils.isBlank(wfWizardPrivList)) {
                    for (String wfWizardPrivName : wfWizardPrivList) {
                        PrivilegeNameParts np = PrivilegeNameUtils.getPrivilegeNameParts(wfWizardPrivName);
                        appletDefList.add(wfWizardDefFactoryMap.get(np.getEntityName()).getAppletDef());
                    }
                }
            }

            if (!StringUtils.isBlank(appletFilter) && !appletDefList.isEmpty()) {
                List<AppletDef> _filAppletDefList = new ArrayList<AppletDef>();
                for (AppletDef appletDef : appletDefList) {
                    if (appletDef.isLabelMatch(appletFilter)) {
                        _filAppletDefList.add(appletDef);
                    }
                }

                return _filAppletDefList;
            }

            return appletDefList;
        }

        return Collections.emptyList();
    }

    @Periodic(PeriodicType.NORMAL)
    public void processWfTransitionQueueItems(TaskMonitor taskMonitor) throws UnifyException {
        logDebug("Processing transition queue items...");
        List<WfTransitionQueue> pendingList = null;
        if (grabClusterLock(WFTRANSITION_QUEUE_LOCK)) {
            try {
                logDebug("Lock acquired for transition queue processing...");
                int batchSize = appletUtil.system().getSysParameterValue(int.class,

                        WorkflowModuleSysParamConstants.WFTRANSITION_PROCESSING_BATCH_SIZE);
                pendingList = environment()
                        .findAll(new WfTransitionQueueQuery().unprocessed().orderById().setLimit(batchSize));
                logDebug("Fetched [{0}] transition queue items...", pendingList.size());
                if (!DataUtils.isBlank(pendingList)) {
                    List<Long> pendingIdList = new ArrayList<Long>();
                    for (WfTransitionQueue wfTransitionQueue : pendingList) {
                        pendingIdList.add(wfTransitionQueue.getId());
                    }

                    environment().updateAll(new WfTransitionQueueQuery().idIn(pendingIdList),
                            new Update().add("processingDt", getNow()));
                }
            } finally {
                logDebug("Releasing transition queue processing lock...");
                releaseClusterLock(WFTRANSITION_QUEUE_LOCK);
            }
        }

        if (!DataUtils.isBlank(pendingList)) {
            logDebug("Performing workflow transition for [{0}] items...", pendingList.size());
            for (WfTransitionQueue wfTransitionQueue : pendingList) {
                if (performWfTransition(wfTransitionQueue)) {
                    environment().deleteById(wfTransitionQueue);
                } else {
                    wfTransitionQueue.setProcessingDt(null);
                    environment().updateByIdVersion(wfTransitionQueue);
                }
            }
            logDebug("Workflow transition completed.");
        }
    }

    @SuppressWarnings("unchecked")
    @Periodic(PeriodicType.SLOW)
    public void processAutoloadingItems(TaskMonitor taskMonitor) throws UnifyException {
        logDebug("Processing workflow auto-loading...");
        if (grabClusterLock(WFAUTOLOADING_LOCK)) {
            try {
                logDebug("Lock acquired for workflow auto loading...");
                final Date now = getNow();
                int batchSize = appletUtil.system().getSysParameterValue(int.class,
                        WorkflowModuleSysParamConstants.WF_AUTOLOADING_BATCH_SIZE);
                List<WfStep> autoLoadStepList = environment().listAll(new WfStepQuery().supportsAutoload()
                        .addSelect("applicationName", "workflowName", "autoLoadConditionName"));
                logDebug("[{0}] steps detected with auto loading...", autoLoadStepList.size());
                for (WfStep wfStep : autoLoadStepList) {
                    String workflowName = ApplicationNameUtils.getApplicationEntityLongName(wfStep.getApplicationName(),
                            wfStep.getWorkflowName());
                    logDebug("Performing workflow auto loading for [{0}]...", workflowName);
                    WfDef wfDef = getWfDef(workflowName);
                    EntityClassDef entityClassDef = appletUtil.application().getEntityClassDef(wfDef.getEntity());
                    Restriction restriction = wfDef.getFilterDef(wfStep.getAutoLoadConditionName()).getFilterDef()
                            .getRestriction(entityClassDef.getEntityDef(), null, now);
                    List<? extends WorkEntity> entityList = environment().listAll(Query
                            .of((Class<? extends WorkEntity>) entityClassDef.getEntityClass())
                            .addRestriction(restriction)
                            .addRestriction(
                                    new Or().add(new Equals("inWorkflow", Boolean.FALSE)).add(new IsNull("inWorkflow")))
                            .addOrder("id").setLimit(batchSize));
                    logDebug("Loading [{0}] items for workflow [{1}]...", entityList.size(), workflowName);
                    for (WorkEntity inst : entityList) {
                        submitToWorkflowByName(workflowName, inst);
                    }

                    logDebug("Workflow auto loading completed for [{0}].", workflowName);
                }
            } finally {
                logDebug("Releasing workflow auto-loading lock...");
                releaseClusterLock(WFAUTOLOADING_LOCK);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Transactional(TransactionAttribute.REQUIRES_NEW)
    public boolean performWfTransition(WfTransitionQueue wfTransitionQueue) throws UnifyException {
        WfItem wfItem = environment().list(WfItem.class, wfTransitionQueue.getWfItemId());
        WfDef wfDef = getWfDef(wfItem.getWorkflowName());
        EntityClassDef entityClassDef = wfDef.getEntityClassDef();
        WorkEntity wfEntityInst = (WorkEntity) environment()
                .list((Class<? extends WorkEntity>) entityClassDef.getEntityClass(), wfItem.getWorkRecId());
        if (entityClassDef.isWithTenantId()) {
            Long tenantId = DataUtils.getBeanProperty(Long.class, wfEntityInst,
                    entityClassDef.getTenantIdDef().getFieldName());
            getSessionContext().setUserTokenTenantId(tenantId);
        }

        return doWfTransition(new TransitionItem(wfItem, wfDef, wfEntityInst));
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {

    }

    private void updateEmails(WfDef wfDef, WorkEntity wfEntityInst, InputArrayEntries emails) throws UnifyException {
        if (emails != null) {
            EmailListProducerConsumer emailProducerConsumer = (EmailListProducerConsumer) getComponent(
                    wfDef.getEntityDef().getEmailProducerConsumer());
            Map<Object, InputValue> map = emails.getValueMap();
            emailProducerConsumer.consume(wfDef.getEntityDef(), new BeanValueStore(wfEntityInst), map);
        }
    }

    private synchronized void submitToWorkflow(final WfDef wfDef, final WorkEntity workInst) throws UnifyException {
        logDebug("Submitting item to workflow [{0}]. Item payload [{1}]", wfDef.getLongName(),
                workInst.getWorkflowItemDesc());
        if (!wfDef.isCompatible(workInst)) {
            Class<?> clazz = workInst != null ? workInst.getClass() : null;
            throw new UnifyException(WorkflowModuleErrorConstants.CANNOT_SUBMIT_INST_TO_INCOMPATIBLE_WORKFLOW, clazz,
                    wfDef.getName(), wfDef.getApplicationName());
        }

        ValueStore instValueStore = new BeanValueStore(workInst);
        instValueStore.save("processingStatus");
        try {
            final WfStepDef startStepDef = wfDef.getStartStepDef();
            // Set values on entry
            if (wfDef.isWithOnEntrySetValuesList()) {
                final EntityDef entityDef = wfDef.getEntityDef();
                final Date now = getNow();
                instValueStore.save(wfDef.getOnEntrySetValuesFields());
                for (WfSetValuesDef wfSetValuesDef : wfDef.getOnEntrySetValuesList()) {
                    if (!wfSetValuesDef.isWithOnCondition() || wfSetValuesDef.getOnCondition()
                            .getObjectFilter(entityDef, instValueStore.getReader(), now)
                            .matchReader(instValueStore.getReader())) {
                        wfSetValuesDef.getSetValues().apply(appletUtil, entityDef, now, workInst,
                                Collections.emptyMap(), null);
                    }
                }
            }
            workInst.setProcessingStatus(startStepDef.getProcessingStatus());

            Long workRecId = (Long) workInst.getId();
            if (workRecId == null) {
                workInst.setInWorkflow(true);
                workRecId = (Long) environment().create(workInst);
            } else {
                if (environment().value(boolean.class, "inWorkflow",
                        Query.of(workInst.getClass()).addEquals("id", workRecId))) {
                    throw new UnifyException(WorkflowModuleErrorConstants.INST_ALREADY_IN_WORKFLOW, workRecId,
                            workInst.getClass(), wfDef.getName(), wfDef.getApplicationName());
                } else {
                    workInst.setInWorkflow(true);
                    environment().findChildren(workInst);
                    environment().updateByIdVersion(workInst);
                }
            }

            final String userLoginId = getUserToken() == null ? DefaultApplicationConstants.SYSTEM_LOGINID
                    : getUserToken().getUserLoginId();
            String itemDesc = workInst.getWorkflowItemDesc();
            if (wfDef.isWithDescFormat()) {
                ParameterizedStringGenerator generator = appletUtil
                        .getStringGenerator(new BeanValueStore(workInst).getReader(), wfDef.getDescFormat());
                itemDesc = generator.generate();
            }

            WfItemHist wfItemHist = new WfItemHist();
            wfItemHist.setApplicationName(wfDef.getApplicationName());
            wfItemHist.setWorkflowName(wfDef.getLongName());
            wfItemHist.setEntity(wfDef.getEntity());
            wfItemHist.setOriginWorkRecId(workRecId);
            wfItemHist.setItemDesc(itemDesc);
            wfItemHist.setBranchCode(workInst.getWorkBranchCode());
            wfItemHist.setDepartmentCode(workInst.getWorkDepartmentCode());
            wfItemHist.setInitiatedBy(userLoginId);
            Long wfItemHistId = (Long) environment().create(wfItemHist);
            Long wfItemEventId = createWfItemEvent(startStepDef, wfItemHistId);

            WfItem wfItem = new WfItem();
            wfItem.setTenantId(workInst.getTenantId());
            wfItem.setWfItemEventId(wfItemEventId);
            wfItem.setForwardedBy(userLoginId);
            wfItem.setWorkRecId(workRecId);
            Long wfItemId = (Long) environment().create(wfItem);

            pushToWfTransitionQueue(wfDef, wfItemId);
        } catch (UnifyException e) {
            instValueStore.restore();
            throw e;
        }
    }

    private void pushToWfTransitionQueue(WfDef wfDef, Long wfItemId) throws UnifyException {
        WfTransitionQueue wfTransitionQueue = new WfTransitionQueue();
        wfTransitionQueue.setWfItemId(wfItemId);
        environment().create(wfTransitionQueue);
    }

    @SuppressWarnings("unchecked")
    private boolean doWfTransition(final TransitionItem transitionItem) throws UnifyException {
        final WfItem wfItem = transitionItem.getWfItem();
        final WfDef wfDef = transitionItem.getWfDef();
        final EntityClassDef entityClassDef = wfDef.getEntityClassDef();
        final EntityDef entityDef = entityClassDef.getEntityDef();
        final WfStepDef currWfStepDef = wfDef.getWfStepDef(wfItem.getWfStepName());
        final WorkEntity wfEntityInst = transitionItem.getWfEntityInst();
        final ValueStoreReader wfInstReader = transitionItem.getReader();
        final ValueStoreWriter wfInstWriter = transitionItem.getWriter();
        final String prevStepName = wfItem.getPrevWfStepName();
        final Long wfItemId = wfItem.getId();
        final Date now = getNow();

        transitionItem.setVariable(ProcessVariable.FORWARDED_BY.variableKey(), wfItem.getForwardedBy());
        transitionItem.setVariable(ProcessVariable.FORWARD_TO.variableKey(), wfItem.getForwardTo());
        transitionItem.setVariable(ProcessVariable.HELD_BY.variableKey(), wfItem.getHeldBy());

        setSavePoint();
        wfItem.setHeldBy(null);
        try {
            logDebug("Transitioning item [{0}] in step [{1}] of type [{2}]...", wfItem.getWfItemDesc(),
                    currWfStepDef.getLabel(), currWfStepDef.getType());
            WfStepDef nextWfStep = currWfStepDef.isWithNextStepName()
                    ? wfDef.getWfStepDef(currWfStepDef.getNextStepName())
                    : null;
            transitionItem.clearUpdated();
            // Common set values
            WfStepSetValuesDef wfSetValuesDef = currWfStepDef.getWfSetValuesDef();
            if (wfSetValuesDef != null && wfSetValuesDef.isSetValues()) {
                wfSetValuesDef.getSetValues().apply(appletUtil, entityDef, now, wfEntityInst,
                        transitionItem.getVariables(), null);
                transitionItem.setUpdated();
            }

            final WorkflowStepType type = currWfStepDef.getType();
            switch (type) {
                case START:
                    break;
                case SET_VALUES:
                    break;
                case ENRICHMENT:
                    if (currWfStepDef.isWithPolicy()) {
                        WfEnrichmentPolicy policy = (WfEnrichmentPolicy) getComponent(currWfStepDef.getPolicy());
                        policy.enrich(wfInstWriter, wfInstReader, currWfStepDef.getRule());
                        transitionItem.setUpdated();
                    }
                    break;
                case PROCEDURE:
                    if (currWfStepDef.isWithPolicy()) {
                        WfProcessPolicy policy = (WfProcessPolicy) getComponent(currWfStepDef.getPolicy());
                        policy.execute(wfInstReader, currWfStepDef.getRule());
                    }
                    break;
                case RECORD_ACTION:
                    if (currWfStepDef.isWithRecordAction()) {
                        switch (currWfStepDef.getRecordActionType()) {
                            case CREATE: {
                                WorkEntity newInst = entityClassDef.newInst(wfEntityInst);
                                Long originWorkRecId = (Long) environment().create(newInst);
                                fileAttachmentProvider.sychFileAttachments(WORK_CATEGORY, entityDef.getLongName(),
                                        originWorkRecId, (Long) wfEntityInst.getId());
                            }
                                break;
                            case DELETE: {
                                environment().delete(wfEntityInst.getClass(), wfEntityInst.getId());
                                transitionItem.setDeleted();
                            }
                                break;
                            case UPDATE: {
                                transitionItem.setUpdated();
                                fileAttachmentProvider.sychFileAttachments(WORK_CATEGORY, entityDef.getLongName(),
                                        (Long) wfEntityInst.getId(), (Long) wfEntityInst.getId());
                            }
                                break;
                        }
                    }
                    break;
                case BINARY_ROUTING:
                    if (wfDef.getFilterDef(currWfStepDef.getBinaryConditionName()).getFilterDef()
                            .getObjectFilter(wfDef.getEntityDef(), wfInstReader, now).matchReader(wfInstReader)) {
                        nextWfStep = wfDef.getWfStepDef(currWfStepDef.getNextStepName());
                    } else {
                        nextWfStep = wfDef.getWfStepDef(currWfStepDef.getAltNextStepName());
                    }
                    break;
                case POLICY_ROUTING:
                    WfBinaryPolicy policy = (WfBinaryPolicy) getComponent(currWfStepDef.getPolicy());
                    if (policy.evaluate(wfInstReader, wfDef.getEntity(), currWfStepDef.getRule())) {
                        nextWfStep = wfDef.getWfStepDef(currWfStepDef.getNextStepName());
                    } else {
                        nextWfStep = wfDef.getWfStepDef(currWfStepDef.getAltNextStepName());
                    }
                    break;
                case MULTI_ROUTING:
                    WfStepDef routeToWfStep = resolveMultiRouting(wfDef, currWfStepDef, wfInstReader);
                    nextWfStep = routeToWfStep != null ? nextWfStep = routeToWfStep : nextWfStep;
                    break;
                case USER_ACTION:
                case ERROR:
                    // Workflow item has settled in current step
                    wfItem.setForwardTo(null);
                    environment().updateByIdVersion(wfItem);
                    break;
                case END:
                    environment().delete(WfItem.class, wfItemId);
                    wfEntityInst.setInWorkflow(false);
                    wfEntityInst.setProcessingStatus(null);
                    transitionItem.setUpdated();
                    break;
                default:
                    break;
            }

            if (type.sendPassThroughAlert()) {
                sendPassThroughAlerts(currWfStepDef, transitionItem, prevStepName);
            }

            if (type.sendUserActionAlert()) {
                sendUserActionAlerts(currWfStepDef, transitionItem, prevStepName);
            }

            if (transitionItem.isUpdated() && type.isFlowing() && nextWfStep != null
                    && !nextWfStep.getProcessingStatus().equals(type.processingStatus())) {
                wfEntityInst.setProcessingStatus(nextWfStep.getProcessingStatus());
            }

            if (transitionItem.isUpdated() && !transitionItem.isDeleted()) {
                environment().updateByIdVersion(wfEntityInst);
            }

            if (type.isFlowing()) {
                if (nextWfStep == null) {
                    throw new RuntimeException("Transition of workflow item is broken.");
                }

                Long wfItemEventId = createWfItemEvent(nextWfStep, wfItem.getWfItemHistId(), currWfStepDef.getName(),
                        null, null, null, null);
                wfItem.setWfItemEventId(wfItemEventId);
                wfItem.setWfStepName(nextWfStep.getName());
                wfItem.setPrevWfStepName(currWfStepDef.getName());
                environment().updateByIdVersion(wfItem);

                if (!transitionItem.isUpdated() && !nextWfStep.getProcessingStatus().equals(type.processingStatus())) {
                    environment().updateAll(
                            Query.of((Class<? extends WorkEntity>) entityClassDef.getEntityClass()).addEquals("id",
                                    wfEntityInst.getId()),
                            new Update().add("processingStatus", nextWfStep.getProcessingStatus()));
                }

                commitTransactions();
                return doWfTransition(transitionItem);
            }
        } catch (Exception e) {
            logError(e);
            try {
                rollbackToSavePoint();
                logDebug("An error has occured. Routing item [{0}] to error step...", wfItem.getWfItemDesc());
                String errorCode = null;
                if (e instanceof UnifyException) {
                    errorCode = ((UnifyException) e).getErrorCode();
                }

                String errorMessage = getExceptionMessage(LocaleType.APPLICATION, e);
                String errorTrace = getPrintableStackTrace(e);
                String errorDoc = wfInstReader.isTempValue(ProcessErrorConstants.ERROR_DOC)
                        ? wfInstReader.getTempValue(String.class, ProcessErrorConstants.ERROR_DOC)
                        : null;
                WfStepDef errWfStepDef = wfDef.getErrorStepDef();
                Long wfItemEventId = createWfItemEvent(errWfStepDef, wfItem.getWfItemHistId(), currWfStepDef.getName(),
                        errorCode, errorMessage, errorTrace, errorDoc);
                wfItem.setWfItemEventId(wfItemEventId);
                wfItem.setWfStepName(errWfStepDef.getName());
                wfItem.setPrevWfStepName(currWfStepDef.getName());
                wfItem.setForwardTo(null);
                environment().updateByIdVersion(wfItem);

                environment().updateAll(
                        Query.of((Class<? extends WorkEntity>) entityClassDef.getEntityClass()).addEquals("id",
                                wfEntityInst.getId()),
                        new Update().add("processingStatus", errWfStepDef.getProcessingStatus()));

                commitTransactions();
            } catch (Exception e1) {
                logError(e1);
                return false;
            }
        }

        return true;
    }

    private WfStepDef resolveMultiRouting(WfDef wfDef, WfStepDef currWfStepDef, ValueStoreReader reader)
            throws UnifyException {
        if (!DataUtils.isBlank(currWfStepDef.getRoutingList())) {
            final Date now = getNow();
            for (WfRoutingDef wfRoutingDef : currWfStepDef.getRoutingList()) {
                if (wfRoutingDef.isWithCondition()) {
                    if (wfDef.getFilterDef(wfRoutingDef.getCondition()).getFilterDef()
                            .getObjectFilter(wfDef.getEntityDef(), reader, now).matchReader(reader)) {
                        return wfDef.getWfStepDef(wfRoutingDef.getNextStepName());
                    }
                } else {
                    return wfDef.getWfStepDef(wfRoutingDef.getNextStepName());
                }
            }
        }

        if (!StringUtils.isBlank(currWfStepDef.getNextStepName())) {
            return wfDef.getWfStepDef(currWfStepDef.getNextStepName());
        }

        return null;
    }

    private void sendPassThroughAlerts(final WfStepDef wfStepDef, final TransitionItem transitionItem,
            final String prevStepName) throws UnifyException {
        for (WfAlertDef wfAlertDef : wfStepDef.getAlertList()) {
            if (wfAlertDef.isPassThrough() && wfAlertDef.isFireAlertOnPreviousStep(prevStepName)) {
                sendAlert(wfStepDef, wfAlertDef, transitionItem);
            }
        }
    }

    private void sendUserActionAlerts(final WfStepDef wfStepDef, final TransitionItem transitionItem,
            final String prevStepName) throws UnifyException {
        for (WfAlertDef wfAlertDef : wfStepDef.getAlertList()) {
            if (wfAlertDef.isUserInteract() && wfAlertDef.isFireAlertOnPreviousStep(prevStepName)) {
                sendAlert(wfStepDef, wfAlertDef, transitionItem);
            }
        }
    }

    private void sendAlert(WfStepDef wfStepDef, WfAlertDef wfAlertDef, TransitionItem transitionItem)
            throws UnifyException {
        final WfDef wfDef = transitionItem.getWfDef();
        final WfItem wfItem = transitionItem.getWfItem();
        final ValueStoreReader reader = transitionItem.getReader();
        final Date now = getNow();
        if (wfAlertDef.isWithFireAlertOnCondition()) {
            if (wfDef.getFilterDef(wfAlertDef.getFireOnCondition()).getFilterDef()
                    .getObjectFilter(wfDef.getEntityDef(), reader, now).matchReader(reader)) {
                return;
            }
        }

        if (!StringUtils.isBlank(wfAlertDef.getGenerator())) {
            NotificationAlertSender sender = getComponent(NotificationAlertSender.class, wfAlertDef.getGenerator());
            final Long tenantId = getTenantIdFromTransitionItem(transitionItem);
            List<Recipient> recipientList = null;
            if (wfAlertDef.isWithRecipientPolicy()) {
                recipientList = ((WfRecipientPolicy) getComponent(wfAlertDef.getRecipientPolicy()))
                        .getRecipients(reader, wfAlertDef.getRecipientNameRule(), wfAlertDef.getRecipientContactRule());
            } else {
                if (!StringUtils.isBlank(wfItem.getHeldBy())) {
                    recipientList = Arrays.asList(notifRecipientProvider.getRecipientByLoginId(tenantId,
                            sender.getNotifType(), wfItem.getHeldBy()));
                } else if (wfStepDef.isWithParticipatingRoles()) {
                    recipientList = notifRecipientProvider.getRecipientsByRole(tenantId, sender.getNotifType(),
                            wfStepDef.getRoleSet());
                }
            }

            if (!DataUtils.isBlank(recipientList)) {
                sender.composeAndSend(reader, recipientList);
            }
        }
    }

    private Long getTenantIdFromTransitionItem(TransitionItem transitionItem) throws UnifyException {
        if (isTenancyEnabled()) {
            SqlEntityInfo sqlEntityInfo = ((SqlDataSourceDialect) db().getDataSource().getDialect())
                    .findSqlEntityInfo(transitionItem.getWfDef().getEntityClassDef().getEntityClass());
            if (sqlEntityInfo.isWithTenantId()) {
                return transitionItem.getReader().read(Long.class, sqlEntityInfo.getTenantIdFieldInfo().getName());
            }
        }

        return Entity.PRIMARY_TENANT_ID;
    }

    private Long createWfItemEvent(final WfStepDef wfStepDef, final Long wfItemHistId) throws UnifyException {
        return createWfItemEvent(wfStepDef, wfItemHistId, null, null, null, null, null);
    }

    private Long createWfItemEvent(final WfStepDef wfStepDef, final Long wfItemHistId, final String prevWfStepName,
            final String errorCode, final String errorMsg, final String errorTrace, final String errorDoc)
            throws UnifyException {
        final Date now = getNow();
        WfItemEvent wfItemEvent = new WfItemEvent();
        wfItemEvent.setWfItemHistId(wfItemHistId);
        wfItemEvent.setPriority(wfStepDef.getPriority());
        wfItemEvent.setWfStepName(wfStepDef.getName());
        wfItemEvent.setStepDt(now);
        wfItemEvent.setErrorCode(errorCode);
        wfItemEvent.setErrorMsg(errorMsg);
        wfItemEvent.setErrorTrace(errorTrace);
        wfItemEvent.setErrorDoc(errorDoc);
        wfItemEvent.setPrevWfStepName(prevWfStepName);
        if (wfStepDef.getCriticalMinutes() > 0) {
            wfItemEvent.setCriticalDt(CalendarUtils.getDateWithFrequencyOffset(now, FrequencyUnit.MINUTE,
                    wfStepDef.getCriticalMinutes()));
        }

        if (wfStepDef.getExpiryMinutes() > 0) {
            wfItemEvent.setExpectedDt(
                    CalendarUtils.getDateWithFrequencyOffset(now, FrequencyUnit.MINUTE, wfStepDef.getExpiryMinutes()));
        }

        return (Long) environment().create(wfItemEvent);
    }

    private class TransitionItem {

        final private WfItem wfItem;

        final private WfDef wfDef;

        final private WfEntityInst wfEntityInst;

        private Map<String, Object> variables;

        private boolean updated;

        private boolean deleted;

        public TransitionItem(WfItem wfItem, WfDef wfDef, WorkEntity wfInst) throws UnifyException {
            this.wfItem = wfItem;
            this.wfDef = wfDef;
            this.variables = new HashMap<String, Object>();
            this.wfEntityInst = new WfEntityInst(wfInst);
            this.wfEntityInst.getWfInstValueStore().setTempValue(CommonTempValueNameConstants.PROCESS_VARIABLES,
                    this.variables);
        }

        public WfItem getWfItem() {
            return wfItem;
        }

        public WfDef getWfDef() {
            return wfDef;
        }

        public ValueStoreReader getReader() {
            return wfEntityInst.getReader();
        }

        public ValueStoreWriter getWriter() {
            return wfEntityInst.getWriter();
        }

        public WorkEntity getWfEntityInst() {
            return wfEntityInst.getWfEntityInst();
        }

        public Map<String, Object> getVariables() {
            return variables;
        }

        public void setVariable(String key, Object val) {
            variables.put(key, val);
        }

        public boolean isUpdated() {
            return updated;
        }

        public void setUpdated() {
            this.updated = true;
        }

        public void clearUpdated() {
            this.updated = false;
        }

        public boolean isDeleted() {
            return deleted;
        }

        public void setDeleted() {
            this.deleted = true;
        }
    }

}

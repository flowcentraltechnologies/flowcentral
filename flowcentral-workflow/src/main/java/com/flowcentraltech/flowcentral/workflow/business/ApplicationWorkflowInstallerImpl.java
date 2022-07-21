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

package com.flowcentraltech.flowcentral.workflow.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.constants.ApplicationPrivilegeConstants;
import com.flowcentraltech.flowcentral.application.entities.AppSetValues;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.common.business.AbstractApplicationArtifactInstaller;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.util.ConfigUtils;
import com.flowcentraltech.flowcentral.configuration.data.ApplicationInstall;
import com.flowcentraltech.flowcentral.configuration.data.WorkflowInstall;
import com.flowcentraltech.flowcentral.configuration.data.WorkflowWizardInstall;
import com.flowcentraltech.flowcentral.configuration.xml.AppConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppWorkflowConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppWorkflowWizardConfig;
import com.flowcentraltech.flowcentral.configuration.xml.FilterConfig;
import com.flowcentraltech.flowcentral.configuration.xml.SetValuesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WfAlertConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WfChannelConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WfConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WfRoutingConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WfSetValuesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WfStepConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WfUserActionConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WfWizardConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WfWizardStepConfig;
import com.flowcentraltech.flowcentral.workflow.constants.WfChannelStatus;
import com.flowcentraltech.flowcentral.workflow.constants.WorkflowModuleNameConstants;
import com.flowcentraltech.flowcentral.workflow.entities.WfChannel;
import com.flowcentraltech.flowcentral.workflow.entities.WfChannelQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WfStep;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepAlert;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepRole;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepRoleQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepRouting;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepSetValues;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepUserAction;
import com.flowcentraltech.flowcentral.workflow.entities.WfWizard;
import com.flowcentraltech.flowcentral.workflow.entities.WfWizardQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WfWizardStep;
import com.flowcentraltech.flowcentral.workflow.entities.WfWizardStepQuery;
import com.flowcentraltech.flowcentral.workflow.entities.Workflow;
import com.flowcentraltech.flowcentral.workflow.entities.WorkflowFilter;
import com.flowcentraltech.flowcentral.workflow.entities.WorkflowFilterQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WorkflowQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WorkflowSetValues;
import com.flowcentraltech.flowcentral.workflow.entities.WorkflowSetValuesQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Application workflow installer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(WorkflowModuleNameConstants.APPLICATION_WORKFLOW_INSTALLER)
public class ApplicationWorkflowInstallerImpl extends AbstractApplicationArtifactInstaller {

    @Configurable
    private ApplicationPrivilegeManager applicationPrivilegeManager;

    public void setApplicationPrivilegeManager(ApplicationPrivilegeManager applicationPrivilegeManager) {
        this.applicationPrivilegeManager = applicationPrivilegeManager;
    }

    @Override
    public void installApplicationArtifacts(final TaskMonitor taskMonitor, final ApplicationInstall applicationInstall)
            throws UnifyException {
        final AppConfig applicationConfig = applicationInstall.getApplicationConfig();
        final String applicationName = applicationConfig.getName();
        final Long applicationId = applicationInstall.getApplicationId();

        logDebug(taskMonitor, "Executing workflow installer...");
        // Install configured workflows
        if (applicationConfig.getWorkflowsConfig() != null) {
            if (!DataUtils.isBlank(applicationConfig.getWorkflowsConfig().getWorkflowList())) {
                for (AppWorkflowConfig applicationWorkflowConfig : applicationConfig.getWorkflowsConfig()
                        .getWorkflowList()) {
                    WorkflowInstall workflowInstall = getConfigurationLoader()
                            .loadWorkflowInstallation(applicationWorkflowConfig.getConfigFile());
                    WfConfig wfConfig = workflowInstall.getWfConfig();
                    String description = resolveApplicationMessage(wfConfig.getDescription());
                    String label = resolveApplicationMessage(wfConfig.getLabel());
                    logDebug(taskMonitor, "Installing configured workflow [{0}]...", description);

                    Workflow oldWorkflow = environment()
                            .findLean(new WorkflowQuery().applicationId(applicationId).name(wfConfig.getName()));
                    if (oldWorkflow == null) {
                        Workflow workflow = new Workflow();
                        workflow.setApplicationId(applicationId);
                        workflow.setName(wfConfig.getName());
                        workflow.setDescription(description);
                        workflow.setDescFormat(wfConfig.getDescFormat());
                        workflow.setLabel(label);
                        workflow.setEntity(
                                ApplicationNameUtils.ensureLongNameReference(applicationName, wfConfig.getEntity()));
                        workflow.setConfigType(ConfigType.MUTABLE_INSTALL);
                        populateChildList(wfConfig, workflow, applicationName);
                        environment().create(workflow);
                    } else {
                        if (ConfigUtils.isSetInstall(oldWorkflow)) {
                            oldWorkflow.setDescription(description);
                            oldWorkflow.setDescFormat(wfConfig.getDescFormat());
                            oldWorkflow.setLabel(label);
                            oldWorkflow.setEntity(ApplicationNameUtils.ensureLongNameReference(applicationName,
                                    wfConfig.getEntity()));
                        }

                        populateChildList(wfConfig, oldWorkflow, applicationName);
                        environment().updateByIdVersion(oldWorkflow);
                    }
                }
            }

        }

        // Install workflow channels
        logDebug(taskMonitor, "Installing application workflow channels...");
        if (applicationConfig.getWfChannelsConfig() != null
                && !DataUtils.isBlank(applicationConfig.getWfChannelsConfig().getChannelList())) {
            WfChannel wfChannel = new WfChannel();
            wfChannel.setApplicationId(applicationId);
            for (WfChannelConfig wfChannelConfig : applicationConfig.getWfChannelsConfig().getChannelList()) {
                String description = resolveApplicationMessage(wfChannelConfig.getDescription());
                String label = resolveApplicationMessage(wfChannelConfig.getLabel());
                WfChannel oldWfChannel = environment()
                        .find(new WfChannelQuery().applicationId(applicationId).name(wfChannelConfig.getName()));
                if (oldWfChannel == null) {
                    logDebug("Installing new application workflow channel [{0}]...", wfChannelConfig.getName());
                    wfChannel.setName(wfChannelConfig.getName());
                    wfChannel.setDescription(description);
                    wfChannel.setLabel(label);
                    wfChannel.setEntity(
                            ApplicationNameUtils.ensureLongNameReference(applicationName, wfChannelConfig.getEntity()));
                    wfChannel.setDestination(ApplicationNameUtils.ensureLongNameReference(applicationName,
                            wfChannelConfig.getDestination()));
                    wfChannel.setRule(wfChannelConfig.getRule());
                    wfChannel.setDirection(wfChannelConfig.getDirection());
                    wfChannel.setConfigType(ConfigType.MUTABLE_INSTALL);
                    wfChannel.setStatus(WfChannelStatus.OPEN);
                    environment().create(wfChannel);
                } else {
                    logDebug("Upgrading application workflow channel [{0}]...", wfChannelConfig.getName());
                    if (ConfigUtils.isSetInstall(oldWfChannel)) {
                        oldWfChannel.setDescription(description);
                        oldWfChannel.setLabel(label);
                        oldWfChannel.setEntity(ApplicationNameUtils.ensureLongNameReference(applicationName,
                                wfChannelConfig.getEntity()));
                        oldWfChannel.setDestination(ApplicationNameUtils.ensureLongNameReference(applicationName,
                                wfChannelConfig.getDestination()));
                        oldWfChannel.setRule(wfChannelConfig.getRule());
                        oldWfChannel.setDirection(wfChannelConfig.getDirection());
                    }

                    environment().updateByIdVersion(oldWfChannel);
                }
            }

            logDebug(taskMonitor, "Installed [{0}] application workflow channels...",
                    applicationConfig.getWfChannelsConfig().getChannelList().size());
        }

        // Install workflow wizards
        logDebug(taskMonitor, "Installing application workflow form wizards...");
        if (applicationConfig.getWorkflowWizardsConfig() != null) {
            if (!DataUtils.isBlank(applicationConfig.getWorkflowWizardsConfig().getWorkflowWizardList())) {
                WfWizard wfWizard = new WfWizard();
                wfWizard.setApplicationId(applicationId);
                for (AppWorkflowWizardConfig appWorkflowWizardConfig : applicationConfig.getWorkflowWizardsConfig()
                        .getWorkflowWizardList()) {
                    WorkflowWizardInstall workflowWizardInstall = getConfigurationLoader()
                            .loadWorkflowWizardInstallation(appWorkflowWizardConfig.getConfigFile());
                    WfWizardConfig wfWizardConfig = workflowWizardInstall.getWfWizardConfig();
                    String description = resolveApplicationMessage(wfWizardConfig.getDescription());
                    String label = resolveApplicationMessage(wfWizardConfig.getLabel());
                    WfWizard oldAppFormWizard = environment()
                            .find(new WfWizardQuery().applicationId(applicationId).name(wfWizardConfig.getName()));
                    if (oldAppFormWizard == null) {
                        logDebug("Installing new application form wizard [{0}]...", wfWizardConfig.getName());
                        wfWizard.setName(wfWizardConfig.getName());
                        wfWizard.setDescription(description);
                        wfWizard.setLabel(label);
                        wfWizard.setEntity(ApplicationNameUtils.ensureLongNameReference(applicationName,
                                wfWizardConfig.getEntity()));
                        wfWizard.setSubmitWorkflow(wfWizardConfig.getSubmitWorkflow());
                        wfWizard.setConfigType(ConfigType.MUTABLE_INSTALL);
                        populateChildList(wfWizard, wfWizardConfig, applicationId, applicationConfig.getName());
                        environment().create(wfWizard);
                    } else {
                        logDebug("Upgrading application form wizard [{0}]...", wfWizardConfig.getName());
                        if (ConfigUtils.isSetInstall(oldAppFormWizard)) {
                            oldAppFormWizard.setDescription(description);
                            oldAppFormWizard.setLabel(label);
                            oldAppFormWizard.setEntity(ApplicationNameUtils.ensureLongNameReference(applicationName,
                                    wfWizardConfig.getEntity()));
                            oldAppFormWizard.setSubmitWorkflow(wfWizardConfig.getSubmitWorkflow());
                        }

                        populateChildList(wfWizard, wfWizardConfig, applicationId, applicationConfig.getName());
                        environment().updateByIdVersion(oldAppFormWizard);
                    }

                    applicationPrivilegeManager.registerPrivilege(applicationId,
                            ApplicationPrivilegeConstants.APPLICATION_WORKFLOW_WIZARD_CATEGORY_CODE,
                            PrivilegeNameUtils.getWfWizardPrivilegeName(ApplicationNameUtils
                                    .getApplicationEntityLongName(applicationName, wfWizardConfig.getName())),
                            description);
                }

                logDebug(taskMonitor, "Installed [{0}] application form wizards...",
                        applicationConfig.getWorkflowWizardsConfig().getWorkflowWizardList().size());
            }
        }
    }

    private void populateChildList(final WfWizard wfWizard, WfWizardConfig wfWizardConfig, final Long applicationId,
            String applicationName) throws UnifyException {
        // Workflow wizard steps
        List<WfWizardStep> oldStepList = wfWizard.isIdBlank() ? Collections.emptyList()
                : environment().findAll(new WfWizardStepQuery().wfWizardId(wfWizard.getId()).orderById());
        boolean noChange = ConfigUtils.isChanged(oldStepList);

        if (noChange) {
            List<WfWizardStep> stepList = null;
            if (!DataUtils.isBlank(wfWizardConfig.getStepList())) {
                stepList = new ArrayList<WfWizardStep>();
                for (WfWizardStepConfig wfWizardStepConfig : wfWizardConfig.getStepList()) {
                    WfWizardStep wfWizardStep = new WfWizardStep();
                    wfWizardStep.setName(wfWizardStepConfig.getName());
                    wfWizardStep.setDescription(resolveApplicationMessage(wfWizardStepConfig.getDescription()));
                    wfWizardStep.setLabel(resolveApplicationMessage(wfWizardStepConfig.getLabel()));
                    wfWizardStep.setForm(ApplicationNameUtils.ensureLongNameReference(applicationName,
                            wfWizardStepConfig.getForm()));
                    wfWizardStep.setReference(wfWizardStepConfig.getReference());
                    wfWizardStep.setConfigType(ConfigType.MUTABLE_INSTALL);
                    stepList.add(wfWizardStep);
                }
            }

            wfWizard.setStepList(stepList);
        } else {
            wfWizard.setStepList(oldStepList);
        }
    }

    private void populateChildList(final WfConfig wfConfig, Workflow workflow, String applicationName)
            throws UnifyException {
        // Filters
        List<WorkflowFilter> filterList = null;
        if (!DataUtils.isBlank(wfConfig.getFilterList())) {
            filterList = new ArrayList<WorkflowFilter>();
            Map<String, WorkflowFilter> map = workflow.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new WorkflowFilterQuery().workflowId(workflow.getId()));
            for (FilterConfig filterConfig : wfConfig.getFilterList()) {
                WorkflowFilter oldWorkflowFilter = map.get(filterConfig.getName());
                if (oldWorkflowFilter == null) {
                    WorkflowFilter workflowFilter = new WorkflowFilter();
                    workflowFilter.setName(filterConfig.getName());
                    workflowFilter.setDescription(resolveApplicationMessage(filterConfig.getDescription()));
                    workflowFilter.setFilter(InputWidgetUtils.newAppFilter(filterConfig));
                    workflowFilter.setConfigType(ConfigType.MUTABLE_INSTALL);
                    filterList.add(workflowFilter);
                } else {
                    if (ConfigUtils.isSetInstall(oldWorkflowFilter)) {
                        oldWorkflowFilter.setDescription(resolveApplicationMessage(filterConfig.getDescription()));
                        oldWorkflowFilter.setFilter(InputWidgetUtils.newAppFilter(filterConfig));
                    } else {
                        environment().findChildren(oldWorkflowFilter);
                    }

                    filterList.add(oldWorkflowFilter);
                }

            }
        }
        workflow.setFilterList(filterList);

        // Workflow set values
        List<WorkflowSetValues> setValuesList = null;
        if (!DataUtils.isBlank(wfConfig.getSetValuesList())) {
            setValuesList = new ArrayList<WorkflowSetValues>();
            Map<String, WorkflowSetValues> map = workflow.isIdBlank() ? Collections.emptyMap()
                    : environment().findAllMap(String.class, "name",
                            new WorkflowSetValuesQuery().workflowId(workflow.getId()));
            for (WfSetValuesConfig wfSetValuesConfig : wfConfig.getSetValuesList()) {
                WorkflowSetValues oldWorkflowSetValues = map.get(wfSetValuesConfig.getName());
                if (oldWorkflowSetValues == null) {
                    WorkflowSetValues workflowSetValues = new WorkflowSetValues();
                    workflowSetValues.setType(wfSetValuesConfig.getType());
                    workflowSetValues.setName(wfSetValuesConfig.getName());
                    workflowSetValues.setDescription(resolveApplicationMessage(wfSetValuesConfig.getDescription()));
                    workflowSetValues.setOnCondition(InputWidgetUtils.newAppFilter(wfSetValuesConfig.getOnCondition()));
                    workflowSetValues.setSetValues(newAppSetValues(wfSetValuesConfig.getSetValues()));
                    workflowSetValues.setConfigType(ConfigType.MUTABLE_INSTALL);
                    setValuesList.add(workflowSetValues);
                } else {
                    if (ConfigUtils.isSetInstall(oldWorkflowSetValues)) {
                        oldWorkflowSetValues.setType(wfSetValuesConfig.getType());
                        oldWorkflowSetValues
                                .setDescription(resolveApplicationMessage(wfSetValuesConfig.getDescription()));
                        oldWorkflowSetValues
                                .setOnCondition(InputWidgetUtils.newAppFilter(wfSetValuesConfig.getOnCondition()));
                        oldWorkflowSetValues.setSetValues(newAppSetValues(wfSetValuesConfig.getSetValues()));
                    } else {
                        environment().findChildren(oldWorkflowSetValues);
                    }

                    setValuesList.add(oldWorkflowSetValues);
                }
            }
        }

        workflow.setSetValuesList(setValuesList);

        // Steps
        List<WfStep> oldStepList = workflow.isIdBlank() ? Collections.emptyList()
                : environment().findAll(new WfStepQuery().workflowId(workflow.getId()).orderById());
        boolean noChange = ConfigUtils.isChanged(oldStepList);

        if (noChange) {
            List<WfStep> stepList = null;
            if (wfConfig.getStepsConfig() != null && !DataUtils.isBlank(wfConfig.getStepsConfig().getStepList())) {
                stepList = new ArrayList<WfStep>();
                for (WfStepConfig stepConfig : wfConfig.getStepsConfig().getStepList()) {
                    WfStep wfStep = new WfStep();
                    wfStep.setType(stepConfig.getType());
                    wfStep.setPriority(stepConfig.getPriority());
                    wfStep.setRecordActionType(stepConfig.getActionType());
                    wfStep.setName(stepConfig.getName());
                    wfStep.setDescription(resolveApplicationMessage(stepConfig.getDescription()));
                    wfStep.setLabel(resolveApplicationMessage(stepConfig.getLabel()));
                    wfStep.setAppletName(
                            ApplicationNameUtils.ensureLongNameReference(applicationName, stepConfig.getAppletName()));
                    wfStep.setCriticalMinutes(stepConfig.getCriticalMinutes());
                    wfStep.setExpiryMinutes(stepConfig.getExpiryMinutes());
                    wfStep.setAudit(stepConfig.isAudit());
                    wfStep.setBranchOnly(stepConfig.isBranchOnly());
                    wfStep.setIncludeForwarder(stepConfig.isIncludeForwarder());
                    wfStep.setForwarderPreffered(stepConfig.isForwarderPreffered());
                    wfStep.setComments(stepConfig.isComments());
                    wfStep.setNextStepName(stepConfig.getNextStepName());
                    wfStep.setAltNextStepName(stepConfig.getAltNextStepName());
                    wfStep.setBinaryConditionName(stepConfig.getBinaryCondition());
                    wfStep.setReadOnlyConditionName(stepConfig.getReadOnlyCondition());
                    wfStep.setAutoLoadConditionName(stepConfig.getAutoLoadCondition());
                    wfStep.setPolicy(stepConfig.getPolicy());
                    wfStep.setRule(stepConfig.getRule());
                    wfStep.setConfigType(ConfigType.MUTABLE_INSTALL);
                    populateChildList(stepConfig, applicationName, wfStep);
                    List<WfStepRole> participatingRoleList = environment()
                            .findAll(new WfStepRoleQuery().applicationName(applicationName)
                                    .workflowName(workflow.getName()).wfStepName(stepConfig.getName()));
                    wfStep.setRoleList(participatingRoleList);
                    stepList.add(wfStep);
                }
            }

            workflow.setStepList(stepList);
        } else {
            for (WfStep wfStep : oldStepList) {
                environment().findChildren(wfStep);
            }

            workflow.setStepList(oldStepList);
        }
    }

    private AppSetValues newAppSetValues(SetValuesConfig setValuesConfig) throws UnifyException {
        if (setValuesConfig != null) {
            return new AppSetValues(InputWidgetUtils.getSetValuesDefinition(setValuesConfig));
        }

        return null;
    }

    private void populateChildList(WfStepConfig stepConfig, String applicationName, WfStep wfStep)
            throws UnifyException {
        // Set values
        if (stepConfig.getSetValuesConfig() != null) {
            WfStepSetValues wfStepSetValues = new WfStepSetValues();
            wfStepSetValues.setSetValues(InputWidgetUtils.newAppSetValues(stepConfig.getSetValuesConfig()));
            wfStep.setSetValues(wfStepSetValues);
        }

        // Routings
        List<WfStepRouting> routingList = null;
        if (stepConfig.getWfRoutingsConfig() != null
                && !DataUtils.isBlank(stepConfig.getWfRoutingsConfig().getWfRoutingConfigList())) {
            routingList = new ArrayList<WfStepRouting>();
            for (WfRoutingConfig wfRoutingConfig : stepConfig.getWfRoutingsConfig().getWfRoutingConfigList()) {
                WfStepRouting wfStepRouting = new WfStepRouting();
                wfStepRouting.setName(wfRoutingConfig.getName());
                wfStepRouting.setDescription(resolveApplicationMessage(wfRoutingConfig.getDescription()));
                wfStepRouting.setConditionName(wfRoutingConfig.getCondition());
                wfStepRouting.setNextStepName(wfRoutingConfig.getNextStepName());
                routingList.add(wfStepRouting);
            }
        }

        wfStep.setRoutingList(routingList);

        // User actions
        List<WfStepUserAction> userActionList = null;
        if (stepConfig.getWfUserActionsConfig() != null
                && !DataUtils.isBlank(stepConfig.getWfUserActionsConfig().getWfUserActionConfigList())) {
            userActionList = new ArrayList<WfStepUserAction>();
            for (WfUserActionConfig wfUserActionConfig : stepConfig.getWfUserActionsConfig()
                    .getWfUserActionConfigList()) {
                WfStepUserAction wfStepUserAction = new WfStepUserAction();
                wfStepUserAction.setCommentRequirement(wfUserActionConfig.getCommentRequirement());
                wfStepUserAction.setHighlightType(wfUserActionConfig.getHighlightType());
                wfStepUserAction.setName(wfUserActionConfig.getName());
                wfStepUserAction.setDescription(resolveApplicationMessage(wfUserActionConfig.getDescription()));
                wfStepUserAction.setLabel(resolveApplicationMessage(wfUserActionConfig.getLabel()));
                wfStepUserAction.setNextStepName(wfUserActionConfig.getNextStepName());
                wfStepUserAction.setOrderIndex(wfUserActionConfig.getOrderIndex());
                wfStepUserAction.setValidatePage(wfUserActionConfig.isValidatePage());
                wfStepUserAction.setForwarderPreferred(wfUserActionConfig.isForwarderPreferred());
                userActionList.add(wfStepUserAction);
            }
        }

        wfStep.setUserActionList(userActionList);

        // Alerts
        List<WfStepAlert> alertList = null;
        if (stepConfig.getWfAlertsConfig() != null
                && !DataUtils.isBlank(stepConfig.getWfAlertsConfig().getWfAlertConfigList())) {
            alertList = new ArrayList<WfStepAlert>();
            for (WfAlertConfig wfAlertConfig : stepConfig.getWfAlertsConfig().getWfAlertConfigList()) {
                WfStepAlert wfStepAlert = new WfStepAlert();
                wfStepAlert.setType(wfAlertConfig.getType());
                wfStepAlert.setNotificationType(wfAlertConfig.getNotificationType());
                wfStepAlert.setName(wfAlertConfig.getName());
                wfStepAlert.setDescription(resolveApplicationMessage(wfAlertConfig.getDescription()));
                wfStepAlert.setRecipientPolicy(wfAlertConfig.getRecipientPolicy());
                wfStepAlert.setRecipientNameRule(wfAlertConfig.getRecipientNameRule());
                wfStepAlert.setRecipientContactRule(wfAlertConfig.getRecipientContactRule());
                wfStepAlert.setTemplate(
                        ApplicationNameUtils.ensureLongNameReference(applicationName, wfAlertConfig.getTemplate()));
                wfStepAlert.setFireOnPrevStepName(wfAlertConfig.getFireOnPrevStepName());
                wfStepAlert.setFireOnConditionName(wfAlertConfig.getFireOnCondition());
                alertList.add(wfStepAlert);
            }
        }

        wfStep.setAlertList(alertList);

    }

}

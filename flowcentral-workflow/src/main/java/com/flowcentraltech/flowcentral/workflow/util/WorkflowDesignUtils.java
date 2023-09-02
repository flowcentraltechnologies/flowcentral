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
package com.flowcentraltech.flowcentral.workflow.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationFilterConstants;
import com.flowcentraltech.flowcentral.application.data.AppletWorkflowCopyInfo;
import com.flowcentraltech.flowcentral.application.entities.AppAppletAlert;
import com.flowcentraltech.flowcentral.application.entities.AppAppletProp;
import com.flowcentraltech.flowcentral.configuration.constants.HighlightType;
import com.flowcentraltech.flowcentral.configuration.constants.RecordActionType;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowAlertType;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepPriority;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepType;
import com.flowcentraltech.flowcentral.workflow.constants.WorkflowModuleNameConstants;
import com.flowcentraltech.flowcentral.workflow.entities.WfStep;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepAlert;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepUserAction;
import com.tcdng.unify.core.constant.RequirementType;

/**
 * Workflow design utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class WorkflowDesignUtils {

    public enum DesignType {
        DEFAULT_WORKFLOW(
                "end",
                "",
                ""),
        WORKFLOW_COPY_CREATE(
                "draftApproval",
                " Create (Workflow Copy)",
                " Create"),
        WORKFLOW_COPY_UPDATE(
                "draftApproval",
                " Update (Workflow Copy)",
                " Update"),
        WORKFLOW_COPY_DELETE(
                "draftApproval",
                " Delete (Workflow Copy)",
                " Delete");

        private final String startNext;

        private final String descSuffix;

        private final String labelSuffix;

        private DesignType(String startNext, String descSuffix, String labelSuffix) {
            this.startNext = startNext;
            this.descSuffix = descSuffix;
            this.labelSuffix = labelSuffix;
        }

        public String startNext() {
            return startNext;
        }

        public String descSuffix() {
            return descSuffix;
        }

        public String labelSuffix() {
            return labelSuffix;
        }

        public boolean isWorkflowCopy() {
            return isWorkflowCopyCreate() || isWorkflowCopyUpdate() || isWorkflowCopyDelete();
        }

        public boolean isWorkflowCopyCreate() {
            return WORKFLOW_COPY_CREATE.equals(this);
        }

        public boolean isWorkflowCopyUpdate() {
            return WORKFLOW_COPY_UPDATE.equals(this);
        }

        public boolean isWorkflowCopyDelete() {
            return WORKFLOW_COPY_DELETE.equals(this);
        }
    }

    private WorkflowDesignUtils() {

    }

    public static List<AppAppletProp> generateLoadingAppletProperties(final String loadingTable,
            final String loadingSearchInput) {
        List<AppAppletProp> propList = new ArrayList<AppAppletProp>();
        propList.add(new AppAppletProp(AppletPropertyConstants.LOADING_TABLE, loadingTable));
        propList.add(new AppAppletProp(AppletPropertyConstants.LOADING_TABLE_ACTIONFOOTER, "false"));
        propList.add(new AppAppletProp(AppletPropertyConstants.SEARCH_TABLE_SEARCHINPUT, loadingSearchInput));
        return propList;
    }

    public static List<WfStep> generateWorkflowSteps(final DesignType type, final String stepLabel,
            final AppletWorkflowCopyInfo appletWorkflowCopyInfo) {
        final boolean isWorkflowCopy = type.isWorkflowCopy();
        List<WfStep> stepList = new ArrayList<WfStep>();

        // Add start step
        final WfStep startWfStep = new WfStep();
        startWfStep.setType(WorkflowStepType.START);
        startWfStep.setPriority(WorkflowStepPriority.NORMAL);
        startWfStep.setName("start");
        startWfStep.setDescription("Start");
        startWfStep.setLabel("Start");
        startWfStep.setNextStepName(type.startNext());
        stepList.add(startWfStep);

        // Add end step
        final WfStep endWfStep = new WfStep();
        endWfStep.setType(WorkflowStepType.END);
        endWfStep.setPriority(WorkflowStepPriority.NORMAL);
        endWfStep.setName("end");
        endWfStep.setDescription("End");
        endWfStep.setLabel("End");
        stepList.add(endWfStep);

        // Add error step
        final WfStep errorWfStep = new WfStep();
        errorWfStep.setType(WorkflowStepType.ERROR);
        errorWfStep.setPriority(WorkflowStepPriority.NORMAL);
        errorWfStep.setName("error");
        errorWfStep.setDescription(stepLabel + " Error");
        errorWfStep.setLabel(stepLabel + " Error");
        errorWfStep.setAppletName(appletWorkflowCopyInfo.getAppletName());
        errorWfStep.setReadOnlyConditionName(ApplicationFilterConstants.RESERVED_ALWAYS_FILTERNAME);
        List<WfStepUserAction> errorActionList = new ArrayList<WfStepUserAction>();
        errorActionList.add(WorkflowDesignUtils.createErrorRecoveryUserAction(null));
        if (isWorkflowCopy) {
            WfStepUserAction errorAbortUserAction = WorkflowDesignUtils.createErrorAbortUserAction(null);
            errorAbortUserAction.setAppletSetValuesName(appletWorkflowCopyInfo.getAbortSetValuesName());
            errorActionList.add(errorAbortUserAction);
        }

        errorWfStep.setUserActionList(errorActionList);
        stepList.add(errorWfStep);

        if (isWorkflowCopy) {
            // Add draft approval step in read-only mode
            final WfStep approvalWfStep = new WfStep();
            approvalWfStep.setType(WorkflowStepType.USER_ACTION);
            approvalWfStep.setPriority(WorkflowStepPriority.NORMAL);
            approvalWfStep.setName("draftApproval");
            approvalWfStep.setDescription(stepLabel + " Approval");
            approvalWfStep.setLabel(stepLabel + " Approval");
            approvalWfStep.setAppletName(appletWorkflowCopyInfo.getAppletName());
            approvalWfStep.setReadOnlyConditionName(ApplicationFilterConstants.RESERVED_ALWAYS_FILTERNAME);

            final String onStartAlert = type.isWorkflowCopyCreate() ? appletWorkflowCopyInfo.getOnCreateAlertName()
                    : (type.isWorkflowCopyUpdate() ? appletWorkflowCopyInfo.getOnUpdateAlertName() : null);
            final String onApprovalAlert = type.isWorkflowCopyCreate()
                    ? appletWorkflowCopyInfo.getOnCreateApprovalAlertName()
                    : (type.isWorkflowCopyUpdate() ? appletWorkflowCopyInfo.getOnUpdateApprovalAlertName() : null);
            final String onRejectionAlert = type.isWorkflowCopyCreate()
                    ? appletWorkflowCopyInfo.getOnCreateRejectionAlertName()
                    : (type.isWorkflowCopyUpdate() ? appletWorkflowCopyInfo.getOnUpdateRejectionAlertName() : null);
            if (appletWorkflowCopyInfo.isWithAlert(onStartAlert)) {
                WfStepAlert wfStepAlert = createWfStepAlert(WorkflowAlertType.USER_INTERACT,
                        appletWorkflowCopyInfo.getAppAppletAlert(onStartAlert));
                approvalWfStep.setAlertList(Arrays.asList(wfStepAlert));
            } else {
                final String sender = type.isWorkflowCopyCreate()
                        ? WorkflowModuleNameConstants.WORKFLOW_COPY_CREATE_APPROVAL_PENDING_EMAIL_SENDER
                        : (type.isWorkflowCopyUpdate()
                                ? WorkflowModuleNameConstants.WORKFLOW_COPY_UPDATE_APPROVAL_PENDING_EMAIL_SENDER
                                : WorkflowModuleNameConstants.WORKFLOW_COPY_DELETION_APPROVAL_PENDING_EMAIL_SENDER);
                WfStepAlert wfStepAlert = createWfStepAlert(WorkflowAlertType.USER_INTERACT, sender);
                approvalWfStep.setAlertList(Arrays.asList(wfStepAlert));
            }

            final boolean isApprovalAlert = appletWorkflowCopyInfo.isWithAlert(onApprovalAlert);
            final boolean isRejectAlert = appletWorkflowCopyInfo.isWithAlert(onRejectionAlert);

            final WfStepUserAction approveUserAction = new WfStepUserAction();
            approveUserAction.setName("approve");
            approveUserAction.setDescription("Approve Draft");
            approveUserAction.setLabel("Approve");
            approveUserAction.setCommentRequirement(RequirementType.OPTIONAL);
            approveUserAction.setHighlightType(HighlightType.GREEN);
            if (type.isWorkflowCopyCreate()) {
                approveUserAction.setNextStepName(isApprovalAlert ? "approvalNotif" : "end");
                approveUserAction.setAppletSetValuesName(appletWorkflowCopyInfo.getCreateApprovalSetValuesName());
            } else if (type.isWorkflowCopyUpdate()) {
                approveUserAction.setNextStepName(isApprovalAlert ? "approvalNotif" : "updateOriginal");
                approveUserAction.setAppletSetValuesName(appletWorkflowCopyInfo.getUpdateApprovalSetValuesName());
            } else {
                approveUserAction.setNextStepName(isApprovalAlert ? "approvalNotif" : "deleteOriginal");
                approveUserAction.setAppletSetValuesName(null); // TODO
            }

            final WfStepUserAction rejectUserAction = new WfStepUserAction();
            rejectUserAction.setName("reject");
            rejectUserAction.setDescription("Reject Draft");
            rejectUserAction.setLabel("Reject");
            rejectUserAction.setCommentRequirement(RequirementType.OPTIONAL);
            rejectUserAction.setHighlightType(HighlightType.RED);
            if (type.isWorkflowCopyCreate()) {
                rejectUserAction.setNextStepName(isRejectAlert ? "rejectNotif" : "deleteDraft");
            } else if (type.isWorkflowCopyUpdate()) {
                rejectUserAction.setNextStepName(isRejectAlert ? "rejectNotif" : "end");
            } else {
                rejectUserAction.setNextStepName(isRejectAlert ? "rejectNotif" : "end");
            }

            approvalWfStep.setUserActionList(Arrays.asList(approveUserAction, rejectUserAction));
            stepList.add(approvalWfStep);

            // Add approval notification step
            if (isApprovalAlert) {
                final WfStep notifWfStep = new WfStep();
                notifWfStep.setType(WorkflowStepType.NOTIFICATION);
                notifWfStep.setPriority(WorkflowStepPriority.NORMAL);
                notifWfStep.setName("approvalNotif");
                notifWfStep.setDescription("Approval Notification");
                notifWfStep.setLabel("Approval Notification");
                if (type.isWorkflowCopyCreate()) {
                    notifWfStep.setNextStepName("end");
                } else if (type.isWorkflowCopyUpdate()) {
                    notifWfStep.setNextStepName("updateOriginal");
                } else {
                    notifWfStep.setNextStepName("deleteOriginal");
                }

                WfStepAlert wfStepAlert = createWfStepAlert(WorkflowAlertType.PASS_THROUGH,
                        appletWorkflowCopyInfo.getAppAppletAlert(onApprovalAlert));
                notifWfStep.setAlertList(Arrays.asList(wfStepAlert));
                stepList.add(notifWfStep);
            }

            // Add rejection notification step
            if (isRejectAlert) {
                final WfStep notifWfStep = new WfStep();
                notifWfStep.setType(WorkflowStepType.NOTIFICATION);
                notifWfStep.setPriority(WorkflowStepPriority.NORMAL);
                notifWfStep.setName("rejectNotif");
                notifWfStep.setDescription("Rejection Notification");
                notifWfStep.setLabel("Rejection Notification");
                if (type.isWorkflowCopyCreate()) {
                    notifWfStep.setNextStepName("deleteDraft");
                } else if (type.isWorkflowCopyUpdate()) {
                    notifWfStep.setNextStepName("end");
                } else {
                    notifWfStep.setNextStepName("end");
                }

                WfStepAlert wfStepAlert = createWfStepAlert(WorkflowAlertType.PASS_THROUGH,
                        appletWorkflowCopyInfo.getAppAppletAlert(onRejectionAlert));
                notifWfStep.setAlertList(Arrays.asList(wfStepAlert));
                stepList.add(notifWfStep);
            }

            // Add actual action step
            if (type.isWorkflowCopyCreate()) {
                // Add delete draft step
                final WfStep deleteDraftWfStep = new WfStep();
                deleteDraftWfStep.setType(WorkflowStepType.RECORD_ACTION);
                deleteDraftWfStep.setRecordActionType(RecordActionType.DELETE);
                deleteDraftWfStep.setPriority(WorkflowStepPriority.NORMAL);
                deleteDraftWfStep.setName("deleteDraft");
                deleteDraftWfStep.setDescription("Delete Draft");
                deleteDraftWfStep.setLabel("Delete Draft");
                deleteDraftWfStep.setNextStepName("end");
                stepList.add(deleteDraftWfStep);
            } else if (type.isWorkflowCopyUpdate()) {
                // Add update original step
                final WfStep updateOrigWfStep = new WfStep();
                updateOrigWfStep.setType(WorkflowStepType.RECORD_ACTION);
                updateOrigWfStep.setRecordActionType(RecordActionType.UPDATE_ORIGINAL);
                updateOrigWfStep.setPriority(WorkflowStepPriority.NORMAL);
                updateOrigWfStep.setName("updateOriginal");
                updateOrigWfStep.setDescription("Update Original");
                updateOrigWfStep.setLabel("Update Original");
                updateOrigWfStep.setNextStepName("end");
                stepList.add(updateOrigWfStep);
            } else {
                // Add delete original step
                final WfStep deleteOrigWfStep = new WfStep();
                deleteOrigWfStep.setType(WorkflowStepType.RECORD_ACTION);
                deleteOrigWfStep.setRecordActionType(RecordActionType.DELETE);
                deleteOrigWfStep.setPriority(WorkflowStepPriority.NORMAL);
                deleteOrigWfStep.setName("deleteOriginal");
                deleteOrigWfStep.setDescription("Delete Original");
                deleteOrigWfStep.setLabel("Delete Original");
                deleteOrigWfStep.setNextStepName("end");
                stepList.add(deleteOrigWfStep);
            }
        }

        return stepList;
    }

    public static WfStepUserAction createErrorRecoveryUserAction(Long wfStepId) {
        WfStepUserAction recoverUserAction = new WfStepUserAction();
        recoverUserAction.setWfStepId(wfStepId);
        recoverUserAction.setName("recover");
        recoverUserAction.setDescription("Recover");
        recoverUserAction.setLabel("Recover");
        recoverUserAction.setCommentRequirement(RequirementType.NONE);
        recoverUserAction.setHighlightType(HighlightType.ORANGE);
        return recoverUserAction;
    }

    public static WfStepUserAction createErrorAbortUserAction(Long wfStepId) {
        WfStepUserAction abortUserAction = new WfStepUserAction();
        abortUserAction.setWfStepId(wfStepId);
        abortUserAction.setName("abort");
        abortUserAction.setDescription("Abort");
        abortUserAction.setLabel("Abort");
        abortUserAction.setCommentRequirement(RequirementType.NONE);
        abortUserAction.setHighlightType(HighlightType.RED);
        abortUserAction.setNextStepName("end");
        return abortUserAction;
    }

    private static WfStepAlert createWfStepAlert(WorkflowAlertType type, AppAppletAlert appAppletAlert) {
        return createWfStepAlert(type, appAppletAlert.getSender());
    }

    private static WfStepAlert createWfStepAlert(WorkflowAlertType type, String sender) {
        WfStepAlert wfStepAlert = new WfStepAlert();
        wfStepAlert.setType(type);
        wfStepAlert.setName("draftAlert");
        wfStepAlert.setDescription("Draft Alert");
        wfStepAlert.setGenerator(sender);
        wfStepAlert.setAlertWorkflowRoles(true);
        return wfStepAlert;
    }

}

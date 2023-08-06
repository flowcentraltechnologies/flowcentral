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
        WfStep wfStep = new WfStep();
        wfStep.setType(WorkflowStepType.START);
        wfStep.setPriority(WorkflowStepPriority.NORMAL);
        wfStep.setName("start");
        wfStep.setDescription("Start");
        wfStep.setLabel("Start");
        wfStep.setNextStepName(type.startNext());
        stepList.add(wfStep);

        // Add end step
        wfStep = new WfStep();
        wfStep.setType(WorkflowStepType.END);
        wfStep.setPriority(WorkflowStepPriority.NORMAL);
        wfStep.setName("end");
        wfStep.setDescription("End");
        wfStep.setLabel("End");
        stepList.add(wfStep);

        // Add error step
        wfStep = new WfStep();
        wfStep.setType(WorkflowStepType.ERROR);
        wfStep.setPriority(WorkflowStepPriority.NORMAL);
        wfStep.setName("error");
        wfStep.setDescription(stepLabel + " Error");
        wfStep.setLabel(stepLabel + " Error");
        wfStep.setAppletName(appletWorkflowCopyInfo.getAppletName());
        wfStep.setReadOnlyConditionName(ApplicationFilterConstants.RESERVED_ALWAYS_FILTERNAME);
        WfStepUserAction recoverUserAction = WorkflowDesignUtils.createErrorRecoveryUserAction(null);
        wfStep.setUserActionList(Arrays.asList(recoverUserAction));
        stepList.add(wfStep);

        if (isWorkflowCopy) {
            // Add draft approval step in read-only mode
            wfStep = new WfStep();
            wfStep.setType(WorkflowStepType.USER_ACTION);
            wfStep.setPriority(WorkflowStepPriority.NORMAL);
            wfStep.setName("draftApproval");
            wfStep.setDescription(stepLabel + " Approval");
            wfStep.setLabel(stepLabel + " Approval");
            wfStep.setAppletName(appletWorkflowCopyInfo.getAppletName());
            wfStep.setReadOnlyConditionName(ApplicationFilterConstants.RESERVED_ALWAYS_FILTERNAME);

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
                wfStep.setAlertList(Arrays.asList(wfStepAlert));
            }

            final boolean isApprovalAlert = appletWorkflowCopyInfo.isWithAlert(onApprovalAlert);
            final boolean isRejectAlert = appletWorkflowCopyInfo.isWithAlert(onRejectionAlert);

            WfStepUserAction approveUserAction = new WfStepUserAction();
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

            WfStepUserAction rejectUserAction = new WfStepUserAction();
            rejectUserAction.setName("reject");
            rejectUserAction.setDescription("Reject Draft");
            rejectUserAction.setLabel("Reject");
            rejectUserAction.setCommentRequirement(RequirementType.OPTIONAL);
            rejectUserAction.setHighlightType(HighlightType.RED);
            if (type.isWorkflowCopyCreate()) {
                approveUserAction.setNextStepName(isRejectAlert ? "rejectNotif" : "deleteDraft");
            } else if (type.isWorkflowCopyUpdate()) {
                approveUserAction.setNextStepName(isRejectAlert ? "rejectNotif" : "end");
            } else {
                approveUserAction.setNextStepName(isRejectAlert ? "rejectNotif" : "end");
            }

            wfStep.setUserActionList(Arrays.asList(approveUserAction, rejectUserAction));
            stepList.add(wfStep);

            // Add approval notification step
            if (isApprovalAlert) {
                wfStep = new WfStep();
                wfStep.setType(WorkflowStepType.NOTIFICATION);
                wfStep.setPriority(WorkflowStepPriority.NORMAL);
                wfStep.setName("approvalNotif");
                wfStep.setDescription("Approval Notification");
                wfStep.setLabel("Approval Notification");
                if (type.isWorkflowCopyCreate()) {
                    wfStep.setNextStepName("end");
                } else if (type.isWorkflowCopyUpdate()) {
                    wfStep.setNextStepName("updateOriginal");
                } else {
                    wfStep.setNextStepName("deleteOriginal");
                }

                WfStepAlert wfStepAlert = createWfStepAlert(WorkflowAlertType.PASS_THROUGH,
                        appletWorkflowCopyInfo.getAppAppletAlert(onApprovalAlert));
                wfStep.setAlertList(Arrays.asList(wfStepAlert));
                stepList.add(wfStep);
            }

            // Add rejection notification step
            if (isRejectAlert) {
                wfStep = new WfStep();
                wfStep.setType(WorkflowStepType.NOTIFICATION);
                wfStep.setPriority(WorkflowStepPriority.NORMAL);
                wfStep.setName("rejectNotif");
                wfStep.setDescription("Rejection Notification");
                wfStep.setLabel("Rejection Notification");
                if (type.isWorkflowCopyCreate()) {
                    wfStep.setNextStepName("deleteDraft");
                } else if (type.isWorkflowCopyUpdate()) {
                    wfStep.setNextStepName("end");
                } else {
                    wfStep.setNextStepName("end");
                }

                WfStepAlert wfStepAlert = createWfStepAlert(WorkflowAlertType.PASS_THROUGH,
                        appletWorkflowCopyInfo.getAppAppletAlert(onRejectionAlert));
                wfStep.setAlertList(Arrays.asList(wfStepAlert));
                stepList.add(wfStep);
            }

            // Add actual action step
            if (type.isWorkflowCopyCreate()) {
                // Add delete draft step
                wfStep = new WfStep();
                wfStep.setType(WorkflowStepType.RECORD_ACTION);
                wfStep.setRecordActionType(RecordActionType.DELETE);
                wfStep.setPriority(WorkflowStepPriority.NORMAL);
                wfStep.setName("deleteDraft");
                wfStep.setDescription("Delete Draft");
                wfStep.setLabel("Delete Draft");
                wfStep.setNextStepName("end");
                stepList.add(wfStep);
            } else if (type.isWorkflowCopyUpdate()) {
                // Add update original step
                wfStep = new WfStep();
                wfStep.setType(WorkflowStepType.RECORD_ACTION);
                wfStep.setRecordActionType(RecordActionType.UPDATE_ORIGINAL);
                wfStep.setPriority(WorkflowStepPriority.NORMAL);
                wfStep.setName("updateOriginal");
                wfStep.setDescription("Update Original");
                wfStep.setLabel("Update Original");
                wfStep.setNextStepName("end");
                stepList.add(wfStep);
            } else {
                // Add delete original step
                wfStep = new WfStep();
                wfStep.setType(WorkflowStepType.RECORD_ACTION);
                wfStep.setRecordActionType(RecordActionType.DELETE);
                wfStep.setPriority(WorkflowStepPriority.NORMAL);
                wfStep.setName("deleteOriginal");
                wfStep.setDescription("Delete Original");
                wfStep.setLabel("Delete Original");
                wfStep.setNextStepName("end");
                stepList.add(wfStep);
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

    private static WfStepAlert createWfStepAlert(WorkflowAlertType type, AppAppletAlert appAppletAlert) {
        WfStepAlert wfStepAlert = new WfStepAlert();
        wfStepAlert.setType(type);
        wfStepAlert.setName("draftAlert");
        wfStepAlert.setDescription("Draft Alert");
        wfStepAlert.setGenerator(appAppletAlert.getSender());
        wfStepAlert.setAlertWorkflowRoles(true);
        return wfStepAlert;
    }

}

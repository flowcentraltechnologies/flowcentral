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
                "end"),
        WORKFLOW_COPY_CREATE(
                "notifyRoleOnDraft"),
        WORKFLOW_COPY_UPDATE(
                "notifyRoleOnDraft");

        private final String startNext;

        private DesignType(String startNext) {
            this.startNext = startNext;
        }

        public String startNext() {
            return startNext;
        }

        public boolean isWorkflowCopyCreate() {
            return WORKFLOW_COPY_CREATE.equals(this);
        }

        public boolean isWorkflowCopyUpdate() {
            return WORKFLOW_COPY_UPDATE.equals(this);
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
        WfStepUserAction recoverUserAction = WorkflowDesignUtils.createErrorRecoveryUserAction(null);
        wfStep.setUserActionList(Arrays.asList(recoverUserAction));
        stepList.add(wfStep);

        if (type.isWorkflowCopyCreate() || type.isWorkflowCopyUpdate()) {
            // Add draft notification step
            wfStep = new WfStep();
            wfStep.setType(WorkflowStepType.NOTIFICATION);
            wfStep.setPriority(WorkflowStepPriority.NORMAL);
            wfStep.setName("notifyRoleOnDraft");
            wfStep.setDescription("Notify Role on Draft");
            wfStep.setLabel("Notify Role on Draft");
            wfStep.setNextStepName("draftApproval");

            WfStepAlert wfStepAlert = new WfStepAlert();
            wfStepAlert.setType(WorkflowAlertType.PASS_THROUGH);
            wfStepAlert.setName("draftAlert");
            wfStepAlert.setDescription("Draft Alert");
            wfStepAlert.setGenerator(type.isWorkflowCopyCreate()
                    ? WorkflowModuleNameConstants.WORKFLOW_COPY_CREATE_APPROVAL_PENDING_EMAIL_SENDER
                    : WorkflowModuleNameConstants.WORKFLOW_COPY_UPDATE_APPROVAL_PENDING_EMAIL_SENDER);
            wfStepAlert.setAlertWorkflowRoles(true);
            wfStep.setAlertList(Arrays.asList(wfStepAlert));
            stepList.add(wfStep);

            // Add draft approval step in read-only mode
            wfStep = new WfStep();
            wfStep.setType(WorkflowStepType.USER_ACTION);
            wfStep.setPriority(WorkflowStepPriority.NORMAL);
            wfStep.setName("draftApproval");
            wfStep.setDescription(stepLabel + " Approval");
            wfStep.setLabel(stepLabel + " Approval");
            wfStep.setAppletName(appletWorkflowCopyInfo.getAppletName());
            wfStep.setReadOnlyConditionName(ApplicationFilterConstants.RESERVED_ALWAYS_FILTERNAME);

            WfStepUserAction approveUserAction = new WfStepUserAction();
            approveUserAction.setName("approve");
            approveUserAction.setDescription("Approve Draft");
            approveUserAction.setLabel("Approve");
            approveUserAction.setCommentRequirement(RequirementType.OPTIONAL);
            approveUserAction.setHighlightType(HighlightType.GREEN);
            approveUserAction.setNextStepName(type.isWorkflowCopyCreate() ? "deleteOriginal" : "updateOriginal");
            approveUserAction.setAppletSetValuesName(
                    type.isWorkflowCopyCreate() ? appletWorkflowCopyInfo.getCreateApprovalSetValuesName()
                            : appletWorkflowCopyInfo.getUpdateApprovalSetValuesName());

            WfStepUserAction rejectUserAction = new WfStepUserAction();
            rejectUserAction.setName("reject");
            rejectUserAction.setDescription("Reject Draft");
            rejectUserAction.setLabel("Reject");
            rejectUserAction.setCommentRequirement(RequirementType.OPTIONAL);
            rejectUserAction.setHighlightType(HighlightType.RED);
            rejectUserAction.setNextStepName("end");

            wfStep.setUserActionList(Arrays.asList(approveUserAction, rejectUserAction));
            stepList.add(wfStep);

            if (type.isWorkflowCopyCreate()) {
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
            } else {
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

}

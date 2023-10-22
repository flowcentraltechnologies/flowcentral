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
import com.flowcentraltech.flowcentral.application.data.AppletAlertDef;
import com.flowcentraltech.flowcentral.application.data.AppletWorkflowCopyInfo;
import com.flowcentraltech.flowcentral.application.data.AppletWorkflowCopyInfo.EventInfo;
import com.flowcentraltech.flowcentral.application.data.AppletWorkflowCopyInfo.EventType;
import com.flowcentraltech.flowcentral.application.data.AppletWorkflowCopyInfo.WorkflowCopyInfo;
import com.flowcentraltech.flowcentral.application.data.AppletWorkflowCopyInfo.WorkflowCopyType;
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
                null,
                "end",
                "end",
                "end",
                "end",
                "end",
                "",
                ""),
        WORKFLOW_COPY_CREATE(
                WorkflowCopyType.CREATION,
                "draftApproval",
                "end",
                "draftReview",
                "draftApproval",
                "deleteDraft",
                " Creation Draft Workflow",
                " Create"),
        WORKFLOW_COPY_UPDATE(
                WorkflowCopyType.UPDATE,
                "draftApproval",
                "updateOriginal",
                "draftReview",
                "draftApproval",
                "end",
                " Update Draft Workflow",
                " Update"),
        WORKFLOW_COPY_DELETE(
                WorkflowCopyType.DELETION,
                "draftApproval",
                "deleteOriginal",
                "end",
                "end",
                "end",
                " Deletion Draft Workflow",
                " Delete");

        private final WorkflowCopyType copyType;

        private final String startNext;

        private final String approvalNext;

        private final String reviewNext;

        private final String resubmitNext;

        private final String discardNext;

        private final String descSuffix;

        private final String labelSuffix;

        private DesignType(WorkflowCopyType copyType, String startNext, String approvalNext, String reviewNext,
                String resubmitNext, String discardNext, String descSuffix, String labelSuffix) {
            this.copyType = copyType;
            this.startNext = startNext;
            this.approvalNext = approvalNext;
            this.reviewNext = reviewNext;
            this.resubmitNext = resubmitNext;
            this.discardNext = discardNext;
            this.descSuffix = descSuffix;
            this.labelSuffix = labelSuffix;
        }

        public WorkflowCopyType copyType() {
            return copyType;
        }

        public String startNext() {
            return startNext;
        }

        public String approvalNext() {
            return approvalNext;
        }

        public String reviewNext() {
            return reviewNext;
        }

        public String resubmitNext() {
            return resubmitNext;
        }

        public String discardNext() {
            return discardNext;
        }

        public String descSuffix() {
            return descSuffix;
        }

        public String labelSuffix() {
            return labelSuffix;
        }

        public boolean isDefault() {
            return DEFAULT_WORKFLOW.equals(this);
        }

        public boolean isWorkflowCopy() {
            return copyType != null;
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

        public boolean isWorkflowCopyCreateOrUpdate() {
            return WORKFLOW_COPY_CREATE.equals(this) || WORKFLOW_COPY_UPDATE.equals(this);
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
        final WorkflowCopyInfo workflowCopyInfo = isWorkflowCopy
                ? appletWorkflowCopyInfo.getWorkflowCopyInfo(type.copyType())
                : null;
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
            if (workflowCopyInfo.isWithEventInfo(EventType.ON_ABORT)) {
                errorAbortUserAction
                        .setAppletSetValuesName(workflowCopyInfo.getEventInfo(EventType.ON_ABORT).getSetValuesName());
            }

            errorActionList.add(errorAbortUserAction);
        }

        errorWfStep.setUserActionList(errorActionList);
        stepList.add(errorWfStep);

        if (isWorkflowCopy) {
            // Draft approval step in read-only mode
            final WfStep approvalWfStep = new WfStep();
            final EventInfo submitEventInfo = workflowCopyInfo.getEventInfo(EventType.ON_SUBMIT);
            final EventInfo resubmitEventInfo = workflowCopyInfo.getEventInfo(EventType.ON_RESUBMIT);
            approvalWfStep.setType(WorkflowStepType.USER_ACTION);
            approvalWfStep.setPriority(WorkflowStepPriority.NORMAL);
            approvalWfStep.setName("draftApproval");
            approvalWfStep.setDescription(stepLabel + " Approval");
            approvalWfStep.setLabel(stepLabel + " Approval");
            approvalWfStep.setAppletName(appletWorkflowCopyInfo.getAppletName());
            approvalWfStep.setReadOnlyConditionName(ApplicationFilterConstants.RESERVED_ALWAYS_FILTERNAME);
            approvalWfStep.setAttachmentProviderName(appletWorkflowCopyInfo.getAttachmentProvider());
            approvalWfStep.setAppletSetValuesName(submitEventInfo.getSetValuesName());
            if (submitEventInfo.isWithAlert()) {
                WfStepAlert wfStepAlert = createWfStepAlert(WorkflowAlertType.USER_INTERACT,
                        appletWorkflowCopyInfo.getAppletAlertDef(submitEventInfo.getAlertName()));
                wfStepAlert.setFireOnPrevStepName("start");
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
            
            if (type.isWorkflowCopyCreateOrUpdate() && resubmitEventInfo.isWithAlert()) {
                WfStepAlert wfStepAlert = createWfStepAlert(WorkflowAlertType.USER_INTERACT,
                        appletWorkflowCopyInfo.getAppletAlertDef(resubmitEventInfo.getAlertName()));
                wfStepAlert.setFireOnPrevStepName("draftReview");
                approvalWfStep.setAlertList(Arrays.asList(wfStepAlert));
            }
            
            final WfStepUserAction approveUserAction = new WfStepUserAction();
            final EventInfo approveEventInfo = workflowCopyInfo.getEventInfo(EventType.ON_APPROVE);
            approveUserAction.setName("approve");
            approveUserAction.setDescription("Approve Draft");
            approveUserAction.setLabel("Approve");
            approveUserAction.setCommentRequirement(RequirementType.OPTIONAL);
            approveUserAction.setHighlightType(HighlightType.GREEN);
            approveUserAction.setAppletSetValuesName(approveEventInfo.getSetValuesName());
            approveUserAction.setNextStepName(approveEventInfo.isWithAlert() ? "approvalNotif" : type.approvalNext());

            final WfStepUserAction rejectUserAction = new WfStepUserAction();
            final EventInfo rejectEventInfo = workflowCopyInfo.getEventInfo(EventType.ON_REJECT);
            rejectUserAction.setName("reject");
            rejectUserAction.setDescription("Reject Draft");
            rejectUserAction.setLabel("Reject");
            rejectUserAction.setCommentRequirement(RequirementType.OPTIONAL);
            rejectUserAction.setHighlightType(HighlightType.RED);
            rejectUserAction.setNextStepName(type.reviewNext());

            approvalWfStep.setUserActionList(Arrays.asList(approveUserAction, rejectUserAction));
            stepList.add(approvalWfStep);

            // Draft review step
            final EventInfo discardEventInfo = workflowCopyInfo.getEventInfo(EventType.ON_DISCARD);
            if (type.isWorkflowCopyCreateOrUpdate()) {
                final WfStep reviewWfStep = new WfStep();
                reviewWfStep.setType(WorkflowStepType.USER_ACTION);
                reviewWfStep.setPriority(WorkflowStepPriority.NORMAL);
                reviewWfStep.setName("draftReview");
                reviewWfStep.setDescription(stepLabel + " Review");
                reviewWfStep.setLabel(stepLabel + " Review");
                reviewWfStep.setAppletName(appletWorkflowCopyInfo.getAppletName());
                reviewWfStep.setReadOnlyConditionName(null);
                reviewWfStep.setAttachmentProviderName(appletWorkflowCopyInfo.getAttachmentProvider());
                reviewWfStep.setAppletSetValuesName(rejectEventInfo.getSetValuesName());
                if (rejectEventInfo.isWithAlert()) {
                    WfStepAlert wfStepAlert = createWfStepAlert(WorkflowAlertType.USER_INTERACT,
                            appletWorkflowCopyInfo.getAppletAlertDef(rejectEventInfo.getAlertName()));
                    reviewWfStep.setAlertList(Arrays.asList(wfStepAlert));
                }

                final WfStepUserAction resubmitUserAction = new WfStepUserAction();
                resubmitUserAction.setName("resubmit");
                resubmitUserAction.setDescription("Resubmit Draft");
                resubmitUserAction.setLabel("Resubmit");
                resubmitUserAction.setCommentRequirement(RequirementType.OPTIONAL);
                resubmitUserAction.setHighlightType(HighlightType.GREEN);
                resubmitUserAction.setAppletSetValuesName(resubmitEventInfo.getSetValuesName());
                resubmitUserAction.setNextStepName(type.resubmitNext());

                final WfStepUserAction discardUserAction = new WfStepUserAction();
                discardUserAction.setName("discard");
                discardUserAction.setDescription("Discard Draft");
                discardUserAction.setLabel("Discard");
                discardUserAction.setCommentRequirement(RequirementType.OPTIONAL);
                discardUserAction.setHighlightType(HighlightType.RED);
                discardUserAction.setAppletSetValuesName(discardEventInfo.getSetValuesName());
                discardUserAction.setNextStepName(discardEventInfo.isWithAlert() ? "discardNotif" : type.discardNext());

                reviewWfStep.setUserActionList(Arrays.asList(resubmitUserAction, discardUserAction));
                stepList.add(reviewWfStep);
            }

            // Add approval notification step
            if (approveEventInfo.isWithAlert()) {
                final WfStep notifWfStep = new WfStep();
                notifWfStep.setType(WorkflowStepType.NOTIFICATION);
                notifWfStep.setPriority(WorkflowStepPriority.NORMAL);
                notifWfStep.setName("approvalNotif");
                notifWfStep.setDescription("Approval Notification");
                notifWfStep.setLabel("Approval Notification");
                notifWfStep.setNextStepName(type.approvalNext());

                WfStepAlert wfStepAlert = createWfStepAlert(WorkflowAlertType.PASS_THROUGH,
                        appletWorkflowCopyInfo.getAppletAlertDef(approveEventInfo.getAlertName()));
                notifWfStep.setAlertList(Arrays.asList(wfStepAlert));
                stepList.add(notifWfStep);
            }

            if (type.isWorkflowCopyCreateOrUpdate()) {
                // Add discard notification step
                if (discardEventInfo.isWithAlert()) {
                    final WfStep notifWfStep = new WfStep();
                    notifWfStep.setType(WorkflowStepType.NOTIFICATION);
                    notifWfStep.setPriority(WorkflowStepPriority.NORMAL);
                    notifWfStep.setName("discardNotif");
                    notifWfStep.setDescription("Discard Notification");
                    notifWfStep.setLabel("Discard Notification");
                    notifWfStep.setNextStepName(type.discardNext());

                    WfStepAlert wfStepAlert = createWfStepAlert(WorkflowAlertType.PASS_THROUGH,
                            appletWorkflowCopyInfo.getAppletAlertDef(discardEventInfo.getAlertName()));
                    notifWfStep.setAlertList(Arrays.asList(wfStepAlert));
                    stepList.add(notifWfStep);
                }
            }

            // Add actual action steps
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

    private static WfStepAlert createWfStepAlert(WorkflowAlertType type, AppletAlertDef appletAlertDef) {
        WfStepAlert wfStepAlert = createWfStepAlert(type, appletAlertDef.getSender());
        wfStepAlert.setName(appletAlertDef.getName());
        wfStepAlert.setDescription(appletAlertDef.getDescription());
        wfStepAlert.setRecipientContactRule(appletAlertDef.getRecipientContactRule());
        wfStepAlert.setRecipientNameRule(appletAlertDef.getRecipientNameRule());
        wfStepAlert.setRecipientPolicy(appletAlertDef.getRecipientPolicy());
        return wfStepAlert;
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

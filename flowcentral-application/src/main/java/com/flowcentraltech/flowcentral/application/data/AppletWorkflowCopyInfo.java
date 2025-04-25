/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Applet workflow copy information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class AppletWorkflowCopyInfo {

    public enum WorkflowCopyType {
        CREATION,
        UPDATE,
        DELETION;

        public boolean isCreate() {
            return CREATION.equals(this);
        }

        public boolean isUpdate() {
            return UPDATE.equals(this);
        }

        public boolean isDelete() {
            return DELETION.equals(this);
        }
    }

    public enum EventType {
        ON_SUBMIT,
        ON_APPROVE,
        ON_REJECT,
        ON_RESUBMIT,
        ON_DISCARD,
        ON_ABORT;

        public boolean isSubmit() {
            return ON_SUBMIT.equals(this);
        }

        public boolean isApprove() {
            return ON_APPROVE.equals(this);
        }

        public boolean isReject() {
            return ON_REJECT.equals(this);
        }

        public boolean isResubmit() {
            return ON_RESUBMIT.equals(this);
        }

        public boolean isDiscard() {
            return ON_DISCARD.equals(this);
        }

        public boolean isAbort() {
            return ON_ABORT.equals(this);
        }
    }

    private String appletName;

    private String appletSearchTable;

    private String attachmentProvider;

    private int noOfCreateApprovals;

    private int noOfUpdateApprovals;

    private int noOfDeleteApprovals;

    private long appletVersionNo;

    private boolean supportMultiItemAction;

    private Map<WorkflowCopyType, WorkflowCopyInfo> workflowCopyInfos;

    private Map<String, AppletAlertDef> alerts;

    public AppletWorkflowCopyInfo(String appletName, String appletSearchTable, String attachmentProvider,
            int noOfCreateApprovals, int noOfUpdateApprovals, int noOfDeleteApprovals, long appletVersionNo,
            boolean supportMultiItemAction, Map<WorkflowCopyType, WorkflowCopyInfo> workflowCopyInfos,
            Map<String, AppletAlertDef> alerts) {
        this.appletName = appletName;
        this.appletSearchTable = appletSearchTable;
        this.attachmentProvider = attachmentProvider;
        this.noOfCreateApprovals = noOfCreateApprovals;
        this.noOfUpdateApprovals = noOfUpdateApprovals;
        this.noOfDeleteApprovals = noOfDeleteApprovals;
        this.appletVersionNo = appletVersionNo;
        this.supportMultiItemAction = supportMultiItemAction;
        this.workflowCopyInfos = DataUtils.unmodifiableMap(workflowCopyInfos);
        this.alerts = DataUtils.unmodifiableMap(alerts);
    }

    public AppletWorkflowCopyInfo() {

    }

    public String getAppletName() {
        return appletName;
    }

    public String getAppletSearchTable() {
        return appletSearchTable;
    }

    public String getAttachmentProvider() {
        return attachmentProvider;
    }

    public boolean isWithAttachmentProvide() {
        return !StringUtils.isBlank(attachmentProvider);
    }

    public long getAppletVersionNo() {
        return appletVersionNo;
    }

    public boolean isSupportMultiItemAction() {
        return supportMultiItemAction;
    }

    public WorkflowCopyInfo getWorkflowCopyInfo(WorkflowCopyType type) {
        return workflowCopyInfos.get(type);
    }

    public boolean isWithWorkflowCopyInfoType(WorkflowCopyType type) {
        return workflowCopyInfos.containsKey(type);
    }

    public AppletAlertDef getAppletAlertDef(String name) {
        return alerts.get(name);
    }

    public boolean isWithAlert(String name) {
        return name != null && alerts.containsKey(name);
    }

    public int getNoOfCreateApprovals() {
        return noOfCreateApprovals;
    }

    public int getNoOfUpdateApprovals() {
        return noOfUpdateApprovals;
    }

    public int getNoOfDeleteApprovals() {
        return noOfDeleteApprovals;
    }

    public static class WorkflowCopyInfo {

        private final WorkflowCopyType type;

        private final Map<EventType, EventInfo> eventInfos;

        private WorkflowCopyInfo(WorkflowCopyType type) {
            this.type = type;
            this.eventInfos = new HashMap<EventType, EventInfo>();
        }

        private WorkflowCopyInfo(WorkflowCopyInfo workflowCopyInfo) {
            this.type = workflowCopyInfo.type;
            this.eventInfos = Collections.unmodifiableMap(workflowCopyInfo.eventInfos);
        }

        public WorkflowCopyType getType() {
            return type;
        }

        public Map<EventType, EventInfo> getEventInfos() {
            return eventInfos;
        }

        public EventInfo getEventInfo(EventType type) {
            return eventInfos.get(type);
        }

        public boolean isWithEventInfo(EventType type) {
            return eventInfos.containsKey(type);
        }

    }

    public static class EventInfo {

        private EventType type;

        private String alertName;

        private String setValuesName;

        public EventInfo(EventType type, String alertName, String setValuesName) {
            this.type = type;
            this.alertName = alertName;
            this.setValuesName = setValuesName;
        }

        public EventType getType() {
            return type;
        }

        public String getAlertName() {
            return alertName;
        }

        public boolean isWithAlert() {
            return !StringUtils.isBlank(alertName);
        }

        public String getSetValuesName() {
            return setValuesName;
        }

        public boolean isWithSetValues() {
            return !StringUtils.isBlank(setValuesName);
        }
    }

    public static Builder newBuilder(String appletName, String appletSearchTable, String attachmentProvider,
            int noOfCreateApprovals, int noOfUpdateApprovals, int noOfDeleteApprovals, long appletVersionNo,
            boolean supportMultiItemAction) {
        return new Builder(appletName, appletSearchTable, attachmentProvider, noOfCreateApprovals, noOfUpdateApprovals,
                noOfDeleteApprovals, appletVersionNo, supportMultiItemAction);
    }

    public static class Builder {

        private String appletName;

        private String appletSearchTable;

        private String attachmentProvider;

        private int noOfCreateApprovals;

        private int noOfUpdateApprovals;

        private int noOfDeleteApprovals;

        private long appletVersionNo;

        private boolean supportMultiItemAction;

        private Map<WorkflowCopyType, WorkflowCopyInfo> workflowCopyInfos;

        private Map<String, AppletAlertDef> alerts;

        public Builder(String appletName, String appletSearchTable, String attachmentProvider, int noOfCreateApprovals,
                int noOfUpdateApprovals, int noOfDeleteApprovals, long appletVersionNo,
                boolean supportMultiItemAction) {
            this.appletName = appletName;
            this.appletSearchTable = appletSearchTable;
            this.attachmentProvider = attachmentProvider;
            this.noOfCreateApprovals = applyApprovalConstraints(noOfCreateApprovals);
            this.noOfUpdateApprovals = applyApprovalConstraints(noOfUpdateApprovals);
            this.noOfDeleteApprovals = applyApprovalConstraints(noOfDeleteApprovals);
            this.appletVersionNo = appletVersionNo;
            this.supportMultiItemAction = supportMultiItemAction;
            this.workflowCopyInfos = new HashMap<WorkflowCopyType, WorkflowCopyInfo>();
            this.alerts = new HashMap<String, AppletAlertDef>();
        }
        
        public Builder withEvent(WorkflowCopyType copyType, EventType eventType, String alertName,
                String setValuesName) {
            return withEvent(copyType, 0, eventType, alertName, setValuesName);
        }

        public Builder withEvent(WorkflowCopyType copyType, int noOfApprovalLevels, EventType eventType,
                String alertName, String setValuesName) {
            if (noOfApprovalLevels > 8) {
                throw new IllegalArgumentException("Approval levels can not be more than 8.");
            }

            getWorkflowCopyInfo(copyType).getEventInfos().put(eventType,
                    new EventInfo(eventType, alertName, setValuesName));
            return this;
        }

        public Builder withAlert(String name, String description, String recipientPolicy, String recipientNameRule,
                String recipientContactRule, String sender, String template) {
            AppletAlertDef alert = new AppletAlertDef(name, description, recipientPolicy, recipientNameRule,
                    recipientContactRule, sender, template);
            alerts.put(alert.getName(), alert);
            return this;
        }

        private WorkflowCopyInfo getWorkflowCopyInfo(WorkflowCopyType type) {
            WorkflowCopyInfo workflowCopyInfo = workflowCopyInfos.get(type);
            if (workflowCopyInfo == null) {
                workflowCopyInfo = new WorkflowCopyInfo(type);
                workflowCopyInfos.put(type, workflowCopyInfo);
            }

            return workflowCopyInfo;
        }

        private int applyApprovalConstraints(int levels) {
            if (levels <= 0) {
                levels = 1;
            } else if (levels > 8) {
                levels = 8;
            }
            
            return levels;
        }

        public AppletWorkflowCopyInfo build() {
            Map<WorkflowCopyType, WorkflowCopyInfo> _workflowCopyInfos = new HashMap<WorkflowCopyType, WorkflowCopyInfo>();
            for (Map.Entry<WorkflowCopyType, WorkflowCopyInfo> entry : workflowCopyInfos.entrySet()) {
                _workflowCopyInfos.put(entry.getKey(), new WorkflowCopyInfo(entry.getValue()));
            }

            return new AppletWorkflowCopyInfo(appletName, appletSearchTable, attachmentProvider, noOfCreateApprovals,
                    noOfUpdateApprovals, noOfDeleteApprovals, appletVersionNo, supportMultiItemAction,
                    Collections.unmodifiableMap(_workflowCopyInfos), Collections.unmodifiableMap(alerts));
        }
    }
}

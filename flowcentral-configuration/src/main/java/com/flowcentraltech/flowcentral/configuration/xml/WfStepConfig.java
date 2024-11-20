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

package com.flowcentraltech.flowcentral.configuration.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.constants.RecordActionType;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepPriority;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.RecordActionTypeXmlAdapter;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.WorkflowStepPriorityXmlAdapter;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.WorkflowStepTypeXmlAdapter;

/**
 * Workflow step configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class WfStepConfig extends BaseNameConfig {

    @JsonSerialize(using = WorkflowStepTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = WorkflowStepTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private WorkflowStepType type;

    @JsonSerialize(using = WorkflowStepPriorityXmlAdapter.Serializer.class)
    @JsonDeserialize(using = WorkflowStepPriorityXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private WorkflowStepPriority priority;

    @JsonSerialize(using = RecordActionTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = RecordActionTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "actionType")
    private RecordActionType actionType;

    @JacksonXmlProperty(isAttribute = true, localName = "applet")
    private String appletName;

    @JacksonXmlProperty(isAttribute = true, localName = "nextStep")
    private String nextStepName;

    @JacksonXmlProperty(isAttribute = true, localName = "altNextStep")
    private String altNextStepName;

    @JacksonXmlProperty(isAttribute = true)
    private String binaryCondition;

    @JacksonXmlProperty(isAttribute = true)
    private String readOnlyCondition;

    @JacksonXmlProperty(isAttribute = true)
    private String autoLoadCondition;
    
    @JacksonXmlProperty(isAttribute = true)
    private String workItemLoadingRestriction;
   
    @JacksonXmlProperty(isAttribute = true)
    private String attachmentProvider;

    @JacksonXmlProperty(isAttribute = true)
    private String newCommentCaption;

    @JacksonXmlProperty(isAttribute = true)
    private String policy;

    @JacksonXmlProperty(isAttribute = true)
    private String rule;

    @JacksonXmlProperty(isAttribute = true)
    private String valueGenerator;
    
    @JacksonXmlProperty(isAttribute = true, localName = "appletSetValues")
    private String appletSetValuesName;

    @JacksonXmlProperty(isAttribute = true)
    private Integer reminderMinutes;

    @JacksonXmlProperty(isAttribute = true)
    private Integer criticalMinutes;
    
    @JacksonXmlProperty(isAttribute = true)
    private Integer expiryMinutes;
    
    @JacksonXmlProperty(isAttribute = true)
    private Integer delayMinutes;

    @JacksonXmlProperty(isAttribute = true)
    private boolean audit;

    @JacksonXmlProperty(isAttribute = true)
    private boolean branchOnly;

    @JacksonXmlProperty(isAttribute = true)
    private boolean departmentOnly;

    @JacksonXmlProperty(isAttribute = true)
    private boolean includeForwarder;

    @JacksonXmlProperty(isAttribute = true)
    private boolean forwarderPreffered;

    @JacksonXmlProperty(isAttribute = true)
    private int designX;

    @JacksonXmlProperty(isAttribute = true)
    private int designY;

    @JacksonXmlProperty(isAttribute = true)
    private String emails;

    @JacksonXmlProperty(isAttribute = true)
    private String comments;

    @JacksonXmlProperty(localName = "setValues")
    private SetValuesConfig setValuesConfig;

    @JacksonXmlProperty(localName = "routings")
    private WfRoutingsConfig wfRoutingsConfig;

    @JacksonXmlProperty(localName = "userActions")
    private WfUserActionsConfig wfUserActionsConfig;

    @JacksonXmlProperty(localName = "alerts")
    private WfAlertsConfig wfAlertsConfig;

    public WfStepConfig() {
        priority = WorkflowStepPriority.NORMAL;
    }

    public WorkflowStepType getType() {
        return type;
    }

    public void setType(WorkflowStepType type) {
        this.type = type;
    }

    public WorkflowStepPriority getPriority() {
        return priority;
    }

    public void setPriority(WorkflowStepPriority priority) {
        this.priority = priority;
    }

    public RecordActionType getActionType() {
        return actionType;
    }

    public void setActionType(RecordActionType actionType) {
        this.actionType = actionType;
    }

    public String getAppletName() {
        return appletName;
    }

    public void setAppletName(String appletName) {
        this.appletName = appletName;
    }

    public void setNextStepName(String nextStepName) {
        this.nextStepName = nextStepName;
    }

    public String getNextStepName() {
        return nextStepName;
    }

    public String getAltNextStepName() {
        return altNextStepName;
    }

    public void setAltNextStepName(String altNextStepName) {
        this.altNextStepName = altNextStepName;
    }

    public String getBinaryCondition() {
        return binaryCondition;
    }

    public void setBinaryCondition(String binaryCondition) {
        this.binaryCondition = binaryCondition;
    }

    public String getReadOnlyCondition() {
        return readOnlyCondition;
    }

    public void setReadOnlyCondition(String readOnlyCondition) {
        this.readOnlyCondition = readOnlyCondition;
    }

    public String getAutoLoadCondition() {
        return autoLoadCondition;
    }

    public void setAutoLoadCondition(String autoLoadCondition) {
        this.autoLoadCondition = autoLoadCondition;
    }

    public String getWorkItemLoadingRestriction() {
        return workItemLoadingRestriction;
    }

    public void setWorkItemLoadingRestriction(String workItemLoadingRestriction) {
        this.workItemLoadingRestriction = workItemLoadingRestriction;
    }

    public String getAttachmentProvider() {
        return attachmentProvider;
    }

    public void setAttachmentProvider(String attachmentProvider) {
        this.attachmentProvider = attachmentProvider;
    }

    public String getNewCommentCaption() {
        return newCommentCaption;
    }

    public void setNewCommentCaption(String newCommentCaption) {
        this.newCommentCaption = newCommentCaption;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Integer getReminderMinutes() {
        return reminderMinutes;
    }

    public void setReminderMinutes(Integer reminderMinutes) {
        this.reminderMinutes = reminderMinutes;
    }

    public Integer getCriticalMinutes() {
        return criticalMinutes;
    }

    public void setCriticalMinutes(Integer criticalMinutes) {
        this.criticalMinutes = criticalMinutes;
    }

    public Integer getDelayMinutes() {
        return delayMinutes;
    }

    public void setDelayMinutes(Integer delayMinutes) {
        this.delayMinutes = delayMinutes;
    }

    public String getValueGenerator() {
        return valueGenerator;
    }

    public void setValueGenerator(String valueGenerator) {
        this.valueGenerator = valueGenerator;
    }

    public String getAppletSetValuesName() {
        return appletSetValuesName;
    }

    public void setAppletSetValuesName(String appletSetValuesName) {
        this.appletSetValuesName = appletSetValuesName;
    }

    public Integer getExpiryMinutes() {
        return expiryMinutes;
    }

    public void setExpiryMinutes(Integer expiryMinutes) {
        this.expiryMinutes = expiryMinutes;
    }

    public boolean isAudit() {
        return audit;
    }

    public void setAudit(boolean audit) {
        this.audit = audit;
    }

    public boolean isBranchOnly() {
        return branchOnly;
    }

    public void setBranchOnly(boolean branchOnly) {
        this.branchOnly = branchOnly;
    }

    public boolean isDepartmentOnly() {
        return departmentOnly;
    }

    public void setDepartmentOnly(boolean departmentOnly) {
        this.departmentOnly = departmentOnly;
    }

    public boolean isIncludeForwarder() {
        return includeForwarder;
    }

    public void setIncludeForwarder(boolean includeForwarder) {
        this.includeForwarder = includeForwarder;
    }

    public boolean isForwarderPreffered() {
        return forwarderPreffered;
    }

    public void setForwarderPreffered(boolean forwarderPreffered) {
        this.forwarderPreffered = forwarderPreffered;
    }

    public int getDesignX() {
        return designX;
    }

    public void setDesignX(int designX) {
        this.designX = designX;
    }

    public int getDesignY() {
        return designY;
    }

    public void setDesignY(int designY) {
        this.designY = designY;
    }

    public String isEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String isComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public SetValuesConfig getSetValuesConfig() {
        return setValuesConfig;
    }

    public void setSetValuesConfig(SetValuesConfig setValuesConfig) {
        this.setValuesConfig = setValuesConfig;
    }

    public WfRoutingsConfig getWfRoutingsConfig() {
        return wfRoutingsConfig;
    }

    public void setWfRoutingsConfig(WfRoutingsConfig wfRoutingsConfig) {
        this.wfRoutingsConfig = wfRoutingsConfig;
    }

    public WfUserActionsConfig getWfUserActionsConfig() {
        return wfUserActionsConfig;
    }

    public void setWfUserActionsConfig(WfUserActionsConfig wfUserActionsConfig) {
        this.wfUserActionsConfig = wfUserActionsConfig;
    }

    public WfAlertsConfig getWfAlertsConfig() {
        return wfAlertsConfig;
    }

    public void setWfAlertsConfig(WfAlertsConfig wfAlertsConfig) {
        this.wfAlertsConfig = wfAlertsConfig;
    }

}

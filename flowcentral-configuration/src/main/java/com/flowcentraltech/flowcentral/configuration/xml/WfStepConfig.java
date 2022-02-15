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

package com.flowcentraltech.flowcentral.configuration.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
public class WfStepConfig extends BaseNameConfig {

    private WorkflowStepType type;

    private WorkflowStepPriority priority;

    private RecordActionType actionType;

    private String appletName;

    private String nextStepName;

    private String altNextStepName;

    private String binaryCondition;

    private String readOnlyCondition;

    private String policy;

    private String rule;

    private Integer criticalMinutes;

    private Integer expiryMinutes;

    private boolean audit;

    private boolean branchOnly;

    private boolean includeForwarder;

    private boolean forwarderPreffered;
    
    private SetValuesConfig setValuesConfig;

    private WfRoutingsConfig wfRoutingsConfig;

    private WfUserActionsConfig wfUserActionsConfig;

    private WfAlertsConfig wfAlertsConfig;

    public WfStepConfig() {
        priority = WorkflowStepPriority.NORMAL;
    }

    public WorkflowStepType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(WorkflowStepTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setType(WorkflowStepType type) {
        this.type = type;
    }

    public WorkflowStepPriority getPriority() {
        return priority;
    }

    @XmlJavaTypeAdapter(WorkflowStepPriorityXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setPriority(WorkflowStepPriority priority) {
        this.priority = priority;
    }

    public RecordActionType getActionType() {
        return actionType;
    }

    @XmlJavaTypeAdapter(RecordActionTypeXmlAdapter.class)
    @XmlAttribute(name = "actionType", required = true)
    public void setActionType(RecordActionType actionType) {
        this.actionType = actionType;
    }

    public String getAppletName() {
        return appletName;
    }

    @XmlAttribute(name = "applet")
    public void setAppletName(String appletName) {
        this.appletName = appletName;
    }

    @XmlAttribute(name = "nextStep")
    public void setNextStepName(String nextStepName) {
        this.nextStepName = nextStepName;
    }

    public String getNextStepName() {
        return nextStepName;
    }

    public String getAltNextStepName() {
        return altNextStepName;
    }

    @XmlAttribute(name = "altNextStep")
    public void setAltNextStepName(String altNextStepName) {
        this.altNextStepName = altNextStepName;
    }

    public String getBinaryCondition() {
        return binaryCondition;
    }

    @XmlAttribute
    public void setBinaryCondition(String binaryCondition) {
        this.binaryCondition = binaryCondition;
    }

    public String getReadOnlyCondition() {
        return readOnlyCondition;
    }

    @XmlAttribute
    public void setReadOnlyCondition(String readOnlyCondition) {
        this.readOnlyCondition = readOnlyCondition;
    }

    public String getPolicy() {
        return policy;
    }

    @XmlAttribute
    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getRule() {
        return rule;
    }

    @XmlAttribute
    public void setRule(String rule) {
        this.rule = rule;
    }

    public Integer getCriticalMinutes() {
        return criticalMinutes;
    }

    @XmlAttribute
    public void setCriticalMinutes(Integer criticalMinutes) {
        this.criticalMinutes = criticalMinutes;
    }

    public Integer getExpiryMinutes() {
        return expiryMinutes;
    }

    @XmlAttribute
    public void setExpiryMinutes(Integer expiryMinutes) {
        this.expiryMinutes = expiryMinutes;
    }

    public boolean isAudit() {
        return audit;
    }

    @XmlAttribute
    public void setAudit(boolean audit) {
        this.audit = audit;
    }

    public boolean isBranchOnly() {
        return branchOnly;
    }

    @XmlAttribute
    public void setBranchOnly(boolean branchOnly) {
        this.branchOnly = branchOnly;
    }

    public boolean isIncludeForwarder() {
        return includeForwarder;
    }

    @XmlAttribute
    public void setIncludeForwarder(boolean includeForwarder) {
        this.includeForwarder = includeForwarder;
    }

    public boolean isForwarderPreffered() {
        return forwarderPreffered;
    }

    @XmlAttribute
    public void setForwarderPreffered(boolean forwarderPreffered) {
        this.forwarderPreffered = forwarderPreffered;
    }

    public SetValuesConfig getSetValuesConfig() {
        return setValuesConfig;
    }

    @XmlElement(name = "setValues")
    public void setSetValuesConfig(SetValuesConfig setValuesConfig) {
        this.setValuesConfig = setValuesConfig;
    }

    public WfRoutingsConfig getWfRoutingsConfig() {
        return wfRoutingsConfig;
    }

    @XmlElement(name = "routings")
    public void setWfRoutingsConfig(WfRoutingsConfig wfRoutingsConfig) {
        this.wfRoutingsConfig = wfRoutingsConfig;
    }

    public WfUserActionsConfig getWfUserActionsConfig() {
        return wfUserActionsConfig;
    }

    @XmlElement(name = "userActions")
    public void setWfUserActionsConfig(WfUserActionsConfig wfUserActionsConfig) {
        this.wfUserActionsConfig = wfUserActionsConfig;
    }

    public WfAlertsConfig getWfAlertsConfig() {
        return wfAlertsConfig;
    }

    @XmlElement(name = "alerts")
    public void setWfAlertsConfig(WfAlertsConfig wfAlertsConfig) {
        this.wfAlertsConfig = wfAlertsConfig;
    }

    @Override
    public String toString() {
        return "WfStepConfig [type=" + type + ", priority=" + priority + ", actionType=" + actionType + ", label="
                + getLabel() + ", appletName=" + appletName + ", nextStepName=" + nextStepName + ", policy=" + policy + ", rule="
                + rule + ", criticalMinutes=" + criticalMinutes + ", expiryMinutes=" + expiryMinutes + ", audit="
                + audit + ", branchOnly=" + branchOnly + ", includeForwarder=" + includeForwarder + ", setValuesConfig="
                + setValuesConfig + ", wfRoutingsConfig=" + wfRoutingsConfig + ", wfUserActionsConfig="
                + wfUserActionsConfig + ", wfAlertsConfig=" + wfAlertsConfig + "]";
    }

}

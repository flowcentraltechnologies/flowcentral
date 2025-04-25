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

package com.flowcentraltech.flowcentral.configuration.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowAlertType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.WorkflowAlertTypeXmlAdapter;

/**
 * Workflow alert configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class WfAlertConfig extends BaseNameConfig {

    @JsonSerialize(using = WorkflowAlertTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = WorkflowAlertTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "type")
    private WorkflowAlertType type;

    @JacksonXmlProperty(isAttribute = true)
    private String recipientPolicy;

    @JacksonXmlProperty(isAttribute = true)
    private String recipientNameRule;

    @JacksonXmlProperty(isAttribute = true)
    private String recipientContactRule;

    @JacksonXmlProperty(isAttribute = true)
    private String generator;
    
    @JacksonXmlProperty(isAttribute = true)
    private String template;

    @JacksonXmlProperty(isAttribute = true)
    private String fireOnPrevStepName;
    
    @JacksonXmlProperty(isAttribute = true)
    private String fireOnActionName;

    @JacksonXmlProperty(isAttribute = true)
    private String fireOnCondition;
    
    @JacksonXmlProperty(isAttribute = true)
    private Integer sendDelayInMinutes;

    @JacksonXmlProperty(isAttribute = true)
    private boolean alertHeldBy;

    @JacksonXmlProperty(isAttribute = true)
    private boolean alertWorkflowRoles;


    public WorkflowAlertType getType() {
        return type;
    }

    public void setType(WorkflowAlertType type) {
        this.type = type;
    }

    public String getRecipientPolicy() {
        return recipientPolicy;
    }

    public void setRecipientPolicy(String recipientPolicy) {
        this.recipientPolicy = recipientPolicy;
    }

    public String getRecipientNameRule() {
        return recipientNameRule;
    }

    public void setRecipientNameRule(String recipientNameRule) {
        this.recipientNameRule = recipientNameRule;
    }

    public String getRecipientContactRule() {
        return recipientContactRule;
    }

    public void setRecipientContactRule(String recipientContactRule) {
        this.recipientContactRule = recipientContactRule;
    }

    public String getGenerator() {
        return generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public Integer getSendDelayInMinutes() {
        return sendDelayInMinutes;
    }

    public void setSendDelayInMinutes(Integer sendDelayInMinutes) {
        this.sendDelayInMinutes = sendDelayInMinutes;
    }

    public boolean isAlertHeldBy() {
        return alertHeldBy;
    }

    public void setAlertHeldBy(boolean alertHeldBy) {
        this.alertHeldBy = alertHeldBy;
    }

    public boolean isAlertWorkflowRoles() {
        return alertWorkflowRoles;
    }

    public void setAlertWorkflowRoles(boolean alertWorkflowRoles) {
        this.alertWorkflowRoles = alertWorkflowRoles;
    }

    public String getFireOnPrevStepName() {
        return fireOnPrevStepName;
    }

    public void setFireOnPrevStepName(String fireOnPrevStepName) {
        this.fireOnPrevStepName = fireOnPrevStepName;
    }

    public String getFireOnActionName() {
        return fireOnActionName;
    }

    public void setFireOnActionName(String fireOnActionName) {
        this.fireOnActionName = fireOnActionName;
    }

    public String getFireOnCondition() {
        return fireOnCondition;
    }

    public void setFireOnCondition(String fireOnCondition) {
        this.fireOnCondition = fireOnCondition;
    }

}

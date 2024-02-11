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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.flowcentraltech.flowcentral.configuration.constants.WorkflowAlertType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.WorkflowAlertTypeXmlAdapter;

/**
 * Workflow alert configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WfAlertConfig extends BaseNameConfig {

    private WorkflowAlertType type;

    private String recipientPolicy;

    private String recipientNameRule;

    private String recipientContactRule;

    private String generator;

    private String fireOnPrevStepName;
    
    private String fireOnActionName;

    private String fireOnCondition;

    private boolean alertHeldBy;

    private boolean alertWorkflowRoles;


    public WorkflowAlertType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(WorkflowAlertTypeXmlAdapter.class)
    @XmlAttribute(name = "type", required = true)
    public void setType(WorkflowAlertType type) {
        this.type = type;
    }

    public String getRecipientPolicy() {
        return recipientPolicy;
    }

    @XmlAttribute
    public void setRecipientPolicy(String recipientPolicy) {
        this.recipientPolicy = recipientPolicy;
    }

    public String getRecipientNameRule() {
        return recipientNameRule;
    }

    @XmlAttribute
    public void setRecipientNameRule(String recipientNameRule) {
        this.recipientNameRule = recipientNameRule;
    }

    public String getRecipientContactRule() {
        return recipientContactRule;
    }

    @XmlAttribute
    public void setRecipientContactRule(String recipientContactRule) {
        this.recipientContactRule = recipientContactRule;
    }

    public String getGenerator() {
        return generator;
    }

    @XmlAttribute(required = true)
    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public boolean isAlertHeldBy() {
        return alertHeldBy;
    }

    @XmlAttribute
    public void setAlertHeldBy(boolean alertHeldBy) {
        this.alertHeldBy = alertHeldBy;
    }

    public boolean isAlertWorkflowRoles() {
        return alertWorkflowRoles;
    }

    @XmlAttribute
    public void setAlertWorkflowRoles(boolean alertWorkflowRoles) {
        this.alertWorkflowRoles = alertWorkflowRoles;
    }

    public String getFireOnPrevStepName() {
        return fireOnPrevStepName;
    }

    @XmlAttribute
    public void setFireOnPrevStepName(String fireOnPrevStepName) {
        this.fireOnPrevStepName = fireOnPrevStepName;
    }

    public String getFireOnActionName() {
        return fireOnActionName;
    }

    @XmlAttribute
    public void setFireOnActionName(String fireOnActionName) {
        this.fireOnActionName = fireOnActionName;
    }

    public String getFireOnCondition() {
        return fireOnCondition;
    }

    @XmlAttribute
    public void setFireOnCondition(String fireOnCondition) {
        this.fireOnCondition = fireOnCondition;
    }

}

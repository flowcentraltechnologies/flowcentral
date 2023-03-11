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

package com.flowcentraltech.flowcentral.configuration.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.flowcentraltech.flowcentral.configuration.constants.NotificationType;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowAlertType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.NotificationTypeXmlAdapter;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.WorkflowAlertTypeXmlAdapter;

/**
 * Workflow alert configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WfAlertConfig extends BaseNameConfig {

    private WorkflowAlertType type;

    private NotificationType notificationType;

    private String recipientPolicy;

    private String recipientNameRule;

    private String recipientContactRule;

    private String template;

    private String fireOnPrevStepName;

    private String fireOnCondition;

    public WorkflowAlertType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(WorkflowAlertTypeXmlAdapter.class)
    @XmlAttribute(name = "type", required = true)
    public void setType(WorkflowAlertType type) {
        this.type = type;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    @XmlJavaTypeAdapter(NotificationTypeXmlAdapter.class)
    @XmlAttribute(name = "channel", required = true)
    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
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

    public String getTemplate() {
        return template;
    }

    @XmlAttribute(required = true)
    public void setTemplate(String template) {
        this.template = template;
    }

    public String getFireOnPrevStepName() {
        return fireOnPrevStepName;
    }

    @XmlAttribute
    public void setFireOnPrevStepName(String fireOnPrevStepName) {
        this.fireOnPrevStepName = fireOnPrevStepName;
    }

    public String getFireOnCondition() {
        return fireOnCondition;
    }

    @XmlAttribute
    public void setFireOnCondition(String fireOnCondition) {
        this.fireOnCondition = fireOnCondition;
    }

}

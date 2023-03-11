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

package com.flowcentraltech.flowcentral.workflow.data;

import com.flowcentraltech.flowcentral.configuration.constants.NotificationType;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowAlertType;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Workflow alert definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WfAlertDef {

    private WorkflowAlertType type;

    private NotificationType notificationType;

    private String name;

    private String description;

    private String recipientPolicy;

    private String recipientNameRule;

    private String recipientContactRule;

    private String template;

    private String fireOnPrevStepName;

    private String fireOnCondition;

    public WfAlertDef(WorkflowAlertType type, NotificationType notificationType, String name, String description,
            String recipientPolicy, String recipientNameRule, String recipientContactRule, String template,
            String fireOnPrevStepName, String fireOnCondition) {
        this.type = type;
        this.notificationType = notificationType;
        this.name = name;
        this.description = description;
        this.recipientPolicy = recipientPolicy;
        this.recipientNameRule = recipientNameRule;
        this.recipientContactRule = recipientContactRule;
        this.template = template;
        this.fireOnPrevStepName = fireOnPrevStepName;
        this.fireOnCondition = fireOnCondition;
    }

    public WorkflowAlertType getType() {
        return type;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRecipientPolicy() {
        return recipientPolicy;
    }

    public boolean isWithRecipientPolicy() {
        return !StringUtils.isBlank(recipientPolicy);
    }

    public String getRecipientNameRule() {
        return recipientNameRule;
    }

    public String getRecipientContactRule() {
        return recipientContactRule;
    }

    public String getTemplate() {
        return template;
    }

    public String getFireOnPrevStepName() {
        return fireOnPrevStepName;
    }

    public boolean isFireAlertOnPreviousStep(String prevStepName) {
        return StringUtils.isBlank(fireOnPrevStepName) || fireOnPrevStepName.equals(prevStepName);
    }

    public boolean isWithFireAlertOnCondition() {
        return fireOnCondition != null;
    }

    public String getFireOnCondition() {
        return fireOnCondition;
    }

    public boolean isPassThrough() {
        return type.isPassThrough();
    }

    public boolean isUserInteract() {
        return type.isUserInteract();
    }

    public boolean isCriticalNotification() {
        return type.isCriticalNotification();
    }

    public boolean isExpirationNotification() {
        return type.isExpirationNotification();
    }

    @Override
    public String toString() {
        return "WfAlertDef [type=" + type + ", notificationType=" + notificationType + ", name=" + name
                + ", description=" + description + ", recipientPolicy=" + recipientPolicy + ", template=" + template
                + ", fireOnPrevStepName=" + fireOnPrevStepName + ", fireOnCondition=" + fireOnCondition + "]";
    }

}

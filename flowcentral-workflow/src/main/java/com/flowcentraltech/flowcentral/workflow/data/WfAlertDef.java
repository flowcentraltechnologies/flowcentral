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

package com.flowcentraltech.flowcentral.workflow.data;

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

    private String name;

    private String description;

    private String recipientPolicy;

    private String recipientNameRule;

    private String recipientContactRule;

    private String generator;

    private String template;

    private String fireOnPrevStepName;

    private String fireOnAction;

    private String fireOnCondition;

    private boolean alertHeldBy;

    private boolean alertWorkflowRoles;

    public WfAlertDef(WorkflowAlertType type, String name, String description, String recipientPolicy,
            String recipientNameRule, String recipientContactRule, String generator, String template, String fireOnPrevStepName,
            String fireOnAction, String fireOnCondition, boolean alertHeldBy, boolean alertWorkflowRoles) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.recipientPolicy = recipientPolicy;
        this.recipientNameRule = recipientNameRule;
        this.recipientContactRule = recipientContactRule;
        this.generator = generator;
        this.template = template;
        this.fireOnPrevStepName = fireOnPrevStepName;
        this.fireOnCondition = fireOnCondition;
        this.alertHeldBy = alertHeldBy;
        this.alertWorkflowRoles = alertWorkflowRoles;
    }

    public WorkflowAlertType getType() {
        return type;
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

    public String getGenerator() {
        return generator;
    }

    public String getTemplate() {
        return template;
    }

    public boolean isAlertHeldBy() {
        return alertHeldBy;
    }

    public boolean isAlertWorkflowRoles() {
        return alertWorkflowRoles;
    }

    public String getFireOnPrevStepName() {
        return fireOnPrevStepName;
    }

    public boolean isFireAlertOnPreviousStep(String prevStepName) {
        return StringUtils.isBlank(fireOnAction)
                && (StringUtils.isBlank(fireOnPrevStepName) || fireOnPrevStepName.equals(prevStepName));
    }

    public String getFireOnAction() {
        return fireOnAction;
    }

    public boolean isFireAlertOnAction(String actionName) {
        return fireOnAction != null && fireOnAction.equals(actionName);
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

    public boolean isOnReminder() {
        return type.isOnReminder();
    }

    public boolean isOnCritical() {
        return type.isOnCritical();
    }

    public boolean isOnExpiration() {
        return type.isOnExpiration();
    }

}

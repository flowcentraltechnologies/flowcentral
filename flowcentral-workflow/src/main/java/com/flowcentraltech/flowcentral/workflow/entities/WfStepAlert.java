/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.workflow.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseNamedEntity;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowAlertType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Workflow step alert entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_WORKSTEPALERT", uniqueConstraints = { @UniqueConstraint({ "wfStepId", "name" }),
        @UniqueConstraint({ "wfStepId", "description" }) })
public class WfStepAlert extends BaseNamedEntity {

    @ForeignKey(WfStep.class)
    private Long wfStepId;

    @ForeignKey(name = "ALERT_TY")
    private WorkflowAlertType type;

    @Column(length = 64, nullable = true)
    private String recipientPolicy;

    @Column(length = 64, nullable = true)
    private String recipientNameRule;

    @Column(length = 64, nullable = true)
    private String recipientContactRule;

    @Column(length = 64, nullable = true)
    private String generator;

    @Column(length = 64, nullable = true)
    private String template;

    @Column(nullable = true)
    private Integer sendDelayInMinutes;
    
    @Column
    private boolean alertHeldBy;

    @Column
    private boolean alertWorkflowRoles;

    @Column(name = "FIRE_ON_PREV_STEP_NM", length = 64, nullable = true)
    private String fireOnPrevStepName;

    @Column(name = "FIRE_ON_ACTION_NM", length = 64, nullable = true)
    private String fireOnActionName;

    @Column(name = "FIRE_ON_CONDITION_NM", length = 64, nullable = true)
    private String fireOnConditionName;

    @ListOnly(key = "wfStepId", property = "workflowName")
    private String workflowName;

    @ListOnly(key = "wfStepId", property = "description")
    private String wfStepDesc;

    @ListOnly(key = "wfStepId", property = "name")
    private String wfStepName;

    @ListOnly(key = "wfStepId", property = "applicationName")
    private String applicationName;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    public Long getWfStepId() {
        return wfStepId;
    }

    public void setWfStepId(Long wfStepId) {
        this.wfStepId = wfStepId;
    }

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

    public String getFireOnConditionName() {
        return fireOnConditionName;
    }

    public void setFireOnConditionName(String fireOnConditionName) {
        this.fireOnConditionName = fireOnConditionName;
    }

    public String getWfStepDesc() {
        return wfStepDesc;
    }

    public void setWfStepDesc(String wfStepDesc) {
        this.wfStepDesc = wfStepDesc;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getWfStepName() {
        return wfStepName;
    }

    public void setWfStepName(String wfStepName) {
        this.wfStepName = wfStepName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

}

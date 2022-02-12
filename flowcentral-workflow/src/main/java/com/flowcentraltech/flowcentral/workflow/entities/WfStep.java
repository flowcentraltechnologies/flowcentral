/*
 * Copyright (c) 2019, 2021, FlowCentral Technologies.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
 */

package com.flowcentraltech.flowcentral.workflow.entities;

import java.util.List;

import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntity;
import com.flowcentraltech.flowcentral.configuration.constants.RecordActionType;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepPriority;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepType;
import com.tcdng.unify.core.annotation.Child;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Workflow step entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "FC_WORKSTEP", uniqueConstraints = { @UniqueConstraint({ "workflowId", "name" }),
        @UniqueConstraint({ "workflowId", "description" }) })
public class WfStep extends BaseConfigNamedEntity {

    @ForeignKey(Workflow.class)
    private Long workflowId;

    @ForeignKey(name = "STEP_TYPE")
    private WorkflowStepType type;

    @ForeignKey
    private WorkflowStepPriority priority;

    @ForeignKey(nullable = true)
    private RecordActionType recordActionType;

    @Column(name = "WFSTEP_LABEL", length = 64)
    private String label;

    @Column(name = "APPLET_NM", length = 128, nullable = true)
    private String appletName;

    @Column(name = "NEXT_STEP_NM", length = 64, nullable = true)
    private String nextStepName;

    @Column(name = "ALT_NEXT_STEP_NM", length = 64, nullable = true)
    private String altNextStepName;

    @Column(name = "BINARY_CONDITION_NM", length = 64, nullable = true)
    private String binaryConditionName;

    @Column(name = "READONLY_CONDITION_NM", length = 64, nullable = true)
    private String readOnlyConditionName;

    @Column(name = "STEP_POLICY", length = 64, nullable = true)
    private String policy;

    @Column(name = "STEP_RULE", length = 64, nullable = true)
    private String rule;

    @Column
    private int designX;

    @Column
    private int designY;

    @Column(nullable = true)
    private Integer criticalMinutes;

    @Column(nullable = true)
    private Integer expiryMinutes;

    @Column
    private boolean audit;

    @Column
    private boolean branchOnly;

    @Column
    private boolean includeForwarder;

    @Column
    private boolean forwarderPreffered;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    @ListOnly(key = "priority", property = "description")
    private String priorityDesc;

    @ListOnly(key = "workflowId", property = "name")
    private String workflowName;

    @ListOnly(key = "workflowId", property = "description")
    private String workflowDesc;

    @ListOnly(key = "workflowId", property = "entity")
    private String entityName;

    @ListOnly(key = "workflowId", property = "applicationName")
    private String applicationName;

    @ListOnly(key = "workflowId", property = "applicationDesc")
    private String applicationDesc;

    @ListOnly(key = "recordActionType", property = "description")
    private String recordActionTypeDesc;

    @Child
    private WfStepSetValues setValues;

    @ChildList
    private List<WfStepRouting> routingList;

    @ChildList
    private List<WfStepUserAction> userActionList;

    @ChildList
    private List<WfStepAlert> alertList;

    @ChildList
    private List<WfStepRole> roleList;

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
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

    public RecordActionType getRecordActionType() {
        return recordActionType;
    }

    public void setRecordActionType(RecordActionType recordActionType) {
        this.recordActionType = recordActionType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAppletName() {
        return appletName;
    }

    public void setAppletName(String appletName) {
        this.appletName = appletName;
    }

    public String getNextStepName() {
        return nextStepName;
    }

    public void setNextStepName(String nextStepName) {
        this.nextStepName = nextStepName;
    }

    public String getAltNextStepName() {
        return altNextStepName;
    }

    public void setAltNextStepName(String altNextStepName) {
        this.altNextStepName = altNextStepName;
    }

    public String getBinaryConditionName() {
        return binaryConditionName;
    }

    public void setBinaryConditionName(String binaryConditionName) {
        this.binaryConditionName = binaryConditionName;
    }

    public String getReadOnlyConditionName() {
        return readOnlyConditionName;
    }

    public void setReadOnlyConditionName(String readOnlyConditionName) {
        this.readOnlyConditionName = readOnlyConditionName;
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

    public Integer getCriticalMinutes() {
        return criticalMinutes;
    }

    public void setCriticalMinutes(Integer criticalMinutes) {
        this.criticalMinutes = criticalMinutes;
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

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getPriorityDesc() {
        return priorityDesc;
    }

    public void setPriorityDesc(String priorityDesc) {
        this.priorityDesc = priorityDesc;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public String getWorkflowDesc() {
        return workflowDesc;
    }

    public void setWorkflowDesc(String workflowDesc) {
        this.workflowDesc = workflowDesc;
    }

    public String getRecordActionTypeDesc() {
        return recordActionTypeDesc;
    }

    public void setRecordActionTypeDesc(String recordActionTypeDesc) {
        this.recordActionTypeDesc = recordActionTypeDesc;
    }

    public WfStepSetValues getSetValues() {
        return setValues;
    }

    public void setSetValues(WfStepSetValues setValues) {
        this.setValues = setValues;
    }

    public List<WfStepRouting> getRoutingList() {
        return routingList;
    }

    public void setRoutingList(List<WfStepRouting> routingList) {
        this.routingList = routingList;
    }

    public List<WfStepUserAction> getUserActionList() {
        return userActionList;
    }

    public void setUserActionList(List<WfStepUserAction> userActionList) {
        this.userActionList = userActionList;
    }

    public List<WfStepAlert> getAlertList() {
        return alertList;
    }

    public void setAlertList(List<WfStepAlert> alertList) {
        this.alertList = alertList;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationDesc() {
        return applicationDesc;
    }

    public void setApplicationDesc(String applicationDesc) {
        this.applicationDesc = applicationDesc;
    }

    public List<WfStepRole> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<WfStepRole> roleList) {
        this.roleList = roleList;
    }

}

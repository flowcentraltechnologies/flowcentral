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

package com.flowcentraltech.flowcentral.workflow.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.FormActionDef;
import com.flowcentraltech.flowcentral.configuration.constants.HighlightType;
import com.flowcentraltech.flowcentral.configuration.constants.NotificationType;
import com.flowcentraltech.flowcentral.configuration.constants.ProcessingStatus;
import com.flowcentraltech.flowcentral.configuration.constants.RecordActionType;
import com.flowcentraltech.flowcentral.configuration.constants.UIActionType;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowAlertType;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepPriority;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepType;
import com.flowcentraltech.flowcentral.workflow.constants.WfStepConstants;
import com.tcdng.unify.core.constant.RequirementType;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Workflow step definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WfStepDef {

    private AppletDef appletDef;

    private WorkflowStepType type;

    private WorkflowStepPriority priority;

    private RecordActionType recordActionType;

    private String nextStepName;

    private String altNextStepName;

    private String binaryConditionName;

    private String readOnlyConditionName;

    private String autoLoadingConditionName;

    private String policy;

    private String rule;

    private String name;

    private String description;

    private String label;

    private int criticalMinutes;

    private int expiryMinutes;

    private boolean audit;

    private boolean branchOnly;

    private boolean includeForwarder;

    private boolean forwarderPreferred;
    
    private boolean comments;

    private WfSetValuesDef wfSetValuesDef;

    private Map<String, WfUserActionDef> userActions;

    private List<WfRoutingDef> routingList;

    private List<WfAlertDef> alertList;

    private List<FormActionDef> formActionDefList;

    private Set<String> roleSet;

    private WfStepDef(AppletDef appletDef, WorkflowStepType type, WorkflowStepPriority priority,
            RecordActionType recordActionType, String nextStepName, String altNextStepName, String binaryConditionName,
            String readOnlyConditionName, String autoLoadingConditionName, String policy, String rule, String name,
            String description, String label, int criticalMinutes, int expiryMinutes, boolean audit, boolean branchOnly,
            boolean includeForwarder, boolean forwarderPreferred, boolean comments, WfSetValuesDef wfSetValuesDef,
            Map<String, WfUserActionDef> userActions, List<WfRoutingDef> routingList, List<WfAlertDef> alertList,
            Set<String> roleSet) {
        this.appletDef = appletDef;
        this.type = type;
        this.priority = priority;
        this.recordActionType = recordActionType;
        this.nextStepName = nextStepName;
        this.altNextStepName = altNextStepName;
        this.binaryConditionName = binaryConditionName;
        this.readOnlyConditionName = readOnlyConditionName;
        this.autoLoadingConditionName = autoLoadingConditionName;
        this.policy = policy;
        this.rule = rule;
        this.name = name;
        this.description = description;
        this.label = label;
        this.criticalMinutes = criticalMinutes;
        this.expiryMinutes = expiryMinutes;
        this.audit = audit;
        this.branchOnly = branchOnly;
        this.includeForwarder = includeForwarder;
        this.forwarderPreferred = forwarderPreferred;
        this.comments = comments;
        this.userActions = userActions;
        this.formActionDefList = Collections.emptyList();
        if (!DataUtils.isBlank(userActions)) {
            this.formActionDefList = new ArrayList<FormActionDef>();
            for (WfUserActionDef wfUserActionDef : userActions.values()) {
                this.formActionDefList.add(new FormActionDef(UIActionType.BUTTON, wfUserActionDef.getHighlightType(),
                        wfUserActionDef.getName(), wfUserActionDef.getDescription(), wfUserActionDef.getLabel(),
                        wfUserActionDef.getSymbol(), wfUserActionDef.getStyleClass(), wfUserActionDef.getOrderIndex(),
                        wfUserActionDef.isValidatePage(), null));
            }
        }

        this.routingList = routingList;
        this.wfSetValuesDef = wfSetValuesDef;
        this.alertList = alertList;
        this.roleSet = roleSet;
    }

    public AppletDef getAppletDef() {
        return appletDef;
    }

    public WorkflowStepType getType() {
        return type;
    }

    public ProcessingStatus getProcessingStatus() {
        return type.processingStatus();
    }

    public WorkflowStepPriority getPriority() {
        return priority;
    }

    public RecordActionType getRecordActionType() {
        return recordActionType;
    }

    public boolean isWithRecordAction() {
        return recordActionType != null;
    }

    public String getNextStepName() {
        return nextStepName;
    }

    public boolean isWithNextStepName() {
        return !StringUtils.isBlank(nextStepName);
    }

    public String getAltNextStepName() {
        return altNextStepName;
    }

    public boolean isWithAltNextStepName() {
        return !StringUtils.isBlank(altNextStepName);
    }

    public String getBinaryConditionName() {
        return binaryConditionName;
    }

    public boolean isWithBinaryConditionName() {
        return !StringUtils.isBlank(binaryConditionName);
    }

    public String getReadOnlyConditionName() {
        return readOnlyConditionName;
    }

    public boolean isWithReadOnlyCondition() {
        return !StringUtils.isBlank(readOnlyConditionName);
    }

    public boolean isReadOnlyAlways() {
        return readOnlyConditionName != null
                && WfStepConstants.RESERVED_READONLY_FILTERNAME.equals(readOnlyConditionName);
    }

    public String getAutoLoadingConditionName() {
        return autoLoadingConditionName;
    }

    public boolean isWithAutoLoadingCondition() {
        return !StringUtils.isBlank(autoLoadingConditionName);
    }

    public Map<String, WfUserActionDef> getUserActions() {
        return userActions;
    }

    public String getPolicy() {
        return policy;
    }

    public boolean isWithPolicy() {
        return !StringUtils.isBlank(policy);
    }

    public String getRule() {
        return rule;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLabel() {
        return label;
    }

    public int getCriticalMinutes() {
        return criticalMinutes;
    }

    public int getExpiryMinutes() {
        return expiryMinutes;
    }

    public boolean isWithApplet() {
        return appletDef != null;
    }

    public boolean isAudit() {
        return audit;
    }

    public boolean isBranchOnly() {
        return branchOnly;
    }

    public boolean isIncludeForwarder() {
        return includeForwarder;
    }

    public boolean isForwarderPreferred() {
        return forwarderPreferred;
    }

    public boolean isComments() {
        return comments;
    }

    public List<FormActionDef> getFormActionDefList() {
        return formActionDefList;
    }

    public WfUserActionDef getUserActionDef(String userActionName) {
        WfUserActionDef wfUserActionDef = userActions.get(userActionName);
        if (wfUserActionDef == null) {
            throw new RuntimeException(
                    "Step with name [" + name + "] does not have user action [" + userActionName + "]");
        }

        return wfUserActionDef;
    }

    public List<WfRoutingDef> getRoutingList() {
        return routingList;
    }

    public WfSetValuesDef getWfSetValuesDef() {
        return wfSetValuesDef;
    }

    public List<WfAlertDef> getAlertList() {
        return alertList;
    }

    public Set<String> getRoleSet() {
        return roleSet;
    }

    public boolean isAutoFlow() {
        return type.isAutomatic() || type.isStart();
    }

    public boolean isSettling() {
        return type.isSettling();
    }

    public boolean isUserInteractive() {
        return type.isInteractive();
    }

    public boolean isUserAction() {
        return type.isUserAction();
    }

    public boolean isStart() {
        return type.isStart();
    }

    public boolean isAutomatic() {
        return type.isAutomatic();
    }

    public boolean isError() {
        return type.isError();
    }

    public boolean isEnd() {
        return type.isEnd();
    }

    public boolean isWithParticipatingRoles() {
        return !roleSet.isEmpty();
    }

    public static Builder newBuilder(AppletDef appletDef, WorkflowStepType type, WorkflowStepPriority priority,
            RecordActionType recordActionType, String nextStepName, String altNextStepName, String binaryConditionName,
            String readOnlyConditionName, String autoLoadingConditionName, String policy, String rule, String name,
            String description, String label, int criticalMinutes, int expiryMinutes, boolean audit, boolean branchOnly,
            boolean includeForwarder, boolean forwarderPreferred, boolean comments) {
        return new Builder(appletDef, type, priority, recordActionType, nextStepName, altNextStepName,
                binaryConditionName, readOnlyConditionName, autoLoadingConditionName, policy, rule, name, description,
                label, criticalMinutes, expiryMinutes, audit, branchOnly, includeForwarder, forwarderPreferred, comments);
    }

    public static class Builder {

        private AppletDef appletDef;

        private WorkflowStepType type;

        private WorkflowStepPriority priority;

        private RecordActionType recordActionType;

        private String nextStepName;

        private String altNextStepName;

        private String binaryConditionName;

        private String readOnlyConditionName;

        private String autoLoadingConditionName;

        private String policy;

        private String rule;

        private String name;

        private String description;

        private String label;

        private int criticalMinutes;

        private int expiryMinutes;

        private boolean audit;

        private boolean branchOnly;

        private boolean includeForwarder;

        private boolean forwarderPreferred;
        
        private boolean comments;

        private WfSetValuesDef wfSetValuesDef;

        private Map<String, WfUserActionDef> userActionList;

        private Map<String, WfRoutingDef> routingList;

        private Map<String, WfAlertDef> alertList;

        private Set<String> roleSet;

        public Builder(AppletDef appletDef, WorkflowStepType type, WorkflowStepPriority priority,
                RecordActionType recordActionType, String nextStepName, String altNextStepName,
                String binaryConditionName, String readOnlyConditionName, String autoLoadingConditionName,
                String policy, String rule, String name, String description, String label, int criticalMinutes,
                int expiryMinutes, boolean audit, boolean branchOnly, boolean includeForwarder,
                boolean forwarderPreferred, boolean comments) {
            this.appletDef = appletDef;
            this.type = type;
            this.priority = priority;
            this.recordActionType = recordActionType;
            this.priority = priority;
            this.rule = rule;
            this.nextStepName = nextStepName;
            this.altNextStepName = altNextStepName;
            this.binaryConditionName = binaryConditionName;
            this.readOnlyConditionName = readOnlyConditionName;
            this.autoLoadingConditionName = autoLoadingConditionName;
            this.policy = policy;
            this.name = name;
            this.description = description;
            this.label = label;
            this.criticalMinutes = criticalMinutes;
            this.expiryMinutes = expiryMinutes;
            this.audit = audit;
            this.branchOnly = branchOnly;
            this.includeForwarder = includeForwarder;
            this.forwarderPreferred = forwarderPreferred;
            this.comments = comments;
        }

        public Builder addWfUserActionDef(RequirementType commentRequirement, HighlightType highlightType, String name,
                String description, String label, String symbol, String styleClass, String nextStepName, int orderIndex,
                boolean validatePage, boolean forwarderPreferred) {
            if (userActionList == null) {
                userActionList = new LinkedHashMap<String, WfUserActionDef>();
            }

            if (userActionList.containsKey(name)) {
                throw new RuntimeException("User action with name [" + name + "] already exists in this step.");
            }

            if (!type.isInteractive()) {
                throw new RuntimeException("Can not add user action policy to step type [" + type + "].");
            }

            userActionList.put(name, new WfUserActionDef(commentRequirement, highlightType, name, description, label,
                    symbol, styleClass, nextStepName, orderIndex, validatePage, forwarderPreferred));
            return this;
        }

        public Builder addWfRoutingDef(String name, String description, String condition, String nextStepName) {
            if (routingList == null) {
                routingList = new LinkedHashMap<String, WfRoutingDef>();
            }

            if (routingList.containsKey(name)) {
                throw new RuntimeException("Routing with name [" + name + "] already exists in this step.");
            }

            if (!WorkflowStepType.MULTI_ROUTING.equals(type)) {
                throw new RuntimeException("Can not add routing to step type [" + type + "].");
            }

            routingList.put(name, new WfRoutingDef(name, description, condition, nextStepName));
            return this;
        }

        public Builder addWfSetValuesDef(WfSetValuesDef wfSetValuesDef) {
            if (!type.supportSetValues()) {
                throw new RuntimeException("Can not add set values to step type [" + type + "].");
            }

            this.wfSetValuesDef = wfSetValuesDef;
            return this;
        }

        public Builder addWfAlertDef(WorkflowAlertType type, NotificationType notificationType, String name,
                String description, String recipientPolicy, String recipientNameRule, String recipientContactRule,
                String template, String fireOnPrevStepName, String fireOnCondition) {
            if (alertList == null) {
                alertList = new LinkedHashMap<String, WfAlertDef>();
            }

            if (alertList.containsKey(name)) {
                throw new RuntimeException("Alert with name [" + name + "] already exists in this step.");
            }

            alertList.put(name, new WfAlertDef(type, notificationType, name, description, recipientPolicy,
                    recipientNameRule, recipientContactRule, template, fireOnPrevStepName, fireOnCondition));
            return this;
        }

        public Builder addParticipatingRole(String roleCode) {
            if (roleSet == null) {
                roleSet = new HashSet<String>();
            }

            roleSet.add(roleCode);
            return this;
        }

        public WfStepDef build() {
            return new WfStepDef(appletDef, type, priority, recordActionType, nextStepName, altNextStepName,
                    binaryConditionName, readOnlyConditionName, autoLoadingConditionName, policy, rule, name,
                    description, label, criticalMinutes, expiryMinutes, audit, branchOnly, includeForwarder,
                    forwarderPreferred, comments, wfSetValuesDef, DataUtils.unmodifiableMap(userActionList),
                    DataUtils.unmodifiableValuesList(routingList), DataUtils.unmodifiableValuesList(alertList),
                    DataUtils.unmodifiableSet(roleSet));
        }
    }

}

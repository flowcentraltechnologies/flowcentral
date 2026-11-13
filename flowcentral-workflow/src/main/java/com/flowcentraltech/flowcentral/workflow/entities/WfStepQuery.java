/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

import java.util.Arrays;
import java.util.List;

import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntityQuery;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepType;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.AndBuilder;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.IsNotNull;
import com.tcdng.unify.core.criterion.NotEquals;
import com.tcdng.unify.core.criterion.OrBuilder;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.database.Query;

/**
 * Workflow step query.
 * 
 * @author FlowCentral Technologies Limited
 * @version 1.0
 */
public class WfStepQuery extends BaseConfigNamedEntityQuery<WfStep> {

    private static final List<WorkflowStepType> ACTIONABLES = Arrays.asList(
            WorkflowStepType.START,
            WorkflowStepType.USER_ACTION,
            WorkflowStepType.ERROR);

    private static final Restriction EJECTION_CHECK_DUE = new AndBuilder()
                                                                .equals("workflowRunnable", true)
                                                                .equals("type", WorkflowStepType.DELAY)
                                                                .restriction(new OrBuilder()
                                                                            .isnull("ejectCheckOn")
                                                                            .less("ejectCheckOn", Query.expOfNow())
                                                                            .build())
                                                                .build();

    private static final Restriction REMINDER_CHECK_DUE = new AndBuilder()
                                                                .equals("workflowRunnable", true)
                                                                .equals("type", WorkflowStepType.USER_ACTION)
                                                                .restriction(new OrBuilder()
                                                                            .isnull("reminderCheckOn")
                                                                            .less("reminderCheckOn", Query.expOfNow())
                                                                            .build())
                                                                .build();

    private static final Restriction AUTOLOADING_CHECK_DUE = new AndBuilder()
                                                                .equals("workflowRunnable", true)
                                                                .equals("type", WorkflowStepType.START)
                                                                .notnull("autoLoadConditionName")
                                                                .restriction(new OrBuilder()
                                                                            .isnull("autoLoadingCheckOn")
                                                                            .less("autoLoadingCheckOn", Query.expOfNow())
                                                                            .build())
                                                                .build();

    public WfStepQuery() {
        super(WfStep.class);
    }

    public WfStepQuery workflowId(Long workflowId) {
        return (WfStepQuery) equals("workflowId", workflowId);
    }

    public WfStepQuery userActionable() {
        return (WfStepQuery) notnull("appletName").amongst("type", ACTIONABLES);
    }

    public WfStepQuery routableTo() {
        return (WfStepQuery) restriction(new And().add(new NotEquals("type", WorkflowStepType.START))
                .add(new NotEquals("type", WorkflowStepType.ERROR)));
    }

    public WfStepQuery receivableFrom() {
        return (WfStepQuery) restriction(new And().add(new NotEquals("type", WorkflowStepType.END))
                .add(new NotEquals("type", WorkflowStepType.ERROR)));
    }

    public WfStepQuery supportsAutoload() {
        return (WfStepQuery) restriction(
                new And().add(new Equals("type", WorkflowStepType.START)).add(new IsNotNull("autoLoadConditionName")));
    }

    public WfStepQuery workflowRunnable(boolean workflowRunnable) {
        return (WfStepQuery) equals("workflowRunnable", workflowRunnable);
    }

    public WfStepQuery workflowDevelopable(boolean workflowDevelopable) {
        return (WfStepQuery) equals("workflowDevelopable", workflowDevelopable);
    }
    
    public WfStepQuery withCasePrefix() {
        return (WfStepQuery) notnull("workflowCasePrefix");
    }
    
    public WfStepQuery withCaseApplet() {
        return (WfStepQuery) notnull("workflowCaseApplet");
    }

    public WfStepQuery ejectionCheckDue() {
        return (WfStepQuery) restriction(EJECTION_CHECK_DUE);
    }
    
    public WfStepQuery reminderCheckDue() {
        return (WfStepQuery) restriction(REMINDER_CHECK_DUE);
    }

    public WfStepQuery autoLoadingCheckDue() {
        return (WfStepQuery) restriction(AUTOLOADING_CHECK_DUE);
    }

    public WfStepQuery type(WorkflowStepType type) {
        return (WfStepQuery) equals("type", type);
    }

    public WfStepQuery typeIn(List<WorkflowStepType> type) {
        return (WfStepQuery) amongst("type", type);
    }

    public WfStepQuery applicationNameNot(String applicationName) {
        return (WfStepQuery) notequals("applicationName", applicationName);
    }

    public WfStepQuery applicationName(String applicationName) {
        return (WfStepQuery) equals("applicationName", applicationName);
    }

    public WfStepQuery workflowName(String workflowName) {
        return (WfStepQuery) equals("workflowName", workflowName);
    }

    public WfStepQuery appletNameBeginsWith(String appletName) {
        return (WfStepQuery) begins("appletName", appletName);
    }

    public WfStepQuery workflowLoadingTable(String workflowLoadingTable) {
        return (WfStepQuery) equals("workflowLoadingTable", workflowLoadingTable);
    }

    public WfStepQuery isWithLoadingTable() {
        return (WfStepQuery) notnull("workflowLoadingTable");
    }
}

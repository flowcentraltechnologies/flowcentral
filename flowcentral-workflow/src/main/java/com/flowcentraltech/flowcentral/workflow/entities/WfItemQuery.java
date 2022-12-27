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

package com.flowcentraltech.flowcentral.workflow.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseTenantEntityQuery;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.IsNull;
import com.tcdng.unify.core.criterion.NotEquals;
import com.tcdng.unify.core.criterion.Or;

/**
 * Workflow item query.
 * 
 * @author FlowCentral Technologies Limited
 * @version 1.0
 */
public class WfItemQuery extends BaseTenantEntityQuery<WfItem> {

    public WfItemQuery() {
        super(WfItem.class);
    }

    public WfItemQuery isCritical(Date now) {
        return (WfItemQuery) addIsNotNull("criticalDt").addLessThan("criticalDt", now);
    }

    public WfItemQuery criticalAlertNotSent() {
        return (WfItemQuery) addRestriction(
                new Or().add(new IsNull("criticalAlertSent")).add(new Equals("criticalAlertSent", false)));
    }

    public WfItemQuery isExpired(Date now) {
        return (WfItemQuery) addIsNotNull("expectedDt").addLessThan("expectedDt", now);
    }

    public WfItemQuery expiredAlertNotSent() {
        return (WfItemQuery) addRestriction(
                new Or().add(new IsNull("expirationAlertSent")).add(new Equals("expirationAlertSent", false)));
    }

    public WfItemQuery workRecId(Long workRecId) {
        return (WfItemQuery) addEquals("workRecId", workRecId);
    }

    public WfItemQuery entity(String entity) {
        return (WfItemQuery) addEquals("entity", entity);
    }

    public WfItemQuery isInWorkflow(String entity, Long entityInstId) {
        return (WfItemQuery) addEquals("entity", entity).addEquals("originWorkRecId", entityInstId);
    }

    public WfItemQuery wfStepName(String wfStepName) {
        return (WfItemQuery) addEquals("wfStepName", wfStepName);
    }

    public WfItemQuery branchCode(String branchCode) {
        return (WfItemQuery) addEquals("branchCode", branchCode);
    }

    public WfItemQuery departmentCode(String departmentCode) {
        return (WfItemQuery) addEquals("departmentCode", departmentCode);
    }

    public WfItemQuery applicationName(String applicationName) {
        return (WfItemQuery) addEquals("applicationName", applicationName);
    }

    public WfItemQuery workflowName(String workflowName) {
        return (WfItemQuery) addEquals("workflowName", workflowName);
    }

    public WfItemQuery wfItemDescLike(String wfItemDesc) {
        return (WfItemQuery) addLike("wfItemDesc", wfItemDesc);
    }

    public WfItemQuery heldBy(String heldBy) {
        return (WfItemQuery) addEquals("heldBy", heldBy);
    }

    public WfItemQuery isHeld() {
        return (WfItemQuery) addIsNotNull("heldBy");
    }

    public WfItemQuery isUnheld() {
        return (WfItemQuery) addIsNull("heldBy");
    }

    public WfItemQuery stepDt(Date stepDt) {
        return (WfItemQuery) addEquals("stepDt", stepDt);
    }

    public WfItemQuery isUnheldOrHeldBy(String heldBy) {
        return (WfItemQuery) addRestriction(new Or().add(new IsNull("heldBy")).add(new Equals("heldBy", heldBy)));
    }

    public WfItemQuery notForwardedBy(String userLoginId) {
        return (WfItemQuery) addRestriction(
                new Or().add(new NotEquals("forwardedBy", userLoginId)).add(new IsNull("forwardedBy")));
    }
}

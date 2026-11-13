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

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntityQuery;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.IsNotNull;
import com.tcdng.unify.core.criterion.IsNull;
import com.tcdng.unify.core.criterion.Less;
import com.tcdng.unify.core.criterion.NotEquals;
import com.tcdng.unify.core.criterion.Or;

/**
 * Workflow item query.
 * 
 * @author FlowCentral Technologies Limited
 * @version 1.0
 */
public class WfItemQuery extends BaseAuditEntityQuery<WfItem> {

    public WfItemQuery() {
        super(WfItem.class);
    }

    public WfItemQuery wfItemEventId(Long wfItemEventId) {
        return (WfItemQuery) equals("wfItemEventId", wfItemEventId);
    }

    public WfItemQuery workRecId(Long workRecId) {
        return (WfItemQuery) equals("workRecId", workRecId);
    }

    public WfItemQuery entity(String entity) {
        return (WfItemQuery) equals("entity", entity);
    }

    public WfItemQuery isInWorkflow(String entity, Long entityInstId) {
        return (WfItemQuery) equals("entity", entity).equals("originWorkRecId", entityInstId);
    }

    public WfItemQuery wfStepName(String wfStepName) {
        return (WfItemQuery) equals("wfStepName", wfStepName);
    }

    public WfItemQuery branchCode(String branchCode) {
        return (WfItemQuery) equals("branchCode", branchCode);
    }

    public WfItemQuery departmentCode(String departmentCode) {
        return (WfItemQuery) equals("departmentCode", departmentCode);
    }

    public WfItemQuery applicationName(String applicationName) {
        return (WfItemQuery) equals("applicationName", applicationName);
    }

    public WfItemQuery workflowName(String workflowName) {
        return (WfItemQuery) equals("workflowName", workflowName);
    }

    public WfItemQuery wfItemDescLike(String wfItemDesc) {
        return (WfItemQuery) like("wfItemDesc", wfItemDesc);
    }

    public WfItemQuery heldBy(String heldBy) {
        return (WfItemQuery) equals("heldBy", heldBy);
    }

    public WfItemQuery isHeld() {
        return (WfItemQuery) notnull("heldBy");
    }

    public WfItemQuery isUnheld() {
        return (WfItemQuery) isnull("heldBy");
    }

    public WfItemQuery stepDt(Date stepDt) {
        return (WfItemQuery) equals("stepDt", stepDt);
    }

    public WfItemQuery ejectionDue(Date now) {
        return (WfItemQuery) restriction(
                new And().add(new IsNotNull("ejectionDt")).add(new Less("ejectionDt", now)));
    }

    public WfItemQuery isUnheldOrHeldBy(String heldBy) {
        return (WfItemQuery) restriction(new Or().add(new IsNull("heldBy")).add(new Equals("heldBy", heldBy)));
    }

    public WfItemQuery notForwardedBy(String userLoginId) {
        return (WfItemQuery) restriction(
                new Or().add(new NotEquals("forwardedBy", userLoginId)).add(new IsNull("forwardedBy")));
    }
}

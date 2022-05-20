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

import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntityQuery;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepType;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.IsNotNull;
import com.tcdng.unify.core.criterion.NotEquals;

/**
 * Workflow step query.
 * 
 * @author FlowCentral Technologies Limited
 * @version 1.0
 */
public class WfStepQuery extends BaseConfigNamedEntityQuery<WfStep> {

    public WfStepQuery() {
        super(WfStep.class);
    }

    public WfStepQuery workflowId(Long workflowId) {
        return (WfStepQuery) addEquals("workflowId", workflowId);
    }

    public WfStepQuery routableTo() {
        return (WfStepQuery) addRestriction(new And().add(new NotEquals("type", WorkflowStepType.START))
                .add(new NotEquals("type", WorkflowStepType.ERROR)));
    }

    public WfStepQuery receivableFrom() {
        return (WfStepQuery) addRestriction(new And().add(new NotEquals("type", WorkflowStepType.END))
                .add(new NotEquals("type", WorkflowStepType.ERROR)));
    }

    public WfStepQuery supportsAutoload() {
        return (WfStepQuery) addRestriction(
                new And().add(new Equals("type", WorkflowStepType.START)).add(new IsNotNull("autoLoadConditionName")));
    }
}

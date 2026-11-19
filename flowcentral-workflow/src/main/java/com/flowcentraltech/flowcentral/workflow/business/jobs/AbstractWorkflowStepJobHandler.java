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
package com.flowcentraltech.flowcentral.workflow.business.jobs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.workflow.entities.WfStep;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepQuery;
import com.tcdng.unify.core.UnifyException;

/**
 * Base class for workflow step job handlers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractWorkflowStepJobHandler extends AbstractWorkflowJobHandler {

    @Override
    protected final List<String> getPayloadIds(List<Long> workItemIds) throws UnifyException {
        if (!workItemIds.isEmpty()) {
            List<WfStep> steps = workflow().findWorkflowSteps((WfStepQuery) new WfStepQuery().idIn(workItemIds)
                    .select("workflowName", "applicationName", "name"));
            if (!steps.isEmpty()) {
                List<String> payloadIds = new ArrayList<String>();
                for (WfStep wfStep : steps) {
                    payloadIds.add(ApplicationNameUtils.getApplicationEntityLongName(wfStep.getApplicationName(),
                            wfStep.getWorkflowName(), wfStep.getName()));
                }

                return payloadIds;
            }
        }

        return Collections.emptyList();
    }

}

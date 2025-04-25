/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.studio.web.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.web.lists.AbstractApplicationListCommand;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.entities.WfStep;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepQuery;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.list.LongParam;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Studio workflow step receive from list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("studiowfstepreceivefromlist")
public class StudioWfStepReceiveFromListCommand extends AbstractApplicationListCommand<LongParam> {

    @Configurable
    private WorkflowModuleService workflowModuleService;

    public StudioWfStepReceiveFromListCommand() {
        super(LongParam.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, LongParam longParam) throws UnifyException {
        if (longParam.isPresent()) {
            WfStep wfStep = workflowModuleService.findLeanWorkflowStepById(longParam.getValue());
            List<WfStep> stepList = workflowModuleService
                    .findWorkflowSteps((WfStepQuery) new WfStepQuery().workflowId(wfStep.getWorkflowId())
                            .receivableFrom().idNot(longParam.getValue()).addSelect("name", "description"));
            if (!DataUtils.isBlank(stepList)) {
                List<ListData> list = new ArrayList<ListData>();
                for (WfStep rwfStep : stepList) {
                    list.add(new ListData(rwfStep.getName(), rwfStep.getDescription()));
                }

                return list;
            }
        }

        return Collections.emptyList();
    }

}

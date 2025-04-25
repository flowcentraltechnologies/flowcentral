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

import com.flowcentraltech.flowcentral.application.constants.ApplicationFilterConstants;
import com.flowcentraltech.flowcentral.application.web.lists.AbstractApplicationListCommand;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.entities.WorkflowFilterQuery;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.list.LongParam;

/**
 * Studio workflow filter list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("studioworkflowfilterlist")
public class StudioWorkflowFilterListCommand extends AbstractApplicationListCommand<LongParam> {

    @Configurable
    private WorkflowModuleService workflowModuleService;

    public StudioWorkflowFilterListCommand() {
        super(LongParam.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, LongParam longParam) throws UnifyException {
        if (longParam.isPresent()) {
            List<ListData> list = new ArrayList<ListData>();
            list.add(new ListData(ApplicationFilterConstants.RESERVED_ALWAYS_FILTERNAME,
                    resolveSessionMessage("$m{studio.workflow.filter.always}")));
            for (Listable filter : workflowModuleService.findWorkflowFilters(
                    (WorkflowFilterQuery) new WorkflowFilterQuery().workflowId(longParam.getValue())
                            .addSelect("name", "description").addOrder("description"))) {
                list.add(new ListData(filter.getListKey(), filter.getListDescription()));
            }

            return list;
        }

        return Collections.emptyList();
    }

}

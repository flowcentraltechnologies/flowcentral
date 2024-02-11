/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.organization.web.lists;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepType;
import com.flowcentraltech.flowcentral.organization.entities.MappedWfStepQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.web.data.AssignParams;

/**
 * Mapped workflow step not in list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("mappedwfstepnotinlist")
public class MappedWfStepNotInListCommand extends AbstractOrganizationListCommand<AssignParams> {

    public MappedWfStepNotInListCommand() {
        super(AssignParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, AssignParams params) throws UnifyException {
        if (QueryUtils.isValidStringCriteria(params.getFilterId1())) {
            MappedWfStepQuery query = new MappedWfStepQuery();
            query.workflowId(params.getFilterId1(Long.class));
            query.typeIn(Arrays.asList(WorkflowStepType.NOTIFICATION, WorkflowStepType.USER_ACTION));
            
            if (params.isAssignedIdList()) {
                query.idNotIn(params.getAssignedIdList(Long.class));
            }

            query.addSelect("id", "description").addOrder("description").ignoreEmptyCriteria(true);
            return environment().findAll(query);
        }

        return Collections.emptyList();
    }

}

/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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

import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.organization.entities.MappedWorkflowQuery;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.list.ZeroParams;

/**
 * Mapped workflow list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("mappedworkflowlist")
public class MappedWorkflowListCommand extends AbstractOrganizationListCommand<ZeroParams> {

    public MappedWorkflowListCommand() {
        super(ZeroParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, ZeroParams params) throws UnifyException {
        return environment()
                .findAll(new MappedWorkflowQuery().runnable().addOrder("description").ignoreEmptyCriteria(true));
    }

}

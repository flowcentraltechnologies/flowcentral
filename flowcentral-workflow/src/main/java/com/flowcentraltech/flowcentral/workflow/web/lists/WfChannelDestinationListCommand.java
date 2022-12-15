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

package com.flowcentraltech.flowcentral.workflow.web.lists;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.configuration.constants.ChannelDirectionType;
import com.flowcentraltech.flowcentral.workflow.entities.WorkflowQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.Listable;

/**
 * Workflow channel destination list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("wfchanneldestinationlist")
public class WfChannelDestinationListCommand extends AbstractWorkflowListCommand<WfChannelDestinationParams> {

    public WfChannelDestinationListCommand() {
        super(WfChannelDestinationParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, WfChannelDestinationParams params) throws UnifyException {
        if (params.isPresent()) {
            if (ChannelDirectionType.INWARD.equals(params.getDirection())) {
                return au().getApplicationEntities(new WorkflowQuery().entity(params.getEntity()));
            }
        }

        return Collections.emptyList();
    }

}

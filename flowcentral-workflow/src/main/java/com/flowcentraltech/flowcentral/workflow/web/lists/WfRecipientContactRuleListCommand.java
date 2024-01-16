/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.Listable;

/**
 * Workflow recipient contact rule list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("wfrecipientcontactrulelist")
public class WfRecipientContactRuleListCommand extends AbstractWorkflowListCommand<WfRecipientPolicyParams> {

    @Configurable
    private ApplicationModuleService applicationModuleService;

    @Configurable
    private SystemModuleService systemModuleService;

    public WfRecipientContactRuleListCommand() {
        super(WfRecipientPolicyParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, WfRecipientPolicyParams params) throws UnifyException {
        if (params.isPresent()) {
            switch (params.getRecipientPolicy()) {
                case "entity-wfrecipientpolicy":
                    EntityDef entityDef = applicationModuleService.getEntityDef(params.getEntity());
                    return entityDef.getStringFieldDefList();
                case "sysparam-wfrecipientpolicy":
                    return systemModuleService.getContactSystemParameters();
                default:
                    break;
            }
        }

        return Collections.emptyList();
    }

}

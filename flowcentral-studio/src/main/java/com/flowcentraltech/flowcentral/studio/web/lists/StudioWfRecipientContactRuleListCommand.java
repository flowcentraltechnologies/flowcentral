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

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.web.lists.AbstractApplicationListCommand;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Workflow recipient contact rule list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("studiowfrecipientcontactrulelist")
public class StudioWfRecipientContactRuleListCommand extends AbstractApplicationListCommand<WfRecipientPolicyParams> {

    @Configurable
    private SystemModuleService systemModuleService;

    public StudioWfRecipientContactRuleListCommand() {
        super(WfRecipientPolicyParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, WfRecipientPolicyParams params) throws UnifyException {
        if (params.isPresent()) {
            switch (params.getRecipientPolicy()) {
                case "entity-wfrecipientpolicy":
                    String entity = application().getAppAppletEntity(params.getPropertyOwnerId());
                    if (!StringUtils.isBlank(entity)) {
                        EntityDef entityDef = application().getEntityDef(entity);
                        return entityDef.getStringFieldDefList();
                    }
                    break;
                case "sysparam-wfrecipientpolicy":
                    return systemModuleService.getContactSystemParameters();
                default:
                    break;
            }
        }

        return Collections.emptyList();
    }

}

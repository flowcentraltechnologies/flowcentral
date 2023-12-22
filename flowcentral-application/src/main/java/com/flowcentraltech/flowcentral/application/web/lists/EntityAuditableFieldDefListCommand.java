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
package com.flowcentraltech.flowcentral.application.web.lists;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.common.web.lists.AbstractFlowCentralListCommand;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.list.StringParam;

/**
 * Entity auditable field definition list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("entityauditablefielddeflist")
public class EntityAuditableFieldDefListCommand extends AbstractFlowCentralListCommand<StringParam> {

    @Configurable
    private ApplicationModuleService applicationModuleService;
    
    public EntityAuditableFieldDefListCommand() {
        super(StringParam.class);
    }

    public final void setApplicationModuleService(ApplicationModuleService applicationModuleService) {
        this.applicationModuleService = applicationModuleService;
    }

    @Override
    public List<? extends Listable> execute(Locale locale, StringParam param) throws UnifyException {
        if (param.isPresent()) {
             return applicationModuleService.getEntityDef(param.getValue()).getAuditableFieldDefList();
        }

        return Collections.emptyList();
    }

}

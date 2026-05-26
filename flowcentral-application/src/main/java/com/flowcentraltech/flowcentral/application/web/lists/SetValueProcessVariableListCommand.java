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
package com.flowcentraltech.flowcentral.application.web.lists;

import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.common.web.lists.AbstractFlowCentralListCommand;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Set value process variable list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("setvalueprocessvariablelist")
public class SetValueProcessVariableListCommand extends AbstractFlowCentralListCommand<EntityDefListParams> {

    @Configurable
    private AppletUtilities au;

    public SetValueProcessVariableListCommand() {
        super(EntityDefListParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, EntityDefListParams params) throws UnifyException {
        return params.isPresent() ? au.getProcessVariables(params.getEntityDef().getLongName()) : au.getProcessVariables(null);
    }

}

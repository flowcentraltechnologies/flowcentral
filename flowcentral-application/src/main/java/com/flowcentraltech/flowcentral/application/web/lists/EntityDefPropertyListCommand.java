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
package com.flowcentraltech.flowcentral.application.web.lists;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.common.web.lists.AbstractFlowCentralListCommand;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.Listable;

/**
 * Entity definition property list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("entitydefpropertylist")
public class EntityDefPropertyListCommand extends AbstractFlowCentralListCommand<EntityDefPropertyListParams> {

    public EntityDefPropertyListCommand() {
        super(EntityDefPropertyListParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, EntityDefPropertyListParams params) throws UnifyException {
        if (params.isPresent()) {
            switch(params.getType()) {
                case CATEGORY:
                    return params.getEntityDef().getCategoryDefList();
                case FIELD:
                    return params.getEntityDef().getSortedFieldDefList();
                 case SERIES:
                     return params.getEntityDef().getSeriesDefList();
                default:
                    break;               
            }
        }

        return Collections.emptyList();
    }

}

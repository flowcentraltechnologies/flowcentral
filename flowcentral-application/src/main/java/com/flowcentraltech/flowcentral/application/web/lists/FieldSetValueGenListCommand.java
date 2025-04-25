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
package com.flowcentraltech.flowcentral.application.web.lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.business.FieldSetValueGenerator;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.common.web.lists.AbstractEntityTypeListCommand;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Field set value generator list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fieldsetvaluegenlist")
public class FieldSetValueGenListCommand
        extends AbstractEntityTypeListCommand<FieldSetValueGenerator, EntityDefListParams> {

    public FieldSetValueGenListCommand() {
        super(FieldSetValueGenerator.class, EntityDefListParams.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<? extends Listable> execute(Locale locale, EntityDefListParams params) throws UnifyException {
        List<Listable> list = (List<Listable>) super.execute(locale, params);
        if (params.isPresent()) {
            EntityDef entityDef = params.getEntityDef();
            if (entityDef.isWithExpressions()) {
                if (list.isEmpty()) {
                    return entityDef.getExpressionsListables();
                }

                list = new ArrayList<Listable>(list);
                list.addAll(entityDef.getExpressionsListables());
            }
        }

        return list;
    }

    @Override
    protected String getEntityName(EntityDefListParams param) throws UnifyException {
        if (param.isPresent()) {
            return param.getEntityDef().getLongName();
        }

        return null;
    }

}

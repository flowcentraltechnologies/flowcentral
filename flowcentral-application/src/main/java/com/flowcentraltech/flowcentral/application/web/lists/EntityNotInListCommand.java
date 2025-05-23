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

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.EntityAssignRuleNameParts;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.web.data.AssignParams;

/**
 * Entity not in list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("entitynotinlist")
public class EntityNotInListCommand extends AbstractApplicationListCommand<AssignParams> {

    public EntityNotInListCommand() {
        super(AssignParams.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<? extends Listable> execute(Locale locale, AssignParams params) throws UnifyException {
        if (params.isWithRule()) {
            EntityAssignRuleNameParts parts = ApplicationNameUtils.getEntityAssignRuleNameParts(params.getRule());
            EntityDef _rootEntityDef = application().getEntityDef(parts.getEntityLongName());
            RefDef _assignRefDef = au()
                    .getRefDef(_rootEntityDef.getFieldDef(parts.getAssignFieldName()).getRefLongName());

            EntityClassDef _assignEntityClassDef = application().getEntityClassDef(_assignRefDef.getEntity());
            Query<?> query = Query.of((Class<? extends Entity>) _assignEntityClassDef.getEntityClass());
            if (_assignRefDef.isWithFilter()) {
                RefDef _baseRefDef = au()
                        .getRefDef(_rootEntityDef.getFieldDef(parts.getBaseFieldName()).getRefLongName());
                EntityClassDef _baseEntityClassDef = application().getEntityClassDef(_baseRefDef.getEntity());
                Entity baseInst = environment().listLean((Class<? extends Entity>) _baseEntityClassDef.getEntityClass(),
                        params.getAssignBaseId());
                Restriction br = _assignRefDef.getFilter().getRestriction(_assignEntityClassDef.getEntityDef(),
                        new BeanValueStore(baseInst).getReader(), application().getNow());
                if (br != null) {
                    query.addRestriction(br);
                }
            }

            if (params.isAssignedIdList()) {
                query.addNotAmongst("id", params.getAssignedIdList(Long.class));
            }

            if (parts.isWithDescField()) {
                query.addSelect("id", parts.getDescField()).addOrder(parts.getDescField());
            }

            query.ignoreEmptyCriteria(true);

            return environment().listAll(query);
        }

        return Collections.emptyList();
    }

}

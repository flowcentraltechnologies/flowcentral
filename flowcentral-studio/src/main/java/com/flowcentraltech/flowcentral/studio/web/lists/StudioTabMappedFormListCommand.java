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
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.entities.AppFormQuery;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.web.lists.AbstractApplicationListCommand;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Studio tab mapped form list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("studiotabmappedformlist")
public class StudioTabMappedFormListCommand extends AbstractApplicationListCommand<EntityFieldParams> {

    public StudioTabMappedFormListCommand() {
        super(EntityFieldParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, EntityFieldParams params) throws UnifyException {
        if (params.isPresent()) {
            final EntityDef entityDef = application().getEntityDef(params.getEntity());
            final EntityFieldDef entityFieldDef = entityDef.getFieldDef(params.getField());
            return ApplicationNameUtils.getListableList(application().findAppForms((AppFormQuery) new AppFormQuery()
                    .entity(entityFieldDef.getMapping()).addSelect("applicationName", "name", "description")));
        }

        return Collections.emptyList();
    }

}

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

package com.flowcentraltech.flowcentral.studio.web.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.entities.AppAppletQuery;
import com.flowcentraltech.flowcentral.application.web.lists.AbstractApplicationListCommand;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.list.LongParam;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Studio applet all child applet list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("studioappletallchildappletlist")
public class StudioAppletAllChildAppletListCommand extends AbstractApplicationListCommand<LongParam> {

    public StudioAppletAllChildAppletListCommand() {
        super(LongParam.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, LongParam longParam) throws UnifyException {
        if (longParam.isPresent()) {
            String entity = application().getAppAppletEntity(longParam.getValue());
            if (!StringUtils.isBlank(entity)) {
                EntityDef entityDef = application().getEntityDef(entity);
                List<EntityFieldDef> childListFieldList = entityDef.getChildListFieldDefList();
                if (!childListFieldList.isEmpty()) {
                    List<String> entityNames = new ArrayList<String>();
                    for (EntityFieldDef entityFieldDef : childListFieldList) {
                        entityNames.add(application().getRefDef(entityFieldDef.getRefLongName()).getEntity());
                    }

                    return au().getApplicationEntityListables(new AppAppletQuery().entityIn(entityNames));
                }
            }
        }

        return Collections.emptyList();
    }

}

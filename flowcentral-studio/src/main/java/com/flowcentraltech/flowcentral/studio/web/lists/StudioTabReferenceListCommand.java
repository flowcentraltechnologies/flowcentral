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

package com.flowcentraltech.flowcentral.studio.web.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.web.lists.AbstractApplicationListCommand;
import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Studio tab reference list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("studiotabreferencelist")
public class StudioTabReferenceListCommand extends AbstractApplicationListCommand<ReferenceParams> {

    public StudioTabReferenceListCommand() {
        super(ReferenceParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, ReferenceParams params) throws UnifyException {
        if (params.isPresent()) {
            final TabContentType type = params.getType();
            if (TabContentType.CHILD.equals(type) || TabContentType.CHILD_LIST.equals(type)) {
                EntityDef entityDef = application().getEntityDef(params.getEntity());
                List<EntityFieldDef> list = new ArrayList<EntityFieldDef>();
                if (TabContentType.CHILD.equals(type)) {
                    for (EntityFieldDef entityFieldDef : entityDef.getFieldDefList()) {
                        if (entityFieldDef.isChild()) {
                            list.add(entityFieldDef);
                        }
                    }
                } else {
                    for (EntityFieldDef entityFieldDef : entityDef.getFieldDefList()) {
                        if (entityFieldDef.isChildList()) {
                            list.add(entityFieldDef);
                        }
                    }
                }

                return list;
            }
        }

        return Collections.emptyList();
    }

}

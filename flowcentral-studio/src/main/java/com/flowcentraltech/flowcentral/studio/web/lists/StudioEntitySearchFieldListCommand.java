/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.list.StringParam;

/**
 * Studio entity search field list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("studioentitysearchfieldlist")
public class StudioEntitySearchFieldListCommand extends AbstractApplicationListCommand<StringParam> {

    public StudioEntitySearchFieldListCommand() {
        super(StringParam.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, StringParam stringParams) throws UnifyException {
        if (stringParams.isPresent()) {
            EntityDef entityDef = application().getEntityDef(stringParams.getValue());
            return entityDef.getFilterStringFieldDefList();
        }

        return Collections.emptyList();
    }

}

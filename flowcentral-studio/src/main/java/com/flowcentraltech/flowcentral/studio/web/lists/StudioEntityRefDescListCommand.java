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

package com.flowcentraltech.flowcentral.studio.web.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.web.lists.AbstractApplicationListCommand;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.Listable;

/**
 * Studio entity reference description list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("studioentityrefdesclist")
public class StudioEntityRefDescListCommand extends AbstractApplicationListCommand<RefDescriptionParams> {

    public StudioEntityRefDescListCommand() {
        super(RefDescriptionParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, RefDescriptionParams params) throws UnifyException {
        if (params.isPresent()) {
            List<Listable> resultList = new ArrayList<Listable>();
            for (String fieldName : application().findForeignEntityStringFields(params.getEntity(),
                    params.getRefField())) {
                resultList.add(new ListData(fieldName, fieldName));
            }

            return resultList;
        }

        return Collections.emptyList();
    }

}

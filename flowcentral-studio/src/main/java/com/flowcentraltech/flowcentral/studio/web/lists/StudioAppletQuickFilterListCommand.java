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

import com.flowcentraltech.flowcentral.application.entities.AppAppletFilter;
import com.flowcentraltech.flowcentral.application.web.lists.AbstractApplicationListCommand;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.list.LongParam;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Studio applet quick filter list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("studioappletquickfilterlist")
public class StudioAppletQuickFilterListCommand extends AbstractApplicationListCommand<LongParam> {

    public StudioAppletQuickFilterListCommand() {
        super(LongParam.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, LongParam longParam) throws UnifyException {
        if (longParam.isPresent()) {
            List<AppAppletFilter> filterList = application().findAppAppletQuickFilters(longParam.getValue());
            if (!DataUtils.isBlank(filterList)) {
                List<ListData> list = new ArrayList<ListData>();
                for (AppAppletFilter filter : filterList) {
                    list.add(new ListData(filter.getName(), filter.getDescription()));
                }
                return list;
            }
        }

        return Collections.emptyList();
    }

}

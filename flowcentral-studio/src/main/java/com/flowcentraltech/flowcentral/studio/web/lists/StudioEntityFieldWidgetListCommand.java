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

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.entities.AppEntityField;
import com.flowcentraltech.flowcentral.application.entities.AppEntityFieldQuery;
import com.flowcentraltech.flowcentral.application.entities.AppWidgetTypeQuery;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationQueryUtils;
import com.flowcentraltech.flowcentral.application.web.lists.AbstractApplicationListCommand;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Studio entity field list command
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("studioentityfieldwidgetlist")
public class StudioEntityFieldWidgetListCommand extends AbstractApplicationListCommand<StudioEntityFieldFormParams> {

    public StudioEntityFieldWidgetListCommand() {
        super(StudioEntityFieldFormParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, StudioEntityFieldFormParams params) throws UnifyException {
        if (params.isPresent()) {
            EntityFieldDataType type = DataUtils.convert(EntityFieldDataType.class, params.getDataType());
            if (EntityFieldDataType.LIST_ONLY.equals(type)) {
                ApplicationEntityNameParts np = ApplicationNameUtils.getApplicationEntityNameParts(params.getEntity());
                AppEntityField appEntityField = application().findAppEntityField(
                        (AppEntityFieldQuery) new AppEntityFieldQuery().applicationName(np.getApplicationName())
                                .appEntityName(np.getEntityName()).name(params.getName()));
                type = application().resolveListOnlyEntityDataType(appEntityField);
            }

            AppWidgetTypeQuery query = new AppWidgetTypeQuery();
            ApplicationQueryUtils.addWidgetTypeCriteria(query, type);
            return au().getApplicationEntityListables(query);
        }

        return Collections.emptyList();
    }

}

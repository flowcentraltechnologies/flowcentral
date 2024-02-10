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

import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.entities.AppWidgetType;
import com.flowcentraltech.flowcentral.application.util.ApplicationQueryUtils;
import com.flowcentraltech.flowcentral.common.business.SearchInputRestrictionGenerator;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.database.Query;

/**
 * Search input field widget definition list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("searchinputfieldwidgetlist")
public class SearchInputFieldWidgetListCommand extends AbstractApplicationListCommand<EntityDefFieldListParams> {

    public SearchInputFieldWidgetListCommand() {
        super(EntityDefFieldListParams.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<? extends Listable> execute(Locale locale, EntityDefFieldListParams params) throws UnifyException {
        if (params.isPresent()) {
            EntityFieldDataType type = null;
            if (params.getFieldName().startsWith("f:")) {
                EntityFieldDef entityFieldDef = params.getEntityDef().getFieldDef(params.getFieldName().substring(2));
                if (entityFieldDef.isWithResolvedTypeFieldDef()) {
                    entityFieldDef = entityFieldDef.getResolvedTypeFieldDef();
                }

                type = entityFieldDef.getDataType();
            } else {
                SearchInputRestrictionGenerator generator = getComponent(SearchInputRestrictionGenerator.class,
                        params.getFieldName().substring(2));
                type = generator.getInputType().dataType();
            }

            Query<AppWidgetType> query = (Query<AppWidgetType>) au().application().queryOf("application.appWidgetType");
            ApplicationQueryUtils.addWidgetTypeCriteria(query, type);
            return au().getApplicationEntitiesListables(query);
        }

        return Collections.emptyList();
    }

}

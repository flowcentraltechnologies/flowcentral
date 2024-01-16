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

package com.flowcentraltech.flowcentral.application.business;

import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.flowcentraltech.flowcentral.common.business.QueryEncoder;
import com.flowcentraltech.flowcentral.common.business.SpecialParamProvider;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.CompoundRestriction;
import com.tcdng.unify.core.criterion.Order;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Application query encoder.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("application-queryencoder")
public class ApplicationQueryEncoder extends AbstractFlowCentralComponent implements QueryEncoder {

    @Configurable
    private ApplicationModuleService applicationModuleService;

    @Configurable
    private SpecialParamProvider specialParamProvider;

    @Configurable
    private AppletUtilities appletUtilities;

    @Override
    public String encodeQueryFilter(Restriction restriction) throws UnifyException {
        return restriction != null && !restriction.isEmpty()
                ? InputWidgetUtils.getFilterDefinition(appletUtilities, restriction)
                : null;
    }

    @Override
    public String encodeQueryFilter(Query<? extends Entity> query) throws UnifyException {
        return encodeQueryFilter(query.getRestrictions());
    }

    @Override
    public String encodeQueryOrder(Query<? extends Entity> query) throws UnifyException {
        return InputWidgetUtils.getOrderDefinition(query.getOrder());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Query<? extends Entity> decodeQuery(String entity, String queryStr, String orderStr) throws UnifyException {
        EntityClassDef entityClassDef = applicationModuleService.getEntityClassDef(entity);
        Restriction restriction = null;
        if (!StringUtils.isBlank(queryStr)) {
            FilterDef filterDef = InputWidgetUtils.getFilterDef(appletUtilities, queryStr);
            restriction = InputWidgetUtils.getRestriction(entityClassDef.getEntityDef(), filterDef,
                    specialParamProvider, applicationModuleService.getNow());
        }

        Query<? extends Entity> query = restriction != null ? (restriction.isSimple()
                ? Query.of((Class<? extends Entity>) entityClassDef.getEntityClass(), new And().add(restriction))
                : Query.of((Class<? extends Entity>) entityClassDef.getEntityClass(),
                        (CompoundRestriction) restriction))
                : Query.of((Class<? extends Entity>) entityClassDef.getEntityClass());
        Order order = InputWidgetUtils.getOrder(orderStr);
        query.setOrder(order);
        query.ignoreEmptyCriteria(true);
        
        return query;
    }

    @Override
    public String encodeUpdate(Update update) throws UnifyException {
        return InputWidgetUtils.getUpdateDefinition(update);
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

}

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

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.common.business.AbstractEnvironmentDelegateUtilities;
import com.flowcentraltech.flowcentral.common.business.QueryEncoder;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.PrintFormat;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Environment delegate utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(ApplicationModuleNameConstants.ENVIRONMENT_DELEGATE_UTILITIES)
public class EnvironmentDelegateUtilitiesImpl extends AbstractEnvironmentDelegateUtilities {

    @Configurable
    private QueryEncoder queryEncoder;

    public final void setQueryEncoder(QueryEncoder queryEncoder) {
        this.queryEncoder = queryEncoder;
    }

    @Override
    public Long encodeDelegateObjectId(Object id) throws UnifyException {
        return (Long) id;
    }

    @Override
    public Long encodeDelegateVersionNo(Object versionNo) throws UnifyException {
        return (Long) versionNo;
    }

    @Override
    public String encodeDelegateQuery(Query<? extends Entity> query) throws UnifyException {
        return queryEncoder.encodeQueryFilter(query);
    }

    @Override
    public String encodeDelegateOrder(Query<? extends Entity> query) throws UnifyException {
        return queryEncoder.encodeQueryOrder(query);
    }

    @Override
    public String encodeDelegateUpdate(Update update) throws UnifyException {
        return queryEncoder.encodeUpdate(update);
    }

    @Override
    public String[] encodeDelegateEntity(Entity inst) throws UnifyException {
        String json = DataUtils.asJsonString(inst, PrintFormat.NONE);
        return new String[] { json };
    }

    @Override
    public Query<? extends Entity> decodeDelegateQuery(String entity, String query, String order)
            throws UnifyException {
        return queryEncoder.decodeQuery(entity, query, order);
    }

}

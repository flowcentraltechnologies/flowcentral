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
package com.flowcentraltech.flowcentral.audit.web.widgets;

import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.policies.LoadingParams;
import com.flowcentraltech.flowcentral.audit.data.EntityAuditConfigDef;
import com.flowcentraltech.flowcentral.audit.entities.EntityAuditKeysQuery;
import com.flowcentraltech.flowcentral.configuration.constants.AuditEventType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.criterion.IBeginsWith;
import com.tcdng.unify.core.criterion.Or;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Entity audit loading table provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("entityaudit-loadingprovider")
public class EntityAuditLoadingTableProvider extends AbstractAuditLoadingTableProvider {

    @Override
    public String getLoadingLabel() throws UnifyException {
        return null;
    }

    @Override
    public List<? extends Entity> getLoadingItems(LoadingParams params) throws UnifyException {
        final String auditConfigName = params.getParam(String.class, "auditConfigName");
        if (!StringUtils.isBlank(auditConfigName)) {
            EntityAuditKeysQuery query = new EntityAuditKeysQuery();
            query.auditConfigName(auditConfigName);

            final AuditEventType eventType = params.getParam(AuditEventType.class, "eventType");
            if (eventType != null) {
                query.eventType(eventType);
            }

            final String key = params.getParam(String.class, "key");
            if (!StringUtils.isBlank(key)) {
                EntityAuditConfigDef entityAuditConfigDef = audit().getEntityAuditConfigDef(auditConfigName);
                Or or = new Or();
                if (entityAuditConfigDef.isWithSearchFieldA()) {
                    or.add(new IBeginsWith("keyA", key));
                }

                if (entityAuditConfigDef.isWithSearchFieldB()) {
                    or.add(new IBeginsWith("keyB", key));
                }

                if (entityAuditConfigDef.isWithSearchFieldC()) {
                    or.add(new IBeginsWith("keyC", key));
                }

                if (entityAuditConfigDef.isWithSearchFieldD()) {
                    or.add(new IBeginsWith("keyD", key));
                }

                if (!or.isEmpty()) {
                    query.addRestriction(or);
                }
            }

            query.addOrder(OrderType.DESCENDING, "eventTimestamp");
            return environment().listAll(query);
        }

        return Collections.emptyList();
    }

}

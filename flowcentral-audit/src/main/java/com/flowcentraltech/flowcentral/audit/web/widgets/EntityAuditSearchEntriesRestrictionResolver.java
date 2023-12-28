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

import java.util.Date;
import java.util.Map;

import com.flowcentraltech.flowcentral.audit.data.EntityAuditConfigDef;
import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.configuration.constants.AuditEventCategoryType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.criterion.Amongst;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.Between;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.GreaterOrEqual;
import com.tcdng.unify.core.criterion.IBeginsWith;
import com.tcdng.unify.core.criterion.LessOrEqual;
import com.tcdng.unify.core.criterion.Or;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Entity audit search entries resolver.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@EntityReferences({ "audit.entityAuditKeys" })
@Component(name = "entityaudit-searchrestrictionresolver", description = "Entity Audit Search Restriction Resolver")
public class EntityAuditSearchEntriesRestrictionResolver extends AbstractAuditSearchEntriesRestrictionResolver {

    @Override
    public Restriction resolveRestriction(Map<String, Object> entries) throws UnifyException {
        And and = new And();
        final AuditEventCategoryType eventCatType = DataUtils.convert(AuditEventCategoryType.class,
                entries.get("eventType"));
        if (eventCatType != null) {
            and.add(new Amongst("eventType", eventCatType.eventTypes()));
        }

        final String auditConfigName = DataUtils.convert(String.class, entries.get("auditConfigName"));
        if (!StringUtils.isBlank(auditConfigName)) {
            and.add(new Equals("auditConfigName", auditConfigName));

            final String key = DataUtils.convert(String.class, entries.get("key"));
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
                    and.add(or);
                }
            }
        }

        final Date eventOnAfter = DataUtils.convert(Date.class, entries.get("eventOnAfter"));
        final Date eventOnBefore = DataUtils.convert(Date.class, entries.get("eventOnBefore"));
        if (eventOnAfter != null && eventOnBefore != null) {
            and.add(new Between("eventTimestamp", CalendarUtils.getMidnightDate(eventOnAfter),
                    CalendarUtils.getLastSecondDate(eventOnBefore)));
        } else {
            if (eventOnAfter != null) {
                and.add(new GreaterOrEqual("eventTimestamp", CalendarUtils.getMidnightDate(eventOnAfter)));
            }

            if (eventOnBefore != null) {
                and.add(new LessOrEqual("eventTimestamp", CalendarUtils.getLastSecondDate(eventOnBefore)));
            }
        }

        return !and.isEmpty() ? and : null;
    }

}

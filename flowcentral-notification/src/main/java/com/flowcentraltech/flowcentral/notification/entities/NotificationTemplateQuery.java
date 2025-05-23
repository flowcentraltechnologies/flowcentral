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
package com.flowcentraltech.flowcentral.notification.entities;

import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntityQuery;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.IsNull;
import com.tcdng.unify.core.criterion.Or;

/**
 * Query class for notification template records.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class NotificationTemplateQuery extends BaseApplicationEntityQuery<NotificationTemplate> {

    public NotificationTemplateQuery() {
        super(NotificationTemplate.class);
    }

    public NotificationTemplateQuery entity(String entity) {
        return (NotificationTemplateQuery) addEquals("entity", entity);
    }

    public NotificationTemplateQuery entityIsOrIsNull(String entity) {
        return (NotificationTemplateQuery) addRestriction(
                new Or().add(new IsNull("entity")).add(new Equals("entity", entity)));
    }

    public NotificationTemplateQuery entityBeginsWith(String entity) {
        return (NotificationTemplateQuery) addBeginsWith("entity", entity);
    }
}

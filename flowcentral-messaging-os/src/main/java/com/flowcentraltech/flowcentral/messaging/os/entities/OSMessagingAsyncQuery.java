/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.messaging.os.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntityQuery;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.IsNull;
import com.tcdng.unify.core.criterion.Or;

/**
 * OS messaging async query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class OSMessagingAsyncQuery extends BaseAuditEntityQuery<OSMessagingAsync> {

    public OSMessagingAsyncQuery() {
        super(OSMessagingAsync.class);
    }

    public OSMessagingAsyncQuery endpoint(String endpoint) {
        return (OSMessagingAsyncQuery) addEquals("endpoint", endpoint);
    }

    public OSMessagingAsyncQuery isDue(Date now) {
        return (OSMessagingAsyncQuery) addLessThanEqual("nextAttemptOn", now);
    }

    public OSMessagingAsyncQuery isSent() {
        return (OSMessagingAsyncQuery) addIsNotNull("sentOn");
    }

    public OSMessagingAsyncQuery isNotSent() {
        return (OSMessagingAsyncQuery) addIsNull("sentOn");
    }

    public OSMessagingAsyncQuery isNotProcessing() {
        return (OSMessagingAsyncQuery) addRestriction(
                new Or().add(new IsNull("processing")).add(new Equals("processing", Boolean.FALSE)));
    }

    public OSMessagingAsyncQuery isProcessing() {
        return (OSMessagingAsyncQuery) addEquals("processing", Boolean.TRUE);
    }

}

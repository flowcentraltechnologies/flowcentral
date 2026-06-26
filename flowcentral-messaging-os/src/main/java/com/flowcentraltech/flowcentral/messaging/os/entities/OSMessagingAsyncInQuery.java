/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntityQuery;

/**
 * OS messaging async in query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class OSMessagingAsyncInQuery extends BaseAuditEntityQuery<OSMessagingAsyncIn> {

    public OSMessagingAsyncInQuery() {
        super(OSMessagingAsyncIn.class);
    }

    public OSMessagingAsyncInQuery correlationId(String correlationId) {
        return (OSMessagingAsyncInQuery) addEquals("correlationId", correlationId);
    }

    public OSMessagingAsyncInQuery processor(String processor) {
        return (OSMessagingAsyncInQuery) addEquals("processor", processor);
    }

    public OSMessagingAsyncInQuery isProcessed() {
        return (OSMessagingAsyncInQuery) addIsNotNull("processedOn");
    }

    public OSMessagingAsyncInQuery isNotProcessed() {
        return (OSMessagingAsyncInQuery) addIsNull("processedOn");
    }

}

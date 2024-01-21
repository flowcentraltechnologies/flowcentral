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
package com.flowcentraltech.flowcentral.messaging.web.lists;

import com.flowcentraltech.flowcentral.common.web.lists.AbstractFlowCentralListCommand;
import com.flowcentraltech.flowcentral.messaging.business.MessagingProvider;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.list.ListParam;

/**
 * Abstract base class for messaging provider list commands.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractMessagingProviderListCommand<T extends ListParam> extends AbstractFlowCentralListCommand<T> {

    protected static final int MAX_RECORDS = 240;
    
    @Configurable
    private MessagingProvider messagingProvider;

    public AbstractMessagingProviderListCommand(Class<T> paramType) {
        super(paramType);
    }

    public MessagingProvider getMessagingProvider() {
        return messagingProvider;
    }

}

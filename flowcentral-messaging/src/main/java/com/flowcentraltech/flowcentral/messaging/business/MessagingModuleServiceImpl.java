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
package com.flowcentraltech.flowcentral.messaging.business;

import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.messaging.constants.MessagingModuleErrorConstants;
import com.flowcentraltech.flowcentral.messaging.constants.MessagingModuleNameConstants;
import com.flowcentraltech.flowcentral.messaging.data.BaseMessage;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Transactional;

/**
 * Implementation of messaging module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(MessagingModuleNameConstants.MESSAGING_MODULE_SERVICE)
public class MessagingModuleServiceImpl extends AbstractFlowCentralService implements MessagingModuleService {

    @Configurable
    private MessagingProvider messagingProvider;

    @Override
    public <T extends BaseMessage> void sendMessage(T message) throws UnifyException {
        String json = asJson(message);
        provider().sendMessage(message.getConfig(), message.getTarget(), json);
    }

    @Override
    public <T extends BaseMessage> T receiveMessage(Class<T> messageType, String config, String target)
            throws UnifyException {
        String json = provider().receiveMessage(config, target);
        return fromJson(messageType, json);
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {

    }

    private MessagingProvider provider() throws UnifyException {
        if (messagingProvider == null) {
            throw new UnifyException(MessagingModuleErrorConstants.NO_MESSAGING_PROVIDER_FOUND);
        }

        return messagingProvider;
    }
}

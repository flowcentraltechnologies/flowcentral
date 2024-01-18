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
import com.flowcentraltech.flowcentral.messaging.data.Message;
import com.flowcentraltech.flowcentral.messaging.data.MessageHeader;
import com.flowcentraltech.flowcentral.messaging.utils.MessagingUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.util.StringUtils;

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
    public void sendMessage(Message message) throws UnifyException {
        MessageHeader header = message.getHeader();
        String text = MessagingUtils.marshal(message);
        provider().sendMessage(header.getConfig(), header.getDestination(), text);
    }

    @Override
    public Message receiveMessage(String config, String source) throws UnifyException {
        String text = provider().receiveMessage(config, source);
        if (!StringUtils.isBlank(text)) {
            return MessagingUtils.unmarshal(config, source, text);
        }

        return null;
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

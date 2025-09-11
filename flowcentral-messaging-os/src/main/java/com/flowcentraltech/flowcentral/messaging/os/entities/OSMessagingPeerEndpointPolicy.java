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
package com.flowcentraltech.flowcentral.messaging.os.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntityPolicy;
import com.flowcentraltech.flowcentral.messaging.os.business.OSMessagingPeerEndpointObserver;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * OS messaging peer endpoint entity policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("osmessagingpeerendpointpolicy")
public class OSMessagingPeerEndpointPolicy extends BaseStatusEntityPolicy {

    @Configurable
    private OSMessagingPeerEndpointObserver observer;

    @Override
    public void postCreate(Entity record, Date now) throws UnifyException {
        super.postCreate(record, now);
        observe((OSMessagingPeerEndpoint) record);
    }

    @Override
    public void postUpdate(Entity record, Date now) throws UnifyException {
        super.postUpdate(record, now);
        observe((OSMessagingPeerEndpoint) record);
    }

    private void observe(OSMessagingPeerEndpoint peerEndpoint) throws UnifyException {
        if (observer != null) {
            observer.observerUpdate(peerEndpoint.getAppId(), peerEndpoint.getEndpointUrl(), peerEndpoint.getPeerPassword());
        }
    }
}

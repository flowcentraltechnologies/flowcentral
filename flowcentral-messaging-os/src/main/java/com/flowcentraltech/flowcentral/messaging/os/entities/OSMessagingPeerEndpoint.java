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

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Column;

/**
 * OS messaging peer end-point.
 *
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_OSMESSAGINGPEERENDPOINT", uniqueConstraints = {
        @UniqueConstraint({ "appId" }),
        @UniqueConstraint({ "name" }),
        @UniqueConstraint({ "description" }) })
public class OSMessagingPeerEndpoint extends BaseStatusEntity {

    @Column(name = "ENDPOINT_APP_ID", length = 32)
    private String appId;

    @Column(name = "ENDPOINT_NM", length = 64)
    private String name;

    @Column(name = "ENDPOINT_DESC", length = 256)
    private String description;

    @Column(name = "ENDPOINT_URL", length = 256)
    private String endpointUrl;

    @Column(name = "PEER_AUTH", length = 256, transformer = "twoway-stringcryptograph")
    private String peerPassword;

    @Override
    public String getDescription() {
        return this.description;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public String getPeerPassword() {
        return peerPassword;
    }

    public void setPeerPassword(String peerPassword) {
        this.peerPassword = peerPassword;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

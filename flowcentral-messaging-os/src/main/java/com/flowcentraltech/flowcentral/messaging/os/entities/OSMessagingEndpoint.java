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
import com.tcdng.unify.core.annotation.Policy;

/**
 * OS messaging end-point.
 *
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Policy("osmessagingendpoint-policy")
@Table(name = "FC_OSMESSAGINGENDPOINT",
        uniqueConstraints = {
                @UniqueConstraint({ "name" }),
                @UniqueConstraint({ "description" }) })
public class OSMessagingEndpoint extends BaseStatusEntity {

    @Column(name = "ENDPOINT_NM", length = 64)
    private String name;

    @Column(name = "ENDPOINT_DESC", length = 128)
    private String description;

    @Column(name = "NODE_URL", length = 256)
    private String nodeUrl;

    @Column(name = "TARGET", length = 64)
    private String target;

    @Column(name = "PROCESSOR", length = 64)
    private String processor;

    @Column(name = "AUTH_USERNAME", length = 64)
    private String userName;

    @Column(name = "AUTH_PASSWORD", length = 64)
    private String password;

    @Column(name = "AUTH_BASE64", length = 512, nullable = true)
    private String authorization;

    @Override
    public String getDescription() {
        return this.description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNodeUrl() {
        return nodeUrl;
    }

    public void setNodeUrl(String nodeUrl) {
        this.nodeUrl = nodeUrl;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthorization() {
        return authorization;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

}

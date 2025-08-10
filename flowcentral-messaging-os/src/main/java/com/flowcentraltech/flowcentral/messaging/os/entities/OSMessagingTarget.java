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
 * OS messaging target.
 *
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_OSMESSAGINGTARGET", uniqueConstraints = {
        @UniqueConstraint({ "name" }),
        @UniqueConstraint({ "description" }) })
public class OSMessagingTarget extends BaseStatusEntity {

    @Column(name = "TARGET_NM", length = 64)
    private String name;

    @Column(name = "TARGET_DESC", length = 128)
    private String description;

    @Column(name = "TARGET_URL", length = 256)
    private String targetUrl;

    @Column(name = "AUTH_PASSWORD", length = 96, transformer = "twoway-stringcryptograph")
    private String password;

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

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

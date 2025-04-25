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
package com.flowcentraltech.flowcentral.messaging.data;

import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.common.data.VersionedEntityDef;

/**
 * Messaging configuration definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class MessagingConfigDef implements VersionedEntityDef {

    private MessagingExecContext ctx;

    private Long id;

    private long version;

    private String name;

    private String description;

    private String endpointConfig;

    private String target;

    private String component;

    private int maxConcurrency;

    private RecordStatus status;

    public MessagingConfigDef(Long id, long version, String name, String description, String endpointConfig,
            String target, String component, int maxConcurrency, RecordStatus status) {
        this.ctx = new MessagingExecContext(endpointConfig, target, component, maxConcurrency);
        this.id = id;
        this.version = version;
        this.name = name;
        this.description = description;
        this.endpointConfig = endpointConfig;
        this.target = target;
        this.component = component;
        this.maxConcurrency = maxConcurrency;
        this.status = status;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public long getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEndpointConfig() {
        return endpointConfig;
    }

    public String getComponent() {
        return component;
    }

    public String getTarget() {
        return target;
    }

    public int getMaxConcurrency() {
        return maxConcurrency;
    }

    public RecordStatus getStatus() {
        return status;
    }

    public MessagingExecContext getCtx() {
        return ctx;
    }

}

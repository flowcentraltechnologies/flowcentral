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
package com.flowcentraltech.flowcentral.messaging.data;

import com.flowcentraltech.flowcentral.common.constants.RecordStatus;

/**
 * Messaging read configuration definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class MessagingReadConfigDef {

    private Long id;

    private long version;

    private String name;

    private String description;

    private String endpointConfig;

    private String consumer;

    private RecordStatus status;

    public MessagingReadConfigDef(Long id, long version, String name, String description, String endpointConfig,
            String consumer, RecordStatus status) {
        this.id = id;
        this.version = version;
        this.name = name;
        this.description = description;
        this.endpointConfig = endpointConfig;
        this.consumer = consumer;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

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

    public String getConsumer() {
        return consumer;
    }

    public RecordStatus getStatus() {
        return status;
    }

}

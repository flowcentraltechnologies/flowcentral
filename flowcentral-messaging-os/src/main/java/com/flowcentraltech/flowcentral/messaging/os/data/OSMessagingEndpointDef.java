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
package com.flowcentraltech.flowcentral.messaging.os.data;

import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.common.data.VersionedEntityDef;

/**
 * OS messaging endpoint definition.
 *
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class OSMessagingEndpointDef implements VersionedEntityDef {

    private Long id;
    
    private String name;

    private String description;

    private String nodeUrl;

    private String target;

    private String password;

    private String authorization;
    
    private RecordStatus status;
    
    private long versionNo;

    public OSMessagingEndpointDef(Long id, String name, String description, String nodeUrl, String target, String password,
            String authorization, RecordStatus status, long versionNo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.nodeUrl = nodeUrl;
        this.target = target;
        this.password = password;
        this.authorization = authorization;
        this.status = status;
        this.versionNo = versionNo;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public long getVersion() {
        return versionNo;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getNodeUrl() {
        return nodeUrl;
    }

    public String getTarget() {
        return target;
    }

    public String getPassword() {
        return password;
    }

    public String getAuthorization() {
        return authorization;
    }

    public RecordStatus getStatus() {
        return status;
    }
        
}

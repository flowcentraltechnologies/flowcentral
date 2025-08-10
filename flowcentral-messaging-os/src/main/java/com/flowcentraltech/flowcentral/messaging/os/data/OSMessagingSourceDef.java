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
 * OS messaging source definition.
 *
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class OSMessagingSourceDef implements VersionedEntityDef {

    private Long id;
    
    private String name;

    private String description;

    private String password;
    
    private RecordStatus status;
    
    private long versionNo;

    public OSMessagingSourceDef(Long id, String name, String description, String password,
            RecordStatus status, long versionNo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public long getVersionNo() {
        return versionNo;
    }

    public RecordStatus getStatus() {
        return status;
    }
        
}

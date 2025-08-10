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

import java.util.HashMap;
import java.util.Map;

import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.common.data.VersionedEntityDef;
import com.flowcentraltech.flowcentral.messaging.os.constants.OSMessagingModuleNameConstants;
import com.tcdng.unify.core.util.EncodingUtils;

/**
 * OS messaging target definition.
 *
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class OSMessagingTargetDef implements VersionedEntityDef {

    private Long id;

    private String name;

    private String description;

    private String targetUrl;

    private String password;

    private RecordStatus status;

    private long versionNo;

    private String source;

    private Map<String, String> authentications;
    
    public OSMessagingTargetDef(Long id, String name, String description, String targetUrl, String password,
            RecordStatus status, long versionNo, String source) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.targetUrl = targetUrl + OSMessagingModuleNameConstants.OSMESSAGING_CONTROLLER;
        this.password = password;
        this.status = status;
        this.versionNo = versionNo;
        this.source = source;
        this.authentications = new HashMap<String, String>();
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

    public String getTargetUrl() {
        return targetUrl;
    }

    public String getPassword() {
        return password;
    }

    public RecordStatus getStatus() {
        return status;
    }
 
    public String getSource() {
        return source;
    }

    public String getAuthentication(String processor) {
        String authentication = authentications.get(processor);
        if (authentication == null) {
            synchronized (this) {
                authentication = authentications.get(processor);
                if (authentication == null) {
                    authentication = EncodingUtils.getBase64String(source + "." + processor + ":" + password);
                    authentications.put(processor, authentication);
                }
            }
        }

        return authentication;
    }
}

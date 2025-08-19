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
import com.flowcentraltech.flowcentral.messaging.os.util.OSMessagingUtils;

/**
 * OS messaging peer end-point definition.
 *
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class OSMessagingPeerEndpointDef implements VersionedEntityDef {

    private Long id;

    private String appId;

    private String name;

    private String description;

    private String endpointUrl;

    private String peerPassword;

    private RecordStatus status;

    private long versionNo;

    private String sourceAppId;

    private Map<String, String> authentications;

    public OSMessagingPeerEndpointDef(Long id, String appId, String name, String description, String endpointUrl,
            String peerPassword, RecordStatus status, long versionNo, String sourceAppId) {
        this.id = id;
        this.appId = appId;
        this.name = name;
        this.description = description;
        this.endpointUrl = endpointUrl + OSMessagingModuleNameConstants.OSMESSAGING_CONTROLLER;
        this.peerPassword = peerPassword;
        this.status = status;
        this.versionNo = versionNo;
        this.sourceAppId = sourceAppId;
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

    public String getAppId() {
        return appId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public String getPeerPassword() {
        return peerPassword;
    }

    public RecordStatus getStatus() {
        return status;
    }

    public long getVersionNo() {
        return versionNo;
    }

    public String getSourceAppId() {
        return sourceAppId;
    }

    public String getAuthentication(String processor) {
        String authentication = authentications.get(processor);
        if (authentication == null) {
            synchronized (this) {
                authentication = authentications.get(processor);
                if (authentication == null) {
                    authentication = OSMessagingUtils.getAuthorization(sourceAppId, processor, peerPassword);
                    authentications.put(processor, authentication);
                }
            }
        }

        return authentication;
    }
}

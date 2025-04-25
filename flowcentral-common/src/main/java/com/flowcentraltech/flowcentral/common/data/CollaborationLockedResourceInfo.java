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

package com.flowcentraltech.flowcentral.common.data;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.business.RequestUserPhotoGenerator;
import com.flowcentraltech.flowcentral.common.constants.CollaborationType;

/**
 * Collaboration locked resource information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class CollaborationLockedResourceInfo {

    private final RequestUserPhotoGenerator requestUserPhotoGenerator;

    private CollaborationType type;

    private String resourceName;

    private String lockedByUserLoginId;

    private String lockedByUserName;

    private String lockedByUserBranch;

    private Date lockedOn;

    public CollaborationLockedResourceInfo(RequestUserPhotoGenerator requestUserPhotoGenerator, CollaborationType type,
            String resourceName, String lockedByUserLoginId, String lockedByUserName, String lockedByUserBranch,
            Date lockedOn) {
        this.requestUserPhotoGenerator = requestUserPhotoGenerator;
        this.type = type;
        this.resourceName = resourceName;
        this.lockedByUserLoginId = lockedByUserLoginId;
        this.lockedByUserName = lockedByUserName;
        this.lockedByUserBranch = lockedByUserBranch;
        this.lockedOn = lockedOn;
    }

    public RequestUserPhotoGenerator getRequestUserPhotoGenerator() {
        return requestUserPhotoGenerator;
    }

    public CollaborationType getType() {
        return type;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getLockedByUserLoginId() {
        return lockedByUserLoginId;
    }

    public String getLockedByUserName() {
        return lockedByUserName;
    }

    public String getLockedByUserBranch() {
        return lockedByUserBranch;
    }

    public Date getLockedOn() {
        return lockedOn;
    }

}

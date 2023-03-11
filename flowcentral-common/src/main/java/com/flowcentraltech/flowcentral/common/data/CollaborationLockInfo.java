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

package com.flowcentraltech.flowcentral.common.data;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.constants.CollaborationType;

/**
 * Collaboration lock information
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class CollaborationLockInfo {

    private CollaborationType type;

    private String resourceName;

    private String lockedBy;

    private Date lockDate;

    public CollaborationLockInfo(CollaborationType type, String resourceName, String lockedBy, Date lockDate) {
        this.type = type;
        this.resourceName = resourceName;
        this.lockedBy = lockedBy;
        this.lockDate = lockDate;
    }

    public CollaborationType getType() {
        return type;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public Date getLockDate() {
        return lockDate;
    }
}

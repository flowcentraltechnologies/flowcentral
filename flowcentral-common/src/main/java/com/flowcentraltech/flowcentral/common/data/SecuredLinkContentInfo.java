/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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

import com.tcdng.unify.core.util.StringUtils;

/**
 * Secured link content information object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SecuredLinkContentInfo {

    public static final SecuredLinkContentInfo NOT_PRESENT = new SecuredLinkContentInfo();

    private String title;

    private String contentUrl;

    private String loginUrl;

    private String assignedLoginId;

    private String assignedRole;

    private boolean present;

    private boolean expired;

    public SecuredLinkContentInfo(String title, String contentUrl, String loginUrl, String assignedLoginId,
            String assignedRole, boolean expired) {
        this.title = title;
        this.contentUrl = contentUrl;
        this.loginUrl = loginUrl;
        this.assignedLoginId = assignedLoginId;
        this.assignedRole = assignedRole;
        this.expired = expired;
        this.present = true;
    }

    private SecuredLinkContentInfo() {

    }

    public String getTitle() {
        return title;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public String getAssignedLoginId() {
        return assignedLoginId;
    }

    public String getAssignedRole() {
        return assignedRole;
    }

    public boolean isWithAssignedLoginId() {
        return !StringUtils.isBlank(assignedLoginId);
    }

    public boolean isWithAssignedRole() {
        return !StringUtils.isBlank(assignedRole);
    }

    public boolean isPresent() {
        return present;
    }

    public boolean isExpired() {
        return expired;
    }

}

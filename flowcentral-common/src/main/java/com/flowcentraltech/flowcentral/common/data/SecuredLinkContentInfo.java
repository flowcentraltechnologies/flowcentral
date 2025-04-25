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

import com.flowcentraltech.flowcentral.common.constants.SecuredLinkType;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Secured link content information object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class SecuredLinkContentInfo {

    public static final SecuredLinkContentInfo NOT_PRESENT = new SecuredLinkContentInfo();

    private SecuredLinkType type;
    
    private String title;

    private String contentPath;

    private String loginUrl;

    private String docUrl;

    private String assignedLoginId;

    private String assignedRole;

    private String accessKey;

    private boolean present;

    private boolean invalidated;

    private boolean expired;

    public SecuredLinkContentInfo(SecuredLinkType type, String title, String contentPath, String loginUrl, String docUrl,
            String assignedLoginId, String assignedRole, String accessKey, boolean invalidated, boolean expired) {
        this.type = type;
        this.title = title;
        this.contentPath = contentPath;
        this.loginUrl = loginUrl;
        this.docUrl = docUrl;
        this.assignedLoginId = assignedLoginId;
        this.assignedRole = assignedRole;
        this.accessKey = accessKey;
        this.invalidated = invalidated;
        this.expired = expired;
        this.present = true;
    }

    private SecuredLinkContentInfo() {

    }

    public SecuredLinkType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getContentPath() {
        return contentPath;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public String getAssignedLoginId() {
        return assignedLoginId;
    }

    public String getAssignedRole() {
        return assignedRole;
    }

    public String getAccessKey() {
        return accessKey;
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

    public boolean isInvalidated() {
        return invalidated;
    }

    public boolean isExpired() {
        return expired;
    }

    public boolean isLogin() {
        return type != null && type.isLogin();
    }

    public boolean isWorkflowDecision() {
        return type != null && type.isWorkflowDecision();
    }
    
    public boolean isRealContentPath() {
        return  contentPath != null && contentPath.endsWith("/openPage");
    }

}

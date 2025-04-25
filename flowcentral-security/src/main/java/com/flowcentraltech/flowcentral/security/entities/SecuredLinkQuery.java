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
package com.flowcentraltech.flowcentral.security.entities;

import com.flowcentraltech.flowcentral.common.constants.SecuredLinkType;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntityQuery;

/**
 * Query class for secured.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class SecuredLinkQuery extends BaseAuditEntityQuery<SecuredLink> {

    public SecuredLinkQuery() {
        super(SecuredLink.class);
    }

    public SecuredLinkQuery assignedToLoginId(String assignedToLoginId) {
        return (SecuredLinkQuery) addEquals("assignedToLoginId", assignedToLoginId);
    }

    public SecuredLinkQuery type(SecuredLinkType type) {
        return (SecuredLinkQuery) addEquals("type", type);
    }

    public SecuredLinkQuery accessKey(String accessKey) {
        return (SecuredLinkQuery) addEquals("accessKey", accessKey);
    }

    public SecuredLinkQuery contentPath(String contentPath) {
        return (SecuredLinkQuery) addEquals("contentPath", contentPath);
    }

    public SecuredLinkQuery titleLike(String title) {
        return (SecuredLinkQuery) addLike("title", title);
    }
}

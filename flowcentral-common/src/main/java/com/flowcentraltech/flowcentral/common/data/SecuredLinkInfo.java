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

/**
 * Secured link information object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SecuredLinkInfo {

    private String title;

    private String linkUrl;

    private int expirationInMinutes;

    public SecuredLinkInfo(String title, String linkUrl, int expirationInMinutes) {
        this.title = title;
        this.linkUrl = linkUrl;
        this.expirationInMinutes = expirationInMinutes;
    }

    public String getTitle() {
        return title;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public int getExpirationInMinutes() {
        return expirationInMinutes;
    }

}

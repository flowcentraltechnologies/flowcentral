/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.system.data;

/**
 * Credential definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class CredentialDef {

    private String credName;

    private String userName;

    private String password;

    private String base64Encoded;

    private long versionNo;

    public CredentialDef(String credName, String userName, String password, String base64Encoded, long versionNo) {
        this.credName = credName;
        this.userName = userName;
        this.password = password;
        this.base64Encoded = base64Encoded;
        this.versionNo = versionNo;
    }

    public String getCredName() {
        return credName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getBase64Encoded() {
        return base64Encoded;
    }

    public long getVersionNo() {
        return versionNo;
    }
}

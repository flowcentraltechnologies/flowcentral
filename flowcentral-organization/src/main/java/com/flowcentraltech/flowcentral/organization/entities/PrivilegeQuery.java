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
package com.flowcentraltech.flowcentral.organization.entities;

import java.util.Collection;

import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntityQuery;

/**
 * Privilege query.
 * 
 * @author FlowCentral Technologies Limited
 * @version 1.0
 */
public class PrivilegeQuery extends BaseAuditEntityQuery<Privilege> {

    public PrivilegeQuery() {
        super(Privilege.class);
    }

    public PrivilegeQuery applicationId(Long applicationId) {
        return (PrivilegeQuery) addEquals("applicationId", applicationId);
    }

    public PrivilegeQuery applicationName(String applicationName) {
        return (PrivilegeQuery) addEquals("applicationName", applicationName);
    }

    public PrivilegeQuery configType(ConfigType configType) {
        return (PrivilegeQuery) addEquals("configType", configType);
    }

    public PrivilegeQuery isCustom() {
        return (PrivilegeQuery) addEquals("configType", ConfigType.CUSTOM);
    }

    public PrivilegeQuery code(String code) {
        return (PrivilegeQuery) addEquals("code", code);
    }

    public PrivilegeQuery codeIn(Collection<String> code) {
        return (PrivilegeQuery) addAmongst("code", code);
    }

    public PrivilegeQuery privilegeCatCode(String privilegeCatCode) {
        return (PrivilegeQuery) addEquals("privilegeCatCode", privilegeCatCode);
    }

    public PrivilegeQuery privilegeCategoryId(Long privilegeCategoryId) {
        return (PrivilegeQuery) addEquals("privilegeCategoryId", privilegeCategoryId);
    }

}

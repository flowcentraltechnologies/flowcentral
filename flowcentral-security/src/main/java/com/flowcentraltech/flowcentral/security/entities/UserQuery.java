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
package com.flowcentraltech.flowcentral.security.entities;

import java.util.Collection;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusWorkTenantEntityQuery;
import com.flowcentraltech.flowcentral.configuration.constants.DefaultApplicationConstants;

/**
 * Query class for user records.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class UserQuery extends BaseStatusWorkTenantEntityQuery<User> {

    public UserQuery() {
        super(User.class);
    }

    @Override
    public UserQuery id(Long id) {
        return (UserQuery) super.id(id);
    }

    @Override
    public UserQuery idIn(Collection<Long> id) {
        return (UserQuery) super.idIn(id);
    }

    @Override
    public UserQuery idNotIn(Collection<Long> id) {
        return (UserQuery) super.idNotIn(id);
    }

    public UserQuery fullNameLike(String fullName) {
        return (UserQuery) addLike("fullName", fullName);
    }

    public UserQuery loginId(String loginId) {
        ignoreTenancy(DefaultApplicationConstants.SYSTEM_LOGINID.equals(loginId));
        return (UserQuery) addEquals("loginId", loginId);
    }

    public UserQuery loginIdLike(String loginId) {
        return (UserQuery) addLike("loginId", loginId);
    }

    public UserQuery password(String password) {
        return (UserQuery) addEquals("password", password);
    }

    public UserQuery branchId(Long branchId) {
        return (UserQuery) addEquals("branchId", branchId);
    }
}

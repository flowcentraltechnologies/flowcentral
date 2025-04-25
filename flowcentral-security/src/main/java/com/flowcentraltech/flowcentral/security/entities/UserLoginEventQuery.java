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

import java.util.Collection;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntityQuery;

/**
 * Query class for user login event records.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class UserLoginEventQuery extends BaseAuditEntityQuery<UserLoginEvent> {

    public UserLoginEventQuery() {
        super(UserLoginEvent.class);
    }

    @Override
    public UserLoginEventQuery id(Long id) {
        return (UserLoginEventQuery) super.id(id);
    }

    @Override
    public UserLoginEventQuery idIn(Collection<Long> id) {
        return (UserLoginEventQuery) super.idIn(id);
    }

    @Override
    public UserLoginEventQuery idNotIn(Collection<Long> id) {
        return (UserLoginEventQuery) super.idNotIn(id);
    }

    public UserLoginEventQuery userFullNameLike(String userFullName) {
        return (UserLoginEventQuery) addLike("userFullName", userFullName);
    }

    public UserLoginEventQuery userLoginId(String userLoginId) {
        return (UserLoginEventQuery) addEquals("userLoginId", userLoginId);
    }

}

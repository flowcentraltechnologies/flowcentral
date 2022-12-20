/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntityQuery;
import com.tcdng.unify.core.criterion.OrBuilder;

/**
 * Query class for roles.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class RoleQuery extends BaseStatusEntityQuery<Role> {

    public RoleQuery() {
        super(Role.class);
    }

    @Override
    public RoleQuery addOrder(String field) {
        return (RoleQuery) super.addOrder(field);
    }

    @Override
    public RoleQuery addSelect(String field) {
        return (RoleQuery) super.addSelect(field);
    }

    public RoleQuery departmentId(Long departmentId) {
        return (RoleQuery) addEquals("departmentId", departmentId);
    }

    public RoleQuery code(String code) {
        return (RoleQuery) addEquals("code", code);
    }

    public RoleQuery codeLike(String code) {
        return (RoleQuery) addLike("code", code);
    }

    public RoleQuery description(String description) {
        return (RoleQuery) addEquals("description", description);
    }

    public RoleQuery descriptionLike(String description) {
        return (RoleQuery) addLike("description", description);
    }

    public RoleQuery activeBefore(Date activeBefore) {
        return (RoleQuery) addRestriction(
                new OrBuilder().lessEqual("activeBefore", activeBefore).isNull("activeBefore").build());
    }

    public RoleQuery activeAfter(Date activeAfter) {
        return (RoleQuery) addRestriction(
                new OrBuilder().greaterEqual("activeAfter", activeAfter).isNull("activeAfter").build());
    }
}

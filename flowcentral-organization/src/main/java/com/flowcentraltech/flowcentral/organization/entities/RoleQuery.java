/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
import java.util.List;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusWorkEntityQuery;
import com.tcdng.unify.core.criterion.OrBuilder;

/**
 * Query class for roles.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class RoleQuery extends BaseStatusWorkEntityQuery<Role> {

    public RoleQuery() {
        super(Role.class);
    }

    @Override
    public RoleQuery order(String field) {
        return (RoleQuery) super.order(field);
    }

    @Override
    public RoleQuery select(String field) {
        return (RoleQuery) super.select(field);
    }

    public RoleQuery departmentId(Long departmentId) {
        return (RoleQuery) equals("departmentId", departmentId);
    }

    public RoleQuery codeIn(List<String> codeList) {
        return (RoleQuery) amongst("code", codeList);
    }

    public RoleQuery code(String code) {
        return (RoleQuery) equals("code", code);
    }

    public RoleQuery codeLike(String code) {
        return (RoleQuery) like("code", code);
    }

    public RoleQuery description(String description) {
        return (RoleQuery) equals("description", description);
    }

    public RoleQuery descriptionLike(String description) {
        return (RoleQuery) like("description", description);
    }

    public RoleQuery activeBefore(Date activeBefore) {
        return (RoleQuery) restriction(
                new OrBuilder().lesseq("activeBefore", activeBefore).isnull("activeBefore").build());
    }

    public RoleQuery activeAfter(Date activeAfter) {
        return (RoleQuery) restriction(
                new OrBuilder().greatereq("activeAfter", activeAfter).isnull("activeAfter").build());
    }
}

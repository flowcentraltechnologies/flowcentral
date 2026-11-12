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

import com.flowcentraltech.flowcentral.common.entities.BaseEntityQuery;

/**
 * Query class for mapped branches.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class MappedBranchQuery extends BaseEntityQuery<MappedBranch> {

    public MappedBranchQuery() {
        super(MappedBranch.class);
    }

    public MappedBranchQuery zoneId(Long zoneId) {
        return (MappedBranchQuery) equals("zoneId", zoneId);
    }

    public MappedBranchQuery tenantId(Long tenantId) {
        return (MappedBranchQuery) equals("tenantId", tenantId);
    }

    public MappedBranchQuery code(String code) {
        return (MappedBranchQuery) equals("code", code);
    }

    public MappedBranchQuery sortCode(String sortCode) {
        return (MappedBranchQuery) equals("sortCode", sortCode);
    }

    public MappedBranchQuery sortCodeLike(String sortCode) {
        return (MappedBranchQuery) like("sortCode", sortCode);
    }

    public MappedBranchQuery descriptionLike(String description) {
        return (MappedBranchQuery) like("description", description);
    }
}

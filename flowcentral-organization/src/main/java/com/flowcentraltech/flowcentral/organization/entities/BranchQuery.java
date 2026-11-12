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

import java.util.Collection;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntityQuery;

/**
 * Query class for branches.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class BranchQuery extends BaseStatusEntityQuery<Branch> {

    public BranchQuery() {
        super(Branch.class);
    }

    public BranchQuery hubId(Long hubId) {
        return (BranchQuery) equals("hubId", hubId);
    }

    public BranchQuery zoneId(Long zoneId) {
        return (BranchQuery) equals("zoneId", zoneId);
    }

    public BranchQuery code(String code) {
        return (BranchQuery) equals("code", code);
    }

    public BranchQuery codeIn(Collection<String> code) {
        return (BranchQuery) amongst("code", code);
    }

    public BranchQuery sortCode(String sortCode) {
        return (BranchQuery) equals("sortCode", sortCode);
    }

    public BranchQuery sortCodeLike(String sortCode) {
        return (BranchQuery) like("sortCode", sortCode);
    }

    public BranchQuery descriptionLike(String description) {
        return (BranchQuery) like("description", description);
    }
}

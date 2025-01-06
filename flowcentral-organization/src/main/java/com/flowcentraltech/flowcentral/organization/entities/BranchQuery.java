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
package com.flowcentraltech.flowcentral.organization.entities;

import java.util.Collection;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntityQuery;

/**
 * Query class for branches.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class BranchQuery extends BaseStatusEntityQuery<Branch> {

    public BranchQuery() {
        super(Branch.class);
    }

    public BranchQuery hubId(Long hubId) {
        return (BranchQuery) addEquals("hubId", hubId);
    }

    public BranchQuery zoneId(Long zoneId) {
        return (BranchQuery) addEquals("zoneId", zoneId);
    }

    public BranchQuery code(String code) {
        return (BranchQuery) addEquals("code", code);
    }

    public BranchQuery codeIn(Collection<String> code) {
        return (BranchQuery) addAmongst("code", code);
    }

    public BranchQuery sortCode(String sortCode) {
        return (BranchQuery) addEquals("sortCode", sortCode);
    }

    public BranchQuery sortCodeLike(String sortCode) {
        return (BranchQuery) addLike("sortCode", sortCode);
    }

    public BranchQuery descriptionLike(String description) {
        return (BranchQuery) addLike("description", description);
    }
}

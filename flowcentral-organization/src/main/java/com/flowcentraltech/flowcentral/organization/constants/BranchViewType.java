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

package com.flowcentraltech.flowcentral.organization.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Branch view type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_BRANCHVIEWTYPE")
@StaticList(name = "branchviewtypelist", description = "$m{staticlist.branchviewtypelist}")
public enum BranchViewType implements EnumConst {

    USER_BRANCH_ONLY("USR"),
    ASSOCIATED_HUB_BRANCHES("HUB");

    private final String code;

    private BranchViewType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String defaultCode() {
        return USER_BRANCH_ONLY.code();
    }

    public boolean isUserBranchOnly() {
        return USER_BRANCH_ONLY.equals(this);
    }
    
    public static BranchViewType fromCode(String code) {
        return EnumUtils.fromCode(BranchViewType.class, code);
    }

    public static BranchViewType fromName(String name) {
        return EnumUtils.fromName(BranchViewType.class, name);
    }
}

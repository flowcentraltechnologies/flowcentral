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

package com.flowcentraltech.flowcentral.security.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * User workflow status.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_USERWORKFLOWSTATUS")
@StaticList(name = "userworkflowstatuslist", description = "$m{staticlist.userworkflowstatuslist}")
public enum UserWorkflowStatus implements EnumConst {

    REGISTERED("REG", true),
    AWAITING_VERIFICATION("AWV", true),
    VERIFIED("VER", false),
    VERIFICATION_FAILED("VFD", false),
    AWAITING_APPROVAL("AWA", true),
    APPROVAL_FAILED("APF", false),
    RESET_EMAIL("RES", false),
    AWAITING_RESET_EMAIL("ARE", true),
    ONBOARDED("ONB", true),
    DEACTIVATED("DCT", true);

    private final String code;

    private final boolean noChange;

    private UserWorkflowStatus(String code, boolean noChange) {
        this.code = code;
        this.noChange = noChange;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return REGISTERED.code;
    }

    public boolean isNoChange() {
        return noChange;
    }

    public static UserWorkflowStatus fromCode(String code) {
        return EnumUtils.fromCode(UserWorkflowStatus.class, code);
    }

    public static UserWorkflowStatus fromName(String name) {
        return EnumUtils.fromName(UserWorkflowStatus.class, name);
    }

}

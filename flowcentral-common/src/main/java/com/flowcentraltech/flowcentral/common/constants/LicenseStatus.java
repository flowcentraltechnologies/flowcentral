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
package com.flowcentraltech.flowcentral.common.constants;

import com.tcdng.unify.convert.constants.EnumConst;
import com.tcdng.unify.core.annotation.StaticList;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * License status enumeration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_LICENSESTATUS")
@StaticList(name = "licensestatuslist", description = "$m{staticlist.licensestatuslist}")
public enum LicenseStatus implements EnumConst {

    LICENSED(
            "LIC"),
    NOT_LICENSED(
            "NLC"),
    EXPIRED(
            "EXP");

    private final String code;

    private LicenseStatus(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return NOT_LICENSED.code;
    }

    public boolean isLicensed() {
        return LICENSED.equals(this);
    }

    public boolean isNotLicensed() {
        return NOT_LICENSED.equals(this);
    }

    public boolean isExpired() {
        return EXPIRED.equals(this);
    }

    public static LicenseStatus fromCode(String code) {
        return EnumUtils.fromCode(LicenseStatus.class, code);
    }

    public static LicenseStatus fromName(String name) {
        return EnumUtils.fromName(LicenseStatus.class, name);
    }

}

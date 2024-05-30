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
package com.flowcentraltech.flowcentral.studio.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Studio snapshot type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_STUDIOSNAPSHOTTYPE")
@StaticList(name = "studiosnapshottypelist", description = "$m{staticlist.studiosnapshottypelist}")
public enum StudioSnapshotType implements EnumConst {

    AUTOMATIC_SYSTEM(
            "AUS"),
    MANUAL_SYSTEM(
            "MNS"),
    MANUAL_UPLOAD(
            "MNU");

    private final String code;

    private StudioSnapshotType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return MANUAL_SYSTEM.code;
    }

    public boolean isAutomatic() {
        return AUTOMATIC_SYSTEM.equals(this);
    }

    public boolean isManual() {
        return MANUAL_SYSTEM.equals(this) || MANUAL_UPLOAD.equals(this);
    }

    public boolean isSystem() {
        return MANUAL_SYSTEM.equals(this) || AUTOMATIC_SYSTEM.equals(this);
    }

    public boolean isUpload() {
        return MANUAL_UPLOAD.equals(this);
    }

    public static StudioSnapshotType fromCode(String code) {
        return EnumUtils.fromCode(StudioSnapshotType.class, code);
    }

    public static StudioSnapshotType fromName(String name) {
        return EnumUtils.fromName(StudioSnapshotType.class, name);
    }
}

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

import com.flowcentraltech.flowcentral.common.entities.BaseStatusTenantEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Represents zone entity.
 * 
 * @author FlowCentral Technologies Limited
 * @version 1.0
 */
@Table(name = "FC_ZONE", uniqueConstraints = { @UniqueConstraint({ "code" }), @UniqueConstraint({ "description" }) })
public class Zone extends BaseStatusTenantEntity {

    @Column(name = "ZONE_CD", length = 16)
    private String code;

    @Column(name = "ZONE_DESC", length = 64)
    private String description;

    @Column(name = "ZONE_TIMEZONE", length = 64, nullable = true)
    private String timeZone;

    @Column(length = 64, nullable = true)
    private String languageTag;

    @Override
    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getLanguageTag() {
        return languageTag;
    }

    public void setLanguageTag(String languageTag) {
        this.languageTag = languageTag;
    }
}

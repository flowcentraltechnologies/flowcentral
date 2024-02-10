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
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Represents branch entity.
 * 
 * @author FlowCentral Technologies Limited
 * @version 1.0
 */
@Table(name = "FC_BRANCH", uniqueConstraints = { @UniqueConstraint({ "code" }), @UniqueConstraint({ "description" }),
        @UniqueConstraint({ "sortCode" }) })
public class Branch extends BaseStatusTenantEntity {

    @ForeignKey(Zone.class)
    private Long zoneId;

    @Column(name = "BRANCH_CD", length = 16)
    private String code;

    @Column(name = "BRANCH_DESC", length = 64)
    private String description;

    @Column(name = "SORT_CD", length = 32, nullable = true)
    private String sortCode;

    @Column(name = "HEAD_OFFICE_FG")
    private boolean headOffice;

    @Column(name = "ADDRESS", length = 512, nullable = true)
    private String address;

    @ListOnly(key = "zoneId", property = "code")
    private String zoneCode;

    @ListOnly(key = "zoneId", property = "description")
    private String zoneDesc;

    @ListOnly(key = "zoneId", property = "languageTag")
    private String languageTag;

    @ListOnly(key = "zoneId", property = "timeZone")
    private String timeZone;

    @Override
    public String getDescription() {
        return this.description;
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

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public boolean getHeadOffice() {
        return headOffice;
    }

    public void setHeadOffice(boolean headOffice) {
        this.headOffice = headOffice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public void setZoneId(Long zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getZoneDesc() {
        return zoneDesc;
    }

    public void setZoneDesc(String zoneDesc) {
        this.zoneDesc = zoneDesc;
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

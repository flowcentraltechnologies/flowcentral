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
package com.flowcentraltech.flowcentral.organization.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Represents branch entity.
 * 
 * @author FlowCentral Technologies Limited
 * @version 1.0
 */
@Table(name = "FC_BRANCH",
    uniqueConstraints = {
        @UniqueConstraint({ "code" }),
        @UniqueConstraint({ "sortCode" }) })
public class Branch extends BaseStatusEntity {

    @ForeignKey(Zone.class)
    private Long zoneId;

    @ForeignKey(type = Hub.class, nullable = true)
    private Long hubId;

    @ForeignKey(type = State.class, nullable = true)
    private Long stateId;

    @Column(name = "BRANCH_CD", length = 16)
    private String code;

    @Column(name = "BRANCH_DESC", length = 64)
    private String description;

    @Column(name = "SORT_CD", length = 32, nullable = true)
    private String sortCode;

    @Column(name = "ALT_SORT_CD", length = 32, nullable = true)
    private String altSortCode;

    @Column(name = "HEAD_OFFICE_FG")
    private boolean headOffice;

    @Column(name = "ADDRESS", length = 512, nullable = true)
    private String address;

    @ListOnly(key = "stateId", property = "code")
    private String stateCode;

    @ListOnly(key = "stateId", property = "description")
    private String stateDesc;

    @ListOnly(key = "hubId", property = "name")
    private String hubName;

    @ListOnly(key = "hubId", property = "description")
    private String hubDesc;

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

    public String getAltSortCode() {
        return altSortCode;
    }

    public void setAltSortCode(String altSortCode) {
        this.altSortCode = altSortCode;
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

    public Long getHubId() {
        return hubId;
    }

    public void setHubId(Long hubId) {
        this.hubId = hubId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateDesc() {
        return stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }

    public String getHubName() {
        return hubName;
    }

    public void setHubName(String hubName) {
        this.hubName = hubName;
    }

    public String getHubDesc() {
        return hubDesc;
    }

    public void setHubDesc(String hubDesc) {
        this.hubDesc = hubDesc;
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

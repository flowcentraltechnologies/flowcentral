/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.system.entities;

import com.flowcentraltech.flowcentral.common.constants.SectorStatus;
import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * System module.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_MODULE", uniqueConstraints = { @UniqueConstraint({ "name" }), @UniqueConstraint({ "description" }),
        @UniqueConstraint({ "shortCode" }) })
public class Module extends BaseStatusEntity {

    @ForeignKey(type = Sector.class, nullable = true)
    private Long sectorId;

    @Column(name = "MODULE_NM", length = 32)
    private String name;

    @Column(name = "MODULE_DESC", length = 196)
    private String description;

    @Column(name = "MODULE_LABEL", length = 64)
    private String label;

    @Column(name = "SHORT_CD", length = 16)
    private String shortCode;

    @ListOnly(key = "sectorId", property = "name")
    private String sectorName;

    @ListOnly(key = "sectorId", property = "description")
    private String sectorDesc;

    @ListOnly(key = "sectorId", property = "color")
    private String sectorColor;

    @ListOnly(key = "sectorId", property = "shortCode")
    private String sectorShortCode;

    @ListOnly(key = "sectorId", property = "status")
    private SectorStatus sectorStatus;

    @ListOnly(key = "sectorId", property = "statusDesc")
    private String sectorStatusDesc;

    @Override
    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public Long getSectorId() {
        return sectorId;
    }

    public void setSectorId(Long sectorId) {
        this.sectorId = sectorId;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getSectorDesc() {
        return sectorDesc;
    }

    public void setSectorDesc(String sectorDesc) {
        this.sectorDesc = sectorDesc;
    }

    public String getSectorColor() {
        return sectorColor;
    }

    public void setSectorColor(String sectorColor) {
        this.sectorColor = sectorColor;
    }

    public String getSectorShortCode() {
        return sectorShortCode;
    }

    public void setSectorShortCode(String sectorShortCode) {
        this.sectorShortCode = sectorShortCode;
    }

    public SectorStatus getSectorStatus() {
        return sectorStatus;
    }

    public void setSectorStatus(SectorStatus sectorStatus) {
        this.sectorStatus = sectorStatus;
    }

    public String getSectorStatusDesc() {
        return sectorStatusDesc;
    }

    public void setSectorStatusDesc(String sectorStatusDesc) {
        this.sectorStatusDesc = sectorStatusDesc;
    }

}

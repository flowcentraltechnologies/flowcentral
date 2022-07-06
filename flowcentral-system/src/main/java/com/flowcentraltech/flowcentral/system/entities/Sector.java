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

package com.flowcentraltech.flowcentral.system.entities;

import com.flowcentraltech.flowcentral.common.constants.SectorStatus;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * A sector is one or more system modules.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_SECTOR", uniqueConstraints = { @UniqueConstraint({ "name" }), @UniqueConstraint({ "description" }),
        @UniqueConstraint({ "shortCode" }) })
public class Sector extends BaseAuditEntity {

    @ForeignKey(name = "REC_ST")
    private SectorStatus status;

    @Column(name = "SECTOR_NM", length = 32)
    private String name;

    @Column(name = "SECTOR_DESC", length = 64)
    private String description;

    @Column(name = "SHORT_CD", length = 2)
    private String shortCode;
    
    @Column(length = 16)
    private String color;
    
    @ListOnly(key = "status", property = "description")
    private String statusDesc;

    @Override
    public String getDescription() {
        return description;
    }

    public SectorStatus getStatus() {
        return status;
    }

    public void setStatus(SectorStatus status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

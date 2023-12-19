/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.audit.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.Index;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Entity audit keys entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_ENTITYAUDITKEYS",
        indexes = { @Index({ "keyA" }), @Index({ "keyB" }), @Index({ "keyC" }), @Index({ "keyD" }) })
public class EntityAuditKeys extends BaseAuditEntity {

    @ForeignKey(EntityAuditDetails.class)
    private Long entityAuditDetailsId;

    @Column(length = 64)
    private String keyA;

    @Column(length = 64)
    private String keyB;

    @Column(length = 64)
    private String keyC;

    @Column(length = 64)
    private String keyD;

    @ListOnly(key = "entityAuditDetailsId", property = "entityAuditConfigId")
    private Long entityAuditConfigId;

    @Override
    public String getDescription() {
        return keyA;
    }

    public Long getEntityAuditDetailsId() {
        return entityAuditDetailsId;
    }

    public void setEntityAuditDetailsId(Long entityAuditDetailsId) {
        this.entityAuditDetailsId = entityAuditDetailsId;
    }

    public String getKeyA() {
        return keyA;
    }

    public void setKeyA(String keyA) {
        this.keyA = keyA;
    }

    public String getKeyB() {
        return keyB;
    }

    public void setKeyB(String keyB) {
        this.keyB = keyB;
    }

    public String getKeyC() {
        return keyC;
    }

    public void setKeyC(String keyC) {
        this.keyC = keyC;
    }

    public String getKeyD() {
        return keyD;
    }

    public void setKeyD(String keyD) {
        this.keyD = keyD;
    }

}

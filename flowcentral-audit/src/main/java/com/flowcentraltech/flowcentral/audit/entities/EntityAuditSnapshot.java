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

package com.flowcentraltech.flowcentral.audit.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.common.annotation.ColumnType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;

/**
 * Entity audit snapshot entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_ENTITYAUDITSNAPSHOT")
public class EntityAuditSnapshot extends BaseAuditEntity {

    @ForeignKey(EntityAuditDetails.class)
    private Long entityAuditDetailsId;
    
    @Column(name = "SNAPSHOT", type = ColumnType.CLOB)
    private String snapshot;

    @Override
    public String getDescription() {
        return null;
    }

    public Long getEntityAuditDetailsId() {
        return entityAuditDetailsId;
    }

    public void setEntityAuditDetailsId(Long entityAuditDetailsId) {
        this.entityAuditDetailsId = entityAuditDetailsId;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

}

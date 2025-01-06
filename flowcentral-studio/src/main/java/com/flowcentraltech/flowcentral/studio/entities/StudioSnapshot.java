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

package com.flowcentraltech.flowcentral.studio.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.common.annotation.ColumnType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;

/**
 * Studio snapshot entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_STUDIOSNAPSHOT")
public class StudioSnapshot extends BaseAuditEntity {

    @ForeignKey(StudioSnapshotDetails.class)
    private Long snapshotDetailsId;
    
    @Column(name = "SNAPSHOT", type = ColumnType.BLOB)
    private byte[] snapshot;

    @Override
    public String getDescription() {
        return null;
    }

    public Long getSnapshotDetailsId() {
        return snapshotDetailsId;
    }

    public void setSnapshotDetailsId(Long snapshotDetailsId) {
        this.snapshotDetailsId = snapshotDetailsId;
    }

    public byte[] getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(byte[] snapshot) {
        this.snapshot = snapshot;
    }

}

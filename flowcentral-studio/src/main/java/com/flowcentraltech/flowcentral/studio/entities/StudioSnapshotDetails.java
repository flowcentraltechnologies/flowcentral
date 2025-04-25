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

package com.flowcentraltech.flowcentral.studio.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.flowcentraltech.flowcentral.studio.constants.StudioSnapshotType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Studio snapshot details entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_STUDIOSNAPSHOTDTLS")
public class StudioSnapshotDetails extends BaseAuditEntity {

    @ForeignKey(name = "snapshot_type")
    private StudioSnapshotType snapshotType;

    @Column(name = "SNAPSHOT_NM", length = 128)
    private String snapshotName;

    @Column(name = "FILE_NM", length = 128, nullable = true)
    private String fileName;

    @Column(name = "MESSAGE", length = 512, nullable = true)
    private String message;

    @ListOnly(key = "snapshotType", property = "description")
    private String snapshotTypeDesc;

    @Override
    public String getDescription() {
        return snapshotName;
    }

    public String getSnapshotName() {
        return snapshotName;
    }

    public void setSnapshotName(String snapshotName) {
        this.snapshotName = snapshotName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StudioSnapshotType getSnapshotType() {
        return snapshotType;
    }

    public void setSnapshotType(StudioSnapshotType snapshotType) {
        this.snapshotType = snapshotType;
    }

    public String getSnapshotTypeDesc() {
        return snapshotTypeDesc;
    }

    public void setSnapshotTypeDesc(String snapshotTypeDesc) {
        this.snapshotTypeDesc = snapshotTypeDesc;
    }

}

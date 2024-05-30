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
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.task.TaskStatus;

/**
 * Studio restore details entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_STUDIORESTOREDTLS")
public class StudioRestoreDetails extends BaseAuditEntity {

    @ForeignKey(StudioSnapshotDetails.class)
    private Long snapshotDetailsId;

    @ForeignKey
    private TaskStatus taskStatus;

    @Column(name = "RESTORE_NM", length = 128)
    private String restoreName;

    @ListOnly(key = "snapshotDetailsId", property = "snapshotName")
    private String snapshotName;

    @ListOnly(key = "snapshotDetailsId", property = "fileName")
    private String fileName;

    @ListOnly(key = "taskStatus", property = "description")
    private String taskStatusDesc;

    @Override
    public String getDescription() {
        return restoreName;
    }

    public Long getSnapshotDetailsId() {
        return snapshotDetailsId;
    }

    public void setSnapshotDetailsId(Long snapshotDetailsId) {
        this.snapshotDetailsId = snapshotDetailsId;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getRestoreName() {
        return restoreName;
    }

    public void setRestoreName(String restoreName) {
        this.restoreName = restoreName;
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

    public String getTaskStatusDesc() {
        return taskStatusDesc;
    }

    public void setTaskStatusDesc(String taskStatusDesc) {
        this.taskStatusDesc = taskStatusDesc;
    }

}

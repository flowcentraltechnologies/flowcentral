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
import com.flowcentraltech.flowcentral.studio.constants.InitiationType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Studio restore details entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_STUDIORESTOREDTLS")
public class StudioRestoreDetails extends BaseAuditEntity {

    @ForeignKey(name = "initiation_type")
    private InitiationType initiationType;

    @Column(name = "RESTORE_NM", length = 128)
    private String restoreName;

    @Column(name = "FILE_NM", length = 128, nullable = true)
    private String fileName;

    @ListOnly(key = "initiationType", property = "description")
    private String initiationTypeDesc;

    @Override
    public String getDescription() {
        return restoreName;
    }

    public InitiationType getInitiationType() {
        return initiationType;
    }

    public void setInitiationType(InitiationType initiationType) {
        this.initiationType = initiationType;
    }

    public String getRestoreName() {
        return restoreName;
    }

    public void setRestoreName(String restoreName) {
        this.restoreName = restoreName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getInitiationTypeDesc() {
        return initiationTypeDesc;
    }

    public void setInitiationTypeDesc(String initiationTypeDesc) {
        this.initiationTypeDesc = initiationTypeDesc;
    }

}

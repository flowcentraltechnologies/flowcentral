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
package com.flowcentraltech.flowcentral.common.entities;

import java.util.Date;

import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ColumnType;
import com.tcdng.unify.core.annotation.Policy;

/**
 * Base class for audit entities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Policy("baseaudit-entitypolicy")
public abstract class BaseAuditEntity extends BaseEntity {

    @Column(type = ColumnType.TIMESTAMP_UTC)
    private Date createDt;

    @Column(length = 64)
    private String createdBy;

    @Column(type = ColumnType.TIMESTAMP_UTC)
    private Date updateDt;

    @Column(length = 64)
    private String updatedBy;

    public final Date getCreateDt() {
        return createDt;
    }

    public final void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    public final String getCreatedBy() {
        return createdBy;
    }

    public final void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public final Date getUpdateDt() {
        return updateDt;
    }

    public final void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public final String getUpdatedBy() {
        return updatedBy;
    }

    public final void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}

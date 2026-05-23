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
package com.flowcentraltech.flowcentral.common.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.configuration.constants.DefaultApplicationConstants;
import com.tcdng.unify.common.annotation.ColumnType;
import com.tcdng.unify.common.database.AuditEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Policy;

/**
 * Base class for audit entities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Policy("baseaudit-entitypolicy")
public abstract class BaseAuditEntity extends BaseVersionEntity implements AuditEntity {

    @Column(type = ColumnType.TIMESTAMP)
    private Date createDt;

    @Column(length = 64, defaultVal = DefaultApplicationConstants.SYSTEM_LOGINID)
    private String createdBy;

    @Column(type = ColumnType.TIMESTAMP)
    private Date updateDt;

    @Column(length = 64, defaultVal = DefaultApplicationConstants.SYSTEM_LOGINID)
    private String updatedBy;

    @Override
    public final Date getCreateDt() {
        return createDt;
    }

    @Override
    public final void setCreateDt(Date createDt) {
        this.createDt = createDt;
    }

    @Override
    public final String getCreatedBy() {
        return createdBy;
    }

    @Override
    public final void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public final Date getUpdateDt() {
        return updateDt;
    }

    @Override
    public final void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    @Override
    public final String getUpdatedBy() {
        return updatedBy;
    }

    @Override
    public final void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}

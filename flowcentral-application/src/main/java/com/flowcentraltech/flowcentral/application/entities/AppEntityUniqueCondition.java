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
package com.flowcentraltech.flowcentral.application.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.Table;

/**
 * Application entity unique condition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_ENTITYUNIQUECOND")
public class AppEntityUniqueCondition extends BaseAuditEntity {

    @ForeignKey(AppEntityUniqueConstraint.class)
    private Long appEntityUniqueConstraintId;

    @Column(name ="COND_FIELD")
    private String field;

    @Column(name ="COND_VALUE", length = 64)
    private String value;

    @Override
    public String getDescription() {
        return field;
    }

    public Long getAppEntityUniqueConstraintId() {
        return appEntityUniqueConstraintId;
    }

    public void setAppEntityUniqueConstraintId(Long appEntityUniqueConstraintId) {
        this.appEntityUniqueConstraintId = appEntityUniqueConstraintId;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}

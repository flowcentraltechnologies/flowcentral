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
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Application enumeration item entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_ENUMERATIONITEM", uniqueConstraints = {
        @UniqueConstraint({ "appEnumerationId", "code" }),
        @UniqueConstraint({ "appEnumerationId", "label" })})
public class AppEnumerationItem extends BaseAuditEntity {

    @ForeignKey(AppEnumeration.class)
    private Long appEnumerationId;

    @Column(name = "ITEM_CD", length = 32)
    private String code;

    @Column(name = "ITEM_LABEL", length = 128)
    private String label;

    @Override
    public String getDescription() {
        return label;
    }

    public Long getAppEnumerationId() {
        return appEnumerationId;
    }

    public void setAppEnumerationId(Long appEnumerationId) {
        this.appEnumerationId = appEnumerationId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}

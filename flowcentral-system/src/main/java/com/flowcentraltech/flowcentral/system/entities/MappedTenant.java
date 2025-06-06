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
package com.flowcentraltech.flowcentral.system.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Mapped;
import com.tcdng.unify.core.annotation.TableName;

/**
 * Mapped tenant entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Mapped
@TableName("FC_MAPPEDTENANT")
public class MappedTenant extends BaseEntity {

    @Column(length = 64)
    private String name;

    @Column(length = 32, nullable = true)
    private String dateFormat;
    
    @Column
    private Boolean primary;
    
    public MappedTenant(Long id, String name, String dateFormat, Boolean primary) {
        super.setId(id);
        this.name = name;
        this.dateFormat = dateFormat;
        this.primary = primary;
    }
    
    public MappedTenant() {

    }

    @Override
    public String getDescription() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

}

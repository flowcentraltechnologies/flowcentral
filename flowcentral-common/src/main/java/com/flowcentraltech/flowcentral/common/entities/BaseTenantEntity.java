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
package com.flowcentraltech.flowcentral.common.entities;

import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.TenantId;

/**
 * Base class for tenant entities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class BaseTenantEntity extends BaseEntity {

    @TenantId
    @Column
    private Long tenantId;

    public final Long getTenantId() {
        return tenantId;
    }

    public final void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

}

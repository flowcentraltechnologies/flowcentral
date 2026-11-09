/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

import com.tcdng.unify.core.annotation.Id;
import com.tcdng.unify.core.annotation.Policy;
import com.tcdng.unify.core.database.AbstractEntity;

/**
 * Base class for all entities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Policy("base-entitypolicy")
public abstract class BaseEntity  extends AbstractEntity {

    @Id
    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isIdBlank() {
        return id == null;
    }

}

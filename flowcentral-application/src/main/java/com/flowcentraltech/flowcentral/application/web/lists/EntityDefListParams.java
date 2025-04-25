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
package com.flowcentraltech.flowcentral.application.web.lists;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.tcdng.unify.core.list.AbstractListParam;

/**
 * Entity definition list parameters.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityDefListParams extends AbstractListParam {

    private EntityDef entityDef;

    public EntityDefListParams(EntityDef entityDef) {
        this.entityDef = entityDef;
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    @Override
    public boolean isPresent() {
        return entityDef != null;
    }

}

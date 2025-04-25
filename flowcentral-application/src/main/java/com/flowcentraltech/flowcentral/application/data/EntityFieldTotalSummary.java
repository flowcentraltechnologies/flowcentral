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

package com.flowcentraltech.flowcentral.application.data;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.widget.Widget;

/**
 * Entity field total summary.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EntityFieldTotalSummary {

    private EntityFieldDef entityFieldDef;

    private Widget renderer;

    public EntityFieldTotalSummary(EntityFieldDef entityFieldDef, Widget renderer) throws UnifyException {
        if (!entityFieldDef.isNumber()) {
            throw new IllegalArgumentException("Total summary can not be computed for field ["
                    + entityFieldDef.getFieldName() + "] of type [" + entityFieldDef.getDataType() + "]");
        }

        this.entityFieldDef = entityFieldDef;
        this.renderer = renderer;
    }
    
    public Widget getRenderer() {
        return renderer;
    }

    public String getFieldName() {
        return entityFieldDef.getFieldName();
    }
}

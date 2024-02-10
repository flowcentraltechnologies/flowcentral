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
package com.flowcentraltech.flowcentral.application.web.lists;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.LabelSuggestionDef;
import com.tcdng.unify.core.list.AbstractListParam;

/**
 * Entity definition and label suggestion list parameters.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityLabelSuggestionDefListParams extends AbstractListParam {

    private EntityDef entityDef;

    private LabelSuggestionDef labelSuggestionDef;

    public EntityLabelSuggestionDefListParams(EntityDef entityDef, LabelSuggestionDef labelSuggestionDef) {
        this.entityDef = entityDef;
        this.labelSuggestionDef = labelSuggestionDef;
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public LabelSuggestionDef getLabelSuggestionDef() {
        return labelSuggestionDef;
    }

    @Override
    public boolean isPresent() {
        return entityDef != null;
    }

    @Override
    public String toString() {
        return "EntityLabelSuggestionDefListParams [entityDef=" + entityDef + ", labelSuggestionDef="
                + labelSuggestionDef + "]";
    }

}

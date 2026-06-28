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
package com.flowcentraltech.flowcentral.application.web.lists;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.LabelSuggestionDef;
import com.tcdng.unify.core.list.AbstractListParam;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Entity definition and label suggestion list parameters.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EntityLabelSuggestionDefListParams extends AbstractListParam {

    private EntityDef entityDef;

    private LabelSuggestionDef labelSuggestionDef;

    private boolean includeSysParam;

    private boolean includeProcessVariable;
 
    public EntityLabelSuggestionDefListParams(EntityDef entityDef, LabelSuggestionDef labelSuggestionDef,
            boolean includeSysParam, boolean includeProcessVariable) {
        this.entityDef = entityDef;
        this.labelSuggestionDef = labelSuggestionDef;
        this.includeSysParam = includeSysParam;
        this.includeProcessVariable = includeProcessVariable;
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public LabelSuggestionDef getLabelSuggestionDef() {
        return labelSuggestionDef;
    }

    public boolean isIncludeSysParam() {
        return includeSysParam;
    }

    public boolean isIncludeProcessVariable() {
        return includeProcessVariable;
    }

    @Override
    public boolean isPresent() {
        return entityDef != null;
    }

    @Override
    public String toString() {
        return StringUtils.toXmlString(this);
    }

}

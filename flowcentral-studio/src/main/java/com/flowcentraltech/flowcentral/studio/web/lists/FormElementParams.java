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

package com.flowcentraltech.flowcentral.studio.web.lists;

import com.flowcentraltech.flowcentral.configuration.constants.FormElementType;
import com.tcdng.unify.core.list.AbstractListParam;

/**
 * Form applet list parameters.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class FormElementParams extends AbstractListParam {

    private Long parentId;

    private FormElementType type;

    public FormElementParams(Long parentId, FormElementType type) {
        this.parentId = parentId;
        this.type = type;
    }

    public Long getParentId() {
        return parentId;
    }

    public FormElementType getType() {
        return type;
    }

    @Override
    public boolean isPresent() {
        return parentId != null && type != null;
    }

    @Override
    public String toString() {
        return "FormElementParams [parentId=" + parentId + ", type=" + type + "]";
    }
}

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

package com.flowcentraltech.flowcentral.application.data;

import java.util.Set;

import com.flowcentraltech.flowcentral.common.data.FormStateRule;
import com.flowcentraltech.flowcentral.configuration.constants.FormElementType;
import com.tcdng.unify.core.constant.TriState;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Application set state definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SetStateDef implements FormStateRule {

    private FormElementType type;

    private Set<String> target;

    private TriState required;

    private TriState visible;

    private TriState editable;

    private TriState disabled;

    public SetStateDef(FormElementType type, Set<String> target, TriState required, TriState visible, TriState editable,
            TriState disabled) {
        this.type = type;
        this.target = DataUtils.unmodifiableSet(target);
        this.required = required;
        this.visible = visible;
        this.editable = editable;
        this.disabled = disabled;
    }

    public FormElementType getType() {
        return type;
    }

    @Override
    public Set<String> getTarget() {
        return target;
    }

    @Override
    public TriState getRequired() {
        return required;
    }

    @Override
    public TriState getVisible() {
        return visible;
    }

    @Override
    public TriState getEditable() {
        return editable;
    }

    @Override
    public TriState getDisabled() {
        return disabled;
    }

    public boolean isAnnotationRule() {
        return type.isAnnotationType();
    }

    @Override
    public boolean isTabRule() {
        return type.isTabType();
    }

    @Override
    public boolean isSectionRule() {
        return type.isSectionType();
    }

    @Override
    public boolean isFieldRule() {
        return type.isFieldType();
    }

    @Override
    public String toString() {
        return "SetStateDef [type=" + type + ", target=" + target + ", required=" + required + ", visible=" + visible
                + ", editable=" + editable + ", disabled=" + disabled + "]";
    }
}

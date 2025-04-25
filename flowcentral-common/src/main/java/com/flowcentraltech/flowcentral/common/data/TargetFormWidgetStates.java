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

package com.flowcentraltech.flowcentral.common.data;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.FormElementType;
import com.tcdng.unify.core.constant.TriState;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Target form widget states.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class TargetFormWidgetStates extends AbstractTargetFormStates {

    private List<TargetFormState> targetStateList;

    public TargetFormWidgetStates() {
        this.targetStateList = new ArrayList<TargetFormState>();
    }

    public void setSectionState(TriState required, TriState visible,
            TriState editable, TriState disabled, String... targets) {
        targetStateList.add(new TargetFormState(FormElementType.SECTION, required, visible,
                editable, disabled, targets));
    }

    public void setFieldState(TriState required, TriState visible,
            TriState editable, TriState disabled, String... targets) {
        targetStateList.add(new TargetFormState(FormElementType.FIELD, required, visible,
                editable, disabled, targets));
    }

    public List<TargetFormState> getTargetStateList() {
        return DataUtils.unmodifiableList(targetStateList);
    }

    public boolean isWithStateList() {
        return !DataUtils.isBlank(targetStateList);
    }

}

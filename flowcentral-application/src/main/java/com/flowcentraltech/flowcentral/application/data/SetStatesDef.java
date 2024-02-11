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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.FormElementType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.TriState;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Application set states definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SetStatesDef {

    private List<SetStateDef> setStateList;

    private SetStatesDef(List<SetStateDef> setStateList) {
        this.setStateList = setStateList;
    }

    public List<SetStateDef> getSetStateList() {
        return setStateList;
    }

    public boolean isBlank() {
        return setStateList.isEmpty();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private List<SetStateDef> setStateList;

        public Builder addSetStateDef(FormElementType type, Collection<String> target, TriState required, TriState visible,
                TriState editable, TriState disabled) {
            if (setStateList == null) {
                setStateList = new ArrayList<SetStateDef>();
            }

            setStateList.add(new SetStateDef(type, new HashSet<String>(target), required, visible, editable, disabled));
            return this;
        }

        public SetStatesDef build() throws UnifyException {
            return new SetStatesDef(DataUtils.unmodifiableList(setStateList));
        }
    }
}

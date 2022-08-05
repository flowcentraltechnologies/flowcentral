/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.data.WidgetTypeDef;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.common.input.AbstractInput;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Input array entries.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class InputArrayEntries {

    private List<InputArrayEntry> entryList;

    private InputArrayEntries(List<InputArrayEntry> entryList) {
        this.entryList = entryList;
    }

    public List<InputArrayEntry> getEntryList() {
        return entryList;
    }

    public static Builder newBuilder(WidgetTypeDef widgetTypeDef) {
        return new Builder(widgetTypeDef, new EntryAttributes());
    }

    public static Builder newBuilder(WidgetTypeDef widgetTypeDef, EntryAttributes attributes) {
        return new Builder(widgetTypeDef, attributes);
    }

    public static class Builder {

        private WidgetTypeDef widgetTypeDef;

        private EntryAttributes attributes;

        private List<InputArrayEntry> entryList;

        private Set<String> names;

        public Builder(WidgetTypeDef widgetTypeDef, EntryAttributes attributes) {
            this.widgetTypeDef = widgetTypeDef;
            this.attributes = attributes;
            this.entryList = new ArrayList<InputArrayEntry>();
            this.names = new HashSet<String>();
        }

        @SuppressWarnings("unchecked")
        public Builder addEntry(String name, String label, Object val, boolean selected, boolean editable)
                throws UnifyException {
            if (names.contains(name)) {
                throw new IllegalArgumentException("Entry with name [" + name + "] already exists.");
            }

            AbstractInput<Object> paramInput = (AbstractInput<Object>) InputWidgetUtils.newInput(widgetTypeDef,
                    attributes);
            paramInput.setValue(val);
            InputArrayEntry entry = new InputArrayEntry(name, label, paramInput, editable, selected);
            entryList.add(entry);
            return this;
        }

        public InputArrayEntries build() {
            return new InputArrayEntries(DataUtils.unmodifiableList(entryList));
        }
    }
}

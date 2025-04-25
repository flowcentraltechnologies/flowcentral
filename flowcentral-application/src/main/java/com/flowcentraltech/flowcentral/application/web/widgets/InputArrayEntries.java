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

package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.data.InputValue;
import com.flowcentraltech.flowcentral.application.data.WidgetTypeDef;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.validation.Validator;
import com.flowcentraltech.flowcentral.common.data.EntryAttributes;
import com.flowcentraltech.flowcentral.common.input.AbstractInput;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Input array entries.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class InputArrayEntries {

    private List<InputArrayEntry> entryList;

    private Validator validator;

    private Set<Object> validationFailedKeys;

    private int columns;

    private InputArrayEntries(List<InputArrayEntry> entryList, Validator validator, int columns) {
        this.entryList = entryList;
        this.columns = columns;
        this.validator = validator;
    }

    public List<InputArrayEntry> getEntryList() {
        return entryList;
    }

    public int getColumns() {
        return columns;
    }

    public boolean validate() throws UnifyException {
        validationFailedKeys = Collections.emptySet();
        if (validator != null) {
            validationFailedKeys = new HashSet<Object>();
            for (InputArrayEntry entry : entryList) {
                if (entry.isEditable()) {
                    Object val = entry.getValueInput().getValue();
                    if (val != null && !validator.validate(val)) {
                        validationFailedKeys.add(entry.getKey());
                    }
                }
            }

            validationFailedKeys = Collections.unmodifiableSet(validationFailedKeys);
        }

        return validationFailedKeys.isEmpty();
    }

    public List<InputValue> getValueList() {
        List<InputValue> list = new ArrayList<InputValue>();
        for (InputArrayEntry entry : entryList) {
            list.add(new InputValue(entry.getKey(), entry.getValueInput().getValue(), entry.getLabel(),
                    entry.isEditable(), entry.isSelected()));
        }

        return list;
    }

    public Map<Object, InputValue> getValueMap() {
        Map<Object, InputValue> map = new HashMap<Object, InputValue>();
        for (InputArrayEntry entry : entryList) {
            map.put(entry.getKey(), new InputValue(entry.getKey(), entry.getValueInput().getValue(), entry.getLabel(),
                    entry.isEditable(), entry.isSelected()));
        }

        return map;
    }

    public static Builder newBuilder(WidgetTypeDef widgetTypeDef) {
        return new Builder(widgetTypeDef, EntryAttributes.BLANK);
    }

    public static Builder newBuilder(WidgetTypeDef widgetTypeDef, EntryAttributes attributes) {
        return new Builder(widgetTypeDef, attributes);
    }

    public static class Builder {

        private WidgetTypeDef widgetTypeDef;

        private Validator validator;

        private EntryAttributes attributes;

        private List<InputArrayEntry> entryList;

        private int columns;

        private Set<Object> keys;

        public Builder(WidgetTypeDef widgetTypeDef, EntryAttributes attributes) {
            this.widgetTypeDef = widgetTypeDef;
            this.attributes = attributes;
            this.entryList = new ArrayList<InputArrayEntry>();
            this.keys = new HashSet<Object>();
        }

        public Builder columns(int columns) {
            this.columns = columns;
            return this;
        }

        public Builder validator(Validator validator) {
            this.validator = validator;
            return this;
        }

        public Builder addEntries(List<InputValue> inputValueList) throws UnifyException {
            for (InputValue inputValue : inputValueList) {
                addEntry(inputValue);
            }

            return this;
        }

        public Builder addEntry(InputValue inputValue) throws UnifyException {
            return addEntry(inputValue.getKey(), inputValue.getValue(), inputValue.getLabel(), inputValue.isEditable(),
                    inputValue.isSelected());
        }

        @SuppressWarnings("unchecked")
        public Builder addEntry(Object key, Object val, String label, boolean editable, boolean selected)
                throws UnifyException {
            if (keys.contains(key)) {
                throw new IllegalArgumentException("Entry with key [" + key + "] already exists.");
            }

            AbstractInput<Object> paramInput = (AbstractInput<Object>) InputWidgetUtils.newInput(widgetTypeDef,
                    attributes);
            paramInput.setValue(val);
            InputArrayEntry entry = new InputArrayEntry(key, label, paramInput, editable, selected);
            entryList.add(entry);
            return this;
        }

        public InputArrayEntries build() {
            return new InputArrayEntries(DataUtils.unmodifiableList(entryList), validator, columns);
        }
    }
}

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
package com.flowcentraltech.flowcentral.common.input;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Abstract input.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractInput<T> {

    private Class<T> type;

    protected T value;

    private String editor;

    private String renderer;

    public AbstractInput(Class<T> type, String editor, String renderer) {
        this.type = type;
        this.editor = editor;
        this.renderer = renderer;
    }

    public String getStringValue() throws UnifyException {
        return DataUtils.convert(String.class, value);
    }

    public void setStringValue(String value) throws UnifyException {
        this.value = DataUtils.convert(type, value);
    }

    public Class<T> getType() {
        return type;
    }

    public String getEditor() {
        return editor;
    }

    public String getRenderer() {
        return renderer;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean compatible(AbstractInput<?> in) {
        return type.equals(in.type) && editor.equals(in.editor);
    }
}

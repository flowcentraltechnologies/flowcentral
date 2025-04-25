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

import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.configuration.constants.InputType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.DataType;

/**
 * Widget type definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class WidgetTypeDef extends BaseApplicationEntityDef {

    private DataType dataType;

    private InputType type;

    private String editor;

    private String renderer;

    private boolean stretch;

    public WidgetTypeDef(DataType dataType, InputType type, String longName, String description, Long id,
            long versionNo, String editor, String renderer) throws UnifyException {
        this(dataType, type, longName, description, id, versionNo, editor, renderer, true);
    }

    public WidgetTypeDef(DataType dataType, InputType type, String longName, String description, Long id,
            long versionNo, String editor, String renderer, boolean stretch) throws UnifyException {
        super(ApplicationNameUtils.getApplicationEntityNameParts(longName), description, id, versionNo);
        this.dataType = dataType;
        this.type = type;
        this.editor = editor;
        this.renderer = renderer;
        this.stretch = stretch;
    }

    public DataType getDataType() {
        return dataType;
    }

    public InputType getInputType() {
        return type;
    }

    public String getEditor() {
        return editor;
    }

    public String getRenderer() {
        return renderer;
    }

    public boolean isStretch() {
        return stretch;
    }

    public boolean isEnumWidget() {
        return InputWidgetUtils.isEnumerationWidget(getName());
    }

}

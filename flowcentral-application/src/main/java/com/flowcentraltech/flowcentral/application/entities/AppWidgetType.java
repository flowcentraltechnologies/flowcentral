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
package com.flowcentraltech.flowcentral.application.entities;

import com.flowcentraltech.flowcentral.configuration.constants.InputType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.constant.DataType;

/**
 * Application widget type entity;
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_WIDGETTYPE")
public class AppWidgetType extends BaseApplicationEntity {

    @ForeignKey
    private DataType dataType;

    @ForeignKey(nullable = true)
    private InputType inputType;

    @Column(length = 512)
    private String editor;

    @Column(length = 512)
    private String renderer;

    @Column
    private boolean stretch;

    @Column
    private boolean listOption;

    @Column
    private boolean enumOption;

    @ListOnly(key = "dataType", property = "description")
    private String dataTypeDesc;

    @ListOnly(key = "inputType", property = "description")
    private String inputTypeDesc;

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public InputType getInputType() {
        return inputType;
    }

    public void setInputType(InputType inputType) {
        this.inputType = inputType;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public boolean isStretch() {
        return stretch;
    }

    public void setStretch(boolean stretch) {
        this.stretch = stretch;
    }

    public boolean isListOption() {
        return listOption;
    }

    public void setListOption(boolean listOption) {
        this.listOption = listOption;
    }

    public boolean isEnumOption() {
        return enumOption;
    }

    public void setEnumOption(boolean enumOption) {
        this.enumOption = enumOption;
    }

    public String getDataTypeDesc() {
        return dataTypeDesc;
    }

    public void setDataTypeDesc(String dataTypeDesc) {
        this.dataTypeDesc = dataTypeDesc;
    }

    public String getInputTypeDesc() {
        return inputTypeDesc;
    }

    public void setInputTypeDesc(String inputTypeDesc) {
        this.inputTypeDesc = inputTypeDesc;
    }

}

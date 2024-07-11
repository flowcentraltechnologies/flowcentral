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

import com.tcdng.unify.core.util.StringUtils;

/**
 * Form field definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormFieldDef {

    private EntityFieldDef entityFieldDef;

    private WidgetTypeDef widgetTypeDef;

    private RefDef inputRefDef;

    private String previewForm;

    private String fieldLabel;

    private String renderer;

    private int column;

    private boolean switchOnChange;

    private boolean saveAs;

    private boolean required;

    private boolean visible;

    private boolean editable;

    private boolean disabled;

    public FormFieldDef(EntityFieldDef entityFieldDef, WidgetTypeDef widgetTypeDef, RefDef inputRefDef,
            String previewForm, String fieldLabel, String renderer, int column, boolean switchOnChange, boolean saveAs, boolean required,
            boolean visible, boolean editable, boolean disabled) {
        this.entityFieldDef = entityFieldDef;
        this.widgetTypeDef = widgetTypeDef;
        this.inputRefDef = inputRefDef;
        this.previewForm = previewForm;
        this.fieldLabel = fieldLabel;
        this.renderer = renderer;
        this.column = column;
        this.switchOnChange = switchOnChange;
        this.saveAs = saveAs;
        this.required = required;
        this.visible = visible;
        this.editable = editable;
        this.disabled = disabled;
    }

    public FormFieldDef(FormFieldDef srcFormFieldDef, int column) {
        this(srcFormFieldDef, column, srcFormFieldDef.required, srcFormFieldDef.visible, srcFormFieldDef.editable,
                srcFormFieldDef.disabled);
    }

    public FormFieldDef(FormFieldDef srcFormFieldDef, int column, boolean required, boolean visible, boolean editable,
            boolean disabled) {
        this.column = column;
        this.entityFieldDef = srcFormFieldDef.entityFieldDef;
        this.widgetTypeDef = srcFormFieldDef.widgetTypeDef;
        this.inputRefDef = srcFormFieldDef.inputRefDef;
        this.previewForm = srcFormFieldDef.previewForm;
        this.fieldLabel = srcFormFieldDef.fieldLabel;
        this.renderer = srcFormFieldDef.renderer;
        this.switchOnChange = srcFormFieldDef.switchOnChange;
        this.saveAs = srcFormFieldDef.saveAs;
        this.required = required;
        this.visible = visible;
        this.editable = editable;
        this.disabled = disabled;
    }

    public String getFieldName() {
        return entityFieldDef.getFieldName();
    }

    public String getWidgetName() {
        return widgetTypeDef.getLongName();
    }

    public RefDef getInputRefDef() {
        return inputRefDef;
    }

    public boolean isWithInputRefDef() {
        return inputRefDef != null;
    }

    public String getPreviewForm() {
        return previewForm;
    }

    public boolean isWithPreviewForm() {
        return !StringUtils.isBlank(previewForm);
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public String getRenderer() {
        return renderer;
    }

    public int getColumn() {
        return column;
    }

    public int getMinLen() {
        return entityFieldDef.getMinLen();
    }

    public int getMaxLen() {
        return entityFieldDef.getMaxLen();
    }

    public boolean isListOnly() {
        return entityFieldDef.isListOnly();
    }

    public boolean isSwitchOnChange() {
        return switchOnChange;
    }

    public boolean isSaveAs() {
        return saveAs;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isEditable() {
        return editable && !entityFieldDef.isListOnly();
    }

    public boolean isDisabled() {
        return disabled;
    }

}

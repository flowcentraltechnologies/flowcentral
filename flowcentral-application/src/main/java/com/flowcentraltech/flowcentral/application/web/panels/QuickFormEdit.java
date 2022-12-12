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
package com.flowcentraltech.flowcentral.application.web.panels;

import java.util.List;

import com.flowcentraltech.flowcentral.application.web.widgets.MiniForm;
import com.flowcentraltech.flowcentral.common.data.FormMessage;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Quick form edit object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class QuickFormEdit {
   
    private final MiniForm form;

    private List<FormMessage> validationErrors;

    private String formCaption;
    
    private int width;

    private int height;

    public QuickFormEdit(MiniForm form) {
        this.form = form;
    }

    public MiniForm getForm() throws UnifyException {
        return form;
    }

    public List<FormMessage> getMessages() {
        return validationErrors;
    }

    public String getFormCaption() {
        return formCaption;
    }

    public void setFormCaption(String formCaption) {
        this.formCaption = formCaption;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean commit() throws UnifyException {
        validationErrors = null;
        return DataUtils.isBlank(validationErrors);
    }

}

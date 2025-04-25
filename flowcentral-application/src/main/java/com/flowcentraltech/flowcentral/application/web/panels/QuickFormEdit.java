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
package com.flowcentraltech.flowcentral.application.web.panels;

import java.util.List;

import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniForm;
import com.flowcentraltech.flowcentral.common.business.policies.FormValidationContext;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.data.FormMessage;
import com.tcdng.unify.core.UnifyException;

/**
 * Quick form edit object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class QuickFormEdit {

    private final AppletContext ctx;

    private final SweepingCommitPolicy scp;

    private final AppletDef formAppletDef;

    private final MiniForm form;

    private String formCaption;

    private int width;

    private int height;

    public QuickFormEdit(AppletContext ctx, SweepingCommitPolicy scp, AppletDef formAppletDef, MiniForm form) {
        this.ctx = ctx;
        this.scp = scp;
        this.formAppletDef = formAppletDef;
        this.form = form;
    }

    public void formSwitchOnChange() throws UnifyException {
        ctx.au().onMiniformSwitchOnChange(form);
    }

    public MiniForm getForm() throws UnifyException {
        return form;
    }

    public FormContext getFormContext() {
        return form.getCtx();
    }

    public List<FormMessage> getMessages() {
        return form.getCtx().getValidationErrors();
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
        FormContext formContext = getForm().getCtx();
        ctx.au().formContextEvaluator().evaluateFormContext(formContext, new FormValidationContext(EvaluationMode.UPDATE));
        if (!form.getCtx().isWithFormErrors()) {
            ctx.au().updateEntityInstByFormContext(formAppletDef, formContext, scp);
            return true;
        }

        return false;
    }

}

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
package com.flowcentraltech.flowcentral.application.web.panels;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.common.business.policies.FormValidationContext;
import com.flowcentraltech.flowcentral.common.business.policies.FormWizardNavigationPolicy;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;

/**
 * Form wizard panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-formwizardpanel")
@UplBinding("web/application/upl/formwizardpanel.upl")
public class FormWizardPanel extends AbstractFormPanel {

    @Configurable
    private AppletUtilities appletUtilities;

    @Action
    public void previous() throws UnifyException {
        FormWizard formWizard = getFormWizard();
        FormContext ctx = getFormWizard().getFormContext();
        ctx.clearValidationErrors();
        ctx.clearReviewErrors();
        if (formWizard.isWithNavPolicy()) {
            FormWizardNavigationPolicy policy = getComponent(FormWizardNavigationPolicy.class, formWizard.getNavPolicy());
            Map<String, Object> attributes = getPageAttributes(policy.pageAttributeNames());
            policy.onPrevious(ctx.getFormValueStore(), ctx, formWizard.getCurrentPage(), attributes);
            if (ctx.isWithFormErrors()) {
                return;
            }
        }

        formWizard.prevPage();
    }

    @Action
    public void next() throws UnifyException {
        FormWizard formWizard = getFormWizard();
        FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(EvaluationMode.CREATE));
        if (!ctx.isWithFormErrors()) {
            if (formWizard.isWithNavPolicy()) {
                FormWizardNavigationPolicy policy = getComponent(FormWizardNavigationPolicy.class, formWizard.getNavPolicy());
                Map<String, Object> attributes = getPageAttributes(policy.pageAttributeNames());
                policy.onNext(ctx.getFormValueStore(), ctx, formWizard.getCurrentPage(), attributes);
                if (ctx.isWithFormErrors()) {
                    return;
                }
            }

            formWizard.nextPage();
        }
    }

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        final FormWizard formWizard = getFormWizard();
        setDisabled("prevBtn", formWizard.isFirstPage());
        setDisabled("nextBtn", formWizard.isLastPage());

        setVisible("cancelBtn", !formWizard.isLastPage());

        final boolean last = formWizard.isLastPage();
        setVisible("submitCloseBtn", last && formWizard.isSubmit());
        setVisible("executeBtn", last && formWizard.isExecute());
        setVisible("saveCloseBtn", last && !formWizard.isSubmit() && !formWizard.isExecute());
    }

    protected final FormContext evaluateCurrentFormContext(FormValidationContext vCtx) throws UnifyException {
        FormContext ctx = getFormWizard().getFormContext();
        if (ctx.getFormDef() != null && ctx.getFormDef().isInputForm()) {
            appletUtilities.formContextEvaluator().evaluateFormContext(ctx, vCtx);

            if (ctx.isWithFieldErrors()) {
                hintUser(MODE.ERROR, "$m{entityformapplet.formvalidation.error.hint}");
            }
        } else {
            ctx.clearReviewErrors();
            ctx.clearValidationErrors();
        }

        return ctx;
    }

    private Map<String, Object> getPageAttributes(List<String> pageAttributeNames) throws UnifyException {
        if (!DataUtils.isBlank(pageAttributeNames)) {
            Map<String, Object> attributes = new HashMap<String, Object>();
            for (String name : pageAttributeNames) {
                attributes.put(name, getPageAttribute(name));
            }

            return attributes;
        }

        return Collections.emptyMap();
    }
    
    private FormWizard getFormWizard() throws UnifyException {
        return getValue(FormWizard.class);
    }

}

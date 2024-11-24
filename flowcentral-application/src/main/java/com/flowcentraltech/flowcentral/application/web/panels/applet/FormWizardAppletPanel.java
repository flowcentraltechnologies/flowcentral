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
package com.flowcentraltech.flowcentral.application.web.panels.applet;

import org.apache.commons.lang3.StringUtils;

import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.FormWizardCompletionType;
import com.flowcentraltech.flowcentral.application.constants.FormWizardExecuteTaskConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.FormWizard;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.FormValidationContext;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;

/**
 * Form wizard applet panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-formwizardappletpanel")
@UplBinding("web/application/upl/formwizardappletpanel.upl")
public class FormWizardAppletPanel extends AbstractAppletPanel {

    @Action
    public void execute() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(EvaluationMode.CREATE));
        if (!ctx.isWithFormErrors()) {
            final FormWizardApplet applet = getFormWizardApplet();
            final AppletDef appletDef = application().getAppletDef(applet.getAppletName());
            final String processorName = appletDef.getPropValue(String.class,
                    AppletPropertyConstants.WIZARD_FORM_TASK_PROCESSOR);
            TaskSetup taskSetup = TaskSetup.newBuilder(FormWizardExecuteTaskConstants.FORM_WIZARD_EXECUTE_TASK_NAME)
                    .setParam(FormWizardExecuteTaskConstants.FORM_WIZARD_ENTITY, ctx.getInst())
                    .setParam(FormWizardExecuteTaskConstants.FORM_WIZARD_PROCESSOR, processorName)
                    .logMessages()
                    .build();
            launchTaskWithMonitorBox(taskSetup, appletDef.getDescription(), getActionFullPath("/closePage"), null);
        }
    }

    @Action
    public void submit() throws UnifyException {
        final FormWizardApplet applet = getFormWizardApplet();
        final EvaluationMode evalMode = EvaluationMode.getCreateSubmitMode(
                applet.getRootAppletProp(boolean.class, AppletPropertyConstants.CREATE_FORM_SUBMIT_VALIDATE, false));
        FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(evalMode));
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = applet.submitInst();
            if (!ctx.isWithReviewErrors()) {
                entityActionResult.setSuccessHint("$m{entityformapplet.submit.success.hint}");
            }

            handleEntityActionResult(entityActionResult, ctx);
            setCloseResultMapping();
        }
    }

    @Action
    public void save() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(EvaluationMode.CREATE));
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getFormWizardApplet().saveNewInst();
            entityActionResult.setSuccessHint("$m{entityformapplet.new.success.hint}");
            handleEntityActionResult(entityActionResult, ctx);
            setCloseResultMapping();
        }
    }

    @Action
    public void cancel() throws UnifyException {
        setCloseResultMapping();
    }

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        final FormWizardApplet applet = getFormWizardApplet();
        final FormWizardCompletionType completionType = applet.getRootAppletProp(FormWizardCompletionType.class,
                AppletPropertyConstants.WIZARD_FORM_COMPLETION);
        final FormWizard formWizard = applet.getFormWizard();
        formWizard.setSubmit(false);
        formWizard.setExecute(false);
        if (completionType != null) {
            if (completionType.isSubmit()) {
                final String submitCaption = applet.getRootAppletProp(String.class,
                        AppletPropertyConstants.CREATE_FORM_SUBMIT_CAPTION);
                formWizard.setSubmitCaption(!StringUtils.isBlank(submitCaption) ? submitCaption
                        : resolveSessionMessage("$m{button.submit}"));
                formWizard.setSubmitStyleClass("fc-greenbutton");
                formWizard.setSubmit(true);
            } else if (completionType.isExecute()) {
                final String processor = applet.getRootAppletProp(String.class,
                        AppletPropertyConstants.WIZARD_FORM_TASK_PROCESSOR);
                formWizard.setExecCaption(resolveSessionMessage("$m{button.execute}"));
                formWizard.setExecStyleClass("fc-redbutton");
                formWizard.setExecProcessor(processor);
                formWizard.setExecute(true);
            }
        }
    }

    @Override
    protected void onReviewErrors(EntityActionResult entityActionResult) throws UnifyException {

    }

    protected final FormContext evaluateCurrentFormContext(FormValidationContext vCtx) throws UnifyException {
        return evaluateCurrentFormContext(vCtx, false);
    }

    protected final FormContext evaluateCurrentFormContext(FormValidationContext vCtx, boolean commentRequired)
            throws UnifyException {
        FormContext ctx = getFormWizardApplet().getFormWizard().getFormContext();
        if (ctx.getFormDef() != null && ctx.getFormDef().isInputForm()) {
            evaluateCurrentFormContext(ctx, vCtx);
        } else {
            ctx.clearReviewErrors();
            ctx.clearValidationErrors();
        }

        if (vCtx.isEvaluation()) {
            if (ctx.isWithFormErrors()) {
                hintUser(MODE.ERROR, "$m{entityformapplet.formvalidation.error.hint}");
            }
        }

        return ctx;
    }

    private FormContext evaluateCurrentFormContext(final FormContext ctx, FormValidationContext vCtx)
            throws UnifyException {
        FormWizardApplet applet = getFormWizardApplet();
        applet.au().formContextEvaluator().evaluateFormContext(ctx, vCtx);

        final boolean isWithFieldErrors = ctx.isWithFieldErrors();
        if (isWithFieldErrors) {
            hintUser(MODE.ERROR, "$m{entityformapplet.formvalidation.error.hint}");
        }

        return ctx;
    }

    private FormWizardApplet getFormWizardApplet() throws UnifyException {
        return getValue(FormWizardApplet.class);
    }
}

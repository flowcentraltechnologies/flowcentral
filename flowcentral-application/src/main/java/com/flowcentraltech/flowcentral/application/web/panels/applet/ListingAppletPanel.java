/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.application.data.FormActionDef;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.FormPanel;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ResultMappingConstants;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;

/**
 * Listing applet panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-listingappletpanel")
@UplBinding("web/application/upl/listingappletpanel.upl")
public class ListingAppletPanel extends AbstractAppletPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        final ListingApplet applet = getListEntityApplet();
        applet.ensureFormStruct();

        final AppletContext appCtx = applet.getCtx();
        setVisible("listingPanel.emailsPanel", appCtx.isReview() && appCtx.isEmails());
        setVisible("listingPanel.commentsPanel", appCtx.isReview() && appCtx.isComments());
        setVisible("listingPanel.errorsPanel", appCtx.isReview() && appCtx.isRecovery());
        setEditable("listingPanel.errorsPanel", false);
    }

    @Action
    public void performFormAction() throws UnifyException {
        String actionName = getRequestTarget(String.class);
        final ListingApplet applet = getListEntityApplet();
        FormActionDef formActionDef = applet.getCurrentFormDef().getFormActionDef(actionName);
        FormContext ctx = evaluateCurrentFormContext(EvaluationMode.getRequiredMode(formActionDef.isValidateForm()));
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = applet.formActionOnInst(formActionDef.getPolicy(), actionName);
            handleEntityActionResult(entityActionResult);
        }
    }

    @Action
    public void close() throws UnifyException {
        setCommandResultMapping(ResultMappingConstants.CLOSE);
        return;
    }

    @Override
    protected void onReviewErrors(EntityActionResult entityActionResult) throws UnifyException {
        
    }

    private FormContext evaluateCurrentFormContext(EvaluationMode evaluationMode) throws UnifyException {
        return evaluateCurrentFormContext(evaluationMode, false);
    }

    private FormContext evaluateCurrentFormContext(EvaluationMode evaluationMode, boolean commentRequired)
            throws UnifyException {
        FormContext ctx = getListEntityApplet().getResolvedForm().getCtx();
        ctx.clearReviewErrors();
        ctx.clearValidationErrors();

        if (evaluationMode.evaluation()) {
            if (evaluationMode.review() && ctx.getAppletContext().isReview()) {
                if (commentRequired) {
                    FormPanel commentsFormPanel = getWidgetByShortName(FormPanel.class, "listingPanel.commentsPanel");;
                    ctx.mergeValidationErrors(commentsFormPanel.validate(evaluationMode));
                }

                if (ctx.getAppletContext().isEmails()) {
                    FormPanel emailsFormPanel = getWidgetByShortName(FormPanel.class, "listingPanel.emailsPanel");;
                    ctx.mergeValidationErrors(emailsFormPanel.validate(evaluationMode));
                }
            }

            if (ctx.isWithFormErrors()) {
                hintUser(MODE.ERROR, "$m{listingapplet.formvalidation.error.hint}");
            }
        }

        return ctx;
    }


    private ListingApplet getListEntityApplet() throws UnifyException {
        return getValue(ListingApplet.class);
    }

}

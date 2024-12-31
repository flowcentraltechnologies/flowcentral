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

import com.flowcentraltech.flowcentral.application.data.LoadingWorkItemInfo;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.FormValidationContext;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;

/**
 * Abstract loading list applet panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-loadingappletpanel")
@UplBinding("web/application/upl/loadingappletpanel.upl")
public abstract class AbstractLoadingAppletPanel<T extends AbstractLoadingApplet> extends AbstractEntityFormAppletPanel {

    @Override
    @Action
    public void performFormAction() throws UnifyException {
        final T applet = getLoadingApplet();
        if (applet.appletCtx().isReview()) {
            final String actionName = getRequestTarget(String.class);
            final LoadingWorkItemInfo loadingWorkItemInfo = applet.getCurrentLoadingWorkItemInfo();
            final FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(
                    EvaluationMode.getUpdateMode(loadingWorkItemInfo.isValidateFormOnAction(actionName)), actionName),
                    applet.isNewCommentRequired(actionName));
            if (!ctx.isWithFormErrors()) {
                if (ctx.getFormDef() == null) {
                    applet.updateSingleFormInst();
                    applet.applyUserAction(actionName);
                    hintUser("$m{reviewsingleformworkitemsapplet.apply.success.hint}");
                } else {
                    if (ctx.getFormDef().isInputForm()) {
                        EntityActionResult entityActionResult = applet.updateInstAndClose(actionName);
                        if (ctx.isWithReviewErrors()) {
                            entityActionResult.setApplyUserAction(true);
                            entityActionResult.setUserAction(actionName);
                            entityActionResult.setCloseView(true);
                            onReviewErrors(entityActionResult);
                            return;
                        }
                    }

                    applet.applyUserAction(actionName);
                    hintUser("$m{reviewworkitemsapplet.apply.success.hint}");
                }
            }

            setCloseResultMapping();
            return;
        }

        super.performFormAction();
    }

    private T getLoadingApplet() throws UnifyException {
        return getValue(getAppletType());
    }
    
    protected abstract Class<T> getAppletType();
}

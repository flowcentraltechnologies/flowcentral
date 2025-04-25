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
package com.flowcentraltech.flowcentral.workflow.web.panels.applet;

import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractEntitySingleFormAppletPanel;
import com.flowcentraltech.flowcentral.common.business.policies.FormValidationContext;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.workflow.constants.WorkflowModuleSysParamConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;

/**
 * Review single form work items applet panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-reviewsingleformworkitemsappletpanel")
@UplBinding("web/workflow/upl/reviewsingleformworkitemsappletpanel.upl")
public class ReviewSingleFormWorkItemsAppletPanel extends AbstractEntitySingleFormAppletPanel {

    @Override
    public void onPageConstruct() throws UnifyException {
        super.onPageConstruct();
        setVisible("entitySearchPanel.reportBtn", false);
    }

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        ReviewSingleFormWorkItemsApplet applet = getReviewWorkItemsApplet();
        final boolean userActionRight = applet.isUserActionRight();
        final boolean editable = applet.appletCtx().isContextEditable();
        final boolean update = userActionRight && editable && system().getSysParameterValue(boolean.class,
                WorkflowModuleSysParamConstants.WF_ENABLE_UPDATES_ON_REVIEW);
        setVisible("frmActionBtns", userActionRight);
        setVisible("updateBtn", update);
        setVisible("updateCloseBtn", update);
        setVisible("deleteBtn", false);
        setVisible("listFrmActionBtns", userActionRight);

        final ReviewSingleFormWorkItemsApplet.ViewMode viewMode = applet.getMode();
        switch (viewMode) {
            case MAINTAIN_FORM_SCROLL:
            case MAINTAIN_FORM:
            case NEW_FORM:
                break;
            case SEARCH:
                switchContent("entitySearchPanel");
            default:
                break;
        }
    }

    @Action
    public void performUserAction() throws UnifyException {
        final String actionName = getRequestTarget(String.class);
        final ReviewSingleFormWorkItemsApplet applet = getReviewWorkItemsApplet();
        final FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(EvaluationMode.UPDATE, actionName),
                applet.isNewCommentRequired(actionName));
        if (!ctx.isWithFormErrors()) {
            applet.updateInst();
            applet.applyUserAction(actionName);
            hintUser("$m{reviewsingleformworkitemsapplet.apply.success.hint}");
        }
    }

    protected ReviewSingleFormWorkItemsApplet getReviewWorkItemsApplet() throws UnifyException {
        return getValue(ReviewSingleFormWorkItemsApplet.class);
    }
}

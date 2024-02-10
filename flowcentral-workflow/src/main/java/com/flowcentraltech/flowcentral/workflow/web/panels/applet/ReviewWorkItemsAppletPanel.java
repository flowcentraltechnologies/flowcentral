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
package com.flowcentraltech.flowcentral.workflow.web.panels.applet;

import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractEntityFormAppletPanel;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.workflow.constants.WorkflowModuleSysParamConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;

/**
 * Review work items applet panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-reviewworkitemsappletpanel")
@UplBinding("web/workflow/upl/reviewworkitemsappletpanel.upl")
public class ReviewWorkItemsAppletPanel extends AbstractEntityFormAppletPanel {

    @Override
    public void onPageConstruct() throws UnifyException {
        super.onPageConstruct();
        setVisible("entitySearchPanel.reportBtn", false);
    }

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        ReviewWorkItemsApplet applet = getReviewWorkItemsApplet();
        final boolean userActionRight = applet.isUserActionRight();
        final boolean editable = applet.getCtx().isContextEditable();
        final boolean update = userActionRight && editable && system().getSysParameterValue(boolean.class,
                WorkflowModuleSysParamConstants.WF_ENABLE_UPDATES_ON_REVIEW);
        setVisible("frmActionBtns", userActionRight);
        setVisible("updateBtn", update);
        setVisible("updateCloseBtn", false);
        setVisible("deleteBtn", false);
        setVisible("listFrmActionBtns", userActionRight);

        final ReviewWorkItemsApplet.ViewMode viewMode = applet.getMode();
        switch (viewMode) {
            case ENTITY_CRUD_PAGE:
            case ENTRY_TABLE_PAGE:
            case ASSIGNMENT_PAGE:
            case PROPERTYLIST_PAGE:
            case LISTING_FORM:
            case MAINTAIN_FORM_SCROLL:
            case MAINTAIN_CHILDLIST_FORM_NO_SCROLL:
            case MAINTAIN_RELATEDLIST_FORM_NO_SCROLL:
            case MAINTAIN_HEADLESSLIST_FORM_NO_SCROLL:
            case MAINTAIN_FORM:
            case MAINTAIN_CHILDLIST_FORM:
            case MAINTAIN_RELATEDLIST_FORM:
            case MAINTAIN_HEADLESSLIST_FORM:
            case NEW_FORM:
            case NEW_CHILD_FORM:
            case NEW_CHILDLIST_FORM:
            case NEW_RELATEDLIST_FORM:
            case NEW_HEADLESSLIST_FORM:
            case HEADLESS_TAB:
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
        final ReviewWorkItemsApplet applet = getReviewWorkItemsApplet();
        final FormContext ctx = evaluateCurrentFormContext(EvaluationMode.UPDATE,
                applet.isNewCommentRequired(actionName));
        if (!ctx.isWithFormErrors()) {
            if (ctx.getFormDef().isInputForm()) {
                EntityActionResult entityActionResult = applet.updateInstAndClose();
                if (ctx.isWithReviewErrors() && applet.isFormReview(actionName)) {
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

    protected ReviewWorkItemsApplet getReviewWorkItemsApplet() throws UnifyException {
        return getValue(ReviewWorkItemsApplet.class);
    }
}

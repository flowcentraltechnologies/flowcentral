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
package com.flowcentraltech.flowcentral.application.web.panels.applet;

import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;

/**
 * Manage loading list applet panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-manageloadinglistappletpanel")
@UplBinding("web/application/upl/manageloadinglistappletpanel.upl")
public class ManageLoadingListAppletPanel extends AbstractEntityFormAppletPanel {

    @Override
    @Action
    public void performFormAction() throws UnifyException {
        super.performFormAction();
        final String actionName = getRequestTarget(String.class);
        final ManageLoadingListApplet applet = getManageLoadingListApplet();
        final FormContext ctx = evaluateCurrentFormContext(EvaluationMode.UPDATE,
                false);
        if (!ctx.isWithFormErrors()) {
            if (ctx.getFormDef().isInputForm()) {
                EntityActionResult entityActionResult = applet.updateInstAndClose();
                if (ctx.isWithReviewErrors()/* && applet.isFormReview(actionName)*/) {
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

    @Override
    public void switchState() throws UnifyException {
        super.switchState();
        setVisible("listPrevBtn", false);
        setVisible("listNextBtn", false);

        final ManageLoadingListApplet applet = getManageLoadingListApplet();
//        applet.ensureRootAppletStruct();
        final AbstractEntityFormApplet.ViewMode viewMode = applet.getMode();
        switch (viewMode) {
            case ENTITY_CRUD_PAGE:
            case ENTRY_TABLE_PAGE:
            case ASSIGNMENT_PAGE:
            case PROPERTYLIST_PAGE:
            case LISTING_FORM:
            case MAINTAIN_FORM_SCROLL:
            case MAINTAIN_PRIMARY_FORM_NO_SCROLL:
            case MAINTAIN_CHILDLIST_FORM_NO_SCROLL:
            case MAINTAIN_RELATEDLIST_FORM_NO_SCROLL:
            case MAINTAIN_HEADLESSLIST_FORM_NO_SCROLL:
            case MAINTAIN_FORM:
            case MAINTAIN_CHILDLIST_FORM:
            case MAINTAIN_RELATEDLIST_FORM:
            case MAINTAIN_HEADLESSLIST_FORM:
            case NEW_FORM:
            case NEW_PRIMARY_FORM:
            case NEW_CHILD_FORM:
            case NEW_CHILDLIST_FORM:
            case NEW_RELATEDLIST_FORM:
            case NEW_HEADLESSLIST_FORM:
            case HEADLESS_TAB:
                break;
            case SEARCH:
                switchContent("loadingSearchPanel");
            default:
                break;
        }
    }

    private ManageLoadingListApplet getManageLoadingListApplet() throws UnifyException {
        return getValue(ManageLoadingListApplet.class);
    }
}

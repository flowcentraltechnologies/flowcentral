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

import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractEntityFormAppletPanel;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;

/**
 * Review wizard work items applet panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-reviewwizardworkitemsappletpanel")
@UplBinding("web/workflow/upl/reviewwizardworkitemsappletpanel.upl")
public class ReviewWizardWorkItemsAppletPanel extends AbstractEntityFormAppletPanel {

    @Override
    public void onPageConstruct() throws UnifyException {
        super.onPageConstruct();
        setVisible("entitySearchPanel.reportBtn", false);
    }

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        ReviewWizardWorkItemsApplet applet = getReviewWizardWorkItemsApplet();
        final ReviewWorkItemsApplet.ViewMode viewMode = applet.getMode();
        final int stepIndex = applet.getWfWizardStepIndex();
        final boolean isLastStep = stepIndex >= (applet.getWfWizardStepCount() - 1);

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
            case MAINTAIN_CHILDLIST_FORM:
            case MAINTAIN_RELATEDLIST_FORM:
            case MAINTAIN_HEADLESSLIST_FORM:
            case NEW_CHILD_FORM:
            case NEW_CHILDLIST_FORM:
            case NEW_RELATEDLIST_FORM:
            case NEW_HEADLESSLIST_FORM: {
                setVisible("prevStepBtn", false);
                setVisible("nextStepBtn", false);
            }
                break;
            case MAINTAIN_FORM:
                setVisible("saveBtn", true);
                setVisible("submitCloseBtn", isLastStep);
            case NEW_FORM: {
                setVisible("prevStepBtn", true);
                setVisible("nextStepBtn", true);
                setDisabled("prevStepBtn", stepIndex == 0);
                setDisabled("nextStepBtn", isLastStep);
            }
                break;
            case HEADLESS_TAB:
                switchContent("headlessTabsFormPanel");
                break;
            case SEARCH:
                switchContent("entitySearchPanel");
            default:
                break;
        }
    }

    @Override
    @Action
    public void save() throws UnifyException {
        ReviewWizardWorkItemsApplet applet = getReviewWizardWorkItemsApplet();
        if (ReviewWorkItemsApplet.ViewMode.NEW_FORM.equals(applet.getMode())) {
            super.save();
        } else {
            update();
        }
    }

    @Override
    @Action
    public void submit() throws UnifyException {
        ReviewWizardWorkItemsApplet applet = getReviewWizardWorkItemsApplet();
        if (applet.getForm().getCtx().getAttribute(String.class,
                AppletPropertyConstants.CREATE_FORM_SUBMIT_WORKFLOW_CHANNEL) != null) {
            super.submit();
        } else {
            applet.graduateWfWizardItem();
        }

        applet.navBackToSearch();
    }

    @Override
    @Action
    public void navBackToPrevious() throws UnifyException {
        ReviewWizardWorkItemsApplet applet = getReviewWizardWorkItemsApplet();
        if (applet.getMode().isRootForm() && applet.getWfWizardStepIndex() > 0) {
            previousStep();
            return;
        }

        super.navBackToPrevious();
    }

    @Action
    public void previousStep() throws UnifyException {
        getReviewWizardWorkItemsApplet().previousStep();
    }

    @Action
    public void nextStep() throws UnifyException {
        ReviewWizardWorkItemsApplet applet = getReviewWizardWorkItemsApplet();
        if (ReviewWorkItemsApplet.ViewMode.NEW_FORM.equals(applet.getMode())) {
            save();
        } else {
            update();
        }

        if (!applet.getForm().getCtx().isWithFormErrors()) {
            clearHintUser();
            applet.nextStep();
        }
    }

    protected ReviewWizardWorkItemsApplet getReviewWizardWorkItemsApplet() throws UnifyException {
        return getValue(ReviewWizardWorkItemsApplet.class);
    }
}

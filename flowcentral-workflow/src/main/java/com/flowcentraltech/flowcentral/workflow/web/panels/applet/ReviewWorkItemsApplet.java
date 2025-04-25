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

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.Comments;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.data.EntityItem;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySearch;
import com.flowcentraltech.flowcentral.application.web.panels.HeaderWithTabsForm;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.TableActionResult;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.constants.WfAppletPropertyConstants;
import com.flowcentraltech.flowcentral.workflow.constants.WfReviewMode;
import com.flowcentraltech.flowcentral.workflow.data.WfDef;
import com.flowcentraltech.flowcentral.workflow.data.WfStepDef;
import com.flowcentraltech.flowcentral.workflow.data.WorkEntityItem;
import com.flowcentraltech.flowcentral.workflow.entities.WfItem;
import com.flowcentraltech.flowcentral.workflow.util.WorkflowEntityUtils;
import com.tcdng.unify.common.database.WorkEntity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.RequirementType;
import com.tcdng.unify.core.criterion.AndBuilder;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Review work items applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ReviewWorkItemsApplet extends AbstractReviewWorkItemsApplet {

    private final WorkflowModuleService wms;

    private final AppletDef instAppletDef;

    private final WfStepDef wfStepDef;

    private WfItem currWfItem;

    private WorkEntity currEntityInst;

    private boolean userActionRight;

    public ReviewWorkItemsApplet(Page page, AppletUtilities au, WorkflowModuleService wms, List<String> pathVariables, String userLoginId,
            AppletWidgetReferences appletWidgetReferences, EntityFormEventHandlers formEventHandlers)
            throws UnifyException {
        super(page, au, pathVariables, appletWidgetReferences, formEventHandlers);
        this.wms = wms;
        AppletDef _appletDef = getRootAppletDef();
        entitySearch = au.constructEntitySearch(new FormContext(appletCtx()), this, null,
                getRootAppletDef().getDescription(), _appletDef, null,
                EntitySearch.ENABLE_ALL & ~(EntitySearch.SHOW_NEW_BUTTON | EntitySearch.SHOW_EDIT_BUTTON), false,
                false);
        final String originApplicationName = _appletDef.getOriginApplicationName();
        final String workflowName = _appletDef.getPropValue(String.class, WfAppletPropertyConstants.WORKFLOW);
        final String wfStepName = _appletDef.getPropValue(String.class, WfAppletPropertyConstants.WORKFLOW_STEP);
        final String appletName = _appletDef.getPropValue(String.class, WfAppletPropertyConstants.WORKFLOW_STEP_APPLET);
        AndBuilder ab = (AndBuilder) new AndBuilder().equals("applicationName", originApplicationName)
                .equals("workflowName", workflowName).equals("wfStepName", wfStepName);
        entitySearch.setBasicSearchOnly(true);
        entitySearch.setBaseRestriction(ab.build(), au.specialParamProvider());
        entitySearch.getEntityTable().setLimitSelectToColumns(false);
        instAppletDef = au.getAppletDef(appletName);
        wfStepDef = wms.getWfDef(workflowName).getWfStepDef(wfStepName);
        setAltSubCaption(wfStepDef.getLabel());
        navBackToSearch();
    }

    @Override
    public TableActionResult maintainInst(int mIndex) throws UnifyException {
        this.mIndex = mIndex;
        EntityItem entityItem = getEntitySearchItem(entitySearch, mIndex);
        final AppletDef _currentFormAppletDef = getFormAppletDef();
        FormDef formDef = getPreferredForm(PreferredFormType.ALL, _currentFormAppletDef, currEntityInst,
                FormMode.MAINTAIN.formProperty());
        ValueStoreReader reader = new BeanValueStore(currEntityInst).getReader();
        WfDef wfDef = wms.getWfDef(currWfItem.getWorkflowName());
        final boolean comments = WorkflowEntityUtils.isWorkflowConditionMatched(au(), reader, wfDef,
                wfStepDef.getComments());
        appletCtx().setRecovery(wfStepDef.isError());
        appletCtx().setComments(comments);
        if (formDef.isInputForm()) {
            if (form == null) {
                form = constructForm(formDef, currEntityInst, FormMode.MAINTAIN, null, false);
                currEntityInst = (WorkEntity) form.getFormBean();
                form.setFormTitle(getRootAppletDef().getLabel());
                form.setFormActionDefList(wfStepDef.getFormActionDefList());
            } else {
                updateForm(HeaderWithTabsForm.UpdateType.MAINTAIN_INST, form, currEntityInst);
            }

            form.setAppendables(entityItem);

            // Check if enter read-only mode
            appletCtx().setReadOnly(!userActionRight || wfStepDef.isError());
            if (userActionRight) {
                final boolean readOnly = WorkflowEntityUtils.isWorkflowConditionMatched(au(), reader, wfDef,
                        wfStepDef.getReadOnlyConditionName());
                appletCtx().setReadOnly(readOnly);
            }

            setDisplayModeMessage(form);
            viewMode = ViewMode.MAINTAIN_FORM_SCROLL;
        } else { // Listing
            listingForm = constructListingForm(formDef, currEntityInst);
            listingForm.setFormTitle(getRootAppletDef().getLabel());
            listingForm.setFormActionDefList(wfStepDef.getFormActionDefList());
            listingForm.setAppendables(entityItem);
            final boolean emails = WorkflowEntityUtils.isWorkflowConditionMatched(au(), reader, wfDef,
                    wfStepDef.getEmails());
            appletCtx().setEmails(emails);
            appletCtx().setReadOnly(!userActionRight || wfStepDef.isError());
            setDisplayModeMessage(listingForm);
            viewMode = ViewMode.LISTING_FORM;
        }

        return null;
    }

    @Override
    public boolean navBackToPrevious() throws UnifyException {
        boolean success = super.navBackToPrevious();
        if (isRootForm()) {
            currEntityInst = (WorkEntity) form.getFormBean();
        }

        return success;
    }

    @Override
    public EntityActionResult updateInstAndClose() throws UnifyException {
        EntityActionResult entityActionResult = super.updateInstAndClose();
        if (isRootForm()) {
            currEntityInst = (WorkEntity) form.getFormBean();
        }

        return entityActionResult;
    }

    @Override
    public void applyUserAction(String actionName) throws UnifyException {
        String comment = getNewComment();
        AbstractForm _form = getResolvedForm();
        wms.applyUserAction(currEntityInst, currWfItem.getId(), wfStepDef.getName(), actionName, comment,
                _form.getEmails(), WfReviewMode.NORMAL, _form.isListing());
        navBackToSearch();
    }

    @Override
    protected EntityItem getEntitySearchItem(EntitySearch entitySearch, int index) throws UnifyException {
        if (isNoForm()) {
            currWfItem = (WfItem) entitySearch.getEntityTable().getDispItemList().get(mIndex);
            WorkEntityItem _workEntityItem = wms.getWfItemWorkEntityFromWorkItemId(currWfItem.getId(), WfReviewMode.NORMAL);
            currEntityInst = _workEntityItem.getWorkEntity();

            final String currentUser = au().getSessionUserLoginId();
            if (StringUtils.isBlank(currWfItem.getHeldBy())) { // Current user should hold current item if it is unheld
                currWfItem.setHeldBy(currentUser);
                au().environment().updateByIdVersion(currWfItem);
            }

            userActionRight = currentUser != null && currentUser.equals(currWfItem.getHeldBy());
            return _workEntityItem;
        }

        return super.getEntitySearchItem(entitySearch, index);
    }

    public boolean isFormReview(String actionName) throws UnifyException {
        return wfStepDef.getUserActionDef(actionName).isFormReview();
    }

    public boolean isNewCommentRequired(String actionName) throws UnifyException { 
        RequirementType commentRequirementType = wfStepDef.getUserActionDef(actionName).getCommentRequirement();
        return RequirementType.MANDATORY.equals(commentRequirementType);
    }

    public boolean isUserActionRight() {
        return userActionRight;
    }

    @Override
    protected AppletDef getAlternateFormAppletDef() throws UnifyException {
        return instAppletDef;
    }

    private String getNewComment() throws UnifyException {
        Comments comments = viewMode == ViewMode.LISTING_FORM ? listingForm.getComments() : form.getComments();
        return comments != null ? comments.getNewComment() : null;
    }

    private void setDisplayModeMessage(AbstractForm form) throws UnifyException {
        if (userActionRight) {
            form.setWarning(null);
        } else {
            form.setWarning(
                    au().resolveSessionMessage("$m{entityformapplet.form.inworkflow.heldby}", currWfItem.getHeldBy()));
        }
    }
}

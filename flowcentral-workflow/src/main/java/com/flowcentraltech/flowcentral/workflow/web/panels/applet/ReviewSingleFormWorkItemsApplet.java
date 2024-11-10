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

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.Comments;
import com.flowcentraltech.flowcentral.application.data.EntityItem;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySearch;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySingleForm;
import com.flowcentraltech.flowcentral.common.business.policies.TableActionResult;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.constants.WfAppletPropertyConstants;
import com.flowcentraltech.flowcentral.workflow.constants.WfReviewMode;
import com.flowcentraltech.flowcentral.workflow.data.WfDef;
import com.flowcentraltech.flowcentral.workflow.data.WfStepDef;
import com.flowcentraltech.flowcentral.workflow.data.WorkEntityItem;
import com.flowcentraltech.flowcentral.workflow.entities.WfItem;
import com.flowcentraltech.flowcentral.workflow.util.WorkflowEntityUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.RequirementType;
import com.tcdng.unify.core.criterion.AndBuilder;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Review single form work items applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ReviewSingleFormWorkItemsApplet extends AbstractReviewSingleFormWorkItemsApplet {

    private final WorkflowModuleService wms;

    private final WfStepDef wfStepDef;

    private WfItem currWfItem;

    private WorkEntity currEntityInst;

    private boolean userActionRight;

    public ReviewSingleFormWorkItemsApplet(Page page, AppletUtilities au, WorkflowModuleService wms, List<String> pathVariables,
            String userLoginId) throws UnifyException {
        super(page, au, pathVariables);
        this.wms = wms;
        AppletDef _appletDef = getRootAppletDef();
        entitySearch = au.constructEntitySearch(new FormContext(appletCtx()), null, null,
                getRootAppletDef().getDescription(), _appletDef, null,
                EntitySearch.ENABLE_ALL & ~(EntitySearch.SHOW_NEW_BUTTON | EntitySearch.SHOW_EDIT_BUTTON), false,
                false);
        final String originApplicationName = _appletDef.getOriginApplicationName();
        final String workflowName = _appletDef.getPropValue(String.class, WfAppletPropertyConstants.WORKFLOW);
        final String wfStepName = _appletDef.getPropValue(String.class, WfAppletPropertyConstants.WORKFLOW_STEP);
        final String appletName = _appletDef.getPropValue(String.class, WfAppletPropertyConstants.WORKFLOW_STEP_APPLET);
        AndBuilder ab = (AndBuilder) new AndBuilder().equals("applicationName", originApplicationName)
                .equals("workflowName", workflowName).equals("wfStepName", wfStepName);
        entitySearch.setBaseRestriction(ab.build(), au.specialParamProvider());
        entitySearch.getEntityTable().setLimitSelectToColumns(false);
        singleFormAppletDef = au.getAppletDef(appletName);
        wfStepDef = wms.getWfDef(workflowName).getWfStepDef(wfStepName);
        setAltSubCaption(wfStepDef.getLabel());
        navBackToSearch();
    }

    @Override
    public TableActionResult maintainInst(int mIndex) throws UnifyException {
        this.mIndex = mIndex;
        EntityItem entityItem = getEntitySearchItem(entitySearch, mIndex);
        ValueStoreReader reader = new BeanValueStore(currEntityInst).getReader();
        WfDef wfDef = wms.getWfDef(currWfItem.getWorkflowName());
        final boolean emails = WorkflowEntityUtils.isWorkflowConditionMatched(au(), reader, wfDef, wfStepDef.getEmails());
        final boolean comments = WorkflowEntityUtils.isWorkflowConditionMatched(au(), reader, wfDef,
                wfStepDef.getComments());
        appletCtx().setRecovery(wfStepDef.isError());
        appletCtx().setEmails(emails);
        appletCtx().setComments(comments);
        if (form == null) {
            form = constructSingleForm(currEntityInst, FormMode.MAINTAIN);
            form.setFormTitle(getRootAppletDef().getLabel());
            form.setFormActionDefList(wfStepDef.getFormActionDefList());
        } else {
            updateSingleForm(EntitySingleForm.UpdateType.MAINTAIN_INST, form, currEntityInst);
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
        return null;
    }

    @Override
    protected EntityItem getEntitySearchItem(EntitySearch entitySearch, int index) throws UnifyException {
        if (isNoForm()) {
            currWfItem = (WfItem) entitySearch.getEntityTable().getDispItemList().get(mIndex);
            WorkEntityItem _workEntityItem = wms.getWfItemWorkEntityFromWorkItemId(currWfItem.getId(), WfReviewMode.SINGLEFORM);
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

    public void applyUserAction(String actionName) throws UnifyException {
        String comment = getNewComment();
        wms.applyUserAction(currEntityInst, currWfItem.getId(), wfStepDef.getName(), actionName, comment,
                form.getEmails(), WfReviewMode.SINGLEFORM, form.isListing());
        navBackToSearch();
    }

    public boolean isNewCommentRequired(String actionName) throws UnifyException {
        RequirementType commentRequirementType = wfStepDef.getUserActionDef(actionName).getCommentRequirement();
        return RequirementType.MANDATORY.equals(commentRequirementType);
    }

    public boolean isUserActionRight() {
        return userActionRight;
    }

    private String getNewComment() throws UnifyException {
        Comments comments = form.getComments();
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

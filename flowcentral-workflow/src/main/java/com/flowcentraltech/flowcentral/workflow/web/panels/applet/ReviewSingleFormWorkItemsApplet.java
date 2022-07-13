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
package com.flowcentraltech.flowcentral.workflow.web.panels.applet;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySearch;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySingleForm;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractEntitySingleFormApplet;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.constants.WfAppletPropertyConstants;
import com.flowcentraltech.flowcentral.workflow.constants.WfReviewMode;
import com.flowcentraltech.flowcentral.workflow.data.WfDef;
import com.flowcentraltech.flowcentral.workflow.data.WfStepDef;
import com.flowcentraltech.flowcentral.workflow.entities.WfItem;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.AndBuilder;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Review single form work items applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ReviewSingleFormWorkItemsApplet extends AbstractEntitySingleFormApplet {

    private final WorkflowModuleService wms;

    private final WfStepDef wfStepDef;

    private WfItem currWfItem;

    private WorkEntity currEntityInst;

    private boolean userActionRight;

    public ReviewSingleFormWorkItemsApplet(AppletUtilities au, WorkflowModuleService wms, String pathVariable, String userLoginId)
            throws UnifyException {
        super(au, pathVariable);
        this.wms = wms;
        AppletDef _appletDef = getRootAppletDef();
        entitySearch = au.constructEntitySearch(new FormContext(getCtx()), null, null,
                getRootAppletDef().getDescription(), _appletDef, null,
                EntitySearch.ENABLE_ALL & ~(EntitySearch.SHOW_NEW_BUTTON | EntitySearch.SHOW_EDIT_BUTTON));
        final String originApplicationName = _appletDef.getOriginApplicationName();
        final String workflowName = _appletDef.getPropValue(String.class, WfAppletPropertyConstants.WORKFLOW);
        final String wfStepName = _appletDef.getPropValue(String.class, WfAppletPropertyConstants.WORKFLOW_STEP);
        final String appletName = _appletDef.getPropValue(String.class, WfAppletPropertyConstants.WORKFLOW_STEP_APPLET);
        AndBuilder ab = (AndBuilder) new AndBuilder().equals("applicationName", originApplicationName)
                .equals("workflowName", workflowName).equals("wfStepName", wfStepName);
        entitySearch.setBaseRestriction(ab.build(), au.getSpecialParamProvider());
        entitySearch.getEntityTable().setLimitSelectToColumns(false);
        singleFormAppletDef = au.getAppletDef(appletName);
        wfStepDef = wms.getWfDef(workflowName).getWfStepDef(wfStepName);
        setAltSubCaption(wfStepDef.getLabel());
        navBackToSearch();
    }

    @Override
    public void maintainInst(int mIndex) throws UnifyException {
        this.mIndex = mIndex;
        getEntitySearchItem(entitySearch, mIndex);

        if (form == null) {
            form = constructForm(currEntityInst, FormMode.MAINTAIN);
            form.setFormTitle(getRootAppletDef().getLabel());
            form.setFormActionDefList(wfStepDef.getFormActionDefList());
        } else {
            updateForm(EntitySingleForm.UpdateType.MAINTAIN_INST, form, currEntityInst);
        }

        // Check if enter read-only mode
        if (wfStepDef.isWithReadOnlyCondition()) {
            WfDef wfDef = wms.getWfDef(currWfItem.getWorkflowName());
            boolean readOnly = wfStepDef.isReadOnlyAlways() || wfDef.getFilterDef(wfStepDef.getReadOnlyConditionName())
                    .getObjectFilter(wfDef.getEntityDef(), au.getSpecialParamProvider(), au.getNow())
                    .match(new BeanValueStore(currEntityInst));
            getCtx().setReadOnly(readOnly);
        }

        setDisplayModeMessage(form);
        viewMode = ViewMode.MAINTAIN_FORM_SCROLL;
        return;
    }

    @Override
    protected Entity getEntitySearchItem(EntitySearch entitySearch, int index) throws UnifyException {
        if (isNoForm()) {
            currWfItem = (WfItem) entitySearch.getEntityTable().getDispItemList().get(mIndex);
            currEntityInst = wms.getWfItemWorkEntity(currWfItem.getId(), WfReviewMode.SINGLEFORM);
            final String currentUser = au.getSessionUserLoginId();
            if (StringUtils.isBlank(currWfItem.getHeldBy())) { // Current user should hold current item if it is unheld
                currWfItem.setHeldBy(currentUser);
                au.environment().updateByIdVersion(currWfItem);
            }

            userActionRight = currentUser != null && currentUser.equals(currWfItem.getHeldBy());
            return currEntityInst;
        }

        return super.getEntitySearchItem(entitySearch, index);
    }

    public void applyUserAction(String actionName) throws UnifyException {
        wms.applyUserAction(currEntityInst, currWfItem.getId(), wfStepDef.getName(), actionName, WfReviewMode.SINGLEFORM);
        navBackToSearch();
    }

    public boolean isUserActionRight() {
        return userActionRight;
    }

    private void setDisplayModeMessage(AbstractForm form) throws UnifyException {
        if (userActionRight) {
            form.setWarning(null);
        } else {
            form.setWarning(au().resolveSessionMessage("$m{entityformapplet.form.inworkflow.heldby}",
                    currWfItem.getHeldBy()));
        }
    }
}

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
import java.util.Stack;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySearch;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractEntityFormApplet;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs.BreadCrumb;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.TableActionResult;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.flowcentraltech.flowcentral.configuration.constants.DefaultApplicationConstants;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.constants.WfWizardAppletPropertyConstants;
import com.flowcentraltech.flowcentral.workflow.constants.WorkflowModuleNameConstants;
import com.flowcentraltech.flowcentral.workflow.data.WfWizardDef;
import com.flowcentraltech.flowcentral.workflow.data.WfWizardStepDef;
import com.flowcentraltech.flowcentral.workflow.entities.WfWizardItem;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.AndBuilder;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.ReflectUtils;

/**
 * Review wizard work items applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ReviewWizardWorkItemsApplet extends AbstractEntityFormApplet {

    private final WfWizardDef wfWizardDef;

    private final EntityDef priEntityDef;

    private final Stack<BreadCrumb> breadCrumbStack;

    private final WorkflowModuleService wms;

    private WorkEntity priEntityInst;

    private Entity currEntityInst;

    private FormDef wfWizardStepFormDef;

    private int wfWizardStepIndex;

    public ReviewWizardWorkItemsApplet(AppletUtilities au, WorkflowModuleService wms, String pathVariable,
            String userLoginId, AppletWidgetReferences appletWidgetReferences,
            EntityFormEventHandlers formEventHandlers) throws UnifyException {
        super(au, pathVariable, appletWidgetReferences, formEventHandlers);
        AppletDef _appletDef = getRootAppletDef();
        this.entitySearch = au.constructEntitySearch(new FormContext(getCtx()), this, null,
                getRootAppletDef().getDescription(), _appletDef, null,
                EntitySearch.ENABLE_ALL & (~EntitySearch.SHOW_EDIT_BUTTON), false, false);
        final String wfWizardName = _appletDef.getPropValue(String.class,
                WfWizardAppletPropertyConstants.WORKFLOW_WIZARD);
        AndBuilder ab = (AndBuilder) new AndBuilder().equals("wizard", wfWizardName);
        if (!DefaultApplicationConstants.SYSTEM_LOGINID.equals(userLoginId)) {
            ab.equals("ownerId", userLoginId);
        }

        this.entitySearch.setBaseRestriction(ab.build(), au.specialParamProvider());
        this.wfWizardDef = wms.getWfWizardDef(wfWizardName);
        this.priEntityDef = au().getEntityDef(wfWizardDef.getEntity());
        this.wms = wms;
        this.breadCrumbStack = new Stack<BreadCrumb>();
        navBackToSearch();
    }

    @Override
    public TableActionResult newEntityInst() throws UnifyException {
        priEntityInst = null;
        loadWizardStep(0);
        return new TableActionResult(priEntityInst);
    }

    @SuppressWarnings("unchecked")
    @Override
    public TableActionResult maintainInst(int mIndex) throws UnifyException {
        this.mIndex = mIndex;
        WfWizardItem currWfWizardItem = (WfWizardItem) entitySearch.getEntityTable().getDispItemList().get(mIndex);
        final EntityClassDef _entityClassDef = au().getEntityClassDef(priEntityDef.getLongName());
        priEntityInst = (WorkEntity) au().environment().listLean(
                (Class<? extends Entity>) _entityClassDef.getEntityClass(), currWfWizardItem.getPrimaryEntityId());
        loadWizardStep(0);
        return null;
    }

    @Override
    public EntityActionResult submitInst() throws UnifyException {
        WorkEntity inst = au().environment().find(priEntityInst.getClass(), priEntityInst.getId());
        FormContext ctx = form.getCtx();
        EntityActionResult entityActionResult = au().workItemUtilities().submitToWorkflowChannel(
                form.getFormDef().getEntityDef(),
                ctx.getAttribute(String.class, AppletPropertyConstants.CREATE_FORM_SUBMIT_WORKFLOW_CHANNEL),
                (WorkEntity) inst, ctx.getAttribute(String.class, AppletPropertyConstants.CREATE_FORM_SUBMIT_POLICY));
        navBackToPrevious();
        return entityActionResult;
    }

    @Override
    public boolean navBackToSearch() throws UnifyException {
        boolean success = super.navBackToSearch();

        priEntityInst = null;
        currEntityInst = null;
        wfWizardStepFormDef = null;
        breadCrumbStack.clear();
        return success;
    }

    public void graduateWfWizardItem() throws UnifyException {
        wms.graduateWfWizardItem(wfWizardDef.getLongName(), (Long) priEntityInst.getId());
    }

    public void previousStep() throws UnifyException {
        BreadCrumb bc = breadCrumbStack.pop();
        try {
            loadWizardStep(wfWizardStepIndex - 1);
        } catch (UnifyException e) {
            breadCrumbStack.push(bc);
            throw e;
        }
    }

    public void nextStep() throws UnifyException {
        breadCrumbStack.push(form.getBreadCrumbs().getLastBreadCrumb());
        try {
            loadWizardStep(wfWizardStepIndex + 1);
        } catch (UnifyException e) {
            breadCrumbStack.pop();
            throw e;
        }
    }

    public int getWfWizardStepIndex() {
        return wfWizardStepIndex;
    }

    public int getWfWizardStepCount() {
        return wfWizardDef.stepCount();
    }

    @Override
    protected List<BreadCrumb> getBaseFormBreadCrumbs() {
        return breadCrumbStack;
    }

    @Override
    protected AppletDef getAlternateFormAppletDef() throws UnifyException {
        return null;
    }

    @SuppressWarnings("unchecked")
    private void loadWizardStep(int index) throws UnifyException {
        WfWizardStepDef _wfWizardStepDef = wfWizardDef.getWfWizardStepDef(index);
        wfWizardStepFormDef = au().getFormDef(_wfWizardStepDef.getForm());
        EntityDef _entityDef = priEntityDef;
        FormMode formMode = FormMode.MAINTAIN;
        viewMode = ViewMode.MAINTAIN_FORM;
        if (index == 0) {
            currEntityInst = priEntityInst;
        } else {
            _entityDef = wfWizardStepFormDef.getEntityDef();
            Restriction childRestriction = au().getChildRestriction(priEntityDef, _wfWizardStepDef.getReference(),
                    priEntityInst);
            final EntityClassDef _entityClassDef = au().getEntityClassDef(_entityDef.getLongName());
            currEntityInst = au().environment().listLean(Query
                    .of((Class<? extends Entity>) _entityClassDef.getEntityClass()).addRestriction(childRestriction));
        }

        if (currEntityInst == null) {
            final EntityClassDef _entityClassDef = au().getEntityClassDef(_entityDef.getLongName());
            currEntityInst = (Entity) ReflectUtils.newInstance(_entityClassDef.getEntityClass());
            if (index == 0) {
                priEntityInst = (WorkEntity) currEntityInst;
            } else {
                au().populateNewChildReferenceFields(priEntityDef, _wfWizardStepDef.getReference(), priEntityInst,
                        currEntityInst);
            }

            formMode = FormMode.CREATE;
            viewMode = ViewMode.NEW_FORM;
        }

//        form = constructForm(wfWizardStepFormDef, currEntityInst, formMode, null, false);
        form = constructForm(currEntityInst, formMode, null, false);
        currEntityInst = (Entity) form.getFormBean();
        form.setFormTitle(_wfWizardStepDef.getLabel());
        form.setFormStepIndex(index + 1);
        wfWizardStepIndex = index;

        FormContext ctx = form.getCtx();
        ctx.setAttribute(AppletPropertyConstants.CREATE_FORM_NEW_POLICY,
                WorkflowModuleNameConstants.REVIEW_WIZARD_CREATE_ACTION_POLICY);
        ctx.setAttribute(AppletPropertyConstants.MAINTAIN_FORM_UPDATE_POLICY,
                WorkflowModuleNameConstants.REVIEW_WIZARD_UPDATE_ACTION_POLICY);
        ctx.setAttribute(AppletPropertyConstants.CREATE_FORM_SUBMIT_WORKFLOW_CHANNEL, wfWizardDef.getSubmitWorkflow());
        ctx.setAttribute(AppletPropertyConstants.CREATE_FORM_SUBMIT_POLICY,
                WorkflowModuleNameConstants.REVIEW_WIZARD_SUBMIT_ACTION_POLICY);
        ctx.setAttribute(WfWizardAppletPropertyConstants.WIZARD_DEFINITION, wfWizardDef);
        ctx.setAttribute(WfWizardAppletPropertyConstants.WIZARD_STEP_INDEX, wfWizardStepIndex);
        ctx.setAttribute(WfWizardAppletPropertyConstants.WIZARD_PRIMARY_ENTITY_INST_ID, priEntityInst.getId());
    }
}

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

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.FormWizard;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.common.business.policies.ActionMode;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.FormReviewContext;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.configuration.constants.FormReviewType;
import com.flowcentraltech.flowcentral.configuration.constants.RecordActionType;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.common.database.WorkEntity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Database;
import com.tcdng.unify.core.system.entities.AbstractSequencedEntity;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * For wizard applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormWizardApplet extends AbstractApplet implements SweepingCommitPolicy {

    private FormWizard formWizard;
    
    private String closePath;
    
    public FormWizardApplet(Page page, AppletUtilities au, List<String> pathVariables) throws UnifyException {
        super(page, au, pathVariables.get(APPLET_NAME_INDEX));
        
        final AppletDef appletDef = getRootAppletDef();
        final String formName = appletDef.getPropValue(String.class, AppletPropertyConstants.CREATE_FORM);
        final FormDef formDef = au.getFormDef(formName);
        final Entity inst = au.application().getEntityClassDef(appletDef.getEntity()).newInst();
        BreadCrumbs.Builder bcb = BreadCrumbs.newBuilder();
        bcb.addHistoryCrumb(appletDef.getDescription(), formDef.getLabel(), 0);
        BreadCrumbs crumbs = bcb.build();

        this.formWizard = au.constructFormWizard(this, formDef, inst, appletDef.getDescription(), formDef.getLabel(), crumbs);
        setAltSubCaption(au.resolveSessionMessage("$m{formwizardpanel.creation}"));
    }
    
    @Override
    public void bumpAllParentVersions(Database db, RecordActionType type) throws UnifyException {
        
    }

    public EntityActionResult saveNewInst() throws UnifyException {
        return saveNewInst(ActionMode.ACTION_ONLY, new FormReviewContext(FormReviewType.ON_SAVE));
    }

    public EntityActionResult submitInst() throws UnifyException {
        return submitInst(ActionMode.ACTION_AND_CLOSE, new FormReviewContext(FormReviewType.ON_SUBMIT));
    }

    public FormWizard getFormWizard() {
        return formWizard;
    }

    public String getClosePath() {
        return closePath;
    }

    public void setClosePath(String closePath) {
        this.closePath = closePath;
    }

    private EntityActionResult saveNewInst(ActionMode actionMode, FormReviewContext rCtx) throws UnifyException {
        EntityActionResult entityActionResult = createInst();
        return entityActionResult;
    }

    private EntityActionResult createInst() throws UnifyException {
        final AppletDef _appletDef = getRootAppletDef();
        return au().createEntityInstByFormContext(_appletDef, formWizard.getFormContext(), this);
    }

    private EntityActionResult submitInst(ActionMode actionMode, FormReviewContext rCtx) throws UnifyException {
        final FormContext formContext = formWizard.getFormContext();
        final Entity inst = (Entity) formContext.getInst();
        final EntityDef _entityDef = formContext.getFormDef().getEntityDef();
        EntityActionResult entityActionResult = createInst();
        Long entityInstId = (Long) inst.getId();
        if (_entityDef.delegated()) {
            ((AbstractSequencedEntity) inst).setId(entityInstId);
        }

        final AppletDef appletDef = getRootAppletDef();
        final String channel = appletDef.getPropValue(String.class,
                AppletPropertyConstants.CREATE_FORM_SUBMIT_WORKFLOW_CHANNEL);
        final String policy = appletDef.getPropValue(String.class,
                AppletPropertyConstants.CREATE_FORM_SUBMIT_POLICY);

        entityActionResult = au().workItemUtilities().submitToWorkflowChannel(_entityDef, channel,
                (WorkEntity) inst, policy);
        return entityActionResult;
    }

}

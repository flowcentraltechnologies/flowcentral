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

package com.flowcentraltech.flowcentral.workflow.business.policies;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.common.business.policies.AbstractEntityActionPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.flowcentraltech.flowcentral.workflow.constants.WfWizardAppletPropertyConstants;
import com.flowcentraltech.flowcentral.workflow.constants.WorkflowModuleNameConstants;
import com.flowcentraltech.flowcentral.workflow.data.WfWizardDef;
import com.flowcentraltech.flowcentral.workflow.entities.WfWizardItem;
import com.flowcentraltech.flowcentral.workflow.entities.WfWizardItemQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.database.Entity;

/**
 * Review wizard create action policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(WorkflowModuleNameConstants.REVIEW_WIZARD_CREATE_ACTION_POLICY)
public class ReviewWizardCreateActionPolicy extends AbstractEntityActionPolicy {

    @Configurable
    private AppletUtilities appletUtil;

    public void setAppletUtil(AppletUtilities appletUtil) {
        this.appletUtil = appletUtil;
    }

    @Override
    public boolean checkAppliesTo(Entity inst) throws UnifyException {
        return true;
    }

    @Override
    protected void doExecutePreAction(EntityActionContext ctx) throws UnifyException {
        int wfWizardStepIndex = ctx.getAttribute(int.class, WfWizardAppletPropertyConstants.WIZARD_STEP_INDEX);
        if (wfWizardStepIndex == 0) {
            ((WorkEntity) ctx.getInst()).setInWorkflow(true);
        }
    }

    @Override
    protected EntityActionResult doExecutePostAction(EntityActionContext ctx) throws UnifyException {
        WfWizardDef wfWizardDef = ctx.getAttribute(WfWizardDef.class,
                WfWizardAppletPropertyConstants.WIZARD_DEFINITION);
        int wfWizardStepIndex = ctx.getAttribute(int.class, WfWizardAppletPropertyConstants.WIZARD_STEP_INDEX);
        int percentCompleted = wfWizardDef.stepCount() > 0 ? ((wfWizardStepIndex + 1) * 100) / wfWizardDef.stepCount()
                : 100;
        if (wfWizardStepIndex == 0) {
            WorkEntity entityInst = (WorkEntity) ctx.getInst();
            WfWizardItem wfWizardItem = new WfWizardItem();
            wfWizardItem.setWizard(wfWizardDef.getLongName());
            wfWizardItem.setPrimaryEntityId((Long) entityInst.getId());
            wfWizardItem.setTitle(entityInst.getDescription());
            wfWizardItem.setOwnerId(getUserToken().getUserLoginId());
            wfWizardItem.setPercentCompleted(percentCompleted);
            appletUtil.environment().create(wfWizardItem);
        } else {
            Long priEntityInstId = ctx.getAttribute(Long.class,
                    WfWizardAppletPropertyConstants.WIZARD_PRIMARY_ENTITY_INST_ID);
            WfWizardItem wfWizardItem = appletUtil.environment()
                    .find(new WfWizardItemQuery().wizard(wfWizardDef.getLongName()).primaryEntityId(priEntityInstId));
            wfWizardItem.setPercentCompleted(percentCompleted);
            appletUtil.environment().updateByIdVersion(wfWizardItem);
        }

        return null;
    }

}

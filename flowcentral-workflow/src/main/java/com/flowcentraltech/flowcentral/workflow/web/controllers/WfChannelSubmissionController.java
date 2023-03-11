/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.workflow.web.controllers;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.flowcentraltech.flowcentral.workflow.constants.WfChannelErrorConstants;
import com.flowcentraltech.flowcentral.workflow.data.WfChannelDef;
import com.flowcentraltech.flowcentral.workflow.web.data.WfChannelSubmission;
import com.flowcentraltech.flowcentral.workflow.web.data.WfChannelSubmissionResult;
import com.tcdng.unify.core.UnifyCoreErrorConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.web.AbstractPlainJsonController;

/**
 * Workflow channel submission controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/workflowchannel/submit")
public class WfChannelSubmissionController extends AbstractPlainJsonController {

    @Configurable
    private ApplicationModuleService applicationModuleService;

    @Configurable
    private WorkflowModuleService workflowModuleService;

    public void setApplicationModuleService(ApplicationModuleService applicationModuleService) {
        this.applicationModuleService = applicationModuleService;
    }

    public void setWorkflowModuleService(WorkflowModuleService workflowModuleService) {
        this.workflowModuleService = workflowModuleService;
    }

    @Override
    protected Object doExecute(String jsonRequest) throws UnifyException {
        int errorCode = WfChannelErrorConstants.NO_ERROR;
        String errorMsg = null;
        try {
            WfChannelSubmission wfChannelSubmission = getObjectFromRequestJson(WfChannelSubmission.class, jsonRequest);
            WfChannelDef wfChannelDef = workflowModuleService.getWfChannelDef(wfChannelSubmission.getChannel());
            EntityClassDef entityClassDef = applicationModuleService.getEntityClassDef(wfChannelDef.getEntity());
            List<WorkEntity> workEntityList = new ArrayList<WorkEntity>();
            for (String document : wfChannelSubmission.getDocuments()) {
                WorkEntity workEntity = (WorkEntity) getObjectFromRequestJson(entityClassDef.getEntityClass(),
                        document);
                applicationModuleService.resolveEntityReferences(workEntity, wfChannelDef.getEntity());
                workEntityList.add(workEntity);
            }

            workflowModuleService.submitToWorkflowByChannel(wfChannelSubmission.getChannel(),
                    wfChannelSubmission.getBranchCode(), wfChannelSubmission.getDepartmentCode(), workEntityList);
        } catch (UnifyException e) {
            errorCode = WfChannelErrorConstants.SYSTEM_EXCEPTION;
            errorMsg = getSessionMessage(e.getErrorCode(), e.getErrorParams());
            logError(e);
        } catch (Exception e) {
            errorCode = WfChannelErrorConstants.SYSTEM_EXCEPTION;
            errorMsg = getSessionMessage(UnifyCoreErrorConstants.COMPONENT_OPERATION_ERROR, getName(), e.getMessage());
            logError(e);
        }

        return new WfChannelSubmissionResult(errorCode, errorMsg);
    }

}

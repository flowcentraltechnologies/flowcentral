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
package com.flowcentraltech.flowcentral.workflow.business;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.UsageProvider;
import com.flowcentraltech.flowcentral.application.data.Usage;
import com.flowcentraltech.flowcentral.application.data.UsageType;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.workflow.constants.WorkflowModuleNameConstants;
import com.flowcentraltech.flowcentral.workflow.entities.WfChannel;
import com.flowcentraltech.flowcentral.workflow.entities.WfChannelQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WfStep;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepAlertQuery;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepQuery;
import com.flowcentraltech.flowcentral.workflow.entities.Workflow;
import com.flowcentraltech.flowcentral.workflow.entities.WorkflowQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;

/**
 * Default implementation of workflow usage service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(WorkflowModuleNameConstants.WORKFLOW_USAGE_SERVICE)
public class WorkflowUsageServiceImpl extends AbstractFlowCentralService implements UsageProvider {

    @Override
    public List<Usage> findApplicationUsagesByOtherApplications(String applicationName, UsageType usageType)
            throws UnifyException {
        final String applicationNameBase = applicationName + '.';
        List<Usage> usageList = new ArrayList<Usage>();
        if (UsageType.isQualifiesEntity(usageType)) {
            List<WfChannel> wfChannelList = environment()
                    .listAll(new WfChannelQuery().entityBeginsWith(applicationNameBase)
                            .applicationNameNot(applicationName).addSelect("applicationName", "name", "entity"));
            for (WfChannel wfChannel : wfChannelList) {
                Usage usage = new Usage(UsageType.ENTITY, "WfChannel",
                        wfChannel.getApplicationName() + "." + wfChannel.getName(), "entity", wfChannel.getEntity());
                usageList.add(usage);
            }

            List<Workflow> workflowList = environment()
                    .listAll(new WorkflowQuery().entityBeginsWith(applicationNameBase)
                            .applicationNameNot(applicationName).addSelect("applicationName", "name", "entity"));
            for (Workflow workflow : workflowList) {
                Usage usage = new Usage(UsageType.ENTITY, "Workflow",
                        workflow.getApplicationName() + "." + workflow.getName(), "entity", workflow.getEntity());
                usageList.add(usage);
            }

            List<WfStep> wfStepList = environment().listAll(
                    new WfStepQuery().applicationNameNot(applicationName).appletNameBeginsWith(applicationNameBase)
                            .addSelect("applicationName", "workflowName", "name", "appletName"));
            for (WfStep wfStep : wfStepList) {
                Usage usage = new Usage(UsageType.ENTITY, "WfStep",
                        wfStep.getApplicationName() + "." + wfStep.getWorkflowName() + "." + wfStep.getName(),
                        "appletName", wfStep.getAppletName());
                usageList.add(usage);
            }
        }

        return usageList;
    }

    @Override
    public long countApplicationUsagesByOtherApplications(String applicationName, UsageType usageType)
            throws UnifyException {
        final String applicationNameBase = applicationName + '.';
        long usages = 0L;
        if (UsageType.isQualifiesEntity(usageType)) {
            usages += environment().countAll(new WfChannelQuery().entityBeginsWith(applicationNameBase)
                    .applicationNameNot(applicationName).addSelect("applicationName", "name", "entity"));

            usages += environment().countAll(new WorkflowQuery().entityBeginsWith(applicationNameBase)
                    .applicationNameNot(applicationName).addSelect("applicationName", "name", "entity"));

            usages += environment().countAll(
                    new WfStepQuery().applicationNameNot(applicationName).appletNameBeginsWith(applicationNameBase)
                            .addSelect("applicationName", "workflowName", "name", "appletName"));

            usages += environment().countAll(
                    new WfStepAlertQuery().applicationNameNot(applicationName).templateBeginsWith(applicationNameBase)
                            .addSelect("applicationName", "workflowName", "wfStepName", "name", "template"));
        }

        return usages;
    }

    @Override
    public List<Usage> findEntityUsages(String entity, UsageType usageType) throws UnifyException {
        List<Usage> usageList = new ArrayList<Usage>();
        if (UsageType.isQualifiesEntity(usageType)) {
            List<WfChannel> wfChannelList = environment()
                    .listAll(new WfChannelQuery().entity(entity).addSelect("applicationName", "name", "entity"));
            for (WfChannel wfChannel : wfChannelList) {
                Usage usage = new Usage(UsageType.ENTITY, "WfChannel",
                        wfChannel.getApplicationName() + "." + wfChannel.getName(), "entity", wfChannel.getEntity());
                usageList.add(usage);
            }

            List<Workflow> workflowList = environment()
                    .listAll(new WorkflowQuery().entity(entity).addSelect("applicationName", "name", "entity"));
            for (Workflow workflow : workflowList) {
                Usage usage = new Usage(UsageType.ENTITY, "Workflow",
                        workflow.getApplicationName() + "." + workflow.getName(), "entity", workflow.getEntity());
                usageList.add(usage);
            }
        }

        return usageList;
    }

    @Override
    public long countEntityUsages(String entity, UsageType usageType) throws UnifyException {
        long usages = 0L;
        if (UsageType.isQualifiesEntity(usageType)) {
            usages += environment()
                    .countAll(new WfChannelQuery().entity(entity).addSelect("applicationName", "name", "entity"));

            usages += environment()
                    .countAll(new WorkflowQuery().entity(entity).addSelect("applicationName", "name", "entity"));
        }

        return usages;
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {

    }
}

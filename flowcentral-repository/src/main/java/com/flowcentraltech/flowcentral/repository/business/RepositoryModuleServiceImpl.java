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
package com.flowcentraltech.flowcentral.repository.business;

import java.util.Date;
import java.util.List;

import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.repository.constants.RepositoryModuleNameConstants;
import com.flowcentraltech.flowcentral.repository.constants.TransferToRemoteTaskConstants;
import com.flowcentraltech.flowcentral.repository.data.TransferToRemote;
import com.flowcentraltech.flowcentral.repository.entities.RemoteRepoBranch;
import com.flowcentraltech.flowcentral.repository.entities.RemoteRepoBranchQuery;
import com.flowcentraltech.flowcentral.repository.entities.RemoteRepoConfig;
import com.flowcentraltech.flowcentral.repository.entities.RemoteRepoConfigQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Taskable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.task.TaskExecLimit;
import com.tcdng.unify.core.task.TaskMonitor;

/**
 * Default Repository business service implementation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(RepositoryModuleNameConstants.REPOSITORY_MODULE_SERVICE)
public class RepositoryModuleServiceImpl extends AbstractFlowCentralService implements RepositoryModuleService {

    @Override
    public void clearDefinitionsCache() throws UnifyException {

    }

    @Override
    public List<RemoteRepoConfig> findRemoteRepoConfigs(RemoteRepoConfigQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Override
    public List<RemoteRepoBranch> findRemoteRepoBranches(RemoteRepoBranchQuery query) throws UnifyException {
        return environment().listAll(query);
    }

    @Taskable(name = TransferToRemoteTaskConstants.TRANSFER_TO_REMOTE_TASK_NAME,
            description = "Transfer file to Remote Repository Task",
            parameters = { @Parameter(name = TransferToRemoteTaskConstants.TRANSFER_ITEM,
                    description = "Transfer Item", type = TransferToRemote.class, mandatory = true) },
            limit = TaskExecLimit.ALLOW_MULTIPLE, schedulable = false)
    public int generateUtilitiesModuleFilesTask(TaskMonitor taskMonitor, TransferToRemote transferItem)
            throws UnifyException {
        Date now = environment().getNow();

        return 0;
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {

    }

}

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

import java.util.List;

import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.repository.constants.RepositoryModuleNameConstants;
import com.flowcentraltech.flowcentral.repository.constants.TransferToRemoteTaskConstants;
import com.flowcentraltech.flowcentral.repository.data.TransferToRemote;
import com.flowcentraltech.flowcentral.repository.entities.RemoteRepoConfig;
import com.flowcentraltech.flowcentral.repository.entities.RemoteRepoConfigQuery;
import com.flowcentraltech.flowcentral.repository.providers.RepositoryProvider;
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

    @Taskable(name = TransferToRemoteTaskConstants.TRANSFER_TO_REMOTE_TASK_NAME,
            description = "Transfer file to Remote Repository Task",
            parameters = { @Parameter(name = TransferToRemoteTaskConstants.TRANSFER_ITEM, description = "Transfer Item",
                    type = TransferToRemote.class, mandatory = true) },
            limit = TaskExecLimit.ALLOW_SINGLE, schedulable = false)
    public int transferFilesToRemoteTask(TaskMonitor taskMonitor, TransferToRemote transferItem)
            throws UnifyException {
        logDebug(taskMonitor, "Tranferring files to remote repository [{0}]...", transferItem.getRemoteName());
        RemoteRepoConfig remoteRepoConfig = environment()
                .find(new RemoteRepoConfigQuery().name(transferItem.getRemoteName()));

        logDebug(taskMonitor, "Successfully retrieved configuration...");
        RepositoryProvider repositoryProvider = getComponent(RepositoryProvider.class, remoteRepoConfig.getProvider());

        logDebug(taskMonitor, "Successfully obtained repository provider [{0}]...", remoteRepoConfig.getProvider());
        logDebug(taskMonitor, "Executing repository provider...");
        switch(transferItem.getType()) {
            case REPLACE_DIRECTORY_WITH_ZIP:
                repositoryProvider.replaceDirectory(taskMonitor, remoteRepoConfig.getRemoteUrl(), transferItem.getRemoteBranch(),
                        remoteRepoConfig.getUserName(), remoteRepoConfig.getPassword(), remoteRepoConfig.getLocalRepoPath(),
                        transferItem.getWorkingPath(), transferItem.getFile());
                break;
            case REPLACE_FILE_IN_DIRECTORY:
                repositoryProvider.replaceFile(taskMonitor, remoteRepoConfig.getRemoteUrl(), transferItem.getRemoteBranch(),
                        remoteRepoConfig.getUserName(), remoteRepoConfig.getPassword(), remoteRepoConfig.getLocalRepoPath(),
                        transferItem.getWorkingPath(), transferItem.getFileName(), transferItem.getFile());
                break;
            default:
                break;            
        }
        
        logDebug(taskMonitor, "Transfer to remote repository successfully completed.");
       return 0;
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {

    }

}

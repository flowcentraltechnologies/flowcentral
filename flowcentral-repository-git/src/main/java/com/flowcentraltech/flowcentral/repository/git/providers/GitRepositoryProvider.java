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
package com.flowcentraltech.flowcentral.repository.git.providers;

import java.io.File;

import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import com.flowcentraltech.flowcentral.repository.git.constants.GitRepositoryModuleNameConstants;
import com.flowcentraltech.flowcentral.repository.providers.AbstractRepositoryProvider;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.ZipUtils;

/**
 * Git repository provider implementation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(name = GitRepositoryModuleNameConstants.GIT_REPOSITORY_PROVIDER,
        description = "$m{repository.git.repository.provider}")
public class GitRepositoryProvider extends AbstractRepositoryProvider {

    @Override
    public void replaceDirectory(TaskMonitor taskMonitor, String repositoryUrl, String branch, String userName,
            String password, String localPath, String target, byte[] zippedFile) throws UnifyException {
        logDebug(taskMonitor, "Replacing all files in remote git repository [{0}] at branch [{1}] in target [{2}]...",
                repositoryUrl, branch, target);

        try {
            UsernamePasswordCredentialsProvider credentials = new UsernamePasswordCredentialsProvider(userName,
                    password);
            logDebug(taskMonitor, "Cloning git repository...");
            final Git git = Git.cloneRepository()
                    .setURI(repositoryUrl)
                    .setDirectory(new File(localPath))
                    .setCredentialsProvider(credentials)
                    .call();
            
            logDebug(taskMonitor, "Checking out branch [{0}]...",branch);
            checkoutBranch(taskMonitor, git, branch);
            
            final String targetPath = IOUtils.buildFilename(localPath, target);
            logDebug(taskMonitor, "Deleting target directory [{0}]...",targetPath);
            IOUtils.deleteDirectory(targetPath);
            
            logDebug(taskMonitor, "Writing new files to target directory...");
            final String parentPath = IOUtils.getParentDirectory(targetPath);
            ZipUtils.extractAll(parentPath, zippedFile);
            
            logDebug(taskMonitor, "Committing changes...");
            git.add()
              .addFilepattern(target)
              .call();
            git.commit()
              .setMessage("Files replaced in " + target)
              .call();
            
            logDebug(taskMonitor, "Pushing changes to remote repository...");
            git.push()
              .setCredentialsProvider(credentials)
              .call();
            git.close();            
        } catch (Exception e) {
            throwOperationErrorException(e);
        }

    }

    private Ref checkoutBranch(TaskMonitor taskMonitor, Git git, String branch) throws Exception {
        boolean exists = git.getRepository().getRefDatabase().findRef(branch) != null;
        if (!exists) {
            logDebug(taskMonitor, "Branch [{0}] does not exist. Creating branch...",branch);
            return git.checkout()
                    .setCreateBranch(true)
                    .setName(branch)
                    .setUpstreamMode(SetupUpstreamMode.TRACK)
                    .setStartPoint("origin/" + branch)
                    .call();
        }

        return git.checkout()
                .setName(branch)
                .call();
    }
    
}

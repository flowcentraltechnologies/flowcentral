/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.repository.providers;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.task.TaskMonitor;

/**
 * Repository provider component.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface RepositoryProvider extends FlowCentralComponent {

    /**
     * Replaces file in remote repository folder with supplied file.
     * 
     * @param taskMonitor
     *                      the task monitor
     * @param repositoryUrl
     *                      the remote repository URL
     * @param branch
     *                      the remote branch
     * @param userName
     *                      the user name
     * @param password
     *                      the password
     * @param localPath
     *                      the local path
     * @param targetPath
     *                      the target folder
     * @param fileName
     *                      the file name
     * @param file
     *                      file used to replace
     * @throws UnifyException
     *                        if an error occurs
     */
    void replaceFile(TaskMonitor taskMonitor, String repositoryUrl, String branch, String userName, String password,
            String localPath, String targetPath, String fileName, byte[] file) throws UnifyException;

    /**
     * Replaces directory in remote repository folder with contents of zipped file.
     * 
     * @param taskMonitor
     *                      the task monitor
     * @param repositoryUrl
     *                      the remote repository URL
     * @param branch
     *                      the remote branch
     * @param userName
     *                      the user name
     * @param password
     *                      the password
     * @param localPath
     *                      the local path
     * @param targetPath
     *                      the target folder
     * @param zippedFile
     *                      zip file containing files
     * @throws UnifyException
     *                        if an error occurs
     */
    void replaceDirectory(TaskMonitor taskMonitor, String repositoryUrl, String branch, String userName,
            String password, String localPath, String targetPath, byte[] zippedFile) throws UnifyException;
}

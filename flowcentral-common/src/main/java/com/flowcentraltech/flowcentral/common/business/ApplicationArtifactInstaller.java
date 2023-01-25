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

package com.flowcentraltech.flowcentral.common.business;

import com.flowcentraltech.flowcentral.configuration.data.ApplicationInstall;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.task.TaskMonitor;

/**
 * Application artifact installer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface ApplicationArtifactInstaller extends UnifyComponent {

    /**
     * Install application artifact.
     * 
     * @param taskMonitor
     *                           the task monitor
     * @param applicationInstall
     *                           the application install object
     * @throws UnifyException
     *                        if an error occurs
     */
    void installApplicationArtifacts(TaskMonitor taskMonitor, ApplicationInstall applicationInstall)
            throws UnifyException;
    
    /**
     * Deletes all application artifact.
     * 
     * @param taskMonitor
     *                      the task monitor
     * @param applicationId
     *                      the application ID
     * @return the deletion count
     * @throws UnifyException
     *                        if an error occurs
     */
    int deleteApplicationArtifacts(TaskMonitor taskMonitor, Long applicationId) throws UnifyException;
}

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

package com.flowcentraltech.flowcentral.application.business;

import com.flowcentraltech.flowcentral.application.util.ApplicationReplicationContext;
import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.configuration.data.ApplicationInstall;
import com.flowcentraltech.flowcentral.configuration.data.ApplicationRestore;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.task.TaskMonitor;

/**
 * Application artifact installer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface ApplicationArtifactInstaller extends FlowCentralComponent {

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
     * Restore custom application artifacts.
     * 
     * @param taskMonitor
     *                           the task monitor
     * @param applicationRestore
     *                           the application restore object
     * @throws UnifyException
     *                        if an error occurs
     */
    void restoreCustomApplicationArtifacts(TaskMonitor taskMonitor, ApplicationRestore applicationRestore)
            throws UnifyException;

    /**
     * Replicates application artifacts
     * 
     * @param taskMonitor
     *                          the task monitor
     * @param srcApplicationId
     *                          the source application ID
     * @param destApplicationId
     *                          the destination application ID
     * @param ctx
     *                          the application replication context
     * @throws UnifyException
     *                        if an error occurs
     */
    void replicateApplicationArtifacts(TaskMonitor taskMonitor, Long srcApplicationId, Long destApplicationId,
            ApplicationReplicationContext ctx) throws UnifyException;

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

    /**
     * Deletes all custom application artifact.
     * 
     * @param taskMonitor
     *                      the task monitor
     * @param applicationId
     *                      the application ID
     * @return the deletion count
     * @throws UnifyException
     *                        if an error occurs
     */
    int deleteCustomApplicationArtifacts(TaskMonitor taskMonitor, Long applicationId) throws UnifyException;

}

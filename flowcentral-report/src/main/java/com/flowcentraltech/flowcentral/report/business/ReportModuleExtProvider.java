/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.report.business;

import com.flowcentraltech.flowcentral.application.business.AbstractApplicationArtifactInstaller.DeletionParams;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.task.TaskMonitor;

/**
 * Report module extension provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface ReportModuleExtProvider extends UnifyComponent {

    /**
     * Deletes application artifacts.
     * 
     * @param taskMonitor
     *                       the task monitor
     * @param deletionParams
     *                       the deletion parameters
     * @param applicationId
     *                       the application ID
     * @param customOnly
     *                       custom only flag
     * @throws UnifyException
     *                        if an error occurs
     */
    void deleteApplicationArtifacts(TaskMonitor taskMonitor, DeletionParams deletionParams, Long applicationId,
            boolean customOnly) throws UnifyException;
}

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
package com.flowcentraltech.flowcentral.common.business;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.task.TaskMonitor;

/**
 * Synchronizable environment delegate.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface SynchronizableEnvironmentDelegate extends EnvironmentDelegate {

    /**
     * Perform delegate create synchronization.
     * 
     * @param taskMonitor
     *                    the task monitor
     * @throws UnifyException
     *                        if an error occurs
     */
    void delegateCreateSynchronization(TaskMonitor taskMonitor) throws UnifyException;

    /**
     * Perform delegate update synchronization.
     * 
     * @param taskMonitor
     *                    the task monitor
     * @throws UnifyException
     *                        if an error occurs
     */
    void delegateUpdateSynchronization(TaskMonitor taskMonitor) throws UnifyException;

}

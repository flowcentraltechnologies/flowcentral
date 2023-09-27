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

package com.flowcentraltech.flowcentral.common.business;

import java.util.List;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Database;
import com.tcdng.unify.core.task.TaskMonitor;

/**
 * Environment delegate.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface EnvironmentDelegate extends Database {
    
    /**
     * Returns true if delegate is direct.
     */
    boolean isDirect();

    /**
     * Executes a procedure.
     * 
     * @param operation
     *                  the operation name
     * @param payload
     *                  the payload
     * @return the procedure result
     * @throws UnifyException
     *                        if an error occurs
     */
    String[] executeProcedure(String operation, String... payload) throws UnifyException;

    /**
     * Gets entity aliases by data source.
     * 
     * @param dataSourceName
     *                       the data source name
     * @return
     * @throws UnifyException
     */
    List<String> getEntityAliasesByDataSource(String dataSourceName) throws UnifyException;

    /**
     * Gets the datasource name for entity
     * 
     * @param entityLongName
     *                       the entity long name
     * @return the data source name
     * @throws UnifyException
     *                        if an error occurs
     */
    String getDataSourceByEntityAlias(String entityLongName) throws UnifyException;

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

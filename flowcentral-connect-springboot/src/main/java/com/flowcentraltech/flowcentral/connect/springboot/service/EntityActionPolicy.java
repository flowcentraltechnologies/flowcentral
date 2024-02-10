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

package com.flowcentraltech.flowcentral.connect.springboot.service;

import com.flowcentraltech.flowcentral.connect.configuration.constants.EvaluationMode;

/**
 * Entity action policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface EntityActionPolicy<T> {

    /**
     * Validates entity bean
     * 
     * @param evaluationMode
     *                       the evaluation mode
     * @param entityBean
     *                       the entity bean
     * @return error messages if any
     * @throws Exception
     *                   if an error occurs
     */
    String[] validate(EvaluationMode evaluationMode, T entityBean) throws Exception;

    /**
     * Executes a pre-create action operation.
     * 
     * @param entityBean
     *                   the entity bean
     * @throws Exception
     *                   if an error occurs
     */
    void executePreCreateAction(T entityBean) throws Exception;

    /**
     * Executes a post-create action operation.
     * 
     * @param entityBean
     *                   the entity bean
     * @return the result object
     * @throws Exception
     *                   if an error occurs
     */
    void executePostCreateAction(T entityBean) throws Exception;

    /**
     * Executes a pre-update action operation.
     * 
     * @param entityBean
     *                   the entity bean
     * @throws Exception
     *                   if an error occurs
     */
    void executePreUpdateAction(T entityBean) throws Exception;

    /**
     * Executes a post-update action operation.
     * 
     * @param entityBean
     *                   the entity bean
     * @return the result object
     * @throws Exception
     *                   if an error occurs
     */
    void executePostUpdateAction(T entityBean) throws Exception;

    /**
     * Executes a pre-delete action operation.
     * 
     * @param entityBean
     *                   the entity bean
     * @throws Exception
     *                   if an error occurs
     */
    void executePreDeleteAction(T entityBean) throws Exception;

    /**
     * Executes a post-delete action operation.
     * 
     * @param entityBean
     *                   the entity bean
     * @return the result object
     * @throws Exception
     *                   if an error occurs
     */
    void executePostDeleteAction(T entityBean) throws Exception;
}

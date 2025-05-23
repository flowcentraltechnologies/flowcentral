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
package com.flowcentraltech.flowcentral.connect.springboot.service;

import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.flowcentraltech.flowcentral.connect.common.data.DetectEntityRequest;
import com.flowcentraltech.flowcentral.connect.common.data.DetectEntityResponse;
import com.flowcentraltech.flowcentral.connect.common.data.EntityListingRequest;
import com.flowcentraltech.flowcentral.connect.common.data.EntityListingResponse;
import com.flowcentraltech.flowcentral.connect.common.data.GetEntityRequest;
import com.flowcentraltech.flowcentral.connect.common.data.GetEntityResponse;
import com.flowcentraltech.flowcentral.connect.common.data.JsonDataSourceResponse;
import com.flowcentraltech.flowcentral.connect.common.data.JsonProcedureResponse;
import com.flowcentraltech.flowcentral.connect.common.data.ProcedureRequest;

/**
 * Flow central spring boot interconnect service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface SpringBootInterconnectService {

    /**
     * Gets the name of associates redirect component/
     * 
     * @return the redirect component name
     */
    String getRedirect();

    /**
     * Lists all entities.
     * 
     * @param req
     *            the listing request
     * @return the listing response
     * @throws Exception
     *                   if an error occurs
     */
    EntityListingResponse listEntities(EntityListingRequest req) throws Exception;
    
    /**
     * Detects an entity.
     * 
     * @param req
     *            the request object
     * @return the response object
     * @throws Exception
     *                   if an error occurs
     */
    DetectEntityResponse detectEntity(DetectEntityRequest req) throws Exception;

    /**
     * Gets an entity.
     * 
     * @param req
     *            the request object
     * @return the response object
     * @throws Exception
     *                   if an error occurs
     */
    GetEntityResponse getEntity(GetEntityRequest req) throws Exception;

    /**
     * Processes a data source request.
     * 
     * @param req
     *            the request to process
     * @return the data source response
     * @throws Exception
     *                   if an error occurs
     */
    JsonDataSourceResponse processDataSourceRequest(DataSourceRequest req) throws Exception;

    /**
     * Executes a procedure request.
     * 
     * @param req
     *            the request to process
     * @return the procedure response
     * @throws Exception
     *                   if an error occurs
     */
    JsonProcedureResponse executeProcedureRequest(ProcedureRequest req) throws Exception;
}

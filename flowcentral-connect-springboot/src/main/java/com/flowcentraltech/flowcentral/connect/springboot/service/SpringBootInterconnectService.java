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
package com.flowcentraltech.flowcentral.connect.springboot.service;

import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.flowcentraltech.flowcentral.connect.common.data.JsonDataSourceResponse;
import com.flowcentraltech.flowcentral.connect.common.data.ProcedureRequest;
import com.flowcentraltech.flowcentral.connect.common.data.JsonProcedureResponse;

/**
 * Flow central spring boot interconnect service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface SpringBootInterconnectService {

    String getRedirect();
    
    /**
     * Processes a data source request.
     * 
     * @param req
     *                the request to process
     * @return the data source response
     * @throws Exception
     *                   if an error occurs
     */
    JsonDataSourceResponse processDataSourceRequest(DataSourceRequest req) throws Exception;

    /**
     * Executes a procedure request.
     * 
     * @param req
     *                the request to process
     * @return the procedure response
     * @throws Exception
     *                   if an error occurs
     */
    JsonProcedureResponse executeProcedureRequest(ProcedureRequest req) throws Exception;
}

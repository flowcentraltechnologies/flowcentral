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

package com.flowcentraltech.flowcentral.connect.springboot.controllers;

import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.flowcentraltech.flowcentral.connect.common.data.GetEntityDataSourceAliasRequest;
import com.flowcentraltech.flowcentral.connect.common.data.GetEntityDataSourceAliasResponse;
import com.flowcentraltech.flowcentral.connect.common.data.JsonDataSourceResponse;
import com.flowcentraltech.flowcentral.connect.common.data.JsonProcedureResponse;
import com.flowcentraltech.flowcentral.connect.common.data.ProcedureRequest;

/**
 * Spring boot redirect component
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface SpringBootInterconnectRedirect {

    /**
     * Gets an entity data source alias.
     * 
     * @param req
     *            the request object
     * @return the response object
     * @throws Exception
     *                   if an error occurs
     */
    GetEntityDataSourceAliasResponse getEntityDataSourceAlias(GetEntityDataSourceAliasRequest req) throws Exception;

    /**
     * Performs a redirect process datasource datasource request
     * 
     * @param req
     *            the datasource request
     * @return the response if successfully redirected otherwise null
     */
    JsonDataSourceResponse processDataSourceRequest(DataSourceRequest req);

    /**
     * Performs a redirect process datasource procedure request
     * 
     * @param req
     *            the datasource request
     * @return the response if successfully redirected otherwise null
     */
    JsonProcedureResponse processDataSourceRequest(ProcedureRequest req);
}

/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.integration.endpoint;

import java.util.Set;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.integration.data.EndpointDef;
import com.tcdng.unify.core.UnifyException;

/**
 * End-point manager.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface EndpointManager extends FlowCentralComponent {

    /**
     * Sets REST endpoint changed.
     * 
     * @param endpointConfigName
     *                           the endpoint configuration name
     * @throws UnifyException
     *                        if an error occurs
     */
    void setRestEndpointChanged(String endpointConfigName) throws UnifyException;
    
    /**
     * Gets and clears changed REST end points.
     * 
     * @return the endpoint names
     * @throws UnifyException
     *                        if an error occurs
     */
    Set<String> getAndClearChangedRestEndpoint() throws UnifyException;
    
    /**
     * Gets the names of all active REST end point paths.
     * 
     * @return the list of endpoint paths
     * @throws UnifyException
     *                        if an error occurs
     */
    Set<String> getActiveRestEndpointPaths() throws UnifyException;
    
    /**
     * Get an end-point using supplied name.
     * 
     * @param endpointConfigName
     *                           the end-point configuration name
     * @return the end-point
     * @throws UnifyException
     *                        if an error occurs
     */
    EndpointDef getEndpointDef(String endpointConfigName) throws UnifyException;

    /**
     * Gets managed instance of an end-point.
     * 
     * @param endpointName
     *                     the end-point name
     * @throws UnifyException
     *                        if an error occurs
     */
    Endpoint getEndpoint(String endpointName) throws UnifyException;
}

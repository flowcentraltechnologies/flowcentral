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
package com.flowcentraltech.flowcentral.integration.endpoint.writer;

import com.flowcentraltech.flowcentral.integration.data.WriteConfigDef;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * End-point writer factory.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface EndpointWriterFactory extends UnifyComponent {

    /**
     * Returns a new instance of a end-point writer.
     * 
     * @param writeConfigDef
     *                      the end-point writer definition
     * @throws UnifyException
     *                        if an error occurs
     */
    EndpointWriter getEndpointWriter(WriteConfigDef writeConfigDef) throws UnifyException;

    /**
     * Disposes a end-point writer. Called when no further use of writer instance is
     * required.
     * 
     * @param endpointWriter
     *                       the writer to dispose
     */
    void disposeEndpointWriter(EndpointWriter endpointWriter) throws UnifyException;
}

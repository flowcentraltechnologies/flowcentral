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
package com.flowcentraltech.flowcentral.integration.endpoint.reader;

import com.flowcentraltech.flowcentral.integration.data.ReadConfigDef;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * End-point reader factory.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface EndpointReaderFactory extends UnifyComponent {

    /**
     * Returns a new instance of a end-point reader.
     * 
     * @param readConfigDef
     *                      the end-point reader definition
     * @throws UnifyException
     *                        if an error occurs
     */
    EndpointReader getEndpointReader(ReadConfigDef readConfigDef) throws UnifyException;

    /**
     * Disposes a end-point reader. Called when no further use of reader instance is
     * required.
     * 
     * @param endpointReader
     *                       the reader to dispose
     */
    void disposeEndpointReader(EndpointReader endpointReader) throws UnifyException;
}

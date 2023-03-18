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

import com.flowcentraltech.flowcentral.integration.constants.IntegrationModuleNameConstants;
import com.flowcentraltech.flowcentral.integration.data.WriteConfigDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Default end-point writer factory implementation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(IntegrationModuleNameConstants.ENDPOINTWRITER_FACTORY)
public class EndpointWriterFactoryImpl extends AbstractEndpointWriterFactory {

    @Override
    public EndpointWriter getEndpointWriter(WriteConfigDef writeConfigDef) throws UnifyException {
        EndpointWriter endpointWriter = (EndpointWriter) getComponent(writeConfigDef.getWriterName());
        endpointWriter.setup(writeConfigDef);
        endpointWriter.beginWatch();
        return endpointWriter;
    }

    @Override
    public void disposeEndpointWriter(EndpointWriter endpointWriter) throws UnifyException {
        endpointWriter.endWatch();
    }

}

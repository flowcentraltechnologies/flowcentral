/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.integration.constants.IntegrationModuleNameConstants;
import com.flowcentraltech.flowcentral.integration.data.ReadConfigDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Default end-point reader factory implementation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(IntegrationModuleNameConstants.ENDPOINTREADER_FACTORY)
public class EndpointReaderFactoryImpl extends AbstractEndpointReaderFactory {

    @Override
    public EndpointReader getEndpointReader(ReadConfigDef readConfigDef) throws UnifyException {
        EndpointReader endpointReader = (EndpointReader) getComponent(readConfigDef.getReaderName());
        endpointReader.setup(readConfigDef);
        endpointReader.beginWatch();
        return endpointReader;
    }

    @Override
    public void disposeEndpointReader(EndpointReader endpointReader) throws UnifyException {
        endpointReader.endWatch();
    }

}

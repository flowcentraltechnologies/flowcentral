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
package com.flowcentraltech.flowcentral.integration.activemq;

import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.common.data.ParamValuesDef;
import com.flowcentraltech.flowcentral.common.entities.ParamValues;
import com.flowcentraltech.flowcentral.common.util.CommonInputUtils;
import com.flowcentraltech.flowcentral.integration.activemq.constants.ActiveMQEndpointConstants;
import com.flowcentraltech.flowcentral.integration.business.IntegrationModuleService;
import com.flowcentraltech.flowcentral.integration.constants.IntegrationModuleNameConstants;
import com.flowcentraltech.flowcentral.integration.endpoint.EndpointType;
import com.flowcentraltech.flowcentral.integration.entities.EndpointConfig;
import com.flowcentraltech.flowcentral.test.AbstractFlowCentralTest;
import com.tcdng.unify.core.data.ParamConfig;

/**
 * Active MQ integration module service tests.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ActiveMQIntegrationModuleServiceTest extends AbstractFlowCentralTest {

    private IntegrationModuleService ims;

//    @Test
//    public void testSendMessage() throws Exception {
//        ims.sendMessage("testQueueEndpoint", "testQueue1", "Hello World!");
//        ims.sendMessage("testQueueEndpoint", "testQueue2", "Blue Skies!");
//    }

    @Override
    protected void onSetup() throws Exception {
        ims = (IntegrationModuleService) getComponent(IntegrationModuleNameConstants.INTEGRATION_MODULE_SERVICE);

        // Create test configuration
        EndpointConfig endpointConfig = new EndpointConfig();
        endpointConfig.setEndpointType(EndpointType.JMS);
        endpointConfig.setName("testQueueEndpoint");
        endpointConfig.setDescription("Test Queue Endpoint");
        endpointConfig.setEndpoint("activemq-endpoint");
        endpointConfig.setStatus(RecordStatus.ACTIVE);
        
        ParamValues paramValues = new ParamValues();
        ParamValuesDef.Builder pvdb = ParamValuesDef.newBuilder();
        pvdb.addParamValueDef(new ParamConfig(String.class, ActiveMQEndpointConstants.PROVIDER_URL), "");
        paramValues.setDefinition(CommonInputUtils.getParamValuesDefinition(pvdb.build()));
        endpointConfig.setEndpointParams(paramValues);
        
        createRecord(endpointConfig);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onTearDown() throws Exception {
        deleteAll(EndpointConfig.class);
    }

}

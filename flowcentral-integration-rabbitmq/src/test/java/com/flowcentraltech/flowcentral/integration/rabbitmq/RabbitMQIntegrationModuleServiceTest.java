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
package com.flowcentraltech.flowcentral.integration.rabbitmq;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.common.AbstractFlowCentralTest;
import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.common.data.ParamValuesDef;
import com.flowcentraltech.flowcentral.common.entities.ParamValues;
import com.flowcentraltech.flowcentral.common.util.CommonInputUtils;
import com.flowcentraltech.flowcentral.configuration.constants.DirectionType;
import com.flowcentraltech.flowcentral.integration.business.IntegrationModuleService;
import com.flowcentraltech.flowcentral.integration.constants.IntegrationModuleNameConstants;
import com.flowcentraltech.flowcentral.integration.constants.JmsEndpointConstants;
import com.flowcentraltech.flowcentral.integration.endpoint.EndpointType;
import com.flowcentraltech.flowcentral.integration.entities.EndpointConfig;
import com.flowcentraltech.flowcentral.integration.entities.EndpointConfigQuery;
import com.flowcentraltech.flowcentral.integration.entities.EndpointPath;
import com.flowcentraltech.flowcentral.integration.rabbitmq.constants.RabbitMQEndpointConstants;
import com.flowcentraltech.flowcentral.system.entities.Credential;
import com.flowcentraltech.flowcentral.system.entities.CredentialQuery;
import com.tcdng.unify.core.data.ParamConfig;

/**
 * Rabbit MQ integration module service tests.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class RabbitMQIntegrationModuleServiceTest extends AbstractFlowCentralTest {

    private IntegrationModuleService ims;

//    @Test
//    public void testSendMessage() throws Exception {
//        ims.sendMessage("testRabbitMQQueueEndpoint", "testDest1", "Hello World!");
//        ims.sendMessage("testRabbitMQQueueEndpoint", "testDest2", "Blue Skies!");
//    }
//
//    @Test
//    public void testReceiveMessage() throws Exception {
//        ims.sendMessage("testRabbitMQQueueEndpoint", "testDest1", "Hello World!");
//        ims.sendMessage("testRabbitMQQueueEndpoint", "testDest2", "Blue Skies!");
//
//        String text = ims.receiveMessage("testRabbitMQQueueEndpoint", "testSource2");
//        assertNotNull(text);
//        assertEquals("Blue Skies!", text);
//
//        text = ims.receiveMessage("testRabbitMQQueueEndpoint", "testSource1");
//        assertNotNull(text);
//        assertEquals("Hello World!", text);
//    }

    @Override
    protected void onSetup() throws Exception {
        ims = (IntegrationModuleService) getComponent(IntegrationModuleNameConstants.INTEGRATION_MODULE_SERVICE);
        // Credentials
        Credential credential = new Credential();
        credential.setName("testRabbitMQCred");
        credential.setDescription("Test Rabbit MQ Credentials");
        credential.setUserName("guest");
        credential.setPassword("guest");
        credential.setStatus(RecordStatus.ACTIVE);
        createRecord(credential);

        // Create test configuration
        EndpointConfig endpointConfig = new EndpointConfig();
        endpointConfig.setEndpointType(EndpointType.JMS);
        endpointConfig.setName("testRabbitMQQueueEndpoint");
        endpointConfig.setDescription("Test Rabbit MQ Queue Endpoint");
        endpointConfig.setEndpoint(RabbitMQEndpointConstants.COMPONENT_NAME);
        endpointConfig.setStatus(RecordStatus.ACTIVE);

        ParamValues paramValues = new ParamValues();
        ParamValuesDef.Builder pvdb = ParamValuesDef.newBuilder();
        pvdb.addParamValueDef(new ParamConfig(String.class, RabbitMQEndpointConstants.HOSTNAME), "localhost");
        pvdb.addParamValueDef(new ParamConfig(int.class, RabbitMQEndpointConstants.PORT), "5672");
        pvdb.addParamValueDef(new ParamConfig(String.class, JmsEndpointConstants.CREDENTIAL_NAME), "testRabbitMQCred");
        paramValues.setDefinition(CommonInputUtils.getParamValuesDefinition(pvdb.build()));
        endpointConfig.setEndpointParams(paramValues);

        List<EndpointPath> pathList = new ArrayList<EndpointPath>();
        EndpointPath path = new EndpointPath();
        path.setDirection(DirectionType.INCOMING);
        path.setName("testSource1");
        path.setDescription("Test Source 1");
        path.setPath("testQueue1");
        pathList.add(path);

        path = new EndpointPath();
        path.setDirection(DirectionType.INCOMING);
        path.setName("testSource2");
        path.setDescription("Test Source 2");
        path.setPath("testQueue2");
        pathList.add(path);

        path = new EndpointPath();
        path.setDirection(DirectionType.OUTGOING);
        path.setName("testDest1");
        path.setDescription("Test Dest 1");
        path.setPath("testQueue1");
        pathList.add(path);

        path = new EndpointPath();
        path.setDirection(DirectionType.OUTGOING);
        path.setName("testDest2");
        path.setDescription("Test Dest 2");
        path.setPath("testQueue2");
        pathList.add(path);

        endpointConfig.setPathList(pathList);
        createRecord(endpointConfig);
    }

    @Override
    protected void onTearDown() throws Exception {
        if (ims != null) {
            while (ims.receiveMessage("testRabbitMQQueueEndpoint", "testSource1") != null)
                ;
            while (ims.receiveMessage("testRabbitMQQueueEndpoint", "testSource2") != null)
                ;
        }

        deleteAll(new CredentialQuery().name("testRabbitMQCred"));
        deleteAll(new EndpointConfigQuery().name("testRabbitMQQueueEndpoint"));
    }

}

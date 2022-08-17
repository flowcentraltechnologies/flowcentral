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

package com.flowcentraltech.flowcentral.integration.rabbitmq.endpoint;

import java.util.Map;

import javax.jms.ConnectionFactory;

import com.flowcentraltech.flowcentral.integration.data.EndpointDef;
import com.flowcentraltech.flowcentral.integration.endpoint.AbstractJmsEndpoint;
import com.flowcentraltech.flowcentral.integration.rabbitmq.constants.RabbitMQEndpointConstants;
import com.rabbitmq.jms.admin.RMQConnectionFactory;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Parameters;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Rabbit MQ end-point.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(name = "rabbitmq-endpoint", description = "$m{endpoint.rabbitmq}")
@Parameters({
        @Parameter(name = RabbitMQEndpointConstants.HOSTNAME, description = "$m{rabbitmqendpoint.hostname}",
                editor = "!ui-text size:32", mandatory = true),
        @Parameter(name = RabbitMQEndpointConstants.PORT, description = "$m{rabbitmqendpoint.port}",
                editor = "!ui-integer precision:5", type = Integer.class, mandatory = true),
        @Parameter(name = RabbitMQEndpointConstants.VIRTUAL_HOST, description = "$m{rabbitmqendpoint.virtualhost}",
                editor = "!ui-text size:32"),
        @Parameter(name = RabbitMQEndpointConstants.USERNAME, description = "$m{rabbitmqendpoint.username}",
                editor = "!ui-text size:32"),
        @Parameter(name = RabbitMQEndpointConstants.PASSWORD, description = "$m{rabbitmqendpoint.password}",
                editor = "!ui-text size:32") })
public class RabbitMQEndpoint extends AbstractJmsEndpoint {

    private String hostName;

    private Integer port;

    private String virtualHost;

    private String userName;

    private String password;

    @Override
    protected void doSetup(EndpointDef endpointDef) throws UnifyException {
        Map<String, Object> parameters = endpointDef.getEndpointParamsDef().getValueMap();
        port = (Integer) parameters.get(RabbitMQEndpointConstants.PORT);
        hostName = (String) parameters.get(RabbitMQEndpointConstants.HOSTNAME);
        virtualHost = (String) parameters.get(RabbitMQEndpointConstants.VIRTUAL_HOST);
        userName = (String) parameters.get(RabbitMQEndpointConstants.USERNAME);
        password = (String) parameters.get(RabbitMQEndpointConstants.PASSWORD);
    }

    @Override
    protected ConnectionFactory doGetConnectionFactory() throws UnifyException {
        RMQConnectionFactory factory = new RMQConnectionFactory();
        if (!StringUtils.isBlank(userName)) {
            factory.setUsername(userName);
        }

        if (!StringUtils.isBlank(password)) {
            factory.setPassword(password);
        }

        if (!StringUtils.isBlank(virtualHost)) {
            factory.setVirtualHost(virtualHost);
        }

        factory.setHost(hostName);
        factory.setPort(port);
        return factory;
    }

}

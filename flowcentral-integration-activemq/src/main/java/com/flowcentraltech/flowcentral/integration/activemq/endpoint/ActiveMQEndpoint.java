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
package com.flowcentraltech.flowcentral.integration.activemq.endpoint;

import java.util.Map;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSslConnectionFactory;

import com.flowcentraltech.flowcentral.integration.activemq.constants.ActiveMQEndpointConstants;
import com.flowcentraltech.flowcentral.integration.data.EndpointDef;
import com.flowcentraltech.flowcentral.integration.endpoint.AbstractJmsEndpoint;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Parameters;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Active MQ end-point.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(name = "activemq-endpoint", description = "$m{endpoint.activemq}")
@Parameters({
        @Parameter(name = ActiveMQEndpointConstants.PROVIDER_URL, description = "$m{activemqendpoint.providerurl}",
                editor = "!ui-text size:48", mandatory = true),
        @Parameter(name = ActiveMQEndpointConstants.USE_SSL, description = "$m{activemqendpoint.usessl}",
                editor = "!ui-select list:booleanlist blankOption:$s{}", type = Boolean.class, mandatory = true),
        @Parameter(name = ActiveMQEndpointConstants.TRUSTSTORE, description = "$m{activemqendpoint.truststore}",
                editor = "!ui-text size:48"),
        @Parameter(name = ActiveMQEndpointConstants.TRUSTSTORE_PASSWORD,
                description = "$m{activemqendpoint.truststorepassword}", editor = "!ui-password"),
        @Parameter(name = ActiveMQEndpointConstants.KEYSTORE, description = "$m{activemqendpoint.keystore}",
                editor = "!ui-text size:48"),
        @Parameter(name = ActiveMQEndpointConstants.KEYSTORE_PASSWORD,
                description = "$m{activemqendpoint.keystorepassword}", editor = "!ui-password") })
public class ActiveMQEndpoint extends AbstractJmsEndpoint {

    private String providerUrl;

    private String trustStore;

    private String trustStorePassword;

    private String keyStore;

    private String keyStorePassword;

    private Boolean useSsl;

    @Override
    protected void doSetup(EndpointDef endpointDef) throws UnifyException {
        Map<String, Object> parameters = endpointDef.getEndpointParamsDef().getValueMap();
        providerUrl = (String) parameters.get(ActiveMQEndpointConstants.PROVIDER_URL);
        useSsl = (Boolean) parameters.get(ActiveMQEndpointConstants.USE_SSL);
        trustStore = (String) parameters.get(ActiveMQEndpointConstants.TRUSTSTORE);
        trustStorePassword = (String) parameters.get(ActiveMQEndpointConstants.TRUSTSTORE_PASSWORD);
        keyStore = (String) parameters.get(ActiveMQEndpointConstants.KEYSTORE);
        keyStorePassword = (String) parameters.get(ActiveMQEndpointConstants.KEYSTORE_PASSWORD);
    }

    @Override
    protected ConnectionFactory doGetConnectionFactory() throws UnifyException {
        if (useSsl) {
            try {
                ActiveMQSslConnectionFactory sslConnectionFactory = new ActiveMQSslConnectionFactory(providerUrl);
                if (!StringUtils.isBlank(trustStore)) {
                    sslConnectionFactory.setTrustStore(trustStore);
                    sslConnectionFactory.setTrustStorePassword(trustStorePassword);
                }

                if (!StringUtils.isBlank(keyStore)) {
                    sslConnectionFactory.setKeyStore(keyStore);
                    sslConnectionFactory.setKeyStorePassword(keyStorePassword);
                }

                return sslConnectionFactory;
            } catch (Exception e) {
                throwOperationErrorException(e);
            }
        }

        return new ActiveMQConnectionFactory(providerUrl);
    }

}

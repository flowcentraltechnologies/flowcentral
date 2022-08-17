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
package com.flowcentraltech.flowcentral.integration.endpoint;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;

import com.flowcentraltech.flowcentral.integration.data.EndpointDef;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.flowcentraltech.flowcentral.system.data.CredentialDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Parameters;
import com.tcdng.unify.core.data.AbstractPool;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Abstract JMS end-point.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Parameters({ @Parameter(name = JmsEndpointConstants.CREDENTIAL_NAME, description = "$m{jmsendpoint.credential}",
        editor = "!ui-select list:$s{credentiallist} listKey:$s{name} blankOption:$s{}", type = String.class) })
public abstract class AbstractJmsEndpoint extends AbstractEndpoint implements JmsEndpoint {

    @Configurable
    private SystemModuleService systemModuleService;

    @Configurable("4000")
    private long getSessionTimeout;

    @Configurable("8")
    private int maxSessionPool;

    @Configurable("1")
    private int minSessionPool;

    private ConnectionFactory connectionFactory;

    private Connection connection;

    private SessionPool sessionPool;

    private String credentialName;

    public void setSystemModuleService(SystemModuleService systemModuleService) {
        this.systemModuleService = systemModuleService;
    }

    public void setGetSessionTimeout(long getSessionTimeout) {
        this.getSessionTimeout = getSessionTimeout;
    }

    public void setMaxSessionPool(int maxSessionPool) {
        this.maxSessionPool = maxSessionPool;
    }

    public void setMinSessionPool(int minSessionPool) {
        this.minSessionPool = minSessionPool;
    }

    @Override
    public void setup(EndpointDef endpointDef) throws UnifyException {
        credentialName = (String) endpointDef.getEndpointParamsDef().getValueMap()
                .get(JmsEndpointConstants.CREDENTIAL_NAME);
        doSetup(endpointDef);
    }

    @Override
    public Session getSession() throws UnifyException {
        return sessionPool.borrowObject();
    }

    @Override
    public void restoreSession(Session session) throws UnifyException {
        sessionPool.returnObject(session);
    }

    @Override
    protected void onInitialize() throws UnifyException {
        super.onInitialize();
        sessionPool = new SessionPool();
    }

    @Override
    protected void onTerminate() throws UnifyException {
        sessionPool.terminate();
        sessionPool = null;

        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                logError(e);
            }
        }

        connectionFactory = null;
        super.onTerminate();
    }

    protected abstract void doSetup(EndpointDef endpointDef) throws UnifyException;

    protected abstract ConnectionFactory doGetConnectionFactory() throws UnifyException;

    private Connection getConnection() throws UnifyException {
        if (connection == null) {
            try {
                if (!StringUtils.isBlank(credentialName)) {
                    CredentialDef credentialDef = systemModuleService.getCredentialDef(credentialName);
                    connection = getConnectionFactory().createConnection(credentialDef.getUserName(),
                            credentialDef.getPassword());
                } else {
                    connection = getConnectionFactory().createConnection();
                }

                connection.start();
            } catch (JMSException e) {
                throwOperationErrorException(e);
            }
        }

        return connection;
    }

    private ConnectionFactory getConnectionFactory() throws UnifyException {
        if (connectionFactory == null) {
            connectionFactory = doGetConnectionFactory();
        }

        return connectionFactory;
    }

    private class SessionPool extends AbstractPool<Session> {

        public SessionPool() {
            super(getSessionTimeout, minSessionPool, maxSessionPool, false);
        }

        @Override
        protected Session createObject(Object... params) throws Exception {
            return getConnection().createSession(Session.AUTO_ACKNOWLEDGE);
        }

        @Override
        protected void destroyObject(Session session) {
            try {
                session.close();
            } catch (JMSException e) {
                logError(e);
            }
        }

        @Override
        protected void onGetObject(Session session, Object... params) throws Exception {

        }
    }
}

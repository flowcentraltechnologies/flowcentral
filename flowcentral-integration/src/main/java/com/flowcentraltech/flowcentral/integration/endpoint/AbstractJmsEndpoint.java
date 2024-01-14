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
package com.flowcentraltech.flowcentral.integration.endpoint;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import com.flowcentraltech.flowcentral.integration.data.EndpointDef;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.flowcentraltech.flowcentral.system.data.CredentialDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Parameters;
import com.tcdng.unify.core.data.AbstractPool;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Abstract JMS end-point.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Parameters({ @Parameter(name = JmsEndpointConstants.CREDENTIAL_NAME, description = "$m{jmsendpoint.credential}",
        editor = "!ui-select list:$s{credentiallist} listKey:$s{name} blankOption:$s{}", type = String.class,
        order = 32) })
public abstract class AbstractJmsEndpoint extends AbstractEndpoint implements JmsEndpoint {

    @Configurable
    private SystemModuleService systemModuleService;

    @Configurable("4000")
    private long getSessionTimeout;

    @Configurable("16")
    private int maxSessionPool;

    @Configurable("1")
    private int minSessionPool;

    @Configurable("5000")
    private int receiveTimeout;

    private ConnectionFactory connectionFactory;

    private Connection connection;

    private SessionPool producerSessionPool;

    private SessionPool consumerSessionPool;

    private String credentialName;

    public final void setSystemModuleService(SystemModuleService systemModuleService) {
        this.systemModuleService = systemModuleService;
    }

    public final void setGetSessionTimeout(long getSessionTimeout) {
        this.getSessionTimeout = getSessionTimeout;
    }

    public final void setMaxSessionPool(int maxSessionPool) {
        this.maxSessionPool = maxSessionPool;
    }

    public final void setMinSessionPool(int minSessionPool) {
        this.minSessionPool = minSessionPool;
    }

    public final void setReceiveTimeout(int receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }

    @Override
    public void setup(EndpointDef endpointDef) throws UnifyException {
        credentialName = (String) endpointDef.getEndpointParamsDef().getValueMap()
                .get(JmsEndpointConstants.CREDENTIAL_NAME);
        doSetup(endpointDef);
    }

    @Override
    public void sendMessage(String destination, String text) throws UnifyException {
        SessionInst sessionInst = producerSessionPool.borrowObject();
        try {
            MessageProducer producer = sessionInst.getSession().createProducer(sessionInst.getDestination(destination));
            TextMessage msg = sessionInst.getSession().createTextMessage(text);
            producer.send(msg);
        } catch (JMSException e) {
            throwOperationErrorException(e);
        } finally {
            producerSessionPool.returnObject(sessionInst);
        }
    }

    @Override
    public String receiveMessage(String source) throws UnifyException {
        String text = null;
        SessionInst sessionInst = consumerSessionPool.borrowObject();
        try {
            MessageConsumer consumer = sessionInst.getSession().createConsumer(sessionInst.getDestination(source));
            TextMessage msg = (TextMessage) consumer.receive(receiveTimeout);
            if (msg != null) {
                text = msg.getText();
            }
        } catch (JMSException e) {
            throwOperationErrorException(e);
        } finally {
            consumerSessionPool.returnObject(sessionInst);
        }

        return text;
    }

    @Override
    protected void onInitialize() throws UnifyException {
        super.onInitialize();
        producerSessionPool = new SessionPool();
        consumerSessionPool = new SessionPool();
    }

    @Override
    protected void onTerminate() throws UnifyException {
        producerSessionPool.terminate();
        producerSessionPool = null;

        consumerSessionPool.terminate();
        consumerSessionPool = null;

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

    private class SessionPool extends AbstractPool<SessionInst> {

        public SessionPool() {
            super(getSessionTimeout, minSessionPool, maxSessionPool, false);
        }

        @Override
        protected SessionInst createObject(Object... params) throws Exception {
            Session session = getConnection().createSession(Session.AUTO_ACKNOWLEDGE);
            return new SessionInst(session);
        }

        @Override
        protected void destroyObject(SessionInst sessionInst) {
            try {
                sessionInst.getSession().close();
            } catch (JMSException e) {
                logError(e);
            }
        }

        @Override
        protected void onGetObject(SessionInst sessionInst, Object... params) throws Exception {

        }
    }

    private class SessionInst {

        private final Session session;

        private final FactoryMap<String, Destination> destinations;

        public SessionInst(Session session) {
            this.session = session;
            this.destinations = new FactoryMap<String, Destination>()
                {

                    @Override
                    protected Destination create(String queueName, Object... params) throws Exception {
                        return session.createQueue(queueName);
                    }

                };
        }

        public Session getSession() {
            return session;
        }

        public Destination getDestination(String queueName) throws UnifyException {
            return destinations.get(queueName);
        }
    }
}

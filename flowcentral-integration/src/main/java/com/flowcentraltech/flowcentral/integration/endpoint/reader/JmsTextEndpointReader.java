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

import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import com.flowcentraltech.flowcentral.integration.constants.IntegrationModuleErrorConstants;
import com.flowcentraltech.flowcentral.integration.data.ReadConfigDef;
import com.flowcentraltech.flowcentral.integration.data.ReadEventInst;
import com.flowcentraltech.flowcentral.integration.endpoint.JmsEndpoint;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Parameters;

/**
 * JMS text endpoint reader.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(name = "jmstext-transportreader", description = "$m{endpointreader.jmstext}")
@Parameters({ @Parameter(name = JmsTextEndpointReaderConstants.ENDPOINT,
        description = "$m{jmstextendpointreader.endpointconfig}",
        editor = "!ui-select list:endpointconfiglist listKey:name listParams:$l{JMS} listParamType:immediate blankOption:$s{}",
        mandatory = true),
        @Parameter(name = JmsTextEndpointReaderConstants.SOURCEQUEUE, description = "$m{jmstextendpointreader.source}",
                editor = "!ui-text size:48", mandatory = true),
        @Parameter(name = JmsTextEndpointReaderConstants.PROCESSEDQUEUE,
                description = "$m{jmstextendpointreader.processed}", editor = "!ui-text size:48"),
        @Parameter(name = JmsTextEndpointReaderConstants.ERRORQUEUE, description = "$m{jmstextendpointreader.error}",
                editor = "!ui-text size:48") })
public class JmsTextEndpointReader extends AbstractEndpointReader {

    private MessageConsumer srcConsumer;

    private MessageProducer prcProducer;

    private MessageProducer errProducer;

    private Message message;

    private Session session;

    private String endpoint;

    private String srcQueue;

    private String prcQueue;

    private String errQueue;

    @Override
    public void setup(ReadConfigDef readConfigDef) throws UnifyException {
        Map<String, Object> parameters = readConfigDef.getReaderParamsDef().getValueMap();
        endpoint = (String) parameters.get(JmsTextEndpointReaderConstants.ENDPOINT);
        srcQueue = (String) parameters.get(JmsTextEndpointReaderConstants.SOURCEQUEUE);
        prcQueue = (String) parameters.get(JmsTextEndpointReaderConstants.PROCESSEDQUEUE);
        errQueue = (String) parameters.get(JmsTextEndpointReaderConstants.ERRORQUEUE);
    }

    @Override
    public void beginWatch() throws UnifyException {
        JmsEndpoint jmsEndpoint = (JmsEndpoint) getEndpoint(endpoint);
        session = jmsEndpoint.getSession();
    }

    @Override
    public boolean next() throws UnifyException {
        try {
            message = getSrcConsumer().receiveNoWait();
        } catch (JMSException e) {
            throwOperationErrorException(e);
        }
        return message != null;
    }

    @Override
    public ReadEventInst getEvent() throws UnifyException {
        if (message != null) {
            try {
                ReadEventInst readEventInst = new ReadEventInst();
                if (message.isBodyAssignableTo(String.class)) {
                    String text = message.getBody(String.class);
                    readEventInst.addEventMessage(null, text.getBytes());
                } else {
                    throw new UnifyException(IntegrationModuleErrorConstants.JMSTEXT_TRANSPORTREADER_UNSUPPORTED_FORMAT,
                            getName());
                }

                return readEventInst;
            } catch (JMSException e) {
                throwOperationErrorException(e);
            } finally {
                message = null;
            }
        }

        return null;
    }

    @Override
    public void endWatch() throws UnifyException {
        if (session != null) {
            JmsEndpoint jmsEndpoint = (JmsEndpoint) this.getEndpoint(endpoint);
            jmsEndpoint.restoreSession(session);
        }

        if (srcConsumer != null) {
            try {
                srcConsumer.close();
            } catch (JMSException e) {
                logError(e);
            }
        }

        if (prcProducer != null) {
            try {
                prcProducer.close();
            } catch (JMSException e) {
                logError(e);
            }
        }

        if (errProducer != null) {
            try {
                errProducer.close();
            } catch (JMSException e) {
                logError(e);
            }
        }
    }

    @Override
    public void housekeepWatch(ReadEventInst event, EndpointReadEventStatus status) throws UnifyException {
        try {
            MessageProducer mProducer = null;
            if (EndpointReadEventStatus.SUCCESSFUL.equals(status)) {
                mProducer = getPrcProducer();
            } else if (EndpointReadEventStatus.FAILED.equals(status)) {
                mProducer = getErrProducer();
            }

            if (mProducer != null) {
                for (ReadEventInst.EventMessage eventMessage : event.getEventMessages()) {
                    Message hMsg = session.createTextMessage(eventMessage.getText());
                    mProducer.send(hMsg);
                }
            }
        } catch (JMSException e) {
            throwOperationErrorException(e);
        }
    }

    protected MessageConsumer getSrcConsumer() throws UnifyException {
        if (srcConsumer == null) {
            srcConsumer = createMessageConsumer(srcQueue);
        }

        return srcConsumer;
    }

    protected MessageProducer getPrcProducer() throws UnifyException {
        if (prcProducer == null) {
            prcProducer = createMessageProducer(prcQueue);
        }

        return prcProducer;
    }

    protected MessageProducer getErrProducer() throws UnifyException {
        if (errProducer == null) {
            errProducer = createMessageProducer(errQueue);
        }

        return errProducer;
    }

    private MessageConsumer createMessageConsumer(String queue) throws UnifyException {
        if (queue != null) {
            try {
                return session.createConsumer(session.createQueue(queue));
            } catch (JMSException e) {
                throwOperationErrorException(e);
            }
        }

        return null;
    }

    private MessageProducer createMessageProducer(String queue) throws UnifyException {
        if (queue != null) {
            try {
                return session.createProducer(session.createQueue(queue));
            } catch (JMSException e) {
                throwOperationErrorException(e);
            }
        }

        return null;
    }
}

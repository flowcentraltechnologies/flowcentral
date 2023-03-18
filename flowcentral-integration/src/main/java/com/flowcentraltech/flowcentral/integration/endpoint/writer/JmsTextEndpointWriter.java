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

import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import com.flowcentraltech.flowcentral.integration.data.WriteConfigDef;
import com.flowcentraltech.flowcentral.integration.endpoint.JmsEndpoint;
import com.flowcentraltech.flowcentral.integration.endpoint.data.EventMessage;
import com.flowcentraltech.flowcentral.integration.endpoint.data.WriteEventInst;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Parameters;

/**
 * JMS text end-point writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(name = "jmstext-transportwriter", description = "$m{endpointwriter.jmstext}")
@Parameters({ @Parameter(name = JmsTextEndpointWriterConstants.ENDPOINT,
        description = "$m{jmstextendpointwriter.endpointconfig}",
        editor = "!ui-select list:endpointconfiglist listKey:name listParams:$l{JMS} listParamType:immediate blankOption:$s{}",
        mandatory = true),
        @Parameter(name = JmsTextEndpointWriterConstants.DESTINATIONQUEUE, description = "$m{jmstextendpointwriter.destination}",
                editor = "!ui-text size:48", mandatory = true) })
public class JmsTextEndpointWriter extends AbstractEndpointWriter {

    private MessageProducer destProducer;

    private Session session;

    private String endpoint;

    private String destQueue;

    @Override
    public void setup(WriteConfigDef writeConfigDef) throws UnifyException {
        Map<String, Object> parameters = writeConfigDef.getWriterParamsDef().getValueMap();
        endpoint = (String) parameters.get(JmsTextEndpointWriterConstants.ENDPOINT);
        destQueue = (String) parameters.get(JmsTextEndpointWriterConstants.DESTINATIONQUEUE);
    }

    @Override
    public void beginWatch() throws UnifyException {
        JmsEndpoint jmsEndpoint = (JmsEndpoint) getEndpoint(endpoint);
        session = jmsEndpoint.getSession();
    }

    @Override
    public boolean setEvent(WriteEventInst writeEventInst) throws UnifyException {
        MessageProducer mProducer = getDestProducer();
        try {
            for (EventMessage eventMessage : writeEventInst.getEventMessages()) {
                Message hMsg = eventMessage.isText() ? session.createTextMessage(eventMessage.getText()) :
                    createBytesMessage(eventMessage.getFile());
                mProducer.send(hMsg);
            }
        } catch (JMSException e) {
            throwOperationErrorException(e);
        }
        
        return true;
    }

    @Override
    public void endWatch() throws UnifyException {
        if (session != null) {
            JmsEndpoint jmsEndpoint = (JmsEndpoint) getEndpoint(endpoint);
            jmsEndpoint.restoreSession(session);
        }

        if (destProducer != null) {
            try {
                destProducer.close();
            } catch (JMSException e) {
                logError(e);
            }
        }
    }

    private MessageProducer getDestProducer() throws UnifyException {
        if (destProducer == null) {
            try {
                destProducer = session.createProducer(session.createQueue(destQueue));
            } catch (JMSException e) {
                throwOperationErrorException(e);
            }
        }

        return destProducer;
    }
    
    private BytesMessage createBytesMessage(byte[] bytes) throws JMSException {
        BytesMessage message = session.createBytesMessage();
        message.writeBytes(bytes);
        return message;
    }
}

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
package com.flowcentraltech.flowcentral.notification.server;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.tcdng.unify.core.UnifyCoreErrorConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.notification.Email;
import com.tcdng.unify.core.notification.EmailRecipient;
import com.tcdng.unify.core.notification.EmailServer;
import com.tcdng.unify.core.notification.EmailServerConfig;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Convenient abstract base class rest email Server.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractRestEmailServer extends AbstractFlowCentralComponent implements EmailServer {

    @Override
    public void configure(String configName, EmailServerConfig config) throws UnifyException {

    }

    @Override
    public boolean isConfigured(String configName) throws UnifyException {
        return true;
    }

    @Override
    public void sendEmail(String configName, Email... emails) {
        for (Email email : emails) {
            send(email);
        }
    }

    @Override
    public void sendEmail(String configName, Email email) {
        send(email);
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    private void send(Email email) {
        RestEmailRequest req = new RestEmailRequest();
        try {
            List<EmailRecipient> recipents = email.getRecipients();
            if (recipents.isEmpty()) {
                throw new UnifyException(UnifyCoreErrorConstants.EMAIL_RECIPIENTS_REQUIRED);
            }

            List<String> to = new ArrayList<String>();
            List<String> cc = new ArrayList<String>();
            List<String> bcc = new ArrayList<String>();
            for (EmailRecipient recipient : recipents) {
                switch (recipient.getType()) {
                    case BCC:
                        bcc.add(recipient.getAddress());
                        break;
                    case CC:
                        cc.add(recipient.getAddress());
                        break;
                    case TO:
                        to.add(recipient.getAddress());
                        break;
                    default:
                        break;

                }
            }

            if (to.isEmpty()) {
                throw new UnifyException(UnifyCoreErrorConstants.EMAIL_RECIPIENTS_REQUIRED);
            }

            req.setTo(to);
            req.setCc(cc);
            req.setBcc(bcc);
            req.setSubject(email.getSubject());
            req.setBody(email.getMessage());

            // Send REST request
            RestEmailResponse resp = IOUtils.postObjectToEndpointUsingJson(RestEmailResponse.class, getRestEndPoint(),
                    req);
            if (resp.getErrorCode() == null) {
                email.setSent(true);
            } else {
                email.setError(resp.getErrorMsg());
            }
        } catch (Exception e) {
            final String error = StringUtils.getPrintableStackTrace(e);
            email.setError(error);
            logError(e);
        }
    }

    protected abstract String getRestEndPoint() throws UnifyException;
}

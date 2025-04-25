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
package com.flowcentraltech.flowcentral.security.business.senders;

import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.notification.senders.AbstractNotificationTemplateAlertSender;
import com.flowcentraltech.flowcentral.security.templatewrappers.UserWelcomeTemplateWrapper;
import com.tcdng.unify.common.util.ProcessVariableUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * User welcome template alert sender.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@EntityReferences({ "security.user" })
@Component("userwelcome-alertsender")
public class UserWelcomeTemplateAlertSender extends AbstractNotificationTemplateAlertSender<UserWelcomeTemplateWrapper> {

    public UserWelcomeTemplateAlertSender() {
        super(UserWelcomeTemplateWrapper.class);
    }

    @Override
    protected void setTemplateVariables(UserWelcomeTemplateWrapper notifWrapper, ValueStoreReader reader)
            throws UnifyException {
        notifWrapper.setFullName(reader.read(String.class, "fullName"));
        notifWrapper.setLoginId(reader.read(String.class, "loginId"));
        notifWrapper.setPlainPassword(reader.read(String.class, ProcessVariableUtils.getVariable("plainPassword")));
    }

    @Override
    protected List<Recipient> getAdditionalRecipients(ValueStoreReader reader) throws UnifyException {
        return Collections.emptyList();
    }

    @Override
    protected List<Attachment> generateAttachments(ValueStoreReader reader) throws UnifyException {
        return Collections.emptyList();
    }

}

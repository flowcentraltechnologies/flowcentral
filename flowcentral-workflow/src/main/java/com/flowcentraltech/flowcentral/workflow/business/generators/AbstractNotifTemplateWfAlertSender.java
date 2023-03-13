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
package com.flowcentraltech.flowcentral.workflow.business.generators;

import java.util.List;

import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.NotifType;
import com.flowcentraltech.flowcentral.notification.data.NotifTemplateWrapper;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for notification template workflow alert
 * sender.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractNotifTemplateWfAlertSender<T extends NotifTemplateWrapper> extends AbstractWfAlertSender {

    private final Class<T> wrapperType;

    public AbstractNotifTemplateWfAlertSender(Class<T> wrapperType) {
        this.wrapperType = wrapperType;
    }

    @Override
    public NotifType getNotifType() throws UnifyException {
        return notification().wrapperOf(wrapperType).getNotifType();
    }

    @Override
    public final void composeAndSend(ValueStoreReader reader, List<Recipient> recipientList) throws UnifyException {
        T wrapper = notification().wrapperOf(wrapperType);
        if (!DataUtils.isBlank(recipientList)) {
            for (Recipient recipient : recipientList) {
                wrapper.addRecipient(recipient);
            }
        }

        compose(wrapper, reader);
        notification().sendNotification(wrapper.getMessage());
    }

    protected abstract void compose(T notifWrapper, ValueStoreReader reader) throws UnifyException;
}

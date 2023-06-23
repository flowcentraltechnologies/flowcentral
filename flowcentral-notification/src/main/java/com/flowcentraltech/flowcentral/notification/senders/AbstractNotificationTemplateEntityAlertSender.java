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
package com.flowcentraltech.flowcentral.notification.senders;

import com.flowcentraltech.flowcentral.common.entities.EntityWrapper;
import com.flowcentraltech.flowcentral.notification.data.NotifTemplateWrapper;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Convenient abstract base class for notification template entity alert sender.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractNotificationTemplateEntityAlertSender<T extends NotifTemplateWrapper, U extends EntityWrapper>
        extends AbstractNotificationTemplateAlertSender<T> {

    private final Class<U> entityWrapperType;

    public AbstractNotificationTemplateEntityAlertSender(Class<T> notifWrapperType, Class<U> entityWrapperType) {
        super(notifWrapperType);
        this.entityWrapperType = entityWrapperType;
    }

    @Override
    protected final void setTemplateVariables(T notifWrapper, ValueStoreReader reader) throws UnifyException {
        U entityWrapper = application().wrapperOf(entityWrapperType, reader.getValueStore());
        setTemplateVariables(notifWrapper, entityWrapper);
    }

    protected abstract void setTemplateVariables(T notifWrapper, U entityWrapper) throws UnifyException;
}

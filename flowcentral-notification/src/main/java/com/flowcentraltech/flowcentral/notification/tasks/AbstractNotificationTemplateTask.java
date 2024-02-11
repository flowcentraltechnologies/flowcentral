/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.notification.tasks;

import java.util.List;

import com.flowcentraltech.flowcentral.common.entities.EntityWrapper;
import com.flowcentraltech.flowcentral.notification.data.NotifTemplateWrapper;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.task.TaskInput;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.task.TaskOutput;

/**
 * Convenient abstract base class for notification template tasks.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractNotificationTemplateTask<T extends NotifTemplateWrapper>
        extends AbstractNotificationTask {

    private final Class<T> notifWrapperType;

    public AbstractNotificationTemplateTask(Class<T> notifWrapperType) {
        this.notifWrapperType = notifWrapperType;
    }

    @Override
    public void execute(TaskMonitor monitor, TaskInput input, TaskOutput output) throws UnifyException {
        composeAndSend();
    }

    protected abstract void composeAndSend() throws UnifyException;

    protected final T getTemplateWrapper() throws UnifyException {
        return notification().wrapperOfNotifTemplate(notifWrapperType);
    }

    protected final void send(T templateWrapper) throws UnifyException {
        notification().sendNotification(templateWrapper.getMessage());
    }

    protected final <U extends EntityWrapper> U wrapperOf(Class<U> wrapperType) throws UnifyException {
        return application().wrapperOf(wrapperType);
    }

    protected final <U extends EntityWrapper> U wrapperOf(Class<U> wrapperType, Entity inst) throws UnifyException {
        return application().wrapperOf(wrapperType, inst);
    }

    protected final <U extends EntityWrapper> U wrapperOf(Class<U> wrapperType, List<? extends Entity> instList)
            throws UnifyException {
        return application().wrapperOf(wrapperType, instList);
    }

    protected final <U extends EntityWrapper> U wrapperOf(Class<U> wrapperType, ValueStore valueStore) throws UnifyException {
        return application().wrapperOf(wrapperType, valueStore);
    }

    protected final Query<? extends Entity> queryOf(Class<? extends EntityWrapper> wrapperType) throws UnifyException {
        return application().queryOf(wrapperType);
    }

}

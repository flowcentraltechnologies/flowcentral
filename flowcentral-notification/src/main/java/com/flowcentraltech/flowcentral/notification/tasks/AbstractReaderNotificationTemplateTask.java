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

import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.notification.data.NotifTemplateWrapper;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.task.TaskInput;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for reader notification template tasks.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractReaderNotificationTemplateTask<T extends NotifTemplateWrapper>
        extends AbstractNotificationTask {

    private final Class<T> notifWrapperType;

    public AbstractReaderNotificationTemplateTask(Class<T> notifWrapperType) {
        this.notifWrapperType = notifWrapperType;
    }

    @Override
    public final void doExecute(TaskMonitor monitor, TaskInput input) throws UnifyException {
        final T notifWrapper = notification().wrapperOfNotifTemplate(notifWrapperType);      
        final ValueStoreReader reader = getReader(input);
        
        // Set recipients
        List<Recipient> recipientList = getRecipientList(reader);
        if (!DataUtils.isBlank(recipientList)) {
            for (Recipient recipient : recipientList) {
                notifWrapper.addRecipient(recipient);
            }
        }

        // Set template variables
        setTemplateVariables(notifWrapper, reader);

        // Set attachments
        List<Attachment> attachmentList = generateAttachments(reader);
        if (!DataUtils.isBlank(attachmentList)) {
            for (Attachment attachment : attachmentList) {
                notifWrapper.addAttachment(attachment);
            }
        }

        // Send notification
        notification().sendNotification(notifWrapper.getMessage());
    }

    /**
     * Gets reader.
     * 
     * @param input
     *              the task input
     * @return the backing store reader for this task
     * @throws UnifyException
     *                        if an error occurs
     */
    protected abstract ValueStoreReader getReader(TaskInput input) throws UnifyException;
    
    /**
     * Sets the notification template variables.
     * 
     * @param notifWrapper
     *                     the notification template wrapper
     * @param reader
     *                     the backing value store reader
     * @throws UnifyException
     *                        if an error occurs
     */
    protected abstract void setTemplateVariables(T notifWrapper, ValueStoreReader reader) throws UnifyException;

     /**
     * Gets recipient list.
     * 
     * @param reader
     *               the backing value store reader
     * @return the list of recipients
     * @throws UnifyException
     *                        if an error occurs
     */
    protected abstract List<Recipient> getRecipientList(ValueStoreReader reader) throws UnifyException;
    
    /**
     * Generates notification attachments using reader.
     * 
     * @param reader
     *               the backing value store reader
     * @return the list of generated attachments
     * @throws UnifyException
     *                        if an error occurs
     */
    protected abstract List<Attachment> generateAttachments(ValueStoreReader reader) throws UnifyException;

}

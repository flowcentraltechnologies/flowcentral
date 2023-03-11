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

package com.flowcentraltech.flowcentral.notification.business;

import java.util.List;

import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.Dictionary;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Notification attachment generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface NotificationAttachmentGenerator extends UnifyComponent {

    /**
     * Generates notification attachments using supplied dictionary.
     * 
     * @param dictionary
     *                   the dictionary to user
     * @return the list of generated attachments
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Attachment> generateAttachment(Dictionary dictionary) throws UnifyException;

    /**
     * Generates notification attachments using supplied packable document.
     * 
     * @param valueStore
     *                   the value store
     * @return the list of generated attachments
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Attachment> generateAttachment(ValueStore valueStore) throws UnifyException;
}

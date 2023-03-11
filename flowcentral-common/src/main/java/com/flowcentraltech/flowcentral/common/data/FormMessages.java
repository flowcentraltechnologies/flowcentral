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

package com.flowcentraltech.flowcentral.common.data;

import java.util.ArrayList;
import java.util.List;

import com.tcdng.unify.web.ui.constant.MessageType;

/**
 * Form messages.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormMessages {

    private List<FormMessage> messageList;

    public FormMessages() {
        this.messageList = new ArrayList<FormMessage>();
    }

    public void addMessage(String message) {
        messageList.add(new FormMessage(MessageType.INFO, message));
    }

    public void addInfo(String message) {
        messageList.add(new FormMessage(MessageType.INFO, message));
    }

    public void addWarning(String message) {
        messageList.add(new FormMessage(MessageType.WARNING, message));
    }

    public void addError(String message) {
        messageList.add(new FormMessage(MessageType.ERROR, message));
    }

    public List<FormMessage> getMessages() {
        return messageList;
    }
}

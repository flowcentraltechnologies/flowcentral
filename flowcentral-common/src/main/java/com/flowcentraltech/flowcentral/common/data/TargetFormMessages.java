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

package com.flowcentraltech.flowcentral.common.data;

import java.util.ArrayList;
import java.util.List;

import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.constant.MessageType;

/**
 * Target form messages.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class TargetFormMessages {

    private List<TargetFormMessage> messageList;

    public TargetFormMessages() {
        this.messageList = new ArrayList<TargetFormMessage>();
    }

    public void addMessage(String message) {
        addMessage(message, DataUtils.ZEROLEN_STRING_ARRAY);
    }

    public void addMessage(String message, String... targets) {
        messageList.add(new TargetFormMessage(new FormMessage(MessageType.INFO, message), false, targets));
    }

    public void addMessage(String message, TargetFormMessage.Target... targets) {
        messageList.add(new TargetFormMessage(new FormMessage(MessageType.INFO, message), false, targets));
    }

    public void addInfo(String message) {
        addInfo(message, DataUtils.ZEROLEN_STRING_ARRAY);
    }

    public void addInfo(String message, String... targets) {
        messageList.add(new TargetFormMessage(new FormMessage(MessageType.INFO, message), false, targets));
    }

    public void addInfo(String message, TargetFormMessage.Target... targets) {
        messageList.add(new TargetFormMessage(new FormMessage(MessageType.INFO, message), false, targets));
    }

    public void addWarning(String message) {
        addWarning(message, DataUtils.ZEROLEN_STRING_ARRAY);
    }

    public void addWarning(String message, String... targets) {
        messageList.add(new TargetFormMessage(new FormMessage(MessageType.WARNING, message), false, targets));
    }

    public void addWarning(String message, TargetFormMessage.Target... targets) {
        messageList.add(new TargetFormMessage(new FormMessage(MessageType.WARNING, message), false, targets));
    }

    public void addError(String message) {
        addError(message, DataUtils.ZEROLEN_STRING_ARRAY);
    }

    public void addError(String message, String... targets) {
        messageList.add(new TargetFormMessage(new FormMessage(MessageType.ERROR, message), false, targets));
    }

    public void addError(String message, TargetFormMessage.Target... targets) {
        messageList.add(new TargetFormMessage(new FormMessage(MessageType.ERROR, message), false, targets));
    }

    public void addSkippableMessage(String message) {
        addSkippableMessage(message, DataUtils.ZEROLEN_STRING_ARRAY);
    }

    public void addSkippableMessage(String message, String... targets) {
        messageList.add(new TargetFormMessage(new FormMessage(MessageType.INFO, message), true, targets));
    }

    public void addSkippableMessage(String message, TargetFormMessage.Target... targets) {
        messageList.add(new TargetFormMessage(new FormMessage(MessageType.INFO, message), true, targets));
    }

    public void addSkippableInfo(String message) {
        addSkippableInfo(message, DataUtils.ZEROLEN_STRING_ARRAY);
    }

    public void addSkippableInfo(String message, String... targets) {
        messageList.add(new TargetFormMessage(new FormMessage(MessageType.INFO, message), true, targets));
    }

    public void addSkippableInfo(String message, TargetFormMessage.Target... targets) {
        messageList.add(new TargetFormMessage(new FormMessage(MessageType.INFO, message), true, targets));
    }

    public void addSkippableWarning(String message) {
        addSkippableWarning(message, DataUtils.ZEROLEN_STRING_ARRAY);
    }

    public void addSkippableWarning(String message, String... targets) {
        messageList.add(new TargetFormMessage(new FormMessage(MessageType.WARNING, message), true, targets));
    }

    public void addSkippableWarning(String message, TargetFormMessage.Target... targets) {
        messageList.add(new TargetFormMessage(new FormMessage(MessageType.WARNING, message), true, targets));
    }

    public void addSkippableError(String message) {
        addSkippableError(message, DataUtils.ZEROLEN_STRING_ARRAY);
    }

    public void addSkippableError(String message, String... targets) {
        messageList.add(new TargetFormMessage(new FormMessage(MessageType.ERROR, message), true, targets));
    }

    public void addSkippableError(String message, TargetFormMessage.Target... targets) {
        messageList.add(new TargetFormMessage(new FormMessage(MessageType.ERROR, message), true, targets));
    }

    public List<TargetFormMessage> getMessages() {
        return DataUtils.unmodifiableList(messageList);
    }
}

/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

    public void addMessage(String message, String... targets) {
        messageList.add(new TargetFormMessage(new FormMessage(MessageType.INFO, message), targets));
    }

    public void addInfo(String message, String... targets) {
        messageList.add(new TargetFormMessage(new FormMessage(MessageType.INFO, message), targets));
    }

    public void addWarning(String message, String... targets) {
        messageList.add(new TargetFormMessage(new FormMessage(MessageType.WARNING, message), targets));
    }

    public void addError(String message, String... targets) {
        messageList.add(new TargetFormMessage(new FormMessage(MessageType.ERROR, message), targets));
    }

    public List<TargetFormMessage> getMessages() {
        return DataUtils.unmodifiableList(messageList);
    }
}

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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tcdng.unify.web.ui.constant.MessageType;

/**
 * Entry table message.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EntryTableMessage {

    public static final EntryTableMessage EMPTY_MESSAGE = new EntryTableMessage(Collections.emptyList(),
            Collections.emptySet());

    private List<FormMessage> messages;

    private Set<Integer> rows;

    private EntryTableMessage(MessageType type, String message, Integer... rows) {
        this.messages = Collections.unmodifiableList(Arrays.asList(new FormMessage(type, message)));
        this.rows = rows.length > 0 ? Collections.unmodifiableSet(new HashSet<Integer>(Arrays.asList(rows)))
                : Collections.emptySet();
    }

    private EntryTableMessage(List<FormMessage> messages, Set<Integer> rows) {
        this.messages = messages;
        this.rows = rows;
    }

    public List<FormMessage> getMessages() {
        return messages;
    }

    public Set<Integer> getRows() {
        return rows;
    }

    public boolean isMessagePresent() {
        return !messages.isEmpty();
    }

    public boolean isMessageEmpty() {
        return messages.isEmpty();
    }

    public MessageType getMessageType() {
        return messages.isEmpty() ? null: messages.get(0).getType();
    }
    
    public boolean isRowReferred(int row) {
        return rows.contains(row);
    }
}

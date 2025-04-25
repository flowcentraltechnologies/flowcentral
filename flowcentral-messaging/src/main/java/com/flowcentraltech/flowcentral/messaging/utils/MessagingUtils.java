/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.messaging.utils;

import com.flowcentraltech.flowcentral.messaging.data.Message;
import com.flowcentraltech.flowcentral.messaging.data.MessageHeader;

/**
 * Messaging utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public final class MessagingUtils {

    private static final String HEADER_START = "HD//";
    
    private static final String HEADER_END = "//";
    
    private MessagingUtils() {
        
    }
    
    public static String marshal(Message message) {
        if (message.isSendHeaderless()) {
            return message.getBody();
        }

        MessageHeader header = message.getHeader();
        StringBuilder sb = new StringBuilder();
        sb.append(HEADER_START);
        sb.append(header.getConfig());
        sb.append(':');
        sb.append(header.getDestination());
        if (header.isWithHandler()) {
            sb.append(':');
            sb.append(header.getHandler());
        }
        sb.append(HEADER_END);
        sb.append(message.getBody());
        return sb.toString();
    }
    
    public static Message unmarshal(String config, String source, String text) {
        if (text != null) {
            if (text.startsWith(HEADER_START)) {
                int index = text.indexOf(HEADER_END, HEADER_START.length());
                if (index > 0) {
                    String headerTxt = text.substring(HEADER_START.length(), index);
                    String[] parts = headerTxt.split(":");
                    Message.Builder mb = Message.newBuilder(parts[0], parts[1]);
                    if (parts.length == 3) {
                        mb.handler(parts[2]);
                    }

                    mb.body(text.substring(index + HEADER_END.length()));
                    return mb.build();
                }
            }

            return new Message.Builder(config, source).body(text).build();
        }

        return null;
    }
}

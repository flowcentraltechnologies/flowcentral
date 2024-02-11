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

package com.flowcentraltech.flowcentral.common.data;

import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.constant.MessageType;

/**
 * Message type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormMessage {
    
    private MessageType type;
    
    private String message;
    
    private boolean local;

    public FormMessage(MessageType type, String message) {
        this.type = type;
        this.message = message;
    }

    public FormMessage(MessageType type, String message, boolean local) {
        this.type = type;
        this.message = message;
        this.local = local;
    }

    public MessageType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public boolean isLocal() {
        return local;
    }

    @Override
    public String toString() {
        return StringUtils.toXmlString(this);
    }
}

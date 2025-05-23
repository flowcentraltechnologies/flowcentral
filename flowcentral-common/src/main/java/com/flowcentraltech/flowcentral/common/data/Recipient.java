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

package com.flowcentraltech.flowcentral.common.data;

import com.flowcentraltech.flowcentral.configuration.constants.NotifRecipientType;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Recipient.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class Recipient {

    private NotifRecipientType type;

    private String name;

    private String contact;

    public Recipient(NotifRecipientType type, String name, String contact) {
        this.type = type;
        this.name = name;
        this.contact = contact;
    }

    public Recipient(String name, String contact) {
        this.type = NotifRecipientType.TO;
        this.name = name;
        this.contact = contact;
    }

    public NotifRecipientType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    @Override
    public String toString() {
        return StringUtils.toXmlString(this);
    }
}

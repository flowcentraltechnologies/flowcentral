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
package com.flowcentraltech.flowcentral.application.data;

import com.tcdng.unify.common.data.Listable;

/**
 * Applet alert definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class AppletAlertDef implements Listable {

    private String name;

    private String description;

    private String recipientPolicy;

    private String recipientNameRule;

    private String recipientContactRule;

    private String sender;

    private String template;

    public AppletAlertDef(String name, String description, String recipientPolicy, String recipientNameRule,
            String recipientContactRule, String sender, String template) {
        this.name = name;
        this.description = description;
        this.recipientPolicy = recipientPolicy;
        this.recipientNameRule = recipientNameRule;
        this.recipientContactRule = recipientContactRule;
        this.sender = sender;
        this.template = template;
    }

    @Override
    public String getListDescription() {
        return description;
    }

    @Override
    public String getListKey() {
        return name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getRecipientPolicy() {
        return recipientPolicy;
    }

    public String getRecipientNameRule() {
        return recipientNameRule;
    }

    public String getRecipientContactRule() {
        return recipientContactRule;
    }

    public String getSender() {
        return sender;
    }

    public String getTemplate() {
        return template;
    }

}

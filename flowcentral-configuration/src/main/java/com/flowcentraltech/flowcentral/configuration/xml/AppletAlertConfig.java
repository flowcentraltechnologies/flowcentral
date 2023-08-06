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
package com.flowcentraltech.flowcentral.configuration.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.flowcentraltech.flowcentral.configuration.constants.WorkflowAlertType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.WorkflowStepTypeXmlAdapter;

/**
 * Applet alert configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppletAlertConfig extends BaseNameConfig {

    private WorkflowAlertType type;

    private String sender;

    private boolean alertHeldBy;

    private boolean alertWorkflowRoles;

    public WorkflowAlertType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(WorkflowStepTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setType(WorkflowAlertType type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    @XmlAttribute(required = true)
    public void setSender(String sender) {
        this.sender = sender;
    }

    public boolean getAlertHeldBy() {
        return alertHeldBy;
    }

    @XmlAttribute
    public void setAlertHeldBy(boolean alertHeldBy) {
        this.alertHeldBy = alertHeldBy;
    }

    public boolean getAlertWorkflowRoles() {
        return alertWorkflowRoles;
    }

    @XmlAttribute
    public void setAlertWorkflowRoles(boolean alertWorkflowRoles) {
        this.alertWorkflowRoles = alertWorkflowRoles;
    }

}

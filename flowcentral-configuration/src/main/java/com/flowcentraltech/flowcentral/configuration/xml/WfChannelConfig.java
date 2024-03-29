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

package com.flowcentraltech.flowcentral.configuration.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.flowcentraltech.flowcentral.configuration.constants.ChannelDirectionType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.ChannelDirectionTypeXmlAdapter;

/**
 * Workflow channel configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WfChannelConfig extends BaseNameConfig {

    private ChannelDirectionType direction;

    private String entity;

    private String destination;

    private String rule;

    public ChannelDirectionType getDirection() {
        return direction;
    }

    @XmlJavaTypeAdapter(ChannelDirectionTypeXmlAdapter.class)
    @XmlAttribute(name = "direction", required = true)
    public void setDirection(ChannelDirectionType direction) {
        this.direction = direction;
    }

    public String getEntity() {
        return entity;
    }

    @XmlAttribute(required = true)
    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getDestination() {
        return destination;
    }

    @XmlAttribute(required = true)
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getRule() {
        return rule;
    }

    @XmlAttribute
    public void setRule(String rule) {
        this.rule = rule;
    }

}

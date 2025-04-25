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
package com.flowcentraltech.flowcentral.workflow.data;

import com.flowcentraltech.flowcentral.application.data.BaseApplicationEntityDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.configuration.constants.ChannelDirectionType;
import com.flowcentraltech.flowcentral.workflow.constants.WfChannelStatus;
import com.tcdng.unify.core.UnifyException;

/**
 * Workflow channel definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WfChannelDef extends BaseApplicationEntityDef {

    private ChannelDirectionType direction;

    private WfChannelStatus status;

    private String label;

    private String entity;

    private String destination;

    private String rule;

    private WfChannelDef(ChannelDirectionType direction, WfChannelStatus status, String label, String entity,
            String destination, String rule, ApplicationEntityNameParts nameParts, String description, Long id,
            long version) {
        super(nameParts, description, id, version);
        this.direction = direction;
        this.status = status;
        this.label = label;
        this.entity = entity;
        this.destination = destination;
        this.rule = rule;
    }

    public ChannelDirectionType getDirection() {
        return direction;
    }

    public WfChannelStatus getStatus() {
        return status;
    }

    public String getLabel() {
        return label;
    }

    public String getEntity() {
        return entity;
    }

    public String getDestination() {
        return destination;
    }

    public String getRule() {
        return rule;
    }

    public static Builder newBuilder(ChannelDirectionType direction, WfChannelStatus status, String label,
            String entity, String destination, String rule, String longName, String description, Long id,
            long version) {
        return new Builder(direction, status, label, entity, destination, rule, longName, description, id, version);
    }

    public static class Builder {

        private ChannelDirectionType direction;

        private WfChannelStatus status;

        private String label;

        private String entity;

        private String destination;

        private String rule;

        private String longName;

        private String description;

        private Long id;

        private long version;

        public Builder(ChannelDirectionType direction, WfChannelStatus status, String label, String entity,
                String destination, String rule, String longName, String description, Long id, long version) {
            this.longName = longName;
            this.description = description;
            this.id = id;
            this.version = version;
            this.direction = direction;
            this.status = status;
            this.label = label;
            this.entity = entity;
            this.destination = destination;
            this.rule = rule;
        }

        public WfChannelDef build() throws UnifyException {
            ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
            return new WfChannelDef(direction, status, label, entity, destination, rule, nameParts, description, id,
                    version);
        }
    }
}

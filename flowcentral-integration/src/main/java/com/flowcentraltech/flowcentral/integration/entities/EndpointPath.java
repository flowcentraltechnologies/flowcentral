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
package com.flowcentraltech.flowcentral.integration.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.flowcentraltech.flowcentral.configuration.constants.DirectionType;
import com.flowcentraltech.flowcentral.integration.endpoint.EndpointType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * End-point path.
 *
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_ENDPOINTPATH", uniqueConstraints = {
        @UniqueConstraint({ "endpointConfigId", "name" }),
        @UniqueConstraint({ "endpointConfigId", "description" }) })
public class EndpointPath extends BaseAuditEntity {

    @ForeignKey(value = EndpointConfig.class)
    private Long endpointConfigId;

    @ForeignKey
    private DirectionType direction;

    @Column(name = "PATH_NM", length = 64)
    private String name;

    @Column(name = "PATH_DESC", length = 96)
    private String description;

    @Column(name = "PATH", length = 128)
    private String path;
    
    @ListOnly(key = "endpointConfigId", property = "endpointType")
    private EndpointType endpointType;

    @ListOnly(key = "endpointConfigId", property = "name")
    private String endpointConfigName;

    @ListOnly(key = "endpointConfigId", property = "description")
    private String endpointConfigDesc;

    @ListOnly(key = "direction", property = "description")
    private String directionDesc;

    @Override
    public String getDescription() {
        return this.description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EndpointType getEndpointType() {
        return endpointType;
    }

    public void setEndpointType(EndpointType endpointType) {
        this.endpointType = endpointType;
    }

    public Long getEndpointConfigId() {
        return endpointConfigId;
    }

    public void setEndpointConfigId(Long endpointConfigId) {
        this.endpointConfigId = endpointConfigId;
    }

    public DirectionType getDirection() {
        return direction;
    }

    public void setDirection(DirectionType direction) {
        this.direction = direction;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getEndpointConfigName() {
        return endpointConfigName;
    }

    public void setEndpointConfigName(String endpointConfigName) {
        this.endpointConfigName = endpointConfigName;
    }

    public String getEndpointConfigDesc() {
        return endpointConfigDesc;
    }

    public void setEndpointConfigDesc(String endpointConfigDesc) {
        this.endpointConfigDesc = endpointConfigDesc;
    }

    public String getDirectionDesc() {
        return directionDesc;
    }

    public void setDirectionDesc(String directionDesc) {
        this.directionDesc = directionDesc;
    }

}

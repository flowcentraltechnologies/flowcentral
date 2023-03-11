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
package com.flowcentraltech.flowcentral.integration.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntity;
import com.flowcentraltech.flowcentral.common.entities.ParamValues;
import com.flowcentraltech.flowcentral.integration.endpoint.EndpointType;
import com.tcdng.unify.core.annotation.Child;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Policy;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * End-point configuration.
 *
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Policy("endpointconfig-entitypolicy")
@Table(name = "FC_ENDPOINTCONFIG",
        uniqueConstraints = { @UniqueConstraint({ "name" }), @UniqueConstraint({ "description" }) })
public class EndpointConfig extends BaseStatusEntity {

    @ForeignKey
    private EndpointType endpointType;

    @Column(name = "ENDPOINTCONFIG_NM", length = 64)
    private String name;

    @Column(name = "ENDPOINTCONFIG_DESC", length = 96)
    private String description;

    @Column(length = 64)
    private String endpoint;

    @ListOnly(key = "endpointType", property = "description")
    private String endpointTypeDesc;

    @Child(category = "endpointconfig")
    private ParamValues endpointParams;

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

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public EndpointType getEndpointType() {
        return endpointType;
    }

    public void setEndpointType(EndpointType endpointType) {
        this.endpointType = endpointType;
    }

    public String getEndpointTypeDesc() {
        return endpointTypeDesc;
    }

    public void setEndpointTypeDesc(String endpointTypeDesc) {
        this.endpointTypeDesc = endpointTypeDesc;
    }

    public ParamValues getEndpointParams() {
        return endpointParams;
    }

    public void setEndpointParams(ParamValues endpointParams) {
        this.endpointParams = endpointParams;
    }

}

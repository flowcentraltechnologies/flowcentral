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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.constants.DashboardColumnsType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.DashboardColumnsTypeXmlAdapter;

/**
 * Dashboard section configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class DashboardSectionConfig extends BaseConfig {

    @JsonSerialize(using = DashboardColumnsTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = DashboardColumnsTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private DashboardColumnsType type;

    @JacksonXmlProperty(isAttribute = true)
    private int index;

    @JacksonXmlProperty(isAttribute = true)
    private Integer height;

    public DashboardSectionConfig() {
        type = DashboardColumnsType.TYPE_1;
    }

    public DashboardColumnsType getType() {
        return type;
    }

    public void setType(DashboardColumnsType type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

}

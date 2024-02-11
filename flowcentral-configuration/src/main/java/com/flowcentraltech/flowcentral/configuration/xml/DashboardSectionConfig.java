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

import com.flowcentraltech.flowcentral.configuration.constants.DashboardColumnsType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.DashboardColumnsTypeXmlAdapter;

/**
 * Dashboard section configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class DashboardSectionConfig {

    private DashboardColumnsType type;

    private int index;

    private Integer height;

    public DashboardSectionConfig() {
        type = DashboardColumnsType.TYPE_1;
    }

    public DashboardColumnsType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(DashboardColumnsTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setType(DashboardColumnsType type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    @XmlAttribute
    public void setIndex(int index) {
        this.index = index;
    }

    public Integer getHeight() {
        return height;
    }

    @XmlAttribute
    public void setHeight(Integer height) {
        this.height = height;
    }

}

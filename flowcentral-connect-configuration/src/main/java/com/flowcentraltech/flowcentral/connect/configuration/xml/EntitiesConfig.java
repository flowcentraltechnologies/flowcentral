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
package com.flowcentraltech.flowcentral.connect.configuration.xml;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Entities configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EntitiesConfig {

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "entity")
    private List<EntityConfig> entityList;

    public List<EntityConfig> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<EntityConfig> entityList) {
        this.entityList = entityList;
    }

}

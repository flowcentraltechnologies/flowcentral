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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.tcdng.unify.core.util.xml.adapter.CDataXmlAdapter;

/**
 * Help sheet configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "helpSheet")
public class HelpSheetConfig extends BaseNameConfig {

    @JacksonXmlProperty(isAttribute = true)
    private String entity;

    @JsonSerialize(using = CDataXmlAdapter.Serializer.class)
    @JsonDeserialize(using = CDataXmlAdapter.Deserializer.class)
    @JacksonXmlProperty
    private String helpOverview;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "entry")
    private List<HelpEntryConfig> entryList;

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getHelpOverview() {
        return helpOverview;
    }

    public void setHelpOverview(String helpOverview) {
        this.helpOverview = helpOverview;
    }

    public List<HelpEntryConfig> getEntryList() {
        return entryList;
    }

    public void setEntryList(List<HelpEntryConfig> entryList) {
        this.entryList = entryList;
    }

}

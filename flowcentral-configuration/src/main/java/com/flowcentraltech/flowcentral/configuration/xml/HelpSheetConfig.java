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

package com.flowcentraltech.flowcentral.configuration.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.tcdng.unify.core.util.xml.adapter.CDataXmlAdapter;

/**
 * Help sheet configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "helpSheet")
public class HelpSheetConfig extends BaseRootConfig {

    @JacksonXmlProperty(isAttribute = true)
    private String entity;

    @JsonSerialize(using = CDataXmlAdapter.Serializer.class)
    @JsonDeserialize(using = CDataXmlAdapter.Deserializer.class)
    @JacksonXmlProperty
    private String helpOverview;

    @JacksonXmlProperty
    private HelpEntriesConfig entries;

    public HelpSheetConfig() {
        super("flowcentral-helpsheet-4.0.0.xsd");
    }
    
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

    public HelpEntriesConfig getEntries() {
        return entries;
    }

    public void setEntries(HelpEntriesConfig entries) {
        this.entries = entries;
    }

}

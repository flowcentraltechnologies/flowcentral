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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.constants.ChildListActionType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.ChildListActionTypeXmlAdapter;

/**
 * Applet filter configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppletFilterConfig extends FilterConfig {

    @JacksonXmlProperty(isAttribute = true)
    private String preferredForm;
    
    @JacksonXmlProperty(isAttribute = true)
    private String preferredChildListApplet;
    
    @JsonSerialize(using = ChildListActionTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = ChildListActionTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private ChildListActionType childListActionType;
    
    @JacksonXmlProperty(isAttribute = true)
    private boolean quickFilter;

    public String getPreferredForm() {
        return preferredForm;
    }

    public void setPreferredForm(String preferredForm) {
        this.preferredForm = preferredForm;
    }

    public String getPreferredChildListApplet() {
        return preferredChildListApplet;
    }

    public  void setPreferredChildListApplet(String preferredChildListApplet) {
        this.preferredChildListApplet = preferredChildListApplet;
    }

    public ChildListActionType getChildListActionType() {
        return childListActionType;
    }

    public void setChildListActionType(ChildListActionType childListActionType) {
        this.childListActionType = childListActionType;
    }

    public boolean isQuickFilter() {
        return quickFilter;
    }

    public void setQuickFilter(boolean quickFilter) {
        this.quickFilter = quickFilter;
    }

}

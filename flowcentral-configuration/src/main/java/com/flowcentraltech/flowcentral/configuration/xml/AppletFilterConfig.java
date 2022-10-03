/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.configuration.constants.ChildListActionType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.ChildListActionTypeXmlAdapter;

/**
 * Applet filter configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppletFilterConfig extends FilterConfig {

    private String preferredForm;
    
    private String preferredChildListApplet;
    
    private ChildListActionType childListActionType;
    
    private boolean quickFilter;

    private String filterGenerator;

    private String filterGeneratorRule;

    public String getPreferredForm() {
        return preferredForm;
    }

    @XmlAttribute
    public void setPreferredForm(String preferredForm) {
        this.preferredForm = preferredForm;
    }

    public String getPreferredChildListApplet() {
        return preferredChildListApplet;
    }

    @XmlAttribute
    public  void setPreferredChildListApplet(String preferredChildListApplet) {
        this.preferredChildListApplet = preferredChildListApplet;
    }

    public ChildListActionType getChildListActionType() {
        return childListActionType;
    }

    @XmlJavaTypeAdapter(ChildListActionTypeXmlAdapter.class)
    @XmlAttribute
    public void setChildListActionType(ChildListActionType childListActionType) {
        this.childListActionType = childListActionType;
    }

    public boolean isQuickFilter() {
        return quickFilter;
    }

    @XmlAttribute
    public void setQuickFilter(boolean quickFilter) {
        this.quickFilter = quickFilter;
    }

    public String getFilterGenerator() {
        return filterGenerator;
    }

    @XmlAttribute
    public void setFilterGenerator(String filterGenerator) {
        this.filterGenerator = filterGenerator;
    }

    public String getFilterGeneratorRule() {
        return filterGeneratorRule;
    }

    @XmlAttribute
    public void setFilterGeneratorRule(String filterGeneratorRule) {
        this.filterGeneratorRule = filterGeneratorRule;
    }

}

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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.flowcentraltech.flowcentral.configuration.constants.AppletType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.AppletTypeXmlAdapter;
import com.tcdng.unify.core.util.xml.MarshalFalseToNullXmlAdapter;

/**
 * Applet configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppletConfig extends BaseNameConfig {

    private AppletType type;

    private String entity;

    private String icon;

    private String routeToApplet;
    
    private String path;

    private String baseField;

    private String assignField;

    private String assignDescField;

    private String pseudoDeleteField;
    
    private int displayIndex;

    private Boolean menuAccess;
    
    private Boolean supportOpenInNewWindow;

    private Boolean allowSecondaryTenants;

    private List<AppletPropConfig> propList;

    private List<AppletFilterConfig> filterList;

    private List<AppletSetValuesConfig> valuesList;
    
    private List<AppletAlertConfig> alertList;
    
    public AppletConfig() {
        this.menuAccess = Boolean.FALSE;
        this.supportOpenInNewWindow = Boolean.FALSE;
        this.allowSecondaryTenants = Boolean.FALSE;;
    }
    
    public AppletType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(AppletTypeXmlAdapter.class)
    @XmlAttribute(required = true)
    public void setType(AppletType type) {
        this.type = type;
    }

    public String getEntity() {
        return entity;
    }

    @XmlAttribute
    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getIcon() {
        return icon;
    }

    @XmlAttribute
    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRouteToApplet() {
        return routeToApplet;
    }

    @XmlAttribute
    public void setRouteToApplet(String routeToApplet) {
        this.routeToApplet = routeToApplet;
    }

    public String getPath() {
        return path;
    }

    @XmlAttribute
    public void setPath(String path) {
        this.path = path;
    }

    public String getBaseField() {
        return baseField;
    }

    @XmlAttribute
    public void setBaseField(String baseField) {
        this.baseField = baseField;
    }

    public String getAssignField() {
        return assignField;
    }

    @XmlAttribute
    public void setAssignField(String assignField) {
        this.assignField = assignField;
    }

    public String getAssignDescField() {
        return assignDescField;
    }

    @XmlAttribute
    public void setAssignDescField(String assignDescField) {
        this.assignDescField = assignDescField;
    }

    public String getPseudoDeleteField() {
        return pseudoDeleteField;
    }

    @XmlAttribute
    public void setPseudoDeleteField(String pseudoDeleteField) {
        this.pseudoDeleteField = pseudoDeleteField;
    }

    public Boolean getMenuAccess() {
        return menuAccess;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setMenuAccess(Boolean menuAccess) {
        this.menuAccess = menuAccess;
    }

    public Boolean getSupportOpenInNewWindow() {
        return supportOpenInNewWindow;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setSupportOpenInNewWindow(Boolean supportOpenInNewWindow) {
        this.supportOpenInNewWindow = supportOpenInNewWindow;
    }

    public Boolean getAllowSecondaryTenants() {
        return allowSecondaryTenants;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setAllowSecondaryTenants(Boolean allowSecondaryTenants) {
        this.allowSecondaryTenants = allowSecondaryTenants;
    }

    public int getDisplayIndex() {
        return displayIndex;
    }

    @XmlAttribute
    public void setDisplayIndex(int displayIndex) {
        this.displayIndex = displayIndex;
    }

    public List<AppletPropConfig> getPropList() {
        return propList;
    }

    @XmlElement(name = "property", required = true)
    public void setPropList(List<AppletPropConfig> propList) {
        this.propList = propList;
    }

    public List<AppletFilterConfig> getFilterList() {
        return filterList;
    }

    @XmlElement(name = "filter", required = true)
    public void setFilterList(List<AppletFilterConfig> filterList) {
        this.filterList = filterList;
    }

    public List<AppletSetValuesConfig> getValuesList() {
        return valuesList;
    }

    @XmlElement(name = "values")
    public void setValuesList(List<AppletSetValuesConfig> valuesList) {
        this.valuesList = valuesList;
    }

    public List<AppletAlertConfig> getAlertList() {
        return alertList;
    }

    @XmlElement(name = "alert")
    public void setAlertList(List<AppletAlertConfig> alertList) {
        this.alertList = alertList;
    }

}

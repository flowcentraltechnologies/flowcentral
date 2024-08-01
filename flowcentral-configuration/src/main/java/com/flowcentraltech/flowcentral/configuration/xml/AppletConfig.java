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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.constants.AppletType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.AppletTypeXmlAdapter;

/**
 * Applet configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppletConfig extends BaseNameConfig {

    @JsonSerialize(using = AppletTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = AppletTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true)
    private AppletType type;

    @JacksonXmlProperty(isAttribute = true)
    private String entity;

    @JacksonXmlProperty(isAttribute = true)
    private String icon;

    @JacksonXmlProperty(isAttribute = true)
    private String routeToApplet;
    
    @JacksonXmlProperty(isAttribute = true)
    private String path;

    @JacksonXmlProperty(isAttribute = true)
    private String baseField;

    @JacksonXmlProperty(isAttribute = true)
    private String assignField;

    @JacksonXmlProperty(isAttribute = true)
    private String assignDescField;

    @JacksonXmlProperty(isAttribute = true)
    private String pseudoDeleteField;

    @JacksonXmlProperty(isAttribute = true)
    private String titleFormat;
    
    @JacksonXmlProperty(isAttribute = true)
    private int displayIndex;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean menuAccess;
    
    @JacksonXmlProperty(isAttribute = true)
    private Boolean supportOpenInNewWindow;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean allowSecondaryTenants;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "property")
    private List<AppletPropConfig> propList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "routeToApplet")
    private List<AppletRouteToAppletConfig> routeToAppletList;
    
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "filter")
    private List<AppletFilterConfig> filterList;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "values")
    private List<AppletSetValuesConfig> valuesList;
    
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "alert")
    private List<AppletAlertConfig> alertList;
    
    public AppletConfig() {
        this.menuAccess = Boolean.FALSE;
        this.supportOpenInNewWindow = Boolean.FALSE;
        this.allowSecondaryTenants = Boolean.FALSE;;
    }
    
    public AppletType getType() {
        return type;
    }

    public void setType(AppletType type) {
        this.type = type;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getRouteToApplet() {
        return routeToApplet;
    }

    public void setRouteToApplet(String routeToApplet) {
        this.routeToApplet = routeToApplet;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBaseField() {
        return baseField;
    }

    public void setBaseField(String baseField) {
        this.baseField = baseField;
    }

    public String getAssignField() {
        return assignField;
    }

    public void setAssignField(String assignField) {
        this.assignField = assignField;
    }

    public String getAssignDescField() {
        return assignDescField;
    }

    public void setAssignDescField(String assignDescField) {
        this.assignDescField = assignDescField;
    }

    public String getPseudoDeleteField() {
        return pseudoDeleteField;
    }

    public void setPseudoDeleteField(String pseudoDeleteField) {
        this.pseudoDeleteField = pseudoDeleteField;
    }

    public Boolean getMenuAccess() {
        return menuAccess;
    }

    public void setMenuAccess(Boolean menuAccess) {
        this.menuAccess = menuAccess;
    }

    public String getTitleFormat() {
        return titleFormat;
    }

    public void setTitleFormat(String titleFormat) {
        this.titleFormat = titleFormat;
    }

    public Boolean getSupportOpenInNewWindow() {
        return supportOpenInNewWindow;
    }

    public void setSupportOpenInNewWindow(Boolean supportOpenInNewWindow) {
        this.supportOpenInNewWindow = supportOpenInNewWindow;
    }

    public Boolean getAllowSecondaryTenants() {
        return allowSecondaryTenants;
    }

    public void setAllowSecondaryTenants(Boolean allowSecondaryTenants) {
        this.allowSecondaryTenants = allowSecondaryTenants;
    }

    public int getDisplayIndex() {
        return displayIndex;
    }

    public void setDisplayIndex(int displayIndex) {
        this.displayIndex = displayIndex;
    }

    public List<AppletPropConfig> getPropList() {
        return propList;
    }

    public void setPropList(List<AppletPropConfig> propList) {
        this.propList = propList;
    }

    public List<AppletRouteToAppletConfig> getRouteToAppletList() {
        return routeToAppletList;
    }

    public void setRouteToAppletList(List<AppletRouteToAppletConfig> routeToAppletList) {
        this.routeToAppletList = routeToAppletList;
    }

    public List<AppletFilterConfig> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<AppletFilterConfig> filterList) {
        this.filterList = filterList;
    }

    public List<AppletSetValuesConfig> getValuesList() {
        return valuesList;
    }

    public void setValuesList(List<AppletSetValuesConfig> valuesList) {
        this.valuesList = valuesList;
    }

    public List<AppletAlertConfig> getAlertList() {
        return alertList;
    }

    public void setAlertList(List<AppletAlertConfig> alertList) {
        this.alertList = alertList;
    }

}

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

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.flowcentraltech.flowcentral.configuration.constants.AppletType;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.AppletTypeXmlAdapter;

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

    private int displayIndex;

    private boolean menuAccess;

    private List<AppletPropConfig> propList;

    private List<FilterConfig> filterList;

    private List<AppletSetValuesConfig> setValuesList;
    
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

    public boolean isMenuAccess() {
        return menuAccess;
    }

    @XmlAttribute(required = true)
    public void setMenuAccess(boolean menuAccess) {
        this.menuAccess = menuAccess;
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

    public List<FilterConfig> getFilterList() {
        return filterList;
    }

    @XmlElement(name = "filter", required = true)
    public void setFilterList(List<FilterConfig> filterList) {
        this.filterList = filterList;
    }

    public List<AppletSetValuesConfig> getSetValuesList() {
        return setValuesList;
    }

    @XmlElement(name = "setValues")
    public void setSetValuesList(List<AppletSetValuesConfig> setValuesList) {
        this.setValuesList = setValuesList;
    }

}

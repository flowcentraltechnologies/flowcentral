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
package com.flowcentraltech.flowcentral.application.entities;

import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.AppletType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Application applet entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_APPLET")
public class AppApplet extends BaseApplicationEntity {

    @ForeignKey(name = "APPLET_TY")
    private AppletType type;

    @Column(length = 128, nullable = true)
    private String entity;

    @Column(length = 128)
    private String label;

    @Column(length = 128, nullable = true)
    private String icon;

    @Column(length = 128, nullable = true)
    private String routeToApplet;

    @Column(length = 128, nullable = true)
    private String path;

    @Column(length = 64, nullable = true)
    private String baseField;

    @Column(length = 64, nullable = true)
    private String assignField;

    @Column(length = 64, nullable = true)
    private String assignDescField;

    @Column(length = 64, nullable = true)
    private String pseudoDeleteField;

    @Column(length = 1024, nullable = true)
    private String titleFormat;

    @Column
    private boolean menuAccess;

    @Column
    private boolean supportOpenInNewWindow;

    @Column
    private boolean allowSecondaryTenants;

    @Column
    private int displayIndex;
    
    @Column
    private long workflowVersionNo;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    @ChildList
    private List<AppAppletProp> propList;

    @ChildList
    private List<AppAppletRouteToApplet> routeToAppletList;
    
    @ChildList
    private List<AppAppletFilter> filterList;

    @ChildList
    private List<AppAppletSetValues> setValuesList;
    
    @ChildList
    private List<AppAppletAlert> alertList;
    
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public String getTitleFormat() {
        return titleFormat;
    }

    public void setTitleFormat(String titleFormat) {
        this.titleFormat = titleFormat;
    }

    public boolean isMenuAccess() {
        return menuAccess;
    }

    public void setMenuAccess(boolean menuAccess) {
        this.menuAccess = menuAccess;
    }

    public boolean isSupportOpenInNewWindow() {
        return supportOpenInNewWindow;
    }

    public void setSupportOpenInNewWindow(boolean supportOpenInNewWindow) {
        this.supportOpenInNewWindow = supportOpenInNewWindow;
    }

    public boolean isAllowSecondaryTenants() {
        return allowSecondaryTenants;
    }

    public void setAllowSecondaryTenants(boolean allowSecondaryTenants) {
        this.allowSecondaryTenants = allowSecondaryTenants;
    }

    public int getDisplayIndex() {
        return displayIndex;
    }

    public void setDisplayIndex(int displayIndex) {
        this.displayIndex = displayIndex;
    }

    public long getWorkflowVersionNo() {
        return workflowVersionNo;
    }

    public void setWorkflowVersionNo(long workflowVersionNo) {
        this.workflowVersionNo = workflowVersionNo;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public List<AppAppletProp> getPropList() {
        return propList;
    }

    public void setPropList(List<AppAppletProp> propList) {
        this.propList = propList;
    }

    public List<AppAppletRouteToApplet> getRouteToAppletList() {
        return routeToAppletList;
    }

    public void setRouteToAppletList(List<AppAppletRouteToApplet> routeToAppletList) {
        this.routeToAppletList = routeToAppletList;
    }

    public List<AppAppletFilter> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<AppAppletFilter> filterList) {
        this.filterList = filterList;
    }

    public List<AppAppletSetValues> getSetValuesList() {
        return setValuesList;
    }

    public void setSetValuesList(List<AppAppletSetValues> setValuesList) {
        this.setValuesList = setValuesList;
    }

    public List<AppAppletAlert> getAlertList() {
        return alertList;
    }

    public void setAlertList(List<AppAppletAlert> alertList) {
        this.alertList = alertList;
    }

}

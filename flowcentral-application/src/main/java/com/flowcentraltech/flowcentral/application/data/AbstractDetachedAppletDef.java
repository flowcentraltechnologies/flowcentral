/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.data;

import java.util.List;

import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.configuration.constants.AppletType;
import com.tcdng.unify.core.UnifyException;

/**
 * Base class for detached applet definitions.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractDetachedAppletDef implements AppletDef {

    protected final AppletType type;
    
    protected final AppletDef parentAppletDef;
    
    public AbstractDetachedAppletDef(AppletType type, AppletDef parentAppletDef) {
        this.type = type;
        this.parentAppletDef = parentAppletDef;
    }

    @Override
    public AppletDef facade(AppletDef appletDef) {
         return null;
    }

    @Override
    public ApplicationEntityNameParts getNameParts() {
        return parentAppletDef.getNameParts();
    }

    @Override
    public String getLongName() {
        return parentAppletDef.getLongName();
    }

    @Override
    public String getApplicationName() {
        return parentAppletDef.getApplicationName();
    }

    @Override
    public String getDescription() {
        return parentAppletDef.getDescription();
    }

    @Override
    public String getName() {
        return parentAppletDef.getName();
    }

    @Override
    public String getFieldName() {
        return parentAppletDef.getFieldName();
    }

    @Override
    public Long getId() {
        return parentAppletDef.getId();
    }

    @Override
    public long getVersion() {
        return parentAppletDef.getVersion();
    }

    @Override
    public AppletType getType() {
         return type;
    }

    @Override
    public AppletDef getDetachedAppletDef() {
        return null;
    }

    @Override
    public boolean isStudioComponent() {
        return parentAppletDef.isStudioComponent();
    }

    @Override
    public String getOriginApplicationName() {
        return parentAppletDef.getOriginApplicationName();
    }

    @Override
    public String getOriginName() {
        return parentAppletDef.getOriginName();
    }

    @Override
    public String getEntity() {
        return parentAppletDef.getEntity();
    }

    @Override
    public String getAssignDescField() {
        return parentAppletDef.getAssignDescField();
    }

    @Override
    public String getPseudoDeleteField() {
        return parentAppletDef.getPseudoDeleteField();
    }

    @Override
    public String getLabel() {
        return parentAppletDef.getLabel();
    }

    @Override
    public String getIcon() {
        return parentAppletDef.getIcon();
    }

    @Override
    public boolean isFacade() {
        return parentAppletDef.isFacade();
    }

    @Override
    public boolean isWithEntity() {
        return parentAppletDef.isWithEntity();
    }

    @Override
    public boolean isWithAssignDescField() {
        return false;
    }

    @Override
    public boolean isWithPseudoDeleteField() {
        return parentAppletDef.isWithPseudoDeleteField();
    }

    @Override
    public boolean isWithIcon() {
        return parentAppletDef.isWithIcon();
    }

    @Override
    public String getPrivilege() {
        return parentAppletDef.getPrivilege();
    }

    @Override
    public String getRouteToApplet() {
        return parentAppletDef.getRouteToApplet();
    }

    @Override
    public boolean isDescriptiveButtons() {
        return parentAppletDef.isDescriptiveButtons();
    }

    @Override
    public String getViewId() {
        return parentAppletDef.getViewId();
    }

    @Override
    public int getDisplayIndex() {
        return parentAppletDef.getDisplayIndex();
    }

    @Override
    public boolean isOpenWindow() {
        return parentAppletDef.isOpenWindow();
    }

    @Override
    public boolean isMenuAccess() {
        return parentAppletDef.isMenuAccess();
    }

    @Override
    public boolean isAllowSecondaryTenants() {
        return parentAppletDef.isAllowSecondaryTenants();
    }

    @Override
    public List<AppletFilterDef> getPreferredFormFilterList() {
        return parentAppletDef.getPreferredFormFilterList();
    }

    @Override
    public boolean isWithPreferredFormFilters() {
        return parentAppletDef.isWithPreferredFormFilters();
    }

    @Override
    public List<AppletPropDef> getPropDefList() {
        return parentAppletDef.getPropDefList();
    }

    @Override
    public boolean isLabelMatch(String filter) {
        return parentAppletDef.isLabelMatch(filter);
    }

    @Override
    public <T> T getPropValue(Class<T> dataClazz, String name) throws UnifyException {
        return parentAppletDef.getPropValue(dataClazz, name);
    }

    @Override
    public <T> T getPropValue(Class<T> dataClazz, String name, T defVal) throws UnifyException {
        return parentAppletDef.getPropValue(dataClazz, name, defVal);
    }

    @Override
    public boolean isProp(String name) {
        return parentAppletDef.isProp(name);
    }

    @Override
    public boolean isPropWithValue(String name) throws UnifyException {
        return parentAppletDef.isPropWithValue(name);
    }

    @Override
    public AppletPropDef getPropDef(String name) {
        return parentAppletDef.getPropDef(name);
    }

    @Override
    public AppletSetValuesDef getSetValues(String name) {
        return parentAppletDef.getSetValues(name);
    }

    @Override
    public boolean isFilter(String name) {
        return parentAppletDef.isFilter(name);
    }

    @Override
    public AppletFilterDef getFilterDef(String name) {
        return parentAppletDef.getFilterDef(name);
    }

    @Override
    public List<AppletFilterDef> getChildListFilterDefs(String prefferedChildListApplet) {
        return parentAppletDef.getChildListFilterDefs(prefferedChildListApplet);
    }

}

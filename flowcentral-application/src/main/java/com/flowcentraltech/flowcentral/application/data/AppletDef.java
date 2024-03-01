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
package com.flowcentraltech.flowcentral.application.data;

import java.util.List;

import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.configuration.constants.AppletType;
import com.tcdng.unify.core.UnifyException;

/**
 * Applet definition
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface AppletDef {

    AppletDef facade(AppletDef appletDef);

    ApplicationEntityNameParts getNameParts();

    String getLongName();

    String getApplicationName();

    String getName();

    String getDescription();

    String getFieldName();

    Long getId();

    long getVersion();

    AppletType getType();

    AppletDef getMaintainAppletDef();

    AppletDef getListingAppletDef();

    boolean isStudioComponent();

    String getOriginApplicationName();

    String getOriginName();

    String getEntity();

    String getAssignDescField();

    String getPseudoDeleteField();

    String getLabel();

    String getIcon();

    boolean isFacade();

    boolean isWithEntity();

    boolean isWithAssignDescField();

    boolean isWithPseudoDeleteField();

    boolean isWithIcon();

    String getPrivilege();

    String getRouteToApplet();

    String getOpenPath();

    String getOpenDraftPath();

    String getOpenDraftWorkflow();

    String getMaintainOpenPath();

    String getListingOpenPath();

    boolean isDescriptiveButtons();

    String getViewId();

    String getDraftViewId();

    int getDisplayIndex();

    boolean isWithOpenDraftPath();

    boolean isOpenWindow();

    boolean isMenuAccess() ;

    boolean isSupportOpenInNewWindow();
    
    boolean isAllowSecondaryTenants();

    List<AppletFilterDef> getPreferredFormFilterList();

    boolean isWithPreferredFormFilters();

    List<AppletPropDef> getPropDefList();

    boolean isLabelMatch(String filter);

    <T> T getPropValue(Class<T> dataClazz, String name) throws UnifyException;

    <T> T getPropValue(Class<T> dataClazz, String name, T defVal) throws UnifyException;

    boolean isProp(String name);

    boolean isPropWithValue(String name) throws UnifyException;

    AppletPropDef getPropDef(String name);

    AppletSetValuesDef getSetValues(String name);

    boolean isFilter(String name);

    AppletFilterDef getFilterDef(String name);

    List<AppletFilterDef> getChildListFilterDefs(String prefferedChildListApplet);
}

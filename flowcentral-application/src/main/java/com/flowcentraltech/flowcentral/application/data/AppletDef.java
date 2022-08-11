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
package com.flowcentraltech.flowcentral.application.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.configuration.constants.AppletType;
import com.tcdng.unify.convert.util.ConverterUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Applet definition
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppletDef extends BaseApplicationEntityDef {

    private static long viewIdCounter;

    private AppletType type;

    private String entity;

    private String label;

    private String lowerCaseLabel;

    private String privilege;

    private String routeToApplet;

    private String openPath;

    private String originApplicationName;

    private String originName;

    private String viewId;

    private String icon;

    private String assignDescField;

    private int displayIndex;

    private boolean openWindow;

    private boolean menuAccess;

    private boolean descriptiveButtons;

    private List<AppletPropDef> propDefList;

    private Map<String, AppletPropDef> propDefMap;

    private Map<String, AppletSetValuesDef> setValuesDefMap;

    private Map<String, FilterDef> filterDefMap;

    private List<FilterDef> preferredFormFilterList;

    private Map<String, List<FilterDef>> childListAppletFilterDefMap;

    private AppletDef(AppletType type, List<AppletPropDef> propDefList, Map<String, AppletPropDef> propDefMap,
            Map<String, AppletSetValuesDef> setValuesDefMap, Map<String, FilterDef> filterDefMap, String entity,
            String label, String icon, String assignDescField, String routeToApplet, String openPath,
            String originApplicationName, String originName, int displayIndex, boolean openWindow, boolean menuAccess,
            boolean descriptiveButtons, ApplicationEntityNameParts nameParts, String description, Long id,
            long version) {
        super(nameParts, description, id, version);
        this.type = type;
        this.entity = entity;
        this.label = label;
        this.lowerCaseLabel = label != null ? label.toLowerCase() : null;
        this.icon = icon;
        this.assignDescField = assignDescField;
        this.routeToApplet = routeToApplet;
        this.openPath = openPath;
        this.originApplicationName = originApplicationName;
        this.originName = originName;
        this.displayIndex = displayIndex;
        this.openWindow = openWindow;
        this.menuAccess = menuAccess;
        this.descriptiveButtons = descriptiveButtons;
        this.propDefList = propDefList;
        this.propDefMap = propDefMap;
        this.setValuesDefMap = setValuesDefMap;
        this.filterDefMap = filterDefMap;
        List<FilterDef> preferredFormFilterList = new ArrayList<FilterDef>();
        Map<String, List<FilterDef>> childListAppletFilterDefMap = new HashMap<String, List<FilterDef>>();
        for (FilterDef filterDef : filterDefMap.values()) {
            if (filterDef.isWithPreferredForm()) {
                preferredFormFilterList.add(filterDef);
            }

            if (filterDef.isWithPreferredChildListApplet()) {
                List<FilterDef> list = childListAppletFilterDefMap.get(filterDef.getPreferredChildListApplet());
                if (list == null) {
                    list = new ArrayList<FilterDef>();
                    childListAppletFilterDefMap.put(filterDef.getPreferredChildListApplet(), list);
                }
                list.add(filterDef);
            }
        }

        for (String appletName : childListAppletFilterDefMap.keySet()) {
            childListAppletFilterDefMap.replace(appletName,
                    DataUtils.unmodifiableList(childListAppletFilterDefMap.get(appletName)));
        }

        this.preferredFormFilterList = DataUtils.unmodifiableList(preferredFormFilterList);
        this.childListAppletFilterDefMap = DataUtils.unmodifiableMap(childListAppletFilterDefMap);
        this.privilege = PrivilegeNameUtils.getAppletPrivilegeName(nameParts.getLongName());
    }

    private AppletDef(ApplicationEntityNameParts nameParts, String description, Long id, long version) {
        super(nameParts, description, id, version);
    }

    public AppletDef facade(AppletDef _appletDef) {
        AppletDef _facade = new AppletDef(getNameParts(), getDescription(), getId(), getVersion());
        _facade.type = type;
        _facade.entity = entity;
        _facade.label = label;
        _facade.icon = icon;
        _facade.assignDescField = assignDescField;
        _facade.routeToApplet = routeToApplet;
        _facade.openPath = openPath;
        _facade.originApplicationName = originApplicationName;
        _facade.originName = originName;
        _facade.displayIndex = _appletDef.displayIndex;
        _facade.openWindow = openWindow;
        _facade.menuAccess = menuAccess;
        _facade.descriptiveButtons = descriptiveButtons;
        _facade.propDefList = propDefList;
        _facade.propDefMap = propDefMap;
        _facade.filterDefMap = filterDefMap;
        _facade.preferredFormFilterList = preferredFormFilterList;
        _facade.privilege = privilege;
        return _facade;
    }

    public AppletType getType() {
        return type;
    }

    public boolean isStudioComponent() {
        return type.isStudioComponent();
    }
    
    public String getOriginApplicationName() {
        return originApplicationName;
    }

    public String getOriginName() {
        return originName;
    }

    public String getEntity() {
        return entity;
    }

    public String getAssignDescField() {
        return assignDescField;
    }

    public String getLabel() {
        return label;
    }

    public String getIcon() {
        return icon;
    }

    public boolean isFacade() {
        return type.isFacade();
    }

    public boolean isWithEntity() {
        return entity != null;
    }

    public boolean isWithAssignDescField() {
        return assignDescField != null;
    }

    public boolean isWithIcon() {
        return icon != null;
    }

    public String getPrivilege() {
        return privilege;
    }

    public String getRouteToApplet() {
        return routeToApplet;
    }

    public String getOpenPath() {
        return openPath;
    }

    public boolean isDescriptiveButtons() {
        return descriptiveButtons;
    }

    public String getViewId() {
        if (viewId == null) {
            synchronized (this) {
                if (viewId == null) {
                    viewId = "v" + (++viewIdCounter);
                }
            }
        }

        return viewId;
    }

    public int getDisplayIndex() {
        return displayIndex;
    }

    public boolean isOpenWindow() {
        return openWindow;
    }

    public boolean isMenuAccess() {
        return menuAccess;
    }

    public List<FilterDef> getPreferredFormFilterList() {
        return preferredFormFilterList;
    }

    public boolean isWithPreferredFormFilters() {
        return !preferredFormFilterList.isEmpty();
    }

    public List<AppletPropDef> getPropDefList() {
        return propDefList;
    }

    public boolean isLabelMatch(String filter) {
        return lowerCaseLabel != null && lowerCaseLabel.indexOf(filter) >= 0;
    }

    @SuppressWarnings("unchecked")
    public <T> T getPropValue(Class<T> dataClazz, String name) throws UnifyException {
        AppletPropDef appletPropDef = propDefMap.get(name);
        if (appletPropDef != null) {
            return appletPropDef.getValue(dataClazz);
        }

        return (T) ConverterUtils.getNullValue(dataClazz);
    }

    public <T> T getPropValue(Class<T> dataClazz, String name, T defVal) throws UnifyException {
        AppletPropDef appletPropDef = propDefMap.get(name);
        if (appletPropDef != null) {
            return appletPropDef.getValue(dataClazz);
        }

        return defVal;
    }

    public boolean isProp(String name) {
        return propDefMap.containsKey(name);
    }

    public boolean isPropWithValue(String name) throws UnifyException {
        return !StringUtils.isBlank(getPropValue(String.class, name));
    }

    public AppletPropDef getPropDef(String name) {
        AppletPropDef appletPropDef = propDefMap.get(name);
        if (appletPropDef == null) {
            throw new RuntimeException(
                    "Property with name [" + name + "] is unknown for applet definition [" + getName() + "].");
        }

        return appletPropDef;
    }

    public AppletSetValuesDef getSetValues(String name) {
        AppletSetValuesDef appletSetValuesDef = setValuesDefMap.get(name);
        if (appletSetValuesDef == null) {
            throw new RuntimeException(
                    "Set values with name [" + name + "] is unknown for applet definition [" + getName() + "].");
        }

        return appletSetValuesDef;
    }
    
    public boolean isFilter(String name) {
        return filterDefMap.containsKey(name);
    }

    public FilterDef getFilterDef(String name) {
        FilterDef filterDef = filterDefMap.get(name);
        if (filterDef == null) {
            throw new RuntimeException(
                    "Filter with name [" + name + "] is unknown for applet definition [" + getName() + "].");
        }

        return filterDef;
    }

    public List<FilterDef> getChildListFilterDefs(String prefferedChildListApplet) {
        List<FilterDef> result = childListAppletFilterDefMap.get(prefferedChildListApplet);
        return result != null ? result : Collections.emptyList();
    }

    public static Builder newBuilder(AppletType type, String entity, String label, String icon, String assignDescField,
            int displayIndex, boolean menuAccess, boolean descriptiveButtons, String longName, String description) {
        return new Builder(type, entity, label, icon, assignDescField, displayIndex, menuAccess, descriptiveButtons,
                longName, description, null, 0L);
    }

    public static Builder newBuilder(AppletType type, String entity, String label, String icon, String assignDescField,
            int displayIndex, boolean menuAccess, boolean descriptiveButtons, String longName, String description,
            Long id, long version) {
        return new Builder(type, entity, label, icon, assignDescField, displayIndex, menuAccess, descriptiveButtons,
                longName, description, id, version);
    }

    public static class Builder {

        private Map<String, AppletPropDef> propDefMap;

        private Map<String, AppletSetValuesDef> setValuesDefMap;

        private Map<String, FilterDef> filterDefMap;

        private AppletType type;

        private String entity;

        private String label;

        private String icon;

        private String assignDescField;

        private String routeToApplet;

        private String openPath;

        private String originApplicationName;

        private String originName;

        private int displayIndex;

        private boolean openWindow;

        private boolean menuAccess;

        private boolean descriptiveButtons;

        private String longName;

        private String description;

        private Long id;

        private long version;

        public Builder(AppletType type, String entity, String label, String icon, String assignDescField,
                int displayIndex, boolean menuAccess, boolean descriptiveButtons, String longName, String description,
                Long id, long version) {
            this.type = type;
            this.propDefMap = new HashMap<String, AppletPropDef>();
            this.setValuesDefMap = new HashMap<String, AppletSetValuesDef>();
            this.filterDefMap = new HashMap<String, FilterDef>();
            this.entity = entity;
            this.label = label;
            this.icon = icon;
            this.assignDescField = assignDescField;
            this.displayIndex = displayIndex;
            this.menuAccess = menuAccess;
            this.descriptiveButtons = descriptiveButtons;
            this.longName = longName;
            this.description = description;
            this.id = id;
            this.version = version;
        }

        public Builder openPath(String openPath) {
            this.openPath = openPath;
            return this;
        }

        public Builder routeToApplet(String routeToApplet) {
            this.routeToApplet = routeToApplet;
            return this;
        }

        public Builder openWindow(boolean openWindow) {
            this.openWindow = openWindow;
            return this;
        }

        public Builder originApplicationName(String originApplicationName) {
            this.originApplicationName = originApplicationName;
            return this;
        }

        public Builder originName(String originName) {
            this.originName = originName;
            return this;
        }

        public Builder addPropDef(String name, String value) {
            if (propDefMap.containsKey(name)) {
                throw new RuntimeException("Property with name [" + name + "] already exists in this definition.");
            }

            propDefMap.put(name, new AppletPropDef(name, value));
            return this;
        }

        public Builder addSetValuesDef(String name, String description, SetValuesDef setValuesDef) {
            if (setValuesDefMap.containsKey(name)) {
                throw new RuntimeException("Set values with name [" + name + "] already exists in this definition.");
            }

            setValuesDefMap.put(name, new AppletSetValuesDef(setValuesDef, name, description));
            return this;
        }

        public Builder addFilterDef(FilterDef filterDef) {
            if (filterDef != null) {
                if (filterDefMap.containsKey(filterDef.getName())) {
                    throw new RuntimeException(
                            "Filter with name [" + filterDef.getName() + "] already exists in this definition.");
                }

                filterDefMap.put(filterDef.getName(), filterDef);
            }

            return this;
        }

        public AppletDef build() throws UnifyException {
            ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
            if (originApplicationName == null) {
                originApplicationName = nameParts.getApplicationName();
            }
            return new AppletDef(type, DataUtils.unmodifiableList(new ArrayList<AppletPropDef>(propDefMap.values())),
                    DataUtils.unmodifiableMap(propDefMap), DataUtils.unmodifiableMap(setValuesDefMap),
                    DataUtils.unmodifiableMap(filterDefMap), entity, label, icon, assignDescField, routeToApplet,
                    openPath, originApplicationName, originName, displayIndex, openWindow, menuAccess,
                    descriptiveButtons, nameParts, description, id, version);
        }
    }

    @Override
    public String toString() {
        return "AppletDef [type=" + type + ", entity=" + entity + ", label=" + label + ", privilege=" + privilege
                + ", openPath=" + openPath + ", originApplicationName=" + originApplicationName + ", viewId=" + viewId
                + ", icon=" + icon + ", assignDescField=" + assignDescField + ", displayIndex=" + displayIndex
                + ", openWindow=" + openWindow + ", menuAccess=" + menuAccess + ", descriptiveButtons="
                + descriptiveButtons + ", propDefList=" + propDefList + ", propDefMap=" + propDefMap + ", filterDefMap="
                + filterDefMap + ", getLongName()=" + getLongName() + ", getApplicationName()=" + getApplicationName()
                + ", getName()=" + getName() + ", getDescription()=" + getDescription() + ", getId()=" + getId()
                + ", getVersion()=" + getVersion() + "]";
    }

}

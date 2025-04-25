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
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.convert.util.ConverterUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Standard applet definition
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class StandardAppletDef extends BaseApplicationEntityDef implements AppletDef {

    private static long viewIdCounter;

    private AppletType type;

    private String entity;

    private String label;

    private String lowerCaseLabel;

    private String privilege;

    private String routeToApplet;

    private String openPath;

    private String openDraftPath;

    private String openDraftWorkflow;

    private String maintainOpenPath;

    private String listingOpenPath;

    private String originApplicationName;

    private String originName;

    private String viewId;

    private String icon;

    private String assignDescField;

    private String pseudoDeleteField;

    private int displayIndex;

    private boolean openWindow;

    private boolean menuAccess;

    private boolean supportOpenInNewWindow;

    private boolean allowSecondaryTenants;

    private boolean descriptiveButtons;

    private List<StringToken> titleFormat;

    private List<AppletPropDef> propDefList;

    private Map<String, AppletPropDef> propDefMap;

    private Map<String, AppletSetValuesDef> setValuesDefMap;

    private Map<String, AppletFilterDef> filterDefMap;

    private List<AppletFilterDef> preferredFormFilterList;

    private Map<String, List<AppletFilterDef>> childListAppletFilterDefMap;

    private List<String> subAppletList;

    private AppletDef maintainAppletDef;

    private AppletDef listingAppletDef;

    private StandardAppletDef(AppletType type, List<StringToken> titleFormat, List<AppletPropDef> propDefList,
            List<String> subAppletList, Map<String, AppletPropDef> propDefMap,
            Map<String, AppletSetValuesDef> setValuesDefMap, Map<String, AppletFilterDef> filterDefMap, String entity,
            String label, String icon, String assignDescField, String pseudoDeleteField, String routeToApplet,
            String openPath, String openDraftPath, String openDraftWorkflow, String maintainOpenPath,
            String listingOpenPath, String originApplicationName, String originName, int displayIndex,
            boolean openWindow, boolean menuAccess, boolean supportOpenInNewWindow, boolean allowSecondaryTenants,
            boolean descriptiveButtons, ApplicationEntityNameParts nameParts, String description, Long id,
            long version) {
        super(nameParts, description, id, version);
        this.type = type;
        this.entity = entity;
        this.label = label;
        this.lowerCaseLabel = label != null ? label.toLowerCase() : null;
        this.icon = icon;
        this.assignDescField = assignDescField;
        this.pseudoDeleteField = pseudoDeleteField;
        this.routeToApplet = routeToApplet;
        this.openPath = openPath;
        this.openDraftPath = openDraftPath;
        this.openDraftWorkflow = openDraftWorkflow;
        this.maintainOpenPath = maintainOpenPath;
        this.listingOpenPath = listingOpenPath;
        this.originApplicationName = originApplicationName;
        this.originName = originName;
        this.displayIndex = displayIndex;
        this.openWindow = openWindow;
        this.menuAccess = menuAccess;
        this.supportOpenInNewWindow = supportOpenInNewWindow;
        this.allowSecondaryTenants = allowSecondaryTenants;
        this.descriptiveButtons = descriptiveButtons;
        this.titleFormat = titleFormat;
        this.propDefList = propDefList;
        this.subAppletList = subAppletList;
        this.propDefMap = propDefMap;
        this.setValuesDefMap = setValuesDefMap;
        this.filterDefMap = filterDefMap;
        List<AppletFilterDef> preferredFormFilterList = new ArrayList<AppletFilterDef>();
        Map<String, List<AppletFilterDef>> childListAppletFilterDefMap = new HashMap<String, List<AppletFilterDef>>();
        for (AppletFilterDef filterDef : filterDefMap.values()) {
            if (filterDef.isWithPreferredForm()) {
                preferredFormFilterList.add(filterDef);
            }

            if (filterDef.isWithPreferredChildListApplet()) {
                List<AppletFilterDef> list = childListAppletFilterDefMap.get(filterDef.getPreferredChildListApplet());
                if (list == null) {
                    list = new ArrayList<AppletFilterDef>();
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

    private StandardAppletDef(ApplicationEntityNameParts nameParts, String description, Long id, long version) {
        super(nameParts, description, id, version);
    }

    public AppletDef facade(AppletDef _appletDef) {
        return new FacadeDetachedAppletDef(this);
    }

    @Override
    public AppletType getType() {
        return type;
    }

    @Override
    public AppletDef getMaintainAppletDef() {
        if (maintainAppletDef == null) {
            synchronized (this) {
                if (maintainAppletDef == null) {
                    maintainAppletDef = new CreateEntityDetachedAppletDef(this);
                }
            }
        }

        return maintainAppletDef;
    }

    @Override
    public AppletDef getListingAppletDef() {
        if (listingAppletDef == null) {
            synchronized (this) {
                if (listingAppletDef == null) {
                    listingAppletDef = new ListingDetachedAppletDef(this);
                }
            }
        }

        return listingAppletDef;
    }

    @Override
    public boolean isStudioComponent() {
        return type.isStudioComponent();
    }

    @Override
    public String getOriginApplicationName() {
        return originApplicationName;
    }

    @Override
    public String getOriginName() {
        return originName;
    }

    @Override
    public String getEntity() {
        return entity;
    }

    @Override
    public String getAssignDescField() {
        return assignDescField;
    }

    @Override
    public String getPseudoDeleteField() {
        return pseudoDeleteField;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getLowerCaseLabel() {
        return lowerCaseLabel;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public boolean isFacade() {
        return type.isFacade();
    }

    @Override
    public boolean isMultiFacade() {
        return type.isMultiFacade();
    }

    @Override
    public boolean isWithEntity() {
        return entity != null;
    }

    @Override
    public boolean isWithAssignDescField() {
        return assignDescField != null;
    }

    @Override
    public boolean isWithPseudoDeleteField() {
        return pseudoDeleteField != null;
    }

    @Override
    public boolean isWithIcon() {
        return icon != null;
    }

    @Override
    public String getPrivilege() {
        return privilege;
    }

    @Override
    public String getRouteToApplet() {
        return routeToApplet;
    }

    @Override
    public String getOpenPath() {
        return openPath;
    }

    @Override
    public String getOpenDraftPath() {
        return openDraftPath;
    }

    @Override
    public String getOpenDraftWorkflow() {
        return openDraftWorkflow;
    }

    @Override
    public String getMaintainOpenPath() {
        return maintainOpenPath;
    }

    @Override
    public String getListingOpenPath() {
        return listingOpenPath;
    }

    @Override
    public boolean isDescriptiveButtons() {
        return descriptiveButtons;
    }

    @Override
    public List<StringToken> getTitleFormat() {
        return titleFormat;
    }

    @Override
    public boolean isWithTitleFormat() {
        return titleFormat != null;
    }

    @Override
    public List<String> getSubAppletList() {
        return subAppletList;
    }

    @Override
    public boolean isWithSubApplets() {
        return !DataUtils.isBlank(subAppletList);
    }

    @Override
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

    @Override
    public String getDraftViewId() {
        return getViewId() + "_dft";
    }

    @Override
    public int getDisplayIndex() {
        return displayIndex;
    }

    @Override
    public boolean isWithOpenDraftPath() {
        return !StringUtils.isBlank(openDraftPath);
    }

    @Override
    public boolean isOpenWindow() {
        return openWindow;
    }

    @Override
    public boolean isMenuAccess() {
        return menuAccess;
    }

    @Override
    public boolean isSupportOpenInNewWindow() {
        return supportOpenInNewWindow;
    }

    @Override
    public boolean isAllowSecondaryTenants() {
        return allowSecondaryTenants;
    }

    @Override
    public List<AppletFilterDef> getPreferredFormFilterList() {
        return preferredFormFilterList;
    }

    @Override
    public boolean isWithPreferredFormFilters() {
        return !preferredFormFilterList.isEmpty();
    }

    @Override
    public List<AppletPropDef> getPropDefList() {
        return propDefList;
    }

    @Override
    public boolean isLabelMatch(String filter) {
        return lowerCaseLabel != null && lowerCaseLabel.indexOf(filter) >= 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getPropValue(Class<T> dataClazz, String name) throws UnifyException {
        AppletPropDef appletPropDef = propDefMap.get(name);
        if (appletPropDef != null) {
            return appletPropDef.getValue(dataClazz);
        }

        return (T) ConverterUtils.getNullValue(dataClazz);
    }

    @Override
    public <T> T getPropValue(Class<T> dataClazz, String name, T defVal) throws UnifyException {
        AppletPropDef appletPropDef = propDefMap.get(name);
        if (appletPropDef != null) {
            return appletPropDef.getValue(dataClazz);
        }

        return defVal;
    }

    @Override
    public boolean isProp(String name) {
        return propDefMap.containsKey(name);
    }

    @Override
    public boolean isPropWithValue(String name) throws UnifyException {
        return !StringUtils.isBlank(getPropValue(String.class, name));
    }

    @Override
    public AppletPropDef getPropDef(String name) {
        AppletPropDef appletPropDef = propDefMap.get(name);
        if (appletPropDef == null) {
            throw new RuntimeException(
                    "Property with name [" + name + "] is unknown for applet definition [" + getName() + "].");
        }

        return appletPropDef;
    }

    @Override
    public AppletSetValuesDef getSetValues(String name) {
        AppletSetValuesDef appletSetValuesDef = setValuesDefMap.get(name);
        if (appletSetValuesDef == null) {
            throw new RuntimeException(
                    "Set values with name [" + name + "] is unknown for applet definition [" + getName() + "].");
        }

        return appletSetValuesDef;
    }

    @Override
    public boolean isFilter(String name) {
        return filterDefMap.containsKey(name);
    }

    @Override
    public AppletFilterDef getFilterDef(String name) {
        AppletFilterDef filterDef = filterDefMap.get(name);
        if (filterDef == null) {
            throw new RuntimeException(
                    "Filter with name [" + name + "] is unknown for applet definition [" + getName() + "].");
        }

        return filterDef;
    }

    @Override
    public List<AppletFilterDef> getChildListFilterDefs(String prefferedChildListApplet) {
        List<AppletFilterDef> result = childListAppletFilterDefMap.get(prefferedChildListApplet);
        return result != null ? result : Collections.emptyList();
    }

    public static Builder newBuilder(AppletType type, String entity, String label, String icon, String assignDescField,
            String pseudoDeleteField, int displayIndex, boolean menuAccess, boolean supportOpenInNewWindow,
            boolean allowSecondaryTenants, boolean descriptiveButtons, String longName, String description) {
        return new Builder(type, entity, label, icon, assignDescField, pseudoDeleteField, displayIndex, menuAccess,
                supportOpenInNewWindow, allowSecondaryTenants, descriptiveButtons, longName, description, null, 0L);
    }

    public static Builder newBuilder(AppletType type, String entity, String label, String icon, String assignDescField,
            String pseudoDeleteField, int displayIndex, boolean menuAccess, boolean supportOpenInNewWindow,
            boolean allowSecondaryTenants, boolean descriptiveButtons, String longName, String description, Long id,
            long version) {
        return new Builder(type, entity, label, icon, assignDescField, pseudoDeleteField, displayIndex, menuAccess,
                supportOpenInNewWindow, allowSecondaryTenants, descriptiveButtons, longName, description, id, version);
    }

    public static class Builder {

        private Map<String, AppletPropDef> propDefMap;

        private Map<String, AppletSetValuesDef> setValuesDefMap;

        private Map<String, AppletFilterDef> filterDefMap;

        private List<StringToken> titleFormat;

        private List<String> subAppletList;

        private AppletType type;

        private String entity;

        private String label;

        private String icon;

        private String assignDescField;

        private String pseudoDeleteField;

        private String routeToApplet;

        private String openPath;

        private String openDraftPath;

        private String openDraftWorkflow;

        private String maintainOpenPath;

        private String listingOpenPath;

        private String originApplicationName;

        private String originName;

        private int displayIndex;

        private boolean openWindow;

        private boolean menuAccess;

        private boolean supportOpenInNewWindow;

        private boolean allowSecondaryTenants;

        private boolean descriptiveButtons;

        private String longName;

        private String description;

        private Long id;

        private long version;

        public Builder(AppletType type, String entity, String label, String icon, String assignDescField,
                String pseudoDeleteField, int displayIndex, boolean menuAccess, boolean supportOpenInNewWindow,
                boolean allowSecondaryTenants, boolean descriptiveButtons, String longName, String description, Long id,
                long version) {
            this.type = type;
            this.propDefMap = new HashMap<String, AppletPropDef>();
            this.setValuesDefMap = new HashMap<String, AppletSetValuesDef>();
            this.filterDefMap = new HashMap<String, AppletFilterDef>();
            this.entity = entity;
            this.label = label;
            this.icon = icon;
            this.assignDescField = assignDescField;
            this.pseudoDeleteField = pseudoDeleteField;
            this.displayIndex = displayIndex;
            this.menuAccess = menuAccess;
            this.supportOpenInNewWindow = supportOpenInNewWindow;
            this.allowSecondaryTenants = allowSecondaryTenants;
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

        public Builder maintainOpenPath(String maintainOpenPath) {
            this.maintainOpenPath = maintainOpenPath;
            return this;
        }

        public Builder openDraftPath(String openDraftPath) {
            this.openDraftPath = openDraftPath;
            return this;
        }

        public Builder openDraftWorkflow(String openDraftWorkflow) {
            this.openDraftWorkflow = openDraftWorkflow;
            return this;
        }

        public Builder listingOpenPath(String listingOpenPath) {
            this.listingOpenPath = listingOpenPath;
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

        public Builder titleFormat(List<StringToken> titleFormat) {
            this.titleFormat = titleFormat;
            return this;
        }

        public Builder subAppletList(List<String> subAppletList) {
            this.subAppletList = subAppletList;
            return this;
        }

        public Builder addPropDef(String name, String value) {
            if (propDefMap.containsKey(name)) {
                throw new RuntimeException("Property with name [" + name + "] already exists in this definition.");
            }

            propDefMap.put(name, new AppletPropDef(name, value));
            return this;
        }

        @SuppressWarnings("unchecked")
        public <T> T getPropValue(Class<T> dataClazz, String name) throws UnifyException {
            AppletPropDef appletPropDef = propDefMap.get(name);
            if (appletPropDef != null) {
                return appletPropDef.getValue(dataClazz);
            }

            return (T) ConverterUtils.getNullValue(dataClazz);
        }

        public Builder addSetValuesDef(String name, String description, SetValuesDef setValuesDef) {
            if (setValuesDefMap.containsKey(name)) {
                throw new RuntimeException("Set values with name [" + name + "] already exists in this definition.");
            }

            setValuesDefMap.put(name, new AppletSetValuesDef(setValuesDef, name, description));
            return this;
        }

        public Builder addFilterDef(AppletFilterDef filterDef) {
            if (filterDef != null) {
                if (filterDefMap.containsKey(filterDef.getName())) {
                    throw new RuntimeException(
                            "Filter with name [" + filterDef.getName() + "] already exists in this definition.");
                }

                filterDefMap.put(filterDef.getName(), filterDef);
            }

            return this;
        }

        public StandardAppletDef build() throws UnifyException {
            ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
            if (originApplicationName == null) {
                originApplicationName = nameParts.getApplicationName();
            }
            return new StandardAppletDef(type, titleFormat,
                    DataUtils.unmodifiableList(new ArrayList<AppletPropDef>(propDefMap.values())), subAppletList,
                    DataUtils.unmodifiableMap(propDefMap), DataUtils.unmodifiableMap(setValuesDefMap),
                    DataUtils.unmodifiableMap(filterDefMap), entity, label, icon, assignDescField, pseudoDeleteField,
                    routeToApplet, openPath, openDraftPath, openDraftWorkflow, maintainOpenPath, listingOpenPath,
                    originApplicationName, originName, displayIndex, openWindow, menuAccess, supportOpenInNewWindow,
                    allowSecondaryTenants, descriptiveButtons, nameParts, description, id, version);
        }
    }

}

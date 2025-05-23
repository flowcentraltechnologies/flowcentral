/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.workflow.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.BaseApplicationEntityDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Workflow definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class WfDef extends BaseApplicationEntityDef {

    private final String entity;

    private Map<String, WfStepDef> steps;

    private WfStepDef startStepDef;

    private WfStepDef errorStepDef;

    private List<AppletDef> appletList;

    private Map<String, WfFilterDef> filterDefMap;

    private Map<String, WfSetValuesDef> setValuesDefMap;

    private List<WfSetValuesDef> onEntrySetValuesList;

    private List<StringToken> descFormat;

    private Set<String> onEntrySetValuesFields;

    private boolean supportMultiItemAction;

    private WfDef(String entity, WfStepDef startStepDef, WfStepDef errorStepDef, Map<String, WfStepDef> steps,
            Map<String, WfFilterDef> filterDefMap, Map<String, WfSetValuesDef> setValuesDefMap,
            List<StringToken> descFormat, boolean supportMultiItemAction, ApplicationEntityNameParts nameParts,
            String description, Long id, long version) {
        super(nameParts, description, id, version);
        this.entity = entity;
        this.startStepDef = startStepDef;
        this.errorStepDef = errorStepDef;
        this.steps = steps;
        this.filterDefMap = filterDefMap;
        this.setValuesDefMap = setValuesDefMap;
        this.descFormat = descFormat;
        this.supportMultiItemAction = supportMultiItemAction;
    }

    public WfStepDef getStartStepDef() {
        return startStepDef;
    }

    public WfStepDef getErrorStepDef() {
        return errorStepDef;
    }

    public List<StringToken> getDescFormat() {
        return descFormat;
    }

    public boolean isWithDescFormat() {
        return !descFormat.isEmpty();
    }

    public boolean isSupportMultiItemAction() {
        return supportMultiItemAction;
    }

    public WfSetValuesDef getSetValuesDef(String name) {
        WfSetValuesDef wfSetValuesDef = setValuesDefMap.get(name);
        if (wfSetValuesDef == null) {
            throw new RuntimeException(
                    "Workflow [" + getLongName() + "] does not have a setvalues definition [" + name + "].");
        }

        return wfSetValuesDef;
    }

    public boolean isWithOnEntrySetValuesList() {
        return !DataUtils.isBlank(getOnEntrySetValuesList());
    }

    public List<WfSetValuesDef> getOnEntrySetValuesList() {
        if (onEntrySetValuesList == null) {
            synchronized (this) {
                if (onEntrySetValuesList == null) {
                    onEntrySetValuesList = new ArrayList<WfSetValuesDef>();
                    for (WfSetValuesDef wfSetValuesDef : setValuesDefMap.values()) {
                        if (wfSetValuesDef.isOnEntry()) {
                            onEntrySetValuesList.add(wfSetValuesDef);
                        }
                    }

                    onEntrySetValuesList = DataUtils.unmodifiableList(onEntrySetValuesList);
                }
            }
        }

        return onEntrySetValuesList;
    }

    public Set<String> getOnEntrySetValuesFields() {
        if (onEntrySetValuesFields == null) {
            synchronized (this) {
                if (onEntrySetValuesFields == null) {
                    onEntrySetValuesFields = new HashSet<String>();
                    for (WfSetValuesDef wfSetValuesDef : getOnEntrySetValuesList()) {
                        if (wfSetValuesDef.getSetValues() != null) {
                            onEntrySetValuesFields.addAll(wfSetValuesDef.getSetValues().getFields());
                        }
                    }

                    onEntrySetValuesFields = DataUtils.unmodifiableSet(onEntrySetValuesFields);
                }
            }
        }

        return onEntrySetValuesFields;
    }

    public String getEntity() {
        return entity;
    }

    public AppletDef getAppletDef(String wfStepName) {
        return getWfStepDef(wfStepName).getAppletDef();
    }

    public List<AppletDef> getAppletDefs() {
        if (appletList == null) {
            appletList = new ArrayList<AppletDef>();
            for (WfStepDef wfStepDef : steps.values()) {
                if (wfStepDef.isWithApplet()) {
                    appletList.add(wfStepDef.getAppletDef());
                }
            }

            if (appletList.isEmpty()) {
                appletList = Collections.emptyList();
            }
        }

        return appletList;
    }

    public WfStepDef getWfStepDef(String name) {
        WfStepDef wfStepDef = steps.get(name);
        if (wfStepDef == null) {
            throw new RuntimeException("Step with name [" + name + "] is unknown for workflow [" + getName() + "].");
        }

        return wfStepDef;
    }

    public boolean isFilter(String name) {
        return filterDefMap.containsKey(name);
    }

    public WfFilterDef getFilterDef(String name) {
        WfFilterDef filterDef = filterDefMap.get(name);
        if (filterDef == null) {
            throw new RuntimeException(
                    "Filter with name [" + name + "] is unknown for workflow definition [" + getName() + "].");
        }

        return filterDef;
    }

    public static Builder newBuilder(String entity, List<StringToken> descFormat, boolean supportMultiItemAction,
            String longName, String description, Long id, long version) {
        return new Builder(entity, descFormat, supportMultiItemAction, longName, description, id, version);
    }

    public static class Builder {

        private String entity;

        private Map<String, WfStepDef> steps;

        private Map<String, WfFilterDef> filterDefMap;

        private Map<String, WfSetValuesDef> setValuesDefMap;

        private WfStepDef startStepDef;

        private WfStepDef errorStepDef;

        private List<StringToken> descFormat;

        private boolean supportMultiItemAction;

        private String longName;

        private String description;

        private Long id;

        private long version;

        public Builder(String entity, List<StringToken> descFormat, boolean supportMultiItemAction, String longName,
                String description, Long id, long version) {
            this.entity = entity;
            this.descFormat = descFormat;
            this.longName = longName;
            this.description = description;
            this.supportMultiItemAction = supportMultiItemAction;
            this.id = id;
            this.version = version;
            this.steps = new HashMap<String, WfStepDef>();
            this.filterDefMap = new HashMap<String, WfFilterDef>();
            this.setValuesDefMap = new HashMap<String, WfSetValuesDef>();
        }

        public Builder addWfStepDef(WfStepDef stepDef) {
            if (steps == null) {
                steps = new HashMap<String, WfStepDef>();
            }

            if (steps.containsKey(stepDef.getName())) {
                throw new RuntimeException(
                        "Step with name [" + stepDef.getName() + "] already exists in this workflow.");
            }

            if (stepDef.isStart()) {
                if (startStepDef != null) {
                    throw new RuntimeException("Start step already exists in this workflow.");
                }

                startStepDef = stepDef;
            }

            if (stepDef.isError()) {
                if (errorStepDef != null) {
                    throw new RuntimeException("Error step already exists in this workflow.");
                }

                errorStepDef = stepDef;
            }

            steps.put(stepDef.getName(), stepDef);
            return this;
        }

        public Builder addFilterDef(WfFilterDef filterDef) {
            if (filterDef.isWithFilter()) {
                if (filterDefMap.containsKey(filterDef.getName())) {
                    throw new RuntimeException(
                            "Filter with name [" + filterDef.getName() + "] already exists in this definition.");
                }

                filterDefMap.put(filterDef.getName(), filterDef);
            }

            return this;
        }

        public Builder addWfSetValuesDef(WfSetValuesDef wfSetValuesDef) {
            if (setValuesDefMap.containsKey(wfSetValuesDef.getName())) {
                throw new RuntimeException(
                        "Set values with name [" + wfSetValuesDef.getName() + "] already exists in this definition.");
            }

            setValuesDefMap.put(wfSetValuesDef.getName(), wfSetValuesDef);
            return this;
        }

        public WfDef build() throws UnifyException {
            if (startStepDef == null) {
                throw new RuntimeException("Workflow has no start step.");
            }

            if (errorStepDef == null) {
                throw new RuntimeException("Workflow has no error step.");
            }

            return new WfDef(entity, startStepDef, errorStepDef, DataUtils.unmodifiableMap(steps), filterDefMap,
                    setValuesDefMap, descFormat, supportMultiItemAction,
                    ApplicationNameUtils.getApplicationEntityNameParts(longName), description, id, version);
        }
    }
}

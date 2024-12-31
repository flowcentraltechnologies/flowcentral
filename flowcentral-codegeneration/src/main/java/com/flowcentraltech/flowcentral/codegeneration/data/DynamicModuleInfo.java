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
package com.flowcentraltech.flowcentral.codegeneration.data;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.common.constants.ComponentType;

/**
 * Module information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class DynamicModuleInfo {

    private String moduleName;

    private List<ApplicationInfo> applications;

    public DynamicModuleInfo(String moduleName, List<ApplicationInfo> applications) {
        this.moduleName = moduleName;
        this.applications = Collections.unmodifiableList(applications);
    }

    public String getModuleName() {
        return moduleName;
    }

    public List<ApplicationInfo> getApplications() {
        return applications;
    }

    public static class ApplicationInfo {

        private String applicationName;

        private Map<ComponentType, List<String>> componentNames;

        private List<EnumInfo> enumerations;

        public ApplicationInfo(String applicationName, Map<ComponentType, List<String>> componentNames,
                List<EnumInfo> enumerations) {
            this.applicationName = applicationName;
            this.componentNames = Collections.unmodifiableMap(componentNames);
            this.enumerations = enumerations;
        }

        public String getApplicationName() {
            return applicationName;
        }

        public Map<ComponentType, List<String>> getComponentNames() {
            return componentNames;
        }

        public List<EnumInfo> getEnumerations() {
            return enumerations;
        }
    }

    public static class EnumInfo {

        private String enumName;

        private Map<String, String> options;

        public EnumInfo(String enumName, Map<String, String> options) {
            this.enumName = enumName;
            this.options = options;
        }

        public String getEnumName() {
            return enumName;
        }

        public Map<String, String> getOptions() {
            return options;
        }

    }

}

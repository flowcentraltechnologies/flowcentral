/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.system.data;

import com.flowcentraltech.flowcentral.system.util.SystemUtils;

/**
 * Process variable definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ProcessVariableDef {

    private String name;

    private String key;

    private String parameter;

    private String label;

    private boolean supportFilter;

    private boolean supportValues;

    public ProcessVariableDef(String name, String label) {
        this(name, label, false, false);
    }

    public ProcessVariableDef(String name, String label, boolean supportFilter, boolean supportValues) {
        this.name = name;
        this.label = label;
        this.key = SystemUtils.getProcessVariableCode(name);
        this.parameter = "{{" + this.key + "}}";
        this.supportFilter = supportFilter;
        this.supportValues = supportValues;
     }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getParameter() {
        return parameter;
    }

    public String getLabel() {
        return label;
    }

    public boolean isSupportValues() {
        return supportValues;
    }

    public boolean isSupportFilter() {
        return supportFilter;
    }

}

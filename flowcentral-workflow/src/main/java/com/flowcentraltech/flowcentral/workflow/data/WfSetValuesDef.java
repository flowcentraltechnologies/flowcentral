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

package com.flowcentraltech.flowcentral.workflow.data;

import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.SetValuesDef;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowSetValuesType;

/**
 * Workflow set values definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WfSetValuesDef {

    private WorkflowSetValuesType type;

    private String name;

    private String description;

    private FilterDef onCondition;

    private SetValuesDef setValues;

    public WfSetValuesDef(WorkflowSetValuesType type, String name, String description, FilterDef onCondition,
            SetValuesDef setValues) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.onCondition = onCondition;
        this.setValues = setValues;
    }

    public WorkflowSetValuesType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public FilterDef getOnCondition() {
        return onCondition;
    }

    public SetValuesDef getSetValues() {
        return setValues;
    }

    public boolean isOnEntry() {
       return WorkflowSetValuesType.ON_ENTRY.equals(type); 
    }
    
    public boolean isWithOnCondition() {
        return onCondition != null;
    }

    public boolean isWithSetValues() {
        return setValues != null;
    }
}

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

/**
 * Workflow routing definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class WfRoutingDef {

    private String name;

    private String description;

    private String conditionName;

    private String nextStepName;

    public WfRoutingDef(String name, String description, String conditionName, String nextStepName) {
        this.name = name;
        this.description = description;
        this.conditionName = conditionName;
        this.nextStepName = nextStepName;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getNextStepName() {
        return nextStepName;
    }

    public String getCondition() {
        return conditionName;
    }

    public boolean isWithCondition() {
        return conditionName != null;
    }
}

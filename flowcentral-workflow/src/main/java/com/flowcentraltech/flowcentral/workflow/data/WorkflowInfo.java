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
package com.flowcentraltech.flowcentral.workflow.data;

import com.tcdng.unify.core.data.Listable;

/**
 * Workflow information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WorkflowInfo implements Listable {

    private String longName;
    
    private String description;

    public WorkflowInfo(String longName, String description) {
        this.longName = longName;
        this.description = description;
    }

    @Override
    public String getListKey() {
         return longName;
    }

    @Override
    public String getListDescription() {
        return description;
    }

    public String getLongName() {
        return longName;
    }

    public String getDescription() {
        return description;
    }
}

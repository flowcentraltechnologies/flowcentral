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
package com.flowcentraltech.flowcentral.configuration.data;

import java.util.List;

import com.flowcentraltech.flowcentral.configuration.xml.ModuleConfig;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Module installation configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ModuleRestore extends ModuleInstall {

    private List<ApplicationRestore> applicationList;

    public ModuleRestore(TaskMonitor taskMonitor, ModuleConfig moduleConfig, List<ApplicationRestore> applicationList) {
        super(taskMonitor, moduleConfig);
        this.applicationList = applicationList;
    }

    public List<ApplicationRestore> getApplicationList() {
        return applicationList;
    }

    public String toString() {
        return StringUtils.toXmlString(this);
    }
}

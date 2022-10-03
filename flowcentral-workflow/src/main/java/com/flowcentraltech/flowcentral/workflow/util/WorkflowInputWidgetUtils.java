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
package com.flowcentraltech.flowcentral.workflow.util;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.configuration.xml.WfFilterConfig;
import com.flowcentraltech.flowcentral.workflow.entities.WorkflowFilter;
import com.tcdng.unify.core.UnifyException;

/**
 * Workflow input widget utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class WorkflowInputWidgetUtils {

    public static WfFilterConfig getFilterConfig(AppletUtilities au, WorkflowFilter workflowFilter)
            throws UnifyException {
        if (workflowFilter != null) {
            WfFilterConfig wfFilterConfig = new WfFilterConfig();
            InputWidgetUtils.getFilterConfig(au, wfFilterConfig, workflowFilter.getName(),
                    workflowFilter.getDescription(), null, null, null, workflowFilter.getFilter());
            wfFilterConfig.setFilterGenerator(workflowFilter.getFilterGenerator());
            wfFilterConfig.setFilterGeneratorRule(workflowFilter.getFilterGeneratorRule());
            return wfFilterConfig;
        }

        return null;
    }
}

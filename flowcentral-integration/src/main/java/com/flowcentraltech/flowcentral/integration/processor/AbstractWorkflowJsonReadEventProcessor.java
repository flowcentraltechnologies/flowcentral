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
package com.flowcentraltech.flowcentral.integration.processor;

import com.flowcentraltech.flowcentral.integration.endpoint.reader.AbstractJsonReadEventProcessor;
import com.flowcentraltech.flowcentral.workflow.business.WorkflowModuleService;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Convenient abstract base class for workflow JSON read event processor.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractWorkflowJsonReadEventProcessor<T> extends AbstractJsonReadEventProcessor<T> {

    @Configurable
    private WorkflowModuleService workflowModuleService;
    
    public AbstractWorkflowJsonReadEventProcessor(Class<T> jsonClass) {
        super(jsonClass);
    }

    public final void setWorkflowModuleService(WorkflowModuleService workflowModuleService) {
        this.workflowModuleService = workflowModuleService;
    }

    protected final WorkflowModuleService getWorkflowModuleService() {
        return workflowModuleService;
    }

    protected final WorkflowModuleService workflow() {
        return workflowModuleService;
    }

}
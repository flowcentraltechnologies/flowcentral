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
package com.flowcentraltech.flowcentral.studio.business.processors;

import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.flowcentraltech.flowcentral.common.business.policies.AbstractFormWizardTaskProcessor;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.task.TaskMonitor;

/**
 * Create JSON entity form wizard policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@EntityReferences({"studio.studioJsonEntity"})
@Component("createjsonentity-formwizardprocessor")
public class CreateJsonEntityFormWizardTaskProcessor extends AbstractFormWizardTaskProcessor {

    @Override
    public void process(TaskMonitor taskMonitor, ValueStore instValueStore) throws UnifyException {
        logDebug(taskMonitor, "Processing form wizard staff item...");
        logDebug(taskMonitor, "Item: {0}...", instValueStore.getValueObject());
    }

}

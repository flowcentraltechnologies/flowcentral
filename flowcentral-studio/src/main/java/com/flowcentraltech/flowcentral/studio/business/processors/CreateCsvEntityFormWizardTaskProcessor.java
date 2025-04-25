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
package com.flowcentraltech.flowcentral.studio.business.processors;

import java.io.StringReader;

import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.common.annotation.EntityReferences;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Create CSV entity form wizard policies.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@EntityReferences({ "studio.studioJsonEntity" })
@Component("createcsventity-formwizardprocessor")
public class CreateCsvEntityFormWizardTaskProcessor extends AbstractCreateEntityFormWizardTaskProcessor {

    @Override
    protected void loadSource(TaskMonitor taskMonitor, String source, String entity) throws UnifyException {
        ApplicationEntityNameParts parts = ApplicationNameUtils.getApplicationEntityNameParts(entity);
        final String uploadName = StringUtils.decapitalize(parts.getEntityName()) + "Upload";
        au().application().executeImportDataTask(taskMonitor, entity, uploadName, new StringReader(source), true);
    }

}

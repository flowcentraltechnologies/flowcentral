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

package com.flowcentraltech.flowcentral.studio.web.lists;

import com.flowcentraltech.flowcentral.common.web.lists.AbstractEntityTypeListCommand;
import com.flowcentraltech.flowcentral.workflow.business.WorkItemLoadingFilterGenerator;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.list.StringParam;

/**
 * Base class for studio work-item loading filter generator list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractStudioWorkItemLoadingFilterGeneratorListCommand
        extends AbstractEntityTypeListCommand<WorkItemLoadingFilterGenerator, StringParam> {

    public AbstractStudioWorkItemLoadingFilterGeneratorListCommand() {
        super(WorkItemLoadingFilterGenerator.class, StringParam.class);
    }

    @Override
    protected String getEntityName(StringParam param) throws UnifyException {
        return param.getValue();
    }

}

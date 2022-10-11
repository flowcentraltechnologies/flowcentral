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
package com.flowcentraltech.flowcentral.workflow.business.policies;

import java.util.List;

import com.flowcentraltech.flowcentral.application.policies.AbstractApplicationLoadingTableProvider;
import com.flowcentraltech.flowcentral.workflow.entities.WfItemQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Convenient abstract base class for workflow step loading table providers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractWfStepLoadingTableProvider extends AbstractApplicationLoadingTableProvider {

    private final String workflowName;

    private final String wfStepName;

    private final String loadingLabel;

    public AbstractWfStepLoadingTableProvider(String workflowName, String wfStepName, String loadingLabel) {
        this.workflowName = workflowName;
        this.wfStepName = wfStepName;
        this.loadingLabel = loadingLabel;
    }

    @Override
    public String getLoadingLabel() throws UnifyException {
        return loadingLabel;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public String getWfStepName() {
        return wfStepName;
    }

    protected List<Long> getIdsOfItemsInWfStep(String heldBy) throws UnifyException {
        WfItemQuery query = new WfItemQuery();
        query.workflowName(workflowName);
        query.wfStepName(wfStepName);
        if (!StringUtils.isBlank(heldBy)) {
            query.heldBy(heldBy);
        }

        return environment().valueList(Long.class, "workRecId", query);
    }

}

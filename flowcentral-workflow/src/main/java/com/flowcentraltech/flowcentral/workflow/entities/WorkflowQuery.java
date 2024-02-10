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
package com.flowcentraltech.flowcentral.workflow.entities;

import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntityQuery;

/**
 * Workflow query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WorkflowQuery extends BaseApplicationEntityQuery<Workflow> {

    public WorkflowQuery() {
        super(Workflow.class);
    }

    public WorkflowQuery entity(String entity) {
        return (WorkflowQuery) addEquals("entity", entity);
    }

    public WorkflowQuery entityBeginsWith(String entity) {
        return (WorkflowQuery) addBeginsWith("entity", entity);
    }

    public WorkflowQuery isWithLoadingTable() {
        return (WorkflowQuery) addIsNotNull("loadingTable");
    }

}

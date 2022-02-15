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

package com.flowcentraltech.flowcentral.studio.web.data;

import java.util.List;

import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.web.ui.widget.data.DialogCrudInfo;

/**
 * Step dialog information object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class StepDialogCrudInfo<T> extends DialogCrudInfo<T> {

    private String entityName;

    private Long workflowId;
    
    private List<? extends Listable> prevStepList;

    public StepDialogCrudInfo(Class<T> typeClass, String entityName, Long workflowId) {
        super(typeClass);
        this.entityName = entityName;
        this.workflowId = workflowId;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public String getEntityName() {
        return entityName;
    }

    public List<? extends Listable> getPrevStepList() {
        return prevStepList;
    }

    public void setPrevStepList(List<? extends Listable> prevStepList) {
        this.prevStepList = prevStepList;
    }

}

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

package com.flowcentraltech.flowcentral.workflow.entities;

import com.flowcentraltech.flowcentral.application.entities.AppSetValues;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Child;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Workflow step set values entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_WORKSTEPSETVALUES")
public class WfStepSetValues extends BaseAuditEntity {

    @ForeignKey(WfStep.class)
    private Long wfStepId;

    @ListOnly(key = "wfStepId", property = "description")
    private String wfStepDesc;

    @Child(category = "wfstep")
    private AppSetValues setValues;

    @Override
    public String getDescription() {
        return null;
    }

    public Long getWfStepId() {
        return wfStepId;
    }

    public void setWfStepId(Long wfStepId) {
        this.wfStepId = wfStepId;
    }

    public AppSetValues getSetValues() {
        return setValues;
    }

    public void setSetValues(AppSetValues setValues) {
        this.setValues = setValues;
    }

    public String getWfStepDesc() {
        return wfStepDesc;
    }

    public void setWfStepDesc(String wfStepDesc) {
        this.wfStepDesc = wfStepDesc;
    }

}

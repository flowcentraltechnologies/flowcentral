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

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;

/**
 * Workflow transition queue entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_WORKTRANSITIONQUEUE")
public class WfTransitionQueue extends BaseEntity {

    @Column
    private Long wfItemId;

    @Column(nullable = true)
    private Date processingDt;

    @Column
    private Boolean flowTransition;

    @Override
    public String getDescription() {
        return null;
    }

    public Long getWfItemId() {
        return wfItemId;
    }

    public void setWfItemId(Long wfItemId) {
        this.wfItemId = wfItemId;
    }

    public Date getProcessingDt() {
        return processingDt;
    }

    public void setProcessingDt(Date processingDt) {
        this.processingDt = processingDt;
    }

    public Boolean getFlowTransition() {
        return flowTransition;
    }

    public void setFlowTransition(Boolean flowTransition) {
        this.flowTransition = flowTransition;
    }

}

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
package com.flowcentraltech.flowcentral.workflow.entities;

import java.util.List;

import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;

/**
 * Application workflow wizard entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_WORKWIZARD")
public class WfWizard extends BaseApplicationEntity {

    @Column(name = "WIZARD_LABEL", length = 64)
    private String label;

    @Column(name = "WIZARD_ENTITY", length = 128)
    private String entity;

    @Column(length = 128, nullable = true)
    private String submitWorkflow;

    @ChildList
    private List<WfWizardStep> stepList;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getSubmitWorkflow() {
        return submitWorkflow;
    }

    public void setSubmitWorkflow(String submitWorkflow) {
        this.submitWorkflow = submitWorkflow;
    }

    public List<WfWizardStep> getStepList() {
        return stepList;
    }

    public void setStepList(List<WfWizardStep> stepList) {
        this.stepList = stepList;
    }

}

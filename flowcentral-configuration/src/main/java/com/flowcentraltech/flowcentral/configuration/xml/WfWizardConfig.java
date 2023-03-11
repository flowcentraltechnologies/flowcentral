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

package com.flowcentraltech.flowcentral.configuration.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Workflow wizard configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@XmlRootElement(name = "workflow-wizard")
public class WfWizardConfig extends BaseNameConfig {

    private String entity;

    private String submitWorkflow;

    private List<WfWizardStepConfig> stepList;

    public String getEntity() {
        return entity;
    }

    @XmlAttribute(required = true)
    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getSubmitWorkflow() {
        return submitWorkflow;
    }

    @XmlAttribute
    public void setSubmitWorkflow(String submitWorkflow) {
        this.submitWorkflow = submitWorkflow;
    }

    public List<WfWizardStepConfig> getStepList() {
        return stepList;
    }

    @XmlElement(name = "workflow-wizard-step")
    public void setStepList(List<WfWizardStepConfig> stepList) {
        this.stepList = stepList;
    }
}

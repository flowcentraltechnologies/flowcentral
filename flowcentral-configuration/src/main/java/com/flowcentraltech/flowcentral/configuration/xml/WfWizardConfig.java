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

package com.flowcentraltech.flowcentral.configuration.xml;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Workflow wizard configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "workflow-wizard")
public class WfWizardConfig extends BaseRootAppConfig {

    @JacksonXmlProperty(isAttribute = true)
    private String entity;

    @JacksonXmlProperty(isAttribute = true)
    private String submitWorkflow;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "workflow-wizard-step")
    private List<WfWizardStepConfig> stepList;

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

    public List<WfWizardStepConfig> getStepList() {
        return stepList;
    }

    public void setStepList(List<WfWizardStepConfig> stepList) {
        this.stepList = stepList;
    }
}

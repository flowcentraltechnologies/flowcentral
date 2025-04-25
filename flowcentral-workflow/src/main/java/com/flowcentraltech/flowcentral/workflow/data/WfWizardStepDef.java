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

package com.flowcentraltech.flowcentral.workflow.data;

/**
 * Workflow wizard step definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class WfWizardStepDef {

    private String name;

    private String description;

    private String label;

    private String form;

    private String reference;

    public WfWizardStepDef(String name, String description, String label, String form, String reference) {
        this.name = name;
        this.description = description;
        this.label = label;
        this.form = form;
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLabel() {
        return label;
    }

    public String getForm() {
        return form;
    }

    public String getReference() {
        return reference;
    }

    @Override
    public String toString() {
        return "WfWizardStepDef [name=" + name + ", description=" + description + ", label=" + label + ", form=" + form
                + ", reference=" + reference + "]";
    }

}

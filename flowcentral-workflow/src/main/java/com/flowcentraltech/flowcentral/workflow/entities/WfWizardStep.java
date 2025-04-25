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

import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;

/**
 * Application workflow wizard step entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_WORKWIZARDSTEP", uniqueConstraints = { @UniqueConstraint({ "wfWizardId", "name" }) })
public class WfWizardStep extends BaseConfigNamedEntity {

    @ForeignKey(WfWizard.class)
    private Long wfWizardId;

    @Column(name = "WIZARDSTEP_LABEL", length = 64)
    private String label;

    @Column(name = "WIZARDSTEP_FORM", length = 128)
    private String form;

    @Column(name = "WIZARDSTEP_REFERENCE", length = 64, nullable = true)
    private String reference;

    public Long getWfWizardId() {
        return wfWizardId;
    }

    public void setWfWizardId(Long wfWizardId) {
        this.wfWizardId = wfWizardId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

}

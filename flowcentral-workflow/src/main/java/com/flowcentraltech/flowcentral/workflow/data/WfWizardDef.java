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
package com.flowcentraltech.flowcentral.workflow.data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.BaseApplicationEntityDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Workflow wizard definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WfWizardDef extends BaseApplicationEntityDef {

    private AppletDef appletDef;

    private String entity;

    private String submitWorkflow;

    private List<WfWizardStepDef> wizardStepDefList;

    private WfWizardDef(AppletDef appletDef, String entity, String submitWorkflow,
            List<WfWizardStepDef> wizardStepDefList, ApplicationEntityNameParts nameParts, String description, Long id,
            long version) {
        super(nameParts, description, id, version);
        this.appletDef = appletDef;
        this.entity = entity;
        this.submitWorkflow = submitWorkflow;
        this.wizardStepDefList = wizardStepDefList;
    }

    public AppletDef getAppletDef() {
        return appletDef;
    }

    public String getEntity() {
        return entity;
    }

    public String getSubmitWorkflow() {
        return submitWorkflow;
    }

    public List<WfWizardStepDef> getFormWizardStepDefList() {
        return wizardStepDefList;
    }

    public WfWizardStepDef getWfWizardStepDef(int index) {
        return wizardStepDefList.get(index);
    }

    public int stepCount() {
        return wizardStepDefList.size();
    }

    public static Builder newBuilder(String entity, String submitWorkflow, String longName, String description, Long id,
            long version) {
        return new Builder(entity, submitWorkflow, longName, description, id, version);
    }

    public static class Builder {

        private AppletDef appletDef;

        private Map<String, WfWizardStepDef> wizardStepDefList;

        private String entity;

        private String submitWorkflow;

        private String longName;

        private String description;

        private Long id;

        private long version;

        public Builder(String entity, String submitWorkflow, String longName, String description, Long id,
                long version) {
            this.longName = longName;
            this.description = description;
            this.id = id;
            this.version = version;
            this.entity = entity;
            this.submitWorkflow = submitWorkflow;
            wizardStepDefList = new LinkedHashMap<String, WfWizardStepDef>();
        }

        public Builder applet(AppletDef appletDef) {
            this.appletDef = appletDef;
            return this;
        }

        public Builder addStep(String name, String description, String label, String form, String reference) {
            if (wizardStepDefList.containsKey(name)) {
                throw new RuntimeException("Wizard step with name [" + name + "] already exists on this form wizard.");
            }

            wizardStepDefList.put(name, new WfWizardStepDef(name, description, label, form, reference));
            return this;
        }

        public WfWizardDef build() throws UnifyException {
            ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
            return new WfWizardDef(appletDef, entity, submitWorkflow,
                    DataUtils.unmodifiableValuesList(wizardStepDefList), nameParts, description, id, version);
        }
    }
}

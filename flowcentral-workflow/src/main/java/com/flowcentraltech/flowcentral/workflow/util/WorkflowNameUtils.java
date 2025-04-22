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

package com.flowcentraltech.flowcentral.workflow.util;

import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Workflow name utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class WorkflowNameUtils {

    public static final String RESERVED_WORKFLOW_APPLET_PREFIX = ApplicationNameUtils.RESERVED_FC_PREFIX + "wf_";

    public static final String RESERVED_WORKFLOW_WIZARD_PREFIX = ApplicationNameUtils.RESERVED_FC_PREFIX + "wfz_";

    public static final String RESERVED_WORKFLOW_NAME_PUBLISHED_SUFFIX = "_wRunnable";

    public static final String RESERVED_WORKFLOW_DESCRIPTION_PUBLISHED_SUFFIX = " (Published)";

    private static final FactoryMap<String, WfAppletNameParts> wfAppletNameParts;

    private static final FactoryMap<String, WfStepLongNameParts> wfStepLongNameParts;

    private static final FactoryMap<String, WfWizardAppletNameParts> wfWizardAppletNameParts;

    static {
        wfStepLongNameParts = new FactoryMap<String, WfStepLongNameParts>()
            {

                @Override
                protected WfStepLongNameParts create(String stepLongName, Object... arg1) throws Exception {
                    int lastIndex = stepLongName.lastIndexOf('.');
                    return new WfStepLongNameParts(stepLongName.substring(0, lastIndex),
                            stepLongName.substring(lastIndex + 1));
                }

            };

        wfAppletNameParts = new FactoryMap<String, WfAppletNameParts>()
            {

                @Override
                protected WfAppletNameParts create(String wfAppletName, Object... arg1) throws Exception {
                    String actAppletName = wfAppletName.substring(RESERVED_WORKFLOW_APPLET_PREFIX.length());
                    String[] po = StringUtils.charSplit(actAppletName, '_');
                    return new WfAppletNameParts(po[0], po[1]);
                }

            };

        wfWizardAppletNameParts = new FactoryMap<String, WfWizardAppletNameParts>()
            {

                @Override
                protected WfWizardAppletNameParts create(String wfWizardAppletName, Object... arg1) throws Exception {
                    String actAppletName = wfWizardAppletName.substring(RESERVED_WORKFLOW_WIZARD_PREFIX.length());
                    return new WfWizardAppletNameParts(actAppletName);
                }

            };
    }

    private WorkflowNameUtils() {

    }

    public static String getWorkflowRunnableName(String workflowName) {
        if (!workflowName.endsWith(RESERVED_WORKFLOW_NAME_PUBLISHED_SUFFIX)
                && !ApplicationNameUtils.isWorkflowCopyName(workflowName)) {
            return workflowName + RESERVED_WORKFLOW_NAME_PUBLISHED_SUFFIX;
        }

        return workflowName;
    }

    public static String getWorkflowRunnableDescription(String workflowDesc) {
        return workflowDesc + RESERVED_WORKFLOW_DESCRIPTION_PUBLISHED_SUFFIX;
    }
    
    public static String getWfStepLongName(String workflowLongName, String stepName) {
        return StringUtils.dotify(workflowLongName, stepName);
    }

    public static WfStepLongNameParts getWfStepLongNameParts(String stepLongName) throws UnifyException {
        return wfStepLongNameParts.get(stepLongName);
    }

    public static WfAppletNameParts getWfAppletNameParts(String wfAppletName) throws UnifyException {
        return wfAppletNameParts.get(wfAppletName);
    }

    public static String getWfAppletName(String workflowName, String stepName) {
        return RESERVED_WORKFLOW_APPLET_PREFIX + workflowName + "_" + stepName;
    }

    public static boolean isWfAppletName(String wfAppletName) {
        return wfAppletName.startsWith(RESERVED_WORKFLOW_APPLET_PREFIX);
    }

    public static WfWizardAppletNameParts getWfWizardAppletNameParts(String wfWizardAppletName) throws UnifyException {
        return wfWizardAppletNameParts.get(wfWizardAppletName);
    }

    public static String getWfWizardAppletName(String wfWizardName) {
        return RESERVED_WORKFLOW_WIZARD_PREFIX + wfWizardName;
    }

    public static boolean isWfWizardAppletName(String wfWizardAppletName) {
        return wfWizardAppletName.startsWith(RESERVED_WORKFLOW_WIZARD_PREFIX);
    }

    public static class WfStepLongNameParts {

        private String workflow;

        private String stepName;

        public WfStepLongNameParts(String workflow, String stepName) {
            this.workflow = workflow;
            this.stepName = stepName;
        }

        public String getWorkflow() {
            return workflow;
        }

        public String getStepName() {
            return stepName;
        }

    }

    public static class WfAppletNameParts {

        private String workflow;

        private String stepName;

        public WfAppletNameParts(String workflow, String stepName) {
            this.workflow = workflow;
            this.stepName = stepName;
        }

        public String getWorkflow() {
            return workflow;
        }

        public String getStepName() {
            return stepName;
        }

    }

    public static class WfWizardAppletNameParts {

        private String wizard;

        public WfWizardAppletNameParts(String wizard) {
            this.wizard = wizard;
        }

        public String getWizard() {
            return wizard;
        }

    }
}

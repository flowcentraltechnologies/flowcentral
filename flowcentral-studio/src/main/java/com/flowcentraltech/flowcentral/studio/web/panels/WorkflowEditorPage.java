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

package com.flowcentraltech.flowcentral.studio.web.panels;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepType;
import com.flowcentraltech.flowcentral.studio.web.widgets.WorkflowEditor;
import com.flowcentraltech.flowcentral.studio.web.widgets.WorkflowEditor.DesignWfStep;
import com.flowcentraltech.flowcentral.studio.web.widgets.WorkflowEditor.DesignWfStepRouting;
import com.flowcentraltech.flowcentral.workflow.entities.WfStep;
import com.flowcentraltech.flowcentral.workflow.entities.Workflow;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Query;

/**
 * Workflow editor page.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WorkflowEditorPage extends AbstractStudioEditorPage {

    private final EntityDef entityDef;
    
    private final Long workflowId;

    private WorkflowEditor workflowEditor;

    public WorkflowEditorPage(AppletUtilities au, EntityDef entityDef, Long workflowId,
            BreadCrumbs breadCrumbs) {
        super(au, breadCrumbs);
        this.entityDef = entityDef;
        this.workflowId = workflowId;
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public WorkflowEditor getWorkflowEditor() {
        return workflowEditor;
    }

    public void commitDesign() throws UnifyException {
        Workflow workflow = getAu().environment().find(Workflow.class, workflowId);
        workflow.setStepList(new ArrayList<WfStep>(workflowEditor.getWorkflowSteps().values()));
        getAu().environment().updateByIdVersion(workflow);
    }

    public void newEditor() throws UnifyException {
        WorkflowEditor.Builder web = WorkflowEditor.newBuilder(getAu(), entityDef, workflowId);
        boolean isOldDesign = false;
        for (WfStep step : getAu().environment()
                .listAllWithChildren(Query.of(WfStep.class).addEquals("workflowId", workflowId).addOrder("id"))) {
            web.addStep(step);
            isOldDesign |= step.getDesignX() != 0;
            isOldDesign |= step.getDesignY() != 0;
        }

        workflowEditor = web.build();
        if (!isOldDesign) {
            new WorkflowLayout(workflowEditor.getDesign().getSteps()).apply();
        }
    }

    private static final int X_GAP = 220;
    private static final int Y_GAP = 100;

    private static class WorkflowLayout {

        private DesignWfStep startStep = null;

        private DesignWfStep errorStep = null;

        private DesignWfStep endStep = null;

        private Map<String, DesignWfStep> steps;

        private int[] yPos;

        private int depth;

        private int maxDepth;

        public WorkflowLayout(List<DesignWfStep> stepList) {
            this.steps = new LinkedHashMap<String, DesignWfStep>();
            this.yPos = new int[20];
            for (DesignWfStep step : stepList) {
                switch (WorkflowStepType.fromIndex(step.getTypeIndex())) {
                    case START:
                        startStep = step;
                        break;
                    case END:
                        endStep = step;
                        break;
                    case ERROR:
                        errorStep = step;
                        break;
                    case ENRICHMENT:
                    case PROCEDURE:
                    case RECORD_ACTION:
                    case BINARY_ROUTING:
                    case POLICY_ROUTING:
                    case MULTI_ROUTING:
                    case SET_VALUES:
                    case USER_ACTION:
                        steps.put(step.getName(), step);
                        break;
                    default:
                        break;

                }
            }
        }

        public void apply() {
            startStep.setX(0);
            startStep.setY(0);
            errorStep.setX(0);
            errorStep.setY(Y_GAP + 20);
            arrange(startStep);
            endStep.setX(X_GAP * (maxDepth + 1));
            endStep.setY(yPos[maxDepth]);
        }

        private void arrange(DesignWfStep step) {
            depth++;
            final int x = X_GAP * depth;
            for (DesignWfStepRouting routing : step.getRoutings()) {
                if (routing.getNextStepName() != null) {
                    DesignWfStep nextStep = steps.remove(routing.getNextStepName());
                    if (nextStep != null) {
                        nextStep.setX(x);
                        nextStep.setY(yPos[depth]);
                        yPos[depth] += Y_GAP + nextStep.getRoutings().size() * 20;
                        if (maxDepth < depth) {
                            maxDepth = depth;
                        }
                        arrange(nextStep);
                    }
                }
            }

            depth--;
        }
    }
}

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

package com.flowcentraltech.flowcentral.studio.web.widgets;

import com.flowcentraltech.flowcentral.common.web.widgets.AbstractFlowCentralMultiControl;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepType;
import com.flowcentraltech.flowcentral.studio.constants.StudioWorkflowEditType;
import com.flowcentraltech.flowcentral.studio.web.widgets.WorkflowEditor.DesignChange;
import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.stream.JsonObjectStreamer;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.Control;

/**
 * Workflow editor widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-workfloweditor")
@UplAttributes({ @UplAttribute(name = "choiceWidth", type = String.class, defaultVal = "240px") })
public class WorkflowEditorWidget extends AbstractFlowCentralMultiControl {

    public static final String WORK_CONTENT = "content";

    @Configurable(ApplicationComponents.APPLICATION_JSONOBJECTSTREAMER)
    private JsonObjectStreamer jsonObjectStreamer;

    private Control valueCtrl;

    private Control createTypeCtrl;

    private Control createXCtrl;

    private Control createYCtrl;

    private Control editTypeCtrl;

    private Control editStepCtrl;

    private String design;

    private String stepName;

    private int createX;

    private int createY;

    private WorkflowStepType workflowStepType;

    private StudioWorkflowEditType studioWorkflowEditType;

    public void setJsonObjectStreamer(JsonObjectStreamer jsonObjectStreamer) {
        this.jsonObjectStreamer = jsonObjectStreamer;
    }

    @Override
    protected void doOnPageConstruct() throws UnifyException {
        valueCtrl = (Control) addInternalChildWidget("!ui-hidden binding:design");
        createTypeCtrl = (Control) addInternalChildWidget("!ui-hidden binding:workflowStepType");
        createXCtrl = (Control) addInternalChildWidget("!ui-hidden binding:createX");
        createYCtrl = (Control) addInternalChildWidget("!ui-hidden binding:createY");
        editTypeCtrl = (Control) addInternalChildWidget("!ui-hidden binding:studioWorkflowEditType");
        editStepCtrl = (Control) addInternalChildWidget("!ui-hidden binding:stepName");
    }

    @Override
    public void addPageAliases() throws UnifyException {
        addPageAlias(valueCtrl);
    }

    @Action
    public void create() throws UnifyException {
        commandShowPopup(getWorkflowEditor().prepareCreate(workflowStepType, createX, createY));
    }

    @Action
    public void edit() throws UnifyException {
        commandShowPopup(getWorkflowEditor().prepareEdit(studioWorkflowEditType, stepName));
    }

    public String getDesign() {
        return design;
    }

    public void setDesign(String design) throws UnifyException {
        if (!StringUtils.isBlank(design)) {
            getWorkflowEditor().applyDesignChange(jsonObjectStreamer.unmarshal(DesignChange.class, design));
        }
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public WorkflowStepType getWorkflowStepType() {
        return workflowStepType;
    }

    public void setWorkflowStepType(WorkflowStepType workflowStepType) {
        this.workflowStepType = workflowStepType;
    }

    public StudioWorkflowEditType getStudioWorkflowEditType() {
        return studioWorkflowEditType;
    }

    public void setStudioWorkflowEditType(StudioWorkflowEditType studioWorkflowEditType) {
        this.studioWorkflowEditType = studioWorkflowEditType;
    }

    public int getCreateX() {
        return createX;
    }

    public void setCreateX(int createX) {
        this.createX = createX;
    }

    public int getCreateY() {
        return createY;
    }

    public void setCreateY(int createY) {
        this.createY = createY;
    }

    public Control getValueCtrl() {
        return valueCtrl;
    }

    public Control getCreateTypeCtrl() {
        return createTypeCtrl;
    }

    public Control getCreateXCtrl() {
        return createXCtrl;
    }

    public Control getCreateYCtrl() {
        return createYCtrl;
    }

    public Control getEditTypeCtrl() {
        return editTypeCtrl;
    }

    public Control getEditStepCtrl() {
        return editStepCtrl;
    }

    public WorkflowEditor getWorkflowEditor() throws UnifyException {
        return getValue(WorkflowEditor.class);
    }

    public String getChoiceWidth() throws UnifyException {
        return getUplAttribute(String.class, "choiceWidth");
    }

    public String getChoiceId() throws UnifyException {
        return getPrefixedId("choice_");
    }

    public String getToolBaseId() throws UnifyException {
        return getPrefixedId("tool_base_");
    }

    public String getDesignBaseId() throws UnifyException {
        return getPrefixedId("design_base_");
    }

    public String getDesignCanvasId() throws UnifyException {
        return getPrefixedId("design_canvas_");
    }

}

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.ApplicationFilterConstants;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.entities.AppSetValues;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.web.widgets.SetValueEntries;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepPriority;
import com.flowcentraltech.flowcentral.configuration.constants.WorkflowStepType;
import com.flowcentraltech.flowcentral.studio.constants.StudioWorkflowEditType;
import com.flowcentraltech.flowcentral.studio.web.data.DialogSetValuesInfo;
import com.flowcentraltech.flowcentral.studio.web.data.StepDialogCrudInfo;
import com.flowcentraltech.flowcentral.workflow.entities.WfStep;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepAlert;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepRouting;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepSetValues;
import com.flowcentraltech.flowcentral.workflow.entities.WfStepUserAction;
import com.flowcentraltech.flowcentral.workflow.util.WorkflowDesignUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.Editable;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.LocaleFactoryMap;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.widget.data.DialogCrudInfo;

/**
 * Workflow editor object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WorkflowEditor {

    private static final List<String> routinKeyList = Collections
            .unmodifiableList(Arrays.asList("always", "default", "ontrue", "onfalse"));
    private static final List<String> routinDescList = Collections.unmodifiableList(
            Arrays.asList("$m{workfloweditor.workflowdesigner.always}", "$m{workfloweditor.workflowdesigner.default}",
                    "$m{workfloweditor.workflowdesigner.ontrue}", "$m{workfloweditor.workflowdesigner.onfalse}"));

    private final AppletUtilities au;

    private final EntityDef entityDef;

    private final Long workflowId;

    private Map<String, WfStep> workflowSteps;

    private Map<String, String> routingLabels;

    private List<DesignWfEditType> editTypeList;

    private List<DesignWfStepType> stepTypeList;

    private Design design;

    private WfStep editStep;

    private DialogSetValuesInfo setValuesInfo;

    private StepDialogCrudInfo<WfStepAlert> alertsCrudInfo;

    private DialogCrudInfo<WfStepRouting> routingsCrudInfo;

    private DialogCrudInfo<WfStepUserAction> userActionsCrudInfo;

    private String lastEditStepName;

    private String createStepPanelName;

    private String editStepPanelName;

    private String stepSetValuesPanelName;

    private String stepAlertsPanelName;

    private String stepRoutingsPanelName;

    private String stepUserActionsPanelName;

    private String dialogTitle;

    private boolean readOnly;

    private WorkflowEditor(AppletUtilities au, EntityDef entityDef, Long workflowId, Map<String, WfStep> workflowSteps,
            Map<String, String> routingLabels, List<DesignWfEditType> editTypeList, List<DesignWfStepType> stepTypeList,
            Design design) {
        this.au = au;
        this.entityDef = entityDef;
        this.workflowId = workflowId;
        this.workflowSteps = workflowSteps;
        this.routingLabels = routingLabels;
        this.editTypeList = editTypeList;
        this.stepTypeList = stepTypeList;
        this.design = design;
        this.setValuesInfo = new DialogSetValuesInfo();
        final String entityName = entityDef.getLongName();
        this.alertsCrudInfo = new StepDialogCrudInfo<WfStepAlert>(WfStepAlert.class, entityName, workflowId);
        this.routingsCrudInfo = new StepDialogCrudInfo<WfStepRouting>(WfStepRouting.class, entityName, workflowId);
        this.userActionsCrudInfo = new StepDialogCrudInfo<WfStepUserAction>(WfStepUserAction.class, entityName,
                workflowId);
    }

    public void init(String createStepPanelName, String editStepPanelName, String stepSetValuesPanelName,
            String stepAlertsPanelName, String stepRoutingsPanelName, String stepUserActionsPanelName) {
        this.createStepPanelName = createStepPanelName;
        this.editStepPanelName = editStepPanelName;
        this.stepSetValuesPanelName = stepSetValuesPanelName;
        this.stepAlertsPanelName = stepAlertsPanelName;
        this.stepRoutingsPanelName = stepRoutingsPanelName;
        this.stepUserActionsPanelName = stepUserActionsPanelName;
    }

    public boolean isInitialized() {
        return stepUserActionsPanelName != null;
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public List<DesignWfEditType> getEditTypeList() {
        return editTypeList;
    }

    public List<DesignWfStepType> getStepTypeList() {
        return stepTypeList;
    }

    public Design getDesign() {
        return design;
    }

    public Map<String, WfStep> getWorkflowSteps() {
        return workflowSteps;
    }

    public String getDialogTitle() {
        return dialogTitle;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public void createStep() throws UnifyException {
        workflowSteps.put(editStep.getName(), editStep);
        design.getSteps().add(DesignWfStep.from(au, editStep, routingLabels));
    }

    public void applySetValues() throws UnifyException {
        if (setValuesInfo.getEntries() != null) {
            WfStep wfStep = workflowSteps.get(lastEditStepName);
            if (wfStep.getSetValues() == null) {
                wfStep.setSetValues(new WfStepSetValues());
            }

            wfStep.getSetValues().setSetValues(new AppSetValues(
                    InputWidgetUtils.getSetValuesDefinition(setValuesInfo.getEntries().getSetValuesDef())));
            DesignWfStep designWfStep = design.findStep(lastEditStepName);
            designWfStep.setCensus(WorkflowEditor.evaluateCensus(wfStep));
        }
    }

    public void updateStepDesign() throws UnifyException {
        DesignWfStep designWfStep = design.findStep(lastEditStepName);
        WfStep wfStep = workflowSteps.get(lastEditStepName);
        designWfStep.setCensus(WorkflowEditor.evaluateCensus(wfStep));
        designWfStep.setRoutings(WorkflowEditor.evaluateRoutings(wfStep, routingLabels));
    }

    public void applyDesignChange(DesignChange designChange) throws UnifyException {
        if (design != null) {
            Map<String, DesignWfStepChange> stepChangeMap = new HashMap<String, DesignWfStepChange>();
            for (DesignWfStepChange stepChange : designChange.getSteps()) {
                stepChangeMap.put(stepChange.getName(), stepChange);
            }

            Iterator<DesignWfStep> it = design.getSteps().iterator();
            while (it.hasNext()) {
                DesignWfStep designWfStep = it.next();
                DesignWfStepChange stepChange = stepChangeMap.get(designWfStep.getName());
                if (stepChange == null) {
                    it.remove();
                    workflowSteps.remove(designWfStep.getName());
                } else {
                    WfStep wfStep = workflowSteps.get(designWfStep.getName());
                    wfStep.setDesignX(stepChange.getX());
                    wfStep.setDesignY(stepChange.getY());

                    Map<String, DesignWfStepRouting> routingChangeMap = stepChange.getRoutingMap();
                    switch (wfStep.getType()) {
                        case START:
                        case ENRICHMENT:
                        case PROCEDURE:
                        case NOTIFICATION:
                        case RECORD_ACTION:
                        case SET_VALUES: {
                            DesignWfStepRouting routingChange = routingChangeMap.get("always");
                            if (routingChange != null) {
                                wfStep.setNextStepName(routingChange.getNextStepName());
                            }
                        }
                            break;
                        case BINARY_ROUTING:
                        case POLICY_ROUTING: {
                            DesignWfStepRouting routingChange = routingChangeMap.get("ontrue");
                            if (routingChange != null) {
                                wfStep.setNextStepName(routingChange.getNextStepName());
                            }

                            routingChange = routingChangeMap.get("onfalse");
                            if (routingChange != null) {
                                wfStep.setAltNextStepName(routingChange.getNextStepName());
                            }
                        }
                            break;
                        case MULTI_ROUTING: {
                            for (WfStepRouting wfStepRouting : wfStep.getRoutingList()) {
                                DesignWfStepRouting routingChange = routingChangeMap.get(wfStepRouting.getName());
                                if (routingChange != null) {
                                    wfStepRouting.setNextStepName(routingChange.getNextStepName());
                                }
                            }

                            DesignWfStepRouting routingChange = routingChangeMap.get("default");
                            if (routingChange != null) {
                                wfStep.setNextStepName(routingChange.getNextStepName());
                            }
                        }
                            break;
                        case USER_ACTION:
                        case ERROR: {
                            for (WfStepUserAction stepUserAction : wfStep.getUserActionList()) {
                                DesignWfStepRouting routingChange = routingChangeMap.get(stepUserAction.getName());
                                if (routingChange != null) {
                                    stepUserAction.setNextStepName(routingChange.getNextStepName());
                                }
                            }
                        }
                            break;
                        case END:
                            break;
                        default:
                            break;

                    }

                    designWfStep.setX(stepChange.getX());
                    designWfStep.setY(stepChange.getY());
                    designWfStep.setRoutings(stepChange.getRoutings());
                }
            }
        }
    }

    public String prepareCreate(WorkflowStepType workflowStepType, int designX, int designY) throws UnifyException {
        String itemDesc = au.getListItemByKey("wfsteptypelist", workflowStepType.code()).getListDescription();
        dialogTitle = au.resolveSessionMessage("$m{wfstepcreationpanel.caption}", itemDesc);

        editStep = new WfStep();
        editStep.setWorkflowId(workflowId);
        editStep.setType(workflowStepType);
        editStep.setPriority(WorkflowStepPriority.NORMAL);
        editStep.setAlertList(new ArrayList<WfStepAlert>());
        editStep.setDesignX(designX);
        editStep.setDesignY(designY);
        editStep.setEntityName(entityDef.getLongName());
        switch (workflowStepType) {
            case ENRICHMENT:
            case PROCEDURE:
            case RECORD_ACTION:
            case BINARY_ROUTING:
            case POLICY_ROUTING:
            case MULTI_ROUTING:
                editStep.setRoutingList(new ArrayList<WfStepRouting>());
                break;
            case NOTIFICATION:
            case SET_VALUES:
                break;
            case USER_ACTION:
                editStep.setUserActionList(new ArrayList<WfStepUserAction>());
                editStep.setComments(ApplicationFilterConstants.RESERVED_ALWAYS_FILTERNAME);
                break;
            case START:
            case ERROR:
            case END:
            default:
                break;

        }

        if (workflowStepType.supportSetValues()) {
            editStep.setSetValues(new WfStepSetValues());
        }

        return createStepPanelName;
    }

    public String prepareEdit(StudioWorkflowEditType studioWorkflowEditType, String stepName) throws UnifyException {
        WfStep step = workflowSteps.get(stepName);
        lastEditStepName = stepName;
        switch (studioWorkflowEditType) {
            case ALERT:
                alertsCrudInfo
                        .setTitle(au.resolveSessionMessage("$m{wfstepalertscrudpanel.caption}", step.getDescription()));
                alertsCrudInfo.setItemList(step.getAlertList());
                List<ListData> prevList = new ArrayList<ListData>();
                for (WfStep prevStep : workflowSteps.values()) {
                    if (!prevStep.getName().equals(stepName) && prevStep.getType().isFlowing()) {
                        prevList.add(new ListData(prevStep.getName(), prevStep.getDescription()));
                    }
                }
                alertsCrudInfo.setPrevStepList(prevList);
                return stepAlertsPanelName;
            case MULTI_ROUTING:
                routingsCrudInfo.setTitle(
                        au.resolveSessionMessage("$m{wfsteproutingscrudpanel.caption}", step.getDescription()));
                routingsCrudInfo.setItemList(step.getRoutingList());
                return stepRoutingsPanelName;
            case SET_VALUES:
                setValuesInfo
                        .setTitle(au.resolveSessionMessage("$m{wfstepsetvaluespanel.caption}", step.getDescription()));
                SetValueEntries entries = (step.getSetValues() != null && step.getSetValues().getSetValues() != null)
                        ? new SetValueEntries(entityDef,
                                InputWidgetUtils.getSetValuesDef(null, step.getSetValues().getSetValues()),
                                Editable.TRUE)
                        : new SetValueEntries(entityDef);
                setValuesInfo.setEntries(entries);
                return stepSetValuesPanelName;
            case STEP:
                editStep = workflowSteps.get(stepName);
                String itemDesc = au.getListItemByKey("wfsteptypelist", editStep.getType().code()).getListDescription();
                dialogTitle = au.resolveSessionMessage("$m{wfstepeditpanel.caption}", itemDesc);
                return editStepPanelName;
            case USER_ACTION:
                userActionsCrudInfo.setTitle(
                        au.resolveSessionMessage("$m{wfstepuseractionscrudpanel.caption}", step.getDescription()));
                userActionsCrudInfo.setItemList(step.getUserActionList());
                return stepUserActionsPanelName;
            default:
                break;
        }

        if (step.getType().supportSetValues()) {
            if (editStep.getSetValues() == null) {
                editStep.setSetValues(new WfStepSetValues());
            }
        }

        return null;
    }

    public WfStep getEditStep() {
        return editStep;
    }

    public DialogSetValuesInfo getSetValuesInfo() {
        return setValuesInfo;
    }

    public DialogCrudInfo<WfStepAlert> getAlertsCrudInfo() {
        return alertsCrudInfo;
    }

    public DialogCrudInfo<WfStepRouting> getRoutingsCrudInfo() {
        return routingsCrudInfo;
    }

    public DialogCrudInfo<WfStepUserAction> getUserActionsCrudInfo() {
        return userActionsCrudInfo;
    }

    public static Builder newBuilder(AppletUtilities au, EntityDef entityDef, Long workflowId) throws UnifyException {
        return new Builder(au, entityDef, workflowId);
    }

    public static class Builder {

        private AppletUtilities au;

        private EntityDef entityDef;

        private Long workflowId;

        private Map<String, WfStep> workflowSteps;

        private Map<String, String> routingLabels;

        private List<DesignWfStep> stepList;

        private static final LocaleFactoryMap<List<DesignWfEditType>> designWfEditTypeMap = new LocaleFactoryMap<List<DesignWfEditType>>()
            {

                @Override
                protected List<DesignWfEditType> create(Locale locale, Object... params) throws Exception {
                    AppletUtilities au = (AppletUtilities) params[0];
                    List<DesignWfEditType> editTypeList = new ArrayList<DesignWfEditType>();
                    for (StudioWorkflowEditType type : StudioWorkflowEditType.asList()) {
                        editTypeList.add(new DesignWfEditType(type.toString().toLowerCase(),
                                au.resolveSessionMessage(type.caption())));
                    }

                    return DataUtils.unmodifiableList(editTypeList);
                }
            };

        private static final LocaleFactoryMap<List<DesignWfStepType>> designWfStepTypeMap = new LocaleFactoryMap<List<DesignWfStepType>>()
            {

                @Override
                protected List<DesignWfStepType> create(Locale locale, Object... params) throws Exception {
                    AppletUtilities au = (AppletUtilities) params[0];
                    List<DesignWfStepType> stepTypeList = new ArrayList<DesignWfStepType>();
                    for (WorkflowStepType type : WorkflowStepType.asList()) {
                        List<Integer> editTypes = new ArrayList<Integer>();
                        ;
                        switch (type) {
                            case END:
                                break;
                            case ENRICHMENT:
                                editTypes.add(StudioWorkflowEditType.ALERT.index());
                                break;
                            case ERROR:
                                editTypes.add(StudioWorkflowEditType.ALERT.index());
                                break;
                            case PROCEDURE:
                                editTypes.add(StudioWorkflowEditType.ALERT.index());
                                break;
                            case RECORD_ACTION:
                                editTypes.add(StudioWorkflowEditType.ALERT.index());
                                break;
                            case BINARY_ROUTING:
                                editTypes.add(StudioWorkflowEditType.ALERT.index());
                                break;
                            case POLICY_ROUTING:
                                editTypes.add(StudioWorkflowEditType.ALERT.index());
                                break;
                            case MULTI_ROUTING:
                                editTypes.add(StudioWorkflowEditType.ALERT.index());
                                editTypes.add(StudioWorkflowEditType.MULTI_ROUTING.index());
                                break;
                            case START:
                                break;
                            case NOTIFICATION:
                                editTypes.add(StudioWorkflowEditType.ALERT.index());
                                break;
                            case SET_VALUES:
                                editTypes.add(StudioWorkflowEditType.ALERT.index());
                                break;
                            case USER_ACTION:
                                editTypes.add(StudioWorkflowEditType.ALERT.index());
                                editTypes.add(StudioWorkflowEditType.USER_ACTION.index());
                                break;
                            default:
                                break;
                        }

                        if (type.supportSetValues()) {
                            editTypes.add(StudioWorkflowEditType.SET_VALUES.index());
                        }

                        stepTypeList.add(new DesignWfStepType(type.code(),
                                au.getListItemByKey("wfsteptypelist", type.code()).getListDescription(),
                                au.getSymbolUnicode(type.icon()), type.color().index(), editTypes,
                                type.isAcceptInflow(), type.isOptional()));
                    }

                    return DataUtils.unmodifiableList(stepTypeList);
                }
            };

        public Builder(AppletUtilities au, EntityDef entityDef, Long workflowId) throws UnifyException {
            this.au = au;
            this.entityDef = entityDef;
            this.workflowId = workflowId;
            this.workflowSteps = new LinkedHashMap<String, WfStep>();
            this.routingLabels = new HashMap<String, String>();
            for (int i = 0; i < routinKeyList.size(); i++) {
                String key = routinKeyList.get(i);
                this.routingLabels.put(key, au.resolveSessionMessage(routinDescList.get(i)));
            }

            this.stepList = new ArrayList<DesignWfStep>();
        }

        public Builder addStep(WfStep step) throws UnifyException {
            if (step.getType().isError() && DataUtils.isBlank(step.getUserActionList())) {
                final WfStepUserAction recoverUserAction = WorkflowDesignUtils.createErrorRecoveryUserAction(step.getId());
                step.setUserActionList(Arrays.asList(recoverUserAction));
            }
            
            workflowSteps.put(step.getName(), step);
            stepList.add(DesignWfStep.from(au, step, routingLabels));
            return this;
        }

        public WorkflowEditor build() throws UnifyException {
            Locale locale = au.getUnifyComponentContext().getSessionContext().getLocale();
            return new WorkflowEditor(au, entityDef, workflowId, workflowSteps, routingLabels,
                    designWfEditTypeMap.get(locale, au), designWfStepTypeMap.get(locale, au), new Design(stepList));
        }
    }

    public static class DesignWfEditType {

        private String name;

        private String caption;

        public DesignWfEditType(String name, String caption) {
            this.name = name;
            this.caption = caption;
        }

        public String getName() {
            return name;
        }

        public String getCaption() {
            return caption;
        }

    }

    public static class DesignWfStepType {

        private String type;

        private String typeDesc;

        private String icon;

        private int color;

        private List<Integer> edits;

        private boolean acceptInflow;

        private boolean deletable;

        public DesignWfStepType(String type, String typeDesc, String icon, int color, List<Integer> edits,
                boolean acceptInflow, boolean deletable) {
            this.type = type;
            this.typeDesc = typeDesc;
            this.icon = icon;
            this.color = color;
            this.edits = edits;
            this.acceptInflow = acceptInflow;
            this.deletable = deletable;
        }

        public String getType() {
            return type;
        }

        public String getTypeDesc() {
            return typeDesc;
        }

        public String getIcon() {
            return icon;
        }

        public int getColor() {
            return color;
        }

        public List<Integer> getEdits() {
            return edits;
        }

        public boolean isAcceptInflow() {
            return acceptInflow;
        }

        public boolean isDeletable() {
            return deletable;
        }

    }

    public static class DesignChange {

        private List<DesignWfStepChange> steps;

        public List<DesignWfStepChange> getSteps() {
            return steps;
        }

        public void setSteps(List<DesignWfStepChange> steps) {
            this.steps = steps;
        }
    }

    public static class Design {

        private List<DesignWfStep> steps;

        public Design(List<DesignWfStep> steps) {
            this.steps = steps;
        }

        public Design() {

        }

        public List<DesignWfStep> getSteps() {
            return steps;
        }

        public void setSteps(List<DesignWfStep> steps) {
            this.steps = steps;
        }

        public DesignWfStep findStep(String name) {
            for (DesignWfStep designWfStep : steps) {
                if (designWfStep.getName().equals(name)) {
                    return designWfStep;
                }
            }

            return null;
        }
    }

    public static class DesignWfStepChange {

        private String name;

        private int x;

        private int y;

        private List<DesignWfStepRouting> routings;

        public DesignWfStepChange() {

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public List<DesignWfStepRouting> getRoutings() {
            return routings;
        }

        public void setRoutings(List<DesignWfStepRouting> routings) {
            this.routings = routings;
        }

        public Map<String, DesignWfStepRouting> getRoutingMap() {
            if (routings != null) {
                Map<String, DesignWfStepRouting> map = new HashMap<String, DesignWfStepRouting>();
                for (DesignWfStepRouting routing : routings) {
                    map.put(routing.getName(), routing);
                }

                return map;
            }

            return Collections.emptyMap();
        }
    }

    public static class DesignWfStep {

        private String name;

        private String description;

        private String label;

        private List<DesignWfStepRouting> routings;

        private List<Integer> census;

        private int typeIndex;

        private int x;

        private int y;

        public DesignWfStep(String name, String description, String label, int typeIndex, int x, int y,
                List<Integer> census, List<DesignWfStepRouting> routings) {
            this.name = name;
            this.description = description;
            this.label = label;
            this.typeIndex = typeIndex;
            this.x = x;
            this.y = y;
            this.census = census;
            this.routings = routings;
        }

        public int getTypeIndex() {
            return typeIndex;
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

        public List<DesignWfStepRouting> getRoutings() {
            return routings;
        }

        public void setRoutings(List<DesignWfStepRouting> routings) {
            this.routings = routings;
        }

        public List<Integer> getCensus() {
            return census;
        }

        public void setCensus(List<Integer> census) {
            this.census = census;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public static DesignWfStep from(AppletUtilities au, WfStep step, Map<String, String> routingLabels)
                throws UnifyException {
            return new DesignWfStep(step.getName(), step.getDescription(), step.getLabel(), step.getType().index(),
                    step.getDesignX(), step.getDesignY(), WorkflowEditor.evaluateCensus(step),
                    WorkflowEditor.evaluateRoutings(step, routingLabels));
        }
    }

    public static class DesignWfStepRouting {

        private String name;

        private String label;

        private String nextStepName;

        public DesignWfStepRouting(String name, String label, String nextStepName) {
            this.name = name;
            this.label = label;
            this.nextStepName = nextStepName;
        }

        public DesignWfStepRouting() {

        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getNextStepName() {
            return nextStepName;
        }

        public void setNextStepName(String nextStepName) {
            this.nextStepName = nextStepName;
        }
    }

    private static List<Integer> evaluateCensus(WfStep step) {
        List<Integer> census = new ArrayList<Integer>();
        switch (step.getType()) {
            case START:
                break;
            case ENRICHMENT:
                census.add(step.getAlertList().size());
                break;
            case PROCEDURE:
                census.add(step.getAlertList().size());
                break;
            case RECORD_ACTION:
                census.add(step.getAlertList().size());
                break;
            case BINARY_ROUTING:
                census.add(step.getAlertList().size());
                break;
            case POLICY_ROUTING:
                census.add(step.getAlertList().size());
                break;
            case MULTI_ROUTING:
                census.add(step.getAlertList().size());
                census.add(step.getRoutingList().size());
                break;
            case NOTIFICATION:
                census.add(step.getAlertList().size());
                break;
            case SET_VALUES:
                census.add(step.getAlertList().size());
                break;
            case USER_ACTION:
                census.add(step.getAlertList().size());
                census.add(step.getUserActionList().size());
                break;
            case ERROR:
                census.add(step.getAlertList().size());
                break;
            case END:
                break;
            default:
                break;

        }

        if (step.getType().supportSetValues()) {
            int size = step.getSetValues() != null && step.getSetValues().getSetValues() != null ? 1 : 0;
            census.add(size);
        }

        return census;
    }

    private static List<DesignWfStepRouting> evaluateRoutings(WfStep step, Map<String, String> routingLabels) {
        List<DesignWfStepRouting> routings = new ArrayList<DesignWfStepRouting>();
        switch (step.getType()) {
            case START:
            case ENRICHMENT:
            case PROCEDURE:
            case RECORD_ACTION:
            case NOTIFICATION:
            case SET_VALUES:
                routings.add(new DesignWfStepRouting("always", routingLabels.get("always"), step.getNextStepName()));
                break;
            case BINARY_ROUTING:
            case POLICY_ROUTING:
                routings.add(new DesignWfStepRouting("ontrue", routingLabels.get("ontrue"), step.getNextStepName()));
                routings.add(
                        new DesignWfStepRouting("onfalse", routingLabels.get("onfalse"), step.getAltNextStepName()));
                break;
            case MULTI_ROUTING:
                for (WfStepRouting stepRouting : step.getRoutingList()) {
                    routings.add(new DesignWfStepRouting(stepRouting.getName(), stepRouting.getDescription(),
                            stepRouting.getNextStepName()));
                }

                routings.add(new DesignWfStepRouting("default", routingLabels.get("default"), step.getNextStepName()));
                break;
            case USER_ACTION:
            case ERROR:
                for (WfStepUserAction stepUserAction : step.getUserActionList()) {
                    routings.add(new DesignWfStepRouting(stepUserAction.getName(), stepUserAction.getLabel(),
                            stepUserAction.getNextStepName()));
                }
                break;
            case END:
                break;
            default:
                break;

        }

        return routings;
    }
}

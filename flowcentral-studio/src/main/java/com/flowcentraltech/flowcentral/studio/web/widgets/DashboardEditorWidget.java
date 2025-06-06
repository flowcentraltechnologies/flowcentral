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

package com.flowcentraltech.flowcentral.studio.web.widgets;

import com.flowcentraltech.flowcentral.common.constants.DialogFormMode;
import com.flowcentraltech.flowcentral.common.web.widgets.AbstractFlowCentralMultiControl;
import com.flowcentraltech.flowcentral.studio.web.widgets.DashboardEditor.Design;
import com.tcdng.unify.core.ApplicationComponents;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.stream.JsonObjectStreamer;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.Control;

/**
 * Dashboard editor widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-dashboardeditor")
@UplAttributes({ @UplAttribute(name = "choiceWidth", type = String.class, defaultVal = "260px") })
public class DashboardEditorWidget extends AbstractFlowCentralMultiControl {

    public static final String WORK_CONTENT = "content";

    public static final String TILES_TAB = "tiles";

    @Configurable(ApplicationComponents.APPLICATION_JSONOBJECTSTREAMER)
    private JsonObjectStreamer jsonObjectStreamer;

    private Control valueCtrl;

    private Control editSectionCtrl;

    private Control editTileCtrl;

    private Control editTileIndexCtrl;

    private Control editModeCtrl;

    private Control editMoveCtrl;

    private String design;

    private String chartName;

    private String move;

    private int sectionIndex;

    private int tileIndex;

    private DialogFormMode editMode;

    @Override
    protected void doOnPageConstruct() throws UnifyException {
        valueCtrl = (Control) addInternalChildWidget("!ui-hidden binding:design");
        editSectionCtrl = (Control) addInternalChildWidget("!ui-hidden binding:sectionIndex");
        editTileCtrl = (Control) addInternalChildWidget("!ui-hidden binding:chartName");
        editTileIndexCtrl = (Control) addInternalChildWidget("!ui-hidden binding:tileIndex");
        editModeCtrl = (Control) addInternalChildWidget("!ui-hidden binding:editMode");
        editMoveCtrl = (Control) addInternalChildWidget("!ui-hidden binding:move");
    }

    @Override
    public void addPageAliases() throws UnifyException {
        addPageAlias(valueCtrl);
    }

    @Action
    public void update() throws UnifyException {

    }

    @Action
    public void editSection() throws UnifyException {
        if (editMode != null) {
            switch (editMode) {
                case CREATE:
                    commandShowPopup(getDashboardEditor().prepareSectionCreate(sectionIndex));
                    break;
                case CREATE_SUB:
                    break;
                case UPDATE:
                    commandShowPopup(getDashboardEditor().prepareSectionEdit(sectionIndex));
                    break;
                case DELETE:
                    commandRefreshPanels(getDashboardEditor().performSectionDel(sectionIndex));
                    break;
                case MOVE:
                    if (!StringUtils.isBlank(move)) {
                        String[] prms = move.split(",");
                        int[] _prms = DataUtils.convert(int[].class, prms);
                        commandRefreshPanels(getDashboardEditor().performSectionMove(_prms));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Action
    public void editTile() throws UnifyException {
        if (editMode != null) {
            switch (editMode) {
                case CREATE:
                    getDashboardEditor().prepareTileCreate(chartName, sectionIndex, tileIndex);
                    commandRefreshPanels(getDashboardEditor().performTileAdd());
                    break;
                case CREATE_SUB:
                    break;
                case UPDATE:
                    commandShowPopup(getDashboardEditor().prepareTileEdit(sectionIndex, tileIndex));
                    break;
                case DELETE:
                    commandRefreshPanels(getDashboardEditor().performTileDel(sectionIndex, tileIndex));
                    break;
                case MOVE:
                    if (!StringUtils.isBlank(move)) {
                        String[] prms = move.split(",");
                        if (prms.length == 4) {
                            int[] _prms = DataUtils.convert(int[].class, prms);
                            commandRefreshPanels(
                                    getDashboardEditor().performTileMove(_prms[0], _prms[1], _prms[2], _prms[3]));
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public String getDesign() {
        return design;
    }

    public void setDesign(String design) throws UnifyException {
        if (!StringUtils.isBlank(design)) {
            getDashboardEditor().setDesign(jsonObjectStreamer.unmarshal(Design.class, design));
        }
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public int getSectionIndex() {
        return sectionIndex;
    }

    public void setSectionIndex(int sectionIndex) {
        this.sectionIndex = sectionIndex;
    }

    public int getTileIndex() {
        return tileIndex;
    }

    public void setTileIndex(int tileIndex) {
        this.tileIndex = tileIndex;
    }

    public DialogFormMode getEditMode() {
        return editMode;
    }

    public void setEditMode(DialogFormMode editMode) {
        this.editMode = editMode;
    }

    public Control getValueCtrl() {
        return valueCtrl;
    }

    public Control getEditSectionCtrl() {
        return editSectionCtrl;
    }

    public Control getEditTileCtrl() {
        return editTileCtrl;
    }

    public Control getEditTileIndexCtrl() {
        return editTileIndexCtrl;
    }

    public Control getEditModeCtrl() {
        return editModeCtrl;
    }

    public Control getEditMoveCtrl() {
        return editMoveCtrl;
    }

    public String getChoiceWidth() throws UnifyException {
        return getUplAttribute(String.class, "choiceWidth");
    }

    public DashboardEditor getDashboardEditor() throws UnifyException {
        return getValue(DashboardEditor.class);
    }

    public String getChartBaseId() throws UnifyException {
        return getPrefixedId("chart_base_");
    }

    public String getDesignBaseId() throws UnifyException {
        return getPrefixedId("design_base_");
    }

    public String getChoiceId() throws UnifyException {
        return getPrefixedId("choice_");
    }

}

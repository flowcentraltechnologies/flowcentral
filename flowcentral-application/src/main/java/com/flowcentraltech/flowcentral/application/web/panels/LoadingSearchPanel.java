/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.web.panels;

import java.util.List;

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.web.widgets.LoadingTable;
import com.flowcentraltech.flowcentral.application.web.widgets.LoadingTableWidget;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionResult;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.data.IndexedTarget;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.UnifyWebSessionAttributeConstants;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.data.ButtonGroupInfo;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;
import com.tcdng.unify.web.ui.widget.data.TaskMonitorInfo;

/**
 * Loading search panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-loadingsearchpanel")
@UplBinding("web/application/upl/loadingsearchpanel.upl")
public class LoadingSearchPanel extends AbstractApplicationPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        LoadingSearch loadingSearch = getLoadingSearch();
        setVisible("sectorIcon", loadingSearch.isWithSectorIcon());

        loadingSearch.ensureTableStruct();
        if (isReloadOnSwitch()) {
            loadingSearch.applySearchEntriesToSearch();
        }

        final LoadingTable loadingTable = loadingSearch.getLoadingTable();
        setVisible("colorLegend", loadingTable.isWithColorLegendInfo());
        setVisible("footerActionPanel", loadingSearch.isShowActionFooter());
        setVisible("searchEntriesPanel", loadingSearch.isShowSearch() && loadingSearch.isWithSearchInput());
        if (loadingSearch.isShowActionFooter()) {
            boolean buttonsForFooterAction = loadingSearch.au().system().getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.SHOW_BUTTONS_FOR_FOOTER_ACTION);
            setVisible("commitActionBtn", false);
            if (buttonsForFooterAction) {
                ButtonGroupInfo.Builder bgib = ButtonGroupInfo.newBuilder();
                bgib.addItems(loadingTable.getActionBtnInfos());
                loadingSearch.setAppTableActionButtonInfo(bgib.build());
                setVisible("tblActionBtns", true);
                setVisible("selFooterActionPanel", false);
            } else {
                loadingSearch.setAppTableActionButtonInfo(null);
                setVisible("tblActionBtns", false);
                setVisible("selFooterActionPanel", true);
            }
        }
    }

    @Action
    public void commitChange() throws UnifyException {
        LoadingSearch loadingSearch = getLoadingSearch();
        loadingSearch.commitChange();
        search();
    }

    @Action
    public final void details() throws UnifyException {
        IndexedTarget target = getRequestTarget(IndexedTarget.class);
        if (target.isValidValueIndex()) {
            LoadingSearch loadingSearch = getLoadingSearch();
            loadingSearch.getLoadingTable().setDetailsIndex(target.getValueIndex());
        }
    }

    @Action
    public final void columns() throws UnifyException {
        IndexedTarget target = getRequestTarget(IndexedTarget.class);
        if (target.isValidValueIndex()) {
            LoadingSearch loadingSearch = getLoadingSearch();
            onColumnSelect(loadingSearch.getLoadingTable().getDispItemList().get(target.getValueIndex()),
                    target.getBinding());
        }
    }

    @Action
    public final void buttons() throws UnifyException {
        IndexedTarget target = getRequestTarget(IndexedTarget.class);
        if (target.isValidValueIndex()) {
            LoadingSearch loadingSearch = getLoadingSearch();
            onAction(loadingSearch.getLoadingTable().getDispItemList().get(target.getValueIndex()), target.getTarget());
        }
    }

    @Action
    public void search() throws UnifyException {
        LoadingSearch loadingSearch = getLoadingSearch();
        loadingSearch.applySearchEntriesToSearch();
        getRequestContextUtil().setContentScrollReset();
    }

    @Action
    public void clear() throws UnifyException {
        LoadingSearch loadingSearch = getLoadingSearch();
        loadingSearch.clearSearchEntries();
        loadingSearch.applySearchEntriesToSearch();
        getRequestContextUtil().setContentScrollReset();
    }

    @Action
    public void applyTableAction() throws UnifyException {
        LoadingSearch loadingSearch = getLoadingSearch();
        applyTableBtnAction(loadingSearch.getAppTableActionPolicy());
    }

    @Action
    public void applyTableBtnAction() throws UnifyException {
        String appTableActionPolicy = getRequestTarget(String.class);
        applyTableBtnAction(appTableActionPolicy);
    }

    protected void onColumnSelect(Entity inst, String column) throws UnifyException {
        LoadingSearch loadingSearch = getLoadingSearch();
        if (!StringUtils.isBlank(loadingSearch.getAppTableActionPolicy())) {
            // TODO
        }
    }

    protected void onAction(Entity inst, String action) throws UnifyException {
        LoadingSearch loadingSearch = getLoadingSearch();
        if (!StringUtils.isBlank(loadingSearch.getAppTableActionPolicy())) {
            // TODO
        }
    }

    private void applyTableBtnAction(String appTableActionPolicy) throws UnifyException {
        LoadingTableWidget tableWidget = getWidgetByShortName(LoadingTableWidget.class, "searchResultTbl");
        if (!StringUtils.isBlank(appTableActionPolicy)) {
            LoadingSearch loadingSearch = getLoadingSearch();
            List<Entity> list = tableWidget.getSelectedItems();
            EntityListActionContext eCtx = new EntityListActionContext(
                    loadingSearch.getLoadingTable().getLoadingItems(), list, appTableActionPolicy);
            EntityListActionResult entityActionResult = loadingSearch.environment().performEntityAction(eCtx);
            handleEntityActionResult(entityActionResult, list.size());
            loadingSearch.applySearchEntriesToSearch();
            commandRefreshPanelsAndHidePopup(this);
        } else {
            hintUser(MODE.WARNING, "$m{entitysearch.actionappliedto.choose.hint}");
        }

        tableWidget.clearSelected();
    }

    private void handleEntityActionResult(EntityListActionResult entityActionResult, int total) throws UnifyException {
        if (entityActionResult != null) {
            if (entityActionResult.isWithTaskResult()) {
                fireEntityActionResultTask(entityActionResult);
            } else {
                hintUser("$m{entitysearch.actionappliedto.items.count.hint}", entityActionResult.getResult(), total);
                return;
            }
        }

        hintUser("$m{entitysearch.actionappliedto.items.hint}");
    }

    private void fireEntityActionResultTask(EntityListActionResult entityActionResult) throws UnifyException {
        TaskMonitor taskMonitor = taskLauncher().launchTask(entityActionResult.getResultTaskSetup());
        TaskMonitorInfo taskMonitorInfo = new TaskMonitorInfo(taskMonitor,
                resolveSessionMessage(entityActionResult.getResultTaskCaption()), null, null);
        setSessionAttribute(UnifyWebSessionAttributeConstants.TASKMONITORINFO, taskMonitorInfo);
        setCommandResultMapping("showapplicationtaskmonitor");
    }

    private LoadingSearch getLoadingSearch() throws UnifyException {
        return getValue(LoadingSearch.class);
    }

}

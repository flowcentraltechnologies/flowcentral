/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.application.constants.AppletRequestAttributeConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.web.widgets.LoadingTable;
import com.flowcentraltech.flowcentral.application.web.widgets.LoadingTableWidget;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionResult;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.task.TaskLauncher;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.UnifyWebSessionAttributeConstants;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.AbstractPanel;
import com.tcdng.unify.web.ui.widget.data.ButtonGroupInfo;
import com.tcdng.unify.web.ui.widget.data.TaskMonitorInfo;

/**
 * Loading search panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-loadingsearchpanel")
@UplBinding("web/application/upl/loadingsearchpanel.upl")
public class LoadingSearchPanel extends AbstractPanel {

    @Configurable
    private ApplicationPrivilegeManager applicationPrivilegeManager;

    @Configurable
    private SystemModuleService systemModuleService;

    @Configurable
    private TaskLauncher taskLauncher;

    public final void setApplicationPrivilegeManager(ApplicationPrivilegeManager applicationPrivilegeManager) {
        this.applicationPrivilegeManager = applicationPrivilegeManager;
    }

    public final void setSystemModuleService(SystemModuleService systemModuleService) {
        this.systemModuleService = systemModuleService;
    }

    public final void setTaskLauncher(TaskLauncher taskLauncher) {
        this.taskLauncher = taskLauncher;
    }

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        LoadingSearch loadingSearch = getLoadingSearch();
        setVisible("sectorIcon", loadingSearch.isWithSectorIcon());
        
        loadingSearch.ensureTableStruct();
        if (Boolean.TRUE.equals(getRequestAttribute(AppletRequestAttributeConstants.RELOAD_ONSWITCH))) {
            loadingSearch.applySearchEntriesToSearch();;
        }

        final LoadingTable loadingTable = loadingSearch.getLoadingTable();
        final TableDef tableDef = loadingTable.getTableDef();
        setVisible("footerActionPanel", loadingSearch.isShowActionFooter());
        if (loadingSearch.isShowActionFooter()) {
            boolean buttonsForFooterAction = systemModuleService.getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.SHOW_BUTTONS_FOR_FOOTER_ACTION);
            if (buttonsForFooterAction) {
                ButtonGroupInfo.Builder bgib = ButtonGroupInfo.newBuilder();
                bgib.addItems(tableDef.getActionBtnInfos());
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
        reload();
    }
    
    @Action
    public void details() throws UnifyException {
        String[] po = StringUtils.charSplit(getRequestTarget(String.class), ':');
        if (po.length > 0) {
            int mIndex = Integer.parseInt(po[1]);
            LoadingSearch loadingSearch = getLoadingSearch();
            loadingSearch.getLoadingTable().setDetailsIndex(mIndex);
         }
    }

    @Action
    public void reload() throws UnifyException {
        LoadingSearch loadingSearch = getLoadingSearch();
        loadingSearch.applySearchEntriesToSearch();;
        getRequestContextUtil().setContentScrollReset();
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

    private void applyTableBtnAction(String appTableActionPolicy) throws UnifyException {
        LoadingSearch loadingSearch = getLoadingSearch();
        if (!StringUtils.isBlank(appTableActionPolicy)) {
            LoadingTableWidget tableWidget = getWidgetByShortName(LoadingTableWidget.class, "searchResultTbl");
            EntityListActionContext eCtx = new EntityListActionContext(tableWidget.getSelectedItems(),
                    appTableActionPolicy);
            EntityListActionResult entityActionResult = loadingSearch.environment().performEntityAction(eCtx);
            handleEntityActionResult(entityActionResult);
//            loadingSearch.applySearchEntriesToSearch();
//            getRequestContextUtil().setContentScrollReset();
        }
    }

    private void handleEntityActionResult(EntityListActionResult entityActionResult) throws UnifyException {
        if (entityActionResult != null && entityActionResult.isWithTaskResult()) {
            fireEntityActionResultTask(entityActionResult);
        }

        hintUser("$m{entitysearch.actionappliedto.items.hint}");
    }

    private void fireEntityActionResultTask(EntityListActionResult entityActionResult) throws UnifyException {
        TaskMonitor taskMonitor = taskLauncher.launchTask(entityActionResult.getResultTaskSetup());
        TaskMonitorInfo taskMonitorInfo = new TaskMonitorInfo(taskMonitor,
                resolveSessionMessage(entityActionResult.getResultTaskCaption()), null, null);
        setSessionAttribute(UnifyWebSessionAttributeConstants.TASKMONITORINFO, taskMonitorInfo);
        setCommandResultMapping("showapplicationtaskmonitor");
    }

    private LoadingSearch getLoadingSearch() throws UnifyException {
        return getValue(LoadingSearch.class);
    }

}

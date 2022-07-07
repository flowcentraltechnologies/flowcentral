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
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTable;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTableWidget;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionResult;
import com.flowcentraltech.flowcentral.common.constants.OwnershipType;
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
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;
import com.tcdng.unify.web.ui.widget.data.TaskMonitorInfo;

/**
 * Entity search panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-entitysearchpanel")
@UplBinding("web/application/upl/entitysearchpanel.upl")
public class EntitySearchPanel extends AbstractPanel {

    @Configurable
    private ApplicationPrivilegeManager applicationPrivilegeManager;

    @Configurable
    private TaskLauncher taskLauncher;

    public void setApplicationPrivilegeManager(ApplicationPrivilegeManager applicationPrivilegeManager) {
        this.applicationPrivilegeManager = applicationPrivilegeManager;
    }

    public void setTaskLauncher(TaskLauncher taskLauncher) {
        this.taskLauncher = taskLauncher;
    }

    @Override
    public void onPageConstruct() throws UnifyException {
        super.onPageConstruct();
        hideSaveFilter();
    }

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        EntitySearch entitySearch = getEntitySearch();
        setVisible("sectorIcon", entitySearch.isWithSectorIcon());

        entitySearch.ensureTableStruct();
        if (Boolean.TRUE.equals(getRequestAttribute(AppletRequestAttributeConstants.RELOAD_ONSWITCH))) {
            entitySearch.applyFilterToSearch();
        }

        String roleCode = getUserToken().getRoleCode();
        EntityTable entityTable = entitySearch.getEntityTable();
        EntityDef entityDef = entityTable.getTableDef().getEntityDef();
        setVisible("newBtn", entitySearch.isNewButtonVisible()
                && applicationPrivilegeManager.isRoleWithPrivilege(roleCode, entityDef.getAddPrivilege()));
        setVisible("editBtn", entitySearch.isEditButtonVisible()
                && applicationPrivilegeManager.isRoleWithPrivilege(roleCode, entityDef.getEditPrivilege()));
        setVisible("viewBtn", entitySearch.isViewButtonVisible()
                && applicationPrivilegeManager.isRoleWithPrivilege(roleCode, entityDef.getEditPrivilege()));
        setVisible("switchToBasic", entityTable.isSupportsBasicSearch());
        if (entitySearch.isBasicSearchOnly() || entityTable.isBasicSearchMode()) {
            setVisible("searchEntriesPanel", true);
            setVisible("searchFilterPanel", false);
            setVisible("quickFilterBlock", entitySearch.isShowQuickFilter());
            setVisible("footerActionPanel", entitySearch.isShowActionFooter());
            setVisible("switchToAdvanced", !entitySearch.isBasicSearchOnly());
            setVisible("searchEntriesHeader", entitySearch.isWithBaseFilter());
        } else {
            setVisible("searchEntriesPanel", false);
            setVisible("searchFilterPanel", true);
            setVisible("quickFilterBlock", entitySearch.isShowQuickFilter());
            setVisible("footerActionPanel", entitySearch.isShowActionFooter());
            setVisible("searchFilterBody", entitySearch.isFilterEditorVisible());
            setDisabled("toggleFilterBtn", !entitySearch.isEditFilterEnabled());
            setVisible("baseFilterTranslation", entitySearch.isWithBaseFilter());
            if (entitySearch.isFilterEditorVisible()) {
                setVisible("tackFilterBtn", entitySearch.isShowFilterThumbtack());
                setDisabled("tackFilterBtn", entitySearch.isFilterEditorPinned());
                setVisible("openSaveFilterBtn", entitySearch.isShowFilterSave());
                if (isWidgetVisible("saveFilterPanel")) {
                    setDisabled("saveFilterScope", !applicationPrivilegeManager.isRoleWithPrivilege(roleCode,
                            entityTable.getSaveGlobalTableQuickFilterPrivilege()));
                }
            }
        }

        setDisabled("fastBackBtn", entityTable.isAtFirstPage());
        setDisabled("backBtn", entityTable.isAtFirstPage());
        setDisabled("forwardBtn", entityTable.isAtLastPage());
        setDisabled("fastForwardBtn", entityTable.isAtLastPage());

        if (!entityTable.isWithRefreshPanels()) {
            entityTable.setRefreshPanelIds(new String[] { getWidgetByShortName("headerRightPanel").getId() });
        }
    }

    @Action
    public void switchToBasicSearch() throws UnifyException {
        getEntitySearch().setBasicSearchMode(true);
    }

    @Action
    public void switchToAdvancedSearch() throws UnifyException {
        getEntitySearch().setBasicSearchMode(false);
    }
    
    @Action
    public void fastBack() throws UnifyException {
        getEntitySearch().getEntityTable().firstPage();
    }

    @Action
    public void back() throws UnifyException {
        getEntitySearch().getEntityTable().prevPage();
    }

    @Action
    public void forward() throws UnifyException {
        getEntitySearch().getEntityTable().nextPage();
    }

    @Action
    public void fastForward() throws UnifyException {
        getEntitySearch().getEntityTable().lastPage();
    }

    @Action
    public void toggleFilter() throws UnifyException {
        getEntitySearch().toggleFilterEditor();
        hideSaveFilter();
        switchState();
    }

    @Action
    public void tackFilter() throws UnifyException {
        getEntitySearch().tackFilterEditor();
        switchState();
    }

    @Action
    public void openSaveFilter() throws UnifyException {
        EntitySearch entitySearch = getEntitySearch();
        entitySearch.setSaveFilterName(null);
        entitySearch.setSaveFilterDesc(null);
        entitySearch.setSaveFilterScope(OwnershipType.USER);
        setVisible("saveFilterPanel", true);
        switchState();
    }

    @Action
    public void saveFilter() throws UnifyException {
        EntitySearch entitySearch = getEntitySearch();
        if (!StringUtils.isBlank(entitySearch.getSaveFilterName())
                && !StringUtils.isBlank(entitySearch.getSaveFilterDesc())) {
            entitySearch.saveQuickFilter(entitySearch.getSaveFilterName(), entitySearch.getSaveFilterDesc(),
                    entitySearch.getSaveFilterScope());
            hideSaveFilter();
            return;
        }

        hintUser(MODE.ERROR, "$m{entitysearchpanel.savefilter.validation}");
    }

    @Action
    public void cancelSaveFilter() throws UnifyException {
        hideSaveFilter();
    }

    @Action
    public void refresh() throws UnifyException {
        EntitySearch entitySearch = getEntitySearch();
        entitySearch.applyFilterToSearch();
        hideFilterEditor();
        getRequestContextUtil().setContentScrollReset();
    }

    @Action
    public void applyQuickFilter() throws UnifyException {
        EntitySearch entitySearch = getEntitySearch();
        entitySearch.applyQuickFilter();
        entitySearch.clearSearchEntries();
        hideFilterEditor();
        getRequestContextUtil().setContentScrollReset();
    }

    @Action
    public void search() throws UnifyException {
        EntitySearch entitySearch = getEntitySearch();
        entitySearch.setAppAppletFilterId(null);
        entitySearch.applySearchEntriesToSearch();
        getRequestContextUtil().setContentScrollReset();
    }

    @Action
    public void clear() throws UnifyException {
        EntitySearch entitySearch = getEntitySearch();
        entitySearch.setAppAppletFilterId(null);
        entitySearch.clearSearchEntries();
        entitySearch.applySearchEntriesToSearch();;
        getRequestContextUtil().setContentScrollReset();
    }

    @Action
    public void runFilter() throws UnifyException {
        EntitySearch entitySearch = getEntitySearch();
        entitySearch.setAppAppletFilterId(null);
        entitySearch.applyFilterToSearch();
        if (!entitySearch.isFilterEditorPinned()) {
            entitySearch.toggleFilterEditor();
        }
        hideSaveFilter();
        getRequestContextUtil().setContentScrollReset();
    }

    @Action
    public void applyTableAction() throws UnifyException {
        EntitySearch entitySearch = getEntitySearch();
        if (!StringUtils.isBlank(entitySearch.getAppTableActionPolicy())) {
            EntityTableWidget tableWidget = getWidgetByShortName(EntityTableWidget.class, "searchResultTbl");
            EntityListActionContext eCtx = new EntityListActionContext(tableWidget.getSelectedItems(),
                    entitySearch.getAppTableActionPolicy());
            EntityListActionResult entityActionResult = entitySearch.environment()
                    .performEntityAction(eCtx);
            handleEntityActionResult(entityActionResult);
            entitySearch.applyFilterToSearch();
            getRequestContextUtil().setContentScrollReset();
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

    private EntitySearch getEntitySearch() throws UnifyException {
        return getValue(EntitySearch.class);
    }

    private void hideSaveFilter() throws UnifyException {
        setVisible("saveFilterPanel", false);
    }
    
    private void hideFilterEditor() throws UnifyException {
        getEntitySearch().hideFilterEditor();
        setVisible("searchFilterBody", false);
        setVisible("saveFilterPanel", false);
    }
}

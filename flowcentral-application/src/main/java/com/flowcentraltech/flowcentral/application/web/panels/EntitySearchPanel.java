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
package com.flowcentraltech.flowcentral.application.web.panels;

import com.flowcentraltech.flowcentral.application.constants.AppletRequestAttributeConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTable;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTableWidget;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityListActionResult;
import com.flowcentraltech.flowcentral.common.constants.OwnershipType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.UnifyWebSessionAttributeConstants;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.PageRequestContextUtil;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;
import com.tcdng.unify.web.ui.widget.data.TaskMonitorInfo;

/**
 * Entity search panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-entitysearchpanel")
@UplBinding("web/application/upl/entitysearchpanel.upl")
public class EntitySearchPanel extends AbstractApplicationPanel {

    @Override
    public void onPageConstruct() throws UnifyException {
        super.onPageConstruct();
        hideSaveFilter();
    }

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        EntitySearch entitySearch = getEntitySearch();
        if (entitySearch != null) {
            final boolean showHeaderParts = !entitySearch.isExpandedMode();
            setVisible("sectorIcon", showHeaderParts && entitySearch.isWithSectorIcon());
            setVisible("titleBlock", showHeaderParts);
            setVisible("headerRightPanel", showHeaderParts);

            // Makes sure edit button does not break on page scroll
            setRequestAttribute(entitySearch.getEditActionKey(), entitySearch.getEditAction());

            entitySearch.ensureTableStruct();
            if (isReloadOnSwitch()) {
                entitySearch.applySearchEntriesToSearch();
            }

            String roleCode = getUserToken().getRoleCode();
            final EntityTable entityTable = entitySearch.getEntityTable();
            final TableDef tableDef = entityTable.getTableDef();
            final EntityDef entityDef = tableDef.getEntityDef();

            setVisible("toggleDetailsBtn", entitySearch.isShowExpandDetails());
            if (entitySearch.isShowExpandDetails()) {
                entitySearch.setToggleDetails(
                        entityTable.isExpandAllDetails() ? resolveSessionMessage("$m{button.collapsedetails}")
                                : resolveSessionMessage("$m{button.expanddetails}"));
            }

            setVisible("newBtn", entitySearch.isNewButtonVisible()
                    && applicationPrivilegeManager().isRoleWithPrivilege(roleCode, entityDef.getAddPrivilege()));
            setVisible("editBtn", entitySearch.isEditButtonVisible()
                    && applicationPrivilegeManager().isRoleWithPrivilege(roleCode, entityDef.getEditPrivilege()));
            final boolean quickEnabled = (entitySearch.isNewButtonVisible() || entitySearch.isEditButtonVisible())
                    && applicationPrivilegeManager().isRoleWithPrivilege(roleCode, entityDef.getEditPrivilege());
            setVisible("quickEditBtn", showHeaderParts && quickEnabled && entitySearch.isShowQuickEdit());
            setVisible("quickOrderBtn", showHeaderParts && quickEnabled && entitySearch.isShowQuickOrder());
            setVisible("viewBtn", entitySearch.isViewButtonVisible()
                    && applicationPrivilegeManager().isRoleWithPrivilege(roleCode, entityDef.getEditPrivilege()));
            setVisible("switchToBasic", showHeaderParts && entityTable.isSupportsBasicSearch());
            setVisible("searchEntriesRequired", showHeaderParts && entitySearch.isRequiredCriteriaNotSet());

            final boolean reportBtnVisible = entityTable.getTotalItemCount() > 0 && entitySearch.isShowReport()
                    && entitySearch.au().reportProvider().isReportable(entityTable.getEntityDef().getLongName())
                    && applicationPrivilegeManager().isRoleWithPrivilege(roleCode,
                            entityTable.getEntityDef().getReportPrivilege());
            setVisible("reportBtn", reportBtnVisible);

            setVisible("colorLegend", entityTable.isWithColorLegendInfo());
            if (entitySearch.isBasicSearchOnly() || entityTable.isBasicSearchMode()) {
                setVisible("searchEntriesPanel", showHeaderParts && entitySearch.isShowSearch() && entitySearch.isWithSearchInput());
                setVisible("searchFilterPanel", false);
                setVisible("quickFilterBlock", showHeaderParts && entitySearch.isShowQuickFilter());
                setVisible("footerActionPanel", showHeaderParts && entitySearch.isShowActionFooter());
                setVisible("switchToAdvanced", showHeaderParts && !entitySearch.isBasicSearchOnly());
                setVisible("searchEntriesHeader", showHeaderParts && entitySearch.isShowBaseFilter());
            } else {
                setVisible("searchEntriesPanel", false);
                setVisible("searchFilterPanel", showHeaderParts && entitySearch.isShowSearch());
                setVisible("quickFilterBlock", showHeaderParts && entitySearch.isShowQuickFilter());
                setVisible("footerActionPanel", showHeaderParts && entitySearch.isShowActionFooter());
                setVisible("searchFilterBody", showHeaderParts && entitySearch.isFilterEditorVisible());
                setDisabled("toggleFilterBtn", showHeaderParts && !entitySearch.isEditFilterEnabled());
                setVisible("baseFilterTranslation", showHeaderParts && entitySearch.isShowBaseFilter());
                if (entitySearch.isFilterEditorVisible()) {
                    setVisible("tackFilterBtn", showHeaderParts && entitySearch.isShowFilterThumbtack());
                    setDisabled("tackFilterBtn", showHeaderParts && entitySearch.isFilterEditorPinned());
                    setVisible("openSaveFilterBtn", showHeaderParts && entitySearch.isShowFilterSave());
                    if (isWidgetVisible("saveFilterPanel")) {
                        setDisabled("saveFilterScope", !applicationPrivilegeManager().isRoleWithPrivilege(roleCode,
                                entityTable.getSaveGlobalTableQuickFilterPrivilege()));
                    }
                }
            }

            if (showHeaderParts && entitySearch.isShowActionFooter()) {
                boolean buttonsForFooterAction = system().getSysParameterValue(boolean.class,
                        ApplicationModuleSysParamConstants.SHOW_BUTTONS_FOR_FOOTER_ACTION);
                setVisible("tblActionBtns", buttonsForFooterAction);
                setVisible("selFooterActionPanel", !buttonsForFooterAction);
            }

            setDisabled("fastBackBtn", entityTable.isAtFirstPage());
            setDisabled("backBtn", entityTable.isAtFirstPage());
            setDisabled("forwardBtn", entityTable.isAtLastPage());
            setDisabled("fastForwardBtn", entityTable.isAtLastPage());

            if (!entityTable.isWithRefreshPanels()) {
                entityTable.setRefreshPanelIds(new String[] { getWidgetByShortName("headerRightPanel").getId() });
            }
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
        if (entitySearch != null) {
            entitySearch.applySearchEntriesToSearch();
            hideFilterEditor();

            PageRequestContextUtil rcUtil = getRequestContextUtil();
            if (entitySearch.isWithPushFormIds()) {
                rcUtil.addListItem(AppletRequestAttributeConstants.MAINFORM_PUSH_COMPONENTS,
                        entitySearch.getPushFormIds());
            }

            if (entitySearch.isWithEditActionKey()) {
                setRequestAttribute(entitySearch.getEditActionKey(), entitySearch.getEditAction());
            }

            rcUtil.setContentScrollReset();
        }
    }

    @Action
    public void applyQuickFilter() throws UnifyException {
        EntitySearch entitySearch = getEntitySearch();
        if (entitySearch != null) {
            entitySearch.applyQuickFilter();
            entitySearch.clearSearchEntries();
            hideFilterEditor();
            getRequestContextUtil().setContentScrollReset();
        }
    }

    @Action
    public void search() throws UnifyException {
        EntitySearch entitySearch = getEntitySearch();
        if (entitySearch != null) {
            entitySearch.setAppAppletFilterName(null);
            entitySearch.applySearchEntriesToSearch();
            getRequestContextUtil().setContentScrollReset();
        }
    }

    @Action
    public void clear() throws UnifyException {
        EntitySearch entitySearch = getEntitySearch();
        if (entitySearch != null) {
            entitySearch.setAppAppletFilterName(null);
            entitySearch.clearSearchEntries();
            entitySearch.applySearchEntriesToSearch();
            getRequestContextUtil().setContentScrollReset();
        }
    }

    @Action
    public void toggleDetails() throws UnifyException {
        EntitySearch entitySearch = getEntitySearch();
        if (entitySearch != null) {
            entitySearch.getEntityTable().setExpandAllDetails(!entitySearch.getEntityTable().isExpandAllDetails());
            getRequestContextUtil().setContentScrollReset();
        }
    }

    @Action
    public void runFilter() throws UnifyException {
        EntitySearch entitySearch = getEntitySearch();
        if (entitySearch != null) {
            entitySearch.setAppAppletFilterName(null);
            entitySearch.applyFilterToSearch();
            if (!entitySearch.isFilterEditorPinned()) {
                entitySearch.toggleFilterEditor();
            }
            hideSaveFilter();
            getRequestContextUtil().setContentScrollReset();
        }
    }

    @Action
    public void applyTableAction() throws UnifyException {
        EntitySearch entitySearch = getEntitySearch();
        if (entitySearch != null) {
            applyTableBtnAction(entitySearch.getAppTableActionPolicy());
        }
    }

    @Action
    public void applyTableBtnAction() throws UnifyException {
        String appTableActionPolicy = getRequestTarget(String.class);
        applyTableBtnAction(appTableActionPolicy);
    }

    private void applyTableBtnAction(String appTableActionPolicy) throws UnifyException {
        EntityTableWidget tableWidget = getWidgetByShortName(EntityTableWidget.class, "searchResultTbl");
        if (!StringUtils.isBlank(appTableActionPolicy)) {
            EntitySearch entitySearch = getEntitySearch();
            EntityListActionContext eCtx = new EntityListActionContext(tableWidget.getSelectedItems(),
                    appTableActionPolicy);
            EntityListActionResult entityActionResult = entitySearch.environment().performEntityAction(eCtx);
            handleEntityActionResult(entityActionResult);
            entitySearch.applySearchEntriesToSearch();
            getRequestContextUtil().setContentScrollReset();
        }

        tableWidget.clearSelected();
    }

    private void handleEntityActionResult(EntityListActionResult entityActionResult) throws UnifyException {
        if (entityActionResult != null && entityActionResult.isWithTaskResult()) {
            fireEntityActionResultTask(entityActionResult);
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

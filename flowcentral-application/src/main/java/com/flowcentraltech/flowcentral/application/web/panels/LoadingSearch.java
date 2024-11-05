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
package com.flowcentraltech.flowcentral.application.web.panels;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityItem;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.LoadingWorkItemInfo;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.policies.LoadingParams;
import com.flowcentraltech.flowcentral.application.policies.LoadingTableProvider;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.application.web.widgets.InputArrayEntries;
import com.flowcentraltech.flowcentral.application.web.widgets.LoadingTable;
import com.flowcentraltech.flowcentral.application.web.widgets.SearchEntries;
import com.flowcentraltech.flowcentral.application.web.widgets.SectorIcon;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.business.SpecialParamProvider;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.Order;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.web.ui.widget.data.ButtonGroupInfo;

/**
 * Loading search object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class LoadingSearch {

    public static final int SHOW_ACTIONFOOTER = 0x00000002;

    public static final int SHOW_SEARCH = 0x00000001;

    public static final int ENABLE_ALL = SHOW_ACTIONFOOTER | SHOW_SEARCH;

    private final Long appAppletId;

    final private AppletContext appletContext;

    final private SectorIcon sectorIcon;

    private FilterDef baseFilterDef;

    private SearchEntries searchEntries;

    private Restriction srcBaseRestriction;

    private Restriction baseRestriction;

    private LoadingTable loadingTable;

    private String entitySubTitle;

    private String baseFilterTranslation;

    private String entityFilterTranslation;

    private String appTableActionPolicy;

    private String commitCaption;

    private ButtonGroupInfo appTableActionButtonInfo;

    private int mode;

    private boolean showActionFooter;
    
    public LoadingSearch(AppletContext appletContext, SectorIcon sectorIcon, TableDef tableDef, Long appAppletId,
            String searchInputConfigName, String preferredSearchEvent, int columns, int mode,
            boolean showConditions) throws UnifyException {
        this.appletContext = appletContext;
        this.sectorIcon = sectorIcon;
        this.searchEntries = new SearchEntries(appletContext.au(), tableDef.getEntityDef(),
                tableDef.getLabelSuggestionDef(), searchInputConfigName, preferredSearchEvent, columns,
                showConditions);
        this.loadingTable = new LoadingTable(appletContext.au(), tableDef);
        this.loadingTable.setCrudMode(LoadingTable.CrudMode.SIMPLE);
        this.loadingTable.setViewOnly(true);
        this.appAppletId = appAppletId;
        this.mode = mode;
    }

    public AppletUtilities au() {
        return appletContext.au();
    }

    public EnvironmentService environment() {
        return appletContext.environment();
    }

    public SearchEntries getSearchEntries() {
        return searchEntries;
    }

    public void clearSearchEntries() {
        if (searchEntries != null) {
            searchEntries.clear();
        }
    }

    public boolean isWithSearchInput() {
        return searchEntries != null && !searchEntries.isEmpty();
    }

    public String getCommitCaption() {
        return commitCaption;
    }

    public void setCommitCaption(String commitCaption) {
        this.commitCaption = commitCaption;
    }

    public LoadingTable getLoadingTable() {
        return loadingTable;
    }

    public String getEntityTitle() {
        return loadingTable.getTableDef().getLabel();
    }

    public EntityDef getEntityDef() {
        return loadingTable.getTableDef().getEntityDef();
    }

    public String getEntitySubTitle() {
        return entitySubTitle;
    }

    public void setEntitySubTitle(String entitySubTitle) {
        this.entitySubTitle = entitySubTitle;
    }

    public Long getAppAppletId() {
        return appAppletId;
    }

    public Long getAppTableId() {
        return loadingTable.getTableDef().getId();
    }

    public String getAppTableActionPolicy() {
        return appTableActionPolicy;
    }

    public void setAppTableActionPolicy(String appTableActionPolicy) {
        this.appTableActionPolicy = appTableActionPolicy;
    }

    public ButtonGroupInfo getAppTableActionButtonInfo() {
        return appTableActionButtonInfo;
    }

    public void setAppTableActionButtonInfo(ButtonGroupInfo appTableActionButtonInfo) {
        this.appTableActionButtonInfo = appTableActionButtonInfo;
    }

    public String getEntityFilterTranslation() {
        return entityFilterTranslation;
    }

    public String getBaseFilterTranslation() {
        return baseFilterTranslation;
    }

    public SectorIcon getSectorIcon() {
        return sectorIcon;
    }

    public boolean isWithSectorIcon() {
        return sectorIcon != null;
    }

    public void commitChange() throws UnifyException {
        loadingTable.commitChange();
    }

    public LoadingTableProvider getLoadingTableProvider(int itemIndex) throws UnifyException {
        return loadingTable.getLoadingTableProvider(itemIndex);
    }

    public EntityItem getSourceItem(int index) throws UnifyException {
        Entity loadingEntity = loadingTable.getDispItemList().get(index);
        LoadingTableProvider loadingTableProvider = getLoadingTableProvider(index);
        int options = loadingTableProvider.getSourceItemOptions(loadingEntity);
        return loadingTableProvider != null ? loadingTableProvider.getSourceItemById((Long) loadingEntity.getId(), options)
                : null;
    }

    public String getSourceItemFormApplet(int index) throws UnifyException {
        LoadingTableProvider loadingTableProvider = getLoadingTableProvider(index);
        return loadingTableProvider != null ? loadingTableProvider.getSourceItemFormApplet() : null;
    }

    public LoadingWorkItemInfo getLoadingWorkItemInfo(WorkEntity inst, int index) throws UnifyException {
        LoadingTableProvider loadingTableProvider = getLoadingTableProvider(index);
        return loadingTableProvider != null ? loadingTableProvider.getLoadingWorkItemInfo(inst)
                : new LoadingWorkItemInfo();
    }

    public boolean applyUserAction(WorkEntity wfEntityInst, String userAction, String comment, InputArrayEntries emails,
            int index, boolean listing) throws UnifyException {
        Entity loadingEntity = loadingTable.getDispItemList().get(index);
        LoadingTableProvider loadingTableProvider = getLoadingTableProvider(index);
        return loadingTableProvider != null
                ? loadingTableProvider.applyUserAction(wfEntityInst, (Long) loadingEntity.getId(), userAction, comment,
                        emails, listing)
                : false;
    }

    public boolean isNewCommentRequired(String userAction, int index) throws UnifyException {
        LoadingTableProvider loadingTableProvider = getLoadingTableProvider(index);
        return loadingTableProvider != null ? loadingTableProvider.isNewCommentRequired(userAction) : false;
    }

    public void setOrder(Order order) {
        loadingTable.setOrder(order);
    }

    public void setBaseFilter(FilterDef filterDef, SpecialParamProvider specialParamProvider) throws UnifyException {
        this.baseFilterDef = filterDef;
        setBaseRestriction(null, specialParamProvider);
    }

    public void setBaseRestriction(Restriction restriction, SpecialParamProvider specialParamProvider)
            throws UnifyException {
        if (baseFilterDef != null || restriction != null) {
            And and = new And();
            if (baseFilterDef != null) {
                and.add(baseFilterDef.getRestriction(getEntityDef(), null, appletContext.au().getNow()));
                if (baseFilterDef.isWithFilterGenerator()) {
                    srcBaseRestriction = restriction;
                }
            }

            if (restriction != null) {
                and.add(restriction);
            }

            baseRestriction = and;
            baseFilterTranslation = appletContext.au().resolveSessionMessage("$m{loadingsearch.basefilter.translation}",
                    appletContext.au().translate(baseRestriction, getEntityDef()));
            return;
        }

        baseRestriction = null;
        baseFilterTranslation = null;
    }

    public void clearBaseRestriction() {
        baseRestriction = null;
        baseFilterTranslation = null;
    }

    public boolean isWithBaseFilter() {
        return baseRestriction != null;
    }

    public void recalcBaseRestriction() throws UnifyException {
        if (baseFilterDef != null && baseFilterDef.isWithFilterGenerator()) {
            setBaseRestriction(srcBaseRestriction, appletContext.au().specialParamProvider());
        }
    }

    public void ensureTableStruct() throws UnifyException {
        searchEntries.normalize();
        if (loadingTable != null) {
            TableDef _eTableDef = loadingTable.getTableDef();
            TableDef _nTableDef = appletContext.au().getTableDef(_eTableDef.getLongName());
            if (_eTableDef.getVersion() != _nTableDef.getVersion()) {
                loadingTable = new LoadingTable(appletContext.au(), _nTableDef);
                applySearchEntriesToSearch();
            }
        }
    }

    public void applySearchEntriesToSearch() throws UnifyException {
        SearchEntries.Entries entries = searchEntries.getEntries();
        Restriction searchRestriction = null;
        if (isWithBaseFilter()) {
            recalcBaseRestriction();            
            if (entries.isWithRestriction()) {
                searchRestriction = new And().add(baseRestriction).add(entries.getRestriction());
            } else {
                searchRestriction = baseRestriction;
            }
        } else {
            searchRestriction = entries.getRestriction();
        }

        final EntityDef entityDef = loadingTable.getEntityDef();
        Restriction branchScopeRestriction = au().getSessionBranchScopeRestriction(entityDef);
        if (branchScopeRestriction != null) {
            searchRestriction = searchRestriction == null ? branchScopeRestriction
                    : new And().add(searchRestriction).add(branchScopeRestriction);
        }

        loadingTable.setSourceObjectClearSelected(new LoadingParams(searchRestriction, entries.getInputs()));
    }

    public boolean isShowActionFooter() {
        return showActionFooter || (mode & SHOW_ACTIONFOOTER) > 0;
    }

    public void setShowActionFooter(boolean showActionFooter) {
        this.showActionFooter = showActionFooter;
    }

    public boolean isShowSearch() {
        return (mode & SHOW_SEARCH) > 0;
    }

}

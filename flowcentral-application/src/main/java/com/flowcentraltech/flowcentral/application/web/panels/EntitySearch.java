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

import java.text.MessageFormat;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTable;
import com.flowcentraltech.flowcentral.application.web.widgets.Filter;
import com.flowcentraltech.flowcentral.application.web.widgets.SearchEntries;
import com.flowcentraltech.flowcentral.application.web.widgets.SectorIcon;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.business.SpecialParamProvider;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.constants.OwnershipType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.FilterConditionListType;
import com.tcdng.unify.core.criterion.Order;
import com.tcdng.unify.core.criterion.Restriction;

/**
 * Entity search object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntitySearch extends AbstractPanelFormBinding {

    public static final int EDIT_FILTER_ENABLED = 0x00000001;
    public static final int SHOW_FILTER_SAVE = 0x00000002;
    public static final int SHOW_FILTER_THUMBTACK = 0x00000004;
    public static final int SHOW_NEW_BUTTON = 0x00000010;
    public static final int SHOW_EDIT_BUTTON = 0x00000020;
    public static final int SHOW_QUICKFILTER = 0x00000040;
    public static final int SHOW_ACTIONFOOTER = 0x00000080;
    public static final int SHOW_SEARCH = 0x00000100;

    public static final int ENABLE_ALL = EDIT_FILTER_ENABLED | SHOW_FILTER_SAVE | SHOW_FILTER_THUMBTACK
            | SHOW_NEW_BUTTON | SHOW_EDIT_BUTTON | SHOW_QUICKFILTER | SHOW_ACTIONFOOTER | SHOW_SEARCH;

    private final Long appAppletId;

    private final SectorIcon sectorIcon;

    private FilterDef baseFilterDef;

    private Filter entityFilter;

    private SearchEntries searchEntries;

    private Restriction baseRestriction;

    private EntityTable entityTable;

    private String entitySubTitle;

    private String entityNewLabel;

    private String headlessList;

    private String relatedList;

    private String baseFilterTranslation;

    private String entityFilterTranslation;

    private String paginationLabel;

    private String editAction;

    private String saveFilterName;

    private String saveFilterDesc;

    private String appTableActionPolicy;

    private OwnershipType saveFilterScope;

    private Long appAppletFilterId;

    private int childTabIndex;

    private int mode;

    private boolean filterEditorPinned;

    private boolean filterEditorVisible;

    private boolean basicSearchOnly;

    private boolean newButtonVisible;

    public EntitySearch(FormContext ctx, SectorIcon sectorIcon, SweepingCommitPolicy sweepingCommitPolicy,
            String tabName, TableDef tableDef, Long appAppletId, String editAction, Long appAppletFilterId, int mode,
            boolean ignoreConditionalDisabled) throws UnifyException {
        super(ctx, sweepingCommitPolicy, tabName, ignoreConditionalDisabled);
        this.sectorIcon = sectorIcon;
        this.entityFilter = new Filter(null, null, tableDef.getEntityDef(), tableDef.getLabelSuggestionDef(),
                FilterConditionListType.IMMEDIATE_FIELD);
        this.appAppletFilterId = appAppletFilterId;
        if (appAppletFilterId != null) {
            localApplyQuickFilter();
        }

        this.searchEntries = new SearchEntries(tableDef.getEntityDef(), tableDef.getLabelSuggestionDef(), 4);
        this.entityTable = new EntityTable(ctx.au(), tableDef, null);
        this.appAppletId = appAppletId;
        this.editAction = editAction;
        this.mode = mode;
        this.newButtonVisible = true;
    }

    public AppletUtilities au() {
        return getAppletCtx().au();
    }

    public EnvironmentService environment() {
        return getAppletCtx().environment();
    }

    public void setBasicSearchMode(boolean basicSearchMode) throws UnifyException {
        setAppAppletFilterId(null);
        entityTable.setBasicSearchMode(basicSearchMode);
        if (basicSearchMode) {
            if (searchEntries != null) {
                searchEntries.clear();
                applySearchEntriesToSearch();
            }
        } else {
            if (entityFilter != null) {
                entityFilter.clear();
                applyFilterToSearch();
            }
        }
    }

    public SectorIcon getSectorIcon() {
        return sectorIcon;
    }

    public boolean isWithSectorIcon() {
        return sectorIcon != null;
    }

    public Filter getEntityFilter() {
        return entityFilter;
    }

    public SearchEntries getSearchEntries() {
        return searchEntries;
    }

    public void clearSearchEntries() {
        if (searchEntries != null) {
            searchEntries.clear();
        }
    }

    public EntityTable getEntityTable() {
        return entityTable;
    }

    public String getEntityTitle() {
        return entityTable.getTableDef().getLabel();
    }

    public EntityDef getEntityDef() {
        return entityTable.getTableDef().getEntityDef();
    }

    public String getEntitySubTitle() {
        return entitySubTitle;
    }

    public void setEntitySubTitle(String entitySubTitle) {
        this.entitySubTitle = entitySubTitle;
    }

    public String getEntityNewLabel() {
        return entityNewLabel;
    }

    public void setEntityNewLabel(String entityNewLabel) {
        this.entityNewLabel = entityNewLabel;
    }

    public Long getAppAppletId() {
        return appAppletId;
    }

    public Long getAppTableId() {
        return entityTable.getTableDef().getId();
    }

    public String getAppTableActionPolicy() {
        return appTableActionPolicy;
    }

    public void setAppTableActionPolicy(String appTableActionPolicy) {
        this.appTableActionPolicy = appTableActionPolicy;
    }

    public Long getAppAppletFilterId() {
        return appAppletFilterId;
    }

    public void setAppAppletFilterId(Long appAppletFilterId) {
        this.appAppletFilterId = appAppletFilterId;
    }

    public String getEditAction() {
        return editAction;
    }

    public String getSaveFilterName() {
        return saveFilterName;
    }

    public void setSaveFilterName(String saveFilterName) {
        this.saveFilterName = saveFilterName;
    }

    public String getSaveFilterDesc() {
        return saveFilterDesc;
    }

    public void setSaveFilterDesc(String saveFilterDesc) {
        this.saveFilterDesc = saveFilterDesc;
    }

    public OwnershipType getSaveFilterScope() {
        return saveFilterScope;
    }

    public void setSaveFilterScope(OwnershipType saveFilterScope) {
        this.saveFilterScope = saveFilterScope;
    }

    public int getChildTabIndex() {
        return childTabIndex;
    }

    public void setChildTabIndex(int childTabIndex) {
        this.childTabIndex = childTabIndex;
    }

    public String getHeadlessList() {
        return headlessList;
    }

    public void setHeadlessList(String headlessList) {
        this.headlessList = headlessList;
    }

    public String getRelatedList() {
        return relatedList;
    }

    public void setRelatedList(String relatedList) {
        this.relatedList = relatedList;
    }

    public String getEntityFilterTranslation() {
        return entityFilterTranslation;
    }

    public String getBaseFilterTranslation() {
        return baseFilterTranslation;
    }

    public String getPaginationLabel() {
        return MessageFormat.format(paginationLabel, entityTable.getDispEndIndex(), entityTable.getTotalItemCount());
    }

    public void setPaginationLabel(String paginationLabel) {
        this.paginationLabel = paginationLabel;
    }

    public int getDisplayStart() {
        if (entityTable.getTotalItemCount() == 0) {
            return 0;
        }

        return entityTable.getDispStartIndex() + 1;
    }

    public void setDisplayStart(int dispStartIndex) {

    }

    public int getTotalItemCount() {
        return entityTable.getTotalItemCount();
    }

    public void setOrder(Order order) {
        entityTable.setOrder(order);
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
                and.add(baseFilterDef.getRestriction(entityFilter.getEntityDef(),
                        getAppletCtx().au().getNow()));
            }

            if (restriction != null) {
                and.add(restriction);
            }

            baseRestriction = and;
            baseFilterTranslation = getAppletCtx().au().resolveSessionMessage("$m{entitysearch.basefilter.translation}",
                    getAppletCtx().au().translate(baseRestriction, entityFilter.getEntityDef()));
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

    public void applyQuickFilter() throws UnifyException {
        localApplyQuickFilter();
        applyFilterToSearch();
        hideFilterEditor();
    }

    public void saveQuickFilter(String name, String description, OwnershipType ownershipType) throws UnifyException {
        getAppletCtx().au().saveAppletQuickFilterDef(getSweepingCommitPolicy(), appAppletId, name, description,
                ownershipType, entityFilter.getFilterDef(getAppletCtx().au()));
    }

    public void ensureTableStruct() throws UnifyException {
        searchEntries.normalize();
        if (entityTable != null) {
            TableDef _eTableDef = entityTable.getTableDef();
            TableDef _nTableDef = getAppletCtx().au().getTableDef(_eTableDef.getLongName());
            if (_eTableDef.getVersion() != _nTableDef.getVersion()) {
                entityTable = new EntityTable(getAppletCtx().au(), _nTableDef, null);
                applyFilterToSearch();
            }
        }
    }

    public void applySearchEntriesToSearch() throws UnifyException {
        EntityDef entityDef = searchEntries.getEntityDef();
        Restriction restriction = searchEntries.getRestriction();
        applyRestrictionToSearch(entityDef, restriction);
    }

    public void applyFilterToSearch() throws UnifyException {
        EntityDef entityDef = entityFilter.getEntityDef();
        Restriction restriction = entityFilter.getRestriction(getAppletCtx().au().getNow());
        applyRestrictionToSearch(entityDef, restriction);
        entityFilterTranslation = getAppletCtx().au().translate(restriction, entityDef);
    }

    private void localApplyQuickFilter() throws UnifyException {
        FilterDef quickFilterDef = appAppletFilterId != null
                ? getAppletCtx().au().retrieveFilterDef("applet", "application.appAppletFilter", appAppletFilterId)
                : null;
        entityFilter = quickFilterDef != null
                ? new Filter(null, null, entityFilter.getEntityDef(), quickFilterDef,
                        FilterConditionListType.IMMEDIATE_FIELD)
                : new Filter(null, null, entityFilter.getEntityDef(), entityFilter.getLabelSuggestionDef(),
                        FilterConditionListType.IMMEDIATE_FIELD);
    }

    private void applyRestrictionToSearch(EntityDef entityDef, Restriction restriction) throws UnifyException {
        Restriction searchRestriction = null;
        if (isWithBaseFilter()) {
            if (restriction != null) {
                searchRestriction = new And().add(baseRestriction).add(restriction);
            } else {
                searchRestriction = baseRestriction;
            }
        } else {
            searchRestriction = restriction;
        }

        entityTable.setSourceObject(searchRestriction);
    }

    public void hideFilterEditor() {
        filterEditorVisible = false;
        filterEditorPinned = false;
    }

    public void toggleFilterEditor() {
        filterEditorVisible = !filterEditorVisible;
        filterEditorPinned = false;
    }

    public void tackFilterEditor() {
        filterEditorPinned = true;
    }

    public boolean isFilterEditorVisible() {
        return filterEditorVisible;
    }

    public boolean isFilterEditorPinned() {
        return filterEditorPinned;
    }

    public boolean isBasicSearchOnly() {
        return basicSearchOnly;
    }

    public void setBasicSearchOnly(boolean basicSearchOnly) {
        this.basicSearchOnly = basicSearchOnly;
    }

    public void setNewButtonVisible(boolean newButtonVisible) {
        this.newButtonVisible = newButtonVisible;
    }

    public boolean isNewButtonVisible() {
        return newButtonVisible && getAppletCtx().isContextEditable() && isTabEditable()
                && (mode & SHOW_NEW_BUTTON) > 0;
    }

    public boolean isEditButtonVisible() {
        return getAppletCtx().isContextEditable() && isTabEditable() && (mode & SHOW_EDIT_BUTTON) > 0;
    }

    public boolean isViewButtonVisible() {
        return (!getAppletCtx().isContextEditable() || !isTabEditable()) && (mode & SHOW_EDIT_BUTTON) > 0;
    }

    public boolean isEditFilterEnabled() {
        return (mode & EDIT_FILTER_ENABLED) > 0;
    }

    public boolean isShowFilterSave() {
        return (mode & SHOW_FILTER_SAVE) > 0;
    }

    public boolean isShowFilterThumbtack() {
        return (mode & SHOW_FILTER_THUMBTACK) > 0;
    }

    public boolean isShowQuickFilter() {
        return (mode & SHOW_QUICKFILTER) > 0;
    }

    public boolean isShowActionFooter() {
        return (mode & SHOW_ACTIONFOOTER) > 0;
    }

    public boolean isShowSearch() {
        return (mode & SHOW_SEARCH) > 0;
    }

}

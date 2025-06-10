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

import java.text.MessageFormat;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTable;
import com.flowcentraltech.flowcentral.application.web.widgets.Filter;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.business.SpecialParamProvider;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.FilterConditionListType;
import com.tcdng.unify.core.criterion.Order;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.web.ui.widget.data.BadgeInfo;

/**
 * Entity choice object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EntityChoice {

    public static final int SHOW_ACTIONFOOTER = 0x00000001;

    public static final int ENABLE_ALL = SHOW_ACTIONFOOTER;

    private final AppletUtilities au;

    private FilterDef baseFilterDef;

    private Filter entityFilter;

    private Restriction srcBaseRestriction;

    private Restriction baseRestriction;

    private EntityTable entityTable;

    private String entitySubTitle;

    private String entityNewLabel;

    private String baseFilterTranslation;

    private String entityFilterTranslation;

    private String paginationLabel;

    private int mode;

    private boolean showBaseRestriction;

    public EntityChoice(AppletUtilities au, TableDef tableDef, int mode) throws UnifyException {
        this.au = au;
        this.entityFilter = new Filter(au, null, null, tableDef.getEntityDef(), tableDef.getLabelSuggestionDef(),
                "application.sessionparamtypelist", FilterConditionListType.IMMEDIATE_FIELD);
        this.entityTable = new EntityTable(au, tableDef, null);
        this.mode = mode;
    }

    public AppletUtilities au() {
        return au;
    }

    public EnvironmentService environment() {
        return au.environment();
    }

    public void setChoiceConfig(BadgeInfo choiceBadgeInfo) throws UnifyException {
        entityTable.setChoiceConfig(choiceBadgeInfo);
    }

    public Filter getEntityFilter() {
        return entityFilter;
    }

    public EntityTable getEntityTable() {
        return entityTable;
    }

    public EntityDef getEntityDef() {
        return entityTable.getTableDef().getEntityDef();
    }

    public String getEntitySubTitle() throws UnifyException {
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

    public Long getAppTableId() {
        return entityTable.getTableDef().getId();
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

    public boolean isRequiredCriteriaNotSet() {
        return entityTable.isRequiredCriteriaNotSet();
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
                and.add(baseFilterDef.getRestriction(entityFilter.getEntityDef(), null, au.getNow()));
                if (baseFilterDef.isWithFilterGenerator()) {
                    srcBaseRestriction = restriction;
                }
            }

            if (restriction != null) {
                and.add(restriction);
            }

            baseRestriction = and;
            baseFilterTranslation = au.resolveSessionMessage("$m{entitysearch.basefilter.translation}",
                    au.translate(baseRestriction, entityFilter.getEntityDef()));
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
            setBaseRestriction(srcBaseRestriction, au.specialParamProvider());
        }
    }

    public boolean isShowBaseFilter() {
        return showBaseRestriction && isWithBaseFilter();
    }

    public void ensureTableStruct() throws UnifyException {
        if (entityTable != null) {
            TableDef _eTableDef = entityTable.getTableDef();
            TableDef _nTableDef = au.getTableDef(_eTableDef.getLongName());
            if (_eTableDef.getVersion() != _nTableDef.getVersion()) {
                entityTable = new EntityTable(au, _nTableDef, null);
                applyFilterToSearch();
            }
        }
    }

    public void applyFilterToSearch() throws UnifyException {
        EntityDef entityDef = entityFilter.getEntityDef();
        Restriction restriction = entityFilter.getRestriction(au.getNow());
        applyRestrictionToSearch(entityDef, restriction);
        entityFilterTranslation = au.translate(restriction, entityDef);
    }

    public boolean isShowBaseRestriction() {
        return showBaseRestriction;
    }

    public void setShowBaseRestriction(boolean showBaseRestriction) {
        this.showBaseRestriction = showBaseRestriction;
    }

    public boolean isShowActionFooter() {
        return (mode & SHOW_ACTIONFOOTER) > 0;
    }

    private void applyRestrictionToSearch(EntityDef entityDef, Restriction restriction) throws UnifyException {
        Restriction searchRestriction = null;
        if (isWithBaseFilter()) {
            recalcBaseRestriction();
            if (restriction != null) {
                searchRestriction = new And().add(baseRestriction).add(restriction);
            } else {
                searchRestriction = baseRestriction;
            }
        } else {
            searchRestriction = restriction;
        }

        Restriction branchScopeRestriction = au().getSessionBranchScopeRestriction(entityDef);
        if (branchScopeRestriction != null) {
            searchRestriction = searchRestriction == null ? branchScopeRestriction
                    : new And().add(searchRestriction).add(branchScopeRestriction);
        }

        entityTable.setSourceObjectClearSelected(searchRestriction);
        au().clearReloadOnSwitch();
    }

}

/*
 * Copyright (c) 2019, 2021, FlowCentral Technologies.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
 */
package com.flowcentraltech.flowcentral.application.web.panels;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.widgets.Filter;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.Editable;
import com.tcdng.unify.core.criterion.FilterConditionListType;

/**
 * Entity filter object.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class EntityFilter extends AbstractPanelFormBinding {

    public static final int SHOW_CLEAR_BUTTON = 0x00000001;
    public static final int SHOW_APPLY_BUTTON = 0x00000002;

    public static final int ENABLE_ALL = SHOW_CLEAR_BUTTON | SHOW_APPLY_BUTTON;

    private final int mode;

    private final EntityDef ownerEntityDef;

    private Filter filter;

    private String category;

    private Long ownerInstId;

    private String paramList;

    private FilterConditionListType listType;

    public EntityFilter(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy, String tabName,
            EntityDef ownerEntityDef, int mode) {
        super(ctx, sweepingCommitPolicy, tabName);
        this.ownerEntityDef = ownerEntityDef;
        this.mode = mode;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setOwnerInstId(Long ownerInstId) {
        this.ownerInstId = ownerInstId;
    }

    public void setListType(FilterConditionListType listType) {
        this.listType = listType;
    }

    public void setParamList(String paramList) {
        this.paramList = paramList;
    }

    public void load(EntityDef entityDef) throws UnifyException {
        FilterDef filterDef = getAppletCtx().getAu().retrieveFilterDef(category, ownerEntityDef.getLongName(),
                ownerInstId);
        filter = new Filter(ownerInstId, paramList, entityDef, filterDef, listType,
                Editable.fromBoolean(isApplyButtonVisible()));
    }

    public void save() throws UnifyException {
        if (filter != null) {
            getAppletCtx().getAu().saveFilterDef(getSweepingCommitPolicy(), category, ownerEntityDef.getLongName(),
                    ownerInstId, filter.getFilterDef());
        }
    }

    public void clear() throws UnifyException {
        if (filter != null) {
            filter.clear();
        }
    }

    public boolean isClearButtonVisible() {
        return getAppletCtx().isContextEditable() && (mode & SHOW_CLEAR_BUTTON) > 0;
    }

    public boolean isApplyButtonVisible() {
        return getAppletCtx().isContextEditable() && (mode & SHOW_APPLY_BUTTON) > 0;
    }
}

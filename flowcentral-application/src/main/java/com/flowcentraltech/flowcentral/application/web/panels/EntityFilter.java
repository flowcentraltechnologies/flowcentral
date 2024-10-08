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
 * @author FlowCentral Technologies Limited
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
            EntityDef ownerEntityDef, int mode, boolean ignoreConditionalDisabled) {
        super(ctx, sweepingCommitPolicy, tabName, ignoreConditionalDisabled);
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
        try {
            FilterDef filterDef = getAppletCtx().au().retrieveFilterDef(category, ownerEntityDef.getLongName(),
                    ownerInstId, null);
            filter = new Filter(getAppletCtx().au(), ownerInstId, paramList, entityDef, filterDef,
                    "application.sessionparamtypelist", listType,
                    Editable.fromBoolean(isApplyButtonVisible()));
        } catch (RuntimeException e) {
            getAppletCtx().au().saveFilterDef(getSweepingCommitPolicy(), category, ownerEntityDef.getLongName(),
                    ownerInstId, null);
            FilterDef filterDef = null;
            filter = new Filter(getAppletCtx().au(), ownerInstId, paramList, entityDef, filterDef,
                    "application.sessionparamtypelist", listType,
                    Editable.fromBoolean(isApplyButtonVisible()));
        }
    }

    public void save() throws UnifyException {
        if (filter != null) {
            getAppletCtx().au().saveFilterDef(getSweepingCommitPolicy(), category, ownerEntityDef.getLongName(),
                    ownerInstId, filter.getFilterDef(getAppletCtx().au()));
        }
    }

    public void clear() throws UnifyException {
        if (filter != null) {
            filter.clear();
        }
    }

    public boolean isClearButtonVisible() {
        return getAppletCtx().isContextEditable() && isTabEditable() && (mode & SHOW_CLEAR_BUTTON) > 0;
    }

    public boolean isApplyButtonVisible() {
        return getAppletCtx().isContextEditable() && isTabEditable() && (mode & SHOW_APPLY_BUTTON) > 0;
    }
}

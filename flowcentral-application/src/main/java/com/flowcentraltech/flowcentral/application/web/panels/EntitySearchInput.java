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
import com.flowcentraltech.flowcentral.application.data.SearchInputsDef;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.widgets.SearchInputs;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.Editable;

/**
 * Entity search input object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntitySearchInput extends AbstractPanelFormBinding {

    public static final int SHOW_CLEAR_BUTTON = 0x00000001;
    public static final int SHOW_APPLY_BUTTON = 0x00000002;

    public static final int ENABLE_ALL = SHOW_CLEAR_BUTTON | SHOW_APPLY_BUTTON;

    private final int mode;

    private final AppletUtilities au;

    private final EntityDef ownerEntityDef;

    private SearchInputs searchInputs;

    private String category;

    private Long ownerInstId;

    public EntitySearchInput(AppletUtilities au, FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy,
            String tabName, EntityDef ownerEntityDef, int mode, boolean ignoreConditionalDisabled) {
        super(ctx, sweepingCommitPolicy, tabName, ignoreConditionalDisabled);
        this.au = au;
        this.ownerEntityDef = ownerEntityDef;
        this.mode = mode;
    }

    public SearchInputs getSearchInputs() {
        return searchInputs;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setOwnerInstId(Long ownerInstId) {
        this.ownerInstId = ownerInstId;
    }

    public void load(EntityDef entityDef) throws UnifyException {
        SearchInputsDef searchInputsDef = getAppletCtx().au().retrieveSearchInputsDef(category,
                ownerEntityDef.getLongName(), ownerInstId);
        searchInputs = new SearchInputs(au, entityDef, searchInputsDef, Editable.fromBoolean(isApplyButtonVisible()));
    }

    public void save() throws UnifyException {
        if (searchInputs != null) {
            getAppletCtx().au().saveSearchInputsDef(getSweepingCommitPolicy(), category, ownerEntityDef.getLongName(),
                    ownerInstId, searchInputs.getSearchInputsDef());
        }
    }

    public void clear() throws UnifyException {
        if (searchInputs != null) {
            searchInputs.clear();
        }
    }

    public boolean isClearButtonVisible() {
        return getAppletCtx().isContextEditable() && isTabEditable() && (mode & SHOW_CLEAR_BUTTON) > 0;
    }

    public boolean isApplyButtonVisible() {
        return getAppletCtx().isContextEditable() && isTabEditable() && (mode & SHOW_APPLY_BUTTON) > 0;
    }
}

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

import java.util.List;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.widgets.ParamValueEntries;
import com.flowcentraltech.flowcentral.application.web.widgets.ParamValueEntry;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.data.ParamValuesDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ParamConfig;

/**
 * Entity parameter values object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityParamValues extends AbstractPanelFormBinding {

    public static final int SHOW_CLEAR_BUTTON = 0x00000001;
    public static final int SHOW_APPLY_BUTTON = 0x00000002;

    public static final int ENABLE_ALL = SHOW_CLEAR_BUTTON | SHOW_APPLY_BUTTON;

    private final int mode;

    private final EntityDef ownerEntityDef;

    private ParamValueEntries paramValueEntries;

    private String category;

    private Long ownerInstId;

    public EntityParamValues(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy, String tabName,
            EntityDef ownerEntityDef, int mode, boolean ignoreConditionalDisabled) {
        super(ctx, sweepingCommitPolicy, tabName, ignoreConditionalDisabled);
        this.ownerEntityDef = ownerEntityDef;
        this.mode = mode;
    }

    public ParamValueEntries getParamValueEntries() {
        return paramValueEntries;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setOwnerInstId(Long ownerInstId) {
        this.ownerInstId = ownerInstId;
    }

    public void load(List<ParamConfig> paramConfigList) throws UnifyException {
        ParamValuesDef paramValuesDef = getAppletCtx().au().retrieveParamValuesDef(category,
                ownerEntityDef.getLongName(), ownerInstId, paramConfigList);
        paramValueEntries = new ParamValueEntries(paramValuesDef);
    }

    public void save() throws UnifyException {
        if (paramValueEntries != null) {
            getAppletCtx().au().saveParamValuesDef(getSweepingCommitPolicy(), category, ownerEntityDef.getLongName(),
                    ownerInstId, paramValueEntries.getParamValuesDef());
        }
    }

    public void clear() {
        if (paramValueEntries != null) {
            for (ParamValueEntry entry : paramValueEntries.getEntryList()) {
                entry.getParamInput().setValue(null);
            }
        }
    }

    public boolean isClearButtonVisible() {
        return getAppletCtx().isContextEditable() && isTabEditable() && (mode & SHOW_CLEAR_BUTTON) > 0;
    }

    public boolean isApplyButtonVisible() {
        return getAppletCtx().isContextEditable() && isTabEditable() && (mode & SHOW_APPLY_BUTTON) > 0;
    }
}

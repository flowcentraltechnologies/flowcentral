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

import com.flowcentraltech.flowcentral.application.constants.PropertySequenceType;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.PropertySequenceDef;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.widgets.PropertySequence;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.Editable;

/**
 * Entity property sequence object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EntityPropertySequence extends AbstractPanelFormBinding {

    public static final int SHOW_CLEAR_BUTTON = 0x00000001;
    public static final int SHOW_APPLY_BUTTON = 0x00000002;

    public static final int ENABLE_ALL = SHOW_CLEAR_BUTTON | SHOW_APPLY_BUTTON;

    private final int mode;

    private final EntityDef ownerEntityDef;

    private PropertySequence propertySequence;

    private String category;

    private Long ownerInstId;

    public EntityPropertySequence(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy, String tabName,
            EntityDef ownerEntityDef, int mode, boolean ignoreConditionalDisabled) {
        super(ctx, sweepingCommitPolicy, tabName, ignoreConditionalDisabled);
        this.ownerEntityDef = ownerEntityDef;
        this.mode = mode;
    }

    public PropertySequence getPropertySequence() {
        return propertySequence;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setOwnerInstId(Long ownerInstId) {
        this.ownerInstId = ownerInstId;
    }

    public void load(EntityDef entityDef) throws UnifyException {
        PropertySequenceDef propertySequenceDef = getAppletCtx().au().retrieveSequenceDef(category,
                ownerEntityDef.getLongName(), ownerInstId);
        propertySequence = new PropertySequence(PropertySequenceType.fromSequenceCategory(category), entityDef,
                propertySequenceDef, Editable.fromBoolean(isApplyButtonVisible()));
    }

    public void save() throws UnifyException {
        if (propertySequence != null) {
            getAppletCtx().au().saveSequenceDef(getSweepingCommitPolicy(), category, ownerEntityDef.getLongName(),
                    ownerInstId, propertySequence.getSequenceDef());
        }
    }

    public void clear() throws UnifyException {
        if (propertySequence != null) {
            propertySequence.clear();
        }
    }

    public boolean isClearButtonVisible() {
        return getAppletCtx().isContextEditable() && isTabEditable() && (mode & SHOW_CLEAR_BUTTON) > 0;
    }

    public boolean isApplyButtonVisible() {
        return getAppletCtx().isContextEditable() && isTabEditable() && (mode & SHOW_APPLY_BUTTON) > 0;
    }
}

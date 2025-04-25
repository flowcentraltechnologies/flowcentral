/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniForm;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniFormScope;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.database.Query;

/**
 * Entity child object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityChild extends AbstractPanelFormBinding {

    private final FormDef childFormDef;

    private String entitySubTitle;

    private MiniForm childForm;

    private Entity childInst;

    private FormContext mCtx;

    private Restriction mRestriction;

    private int childTabIndex;

    private boolean canCreate;

    private boolean quickEdit;

    private boolean expandedMode;

    public EntityChild(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy, String tabName, FormDef childFormDef,
            boolean quickEdit, boolean ignoreConditionalDisabled) {
        super(ctx, sweepingCommitPolicy, tabName, ignoreConditionalDisabled);
        this.quickEdit = quickEdit;
        this.childFormDef = childFormDef;
    }

    public MiniForm getChildForm() {
        return childForm;
    }

    public boolean isWithChild() {
        return childForm != null;
    }

    public boolean isQuickEdit() {
        return quickEdit;
    }

    public String getEntityTitle() {
        return childFormDef.getEntityDef().getDescription();
    }

    public String getEntitySubTitle() {
        return entitySubTitle;
    }

    public void setEntitySubTitle(String entitySubTitle) {
        this.entitySubTitle = entitySubTitle;
    }

    public Entity getChildInst() {
        return childInst;
    }

    public int getChildTabIndex() {
        return childTabIndex;
    }

    public void setChildTabIndex(int childTabIndex) {
        this.childTabIndex = childTabIndex;
    }

    public boolean isCreateButtonVisible() {
        return canCreate && getAppletCtx().isContextEditable() && isTabEditable();
    }

    public boolean isCanCreate() {
        return canCreate;
    }

    public void setCanCreate(boolean canCreate) {
        this.canCreate = canCreate;
    }

    public boolean isExpandedMode() {
        return expandedMode;
    }

    public void setExpandedMode(boolean expandedMode) {
        this.expandedMode = expandedMode;
    }

    public boolean isEditButtonVisible() {
        return getAppletCtx().isContextEditable() && isTabEditable();
    }

    public boolean isViewButtonVisible() {
        return !getAppletCtx().isContextEditable() || !isTabEditable();
    }

    public FormContext getMCtx() {
        return mCtx;
    }

    public Restriction getMRestriction() {
        return mRestriction;
    }

    public void reload() throws UnifyException {
        if (mCtx != null && mRestriction != null) {
            load(mCtx, mRestriction);
        }
    }

    @SuppressWarnings("unchecked")
    public void load(FormContext ctx, Restriction restriction) throws UnifyException {
        final EntityClassDef entityClassDef = ctx.au().getEntityClassDef(childFormDef.getEntityDef().getLongName());
        mCtx = ctx;
        mRestriction = restriction;
        childInst = ctx.getEnvironment().listLean(
                Query.of((Class<? extends Entity>) entityClassDef.getEntityClass()).addRestriction(restriction));
        if (childInst != null) {
            FormContext _ctx = new FormContext(ctx.getAppletContext(), childFormDef, ctx.getFormEventHandlers(),
                    childInst);
            _ctx.revertTabStates();
            childForm = new MiniForm(MiniFormScope.CHILD_FORM, _ctx, childFormDef.getFormTabDef(0));
        } else {
            childForm = null;
        }
    }
}

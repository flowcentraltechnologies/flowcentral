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

import java.util.List;

import com.flowcentraltech.flowcentral.application.constants.ApplicationPredefinedTableConstants;
import com.flowcentraltech.flowcentral.application.data.PropertyListItem;
import com.flowcentraltech.flowcentral.application.data.PropertyRuleDef;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.widgets.BeanListTable;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Property search object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class PropertySearch extends AbstractPanelFormBinding {

    public static final int SHOW_EDIT_BUTTON = 0x00000001;

    public static final int ENABLE_ALL = SHOW_EDIT_BUTTON;

    private PropertyRuleDef propertyRuleDef;

    private BeanListTable beanListTable;

    private String entitySubTitle;

    private int childTabIndex;

    private int mode;

    public PropertySearch(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy, String tabName,
            PropertyRuleDef propertyRuleDef, int mode, boolean ignoreConditionalDisabled) throws UnifyException {
        super(ctx, sweepingCommitPolicy, tabName, ignoreConditionalDisabled);
        this.beanListTable = new BeanListTable(ctx.au(),
                ctx.au().getTableDef(ApplicationPredefinedTableConstants.PROPERTYITEM_TABLE), null);
        this.propertyRuleDef = propertyRuleDef;
        this.mode = mode;
    }

    public PropertyRuleDef getPropertyRuleDef() {
        return propertyRuleDef;
    }

    public int getMode() {
        return mode;
    }

    public BeanListTable getBeanTable() {
        return beanListTable;
    }

    public String getEntityTitle() {
        return beanListTable.getTableDef().getLabel();
    }

    public String getEntitySubTitle() {
        return entitySubTitle;
    }

    public void setEntitySubTitle(String entitySubTitle) {
        this.entitySubTitle = entitySubTitle;
    }

    public int getChildTabIndex() {
        return childTabIndex;
    }

    public void setChildTabIndex(int childTabIndex) {
        this.childTabIndex = childTabIndex;
    }

    public int getTotalItemCount() {
        return beanListTable.getTotalItemCount();
    }

    public void applyEntityToSearch(Entity inst, String childFkFieldName) throws UnifyException {
        List<PropertyListItem> propertyItemList = beanListTable.au().getPropertyListItems(inst, childFkFieldName,
                propertyRuleDef);
        beanListTable.setSourceObjectKeepSelected(propertyItemList);
    }

    public boolean isEditButtonVisible() {
        return getAppletCtx().isContextEditable() && isTabEditable() && (mode & SHOW_EDIT_BUTTON) > 0
                && !DataUtils.isBlank(beanListTable.getSourceObject());
    }

    public boolean isViewButtonVisible() {
        return (!getAppletCtx().isContextEditable() || !isTabEditable()) && (mode & SHOW_EDIT_BUTTON) > 0
                && !DataUtils.isBlank(beanListTable.getSourceObject());
    }
}

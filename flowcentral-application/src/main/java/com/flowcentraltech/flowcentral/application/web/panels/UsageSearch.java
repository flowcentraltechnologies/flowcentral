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

import java.text.MessageFormat;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.UsageListProvider;
import com.flowcentraltech.flowcentral.application.constants.ApplicationPredefinedTableConstants;
import com.flowcentraltech.flowcentral.application.data.Usage;
import com.flowcentraltech.flowcentral.application.data.UsageType;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.widgets.BeanListTable;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Usage search object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class UsageSearch extends AbstractPanelFormBinding {

    private BeanListTable beanListTable;

    private ValueStoreReader instReader;

    private UsageType searchUsageType;

    private String paginationLabel;

    private String entitySubTitle;

    private String provider;

    private int childTabIndex;

    private int mode;

    public UsageSearch(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy, String tabName, String provider,
            int mode, boolean ignoreConditionalDisabled) throws UnifyException {
        super(ctx, sweepingCommitPolicy, tabName, ignoreConditionalDisabled);
        this.beanListTable = new BeanListTable(ctx.au(),
                ctx.au().getTableDef(ApplicationPredefinedTableConstants.USAGE_TABLE), null);
        this.provider = provider;
        this.mode = mode;
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

    public UsageType getSearchUsageType() {
        return searchUsageType;
    }

    public void setSearchUsageType(UsageType searchUsageType) {
        this.searchUsageType = searchUsageType;
    }

    public String getPaginationLabel() {
        return MessageFormat.format(paginationLabel, beanListTable.getDispEndIndex(),
                beanListTable.getTotalItemCount());
    }

    public void setPaginationLabel(String paginationLabel) {
        this.paginationLabel = paginationLabel;
    }

    public int getDisplayStart() {
        if (beanListTable.getTotalItemCount() == 0) {
            return 0;
        }

        return beanListTable.getDispStartIndex() + 1;
    }

    public void setDisplayStart(int dispStartIndex) {

    }

    public int getTotalItemCount() {
        return beanListTable.getTotalItemCount();
    }

    public int getChildTabIndex() {
        return childTabIndex;
    }

    public void setChildTabIndex(int childTabIndex) {
        this.childTabIndex = childTabIndex;
    }

    public void applyEntityToSearch(Entity inst) throws UnifyException {
        instReader = new BeanValueStore(inst).getReader();
        applyEntityToSearch();
    }

    public void applyEntityToSearch() throws UnifyException {
        List<Usage> usageList = beanListTable.au().getComponent(UsageListProvider.class, provider)
                .findUsages(instReader, searchUsageType);
        beanListTable.setSourceObjectKeepSelected(usageList);
    }
}

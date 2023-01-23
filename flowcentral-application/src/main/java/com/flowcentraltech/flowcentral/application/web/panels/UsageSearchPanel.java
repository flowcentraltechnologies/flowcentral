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

import com.flowcentraltech.flowcentral.application.web.widgets.BeanTable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.AbstractPanel;

/**
 * Usage search panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-usagesearchpanel")
@UplBinding("web/application/upl/usagesearchpanel.upl")
public class UsageSearchPanel extends AbstractPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();
        final BeanTable beanTable = getUsageSearch().getBeanTable();
        setDisabled("fastBackBtn", beanTable.isAtFirstPage());
        setDisabled("backBtn", beanTable.isAtFirstPage());
        setDisabled("forwardBtn", beanTable.isAtLastPage());
        setDisabled("fastForwardBtn", beanTable.isAtLastPage());
    }

    @Action
    public void search() throws UnifyException {
         getUsageSearch().applyEntityToSearch();
    }

    @Action
    public void fastBack() throws UnifyException {
        getUsageSearch().getBeanTable().firstPage();
    }

    @Action
    public void back() throws UnifyException {
        getUsageSearch().getBeanTable().prevPage();
    }

    @Action
    public void forward() throws UnifyException {
        getUsageSearch().getBeanTable().nextPage();
    }

    @Action
    public void fastForward() throws UnifyException {
        getUsageSearch().getBeanTable().lastPage();
    }

    private UsageSearch getUsageSearch() throws UnifyException {
        return getValue(UsageSearch.class);
    }
}
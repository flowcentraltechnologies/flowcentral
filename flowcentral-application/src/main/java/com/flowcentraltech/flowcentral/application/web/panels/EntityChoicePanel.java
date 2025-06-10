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

import com.flowcentraltech.flowcentral.application.web.widgets.EntityTable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.PageRequestContextUtil;

/**
 * Entity choice panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-entitychoicepanel")
@UplBinding("web/application/upl/entitychoicepanel.upl")
public class EntityChoicePanel extends AbstractApplicationPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        EntityChoice entityChoice = getEntityChoice();
        if (entityChoice != null) {
            entityChoice.ensureTableStruct();
            if (isReloadOnSwitch()) {
                entityChoice.applyFilterToSearch();
            }

            final EntityTable entityTable = entityChoice.getEntityTable();
            setDisabled("fastBackBtn", entityTable.isAtFirstPage());
            setDisabled("backBtn", entityTable.isAtFirstPage());
            setDisabled("forwardBtn", entityTable.isAtLastPage());
            setDisabled("fastForwardBtn", entityTable.isAtLastPage());
        }
    }

    @Action
    public void fastBack() throws UnifyException {
        getEntityChoice().getEntityTable().firstPage();
    }

    @Action
    public void back() throws UnifyException {
        getEntityChoice().getEntityTable().prevPage();
    }

    @Action
    public void forward() throws UnifyException {
        getEntityChoice().getEntityTable().nextPage();
    }

    @Action
    public void fastForward() throws UnifyException {
        getEntityChoice().getEntityTable().lastPage();
    }

    @Action
    public void refresh() throws UnifyException {
        EntityChoice entityChoice = getEntityChoice();
        if (entityChoice != null) {
            entityChoice.applyFilterToSearch();
            PageRequestContextUtil rcUtil = getRequestContextUtil();
            rcUtil.setContentScrollReset();
        }
    }

    private EntityChoice getEntityChoice() throws UnifyException {
        return getValue(EntityChoice.class);
    }

}

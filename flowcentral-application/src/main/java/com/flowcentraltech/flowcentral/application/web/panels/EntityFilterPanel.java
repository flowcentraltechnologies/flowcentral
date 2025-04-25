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

import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.web.panels.AbstractFlowCentralStandalonePanel;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;

/**
 * Entity filter panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-entityfilterpanel")
@UplBinding("web/application/upl/entityfilterpanel.upl")
public class EntityFilterPanel extends AbstractFlowCentralStandalonePanel {

    @Configurable
    private ApplicationPrivilegeManager applicationPrivilegeManager;

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        EntityFilter entityFilter = getEntityFilter();
        setVisible("clearBtn", entityFilter.isClearButtonVisible());
        setVisible("applyBtn", entityFilter.isApplyButtonVisible());
    }

    @Action
    public void clear() throws UnifyException {
        getEntityFilter().clear();
    }

    @Action
    public void apply() throws UnifyException {
        getEntityFilter().save();
    }

    private EntityFilter getEntityFilter() throws UnifyException {
        return getValue(EntityFilter.class);
    }
}

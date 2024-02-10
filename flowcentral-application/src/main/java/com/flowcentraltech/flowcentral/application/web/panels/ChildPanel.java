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

import com.flowcentraltech.flowcentral.common.web.panels.AbstractFlowCentralStandalonePanel;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;

/**
 * Child panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-childpanel")
@UplBinding("web/application/upl/childpanel.upl")
public class ChildPanel extends AbstractFlowCentralStandalonePanel {

    @Override
    public void onPageConstruct() throws UnifyException {
        super.onPageConstruct();
        setWidgetEditable("childFormPanel", false);
    }

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        EntityChild entityChild = getEntityChild();
        entityChild.reload();

        boolean editMode = entityChild.isWithChild();
        setWidgetVisible("createBtn", !editMode && entityChild.isCreateButtonVisible());
        setWidgetVisible("editBtn", editMode && entityChild.isEditButtonVisible());
        setWidgetVisible("quickEditBtn", editMode && entityChild.isEditButtonVisible() && entityChild.isQuickEdit());
        setWidgetVisible("viewBtn", editMode && entityChild.isViewButtonVisible());
        setWidgetVisible("childFormPanel", editMode);
    }

    private EntityChild getEntityChild() throws UnifyException {
        return getValue(EntityChild.class);
    }
}

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

package com.flowcentraltech.flowcentral.studio.web.panels.applet;

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.entities.AppTable;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.studio.business.StudioModuleService;
import com.flowcentraltech.flowcentral.studio.web.panels.TableEditorPage;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Studio application table applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class StudioAppTableApplet extends StudioAppComponentApplet {

    private TableEditorPage tableEditorPage;

    public StudioAppTableApplet(Page page, StudioModuleService sms, AppletUtilities au, List<String> pathVariables,
            String applicationName, AppletWidgetReferences appletWidgetReferences,
            EntityFormEventHandlers formEventHandlers) throws UnifyException {
        super(page, sms, au, pathVariables, applicationName, appletWidgetReferences, formEventHandlers);
    }

    public TableEditorPage getTableEditorPage() {
        return tableEditorPage;
    }

    public void designChildItem(int childTabIndex) throws UnifyException {
        AppTable appTable = (AppTable) form.getFormBean();
        Object id = appTable.getId();
        String subTitle = appTable.getDescription();
        setTabDefAndSaveCurrentForm(childTabIndex);
        tableEditorPage = constructNewTableEditorPage(appTable.getEntity(), id, subTitle);
        tableEditorPage.newEditor();
        viewMode = ViewMode.CUSTOM_PAGE;
    }

    private TableEditorPage constructNewTableEditorPage(String entityName, Object id, String subTitle)
            throws UnifyException {
        BreadCrumbs breadCrumbs = form.getBreadCrumbs().advance();
        EntityDef entityDef = getEntityDef(entityName);
        breadCrumbs.setLastCrumbTitle(au().resolveSessionMessage("$m{tableeditor.tabledesigner}"));
        breadCrumbs.setLastCrumbSubTitle(subTitle);
        return new TableEditorPage(au(), entityDef, id, breadCrumbs);
    }

}

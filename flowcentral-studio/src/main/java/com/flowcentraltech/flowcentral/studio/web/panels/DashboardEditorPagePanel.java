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
package com.flowcentraltech.flowcentral.studio.web.panels;

import com.flowcentraltech.flowcentral.studio.web.widgets.DashboardEditor;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;

/**
 * Dashboard editor page panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-dashboardeditorpagepanel")
@UplBinding("web/studio/upl/dashboardeditorpagepanel.upl")
public class DashboardEditorPagePanel extends AbstractStudioEditorPagePanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        final boolean readOnly = isAppletContextReadOnly();
        DashboardEditor dashboardEditor = getDashboardEditorPage().getDashboardEditor();
        dashboardEditor.setReadOnly(readOnly);
        if (!dashboardEditor.isInitialized()) {
            dashboardEditor.init(getWidgetByShortName("dashboardEditorBodyPanel").getLongName(),
                    getWidgetByShortName("editSectionPanel").getLongName(),
                    getWidgetByShortName("editTilePanel").getLongName());
        }

        boolean isEditable = !readOnly;
        setWidgetVisible("saveBtn", isEditable);
        setWidgetEditable("editSectionPanel", isEditable);
        setWidgetEditable("editTilePanel", isEditable);
    }

    @Action
    public void saveDesign() throws UnifyException {
        DashboardEditorPage dashboardEditorPage = getDashboardEditorPage();
        dashboardEditorPage.commitDesign();
        hintUser("$m{studiodashboardapplet.dashboardeditor.success.hint}", dashboardEditorPage.getSubTitle());
    }

    private DashboardEditorPage getDashboardEditorPage() throws UnifyException {
        return getValue(DashboardEditorPage.class);
    }
}

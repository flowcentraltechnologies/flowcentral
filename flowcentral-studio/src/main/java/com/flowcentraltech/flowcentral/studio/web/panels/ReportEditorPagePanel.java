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
package com.flowcentraltech.flowcentral.studio.web.panels;

import com.flowcentraltech.flowcentral.studio.web.widgets.ReportEditor;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;

/**
 * Report editor page panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-reporteditorpagepanel")
@UplBinding("web/studio/upl/reporteditorpagepanel.upl")
public class ReportEditorPagePanel extends AbstractStudioEditorPagePanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        final boolean readOnly = isAppletContextReadOnly();
        ReportEditor reportEditor = getReportEditorPage().getReportEditor();
        reportEditor.setReadOnly(readOnly);
        if (reportEditor.getEditColumnId() == null) {
            reportEditor.setEditColumnId(getWidgetByShortName("editColumnPanel").getId());
        }

        setWidgetVisible("saveBtn", !readOnly);
        setWidgetEditable("editColumnPanel", !readOnly);
    }

    @Action
    public void preview() throws UnifyException {
        getReportEditorPage().getReportPreview().reload();
    }

    @Action
    public void saveDesign() throws UnifyException {
        ReportEditorPage reportEditorPage = getReportEditorPage();
        reportEditorPage.commitDesign();
        hintUser("$m{studioreportconfigurationapplet.reporteditor.success.hint}", reportEditorPage.getSubTitle());
    }

    private ReportEditorPage getReportEditorPage() throws UnifyException {
        return getValue(ReportEditorPage.class);
    }
}

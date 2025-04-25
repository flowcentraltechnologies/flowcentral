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

import com.flowcentraltech.flowcentral.studio.web.widgets.FormEditor;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;

/**
 * Form editor page panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-formeditorpagepanel")
@UplBinding("web/studio/upl/formeditorpagepanel.upl")
public class FormEditorPagePanel extends AbstractStudioEditorPagePanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        final boolean readOnly = isAppletContextReadOnly();
        FormEditor formEditor = getFormEditorPage().getFormEditor();
        formEditor.setReadOnly(readOnly);
        if (!formEditor.isInitialized()) {
            formEditor.init(getWidgetByShortName("formEditorBodyPanel").getLongName(),
                    getWidgetByShortName("editTabPanel").getLongName(),
                    getWidgetByShortName("editSectionPanel").getLongName(),
                    getWidgetByShortName("editFieldPanel").getLongName());
        }

        boolean isEditable = !readOnly;
        setWidgetVisible("saveBtn", isEditable);
        setWidgetEditable("editTabPanel", isEditable);
        setWidgetEditable("editSectionPanel", isEditable);
        setWidgetEditable("editFieldPanel", isEditable);
    }

    @Action
    public void saveDesign() throws UnifyException {
        FormEditorPage formEditorPage = getFormEditorPage();
        formEditorPage.commitDesign();
        hintUser("$m{studioappformapplet.formeditor.success.hint}", formEditorPage.getSubTitle());
    }

    private FormEditorPage getFormEditorPage() throws UnifyException {
        return getValue(FormEditorPage.class);
    }
}

/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;

/**
 * Applet editor page panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-appleteditorpagepanel")
@UplBinding("web/studio/upl/appleteditorpagepanel.upl")
public class AppletEditorPagePanel extends AbstractStudioEditorPagePanel {

    @Action
    public void preview() throws UnifyException {
        // TODO ?
        getAppletEditorPage().getTablePreview().reload();
        getAppletEditorPage().getFormPreview().reload();
    }

    @Action
    public void saveDesign() throws UnifyException {
        AppletEditorPage appletEditorPage = getAppletEditorPage();
        appletEditorPage.commitDesign();
        hintUser("$m{studioappappletapplet.appleteditor.success.hint}", appletEditorPage.getSubTitle());
    }

    @Override
    protected boolean isAppletContextReadOnly() throws UnifyException {
        // TODO
        return false;
    }

    private AppletEditorPage getAppletEditorPage() throws UnifyException {
        return getValue(AppletEditorPage.class);
    }
}

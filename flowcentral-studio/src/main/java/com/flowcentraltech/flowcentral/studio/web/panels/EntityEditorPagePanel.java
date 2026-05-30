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

import com.flowcentraltech.flowcentral.studio.web.widgets.EntityEditor;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;

/**
 * Entity editor page panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-entityeditorpagepanel")
@UplBinding("web/studio/upl/entityeditorpagepanel.upl")
public class EntityEditorPagePanel extends AbstractStudioEditorPagePanel {

    @Override
    public void switchState() throws UnifyException {
        EntityEditorPage entityEditorPage = getEntityEditorPage();
        if (entityEditorPage != null) {
            final boolean readOnly = isAppletContextReadOnly();
            EntityEditor entityEditor = entityEditorPage.getEntityEditor();
            entityEditor.setReadOnly(readOnly);

            final boolean isEditable = !readOnly;
            setWidgetVisible("saveBtn", isEditable);
        }
    }
    
    @Action
    public void refreshDesign() throws UnifyException {
        EntityEditorPage entityEditorPage = getEntityEditorPage();
        entityEditorPage.newEditor();
    }
    
    @Action
    public void saveDesign() throws UnifyException {
        EntityEditorPage entityEditorPage = getEntityEditorPage();
        entityEditorPage.commitDesign();
        hintUser("$m{studioappentityapplet.entityeditor.success.hint}", entityEditorPage.getSubTitle());
    }

    @Override
    protected boolean isAppletContextReadOnly() throws UnifyException {
        // TODO
        return false;
    }

    private EntityEditorPage getEntityEditorPage() throws UnifyException {
        return getValue(EntityEditorPage.class);
    }
}

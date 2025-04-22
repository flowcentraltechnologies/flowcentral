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

package com.flowcentraltech.flowcentral.studio.web.panels;

import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.application.util.ApplicationCollaborationUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.common.business.CollaborationProvider;
import com.flowcentraltech.flowcentral.common.constants.CollaborationType;
import com.flowcentraltech.flowcentral.common.web.panels.AbstractFlowCentralPanel;
import com.flowcentraltech.flowcentral.studio.web.panels.applet.StudioAppComponentApplet;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;

/**
 * Convenient abstract base class for studio editor page panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@UplBinding("web/studio/upl/studioeditorpagepanel.upl")
public abstract class AbstractStudioEditorPagePanel extends AbstractFlowCentralPanel {

    @Configurable
    private CollaborationProvider collaborationProvider;

    @Override
    public void switchState() throws UnifyException {
        super.switchState();
        final StudioAppComponentApplet applet = (StudioAppComponentApplet) getValueStore().getValueObject();
        final boolean isCollaboration = applet.isCollaboration() && collaborationProvider != null;
        final boolean isEditable = !applet.appletCtx().isReadOnly();
        if (isCollaboration) {
            AbstractStudioEditorPage editorPage = getValue(AbstractStudioEditorPage.class);
            boolean isFrozen = checkFrozen((Entity) applet.getForm().getFormBean());
            if (isFrozen) {
                editorPage.setDisplayItemCounterClass("fc-dispcounterfrozen");
                editorPage.setDisplayItemCounter(
                        resolveSessionMessage("$m{entityformapplet.form.collaboration.frozen}"));
                applet.appletCtx().setReadOnly(true);
            } else if (isEditable) {
                editorPage.setDisplayItemCounterClass("fc-dispcountergreen");
                editorPage.setDisplayItemCounter(
                        resolveSessionMessage("$m{entityformapplet.form.collaboration.editable}"));
            } else {
                editorPage.setDisplayItemCounterClass(null);
                editorPage.setDisplayItemCounter(
                        resolveSessionMessage("$m{entityformapplet.form.collaboration.viewonly}"));
            }
        }

        setVisible("displayCounterLabel", isCollaboration);
    }

    protected boolean isAppletContextReadOnly() throws UnifyException {
        return ((StudioAppComponentApplet) getValueStore().getValueObject()).appletCtx().isReadOnly();
    }
    
    private boolean checkFrozen(Entity inst) throws UnifyException {
        BaseApplicationEntity _appInst = (BaseApplicationEntity) inst;
        CollaborationType type = ApplicationCollaborationUtils.getCollaborationType(_appInst.getClass());
        if (type != null) {
            String resourceName = ApplicationNameUtils
                    .getApplicationEntityLongName(_appInst.getApplicationName(), _appInst.getName());
            return collaborationProvider.isFrozen(type, resourceName);
        }
        
        return false;
    }
}

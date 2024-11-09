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
package com.flowcentraltech.flowcentral.studio.web.panels.applet;

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractEntityFormApplet;
import com.flowcentraltech.flowcentral.studio.business.StudioModuleService;
import com.flowcentraltech.flowcentral.studio.constants.StudioAppComponentType;
import com.flowcentraltech.flowcentral.studio.constants.StudioAppletPropertyConstants;
import com.flowcentraltech.flowcentral.studio.constants.StudioSessionAttributeConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Studio application component applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class StudioAppComponentApplet extends AbstractEntityFormApplet {

    private final StudioModuleService sms;

    private final String instTitle;

    private final String typeTitle;

    private final String applicationName;

    public StudioAppComponentApplet(Page page, StudioModuleService sms, AppletUtilities au, List<String> pathVariables,
            String applicationName, AppletWidgetReferences appletWidgetReferences,
            EntityFormEventHandlers formEventHandlers) throws UnifyException {
        super(page, au, pathVariables, appletWidgetReferences, formEventHandlers, true);
        this.sms = sms;
        this.applicationName = applicationName;

        setCurrFormAppletDef(getRootAppletDef());
        StudioAppComponentType type = getCurrFormAppletDef().getPropValue(StudioAppComponentType.class,
                StudioAppletPropertyConstants.ENTITY_TYPE);
        this.instTitle = getCurrFormAppletDef().getDescription();
        this.typeTitle = au.resolveSessionMessage(type.caption());
        Long instId = getCurrFormAppletDef().getPropValue(Long.class, StudioAppletPropertyConstants.ENTITY_INST_ID);
        if (instId == null || instId.longValue() == 0L) {
            constructNewForm();
        } else {
            Entity _inst = au.environment().listLean(type.componentType(), instId);
            form = constructForm(_inst, FormMode.ENTITY_MAINTAIN, null, false);
            viewMode = ViewMode.MAINTAIN_PRIMARY_FORM_NO_SCROLL;
        }
    }

    @Override
    public AppletDef getRootAppletDef() throws UnifyException {
        return sms != null ? sms.getAppletDef(getAppletName()) : null;
    }

    public String getInstTitle() {
        return instTitle;
    }

    public String getTypeTitle() {
        return typeTitle;
    }

    public void ensureClearOnNew() throws UnifyException {
        Long instId = getCurrFormAppletDef().getPropValue(Long.class, StudioAppletPropertyConstants.ENTITY_INST_ID);
        if (instId == null || instId.longValue() == 0L) {
            constructNewForm();
        }
    }
    
    @Override
    protected AppletDef getAlternateFormAppletDef() throws UnifyException {
        return null;
    }

    @Override
    protected boolean restoreForm() {
        super.restoreForm();
        return true; // Do this so applet does not revert to search mode at terminal form
    }

    protected StudioModuleService getSms() {
        return sms;
    }

    protected String getApplicationName() {
        return applicationName;
    }
    
    private void constructNewForm() throws UnifyException {
        form = constructNewForm(FormMode.ENTITY_CREATE, null, false);
        ((BaseApplicationEntity) form.getCtx().getInst()).setApplicationId(
                au().getSessionAttribute(Long.class, StudioSessionAttributeConstants.CURRENT_APPLICATION_ID));
        viewMode = ViewMode.NEW_PRIMARY_FORM;
    }
}

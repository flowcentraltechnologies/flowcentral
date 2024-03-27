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
package com.flowcentraltech.flowcentral.application.web.panels.applet;

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Headless tabs form applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class HeadlessTabsFormApplet extends AbstractEntityFormApplet {

    public HeadlessTabsFormApplet(Page page, AppletUtilities au, List<String> pathVariables,
            AppletWidgetReferences appletWidgetReferences, EntityFormEventHandlers formEventHandlers)
            throws UnifyException {
        super(page, au, pathVariables, appletWidgetReferences, formEventHandlers);
        setCurrFormAppletDef(getRootAppletDef());
        headlessForm = au.constructHeadlessTabsForm(getCtx(), this, getRootAppletDef().getDescription(),
                getCurrFormAppletDef());
        setAltSubCaption(getRootAppletDef().getDescription());
        navBackToHeadless();
    }

    @Override
    protected AppletDef getAlternateFormAppletDef() throws UnifyException {
        return null;
    }
}

/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.AppletFilterDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySearch;
import com.tcdng.unify.core.UnifyException;

/**
 * Manage entity list applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ManageEntityListApplet extends AbstractEntityFormApplet {

    public ManageEntityListApplet(AppletUtilities au, String pathVariable,
            AppletWidgetReferences appletWidgetReferences, EntityFormEventHandlers formEventHandlers)
            throws UnifyException {
        super(au, pathVariable, appletWidgetReferences, formEventHandlers);
        setCurrFormAppletDef(getRootAppletDef());
        entitySearch = au.constructEntitySearch(new FormContext(getCtx()), this, null,
                getRootAppletDef().getDescription(), getCurrFormAppletDef(), null, EntitySearch.ENABLE_ALL, false,
                false);
        if (isRootAppletPropWithValue(AppletPropertyConstants.BASE_RESTRICTION)) {
            AppletFilterDef appletFilterDef = getRootAppletFilterDef(AppletPropertyConstants.BASE_RESTRICTION);
            entitySearch.setBaseFilter(new FilterDef(appletFilterDef.getFilterDef()), au.specialParamProvider());
        }

        navBackToSearch();
    }

    @Override
    public boolean navBackToSearch() throws UnifyException {
        setAltSubCaption(entitySearch.getEntityDef().getDescription());
        return super.navBackToSearch();
    }

    @Override
    protected AppletDef getAlternateFormAppletDef() throws UnifyException {
        return null;
    }
}

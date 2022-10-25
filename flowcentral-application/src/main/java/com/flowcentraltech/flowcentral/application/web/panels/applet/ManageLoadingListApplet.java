/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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
import com.flowcentraltech.flowcentral.application.data.EntityItem;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.flowcentraltech.flowcentral.application.web.panels.HeaderWithTabsForm;
import com.flowcentraltech.flowcentral.application.web.panels.LoadingSearch;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;

/**
 * Manage loading list applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ManageLoadingListApplet extends AbstractEntityFormApplet {

    private LoadingSearch loadingSearch;

    public ManageLoadingListApplet(AppletUtilities au, String pathVariable,
            AppletWidgetReferences appletWidgetReferences, EntityFormEventHandlers formEventHandlers)
            throws UnifyException {
        super(au, pathVariable, appletWidgetReferences, formEventHandlers);
        loadingSearch = au.constructLoadingSearch(ctx, LoadingSearch.ENABLE_ALL);
        if (isRootAppletPropWithValue(AppletPropertyConstants.BASE_RESTRICTION)) {
            AppletFilterDef appletFilterDef = getRootAppletFilterDef(AppletPropertyConstants.BASE_RESTRICTION);
            loadingSearch.setBaseFilter(new FilterDef(appletFilterDef.getFilterDef()), au.getSpecialParamProvider());
        }

        loadingSearch.applySearchEntriesToSearch();
        loadingSearch.getLoadingTable().setCrudActionHandlers(formEventHandlers.getMaintainActHandlers());

        navBackToSearch();
    }

    public LoadingSearch getLoadingSearch() {
        return loadingSearch;
    }

    @Override
    public void maintainInst(int mIndex) throws UnifyException {
        this.mIndex = mIndex;
        EntityItem item = loadingSearch.getSourceItem(mIndex);
        if (item.isEdit()) {
            Entity _inst = item.getEntity();
            _inst = reloadEntity(_inst, true);
            if (form == null) {
                form = constructForm(_inst, FormMode.MAINTAIN, null, false);
            } else {
                updateForm(HeaderWithTabsForm.UpdateType.MAINTAIN_INST, form, _inst);
            }

            viewMode = ViewMode.MAINTAIN_FORM_SCROLL;
        } else if (item.isUserAction()) {
            // TODO
        }
        
        return;
    }

    @Override
    protected AppletDef getAlternateFormAppletDef() throws UnifyException {
        // TODO Auto-generated method stub
        return null;
    }

}

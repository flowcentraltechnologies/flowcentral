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
import com.flowcentraltech.flowcentral.application.data.AppletFilterDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.web.panels.LoadingSearch;
import com.tcdng.unify.core.UnifyException;

/**
 * Manage loading list applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ManageLoadingListApplet extends AbstractApplet {

    private LoadingSearch loadingSearch;

    public ManageLoadingListApplet(AppletUtilities au, String pathVariable)
            throws UnifyException {
        super(au, pathVariable);
        loadingSearch = au.constructLoadingSearch(ctx, LoadingSearch.ENABLE_ALL);
        if (isRootAppletPropWithValue(AppletPropertyConstants.BASE_RESTRICTION)) {
            AppletFilterDef appletFilterDef = getRootAppletFilterDef(AppletPropertyConstants.BASE_RESTRICTION);
            loadingSearch.setBaseFilter(new FilterDef(appletFilterDef.getFilterDef()),
                    au.getSpecialParamProvider());
        }
    }

    public LoadingSearch getLoadingSearch() {
        return loadingSearch;
    }

}

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
package com.flowcentraltech.flowcentral.application.web.controllers;

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractLoadingApplet;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Abstract loading applet controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@ResultMappings({
    @ResultMapping(name ="notfound",
            response = { "!forwardresponse path:$s{/application/securedlinkaccess}" }) })
public abstract class AbstractLoadingAppletController<T extends AbstractLoadingApplet, U extends AbstractLoadingAppletPageBean<T>>
        extends AbstractEntityFormAppletController<T, U> { 

    public AbstractLoadingAppletController(Class<U> pageBeanClass) {
        super(pageBeanClass, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();

        AbstractLoadingAppletPageBean<T> pageBean = getPageBean();
        AppletWidgetReferences appletWidgetReferences = getAppletWidgetReferences();
        EntityFormEventHandlers formEventHandlers = getEntityFormEventHandlers();
        T applet = createAbstractLoadingApplet(getPage(), au(), getPathVariables(),
                appletWidgetReferences, formEventHandlers);
        pageBean.setApplet(applet);
        if (pageBean.getAltCaption() == null) {
            setPageTitle(applet);
        }
        
        if (!applet.isWithWorkItemId()) {
            setResultMapping("notfound");
        }
    }
    
    protected abstract T createAbstractLoadingApplet(Page page, AppletUtilities au, List<String> pathVariables,
            AppletWidgetReferences appletWidgetReferences, EntityFormEventHandlers formEventHandlers) throws UnifyException;
}

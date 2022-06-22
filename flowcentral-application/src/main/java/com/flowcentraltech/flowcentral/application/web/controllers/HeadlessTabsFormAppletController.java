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
package com.flowcentraltech.flowcentral.application.web.controllers;

import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.web.panels.applet.HeadlessTabsFormApplet;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;

/**
 * Headless tabs form applet controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("/headlesstabsformapplet")
@UplBinding("web/application/upl/headlesstabsformappletpage.upl")
public class HeadlessTabsFormAppletController
        extends AbstractEntityFormAppletController<HeadlessTabsFormApplet, HeadlessTabsFormAppletPageBean> {

    public HeadlessTabsFormAppletController() {
        super(HeadlessTabsFormAppletPageBean.class, Secured.TRUE, ReadOnly.FALSE, ResetOnWrite.FALSE);
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();

        HeadlessTabsFormAppletPageBean pageBean = getPageBean();
        if (pageBean.getApplet() == null) {
            AppletWidgetReferences appletWidgetReferences = getAppletWidgetReferences();
            EntityFormEventHandlers formEventHandlers = getEntityFormEventHandlers();
            HeadlessTabsFormApplet applet = new HeadlessTabsFormApplet(getAu(), getPathVariable(),
                    appletWidgetReferences, formEventHandlers);
            pageBean.setApplet(applet);
            if (pageBean.getAltCaption() == null) {
                setPageTitle(applet);
            }
        }
    }

}

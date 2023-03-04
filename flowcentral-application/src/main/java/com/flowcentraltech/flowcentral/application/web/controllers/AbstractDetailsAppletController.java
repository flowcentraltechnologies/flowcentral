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
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractDetailsApplet;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.Widget;

/**
 * Abstract base class for details applet controllers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractDetailsAppletController<T extends AbstractDetailsApplet, U extends AbstractDetailsAppletPageBean<T>>
        extends AbstractAppletController<U> {

    public AbstractDetailsAppletController(Class<U> pageBeanClass, Secured secured, ReadOnly readOnly,
            ResetOnWrite resetOnWrite) {
        super(pageBeanClass, secured, readOnly, resetOnWrite);
    }

    @Action
    public String crudToChildItem() throws UnifyException {

        return noResult();
    }

    protected void showChildCrud() throws UnifyException {
        final AbstractDetailsAppletPageBean<T> pageBean = getPageBean();
        if (pageBean.isSupportChild()) {
            pageBean.getApplet().createNewChildCrud(pageBean.getParentId());
            if (pageBean.isChildAppletInlineMode()) {
                setPageWidgetVisible("entityCrudInlinePanel", true);
            } else {
                // TODO
            }
        } else {
            pageBean.getApplet().clearChildEntityCrud();
            setPageWidgetVisible("entityCrudInlinePanel", false);
        }
    }

    protected EntityFormEventHandlers getEntityFormEventHandlers() throws UnifyException {
        EventHandler[] formSwitchOnChangeHandlers = {};
        EventHandler[] assnSwitchOnChangeHandlers = {};
        EventHandler[] entrySwitchOnChangeHandlers = {};
        EventHandler[] crudActionHandlers = getPageWidgetByShortName(Widget.class, "crudActionHolder")
                .getUplAttribute(EventHandler[].class, "eventHandler");
        EventHandler[] crudSwitchOnChangeHandlers = getPageWidgetByShortName(Widget.class, "switchOnChangeHolder")
                .getUplAttribute(EventHandler[].class, "eventHandler");
        EventHandler[] saveAsSwitchOnChangeHandlers = {};
        EventHandler[] maintainActHandlers = {};
        return new EntityFormEventHandlers(formSwitchOnChangeHandlers, assnSwitchOnChangeHandlers,
                entrySwitchOnChangeHandlers, crudActionHandlers, crudSwitchOnChangeHandlers,
                saveAsSwitchOnChangeHandlers, maintainActHandlers);
    }
}

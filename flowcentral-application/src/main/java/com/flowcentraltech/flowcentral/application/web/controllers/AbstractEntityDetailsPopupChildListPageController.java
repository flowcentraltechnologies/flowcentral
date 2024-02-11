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

import com.flowcentraltech.flowcentral.application.constants.ApplicationResultMappingConstants;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.web.panels.applet.ManageEntityDetailsApplet;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.data.Popup;

/**
 * Convenient abstract base class for entity detail popup child list
 * controllers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@UplBinding("web/application/upl/entitydetailspopupchildlistpage.upl")
@ResultMappings({
        @ResultMapping(name = "refreshChildCrud",
                response = { "!refreshpanelresponse panels:$l{entityCrudPopup.entityCrudPanel}",
                        "!commonreportresponse" }),
        @ResultMapping(name = ApplicationResultMappingConstants.SHOW_CHILD_CRUD,
                response = { "!showpopupresponse popup:$s{entityCrudPopup}" }) })
public abstract class AbstractEntityDetailsPopupChildListPageController<T extends AbstractEntityDetailsPageBean>
        extends AbstractEntityDetailsPageController<T> {

    private final String childAppletName;

    private final String childBaseFieldName;

    public AbstractEntityDetailsPopupChildListPageController(Class<T> pageBeanClass, Secured secured, ReadOnly readOnly,
            ResetOnWrite resetOnWrite, String detailsAppletName, String childAppletName, String childBaseFieldName) {
        super(pageBeanClass, secured, readOnly, resetOnWrite, detailsAppletName);
        this.childAppletName = childAppletName;
        this.childBaseFieldName = childBaseFieldName;
    }

    @Action
    public final String crudSelectItem() throws UnifyException {
        int index = getRequestTarget(int.class);
        AbstractEntityDetailsPageBean pageBean = getPageBean();
        pageBean.getChildEntityCrud().enterMaintain(index);
        return "refreshChildCrud";
    }

    @Action
    public final String crudSwitchOnChange() throws UnifyException {
        AbstractEntityDetailsPageBean pageBean = getPageBean();
        au().onMiniformSwitchOnChange(pageBean.getChildEntityCrud().getForm());
        return "refreshChildCrud";
    }

    protected final ManageEntityDetailsApplet createManageEntityDetailsApplet() throws UnifyException {
        return new ManageEntityDetailsApplet(au(), getDetailsAppletName(), childAppletName, childBaseFieldName,
                getEntityFormEventHandlers());
    }

    protected String showChildCrud(Long parentId) throws UnifyException {
        if (parentId != null) {
            final AbstractEntityDetailsPageBean pageBean = getPageBean();
            pageBean.getApplet().createNewChildCrud(parentId);
            return showPopup(new Popup(ApplicationResultMappingConstants.SHOW_CHILD_CRUD, pageBean, 800, 500));
        }

        return noResult();
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

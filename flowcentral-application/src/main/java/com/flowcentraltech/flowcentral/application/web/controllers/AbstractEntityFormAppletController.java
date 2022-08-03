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

import com.flowcentraltech.flowcentral.application.business.EntityTreeSelectGenerator;
import com.flowcentraltech.flowcentral.application.constants.AppletRequestAttributeConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationResultMappingConstants;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySelect;
import com.flowcentraltech.flowcentral.application.web.panels.EntityTreeSelect;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractEntityFormApplet;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractEntityFormApplet.ShowPopupInfo;
import com.flowcentraltech.flowcentral.application.web.widgets.BeanTableWidget;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.Widget;

/**
 * Abstract base class for entity form applet controllers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@ResultMappings({ @ResultMapping(name = "refreshapplet",
        response = { "!hidepopupresponse", "!refreshpanelresponse panels:$l{appletPanel}" }) })
public abstract class AbstractEntityFormAppletController<T extends AbstractEntityFormApplet, U extends AbstractEntityFormAppletPageBean<T>>
        extends AbstractAppletController<U> {

    public AbstractEntityFormAppletController(Class<U> pageBeanClass, Secured secured, ReadOnly readOnly,
            ResetOnWrite resetOnWrite) {
        super(pageBeanClass, secured, readOnly, resetOnWrite);
    }

    @Action
    public String newChildItem() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        int childTabIndex = getRequestTarget(int.class);
        applet.newChildItem(childTabIndex);
        getPageRequestContextUtil().setContentScrollReset();
        return "refreshapplet";
    }

    @Action
    public String newChildListItem() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        int childTabIndex = getRequestTarget(int.class);
        ShowPopupInfo showPopupInfo = applet.newChildShowPopup(childTabIndex);
        if (showPopupInfo != null) {
            ValueStore formValueStore = applet.getForm().getCtx().getFormValueStore();
            switch (showPopupInfo.getType()) {
                case SHOW_MULTISELECT: {
                    RefDef refDef = getAu().getRefDef(showPopupInfo.getReference());
                    EntitySelect entitySelect = applet.au().constructEntitySelect(refDef, formValueStore, null, 0);
                    entitySelect.setEnableFilter(false);
                    entitySelect.applyFilterToSearch();
                    String title = resolveSessionMessage("$m{entitymultiselectpanel.select.entity}",
                            entitySelect.getEntityTable().getEntityDef().getLabel());
                    entitySelect.setTitle(title);
                    setSessionAttribute(FlowCentralSessionAttributeConstants.ENTITYSELECT, entitySelect);
                    return ApplicationResultMappingConstants.SHOW_ENTITY_MULTISELECT;
                }
                case SHOW_TREEMULTISELECT: {
                    EntityTreeSelectGenerator generator = getAu().getComponent(EntityTreeSelectGenerator.class,
                            showPopupInfo.getReference());
                    EntityTreeSelect entityTreeSelect = generator.generate(getAu(), formValueStore);
                    entityTreeSelect.setTitle(entityTreeSelect.getEntityTreeTable().getTitle());
                    setSessionAttribute(FlowCentralSessionAttributeConstants.ENTITYTREESELECT, entityTreeSelect);
                    return ApplicationResultMappingConstants.SHOW_ENTITY_TREEMULTISELECT;
                }
                default:
                    break;
            }
        }

        applet.newChildListItem(childTabIndex);
        getPageRequestContextUtil().setContentScrollReset();
        return "refreshapplet";
    }

    @Action
    public String editChildItem() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        int childTabIndex = getRequestTarget(int.class);
        applet.editChildItem(childTabIndex);
        getPageRequestContextUtil().setContentScrollReset();
        return "refreshapplet";
    }

    @Action
    public String assignToChildItem() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        int childTabIndex = getRequestTarget(int.class);
        applet.assignToChildItem(childTabIndex);
        getPageRequestContextUtil().setContentScrollReset();
        return "refreshapplet";
    }

    @Action
    public String entryToChildItem() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        int childTabIndex = getRequestTarget(int.class);
        applet.entryToChildItem(childTabIndex);
        getPageRequestContextUtil().setContentScrollReset();
        return "refreshapplet";
    }

    @Action
    public String crudToChildItem() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        int childTabIndex = getRequestTarget(int.class);
        applet.crudToChildItem(childTabIndex);
        getPageRequestContextUtil().setContentScrollReset();
        return "refreshapplet";
    }

    @Action
    public String newRelatedItem() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        String relatedListName = getRequestTarget(String.class);
        applet.newRelatedListItem(relatedListName);
        getPageRequestContextUtil().setContentScrollReset();
        return "refreshapplet";
    }

    @Action
    public String newHeadlessItem() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        String appletName = getRequestTarget(String.class);
        applet.newHeadlessListItem(appletName);
        getPageRequestContextUtil().setContentScrollReset();
        return "refreshapplet";
    }

    @Action
    public String assignToRelatedItem() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        String relatedListName = getRequestTarget(String.class);
        applet.assignToRelatedListItem(relatedListName);
        getPageRequestContextUtil().setContentScrollReset();
        return "refreshapplet";
    }

    @Action
    public String manageProperties() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        int childTabIndex = getRequestTarget(int.class);
        applet.prepareItemProperties(childTabIndex);
        getPageRequestContextUtil().setContentScrollReset();
        return "refreshapplet";
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        setRequestAttribute(AppletRequestAttributeConstants.RELOAD_ONSWITCH, Boolean.TRUE);
    }

    protected AppletWidgetReferences getAppletWidgetReferences() throws UnifyException {
        BeanTableWidget assignmentEntryTableWidget = getPageWidgetByShortName(BeanTableWidget.class,
                "appletPanel.assignmentPanel.assignmentEntryTbl");
        BeanTableWidget entryEntryTableWidget = getPageWidgetByShortName(BeanTableWidget.class,
                "appletPanel.entryTablePanel.entryTableEntryTbl");
        return new AppletWidgetReferences(assignmentEntryTableWidget, entryEntryTableWidget);
    }

    protected EntityFormEventHandlers getEntityFormEventHandlers() throws UnifyException {
        EventHandler[] formSwitchOnChangeHandlers = getPageWidgetByShortName(Widget.class,
                "appletPanel.formPanel.switchOnChangeHolder").getUplAttribute(EventHandler[].class, "eventHandler");
        EventHandler[] assnSwitchOnChangeHandlers = getPageWidgetByShortName(Widget.class,
                "appletPanel.assignmentPanel.switchOnChangeHolder").getUplAttribute(EventHandler[].class,
                        "eventHandler");
        EventHandler[] entrySwitchOnChangeHandlers = getPageWidgetByShortName(Widget.class,
                "appletPanel.entryTablePanel.switchOnChangeHolder").getUplAttribute(EventHandler[].class,
                        "eventHandler");
        EventHandler[] crudActionHandlers = getPageWidgetByShortName(Widget.class,
                "appletPanel.entityCrudPanel.crudActionHolder").getUplAttribute(EventHandler[].class, "eventHandler");
        EventHandler[] crudSwitchOnChangeHandlers = getPageWidgetByShortName(Widget.class,
                "appletPanel.entityCrudPanel.switchOnChangeHolder").getUplAttribute(EventHandler[].class,
                        "eventHandler");
        EventHandler[] saveAsSwitchOnChangeHandlers = getPageWidgetByShortName(Widget.class,
                "appletPanel.entitySaveAsPanel.switchOnChangeHolder").getUplAttribute(EventHandler[].class,
                        "eventHandler");
        return new EntityFormEventHandlers(formSwitchOnChangeHandlers, assnSwitchOnChangeHandlers,
                entrySwitchOnChangeHandlers, crudActionHandlers, crudSwitchOnChangeHandlers,
                saveAsSwitchOnChangeHandlers);
    }
}

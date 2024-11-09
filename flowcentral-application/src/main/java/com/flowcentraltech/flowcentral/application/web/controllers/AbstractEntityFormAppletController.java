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

import com.flowcentraltech.flowcentral.application.business.EntityTreeSelectGenerator;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationResultMappingConstants;
import com.flowcentraltech.flowcentral.application.constants.WorkflowDraftType;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.data.WorkflowDraftInfo;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySelect;
import com.flowcentraltech.flowcentral.application.web.panels.EntityTreeSelect;
import com.flowcentraltech.flowcentral.application.web.panels.QuickFormEdit;
import com.flowcentraltech.flowcentral.application.web.panels.QuickTableEdit;
import com.flowcentraltech.flowcentral.application.web.panels.QuickTableOrder;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractEntityFormApplet;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractEntityFormApplet.ShowPopupInfo;
import com.flowcentraltech.flowcentral.application.web.widgets.BeanListTableWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.data.MessageBoxCaptions;
import com.tcdng.unify.web.ui.widget.data.MessageIcon;
import com.tcdng.unify.web.ui.widget.data.MessageMode;
import com.tcdng.unify.web.ui.widget.data.MessageResult;
import com.tcdng.unify.web.ui.widget.data.Popup;

/**
 * Abstract base class for entity form applet controllers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractEntityFormAppletController<T extends AbstractEntityFormApplet, U extends AbstractEntityFormAppletPageBean<T>>
        extends AbstractAppletController<U> {

    private static final String IN_WORKFLOW_DRAFT_LOOP_FLAG = "IN_WORKFLOW_DRAFT_LOOP_FLAG";

    public AbstractEntityFormAppletController(Class<U> pageBeanClass, Secured secured, ReadOnly readOnly,
            ResetOnWrite resetOnWrite) {
        super(pageBeanClass, secured, readOnly, resetOnWrite);
    }

    @Action
    public String newChildItem() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        final int childTabIndex = getChildTabIndex();
        if (applet.isPromptEnterWorkflowDraft()) {
            return showPromptWorkflowDraft(WorkflowDraftType.NEW_CHILD_ITEM, childTabIndex);
        }

        if (saveFormState(applet)) {
            applet.newChildItem(childTabIndex);
            getPageRequestContextUtil().setContentScrollReset();
        }

        return "refreshapplet";
    }

    @Action
    public String quickTableEdit() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        final int childTabIndex = getChildTabIndex();
        if (applet.isPromptEnterWorkflowDraft()) {
            return showPromptWorkflowDraft(WorkflowDraftType.QUICK_TABLE_EDIT, childTabIndex);
        }

        if (saveFormState(applet)) {
            QuickTableEdit quickTableEdit = applet.quickTableEdit(childTabIndex);
            if (quickTableEdit != null) {
                return showPopup(new Popup(ApplicationResultMappingConstants.SHOW_QUICK_TABLE_EDIT, quickTableEdit,
                        quickTableEdit.getWidth(), quickTableEdit.getHeight()));
            }
        }

        return "refreshapplet";
    }

    @Action
    public String quickTableOrder() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        final int childTabIndex = getChildTabIndex();
        if (applet.isPromptEnterWorkflowDraft()) {
            return showPromptWorkflowDraft(WorkflowDraftType.QUICK_TABLE_ORDER, childTabIndex);
        }

        if (saveFormState(applet)) {
            QuickTableOrder quickTableOrder = applet.quickTableOrder(childTabIndex);
            if (quickTableOrder != null) {
                return showPopup(new Popup(ApplicationResultMappingConstants.SHOW_QUICK_TABLE_ORDER, quickTableOrder));
            }
        }

        return "refreshapplet";
    }

    @Action
    public String quickFormEdit() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        final int childTabIndex = getChildTabIndex();
        if (applet.isPromptEnterWorkflowDraft()) {
            return showPromptWorkflowDraft(WorkflowDraftType.QUICK_FORM_EDIT, childTabIndex);
        }

        if (saveFormState(applet)) {
            QuickFormEdit quickFormEdit = applet.quickFormEdit(childTabIndex);
            if (quickFormEdit != null) {
                return showPopup(new Popup(ApplicationResultMappingConstants.SHOW_QUICK_FORM_EDIT, quickFormEdit,
                        quickFormEdit.getWidth(), quickFormEdit.getHeight()));
            }
        }

        return "refreshapplet";
    }

    @Action
    public String newChildListItem() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        final int childTabIndex = getChildTabIndex();
        if (applet.isPromptEnterWorkflowDraft()) {
            return showPromptWorkflowDraft(WorkflowDraftType.NEW_CHILDLIST_ITEM, childTabIndex);
        }

        if (saveFormState(applet)) {
            ShowPopupInfo showPopupInfo = applet.newChildShowPopup(childTabIndex);
            if (showPopupInfo != null) {
                ValueStore formValueStore = applet.getForm().getCtx().getFormValueStore();
                switch (showPopupInfo.getType()) {
                    case SHOW_MULTISELECT: {
                        RefDef refDef = au().getRefDef(showPopupInfo.getReference());
                        EntitySelect entitySelect = applet.au().constructEntitySelect(refDef, formValueStore, null,
                                null, null, 0);
                        entitySelect.setEnableFilter(false);
                        entitySelect.applyFilterToSearch();
                        String title = resolveSessionMessage("$m{entitymultiselectpanel.select.entity}",
                                entitySelect.getEntityTable().getEntityDef().getLabel());
                        entitySelect.setTitle(title);
                        return showPopup(
                                new Popup(ApplicationResultMappingConstants.SHOW_ENTITY_MULTISELECT, entitySelect));
                    }
                    case SHOW_TREEMULTISELECT: {
                        EntityTreeSelectGenerator generator = au().getComponent(EntityTreeSelectGenerator.class,
                                showPopupInfo.getReference());
                        EntityTreeSelect entityTreeSelect = generator.generate(au(), formValueStore);
                        entityTreeSelect.setTitle(entityTreeSelect.getEntityTreeTable().getTitle());
                        return showPopup(new Popup(ApplicationResultMappingConstants.SHOW_ENTITY_TREEMULTISELECT,
                                entityTreeSelect));
                    }
                    default:
                        break;
                }
            }

            applet.newChildListItem(childTabIndex);
            getPageRequestContextUtil().setContentScrollReset();
        }

        return "refreshapplet";
    }

    @Action
    public String editChildItem() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        final int childTabIndex = getChildTabIndex();
        if (applet.isPromptEnterWorkflowDraft()) {
            return showPromptWorkflowDraft(WorkflowDraftType.EDIT_CHILD_ITEM, childTabIndex);
        }

        if (saveFormState(applet)) {
            applet.editChildItem(childTabIndex);
            getPageRequestContextUtil().setContentScrollReset();
        }

        return "refreshapplet";
    }

    @Action
    public String assignToChildItem() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        final int childTabIndex = getChildTabIndex();
        if (applet.isPromptEnterWorkflowDraft()) {
            return showPromptWorkflowDraft(WorkflowDraftType.ASSIGN_TO_CHILD_ITEM, childTabIndex);
        }

        if (saveFormState(applet)) {
            applet.assignToChildItem(childTabIndex);
            getPageRequestContextUtil().setContentScrollReset();
        }

        return "refreshapplet";
    }

    @Action
    public String entryToChildItem() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        final int childTabIndex = getChildTabIndex();
        if (applet.isPromptEnterWorkflowDraft()) {
            return showPromptWorkflowDraft(WorkflowDraftType.ENTRY_TO_CHILD_ITEM, childTabIndex);
        }

        if (saveFormState(applet)) {
            applet.entryToChildItem(childTabIndex);
            getPageRequestContextUtil().setContentScrollReset();
        }

        return "refreshapplet";
    }

    @Action
    public String crudToChildItem() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        final int childTabIndex = getChildTabIndex();
        if (applet.isPromptEnterWorkflowDraft()) {
            return showPromptWorkflowDraft(WorkflowDraftType.CRUD_TO_CHILD_ITEM, childTabIndex);
        }

        if (saveFormState(applet)) {
            applet.crudToChildItem(childTabIndex);
            getPageRequestContextUtil().setContentScrollReset();
        }

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

    @Action
    public String openWorkflowDraft() throws UnifyException {
        MessageResult result = getMessageResult();
        switch (result == null ? MessageResult.CANCEL : result) {
            case NO:
                performNormalViewMode();
                break;
            case OK:
            case YES:
                performEditModeWorkflowDraft();
                break;
            case CANCEL:
            case RETRY:
            default:
                break;
        }

        return "refreshapplet";
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        final boolean indicateHighLatency = system().getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.ENABLE_HIGH_LATENCY_INDICATION);
        if (indicateHighLatency) {
            getPageRequestContextUtil().setLowLatencyRequest();
        } else {
            setReloadOnSwitch();
        }
    }

    @Override
    protected void onClosePage() throws UnifyException {
        super.onClosePage();
        final boolean indicateHighLatency = system().getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.ENABLE_HIGH_LATENCY_INDICATION);
        if (indicateHighLatency) {
            getPageRequestContextUtil().setLowLatencyRequest();
        }

        setReloadOnSwitch();
    }

    private int getChildTabIndex() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        return getRequestAttribute(boolean.class, IN_WORKFLOW_DRAFT_LOOP_FLAG)
                ? DataUtils.convert(int.class, applet.removeWorkflowDraftInfo().getRequestTarget())
                : super.getRequestTarget(int.class);
    }

    protected AppletWidgetReferences getAppletWidgetReferences() throws UnifyException {
        BeanListTableWidget assignmentEntryTableWidget = getPageWidgetByShortName(BeanListTableWidget.class,
                "appletPanel.assignmentPanel.assignmentEntryTbl");
        BeanListTableWidget entryEntryTableWidget = getPageWidgetByShortName(BeanListTableWidget.class,
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
        EventHandler[] maintainActHandlers = getPageWidgetByShortName(Widget.class, "appletPanel.maintainActHolder")
                .getUplAttribute(EventHandler[].class, "eventHandler");
        return new EntityFormEventHandlers(formSwitchOnChangeHandlers, assnSwitchOnChangeHandlers,
                entrySwitchOnChangeHandlers, crudActionHandlers, crudSwitchOnChangeHandlers,
                saveAsSwitchOnChangeHandlers, maintainActHandlers);
    }

    private void performNormalViewMode() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        applet.appletCtx().setInWorkflowPromptViewMode(true);
        setRequestAttribute(IN_WORKFLOW_DRAFT_LOOP_FLAG, Boolean.TRUE);
        WorkflowDraftInfo workflowDraftInfo = applet.getWorkflowDraftInfo();
        switch (workflowDraftInfo.getType()) {
            case ASSIGN_TO_CHILD_ITEM:
                assignToChildItem();
                break;
            case CRUD_TO_CHILD_ITEM:
                crudToChildItem();
                break;
            case EDIT_CHILD_ITEM:
                editChildItem();
                break;
            case ENTRY_TO_CHILD_ITEM:
                entryToChildItem();
                break;
            case NEW_CHILDLIST_ITEM:
                newChildListItem();
                break;
            case NEW_CHILD_ITEM:
                newChildItem();
                break;
            case QUICK_FORM_EDIT:
                quickFormEdit();
                break;
            case QUICK_TABLE_EDIT:
                quickTableEdit();
                break;
            case QUICK_TABLE_ORDER:
                quickTableOrder();
                break;
            case MAINTAIN:
            case UPDATE:
            case UPDATE_CLOSE:
            default:
                applet.appletCtx().setInWorkflowPromptViewMode(false);
                setRequestAttribute(IN_WORKFLOW_DRAFT_LOOP_FLAG, Boolean.FALSE);
                break;
        }
    }

    private void performEditModeWorkflowDraft() throws UnifyException {
        AbstractEntityFormAppletPageBean<T> pageBean = getPageBean();
        AbstractEntityFormApplet applet = pageBean.getApplet();
        WorkflowDraftInfo workflowDraftInfo = applet.getWorkflowDraftInfo();
        applet.enterWorkflowDraft(workflowDraftInfo.getType());
    }

    private String showPromptWorkflowDraft(WorkflowDraftType type, int requestTarget) throws UnifyException {
        getPageBean().getApplet().setWorkflowDraftInfo(new WorkflowDraftInfo(type, requestTarget));
        final String caption = resolveSessionMessage("$m{formapplet.workflowdraft.caption}");
        final String prompt = resolveSessionMessage("$m{formapplet.workflowdraft.prompt}");
        final String viewMessage = resolveSessionMessage("$m{formapplet.workflowdraft.enterview}");
        final String editMessage = resolveSessionMessage("$m{formapplet.workflowdraft.enteredit}");
        final String cancelMessage = resolveSessionMessage("$m{formapplet.workflowdraft.cancel}");
        final String openWorkflowDraftPath = getVariableActionPath("/openWorkflowDraft");
        MessageBoxCaptions captions = new MessageBoxCaptions(caption);
        if (type.isNew() || type.isUpdate()) {
            captions.setOkStyleClass("fc-orangebutton");
            captions.setOkCaption(editMessage);
            captions.setCancelCaption(cancelMessage);
            return showMessageBox(MessageIcon.QUESTION, MessageMode.OK_CANCEL, captions, prompt, openWorkflowDraftPath);
        }

        captions.setYesStyleClass("fc-orangebutton");
        captions.setNoStyleClass("fc-bluebutton");

        captions.setYesCaption(editMessage);
        captions.setNoCaption(viewMessage);
        return showMessageBox(MessageIcon.QUESTION, MessageMode.YES_NO_CANCEL, captions, prompt, openWorkflowDraftPath);
    }

    private boolean saveFormState(AbstractEntityFormApplet applet) throws UnifyException {
        // TODO
        return true;
    }
}

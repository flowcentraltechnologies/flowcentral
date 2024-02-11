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
import java.util.Set;

import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.web.panels.applet.ManageEntityDetailsApplet;
import com.flowcentraltech.flowcentral.common.business.policies.EntryTablePolicy;
import com.flowcentraltech.flowcentral.common.business.policies.FixedRowActionType;
import com.flowcentraltech.flowcentral.common.business.policies.TableStateOverride;
import com.flowcentraltech.flowcentral.common.business.policies.TableSummaryLine;
import com.flowcentraltech.flowcentral.common.constants.EntryActionType;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.constants.TableChangeType;
import com.flowcentraltech.flowcentral.common.data.FormValidationErrors;
import com.flowcentraltech.flowcentral.common.data.RowChangeInfo;
import com.tcdng.unify.core.UnifyComponentContext;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.Widget;

/**
 * Convenient abstract base class for entity detail inline child list
 * controllers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@UplBinding("web/application/upl/entitydetailsinlinechildlistpage.upl")
@ResultMappings({ @ResultMapping(name = "refreshChildCrud",
        response = { "!refreshpanelresponse panels:$l{entityCrudPanel}", "!commonreportresponse" }) })
public abstract class AbstractEntityDetailsInlineChildListPageController<T extends AbstractEntityDetailsPageBean>
        extends AbstractEntityDetailsPageController<T> {

    private final String childAppletName;

    private final String childBaseFieldName;

    private final InlineDetailsEntryTablePolicy childListEntryTablePolicy;

    public AbstractEntityDetailsInlineChildListPageController(Class<T> pageBeanClass, Secured secured,
            ReadOnly readOnly, ResetOnWrite resetOnWrite, String detailsAppletName, String childAppletName,
            String childBaseFieldName) {
        super(pageBeanClass, secured, readOnly, resetOnWrite, detailsAppletName);
        this.childAppletName = childAppletName;
        this.childBaseFieldName = childBaseFieldName;
        this.childListEntryTablePolicy = new InlineDetailsEntryTablePolicy();
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

    protected abstract List<TableSummaryLine> getInlinePreDetailsTableSummaryLines(ValueStore tableValueStore)
            throws UnifyException;

    protected abstract List<TableSummaryLine> getInlinePostDetailsTableSummaryLines(ValueStore tableValueStore)
            throws UnifyException;

    @Override
    protected void onOpenPage() throws UnifyException {
        AbstractEntityDetailsPageBean pageBean = getPageBean();
        setPageWidgetVisible("entityCrudInlinePanel", pageBean.getApplet() != null);
        super.onOpenPage();
    }

    protected final ManageEntityDetailsApplet createManageEntityDetailsApplet() throws UnifyException {
        return new ManageEntityDetailsApplet(au(), getDetailsAppletName(), childAppletName, childBaseFieldName,
                getEntityFormEventHandlers());
    }

    protected String showChildCrud(Long parentId) throws UnifyException {
        if (parentId != null) {
            final AbstractEntityDetailsPageBean pageBean = getPageBean();
            pageBean.getApplet().createNewChildCrud(parentId);
            pageBean.getChildEntityCrud().getTable().setPolicy(childListEntryTablePolicy);
            setPageWidgetVisible("entityCrudInlinePanel", true);
        } else {
            hideChildCrud();
        }

        return refreshBase();
    }

    protected void hideChildCrud() throws UnifyException {
        final AbstractEntityDetailsPageBean pageBean = getPageBean();
        pageBean.getApplet().clearChildEntityCrud();
        setPageWidgetVisible("entityCrudInlinePanel", false);
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

    protected class InlineDetailsEntryTablePolicy implements EntryTablePolicy {

        @Override
        public String getName() {
            return null;
        }

        @Override
        public UnifyComponentContext getUnifyComponentContext() throws UnifyException {
            return null;
        }

        @Override
        public void initialize(UnifyComponentContext arg0) throws UnifyException {

        }

        @Override
        public boolean isInitialized() {
            return false;
        }

        @Override
        public void terminate() throws UnifyException {

        }

        @Override
        public void validateEntries(EvaluationMode evaluationMode, ValueStoreReader parentReader,
                ValueStore tableValueStore, FormValidationErrors errors) throws UnifyException {
        }

        @Override
        public void onEntryTableLoad(ValueStoreReader parentReader, ValueStore tableValueStore, Set<Integer> selected)
                throws UnifyException {
        }

        @Override
        public EntryActionType onEntryTableChange(ValueStoreReader parentReader, ValueStore tableValueStore,
                Set<Integer> selected, TableChangeType changeType) throws UnifyException {
            return null;
        }

        @Override
        public EntryActionType onEntryRowChange(ValueStoreReader parentReader, ValueStore tableValueStore,
                RowChangeInfo rowChangeInfo) throws UnifyException {
            return null;
        }

        @Override
        public List<TableSummaryLine> getPreTableSummaryLines(ValueStoreReader parentReader, ValueStore tableValueStore)
                throws UnifyException {
            return getInlinePreDetailsTableSummaryLines(tableValueStore);
        }

        @Override
        public List<TableSummaryLine> getPostTableSummaryLines(ValueStoreReader parentReader, ValueStore tableValueStore)
                throws UnifyException {
            return getInlinePostDetailsTableSummaryLines(tableValueStore);
        }

        @Override
        public void applyTableStateOverride(ValueStoreReader parentReader, ValueStore rowValueStore,
                TableStateOverride tableStateOverride) throws UnifyException {
        }

        @Override
        public void applyFixedAction(ValueStoreReader parentReader, ValueStore valueStore, int index,
                FixedRowActionType fixedActionType) throws UnifyException {
        }

        @Override
        public FixedRowActionType resolveFixedIndex(ValueStoreReader parentReader, ValueStore valueStore, int index,
                int size) throws UnifyException {
            return null;
        }

        @Override
        public int resolveActionIndex(ValueStoreReader parentReader, ValueStore valueStore, int index, int size)
                throws UnifyException {
            return 0;
        }

    }

}

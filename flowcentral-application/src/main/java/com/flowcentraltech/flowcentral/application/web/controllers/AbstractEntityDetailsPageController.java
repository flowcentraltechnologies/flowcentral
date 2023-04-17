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
package com.flowcentraltech.flowcentral.application.web.controllers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.web.data.DetailsFormListing;
import com.flowcentraltech.flowcentral.application.web.data.Formats;
import com.flowcentraltech.flowcentral.application.web.data.Summary;
import com.flowcentraltech.flowcentral.application.web.panels.applet.ManageEntityDetailsApplet;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityListTable;
import com.flowcentraltech.flowcentral.common.business.policies.EntryTablePolicy;
import com.flowcentraltech.flowcentral.common.business.policies.FixedRowActionType;
import com.flowcentraltech.flowcentral.common.business.policies.TableStateOverride;
import com.flowcentraltech.flowcentral.common.business.policies.TableSummaryLine;
import com.flowcentraltech.flowcentral.common.constants.CommonModuleNameConstants;
import com.flowcentraltech.flowcentral.common.constants.EntryActionType;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralRequestAttributeConstants;
import com.flowcentraltech.flowcentral.common.constants.TableChangeType;
import com.flowcentraltech.flowcentral.common.data.FormValidationErrors;
import com.flowcentraltech.flowcentral.common.data.ReportOptions;
import com.flowcentraltech.flowcentral.common.data.RowChangeInfo;
import com.tcdng.unify.core.UnifyComponentContext;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.data.IndexedTarget;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.annotation.ResultMapping;
import com.tcdng.unify.web.annotation.ResultMappings;
import com.tcdng.unify.web.constant.ReadOnly;
import com.tcdng.unify.web.constant.ResetOnWrite;
import com.tcdng.unify.web.constant.Secured;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.Widget;

/**
 * Convenient abstract base class for entity details controllers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@UplBinding("web/application/upl/entitydetailspage.upl")
@ResultMappings({
        @ResultMapping(name = "refreshBase",
                response = { "!refreshpanelresponse panels:$l{basePanel}", "!commonreportresponse" }),
        @ResultMapping(name = "refreshResult",
                response = { "!refreshpanelresponse panels:$l{resultPanel}", "!commonreportresponse" }),
        @ResultMapping(name = "reloadResult",
                response = { "!refreshpanelresponse panels:$l{resultPanel}", "!commonreportresponse" },
                reload = true) })
public abstract class AbstractEntityDetailsPageController<T extends AbstractEntityDetailsPageBean>
        extends AbstractAppletController<T> {

    private final String detailsAppletName;

    private final DetailsEntryTablePolicy entryTablePolicy;

    public AbstractEntityDetailsPageController(Class<T> pageBeanClass, Secured secured, ReadOnly readOnly,
            ResetOnWrite resetOnWrite, String detailsAppletName) {
        super(pageBeanClass, secured, readOnly, resetOnWrite);
        this.detailsAppletName = detailsAppletName;
        this.entryTablePolicy = new DetailsEntryTablePolicy();
    }

    @Action
    public final String details() throws UnifyException {
        IndexedTarget target = getRequestTarget(IndexedTarget.class);
        if (target.isValidIndex()) {
            getResultTable().setDetailsIndex(target.getIndex());
            return refreshResult();
        }

        return noResult();
    }

    @Action
    public final String buttons() throws UnifyException {
        IndexedTarget target = getRequestTarget(IndexedTarget.class);
        if (target.isValidIndex()) {
            return onAction(target.getIndex(), getResultTable().getDispItemList().get(target.getIndex()),
                    target.getTarget());
        }

        return noResult();
    }

    @Action
    public final String view() throws UnifyException {
        final int index = getRequestTarget(int.class);
        return onView(index, getResultTable().getDispItemList().get(index));
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();
        AbstractEntityDetailsPageBean pageBean = getPageBean();
        if (pageBean.getApplet() == null) {
            ManageEntityDetailsApplet applet = createManageEntityDetailsApplet();
            EntityListTable resultTable = new EntityListTable(au(), getTableDef());
            resultTable.setPolicy(entryTablePolicy);
            if (pageBean.isViewActionMode()) {
                String viewCaption = resolveSessionMessage(pageBean.getViewActionCaption());
                resultTable.setViewButtonCaption(viewCaption);
                EventHandler[] viewActHandlers = getPageWidgetByShortName(Widget.class, "viewActHolder")
                        .getUplAttribute(EventHandler[].class, "eventHandler");
                resultTable.setCrudActionHandlers(Arrays.asList(viewActHandlers));
                resultTable.setCrudMode(true);
                resultTable.setViewOnly(true);
            }

            applet.setResultTable(resultTable);
            pageBean.setApplet(applet);
        }
    }

    protected ManageEntityDetailsApplet createManageEntityDetailsApplet() throws UnifyException {
        return new ManageEntityDetailsApplet(au(), detailsAppletName);
    }

    protected final EntityListTable getResultTable() throws UnifyException {
        return getPageBean().getApplet().getResultTable();
    }

    protected final void setReport(String reportConfigName, List<? extends Entity> entityList) throws UnifyException {
        ReportOptions reportOptions = au().reportProvider().getReportOptionsForConfiguration(reportConfigName);
        reportOptions.setContent(entityList);
        reportOptions.setReportResourcePath(CommonModuleNameConstants.CONFIGURED_REPORT_RESOURCE);
        setRequestAttribute(FlowCentralRequestAttributeConstants.REPORTOPTIONS, reportOptions);
    }

    protected final String viewListingReport() throws UnifyException {
        return viewListingReport(getTableDef(), ApplicationModuleNameConstants.BASIC_DETAILSFORMLISTING_GENERATOR,
                Collections.emptyMap(), Formats.DEFAULT);
    }

    protected final String viewListingReport(String tableName) throws UnifyException {
        return viewListingReport(au().getTableDef(tableName),
                ApplicationModuleNameConstants.BASIC_DETAILSFORMLISTING_GENERATOR, Collections.emptyMap(),
                Formats.DEFAULT);
    }

    protected final String viewListingReport(String tableName, Formats formats) throws UnifyException {
        return viewListingReport(au().getTableDef(tableName),
                ApplicationModuleNameConstants.BASIC_DETAILSFORMLISTING_GENERATOR, Collections.emptyMap(), formats);
    }

    protected final String viewListingReport(String tableName, String generator, Map<String, Object> properties)
            throws UnifyException {
        return viewListingReport(au().getTableDef(tableName), generator, properties, Formats.DEFAULT);
    }

    protected final String viewListingReport(String tableName, String generator, Map<String, Object> properties,
            Formats formats) throws UnifyException {
        return viewListingReport(au().getTableDef(tableName), generator, properties, formats);
    }

    protected final String viewListingReport(String generator, Map<String, Object> properties) throws UnifyException {
        return viewListingReport(getTableDef(), generator, properties, Formats.DEFAULT);
    }

    protected final String viewListingReport(TableDef tableDef, String generator, Map<String, Object> properties)
            throws UnifyException {
        return viewListingReport(tableDef, generator, properties, Formats.DEFAULT);
    }

    protected final String viewListingReport(TableDef tableDef, String generator, Map<String, Object> properties,
            Formats formats) throws UnifyException {
        final EntityListTable table = getResultTable();
        DetailsFormListing.Builder lb = DetailsFormListing.newBuilder(tableDef, table.getSourceObject())
                .useGenerator(generator).useFormats(formats);
        if (table.isWithPreTableSummaryLines()) {
            for (TableSummaryLine line : table.getPreTableSummaryLines()) {
                lb.addPreSummary(new Summary(line.getLabel(), line.values()));
            }
        }

        if (table.isWithPostTableSummaryLines()) {
            for (TableSummaryLine line : table.getPostTableSummaryLines()) {
                lb.addPostSummary(new Summary(line.getLabel(), line.values()));
            }
        }

        lb.addProperties(properties);
        lb.summaryTitleColumn(table.getTotalLabelColumnIndex());
        return viewListingReport(lb.build());
    }

    protected String getDetailsAppletName() {
        return detailsAppletName;
    }

    protected final String refreshBase() throws UnifyException {
        return "refreshBase";
    }

    protected final String refreshResult() throws UnifyException {
        return "refreshResult";
    }

    protected final String reloadResult() throws UnifyException {
        return "reloadResult";
    }

    protected final void setPageSubCaption(String subCaption) throws UnifyException {
        getPageBean().getApplet().setSubCaption(subCaption);
    }

    protected abstract List<TableSummaryLine> getPreDetailsTableSummaryLines(ValueStore tableValueStore)
            throws UnifyException;

    protected abstract List<TableSummaryLine> getPostDetailsTableSummaryLines(ValueStore tableValueStore)
            throws UnifyException;

    protected abstract TableDef getTableDef() throws UnifyException;

    protected abstract String onView(int rowIndex, Entity inst) throws UnifyException;

    protected abstract String onAction(int rowIndex, Entity inst, String action) throws UnifyException;

    protected final class DetailsEntryTablePolicy implements EntryTablePolicy {

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
        public Number getParentColumnSummaryValue(ValueStoreReader parentReader, String fieldName)
                throws UnifyException {
            return null;
        }

        @Override
        public Number getTableColumnSummaryValue(ValueStoreReader parentReader, String fieldName,
                ValueStore itemValueStore) throws UnifyException {
            return null;
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
            return getPreDetailsTableSummaryLines(tableValueStore);
        }

        @Override
        public List<TableSummaryLine> getPostTableSummaryLines(ValueStoreReader parentReader,
                ValueStore tableValueStore) throws UnifyException {
            return getPostDetailsTableSummaryLines(tableValueStore);
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

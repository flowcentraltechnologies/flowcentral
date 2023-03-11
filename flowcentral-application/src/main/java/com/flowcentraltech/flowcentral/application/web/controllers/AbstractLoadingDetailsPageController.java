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
import java.util.List;

import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.web.panels.applet.ManageLoadingDetailsApplet;
import com.flowcentraltech.flowcentral.application.web.widgets.LoadingTable;
import com.flowcentraltech.flowcentral.common.constants.CommonModuleNameConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralRequestAttributeConstants;
import com.flowcentraltech.flowcentral.common.data.ReportOptions;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.data.IndexedTarget;
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
 * Convenient abstract base class for loading details controllers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@UplBinding("web/application/upl/loadingdetailspage.upl")
@ResultMappings({
        @ResultMapping(name = "refreshBase",
                response = { "!refreshpanelresponse panels:$l{basePanel}", "!commonreportresponse" }),
        @ResultMapping(name = "refreshResult",
                response = { "!refreshpanelresponse panels:$l{resultPanel}", "!commonreportresponse" }),
        @ResultMapping(name = "reloadResult",
                response = { "!refreshpanelresponse panels:$l{resultPanel}", "!commonreportresponse" },
                reload = true) })
public abstract class AbstractLoadingDetailsPageController<T extends AbstractLoadingDetailsPageBean>
        extends AbstractAppletController<T> {

    private final String detailsAppletName;

    public AbstractLoadingDetailsPageController(Class<T> pageBeanClass, Secured secured, ReadOnly readOnly,
            ResetOnWrite resetOnWrite, String detailsAppletName) {
        super(pageBeanClass, secured, readOnly, resetOnWrite);
        this.detailsAppletName = detailsAppletName;
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

        AbstractLoadingDetailsPageBean pageBean = getPageBean();
        if (pageBean.getApplet() == null) {
            ManageLoadingDetailsApplet applet = new ManageLoadingDetailsApplet(au(), detailsAppletName);
            LoadingTable resultTable = new LoadingTable(au(), getTableDef());
            if (pageBean.isViewActionMode()) {
                String viewCaption = resolveSessionMessage(pageBean.getViewActionCaption());
                resultTable.setViewButtonCaption(viewCaption);
                EventHandler[] viewActHandlers = getPageWidgetByShortName(Widget.class, "viewActHolder")
                        .getUplAttribute(EventHandler[].class, "eventHandler");
                resultTable.setCrudActionHandlers(Arrays.asList(viewActHandlers));
                resultTable.setCrudMode(true);
                resultTable.setView(true);
            }

            applet.setResultTable(resultTable);
            pageBean.setApplet(applet);
        }
    }

    protected String getDetailsAppletName() {
        return detailsAppletName;
    }

    protected final LoadingTable getResultTable() throws UnifyException {
        return getPageBean().getResultTable();
    }

    protected final void setReport(String reportConfigName, List<? extends Entity> entityList) throws UnifyException {
        ReportOptions reportOptions = au().reportProvider().getReportOptionsForConfiguration(reportConfigName);
        reportOptions.setContent(entityList);
        reportOptions.setReportResourcePath(CommonModuleNameConstants.CONFIGURED_REPORT_RESOURCE);
        setRequestAttribute(FlowCentralRequestAttributeConstants.REPORTOPTIONS, reportOptions);
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

    protected abstract TableDef getTableDef() throws UnifyException;

    protected abstract String onView(int rowIndex, Entity inst) throws UnifyException;

    protected abstract String onAction(int rowIndex, Entity inst, String action) throws UnifyException;
}

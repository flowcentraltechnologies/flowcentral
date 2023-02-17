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

import java.util.Arrays;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityListTable;
import com.flowcentraltech.flowcentral.common.constants.CommonModuleNameConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralRequestAttributeConstants;
import com.flowcentraltech.flowcentral.common.data.ReportOptions;
import com.flowcentraltech.flowcentral.common.web.controllers.AbstractFlowCentralPageController;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.StringUtils;
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
        @ResultMapping(name = "refreshResult",
                response = { "!refreshpanelresponse panels:$l{resultPanel}", "!commonreportresponse" }),
        @ResultMapping(name = "reloadResult",
                response = { "!refreshpanelresponse panels:$l{resultPanel}", "!commonreportresponse" },
                reload = true) })
public abstract class AbstractEntityDetailsPageController<T extends AbstractEntityDetailsPageBean>
        extends AbstractFlowCentralPageController<T> {

    @Configurable
    private AppletUtilities appletUtilities;

    public AbstractEntityDetailsPageController(Class<T> pageBeanClass, Secured secured, ReadOnly readOnly,
            ResetOnWrite resetOnWrite) {
        super(pageBeanClass, secured, readOnly, resetOnWrite);
    }

    public final void setAppletUtilities(AppletUtilities appletUtilities) {
        this.appletUtilities = appletUtilities;
    }

    @Action
    public final String details() throws UnifyException {
        final String[] po = StringUtils.charSplit(getRequestTarget(String.class), ':');
        if (po.length > 0) {
            final int mIndex = Integer.parseInt(po[0]);
            getResultTable().setDetailsIndex(mIndex);
            return refreshResult();
        }

        return noResult();
    }

    @Action
    public final String view() throws UnifyException {
        final int index = getRequestTarget(int.class);
        return onView(getResultTable().getDispItemList().get(index));
    }

    @Override
    protected void onOpenPage() throws UnifyException {
        super.onOpenPage();

        AbstractEntityDetailsPageBean pageBean = getPageBean();
        if (pageBean.getResultTable() == null) {
            EntityListTable resultTable = new EntityListTable(au(), getTableDef());
            if (pageBean.isViewActionMode()) {
                String viewCaption = resolveSessionMessage(pageBean.getViewActionCaption());
                resultTable.setViewButtonCaption(viewCaption);
                EventHandler[] viewActHandlers = getPageWidgetByShortName(Widget.class, "viewActHolder")
                        .getUplAttribute(EventHandler[].class, "eventHandler");
                resultTable.setCrudActionHandlers(Arrays.asList(viewActHandlers));
                resultTable.setCrudMode(true);
                resultTable.setView(true);
            }

            pageBean.setResultTable(resultTable);
        }
    }

    protected final AppletUtilities au() {
        return appletUtilities;
    }

    protected final ApplicationModuleService application() {
        return appletUtilities.application();
    }

    protected final EntityListTable getResultTable() throws UnifyException {
        return getPageBean().getResultTable();
    }

    protected final void setReport(String reportConfigName, List<? extends Entity> entityList) throws UnifyException {
        ReportOptions reportOptions = au().reportProvider().getReportOptionsForConfiguration(reportConfigName);
        reportOptions.setContent(entityList);
        reportOptions.setReportResourcePath(CommonModuleNameConstants.CONFIGURED_REPORT_RESOURCE);
        setRequestAttribute(FlowCentralRequestAttributeConstants.REPORTOPTIONS, reportOptions);
    }

    protected final String refreshResult() throws UnifyException {
        return "refreshResult";
    }

    protected final String reloadResult() throws UnifyException {
        return "reloadResult";
    }

    protected abstract TableDef getTableDef() throws UnifyException;

    protected abstract String onView(Entity inst) throws UnifyException;
}

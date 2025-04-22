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
package com.flowcentraltech.flowcentral.application.web.panels;

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.constants.CommonModuleNameConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralRequestAttributeConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralResultMappingConstants;
import com.flowcentraltech.flowcentral.common.data.GenerateListingReportOptions;
import com.flowcentraltech.flowcentral.common.data.ReportOptions;
import com.flowcentraltech.flowcentral.common.web.panels.AbstractDetailsPanel;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.report.Report;

/**
 * Convenient abstract base class for entity details panels.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@UplBinding("web/application/upl/entitydetailspanel.upl")
public abstract class AbstractEntityDetailsPanel extends AbstractDetailsPanel<Entity> {

    private static final String ERROR_BINDING = "errorMsg";

    @Configurable
    private AppletUtilities au;

    @Override
    public final void switchState() throws UnifyException {
        super.switchState();
        removeTempValue(ERROR_BINDING);
        doSwitchState();
        setVisible(ERROR_BINDING, isWithError());
    }

    @Override
    protected final Entity getDetails(ValueStore valueStore) throws UnifyException {
        return (Entity) valueStore.getValueObjectAtDataIndex();
    }

    protected final AppletUtilities au() {
        return au;
    }

    protected final ApplicationModuleService application() {
        return au.application();
    }

    protected final EnvironmentService environment() {
        return au.environment();
    }

    protected final void setRefreshResult() throws UnifyException {
        setCommandResultMapping("refreshResult");
    }

    protected final void setReloadResult() throws UnifyException {
        setCommandResultMapping("reloadResult");
    }

    protected final void setReport(String reportConfigName, List<? extends Entity> entityList) throws UnifyException {
        ReportOptions reportOptions = au().reportProvider().getReportOptionsForConfiguration(reportConfigName);
        reportOptions.setContent(entityList);
        reportOptions.setReportResourcePath(CommonModuleNameConstants.CONFIGURED_REPORT_RESOURCE);
        setRequestAttribute(FlowCentralRequestAttributeConstants.REPORTOPTIONS, reportOptions);
    }

    protected final void generateAndShowReport(ValueStoreReader reader, List<GenerateListingReportOptions> options)
            throws UnifyException {
        Report report = au.generateViewListingReport(reader, options);
        setRequestAttribute(FlowCentralRequestAttributeConstants.REPORT, report);
        setCommandResultMapping(FlowCentralResultMappingConstants.VIEW_LISTING_REPORT);
    }

    protected void setError(String message, Object... params) throws UnifyException {
        setTempValue(ERROR_BINDING, resolveSessionMessage(message, params));
    }

    protected boolean isWithError() throws UnifyException {
        return isTempValue(ERROR_BINDING);
    }

    protected abstract void doSwitchState() throws UnifyException;
}

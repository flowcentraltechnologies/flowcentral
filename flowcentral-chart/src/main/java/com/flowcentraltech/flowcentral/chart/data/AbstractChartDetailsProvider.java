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

package com.flowcentraltech.flowcentral.chart.data;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.chart.business.ChartModuleService;
import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;

/**
 * Convenient abstract base class for chart details providers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractChartDetailsProvider extends AbstractFlowCentralComponent implements ChartDetailsProvider {

    @Configurable
    private ChartModuleService chartModuleService;

    @Configurable
    private AppletUtilities appletUtilities;

    @Configurable
    private EnvironmentService environmentService;

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    protected final AppletUtilities au() {
        return appletUtilities;
    }

    protected final ApplicationModuleService application() {
        return appletUtilities.application();
    }

    protected final EnvironmentService environment() {
        return environmentService;
    }

    protected final ChartModuleService chart() {
        return chartModuleService;
    }
    
    @SuppressWarnings("unchecked")
    protected final List<? extends Entity> getStatistics(String entity) {
        try {
            EntityClassDef entityClassDef = application().getEntityClassDef(entity);
            return environment().findAll(Query.of((Class<? extends Entity>) entityClassDef.getEntityClass()));
        } catch (UnifyException e) {
            appletUtilities.consumeExceptionAndGenerateHint(e, "$m{chart.provider.errorgettingdata}");
        }

        return Collections.emptyList();
    }
    
    protected SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd");
    }
    
    protected final String formatInteger(double val) {
        return new DecimalFormat("###,###,###").format(val);
    }
    
    protected final String formatDecimal(double val) {
        return new DecimalFormat("###,###,###.##").format(val);
    }
    
    protected final String formatDate(SimpleDateFormat sdf, Date val) {
        return val != null ? sdf.format(val) : "(null)";
    }
    
    protected final double doubleValue(BigDecimal val) {
        return val != null ? val.doubleValue() : 0;
    }

}

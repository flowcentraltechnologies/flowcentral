/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.studio.web.panels;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.chart.business.ChartModuleService;
import com.flowcentraltech.flowcentral.chart.data.ChartConfiguration;
import com.flowcentraltech.flowcentral.chart.data.SimpleChartConfiguration;
import com.tcdng.unify.core.UnifyException;

/**
 * Chart view.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ChartView extends AbstractStudioEditorPage {

    final ChartModuleService cms;

    final ChartConfiguration configuration;

    private final Object baseId;

    public ChartView(AppletUtilities au, ChartModuleService cms, String chartName, Object baseId,
            BreadCrumbs breadCrumbs) {
        super(au, breadCrumbs);
        this.cms = cms;
        this.configuration = new SimpleChartConfiguration(chartName);
        this.baseId = baseId;
    }

    public ChartConfiguration getConfiguration() {
        return configuration;
    }

    public Object getBaseId() {
        return baseId;
    }

    public void reloadContent() throws UnifyException {

    }
}

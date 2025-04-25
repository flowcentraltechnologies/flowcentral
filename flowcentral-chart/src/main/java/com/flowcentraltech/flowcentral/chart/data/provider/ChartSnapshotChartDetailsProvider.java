/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.chart.data.provider;

import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.chart.constants.ChartModuleNameConstants;
import com.flowcentraltech.flowcentral.chart.data.AbstractChartDetailsProvider;
import com.flowcentraltech.flowcentral.chart.data.ChartDetails;
import com.flowcentraltech.flowcentral.chart.entities.ChartSnapshotQuery;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.criterion.Restriction;

/**
 * Chart snapshot chart details provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(name = ChartModuleNameConstants.CHARTSNAPSHOT_PROVIDER, description = "$m{chartsnapshot.provider}")
public class ChartSnapshotChartDetailsProvider extends AbstractChartDetailsProvider {

    @Override
    public ChartDetails provide(String rule, Restriction restriction) throws UnifyException {
        return chart().getChartSnapshotDef(rule).getChartDetails();
    }

    @Override
    public List<? extends Listable> getRuleList(Locale locale) throws UnifyException {
        return chart().findChartSnapshots((ChartSnapshotQuery) new ChartSnapshotQuery()
                .addSelect("name", "description").ignoreEmptyCriteria(true));
    }

    @Override
    public boolean isUsesChartDataSource() {
        return false;
    }

}

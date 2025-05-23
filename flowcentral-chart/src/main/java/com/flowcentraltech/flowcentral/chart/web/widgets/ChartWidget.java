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

package com.flowcentraltech.flowcentral.chart.web.widgets;

import com.flowcentraltech.flowcentral.chart.data.ChartConfiguration;
import com.flowcentraltech.flowcentral.common.web.widgets.AbstractFlowCentralControl;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;

/**
 * Chart widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-chart")
@UplAttributes({
    @UplAttribute(name = "sparkLine", type = boolean.class, defaultVal = "false"),
    @UplAttribute(name = "preferredHeight", type = int.class) })
public class ChartWidget extends AbstractFlowCentralControl {

    public ChartConfiguration getChartConfiguration() throws UnifyException {
        return (ChartConfiguration) getValueStore().getValueObject();
    }

    public boolean isSparkLine() throws UnifyException {
        return getUplAttribute(boolean.class, "sparkLine");
    }

    public int getPreferredHeight() throws UnifyException {
        return getUplAttribute(int.class, "preferredHeight");
    }
}

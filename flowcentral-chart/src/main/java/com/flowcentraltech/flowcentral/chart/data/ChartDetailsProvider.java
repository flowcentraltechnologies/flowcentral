/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.common.business.RuleListComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Restriction;

/**
 * Chart details provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface ChartDetailsProvider extends RuleListComponent {

    /**
     * Provides data.
     * 
     * @param rule
     *             optional rule
     * @return the chart data
     * @throws UnifyException
     *                        if an error occurs
     */
    ChartDetails provide(String rule) throws UnifyException;

    /**
     * Provides data.
     * 
     * @param rule
     *                    optional rule
     * @param restriction
     *                    optional restriction
     * @return the chart data
     * @throws UnifyException
     *                        if an error occurs
     */
    ChartDetails provide(String rule, Restriction restriction) throws UnifyException;

    /**
     * Indicates if provider utilizes a chart data source
     * 
     * @return true otherwise false
     */
    boolean isUsesChartDataSource();
}

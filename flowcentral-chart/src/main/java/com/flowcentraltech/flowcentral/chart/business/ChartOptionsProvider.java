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
package com.flowcentraltech.flowcentral.chart.business;

import com.flowcentraltech.flowcentral.chart.data.ChartDef;
import com.flowcentraltech.flowcentral.chart.data.ChartDetails;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.json.JsonWriter;

/**
 * Chart options provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface ChartOptionsProvider extends UnifyComponent {
    
    /**
     * Get options type.
     * @return the options type
     * @throws UnifyException if an error occurs
     */
    String getOptionsType() throws UnifyException;
    
    /**
     * Gets chart options.
     * 
     * @param chartDef
     *                        the chart definition.
     * @param chartDetails
     *                        the chart details
     * @param sparkLine
     *                        spark line flag
     * @param preferredHeight
     *                        preferred chart height
     * @return the chart options in JSON writer
     * @throws UnifyException
     *                        if an error occurs
     */
    JsonWriter getChartOptions(ChartDef chartDef, ChartDetails chartDetails, boolean sparkLine, int preferredHeight)
            throws UnifyException;
   
}

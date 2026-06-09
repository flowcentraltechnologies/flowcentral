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

package com.flowcentraltech.flowcentral.chart.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import com.flowcentraltech.flowcentral.chart.data.CDSnapshotCategory;
import com.flowcentraltech.flowcentral.chart.data.ChartCategory;

/**
 * Chart utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public final class ChartUtils {

    private static final BigDecimal QUINTILION = BigDecimal.valueOf(1000000000000000L);

    private static final BigDecimal TRILLION = BigDecimal.valueOf(1000000000000L);

    private static final BigDecimal BILLION = BigDecimal.valueOf(1000000000L);

    private static final BigDecimal MILLION = BigDecimal.valueOf(1000000);

    private ChartUtils() {

    }

    public static ChartCategory getChartCategory(CDSnapshotCategory cdSnapshotCategory) {
        return new ChartCategory(cdSnapshotCategory.getCat(), cdSnapshotCategory.getLbl(), cdSnapshotCategory.getCat());
    }
    
    public static String getFormattedCardValue(Number num) {
        if (num != null) {
            BigDecimal _num = BigDecimal.valueOf(num.longValue());
            if (_num.compareTo(MILLION) < 0) {
                return new DecimalFormat("###,###").format(_num);
            }

            if (_num.compareTo(BILLION) < 0) {
                return new DecimalFormat("###,###.0").format(_num.divide(MILLION)) + "M";
            }

            if (_num.compareTo(TRILLION) < 0) {
                return new DecimalFormat("###,###.0").format(_num.divide(BILLION)) + "B";
            }

            if (_num.compareTo(QUINTILION) < 0) {
                return new DecimalFormat("###,###.0").format(_num.divide(TRILLION)) + "T";
            }

            return new DecimalFormat("###,###.0").format(_num.divide(QUINTILION)) + "Q";
        }

        return "";
    }

}

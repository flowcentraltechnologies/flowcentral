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

package com.flowcentraltech.flowcentral.common.util;

import java.util.Calendar;
import java.util.Date;

import com.flowcentraltech.flowcentral.common.data.DateRange;
import com.flowcentraltech.flowcentral.configuration.constants.LingualDateType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Lingual date utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class LingualDateUtils {

    private LingualDateUtils() {

    }

    public static DateRange getDateRangeFromNow(Date now, LingualDateType type) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(now);
        cal2.setTime(now);
        switch (type) {
            case LAST_15_DAYS:
                cal1.add(Calendar.DAY_OF_YEAR, -15);
                break;
            case LAST_30_DAYS:
                cal1.add(Calendar.DAY_OF_YEAR, -30);
                break;
            case LAST_45_DAYS:
                cal1.add(Calendar.DAY_OF_YEAR, -45);
                break;
            case LAST_60_DAYS:
                cal1.add(Calendar.DAY_OF_YEAR, -60);
                break;
            case LAST_90_DAYS:
                cal1.add(Calendar.DAY_OF_YEAR, -90);
                break;
            case LAST_12MONTHS:
                cal1.add(Calendar.MONTH, -12);
                break;
            case LAST_3MONTHS:
                cal1.add(Calendar.MONTH, -3);
                break;
            case LAST_6MONTHS:
                cal1.add(Calendar.MONTH, -6);
                break;
            case LAST_9MONTHS:
                cal1.add(Calendar.MONTH, -9);
                break;
            case LAST_MONTH:
                cal1.add(Calendar.MONTH, -1);
                cal2.add(Calendar.MONTH, -1);
                cal1.set(Calendar.DAY_OF_MONTH, 1);
                cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
            case LAST_WEEK:
                cal1.add(Calendar.DATE, -7);
                cal2.add(Calendar.DATE, -7);
                cal1.set(Calendar.DAY_OF_WEEK, 1);
                cal2.set(Calendar.DAY_OF_WEEK, 7);
                break;
            case LAST_YEAR:
                cal1.add(Calendar.YEAR, -1);
                cal2.add(Calendar.YEAR, -1);
                cal1.set(Calendar.DAY_OF_YEAR, 1);
                cal2.set(Calendar.DAY_OF_YEAR, cal2.getActualMaximum(Calendar.DAY_OF_YEAR));
                break;
            case NEXT_15_DAYS:
                cal2.add(Calendar.DAY_OF_YEAR, 15);
                break;
            case NEXT_30_DAYS:
                cal2.add(Calendar.DAY_OF_YEAR, 30);
                break;
            case NEXT_45_DAYS:
                cal2.add(Calendar.DAY_OF_YEAR, 45);
                break;
            case NEXT_60_DAYS:
                cal2.add(Calendar.DAY_OF_YEAR, 60);
                break;
            case NEXT_90_DAYS:
                cal2.add(Calendar.DAY_OF_YEAR, 90);
                break;
            case NEXT_12MONTHS:
                cal2.add(Calendar.MONTH, 12);
                break;
            case NEXT_3MONTHS:
                cal2.add(Calendar.MONTH, 3);
                break;
            case NEXT_6MONTHS:
                cal2.add(Calendar.MONTH, 6);
                break;
            case NEXT_9MONTHS:
                cal2.add(Calendar.MONTH, 9);
                break;
            case NEXT_MONTH:
                cal1.add(Calendar.MONTH, 1);
                cal2.add(Calendar.MONTH, 1);
                cal1.set(Calendar.DAY_OF_MONTH, 1);
                cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
            case NEXT_WEEK:
                cal1.add(Calendar.DATE, 7);
                cal2.add(Calendar.DATE, 7);
                cal1.set(Calendar.DAY_OF_WEEK, 1);
                cal2.set(Calendar.DAY_OF_WEEK, 7);
                break;
            case NEXT_YEAR:
                cal1.add(Calendar.YEAR, 1);
                cal2.add(Calendar.YEAR, 1);
                cal1.set(Calendar.DAY_OF_YEAR, 1);
                cal2.set(Calendar.DAY_OF_YEAR, cal2.getActualMaximum(Calendar.DAY_OF_YEAR));
                break;
            case THIS_MONTH:
                cal1.set(Calendar.DAY_OF_MONTH, 1);
                cal2.set(Calendar.DAY_OF_MONTH, cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
                break;
            case THIS_WEEK:
                cal1.set(Calendar.DAY_OF_WEEK, 1);
                cal2.set(Calendar.DAY_OF_WEEK, 7);
                break;
            case THIS_YEAR:
                cal1.set(Calendar.DAY_OF_YEAR, 1);
                cal2.set(Calendar.DAY_OF_YEAR, cal2.getActualMaximum(Calendar.DAY_OF_YEAR));
                break;
            case TOMORROW:
                cal1.add(Calendar.DATE, 1);
                cal2.add(Calendar.DATE, 1);
                break;
            case YESTERDAY:
                cal1.add(Calendar.DATE, -1);
                cal2.add(Calendar.DATE, -1);
                break;
            case TODAY:
            case NOW:
            default:
                break;
        }

        return new DateRange(CalendarUtils.getMidnightDate(cal1.getTime()),
                CalendarUtils.getLastSecondDate(cal2.getTime()));
    }

    public static Date getDateFromNow(Date now, String typeVal) throws UnifyException {
        LingualDateType lingualType = DataUtils.convert(LingualDateType.class, typeVal);
        return LingualDateUtils.getDateFromNow(now, lingualType);
    }
    
    public static Date getDateFromNow(Date now, LingualDateType type) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(now);
        switch (type) {
            case LAST_15_DAYS:
                cal1.add(Calendar.DAY_OF_YEAR, -15);
                break;
            case LAST_30_DAYS:
                cal1.add(Calendar.DAY_OF_YEAR, -30);
                break;
            case LAST_45_DAYS:
                cal1.add(Calendar.DAY_OF_YEAR, -45);
                break;
            case LAST_60_DAYS:
                cal1.add(Calendar.DAY_OF_YEAR, -60);
                break;
            case LAST_90_DAYS:
                cal1.add(Calendar.DAY_OF_YEAR, -90);
                break;
            case LAST_12MONTHS:
                cal1.add(Calendar.MONTH, -12);
                break;
            case LAST_3MONTHS:
                cal1.add(Calendar.MONTH, -3);
                break;
            case LAST_6MONTHS:
                cal1.add(Calendar.MONTH, -6);
                break;
            case LAST_9MONTHS:
                cal1.add(Calendar.MONTH, -9);
                break;
            case LAST_MONTH:
                cal1.add(Calendar.MONTH, -1);
                cal1.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case LAST_WEEK:
                cal1.add(Calendar.DATE, -7);
                cal1.set(Calendar.DAY_OF_WEEK, 1);
                break;
            case LAST_YEAR:
                cal1.add(Calendar.YEAR, -1);
                cal1.set(Calendar.DAY_OF_YEAR, 1);
                break;
            case NEXT_15_DAYS:
                cal1.add(Calendar.DAY_OF_YEAR, 15);
                break;
            case NEXT_30_DAYS:
                cal1.add(Calendar.DAY_OF_YEAR, 30);
                break;
            case NEXT_45_DAYS:
                cal1.add(Calendar.DAY_OF_YEAR, 45);
                break;
            case NEXT_60_DAYS:
                cal1.add(Calendar.DAY_OF_YEAR, 60);
                break;
            case NEXT_90_DAYS:
                cal1.add(Calendar.DAY_OF_YEAR, 90);
                break;
            case NEXT_12MONTHS:
                cal1.add(Calendar.MONTH, 12);
                break;
            case NEXT_3MONTHS:
                cal1.add(Calendar.MONTH, 3);
                break;
            case NEXT_6MONTHS:
                cal1.add(Calendar.MONTH, 6);
                break;
            case NEXT_9MONTHS:
                cal1.add(Calendar.MONTH, 9);
                break;
            case NEXT_MONTH:
                cal1.add(Calendar.MONTH, 1);
                cal1.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case NEXT_WEEK:
                cal1.add(Calendar.DATE, 7);
                cal1.set(Calendar.DAY_OF_WEEK, 1);
                break;
            case NEXT_YEAR:
                cal1.add(Calendar.YEAR, 1);
                cal1.set(Calendar.DAY_OF_YEAR, 1);
                break;
            case THIS_MONTH:
                cal1.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case THIS_WEEK:
                cal1.set(Calendar.DAY_OF_WEEK, 1);
                break;
            case THIS_YEAR:
                cal1.set(Calendar.DAY_OF_YEAR, 1);
                break;
            case TOMORROW:
                cal1.add(Calendar.DATE, 1);
                break;
            case YESTERDAY:
                cal1.add(Calendar.DATE, -1);
                break;
            case NOW:
                return now;
            case TODAY:
            default:
                break;
        }

        return CalendarUtils.getMidnightDate(cal1.getTime());
    }
}

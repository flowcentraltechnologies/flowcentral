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

package com.flowcentraltech.flowcentral.chart.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.chart.data.AbstractSeries;
import com.flowcentraltech.flowcentral.chart.data.ChartDef;
import com.flowcentraltech.flowcentral.chart.data.ChartDetails;
import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartTimeSeriesType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Aggregation;
import com.tcdng.unify.core.database.GroupingAggregation;
import com.tcdng.unify.core.database.GroupingAggregation.Grouping;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.core.util.json.JsonWriter;

/**
 * Chart utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class ChartUtils {

    private static final BigDecimal QUINTILION = BigDecimal.valueOf(1000000000000000L);

    private static final BigDecimal TRILLION = BigDecimal.valueOf(1000000000000L);

    private static final BigDecimal BILLION = BigDecimal.valueOf(1000000000L);

    private static final BigDecimal MILLION = BigDecimal.valueOf(1000000);

    private ChartUtils() {

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

    public static JsonWriter getOptionsJsonWriter(ChartDef chartDef, ChartDetails chartDetails, boolean sparkLine,
            int preferredHeight) throws UnifyException {
        JsonWriter jw = new JsonWriter();
        jw.beginObject();
        final ChartType chartType = chartDef.getType();
        final boolean isDynamicCategories = chartDetails.isDynamicCategories();
        final Map<String, AbstractSeries<?, ?>> series = isDynamicCategories ? chartDetails.getGroupingSeries(chartDef)
                : (chartDetails.getSeries(chartDetails.isWithSeriesInclusion() ? chartDetails.getSeriesInclusion()
                        : chartDef.getSeriesInclusion()));
        final ChartCategoryDataType categoryType = chartDetails.getCategoryType();

        // Title
        String title = !StringUtils.isBlank(chartDetails.getTitle()) ? chartDetails.getTitle() : chartDef.getTitle();
        if (!StringUtils.isBlank(title)) {
            jw.beginObject("title");
            jw.write("text", title);
            if (chartDetails.getTitleOffsetX() > 0) {
                jw.write("offsetX", chartDetails.getTitleOffsetX());
            }

            if (chartDetails.getTitleFontSize() > 0) {
                jw.beginObject("style");
                jw.write("fontSize", chartDetails.getTitleFontSize() + "px");
                jw.endObject();
            }

            jw.endObject();
        }

        // Sub-title
        String subTitle = !StringUtils.isBlank(chartDetails.getSubTitle()) ? chartDetails.getSubTitle()
                : chartDef.getSubTitle();
        if (!StringUtils.isBlank(subTitle)) {
            jw.beginObject("subtitle");
            jw.write("text", subTitle);
            if (chartDetails.getSubTitleOffsetX() > 0) {
                jw.write("offsetX", chartDetails.getSubTitleOffsetX());
            }

            jw.beginObject("style");
            jw.write("color", "#a0a0a0");
            if (chartDetails.getSubTitleFontSize() > 0) {
                jw.write("fontSize", chartDetails.getSubTitleFontSize() + "px");
            }
            jw.endObject();
            jw.endObject();
        }

        // Chart
        jw.beginObject("chart");
        if (chartDef.getWidth() > 0) {
            jw.write("width", chartDef.getWidth());
        }

        int _preferredHeight = chartDef.getHeight() > 0 ? chartDef.getHeight() : preferredHeight;
        if (_preferredHeight > 0) {
            jw.write("height", _preferredHeight);
        }

        jw.write("type", chartType.optionsType());
        jw.write("stacked", chartDef.isStacked());
        jw.beginObject("toolbar");
        jw.write("show", false);
        jw.endObject();

        jw.beginObject("sparkline");
        jw.write("enabled", sparkLine);
        jw.endObject();

        jw.endObject();

        // Grid
        jw.beginObject("grid");
        jw.write("show", chartDef.isShowGrid());
        jw.endObject();

        // Stroke
        jw.beginObject("stroke");
        if (chartDef.isSmooth()) {
            jw.write("curve", "smooth");
        } else {
            jw.write("curve", "straight");
        }
        jw.write("width", 1.5);
        jw.endObject();

        // Data labels
        jw.beginObject("dataLabels");
        jw.write("enabled", chartDef.isShowDataLabels());
        jw.write("_dformatter", chartDef.isFormatDataLabels());
        jw.endObject();

        // Theme
        jw.beginObject("theme");
        jw.write("mode", "light");
        if (chartDef.getPaletteType().monochrome()) {
            jw.beginObject("monochrome");
            jw.write("enabled", true);
            jw.write("color", chartDef.isWithColor() ? chartDef.getColor() : "#606060");
            jw.write("shadeTo", "light");
            jw.write("shadeIntensity", 0.65);
            jw.endObject();
        } else {
            jw.write("palette", chartDef.getPaletteType().optionsType());
        }
        jw.endObject();

        // Options
        if (chartType.plotOptions()) {
            jw.beginObject("plotOptions");
            if (chartType.axisChart()) {
                jw.beginObject("bar");
                jw.write("horizontal", chartType.isBar());
                jw.endObject();
            } else {
                jw.beginObject("pie");
                jw.write("customScale", 0.8);
                if (ChartType.DONUT.equals(chartType)) {
                    jw.beginObject("donut");
                    jw.write("size", "65%");
                    jw.endObject();
                }
                jw.endObject();
            }

            jw.endObject();
        }

        final Set<String> categoryInclusion = isDynamicCategories ? Collections.emptySet()
                : (chartDetails.isWithCategoryInclusion() ? chartDetails.getCategoryInclusion()
                        : chartDef.getCategoryInclusion());
        List<AbstractSeries<?, ?>> actseries = new ArrayList<AbstractSeries<?, ?>>(series.values());
        if (chartType.axisChart()) {
            // Series
            boolean integers = true;
            jw.beginArray("series");
            for (AbstractSeries<?, ?> _series : actseries) {
                _series.setCategoryInclusion(categoryInclusion);
                _series.writeAsObject(jw);
                integers &= _series.getDataType().isInteger();
            }
            jw.endArray();

            if (chartType.isColumn()) {
                // Y-axis
                jw.write("_yintegers", integers);
                jw.write("_yformatter", chartDef.isFormatYLabels());
                jw.beginObject("yaxis");
                jw.beginObject("labels");
                jw.endObject();
                jw.endObject();

                // X-axis
                jw.beginObject("xaxis");
                jw.write("type", categoryType.optionsType());
                jw.endObject();
            }
        } else {
            AbstractSeries<?, ?> pseries = actseries.get(0);
            pseries.setCategoryInclusion(categoryInclusion);

            // Series
            pseries.writeYValuesArray("series", jw);

            // Labels
            pseries.writeXValuesArray("labels", jw);

            // Legend
            jw.beginObject("legend");
            jw.write("position", "left");
            jw.write("offsetY", 60);
            jw.endObject();
        }

        jw.endObject();
        return jw;
    }

    public static String getOptionsJson(ChartDef chartDef, ChartDetails chartDetails, boolean sparkLine,
            int preferredHeight) throws UnifyException {
        return ChartUtils.getOptionsJsonWriter(chartDef, chartDetails, sparkLine, preferredHeight).toString();
    }

    public static List<GroupingAggregation> fill(EntityDef entityDef, List<GroupingAggregation> gaggregations,
            ChartTimeSeriesType timeSeriesType) throws UnifyException {
        if (!DataUtils.isBlank(gaggregations)) {
            Filler filler = null;
            switch (timeSeriesType) {
                case DAY:
                    break;
                case DAY_OVER_MONTH:
                    filler = new DayOverMonthFiller();
                    break;
                case DAY_OVER_MONTH_MERGED:
                    filler = DayOverMonthMergeFiller.DAY_OVER_MONTH_MERGE_FILLER;
                    break;
                case DAY_OVER_WEEK:
                    filler = new DayOverWeekFiller();
                    break;
                case DAY_OVER_WEEK_MERGED:
                    filler = DayOverWeekMergeFiller.DAY_OVER_WEEK_MERGE_FILLER;
                    break;
                case DAY_OVER_YEAR:
                    break;
                case DAY_OVER_YEAR_MERGED:
                    filler = DayOverYearMergeFiller.DAY_OVER_YEAR_MERGE_FILLER;
                    break;
                case HOUR:
                    break;
                case HOUR_OVER_DAY:
                    filler = new HourOverDayFiller();
                    break;
                case HOUR_OVER_DAY_MERGED:
                    filler = HourOverDayMergeFiller.HOUR_OVER_DAY_MERGE_FILLER;
                    break;
                case MONTH:
                    break;
                case MONTH_MERGED:
                    filler = MonthMergeFiller.MONTH_MERGE_FILLER;
                    break;
                case WEEK:
                    break;
                case WEEK_MERGED:
                    filler = WeekMergeFiller.WEEK_MERGE_FILLER;
                    break;
                case YEAR:
                    break;
                case YEAR_MERGED:
                    break;
                default:
                    break;
            }

            if (filler != null) {
                return filler.fill(entityDef, gaggregations);
            }
        }

        return gaggregations;
    }

    private static interface Filler {

        List<GroupingAggregation> fill(EntityDef entityDef, List<GroupingAggregation> gaggregations)
                throws UnifyException;
    }

    private static class WeekMergeFiller extends AbstractMergeFiller {

        public static WeekMergeFiller WEEK_MERGE_FILLER = new WeekMergeFiller();

        private WeekMergeFiller() {
            super(1, 54, 53, 2);
        }
    }

    private static class MonthMergeFiller extends AbstractMergeFiller {

        public static MonthMergeFiller MONTH_MERGE_FILLER = new MonthMergeFiller();

        private MonthMergeFiller() {
            super(1, 12, 12, 2);
        }
    }

    private static class DayOverYearMergeFiller extends AbstractMergeFiller {

        public static DayOverYearMergeFiller DAY_OVER_YEAR_MERGE_FILLER = new DayOverYearMergeFiller();

        private DayOverYearMergeFiller() {
            super(1, 366, 365, 3);
        }
    }

    private static class DayOverWeekMergeFiller extends AbstractMergeFiller {

        public static DayOverWeekMergeFiller DAY_OVER_WEEK_MERGE_FILLER = new DayOverWeekMergeFiller();

        private DayOverWeekMergeFiller() {
            super(1, 7, 7, 1);
        }
    }

    private static class DayOverMonthMergeFiller extends AbstractMergeFiller {

        public static DayOverMonthMergeFiller DAY_OVER_MONTH_MERGE_FILLER = new DayOverMonthMergeFiller();

        private DayOverMonthMergeFiller() {
            super(1, 31, 28, 2);
        }
    }

    private static class HourOverDayMergeFiller extends AbstractMergeFiller {

        public static HourOverDayMergeFiller HOUR_OVER_DAY_MERGE_FILLER = new HourOverDayMergeFiller();

        private HourOverDayMergeFiller() {
            super(0, 23, 23, 2);
        }
    }

    private static class HourOverDayFiller extends AbstractSpanFiller {

        public HourOverDayFiller() {
            super(new int[] { Calendar.HOUR_OF_DAY }, new int[] { 0 }, Calendar.DAY_OF_YEAR, Calendar.HOUR_OF_DAY, 0,
                    23);
        }
    }

    private static class DayOverWeekFiller extends AbstractSpanFiller {

        public DayOverWeekFiller() {
            super(new int[] { Calendar.DAY_OF_WEEK, Calendar.HOUR_OF_DAY }, new int[] { 1, 0 }, Calendar.WEEK_OF_YEAR,
                    Calendar.DAY_OF_WEEK, 1, 7);
        }
    }

    private static class DayOverMonthFiller extends AbstractSpanFiller {

        public DayOverMonthFiller() {
            super(new int[] { Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY }, new int[] { 1, 0 }, Calendar.MONTH,
                    Calendar.DAY_OF_MONTH, 1, 31);
        }
    }

    private static abstract class AbstractMergeFiller implements Filler {

        private final List<String> range;

        private final int minend;

        private final int padding;

        public AbstractMergeFiller(int start, int end, int minend, int padding) {
            List<String> _range = new ArrayList<String>();
            for (int i = start; i <= end; i++) {
                _range.add(String.format("%0" + padding + "d", i));
            }

            this.minend = minend;
            this.padding = padding;
            this.range = Collections.unmodifiableList(_range);
        }

        @Override
        public List<GroupingAggregation> fill(EntityDef entityDef, List<GroupingAggregation> gaggregations)
                throws UnifyException {
            List<GroupingAggregation> _gaggregations = new ArrayList<GroupingAggregation>();
            Map<String, GroupingAggregation> inputs = new HashMap<String, GroupingAggregation>();
            int _minend = minend;
            for (GroupingAggregation gaggregation : gaggregations) {
                String cat = gaggregation.getGroupingAsString(0);
                inputs.put(cat, gaggregation);

                int _cat = Integer.parseInt(cat);
                if (_minend < _cat) {
                    _minend = _cat;
                }
            }

            String _mcat = String.format("%0" + padding + "d", _minend);
            List<Aggregation> fillerAggregation = ChartUtils.getFillerAggregation(entityDef, gaggregations);
            for (String cat : range) {
                GroupingAggregation gaggregation = inputs.get(cat);
                if (gaggregation == null) {
                    gaggregation = new GroupingAggregation(Arrays.asList(new Grouping(cat)), fillerAggregation);
                }

                _gaggregations.add(gaggregation);
                if (_mcat.equals(cat)) {
                    break;
                }
            }

            return _gaggregations;
        }

    }

    private static abstract class AbstractSpanFiller implements Filler {

        private final Calendar cal = Calendar.getInstance();

        private final int[] clearFields;

        private final int[] clearVals;

        private final int nextField;

        private final int valueField;

        private final int initialVal;

        private final int endVal;

        public AbstractSpanFiller(int[] clearFields, int[] clearVals, int nextField, int valueField, int initialVal,
                int endVal) {
            this.clearFields = clearFields;
            this.clearVals = clearVals;
            this.nextField = nextField;
            this.valueField = valueField;
            this.initialVal = initialVal;
            this.endVal = endVal;
        }

        @Override
        public List<GroupingAggregation> fill(EntityDef entityDef, List<GroupingAggregation> gaggregations)
                throws UnifyException {
            List<GroupingAggregation> _gaggregations = new ArrayList<GroupingAggregation>();
            Map<String, FillerGrouping> bases = new LinkedHashMap<String, FillerGrouping>();
            Map<String, GroupingAggregation> inputs = new HashMap<String, GroupingAggregation>();
            for (GroupingAggregation gaggregation : gaggregations) {
                FillerGrouping grouping = grouping(gaggregation);
                bases.put(grouping.getBase(), grouping);
                inputs.put(grouping.getActual(), gaggregation);
            }

            List<Aggregation> fillerAggregation = ChartUtils.getFillerAggregation(entityDef, gaggregations);
            Date workingDate = clear(gaggregations.get(0).getGroupingAsDate(0));
            Date endDate = clear(gaggregations.get(gaggregations.size() - 1).getGroupingAsDate(0));
            while (workingDate.compareTo(endDate) <= 0) {
                for (Map.Entry<String, FillerGrouping> entry : bases.entrySet()) {
                    for (int val = initialVal; val <= endVal; val++) {
                        Date valDate = value(workingDate, val);
                        String grouping = grouping(valDate, entry.getValue().getGroupings());
                        GroupingAggregation gaggregation = inputs.get(grouping);
                        if (gaggregation == null) {
                            List<Grouping> groupings = new ArrayList<Grouping>(entry.getValue().getGroupings());
                            groupings.set(0, new Grouping(valDate));
                            gaggregation = new GroupingAggregation(groupings, fillerAggregation);
                        }

                        _gaggregations.add(gaggregation);
                    }
                }

                workingDate = next(workingDate);
            }

            return _gaggregations;
        }

        private FillerGrouping grouping(GroupingAggregation gaggregation) {
            List<Grouping> groupings = gaggregation.getGroupings();
            return new FillerGrouping(grouping(clear(gaggregation.getGroupingAsDate(0)), groupings),
                    grouping(gaggregation.getGroupingAsDate(0), groupings), groupings);
        }

        private String grouping(Date date, List<Grouping> groupings) {
            StringBuilder sb = new StringBuilder();
            sb.append(date.getTime());
            final int len = groupings.size();
            for (int i = 1; i < len; i++) {
                sb.append(groupings.get(i).getAsString());
            }

            return sb.toString();
        }

        private Date clear(Date date) {
            cal.setTime(date);
            for (int i = 0; i < clearFields.length; i++) {
                cal.set(clearFields[i], clearVals[i]);
            }

            return cal.getTime();
        }

        private Date next(Date date) {
            cal.setTime(date);
            cal.add(nextField, 1);
            return cal.getTime();
        }

        private Date value(Date date, int val) {
            cal.setTime(date);
            cal.add(valueField, val);
            return cal.getTime();
        }

        private class FillerGrouping {

            private final String base;

            private final String actual;

            private final List<Grouping> groupings;

            public FillerGrouping(String base, String actual, List<Grouping> groupings) {
                this.base = base;
                this.actual = actual;
                this.groupings = groupings;
            }

            public String getBase() {
                return base;
            }

            public String getActual() {
                return actual;
            }

            public List<Grouping> getGroupings() {
                return groupings;
            }

        }
    }

    private static List<Aggregation> getFillerAggregation(EntityDef entityDef, List<GroupingAggregation> gaggregations)
            throws UnifyException {
        List<Aggregation> fillerAggregation = new ArrayList<Aggregation>();
        for (Aggregation aggregation : gaggregations.get(0).getAggregation()) {
            EntityFieldDef entityFieldDef = entityDef.getFieldDef(aggregation.getFieldName());
            fillerAggregation.add(new Aggregation(aggregation.getType(), aggregation.getFieldName(),
                    DataUtils.convert(entityFieldDef.getDataType().dataType().javaClass(), 0)));
        }

        return fillerAggregation;
    }
}

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

package com.flowcentraltech.flowcentral.chart.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.FieldSequenceDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.PropertySequenceDef;
import com.flowcentraltech.flowcentral.configuration.constants.ChartDataSourceType;
import com.flowcentraltech.flowcentral.configuration.constants.ChartTimeSeriesType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Chart snapshot definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ChartDataSourceDef {

    private final ChartDataSourceType type;

    private final ChartTimeSeriesType timeSeriesType;

    private final String longName;

    private final String description;

    private final String categoryField;

    private final EntityDef entityDef;

    private final FilterDef categoryBase;

    private final PropertySequenceDef series;

    private final PropertySequenceDef categories;

    private final Integer limit;

    private final Long id;

    private final long version;

    private FieldSequenceDef groupingFieldSequenceDef;

    private List<String> groupingFieldNames;
    
    public ChartDataSourceDef(ChartDataSourceType type, ChartTimeSeriesType timeSeriesType, String longName,
            String description, String categoryField, EntityDef entityDef, FilterDef categoryBase,
            PropertySequenceDef series, PropertySequenceDef categories, FieldSequenceDef groupingFieldSequenceDef,
            Integer limit, Long id, long version) {
        this.type = type;
        this.timeSeriesType = timeSeriesType;
        this.longName = longName;
        this.description = description;
        this.categoryField = categoryField;
        this.entityDef = entityDef;
        this.categoryBase = categoryBase;
        this.series = series;
        this.categories = categories;
        this.groupingFieldSequenceDef = groupingFieldSequenceDef;
        this.limit = limit;
        this.id = id;
        this.version = version;
    }

    public ChartDataSourceType getType() {
        return type;
    }

    public ChartTimeSeriesType getTimeSeriesType() {
        return timeSeriesType;
    }

    public String getCategoryField() {
        return categoryField;
    }

    public EntityFieldDef getCategoryEntityFieldDef() throws UnifyException {
        return !StringUtils.isBlank(categoryField) ? entityDef.getFieldDef(categoryField) : null;
    }

    public String getLongName() {
        return longName;
    }

    public String getDescription() {
        return description;
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public FilterDef getCategoryBase() {
        return categoryBase;
    }

    public PropertySequenceDef getSeries() {
        return series;
    }

    public PropertySequenceDef getCategories() {
        return categories;
    }

    public Integer getLimit() {
        return limit;
    }

    public Long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    public boolean isWithSeries() {
        return series != null;
    }

    public boolean isWithCategories() {
        return categories != null;
    }

    public List<String> getGroupingFieldNames() {
        if (groupingFieldNames == null) {
            synchronized(this) {
                if (groupingFieldNames == null) {
                    groupingFieldNames = new ArrayList<String>();
                    if (isTimeSeries()) {
                        groupingFieldNames.add(categoryField);
                    }
                    
                    if (isWithGroupingFields()) {
                        groupingFieldNames.addAll(groupingFieldSequenceDef.getFieldNames());
                    }
                    
                    groupingFieldNames = Collections.unmodifiableList(groupingFieldNames);
                }
            }
        }
        
        return groupingFieldNames;
    }

    public boolean isTimeSeries() {
        return timeSeriesType != null && categoryField != null && entityDef.getFieldDef(categoryField).isTime();
    }

    public boolean isWithGroupingFields() {
        return groupingFieldSequenceDef != null && !groupingFieldSequenceDef.isBlank();
    }
    
    public boolean isWithGroupingFieldsAndOrTimeSeries() {
        return isWithGroupingFields() || isTimeSeries();
    }
}

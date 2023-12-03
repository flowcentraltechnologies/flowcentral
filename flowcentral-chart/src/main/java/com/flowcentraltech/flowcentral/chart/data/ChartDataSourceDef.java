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

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.PropertySequenceDef;

/**
 * Chart snapshot definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ChartDataSourceDef {

    private String name;

    private String description;

    private final EntityDef entityDef;

    private final FilterDef categoryBase;

    private final PropertySequenceDef series;

    private final PropertySequenceDef categories;

    private Long id;

    private long version;

    public ChartDataSourceDef(String name, String description, EntityDef entityDef, FilterDef categoryBase,
            PropertySequenceDef series, PropertySequenceDef categories, Long id, long version) {
        this.name = name;
        this.description = description;
        this.entityDef = entityDef;
        this.categoryBase = categoryBase;
        this.series = series;
        this.categories = categories;
        this.id = id;
        this.version = version;
    }

    public String getName() {
        return name;
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

    public Long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }
}
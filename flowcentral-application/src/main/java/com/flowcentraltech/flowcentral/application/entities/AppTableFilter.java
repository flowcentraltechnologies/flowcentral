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
package com.flowcentraltech.flowcentral.application.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Child;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;

/**
 * Application table filter entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_TABLEFILTER", uniqueConstraints = { @UniqueConstraint({ "appTableId", "name" }),
        @UniqueConstraint({ "appTableId", "description" }) })
public class AppTableFilter extends BaseConfigNamedEntity {

    @ForeignKey(AppTable.class)
    private Long appTableId;
    
    @Column(length = 16, nullable = true)
    private String rowColor;
    
    @Column(length = 128, nullable = true)
    private String legendLabel;
    
    @Column(length = 64, nullable = true)
    private String filterGenerator;

    @Column(length = 64, nullable = true)
    private String filterGeneratorRule;
    
    @Child(category = "table")
    private AppFilter filter;

    public AppTableFilter(String name, String description, String definition) {
        this.setName(name);
        this.setDescription(description);
        this.filter = new AppFilter(definition);
    }

    public AppTableFilter() {
        
    }

    public Long getAppTableId() {
        return appTableId;
    }

    public void setAppTableId(Long appTableId) {
        this.appTableId = appTableId;
    }

    public String getRowColor() {
        return rowColor;
    }

    public void setRowColor(String rowColor) {
        this.rowColor = rowColor;
    }

    public String getLegendLabel() {
        return legendLabel;
    }

    public void setLegendLabel(String legendLabel) {
        this.legendLabel = legendLabel;
    }

    public String getFilterGenerator() {
        return filterGenerator;
    }

    public void setFilterGenerator(String filterGenerator) {
        this.filterGenerator = filterGenerator;
    }

    public String getFilterGeneratorRule() {
        return filterGeneratorRule;
    }

    public void setFilterGeneratorRule(String filterGeneratorRule) {
        this.filterGeneratorRule = filterGeneratorRule;
    }

    public AppFilter getFilter() {
        return filter;
    }

    public void setFilter(AppFilter filter) {
        this.filter = filter;
    }

}

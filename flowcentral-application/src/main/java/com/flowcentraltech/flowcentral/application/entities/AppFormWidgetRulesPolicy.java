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
import com.tcdng.unify.core.annotation.Child;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;

/**
 * Application form widget rules policy entity;
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_FORMWIDGETRULESPOLICY")
public class AppFormWidgetRulesPolicy extends BaseConfigNamedEntity {

    @ForeignKey(AppForm.class)
    private Long appFormId;

    @Column(length = 64, nullable = true)
    private Integer executionIndex;
    
    @Child(category = "formwidgetrulespolicy")
    private AppFilter onCondition;

    @Child(category = "formwidgetrulespolicy")
    private AppWidgetRules widgetRules;

    public Long getAppFormId() {
        return appFormId;
    }

    public void setAppFormId(Long appFormId) {
        this.appFormId = appFormId;
    }

    public AppFilter getOnCondition() {
        return onCondition;
    }

    public void setOnCondition(AppFilter onCondition) {
        this.onCondition = onCondition;
    }

    public AppWidgetRules getWidgetRules() {
        return widgetRules;
    }

    public void setWidgetRules(AppWidgetRules widgetRules) {
        this.widgetRules = widgetRules;
    }

    public Integer getExecutionIndex() {
        return executionIndex;
    }

    public void setExecutionIndex(Integer executionIndex) {
        this.executionIndex = executionIndex;
    }

}

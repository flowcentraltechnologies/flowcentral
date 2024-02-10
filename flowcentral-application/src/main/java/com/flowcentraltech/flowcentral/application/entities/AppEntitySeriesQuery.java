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
package com.flowcentraltech.flowcentral.application.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseConfigEntityQuery;

/**
 * Application entity series query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppEntitySeriesQuery extends BaseConfigEntityQuery<AppEntitySeries> {

    public AppEntitySeriesQuery() {
        super(AppEntitySeries.class);
    }

    public AppEntitySeriesQuery appEntityId(Long appEntityId) {
        return (AppEntitySeriesQuery) addEquals("appEntityId", appEntityId);
    }

    public AppEntitySeriesQuery appEntityName(String appEntityName) {
        return (AppEntitySeriesQuery) addEquals("appEntityName", appEntityName);
    }

    public AppEntitySeriesQuery name(String name) {
        return (AppEntitySeriesQuery) addEquals("name", name);
    }

    public AppEntitySeriesQuery applicationName(String applicationName) {
        return (AppEntitySeriesQuery) addEquals("applicationName", applicationName);
    }

    public AppEntitySeriesQuery applicationNameNot(String applicationName) {
        return (AppEntitySeriesQuery) addNotEquals("applicationName", applicationName);
    }

}

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
package com.flowcentraltech.flowcentral.application.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntityQuery;

/**
 * Application filter query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppFilterQuery extends BaseAuditEntityQuery<AppFilter> {

    public AppFilterQuery() {
        super(AppFilter.class);
    }

    public AppFilterQuery entityInstId(Long entityInstId) {
        return (AppFilterQuery) addEquals("entityInstId", entityInstId);
    }

    public AppFilterQuery entity(String entity) {
        return (AppFilterQuery) addEquals("entity", entity);
    }

    public AppFilterQuery category(String category) {
        return (AppFilterQuery) addEquals("category", category);
    }

}

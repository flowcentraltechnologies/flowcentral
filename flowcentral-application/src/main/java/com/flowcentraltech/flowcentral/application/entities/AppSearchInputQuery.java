/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
 * Application search input query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class AppSearchInputQuery extends BaseAuditEntityQuery<AppSearchInput> {

    public AppSearchInputQuery() {
        super(AppSearchInput.class);
    }

    public AppSearchInputQuery entityInstId(Long entityInstId) {
        return (AppSearchInputQuery) addEquals("entityInstId", entityInstId);
    }

    public AppSearchInputQuery entity(String entity) {
        return (AppSearchInputQuery) addEquals("entity", entity);
    }

    public AppSearchInputQuery category(String category) {
        return (AppSearchInputQuery) addEquals("category", category);
    }

}

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

/**
 * Application table query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppTableQuery extends BaseApplicationEntityQuery<AppTable> {

    public AppTableQuery() {
        super(AppTable.class);
    }

    public AppTableQuery entity(String entity) {
        return (AppTableQuery) addEquals("entity", entity);
    }

    public AppTableQuery applicationNameNot(String applicationName) {
        return (AppTableQuery) addNotEquals("applicationName", applicationName);
    }

    public AppTableQuery entityBeginsWith(String entity) {
        return (AppTableQuery) addBeginsWith("entity", entity);
    }

}

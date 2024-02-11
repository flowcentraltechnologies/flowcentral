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

import com.flowcentraltech.flowcentral.configuration.constants.FormType;

/**
 * Application form query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppFormQuery extends BaseApplicationEntityQuery<AppForm> {

    public AppFormQuery() {
        super(AppForm.class);
    }

    public AppFormQuery entity(String entity) {
        return (AppFormQuery) addEquals("entity", entity);
    }

    public AppFormQuery type(FormType type) {
        return (AppFormQuery) addEquals("type", type);
    }

    public AppFormQuery entityBeginsWith(String entity) {
        return (AppFormQuery) addBeginsWith("entity", entity);
    }

}

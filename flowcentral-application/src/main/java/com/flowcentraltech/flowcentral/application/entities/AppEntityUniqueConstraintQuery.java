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

import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntityQuery;

/**
 * Application entity unique constraint query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppEntityUniqueConstraintQuery extends BaseConfigNamedEntityQuery<AppEntityUniqueConstraint> {

    public AppEntityUniqueConstraintQuery() {
        super(AppEntityUniqueConstraint.class);
    }

    public AppEntityUniqueConstraintQuery appEntityId(Long appEntityId) {
        return (AppEntityUniqueConstraintQuery) addEquals("appEntityId", appEntityId);
    }

    public AppEntityUniqueConstraintQuery applicationName(String applicationName) {
        return (AppEntityUniqueConstraintQuery) addEquals("applicationName", applicationName);
    }

    public AppEntityUniqueConstraintQuery entityName(String entityName) {
        return (AppEntityUniqueConstraintQuery) addEquals("entityName", entityName);
    }


}

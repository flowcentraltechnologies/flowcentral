/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.integration.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntityQuery;

/**
 * Query object for read configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ReadConfigQuery extends BaseStatusEntityQuery<ReadConfig> {

    public ReadConfigQuery() {
        super(ReadConfig.class);
    }

    public ReadConfigQuery name(String name) {
        return (ReadConfigQuery) addEquals("name", name);
    }

    public ReadConfigQuery descriptionLike(String description) {
        return (ReadConfigQuery) addLike("description", description);
    }
}

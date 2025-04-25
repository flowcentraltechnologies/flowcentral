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
package com.flowcentraltech.flowcentral.organization.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntityQuery;

/**
 * Query class for zones.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ZoneQuery extends BaseStatusEntityQuery<Zone> {

    public ZoneQuery() {
        super(Zone.class);
    }

    public ZoneQuery code(String code) {
        return (ZoneQuery) addEquals("code", code);
    }

    public ZoneQuery codeLike(String code) {
        return (ZoneQuery) addLike("code", code);
    }

    public ZoneQuery descriptionLike(String description) {
        return (ZoneQuery) addLike("description", description);
    }
}

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
package com.flowcentraltech.flowcentral.collaboration.entities;

import com.flowcentraltech.flowcentral.common.constants.CollaborationType;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntityQuery;

/**
 * Query class for collaboration freeze.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class CollaborationFreezeQuery extends BaseAuditEntityQuery<CollaborationFreeze> {

    public CollaborationFreezeQuery() {
        super(CollaborationFreeze.class);
    }

    public CollaborationFreezeQuery type(CollaborationType type) {
        return (CollaborationFreezeQuery) addEquals("type", type);
    }

    public CollaborationFreezeQuery applicationName(String applicationName) {
        return (CollaborationFreezeQuery) addEquals("applicationName", applicationName);
    }

    public CollaborationFreezeQuery resourceName(String resourceName) {
        return (CollaborationFreezeQuery) addEquals("resourceName", resourceName);
    }

}

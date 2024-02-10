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
package com.flowcentraltech.flowcentral.collaboration.entities;

import com.flowcentraltech.flowcentral.common.constants.CollaborationType;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntityQuery;

/**
 * Query class for collaboration lock history.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class CollaborationLockHistoryQuery extends BaseAuditEntityQuery<CollaborationLockHistory> {

    public CollaborationLockHistoryQuery() {
        super(CollaborationLockHistory.class);
    }

    public CollaborationLockHistoryQuery type(CollaborationType type) {
        return (CollaborationLockHistoryQuery) addEquals("type", type);
    }

    public CollaborationLockHistoryQuery resourceName(String resourceName) {
        return (CollaborationLockHistoryQuery) addEquals("resourceName", resourceName);
    }
}

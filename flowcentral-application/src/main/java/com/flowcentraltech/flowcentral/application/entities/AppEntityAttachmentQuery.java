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

import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntityQuery;

/**
 * Application entity attachment query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppEntityAttachmentQuery extends BaseConfigNamedEntityQuery<AppEntityAttachment> {

    public AppEntityAttachmentQuery() {
        super(AppEntityAttachment.class);
    }

    public AppEntityAttachmentQuery appEntityId(Long appEntityId) {
        return (AppEntityAttachmentQuery) addEquals("appEntityId", appEntityId);
    }

}

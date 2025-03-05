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

package com.flowcentraltech.flowcentral.workflow.data;

import com.flowcentraltech.flowcentral.application.data.Attachments;
import com.flowcentraltech.flowcentral.application.data.Comments;
import com.flowcentraltech.flowcentral.application.data.EntityItem;
import com.flowcentraltech.flowcentral.application.data.Errors;
import com.flowcentraltech.flowcentral.application.web.widgets.InputArrayEntries;
import com.flowcentraltech.flowcentral.common.constants.MaintainType;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.common.database.WorkEntity;

/**
 * Work entity item
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WorkEntityItem extends EntityItem {

    public WorkEntityItem(Entity entity, InputArrayEntries emails, Comments comments, Attachments attachments,
            Errors errors) {
        super(MaintainType.WORK_ITEM, entity, emails, comments, attachments, errors, null, null);
    }

    public WorkEntityItem(Entity entity) {
        super(MaintainType.WORK_ITEM, entity, null, null);
    }

    protected WorkEntityItem(MaintainType type, Entity entity, InputArrayEntries emails, Comments comments,
            Attachments attachments, Errors errors) {
        super(type, entity, emails, comments, attachments, errors, null, null);
    }

    protected WorkEntityItem(MaintainType type, Entity entity) {
        super(type, entity, null, null);
    }

    public WorkEntity getWorkEntity() {
        return (WorkEntity) getEntity();
    }
}

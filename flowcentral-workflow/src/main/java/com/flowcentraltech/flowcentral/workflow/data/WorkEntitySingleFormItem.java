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

package com.flowcentraltech.flowcentral.workflow.data;

import com.flowcentraltech.flowcentral.application.data.Comments;
import com.flowcentraltech.flowcentral.application.data.Errors;
import com.flowcentraltech.flowcentral.application.web.widgets.InputArrayEntries;
import com.flowcentraltech.flowcentral.common.constants.MaintainType;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.tcdng.unify.core.database.Entity;

/**
 * Work entity single form item
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WorkEntitySingleFormItem extends WorkEntityItem {

    public WorkEntitySingleFormItem(Entity entity, InputArrayEntries emails, Comments comments, Errors errors) {
        super(MaintainType.WORK_ITEM_SINGLEFORM, entity, emails, comments, errors);
    }

    public WorkEntitySingleFormItem(Entity entity) {
        super(MaintainType.WORK_ITEM_SINGLEFORM, entity);
    }

    public WorkEntity getWorkEntity() {
        return (WorkEntity) getEntity();
    }
}

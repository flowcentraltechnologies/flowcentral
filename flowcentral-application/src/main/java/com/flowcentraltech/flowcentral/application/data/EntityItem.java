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

package com.flowcentraltech.flowcentral.application.data;

import com.flowcentraltech.flowcentral.application.web.widgets.InputArrayEntries;
import com.flowcentraltech.flowcentral.common.constants.MaintainType;
import com.tcdng.unify.core.database.Entity;

/**
 * Entity item.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityItem implements FormAppendables {

    private MaintainType maintainType;

    private Entity entity;

    private InputArrayEntries emails;

    private Comments comments;

    private Errors errors;

    protected EntityItem(MaintainType maintainType, Entity entity, InputArrayEntries emails, Comments comments,
            Errors errors) {
        this.maintainType = maintainType;
        this.entity = entity;
        this.emails = emails;
        this.comments = comments;
        this.errors = errors;
    }

    protected EntityItem(MaintainType maintainType, Entity entity) {
        this.maintainType = maintainType;
        this.entity = entity;
    }

    public MaintainType getMaintainType() {
        return maintainType;
    }

    public boolean isEdit() {
        return maintainType.isEdit();
    }

    public boolean isWorkItem() {
        return maintainType.isWorkItem();
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public InputArrayEntries getEmails() {
        return emails;
    }

    @Override
    public Comments getComments() {
        return comments;
    }

    @Override
    public Errors getErrors() {
        return errors;
    }
}

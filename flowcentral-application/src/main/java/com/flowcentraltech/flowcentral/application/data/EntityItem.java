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

import com.tcdng.unify.core.database.Entity;

/**
 * Entity item.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityItem {

    private Entity entity;
    
    private Comments comments;

    private Errors errors;
    
    public EntityItem(Entity entity, Comments comments, Errors errors) {
        this.entity = entity;
        this.comments = comments;
        this.errors = errors;
    }

    public EntityItem(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public Comments getComments() {
        return comments;
    }

    public Errors getErrors() {
        return errors;
    }
}

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

package com.flowcentraltech.flowcentral.application.constants;

/**
 * Workflow draft type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public enum WorkflowDraftType {
    ASSIGN_TO_CHILD_ITEM,
    CRUD_TO_CHILD_ITEM,
    EDIT_CHILD_ITEM,
    ENTRY_TO_CHILD_ITEM,
    NEW_CHILD_ITEM,
    NEW_CHILDLIST_ITEM,
    QUICK_TABLE_EDIT,
    QUICK_TABLE_ORDER,
    QUICK_FORM_EDIT,
    MAINTAIN,
    UPDATE,
    UPDATE_CLOSE,
    DELETE;
    
    public boolean isNew() {
        return NEW_CHILD_ITEM.equals(this) || NEW_CHILDLIST_ITEM.equals(this);
    }
    
    public boolean isUpdate() {
        return UPDATE.equals(this) || UPDATE_CLOSE.equals(this);
    }
    
    public boolean isDelete() {
        return DELETE.equals(this);
    }
}

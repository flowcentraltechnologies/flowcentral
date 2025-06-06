/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.common.constants;

/**
 * Table change type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public enum TableChangeType {
    ADD_ENTRY,
    DELETE_ENTRY,
    INSERT_ENTRIES,
    REPLACE_ENTRIES;
    
    public boolean isAddEntry() {
        return ADD_ENTRY.equals(this);
    }
    
    public boolean isDeleteEntry() {
        return DELETE_ENTRY.equals(this);
    }
    
    public boolean isInsertEntries() {
        return INSERT_ENTRIES.equals(this);
    }
    
    public boolean isReplaceEntries() {
        return REPLACE_ENTRIES.equals(this);
    }
}

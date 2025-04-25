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
 * Data change type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public enum DataChangeType {

    NEW("new", "$m{datachange.new}"),
    UPDATED("upd", "$m{datachange.updated}"),
    DELETED("del", "$m{datachange.deleted}"),
    NONE("non", "$m{datachange.none}");
    
    private final String shade;
    
    private final String hint;
    
    private DataChangeType(String shade, String hint) {
       this.shade = shade; 
       this.hint = hint;
    }

    public String shade() {
        return shade;
    }

    public String hint() {
        return hint;
    }

    public boolean isNone() {
        return NONE.equals(this);
    }
}

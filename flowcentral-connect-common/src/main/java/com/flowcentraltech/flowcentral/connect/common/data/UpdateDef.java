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

package com.flowcentraltech.flowcentral.connect.common.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Update definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class UpdateDef {

    private Map<String, Object> entries;

    private UpdateDef(Map<String, Object> entries) {
        this.entries = entries;
    }

    public Set<String> getFieldNames() {
        return entries.keySet();
    }
    
    public Object getUpdate(String fieldName) {
        return entries.get(fieldName);
    }
    
    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private Map<String, Object> entries;

        public Builder() {
            this.entries = new HashMap<String, Object>();
        }
        
        public Builder update(String fieldName, Object val) {
            entries.put(fieldName, val);
            return this;
        }

        public UpdateDef build() {
            return new UpdateDef(entries);
        }
    }
}

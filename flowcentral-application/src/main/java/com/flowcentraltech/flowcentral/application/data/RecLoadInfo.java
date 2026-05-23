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

package com.flowcentraltech.flowcentral.application.data;

import com.tcdng.unify.core.format.Formatter;

/**
 * Record load information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class RecLoadInfo {

    public static final RecLoadInfo NONUNIQUE_LISTONLY_PROPERTY = new RecLoadInfo();
    
    private String fieldName;

    private Object val;

    private Formatter<?> formatter;

    private boolean nonUniqueListOnlyProperty;
    
    public RecLoadInfo(String fieldName, Object val, Formatter<?> formatter) {
        this.fieldName = fieldName;
        this.val = val;
        this.formatter = formatter;
    }

    private RecLoadInfo() {
        this.nonUniqueListOnlyProperty = true;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getVal() {
        return val;
    }

    public Formatter<?> getFormatter() {
        return formatter;
    }

    public boolean isNonUniqueListOnlyProperty() {
        return nonUniqueListOnlyProperty;
    }
}

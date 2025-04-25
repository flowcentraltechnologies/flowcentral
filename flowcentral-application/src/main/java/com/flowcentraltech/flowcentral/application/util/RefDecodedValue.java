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

package com.flowcentraltech.flowcentral.application.util;

import com.tcdng.unify.core.util.StringUtils;

/**
 * Reference decoded value
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class RefDecodedValue {

    private int index;

    private String refLongName;

    private Object value;

    public RefDecodedValue(int index, String refLongName, Object value) {
        this.index = index;
        this.refLongName = refLongName;
        this.value = value;
    }

    public RefDecodedValue(Object value) {
        this.index = 0;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public String getRefLongName() {
        return refLongName;
    }

    public Object getValue() {
        return value;
    }
    
    public boolean isLongNameRef() {
        return !StringUtils.isBlank(refLongName);
    }
    
    public boolean isNullValue() {
        return value == null;
    }

    @Override
    public String toString() {
        return "RefDecodedValue [index=" + index + ", refLongName=" + refLongName + ", value=" + value + "]";
    }
}

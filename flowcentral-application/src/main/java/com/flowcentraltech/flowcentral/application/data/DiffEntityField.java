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

package com.flowcentraltech.flowcentral.application.data;

import com.flowcentraltech.flowcentral.application.constants.DataChangeType;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Difference entity field.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class DiffEntityField {

    private DataChangeType changeType;

    private String name;

    private String label;

    private String value;

    private boolean number;

    public DiffEntityField(DataChangeType changeType, String name, String label, String value, boolean number) {
        this.changeType = changeType;
        this.name = name;
        this.label = label;
        this.value = value;
        this.number = number;
    }

    public DataChangeType getChangeType() {
        return changeType;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public boolean isWithValue() {
        return value != null;
    }

    public boolean isNumber() {
        return number;
    }

    public String toString() {
        return StringUtils.toXmlString(this);
    }
}

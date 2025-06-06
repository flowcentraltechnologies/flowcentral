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

import java.util.List;

import com.tcdng.unify.core.util.StringUtils;

/**
 * Difference entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class DiffEntity {

    private String label;

    private List<DiffEntityField> fields;

    public DiffEntity(String label, List<DiffEntityField> fields) {
        this.label = label;
        this.fields = fields;
    }

    public String getLabel() {
        return label;
    }

    public List<DiffEntityField> getFields() {
        return fields;
    }

    public String toString() {
        return StringUtils.toXmlString(this);
    }

}

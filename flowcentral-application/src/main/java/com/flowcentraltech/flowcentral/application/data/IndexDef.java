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

import java.util.Collections;
import java.util.List;

/**
 * Index definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class IndexDef {

    private String name;

    private String description;

    private List<String> fieldList;

    public IndexDef(String name, String description, List<String> fieldList) {
        this.name = name;
        this.description = description;
        this.fieldList = Collections.unmodifiableList(fieldList);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getFieldList() {
        return fieldList;
    }

    @Override
    public String toString() {
        return "IndexDef [fieldList=" + fieldList + "]";
    }

}

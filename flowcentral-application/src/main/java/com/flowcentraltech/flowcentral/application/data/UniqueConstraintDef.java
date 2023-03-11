/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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
 * Unique constraint definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class UniqueConstraintDef {

    private String name;

    private String description;

    private List<String> fieldList;

    private boolean caseInsensitive;
    
    public UniqueConstraintDef(String name, String description, List<String> fieldList, boolean caseInsensitive) {
        this.name = name;
        this.description = description;
        this.fieldList = Collections.unmodifiableList(fieldList);
        this.caseInsensitive = caseInsensitive;
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

    public boolean isSingleFieldConstraint(String fieldName) {
        return fieldList != null && fieldList.size() == 1 && fieldList.get(0).equals(fieldName);
    }
    
    public boolean isCaseInsensitive() {
        return caseInsensitive;
    }

    @Override
    public String toString() {
        return "UniqueConstraintDef [name=" + name + ", description=" + description + ", fieldList=" + fieldList
                + ", caseInsensitive=" + caseInsensitive + "]";
    }

}

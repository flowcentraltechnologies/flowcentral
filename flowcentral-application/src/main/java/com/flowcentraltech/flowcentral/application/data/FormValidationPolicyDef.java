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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Form validation policy definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormValidationPolicyDef {

    private String name;

    private String description;

    private String errorMatcher;

    private FilterDef errorCondition;

    private Set<String> target;

    private String message;

    public FormValidationPolicyDef(FilterDef errorCondition, String name, String description, String message,
            String errorMatcher, List<String> targetList) {
        this.name = name;
        this.description = description;
        this.errorCondition = errorCondition;
        this.message = message;
        this.errorMatcher = errorMatcher;
        if (!DataUtils.isBlank(targetList)) {
            this.target = Collections.unmodifiableSet(new HashSet<String>(targetList));
        } else {
            this.target = Collections.emptySet();
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorMatcher() {
        return errorMatcher;
    }

    public FilterDef getErrorCondition() {
        return errorCondition;
    }

    public boolean isErrorMatcher() {
        return !StringUtils.isBlank(errorMatcher);
    }

    public boolean isErrorCondition() {
        return errorCondition != null;
    }

    public Set<String> getTarget() {
        return target;
    }
    
    public boolean isWithTarget() {
        return !target.isEmpty();
    }
}

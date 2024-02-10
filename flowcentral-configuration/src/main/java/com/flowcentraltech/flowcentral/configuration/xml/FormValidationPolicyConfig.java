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
package com.flowcentraltech.flowcentral.configuration.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Form validation policy configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormValidationPolicyConfig extends BaseNameConfig {

    private String message;

    private String target;

    private String errorMatcher;
    
    private boolean skippable;

    private FilterConfig errorCondition;

    public String getMessage() {
        return message;
    }

    @XmlAttribute(required = true)
    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMatcher() {
        return errorMatcher;
    }

    public String getTarget() {
        return target;
    }

    @XmlAttribute
    public void setTarget(String target) {
        this.target = target;
    }

    @XmlAttribute
    public void setErrorMatcher(String errorMatcher) {
        this.errorMatcher = errorMatcher;
    }

    public boolean isSkippable() {
        return skippable;
    }

    @XmlAttribute
    public void setSkippable(boolean skippable) {
        this.skippable = skippable;
    }

    public FilterConfig getErrorCondition() {
        return errorCondition;
    }

    @XmlElement(required = true)
    public void setErrorCondition(FilterConfig errorCondition) {
        this.errorCondition = errorCondition;
    }

}

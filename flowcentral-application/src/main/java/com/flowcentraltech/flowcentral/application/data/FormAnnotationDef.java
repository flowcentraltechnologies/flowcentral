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

package com.flowcentraltech.flowcentral.application.data;

import com.flowcentraltech.flowcentral.configuration.constants.FormAnnotationType;
import com.flowcentraltech.flowcentral.configuration.constants.VisibilityType;

/**
 * Form annotation definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormAnnotationDef {

    private FormAnnotationType type;

    private VisibilityType visibility;

    private FilterDef onCondition;

    private String name;

    private String description;

    private String message;

    private boolean html;

    private boolean directPlacement;

    public FormAnnotationDef(FormAnnotationType type, VisibilityType visibility, String name, String description,
            String message, boolean html, boolean directPlacement, FilterDef onCondition) {
        this.type = type;
        this.visibility = visibility == null ? VisibilityType.FIXED : visibility;
        this.name = name;
        this.description = description;
        this.message = message;
        this.html = html;
        this.directPlacement = directPlacement;
        this.onCondition = onCondition;
    }

    public FormAnnotationType getType() {
        return type;
    }

    public VisibilityType getVisibility() {
        return visibility;
    }

    public boolean isFixed() {
        return visibility.isFixed();
    }

    public boolean isDisappearing() {
        return visibility.isDisappearing();
    }

    public boolean isClosable() {
        return visibility.isClosable();
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

    public boolean isHtml() {
        return html;
    }

    public boolean isDirectPlacement() {
        return directPlacement;
    }

    public FilterDef getOnCondition() {
        return onCondition;
    }

    public boolean isWithCondition() {
        return onCondition != null;
    }

}

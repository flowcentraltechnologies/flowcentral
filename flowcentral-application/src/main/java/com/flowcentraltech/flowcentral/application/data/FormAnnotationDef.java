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

import com.flowcentraltech.flowcentral.configuration.constants.FormAnnotationType;

/**
 * Form annotation definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormAnnotationDef {

    private FormAnnotationType type;

    private String name;

    private String description;

    private String message;

    private boolean html;

    public FormAnnotationDef(FormAnnotationType type, String name, String description, String message, boolean html) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.message = message;
        this.html = html;
    }

    public FormAnnotationType getType() {
        return type;
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

    @Override
    public String toString() {
        return "FormAnnotationDef [type=" + type + ", name=" + name + ", description=" + description + ", message="
                + message + ", html=" + html + "]";
    }
}

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
package com.flowcentraltech.flowcentral.application.data.portal;

/**
 * Portal report parameter object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class PortalReportParam {

    private String type;
    
    private String name;
    
    private String description;
    
    private String label;
    
    private String defaultVal;
    
    private String editor;

    private boolean mandatory;

    public PortalReportParam(String type, String name, String description, String label, String defaultVal,
            String editor, boolean mandatory) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.label = label;
        this.defaultVal = defaultVal;
        this.editor = editor;
        this.mandatory = mandatory;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLabel() {
        return label;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public String getEditor() {
        return editor;
    }

    public boolean isMandatory() {
        return mandatory;
    }
    
}

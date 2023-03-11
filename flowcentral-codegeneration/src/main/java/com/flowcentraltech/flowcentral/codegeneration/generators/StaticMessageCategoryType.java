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

package com.flowcentraltech.flowcentral.codegeneration.generators;

/**
 * Static message category type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public enum StaticMessageCategoryType {
    HEADER("Header"),
    APPLET("Applet"),
    CHART("Chart"),
    DASHBOARD("Dashboard"),
    NOTIFICATION("Notification"),
    REPORT("Report"),
    WORKFLOW("Workflow"),
    WIDGET("Widget"),
    SUGGESTION("Suggestion"),
    REF("Reference"),
    ENTITY("Entity"),
    TABLE("Table"),
    FORM("Form"),
    PROPERTY_LIST("Property List"),
    PROPERTY_RULE("Property Rule"),
    ASSIGNMENT_PAGE("Assignment Page");
    
    private final String comment;
    
    private StaticMessageCategoryType(String comment) {
        this.comment = comment;
    }

    public String comment() {
        return comment;
    }
       
}

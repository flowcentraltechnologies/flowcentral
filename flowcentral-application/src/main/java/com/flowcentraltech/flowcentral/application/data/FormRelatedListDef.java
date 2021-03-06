/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

/**
 * Form related list definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormRelatedListDef {

    private String name;

    private String description;

    private String label;

    private String applet;

    private String filter;

    private String editAction;

    public FormRelatedListDef(String name, String description, String label, String applet, String filter,
            String editAction) {
        this.description = description;
        this.name = name;
        this.label = label;
        this.applet = applet;
        this.filter = filter;
        this.editAction = editAction;
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

    public String getApplet() {
        return applet;
    }

    public String getFilter() {
        return filter;
    }

    public String getEditAction() {
        return editAction;
    }

    @Override
    public String toString() {
        return "FormRelatedListDef [name=" + name + ", description=" + description + ", label=" + label + ", applet="
                + applet + ", editAction=" + editAction + "]";
    }

}

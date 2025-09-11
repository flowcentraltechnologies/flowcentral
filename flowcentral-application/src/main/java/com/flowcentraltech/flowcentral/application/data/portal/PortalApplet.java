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
 * Portal applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class PortalApplet {

    private String type;

    private String name;

    private String description;

    private String label;

    private String entity;

    private String icon;

    private String createForm;

    private String maintainForm;

    private String table;

    public PortalApplet(String type, String name, String description, String label, String entity, String icon, String createForm,
            String maintainForm, String table) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.label = label;
        this.entity = entity;
        this.icon = icon;
        this.createForm = createForm;
        this.maintainForm = maintainForm;
        this.table = table;
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

    public String getEntity() {
        return entity;
    }

    public String getIcon() {
        return icon;
    }

    public String getCreateForm() {
        return createForm;
    }

    public String getMaintainForm() {
        return maintainForm;
    }

    public String getTable() {
        return table;
    }

}

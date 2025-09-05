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

import java.util.List;

/**
 * Portal application object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class PortalApplication {

    private String name;

    private String description;

    private List<PortalApplet> applets;

    private List<PortalTable> tables;

    private List<PortalForm> forms;

    private List<PortalEntity> entities;

    public PortalApplication(String name, String description, List<PortalApplet> applets,
            List<PortalTable> tables, List<PortalForm> forms, List<PortalEntity> entities) {
        this.name = name;
        this.description = description;
        this.applets = applets;
        this.tables = tables;
        this.forms = forms;
        this.entities = entities;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<PortalApplet> getApplets() {
        return applets;
    }

    public List<PortalTable> getTables() {
        return tables;
    }

    public List<PortalForm> getForms() {
        return forms;
    }

    public List<PortalEntity> getEntities() {
        return entities;
    }
}

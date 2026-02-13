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

    private String label;

    private String module;

    private List<PortalApplet> applets;

    private List<PortalTable> tables;

    private List<PortalForm> forms;

    private List<PortalEntity> entities;

    private List<PortalReference> references;

    private List<PortalEnum> enums;

    private List<PortalWorkflow> workflows;

    private List<PortalReport> reports;
    
    public PortalApplication(String name, String description, String label, String module, List<PortalApplet> applets,
            List<PortalTable> tables, List<PortalForm> forms, List<PortalEntity> entities,
            List<PortalReference> references, List<PortalEnum> enums, List<PortalWorkflow> workflows,
            List<PortalReport> reports) {
        this.name = name;
        this.description = description;
        this.label = label;
        this.module = module;
        this.applets = applets;
        this.tables = tables;
        this.forms = forms;
        this.entities = entities;
        this.references = references;
        this.enums = enums;
        this.workflows = workflows;
        this.reports = reports;
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

    public String getModule() {
        return module;
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

    public List<PortalReference> getReferences() {
        return references;
    }

    public List<PortalEnum> getEnums() {
        return enums;
    }

    public List<PortalWorkflow> getWorkflows() {
        return workflows;
    }

    public List<PortalReport> getReports() {
        return reports;
    }
}

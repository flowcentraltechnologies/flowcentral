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
 * Portal workflow step object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class PortalWorkflowStep {

    private String name;

    private String description;

    private String label;

    private String applet;

    private boolean error;
    
    private List<PortalWorkflowUserAction> userActions;

    public PortalWorkflowStep(String name, String description, String label, String applet,
            boolean error, List<PortalWorkflowUserAction> userActions) {
        this.name = name;
        this.description = description;
        this.label = label;
        this.applet = applet;
        this.error = error;
        this.userActions = userActions;
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

    public boolean isError() {
        return error;
    }

    public List<PortalWorkflowUserAction> getUserActions() {
        return userActions;
    }
    
}

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
package com.flowcentraltech.flowcentral.application.web.controllers;

import com.flowcentraltech.flowcentral.common.business.LoginUserPhotoGenerator;
import com.flowcentraltech.flowcentral.common.web.controllers.AbstractForwarderPageBean;

/**
 * Application page bean.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ApplicationPageBean extends AbstractForwarderPageBean {

    private LoginUserPhotoGenerator userPhotoGenerator;

    private String[] applicationPaths;
    
    private String workspaceCode;
    
    private String headerTitle;
    
    private String contentStyleClass;
    
    private boolean enableStickyPaths;
    
    private boolean enableMultipleTabs;
    
    private boolean indicateHighLatency;
    
    public LoginUserPhotoGenerator getUserPhotoGenerator() {
        return userPhotoGenerator;
    }

    public void setUserPhotoGenerator(LoginUserPhotoGenerator userPhotoGenerator) {
        this.userPhotoGenerator = userPhotoGenerator;
    }

    public String[] getApplicationPaths() {
        return applicationPaths;
    }

    public void setApplicationPaths(String[] applicationPaths) {
        this.applicationPaths = applicationPaths;
    }

    public String getWorkspaceCode() {
        return workspaceCode;
    }

    public void setWorkspaceCode(String workspaceCode) {
        this.workspaceCode = workspaceCode;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getContentStyleClass() {
        return contentStyleClass;
    }

    public void setContentStyleClass(String contentStyleClass) {
        this.contentStyleClass = contentStyleClass;
    }

    public boolean isEnableStickyPaths() {
        return enableStickyPaths;
    }

    public void setEnableStickyPaths(boolean enableStickyPaths) {
        this.enableStickyPaths = enableStickyPaths;
    }

    public boolean isEnableMultipleTabs() {
        return enableMultipleTabs;
    }

    public void setEnableMultipleTabs(boolean enableMultipleTabs) {
        this.enableMultipleTabs = enableMultipleTabs;
    }

    public boolean isIndicateHighLatency() {
        return indicateHighLatency;
    }

    public void setIndicateHighLatency(boolean indicateHighLatency) {
        this.indicateHighLatency = indicateHighLatency;
    }

}

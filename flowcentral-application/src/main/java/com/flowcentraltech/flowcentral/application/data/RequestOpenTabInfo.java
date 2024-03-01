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

/**
 * Request open tab information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class RequestOpenTabInfo {

    private String title;

    private String tabName;

    private String contentPath;

    private boolean multiPage;
    
    public RequestOpenTabInfo(String title, String tabName, String contentPath) {
        this.title = title;
        this.tabName = tabName;
        this.contentPath = contentPath;
    }
    
    public RequestOpenTabInfo(String title, String tabName, String contentPath, boolean multiPage) {
        this.title = title;
        this.tabName = tabName;
        this.contentPath = contentPath;
        this.multiPage = multiPage;
    }

    public String getTitle() {
        return title;
    }

    public String getTabName() {
        return tabName;
    }

    public String getContentPath() {
        return contentPath;
    }

    public boolean isMultiPage() {
        return multiPage;
    }

}

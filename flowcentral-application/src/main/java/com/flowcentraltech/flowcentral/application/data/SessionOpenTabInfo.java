/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
 * Session open tab information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SessionOpenTabInfo {

    private String title;

    private String documentPath;

    private String contentPath;

    public SessionOpenTabInfo(String title, String documentPath, String contentPath) {
        this.title = title;
        this.documentPath = documentPath;
        this.contentPath = contentPath;
    }

    public String getTitle() {
        return title;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public String getContentPath() {
        return contentPath;
    }
        
}

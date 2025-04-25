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
package com.flowcentraltech.flowcentral.application.util;

/**
 * Open page path parts.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class OpenPagePathParts {

    private String openPath;

    private AppletNameParts appletNameParts;

    public OpenPagePathParts(String openPath, AppletNameParts appletNameParts) {
        this.openPath = openPath;
        this.appletNameParts = appletNameParts;
    }

    public String getOpenPath() {
        return openPath;
    }

    public String getExtAppletName() {
        return appletNameParts.getExtAppletName();
    }

    public String getAppletName() {
        return appletNameParts.getAppletName();
    }

    public String getVestigial() {
        return appletNameParts.getVestigial();
    }

    public boolean isWithVestigial() {
        return appletNameParts.isWithVestigial();
    }

    public String getPseudo() {
        return appletNameParts.getPseudo();
    }

    public boolean isWithPseudo() {
        return appletNameParts.isWithPseudo();
    }

}
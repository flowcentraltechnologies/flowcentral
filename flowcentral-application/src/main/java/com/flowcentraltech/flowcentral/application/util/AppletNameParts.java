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
package com.flowcentraltech.flowcentral.application.util;

import com.tcdng.unify.core.util.StringUtils;

/**
 * Applet name parts.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppletNameParts {

    private String extAppletName;

    private String appletName;

    private String vestigial;

    private String pseudo;

    public AppletNameParts(String extendedAppletName, String appletName, String vestigial, String pseudo) {
        this.extAppletName = extendedAppletName;
        this.appletName = appletName;
        this.vestigial = vestigial;
        this.pseudo = pseudo;
    }

    public AppletNameParts(String extAppletName) {
        this.extAppletName = extAppletName;
        this.appletName = extAppletName;
    }

    public String getExtAppletName() {
        return extAppletName;
    }

    public String getAppletName() {
        return appletName;
    }

    public String getVestigial() {
        return vestigial;
    }

    public boolean isWithVestigial() {
        return !StringUtils.isBlank(vestigial);
    }

    public String getPseudo() {
        return pseudo;
    }

    public boolean isWithPseudo() {
        return !StringUtils.isBlank(pseudo);
    }
}
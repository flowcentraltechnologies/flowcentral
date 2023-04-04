/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.configuration.constants.AppletType;

/**
 * Application page utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class ApplicationPageUtils {

    private ApplicationPageUtils() {

    }

    public static String constructAppletOpenPagePath(AppletType type, String appletName) {
        return ApplicationPageUtils.constructAppletOpenPagePath(type.path(), appletName);
    }

    public static String constructAppletOpenPagePath(AppletType type, String appletName, Object instId) {
        return ApplicationPageUtils.constructAppletOpenPagePath(type.path(), appletName, instId);
    }

    public static String constructAppletOpenPagePath(String controllerName, String appletName, Object instId) {
        return ApplicationPageUtils.constructAppletOpenPagePath(controllerName,
                ApplicationNameUtils.addVestigialNamePart(appletName, String.valueOf(instId)));
    }

    public static String constructAppletOpenPagePath(String controllerName, String appletName) {
        return new StringBuilder().append(controllerName).append(':').append(appletName).append("/openPage").toString();
    }

    public static String constructAppletReplacePagePath(String controllerName, String appletName) {
        return new StringBuilder().append(controllerName).append(':').append(appletName).append("/replacePage")
                .toString();
    }
}

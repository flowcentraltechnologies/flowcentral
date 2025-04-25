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
package com.flowcentraltech.flowcentral.application.util;

import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.AppletType;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.constant.UnifyWebRequestAttributeConstants;

/**
 * Application page utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public final class ApplicationPageUtils {
    
    private static final String MULTIPAGE_PART = UnifyWebRequestAttributeConstants.TIMESTAMP_VARIABLE + "/";
    
    private ApplicationPageUtils() {

    }

    public static String mergePathVariables(List<String> pathVariables) {
        return StringUtils.concatenateUsingSeparator(':', pathVariables);
    }
    
    public static String constructMultiPageAppletOpenPagePath(String path) {
        return StringUtils.replaceLast(path, "/", MULTIPAGE_PART);
    }

    public static OpenPagePathParts constructAppletOpenPagePath(AppletType type, String appletName) {
        return ApplicationPageUtils.constructAppletOpenPagePath(type.path(), appletName);
    }

    public static OpenPagePathParts constructAppletNewInstOpenPagePath(AppletType type, String appletName) {
        return ApplicationPageUtils.constructAppletOpenPagePath(type.path(), ApplicationNameUtils.addPseudoNamePart(appletName));
    }

    public static OpenPagePathParts constructAppletOpenPagePath(AppletType type, String appletName, Object instId) {
        return ApplicationPageUtils.constructAppletOpenPagePath(type.path(), appletName, instId);
    }

    public static OpenPagePathParts constructAppletOpenPagePath(String controllerName, String appletName, Object instId) {
        return ApplicationPageUtils.constructAppletOpenPagePath(controllerName, ApplicationNameUtils.addVestigialNamePart(appletName, String.valueOf(instId)));
    }

    public static OpenPagePathParts constructAppletOpenPagePath(String controllerName, String appletName) {
        final String path = new StringBuilder().append(controllerName).append(':').append(appletName).append("/openPage").toString();
        return new OpenPagePathParts(path, ApplicationNameUtils.getAppletNameParts(appletName));
    }

    public static String constructAppletOpenInBrowserWindowPath(String controllerName, String tabName) {
        return new StringBuilder().append(controllerName).append(':').append(tabName).append("/openInBrowserWindow").toString();
    }

    public static String constructAppletPath(String controllerName, String appletName) {
        return new StringBuilder().append(controllerName).append(':').append(appletName).toString();
    }

    public static String constructAppletReplacePagePath(String controllerName, String appletName) {
        return new StringBuilder().append(controllerName).append(':').append(appletName).append("/replacePage")
                .toString();
    }

}

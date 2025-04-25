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

package com.flowcentraltech.flowcentral.notification.util;

import com.tcdng.unify.core.util.StringUtils;

/**
 * Notification code generation utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class NotificationCodeGenUtils {

    public static final String TEMPLATE_NAME = "__TEMPLATE_NAME";


    public static final String LARGETEXT_NAME = "__LARGETEXT_NAME";

    private NotificationCodeGenUtils() {

    }

    public static String generateUtilitiesTemplateWrapperClassName(String basePackage, String moduleName,
            String templateName) {
        return StringUtils.isBlank(templateName) ? null
                : NotificationCodeGenUtils.generateUtilitiesTemplateWrapperPackageName(basePackage, moduleName) + "."
                        + StringUtils.capitalizeFirstLetter(templateName);
    }

    public static String generateUtilitiesTemplateWrapperPackageName(String basePackage, String moduleName) {
        return basePackage + ".utilities." + moduleName.toLowerCase() + ".templatewrappers";
    }

    public static String generateUtilitiesLargeTextWrapperClassName(String basePackage, String moduleName,
            String largeTextName) {
        return StringUtils.isBlank(largeTextName) ? null
                : NotificationCodeGenUtils.generateUtilitiesLargeTextWrapperPackageName(basePackage, moduleName) + "."
                        + StringUtils.capitalizeFirstLetter(largeTextName);
    }

    public static String generateUtilitiesLargeTextWrapperPackageName(String basePackage, String moduleName) {
        return basePackage + ".utilities." + moduleName.toLowerCase() + ".largetextwrappers";
    }
}

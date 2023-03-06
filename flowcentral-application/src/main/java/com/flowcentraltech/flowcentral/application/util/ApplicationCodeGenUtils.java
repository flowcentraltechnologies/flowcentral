/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.tcdng.unify.core.util.SqlUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Application code generation utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class ApplicationCodeGenUtils {

    private ApplicationCodeGenUtils() {

    }

    public static String generateCustomEntityTableName(String moduleShortName, String entityName) {
        return StringUtils.isBlank(entityName) ? null
                : "U_" + moduleShortName + "_" + SqlUtils.generateSchemaElementName(entityName, true);
    }

    public static String generateCustomEntityClassName(ConfigType type, String applicationName, String entityName) {
        String supPackage = type.isStatic() ? "z" : "u";
        return StringUtils.isBlank(entityName) ? null
                : "com.flowcentraltech.flowcentral." + applicationName.toLowerCase() + "." + supPackage + "."
                        + StringUtils.capitalizeFirstLetter(entityName) + supPackage;
    }

    public static String generateExtensionEntityClassName(String basePackage, String moduleName, String entityName) {
        return StringUtils.isBlank(entityName) ? null
                : basePackage + ".extension." + moduleName.toLowerCase() + ".entities."
                        + StringUtils.capitalizeFirstLetter(entityName);
    }

    public static String generateUtilitiesEntityWrapperClassName(String basePackage, String moduleName, String entityName) {
        return StringUtils.isBlank(entityName) ? null
                : basePackage + ".utilities." + moduleName.toLowerCase() + ".entitywrappers."
                        + StringUtils.capitalizeFirstLetter(entityName);
    }
}

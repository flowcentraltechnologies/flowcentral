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

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Generator name utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public final class GeneratorNameUtils {

    private static final FactoryMap<String, GeneratorNameParts> generatorNameParts;

    static {
        generatorNameParts = new FactoryMap<String, GeneratorNameParts>()
            {

                @Override
                protected GeneratorNameParts create(String fullName, Object... arg1) throws Exception {
                    try {
                        String[] po = StringUtils.charSplit(fullName, ':');
                        return new GeneratorNameParts(fullName, po[0], po[1]);
                    } catch (Exception e) {
                        throw new RuntimeException("Name parts error: fullName = " + fullName, e);
                    }
                }

            };
    }

    private GeneratorNameUtils() {

    }

    public static String getGeneratorFullName(String componentName, String rule) {
        return componentName + ':' + rule;
    }

    public static GeneratorNameParts getGeneratorNameParts(String fullName) throws UnifyException {
        return generatorNameParts.get(fullName);
    }

    public static boolean isFullNameWithParts(String fullName) {
        return fullName.indexOf(':') > 0;
    }

}

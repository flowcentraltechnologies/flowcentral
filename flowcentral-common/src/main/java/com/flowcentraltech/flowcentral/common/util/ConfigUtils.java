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

package com.flowcentraltech.flowcentral.common.util;

import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.entities.ConfigEntity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UnifyOperationException;

/**
 * Configuration utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class ConfigUtils {

    private ConfigUtils() {

    }

    public static void preCreate(ConfigEntity configEntity) throws UnifyException {
        if (configEntity.getConfigType() == null) {
             configEntity.setConfigType(ConfigType.CUSTOM);
        }
    }

    public static void preUpdate(ConfigEntity configEntity) throws UnifyException {
        final ConfigType type = configEntity.getConfigType();
        if (ConfigType.STATIC.equals(type)) {
            throw new UnifyOperationException("Attempt to alter static configuration record.");
        }
    }

    public static void preDelete(ConfigEntity configEntity) throws UnifyException {
        final ConfigType type = configEntity.getConfigType();
        if (ConfigType.STATIC.equals(type)) {
            throw new UnifyOperationException("Attempt to alter static configuration record.");
        }
    }

}

/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.system.util;

import com.flowcentraltech.flowcentral.configuration.constants.SysParamType;
import com.flowcentraltech.flowcentral.system.entities.DataSourceConnection;
import com.tcdng.unify.core.database.dynamic.DynamicDataSourceConfig;
import com.tcdng.unify.core.database.dynamic.DynamicDataSourceDef;
import com.tcdng.unify.core.util.SqlUtils;

/**
 * System utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public final class SystemUtils {

    private static final String PRC_PREFIX = "pv:";

    private static final String PRC_PREFIX_UPPERCASE = PRC_PREFIX.toUpperCase();

    private static final String SYS_PREFIX = "sp:";

    private static final String SYS_PREFIX_UPPERCASE = SYS_PREFIX.toUpperCase();

    private SystemUtils() {

    }

    public static DynamicDataSourceConfig getDynamicDataSourceConfig(DataSourceConnection dataSourceConnection) {
        return SqlUtils.getDynamicDataSourceConfig(getDynamicDataSourceDef(dataSourceConnection));
    }

    public static DynamicDataSourceDef getDynamicDataSourceDef(DataSourceConnection dataSourceConnection) {
        return new DynamicDataSourceDef(dataSourceConnection.getName(), null, dataSourceConnection.getDescription(),
                dataSourceConnection.getDialect(), dataSourceConnection.getHost(), dataSourceConnection.getPort(),
                dataSourceConnection.getDatabase(), dataSourceConnection.getService(), dataSourceConnection.getSchema(),
                dataSourceConnection.getUserName(), dataSourceConnection.getPassword(), 4 /* TODO */, false, false,
                dataSourceConnection.getId(), dataSourceConnection.getVersionNo());
    }
    
    public static String encodeProcessVariableCode(String code) {
        return PRC_PREFIX + code;
    }

    public static String encodeProcessVariableLabel(String name) {
        return PRC_PREFIX_UPPERCASE + " " + name;
    }

    public static String encodeSysParamCode(SysParamType type, String code) {
        return SYS_PREFIX + code + ":" + type.code();
    }

    public static String encodeSysParamLabel(String name) {
        return SYS_PREFIX_UPPERCASE + " " + name;
    }
    
    public static boolean isProcessVariable(String encoded) {
        return encoded.startsWith(PRC_PREFIX);
    }
    
    public static boolean isSysParam(String encoded) {
        return encoded.startsWith(SYS_PREFIX);
    }
    
    public static String getSysParamCode(String encoded) {
        return encoded.substring(SYS_PREFIX.length(), encoded.length() - 2);
    }
}

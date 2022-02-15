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

package com.flowcentraltech.flowcentral.system.business;

import com.flowcentraltech.flowcentral.system.constants.SystemModuleSysParamConstants;
import com.flowcentraltech.flowcentral.system.entities.DownloadLog;
import com.tcdng.unify.core.SessionContext;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.web.AbstractDownloadPathLogger;

/**
 * System download path logger.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("system-downloadpathlogger")
public class SystemDownloadPathLogger extends AbstractDownloadPathLogger {

    @Configurable
    private SystemModuleService systemService;

    public void setSystemService(SystemModuleService systemService) {
        this.systemService = systemService;
    }

    @Override
    public void logDownloadAttempt(String resourceName) throws UnifyException {
        if (systemService.getSysParameterValue(boolean.class,
                SystemModuleSysParamConstants.SYSTEM_DOWNLOADPATH_LOG_ENABLED)) {
            SessionContext ctx = getSessionContext();
            DownloadLog downloadLog = new DownloadLog(resourceName, ctx.getRemoteAddress(), ctx.getRemoteHost());
            systemService.createDownloadLog(downloadLog);
        }
    }

}

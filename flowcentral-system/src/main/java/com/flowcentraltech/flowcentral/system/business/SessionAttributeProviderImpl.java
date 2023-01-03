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

import java.util.Map;

import com.flowcentraltech.flowcentral.system.constants.SystemModuleNameConstants;
import com.flowcentraltech.flowcentral.system.constants.SystemModuleSysParamConstants;
import com.tcdng.unify.core.AbstractSessionAttributeProvider;
import com.tcdng.unify.core.UnifyCoreSessionAttributeConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Session attribute provider implementation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(SystemModuleNameConstants.SESSION_ATTRIBUTE_PROVIDER)
public class SessionAttributeProviderImpl extends AbstractSessionAttributeProvider {

    @Configurable
    private SystemModuleService systemModuleService;

    public final void setSystemModuleService(SystemModuleService systemModuleService) {
        this.systemModuleService = systemModuleService;
    }

    @Override
    protected void load(Map<String, Object> attributes) throws UnifyException {
        final boolean globalAccounting = systemModuleService.getSysParameterValue(boolean.class,
                SystemModuleSysParamConstants.SYSTEM_GLOBAL_ACCOUNTING_INPUT_ENABLED);
        final boolean useTenantDateFormat = systemModuleService.getSysParameterValue(boolean.class,
                SystemModuleSysParamConstants.SYSTEM_USE_TENANT_DATEFORMAT);
        setApplicationAttribute(UnifyCoreSessionAttributeConstants.INPUT_GLOBAL_ACCOUNTING_FLAG, globalAccounting);
        setSessionAttribute(UnifyCoreSessionAttributeConstants.OVERRIDE_WIDGET_DATEFORMAT_FLAG, useTenantDateFormat);
    }

}

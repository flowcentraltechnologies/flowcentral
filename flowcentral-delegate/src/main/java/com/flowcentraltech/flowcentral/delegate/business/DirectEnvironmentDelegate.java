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
package com.flowcentraltech.flowcentral.delegate.business;

import com.flowcentraltech.flowcentral.application.business.AbstractEnvironmentDelegate;
import com.flowcentraltech.flowcentral.connect.common.data.BaseResponse;
import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.flowcentraltech.flowcentral.system.constants.SystemModuleNameConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Direct environment delegate.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(name = SystemModuleNameConstants.DIRECT_ENVIRONMENT_DELEGATE,
    description = "Direct Environment Delegate")
public class DirectEnvironmentDelegate extends AbstractEnvironmentDelegate {

    @Override
    public boolean isDirect() {
        return true;
    }

    @Override
    public String[] executeProcedure(String operation, String... payload) throws UnifyException {
        return null;
    }

    @Override
    protected BaseResponse sendToDelegateDatasourceService(DataSourceRequest req) throws UnifyException {
        return null;
    }

}

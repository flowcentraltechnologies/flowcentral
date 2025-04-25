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

package com.flowcentraltech.flowcentral.security.business;

import com.flowcentraltech.flowcentral.common.business.RequestUserPhotoGenerator;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralRequestAttributeConstants;
import com.flowcentraltech.flowcentral.security.constants.SecurityModuleNameConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Request user photo generator implementation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(SecurityModuleNameConstants.SECURITY_REQUEST_USER_PHOTO_GENERATOR)
public class RequestUserPhotoGeneratorImpl extends AbstractUserPhotoGenerator implements RequestUserPhotoGenerator {

    @Override
    protected String getUserLoginId() throws UnifyException {
        return (String) getRequestAttribute(FlowCentralRequestAttributeConstants.USER_LOGINID);
    }

}

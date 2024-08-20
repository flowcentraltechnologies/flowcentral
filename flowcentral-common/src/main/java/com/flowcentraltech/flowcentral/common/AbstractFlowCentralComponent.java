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
package com.flowcentraltech.flowcentral.common;

import com.flowcentraltech.flowcentral.common.constants.FlowCentralContainerPropertyConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralEditionConstants;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.web.TargetPath;
import com.tcdng.unify.web.ui.PageRequestContextUtil;
import com.tcdng.unify.web.ui.WebUIApplicationComponents;

/**
 * Base class for flowCentral components.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractFlowCentralComponent extends AbstractUnifyComponent implements FlowCentralComponent {

    /**
     * Posts a JSON object.
     * 
     * @param respClass
     *                  the response type
     * @param endpoint
     *                  the endpoint
     * @param req
     *                  the request object
     * @return the response object
     * @throws UnifyException
     *                        if an error occurs
     */
    protected <T, U> T postJsonObject(Class<T> respClass, String endpoint, U req) throws UnifyException {
        return IOUtils.postObjectToEndpointUsingJson(respClass, endpoint, req);
    }
  
    protected final boolean isEnterprise() throws UnifyException {
        return FlowCentralEditionConstants.ENTERPRISE.equalsIgnoreCase(getContainerSetting(String.class,
                FlowCentralContainerPropertyConstants.FLOWCENTRAL_INSTALLATION_TYPE));
    }

	protected void setCommandResultMapping(String resultMappingName) throws UnifyException {
		getRequestContextUtil().setCommandResultMapping(resultMappingName);
	}

	protected void setCommandResponsePath(TargetPath targetPath) throws UnifyException {
		getRequestContextUtil().setCommandResponsePath(targetPath);
	}

	protected PageRequestContextUtil getRequestContextUtil() throws UnifyException {
		return (PageRequestContextUtil) getComponent(WebUIApplicationComponents.APPLICATION_PAGEREQUESTCONTEXTUTIL);
	}
    
}

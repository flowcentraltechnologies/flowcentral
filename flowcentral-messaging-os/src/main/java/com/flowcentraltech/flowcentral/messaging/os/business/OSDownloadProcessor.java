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
package com.flowcentraltech.flowcentral.messaging.os.business;

import java.io.OutputStream;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.messaging.os.data.BaseOSMessagingResp;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.http.HttpRequestHeaders;

/**
 * OS download processor.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface OSDownloadProcessor<T extends BaseOSMessagingResp> extends FlowCentralComponent {

    /**
     * Gets the processor response class.
     */
    Class<? extends BaseOSMessagingResp> getResponseClass();

    /**
     * Processes request output stream.
     * 
     * @param headers
     *                    the request headers
     * @param out
     *                    the output stream
     * @return the gateway response
     * @throws UnifyException
     *                        if an error occurs
     */
    T process(HttpRequestHeaders headers, OutputStream out) throws UnifyException;

}

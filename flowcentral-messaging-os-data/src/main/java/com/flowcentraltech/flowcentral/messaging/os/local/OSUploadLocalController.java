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
package com.flowcentraltech.flowcentral.messaging.os.local;

import java.io.InputStream;
import java.util.Map;

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.PostResp;
import com.tcdng.unify.web.util.ContentDisposition;

/**
 * OS Upload Local Controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface OSUploadLocalController extends UnifyComponent {

    /**
     * Handles local upload.
     * 
     * @param headers
     *                    the headers
     * @param disposition
     *                    the content disposition
     * @param in
     *                    the payload input stream
     * @return the response
     * @throws UnifyException
     *                        if an error occurs
     */
    PostResp<String> handleLocalUpload(Map<String, String> headers, ContentDisposition disposition, InputStream in)
            throws UnifyException;
}

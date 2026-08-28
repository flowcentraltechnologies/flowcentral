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
package com.flowcentraltech.flowcentral.messaging.os.data.local;

import java.io.OutputStream;
import java.util.Map;

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.UploadedFile;
import com.tcdng.unify.core.util.PostResp;

/**
 * OS Download Local Controller.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface OSDownloadLocalController extends UnifyComponent {

    /**
     * Handles local download.
     * 
     * @param headers
     *                the download headers
     * @param out
     *                the output stream
     * @return the post response
     * @throws UnifyException
     *                        if an error occurs
     */
    PostResp<String> handleLocalDownload(Map<String, String> headers, OutputStream out) throws UnifyException;
    
    /**
     * Handles local download
     * 
     * @param headers
     *                the headers
     * @return the uploaded file
     * @throws UnifyException
     *                        if an error occurs
     */
    UploadedFile handleLocalDownload(Map<String, String> headers) throws UnifyException;
}

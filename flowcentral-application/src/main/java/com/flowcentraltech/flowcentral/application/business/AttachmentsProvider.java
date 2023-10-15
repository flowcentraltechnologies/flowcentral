/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.application.business;

import com.flowcentraltech.flowcentral.application.data.Attachment;
import com.flowcentraltech.flowcentral.application.data.Attachments;
import com.flowcentraltech.flowcentral.application.data.AttachmentsOptions;
import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.web.ui.widget.data.FileAttachmentInfo;

/**
 * Attachments provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface AttachmentsProvider extends FlowCentralComponent {

    /**
     * Provides attachment based on supplied reader.
     * 
     * @param reader
     *                the reader to use
     * @param options
     *                the attachments options
     * @return the attachments
     * @throws UnifyException
     *                        if an error occurs
     */
    Attachments provide(ValueStoreReader reader, AttachmentsOptions options) throws UnifyException;

    /**
     * Gets file attachment information.
     * 
     * @param attachment
     *                   the attachment source
     * @return the file attachment info
     * @throws UnifyException
     *                        if an error occurs
     */
    FileAttachmentInfo getFileAttachmentInfo(Attachment attachment) throws UnifyException;

    /**
     * Saves attachment file data.
     * 
     * @param attachment
     *                   the attachment source
     * @param filename
     *                   the file name
     * @param fileData
     *                   the file data
     * @throws UnifyException
     *                        if an error occurs
     */
    void saveAttachmentData(Attachment attachment, String filename, byte[] fileData) throws UnifyException;

}

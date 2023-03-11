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

package com.flowcentraltech.flowcentral.system.web.widgets;

import java.io.IOException;
import java.io.OutputStream;

import com.flowcentraltech.flowcentral.common.business.FileAttachmentProvider;
import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Singleton;
import com.tcdng.unify.core.resource.AbstractImageGenerator;
import com.tcdng.unify.core.resource.ImageFormat;

/**
 * Implementation of file attachment image generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Singleton(false)
@Component("fileattachment-imagegenerator")
public class FileAttachmentImageGeneratorImpl extends AbstractImageGenerator implements FileAttachmentImageGenerator {

    @Configurable
    private FileAttachmentProvider fileAttachmentProvider;

    private String category;

    private String entityName;

    private String attachmentName;

    private Long entityInstId;

    private byte[] photo;

    public void setFileAttachmentProvider(FileAttachmentProvider fileAttachmentProvider) {
        this.fileAttachmentProvider = fileAttachmentProvider;
    }

    @Override
    public ImageFormat generate(OutputStream outputStream) throws UnifyException {
        try {
            outputStream.write(photo);
            outputStream.flush();
        } catch (IOException e) {
            throwOperationErrorException(e);
        } finally {
            photo = null;
        }

        return ImageFormat.WILDCARD;
    }

    @Override
    public boolean isReady() throws UnifyException {
        if (photo == null) {
            Attachment attachment = fileAttachmentProvider.retrieveFileAttachment(category, entityName, entityInstId,
                    attachmentName);
            if (attachment != null) {
                photo = attachment.getData();
                return true;
            }
        }

        return false;
    }

    @Override
    public void init(String category, String entityName, String attachmentName) {
        this.category = category;
        this.entityName = entityName;
        this.attachmentName = attachmentName;
    }

    @Override
    public void prepareGenerationFor(Long entityInstId) {
        this.entityInstId = entityInstId;
    }

}

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

package com.flowcentraltech.flowcentral.application.business;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.common.business.FileAttachmentProvider;
import com.flowcentraltech.flowcentral.common.constants.FileAttachmentCategoryType;
import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.system.constants.SystemFileResourceConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.file.AbstractFileResourceProvider;

/**
 * Application file resource provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(ApplicationModuleNameConstants.APPLICATION_FILE_RESOURCE_PROVIDER)
public class ApplicationFileResourceProviderImpl extends AbstractFileResourceProvider {

    @Configurable
    private FileAttachmentProvider fileAttachmentProvider;

    @Override
    public InputStream openFileResourceInputStream(String category, String resourceName) throws UnifyException {
        byte[] resource = readFileResource(category, resourceName);
        return resource != null ? new ByteArrayInputStream(resource) : null;
    }

    @Override
    public byte[] readFileResource(String category, String resourceName) throws UnifyException {
        Attachment attachment = fileAttachmentProvider.retrieveFileAttachment(
                FileAttachmentCategoryType.fromCode(category), SystemFileResourceConstants.FILERESOURCE_ENTITY_NAME,
                SystemFileResourceConstants.FILERESOURCE_ENTITY_INST_ID, resourceName);
        return attachment != null ? attachment.getData() : null;
    }

}

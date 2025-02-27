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

package com.flowcentraltech.flowcentral.application.web.panels;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.data.EntityInstNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityUtils;
import com.flowcentraltech.flowcentral.common.business.FileAttachmentProvider;
import com.flowcentraltech.flowcentral.common.constants.FileAttachmentCategoryType;
import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.AttachmentDetails;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.FileAttachmentInfo;
import com.tcdng.unify.web.ui.widget.control.AbstractFileAttachmentHandler;

/**
 * Form file attachment handler.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(ApplicationModuleNameConstants.FORM_FILE_ATTACHMENTS_HANDLER)
public class FormFileAttachmentsHandler extends AbstractFileAttachmentHandler {

    @Configurable
    private FileAttachmentProvider fileAttachmentProvider;

    @Override
    public void fillAttachFileNames(Object parentId, List<FileAttachmentInfo> attachmentInfoList)
            throws UnifyException {
        EntityInstNameParts np = getEntityInstNameParts(parentId);
        Map<String, AttachmentDetails> fileNames = fileAttachmentProvider.retrieveAllFileAttachmentsByName(
                FileAttachmentCategoryType.FORM_CATEGORY, np.getEntityName(), np.getInstId());
        for (FileAttachmentInfo fileAttachmentInfo : attachmentInfoList) {
            AttachmentDetails details = fileNames.get(fileAttachmentInfo.getName());
            if (details != null) {
                fileAttachmentInfo.setFilename(details.getFileName());
            }
        }
    }

    @Override
    public void handleAttach(Object parentId, FileAttachmentInfo fileAttachmenInfo) throws UnifyException {
        EntityInstNameParts np = getEntityInstNameParts(parentId);
        fileAttachmentProvider.saveFileAttachment(FileAttachmentCategoryType.FORM_CATEGORY, np.getEntityName(),
                np.getInstId(),
                Attachment.newBuilder(fileAttachmenInfo.getType(), false).name(fileAttachmenInfo.getName())
                        .title(!StringUtils.isBlank(fileAttachmenInfo.getDescription())
                                ? fileAttachmenInfo.getDescription()
                                : fileAttachmenInfo.getFilename())
                        .fileName(fileAttachmenInfo.getFilename()).data(fileAttachmenInfo.getAttachment()).build());
    }

    @Override
    public void handleDetach(Object parentId, FileAttachmentInfo fileAttachmenInfo) throws UnifyException {
        EntityInstNameParts np = getEntityInstNameParts(parentId);
        fileAttachmentProvider.deleteFileAttachment(FileAttachmentCategoryType.FORM_CATEGORY, np.getEntityName(),
                np.getInstId(), fileAttachmenInfo.getName());
    }

    @Override
    public FileAttachmentInfo handleView(Object parentId, FileAttachmentInfo fileAttachmenInfo) throws UnifyException {
        EntityInstNameParts np = getEntityInstNameParts(parentId);
        FileAttachmentInfo result = new FileAttachmentInfo(fileAttachmenInfo.getType(), fileAttachmenInfo.getName(),
                fileAttachmenInfo.getDescription(), fileAttachmenInfo.getFilename());
        Attachment attachment = fileAttachmentProvider.retrieveFileAttachment(FileAttachmentCategoryType.FORM_CATEGORY,
                np.getEntityName(), np.getInstId(), fileAttachmenInfo.getName());
        if (attachment != null) {
            result.setFilename(attachment.getFileName());
            result.setAttachment(attachment.getData());
        }

        return result;
    }

    private EntityInstNameParts getEntityInstNameParts(Object parentId) {
        return ApplicationEntityUtils.getEntityInstNameParts((String) parentId);
    }
}

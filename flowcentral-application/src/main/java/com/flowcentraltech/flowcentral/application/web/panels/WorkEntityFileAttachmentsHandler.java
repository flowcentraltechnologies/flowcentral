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

package com.flowcentraltech.flowcentral.application.web.panels;

import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.data.EntityInstNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityUtils;
import com.flowcentraltech.flowcentral.common.business.FileAttachmentProvider;
import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.web.ui.widget.control.AbstractFileAttachmentHandler;
import com.tcdng.unify.web.ui.widget.data.FileAttachmentInfo;

/**
 * Work entity file attachment handler.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(ApplicationModuleNameConstants.WORKENTITY_FILE_ATTACHMENTS_HANDLER)
public class WorkEntityFileAttachmentsHandler extends AbstractFileAttachmentHandler {

    private static final String WORK_CATEGORY = "work";

    @Configurable
    private FileAttachmentProvider fileAttachmentProvider;

    public void setFileAttachmentProvider(FileAttachmentProvider fileAttachmentProvider) {
        this.fileAttachmentProvider = fileAttachmentProvider;
    }

    @Override
    public void fillAttachFileNames(Object parentId, List<FileAttachmentInfo> attachmentInfoList)
            throws UnifyException {
        EntityInstNameParts np = getEntityInstNameParts(parentId);
        Map<String, String> fileNames = fileAttachmentProvider.retrieveAllAttachmentFileNames(WORK_CATEGORY,
                np.getEntityName(), np.getInstId());
        for (FileAttachmentInfo fileAttachmentInfo : attachmentInfoList) {
            String fileName = fileNames.get(fileAttachmentInfo.getName());
            if (fileName != null) {
                fileAttachmentInfo.setFilename(fileName);
            }
        }
    }

    @Override
    public void handleAttach(Object parentId, FileAttachmentInfo fileAttachmenInfo) throws UnifyException {
        EntityInstNameParts np = getEntityInstNameParts(parentId);
        fileAttachmentProvider.saveFileAttachment(WORK_CATEGORY, np.getEntityName(), np.getInstId(),
                Attachment.newBuilder(fileAttachmenInfo.getType()).name(fileAttachmenInfo.getName())
                        .title(fileAttachmenInfo.getDescription()).fileName(fileAttachmenInfo.getFilename())
                        .data(fileAttachmenInfo.getAttachment()).build());
    }

    @Override
    public void handleDetach(Object parentId, FileAttachmentInfo fileAttachmenInfo) throws UnifyException {
        EntityInstNameParts np = getEntityInstNameParts(parentId);
        fileAttachmentProvider.deleteFileAttachment(WORK_CATEGORY, np.getEntityName(), np.getInstId(),
                fileAttachmenInfo.getName());
    }

    @Override
    public FileAttachmentInfo handleView(Object parentId, FileAttachmentInfo fileAttachmenInfo) throws UnifyException {
        EntityInstNameParts np = getEntityInstNameParts(parentId);
        FileAttachmentInfo result = new FileAttachmentInfo(fileAttachmenInfo.getType(), fileAttachmenInfo.getName(),
                fileAttachmenInfo.getDescription(), fileAttachmenInfo.getFilename());
        Attachment attachment = fileAttachmentProvider.retrieveFileAttachment(WORK_CATEGORY, np.getEntityName(),
                np.getInstId(), fileAttachmenInfo.getName());
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

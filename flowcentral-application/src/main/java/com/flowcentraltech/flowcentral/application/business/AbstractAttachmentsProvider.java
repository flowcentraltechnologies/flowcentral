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

package com.flowcentraltech.flowcentral.application.business;

import java.util.List;

import com.flowcentraltech.flowcentral.application.data.Attachment;
import com.flowcentraltech.flowcentral.application.data.Attachments;
import com.flowcentraltech.flowcentral.application.data.AttachmentsOptions;
import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.data.FileAttachmentInfo;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Convenient abstract base class for attachments provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractAttachmentsProvider extends AbstractFlowCentralComponent implements AttachmentsProvider {

    @Configurable
    private AppletUtilities appletUtilities;

    @Override
    public final Attachments provide(ValueStoreReader reader, AttachmentsOptions options) throws UnifyException {
        Attachments.Builder ab = Attachments.newBuilder(getName());
        ab.caption(getCaption(reader, options));
        ab.addAttachments(getAttachments(reader, options));
        ab.enableUpload(isUploadEnabled(reader, options));
        return ab.build();
    }

    @Override
    public final FileAttachmentInfo getFileAttachmentInfo(Attachment attachment) throws UnifyException {
        final FileAttachmentType type = FileAttachmentType.fromName(attachment.getFormat());
        FileAttachmentInfo fileAttachmentInfo = new FileAttachmentInfo(type);
        fileAttachmentInfo.setFilename(attachment.isWithFileName() ? attachment.getFileName()
                : type.appendDefaultExtension(attachment.getName()));
        fileAttachmentInfo.setAttachment(getData(type, attachment));
        return fileAttachmentInfo;
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    protected final ApplicationModuleService application() {
        return appletUtilities.application();
    }

    protected final EnvironmentService environment() {
        return appletUtilities.environment();
    }

    protected final SystemModuleService system() {
        return appletUtilities.system();
    }

    protected abstract boolean isUploadEnabled(ValueStoreReader reader, AttachmentsOptions options)
            throws UnifyException;

    protected abstract String getCaption(ValueStoreReader reader, AttachmentsOptions options) throws UnifyException;

    protected abstract List<Attachment> getAttachments(ValueStoreReader reader, AttachmentsOptions options)
            throws UnifyException;

    protected abstract byte[] getData(FileAttachmentType resolvedType, Attachment attachment) throws UnifyException;
}

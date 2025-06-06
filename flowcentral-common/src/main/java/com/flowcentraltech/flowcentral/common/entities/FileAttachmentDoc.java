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
package com.flowcentraltech.flowcentral.common.entities;

import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;

/**
 * File attachment document entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_FILEATTACHMENTDOC")
public class FileAttachmentDoc extends BaseEntity {

    @ForeignKey(FileAttachment.class)
    private Long fileAttachmentId;

    @Column(name = "ATTACHMENT_DOC")
    private byte[] data;

    public FileAttachmentDoc(byte[] data) {
        this.data = data;
    }

    public FileAttachmentDoc() {

    }

    @Override
    public String getDescription() {
        return null;
    }

    public Long getFileAttachmentId() {
        return fileAttachmentId;
    }

    public void setFileAttachmentId(Long fileAttachmentId) {
        this.fileAttachmentId = fileAttachmentId;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

}

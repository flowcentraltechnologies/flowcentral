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

package com.flowcentraltech.flowcentral.common.data;

import com.tcdng.unify.core.constant.FileAttachmentType;

/**
 * Attachment details.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class AttachmentDetails {

    private Long id;

    private FileAttachmentType type;

    private String name;

    private String title;

    private String fileName;

    private boolean inline;

    private long versionNo;

    public AttachmentDetails(Long id, FileAttachmentType type, String name, String title, String fileName,
            boolean inline, long versionNo) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.title = title;
        this.fileName = fileName;
        this.inline = inline;
        this.versionNo = versionNo;
    }

    public Long getId() {
        return id;
    }

    public FileAttachmentType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isInline() {
        return inline;
    }

    public long getVersionNo() {
        return versionNo;
    }

}

/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
 * Attachment.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class Attachment extends AttachmentDetails {

    private byte[] data;

    private String sourceId;

    private String provider;

    public Attachment(Long id, FileAttachmentType type, String name, String title, String fileName, byte[] data,
            boolean inline, long versionNo) {
        super(id, type, name, title, fileName, inline, versionNo);
        this.data = data;
    }

    public Attachment(Long id, FileAttachmentType type, String name, String title, String fileName, String provider,
            String sourceId, boolean inline, long versionNo) {
        super(id, type, name, title, fileName, inline, versionNo);
        this.provider = provider;
        this.sourceId = sourceId;
    }

    public byte[] getData() {
        return data;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getProvider() {
        return provider;
    }

    public static Builder newBuilder(Long id, FileAttachmentType type, boolean inline, long versionNo) {
        return new Builder(id, type, inline, versionNo);
    }

    public static Builder newBuilder(FileAttachmentType type, boolean inline) {
        return new Builder(null, type, inline, 0L);
    }

    public static class Builder {

        private Long id;

        private FileAttachmentType type;

        private String name;

        private String title;

        private String fileName;

        private byte[] data;

        private String sourceId;

        private String provider;

        private boolean inline;

        private long versionNo;

        public Builder(Long id, FileAttachmentType type, boolean inline, long versionNo) {
            this.id = id;
            this.type = type;
            this.inline = inline;
            this.versionNo = versionNo;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder data(byte[] data) {
            this.data = data;
            return this;
        }

        public Builder source(String provider, String sourceId) {
            this.provider = provider;
            this.sourceId = sourceId;
            return this;
        }

        public Attachment build() {
            if (type == null) {
                throw new RuntimeException("Attachment type is required.");
            }

            if (name == null) {
                throw new RuntimeException("Attachment name is required.");
            }

            if (title == null) {
                throw new RuntimeException("Attachment title is required.");
            }

            if (data == null && provider == null) {
                throw new RuntimeException("Attachment data source is required.");
            }

            if (provider != null && sourceId == null) {
                throw new RuntimeException("Source is required for provider datasource.");
            }

            return provider != null
                    ? new Attachment(id, type, name, title, fileName, provider, sourceId, inline, versionNo)
                    : new Attachment(id, type, name, title, fileName, data, inline, versionNo);
        }
    }

}

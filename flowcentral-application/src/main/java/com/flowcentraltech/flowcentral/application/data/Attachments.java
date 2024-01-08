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

package com.flowcentraltech.flowcentral.application.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tcdng.unify.core.util.DataUtils;

/**
 * Attachments.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class Attachments {

    private String provider;

    private String caption;

    private List<Attachment> attachments;

    private boolean editable;

    private boolean enableUpload;

    private Attachments(String provider, String caption, List<Attachment> attachments, boolean enableUpload) {
        this.provider = provider;
        this.caption = caption;
        this.attachments = attachments;
        this.enableUpload = enableUpload;
        this.editable = true;
    }

    public String getProvider() {
        return provider;
    }

    public String getCaption() {
        return caption;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public Attachment getAttachment(int index) {
        return attachments.get(index);
    }

    public boolean isEnableUpload() {
        return enableUpload && editable;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public static Builder newBuilder(String provider) {
        return new Builder(provider);
    }

    public static class Builder {

        private String provider;

        private String caption;

        private List<Attachment> attachments;

        private boolean enableUpload;

        public Builder(String provider) {
            this.provider = provider;
            this.attachments = new ArrayList<Attachment>();
        }

        public Builder caption(String caption) {
            this.caption = caption;
            return this;
        }

        public Builder enableUpload(boolean enableUpload) {
            this.enableUpload = enableUpload;
            return this;
        }

        public Builder addAttachments(List<Attachment> attachments) {
            this.attachments.addAll(attachments);
            return this;
        }

        public Builder addAttachments(Attachment attachment) {
            this.attachments.add(attachment);
            return this;
        }

        public Builder addAttachment(Long ownerId, String ownerEntity, Long id, String name, String description,
                String format, Date createdOn, boolean mandatory, boolean enableUpload, boolean present) {
            attachments.add(new Attachment(ownerId, ownerEntity, id, name, description, format, createdOn, enableUpload,
                    mandatory, present));
            return this;
        }

        public Attachments build() {
            return new Attachments(provider, caption, DataUtils.unmodifiableList(attachments), enableUpload);
        }
    }
}

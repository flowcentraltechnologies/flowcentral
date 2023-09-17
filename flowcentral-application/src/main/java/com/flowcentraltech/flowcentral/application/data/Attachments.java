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

    private String caption;

    private List<Attachment> attachments;
    
    public Attachments(String caption, List<Attachment> attachments) {
        this.caption = caption;
        this.attachments = attachments;
    }

    public String getCaption() {
        return caption;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public static Builder newBuilder() {
        return new Builder();
    }
    
    public static class Builder {

        private String caption;
        
        private List<Attachment> attachments;
        
        public Builder() {
            this.attachments = new ArrayList<Attachment>();
        }
        
        public Builder caption(String caption) {
            this.caption = caption;
            return this;
        }
        
        public Builder addAttachment(Long id, String name, String description, String format, Date createdOn) {
            attachments.add(new Attachment(id, name, description, format, createdOn));
            return this;
        }
        
        public Attachments build() {
            return new Attachments(caption, DataUtils.unmodifiableList(attachments));
        }
    }
}

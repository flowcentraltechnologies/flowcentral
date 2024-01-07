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

import java.util.Date;

/**
 * Attachment.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class Attachment {

    private Object ownerId;

    private String ownerEntity;

    private Long id;

    private String name;

    private String description;

    private String format;

    private Date createdOn;

    private boolean enableUpload;

    private boolean mandatory;

    private boolean present;

    public Attachment(Object ownerId, String ownerEntity, Long id, String name, String description, String format,
            Date createdOn, boolean enableUpload, boolean mandatory, boolean present) {
        this.ownerId = ownerId;
        this.id = id;
        this.name = name;
        this.description = description;
        this.format = format;
        this.createdOn = createdOn;
        this.enableUpload = enableUpload;
        this.mandatory = mandatory;
        this.present = present;
    }

    public Object getOwnerId() {
        return ownerId;
    }

    public String getOwnerEntity() {
        return ownerEntity;
    }

    public boolean isEnableUpload() {
        return enableUpload;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFormat() {
        return format;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public boolean isPresent() {
        return present;
    }

    public static Builder newBuilder(Object ownerId, String ownerEntity, Long id, String name, String description) {
        return new Builder(ownerId, ownerEntity, id, name, description);
    }

    public static class Builder {

        private Object ownerId;

        private String ownerEntity;

        private Long id;

        private String name;

        private String description;

        private String format;

        private Date createdOn;

        private boolean enableUpload;

        private boolean mandatory;

        private boolean present;

        public Builder(Object ownerId, String ownerEntity, Long id, String name, String description) {
            this.ownerId = ownerId;
            this.ownerEntity = ownerEntity;
            this.id = id;
            this.name = name;
            this.description = description;
        }

        public Builder format(String format) {
            this.format = format;
            return this;
        }

        public Builder createdOn(Date createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        public Builder enableUpload(boolean enableUpload) {
            this.enableUpload = enableUpload;
            return this;
        }

        public Builder mandatory(boolean mandatory) {
            this.mandatory = mandatory;
            return this;
        }

        public Builder present(boolean present) {
            this.present = present;
            return this;
        }

        public Attachment build() {
            return new Attachment(ownerId, ownerEntity, id, name, description, format, createdOn, enableUpload,
                    mandatory, present);
        }
    }
}

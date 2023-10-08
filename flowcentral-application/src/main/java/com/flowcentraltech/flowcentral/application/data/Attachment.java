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

    private Long id;

    private String name;

    private String description;

    private String format;

    private Date createdOn;

    private boolean mandatory;

    private boolean present;

    public Attachment(Long id, String name, String description, String format, Date createdOn, boolean mandatory,
            boolean present) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.format = format;
        this.createdOn = createdOn;
        this.mandatory = mandatory;
        this.present = present;
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

    public boolean isPresent() {
        return present;
    }

}

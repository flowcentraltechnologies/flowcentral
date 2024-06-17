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
package com.flowcentraltech.flowcentral.application.data;

import java.util.Date;

import com.tcdng.unify.core.util.StringUtils;

/**
 * SnapshotDetails details.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SnapshotDetails {

    private Long snapshotDetailsId;

    private String type;

    private String name;

    private String filename;

    private String message;

    private Date snapshotDate;

    private String snapshotBy;

    public SnapshotDetails(Long snapshotDetailsId, String type, String name, String filename, String message,
            Date snapshotDate, String snapshotBy) {
        this.snapshotDetailsId = snapshotDetailsId;
        this.type = type;
        this.name = name;
        this.filename = filename;
        this.message = message;
        this.snapshotDate = snapshotDate;
        this.snapshotBy = snapshotBy;
    }

    public Long getSnapshotDetailsId() {
        return snapshotDetailsId;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getFilename() {
        return filename;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getSnapshotDate() {
        return snapshotDate;
    }

    public String getSnapshotBy() {
        return snapshotBy;
    }

    @Override
    public String toString() {
        return StringUtils.toXmlString(this);
    }

}

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
package com.flowcentraltech.flowcentral.configuration.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Snapshot configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@XmlRootElement(name = "snapshot")
public class SnapshotConfig extends BaseConfig {

    private String applicationCode;

    private String applicationName;

    private String applicationVersion;

    private String snapshotTimestamp;

    private String snapshotVersion;

    private String snapshotTakenBy;

    private String snapshotType;

    public String getApplicationCode() {
        return applicationCode;
    }

    @XmlElement
    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public String getApplicationName() {
        return applicationName;
    }

    @XmlElement
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    @XmlElement
    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getSnapshotTimestamp() {
        return snapshotTimestamp;
    }

    @XmlElement
    public void setSnapshotTimestamp(String snapshotTimestamp) {
        this.snapshotTimestamp = snapshotTimestamp;
    }

    public String getSnapshotVersion() {
        return snapshotVersion;
    }

    @XmlElement
    public void setSnapshotVersion(String snapshotVersion) {
        this.snapshotVersion = snapshotVersion;
    }

    public String getSnapshotTakenBy() {
        return snapshotTakenBy;
    }

    @XmlElement
    public void setSnapshotTakenBy(String snapshotTakenBy) {
        this.snapshotTakenBy = snapshotTakenBy;
    }

    public String getSnapshotType() {
        return snapshotType;
    }

    @XmlElement
    public void setSnapshotType(String snapshotType) {
        this.snapshotType = snapshotType;
    }

}

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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.tcdng.unify.core.util.xml.adapter.CDataXmlAdapter;

/**
 * Snapshot configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
@JacksonXmlRootElement(localName = "snapshot")
public class SnapshotConfig extends BaseConfig {

    @JacksonXmlProperty
    private String applicationCode;

    @JacksonXmlProperty
    private String applicationName;

    @JacksonXmlProperty
    private String applicationVersion;

    @JacksonXmlProperty
    private String snapshotTimestamp;

    @JacksonXmlProperty
    private String snapshotVersion;

    @JacksonXmlProperty
    private String snapshotTakenBy;

    @JacksonXmlProperty
    private String snapshotType;

    @JsonSerialize(using = CDataXmlAdapter.Serializer.class)
    @JsonDeserialize(using = CDataXmlAdapter.Deserializer.class)
    @JacksonXmlProperty
    private String snapshotTitle;

    @JsonSerialize(using = CDataXmlAdapter.Serializer.class)
    @JsonDeserialize(using = CDataXmlAdapter.Deserializer.class)
    @JacksonXmlProperty
    private String snapshotMessage;

    public String getApplicationCode() {
        return applicationCode;
    }

    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public String getSnapshotTimestamp() {
        return snapshotTimestamp;
    }

    public void setSnapshotTimestamp(String snapshotTimestamp) {
        this.snapshotTimestamp = snapshotTimestamp;
    }

    public String getSnapshotVersion() {
        return snapshotVersion;
    }

    public void setSnapshotVersion(String snapshotVersion) {
        this.snapshotVersion = snapshotVersion;
    }

    public String getSnapshotTakenBy() {
        return snapshotTakenBy;
    }

    public void setSnapshotTakenBy(String snapshotTakenBy) {
        this.snapshotTakenBy = snapshotTakenBy;
    }

    public String getSnapshotType() {
        return snapshotType;
    }

    public void setSnapshotType(String snapshotType) {
        this.snapshotType = snapshotType;
    }

    public String getSnapshotTitle() {
        return snapshotTitle;
    }

    public void setSnapshotTitle(String snapshotTitle) {
        this.snapshotTitle = snapshotTitle;
    }

    public String getSnapshotMessage() {
        return snapshotMessage;
    }

    public void setSnapshotMessage(String snapshotMessage) {
        this.snapshotMessage = snapshotMessage;
    }

}

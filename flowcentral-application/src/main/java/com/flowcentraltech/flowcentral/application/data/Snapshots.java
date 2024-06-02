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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.flowcentraltech.flowcentral.application.web.widgets.BeanListTable;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Snapshots.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class Snapshots {

    private List<SnapshotDetails> details;

    private BeanListTable snapshotsTable;

    private Snapshots(List<SnapshotDetails> details) {
        this.details = details;
    }

    public SnapshotDetails getDetails(int index) {
        return details.get(index);
    }

    public List<SnapshotDetails> getDetails() {
        return details;
    }

    public BeanListTable getSnapshotsTable() {
        return snapshotsTable;
    }

    public void setSnapshotsTable(BeanListTable snapshotsTable) {
        this.snapshotsTable = snapshotsTable;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private List<SnapshotDetails> details;

        public Builder() {
            this.details = new ArrayList<SnapshotDetails>();
        }

        public Builder addDetails(SnapshotDetails details) {
            this.details.add(details);
            return this;
        }

        public Builder addDetails(Long snapshotId, String name, String filename, String message, Date snapshotDate,
                String snapshotBy) {
            details.add(new SnapshotDetails(snapshotId, name, filename, message, snapshotDate, snapshotBy));
            return this;
        }

        public Snapshots build() {
            return new Snapshots(DataUtils.unmodifiableList(details));
        }
    }
}

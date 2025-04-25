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

package com.flowcentraltech.flowcentral.studio.web.controllers;

import com.flowcentraltech.flowcentral.studio.business.data.SnapshotResultDetails;
import com.tcdng.unify.web.ui.AbstractPageBean;

/**
 * Studio take snapshot page bean.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class StudioTakeSnapshotPageBean extends AbstractPageBean {
    
    private String snapshotTitle;
    
    private String message;

    private SnapshotResultDetails resultDetails;
    
    public String getSnapshotTitle() {
        return snapshotTitle;
    }

    public void setSnapshotTitle(String snapshotTitle) {
        this.snapshotTitle = snapshotTitle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SnapshotResultDetails getResultDetails() {
        return resultDetails;
    }

    public void setResultDetails(SnapshotResultDetails resultDetails) {
        this.resultDetails = resultDetails;
    }
    
}

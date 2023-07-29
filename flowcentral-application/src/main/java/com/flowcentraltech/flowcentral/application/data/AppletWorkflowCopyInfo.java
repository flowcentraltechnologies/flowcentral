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

/**
 * Applet workflow copy information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class AppletWorkflowCopyInfo {

    private String appletName;

    private String createApprovalSetValuesName;

    private String updateApprovalSetValuesName;

    private String appletSearchTable;

    private long appletVersionNo;

    public AppletWorkflowCopyInfo(String appletName, String createApprovalSetValuesName,
            String updateApprovalSetValuesName, String appletSearchTable, long appletVersionNo) {
        this.appletName = appletName;
        this.createApprovalSetValuesName = createApprovalSetValuesName;
        this.updateApprovalSetValuesName = updateApprovalSetValuesName;
        this.appletSearchTable = appletSearchTable;
        this.appletVersionNo = appletVersionNo;
    }

    public AppletWorkflowCopyInfo() {

    }

    public String getAppletName() {
        return appletName;
    }

    public String getCreateApprovalSetValuesName() {
        return createApprovalSetValuesName;
    }

    public String getUpdateApprovalSetValuesName() {
        return updateApprovalSetValuesName;
    }

    public String getAppletSearchTable() {
        return appletSearchTable;
    }

    public long getAppletVersionNo() {
        return appletVersionNo;
    }
}

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

import java.util.Map;

import com.flowcentraltech.flowcentral.application.entities.AppAppletAlert;
import com.tcdng.unify.core.util.DataUtils;

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

    private String abortSetValuesName;

    private String appletSearchTable;

    private String onCreateAlertName;

    private String onUpdateAlertName;

    private String onCreateApprovalAlertName;

    private String onUpdateApprovalAlertName;

    private String onCreateRejectionAlertName;

    private String onUpdateRejectionAlertName;

    private long appletVersionNo;

    private Map<String, AppAppletAlert> alerts;
    
    public AppletWorkflowCopyInfo(String appletName, String createApprovalSetValuesName,
            String updateApprovalSetValuesName, String abortSetValuesName, String appletSearchTable, String onCreateAlertName,
            String onUpdateAlertName, String onCreateApprovalAlertName, String onUpdateApprovalAlertName,
            String onCreateRejectionAlertName, String onUpdateRejectionAlertName, long appletVersionNo,
            Map<String, AppAppletAlert> alerts) {
        this.appletName = appletName;
        this.createApprovalSetValuesName = createApprovalSetValuesName;
        this.updateApprovalSetValuesName = updateApprovalSetValuesName;
        this.abortSetValuesName = abortSetValuesName;
        this.appletSearchTable = appletSearchTable;
        this.onCreateAlertName = onCreateAlertName;
        this.onUpdateAlertName = onUpdateAlertName;
        this.onCreateApprovalAlertName = onCreateApprovalAlertName;
        this.onUpdateApprovalAlertName = onUpdateApprovalAlertName;
        this.onCreateRejectionAlertName = onCreateRejectionAlertName;
        this.onUpdateRejectionAlertName = onUpdateRejectionAlertName;
        this.appletVersionNo = appletVersionNo;
        this.alerts = DataUtils.unmodifiableMap(alerts);
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

    public String getAbortSetValuesName() {
        return abortSetValuesName;
    }

    public String getAppletSearchTable() {
        return appletSearchTable;
    }

    public String getOnCreateAlertName() {
        return onCreateAlertName;
    }

    public String getOnUpdateAlertName() {
        return onUpdateAlertName;
    }

    public String getOnCreateApprovalAlertName() {
        return onCreateApprovalAlertName;
    }

    public String getOnUpdateApprovalAlertName() {
        return onUpdateApprovalAlertName;
    }

    public String getOnCreateRejectionAlertName() {
        return onCreateRejectionAlertName;
    }

    public String getOnUpdateRejectionAlertName() {
        return onUpdateRejectionAlertName;
    }

    public long getAppletVersionNo() {
        return appletVersionNo;
    }
    
    public AppAppletAlert getAppAppletAlert(String name) {
        return alerts.get(name);
    }
    
    public boolean isWithAlert(String name) {
        return name != null && alerts.containsKey(name);
    }
}

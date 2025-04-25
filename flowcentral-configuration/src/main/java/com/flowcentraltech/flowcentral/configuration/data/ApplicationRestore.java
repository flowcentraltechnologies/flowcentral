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
package com.flowcentraltech.flowcentral.configuration.data;

import java.util.List;

import com.flowcentraltech.flowcentral.configuration.xml.AppConfig;

/**
 * Application restore configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ApplicationRestore extends ApplicationInstall {

    private List<HelpSheetRestore> helpSheetList;

    private List<ReportRestore> reportList;

    private List<NotifTemplateRestore> notifTemplateList;
    
    private List<NotifLargeTextRestore> notifLargeTextList;
    
    private List<WorkflowRestore> workflowList;
    
    private List<WorkflowWizardRestore> workflowWizardList;
    
    public ApplicationRestore(AppConfig applicationConfig, List<HelpSheetRestore> helpSheetList,
            List<ReportRestore> reportList, List<NotifTemplateRestore> notifTemplateList,
            List<NotifLargeTextRestore> notifLargeTextList, List<WorkflowRestore> workflowList,
            List<WorkflowWizardRestore> workflowWizardList) {
        super(applicationConfig);
        this.helpSheetList = helpSheetList;
        this.reportList = reportList;
        this.notifTemplateList = notifTemplateList;
        this.notifLargeTextList = notifLargeTextList;
        this.workflowList = workflowList;
        this.workflowWizardList = workflowWizardList;
    }

    public List<HelpSheetRestore> getHelpSheetList() {
        return helpSheetList;
    }

    public List<ReportRestore> getReportList() {
        return reportList;
    }

    public List<NotifTemplateRestore> getNotifTemplateList() {
        return notifTemplateList;
    }

    public List<NotifLargeTextRestore> getNotifLargeTextList() {
        return notifLargeTextList;
    }

    public List<WorkflowRestore> getWorkflowList() {
        return workflowList;
    }

    public List<WorkflowWizardRestore> getWorkflowWizardList() {
        return workflowWizardList;
    }

}

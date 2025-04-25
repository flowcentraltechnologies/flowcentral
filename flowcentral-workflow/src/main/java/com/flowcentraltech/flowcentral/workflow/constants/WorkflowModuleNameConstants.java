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
package com.flowcentraltech.flowcentral.workflow.constants;

/**
 * Workflow module name constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface WorkflowModuleNameConstants {

    String WORKFLOW_MODULE_NAME = "workflow";

    String WORKFLOW_MODULE_SERVICE = "workflow-moduleservice";

    String WORKFLOW_USAGE_SERVICE = "workflow-usageservice";

    String APPLICATION_WORKFLOW_INSTALLER = "application-workflowinstaller";

    String REVIEW_WIZARD_CREATE_ACTION_POLICY = "reviewwizard-createactionpolicy";

    String REVIEW_WIZARD_UPDATE_ACTION_POLICY = "reviewwizard-updateactionpolicy";

    String REVIEW_WIZARD_SUBMIT_ACTION_POLICY = "reviewwizard-submitactionpolicy";

    String WORKFLOW_COPY_CREATE_APPROVAL_TEMPLATE = "workflow.copyCreateApproval";

    String WORKFLOW_COPY_UPDATE_APPROVAL_TEMPLATE= "workflow.copyUpdateApproval";

    String WORKFLOW_COPY_DELETION_APPROVAL_TEMPLATE= "workflow.copyDeletionApproval";
    
    String WORKFLOW_MY_WORKITEMS_LOADING_TABLE_PROVIDER = "workflowmyworkitems-loadingtableprovider";
    
    String WORKFLOW_WORKITEMS_TABLE_ACTION_POLICY = "workflowmyworkitems-tableactionpolicy";
}

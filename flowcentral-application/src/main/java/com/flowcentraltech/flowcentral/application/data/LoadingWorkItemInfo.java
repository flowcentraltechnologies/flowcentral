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
package com.flowcentraltech.flowcentral.application.data;

import java.util.Collections;
import java.util.List;

/**
 * Loading work item information..
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class LoadingWorkItemInfo {

    private List<FormActionDef> formActionDefList;

    private String workflowDesc;

    private String stepLabel;

    private boolean readOnly;

    private boolean comments;

    private boolean emails;

    private boolean error;

    private boolean attachments;

    public LoadingWorkItemInfo(List<FormActionDef> formActionDefList, String workflowDesc, String stepLabel, boolean readOnly,
            boolean comments, boolean emails, boolean error, boolean attachments) {
        this.formActionDefList = formActionDefList;
        this.workflowDesc = workflowDesc;
        this.stepLabel = stepLabel;
        this.readOnly = readOnly;
        this.comments = comments;
        this.emails = emails;
        this.error = error;
        this.attachments = attachments;
    }

    public LoadingWorkItemInfo() {
        this.formActionDefList = Collections.emptyList();
        this.readOnly = false;
        this.comments = false;
        this.emails = false;
        this.error = false;
        this.attachments = false;
    }

    public boolean isValidateFormOnAction(String actionName) {
        for (FormActionDef formActionDef: formActionDefList) {
            if (formActionDef.getName().equals(actionName)) {
                return formActionDef.isValidateForm();
            }
        }
        
        return false;
    }
    
    public List<FormActionDef> getFormActionDefList() {
        return formActionDefList;
    }

    public String getWorkflowDesc() {
        return workflowDesc;
    }

    public String getStepLabel() {
        return stepLabel;
    }

    public boolean isWithStepLabel() {
        return stepLabel != null;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean isComments() {
        return comments;
    }

    public boolean isEmails() {
        return emails;
    }

    public boolean isError() {
        return error;
    }

    public boolean isAttachments() {
        return attachments;
    }
}

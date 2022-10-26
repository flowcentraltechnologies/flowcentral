/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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
 * Loading workitem information..
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class LoadingWorkItemInfo {

    private List<FormActionDef> formActionDefList;

    private boolean readOnly;

    private boolean comments;

    private boolean emails;

    private boolean error;

    public LoadingWorkItemInfo(List<FormActionDef> formActionDefList, boolean readOnly, boolean comments,
            boolean emails, boolean error) {
        this.formActionDefList = formActionDefList;
        this.readOnly = readOnly;
        this.comments = comments;
        this.emails = emails;
        this.error = error;
    }

    public LoadingWorkItemInfo() {
        this.formActionDefList = Collections.emptyList();
        this.readOnly = false;
        this.comments = false;
        this.emails = false;
        this.error = false;
    }

    public List<FormActionDef> getFormActionDefList() {
        return formActionDefList;
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
}

/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.workflow.data;

import java.util.Date;

/**
 * Workflow item accessible definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface WfItemAccessible {

    /**
     * Gets work item ID.
     * 
     * @return the work item ID
     */
    Long getId();

    /**
     * Gets item actual record ID.
     * 
     * @return the item actual record ID
     */
    Long getWorkRecId();

    /**
     * Gets item workflow name.
     * 
     * @return the workflow name
     */
    String getWorkflowName();

    /**
     * Gets item workflow step name.
     * 
     * @return the workflow step name
     */
    String getWfStepName();

    /**
     * Gets item entity name.
     * 
     * @return the item entity name
     */
    String getEntity();

    /**
     * Gets workflow item case number.
     * 
     * @return the workflow item number
     */
    String getWfItemCaseNo();

    /**
     * Gets workflow item description.
     * 
     * @return the workflow item description
     */
    String getWfItemDesc();

    /**
     * Gets workflow item's branch.
     * 
     * @return the workflow item's branch
     */
    String getBranchCode();

    /**
     * Gets workflow item's department.
     * 
     * @return the workflow item's department
     */
    String getDepartmentCode();

    /**
     * Gets the timestamp of when item settled in current step
     * 
     * @return the step timestamp
     */
    Date getStepDt();

    /**
     * Gets the time stamp for reminder
     * 
     * @return the reminder timestamp
     */
    Date getReminderDt();

    /**
     * Gets the time stamp for expected resolution
     * 
     * @return the expected timestamp for resolution
     */
    Date getExpectedDt();

    /**
     * Gets the time stamp for when no resolution goes critical.
     * 
     * @return the critical timestamp
     */
    Date getCriticalDt();

}

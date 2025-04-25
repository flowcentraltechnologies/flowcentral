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
package com.flowcentraltech.flowcentral.common.entities;

import com.tcdng.unify.common.constants.ProcessingStatus;
import com.tcdng.unify.common.constants.WfItemVersionType;

/**
 * Base query object for base work entity sub-classes.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class BaseWorkEntityQuery<T extends BaseWorkEntity> extends BaseAuditEntityQuery<T> {

    public BaseWorkEntityQuery(Class<T> entityClass) {
        super(entityClass);
    }

    public BaseWorkEntityQuery(Class<T> entityClass, boolean applyAppQueryLimit) {
        super(entityClass, applyAppQueryLimit);
    }

    public final BaseWorkEntityQuery<T> workBranchCode(String workBranchCode) {
        return (BaseWorkEntityQuery<T>) addEquals("workBranchCode", workBranchCode);
    }

    public final BaseWorkEntityQuery<T> workBranchCodeIsNull() {
        return (BaseWorkEntityQuery<T>) addIsNull("workBranchCode");
    }

    public final BaseWorkEntityQuery<T> workDepartmentCode(String workDepartmentCode) {
        return (BaseWorkEntityQuery<T>) addEquals("workDepartmentCode", workDepartmentCode);
    }

    public final BaseWorkEntityQuery<T> workDepartmentCodeIsNull() {
        return (BaseWorkEntityQuery<T>) addIsNull("workDepartmentCode");
    }

    public final BaseWorkEntityQuery<T> inWorkflow(boolean inWorkflow) {
        return (BaseWorkEntityQuery<T>) addEquals("inWorkflow", inWorkflow);
    }

    public final BaseWorkEntityQuery<T> processingStatus(ProcessingStatus processingStatus) {
        return (BaseWorkEntityQuery<T>) addEquals("processingStatus", processingStatus);
    }

    public final BaseWorkEntityQuery<T> originalCopyId(Long originalCopyId) {
        return (BaseWorkEntityQuery<T>) addEquals("originalCopyId", originalCopyId);
    }
    
    public final BaseWorkEntityQuery<T> isOriginalVersion() {
        return (BaseWorkEntityQuery<T>) addEquals("wfItemVersionType", WfItemVersionType.ORIGINAL);
    }
    
    public final BaseWorkEntityQuery<T> isDraftVersion() {
        return (BaseWorkEntityQuery<T>) addEquals("wfItemVersionType", WfItemVersionType.DRAFT);
    }
}

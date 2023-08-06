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
package com.flowcentraltech.flowcentral.common.entities;

import java.util.List;

import com.flowcentraltech.flowcentral.common.constants.WfItemVersionType;
import com.flowcentraltech.flowcentral.configuration.constants.ProcessingStatus;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.DefaultQueryRestrictions;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Policy;
import com.tcdng.unify.core.annotation.QueryRestriction;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.UniqueConstraints;

/**
 * Base class for all work entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Policy("basework-entitypolicy")
@DefaultQueryRestrictions({ @QueryRestriction(field = "wfItemVersionType", value = "ORN") })
@UniqueConstraints(baseFields = { "originalCopyId" },
    value = { @UniqueConstraint(value = { "wfItemVersionType" }) })
public abstract class BaseWorkEntity extends BaseAuditEntity implements WorkEntity {

    @ForeignKey(name = "PROCESSING_STATUS", nullable = true)
    private ProcessingStatus processingStatus;

    @Column
    private WfItemVersionType wfItemVersionType;

    @Column(name = "WORK_BRANCH_CD", length = 32, nullable = true)
    private String workBranchCode;

    @Column(name = "WORK_DEPARTMENT_CD", length = 32, nullable = true)
    private String workDepartmentCode;

    @Column(nullable = true)
    private Long originalCopyId;

    @Column
    private boolean inWorkflow;

    @ListOnly(key = "processingStatus", property = "description")
    private String processingStatusDesc;

    @ChildList(category = "work")
    private List<FileAttachment> attachmentList;

    @Override
    public String getWorkflowItemDesc() {
        return getDescription();
    }

    @Override
    public Long getTenantId() {
        return null;
    }

    @Override
    public final ProcessingStatus getProcessingStatus() {
        return processingStatus;
    }

    @Override
    public final void setProcessingStatus(ProcessingStatus processingStatus) {
        this.processingStatus = processingStatus;
    }

    @Override
    public final WfItemVersionType getWfItemVersionType() {
        return wfItemVersionType;
    }

    @Override
    public final void setWfItemVersionType(WfItemVersionType wfItemVersionType) {
        this.wfItemVersionType = wfItemVersionType;
    }

    @Override
    public final String getWorkBranchCode() {
        return workBranchCode;
    }

    @Override
    public final void setWorkBranchCode(String workBranchCode) {
        this.workBranchCode = workBranchCode;
    }

    @Override
    public final String getWorkDepartmentCode() {
        return workDepartmentCode;
    }

    @Override
    public final void setWorkDepartmentCode(String workDepartmentCode) {
        this.workDepartmentCode = workDepartmentCode;
    }

    @Override
    public Long getOriginalCopyId() {
        return originalCopyId;
    }

    @Override
    public void setOriginalCopyId(Long originalCopyId) {
        this.originalCopyId = originalCopyId;
    }

    @Override
    public final boolean isInWorkflow() {
        return inWorkflow;
    }

    @Override
    public final void setInWorkflow(boolean inWorkflow) {
        this.inWorkflow = inWorkflow;
    }

    public final String getProcessingStatusDesc() {
        return processingStatusDesc;
    }

    public final void setProcessingStatusDesc(String processingStatusDesc) {
        this.processingStatusDesc = processingStatusDesc;
    }

    public final List<FileAttachment> getAttachmentList() {
        return attachmentList;
    }

    public final void setAttachmentList(List<FileAttachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

}

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

package com.flowcentraltech.flowcentral.workflow.entities;

import java.util.List;

import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Policy;

/**
 * Workflow entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Policy("workflowpolicy")
@Table(name = "FC_WORKFLOW")
public class Workflow extends BaseApplicationEntity {

    @Column(name = "FLOW_LABEL", length = 64)
    private String label;

    @Column(length = 128)
    private String entity;
    
    @Column(length = 128, nullable = true)
    private String loadingTable;

    @Column(length = 128, nullable = true)
    private String descFormat;
    
    @Column
    private long appletVersionNo;
    
    @Column
    private boolean supportMultiItemAction;
    
    @Column
    private boolean published;
    
    @Column
    private boolean runnable;
   
    @ChildList
    private List<WorkflowFilter> filterList;
    
    @ChildList
    private List<WorkflowSetValues> setValuesList;

    @ChildList
    private List<WfStep> stepList;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getLoadingTable() {
        return loadingTable;
    }

    public void setLoadingTable(String loadingTable) {
        this.loadingTable = loadingTable;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public boolean isRunnable() {
        return runnable;
    }

    public void setRunnable(boolean runnable) {
        this.runnable = runnable;
    }

    public String getDescFormat() {
        return descFormat;
    }

    public void setDescFormat(String descFormat) {
        this.descFormat = descFormat;
    }

    public long getAppletVersionNo() {
        return appletVersionNo;
    }

    public void setAppletVersionNo(long appletVersionNo) {
        this.appletVersionNo = appletVersionNo;
    }

    public boolean isSupportMultiItemAction() {
        return supportMultiItemAction;
    }

    public void setSupportMultiItemAction(boolean supportMultiItemAction) {
        this.supportMultiItemAction = supportMultiItemAction;
    }

    public List<WorkflowFilter> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<WorkflowFilter> filterList) {
        this.filterList = filterList;
    }

    public List<WorkflowSetValues> getSetValuesList() {
        return setValuesList;
    }

    public void setSetValuesList(List<WorkflowSetValues> setValuesList) {
        this.setValuesList = setValuesList;
    }

    public List<WfStep> getStepList() {
        return stepList;
    }

    public void setStepList(List<WfStep> stepList) {
        this.stepList = stepList;
    }

}

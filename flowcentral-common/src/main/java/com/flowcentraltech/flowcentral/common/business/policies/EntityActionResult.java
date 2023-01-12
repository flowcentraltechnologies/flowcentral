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

package com.flowcentraltech.flowcentral.common.business.policies;

import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Entity action result
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityActionResult {

    private TaskSetup resultTaskSetup;

    private String resultTaskCaption;

    private String resultPath;

    private String successHint;

    private String userAction;

    private Object result;

    private ReviewResult reviewResult;
    
    private ActionMode actionMode;
    
    private boolean skipUpdate;
    
    private boolean submitToWorkflow;
    
    private boolean applyUserAction;
    
    private boolean refreshMenu;

    private boolean closeView;

    private boolean closePage;

    private boolean hidePopupOnly;

    private boolean displayListingReport;

    public EntityActionResult(EntityActionContext ctx, TaskSetup resultTaskSetup, String resultTaskCaption) {
        this.resultTaskSetup = resultTaskSetup;
        this.resultTaskCaption = resultTaskCaption;
        this.result = ctx.getResult();
    }

    public EntityActionResult(EntityActionContext ctx, String resultPath) {
        this.resultPath = resultPath;
        this.result = ctx.getResult();
    }

    public EntityActionResult(EntityActionContext ctx) {
        this.result = ctx.getResult();
    }

    public EntityActionResult() {

    }

    public TaskSetup getResultTaskSetup() {
        return resultTaskSetup;
    }

    public String getResultTaskCaption() {
        return resultTaskCaption;
    }

    public ActionMode getActionMode() {
        return actionMode;
    }

    public void setActionMode(ActionMode actionMode) {
        this.actionMode = actionMode;
    }

    public boolean isSkipUpdate() {
        return skipUpdate;
    }

    public void setSkipUpdate(boolean skipUpdate) {
        this.skipUpdate = skipUpdate;
    }

    public boolean isSubmitToWorkflow() {
        return submitToWorkflow;
    }

    public void setSubmitToWorkflow(boolean submitToWorkflow) {
        this.submitToWorkflow = submitToWorkflow;
    }

    public String getUserAction() {
        return userAction;
    }

    public void setUserAction(String userAction) {
        this.userAction = userAction;
    }

    public boolean isApplyUserAction() {
        return applyUserAction;
    }

    public void setApplyUserAction(boolean applyUserAction) {
        this.applyUserAction = applyUserAction;
    }

    public String getResultPath() {
        return resultPath;
    }

    public Object getResult() {
        return result;
    }

    public ReviewResult getReviewResult() {
        return reviewResult;
    }

    public void setReviewResult(ReviewResult reviewResult) {
        this.reviewResult = reviewResult;
    }

    public boolean isWithReviewResult() {
        return reviewResult != null;
    }

    public boolean isWithTaskResult() {
        return resultTaskSetup != null;
    }

    public boolean isWithResultPath() {
        return !StringUtils.isBlank(resultPath);
    }

    public boolean isRefreshMenu() {
        return refreshMenu;
    }

    public void setRefreshMenu(boolean refreshMenu) {
        this.refreshMenu = refreshMenu;
    }

    public boolean isHidePopupOnly() {
        return hidePopupOnly;
    }

    public void setHidePopupOnly(boolean hidePopupOnly) {
        this.hidePopupOnly = hidePopupOnly;
    }

    public boolean isClosePage() {
        return closePage;
    }

    public void setClosePage(boolean closePage) {
        this.closePage = closePage;
    }

    public boolean isCloseView() {
        return closeView;
    }

    public void setCloseView(boolean closeView) {
        this.closeView = closeView;
    }

    public boolean isDisplayListingReport() {
        return displayListingReport;
    }

    public void setDisplayListingReport(boolean displayListingReport) {
        this.displayListingReport = displayListingReport;
    }

    public String getSuccessHint() {
        return successHint;
    }

    public void setSuccessHint(String successHint) {
        this.successHint = successHint;
    }

}

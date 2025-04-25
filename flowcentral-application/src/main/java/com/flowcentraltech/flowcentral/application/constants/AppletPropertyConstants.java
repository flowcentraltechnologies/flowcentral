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

package com.flowcentraltech.flowcentral.application.constants;

/**
 * Applet property constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface AppletPropertyConstants {

    String PAGE_ALTERNATE_CAPTION = "page.alternateCaption";

    String PAGE_ALTERNATE_SUBCAPTION = "page.alternateSubCaption";

    String PAGE_MAINTAIN_CAPTION = "page.maintainCaption";

    String PAGE_LISTING_CAPTION = "page.listingCaption";

    String LOADING_TABLE = "loadingTable";
    
    String LOADING_TABLE_ACTIONFOOTER = "loadingTable.actionFooter";

    String SEARCH_TABLE = "searchTable";

    String SEARCH_TABLE_SEARCHINPUT = "searchTable.searchInput";

    String SEARCH_TABLE_NEW = "searchTable.new";

    String SEARCH_TABLE_NEW_CAPTION= "searchTable.new.caption";

    String SEARCH_TABLE_MULTISELECT_NEW_REF= "searchTable.new.multiselect.ref";

    String SEARCH_TABLE_TREEEMULTISELECT_NEW_GENERATOR = "searchTable.new.treemultiselect.gen";
    
    String SEARCH_TABLE_EDIT = "searchTable.edit";

    String SEARCH_TABLE_REPORT = "searchTable.report";

    String SEARCH_TABLE_QUICKFILTER = "searchTable.quickFilter";
    
    String SEARCH_TABLE_QUICKFILTER_DEFAULT = "searchTable.quickFilter.default";
    
    String SEARCH_TABLE_ACTIONFOOTER = "searchTable.actionFooter";

    String SEARCH_TABLE_BASICSEARCHONLY = "searchTable.basicSearchOnly";
    
    String SEARCH_TABLE_SEARCH_COLUMNS = "searchTable.searchColumns";
    
    String SEARCH_TABLE_SELECT_BY_CONSTRAINT = "searchTable.selectByConstraint";
    
    String SEARCH_TABLE_SEARCH_ON_CRITERIA_ONLY = "searchTable.searchOnCriteriaOnly";
    
    String SEARCH_TABLE_VIEW_ITEM_SEPARATE_TAB = "searchTable.viewItemInSeparateTab";
    
    String SEARCH_TABLE_SHOW_EXPANDED_DETAILS = "searchTable.showExpandedDetails";

    String SEARCH_TABLE_DIFF_IGNORE = "searchTable.diffIgnore";
    
    String ENTITY_FORM = "entityForm";
    
    String ENTITY_FORM_CLOSE_DETACHED_ONSUBMIT = "entityForm.submit.close";

    String LONGFORM_SUPPORT = "longForm.support";

    String CREATE_FORM = "createForm";

    String CREATE_FORM_NEW_CAPTION= "createForm.new.caption";

    String CREATE_FORM_NEW_POLICY = "createForm.new.policy";

    String CREATE_FORM_NAVIGATION_POLICY = "createForm.navigation.policy";

    String CREATE_FORM_SUBMIT = "createForm.submit";

    String CREATE_FORM_SUBMIT_CAPTION = "createForm.submit.caption";

    String CREATE_FORM_SUBMIT_POLICY = "createForm.submit.policy";

    String CREATE_FORM_SUBMIT_VALIDATE = "createForm.submit.validate";

    String CREATE_FORM_SUBMIT_WORKFLOW_CHANNEL = "createForm.submit.workflow.channel";

    String CREATE_FORM_SUBMIT_NEXT = "createForm.submitnext";

    String CREATE_FORM_SUBMIT_NEXT_CAPTION = "createForm.submitnext.caption";

    String CREATE_FORM_SUBMIT_BUTTON_HIGHLIGHT = "createForm.submit.buttonhighlight";

    String CREATE_FORM_SUBMIT_CONDITION = "createForm.submit.condition";

    String CREATE_FORM_SAVE = "createForm.save";

    String CREATE_FORM_SAVE_NEXT = "createForm.save.next";

    String CREATE_FORM_STATE_POLICY = "createForm.onCreate.state.policy";

    String CREATE_FORM_SAVE_CLOSE = "createForm.save.close";

    String LISTING_FORM = "listingForm";

    String MAINTAIN_FORM = "maintainForm";

    String MAINTAIN_FORM_UPDATE = "maintainForm.update";

    String MAINTAIN_FORM_UPDATE_POLICY = "maintainForm.update.policy";

    String MAINTAIN_FORM_UPDATE_CONDITION = "maintainForm.update.condition";

    String MAINTAIN_FORM_SUBMIT = "maintainForm.submit";

    String MAINTAIN_FORM_SUBMIT_CAPTION = "maintainForm.submit.caption";

    String MAINTAIN_FORM_SUBMIT_POLICY = "maintainForm.submit.policy";

    String MAINTAIN_FORM_SUBMIT_VALIDATE = "maintainForm.submit.validate";

    String MAINTAIN_FORM_SUBMIT_WORKFLOW_CHANNEL = "maintainForm.submit.workflow.channel";

    String MAINTAIN_FORM_SUBMIT_NEXT = "maintainForm.submitnext";

    String MAINTAIN_FORM_SUBMIT_CONDITION = "maintainForm.submit.condition";

    String MAINTAIN_FORM_CAPTURE = "maintainForm.capture";

    String MAINTAIN_FORM_CAPTURE_ATTACHMENT_PROVIDER = "maintainForm.capture.attachmentprovider";

    String MAINTAIN_FORM_DELETE = "maintainForm.delete";

    String MAINTAIN_FORM_DELETE_PSEUDO = "maintainForm.delete.pseudo";

    String MAINTAIN_FORM_DELETE_PSEUDO_SETVALUES = "maintainForm.delete.pseudo.setvalues";

    String MAINTAIN_FORM_DELETE_POLICY = "maintainForm.delete.policy";

    String MAINTAIN_FORM_DELETE_CONDITION = "maintainForm.delete.condition";

    String MAINTAIN_FORM_ATTACHMENTS = "maintainForm.attachments";

    String MAINTAIN_FORM_ATTACHMENTS_ADHOC = "maintainForm.attachments.adhoc";

    String MAINTAIN_FORM_SAVEAS = "maintainForm.saveas";

    String MAINTAIN_FORM_SAVEAS_POLICY = "maintainForm.saveas.policy";

    String ASSIGNMENT_PAGE = "assignmentPage";

    String ASSIGNMENT_ENTRY_TABLE = "assignmentEntryTable";

    String ASSIGNMENT_ENTRY_TABLE_POLICY = "assignmentEntryTable.policy";

    String ASSIGNMENT_FIXED = "assignmentFixed";

    String ASSIGNMENT_PSEUDO_DELETE = "assignment.pseudo.delete";
    
    String ENTRY_TABLE = "entryTable";

    String ENTRY_TABLE_POLICY = "entryTable.policy";

    String LISTING_REDIRECT_POLICY = "listingRedirect.policy";

    String QUICK_EDIT_FORM = "quickEdit.form";

    String QUICK_EDIT_TABLE = "quickEdit.table";

    String QUICK_EDIT_POLICY = "quickEdit.policy";

    String QUICK_EDIT_WIDTH = "quickEdit.width";

    String QUICK_EDIT_HEIGHT = "quickEdit.height";

    String QUICK_ORDER_FIELD = "quickOrder.field";

    String BASE_RESTRICTION = "baseRestriction";

    String PROPERTY_LIST_RULE = "propertyList.rule";

    String PROPERTY_LIST_UPDATE = "propertyList.update";

    String TASKEXECUTION_TASKNAME = "taskexecution.taskName";

    String IMPORTDATA_CONFIGNAME = "importdata.configName";

    String IMPORTDATA_ROUTETO_APPLETNAME = "importdata.routeTo.appletName";
    
    String HEADLESS_TABS_APPLETS = "headlesstabs.applets";

    String SINGLE_FORM_PANEL = "singleForm.panel";

    String PAGE_MULTIPLE = "page.multiple";    

    String WIZARD_FORM_COMPLETION = "wizardForm.completion";    

    String WIZARD_FORM_TASK_PROCESSOR = "wizardForm.task.processor";    
    
    String WORKFLOWCOPY = "workflowCopy";

    String WORKFLOWCOPY_ATTACHMENT_PROVIDER = "workflowCopy.attachmentprovider";

    String WORKFLOWCOPY_CREATE_COPY_SETVALUES = "workflowCopy.create.copy.setvalues";

    String WORKFLOWCOPY_CREATE_SUBMIT_ALERT = "workflowCopy.create.alert";

    String WORKFLOWCOPY_CREATE_SUBMIT_SETVALUES = "workflowCopy.create.setvalues";

    String WORKFLOWCOPY_CREATE_APPROVAL_ALERT = "workflowCopy.create.approval.alert";

    String WORKFLOWCOPY_CREATE_APPROVAL_SETVALUES = "workflowCopy.create.approval.setvalues";

    String WORKFLOWCOPY_CREATE_APPROVAL_LEVELS = "workflowCopy.create.approval.levels";

    String WORKFLOWCOPY_CREATE_REJECTION_ALERT = "workflowCopy.create.rejection.alert";

    String WORKFLOWCOPY_CREATE_REJECTION_SETVALUES = "workflowCopy.create.rejection.setvalues";

    String WORKFLOWCOPY_CREATE_RESUBMIT_ALERT = "workflowCopy.create.resubmit.alert";

    String WORKFLOWCOPY_CREATE_RESUBMIT_SETVALUES = "workflowCopy.create.resubmit.setvalues";

    String WORKFLOWCOPY_CREATE_DISCARD_ALERT = "workflowCopy.create.discard.alert";

    String WORKFLOWCOPY_CREATE_DISCARD_SETVALUES = "workflowCopy.create.discard.setvalues";

    String WORKFLOWCOPY_CREATE_ABORT_ALERT = "workflowCopy.create.abort.alert";

    String WORKFLOWCOPY_CREATE_ABORT_SETVALUES = "workflowCopy.create.abort.setvalues";
    

    String WORKFLOWCOPY_UPDATE_COPY_SETVALUES = "workflowCopy.update.copy.setvalues";

    String WORKFLOWCOPY_UPDATE_SUBMIT_ALERT= "workflowCopy.update.alert";

    String WORKFLOWCOPY_UPDATE_SUBMIT_SETVALUES = "workflowCopy.update.setvalues";

    String WORKFLOWCOPY_UPDATE_APPROVAL_ALERT = "workflowCopy.update.approval.alert";

    String WORKFLOWCOPY_UPDATE_APPROVAL_SETVALUES = "workflowCopy.update.approval.setvalues";

    String WORKFLOWCOPY_UPDATE_APPROVAL_LEVELS = "workflowCopy.update.approval.levels";

    String WORKFLOWCOPY_UPDATE_REJECTION_ALERT = "workflowCopy.update.rejection.alert";

    String WORKFLOWCOPY_UPDATE_REJECTION_SETVALUES = "workflowCopy.update.rejection.setvalues";

    String WORKFLOWCOPY_UPDATE_RESUBMIT_ALERT = "workflowCopy.update.resubmit.alert";

    String WORKFLOWCOPY_UPDATE_RESUBMIT_SETVALUES = "workflowCopy.update.resubmit.setvalues";

    String WORKFLOWCOPY_UPDATE_DISCARD_ALERT = "workflowCopy.update.discard.alert";

    String WORKFLOWCOPY_UPDATE_DISCARD_SETVALUES = "workflowCopy.update.discard.setvalues";

    String WORKFLOWCOPY_UPDATE_ABORT_ALERT = "workflowCopy.update.abort.alert";

    String WORKFLOWCOPY_UPDATE_ABORT_SETVALUES = "workflowCopy.update.abort.setvalues";
    

    String WORKFLOWCOPY_DELETE_SUBMIT_ALERT= "workflowCopy.delete.alert";

    String WORKFLOWCOPY_DELETE_SUBMIT_SETVALUES = "workflowCopy.delete.setvalues";

    String WORKFLOWCOPY_DELETE_APPROVAL_ALERT = "workflowCopy.delete.approval.alert";

    String WORKFLOWCOPY_DELETE_APPROVAL_SETVALUES = "workflowCopy.delete.approval.setvalues";

    String WORKFLOWCOPY_DELETE_APPROVAL_LEVELS = "workflowCopy.delete.approval.levels";

    String WORKFLOWCOPY_DELETE_REJECTION_ALERT = "workflowCopy.delete.rejection.alert";

    String WORKFLOWCOPY_DELETE_REJECTION_SETVALUES = "workflowCopy.delete.rejection.setvalues";

    String WORKFLOWCOPY_DELETE_ABORT_ALERT = "workflowCopy.delete.abort.alert";

    String WORKFLOWCOPY_DELETE_ABORT_SETVALUES = "workflowCopy.delete.abort.setvalues";
    
    
}

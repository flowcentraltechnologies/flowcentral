/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.application.web.panels.applet;

import java.util.List;

import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationResultMappingConstants;
import com.flowcentraltech.flowcentral.application.constants.WorkflowDraftType;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.FormActionDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormTabDef;
import com.flowcentraltech.flowcentral.application.data.TabDef;
import com.flowcentraltech.flowcentral.application.data.WorkflowDraftInfo;
import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.application.util.HtmlUtils;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.flowcentraltech.flowcentral.application.web.panels.EditPropertyList;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySaveAs;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySearchValueMarkerConstants;
import com.flowcentraltech.flowcentral.application.web.panels.EntryTablePage;
import com.flowcentraltech.flowcentral.application.web.panels.FormPanel;
import com.flowcentraltech.flowcentral.application.web.panels.HeaderWithTabsForm;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet.TabSheetItem;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheetWidget;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.FileAttachmentProvider;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.FormValidationContext;
import com.flowcentraltech.flowcentral.common.business.policies.ReviewResult;
import com.flowcentraltech.flowcentral.common.business.policies.TableActionResult;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.constants.FileAttachmentCategoryType;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralRequestAttributeConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralResultMappingConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.flowcentraltech.flowcentral.common.data.TargetFormMessage.FieldTarget;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.data.IndexedTarget;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ResultMappingConstants;
import com.tcdng.unify.web.ui.constant.MessageType;
import com.tcdng.unify.web.ui.widget.Panel;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;
import com.tcdng.unify.web.ui.widget.data.MessageBoxCaptions;
import com.tcdng.unify.web.ui.widget.data.MessageIcon;
import com.tcdng.unify.web.ui.widget.data.MessageMode;
import com.tcdng.unify.web.ui.widget.data.MessageResult;

/**
 * Convenient abstract base panel for entity form applet panels.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@UplBinding("web/application/upl/entityformappletpanel.upl")
public abstract class AbstractEntityFormAppletPanel extends AbstractAppletPanel {

    private static final String IN_WORKFLOW_DRAFT_LOOP_FLAG = "IN_WORKFLOW_DRAFT_LOOP_FLAG";

    @Configurable
    protected ApplicationPrivilegeManager applicationPrivilegeManager;

    @Configurable
    private FileAttachmentProvider fileAttachmentProvider;

    private String focusMemoryId;

    private String tabMemoryId;

    @Override
    public void onPageConstruct() throws UnifyException {
        super.onPageConstruct();
        focusMemoryId = getWidgetByShortName("focusMemory").getId();
        tabMemoryId = getWidgetByShortName("tabMemory").getId();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        final AbstractEntityFormApplet applet = getEntityFormApplet();
        applet.ensureFormStruct();

        final AppletDef formAppletDef = applet.getFormAppletDef();
        logDebug("Switching form applet panel [{0}]...", formAppletDef != null ? formAppletDef.getLongName() : null);
        final AppletContext appCtx = applet.getCtx();
        final boolean isCollaboration = applet.isCollaboration() && collaborationProvider() != null;
        final AbstractEntityFormApplet.ViewMode viewMode = applet.getMode();
        final String roleCode = getUserToken().getRoleCode();
        final AbstractForm form = applet.getResolvedForm();
        final Entity inst = form != null ? (Entity) form.getFormBean() : null;
        final boolean isRootForm = applet.isRootForm();
        final boolean isWorkflowCopyForm = isRootForm && formAppletDef != null
                && formAppletDef.getPropValue(boolean.class, AppletPropertyConstants.WORKFLOWCOPY);
        final boolean isInWorkflow = form != null && form.isInWorkflow();
        final boolean isUpdateDraft = form != null && form.isUpdateDraft();
        if (isRootForm) {
            appCtx.setInWorkflow(isInWorkflow);
        }

        final boolean isContextEditable = appCtx.isContextEditable();
        boolean enableSaveAs = false;
        boolean enableUpdate = false;
        boolean enableDelete = false;
        boolean enableCreate = false;
        boolean enableAttachment = false;
        boolean enableCreateSubmit = false;
        boolean enableUpdateSubmit = false;
        boolean capture = false;
        if (viewMode.isCreateForm()) {
            EntityDef formEntityDef = form.getFormDef().getEntityDef();
            enableCreate = isContextEditable
                    && applicationPrivilegeManager.isRoleWithPrivilege(roleCode, formEntityDef.getAddPrivilege());
            enableCreateSubmit = isRootForm && applet
                    .formBeanMatchAppletPropertyCondition(AppletPropertyConstants.CREATE_FORM_SUBMIT_CONDITION);
        } else if (viewMode.isMaintainForm()) {
            if (form.isSingleFormType()) {
                final AppletDef _appletDef = applet.getRootAppletDef();
                final EntityDef _entityDef = applet.getEntityDef();
                capture = _appletDef.getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_CAPTURE, false);
                enableUpdate = isContextEditable
                        && _appletDef.getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_UPDATE, false)
                        && applicationPrivilegeManager.isRoleWithPrivilege(roleCode, _entityDef.getEditPrivilege())
                        && applet.formBeanMatchAppletPropertyCondition(
                                AppletPropertyConstants.MAINTAIN_FORM_UPDATE_CONDITION);
                enableDelete = !isInWorkflow && isContextEditable
                        && _appletDef.getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_DELETE, false)
                        && applicationPrivilegeManager.isRoleWithPrivilege(roleCode, _entityDef.getDeletePrivilege())
                        && applet.formBeanMatchAppletPropertyCondition(
                                AppletPropertyConstants.MAINTAIN_FORM_DELETE_CONDITION);
                enableUpdateSubmit = !isInWorkflow && applet
                        .formBeanMatchAppletPropertyCondition(AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_CONDITION);
            } else {
                EntityDef formEntityDef = form.getFormDef().getEntityDef();
                if (formAppletDef != null) { // Normal, null for workflow applet root
                    capture = formAppletDef.getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_CAPTURE,
                            false);
                    enableSaveAs = formAppletDef.getPropValue(boolean.class,
                            AppletPropertyConstants.MAINTAIN_FORM_SAVEAS, false)
                            && applicationPrivilegeManager.isRoleWithPrivilege(roleCode,
                                    formEntityDef.getAddPrivilege());
                    enableUpdate = isContextEditable
                            && formAppletDef.getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_UPDATE,
                                    false)
                            && applicationPrivilegeManager.isRoleWithPrivilege(roleCode,
                                    formEntityDef.getEditPrivilege())
                            && applet.formBeanMatchAppletPropertyCondition(
                                    AppletPropertyConstants.MAINTAIN_FORM_UPDATE_CONDITION);
                    enableDelete = !isInWorkflow && isContextEditable
                            && formAppletDef.getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_DELETE,
                                    false)
                            && applicationPrivilegeManager.isRoleWithPrivilege(roleCode,
                                    formEntityDef.getDeletePrivilege())
                            && applet.formBeanMatchAppletPropertyCondition(
                                    AppletPropertyConstants.MAINTAIN_FORM_DELETE_CONDITION);
                    enableAttachment = isRootForm
                            && formAppletDef.getPropValue(boolean.class,
                                    AppletPropertyConstants.MAINTAIN_FORM_ATTACHMENTS, false)
                            && (formEntityDef.isWithAttachments() || formAppletDef.getPropValue(boolean.class,
                                    AppletPropertyConstants.MAINTAIN_FORM_ATTACHMENTS_ADHOC, false));
                    enableUpdateSubmit = !isInWorkflow && isRootForm && applet.formBeanMatchAppletPropertyCondition(
                            AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_CONDITION);
                    if (enableAttachment) {
                        applet.setFileAttachmentsDisabled(!applicationPrivilegeManager.isRoleWithPrivilege(roleCode,
                                formEntityDef.getAttachPrivilege()));
                    }
                } else {
                    enableUpdate = isContextEditable
                            && applicationPrivilegeManager.isRoleWithPrivilege(roleCode,
                                    formEntityDef.getEditPrivilege())
                            && applet.formBeanMatchAppletPropertyCondition(
                                    AppletPropertyConstants.MAINTAIN_FORM_UPDATE_CONDITION);
                    enableDelete = false;
                    enableAttachment = isRootForm && formEntityDef.isWithAttachments();
                }

                if (enableAttachment) {
                    applet.getFormFileAttachments().setDisabled(!isContextEditable);
                    form.setAttachmentCount(
                            fileAttachmentProvider.countFileAttachments(FileAttachmentCategoryType.FORM_CATEGORY,
                                    formEntityDef.getLongName(), (Long) inst.getId()));
                }
            }
        }

        appCtx.setCapture(capture);
        if (viewMode.isInForm()) {
            boolean showAlternateFormActions = au().system().getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.SHOW_FORM_ALTERNATE_ACTIONS);
            if (viewMode.isSingleForm()) {
                setVisible("singleFormPanel.altActionPanel", showAlternateFormActions);
                setVisible("singleFormPanel.emailsPanel", appCtx.isReview() && appCtx.isEmails());
                setVisible("singleFormPanel.attachmentsPanel",
                        appCtx.isCapture() || (appCtx.isAttachments() && appCtx.isReview()));
                setVisible("singleFormPanel.commentsPanel", appCtx.isReview() && appCtx.isComments());
                setVisible("singleFormPanel.errorsPanel", appCtx.isReview() && appCtx.isRecovery());
                setVisible("sfrmActionBtns", !DataUtils.isBlank(form.getFormActionDefList()));
                setEditable("singleFormPanel.errorsPanel", false);
                Panel singleFormPanel = getWidgetByShortName(Panel.class, "singleFormPanel");
                setPageAttribute("singleFormPanel.id", singleFormPanel.getId());
            } else {
                setVisible("formPanel.altActionPanel", showAlternateFormActions);
                setVisible("formPanel.emailsPanel", isRootForm && appCtx.isReview() && appCtx.isEmails());
                setVisible("formPanel.attachmentsPanel",
                        appCtx.isCapture() || (appCtx.isAttachments() && appCtx.isReview()));
                setVisible("formPanel.commentsPanel", isRootForm && appCtx.isReview() && appCtx.isComments());
                setVisible("formPanel.errorsPanel", isRootForm && appCtx.isReview() && appCtx.isRecovery());
                setVisible("frmActionBtns", !DataUtils.isBlank(form.getFormActionDefList()));
                setEditable("formPanel.errorsPanel", false);
                Panel formPanel = getWidgetByShortName(Panel.class, "formPanel");
                setPageAttribute("formPanel.id", formPanel.getId());
                form.getCtx().setFocusMemoryId(focusMemoryId);
                form.getCtx().setTabMemoryId(tabMemoryId);
            }
        }

        boolean parentDisabled = false;
        boolean showReviewFormCaption = false;
        if (form != null) {
            form.getCtx().setUpdateEnabled(enableUpdate);
            if (form.isWithAttachments()) {
                form.getAttachments()
                        .setErrorMsg(form.getCtx().isWithSectionError("documents")
                                ? form.getCtx().getSectionError("documents").get(0)
                                : null);
            }

            final String displayCounter = form.getDisplayItemCounter();
            form.clearDisplayItem();

            if (isCollaboration) {
                if (isContextEditable) {
                    form.setDisplayItemCounterClass("fc-dispcountergreen");
                    form.setDisplayItemCounter(
                            resolveSessionMessage("$m{entityformapplet.form.collaboration.editable}"));
                } else {
                    form.setDisplayItemCounter(
                            resolveSessionMessage("$m{entityformapplet.form.collaboration.viewonly}"));
                }
            }

            if (isUpdateDraft) {
                if (appCtx.isInWorkflow()) {
                    form.setDisplayItemCounterClass("fc-dispcounterorange");
                    form.setDisplayItemCounter(
                            resolveSessionMessage("$m{entityformapplet.form.workflowupdatecopy.viewonly}"));
                } else {
                    form.setDisplayItemCounterClass("fc-dispcounterfrozen");
                    form.setDisplayItemCounter(resolveSessionMessage("$m{entityformapplet.form.workflowupdatecopy}"));
                }
            } else {
                if (appCtx.isInWorkflow()) {
                    if (appCtx.isReview()) {
                        if (isRootForm) {
                            showReviewFormCaption = true;
                            form.setDisplayItemCounterClass("fc-dispcounterfrozen fc-dispcounterlarge");
                            form.setDisplayItemCounter(displayCounter);
                        }
                    } else {
                        form.setDisplayItemCounterClass("fc-dispcounterorange");
                        if (isRootForm) {
                            if (isWorkflowCopyForm && au().isWorkEntityWithPendingDraft(
                                    (Class<? extends WorkEntity>) inst.getClass(), (Long) inst.getId())) {
                                form.setDisplayItemCounter(
                                        resolveSessionMessage("$m{entityformapplet.form.pendingdraft.viewonly}"));
                            } else {
                                form.setDisplayItemCounter(
                                        resolveSessionMessage("$m{entityformapplet.form.inworkflow.viewonly}"));
                            }
                        } else {
                            form.setDisplayItemCounter(
                                    resolveSessionMessage("$m{entityformapplet.form.parentinworkflow.viewonly}"));
                            parentDisabled = true;
                        }
                    }
                }
            }
        }

        boolean enableNonFormEdit = isContextEditable;
        if (!isRootForm && appCtx.isInWorkflowPromptViewMode()) {
            enableNonFormEdit = false;
            enableUpdate = false;
            enableDelete = false;
            parentDisabled = true;
        }

        switch (viewMode) {
            case ENTITY_CRUD_PAGE:
                switchContent("entityCrudPanel");
                setEditable("entityCrudPanel", enableNonFormEdit);
                applet.getEntityCrudPage().setDisabled(parentDisabled);
                break;
            case ENTRY_TABLE_PAGE:
                switchContent("entryTablePanel");
                setEditable("entryTablePanel", enableNonFormEdit);
                setVisible("entryTablePanel.saveBtn", enableNonFormEdit);
                setVisible("saveEntryCloseBtn", enableNonFormEdit);
                applet.getEntryTablePage().setDisabled(parentDisabled);
                break;
            case ASSIGNMENT_PAGE:
                switchContent("assignmentPanel");
                setEditable("assignmentPanel", enableNonFormEdit);
                setVisible("assignmentPanel.saveBtn", enableNonFormEdit);
                setVisible("saveAssignCloseBtn", enableNonFormEdit);
                final boolean isEntryMode = applet.getAssignmentPage().isEntryTableMode();
                setVisible("assignmentPanel.assignmentPage", !isEntryMode);
                setVisible("assignmentPanel.assignmentEntryTbl", isEntryMode);
                applet.getAssignmentPage().setDisabled(parentDisabled);
                break;
            case PROPERTYLIST_PAGE:
                switchContent("editPropertyListPanel");
                setEditable("editPropertyListPanel", isContextEditable);
                setVisible("editPropertyListPanel.saveBtn", isContextEditable);
                setVisible("savePropListCloseBtn", isContextEditable);
                break;
            case SINGLE_FORM:
                switchContent("singleFormPanel");
                setVisible("scancelBtn", true);
                setVisible("sdisplayCounterLabel", isCollaboration);
                setVisible("supdateBtn", enableUpdate);
                setVisible("supdateCloseBtn", enableUpdate);
                setVisible("sdeleteBtn", enableDelete);
                setEditable("singleFormPanel", enableUpdate);
                addPanelToPushComponents("singleFormPanel", enableUpdate);
                break;
            case LISTING_FORM:
                switchContent("listingPanel");
                setDisabled("listPrevBtn", !applet.isPrevNav());
                setDisabled("listNextBtn", !applet.isNextNav());
                setVisible("listingPanel.emailsPanel", appCtx.isReview() && appCtx.isEmails());
                setVisible("listingPanel.commentsPanel", appCtx.isReview() && appCtx.isComments());
                setVisible("listingPanel.errorsPanel", appCtx.isReview() && appCtx.isRecovery());
                setEditable("listingPanel.errorsPanel", false);
                form.setDisplayItemCounter(applet.getDisplayItemCounter());
                break;
            case MAINTAIN_FORM_SCROLL:
            case MAINTAIN_PRIMARY_FORM_NO_SCROLL:
                final boolean closable = !(isRootForm && appCtx.isInDetachedWindow());
                switchContent("formPanel");
                setVisible("cancelBtn", true);
                setVisible("saveBtn", false);
                setVisible("saveNextBtn", false);
                setVisible("saveCloseBtn", false);
                setVisible("submitCloseBtn",
                        (enableUpdate && isWorkflowCopyForm && isUpdateDraft && !isInWorkflow)
                                || (enableUpdateSubmit && formAppletDef.getPropValue(boolean.class,
                                        AppletPropertyConstants.MAINTAIN_FORM_SUBMIT, false)));
                setVisible("submitNextBtn", !isWorkflowCopyForm && enableUpdateSubmit && formAppletDef
                        .getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_NEXT, false));
                setDisabled("prevBtn", viewMode.isScroll() && !applet.isPrevNav());
                setDisabled("nextBtn", viewMode.isScroll() && !applet.isNextNav());
                setVisible("prevBtn", closable);
                setVisible("nextBtn", closable);
                setVisible("formAttachmentBtn", enableAttachment);
                setVisible("saveAsBtn", enableSaveAs && !isWorkflowCopyForm);
                setVisible("updateBtn", enableUpdate);
                setVisible("updateCloseBtn", enableUpdate);
                setVisible("deleteBtn",
                        enableDelete || (enableUpdate && isWorkflowCopyForm && isUpdateDraft && !isInWorkflow));

                if (viewMode.isScroll()) {
                    setVisible("displayCounterLabel", true);
                    if (!form.isWithDisplayItemCounter()) {
                        form.setDisplayItemCounter(applet.getDisplayItemCounter());
                    }
                } else {
                    setVisible("displayCounterLabel", isCollaboration);
                }

                if (isWorkflowCopyForm) {
                    form.setSubmitCaption(resolveSessionMessage("$m{button.submitforapproval}"));
                    form.setSubmitStyleClass("fc-greenbutton");
                }

                if (form.isWithAttachments()) {
                    form.getAttachments().setEditable(enableUpdate);
                }

                setEditable("formPanel", enableUpdate);
                addPanelToPushComponents("formPanel", enableUpdate);
                break;
            case MAINTAIN_CHILDLIST_FORM_NO_SCROLL:
            case MAINTAIN_RELATEDLIST_FORM_NO_SCROLL:
            case MAINTAIN_HEADLESSLIST_FORM_NO_SCROLL:
                switchContent("formPanel");
                setVisible("cancelBtn", true);
                setVisible("saveBtn", false);
                setVisible("saveNextBtn", false);
                setVisible("saveCloseBtn", false);
                setVisible("submitCloseBtn", false);
                setVisible("submitNextBtn", false);
                setVisible("prevBtn", false);
                setVisible("nextBtn", false);
                setVisible("displayCounterLabel", isCollaboration);
                setVisible("formAttachmentBtn", enableAttachment);
                setVisible("saveAsBtn", enableSaveAs);
                setVisible("updateBtn", enableUpdate);
                setVisible("updateCloseBtn", enableUpdate);
                setVisible("deleteBtn", enableDelete);
                if (form.isWithAttachments()) {
                    form.getAttachments().setEditable(enableUpdate);
                }

                setEditable("formPanel", enableUpdate);
                addPanelToPushComponents("formPanel", enableUpdate);
                break;
            case MAINTAIN_FORM:
            case MAINTAIN_CHILDLIST_FORM:
            case MAINTAIN_RELATEDLIST_FORM:
            case MAINTAIN_HEADLESSLIST_FORM:
                switchContent("formPanel");
                setVisible("cancelBtn", true);
                setVisible("saveBtn", false);
                setVisible("saveNextBtn", false);
                setVisible("saveCloseBtn", false);
                setVisible("submitCloseBtn",
                        (enableUpdate && isWorkflowCopyForm && isUpdateDraft && !isInWorkflow)
                                || (enableUpdateSubmit && formAppletDef.getPropValue(boolean.class,
                                        AppletPropertyConstants.MAINTAIN_FORM_SUBMIT, false)));
                setVisible("submitNextBtn", !isWorkflowCopyForm && enableUpdateSubmit && formAppletDef
                        .getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_NEXT, false));
                setVisible("prevBtn", false);
                setVisible("nextBtn", false);
                setVisible("displayCounterLabel", showReviewFormCaption || isCollaboration);
                setVisible("formAttachmentBtn", enableAttachment);
                setVisible("saveAsBtn", enableSaveAs && !isWorkflowCopyForm);
                setVisible("updateBtn", enableUpdate);
                setVisible("updateCloseBtn", enableUpdate);
                setVisible("deleteBtn",
                        enableDelete || (enableUpdate && isWorkflowCopyForm && isUpdateDraft && !isInWorkflow));

                if (isWorkflowCopyForm) {
                    form.setSubmitCaption(resolveSessionMessage("$m{button.submitforapproval}"));
                    form.setSubmitStyleClass("fc-greenbutton");
                }

                if (form.isWithAttachments()) {
                    form.getAttachments().setEditable(enableUpdate);
                }

                setEditable("formPanel", enableUpdate);
                addPanelToPushComponents("formPanel", enableUpdate);
                break;
            case NEW_PRIMARY_FORM:
                enableCreate = true;
            case NEW_FORM:
            case NEW_CHILD_FORM:
            case NEW_CHILDLIST_FORM:
            case NEW_RELATEDLIST_FORM:
            case NEW_HEADLESSLIST_FORM:
                switchContent("formPanel");
                setVisible("cancelBtn", true);
                final boolean allowSaveAndNext = viewMode != AbstractEntityFormApplet.ViewMode.NEW_CHILD_FORM;
                if (enableCreate && !isWorkflowCopyForm && formAppletDef != null) {
                    setVisible("saveBtn",
                            formAppletDef.getPropValue(boolean.class, AppletPropertyConstants.CREATE_FORM_SAVE, false));
                    setVisible("saveNextBtn", allowSaveAndNext && formAppletDef.getPropValue(boolean.class,
                            AppletPropertyConstants.CREATE_FORM_SAVE_NEXT, false));
                    setVisible("saveCloseBtn", formAppletDef.getPropValue(boolean.class,
                            AppletPropertyConstants.CREATE_FORM_SAVE_CLOSE, false));
                    setVisible("submitCloseBtn", enableCreateSubmit && formAppletDef.getPropValue(boolean.class,
                            AppletPropertyConstants.CREATE_FORM_SUBMIT, false));
                    setVisible("submitNextBtn", enableCreateSubmit && formAppletDef.getPropValue(boolean.class,
                            AppletPropertyConstants.CREATE_FORM_SUBMIT_NEXT, false));
                } else {
                    setVisible("saveBtn", enableCreate && (isWorkflowCopyForm || formAppletDef == null));
                    setVisible("saveNextBtn", false);
                    setVisible("saveCloseBtn", false);
                    setVisible("submitCloseBtn", enableCreate && isWorkflowCopyForm);
                    setVisible("submitNextBtn", false);
                }

                setVisible("prevBtn", false);
                setVisible("nextBtn", false);
                setVisible("displayCounterLabel", false);
                setVisible("formAttachmentBtn", false);
                setVisible("saveAsBtn", false);
                setVisible("updateBtn", false);
                setVisible("updateCloseBtn", false);
                setVisible("deleteBtn", false);

                if (isWorkflowCopyForm) {
                    form.setSubmitCaption(resolveSessionMessage("$m{button.submitforapproval}"));
                    form.setSubmitStyleClass("fc-greenbutton");
                }

                if (form.isWithAttachments()) {
                    form.getAttachments().setEditable(true);
                }

                setEditable("formPanel", true);
                addPanelToPushComponents("formPanel", true);
                break;
            case CUSTOM_PAGE:
                break;
            case SEARCH:
            case HEADLESS_TAB:
            default:
                break;
        }

        logDebug("Switching completed for form applet panel [{0}].",
                formAppletDef != null ? formAppletDef.getLongName() : null);
    }

    @Action
    public void newInst() throws UnifyException {
        TableActionResult result = getEntityFormApplet().newEntityInst();
        if (result != null) {
            if (result.isOpenTab()) {
                result.setMultiPage(true);
                openInBrowserTab(result, getEntityFormApplet().getFormAppletDef(), FormMode.CREATE);
            } else if (result.isOpenPath()) {
                setCommandOpenPath((String) result.getResult());
            }
        } else {
            getRequestContextUtil().setContentScrollReset();
        }
    }

    @Action
    public void navBackToPrevious() throws UnifyException {
        AbstractEntityFormApplet applet = getEntityFormApplet();
        boolean strictUpdate = applet.au().getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.STRICT_FORM_REVIEW_MODE);
        if (strictUpdate) {
            FormContext ctx = applet.reviewOnClose();
            if (ctx.isWithReviewErrors()) {
                handleEntityActionResult(new EntityActionResult(), ctx);
                return;
            }
        }

        if (applet.getMode().isPrimary()) {
            setCloseResultMapping();
            return;
        }

        applet.navBackToPrevious();
        setCommandResultMapping(ApplicationResultMappingConstants.REFRESH_CONTENT);
        getRequestContextUtil().setContentScrollReset();
    }

    @Action
    public void navBackToSearch() throws UnifyException {
        getEntityFormApplet().navBackToSearch();
        getRequestContextUtil().setContentScrollReset();
    }

    @Action
    public void formSwitchOnChange() throws UnifyException {
        getEntityFormApplet().formSwitchOnChange();
    }

    @Action
    public void crudSwitchOnChange() throws UnifyException {
        getEntityFormApplet().crudSwitchOnChange();
    }

    @Action
    public void assignSwitchOnChange() throws UnifyException {
        int index = getRequestTarget(int.class);
        getEntityFormApplet().assignSwitchOnChange(index);
    }

    @Action
    public void entrySwitchOnChange() throws UnifyException {
        int index = getRequestTarget(int.class);
        getEntityFormApplet().entrySwitchOnChange(index);
    }

    @Action
    public void crudSelectItem() throws UnifyException {
        int index = getRequestTarget(int.class);
        getEntityFormApplet().crudSelectItem(index);
    }

    @Action
    public void saveAsSwitchOnChange() throws UnifyException {
        getEntityFormApplet().saveAsSwitchOnChange();
    }

    @Action
    public void showFormFileAttachments() throws UnifyException {
        setCommandResultMapping(ApplicationResultMappingConstants.SHOW_FILE_ATTACHMENTS);
    }

    @Action
    public void prepareSaveEntityAs() throws UnifyException {
        getEntityFormApplet().prepareSaveEntityAs();
        commandShowPopup(getWidgetByShortName("entitySaveAsPanel").getLongName());
    }

    @Action
    public void saveEntityAs() throws UnifyException {
        AbstractEntityFormApplet applet = getEntityFormApplet();
        EntitySaveAs entitySaveAs = applet.getEntitySaveAs();
        FormContext ctx = evaluateCurrentFormContext(entitySaveAs.getInputForm().getCtx(),
                new FormValidationContext(EvaluationMode.CREATE));
        Object inst = entitySaveAs.getInputForm().getFormBean();
        Long saveApplicatIonId = null;
        if (inst instanceof BaseApplicationEntity) {
            BaseApplicationEntity entity = (BaseApplicationEntity) inst;
            saveApplicatIonId = entity.getApplicationId();
            if (!applet.au().isApplicationDevelopable(saveApplicatIonId)) {
                ctx.addValidationError(new FieldTarget("applicationId"),
                        getApplicationMessage("application.validation.application.nondevelopable"));
            }
        }

        if (!ctx.isWithFormErrors()) {
            String saveAsPolicy = applet.getRootAppletProp(String.class,
                    AppletPropertyConstants.MAINTAIN_FORM_SAVEAS_POLICY);
            EntityActionResult entityActionResult = applet.saveEntityAs(saveAsPolicy);
            entityActionResult.setSuccessHint("$m{entityformapplet.saveas.success.hint}");

            Long sessionApplicationId = (Long) getSessionAttribute(
                    FlowCentralSessionAttributeConstants.CURRENT_APPLICATION_ID);
            if (!DataUtils.equals(saveApplicatIonId, sessionApplicationId)) {
                entityActionResult.setHidePopupOnly(true);
            }

            handleEntityActionResult(entityActionResult, null);
            return;
        }

        commandRefreshPanels(getWidgetByShortName("entitySaveAsPanel.entityFormBodyPanel").getLongName());
    }

    @Action
    public void cancelSaveEntityAs() throws UnifyException {
        getEntityFormApplet().cancelSaveEntityAs();
        commandHidePopup();
    }

    @Action
    public void performFormAction() throws UnifyException {
        String actionName = getRequestTarget(String.class);
        AbstractEntityFormApplet applet = getEntityFormApplet();
        FormActionDef formActionDef = applet.getCurrentFormDef().getFormActionDef(actionName);
        FormContext ctx = evaluateCurrentFormContext(
                new FormValidationContext(EvaluationMode.getRequiredMode(formActionDef.isValidateForm()), actionName));
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = applet.formActionOnInst(formActionDef.getPolicy(), actionName);
            handleEntityActionResult(entityActionResult);
        }
    }

    @Action
    public void saveAssign() throws UnifyException {
        getEntityFormApplet().saveAssign();
        hintUser("$m{entityformapplet.assignment.success.hint}", getAssignmentTitle());
    }

    @Action
    public void saveAssignAndClose() throws UnifyException {
        AbstractEntityFormApplet applet = getEntityFormApplet();
        applet.saveAssignOnClose();
        applet.navBackToPrevious();
        hintUser("$m{entityformapplet.assignment.success.hint}", getAssignmentTitle());
    }

    @Action
    public void saveEntryAndClose() throws UnifyException {
        AbstractEntityFormApplet applet = getEntityFormApplet();
        EntryTablePage entryTablePage = applet.getEntryTablePage();
        entryTablePage.commitEntryList(false);
        if (entryTablePage.isWithValidationErrors()) {
            hintUser(MODE.ERROR, "$m{entityformapplet.entrytable.errors.hint}", getEntryTitle());
        } else {
            applet.navBackToPrevious();
            hintUser("$m{entityformapplet.entrytable.success.hint}", getEntryTitle());
        }
    }

    @Action
    public void savePropListAndClose() throws UnifyException {
        AbstractEntityFormApplet applet = getEntityFormApplet();
        EditPropertyList editPropertyList = applet.getEditPropertyList();
        editPropertyList.commitPropertyList();
        applet.navBackToPrevious();
        hintUser("$m{entityformapplet.editpropertylist.success.hint}", getPropertiesTitle());
    }

    @Action
    public void save() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(EvaluationMode.CREATE));
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().saveNewInst();
            entityActionResult.setSuccessHint("$m{entityformapplet.new.success.hint}");
            handleEntityActionResult(entityActionResult, ctx);
        }
    }

    @Action
    public void saveAndNext() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(EvaluationMode.CREATE));
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().saveNewInstAndNext();
            entityActionResult.setSuccessHint("$m{entityformapplet.new.success.hint}");
            handleEntityActionResult(entityActionResult, ctx);
        }
    }

    @Action
    public void saveAndClose() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(EvaluationMode.CREATE));
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().saveNewInstAndClose();
            entityActionResult.setSuccessHint("$m{entityformapplet.new.success.hint}");
            handleEntityActionResult(entityActionResult, ctx);
        }
    }

    @Action
    public void submit() throws UnifyException {
        final AbstractEntityFormApplet applet = getEntityFormApplet();
        final EvaluationMode evalMode = applet.getMode().isMaintainForm()
                ? EvaluationMode.getUpdateSubmitMode(applet.getRootAppletProp(boolean.class,
                        AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_VALIDATE, false))
                : EvaluationMode.getCreateSubmitMode(applet.getRootAppletProp(boolean.class,
                        AppletPropertyConstants.CREATE_FORM_SUBMIT_VALIDATE, false));
        FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(evalMode));
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = applet.submitInst();
            if (!ctx.isWithReviewErrors()) {
                entityActionResult.setSuccessHint("$m{entityformapplet.submit.success.hint}");
            }

            handleEntityActionResult(entityActionResult, ctx);
        }
    }

    @Action
    public void submitAndNext() throws UnifyException {
        final AbstractEntityFormApplet applet = getEntityFormApplet();
        final EvaluationMode evalMode = applet.getMode().isMaintainForm()
                ? EvaluationMode.getUpdateSubmitMode(applet.getRootAppletProp(boolean.class,
                        AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_VALIDATE, false))
                : EvaluationMode.getCreateSubmitMode(applet.getRootAppletProp(boolean.class,
                        AppletPropertyConstants.CREATE_FORM_SUBMIT_VALIDATE, false));
        FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(evalMode));
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().submitInstAndNext();
            if (!ctx.isWithReviewErrors()) {
                entityActionResult.setSuccessHint("$m{entityformapplet.submit.success.hint}");
            }

            handleEntityActionResult(entityActionResult, ctx);
        }
    }

    @Action
    public void update() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(EvaluationMode.UPDATE));
        if (!ctx.isWithFormErrors()) {
            final AbstractEntityFormApplet applet = getEntityFormApplet();
            if (applet.isPromptEnterWorkflowDraft()) {
                showPromptWorkflowDraft(WorkflowDraftType.UPDATE, IndexedTarget.BLANK);
            } else {
                EntityActionResult entityActionResult = applet.updateInst();
                entityActionResult.setSuccessHint("$m{entityformapplet.update.success.hint}");
                handleEntityActionResult(entityActionResult, ctx);
            }
        }
    }

    @Action
    public void updateAndClose() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(EvaluationMode.UPDATE));
        if (!ctx.isWithFormErrors()) {
            final AbstractEntityFormApplet applet = getEntityFormApplet();
            if (applet.isPromptEnterWorkflowDraft()) {
                showPromptWorkflowDraft(WorkflowDraftType.UPDATE_CLOSE, IndexedTarget.BLANK);
            } else {
                EntityActionResult entityActionResult = applet.updateInstAndClose();
                entityActionResult.setSuccessHint("$m{entityformapplet.update.success.hint}");
                handleEntityActionResult(entityActionResult, ctx);
            }
        }
    }

    @Action
    public void delete() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(EvaluationMode.DELETE));
        if (!ctx.isWithFormErrors()) {
            final AbstractEntityFormApplet applet = getEntityFormApplet();
            if (applet.isPromptEnterWorkflowDraft()) {
                showPromptWorkflowDraft(WorkflowDraftType.DELETE, IndexedTarget.BLANK);
            } else {
                EntityActionResult entityActionResult = getEntityFormApplet().deleteInst();
                if (!entityActionResult.isWithReviewResult()) {
                    entityActionResult.setSuccessHint("$m{entityformapplet.delete.success.hint}");
                }

                handleEntityActionResult(entityActionResult, ctx);
            }
        }
    }

    @Action
    public void previous() throws UnifyException {
        TableActionResult result = getEntityFormApplet().previousInst();
        processTableActionResult(result);
        getRequestContextUtil().setContentScrollReset();
    }

    @Action
    public void next() throws UnifyException {
        TableActionResult result = getEntityFormApplet().nextInst();
        processTableActionResult(result);
        getRequestContextUtil().setContentScrollReset();
    }

    @Action
    public void maintain() throws UnifyException {
        IndexedTarget target = getIndexedTarget();
        if (target.isValidIndex()) {
            if (getEntityFormApplet().isPromptEnterWorkflowDraft()) {
                showPromptWorkflowDraft(WorkflowDraftType.MAINTAIN, target);
            } else {
                getRequestContextUtil().setContentScrollReset();
                switch (target.getTarget()) {
                    case EntitySearchValueMarkerConstants.CHILD_LIST:
                        getEntityFormApplet().maintainChildInst(target.getIndex());
                        return;
                    case EntitySearchValueMarkerConstants.RELATED_LIST:
                        getEntityFormApplet().maintainRelatedInst(target.getIndex());
                        return;
                    case EntitySearchValueMarkerConstants.HEADLESS_LIST:
                        getEntityFormApplet().maintainHeadlessInst(target.getIndex());
                        return;
                    default:
                }

                TableActionResult result = getEntityFormApplet().maintainInst(target.getIndex());
                processTableActionResult(result);
            }
        } else {
            setCommandResultMapping(ResultMappingConstants.NONE);
        }
    }

    @Action
    public void columns() throws UnifyException {
        // TODO
    }

    @Action
    public void listing() throws UnifyException {
        getRequestContextUtil().setContentScrollReset();
        IndexedTarget target = getRequestTarget(IndexedTarget.class);
        if (target.isValidIndex()) {
            switch (target.getTarget()) {
                case EntitySearchValueMarkerConstants.CHILD_LIST:
                    return;
                case EntitySearchValueMarkerConstants.RELATED_LIST:
                    return;
                case EntitySearchValueMarkerConstants.HEADLESS_LIST:
                    return;
                default:
            }

            TableActionResult result = getEntityFormApplet().listingInst(target.getIndex());
            processTableActionResult(result);
        }
    }

    @Action
    public void reviewAcknowledged() throws UnifyException {
        EntityActionResult entityActionResult = getEntityFormApplet().getCtx().getOriginalEntityActionResult();
        setCommandResultMapping(entityActionResult, true);
    }

    @Action
    public void reviewConfirm() throws UnifyException {
        MessageResult messageResult = getMessageResult();
        if (MessageResult.YES.equals(messageResult)) {
            EntityActionResult entityActionResult = getEntityFormApplet().getCtx().getOriginalEntityActionResult();
            if (entityActionResult.isSubmitToWorkflow()) {
                entityActionResult = getEntityFormApplet().submitCurrentInst(entityActionResult.getActionMode());
                entityActionResult.setSuccessHint("$m{entityformapplet.submit.success.hint}");
            } else if (entityActionResult.isApplyUserAction()) {
                getEntityFormApplet().applyUserAction(entityActionResult.getUserAction());
                entityActionResult.setSuccessHint("$m{reviewworkitemsapplet.apply.success.hint}");
            }

            setCommandResultMapping(entityActionResult, true);
            handleHints(entityActionResult, null);
        } else {
            setCommandResultMapping(ApplicationResultMappingConstants.REFRESH_CONTENT);
        }
    }

    @Action
    public void openWorkflowDraft() throws UnifyException {
        MessageResult result = getMessageResult();
        switch (result) {
            case NO:
                performNormalViewMode();
                break;
            case OK:
            case YES:
                performEditModeWorkflowDraft();
                break;
            case CANCEL:
            case RETRY:
            default:
                break;
        }

        setCommandResultMapping(ApplicationResultMappingConstants.REFRESH_CONTENT);
    }

    @Action
    public void deletionToWorkflow() throws UnifyException {
        MessageResult result = getMessageResult();
        switch (result) {
            case OK:
            case YES:
                performSubmitDeleteToWorkflow();
                return;
            case NO:
            case CANCEL:
            case RETRY:
            default:
                break;
        }

        setCommandResultMapping(ApplicationResultMappingConstants.REFRESH_CONTENT);
    }

    @Override
    protected void onReviewErrors(EntityActionResult entityActionResult) throws UnifyException {
        // Set recovery path on error to prevent possible manual duplication of record
        getRequestContextUtil().setSystemErrorRecoveryPath("/application/refreshContent");

        // Select first tab with review message TODO Move to method?
        TabSheetWidget tabSheetWidget = getWidgetByShortName(TabSheetWidget.class, "formPanel.formTabSheet");
        TabSheet tabSheet = tabSheetWidget.getTabSheet();
        if (tabSheet != null && tabSheet.isInStateForDisplay()) {
            List<TabDef> tabDefList = tabSheet.getTabDefList();
            int len = tabDefList.size();
            for (int i = 0; i < len; i++) {
                if (tabSheet.getTabSheetItem(i).isVisible()) {
                    TabDef tabDef = tabDefList.get(i);
                    MessageType messageType = tabSheet.getReviewMessageType(tabDef.getTabName());
                    if (messageType != null) {
                        tabSheet.setCurrentTabIndex(i);
                        break;
                    }
                }
            }
        }

        // Show message box
        ReviewResult reviewResult = entityActionResult.getReviewResult();
        if (reviewResult != null) {
            if (reviewResult.isSkippableOnly()) {
                getEntityFormApplet().getCtx().setOriginalEntityActionResult(entityActionResult);
                final String message = concatenateMessages("$m{entityformapplet.formreview.skippable}",
                        reviewResult.getSkippableMessages());
                final String commandPath = getCommandFullPath("reviewConfirm");
                showMessageBox(MessageIcon.WARNING, MessageMode.YES_NO, "$m{entityformapplet.formreview}", message,
                        commandPath);
            } else {
                final String message = concatenateMessages("$m{entityformapplet.formreview.nonskippable}",
                        reviewResult.getAllMessages());
                final String commandPath = getCommandFullPath("reviewAcknowledged");
                showMessageBox(MessageIcon.WARNING, MessageMode.OK, "$m{entityformapplet.formreview}", message,
                        commandPath);
            }
        } else {
            final String commandPath = getCommandFullPath("reviewAcknowledged");
            showMessageBox(MessageIcon.WARNING, MessageMode.OK, "$m{entityformapplet.formreview}",
                    "$m{entityformapplet.formreview.failure}", commandPath);
        }
    }

    private IndexedTarget getIndexedTarget() throws UnifyException {
        AbstractEntityFormApplet applet = getEntityFormApplet();
        return getRequestAttribute(boolean.class, IN_WORKFLOW_DRAFT_LOOP_FLAG)
                ? DataUtils.convert(IndexedTarget.class, applet.removeWorkflowDraftInfo().getRequestTarget())
                : getRequestTarget(IndexedTarget.class);
    }

    private void performNormalViewMode() throws UnifyException {
        AbstractEntityFormApplet applet = getEntityFormApplet();
        applet.getCtx().setInWorkflowPromptViewMode(true);
        setRequestAttribute(IN_WORKFLOW_DRAFT_LOOP_FLAG, Boolean.TRUE);
        WorkflowDraftInfo workflowDraftInfo = applet.getWorkflowDraftInfo();
        switch (workflowDraftInfo.getType()) {
            case MAINTAIN:
                maintain();
                break;
            case ASSIGN_TO_CHILD_ITEM:
            case CRUD_TO_CHILD_ITEM:
            case EDIT_CHILD_ITEM:
            case ENTRY_TO_CHILD_ITEM:
            case NEW_CHILDLIST_ITEM:
            case NEW_CHILD_ITEM:
            case QUICK_FORM_EDIT:
            case QUICK_TABLE_EDIT:
            case QUICK_TABLE_ORDER:
            case UPDATE:
            case UPDATE_CLOSE:
            default:
                applet.getCtx().setInWorkflowPromptViewMode(false);
                setRequestAttribute(IN_WORKFLOW_DRAFT_LOOP_FLAG, Boolean.FALSE);
                break;
        }
    }

    private void performSubmitDeleteToWorkflow() throws UnifyException {
        AbstractEntityFormApplet applet = getEntityFormApplet();
        EntityActionResult entityActionResult = applet.submitDeleteToWorkflow();
        setCommandResultMapping(entityActionResult, true);
    }

    private void performEditModeWorkflowDraft() throws UnifyException {
        AbstractEntityFormApplet applet = getEntityFormApplet();
        WorkflowDraftInfo workflowDraftInfo = applet.getWorkflowDraftInfo();
        applet.enterWorkflowDraft(workflowDraftInfo.getType());
    }

    private void processTableActionResult(TableActionResult result) throws UnifyException {
        if (result != null) {
            if (result.isOpenTab()) {
                openInBrowserTab(result, getEntityFormApplet().getFormAppletDef(), FormMode.MAINTAIN);
            } else if (result.isOpenPath()) {
                setCommandOpenPath((String) result.getResult());
            } else if (result.isDisplayListingReport()) {
                setRequestAttribute(FlowCentralRequestAttributeConstants.REPORT, result.getResult());
                setCommandResultMapping(FlowCentralResultMappingConstants.VIEW_LISTING_REPORT);
            } else if (result.isRefreshContent()) {
                setCommandResultMapping(ApplicationResultMappingConstants.REFRESH_CONTENT);
            }
        }
    }

    private String concatenateMessages(String base, List<String> messages) throws UnifyException {
        StringBuilder sb = new StringBuilder();
        boolean appendSym = false;
        for (String msg : messages) {
            if (appendSym) {
                sb.append('\n');
            } else {
                appendSym = true;
            }

            sb.append(resolveSessionMessage(msg));
        }

        String msg = sb.toString();
        return HtmlUtils.formatHTML(resolveSessionMessage(base, msg));
    }

    protected AbstractEntityFormApplet getEntityFormApplet() throws UnifyException {
        return getValue(AbstractEntityFormApplet.class);
    }

    protected String getFormTitle() throws UnifyException {
        return getEntityFormApplet().getFormTitle();
    }

    protected String getBeanTitle() throws UnifyException {
        return getEntityFormApplet().getBeanTitle();
    }

    protected String getAssignmentTitle() throws UnifyException {
        return getEntityFormApplet().getAssignmentTitle();
    }

    protected String getAssignmentSubTitle() throws UnifyException {
        return getEntityFormApplet().getAssignmentSubTitle();
    }

    protected String getEntryTitle() throws UnifyException {
        return getEntityFormApplet().getEntryTitle();
    }

    protected String getPropertiesTitle() throws UnifyException {
        return getEntityFormApplet().getPropertiesTitle();
    }

    protected FormContext evaluateCurrentFormContext(FormValidationContext vCtx) throws UnifyException {
        return evaluateCurrentFormContext(vCtx, false);
    }

    protected FormContext evaluateCurrentFormContext(FormValidationContext vCtx, boolean commentRequired)
            throws UnifyException {
        FormContext ctx = getEntityFormApplet().getResolvedForm().getCtx();
        if (ctx.getFormDef() != null && ctx.getFormDef().isInputForm()) {
            evaluateCurrentFormContext(ctx, vCtx);
        } else {
            ctx.clearReviewErrors();
            ctx.clearValidationErrors();
        }

        if (vCtx.isEvaluation()) {
            if (vCtx.isReview() && ctx.getAppletContext().isReview()) {
                AbstractEntityFormApplet applet = getEntityFormApplet();
                final AbstractEntityFormApplet.ViewMode viewMode = applet.getMode();
                if (commentRequired) {
                    FormPanel commentsFormPanel = viewMode == AbstractEntityFormApplet.ViewMode.LISTING_FORM
                            ? getWidgetByShortName(FormPanel.class, "listingPanel.commentsPanel")
                            : (viewMode == AbstractEntityFormApplet.ViewMode.SINGLE_FORM
                                    ? getWidgetByShortName(FormPanel.class, "singleFormPanel.commentsPanel")
                                    : getWidgetByShortName(FormPanel.class, "formPanel.commentsPanel"));
                    ctx.mergeValidationErrors(commentsFormPanel.validate(vCtx));
                }

                if (ctx.getAppletContext().isEmails()) {
                    FormPanel emailsFormPanel = viewMode == AbstractEntityFormApplet.ViewMode.LISTING_FORM
                            ? getWidgetByShortName(FormPanel.class, "listingPanel.emailsPanel")
                            : (viewMode == AbstractEntityFormApplet.ViewMode.SINGLE_FORM
                                    ? getWidgetByShortName(FormPanel.class, "singleFormPanel.emailsPanel")
                                    : getWidgetByShortName(FormPanel.class, "formPanel.emailsPanel"));
                    ctx.mergeValidationErrors(emailsFormPanel.validate(vCtx));
                }
            }

            if (ctx.isWithFormErrors()) {
                hintUser(MODE.ERROR, "$m{entityformapplet.formvalidation.error.hint}");
            }
        }

        return ctx;
    }

    private void showPromptWorkflowDraft(WorkflowDraftType type, IndexedTarget target) throws UnifyException {
        getEntityFormApplet().setWorkflowDraftInfo(new WorkflowDraftInfo(type, target));
        final String caption = resolveSessionMessage("$m{formapplet.workflowdraft.caption}");
        final String prompt = type.isDelete() ? resolveSessionMessage("$m{formapplet.workflowdraft.submitdeleteprompt}")
                : resolveSessionMessage("$m{formapplet.workflowdraft.prompt}");
        final String viewMessage = resolveSessionMessage("$m{formapplet.workflowdraft.enterview}");
        final String okMessage = type.isDelete() ? resolveSessionMessage("$m{formapplet.workflowdraft.submitdelete}")
                : resolveSessionMessage("$m{formapplet.workflowdraft.enteredit}");
        final String cancelMessage = resolveSessionMessage("$m{formapplet.workflowdraft.cancel}");
        final String commandPath = type.isDelete() ? getCommandFullPath("deletionToWorkflow")
                : getCommandFullPath("openWorkflowDraft");
        MessageBoxCaptions captions = new MessageBoxCaptions(caption);
        if (type.isNew() || type.isUpdate() || type.isDelete()) {
            captions.setOkStyleClass("fc-orangebutton");
            captions.setOkCaption(okMessage);
            captions.setCancelCaption(cancelMessage);
            showMessageBox(MessageIcon.QUESTION, MessageMode.OK_CANCEL, captions, prompt, commandPath);
            return;
        }

        captions.setYesStyleClass("fc-orangebutton");
        captions.setNoStyleClass("fc-bluebutton");

        captions.setYesCaption(okMessage);
        captions.setNoCaption(viewMessage);
        showMessageBox(MessageIcon.QUESTION, MessageMode.YES_NO_CANCEL, captions, prompt, commandPath);
    }

    private FormContext evaluateCurrentFormContext(final FormContext ctx, FormValidationContext vCtx)
            throws UnifyException {
        AbstractEntityFormApplet applet = getEntityFormApplet();
        applet.au().formContextEvaluator().evaluateFormContext(ctx, vCtx);

        // Detect tab error
        final boolean isWithFieldErrors = ctx.isWithFieldErrors();
        HeaderWithTabsForm form = (HeaderWithTabsForm) applet.getForm();
        if (form.isWithTabSheet()) {
            FormDef formDef = form.getFormDef();
            TabSheet tabSheet = form.getTabSheet();
            for (TabSheetItem tabSheetItem : tabSheet.getTabSheetItemList()) {
                FormTabDef formTabDef = formDef.getFormTabDef(tabSheetItem.getIndex());
                if (TabContentType.MINIFORM.equals(formTabDef.getContentType())) {
                    TabDef tabDef = form.getTabSheet().getTabDef(tabSheetItem.getIndex() - 1);
                    if (isWithFieldErrors) {
                        tabDef.setErrors(ctx.isWithFieldError(formTabDef.getFieldNameList()));
                    } else {
                        tabDef.setErrors(false);
                    }
                }
            }
        }
        // End

        if (isWithFieldErrors) {
            hintUser(MODE.ERROR, "$m{entityformapplet.formvalidation.error.hint}");
        }

        return ctx;
    }

}

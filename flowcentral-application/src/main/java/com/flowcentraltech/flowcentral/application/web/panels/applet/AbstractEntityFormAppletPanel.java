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

package com.flowcentraltech.flowcentral.application.web.panels.applet;

import java.util.List;

import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationResultMappingConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.FormActionDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormTabDef;
import com.flowcentraltech.flowcentral.application.data.TabDef;
import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm;
import com.flowcentraltech.flowcentral.application.web.panels.EditPropertyList;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySaveAs;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySearchValueMarkerConstants;
import com.flowcentraltech.flowcentral.application.web.panels.FormPanel;
import com.flowcentraltech.flowcentral.application.web.panels.HeaderWithTabsForm;
import com.flowcentraltech.flowcentral.application.web.widgets.AssignmentPage;
import com.flowcentraltech.flowcentral.application.web.widgets.EntryTablePage;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet.TabSheetItem;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheetWidget;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.CollaborationProvider;
import com.flowcentraltech.flowcentral.common.business.FileAttachmentProvider;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.ReviewResult;
import com.flowcentraltech.flowcentral.common.business.policies.TableActionResult;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralRequestAttributeConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ResultMappingConstants;
import com.tcdng.unify.web.ui.constant.MessageType;
import com.tcdng.unify.web.ui.widget.Panel;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;
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

    private static final String WORK_CATEGORY = "work";

    @Configurable
    protected ApplicationPrivilegeManager applicationPrivilegeManager;

    @Configurable
    private SystemModuleService systemModuleService;

    @Configurable
    private FileAttachmentProvider fileAttachmentProvider;

    @Configurable
    private CollaborationProvider collaborationProvider;

    private String focusMemoryId;

    private String tabMemoryId;

    public final void setCollaborationProvider(CollaborationProvider collaborationProvider) {
        this.collaborationProvider = collaborationProvider;
    }

    public final void setApplicationPrivilegeManager(ApplicationPrivilegeManager applicationPrivilegeManager) {
        this.applicationPrivilegeManager = applicationPrivilegeManager;
    }

    public final void setSystemModuleService(SystemModuleService systemModuleService) {
        this.systemModuleService = systemModuleService;
    }

    public final void setFileAttachmentProvider(FileAttachmentProvider fileAttachmentProvider) {
        this.fileAttachmentProvider = fileAttachmentProvider;
    }

    @Override
    public void onPageConstruct() throws UnifyException {
        super.onPageConstruct();
        focusMemoryId = getWidgetByShortName("focusMemory").getId();
        tabMemoryId = getWidgetByShortName("tabMemory").getId();
    }

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        final AbstractEntityFormApplet applet = getEntityFormApplet();
        applet.ensureFormStruct();

        final AppletDef formAppletDef = applet.getFormAppletDef();
        logDebug("Switching form applet panel [{0}]...", formAppletDef != null ? formAppletDef.getLongName() : null);
        final AppletContext appCtx = applet.getCtx();
        final boolean isCollaboration = applet.isCollaboration() && collaborationProvider != null;
        final AbstractEntityFormApplet.ViewMode viewMode = applet.getMode();
        final String roleCode = getUserToken().getRoleCode();
        final AbstractForm form = applet.getResolvedForm();
        final Entity inst = form != null ? (Entity) form.getFormBean() : null;
        final boolean isRootForm = applet.isRootForm();
        final boolean isInWorkflow = inst instanceof WorkEntity && ((WorkEntity) inst).isInWorkflow();
        if (isRootForm) {
            appCtx.setInWorkflow(isInWorkflow);
        }

        final boolean isContextEditable = appCtx.isContextEditable();
        applet.getFormFileAttachments().setDisabled(!isContextEditable);
        boolean enableSaveAs = false;
        boolean enableUpdate = false;
        boolean enableDelete = false;
        boolean enableCreate = false;
        boolean enableAttachment = false;
        boolean enableCreateSubmit = false;
        boolean enableUpdateSubmit = false;
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
                    enableAttachment = isRootForm && formEntityDef.getBaseType().isWorkEntityType()
                            && formEntityDef.isWithAttachments() && formAppletDef.getPropValue(boolean.class,
                                    AppletPropertyConstants.MAINTAIN_FORM_ATTACHMENTS, false);
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
                    enableAttachment = isRootForm && formEntityDef.getBaseType().isWorkEntityType()
                            && formEntityDef.isWithAttachments();
                }

                if (enableAttachment) {
                    form.setAttachmentCount(fileAttachmentProvider.countFileAttachments(WORK_CATEGORY,
                            formEntityDef.getLongName(), (Long) inst.getId()));
                }
            }
        }

        if (viewMode.isInForm()) {
            boolean showAlternateFormActions = systemModuleService.getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.SHOW_FORM_ALTERNATE_ACTIONS);
            if (viewMode.isSingleForm()) {
                setVisible("singleFormPanel.altActionPanel", showAlternateFormActions);
                setVisible("singleFormPanel.emailsPanel", appCtx.isReview() && appCtx.isEmails());
                setVisible("singleFormPanel.commentsPanel", appCtx.isReview() && appCtx.isComments());
                setVisible("singleFormPanel.errorsPanel", appCtx.isReview() && appCtx.isRecovery());
                setVisible("sfrmActionBtns", !DataUtils.isBlank(form.getFormActionDefList()));
                setEditable("singleFormPanel.errorsPanel", false);
                Panel singleFormPanel = getWidgetByShortName(Panel.class, "singleFormPanel");
                setPageAttribute("singleFormPanel.id", singleFormPanel.getId());
            } else {
                setVisible("formPanel.altActionPanel", showAlternateFormActions);
                setVisible("formPanel.emailsPanel", isRootForm && appCtx.isReview() && appCtx.isEmails());
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

        if (form != null) {
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

            if (appCtx.isInWorkflow() && !appCtx.isReview()) {
                form.setDisplayItemCounterClass("fc-dispcounterorange");
                if (isRootForm) {
                    form.setDisplayItemCounter(resolveSessionMessage("$m{entityformapplet.form.inworkflow.viewonly}"));
                } else {
                    form.setDisplayItemCounter(
                            resolveSessionMessage("$m{entityformapplet.form.parentinworkflow.viewonly}"));
                }
            }
        }

        switch (viewMode) {
            case ENTITY_CRUD_PAGE:
                switchContent("entityCrudPanel");
                setEditable("entityCrudPanel", isContextEditable);
                break;
            case ENTRY_TABLE_PAGE:
                switchContent("entryTablePanel");
                setEditable("entryTablePanel", isContextEditable);
                setVisible("entryTablePanel.saveBtn", isContextEditable);
                setVisible("saveEntryCloseBtn", isContextEditable);
                break;
            case ASSIGNMENT_PAGE:
                switchContent("assignmentPanel");
                setEditable("assignmentPanel", isContextEditable);
                setVisible("assignmentPanel.saveBtn", isContextEditable);
                setVisible("saveAssignCloseBtn", isContextEditable);
                final boolean isEntryMode = applet.getAssignmentPage().isEntryTableMode();
                setVisible("assignmentPanel.assignmentPage", !isEntryMode);
                setVisible("assignmentPanel.assignmentEntryTbl", isEntryMode);
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
                switchContent("formPanel");
                setVisible("cancelBtn", true);
                setVisible("saveBtn", false);
                setVisible("saveNextBtn", false);
                setVisible("saveCloseBtn", false);
                setVisible("submitCloseBtn", enableUpdateSubmit && formAppletDef.getPropValue(boolean.class,
                        AppletPropertyConstants.MAINTAIN_FORM_SUBMIT, false));
                setVisible("submitNextBtn", enableUpdateSubmit && formAppletDef.getPropValue(boolean.class,
                        AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_NEXT, false));
                setVisible("prevBtn", true);
                setVisible("nextBtn", true);
                setDisabled("prevBtn", !applet.isPrevNav());
                setDisabled("nextBtn", !applet.isNextNav());
                setVisible("formAttachmentBtn", enableAttachment);
                setVisible("saveAsBtn", enableSaveAs);
                setVisible("updateBtn", enableUpdate);
                setVisible("updateCloseBtn", enableUpdate);
                setVisible("deleteBtn", enableDelete);

                setVisible("displayCounterLabel", true);
                if (!form.isWithDisplayItemCounter()) {
                    form.setDisplayItemCounter(applet.getDisplayItemCounter());
                }
                setEditable("formPanel", enableUpdate);
                addPanelToPushComponents("formPanel", enableUpdate);
                break;
            case MAINTAIN_PRIMARY_FORM_NO_SCROLL:
                switchContent("formPanel");
                setVisible("cancelBtn", true);
                setVisible("saveBtn", false);
                setVisible("saveNextBtn", false);
                setVisible("saveCloseBtn", false);
                setVisible("submitCloseBtn", enableUpdateSubmit && formAppletDef.getPropValue(boolean.class,
                        AppletPropertyConstants.MAINTAIN_FORM_SUBMIT, false));
                setVisible("submitNextBtn", enableUpdateSubmit && formAppletDef.getPropValue(boolean.class,
                        AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_NEXT, false));
                setVisible("prevBtn", false);
                setVisible("nextBtn", false);
                setVisible("displayCounterLabel", isCollaboration);
                setVisible("formAttachmentBtn", enableAttachment);
                setVisible("saveAsBtn", enableSaveAs);
                setVisible("updateBtn", enableUpdate);
                setVisible("updateCloseBtn", enableUpdate);
                setVisible("deleteBtn", enableDelete);
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
                setVisible("submitCloseBtn", enableUpdateSubmit && formAppletDef.getPropValue(boolean.class,
                        AppletPropertyConstants.MAINTAIN_FORM_SUBMIT, false));
                setVisible("submitNextBtn", enableUpdateSubmit && formAppletDef.getPropValue(boolean.class,
                        AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_NEXT, false));
                setVisible("prevBtn", false);
                setVisible("nextBtn", false);
                setVisible("displayCounterLabel", isCollaboration);
                setVisible("formAttachmentBtn", enableAttachment);
                setVisible("saveAsBtn", enableSaveAs);
                setVisible("updateBtn", enableUpdate);
                setVisible("updateCloseBtn", enableUpdate);
                setVisible("deleteBtn", enableDelete);
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
                final boolean allowSaveAndNext = viewMode != AbstractEntityFormApplet.ViewMode.NEW_CHILD_FORM
                /* && !form.getFormDef().isChildOrChildListTabs() */;
                if (enableCreate) {
                    if (formAppletDef != null) {
                        setVisible("saveBtn", formAppletDef.getPropValue(boolean.class,
                                AppletPropertyConstants.CREATE_FORM_SAVE, false));
                        setVisible("saveNextBtn", allowSaveAndNext && formAppletDef.getPropValue(boolean.class,
                                AppletPropertyConstants.CREATE_FORM_SAVE_NEXT, false));
                        setVisible("saveCloseBtn", formAppletDef.getPropValue(boolean.class,
                                AppletPropertyConstants.CREATE_FORM_SAVE_CLOSE, false));
                        setVisible("submitCloseBtn", enableCreateSubmit && formAppletDef.getPropValue(boolean.class,
                                AppletPropertyConstants.CREATE_FORM_SUBMIT, false));
                        setVisible("submitNextBtn", enableCreateSubmit && formAppletDef.getPropValue(boolean.class,
                                AppletPropertyConstants.CREATE_FORM_SUBMIT_NEXT, false));
                    } else {
                        setVisible("saveBtn", true);
                        setVisible("saveNextBtn", false);
                        setVisible("saveCloseBtn", false);
                        setVisible("submitCloseBtn", false);
                        setVisible("submitNextBtn", false);
                    }
                } else {
                    setVisible("saveBtn", false);
                    setVisible("saveNextBtn", false);
                    setVisible("saveCloseBtn", false);
                    setVisible("submitCloseBtn", false);
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
        getEntityFormApplet().newEntityInst();
        getRequestContextUtil().setContentScrollReset();
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
            setCommandResultMapping(ResultMappingConstants.CLOSE);
            return;
        }

        applet.navBackToPrevious();
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
        setCommandResultMapping("showfileattachments");
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
        FormContext ctx = evaluateCurrentFormContext(entitySaveAs.getInputForm().getCtx(), EvaluationMode.CREATE);
        Object inst = entitySaveAs.getInputForm().getFormBean();
        Long saveApplicatIonId = null;
        if (inst instanceof BaseApplicationEntity) {
            BaseApplicationEntity entity = (BaseApplicationEntity) inst;
            saveApplicatIonId = entity.getApplicationId();
            if (!applet.au().isApplicationDevelopable(saveApplicatIonId)) {
                ctx.addValidationError("applicationId",
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
        FormContext ctx = evaluateCurrentFormContext(EvaluationMode.getRequiredMode(formActionDef.isValidateForm()));
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = applet.formActionOnInst(formActionDef.getPolicy(), actionName);
            handleEntityActionResult(entityActionResult);
        }
    }

    @Action
    public void saveAssignAndClose() throws UnifyException {
        AbstractEntityFormApplet applet = getEntityFormApplet();
        AssignmentPage assignmentPage = applet.getAssignmentPage();
        assignmentPage.commitAssignedList(false);
        applet.navBackToPrevious();
        hintUser("$m{entityformapplet.assignment.success.hint}", assignmentPage.getSubTitle());
    }

    @Action
    public void saveEntryAndClose() throws UnifyException {
        AbstractEntityFormApplet applet = getEntityFormApplet();
        EntryTablePage entryTablePage = applet.getEntryTablePage();
        entryTablePage.commitEntryList(false);
        if (entryTablePage.isWithValidationErrors()) {
            hintUser(MODE.ERROR, "$m{entityformapplet.entrytable.errors.hint}", entryTablePage.getSubTitle());
        } else {
            applet.navBackToPrevious();
            hintUser("$m{entityformapplet.entrytable.success.hint}", entryTablePage.getSubTitle());
        }
    }

    @Action
    public void savePropListAndClose() throws UnifyException {
        AbstractEntityFormApplet applet = getEntityFormApplet();
        EditPropertyList editPropertyList = applet.getEditPropertyList();
        editPropertyList.commitPropertyList();
        applet.navBackToPrevious();
        hintUser("$m{entityformapplet.editpropertylist.success.hint}", editPropertyList.getSubTitle());
    }

    @Action
    public void save() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(EvaluationMode.CREATE);
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().saveNewInst();
            entityActionResult.setSuccessHint("$m{entityformapplet.new.success.hint}");
            handleEntityActionResult(entityActionResult, ctx);
        }
    }

    @Action
    public void saveAndNext() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(EvaluationMode.CREATE);
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().saveNewInstAndNext();
            entityActionResult.setSuccessHint("$m{entityformapplet.new.success.hint}");
            handleEntityActionResult(entityActionResult, ctx);
        }
    }

    @Action
    public void saveAndClose() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(EvaluationMode.CREATE);
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
        FormContext ctx = evaluateCurrentFormContext(evalMode);
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
        FormContext ctx = evaluateCurrentFormContext(evalMode);
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
        FormContext ctx = evaluateCurrentFormContext(EvaluationMode.UPDATE);
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().updateInst();
            entityActionResult.setSuccessHint("$m{entityformapplet.update.success.hint}");
            handleEntityActionResult(entityActionResult, ctx);
        }
    }

    @Action
    public void updateAndClose() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(EvaluationMode.UPDATE);
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().updateInstAndClose();
            entityActionResult.setSuccessHint("$m{entityformapplet.update.success.hint}");
            handleEntityActionResult(entityActionResult, ctx);
        }
    }

    @Action
    public void delete() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(EvaluationMode.DELETE);
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().deleteInst();
            entityActionResult.setSuccessHint("$m{entityformapplet.delete.success.hint}");
            handleEntityActionResult(entityActionResult, ctx);
        }
    }

    @Action
    public void previous() throws UnifyException {
        getEntityFormApplet().previousInst();
        getRequestContextUtil().setContentScrollReset();
    }

    @Action
    public void next() throws UnifyException {
        getEntityFormApplet().nextInst();
        getRequestContextUtil().setContentScrollReset();
    }

    @Action
    public void maintain() throws UnifyException {
        String[] po = StringUtils.charSplit(getRequestTarget(String.class), ':');
        if (po.length > 0) {
            String valMarker = po[0];
            int mIndex = Integer.parseInt(po[1]);
            if (valMarker != null) {
                switch (valMarker) {
                    case EntitySearchValueMarkerConstants.CHILD_LIST:
                        getEntityFormApplet().maintainChildInst(mIndex);
                        return;
                    case EntitySearchValueMarkerConstants.RELATED_LIST:
                        getEntityFormApplet().maintainRelatedInst(mIndex);
                        return;
                    case EntitySearchValueMarkerConstants.HEADLESS_LIST:
                        getEntityFormApplet().maintainHeadlessInst(mIndex);
                        return;
                    default:
                }
            }

            TableActionResult result = getEntityFormApplet().maintainInst(mIndex);
            if (result != null) {
                if (result.isDisplayListingReport()) {
                    setRequestAttribute(FlowCentralRequestAttributeConstants.REPORT, result.getResult());
                    setCommandResultMapping("viewlistingreport");
                }
            }
            
            getRequestContextUtil().setContentScrollReset();
        } else {
            setCommandResultMapping(ResultMappingConstants.NONE);
        }
    }

    @Action
    public void listing() throws UnifyException {
        String[] po = StringUtils.charSplit(getRequestTarget(String.class), ':');
        String valMarker = po[0];
        int mIndex = Integer.parseInt(po[1]);
        if (valMarker != null) {
            switch (valMarker) {
                case EntitySearchValueMarkerConstants.CHILD_LIST:
                    // TODO
//                    getEntityFormApplet().maintainChildInst(mIndex);
                    return;
                case EntitySearchValueMarkerConstants.RELATED_LIST:
                    // TODO
//                    getEntityFormApplet().maintainRelatedInst(mIndex);
                    return;
                case EntitySearchValueMarkerConstants.HEADLESS_LIST:
                    // TODO
//                    getEntityFormApplet().maintainHeadlessInst(mIndex);
                    return;
                default:
            }
        }

        getEntityFormApplet().listingInst(mIndex);
        getRequestContextUtil().setContentScrollReset();
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

    protected SystemModuleService system() {
        return systemModuleService;
    }

    protected void onReviewErrors(EntityActionResult entityActionResult) throws UnifyException {
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
        if (reviewResult != null && reviewResult.isSkippableOnly()) {
            getEntityFormApplet().getCtx().setOriginalEntityActionResult(entityActionResult);
            final String message = getReviewSkippableMessage(reviewResult);
            final String commandPath = getCommandFullPath("reviewConfirm");
            showMessageBox(MessageIcon.WARNING, MessageMode.YES_NO, "$m{entityformapplet.formreview}", message,
                    commandPath);
        } else {
            showMessageBox(MessageIcon.WARNING, MessageMode.OK, "$m{entityformapplet.formreview}",
                    "$m{entityformapplet.formreview.failure}", "/application/refreshContent");
        }
    }

    private void handleEntityActionResult(EntityActionResult entityActionResult) throws UnifyException {
        handleEntityActionResult(entityActionResult, null);
    }

    private void handleEntityActionResult(EntityActionResult entityActionResult, FormContext ctx)
            throws UnifyException {
        if (entityActionResult.isRefreshMenu()) {
            refreshApplicationMenu();
        }

        if (ctx != null && ctx.isWithReviewErrors()) {
            onReviewErrors(entityActionResult);
        } else {
            setCommandResultMapping(entityActionResult, false);
        }

        handleHints(entityActionResult, ctx);
    }

    private void handleHints(EntityActionResult entityActionResult, FormContext ctx) throws UnifyException {
        String successHint = entityActionResult.getSuccessHint();
        if (!StringUtils.isBlank(successHint)) {
            formHintSuccess(successHint, ctx != null ? ctx.getEntityName() : null);
        }
    }

    private void setCommandResultMapping(EntityActionResult entityActionResult, boolean refereshPanel)
            throws UnifyException {
        if (entityActionResult.isHidePopupOnly()) {
            setCommandResultMapping(ResultMappingConstants.REFRESH_HIDE_POPUP);
        } else if (entityActionResult.isWithResultPath()) {
            commandPost(entityActionResult.getResultPath());
        } else if (entityActionResult.isWithTaskResult()) {
            fireEntityActionResultTask(entityActionResult);
        } else if (entityActionResult.isCloseView()) {
            if (getEntityFormApplet().navBackToPrevious()) {
                if (refereshPanel) {
                    getEntityFormApplet().au().commandRefreshPanelsAndHidePopup(this);
                }
            } else {
                setCommandResultMapping(ResultMappingConstants.CLOSE);
            }
        } else if (entityActionResult.isClosePage()) {
            setCommandResultMapping(ResultMappingConstants.CLOSE);
        } else if (entityActionResult.isDisplayListingReport()) {
            setRequestAttribute(FlowCentralRequestAttributeConstants.REPORT, entityActionResult.getResult());
//            getEventLogger().logUserEvent(CommonModuleAuditConstants.GENERATE_REPORT, reportOptions.getTitle());
            setCommandResultMapping("viewlistingreport");
        }
    }

    private String getReviewSkippableMessage(ReviewResult reviewResult) throws UnifyException {
        StringBuilder sb = new StringBuilder();
        boolean appendSym = false;
        for (String msg : reviewResult.getSkippableMessages()) {
            if (appendSym) {
                sb.append(' ');
            } else {
                appendSym = true;
            }

            sb.append(resolveSessionMessage(msg));
        }

        String msg = sb.toString();
        return resolveSessionMessage("$m{entityformapplet.formreview.skippable}", msg);
    }

    private void fireEntityActionResultTask(EntityActionResult entityActionResult) throws UnifyException {
        // TODO Set success and failure path
        launchTaskWithMonitorBox(entityActionResult.getResultTaskSetup(), entityActionResult.getResultTaskCaption());
    }

    protected AbstractEntityFormApplet getEntityFormApplet() throws UnifyException {
        return getValue(AbstractEntityFormApplet.class);
    }

    protected FormContext evaluateCurrentFormContext(EvaluationMode evaluationMode) throws UnifyException {
        return evaluateCurrentFormContext(evaluationMode, false);
    }

    protected FormContext evaluateCurrentFormContext(EvaluationMode evaluationMode, boolean commentRequired)
            throws UnifyException {
        FormContext ctx = getEntityFormApplet().getResolvedForm().getCtx();
        if (ctx.getFormDef() != null && ctx.getFormDef().isInputForm()) {
            evaluateCurrentFormContext(ctx, evaluationMode);
        } else {
            ctx.clearReviewErrors();
            ctx.clearValidationErrors();
        }

        if (evaluationMode.evaluation()) {
            if (evaluationMode.review() && ctx.getAppletContext().isReview()) {
                AbstractEntityFormApplet applet = getEntityFormApplet();
                final AbstractEntityFormApplet.ViewMode viewMode = applet.getMode();
                if (commentRequired) {
                    FormPanel commentsFormPanel = viewMode == AbstractEntityFormApplet.ViewMode.LISTING_FORM
                            ? getWidgetByShortName(FormPanel.class, "listingPanel.commentsPanel")
                            : (viewMode == AbstractEntityFormApplet.ViewMode.SINGLE_FORM
                                    ? getWidgetByShortName(FormPanel.class, "singleFormPanel.commentsPanel")
                                    : getWidgetByShortName(FormPanel.class, "formPanel.commentsPanel"));
                    ctx.mergeValidationErrors(commentsFormPanel.validate(evaluationMode));
                }

                if (ctx.getAppletContext().isEmails()) {
                    FormPanel emailsFormPanel = viewMode == AbstractEntityFormApplet.ViewMode.LISTING_FORM
                            ? getWidgetByShortName(FormPanel.class, "listingPanel.emailsPanel")
                            : (viewMode == AbstractEntityFormApplet.ViewMode.SINGLE_FORM
                                    ? getWidgetByShortName(FormPanel.class, "singleFormPanel.emailsPanel")
                                    : getWidgetByShortName(FormPanel.class, "formPanel.emailsPanel"));
                    ctx.mergeValidationErrors(emailsFormPanel.validate(evaluationMode));
                }
            }

            if (ctx.isWithFormErrors()) {
                hintUser(MODE.ERROR, "$m{entityformapplet.formvalidation.error.hint}");
            }
        }

        return ctx;
    }

    private FormContext evaluateCurrentFormContext(final FormContext ctx, EvaluationMode evaluationMode)
            throws UnifyException {
        AbstractEntityFormApplet applet = getEntityFormApplet();
        applet.au().getFormContextEvaluator().evaluateFormContext(ctx, evaluationMode);

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

    private void formHintSuccess(String messageKey, String entityName) throws UnifyException {
        if (!StringUtils.isBlank(entityName)) {
            hintUser(messageKey, StringUtils.capitalizeFirstLetter(entityName));
        } else {
            hintUser(messageKey);
        }
    }

}

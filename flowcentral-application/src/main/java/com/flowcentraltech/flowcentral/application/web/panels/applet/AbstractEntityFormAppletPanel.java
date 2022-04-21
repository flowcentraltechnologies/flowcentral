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
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.FormActionDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormTabDef;
import com.flowcentraltech.flowcentral.application.data.TabDef;
import com.flowcentraltech.flowcentral.application.validation.FormContextEvaluator;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm;
import com.flowcentraltech.flowcentral.application.web.panels.EditPropertyList;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySaveAs;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySearchValueMarkerConstants;
import com.flowcentraltech.flowcentral.application.web.panels.HeaderWithTabsForm;
import com.flowcentraltech.flowcentral.application.web.widgets.AssignmentPage;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet.TabSheetItem;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheetWidget;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.CollaborationProvider;
import com.flowcentraltech.flowcentral.common.business.FileAttachmentProvider;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
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
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;
import com.tcdng.unify.web.ui.widget.data.MessageIcon;
import com.tcdng.unify.web.ui.widget.data.MessageMode;

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
    private FormContextEvaluator formContextEvaluator;

    @Configurable
    private SystemModuleService systemModuleService;

    @Configurable
    private FileAttachmentProvider fileAttachmentProvider;

    @Configurable
    private CollaborationProvider collaborationProvider;

    private String focusMemoryId;

    private String tabMemoryId;

    public void setCollaborationProvider(CollaborationProvider collaborationProvider) {
        this.collaborationProvider = collaborationProvider;
    }

    public void setApplicationPrivilegeManager(ApplicationPrivilegeManager applicationPrivilegeManager) {
        this.applicationPrivilegeManager = applicationPrivilegeManager;
    }

    public void setFormContextEvaluator(FormContextEvaluator formContextEvaluator) {
        this.formContextEvaluator = formContextEvaluator;
    }

    public void setSystemModuleService(SystemModuleService systemModuleService) {
        this.systemModuleService = systemModuleService;
    }

    public void setFileAttachmentProvider(FileAttachmentProvider fileAttachmentProvider) {
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
        final AppletContext appCtx = applet.getCtx();
        final boolean isCollaboration = applet.isCollaboration() && collaborationProvider != null
                && collaborationProvider.isLicensedForCollaboration();
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
            EntityDef formEntityDef = form.getFormDef().getEntityDef();
            if (formAppletDef != null) { // Normal, null for workflow applet root
                enableSaveAs = formAppletDef.getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_SAVEAS,
                        false)
                        && applicationPrivilegeManager.isRoleWithPrivilege(roleCode, formEntityDef.getAddPrivilege());
                enableUpdate = isContextEditable
                        && formAppletDef.getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_UPDATE,
                                false)
                        && applicationPrivilegeManager.isRoleWithPrivilege(roleCode, formEntityDef.getEditPrivilege())
                        && applet.formBeanMatchAppletPropertyCondition(
                                AppletPropertyConstants.MAINTAIN_FORM_UPDATE_CONDITION);
                enableDelete = !isInWorkflow && isContextEditable
                        && formAppletDef.getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_DELETE,
                                false)
                        && applicationPrivilegeManager.isRoleWithPrivilege(roleCode, formEntityDef.getDeletePrivilege())
                        && applet.formBeanMatchAppletPropertyCondition(
                                AppletPropertyConstants.MAINTAIN_FORM_DELETE_CONDITION);
                enableAttachment = isRootForm && formEntityDef.getBaseType().isWorkEntityType()
                        && formEntityDef.isWithAttachments() && formAppletDef.getPropValue(boolean.class,
                                AppletPropertyConstants.MAINTAIN_FORM_ATTACHMENTS, false);
                enableUpdateSubmit = !isInWorkflow && isRootForm && applet
                        .formBeanMatchAppletPropertyCondition(AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_CONDITION);
                if (enableAttachment) {
                    applet.setFileAttachmentsDisabled(!applicationPrivilegeManager.isRoleWithPrivilege(roleCode,
                            formEntityDef.getAttachPrivilege()));
                }
            } else {
                enableUpdate = isContextEditable
                        && applicationPrivilegeManager.isRoleWithPrivilege(roleCode, formEntityDef.getEditPrivilege())
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

        if (viewMode.isInForm()) {
            boolean showAlternateFormActions = systemModuleService.getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.SHOW_FORM_ALTERNATE_ACTIONS);
            setVisible("formPanel.altActionPanel", showAlternateFormActions);
            setVisible("frmActionBtns", !DataUtils.isBlank(form.getFormActionDefList()));
            form.getCtx().setFocusMemoryId(focusMemoryId);
            form.getCtx().setTabMemoryId(tabMemoryId);
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
            case LISTING_FORM:
                switchContent("listingPanel");
                setDisabled("listPrevBtn", !applet.isPrevNav());
                setDisabled("listNextBtn", !applet.isNextNav());
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
                break;
            case MAINTAIN_PRIMARY_FORM_NO_SCROLL:
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
                        && !form.getFormDef().isChildOrChildListTabs();
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
                break;
            case CUSTOM_PAGE:
                break;
            case SEARCH:
            case HEADLESS_TAB:
            default:
                break;
        }
    }

    @Action
    public void newInst() throws UnifyException {
        getEntityFormApplet().newEntityInst();
        getRequestContextUtil().setContentScrollReset();
    }

    @Action
    public void navBackToPrevious() throws UnifyException {
        AbstractEntityFormApplet applet = getEntityFormApplet();
        boolean strictUpdate = applet.getAu().getSysParameterValue(boolean.class,
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
    public void assignSwitchOnChange() throws UnifyException {
        getEntityFormApplet().assignSwitchOnChange();
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
        if (!ctx.isWithFormErrors()) {
            String saveAsPolicy = applet.getRootAppletProp(String.class,
                    AppletPropertyConstants.MAINTAIN_FORM_SAVEAS_POLICY);
            EntityActionResult entityActionResult = applet.saveEntityAs(saveAsPolicy);
            entityActionResult.setSuccessHint("$m{entityformapplet.saveas.success.hint}");
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
        FormContext ctx = evaluateCurrentFormContext(EvaluationMode.REQUIRED);
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = applet.formActionOnInst(formActionDef.getPolicy());
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

        getEntityFormApplet().maintainInst(mIndex);
        getRequestContextUtil().setContentScrollReset();
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

    private void handleEntityActionResult(EntityActionResult entityActionResult) throws UnifyException {
        handleEntityActionResult(entityActionResult, null);
    }

    private void handleEntityActionResult(EntityActionResult entityActionResult, FormContext ctx)
            throws UnifyException {
        if (entityActionResult.isRefreshMenu()) {
            refreshApplicationMenu();
        }

        if (ctx != null && ctx.isWithReviewErrors()) {
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
            final String fullActionPath = "/application/refreshContent";
            showMessageBox(MessageIcon.WARNING, MessageMode.OK, "$m{entityformapplet.formreview}",
                    "$m{entityformapplet.formreview.failure}", fullActionPath);
        } else if (entityActionResult.isWithResultPath()) {
            commandPost(entityActionResult.getResultPath());
        } else if (entityActionResult.isWithTaskResult()) {
            fireEntityActionResultTask(entityActionResult);
        } else if (entityActionResult.isCloseView()) {
            getEntityFormApplet().navBackToPrevious();
        } else if (entityActionResult.isClosePage()) {
            setCommandResultMapping(ResultMappingConstants.CLOSE);
        }

        String successHint = entityActionResult.getSuccessHint();
        if (ctx != null && !StringUtils.isBlank(successHint)) {
            formHintSuccess(successHint, ctx.getEntityName());
        }
    }

    private void fireEntityActionResultTask(EntityActionResult entityActionResult) throws UnifyException {
        // TODO Set success and failure path
        launchTaskWithMonitorBox(entityActionResult.getResultTaskSetup(), entityActionResult.getResultTaskCaption());
    }

    protected AbstractEntityFormApplet getEntityFormApplet() throws UnifyException {
        return getValue(AbstractEntityFormApplet.class);
    }

    protected FormContext evaluateCurrentFormContext(EvaluationMode evaluationMode) throws UnifyException {
        FormContext ctx = getEntityFormApplet().getResolvedForm().getCtx();
        if (ctx.getFormDef().isInputForm()) {
            evaluateCurrentFormContext(ctx, evaluationMode);
        }
        
        return ctx;
    }

    private FormContext evaluateCurrentFormContext(final FormContext ctx, EvaluationMode evaluationMode)
            throws UnifyException {
        AbstractEntityFormApplet applet = getEntityFormApplet();
        formContextEvaluator.evaluateFormContext(ctx, evaluationMode);

        // Detect tab error
        final boolean isWithFieldErrors = ctx.isWithFieldErrors();
        HeaderWithTabsForm form = applet.getForm();
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

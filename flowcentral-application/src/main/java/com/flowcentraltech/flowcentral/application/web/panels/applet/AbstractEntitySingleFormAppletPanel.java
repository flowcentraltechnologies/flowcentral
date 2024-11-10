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

import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.AppletRequestAttributeConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationResultMappingConstants;
import com.flowcentraltech.flowcentral.application.constants.WorkflowDraftType;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.Diff;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.HelpSheetDef;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySelect;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySingleForm;
import com.flowcentraltech.flowcentral.application.web.panels.FormPanel;
import com.flowcentraltech.flowcentral.application.web.panels.HelpFormInfo;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.FormValidationContext;
import com.flowcentraltech.flowcentral.common.business.policies.TableActionResult;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.constants.WfItemVersionType;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.data.IndexedTarget;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.Panel;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;
import com.tcdng.unify.web.ui.widget.data.Popup;

/**
 * Convenient abstract base panel for entity single form applet panels.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@UplBinding("web/application/upl/entitysingleformappletpanel.upl")
public abstract class AbstractEntitySingleFormAppletPanel extends AbstractAppletPanel {

    @SuppressWarnings("unchecked")
    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        final AbstractEntitySingleFormApplet applet = getEntityFormApplet();
        applet.ensureRootAppletStruct();

        final AppletDef _appletDef = applet.getRootAppletDef();
        final AppletContext appCtx = applet.appletCtx();
        final AbstractEntitySingleFormApplet.ViewMode viewMode = applet.getMode();
        final EntityDef _entityDef = applet.getEntityDef();
        final String roleCode = getUserToken().getRoleCode();
        final EntitySingleForm form = applet.getForm();
        final Entity inst = form != null ? (Entity) form.getFormBean() : null;
        final boolean isWorkflowCopyForm = _appletDef != null
                && _appletDef.getPropValue(boolean.class, AppletPropertyConstants.WORKFLOWCOPY);
        final boolean isInWorkflow = inst instanceof WorkEntity && ((WorkEntity) inst).isInWorkflow();
        final boolean isUpdateCopy = inst instanceof WorkEntity
                && WfItemVersionType.DRAFT.equals(((WorkEntity) inst).getWfItemVersionType());
        appCtx.setInWorkflow(isInWorkflow);

        // Page synchronization
        if (_appletDef != null) {
            EntityClassDef entityClassDef = application().getEntityClassDef(_entityDef.getLongName());
            if (viewMode.isInForm() && inst != null) {
                setClientListenToEntity((Class<? extends Entity>) entityClassDef.getEntityClass(), inst.getId());
            } else {
                setClientListenToEntity((Class<? extends Entity>) entityClassDef.getEntityClass());
            }
        }

        final boolean isContextEditable = appCtx.isContextEditable();
        boolean enableUpdate = false;
        boolean enableDelete = false;
        boolean enableCreate = false;
        boolean enableCreateSubmit = false;
        boolean enableUpdateSubmit = false;
        boolean capture = false;
        if (viewMode.isCreateForm()) {
            enableCreate = isContextEditable
                    && applicationPrivilegeManager().isRoleWithPrivilege(roleCode, _entityDef.getAddPrivilege());
            enableCreateSubmit = !isWorkflowCopyForm && applet
                    .formBeanMatchAppletPropertyCondition(AppletPropertyConstants.CREATE_FORM_SUBMIT_CONDITION);
        } else if (viewMode.isMaintainForm()) {
            capture = _appletDef.getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_CAPTURE, false);
            enableUpdate = isContextEditable && !isUpdateCopy
                    && _appletDef.getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_UPDATE, false)
                    && applicationPrivilegeManager().isRoleWithPrivilege(roleCode, _entityDef.getEditPrivilege())
                    && applet.formBeanMatchAppletPropertyCondition(
                            AppletPropertyConstants.MAINTAIN_FORM_UPDATE_CONDITION);
            enableDelete = !isInWorkflow && isContextEditable && !isUpdateCopy
                    && _appletDef.getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_DELETE, false)
                    && applicationPrivilegeManager().isRoleWithPrivilege(roleCode, _entityDef.getDeletePrivilege())
                    && applet.formBeanMatchAppletPropertyCondition(
                            AppletPropertyConstants.MAINTAIN_FORM_DELETE_CONDITION);
            enableUpdateSubmit = !isWorkflowCopyForm && !isInWorkflow && applet
                    .formBeanMatchAppletPropertyCondition(AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_CONDITION);
        }

        appCtx.setCapture(capture);
        if (viewMode.isInForm()) {
            boolean showAlternateFormActions = system().getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.SHOW_FORM_ALTERNATE_ACTIONS);
            setVisible("formPanel.altActionPanel", showAlternateFormActions);
            setVisible("formPanel.emailsPanel", appCtx.isReview() && appCtx.isEmails());
            setVisible("formPanel.attachmentsPanel",
                    appCtx.isCapture() || (appCtx.isAttachments() && appCtx.isReview()));
            setVisible("formPanel.commentsPanel", appCtx.isReview() && appCtx.isComments());
            setVisible("formPanel.errorsPanel", appCtx.isReview() && appCtx.isRecovery());
            setVisible("frmActionBtns", !DataUtils.isBlank(form.getFormActionDefList()));
            setEditable("formPanel.errorsPanel", false);
            Panel formPanel = getWidgetByShortName(Panel.class, "formPanel");
            setPageAttribute("formPanel.id", formPanel.getId());
        }

        boolean showDiff = false;
        boolean overview = false;
        if (form != null) {
            if (form.getFormDef().isWithHelpSheet()) {
                overview = application().getHelpSheetDef(form.getFormDef().getHelpSheet()).isWithHelpOverview();
            }

            form.getCtx().setUpdateEnabled(enableUpdate);
            form.clearDisplayItem();
            if (isUpdateCopy) {
                showDiff = isInWorkflow && viewMode.isMaintainForm();
                form.setDisplayItemCounterClass("fc-dispcounterorange");
                form.setDisplayItemCounter(
                        resolveSessionMessage("$m{entityformapplet.form.workflowupdatecopy.viewonly}"));
            } else if (appCtx.isInWorkflow() && !appCtx.isReview()) {
                form.setDisplayItemCounterClass("fc-dispcounterorange");
                form.setDisplayItemCounter(resolveSessionMessage("$m{entityformapplet.form.inworkflow.viewonly}"));
            }

            if (form.isWithAttachments()) {
                form.getAttachments()
                        .setErrorMsg(form.getCtx().isWithSectionError("documents")
                                ? form.getCtx().getSectionError("documents").get(0)
                                : null);
            }
        }

        setVisible("frmDiffBtn", showDiff);
        setVisible("overviewBtn", overview);
        switch (viewMode) {
            case MAINTAIN_FORM_SCROLL:
                final boolean closable = !appCtx.isInDetachedWindow();
                switchContent("formPanel");
                setVisible("cancelBtn", true);
                setVisible("saveBtn", false);
                setVisible("saveNextBtn", false);
                setVisible("saveCloseBtn", false);
                setVisible("submitCloseBtn", enableUpdateSubmit
                        && _appletDef.getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_SUBMIT, false));
                setVisible("submitNextBtn", enableUpdateSubmit && _appletDef.getPropValue(boolean.class,
                        AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_NEXT, false));
                setVisible("prevBtn", closable);
                setVisible("nextBtn", closable);
                setDisabled("prevBtn", !applet.isPrevNav());
                setDisabled("nextBtn", !applet.isNextNav());
                setVisible("updateBtn", enableUpdate);
                setVisible("updateCloseBtn", enableUpdate);
                setVisible("deleteBtn", enableDelete);

                setVisible("displayCounterLabel", true);
                if (!form.isWithDisplayItemCounter()) {
                    form.setDisplayItemCounter(applet.getDisplayItemCounter());
                }

                if (form.isWithAttachments()) {
                    form.getAttachments().setEditable(enableUpdate);
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
                setVisible("submitCloseBtn", enableUpdateSubmit
                        && _appletDef.getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_SUBMIT, false));
                setVisible("submitNextBtn", enableUpdateSubmit && _appletDef.getPropValue(boolean.class,
                        AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_NEXT, false));
                setVisible("prevBtn", false);
                setVisible("nextBtn", false);
                setVisible("displayCounterLabel", false);
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
                switchContent("formPanel");
                setVisible("cancelBtn", true);
                setVisible("saveBtn", false);
                setVisible("saveNextBtn", false);
                setVisible("saveCloseBtn", false);
                setVisible("submitCloseBtn", enableUpdateSubmit
                        && _appletDef.getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_SUBMIT, false));
                setVisible("submitNextBtn", enableUpdateSubmit && _appletDef.getPropValue(boolean.class,
                        AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_NEXT, false));
                setVisible("prevBtn", false);
                setVisible("nextBtn", false);
                setVisible("displayCounterLabel", false);
                setVisible("updateBtn", enableUpdate);
                setVisible("updateCloseBtn", enableUpdate);
                setVisible("deleteBtn", enableDelete);
                if (form.isWithAttachments()) {
                    form.getAttachments().setEditable(enableUpdate);
                }

                setEditable("formPanel", enableUpdate);
                addPanelToPushComponents("formPanel", enableUpdate);
                break;
            case NEW_PRIMARY_FORM:
                enableCreate = true;
            case NEW_FORM:
                switchContent("formPanel");
                setVisible("cancelBtn", true);
                final boolean allowSaveAndNext = true;
                if (enableCreate) {
                    setVisible("saveBtn",
                            _appletDef.getPropValue(boolean.class, AppletPropertyConstants.CREATE_FORM_SAVE, false));
                    setVisible("saveNextBtn", allowSaveAndNext && _appletDef.getPropValue(boolean.class,
                            AppletPropertyConstants.CREATE_FORM_SAVE_NEXT, false));
                    setVisible("saveCloseBtn", _appletDef.getPropValue(boolean.class,
                            AppletPropertyConstants.CREATE_FORM_SAVE_CLOSE, false));
                    setVisible("submitCloseBtn", enableCreateSubmit && _appletDef.getPropValue(boolean.class,
                            AppletPropertyConstants.CREATE_FORM_SUBMIT, false));
                    setVisible("submitNextBtn", enableCreateSubmit && _appletDef.getPropValue(boolean.class,
                            AppletPropertyConstants.CREATE_FORM_SUBMIT_NEXT, false));
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
                setVisible("updateBtn", false);
                setVisible("updateCloseBtn", false);
                setVisible("deleteBtn", false);
                if (form.isWithAttachments()) {
                    form.getAttachments().setEditable(true);
                }

                setEditable("formPanel", true);
                addPanelToPushComponents("formPanel", true);
                break;
            case SEARCH:
            default:
                break;
        }
    }

    @Action
    public void newInst() throws UnifyException {
        TableActionResult result = getEntityFormApplet().newEntityInst();
        if (result != null) {
            if (result.isOpenTab()) {
                result.setMultiPage(true);
                openInBrowserTab(result, getEntityFormApplet().getSingleFormAppletDef(), FormMode.CREATE);
            } else if (result.isOpenPath()) {
                setCommandOpenPath((String) result.getResult());
            }
        }
    }

    @Action
    public void navBackToPrevious() throws UnifyException {
        AbstractEntitySingleFormApplet applet = getEntityFormApplet();
        if (applet.getMode().isPrimary()) {
            setCloseResultMapping();
            return;
        }

        applet.navBackToPrevious();
    }

    @Action
    public void navBackToSearch() throws UnifyException {
        getEntityFormApplet().navBackToSearch();
    }

    @Action
    public void performFormAction() throws UnifyException {

    }

    @Action
    public void showFormFileAttachments() throws UnifyException {
        final AbstractEntitySingleFormApplet applet = getEntityFormApplet();
        if (applet.isPromptEnterWorkflowDraft()) {
            showPromptWorkflowDraft(WorkflowDraftType.UPDATE, IndexedTarget.BLANK);
        } else {
            setCommandResultMapping(ApplicationResultMappingConstants.SHOW_FILE_ATTACHMENTS);
        }
    }

    @Action
    public void diff() throws UnifyException {
        final AbstractEntitySingleFormApplet applet = getEntityFormApplet();
        Diff diff = applet.diff();
        setRequestAttribute(AppletRequestAttributeConstants.FORM_DIFF, diff);
        setCommandResultMapping(ApplicationResultMappingConstants.SHOW_DIFF);
    }

    @Action
    public void save() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(EvaluationMode.CREATE));
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().saveNewInst();
            entityActionResult.setSuccessHint("$m{entityformapplet.new.success.hint}");
            handleEntityActionResult(entityActionResult, ctx.getEntityName());
        }
    }

    @Action
    public void saveAndNext() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(EvaluationMode.CREATE));
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().saveNewInstAndNext();
            entityActionResult.setSuccessHint("$m{entityformapplet.new.success.hint}");
            handleEntityActionResult(entityActionResult, ctx.getEntityName());
        }
    }

    @Action
    public void saveAndClose() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(EvaluationMode.CREATE));
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().saveNewInstAndClose();
            entityActionResult.setSuccessHint("$m{entityformapplet.new.success.hint}");
            handleEntityActionResult(entityActionResult, ctx.getEntityName());
        }
    }

    @Action
    public void submit() throws UnifyException {
        final AbstractEntitySingleFormApplet applet = getEntityFormApplet();
        final EvaluationMode evalMode = applet.getMode().isMaintainForm()
                ? EvaluationMode.getUpdateSubmitMode(applet.getRootAppletProp(boolean.class,
                        AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_VALIDATE, false))
                : EvaluationMode.getCreateSubmitMode(applet.getRootAppletProp(boolean.class,
                        AppletPropertyConstants.CREATE_FORM_SUBMIT_VALIDATE, false));
        FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(evalMode));
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = applet.submitInst();
            entityActionResult.setSuccessHint("$m{entityformapplet.submit.success.hint}");
            handleEntityActionResult(entityActionResult, ctx.getEntityName());
        }
    }

    @Action
    public void submitAndNext() throws UnifyException {
        final AbstractEntitySingleFormApplet applet = getEntityFormApplet();
        final EvaluationMode evalMode = applet.getMode().isMaintainForm()
                ? EvaluationMode.getUpdateSubmitMode(applet.getRootAppletProp(boolean.class,
                        AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_VALIDATE, false))
                : EvaluationMode.getCreateSubmitMode(applet.getRootAppletProp(boolean.class,
                        AppletPropertyConstants.CREATE_FORM_SUBMIT_VALIDATE, false));
        FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(evalMode));
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().submitInstAndNext();
            entityActionResult.setSuccessHint("$m{entityformapplet.submit.success.hint}");
            handleEntityActionResult(entityActionResult, ctx.getEntityName());
        }
    }

    @Action
    public void update() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(EvaluationMode.UPDATE));
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().updateInst();
            entityActionResult.setSuccessHint("$m{entityformapplet.update.success.hint}");
            handleEntityActionResult(entityActionResult, ctx.getEntityName());
        }
    }

    @Action
    public void updateAndClose() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(EvaluationMode.UPDATE));
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().updateInstAndClose();
            entityActionResult.setSuccessHint("$m{entityformapplet.update.success.hint}");
            handleEntityActionResult(entityActionResult, ctx.getEntityName());
        }
    }

    @Action
    public void overview() throws UnifyException {
        final FormContext ctx = getEntityFormApplet().getForm().getCtx();
        final FormDef formDef = ctx.getFormDef();
        if (formDef.isWithHelpSheet()) {
            HelpSheetDef helpSheetDef = application().getHelpSheetDef(formDef.getHelpSheet());
            if (helpSheetDef.isWithHelpOverview()) {
                final String label = resolveSessionMessage("$m{button.overview.title}",
                        formDef.isWithLabel() ? formDef.getLabel() : "");
                HelpFormInfo helpFormInfo = new HelpFormInfo(label, helpSheetDef.getHelpOverview());
                commandShowPopup(new Popup(ApplicationResultMappingConstants.SHOW_HELP_FORM, helpFormInfo));
            }
        }
    }

    @Action
    public void delete() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(new FormValidationContext(EvaluationMode.DELETE));
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().deleteInst();
            entityActionResult.setSuccessHint("$m{entityformapplet.delete.success.hint}");
            handleEntityActionResult(entityActionResult, ctx.getEntityName());
        }
    }

    @Action
    public void previous() throws UnifyException {
        TableActionResult result = getEntityFormApplet().previousInst();
        processTableActionResult(result);
    }

    @Action
    public void next() throws UnifyException {
        TableActionResult result = getEntityFormApplet().nextInst();
        processTableActionResult(result);
    }

    @Action
    public void maintain() throws UnifyException {
        IndexedTarget target = getRequestTarget(IndexedTarget.class);
        if (target.isValidIndex()) {
            TableActionResult result = getEntityFormApplet().maintainInst(target.getIndex());
            processTableActionResult(result);
        }
    }

    @Action
    public void columns() throws UnifyException {
        // TODO
    }

    @Override
    protected void onReviewErrors(EntityActionResult entityActionResult) throws UnifyException {

    }

    private void processTableActionResult(TableActionResult result) throws UnifyException {
        if (result != null) {
            if (result.isOpenTab()) {
                openInBrowserTab(result, getEntityFormApplet().getSingleFormAppletDef(), FormMode.MAINTAIN);
            } else if (result.isOpenPath()) {
                setCommandOpenPath((String) result.getResult());
            } else if (result.isRefreshContent()) {
                setCommandResultMapping(ApplicationResultMappingConstants.REFRESH_CONTENT);
            }
        }
    }

    private void handleEntityActionResult(EntityActionResult entityActionResult, String entityName)
            throws UnifyException {
        if (entityActionResult.isRefreshMenu()) {
            refreshApplicationMenu();
        }

        if (entityActionResult.isWithResultPath()) {
            commandPost(entityActionResult.getResultPath());
        } else if (entityActionResult.isWithTaskResult()) {
            fireEntityActionResultTask(entityActionResult);
        } else if (entityActionResult.isEntitySelect()) {
            EntitySelect entitySelect = createEntitySelect(entityActionResult);
            commandShowPopup(new Popup(ApplicationResultMappingConstants.SHOW_ENTITY_SELECT, entitySelect));
        } else if (entityActionResult.isCloseView()) {
            getEntityFormApplet().navBackToPrevious();
        } else if (entityActionResult.isClosePage()) {
            setCloseResultMapping();
        }

        String successHint = entityActionResult.getSuccessHint();
        if (!StringUtils.isBlank(successHint)) {
            formHintSuccess(successHint, entityName);
        } else {
            formHintFailure(entityActionResult.getFailureHint(), entityName);
        }
    }

    protected AbstractEntitySingleFormApplet getEntityFormApplet() throws UnifyException {
        return getValue(AbstractEntitySingleFormApplet.class);
    }

    protected String getFormTitle() throws UnifyException {
        return getEntityFormApplet().getFormTitle();
    }

    protected String getBeanTitle() throws UnifyException {
        return getEntityFormApplet().getBeanTitle();
    }

    protected FormContext evaluateCurrentFormContext(FormValidationContext vCtx) throws UnifyException {
        return evaluateCurrentFormContext(vCtx, false);
    }

    protected FormContext evaluateCurrentFormContext(FormValidationContext vCtx, boolean commentRequired)
            throws UnifyException {
        FormContext ctx = getEntityFormApplet().getForm().getCtx();
        ctx.clearReviewErrors();
        ctx.clearValidationErrors();
        if (vCtx.isEvaluation()) {
            FormPanel formPanel = getWidgetByShortName(FormPanel.class, "formPanel");
            ctx.mergeValidationErrors(formPanel.validate(vCtx));

            if (vCtx.isReview() && ctx.getAppletContext().isReview()) {
                if (commentRequired) {
                    FormPanel commentsFormPanel = getWidgetByShortName(FormPanel.class, "formPanel.commentsPanel");
                    ctx.mergeValidationErrors(commentsFormPanel.validate(vCtx));
                }

                if (ctx.getAppletContext().isEmails()) {
                    FormPanel emailsFormPanel = getWidgetByShortName(FormPanel.class, "formPanel.emailsPanel");
                    ctx.mergeValidationErrors(emailsFormPanel.validate(vCtx));
                }
            }

            if (ctx.isWithFormErrors()) {
                hintUser(MODE.ERROR, "$m{entityformapplet.formvalidation.error.hint}");
            }
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

    private void formHintFailure(String messageKey, String entityName) throws UnifyException {
        if (!StringUtils.isBlank(messageKey)) {
            if (!StringUtils.isBlank(entityName)) {
                hintUser(MODE.ERROR, messageKey, StringUtils.capitalizeFirstLetter(entityName));
            } else {
                hintUser(MODE.ERROR, messageKey);
            }
        }
    }

}

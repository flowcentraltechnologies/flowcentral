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

package com.flowcentraltech.flowcentral.application.web.panels.applet;

import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySingleForm;
import com.flowcentraltech.flowcentral.application.web.panels.FormPanel;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.constants.WfItemVersionType;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.data.IndexedTarget;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ResultMappingConstants;
import com.tcdng.unify.web.ui.widget.Panel;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;

/**
 * Convenient abstract base panel for entity single form applet panels.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@UplBinding("web/application/upl/entitysingleformappletpanel.upl")
public abstract class AbstractEntitySingleFormAppletPanel extends AbstractAppletPanel {

    @Configurable
    protected ApplicationPrivilegeManager applicationPrivilegeManager;

    @Configurable
    private SystemModuleService systemModuleService;

    public final void setApplicationPrivilegeManager(ApplicationPrivilegeManager applicationPrivilegeManager) {
        this.applicationPrivilegeManager = applicationPrivilegeManager;
    }

    public final void setSystemModuleService(SystemModuleService systemModuleService) {
        this.systemModuleService = systemModuleService;
    }

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        final AbstractEntitySingleFormApplet applet = getEntityFormApplet();
        applet.ensureRootAppletStruct();

        final AppletDef _appletDef = applet.getRootAppletDef();
        final AppletContext appCtx = applet.getCtx();
        final AbstractEntitySingleFormApplet.ViewMode viewMode = applet.getMode();
        final EntityDef _entityDef = applet.getEntityDef();
        final String roleCode = getUserToken().getRoleCode();
        final EntitySingleForm form = applet.getForm();
        final Entity inst = form != null ? (Entity) form.getFormBean() : null;
        final boolean isInWorkflow = inst instanceof WorkEntity && ((WorkEntity) inst).isInWorkflow();
        final boolean isUpdateCopy = inst instanceof WorkEntity
                && WfItemVersionType.DRAFT.equals(((WorkEntity) inst).getWfItemVersionType());
        appCtx.setInWorkflow(isInWorkflow);

        final boolean isContextEditable = appCtx.isContextEditable();
        boolean enableUpdate = false;
        boolean enableDelete = false;
        boolean enableCreate = false;
        boolean enableCreateSubmit = false;
        boolean enableUpdateSubmit = false;
        boolean capture = false;
        if (viewMode.isCreateForm()) {
            enableCreate = isContextEditable
                    && applicationPrivilegeManager.isRoleWithPrivilege(roleCode, _entityDef.getAddPrivilege());
            enableCreateSubmit = applet
                    .formBeanMatchAppletPropertyCondition(AppletPropertyConstants.CREATE_FORM_SUBMIT_CONDITION);
        } else if (viewMode.isMaintainForm()) {
            capture = _appletDef.getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_CAPTURE, false);
            enableUpdate = isContextEditable && !isUpdateCopy
                    && _appletDef.getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_UPDATE, false)
                    && applicationPrivilegeManager.isRoleWithPrivilege(roleCode, _entityDef.getEditPrivilege())
                    && applet.formBeanMatchAppletPropertyCondition(
                            AppletPropertyConstants.MAINTAIN_FORM_UPDATE_CONDITION);
            enableDelete = !isInWorkflow && isContextEditable && !isUpdateCopy
                    && _appletDef.getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_DELETE, false)
                    && applicationPrivilegeManager.isRoleWithPrivilege(roleCode, _entityDef.getDeletePrivilege())
                    && applet.formBeanMatchAppletPropertyCondition(
                            AppletPropertyConstants.MAINTAIN_FORM_DELETE_CONDITION);
            enableUpdateSubmit = !isInWorkflow && applet
                    .formBeanMatchAppletPropertyCondition(AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_CONDITION);
        }

        appCtx.setCapture(capture);
        if (viewMode.isInForm()) {
            boolean showAlternateFormActions = systemModuleService.getSysParameterValue(boolean.class,
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

        if (form != null) {
            form.getCtx().setUpdateEnabled(enableUpdate);
            form.clearDisplayItem();
            if (isUpdateCopy) {
                form.setDisplayItemCounterClass("fc-dispcounterorange");
                form.setDisplayItemCounter(
                        resolveSessionMessage("$m{entityformapplet.form.workflowupdatecopy.viewonly}"));
            } else if (appCtx.isInWorkflow() && !appCtx.isReview()) {
                form.setDisplayItemCounterClass("fc-dispcounterorange");
                form.setDisplayItemCounter(resolveSessionMessage("$m{entityformapplet.form.inworkflow.viewonly}"));
            }
        }

        switch (viewMode) {
            case MAINTAIN_FORM_SCROLL:
                switchContent("formPanel");
                setVisible("cancelBtn", true);
                setVisible("saveBtn", false);
                setVisible("saveNextBtn", false);
                setVisible("saveCloseBtn", false);
                setVisible("submitCloseBtn", enableUpdateSubmit
                        && _appletDef.getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_SUBMIT, false));
                setVisible("submitNextBtn", enableUpdateSubmit && _appletDef.getPropValue(boolean.class,
                        AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_NEXT, false));
                setVisible("prevBtn", true);
                setVisible("nextBtn", true);
                setDisabled("prevBtn", !applet.isPrevNav());
                setDisabled("nextBtn", !applet.isNextNav());
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
                setVisible("submitCloseBtn", false);
                setVisible("submitNextBtn", false);
                setVisible("prevBtn", false);
                setVisible("nextBtn", false);
                setVisible("displayCounterLabel", false);
                setVisible("updateBtn", enableUpdate);
                setVisible("updateCloseBtn", enableUpdate);
                setVisible("deleteBtn", enableDelete);
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
        getEntityFormApplet().newEntityInst();
    }

    @Action
    public void navBackToPrevious() throws UnifyException {
        AbstractEntitySingleFormApplet applet = getEntityFormApplet();
        if (applet.getMode().isPrimary()) {
            setCommandResultMapping(ResultMappingConstants.CLOSE);
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
//        String actionName = getRequestTarget(String.class);
//        AbstractEntitySingleFormApplet applet = getEntityFormApplet();
//        FormActionDef formActionDef = applet.getCurrentFormDef().getFormActionDef(actionName);
//        FormContext ctx = evaluateCurrentFormContext(EvaluationMode.getRequiredMode(formActionDef.isValidateForm()));
//        if (!ctx.isWithFormErrors()) {
//            EntityActionResult entityActionResult = applet.formActionOnInst(formActionDef.getPolicy());
//            handleEntityActionResult(entityActionResult);
//        }
    }

    @Action
    public void save() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(EvaluationMode.CREATE);
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().saveNewInst();
            entityActionResult.setSuccessHint("$m{entityformapplet.new.success.hint}");
            handleEntityActionResult(entityActionResult, ctx.getEntityName());
        }
    }

    @Action
    public void saveAndNext() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(EvaluationMode.CREATE);
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().saveNewInstAndNext();
            entityActionResult.setSuccessHint("$m{entityformapplet.new.success.hint}");
            handleEntityActionResult(entityActionResult, ctx.getEntityName());
        }
    }

    @Action
    public void saveAndClose() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(EvaluationMode.CREATE);
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
        FormContext ctx = evaluateCurrentFormContext(evalMode);
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
        FormContext ctx = evaluateCurrentFormContext(evalMode);
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().submitInstAndNext();
            entityActionResult.setSuccessHint("$m{entityformapplet.submit.success.hint}");
            handleEntityActionResult(entityActionResult, ctx.getEntityName());
        }
    }

    @Action
    public void update() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(EvaluationMode.UPDATE);
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().updateInst();
            entityActionResult.setSuccessHint("$m{entityformapplet.update.success.hint}");
            handleEntityActionResult(entityActionResult, ctx.getEntityName());
        }
    }

    @Action
    public void updateAndClose() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(EvaluationMode.UPDATE);
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().updateInstAndClose();
            entityActionResult.setSuccessHint("$m{entityformapplet.update.success.hint}");
            handleEntityActionResult(entityActionResult, ctx.getEntityName());
        }
    }

    @Action
    public void delete() throws UnifyException {
        FormContext ctx = evaluateCurrentFormContext(EvaluationMode.DELETE);
        if (!ctx.isWithFormErrors()) {
            EntityActionResult entityActionResult = getEntityFormApplet().deleteInst();
            entityActionResult.setSuccessHint("$m{entityformapplet.delete.success.hint}");
            handleEntityActionResult(entityActionResult, ctx.getEntityName());
        }
    }

    @Action
    public void previous() throws UnifyException {
        getEntityFormApplet().previousInst();
    }

    @Action
    public void next() throws UnifyException {
        getEntityFormApplet().nextInst();
    }

    @Action
    public void maintain() throws UnifyException {
        IndexedTarget target = getRequestTarget(IndexedTarget.class);
        if (target.isValidIndex()) {
            getEntityFormApplet().maintainInst(target.getIndex());
        }
    }
    
    @Action
    public void columns() throws UnifyException {
        IndexedTarget target = getRequestTarget(IndexedTarget.class);
        // TODO
    }

    @Override
    protected void onReviewErrors(EntityActionResult entityActionResult) throws UnifyException {

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
        } else if (entityActionResult.isCloseView()) {
            getEntityFormApplet().navBackToPrevious();
        } else if (entityActionResult.isClosePage()) {
            setCommandResultMapping(ResultMappingConstants.CLOSE);
        }

        String successHint = entityActionResult.getSuccessHint();
        if (!StringUtils.isBlank(successHint)) {
            formHintSuccess(successHint, entityName);
        }
    }

    protected AbstractEntitySingleFormApplet getEntityFormApplet() throws UnifyException {
        return getValue(AbstractEntitySingleFormApplet.class);
    }

    protected FormContext evaluateCurrentFormContext(EvaluationMode evaluationMode) throws UnifyException {
        return evaluateCurrentFormContext(evaluationMode, false);
    }

    protected FormContext evaluateCurrentFormContext(EvaluationMode evaluationMode, boolean commentRequired)
            throws UnifyException {
        FormContext ctx = getEntityFormApplet().getForm().getCtx();
        ctx.clearReviewErrors();
        ctx.clearValidationErrors();
        if (evaluationMode.evaluation()) {
            FormPanel formPanel = getWidgetByShortName(FormPanel.class, "formPanel");
            ctx.mergeValidationErrors(formPanel.validate(evaluationMode));

            if (evaluationMode.review() && ctx.getAppletContext().isReview()) {
                if (commentRequired) {
                    FormPanel commentsFormPanel = getWidgetByShortName(FormPanel.class, "formPanel.commentsPanel");
                    ctx.mergeValidationErrors(commentsFormPanel.validate(evaluationMode));
                }

                if (ctx.getAppletContext().isEmails()) {
                    FormPanel emailsFormPanel = getWidgetByShortName(FormPanel.class, "formPanel.emailsPanel");
                    ctx.mergeValidationErrors(emailsFormPanel.validate(evaluationMode));
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

}

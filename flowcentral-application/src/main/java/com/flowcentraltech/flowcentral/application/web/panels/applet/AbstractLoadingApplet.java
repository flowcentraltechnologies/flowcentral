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
package com.flowcentraltech.flowcentral.application.web.panels.applet;

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.data.EntityItem;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.LoadingWorkItemInfo;
import com.flowcentraltech.flowcentral.application.data.WorkflowStepInfo;
import com.flowcentraltech.flowcentral.application.policies.LoadingTableProvider;
import com.flowcentraltech.flowcentral.application.util.AppletNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySingleForm;
import com.flowcentraltech.flowcentral.application.web.panels.HeaderWithTabsForm;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.constants.SecuredLinkType;
import com.flowcentraltech.flowcentral.configuration.constants.AuditEventType;
import com.flowcentraltech.flowcentral.configuration.constants.RecordActionType;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.common.database.WorkEntity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Abstract base class for loading applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractLoadingApplet extends AbstractEntityFormApplet {

    private final LoadingTableProvider loadingTableProvider;

    private Long workItemId;

    private EntitySingleForm singleForm;

    public AbstractLoadingApplet(Page page, AppletUtilities au, List<String> pathVariables,
            AppletWidgetReferences appletWidgetReferences, EntityFormEventHandlers formEventHandlers,
            String providerName) throws UnifyException {
        super(page, au, pathVariables, appletWidgetReferences, formEventHandlers);

        setCurrFormAppletDef(getRootAppletDef());
        final AppletNameParts parts = ApplicationNameUtils.getAppletNameParts(pathVariables.get(APPLET_NAME_INDEX));
        loadingTableProvider = au.getComponent(LoadingTableProvider.class, providerName);
        if (parts.isWithVestigial()) {
            final Long workItemEventId = Long.valueOf(parts.getVestigial());
            final WorkflowStepInfo workflowStepInfo = getWorkflowStepInfo(au, workItemEventId);
            if (workflowStepInfo.isWithWorkItemId()) {
                workItemId = workflowStepInfo.getWorkItemId();
                loadingTableProvider.setWorkingParameter(workflowStepInfo);
                final int options = loadingTableProvider.getSourceItemOptions(null);
                final EntityItem item = loadingTableProvider.getSourceItemByWorkItemId(workItemId, options);
                if (item.isEdit()) {
                    appletCtx().setReview(false);
                    Entity _inst = item.getEntity();
                    _inst = reloadEntity(_inst, true);
                    if (form == null) {
                        form = constructForm(_inst, FormMode.MAINTAIN, null, false);
                    } else {
                        updateForm(HeaderWithTabsForm.UpdateType.MAINTAIN_INST, form, _inst);
                    }

                    setAltSubCaption(form.getFormTitle());
                    viewMode = ViewMode.MAINTAIN_PRIMARY_FORM_NO_SCROLL;
                    takeAuditSnapshot(AuditEventType.VIEW);
                } else if (item.isWorkItem()) {
                    final AppletDef _currentFormAppletDef = getFormAppletDef();
                    WorkEntity currEntityInst = (WorkEntity) item.getEntity();
                    FormDef formDef = getPreferredForm(PreferredFormType.ALL, _currentFormAppletDef, currEntityInst,
                            FormMode.MAINTAIN.formProperty());
                    LoadingWorkItemInfo loadingWorkItemInfo = loadingTableProvider
                            .getLoadingWorkItemInfo(currEntityInst);
                    final String display = loadingWorkItemInfo.isWithStepLabel()
                            ? au.resolveSessionMessage("$m{loading.workitem.display}", loadingWorkItemInfo.getStepLabel())
                            : null;
                    appletCtx().setRecovery(loadingWorkItemInfo.isError());
                    appletCtx().setComments(loadingWorkItemInfo.isComments());
                    appletCtx().setAttachments(loadingWorkItemInfo.isAttachments());
                    appletCtx().setReview(true);
                    if (formDef.isInputForm()) {
                        if (form == null) {
                            form = constructForm(formDef, currEntityInst, FormMode.MAINTAIN, null, false);
                            currEntityInst = (WorkEntity) form.getFormBean();
                            form.setFormTitle(getRootAppletDef().getLabel());
                            form.setFormActionDefList(loadingWorkItemInfo.getFormActionDefList());
                        } else {
                            updateForm(HeaderWithTabsForm.UpdateType.MAINTAIN_INST, form, currEntityInst);
                        }

                        form.setDisplayItemCounter(display);
                        form.setAppendables(item);

                        appletCtx().setReadOnly(loadingWorkItemInfo.isReadOnly());
                        setAltSubCaption(form.getFormTitle());
                        viewMode = ViewMode.MAINTAIN_PRIMARY_FORM_NO_SCROLL;
                        takeAuditSnapshot(AuditEventType.VIEW);
                    } else { // Listing
                        listingForm = constructListingForm(formDef, currEntityInst);
                        listingForm.setFormTitle(getRootAppletDef().getLabel());
                        listingForm.setFormActionDefList(loadingWorkItemInfo.getFormActionDefList());

                        listingForm.setDisplayItemCounter(display);
                        listingForm.setAppendables(item);
                        appletCtx().setEmails(loadingWorkItemInfo.isEmails());
                        appletCtx().setReadOnly(loadingWorkItemInfo.isError());
                        setAltSubCaption(listingForm.getFormTitle());
                        viewMode = ViewMode.LISTING_FORM;
                    }
                } else if (item.isWorkItemSingleForm()) {
                    WorkEntity currEntityInst = (WorkEntity) item.getEntity();
                    LoadingWorkItemInfo loadingWorkItemInfo = loadingTableProvider
                            .getLoadingWorkItemInfo(currEntityInst);
                    appletCtx().setRecovery(loadingWorkItemInfo.isError());
                    appletCtx().setEmails(loadingWorkItemInfo.isEmails());
                    appletCtx().setComments(loadingWorkItemInfo.isComments());
                    appletCtx().setAttachments(loadingWorkItemInfo.isAttachments());
                    appletCtx().setReview(true);
                    if (singleForm == null) {
                        singleForm = constructSingleForm(currEntityInst, FormMode.MAINTAIN);
                        singleForm.setFormTitle(getRootAppletDef().getLabel());
                        singleForm.setFormActionDefList(loadingWorkItemInfo.getFormActionDefList());
                    } else {
                        updateSingleForm(EntitySingleForm.UpdateType.MAINTAIN_INST, singleForm, currEntityInst);
                    }

                    singleForm.setAppendables(item);
                    appletCtx().setReadOnly(loadingWorkItemInfo.isReadOnly());
                    setAltSubCaption(singleForm.getFormTitle());
                    viewMode = ViewMode.SINGLE_FORM;
                }
            }
        } else {
            form = constructNewForm(FormMode.CREATE, null, false);
            viewMode = ViewMode.NEW_PRIMARY_FORM;
        }

        if (form != null) {
            setAltSubCaption(form.getFormTitle());
        }
    }

    @Override
    public AbstractForm getResolvedForm() {
        return singleForm != null ? singleForm : super.getResolvedForm();
    }

    @Override
    public AbstractForm getForm() {
        return getResolvedForm();
    }

    public Long getWorkItemId() {
        return workItemId;
    }

    public boolean isWithWorkItemId() {
        return workItemId != null;
    }

    public EntitySingleForm getSingleForm() {
        return singleForm;
    }

    public EntityActionResult updateSingleFormInst() throws UnifyException {
        singleForm.unloadSingleFormBean();
        Entity inst = (Entity) singleForm.getFormBean();
        String updatePolicy = getRootAppletProp(String.class, AppletPropertyConstants.MAINTAIN_FORM_UPDATE_POLICY);
        EntityActionContext eCtx = new EntityActionContext(getEntityDef(), inst, RecordActionType.UPDATE, null,
                updatePolicy);
        eCtx.setAll(singleForm.getCtx());

        EntityActionResult entityActionResult = au().environment().updateByIdVersion(eCtx);
        updateSingleForm(EntitySingleForm.UpdateType.UPDATE_INST, singleForm, reloadSingleFormEntity(inst));
        return entityActionResult;
    }

    public LoadingWorkItemInfo getCurrentLoadingWorkItemInfo() throws UnifyException {
        final AbstractForm _form = getResolvedForm();
        WorkEntity currEntityInst = (WorkEntity) _form.getFormBean();
        return loadingTableProvider.getLoadingWorkItemInfo(currEntityInst);
    }

    public boolean isNewCommentRequired(String actionName) throws UnifyException {
        return loadingTableProvider.isNewCommentRequired(actionName);
    }

    @Override
    public void applyUserAction(String actionName) throws UnifyException {
        final AbstractForm _form = getResolvedForm();
        WorkEntity currEntityInst = (WorkEntity) _form.getFormBean();
        loadingTableProvider.applyUserActionByWorkItemId(currEntityInst, workItemId, actionName, _form.getNewComment(),
                _form.getEmails(), _form.isListing());
        invalidateSecuredLink(SecuredLinkType.WORKFLOW_DECISION);
    }

    protected abstract WorkflowStepInfo getWorkflowStepInfo(AppletUtilities au, Long sourceItemId)
            throws UnifyException;

    protected final AppletDef resolveRootAppletDef(String appletName) throws UnifyException {
        AppletDef appletDef = au().getAppletDef(appletName);
        if (appletDef.getType().isEntityList()) {
            appletDef = appletDef.getMaintainAppletDef();
            setAltCaption(appletDef.getPropValue(String.class, AppletPropertyConstants.PAGE_MAINTAIN_CAPTION));
        }

        return appletDef;
    }

    @Override
    protected AppletDef getAlternateFormAppletDef() throws UnifyException {
        String formAppletName = loadingTableProvider.getSourceItemFormApplet();
        return !StringUtils.isBlank(formAppletName) ? au().getAppletDef(formAppletName) : null;
    }

    private Entity reloadSingleFormEntity(Entity _inst) throws UnifyException {
        // For single form we list with child/ children information
        return au().environment().list((Class<? extends Entity>) _inst.getClass(), _inst.getId());
    }
}

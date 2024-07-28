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

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.AppletFilterDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.data.EntityItem;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.LoadingWorkItemInfo;
import com.flowcentraltech.flowcentral.application.listing.FormListingGenerator;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySingleForm;
import com.flowcentraltech.flowcentral.application.web.panels.HeaderWithTabsForm;
import com.flowcentraltech.flowcentral.application.web.panels.LoadingSearch;
import com.flowcentraltech.flowcentral.application.web.widgets.AbstractTable;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.TableActionResult;
import com.flowcentraltech.flowcentral.common.data.AuditSnapshot;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.flowcentraltech.flowcentral.configuration.constants.AuditEventType;
import com.flowcentraltech.flowcentral.configuration.constants.AuditSourceType;
import com.flowcentraltech.flowcentral.configuration.constants.RecordActionType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.report.Report;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Manage loading list applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ManageLoadingListApplet extends AbstractEntityFormApplet {

    private LoadingSearch loadingSearch;

    private EntitySingleForm singleForm;

    public ManageLoadingListApplet(Page page, AppletUtilities au, List<String> pathVariables,
            AppletWidgetReferences appletWidgetReferences, EntityFormEventHandlers formEventHandlers)
            throws UnifyException {
        super(page, au, pathVariables, appletWidgetReferences, formEventHandlers);
        loadingSearch = au.constructLoadingSearch(ctx, LoadingSearch.ENABLE_ALL);
        if (isRootAppletPropWithValue(AppletPropertyConstants.BASE_RESTRICTION)) {
            AppletFilterDef appletFilterDef = getRootAppletFilterDef(AppletPropertyConstants.BASE_RESTRICTION);
            loadingSearch.setBaseFilter(new FilterDef(appletFilterDef.getFilterDef()), au.specialParamProvider());
        }

        loadingSearch.applySearchEntriesToSearch();
        loadingSearch.getLoadingTable().setCrudActionHandlers(formEventHandlers.getMaintainActHandlers());
        navBackToSearch();
    }

    public LoadingSearch getLoadingSearch() {
        return loadingSearch;
    }

    @Override
    public AbstractForm getResolvedForm() {
        return singleForm != null ? singleForm : super.getResolvedForm();
    }

    @Override
    public AbstractForm getForm() {
        return getResolvedForm();
    }

    public EntitySingleForm getSingleForm() {
        return singleForm;
    }

    @Override
    public boolean navBackToSearch() throws UnifyException {
        setAltSubCaption(loadingSearch.getEntityDef().getDescription());
        if (!super.navBackToSearch()) {
            if (loadingSearch != null) {
                loadingSearch.clearSearchEntries(); // TODO Configurable? System parameter?
                loadingSearch.applySearchEntriesToSearch();
                singleForm = null;
                return true;
            }
        }

        singleForm = null;
        return false;
    }

    @Override
    public boolean navBackToPrevious() throws UnifyException {
        setAltSubCaption(loadingSearch.getEntityDef().getDescription());
        return super.navBackToPrevious();
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

    private Entity reloadSingleFormEntity(Entity _inst) throws UnifyException {
        // For single form we list with child/ children information
        return au().environment().list((Class<? extends Entity>) _inst.getClass(), _inst.getId());
    }

    @Override
    public TableActionResult maintainInst(int mIndex) throws UnifyException {
        this.mIndex = mIndex;
        EntityItem item = loadingSearch.getSourceItem(mIndex);
        if (item.isEdit()) {
            getCtx().setReview(false);
            Entity _inst = item.getEntity();
            _inst = reloadEntity(_inst, true);
            if (form == null) {
                form = constructForm(_inst, FormMode.MAINTAIN, null, false);
            } else {
                updateForm(HeaderWithTabsForm.UpdateType.MAINTAIN_INST, form, _inst);
            }

            setAltSubCaption(form.getFormTitle());
            viewMode = ViewMode.MAINTAIN_FORM;
            takeAuditSnapshot(AuditEventType.VIEW);
        } else if (item.isReport()) {
            if (item.isWithListingParams()) {
                FormListingGenerator generator = au().getComponent(FormListingGenerator.class,
                        item.getListingGenerator());
                Report report = generator.generateHtmlReport(new BeanValueStore(item.getEntity()).getReader(),
                        item.getListingOptions());
                TableActionResult result = new TableActionResult(item.getEntity(), report);
                result.setDisplayListingReport(true);
                return result;
            }
        } else if (item.isWorkItem()) {
            final AppletDef _currentFormAppletDef = getFormAppletDef();
            WorkEntity currEntityInst = (WorkEntity) item.getEntity();
            FormDef formDef = getPreferredForm(PreferredFormType.ALL, _currentFormAppletDef, currEntityInst,
                    FormMode.MAINTAIN.formProperty());
            LoadingWorkItemInfo loadingWorkItemInfo = loadingSearch.getLoadingWorkItemInfo(currEntityInst, mIndex);
            final String display = loadingWorkItemInfo.isWithStepLabel()
                    ? au.resolveSessionMessage("$m{loading.workitem.display}", loadingWorkItemInfo.getWorkflowDesc(),
                            loadingWorkItemInfo.getStepLabel())
                    : null;
            getCtx().setRecovery(loadingWorkItemInfo.isError());
            getCtx().setComments(loadingWorkItemInfo.isComments());
            getCtx().setAttachments(loadingWorkItemInfo.isAttachments());
            getCtx().setReview(true);
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

                getCtx().setReadOnly(loadingWorkItemInfo.isReadOnly());
                setAltSubCaption(form.getFormTitle());
                viewMode = ViewMode.MAINTAIN_FORM;
                takeAuditSnapshot(AuditEventType.VIEW);
            } else { // Listing
                listingForm = constructListingForm(formDef, currEntityInst);
                listingForm.setFormTitle(getRootAppletDef().getLabel());
                listingForm.setFormActionDefList(loadingWorkItemInfo.getFormActionDefList());

                listingForm.setDisplayItemCounter(display);
                listingForm.setAppendables(item);

                getCtx().setEmails(loadingWorkItemInfo.isEmails());
                getCtx().setReadOnly(loadingWorkItemInfo.isError());
                setAltSubCaption(listingForm.getFormTitle());
                viewMode = ViewMode.LISTING_FORM;
            }
        } else if (item.isWorkItemSingleForm()) {
            WorkEntity currEntityInst = (WorkEntity) item.getEntity();
            LoadingWorkItemInfo loadingWorkItemInfo = loadingSearch.getLoadingWorkItemInfo(currEntityInst, mIndex);
            final String display = loadingWorkItemInfo.isWithStepLabel()
                    ? au.resolveSessionMessage("$m{loading.workitem.display}", loadingWorkItemInfo.getWorkflowDesc(),
                            loadingWorkItemInfo.getStepLabel())
                    : null;

            getCtx().setRecovery(loadingWorkItemInfo.isError());
            getCtx().setEmails(loadingWorkItemInfo.isEmails());
            getCtx().setComments(loadingWorkItemInfo.isComments());
            getCtx().setAttachments(loadingWorkItemInfo.isAttachments());
            getCtx().setReview(true);
            if (singleForm == null) {
                singleForm = constructSingleForm(currEntityInst, FormMode.MAINTAIN);
                singleForm.setFormTitle(getRootAppletDef().getLabel());
                singleForm.setFormActionDefList(loadingWorkItemInfo.getFormActionDefList());
            } else {
                updateSingleForm(EntitySingleForm.UpdateType.MAINTAIN_INST, singleForm, currEntityInst);
            }

            singleForm.setDisplayItemCounter(display);
            singleForm.setAppendables(item);

            getCtx().setReadOnly(loadingWorkItemInfo.isReadOnly());
            setAltSubCaption(singleForm.getFormTitle());
            viewMode = ViewMode.SINGLE_FORM;
            takeSingleFormAuditSnapshot(AuditEventType.VIEW);
        }

        TableActionResult result = new TableActionResult(item.getEntity());
        result.setRefreshContent(true);
        return result;
    }

    @Override
    public void applyUserAction(String actionName) throws UnifyException {
        final AbstractForm _form = getResolvedForm();
        WorkEntity currEntityInst = (WorkEntity) _form.getFormBean();
        loadingSearch.applyUserAction(currEntityInst, actionName, _form.getNewComment(), _form.getEmails(), mIndex,
                _form.isListing());
        navBackToSearch();
    }

    public LoadingWorkItemInfo getCurrentLoadingWorkItemInfo() throws UnifyException {
        final AbstractForm _form = getResolvedForm();
        WorkEntity currEntityInst = (WorkEntity) _form.getFormBean();
        return loadingSearch.getLoadingWorkItemInfo(currEntityInst, mIndex);
    }

    public boolean isNewCommentRequired(String actionName) throws UnifyException {
        return loadingSearch.isNewCommentRequired(actionName, mIndex);
    }

    @Override
    public AppletDef getSingleFormAppletDef() throws UnifyException {
        return getAlternateFormAppletDef();
    }

    @Override
    protected AbstractTable<?, ? extends Entity> getSearchTable() throws UnifyException {
        return loadingSearch.getLoadingTable();
    }

    @Override
    protected AppletDef getAlternateFormAppletDef() throws UnifyException {
        String formAppletName = loadingSearch.getSourceItemFormApplet(mIndex);
        return !StringUtils.isBlank(formAppletName) ? au.getAppletDef(formAppletName) : null;
    }

    public final EntityDef getEntityDef() throws UnifyException {
        if (loadingSearch != null) {
            // TODO
//            return entitySearch.getEntityTable().getEntityDef();
        }

        return au().getEntityDef(getRootAppletDef().getEntity());
    }

    protected void takeSingleFormAuditSnapshot(AuditEventType auditEventType) throws UnifyException {
        if (isAuditingEnabled()) {
            AuditSnapshot.Builder asb = AuditSnapshot.newBuilder(AuditSourceType.APPLET, auditEventType, au.getNow(),
                    getAppletName());
            UserToken userToken = au.getSessionUserToken();
            asb.userLoginId(userToken.getUserLoginId());
            asb.userName(userToken.getUserName());
            asb.userIpAddress(userToken.getIpAddress());
            asb.roleCode(userToken.getRoleCode());

            if (formStack != null && !formStack.isEmpty()) {
                final AuditEventType parentType = isParentStateAuditingEnabled() ? AuditEventType.VIEW
                        : AuditEventType.VIEW_PHANTOM;
                final int len = formStack.size();
                for (int i = 0; i < len; i++) {
                    FormContext fCtx = formStack.get(i).getForm().getCtx();
                    if (fCtx.isSupportAudit()) {
                        asb.addSnapshot(fCtx.getEntityAudit(), parentType);
                    }
                }
            }

            if (viewMode.isInForm()) {
                FormContext fCtx = singleForm.getCtx();
                if (fCtx.isSupportAudit()) {
                    asb.addSnapshot(fCtx.getEntityAudit(), auditEventType);
                }
            } else if (viewMode.isInAssignment()) {
                if (assignmentPage.isSupportAudit()) {
                    asb.addSnapshot(assignmentPage.getEntityAssignmentAudit(), auditEventType);
                }
            }

            AuditSnapshot auditSnapshot = asb.build();
            au.audit().log(auditSnapshot);
        }
    }

}

/*

    public boolean isChangeOnlyAuditingEnabled() {
        return changeOnlyAuditingEnabled;
    }
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

import java.util.Date;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.AppletFilterDef;
import com.flowcentraltech.flowcentral.application.data.AssignmentPageDef;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.PropertyRuleDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.data.WorkflowDraftInfo;
import com.flowcentraltech.flowcentral.application.policies.ListingRedirect;
import com.flowcentraltech.flowcentral.application.policies.ListingRedirectionPolicy;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationPageUtils;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySingleForm;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.common.business.policies.TableActionResult;
import com.flowcentraltech.flowcentral.configuration.constants.AppletType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.Page;

/**
 * Convenient abstract base class for applet objects.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractApplet {

    protected enum PreferredFormType {
        INPUT_ONLY,
        LISTING_ONLY,
        ALL;

        public boolean supports(FormDef formDef) {
            return (this.equals(INPUT_ONLY) && formDef.isInputForm())
                    || (this.equals(LISTING_ONLY) && formDef.isListingForm()) || this.equals(ALL);
        }
    }

    protected static final int APPLET_NAME_INDEX = 0;

    protected final AppletUtilities au;

    protected final AppletContext ctx;

    private final String appletName;

    private AppletDef rootAppletDef;

    private String altCaption;

    private String altSubCaption;

    private WorkflowDraftInfo workflowDraftInfo;

    public AbstractApplet(Page page, AppletUtilities au, String appletName) throws UnifyException {
        this.appletName = ApplicationNameUtils.removeVestigialNamePart(appletName);
        this.au = au;
        this.ctx = new AppletContext(page, this, au);
    }

    public String getAppletName() {
        return appletName;
    }

    public final AppletContext getCtx() {
        return ctx;
    }

    public String getAppletLabel() throws UnifyException {
        return getRootAppletDef().getLabel();
    }

    public String getAppletDescription() throws UnifyException {
        return getRootAppletDef().getDescription();
    }

    public String getPageAltCaption() throws UnifyException {
        return !StringUtils.isBlank(altCaption) ? altCaption
                : au.resolveSessionMessage(
                        getRootAppletDef().getPropValue(String.class, AppletPropertyConstants.PAGE_ALTERNATE_CAPTION));
    }

    public String getPageAltSubCaption() throws UnifyException {
        return !StringUtils.isBlank(altSubCaption) ? altSubCaption
                : au.resolveSessionMessage(getRootAppletDef().getPropValue(String.class,
                        AppletPropertyConstants.PAGE_ALTERNATE_SUBCAPTION));
    }

    public AppletDef getRootAppletDef() throws UnifyException {
        if (rootAppletDef == null) {
            rootAppletDef = resolveRootAppletDef(appletName);
        }

        return rootAppletDef;
    }

    public boolean isAuditingEnabled() {
        return ctx.isAuditingEnabled();
    }

    public boolean isParentStateAuditingEnabled() {
        return ctx.isParentStateAuditingEnabled();
    }

    public boolean isWorkflowCopy() throws UnifyException {
        return getRootAppletDef().getPropValue(boolean.class, AppletPropertyConstants.WORKFLOWCOPY);
    }

    public boolean navBackToPrevious() throws UnifyException {
        return false;
    }

    public AppletDef getSingleFormAppletDef() throws UnifyException {
        return null;
    }

    public EntityDef getEntityDef() throws UnifyException {
        return au().getEntityDef(getRootAppletDef().getEntity());
    }

    public boolean isSaveHeaderFormOnTabAction() throws UnifyException {
        return au.getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.SAVE_HEADER_FORM_ON_TAB_ACTION);
    }

    public final AppletUtilities au() {
        return au;
    }

    public void ensureRootAppletStruct() throws UnifyException {
        if (rootAppletDef != null) {
            AppletDef _nAppletDef = resolveRootAppletDef(appletName);
            if (rootAppletDef.getVersion() != _nAppletDef.getVersion()) {
                rootAppletDef = _nAppletDef;
            }
        }
    }

    public boolean isContextEditable() {
        return ctx.isContextEditable();
    }

    public boolean isRootFormUpdateDraft() {
        return ctx.isRootFormUpdateDraft();
    }

    public WorkflowDraftInfo getWorkflowDraftInfo() {
        return workflowDraftInfo;
    }

    public WorkflowDraftInfo removeWorkflowDraftInfo() {
        final WorkflowDraftInfo _workflowDraftInfo = workflowDraftInfo;
        workflowDraftInfo = null;
        return _workflowDraftInfo;
    }

    public void setWorkflowDraftInfo(WorkflowDraftInfo workflowDraftInfo) {
        this.workflowDraftInfo = workflowDraftInfo;
    }

    public boolean isWithWorkflowDraftInfo() {
        return workflowDraftInfo != null;
    }

    protected void setAltCaption(String altCaption) {
        this.altCaption = altCaption;
    }

    protected void setAltSubCaption(String altSubCaption) {
        this.altSubCaption = altSubCaption;
    }

    protected AppletDef resolveRootAppletDef(String appletName) throws UnifyException {
        return au.getAppletDef(appletName);
    }

    protected void setFormProperties(AppletDef _appletDef, AbstractForm form) throws UnifyException {
        String submitCaption = _appletDef.getPropValue(String.class,
                AppletPropertyConstants.CREATE_FORM_SUBMIT_CAPTION);
        String submitNextCaption = _appletDef.getPropValue(String.class,
                AppletPropertyConstants.CREATE_FORM_SUBMIT_NEXT_CAPTION);
        boolean submitButtonHighlight = _appletDef.getPropValue(boolean.class,
                AppletPropertyConstants.CREATE_FORM_SUBMIT_BUTTON_HIGHLIGHT);
        form.setSubmitCaption(submitCaption);
        form.setSubmitNextCaption(submitNextCaption);
        if (submitButtonHighlight) {
            form.setSubmitStyleClass("fc-greenbutton");
        }
    }

    public FormDef getPreferredForm(PreferredFormType type, AppletDef appletDef, Entity inst, String formProperty)
            throws UnifyException {
        if (appletDef.isWithPreferredFormFilters()) {
            final Date now = au.getNow();
            final ValueStoreReader reader = new BeanValueStore(inst).getReader();
            EntityDef _entityDef = getEntityClassDef(appletDef.getEntity()).getEntityDef();
            for (AppletFilterDef filterDef : appletDef.getPreferredFormFilterList()) {
                if (filterDef.getFilterDef().getObjectFilter(_entityDef, reader, now).matchReader(reader)) {
                    String formName = filterDef.getPreferredForm();
                    FormDef formDef = au.getFormDef(formName);
                    if (type.supports(formDef)) {
                        return formDef;
                    }
                }
            }
        }

        String formName = appletDef.getPropValue(String.class, formProperty);
        return !StringUtils.isBlank(formName) ? au.getFormDef(formName) : null;
    }

    protected boolean isRootAppletProp(String propName) throws UnifyException {
        return getRootAppletDef().isProp(propName);
    }

    protected boolean isRootAppletPropWithValue(String propName) throws UnifyException {
        AppletDef rootAppletDef = getRootAppletDef();
        return rootAppletDef.isProp(propName)
                && !StringUtils.isBlank(rootAppletDef.getPropValue(String.class, propName));
    }

    protected <T> T getRootAppletProp(Class<T> dataClazz, String propName) throws UnifyException {
        return getRootAppletDef().getPropValue(dataClazz, propName);
    }

    protected <T> T getRootAppletProp(Class<T> dataClazz, String propName, T defVal) throws UnifyException {
        return getRootAppletDef().getPropValue(dataClazz, propName, defVal);
    }

    protected EntityClassDef getEntityClassDef(String entityName) throws UnifyException {
        return au.getEntityClassDef(entityName);
    }

    protected EntityClassDef getAppletEntityClassDef(String appletName) throws UnifyException {
        return au.getAppletEntityClassDef(appletName);
    }

    protected EntityDef getAppletEntityDef(String appletName) throws UnifyException {
        return au.getAppletEntityDef(appletName);
    }

    protected EntityDef getEntityDef(String entityName) throws UnifyException {
        return au.getEntityDef(entityName);
    }

    protected FormDef getFormDef(String formName) throws UnifyException {
        return au.getFormDef(formName);
    }

    protected TableDef getRootAppletTableDef(String tblPropName) throws UnifyException {
        return au.getTableDef(getRootAppletDef().getPropValue(String.class, tblPropName));
    }

    protected AppletFilterDef getRootAppletFilterDef(String frmPropName) throws UnifyException {
        return getRootAppletDef().getFilterDef(getRootAppletDef().getPropValue(String.class, frmPropName));
    }

    protected FormDef getRootAppletFormDef(String frmPropName) throws UnifyException {
        return getFormDef(getRootAppletDef(), frmPropName);
    }

    protected FormDef getFormDef(AppletDef appletDef, String frmPropName) throws UnifyException {
        String _formName = appletDef.getPropValue(String.class, frmPropName);
        if (_formName != null) {
            return au.getFormDef(_formName);
        }

        return null;
    }

    protected AssignmentPageDef getAssignmentPageDef(AppletDef appletDef, String frmPropName) throws UnifyException {
        return au.getAssignmentPageDef(appletDef.getPropValue(String.class, frmPropName));
    }

    protected PropertyRuleDef getPropertyRuleDef(AppletDef appletDef, String frmPropName) throws UnifyException {
        return au.getPropertyRuleDef(appletDef.getPropValue(String.class, frmPropName));
    }

    protected EntitySingleForm constructSingleForm(Entity inst, FormMode formMode) throws UnifyException {
        final EntityDef entityDef = au.getEntityDef(getRootAppletDef().getEntity());
        final String createNewCaption = getRootAppletProp(String.class,
                AppletPropertyConstants.CREATE_FORM_NEW_CAPTION);
        final String beanTitle = inst.getDescription() != null ? inst.getDescription()
                : !StringUtils.isBlank(createNewCaption) ? createNewCaption
                        : au.resolveSessionMessage("$m{form.newentity}", entityDef.getDescription());
        return constructSingleForm(inst, formMode, beanTitle);
    }

    protected EntitySingleForm constructSingleForm(Entity inst, FormMode formMode, String beanTitle)
            throws UnifyException {
        EntitySingleForm form = au.constructEntitySingleForm(this, getRootAppletDef().getDescription(), beanTitle, inst,
                formMode, makeSingleFormBreadCrumbs());
        if (formMode.isCreate()) {
            // TODO
        }

        form.loadSingleFormBean();
        setFormProperties(getRootAppletDef(), form);
        return form;
    }

    protected void updateSingleForm(EntitySingleForm.UpdateType updateType, EntitySingleForm form, Entity inst)
            throws UnifyException {
        form.getCtx().resetTabIndex();
        form.setUpdateType(updateType);
        au.updateEntitySingleForm(this, form, inst);
        form.loadSingleFormBean();
    }

    protected TableActionResult openInTab(AppletType appletType, Entity _inst) throws UnifyException {
        final String appletName = getAppletName();
        final String openPath = ApplicationPageUtils.constructAppletOpenPagePath(appletType, appletName, _inst.getId());
        TableActionResult result = new TableActionResult(_inst, openPath);
        result.setOpenPath(true);

        if (au().system().getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.ENABLE_OPEN_TAB_IN_BROWSER)) {
            final String tabName = ApplicationNameUtils.addVestigialNamePart(appletName, String.valueOf(_inst.getId()));
            result.setTabName(tabName);
            result.setOpenTab(true);
        }

        return result;
    }

    protected TableActionResult openInTab(AppletType appletType) throws UnifyException {
        final String appletName = getAppletName();
        final String openPath = ApplicationPageUtils.constructAppletOpenPagePath(appletType, appletName);
        TableActionResult result = new TableActionResult(null, openPath);
        result.setOpenPath(true);

        if (au().system().getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.ENABLE_OPEN_TAB_IN_BROWSER)) {
            result.setTabName(appletName + "_new");
            result.setOpenTab(true);
        }

        return result;
    }

    protected TableActionResult openListingInTab(Entity _inst) throws UnifyException {
        ListingRedirect listingRedirect = isRootAppletPropWithValue(AppletPropertyConstants.LISTING_REDIRECT_POLICY)
                ? au().getComponent(ListingRedirectionPolicy.class,
                        getRootAppletProp(String.class, AppletPropertyConstants.LISTING_REDIRECT_POLICY))
                        .evaluateRedirection(getAppletName(), (Long) _inst.getId())
                : new ListingRedirect(getAppletName(), (Long) _inst.getId());
        final String openPath = ApplicationPageUtils.constructAppletOpenPagePath(AppletType.LISTING,
                listingRedirect.getTargetAppletName(), listingRedirect.getTargetInstId());
        TableActionResult result = new TableActionResult(_inst, openPath);
        result.setOpenPath(true);

        if (au().system().getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.ENABLE_OPEN_TAB_IN_BROWSER)) {
            final String tabName = ApplicationNameUtils.addVestigialNamePart(listingRedirect.getTargetAppletName(),
                    String.valueOf(listingRedirect.getTargetInstId()));
            result.setTabName(tabName);
            result.setOpenTab(true);
        }

        return result;
    }

    private BreadCrumbs makeSingleFormBreadCrumbs() {
        BreadCrumbs.Builder bcb = BreadCrumbs.newBuilder();
        // bcb.addHistoryCrumb(form.getFormTitle(), form.getBeanTitle(),
        // form.getFormStepIndex());
        return bcb.build();
    }
}

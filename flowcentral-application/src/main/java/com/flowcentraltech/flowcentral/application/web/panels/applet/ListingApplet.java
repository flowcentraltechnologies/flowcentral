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

import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.flowcentraltech.flowcentral.application.web.panels.EntityFileAttachments;
import com.flowcentraltech.flowcentral.application.web.panels.ListingForm;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs.BreadCrumb;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.data.FormListingOptions;
import com.flowcentraltech.flowcentral.configuration.constants.RecordActionType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Database;
import com.tcdng.unify.core.database.Entity;

/**
 * Listing applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ListingApplet extends AbstractApplet implements SweepingCommitPolicy {

    public enum ViewMode {
        LISTING_FORM;
    };

    protected ListingForm listingForm;

    protected ViewMode viewMode;

    protected EntityFileAttachments formFileAttachments;

    public ListingApplet(AppletUtilities au, List<String> pathVariables) throws UnifyException {
        super(au, pathVariables.get(APPLET_NAME_INDEX));
        this.formFileAttachments = new EntityFileAttachments();
        final String vestigial = ApplicationNameUtils.getVestigialNamePart(pathVariables.get(APPLET_NAME_INDEX));
        final Long entityInstId = Long.valueOf(vestigial);
        Entity _inst = loadEntity(entityInstId);
        listingForm = constructListingForm(_inst);
        this.viewMode = ViewMode.LISTING_FORM;

        setAltSubCaption(listingForm.getFormTitle());
    }

    public void ensureFormStruct() throws UnifyException {
        if (listingForm != null) {
            FormDef _fFormDef = listingForm.getFormDef();
            FormDef _nFormDef = au().getFormDef(_fFormDef.getLongName());
            if (_fFormDef.getVersion() != _nFormDef.getVersion()) {
                Entity _inst = (Entity) listingForm.getFormBean();
                _inst = reloadEntity(_inst, false);
                listingForm = constructListingForm(_inst);
            }
        }
    }

    public EntityActionResult formActionOnInst(String actionPolicyName, String formActionName) throws UnifyException {
        AbstractForm _form = getResolvedForm();
        final FormContext formContext = _form.getCtx();
        Entity _inst = (Entity) _form.getFormBean();
        EntityActionContext efCtx = new EntityActionContext(_form.getFormDef().getEntityDef(), _inst, actionPolicyName);
        efCtx.setAll(formContext);

        final String listingGenerator = listingForm.getFormListing().getListingGenerator();
        efCtx.setListingOptions(new FormListingOptions(formActionName));
        efCtx.setListingGenerator(listingGenerator);
        return au().environment().performEntityAction(efCtx);
    }

    public AbstractForm getForm() {
        return listingForm;
    }

    public ListingForm getListingForm() {
        return listingForm;
    }

    public AbstractForm getResolvedForm() {
        return listingForm;
    }

    public FormDef getCurrentFormDef() {
        return getResolvedForm().getFormDef();
    }

    public ViewMode getMode() {
        return viewMode;
    }

    public boolean isListingView() {
        return ViewMode.LISTING_FORM.equals(viewMode);
    }

    public boolean isRootForm() {
        return true;
    }

    public boolean isNoForm() {
        return false;
    }

    @Override
    public void bumpAllParentVersions(Database db, RecordActionType type) throws UnifyException {

    }

    protected final AppletDef resolveRootAppletDef(String appletName) throws UnifyException {
        AppletDef appletDef = au.getAppletDef(appletName);
        if (appletDef.getType().isEntityList()) {
            appletDef = appletDef.getMaintainAppletDef();
            setAltCaption(appletDef.getPropValue(String.class, AppletPropertyConstants.PAGE_LISTING_CAPTION));
        }

        return appletDef;
    }

    private ListingForm constructListingForm(Entity _inst) throws UnifyException {
        final AppletDef _appletDef = getRootAppletDef();
        FormDef formDef = getPreferredForm(PreferredFormType.LISTING_ONLY, _appletDef, _inst,
                FormMode.LISTING.formProperty());

        String beanTitle = au.getEntityDescription(au.getEntityClassDef(formDef.getEntityDef().getLongName()), _inst,
                null);
        ListingForm listingForm = au.constructListingForm(this, _appletDef.getDescription(), beanTitle, formDef, _inst,
                makeFormBreadCrumbs());
        return listingForm;
    }

    protected List<BreadCrumb> getBaseFormBreadCrumbs() {
        return Collections.emptyList();
    }

    private Entity reloadEntity(Entity _inst, boolean maintainAct) throws UnifyException {
        return au().environment().listLean((Class<? extends Entity>) _inst.getClass(), _inst.getId());
    }

    @SuppressWarnings("unchecked")
    private Entity loadEntity(Object entityInstId) throws UnifyException {
        final AppletDef _currentFormAppletDef = getRootAppletDef();
        final EntityClassDef entityClassDef = au().getEntityClassDef(_currentFormAppletDef.getEntity());
        return au().environment().listLean((Class<? extends Entity>) entityClassDef.getEntityClass(), entityInstId);
    }

    private BreadCrumbs makeFormBreadCrumbs() {
        BreadCrumbs.Builder bcb = BreadCrumbs.newBuilder();
        for (BreadCrumb bc : getBaseFormBreadCrumbs()) {
            bcb.addHistoryCrumb(bc);
        }

        return bcb.build();
    }
}

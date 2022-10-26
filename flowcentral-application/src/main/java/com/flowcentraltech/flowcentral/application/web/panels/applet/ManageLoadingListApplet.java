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

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.AppletFilterDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.data.EntityItem;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.LoadingWorkItemInfo;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.flowcentraltech.flowcentral.application.web.panels.HeaderWithTabsForm;
import com.flowcentraltech.flowcentral.application.web.panels.LoadingSearch;
import com.flowcentraltech.flowcentral.application.web.widgets.AbstractTable;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;

/**
 * Manage loading list applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ManageLoadingListApplet extends AbstractEntityFormApplet {

    private LoadingSearch loadingSearch;

    public ManageLoadingListApplet(AppletUtilities au, String pathVariable,
            AppletWidgetReferences appletWidgetReferences, EntityFormEventHandlers formEventHandlers)
            throws UnifyException {
        super(au, pathVariable, appletWidgetReferences, formEventHandlers);
        loadingSearch = au.constructLoadingSearch(ctx, LoadingSearch.ENABLE_ALL);
        if (isRootAppletPropWithValue(AppletPropertyConstants.BASE_RESTRICTION)) {
            AppletFilterDef appletFilterDef = getRootAppletFilterDef(AppletPropertyConstants.BASE_RESTRICTION);
            loadingSearch.setBaseFilter(new FilterDef(appletFilterDef.getFilterDef()), au.getSpecialParamProvider());
        }

        loadingSearch.applySearchEntriesToSearch();
        loadingSearch.getLoadingTable().setCrudActionHandlers(formEventHandlers.getMaintainActHandlers());

        navBackToSearch();
    }

    public LoadingSearch getLoadingSearch() {
        return loadingSearch;
    }

    @Override
    public boolean navBackToSearch() throws UnifyException {
        if (!super.navBackToSearch()) {
            if (loadingSearch != null) {
                loadingSearch.applySearchEntriesToSearch();
                return true;
            }
        }

        return false;
    }

    @Override
    public void maintainInst(int mIndex) throws UnifyException {
        this.mIndex = mIndex;
        EntityItem item = loadingSearch.getSourceItem(mIndex);
        if (item.isEdit()) {
            Entity _inst = item.getEntity();
            _inst = reloadEntity(_inst, true);
            if (form == null) {
                form = constructForm(_inst, FormMode.MAINTAIN, null, false);
            } else {
                updateForm(HeaderWithTabsForm.UpdateType.MAINTAIN_INST, form, _inst);
            }

            viewMode = ViewMode.MAINTAIN_FORM;
        } else if (item.isWorkItem()) {
            final AppletDef _currentFormAppletDef = getFormAppletDef();
            WorkEntity currEntityInst = (WorkEntity) item.getEntity();
            FormDef formDef = getPreferredForm(PreferredFormType.ALL, _currentFormAppletDef, currEntityInst,
                    FormMode.MAINTAIN.formProperty());
            LoadingWorkItemInfo loadingWorkItemInfo = loadingSearch.getLoadingWorkItemInfo(currEntityInst, mIndex);
            getCtx().setRecovery(loadingWorkItemInfo.isError());
            getCtx().setComments(loadingWorkItemInfo.isComments());
            if (formDef.isInputForm()) {
                if (form == null) {
                    form = constructForm(formDef, currEntityInst, FormMode.MAINTAIN, null, false);
                    currEntityInst = (WorkEntity) form.getFormBean();
                    form.setFormTitle(getRootAppletDef().getLabel());
                    form.setFormActionDefList(loadingWorkItemInfo.getFormActionDefList());
                } else {
                    updateForm(HeaderWithTabsForm.UpdateType.MAINTAIN_INST, form, currEntityInst);
                }

                form.setAppendables(item);
                getCtx().setReadOnly(loadingWorkItemInfo.isReadOnly());
                viewMode = ViewMode.MAINTAIN_FORM;
            } else { // Listing
                listingForm = constructListingForm(formDef, currEntityInst);
                listingForm.setFormTitle(getRootAppletDef().getLabel());
                listingForm.setFormActionDefList(loadingWorkItemInfo.getFormActionDefList());
                listingForm.setAppendables(item);
                getCtx().setEmails(loadingWorkItemInfo.isEmails());
                getCtx().setReadOnly(loadingWorkItemInfo.isError());
                viewMode = ViewMode.LISTING_FORM;
            }
        }
        
        return;
    }

    @Override
    public void applyUserAction(String actionName) throws UnifyException {
        String comment = null;// getNewComment();
        final AbstractForm _form = getResolvedForm();
        WorkEntity currEntityInst = (WorkEntity) _form.getFormBean();
        loadingSearch.applyUserAction(currEntityInst, actionName, comment, _form.getEmails(), mIndex);
        navBackToSearch();
    }

    @Override
    protected AbstractTable<?, ? extends Entity> getSearchTable()  throws UnifyException {
        return loadingSearch.getLoadingTable();
    }

    @Override
    protected AppletDef getAlternateFormAppletDef() throws UnifyException {
        String formAppletName = loadingSearch.getSourceItemFormApplet(mIndex);
        return au.getAppletDef(formAppletName);
    }

}

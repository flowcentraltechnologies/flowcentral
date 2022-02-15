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

import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.AssignmentPageDef;
import com.flowcentraltech.flowcentral.application.data.EntityAttachmentDef;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormRelatedListDef;
import com.flowcentraltech.flowcentral.application.data.FormStatePolicyDef;
import com.flowcentraltech.flowcentral.application.data.FormTabDef;
import com.flowcentraltech.flowcentral.application.data.PropertyRuleDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityUtils;
import com.flowcentraltech.flowcentral.application.validation.FormContextEvaluator;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.flowcentraltech.flowcentral.application.web.panels.EditPropertyList;
import com.flowcentraltech.flowcentral.application.web.panels.EntityChild;
import com.flowcentraltech.flowcentral.application.web.panels.EntityFileAttachments;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySaveAs;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySearch;
import com.flowcentraltech.flowcentral.application.web.panels.HeaderWithTabsForm;
import com.flowcentraltech.flowcentral.application.web.panels.HeadlessTabsForm;
import com.flowcentraltech.flowcentral.application.web.panels.ListingForm;
import com.flowcentraltech.flowcentral.application.web.widgets.AssignmentPage;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs.BreadCrumb;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet.TabSheetItem;
import com.flowcentraltech.flowcentral.common.business.SequenceCodeGenerator;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.flowcentraltech.flowcentral.configuration.constants.FormReviewType;
import com.flowcentraltech.flowcentral.configuration.constants.RecordActionType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Database;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.filter.ObjectFilter;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.data.FileAttachmentsInfo;

/**
 * Abstract entity form applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractEntityFormApplet extends AbstractApplet implements SweepingCommitPolicy {

    protected enum ActionMode {
        ACTION_ONLY,
        ACTION_AND_NEXT,
        ACTION_AND_CLOSE;

        public boolean isWithNext() {
            return ACTION_AND_NEXT.equals(this);
        }

        public boolean isWithClose() {
            return ACTION_AND_CLOSE.equals(this);
        }
    }

    public enum ViewMode {
        SEARCH,
        HEADLESS_TAB,
        NEW_FORM,
        NEW_PRIMARY_FORM,
        NEW_CHILD_FORM,
        NEW_CHILDLIST_FORM,
        NEW_RELATEDLIST_FORM,
        NEW_HEADLESSLIST_FORM,
        LISTING_FORM,
        MAINTAIN_FORM,
        MAINTAIN_FORM_SCROLL,
        MAINTAIN_PRIMARY_FORM_NO_SCROLL,
        MAINTAIN_CHILDLIST_FORM,
        MAINTAIN_CHILDLIST_FORM_NO_SCROLL,
        MAINTAIN_RELATEDLIST_FORM,
        MAINTAIN_RELATEDLIST_FORM_NO_SCROLL,
        MAINTAIN_HEADLESSLIST_FORM,
        MAINTAIN_HEADLESSLIST_FORM_NO_SCROLL,
        ASSIGNMENT_PAGE,
        PROPERTYLIST_PAGE,
        CUSTOM_PAGE;

        private static final Set<ViewMode> newEntityModes = EnumSet.of(NEW_FORM, NEW_PRIMARY_FORM, NEW_CHILD_FORM,
                NEW_CHILDLIST_FORM, NEW_RELATEDLIST_FORM, NEW_HEADLESSLIST_FORM);

        private static final Set<ViewMode> maintainEntityModes = EnumSet.of(MAINTAIN_FORM, MAINTAIN_FORM_SCROLL,
                MAINTAIN_PRIMARY_FORM_NO_SCROLL, MAINTAIN_CHILDLIST_FORM, MAINTAIN_CHILDLIST_FORM_NO_SCROLL,
                MAINTAIN_RELATEDLIST_FORM, MAINTAIN_RELATEDLIST_FORM_NO_SCROLL, MAINTAIN_HEADLESSLIST_FORM,
                MAINTAIN_HEADLESSLIST_FORM_NO_SCROLL);

        private static final Set<ViewMode> rootEntityModes = EnumSet.of(NEW_FORM, NEW_PRIMARY_FORM, MAINTAIN_FORM,
                MAINTAIN_FORM_SCROLL, MAINTAIN_PRIMARY_FORM_NO_SCROLL);

        public boolean isCreateForm() {
            return newEntityModes.contains(this);
        }

        public boolean isMaintainForm() {
            return maintainEntityModes.contains(this);
        }

        public boolean isListingForm() {
            return LISTING_FORM.equals(this);
        }

        public boolean isRootForm() {
            return rootEntityModes.contains(this);
        }

        public boolean isPrimary() {
            return NEW_PRIMARY_FORM.equals(this) || MAINTAIN_PRIMARY_FORM_NO_SCROLL.equals(this);
        }

        public boolean isInForm() {
            return isCreateForm() || isMaintainForm();
        }
    };

    protected EntitySearch entitySearch;

    protected AssignmentPage assignmentPage;

    protected EditPropertyList editPropertyList;

    protected EntitySaveAs entitySaveAs;

    protected HeaderWithTabsForm form;

    protected ListingForm listingForm;

    protected HeadlessTabsForm headlessForm;

    protected Stack<FormState> formStack;

    protected FormRelatedListDef currFormRelatedListDef;

    protected AppletDef currFormAppletDef;

    protected FormTabDef currFormTabDef;

    protected EntityDef currParentEntityDef;

    protected Entity currParentInst;

    protected ViewMode viewMode;

    protected EventHandler[] formSwitchOnChangeHandlers;

    protected EventHandler[] assnSwitchOnChangeHandlers;

    protected EntityFileAttachments formFileAttachments;

    protected int mIndex;

    private boolean fileAttachmentsDisabled;

    private final boolean collaboration;

    public AbstractEntityFormApplet(AppletUtilities au, String pathVariable, EventHandler[] formSwitchOnChangeHandlers,
            EventHandler[] assnSwitchOnChangeHandlers) throws UnifyException {
        this(au, pathVariable, formSwitchOnChangeHandlers, assnSwitchOnChangeHandlers, false);
    }

    public AbstractEntityFormApplet(AppletUtilities au, String pathVariable, EventHandler[] formSwitchOnChangeHandlers,
            EventHandler[] assnSwitchOnChangeHandlers, boolean collaboration) throws UnifyException {
        super(au, pathVariable);
        this.formSwitchOnChangeHandlers = formSwitchOnChangeHandlers;
        this.assnSwitchOnChangeHandlers = assnSwitchOnChangeHandlers;
        this.formFileAttachments = new EntityFileAttachments();
        this.collaboration = collaboration;
    }

    public boolean isCollaboration() {
        return collaboration;
    }

    public boolean navBackToPrevious() throws UnifyException {
        boolean success = true;
        if (restoreForm()) {
            Entity _inst = reloadEntity((Entity) form.getFormBean(), false);
            if (_inst != null) {
                updateForm(HeaderWithTabsForm.UpdateType.NAV_BACK_TO_PREVIOUS, form, _inst);
                reviewFormContext(form.getCtx(), FormReviewType.ON_UPDATE);
            } else {
                success = false;
            }
        } else {
            if (headlessForm != null) {
                navBackToHeadless();
            } else {
                navBackToSearch();
            }
        }

        assignmentPage = null;
        editPropertyList = null;
        entitySaveAs = null;
        return success;
    }

    public void navBackToSearch() throws UnifyException {
        getCtx().setInWorkflow(false);
        form = null;
        listingForm = null;
        viewMode = ViewMode.SEARCH;
        currParentEntityDef = null;
        currFormTabDef = null;
        currFormRelatedListDef = null;
        formFileAttachments.setFileAttachmentsInfo(null);
        entitySearch.applyFilterToSearch();
    }

    public void navBackToHeadless() throws UnifyException {
        getCtx().setInWorkflow(false);
        form = null;
        listingForm = null;
        viewMode = ViewMode.HEADLESS_TAB;
        currParentEntityDef = null;
        currFormTabDef = null;
        currFormRelatedListDef = null;
        headlessForm.getCurrentEntitySearch().applyFilterToSearch();
    }

    public void formSwitchOnChange() throws UnifyException {
        updateForm(HeaderWithTabsForm.UpdateType.SWITCH_ON_CHANGE, form, (Entity) form.getFormBean());
    }

    public void assignSwitchOnChange() throws UnifyException {
        assignmentPage.switchOnChange();
    }

    public void previousInst() throws UnifyException {
        if (isPrevNav()) {
            mIndex--;
            loadScrollInst();
        }
    }

    public void nextInst() throws UnifyException {
        if (isNextNav()) {
            mIndex++;
            loadScrollInst();
        }
    }

    private void loadScrollInst() throws UnifyException {
        if (getCtx().isReview()) {
            maintainInst(mIndex);
        } else {
            if (ViewMode.LISTING_FORM.equals(viewMode)) {
                listingInst(mIndex);
            } else {
                maintainInst(mIndex);
            }
        }
    }

    public void newEntityInst() throws UnifyException {
        form = constructNewForm(FormMode.CREATE, null, false);
        viewMode = ViewMode.NEW_FORM;
    }

    public void newChildItem(int childTabIndex) throws UnifyException {
        newChildItem(childTabIndex, false);
    }

    public void newChildListItem(int childTabIndex) throws UnifyException {
        newChildItem(childTabIndex, true);
    }

    private void newChildItem(int childTabIndex, boolean childList) throws UnifyException {
        FormTabDef _currFormTabDef = form.getFormDef().getFormTabDef(childTabIndex);
        AppletDef _childAppletDef = getAppletDef(_currFormTabDef.getApplet());
        saveCurrentForm();
        currParentEntityDef = form.getFormDef().getEntityDef();
        currParentInst = ((Entity) form.getFormBean());
        currFormTabDef = _currFormTabDef;
        currFormAppletDef = _childAppletDef;
        String childFkFieldName = getAu().getChildFkFieldName(currParentEntityDef, _currFormTabDef.getReference());
        form = constructNewForm(FormMode.CREATE, childFkFieldName, true);
        viewMode = childList ? ViewMode.NEW_CHILDLIST_FORM : ViewMode.NEW_CHILD_FORM;
    }

    public void editChildItem(int childTabIndex) throws UnifyException {
        EntityChild _entityChild = (EntityChild) form.getTabSheet().getCurrentItem().getValObject();
        maintainChildInst(_entityChild.getChildInst(), childTabIndex);
    }

    public void assignToChildItem(int childTabIndex) throws UnifyException {
        currFormTabDef = form.getFormDef().getFormTabDef(childTabIndex);
        final AppletDef _appletDef = getAppletDef(currFormTabDef.getApplet());
        final AssignmentPageDef assignPageDef = getAssignmentPageDef(_appletDef,
                AppletPropertyConstants.ASSIGNMENT_PAGE);
        final String entryTable = _appletDef.getPropValue(String.class, AppletPropertyConstants.ASSIGNMENT_ENTRY_TABLE);
        final String entryTablePolicy = _appletDef.getPropValue(String.class,
                AppletPropertyConstants.ASSIGNMENT_ENTRY_TABLE_POLICY);
        final String assgnUpdatePolicy = _appletDef.getPropValue(String.class,
                AppletPropertyConstants.ASSIGNMENT_UPDATE_POLICY);
        final Object id = ((Entity) form.getFormBean()).getId();
        final String subTitle = ((Entity) form.getFormBean()).getDescription();
        saveCurrentForm();
        assignmentPage = constructNewAssignmentPage(assignPageDef, entryTable, entryTablePolicy, assgnUpdatePolicy, id,
                subTitle);
        assignmentPage.loadAssignedList();
        viewMode = ViewMode.ASSIGNMENT_PAGE;
    }

    public void newRelatedListItem(String relatedListName) throws UnifyException {
        FormRelatedListDef _currFormRelatedListDef = form.getFormDef().getFormRelatedListDef(relatedListName);
        AppletDef _relAppletDef = getAppletDef(_currFormRelatedListDef);
        saveCurrentForm();
        currParentEntityDef = form.getFormDef().getEntityDef();
        currParentInst = ((Entity) form.getFormBean());
        currFormRelatedListDef = _currFormRelatedListDef;
        currFormAppletDef = _relAppletDef;
        String reference = getAu().getChildFkFieldName(currParentEntityDef.getLongName(), _relAppletDef.getEntity());
        form = constructNewForm(FormMode.CREATE, reference, false);
        viewMode = ViewMode.NEW_RELATEDLIST_FORM;
    }

    public void newHeadlessListItem(String headlessListName) throws UnifyException {
        AppletDef _hdlAppletDef = getAppletDef(headlessListName);
        saveCurrentForm();
        currFormAppletDef = _hdlAppletDef;
        form = constructNewForm(FormMode.CREATE, null, false);
        viewMode = ViewMode.NEW_HEADLESSLIST_FORM;
    }

    public void assignToRelatedListItem(String relatedListName) throws UnifyException {
        currFormRelatedListDef = form.getFormDef().getFormRelatedListDef(relatedListName);
        final AppletDef _appletDef = getAppletDef(currFormRelatedListDef.getApplet());
        final AssignmentPageDef assignPageDef = getAssignmentPageDef(_appletDef,
                AppletPropertyConstants.ASSIGNMENT_PAGE);
        final String entryTable = _appletDef.getPropValue(String.class, AppletPropertyConstants.ASSIGNMENT_ENTRY_TABLE);
        final String entryTablePolicy = _appletDef.getPropValue(String.class,
                AppletPropertyConstants.ASSIGNMENT_ENTRY_TABLE_POLICY);
        final String assgnUpdatePolicy = _appletDef.getPropValue(String.class,
                AppletPropertyConstants.ASSIGNMENT_UPDATE_POLICY);
        final Object id = ((Entity) form.getFormBean()).getId();
        final String subTitle = ((Entity) form.getFormBean()).getDescription();
        saveCurrentForm();
        assignmentPage = constructNewAssignmentPage(assignPageDef, entryTable, entryTablePolicy, assgnUpdatePolicy, id,
                subTitle);
        assignmentPage.loadAssignedList();
        viewMode = ViewMode.ASSIGNMENT_PAGE;
    }

    public void prepareItemProperties(int childTabIndex) throws UnifyException {
        currFormTabDef = form.getFormDef().getFormTabDef(childTabIndex);
        PropertyRuleDef _propertyRuleDef = getPropertyRuleDef(getAppletDef(currFormTabDef.getApplet()),
                AppletPropertyConstants.PROPERTY_LIST_RULE);
        Entity inst = (Entity) form.getFormBean();
        saveCurrentForm();
        String childFkFieldName = getAu().getChildFkFieldName(form.getFormDef().getEntityDef(),
                currFormTabDef.getReference());
        editPropertyList = constructNewEditPropertyList(_propertyRuleDef, inst, inst.getDescription(),
                childFkFieldName);
        editPropertyList.loadPropertyList(form.getCtx());
        viewMode = ViewMode.PROPERTYLIST_PAGE;
    }

    public void prepareSaveEntityAs() throws UnifyException {
        Entity _inst = (Entity) form.getFormBean();
        Entity _saveAsInst = getAu().getEnvironment().find((Class<? extends Entity>) _inst.getClass(), _inst.getId());
        ((BaseEntity) _saveAsInst).setId(null);
        entitySaveAs = constructNewEntitySaveAs(_saveAsInst);
        entitySaveAs.loadEntitySaveAs(form.getCtx());
    }

    public EntityActionResult saveEntityAs(String saveAsPolicy) throws UnifyException {
        EntityActionResult result = entitySaveAs.commitEntitySaveAs(saveAsPolicy);
        entitySaveAs = null;
        return result;
    }

    public void cancelSaveEntityAs() throws UnifyException {
        entitySaveAs = null;
    }

    public void maintainInst(int mIndex) throws UnifyException {
        this.mIndex = mIndex;
        Entity _inst = getEntitySearchItem(entitySearch, mIndex);
        _inst = reloadEntity(_inst, true);
        if (form == null) {
            form = constructForm(_inst, FormMode.MAINTAIN, null, false);
        } else {
            updateForm(HeaderWithTabsForm.UpdateType.MAINTAIN_INST, form, _inst);
        }

        viewMode = ViewMode.MAINTAIN_FORM_SCROLL;
        return;
    }

    public void listingInst(int mIndex) throws UnifyException {
        this.mIndex = mIndex;
        Entity _inst = getEntitySearchItem(entitySearch, mIndex);
        _inst = reloadEntity(_inst, true);
        listingForm = constructListingForm(_inst);
        viewMode = ViewMode.LISTING_FORM;
        return;
    }

    public void maintainChildInst(int mIndex) throws UnifyException {
        EntitySearch _entitySearch = (EntitySearch) form.getTabSheet().getCurrentItem().getValObject();
        Entity _inst = getEntitySearchItem(_entitySearch, mIndex);
        maintainChildInst(_inst, _entitySearch.getChildTabIndex());
    }

    private void maintainChildInst(Entity _inst, int tabIndex) throws UnifyException {
        _inst = reloadEntity(_inst, true);
        FormTabDef _currFormTabDef = form.getFormDef().getFormTabDef(tabIndex);
        AppletDef childAppletDef = getAppletDef(_currFormTabDef.getApplet());
        saveCurrentForm();
        currParentEntityDef = form.getFormDef().getEntityDef();
        currParentInst = ((Entity) form.getFormBean());
        currFormTabDef = _currFormTabDef;
        currFormAppletDef = childAppletDef;
        String childFkFieldName = getAu().getChildFkFieldName(currParentEntityDef, _currFormTabDef.getReference());
        form = constructForm(_inst, FormMode.MAINTAIN, childFkFieldName, true);
        viewMode = ViewMode.MAINTAIN_CHILDLIST_FORM_NO_SCROLL;
    }

    public void maintainRelatedInst(int mIndex) throws UnifyException {
        EntitySearch _entitySearch = (EntitySearch) form.getRelatedListTabSheet().getCurrentItem().getValObject();
        Entity _inst = getEntitySearchItem(_entitySearch, mIndex);
        _inst = reloadEntity(_inst, true);
        FormRelatedListDef _formRelatedListDef = form.getFormDef()
                .getFormRelatedListDef(_entitySearch.getRelatedList());
        AppletDef relAppletDef = getAppletDef(_formRelatedListDef);
        saveCurrentForm();
        currParentEntityDef = form.getFormDef().getEntityDef();
        currParentInst = ((Entity) form.getFormBean());
        currFormAppletDef = relAppletDef;
        String childFkFieldName = getAu().getChildFkFieldName(currParentEntityDef.getLongName(),
                relAppletDef.getEntity());
        form = constructForm(_inst, FormMode.MAINTAIN, childFkFieldName, false);
        viewMode = ViewMode.MAINTAIN_RELATEDLIST_FORM_NO_SCROLL;
    }

    public void maintainHeadlessInst(int mIndex) throws UnifyException {
        TabSheetItem tabSheetItem = headlessForm.getHeadlessTabSheet().getCurrentItem();
        EntitySearch _entitySearch = (EntitySearch) tabSheetItem.getValObject();
        Entity _inst = getEntitySearchItem(_entitySearch, mIndex);
        _inst = reloadEntity(_inst, true);
        final AppletDef hdlAppletDef = getAppletDef(tabSheetItem.getAppletName());
        saveCurrentForm();
        currFormAppletDef = hdlAppletDef;
        form = constructForm(_inst, FormMode.MAINTAIN, null, false);
        viewMode = ViewMode.MAINTAIN_HEADLESSLIST_FORM_NO_SCROLL;
    }

    public FormContext reviewOnClose() throws UnifyException {
        if (form != null && viewMode.isMaintainForm()) {
            reviewFormContext(form.getCtx(), FormReviewType.ON_CLOSE);
            return form.getCtx();
        }
        
        return null;
    }
    
    public EntityActionResult saveNewInst() throws UnifyException {
        return saveNewInst(ActionMode.ACTION_ONLY, FormReviewType.ON_SAVE);
    }

    public EntityActionResult saveNewInstAndNext() throws UnifyException {
        return saveNewInst(ActionMode.ACTION_AND_NEXT, FormReviewType.ON_SAVE_NEXT);
    }

    public EntityActionResult saveNewInstAndClose() throws UnifyException {
        return saveNewInst(ActionMode.ACTION_AND_CLOSE, FormReviewType.ON_SAVE_CLOSE);
    }

    public EntityActionResult submitInst() throws UnifyException {
        return submitInst(ActionMode.ACTION_AND_CLOSE, FormReviewType.ON_SUBMIT);
    }

    public EntityActionResult submitInstAndNext() throws UnifyException {
        return submitInst(ActionMode.ACTION_AND_NEXT, FormReviewType.ON_SUBMIT_NEXT);
    }

    public EntityActionResult updateInst() throws UnifyException {
        return updateInst(FormReviewType.ON_UPDATE);
    }

    public EntityActionResult updateInstAndClose() throws UnifyException {
        EntityActionResult entityActionResult = updateInst(FormReviewType.ON_UPDATE_CLOSE);
        if (!form.getCtx().isWithReviewErrors()) {
            if (isRootForm() && getRootAppletDef().getType().isFormInitial()) {
                entityActionResult.setClosePage(true);
            } else {
                entityActionResult.setCloseView(true);
            }
        }

        return entityActionResult;
    }

    public EntityActionResult deleteInst() throws UnifyException {
        Entity inst = (Entity) form.getFormBean();
        final AppletDef _currFormAppletDef = getFormAppletDef();
        String deletePolicy = _currFormAppletDef != null
                ? _currFormAppletDef.getPropValue(String.class, AppletPropertyConstants.MAINTAIN_FORM_DELETE_POLICY)
                : form.getCtx().getAttribute(String.class, AppletPropertyConstants.MAINTAIN_FORM_DELETE_POLICY);
        EntityActionContext eCtx = new EntityActionContext(form.getFormDef().getEntityDef(), inst,
                RecordActionType.DELETE, this, deletePolicy);
        eCtx.setAll(form.getCtx());
        EntityActionResult entityActionResult = getAu().getEnvironment().delete(eCtx);
        if (viewMode == ViewMode.MAINTAIN_FORM_SCROLL) {
            List<Entity> itemList = entitySearch.getEntityTable().getDispItemList();
            itemList.remove(mIndex);
            int size = itemList.size();
            if (mIndex > 0 && mIndex >= size) {
                mIndex--;
            }

            if (size > 0) {
                loadScrollInst();
                return entityActionResult;
            }
        }

        final boolean closePage = !navBackToPrevious();
        entityActionResult.setClosePage(closePage);
        entityActionResult.setRefreshMenu(closePage);
        return entityActionResult;
    }

    public EntityActionResult formActionOnInst(String actionPolicyName) throws UnifyException {
        Entity _inst = (Entity) form.getFormBean();
        EntityActionContext efCtx = new EntityActionContext(form.getFormDef().getEntityDef(), _inst, actionPolicyName);
        efCtx.setAll(form.getCtx());
        EntityActionResult entityActionResult = getAu().getEnvironment().performEntityAction(efCtx);
        updateForm(HeaderWithTabsForm.UpdateType.FORMACTION_ON_INST, form, reloadEntity(_inst, false));
        return entityActionResult;
    }

    public boolean isPrevNav() {
        return mIndex > 0;
    }

    public boolean isNextNav() {
        return mIndex < (entitySearch.getEntityTable().getDispItemList().size() - 1);
    }

    public String getDisplayItemCounter() throws UnifyException {
        return getAu().resolveSessionMessage("$m{entityformapplet.displaycounter}", mIndex + 1,
                entitySearch.getEntityTable().getDispItemList().size());
    }

    public EntitySearch getEntitySearch() {
        return entitySearch;
    }

    public HeadlessTabsForm getHeadlessForm() {
        return headlessForm;
    }

    public AssignmentPage getAssignmentPage() {
        return assignmentPage;
    }

    public EditPropertyList getEditPropertyList() {
        return editPropertyList;
    }

    public EntitySaveAs getEntitySaveAs() {
        return entitySaveAs;
    }

    public boolean isWithBaseFilter() {
        return entitySearch.isWithBaseFilter();
    }

    public AppletDef getFormAppletDef() throws UnifyException {
        return currFormAppletDef != null ? currFormAppletDef : getAlternateFormAppletDef();
    }

    public HeaderWithTabsForm getForm() {
        return form;
    }

    public ListingForm getListingForm() {
        return listingForm;
    }

    public AbstractForm getResolvedForm() {
        return ViewMode.LISTING_FORM.equals(viewMode) ? listingForm : form;
    }

    public void setFileAttachmentsDisabled(boolean fileAttachmentsDisabled) {
        this.fileAttachmentsDisabled = fileAttachmentsDisabled;
    }

    public EntityFileAttachments getFormFileAttachments() {
        if (form != null) {
            EntityDef formEntityDef = form.getFormDef().getEntityDef();
            String parentId = ApplicationEntityUtils.getEntityInstName(formEntityDef.getLongName(),
                    (Long) ((Entity) form.getFormBean()).getId());
            FileAttachmentsInfo.Builder fb = FileAttachmentsInfo.newBuilder(parentId);
            for (EntityAttachmentDef entityAttachmentDef : formEntityDef.getAttachmentList()) {
                fb.addFileAttachmentInfo(entityAttachmentDef.getType(), entityAttachmentDef.getName(),
                        entityAttachmentDef.getDescription());
            }

            FileAttachmentsInfo fileAttachmentsInfo = fb.build();
            fileAttachmentsInfo.setDisabled(fileAttachmentsDisabled);
            formFileAttachments.setFileAttachmentsInfo(fileAttachmentsInfo);
        }

        return formFileAttachments;
    }

    public FormDef getCurrentFormDef() {
        return form.getFormDef();
    }

    public ViewMode getMode() {
        return viewMode;
    }

    public boolean isRootForm() {
        return form != null && (formStack == null || formStack.isEmpty());
    }

    public boolean isNoForm() {
        return form == null;
    }

    public boolean formBeanMatchAppletPropertyCondition(String conditionPropName) throws UnifyException {
        return formBeanMatchAppletPropertyCondition(getFormAppletDef(), form, conditionPropName);
    }

    public void ensureCurrentAppletStruct() throws UnifyException {
        AppletDef _currFormAppletDef = getFormAppletDef();
        if (_currFormAppletDef != null) {
            AppletDef _nAppletDef = au.getAppletDef(_currFormAppletDef.getLongName());
            if (_currFormAppletDef.getVersion() != _nAppletDef.getVersion()) {
                if (_currFormAppletDef == getRootAppletDef()) {
                    ensureRootAppletStruct();
                }
                _currFormAppletDef = _nAppletDef;
            }
        }
    }

    public void ensureFormStruct() throws UnifyException {
        if (form != null) {
            FormDef _fFormDef = form.getFormDef();
            FormDef _nFormDef = getAu().getFormDef(_fFormDef.getLongName());
            if (_fFormDef.getVersion() != _nFormDef.getVersion()) {
                if (form.getFormMode().isCreate()) {
                    form = constructNewForm(form.getFormMode(), null, false);
                } else {
                    Entity _inst = (Entity) form.getFormBean();
                    _inst = reloadEntity(_inst, false);
                    form = constructForm(_inst, form.getFormMode(), null, false);
                }
            }
        }
    }

    @Override
    public void bumpAllParentVersions(Database db, RecordActionType type) throws UnifyException {
        if (formStack != null && !formStack.isEmpty()) {
            bumpVersion(db, currParentEntityDef, currParentInst);
            for (FormState formState : formStack) {
                bumpVersion(db, formState.parentEntityDef, formState.parentInst);
            }

            if (editPropertyList != null) {
                bumpVersion(db, form.getFormDef().getEntityDef(), (Entity) form.getFormBean());
            }
        }
    }

    protected abstract AppletDef getAlternateFormAppletDef() throws UnifyException;

    protected AssignmentPage constructNewAssignmentPage(AssignmentPageDef assignPageDef, String entryTable,
            String entryTablePolicy, String assignmentUpdatePolicy, Object id, String subTitle) throws UnifyException {
        BreadCrumbs breadCrumbs = form.getBreadCrumbs().advance();
        EntityClassDef entityClassDef = getEntityClassDef(assignPageDef.getEntity());
        breadCrumbs.setLastCrumbTitle(entityClassDef.getEntityDef().getDescription());
        breadCrumbs.setLastCrumbSubTitle(subTitle);
        return new AssignmentPage(getCtx(), assnSwitchOnChangeHandlers, this, assignPageDef, entityClassDef, id,
                breadCrumbs, entryTable, entryTablePolicy, assignmentUpdatePolicy);
    }

    protected EditPropertyList constructNewEditPropertyList(PropertyRuleDef propertyRuleDef, Entity inst,
            String subTitle, String childFkFieldName) throws UnifyException {
        BreadCrumbs breadCrumbs = form.getBreadCrumbs().advance();
        breadCrumbs.setLastCrumbTitle(getAu().resolveSessionMessage("$m{application.propertyitem.table.label}"));
        breadCrumbs.setLastCrumbSubTitle(subTitle);
        return new EditPropertyList(getCtx(), this, propertyRuleDef, inst, breadCrumbs, childFkFieldName);
    }

    protected EntitySaveAs constructNewEntitySaveAs(Entity inst) throws UnifyException {
        return new EntitySaveAs(au, this, inst);
    }

    protected HeaderWithTabsForm constructNewForm(FormMode formMode, String childFkFieldName, boolean isChild)
            throws UnifyException {
        final AppletDef _currentFormAppletDef = getFormAppletDef();
        final EntityClassDef entityClassDef = getAu().getEntityClassDef(_currentFormAppletDef.getEntity());
        final Object inst = ReflectUtils.newInstance(entityClassDef.getEntityClass());
        String createNewCaption = _currentFormAppletDef != null
                ? _currentFormAppletDef.getPropValue(String.class, AppletPropertyConstants.CREATE_FORM_NEW_CAPTION)
                : null;
        final String beanTitle = !StringUtils.isBlank(createNewCaption) ? createNewCaption
                : au.resolveSessionMessage("$m{form.newrecord}");
        FormDef formDef = getPreferredForm(PreferredFormType.INPUT_ONLY, _currentFormAppletDef, (Entity) inst,
                formMode.formProperty());
        return constructForm(formDef, (Entity) inst, formMode, beanTitle, childFkFieldName, isChild);
    }

    protected HeaderWithTabsForm constructForm(Entity inst, FormMode formMode, String childFkFieldName, boolean isChild)
            throws UnifyException {
        final AppletDef _currentFormAppletDef = getFormAppletDef();
        FormDef formDef = getPreferredForm(PreferredFormType.INPUT_ONLY, _currentFormAppletDef, inst,
                formMode.formProperty());
        return constructForm(formDef, inst, formMode, childFkFieldName, isChild);
    }

    protected ListingForm constructListingForm(Entity _inst) throws UnifyException {
        final AppletDef _currentFormAppletDef = getFormAppletDef();
        FormDef formDef = getPreferredForm(PreferredFormType.LISTING_ONLY, _currentFormAppletDef, _inst,
                FormMode.LISTING.formProperty());

        ListingForm listingForm = au.constructListingForm(this, getRootAppletDef().getDescription(),
                _inst.getDescription(), formDef, _inst, makeFormBreadCrumbs());
        return listingForm;
    }

    protected ListingForm constructListingForm(FormDef formDef, Entity _inst) throws UnifyException {
        ListingForm listingForm = au.constructListingForm(this, getRootAppletDef().getDescription(),
                _inst.getDescription(), formDef, _inst, makeFormBreadCrumbs());
        return listingForm;
    }

    protected HeaderWithTabsForm constructForm(FormDef formDef, Entity inst, FormMode formMode, String childFkFieldName,
            boolean isChild) throws UnifyException {
        AppletDef _currentFormAppletDef = getFormAppletDef();
        String createNewCaption = _currentFormAppletDef != null
                ? _currentFormAppletDef.getPropValue(String.class, AppletPropertyConstants.CREATE_FORM_NEW_CAPTION)
                : null;
        final String beanTitle = inst.getDescription() != null ? inst.getDescription()
                : !StringUtils.isBlank(createNewCaption) ? createNewCaption
                        : au.resolveSessionMessage("$m{form.newrecord}");
        return constructForm(formDef, inst, formMode, beanTitle, childFkFieldName, isChild);
    }

    private HeaderWithTabsForm constructForm(FormDef formDef, Entity inst, FormMode formMode, String beanTitle,
            String childFkFieldName, boolean isChild) throws UnifyException {
        final AppletDef _currentFormAppletDef = getFormAppletDef();
        if (formDef == null && formMode.isMaintain()) {
            formDef = form.getCtx().getFormDef();
        }

        final HeaderWithTabsForm form = au.constructHeaderWithTabsForm(this, getRootAppletDef().getDescription(),
                beanTitle, formDef, inst, formMode, makeFormBreadCrumbs(), formSwitchOnChangeHandlers);
        final boolean isReference = !StringUtils.isBlank(childFkFieldName);
        if (isReference) {
            form.getCtx().setFixedReference(childFkFieldName);
        }

        if (formMode.isCreate()) {
            if (isReference) {
                if (isChild) {
                    getAu().populateNewChildReferenceFields(currParentEntityDef, currFormTabDef.getReference(),
                            currParentInst, (Entity) form.getFormBean());
                } else { // Related List
                    DataUtils.setBeanProperty(form.getFormBean(), childFkFieldName, currParentInst.getId());
                }
            }

            // Apply create state policy
            String onCreateStatePolicy = _currentFormAppletDef.getPropValue(String.class,
                    AppletPropertyConstants.CREATE_FORM_STATE_POLICY);
            if (!StringUtils.isBlank(onCreateStatePolicy)) {
                FormStatePolicyDef onCreateFormStatePolicyDef = formDef
                        .getOnCreateFormStatePolicyDef(onCreateStatePolicy);
                if (onCreateFormStatePolicyDef.isSetValues()) {
                    onCreateFormStatePolicyDef.getSetValuesDef().apply(au, formDef.getEntityDef(), au.getNow(),
                            form.getCtx().getFormValueStore(), null);
                }
            }

            // Populate skeleton for auto-format fields
            EntityDef _entityDef = formDef.getEntityDef();
            if (_entityDef.isWithAutoFormatFields()) {
                final SequenceCodeGenerator gen = au.getSequenceCodeGenerator();
                for (EntityFieldDef entityFieldDef : _entityDef.getAutoFormatFieldDefList()) {
                    if (entityFieldDef.isStringAutoFormat()) {
                        DataUtils.setBeanProperty(inst, entityFieldDef.getFieldName(),
                                gen.getCodeSkeleton(entityFieldDef.getAutoFormat()));
                    }
                }
            }
        }

        // Fire on form construct value generators
        for (FormStatePolicyDef formStatePolicyDef : formDef.getOnFormConstructSetValuesFormStatePolicyDefList()) {
            if (formStatePolicyDef.isSetValues()) {
                formStatePolicyDef.getSetValuesDef().apply(au, formDef.getEntityDef(), au.getNow(),
                        form.getCtx().getFormValueStore(), null);
            }
        }

        return form;
    }

    protected void updateForm(HeaderWithTabsForm.UpdateType updateType, HeaderWithTabsForm form, Entity inst)
            throws UnifyException {
        form.getCtx().resetTabIndex();
        form.setUpdateType(updateType);
        au.updateHeaderWithTabsForm(this, form, inst);
    }

    protected void saveCurrentForm() throws UnifyException {
        if (formStack == null) {
            formStack = new Stack<FormState>();
        }

        formStack.push(new FormState(currFormAppletDef, form, currFormRelatedListDef, currFormTabDef,
                currParentEntityDef, currParentInst, viewMode));
    }

    protected boolean restoreForm() {
        if (formStack != null && !formStack.isEmpty()) {
            FormState formState = formStack.pop();
            currFormAppletDef = formState.getAppletDef();
            currFormRelatedListDef = formState.getFormRelatedListDef();
            currFormTabDef = formState.getFormTabDef();
            currParentEntityDef = formState.getParentEntityDef();
            currParentInst = formState.getParentInst();
            form = formState.getForm();
            viewMode = formState.getMode();
            return form != null;
        }

        return false;
    }

    protected AppletDef getAppletDef(String appletName) throws UnifyException {
        return au.getAppletDef(appletName);
    }

    protected AppletDef getAppletDef(FormRelatedListDef formRelatedListDef) throws UnifyException {
        return au.getAppletDef(formRelatedListDef.getApplet());
    }

    protected List<BreadCrumb> getBaseFormBreadCrumbs() {
        return Collections.emptyList();
    }

    protected Entity getEntitySearchItem(EntitySearch entitySearch, int index) throws UnifyException {
        return entitySearch.getEntityTable().getDispItemList().get(index);
    }

    protected class FormState {

        private AppletDef appletDef;

        private HeaderWithTabsForm form;

        private FormRelatedListDef formRelatedListDef;

        private FormTabDef formTabDef;

        private EntityDef parentEntityDef;

        private Entity parentInst;

        private ViewMode viewMode;

        public FormState(AppletDef appletDef, HeaderWithTabsForm form, FormRelatedListDef formRelatedListDef,
                FormTabDef formTabDef, EntityDef parentEntityDef, Entity parentInst, ViewMode viewMode) {
            this.appletDef = appletDef;
            this.form = form;
            this.formRelatedListDef = formRelatedListDef;
            this.formTabDef = formTabDef;
            this.parentEntityDef = parentEntityDef;
            this.parentInst = parentInst;
            this.viewMode = viewMode;
        }

        public AppletDef getAppletDef() {
            return this.appletDef;
        }

        public HeaderWithTabsForm getForm() {
            return this.form;
        }

        public boolean isWithForm() {
            return form != null;
        }

        public FormRelatedListDef getFormRelatedListDef() {
            return this.formRelatedListDef;
        }

        public FormTabDef getFormTabDef() {
            return this.formTabDef;
        }

        public EntityDef getParentEntityDef() {
            return parentEntityDef;
        }

        public Entity getParentInst() {
            return this.parentInst;
        }

        public ViewMode getMode() {
            return this.viewMode;
        }

        @Override
        public String toString() {
            return "FormState [parentEntityDef=" + parentEntityDef + ", parentInst=" + parentInst + ", viewMode="
                    + viewMode + "]";
        }

    }

    private void applyDelayedSetValues(HeaderWithTabsForm form) throws UnifyException {
        final FormDef _formDef = form.getFormDef();
        final ValueStore _formValueStore = form.getCtx().getFormValueStore();
        final Date now = au.getNow();
        // Execute delayed set values
        for (FormStatePolicyDef formStatePolicyDef : _formDef.getOnDelayedSetValuesFormStatePolicyDefList()) {
            ObjectFilter objectFilter = formStatePolicyDef.isWithCondition()
                    ? formStatePolicyDef.getOnCondition().getObjectFilter(_formDef.getEntityDef(),
                            au.getSpecialParamProvider(), now)
                    : null;
            if (objectFilter == null || objectFilter.match(_formValueStore)) {
                if (formStatePolicyDef.isSetValues()) {
                    formStatePolicyDef.getSetValuesDef().apply(au, _formDef.getEntityDef(), now, _formValueStore, null);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void bumpVersion(Database db, EntityDef entityDef, Entity inst) throws UnifyException {
        if (inst != null) {
            final EntityClassDef entityClassDef = getAu().getEntityClassDef(entityDef.getLongName());
            Query<?> query = Query.of((Class<? extends Entity>) entityClassDef.getEntityClass()).addEquals("id",
                    inst.getId());
            long bumpedVersionNo = db.value(long.class, "versionNo", query) + 1L;
            db.updateAll(query, new Update().add("versionNo", bumpedVersionNo));
            ((BaseEntity) inst).setVersionNo(bumpedVersionNo);
        }
    }

    @SuppressWarnings("unchecked")
    private Entity loadEntity(Object entityInstId) throws UnifyException {
        final AppletDef _currentFormAppletDef = getFormAppletDef();
        final EntityClassDef entityClassDef = getAu().getEntityClassDef(_currentFormAppletDef.getEntity());
        return getAu().getEnvironment().listLean((Class<? extends Entity>) entityClassDef.getEntityClass(),
                entityInstId);
    }

    private Entity reloadEntity(Entity _inst, boolean maintainAct) throws UnifyException {
        if (maintainAct) {
            // TODO Fire on-maintain-action policy
        }

        return getAu().getEnvironment().listLean((Class<? extends Entity>) _inst.getClass(), _inst.getId());
    }

    private BreadCrumbs makeFormBreadCrumbs() {
        BreadCrumbs.Builder bcb = BreadCrumbs.newBuilder();
        for (BreadCrumb bc : getBaseFormBreadCrumbs()) {
            bcb.addHistoryCrumb(bc);
        }

        if (formStack != null && !formStack.isEmpty()) {
            int len = formStack.size();
            for (int i = 0; i < len; i++) {
                FormState formState = formStack.get(i);
                if (formState.isWithForm()) {
                    HeaderWithTabsForm sf = formState.getForm();
                    bcb.addHistoryCrumb(sf.getFormTitle(), sf.getBeanTitle(), sf.getFormStepIndex());
                } else {
                    bcb.addHistoryCrumb(formState.getAppletDef().getLabel(), "", 0);
                }
            }
        }

        return bcb.build();
    }

    private EntityActionResult saveNewInst(ActionMode actionMode, FormReviewType reviewType) throws UnifyException {
        final FormContext formContext = form.getCtx();
        EntityActionResult entityActionResult = createInst();
        Long entityInstId = (Long) entityActionResult.getResult();

        // Review form
        reviewFormContext(formContext, reviewType);

        if (FormReviewType.ON_SAVE.equals(reviewType) || formContext.isWithReviewErrors()) {
            enterMaintainForm(formContext, entityInstId);
        } else {
            if (actionMode.isWithNext()) {
                enterNextForm();
            } else if (actionMode.isWithClose()) {
                if (viewMode == ViewMode.NEW_PRIMARY_FORM) {
                    entityActionResult.setClosePage(true);
                } else {
                    navBackToPrevious();
                }
            }
        }

        return entityActionResult;
    }

    private EntityActionResult submitInst(ActionMode actionMode, FormReviewType reviewType) throws UnifyException {
        final FormContext formContext = form.getCtx();
        final Entity inst = (Entity) form.getFormBean();
        final AppletDef _currFormAppletDef = getFormAppletDef();
        EntityActionResult entityActionResult = null;
        Long entityInstId = (Long) inst.getId();
        if (viewMode.isCreateForm()) {
            entityActionResult = createInst();
            entityInstId = (Long) entityActionResult.getResult();
        }

        // Review form
        reviewFormContext(formContext, reviewType);

        if (formContext.isWithReviewErrors()) {
            enterMaintainForm(formContext, entityInstId);
            entityActionResult = new EntityActionResult(
                    new EntityActionContext(form.getFormDef().getEntityDef(), inst, null));
        } else {
            entityActionResult = getAu().getWorkItemUtilities().submitToWorkflowChannel(
                    form.getFormDef().getEntityDef(),
                    _currFormAppletDef.getPropValue(String.class,
                            AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_WORKFLOW_CHANNEL),
                    (WorkEntity) inst,
                    _currFormAppletDef.getPropValue(String.class, AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_POLICY));
            if (actionMode.isWithNext()) {
                enterNextForm();
            } else {
                if (viewMode == ViewMode.NEW_PRIMARY_FORM) {
                    entityActionResult.setClosePage(true);
                } else {
                    navBackToPrevious();
                }
            }
        }

        return entityActionResult;
    }

    private EntityActionResult updateInst(FormReviewType reviewType) throws UnifyException {
        Entity inst = (Entity) form.getFormBean();
        final AppletDef _currFormAppletDef = getFormAppletDef();
        String updatePolicy = _currFormAppletDef != null
                ? _currFormAppletDef.getPropValue(String.class, AppletPropertyConstants.MAINTAIN_FORM_UPDATE_POLICY)
                : form.getCtx().getAttribute(String.class, AppletPropertyConstants.MAINTAIN_FORM_UPDATE_POLICY);
        EntityActionContext eCtx = new EntityActionContext(form.getFormDef().getEntityDef(), inst,
                RecordActionType.UPDATE, this, updatePolicy);
        eCtx.setAll(form.getCtx());

        applyDelayedSetValues(form);

        EntityActionResult entityActionResult = getAu().getEnvironment().updateLean(eCtx);
        updateForm(HeaderWithTabsForm.UpdateType.UPDATE_INST, form, reloadEntity(inst, false));

        // Review form
        reviewFormContext(form.getCtx(), reviewType);

        return entityActionResult;
    }

    private void reviewFormContext(FormContext formContext, FormReviewType reviewType) throws UnifyException {
        FormContextEvaluator formContextEvaluator = getAu().getComponent(FormContextEvaluator.class,
                ApplicationModuleNameConstants.FORMCONTEXT_EVALUATOR);
        formContextEvaluator.reviewFormContext(formContext, reviewType);
    }

    private EntityActionResult createInst() throws UnifyException {
        final Entity inst = (Entity) form.getFormBean();
        final FormDef _formDef = form.getFormDef();
        final EntityDef _entityDef = _formDef.getEntityDef();
        final AppletDef _currFormAppletDef = getFormAppletDef();
        final FormContext formContext = form.getCtx();
        String createPolicy = _currFormAppletDef != null
                ? _currFormAppletDef.getPropValue(String.class, AppletPropertyConstants.CREATE_FORM_NEW_POLICY)
                : formContext.getAttribute(String.class, AppletPropertyConstants.CREATE_FORM_NEW_POLICY);
        EntityActionContext eCtx = new EntityActionContext(_entityDef, inst, RecordActionType.CREATE, this,
                createPolicy);
        eCtx.setAll(formContext);

        // Populate values for auto-format fields
        final SequenceCodeGenerator gen = au.getSequenceCodeGenerator();
        for (EntityFieldDef entityFieldDef : _entityDef.getAutoFormatFieldDefList()) {
            if (entityFieldDef.isStringAutoFormat()) {
                String skeleton = gen.getCodeSkeleton(entityFieldDef.getAutoFormat());
                if (skeleton.equals(DataUtils.getBeanProperty(String.class, inst, entityFieldDef.getFieldName()))) {
                    DataUtils.setBeanProperty(inst, entityFieldDef.getFieldName(),
                            gen.getNextSequenceCode(_entityDef.getLongName(), entityFieldDef.getAutoFormat()));
                }
            }
        }

        // Apply delayed set values here since set values can depend on auto-format
        // fields
        applyDelayedSetValues(form);

        EntityActionResult entityActionResult;
        try {
            entityActionResult = getAu().getEnvironment().create(eCtx);
        } catch (UnifyException e) {
            // Revert to skeleton values
            if (_entityDef.isWithAutoFormatFields()) {
                for (EntityFieldDef entityFieldDef : _entityDef.getAutoFormatFieldDefList()) {
                    if (entityFieldDef.isStringAutoFormat()) {
                        DataUtils.setBeanProperty(inst, entityFieldDef.getFieldName(),
                                gen.getCodeSkeleton(entityFieldDef.getAutoFormat()));
                    }
                }
            }
            throw e;
        }

        return entityActionResult;
    }

    private void enterNextForm() throws UnifyException {
        if (viewMode == ViewMode.NEW_FORM || viewMode == ViewMode.NEW_HEADLESSLIST_FORM
                || viewMode == ViewMode.NEW_PRIMARY_FORM) {
            form = constructNewForm(FormMode.CREATE, null, false);
        } else if (viewMode == ViewMode.NEW_CHILD_FORM || viewMode == ViewMode.NEW_CHILDLIST_FORM) {
            String childFkFieldName = getAu().getChildFkFieldName(currParentEntityDef, currFormTabDef.getReference());
            form = constructNewForm(FormMode.CREATE, childFkFieldName, true);
        } else if (viewMode == ViewMode.NEW_RELATEDLIST_FORM) {
            String reference = getAu().getChildFkFieldName(currParentEntityDef.getLongName(),
                    form.getFormDef().getEntityDef().getLongName());
            form = constructNewForm(FormMode.CREATE, reference, false);
        }
    }

    private void enterMaintainForm(FormContext oldFormContext, Long entityInstId) throws UnifyException {
        if (entityInstId != null) {
            switch (viewMode) {
                case NEW_FORM: {
                    Entity inst = loadEntity(entityInstId);
                    form = constructForm(inst, FormMode.MAINTAIN, null, false);
                    viewMode = ViewMode.MAINTAIN_FORM;
                }
                    break;
                case NEW_PRIMARY_FORM: {
                    Entity inst = loadEntity(entityInstId);
                    form = constructForm(inst, FormMode.MAINTAIN, null, false);
                    viewMode = ViewMode.MAINTAIN_PRIMARY_FORM_NO_SCROLL;
                }
                    break;
                case NEW_CHILD_FORM:
                case NEW_CHILDLIST_FORM: {
                    String childFkFieldName = getAu().getChildFkFieldName(currParentEntityDef,
                            currFormTabDef.getReference());
//                  FormDef formDef = getFormDef(getAppletDef(currFormTabDef.getApplet()),
//                  AppletPropertyConstants.MAINTAIN_FORM);
                    Entity inst = loadEntity(entityInstId);
                    form = constructForm(inst, FormMode.MAINTAIN, childFkFieldName, true);
                    viewMode = ViewMode.MAINTAIN_CHILDLIST_FORM;
                }
                    break;
                case NEW_RELATEDLIST_FORM: {
                    String childFkFieldName = getAu().getChildFkFieldName(currParentEntityDef.getLongName(),
                            form.getFormDef().getEntityDef().getLongName());
//                  FormDef formDef = getFormDef(getAppletDef(currFormRelatedListDef),
//                  AppletPropertyConstants.MAINTAIN_FORM);
                    Entity inst = loadEntity(entityInstId);
                    form = constructForm(inst, FormMode.MAINTAIN, childFkFieldName, false);
                    viewMode = ViewMode.MAINTAIN_RELATEDLIST_FORM;
                }
                    break;
                case NEW_HEADLESSLIST_FORM: {
//                  FormDef formDef = getFormDef(getAppletDef(headlessForm.getCurrentAppletName()),
//                  AppletPropertyConstants.MAINTAIN_FORM);
                    Entity inst = loadEntity(entityInstId);
                    form = constructForm(inst, FormMode.MAINTAIN, null, false);
                    viewMode = ViewMode.MAINTAIN_HEADLESSLIST_FORM;
                }
                    break;
                default:
            }

            if (oldFormContext.isWithReviewErrors()) {
                form.getCtx().copyReviewErrors(oldFormContext);
            }
        }
    }
}

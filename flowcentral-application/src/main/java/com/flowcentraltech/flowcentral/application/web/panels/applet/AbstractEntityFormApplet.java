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

import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.AttachmentsProvider;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleErrorConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.constants.WorkflowDraftType;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.AppletFilterDef;
import com.flowcentraltech.flowcentral.application.data.AssignmentPageDef;
import com.flowcentraltech.flowcentral.application.data.Attachments;
import com.flowcentraltech.flowcentral.application.data.AttachmentsOptions;
import com.flowcentraltech.flowcentral.application.data.Diff;
import com.flowcentraltech.flowcentral.application.data.EditEntityItem;
import com.flowcentraltech.flowcentral.application.data.EntityAttachmentDef;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.data.EntityItem;
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormRelatedListDef;
import com.flowcentraltech.flowcentral.application.data.FormTabDef;
import com.flowcentraltech.flowcentral.application.data.PropertyRuleDef;
import com.flowcentraltech.flowcentral.application.data.UniqueConditionDef;
import com.flowcentraltech.flowcentral.application.data.UniqueConstraintDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.DiffUtils;
import com.flowcentraltech.flowcentral.application.validation.FormContextEvaluator;
import com.flowcentraltech.flowcentral.application.web.controllers.AppletWidgetReferences;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.flowcentraltech.flowcentral.application.web.panels.EditPropertyList;
import com.flowcentraltech.flowcentral.application.web.panels.EntityChild;
import com.flowcentraltech.flowcentral.application.web.panels.EntityFileAttachments;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySaveAs;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySearch;
import com.flowcentraltech.flowcentral.application.web.panels.EntryTablePage;
import com.flowcentraltech.flowcentral.application.web.panels.HeaderWithTabsForm;
import com.flowcentraltech.flowcentral.application.web.panels.HeadlessTabsForm;
import com.flowcentraltech.flowcentral.application.web.panels.ListingForm;
import com.flowcentraltech.flowcentral.application.web.panels.QuickFormEdit;
import com.flowcentraltech.flowcentral.application.web.panels.QuickTableEdit;
import com.flowcentraltech.flowcentral.application.web.panels.QuickTableOrder;
import com.flowcentraltech.flowcentral.application.web.widgets.AbstractTable;
import com.flowcentraltech.flowcentral.application.web.widgets.AssignmentPage;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs.BreadCrumb;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityCRUDPage;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniForm;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniFormScope;
import com.flowcentraltech.flowcentral.application.web.widgets.SectorIcon;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet.TabSheetItem;
import com.flowcentraltech.flowcentral.common.business.FileAttachmentProvider;
import com.flowcentraltech.flowcentral.common.business.policies.ActionMode;
import com.flowcentraltech.flowcentral.common.business.policies.ConsolidatedFormStatePolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.FormReviewContext;
import com.flowcentraltech.flowcentral.common.business.policies.FormValidationContext;
import com.flowcentraltech.flowcentral.common.business.policies.ReviewResult;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.TableActionResult;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.constants.FileAttachmentCategoryType;
import com.flowcentraltech.flowcentral.common.data.AttachmentDetails;
import com.flowcentraltech.flowcentral.common.data.AuditSnapshot;
import com.flowcentraltech.flowcentral.common.data.FormListingOptions;
import com.flowcentraltech.flowcentral.common.data.RowChangeInfo;
import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.flowcentraltech.flowcentral.common.entities.BaseWorkEntity;
import com.flowcentraltech.flowcentral.configuration.constants.AppletType;
import com.flowcentraltech.flowcentral.configuration.constants.AuditEventType;
import com.flowcentraltech.flowcentral.configuration.constants.AuditSourceType;
import com.flowcentraltech.flowcentral.configuration.constants.FormReviewType;
import com.flowcentraltech.flowcentral.configuration.constants.RecordActionType;
import com.tcdng.unify.common.constants.WfItemVersionType;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.common.database.WorkEntity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.FileAttachmentsInfo;
import com.tcdng.unify.core.data.Formats;
import com.tcdng.unify.core.data.IndexedTarget;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.database.Database;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.filter.ObjectFilter;
import com.tcdng.unify.core.system.entities.AbstractSequencedEntity;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.QueryUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.constant.WidgetTempValueConstants;
import com.tcdng.unify.web.ui.widget.Page;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;

/**
 * Abstract entity form applet object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractEntityFormApplet extends AbstractApplet implements SweepingCommitPolicy {

    public enum ViewMode {
        SEARCH,
        HEADLESS_TAB,
        NEW_FORM,
        NEW_PRIMARY_FORM,
        NEW_CHILD_FORM,
        NEW_CHILDLIST_FORM,
        NEW_RELATEDLIST_FORM,
        NEW_HEADLESSLIST_FORM,
        SINGLE_FORM,
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
        ENTRY_TABLE_PAGE,
        ENTITY_CRUD_PAGE,
        PROPERTYLIST_PAGE,
        CUSTOM_PAGE;

        private static final Set<ViewMode> NEW_ENTITY_MODES = EnumSet.of(NEW_FORM, NEW_PRIMARY_FORM, NEW_CHILD_FORM,
                NEW_CHILDLIST_FORM, NEW_RELATEDLIST_FORM, NEW_HEADLESSLIST_FORM);

        private static final Set<ViewMode> MAINTAIN_ENTITY_MODES = EnumSet.of(MAINTAIN_FORM, MAINTAIN_FORM_SCROLL,
                MAINTAIN_PRIMARY_FORM_NO_SCROLL, MAINTAIN_CHILDLIST_FORM, MAINTAIN_CHILDLIST_FORM_NO_SCROLL,
                MAINTAIN_RELATEDLIST_FORM, MAINTAIN_RELATEDLIST_FORM_NO_SCROLL, MAINTAIN_HEADLESSLIST_FORM,
                MAINTAIN_HEADLESSLIST_FORM_NO_SCROLL, SINGLE_FORM);

        private static final Set<ViewMode> ROOT_ENTITY_MODES = EnumSet.of(NEW_FORM, NEW_PRIMARY_FORM, MAINTAIN_FORM,
                MAINTAIN_FORM_SCROLL, MAINTAIN_PRIMARY_FORM_NO_SCROLL, SINGLE_FORM);

        public boolean isCreateForm() {
            return NEW_ENTITY_MODES.contains(this);
        }

        public boolean isMaintainForm() {
            return MAINTAIN_ENTITY_MODES.contains(this);
        }

        public boolean isListingForm() {
            return LISTING_FORM.equals(this);
        }

        public boolean isRootForm() {
            return ROOT_ENTITY_MODES.contains(this);
        }

        public boolean isScroll() {
            return MAINTAIN_FORM_SCROLL.equals(this);
        }

        public boolean isSearch() {
            return SEARCH.equals(this);
        }

        public boolean isPrimary() {
            return NEW_PRIMARY_FORM.equals(this) || MAINTAIN_PRIMARY_FORM_NO_SCROLL.equals(this);
        }

        public boolean isInForm() {
            return isCreateForm() || isMaintainForm();
        }

        public boolean isInAssignment() {
            return ASSIGNMENT_PAGE.equals(this);
        }

        public boolean isSingleForm() {
            return SINGLE_FORM.equals(this);
        }
    };

    protected EntitySearch entitySearch;

    protected AssignmentPage assignmentPage;

    protected EntryTablePage entryTablePage;

    protected EntityCRUDPage entityCrudPage;

    protected EditPropertyList editPropertyList;

    protected EntitySaveAs entitySaveAs;

    protected HeaderWithTabsForm form;

    protected ListingForm listingForm;

    protected HeadlessTabsForm headlessForm;

    protected Stack<FormState> formStack;

    protected FormRelatedListDef currFormRelatedListDef;

    private AppletDef currFormAppletDef;

    private UniqueConstraintDef currUniqueConstraintDef;

    private FormTabDef currFormTabDef;

    private EntityDef currParentEntityDef;

    private Entity currParentInst;

    protected ViewMode viewMode;

    protected EntityFormEventHandlers formEventHandlers;

    protected EntityFileAttachments formFileAttachments;

    protected int mIndex;

    private AppletWidgetReferences appletWidgetReferences;

    private boolean fileAttachmentsDisabled;

    private final boolean collaboration;

    private String assignmentTitle;

    private String assignmentSubTitle;

    private String entryTitle;

    private String propertiesTitle;

    public AbstractEntityFormApplet(Page page, AppletUtilities au, List<String> pathVariables,
            AppletWidgetReferences appletWidgetReferences, EntityFormEventHandlers formEventHandlers)
            throws UnifyException {
        this(page, au, pathVariables, appletWidgetReferences, formEventHandlers, false);
    }

    public AbstractEntityFormApplet(Page page, AppletUtilities au, List<String> pathVariables,
            AppletWidgetReferences appletWidgetReferences, EntityFormEventHandlers formEventHandlers,
            boolean collaboration) throws UnifyException {
        super(page, au, pathVariables.get(APPLET_NAME_INDEX));
        this.appletWidgetReferences = appletWidgetReferences;
        this.formEventHandlers = formEventHandlers;
        this.formFileAttachments = new EntityFileAttachments();
        this.collaboration = collaboration;
        entitySearch = null;
    }

    public boolean isCollaboration() {
        return collaboration;
    }

    public ViewMode getViewMode() {
        return viewMode;
    }

    @Override
    public void reload() throws UnifyException {
        super.reload();
        if (viewMode.isSearch()) {
            entitySearch.applySearchEntriesToSearch();
        } else if (viewMode.isMaintainForm()) {
            Entity _inst = reloadEntity((Entity) form.getFormBean(), false);
            if (_inst != null) {
                form.getCtx().clearValidationErrors();
                updateForm(HeaderWithTabsForm.UpdateType.RELOAD, form, _inst);
            }
        } else if (viewMode.isListingForm()) {
            Entity _inst = (Entity) listingForm.getFormBean();
            _inst = reloadEntity(_inst, true);
            listingForm = constructListingForm(_inst);
        }

        // TODO Other modes?
    }

    @Override
    public boolean navBackToPrevious() throws UnifyException {
        boolean success = true;
        if (restoreForm()) {
            Entity _inst = reloadEntity((Entity) form.getFormBean(), false);
            if (_inst != null) {
                form.getCtx().clearValidationErrors();
                updateForm(HeaderWithTabsForm.UpdateType.NAV_BACK_TO_PREVIOUS, form, _inst);
                reviewFormContext(form.getCtx(), new FormValidationContext(EvaluationMode.REQUIRED),
                        new FormReviewContext(FormReviewType.ON_UPDATE));
            } else {
                success = false;
            }
        } else {
            if (headlessForm != null) {
                navBackToHeadless();
            } else {
                success = navBackToSearch();
            }
        }

        assignmentPage = null;
        entryTablePage = null;
        entityCrudPage = null;
        editPropertyList = null;
        entitySaveAs = null;
        return success;
    }

    public boolean navBackToSearch() throws UnifyException {
        appletCtx().setInWorkflow(false);
        form = null;
        listingForm = null;
        viewMode = ViewMode.SEARCH;
        currParentEntityDef = null;
        currFormTabDef = null;
        currFormRelatedListDef = null;
        formFileAttachments.setFileAttachmentsInfo(null);
        if (entitySearch != null) {
            entitySearch.applySearchEntriesToSearch();
            return true;
        }

        return false;
    }

    public void navBackToHeadless() throws UnifyException {
        appletCtx().setInWorkflow(false);
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

    public void crudSwitchOnChange() throws UnifyException {
        entityCrudPage.switchOnChange();
    }

    public void assignSwitchOnChange(int index) throws UnifyException {
        String focusWidgetId = au().getTriggerWidgetId();
        String trigger = appletWidgetReferences.getAssignmentEntryTableWidget().resolveChildWidgetName(focusWidgetId);
        assignmentPage.switchOnChange(new RowChangeInfo(trigger, index));
    }

    public void entrySwitchOnChange(int index) throws UnifyException {
        String focusWidgetId = au().getTriggerWidgetId();
        String trigger = appletWidgetReferences.getEntryEntryTableWidget().resolveChildWidgetName(focusWidgetId);
        entryTablePage.switchOnChange(new RowChangeInfo(trigger, index));
    }

    public void crudSelectItem(int index) throws UnifyException {
        entityCrudPage.crudSelectItem(index);
    }

    public void saveAsSwitchOnChange() throws UnifyException {
        // TODO
    }

    public TableActionResult previousInst() throws UnifyException {
        if (isPrevNav()) {
            mIndex--;
            return loadScrollInst();
        }

        return null;
    }

    public TableActionResult nextInst() throws UnifyException {
        if (isNextNav()) {
            mIndex++;
            return loadScrollInst();
        }

        return null;
    }

    private TableActionResult loadScrollInst() throws UnifyException {
        TableActionResult result = null;
        if (appletCtx().isReview()) {
            result = maintainInst(mIndex);
        } else {
            if (ViewMode.LISTING_FORM.equals(viewMode)) {
                result = listingInst(mIndex);
            } else {
                result = maintainInst(mIndex);
            }
        }

        return result;
    }

    public TableActionResult newEntityInst() throws UnifyException {
        if (isViewItemsInSeparateTabs()) {
            return openInTab(AppletType.CREATE_ENTITY);
        }

        form = constructNewForm(FormMode.CREATE, null, false);
        viewMode = ViewMode.NEW_FORM;
        return new TableActionResult(null);
    }

    public void newChildItem(int childTabIndex) throws UnifyException {
        newChildItem(childTabIndex, false);
    }

    public void newChildListItem(int childTabIndex) throws UnifyException {
        newChildItem(childTabIndex, true);
    }

    public QuickTableEdit quickTableEdit(int childTabIndex) throws UnifyException {
        if (ensureSaveOnTabAction()) {
            FormTabDef quickEditFormTabDef = form.getFormDef().getFormTabDef(childTabIndex);
            final AppletDef _appletDef = getAppletDef(quickEditFormTabDef.getApplet());
            final String quickEditTable = _appletDef.getPropValue(String.class,
                    AppletPropertyConstants.QUICK_EDIT_TABLE);
            final String quickEditTablePolicy = _appletDef.getPropValue(String.class,
                    AppletPropertyConstants.QUICK_EDIT_POLICY);
            if (StringUtils.isBlank(quickEditTable)) {
                throw new UnifyException(
                        ApplicationModuleErrorConstants.NO_ENTRY_TABLE_CONFIGURED_FOR_APPLET_QUICK_EDIT,
                        _appletDef.getLongName());
            }

            final String baseField = au().getChildFkFieldName(form.getFormDef().getEntityDef(),
                    quickEditFormTabDef.getReference());
            final Object id = ((Entity) form.getFormBean()).getId();
            QuickTableEdit quickTableEdit = constructQuickTableEdit(_appletDef.getEntity(), quickEditTable,
                    quickEditTablePolicy, null, baseField, id);
            final int width = _appletDef.getPropValue(int.class, AppletPropertyConstants.QUICK_EDIT_WIDTH);
            final int height = _appletDef.getPropValue(int.class, AppletPropertyConstants.QUICK_EDIT_HEIGHT);
            quickTableEdit.setWidth(width);
            quickTableEdit.setHeight(height);
            String caption = _appletDef.getLabel() != null
                    ? au().resolveSessionMessage("$m{quickedit.caption.param}", _appletDef.getLabel())
                    : au().resolveSessionMessage("$m{quickedit.caption}");
            quickTableEdit.setEntryCaption(caption);
            quickTableEdit.loadEntryList();
            return quickTableEdit;
        }

        return null;
    }

    public QuickTableOrder quickTableOrder(int childTabIndex) throws UnifyException {
        if (ensureSaveOnTabAction()) {
            FormTabDef quickEditFormTabDef = form.getFormDef().getFormTabDef(childTabIndex);
            final AppletDef _appletDef = getAppletDef(quickEditFormTabDef.getApplet());
            final String quickOrderField = _appletDef.getPropValue(String.class,
                    AppletPropertyConstants.QUICK_ORDER_FIELD);
            if (StringUtils.isBlank(quickOrderField)) {
                throw new UnifyException(
                        ApplicationModuleErrorConstants.NO_ORDER_FIELD_CONFIGURED_FOR_APPLET_QUICK_ORDER,
                        _appletDef.getLongName());
            }

            final String baseField = au().getChildFkFieldName(form.getFormDef().getEntityDef(),
                    quickEditFormTabDef.getReference());
            final Object id = ((Entity) form.getFormBean()).getId();
            QuickTableOrder quickTableOrder = constructQuickTableOrder(_appletDef.getEntity(), _appletDef, baseField,
                    id, quickOrderField);
            String caption = _appletDef.getLabel() != null
                    ? au().resolveSessionMessage("$m{quickorder.caption.param}", _appletDef.getLabel())
                    : au().resolveSessionMessage("$m{quickorder.caption}");
            quickTableOrder.setOrderCaption(caption);
            quickTableOrder.loadOrderItems();
            return quickTableOrder;
        }

        return null;
    }

    public QuickFormEdit quickFormEdit(int childTabIndex) throws UnifyException {
        if (ensureSaveOnTabAction()) {
            FormTabDef quickEditFormTabDef = form.getFormDef().getFormTabDef(childTabIndex);
            final AppletDef _appletDef = getAppletDef(quickEditFormTabDef.getApplet());
            final String quickEditForm = _appletDef.getPropValue(String.class, AppletPropertyConstants.QUICK_EDIT_FORM);
            if (StringUtils.isBlank(quickEditForm)) {
                throw new UnifyException(ApplicationModuleErrorConstants.NO_FORM_CONFIGURED_FOR_APPLET_QUICK_EDIT,
                        _appletDef.getLongName());
            }

            EntityChild entityChild = (EntityChild) form.getTabSheet().getCurrentItem().getValObject();
            QuickFormEdit quickFormEdit = constructQuickFormEdit(_appletDef.getEntity(), quickEditForm, _appletDef,
                    entityChild);
            if (quickFormEdit != null) {
                final int width = _appletDef.getPropValue(int.class, AppletPropertyConstants.QUICK_EDIT_WIDTH);
                final int height = _appletDef.getPropValue(int.class, AppletPropertyConstants.QUICK_EDIT_HEIGHT);
                quickFormEdit.setWidth(width);
                quickFormEdit.setHeight(height);
                String caption = _appletDef.getLabel() != null
                        ? au().resolveSessionMessage("$m{quickedit.caption.param}", _appletDef.getLabel())
                        : au().resolveSessionMessage("$m{quickedit.caption}");
                quickFormEdit.setFormCaption(caption);
                return quickFormEdit;
            }
        }

        return null;
    }

    public ShowPopupInfo newChildShowPopup(int childTabIndex) throws UnifyException {
        FormTabDef _currFormTabDef = form.getFormDef().getFormTabDef(childTabIndex);
        List<AppletFilterDef> filterList = currFormAppletDef != null
                ? currFormAppletDef.getChildListFilterDefs(_currFormTabDef.getApplet())
                : null;
        if (!DataUtils.isBlank(filterList)) {
            final EntityDef entityDef = form.getFormDef().getEntityDef();
            final ValueStoreReader reader = form.getCtx().getFormValueStore().getReader();
            final Date now = au().getNow();
            for (AppletFilterDef filterDef : filterList) {
                if (filterDef.isShowPopupChildListAction()) {
                    ObjectFilter filter = filterDef.getFilterDef().getObjectFilter(entityDef, reader, now);
                    if (filter.matchReader(reader)) {
                        AppletDef _childAppletDef = getAppletDef(_currFormTabDef.getApplet());
                        ShowPopupInfo.Type type = null;
                        String reference = null;
                        if (filterDef.isShowMultiSelectChildListAction()) {
                            type = ShowPopupInfo.Type.SHOW_MULTISELECT;
                            reference = _childAppletDef.getPropValue(String.class,
                                    AppletPropertyConstants.SEARCH_TABLE_MULTISELECT_NEW_REF);
                        } else if (filterDef.isShowTreeMultiSelectChildListAction()) {
                            type = ShowPopupInfo.Type.SHOW_TREEMULTISELECT;
                            reference = _childAppletDef.getPropValue(String.class,
                                    AppletPropertyConstants.SEARCH_TABLE_TREEEMULTISELECT_NEW_GENERATOR);
                        }

                        if (!StringUtils.isBlank(reference)) {
                            return new ShowPopupInfo(type, reference);
                        }

                        break;
                    }
                }
            }
        }

        return null;
    }

    private void newChildItem(int childTabIndex, boolean childList) throws UnifyException {
        if (ensureSaveOnTabAction()) {
            FormTabDef _currFormTabDef = form.getFormDef().getFormTabDef(childTabIndex);
            AppletDef _childAppletDef = getAppletDef(_currFormTabDef.getApplet());
            saveCurrentForm(_currFormTabDef);
            currParentEntityDef = form.getFormDef().getEntityDef();
            currParentInst = ((Entity) form.getFormBean());
            currFormTabDef = _currFormTabDef;
            setCurrFormAppletDef(_childAppletDef);
            String childFkFieldName = au().getChildFkFieldName(currParentEntityDef, _currFormTabDef.getReference());
            form = constructNewForm(FormMode.CREATE, childFkFieldName, true);
            viewMode = childList ? ViewMode.NEW_CHILDLIST_FORM : ViewMode.NEW_CHILD_FORM;
        }
    }

    public void editChildItem(int childTabIndex) throws UnifyException {
        if (ensureSaveOnTabAction()) {
            EntityChild _entityChild = (EntityChild) form.getTabSheet().getCurrentItem().getValObject();
            maintainChildInst(_entityChild.getChildInst(), childTabIndex);
        }
    }

    public void assignToChildItem(int childTabIndex) throws UnifyException {
        if (ensureSaveOnTabAction()) {
            currFormTabDef = form.getFormDef().getFormTabDef(childTabIndex);
            final AppletDef _appletDef = getAppletDef(currFormTabDef.getApplet());
            final FilterGroupDef filterGroupDef = currFormTabDef.getFilterGroupDef();
            final boolean fixedAssignment = _appletDef.getPropValue(boolean.class,
                    AppletPropertyConstants.ASSIGNMENT_FIXED);
            final AssignmentPageDef assignPageDef = getAssignmentPageDef(_appletDef,
                    AppletPropertyConstants.ASSIGNMENT_PAGE);
            final String entryTable = _appletDef.getPropValue(String.class,
                    AppletPropertyConstants.ASSIGNMENT_ENTRY_TABLE);
            final String assnEditPolicy = _appletDef.getPropValue(String.class,
                    AppletPropertyConstants.ASSIGNMENT_ENTRY_TABLE_POLICY);
            final Object id = ((Entity) form.getFormBean()).getId();
            final String subTitle = ((Entity) form.getFormBean()).getDescription();
            saveCurrentForm(currFormTabDef);
            assignmentPage = constructNewAssignmentPage(_appletDef, assignPageDef, entryTable, assnEditPolicy,
                    filterGroupDef, fixedAssignment, id, subTitle);
            assignmentPage.loadAssignedList();
            getAssignmentTitle();
            getAssignmentSubTitle();
            viewMode = ViewMode.ASSIGNMENT_PAGE;
            takeAuditSnapshot(isRootFormUpdateDraft() ? AuditEventType.VIEW_DRAFT : AuditEventType.VIEW);
        }
    }

    public void entryToChildItem(int childTabIndex) throws UnifyException {
        if (ensureSaveOnTabAction()) {
            currFormTabDef = form.getFormDef().getFormTabDef(childTabIndex);
            final AppletDef _appletDef = getAppletDef(currFormTabDef.getApplet());
            final FilterGroupDef filterGroupDef = currFormTabDef.getFilterGroupDef();
            final String entryTable = _appletDef.getPropValue(String.class, AppletPropertyConstants.ENTRY_TABLE);
            final String entryTablePolicy = _appletDef.getPropValue(String.class,
                    AppletPropertyConstants.ENTRY_TABLE_POLICY);
            final String baseField = au().getChildFkFieldName(form.getFormDef().getEntityDef(),
                    currFormTabDef.getReference());
            final Object id = ((Entity) form.getFormBean()).getId();
            final String subTitle = ((Entity) form.getFormBean()).getDescription();
            saveCurrentForm(currFormTabDef);
            entryTablePage = constructNewEntryPage(_appletDef.getEntity(), entryTable, entryTablePolicy, filterGroupDef,
                    baseField, id, subTitle);
            String caption = _appletDef.getLabel() != null ? _appletDef.getLabel().toUpperCase() : null;
            entryTablePage.setEntryCaption(caption);
            entryTablePage.loadEntryList();
            getEntryTitle();
            viewMode = ViewMode.ENTRY_TABLE_PAGE;
        }
    }

    public void crudToChildItem(int childTabIndex) throws UnifyException {
        if (ensureSaveOnTabAction()) {
            currFormTabDef = form.getFormDef().getFormTabDef(childTabIndex);
            final boolean viewOnly = form.matchFormBean(currFormTabDef.getEditViewOnly());
            final boolean allowAddition = form.matchFormBean(currFormTabDef.getEditAllowAddition());
            final boolean fixedRows = form.matchFormBean(currFormTabDef.getEditFixedRows());
            final String baseField = au().getChildFkFieldName(form.getFormDef().getEntityDef(),
                    currFormTabDef.getReference());
            final Object baseId = ((Entity) form.getFormBean()).getId();
            final String subTitle = ((Entity) form.getFormBean()).getDescription();
            saveCurrentForm(currFormTabDef);
            entityCrudPage = constructNewEntityCRUDPage(currFormTabDef.getApplet(), currFormTabDef.getFilterGroupDef(),
                    baseField, baseId, subTitle, currFormTabDef.getReference(), viewOnly, allowAddition, fixedRows);
            entityCrudPage.loadCrudList();
            viewMode = ViewMode.ENTITY_CRUD_PAGE;
        }
    }

    public void newRelatedListItem(String relatedListName) throws UnifyException {
        FormRelatedListDef _currFormRelatedListDef = form.getFormDef().getFormRelatedListDef(relatedListName);
        AppletDef _relAppletDef = getAppletDef(_currFormRelatedListDef);
        saveCurrentForm(null);
        currParentEntityDef = form.getFormDef().getEntityDef();
        currParentInst = ((Entity) form.getFormBean());
        currFormRelatedListDef = _currFormRelatedListDef;
        setCurrFormAppletDef(_relAppletDef);
        String reference = au().getChildFkFieldName(currParentEntityDef.getLongName(), _relAppletDef.getEntity());
        form = constructNewForm(FormMode.CREATE, reference, false);
        viewMode = ViewMode.NEW_RELATEDLIST_FORM;
    }

    public void newHeadlessListItem(String headlessListName) throws UnifyException {
        AppletDef _hdlAppletDef = getAppletDef(headlessListName);
        saveCurrentForm(null);
        setCurrFormAppletDef(_hdlAppletDef);
        form = constructNewForm(FormMode.CREATE, null, false);
        viewMode = ViewMode.NEW_HEADLESSLIST_FORM;
    }

    public void assignToRelatedListItem(String relatedListName) throws UnifyException {
        currFormRelatedListDef = form.getFormDef().getFormRelatedListDef(relatedListName);
        final AppletDef _appletDef = getAppletDef(currFormRelatedListDef.getApplet());
        final FilterGroupDef filterGroupDef = currFormRelatedListDef.getFilterGroupDef();
        final boolean fixedAssignment = _appletDef.getPropValue(boolean.class,
                AppletPropertyConstants.ASSIGNMENT_FIXED);
        final AssignmentPageDef assignPageDef = getAssignmentPageDef(_appletDef,
                AppletPropertyConstants.ASSIGNMENT_PAGE);
        final String entryTable = _appletDef.getPropValue(String.class, AppletPropertyConstants.ASSIGNMENT_ENTRY_TABLE);
        final String assgnEditPolicy = _appletDef.getPropValue(String.class,
                AppletPropertyConstants.ASSIGNMENT_ENTRY_TABLE_POLICY);
        final Object id = ((Entity) form.getFormBean()).getId();
        final String subTitle = ((Entity) form.getFormBean()).getDescription();
        saveCurrentForm(null);
        assignmentPage = constructNewAssignmentPage(_appletDef, assignPageDef, entryTable, assgnEditPolicy,
                filterGroupDef, fixedAssignment, id, subTitle);
        assignmentPage.loadAssignedList();
        viewMode = ViewMode.ASSIGNMENT_PAGE;
    }

    public void prepareItemProperties(int childTabIndex) throws UnifyException {
        if (ensureSaveOnTabAction()) {
            currFormTabDef = form.getFormDef().getFormTabDef(childTabIndex);
            PropertyRuleDef _propertyRuleDef = getPropertyRuleDef(getAppletDef(currFormTabDef.getApplet()),
                    AppletPropertyConstants.PROPERTY_LIST_RULE);
            Entity inst = (Entity) form.getFormBean();
            saveCurrentForm(currFormTabDef);
            String childFkFieldName = au().getChildFkFieldName(form.getFormDef().getEntityDef(),
                    currFormTabDef.getReference());
            editPropertyList = constructNewEditPropertyList(_propertyRuleDef, inst, inst.getDescription(),
                    childFkFieldName);
            editPropertyList.loadPropertyList(form.getCtx());
            getPropertiesTitle();
            viewMode = ViewMode.PROPERTYLIST_PAGE;
        }
    }

    public void prepareSaveEntityAs() throws UnifyException {
        Entity _inst = (Entity) form.getFormBean();
        Entity _saveAsInst = au().environment().find((Class<? extends Entity>) _inst.getClass(), _inst.getId());
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

    public TableActionResult maintainInst(int mIndex) throws UnifyException {
        this.mIndex = mIndex;
        Entity _inst = getEntitySearchItem(entitySearch, mIndex).getEntity();
        if (isViewItemsInSeparateTabs()) {
            return openInTab(AppletType.CREATE_ENTITY, _inst);
        }

        _inst = reloadEntity(_inst, true);
        if (form == null) {
            form = constructForm(_inst, FormMode.MAINTAIN, null, false);
        } else {
            updateForm(HeaderWithTabsForm.UpdateType.MAINTAIN_INST, form, _inst);
        }

        if (isRootForm()) {
            appletCtx().setRootFormUpdateDraft(form.isUpdateDraft());
        }

        viewMode = ViewMode.MAINTAIN_FORM_SCROLL;
        takeAuditSnapshot(form.isUpdateDraft() ? AuditEventType.VIEW_DRAFT : AuditEventType.VIEW);
        return null;
    }

    public TableActionResult listingInst(int mIndex) throws UnifyException {
        this.mIndex = mIndex;
        Entity _inst = getEntitySearchItem(entitySearch, mIndex).getEntity();
        if (isViewItemsInSeparateTabs()) {
            return openListingInTab(_inst);
        }

        _inst = reloadEntity(_inst, true);
        listingForm = constructListingForm(_inst);
        setAltSubCaption(listingForm.getFormTitle());
        viewMode = ViewMode.LISTING_FORM;
        return null;
    }

    public void maintainChildInst(IndexedTarget target) throws UnifyException {
        if (ensureSaveOnTabAction()) {
            EntitySearch _entitySearch = (EntitySearch) (target.isValidTabIndex()
                    ? form.getTabSheet().getTabSheetItem(target.getTabIndex()).getValObject()
                    : form.getTabSheet().getCurrentItem().getValObject());
            Entity _inst = getEntitySearchItem(_entitySearch, target.getValueIndex()).getEntity();
            maintainChildInst(_inst, _entitySearch.getChildTabIndex());
            takeAuditSnapshot(form.isUpdateDraft() ? AuditEventType.VIEW_DRAFT : AuditEventType.VIEW);
        }
    }

    public boolean isViewItemsInSeparateTabs() {
        return entitySearch.isViewItemsInSeparateTabs();
    }

    public void enterWorkflowDraft(WorkflowDraftType type) throws UnifyException {
        final AppletDef _currFormAppletDef = getFormAppletDef();
        EntityActionResult entityActionResult = au().createEntityInstWorkflowDraftByFormContext(_currFormAppletDef,
                form.getCtx(), this);
        takeAuditSnapshot(AuditEventType.CREATE_DRAFT);
        updateForm(HeaderWithTabsForm.UpdateType.UPDATE_INST, form, reloadEntity(entityActionResult.getInst(), false));
    }

    public EntityActionResult submitDeleteToWorkflow() throws UnifyException {
        return submitCurrentInst(ActionMode.DELETE_AND_CLOSE);
    }

    private void maintainChildInst(Entity _inst, int tabIndex) throws UnifyException { 
        FormTabDef _currFormTabDef = form.getFormDef().getFormTabDef(tabIndex);
        AppletDef childAppletDef = getAppletDef(_currFormTabDef.getApplet());
        saveCurrentForm(_currFormTabDef);
        currParentEntityDef = form.getFormDef().getEntityDef();
        currParentInst = ((Entity) form.getFormBean());
        currFormTabDef = _currFormTabDef;
        setCurrFormAppletDef(childAppletDef);
        String childFkFieldName = au().getChildFkFieldName(currParentEntityDef, _currFormTabDef.getReference());
        _inst = reloadEntity(_inst, true);
        form = constructForm(_inst, FormMode.MAINTAIN, childFkFieldName, true);
        viewMode = ViewMode.MAINTAIN_CHILDLIST_FORM_NO_SCROLL;
    }

    public void maintainRelatedInst(IndexedTarget target) throws UnifyException {
        if (ensureSaveOnTabAction()) {
            EntitySearch _entitySearch = (EntitySearch) (target.isValidTabIndex()
                    ? form.getRelatedListTabSheet().getTabSheetItem(target.getTabIndex()).getValObject()
                    : form.getRelatedListTabSheet().getCurrentItem().getValObject());
            Entity _inst = getEntitySearchItem(_entitySearch, target.getValueIndex()).getEntity();
            FormRelatedListDef _formRelatedListDef = form.getFormDef()
                    .getFormRelatedListDef(_entitySearch.getRelatedList());
            AppletDef relAppletDef = getAppletDef(_formRelatedListDef);
            saveCurrentForm(null);
            currParentEntityDef = form.getFormDef().getEntityDef();
            currParentInst = ((Entity) form.getFormBean());
            setCurrFormAppletDef(relAppletDef);
            String childFkFieldName = au().getChildFkFieldName(currParentEntityDef.getLongName(),
                    relAppletDef.getEntity());
            _inst = reloadEntity(_inst, true);
            form = constructForm(_inst, FormMode.MAINTAIN, childFkFieldName, false);
            viewMode = ViewMode.MAINTAIN_RELATEDLIST_FORM_NO_SCROLL;
            takeAuditSnapshot(form.isUpdateDraft() ? AuditEventType.VIEW_DRAFT : AuditEventType.VIEW);
        }
    }

    public void maintainHeadlessInst(int mIndex) throws UnifyException {
        TabSheetItem tabSheetItem = headlessForm.getHeadlessTabSheet().getCurrentItem();
        EntitySearch _entitySearch = (EntitySearch) tabSheetItem.getValObject();
        Entity _inst = getEntitySearchItem(_entitySearch, mIndex).getEntity();
        final AppletDef hdlAppletDef = getAppletDef(tabSheetItem.getAppletName());
        saveCurrentForm(null);
        setCurrFormAppletDef(hdlAppletDef);
        _inst = reloadEntity(_inst, true);
        form = constructForm(_inst, FormMode.MAINTAIN, null, false);
        viewMode = ViewMode.MAINTAIN_HEADLESSLIST_FORM_NO_SCROLL;
        takeAuditSnapshot(form.isUpdateDraft() ? AuditEventType.VIEW_DRAFT : AuditEventType.VIEW);
    }

    public FormContext reviewOnClose() throws UnifyException {
        if (form != null && viewMode.isMaintainForm()) {
            reviewFormContext(form.getCtx(), new FormValidationContext(EvaluationMode.REQUIRED),
                    new FormReviewContext(FormReviewType.ON_CLOSE));
            return form.getCtx();
        }

        return null;
    }

    public Diff diff() throws UnifyException {
        WorkEntity workEntity = (WorkEntity) form.getFormBean();
        if (!QueryUtils.isValidLongCriteria((Long) workEntity.getId())
                || !QueryUtils.isValidLongCriteria(workEntity.getOriginalCopyId())) {
            return null;
        }

        return DiffUtils.diff(au(), form.getFormDef(), (Long) workEntity.getId(), workEntity.getOriginalCopyId(),
                Formats.DEFAULT.createInstance());
    }

    public EntityActionResult saveNewInst() throws UnifyException {
        return saveNewInst(ActionMode.ACTION_ONLY, new FormReviewContext(FormReviewType.ON_SAVE));
    }

    public EntityActionResult saveNewInstAndNext() throws UnifyException {
        return saveNewInst(ActionMode.ACTION_AND_NEXT, new FormReviewContext(FormReviewType.ON_SAVE_NEXT));
    }

    public EntityActionResult saveNewInstAndClose() throws UnifyException {
        return saveNewInst(ActionMode.ACTION_AND_CLOSE, new FormReviewContext(FormReviewType.ON_SAVE_CLOSE));
    }

    public AssignmentPage saveAssign() throws UnifyException {
        assignmentPage.commitAssignedList(true);
        takeAuditSnapshot(isRootFormUpdateDraft() ? AuditEventType.UPDATE_DRAFT : AuditEventType.UPDATE);
        return assignmentPage;
    }

    public AssignmentPage saveAssignOnClose() throws UnifyException {
        assignmentPage.commitAssignedList(false);
        takeAuditSnapshot(isRootFormUpdateDraft() ? AuditEventType.UPDATE_DRAFT : AuditEventType.UPDATE_CLOSE);
        return assignmentPage;
    }

    public void applyUserAction(String actionName) throws UnifyException {

    }

    public EntityActionResult submitInst() throws UnifyException {
        if (appletCtx().isInDetachedWindow() && !getFormAppletDef().getPropValue(boolean.class,
                AppletPropertyConstants.ENTITY_FORM_CLOSE_DETACHED_ONSUBMIT, false)) {
            return submitInstAndNext();
        }

        return submitInst(ActionMode.ACTION_AND_CLOSE, new FormReviewContext(FormReviewType.ON_SUBMIT));
    }

    public EntityActionResult submitInstAndNext() throws UnifyException {
        return submitInst(ActionMode.ACTION_AND_NEXT, new FormReviewContext(FormReviewType.ON_SUBMIT_NEXT));
    }

    public EntityActionResult submitCurrentInst(ActionMode actionMode) throws UnifyException {
        final Entity inst = (Entity) form.getFormBean();
        final AppletDef _currFormAppletDef = getFormAppletDef();
        final EntityDef _entityDef = form.getFormDef().getEntityDef();
        EntityActionResult entityActionResult = null;
        if (isWorkflowCopy()) {
            final WorkEntity workEntity = (WorkEntity) inst;
            final String workflowName = viewMode.isCreateForm() || workEntity.getOriginalCopyId() == null
                    ? ApplicationNameUtils.getWorkflowCopyCreateWorkflowName(_currFormAppletDef.getLongName())
                    : (actionMode.isDelete()
                            ? ApplicationNameUtils.getWorkflowCopyDeleteWorkflowName(_currFormAppletDef.getLongName())
                            : ApplicationNameUtils.getWorkflowCopyUpdateWorkflowName(_currFormAppletDef.getLongName()));
            final String policy = actionMode.isDelete() ? null
                    : (viewMode.isCreateForm()
                            ? _currFormAppletDef.getPropValue(String.class,
                                    AppletPropertyConstants.CREATE_FORM_SUBMIT_POLICY)
                            : _currFormAppletDef.getPropValue(String.class,
                                    AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_POLICY));
            entityActionResult = au().workItemUtilities().submitToWorkflow(_entityDef, workflowName, workEntity,
                    policy);
        } else {
            String channel = _currFormAppletDef.getPropValue(String.class,
                    AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_WORKFLOW_CHANNEL);
            String policy = _currFormAppletDef.getPropValue(String.class,
                    AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_POLICY);
            if (StringUtils.isBlank(channel)) {
                channel = _currFormAppletDef.getPropValue(String.class,
                        AppletPropertyConstants.CREATE_FORM_SUBMIT_WORKFLOW_CHANNEL);
                policy = _currFormAppletDef.getPropValue(String.class,
                        AppletPropertyConstants.CREATE_FORM_SUBMIT_POLICY);
            }

            entityActionResult = au().workItemUtilities().submitToWorkflowChannel(_entityDef, channel,
                    (WorkEntity) inst, policy);
        }

        if (actionMode.isWithNext()) {
            enterNextForm();
        } else {
            if (viewMode == ViewMode.NEW_PRIMARY_FORM) {
                entityActionResult.setClosePage(true);
            } else {
                entityActionResult.setCloseView(true);
            }
        }

        return entityActionResult;
    }

    public EntityActionResult updateInst() throws UnifyException {
        return updateInst(new FormReviewContext(FormReviewType.ON_UPDATE));
    }

    public EntityActionResult updateInstAndClose() throws UnifyException {
        return updateInstAndClose(null);
    }

    public EntityActionResult updateInstAndClose(String actionName) throws UnifyException {
        EntityActionResult entityActionResult = updateInst(
                new FormReviewContext(FormReviewType.ON_UPDATE_CLOSE, actionName));
        setClosePage(entityActionResult);
        return entityActionResult;
    }

    public EntityActionResult deleteInst() throws UnifyException {
        final AppletDef formAppletDef = getFormAppletDef();
        // Review form
        ReviewResult reviewResult = reviewFormContext(form.getCtx(), new FormValidationContext(EvaluationMode.DELETE),
                new FormReviewContext(FormReviewType.ON_DELETE));
        if (reviewResult.isWithMessages()) {
            EntityActionResult entityActionResult = new EntityActionResult();
            entityActionResult.setReviewResult(reviewResult);
            return entityActionResult;
        }

        EntityActionResult entityActionResult = au().deleteEntityInstByFormContext(formAppletDef, form.getCtx(), this);
        takeAuditSnapshot(
                isWorkflowCopy() || form.isUpdateDraft() ? AuditEventType.DELETE_DRAFT : AuditEventType.DELETE);
        final boolean closePage = !navBackToPrevious();
        entityActionResult.setClosePage(closePage);
        entityActionResult.setRefreshMenu(closePage);
        return entityActionResult;
    }

    public EntityActionResult formActionOnInst(String actionPolicyName, String formActionName) throws UnifyException {
        AbstractForm _form = getResolvedForm();
        final FormContext formContext = _form.getCtx();
        Entity _inst = (Entity) _form.getFormBean();
        EntityActionContext efCtx = new EntityActionContext(_form.getFormDef().getEntityDef(), _inst, actionPolicyName);
        efCtx.setAll(formContext);
        if (isListingView()) {
            final String listingGenerator = listingForm.getFormListing().getListingGenerator();
            efCtx.setListingOptions(new FormListingOptions(formActionName));
            efCtx.setListingGenerator(listingGenerator);
            return au().environment().performEntityAction(efCtx);
        }

        EntityActionResult entityActionResult = au().environment().performEntityAction(efCtx);
        if (!entityActionResult.isSkipUpdate()) {
            updateForm(HeaderWithTabsForm.UpdateType.FORMACTION_ON_INST, form, reloadEntity(_inst, false));
            if (viewMode.isCreateForm() && ((Entity) formContext.getInst()).getId() != null) {
                enterMaintainForm(formContext, (Long) ((Entity) formContext.getInst()).getId());
            }
        }

        return entityActionResult;
    }

    public boolean isPrevNav() {
        return mIndex > 0;
    }

    public boolean isNextNav() throws UnifyException {
        return mIndex < (getSearchTable().getDispItemList().size() - 1);
    }

    public String getDisplayItemCounter() throws UnifyException {
        AbstractForm _form = getResolvedForm();
        if (_form.getWarning() != null) {
            _form.setDisplayItemCounterClass("fc-dispcounterorange");
            return _form.getWarning();
        }

        return au().resolveSessionMessage("$m{entityformapplet.displaycounter}", mIndex + 1,
                getSearchTable().getDispItemList().size());
    }

    protected AbstractTable<?, ? extends Entity> getSearchTable() throws UnifyException {
        return entitySearch.getEntityTable();
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

    public EntryTablePage getEntryTablePage() {
        return entryTablePage;
    }

    public EntityCRUDPage getEntityCrudPage() {
        return entityCrudPage;
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

    public <T> T getFormAppletPropValue(Class<T> dataClazz, String name) throws UnifyException {
        return getFormAppletDef().getPropValue(dataClazz, name);
    }

    public <T> T getFormAppletPropValue(Class<T> dataClazz, String name, T defVal) throws UnifyException {
        return getFormAppletDef().getPropValue(dataClazz, name, defVal);
    }

    public boolean isFormAppletProp(String name) throws UnifyException {
        return getFormAppletDef().isProp(name);
    }

    public AbstractForm getForm() {
        return form;
    }

    public ListingForm getListingForm() {
        return listingForm;
    }

    public AbstractForm getResolvedForm() {
        return ViewMode.LISTING_FORM.equals(viewMode) ? listingForm : form;
    }

    public String getFormTitle() {
        return getResolvedForm().getFormTitle();
    }

    public String getBeanTitle() {
        return getResolvedForm().getBeanTitle();
    }

    public String getAssignmentTitle() throws UnifyException {
        return assignmentPage != null ? assignmentTitle = au().resolveSessionMessage(assignmentPage.getMainTitle()) : assignmentTitle;
    }

    public String getAssignmentSubTitle() {
        return assignmentPage != null ? assignmentSubTitle = assignmentPage.getSubTitle() : assignmentSubTitle;
    }

    public String getEntryTitle() {
        return entryTablePage != null ? entryTitle = entryTablePage.getMainTitle() : entryTitle;
    }

    public String getPropertiesTitle() {
        return editPropertyList != null ? propertiesTitle = editPropertyList.getMainTitle() : propertiesTitle;
    }

    public void setFileAttachmentsDisabled(boolean fileAttachmentsDisabled) {
        this.fileAttachmentsDisabled = fileAttachmentsDisabled;
    }

    public EntityFileAttachments getFormFileAttachments() throws UnifyException {
        if (form != null) {
            final Long parentEntityId = (Long) ((Entity) form.getFormBean()).getId();
            EntityDef formEntityDef = form.getFormDef().getEntityDef();
            String parentId = ApplicationEntityUtils.getEntityInstName(formEntityDef.getLongName(), parentEntityId);
            FileAttachmentsInfo.Builder fb = FileAttachmentsInfo.newBuilder(parentId);
            if (getFormAppletDef().getPropValue(boolean.class,
                    AppletPropertyConstants.MAINTAIN_FORM_ATTACHMENTS_ADHOC)) {
                FileAttachmentProvider fileAttachmentProvider = au().getComponent(FileAttachmentProvider.class);
                List<AttachmentDetails> attachmentDetailsList = fileAttachmentProvider.retrieveAllFileAttachments(
                        FileAttachmentCategoryType.FORM_CATEGORY, formEntityDef.getLongName(), parentEntityId);
                for (AttachmentDetails attachmentDetails : attachmentDetailsList) {
                    fb.addFileAttachmentInfo(attachmentDetails.getType(), attachmentDetails.getName(),
                            attachmentDetails.getTitle());
                }

                fb.adhoc(true);
            } else {
                for (EntityAttachmentDef entityAttachmentDef : formEntityDef.getAttachmentList()) {
                    fb.addFileAttachmentInfo(entityAttachmentDef.getType(), entityAttachmentDef.getName(),
                            entityAttachmentDef.getDescription());
                }
            }

            FileAttachmentsInfo fileAttachmentsInfo = fb.build();
            fileAttachmentsInfo.setDisabled(fileAttachmentsDisabled);
            formFileAttachments.setFileAttachmentsInfo(fileAttachmentsInfo);
        }

        return formFileAttachments;
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
        return form != null && (formStack == null || formStack.isEmpty());
    }

    public boolean isNoForm() {
        return form == null;
    }

    public boolean isPromptEnterWorkflowDraft() throws UnifyException {
        return isRootForm() && isWorkflowCopy() && !appletCtx().isInWorkflowPromptViewMode()
                && WfItemVersionType.ORIGINAL.equals(((WorkEntity) form.getFormBean()).getWfItemVersionType())
                && !((WorkEntity) form.getFormBean()).isInWorkflow();
    }

    public boolean matchFormBeanToAppletPropertyCondition(String conditionPropName) throws UnifyException {
        return au().formBeanMatchAppletPropertyCondition(getFormAppletDef(), form, conditionPropName);
    }

    public void ensureCurrentAppletStruct() throws UnifyException {
        AppletDef _currFormAppletDef = getFormAppletDef();
        if (_currFormAppletDef != null) {
            AppletDef _nAppletDef = au().getAppletDef(_currFormAppletDef.getLongName());
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
            FormDef _nFormDef = au().getFormDef(_fFormDef.getLongName());
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
            au().bumpVersion(db, currParentEntityDef, currParentInst);
            for (FormState formState : formStack) {
                au().bumpVersion(db, formState.parentEntityDef, formState.parentInst);
            }

            if (editPropertyList != null) {
                au().bumpVersion(db, form.getFormDef().getEntityDef(), (Entity) form.getFormBean());
            }
        }
    }

    protected abstract AppletDef getAlternateFormAppletDef() throws UnifyException;

    protected AssignmentPage constructNewAssignmentPage(AppletDef _appletDef, AssignmentPageDef assignPageDef,
            String entryTable, String assnEditPolicy, FilterGroupDef filterGroupDef, boolean fixedAssignment, Object id,
            String subTitle) throws UnifyException {
        SectorIcon sectorIcon = getSectorIcon();
        BreadCrumbs breadCrumbs = form.getBreadCrumbs().advance();
        EntityClassDef entityClassDef = getEntityClassDef(assignPageDef.getEntity());
        breadCrumbs.setLastCrumbTitle(au().resolveSessionMessage(entityClassDef.getEntityDef().getDescription()));
        breadCrumbs.setLastCrumbSubTitle(subTitle);
        final String pseudoDeleteField = _appletDef.getPropValue(boolean.class,
                AppletPropertyConstants.ASSIGNMENT_PSEUDO_DELETE) ? _appletDef.getPseudoDeleteField() : null;
        return new AssignmentPage(appletCtx(), formEventHandlers.getAssnSwitchOnChangeHandlers(), this, assignPageDef,
                entityClassDef, id, sectorIcon, breadCrumbs, entryTable, assnEditPolicy, pseudoDeleteField,
                filterGroupDef, fixedAssignment);
    }

    protected QuickTableEdit constructQuickTableEdit(String entity, String entryTable, String entryTablePolicy,
            FilterGroupDef filterGroupDef, String baseField, Object baseId) throws UnifyException {
        EntityClassDef entityClassDef = getEntityClassDef(entity);
        return new QuickTableEdit(appletCtx(), this, entityClassDef, baseField, baseId, entryTable, entryTablePolicy);
    }

    protected QuickTableOrder constructQuickTableOrder(String entity, AppletDef appletDef, String baseField,
            Object baseId, String orderField) throws UnifyException {
        EntityClassDef entityClassDef = getEntityClassDef(entity);
        return new QuickTableOrder(appletCtx(), appletDef, entityClassDef, baseField, baseId, orderField);
    }

    @SuppressWarnings("unchecked")
    protected QuickFormEdit constructQuickFormEdit(String entity, String formName, AppletDef formAppletDef,
            EntityChild entityChild) throws UnifyException {
        Restriction mRestriction = entityChild.getMRestriction();
        if (mRestriction != null) {
            final EntityClassDef entityClassDef = getEntityClassDef(entity);
            final FormDef mFormDef = au().getFormDef(formName);
            final FormContext mCtx = entityChild.getMCtx();
            final Entity childInst = au().environment().listLean(
                    Query.of((Class<? extends Entity>) entityClassDef.getEntityClass()).addRestriction(mRestriction));
            if (childInst != null) {
                FormContext _ctx = new FormContext(mCtx.getAppletContext(), mFormDef, mCtx.getFormEventHandlers(),
                        childInst);
                _ctx.revertTabStates();
                MiniForm miniForm = new MiniForm(MiniFormScope.CHILD_FORM, _ctx, mFormDef.getFormTabDef(0));
                return new QuickFormEdit(appletCtx(), this, formAppletDef, miniForm);
            }
        }

        return null;
    }

    protected EntryTablePage constructNewEntryPage(String entity, String entryTable, String entryTablePolicy,
            FilterGroupDef filterGroupDef, String baseField, Object baseId, String subTitle) throws UnifyException {
        SectorIcon sectorIcon = getSectorIcon();
        BreadCrumbs breadCrumbs = form.getBreadCrumbs().advance();
        EntityClassDef entityClassDef = getEntityClassDef(entity);
        breadCrumbs.setLastCrumbTitle(entityClassDef.getEntityDef().getDescription());
        breadCrumbs.setLastCrumbSubTitle(subTitle);
        return new EntryTablePage(appletCtx(), formEventHandlers.getEntrySwitchOnChangeHandlers(), this, entityClassDef,
                baseField, baseId, sectorIcon, breadCrumbs, entryTable, entryTablePolicy, filterGroupDef);
    }

    protected EntityCRUDPage constructNewEntityCRUDPage(String appletName, FilterGroupDef filterGroupDef,
            String baseField, Object baseId, String subTitle, String childListName, boolean viewOnly,
            boolean allowAddition, boolean fixedRows) throws UnifyException {
        SectorIcon sectorIcon = getSectorIcon();
        BreadCrumbs breadCrumbs = form.getBreadCrumbs().advance();
        breadCrumbs.setLastCrumbTitle(getAppletEntityDef(appletName).getDescription());
        breadCrumbs.setLastCrumbSubTitle(subTitle);
        EntityDef parentEntityDef = form.getEntityDef();
        Entity parentInst = (Entity) form.getCtx().getInst();
        return new EntityCRUDPage(appletCtx(), appletName, formEventHandlers, this, parentEntityDef, parentInst,
                baseField, baseId, childListName, sectorIcon, breadCrumbs, filterGroupDef, viewOnly, allowAddition,
                fixedRows);
    }

    protected EditPropertyList constructNewEditPropertyList(PropertyRuleDef propertyRuleDef, Entity inst,
            String subTitle, String childFkFieldName) throws UnifyException {
        SectorIcon sectorIcon = getSectorIcon();
        BreadCrumbs breadCrumbs = form.getBreadCrumbs().advance();
        breadCrumbs.setLastCrumbTitle(au().resolveSessionMessage("$m{application.propertyitem.table.label}"));
        breadCrumbs.setLastCrumbSubTitle(subTitle);
        return new EditPropertyList(appletCtx(), this, propertyRuleDef, inst, sectorIcon, breadCrumbs,
                childFkFieldName);
    }

    protected EntitySaveAs constructNewEntitySaveAs(Entity inst) throws UnifyException {
        return new EntitySaveAs(au(), this, inst);
    }

    protected HeaderWithTabsForm constructNewForm(FormMode formMode, String childFkFieldName, boolean isChild)
            throws UnifyException {
        final AppletDef _currentFormAppletDef = getFormAppletDef();
        final EntityClassDef entityClassDef = au().getEntityClassDef(_currentFormAppletDef.getEntity());
        final Object inst = ReflectUtils.newInstance(entityClassDef.getEntityClass());
        if (isWorkflowCopy() && (isNoForm() || isRootForm())) {
            ((BaseWorkEntity) inst).setWfItemVersionType(WfItemVersionType.DRAFT);
        }

        String createNewCaption = _currentFormAppletDef != null
                ? _currentFormAppletDef.getPropValue(String.class, AppletPropertyConstants.CREATE_FORM_NEW_CAPTION)
                : null;
        final String beanTitle = !StringUtils.isBlank(createNewCaption) ? createNewCaption
                : au().resolveSessionMessage(formMode.isCreate() ? "$m{form.newentity}" : "$m{form.maintainentity}",
                        au().resolveSessionMessage(entityClassDef.getEntityDef().getDescription()));
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

        String beanTitle = au().getEntityDescription(au().getEntityClassDef(formDef.getEntityDef().getLongName()),
                _inst, null);
        ListingForm listingForm = au().constructListingForm(this, getRootAppletDef().getDescription(), beanTitle,
                formDef, _inst, makeFormBreadCrumbs());
        return listingForm;
    }

    protected ListingForm constructListingForm(FormDef formDef, Entity _inst) throws UnifyException {
        String beanTitle = au().getEntityDescription(au().getEntityClassDef(formDef.getEntityDef().getLongName()),
                _inst, null);
        ListingForm listingForm = au().constructListingForm(this, getRootAppletDef().getDescription(), beanTitle,
                formDef, _inst, makeFormBreadCrumbs());
        return listingForm;
    }

    protected HeaderWithTabsForm constructForm(FormDef formDef, Entity inst, FormMode formMode, String childFkFieldName,
            boolean isChild) throws UnifyException {
        AppletDef _currentFormAppletDef = getFormAppletDef();
        String createNewCaption = _currentFormAppletDef != null
                ? _currentFormAppletDef.getPropValue(String.class, AppletPropertyConstants.CREATE_FORM_NEW_CAPTION)
                : null;
        String beanTitle = au().getEntityDescription(au().getEntityClassDef(formDef.getEntityDef().getLongName()), inst,
                null);
        beanTitle = !StringUtils.isBlank(beanTitle) ? beanTitle
                : !StringUtils.isBlank(createNewCaption) ? createNewCaption
                        : au().resolveSessionMessage(
                                formMode.isCreate() ? "$m{form.newentity}" : "$m{form.maintainentity}",
                                        au().resolveSessionMessage(formDef.getEntityDef().getDescription()));
        return constructForm(formDef, inst, formMode, beanTitle, childFkFieldName, isChild);
    }

    private HeaderWithTabsForm constructForm(FormDef formDef, Entity inst, FormMode formMode, String beanTitle,
            String childFkFieldName, boolean isChild) throws UnifyException {
        final AppletDef _currentFormAppletDef = getFormAppletDef();
        final boolean isReference = !StringUtils.isBlank(childFkFieldName);
        if (formDef == null && formMode.isMaintain()) {
            formDef = form.getCtx().getFormDef();
        }

        if (formMode.isCreate()) {
            if (isReference) {
                if (isChild) {
                    au().populateNewChildReferenceFields(currParentEntityDef, currFormTabDef.getReference(),
                            currParentInst, inst);
                } else { // Related List
                    DataUtils.setBeanProperty(inst, childFkFieldName, currParentInst.getId());
                }
            }
        } else if (formMode.isMaintain()) {
            if (formDef.isWithConsolidatedFormState()) {
                ConsolidatedFormStatePolicy policy = au().getComponent(ConsolidatedFormStatePolicy.class,
                        formDef.getConsolidatedFormState());
                boolean autoUpdated = policy.performAutoUpdates(new BeanValueStore(inst));
                if (autoUpdated) {
                    inst = au().environment().listLean(inst.getClass(), inst.getId());
                }
            }
        }

        final HeaderWithTabsForm form = au().constructHeaderWithTabsForm(this, getRootAppletDef().getDescription(),
                beanTitle, formDef, inst, formMode, makeFormBreadCrumbs(), formEventHandlers);
        setFormProperties(_currentFormAppletDef, form);
        au().onFormConstruct(_currentFormAppletDef, form.getCtx(), childFkFieldName, formMode.isCreate());
        final Long overrideTenantId = au().getOverrideTenantId(currParentEntityDef, currParentInst);
        form.getCtx().getFormValueStore().setTempValue(WidgetTempValueConstants.OVERRIDE_TENANT_ID, overrideTenantId);

        if (formMode.isMaintain()) {
            loadFormAppendables(_currentFormAppletDef, form, inst);
        }

        return form;
    }

    protected void updateForm(HeaderWithTabsForm.UpdateType updateType, HeaderWithTabsForm form, Entity inst)
            throws UnifyException {
        form.getCtx().resetTabIndex();
        form.setUpdateType(updateType);
        au().updateHeaderWithTabsForm(form, inst);

        if (updateType.isMaintain()) {
            final AppletDef _currentFormAppletDef = getFormAppletDef();
            loadFormAppendables(_currentFormAppletDef, form, inst);
        }
    }

    private void loadFormAppendables(AppletDef formAppletDef, HeaderWithTabsForm form, Entity inst)
            throws UnifyException {
        if (!appletCtx().isReview()
                && formAppletDef.getPropValue(boolean.class, AppletPropertyConstants.MAINTAIN_FORM_CAPTURE, false)) {
            String attachmentProviderName = formAppletDef.getPropValue(String.class,
                    AppletPropertyConstants.MAINTAIN_FORM_CAPTURE_ATTACHMENT_PROVIDER);
            if (!StringUtils.isBlank(attachmentProviderName)) {
                AttachmentsProvider attachmentsProvider = au().getComponent(AttachmentsProvider.class,
                        attachmentProviderName);
                Attachments attachments = attachmentsProvider.provide(form.getFormValueStoreReader(),
                        new AttachmentsOptions());
                form.setAppendables(new EditEntityItem(inst, attachments));
            }
        }
    }

    protected void setTabDefAndSaveCurrentForm(int childTabIndex) throws UnifyException {
        currFormTabDef = form.getFormDef().getFormTabDef(childTabIndex);
        saveCurrentForm(currFormTabDef);
    }

    protected void saveCurrentForm(FormTabDef readOnlyFormTabDef) throws UnifyException {
        if (formStack == null) {
            formStack = new Stack<FormState>();
        }

        if (readOnlyFormTabDef != null && form.getCtx().isTabDisabled(readOnlyFormTabDef.getName(),
                readOnlyFormTabDef.isIgnoreParentCondition())) {
            appletCtx().incTabReadOnlyCounter();
        }

        formStack.push(new FormState(currFormAppletDef, form, currFormRelatedListDef, currFormTabDef,
                readOnlyFormTabDef, currParentEntityDef, currUniqueConstraintDef, currParentInst, viewMode));
    }

    protected boolean restoreForm() {
        if (formStack != null && !formStack.isEmpty()) {
            FormState formState = formStack.pop();
            currFormAppletDef = formState.getAppletDef();
            currFormRelatedListDef = formState.getFormRelatedListDef();
            currFormTabDef = formState.getFormTabDef();
            currParentEntityDef = formState.getParentEntityDef();
            currUniqueConstraintDef = formState.getUniqueConstraintDef();
            currParentInst = formState.getParentInst();
            form = formState.getForm();
            viewMode = formState.getMode();

            if (form != null) {
                FormTabDef readOnlyFormTabDef = formState.getReadOnlyFormTabDef();
                if (readOnlyFormTabDef != null && form.getCtx().isTabDisabled(readOnlyFormTabDef.getName(),
                        readOnlyFormTabDef.isIgnoreParentCondition())) {
                    appletCtx().decTabReadOnlyCounter();
                }

                if (isRootForm()) {
                    appletCtx().setInWorkflowPromptViewMode(false);
                }

                return true;
            }
        }

        return false;
    }

    protected void takeAuditSnapshot(AuditEventType auditEventType) throws UnifyException {
        if (isAuditingEnabled()) {
            AuditSnapshot.Builder asb = AuditSnapshot.newBuilder(AuditSourceType.APPLET, auditEventType, au().getNow(),
                    getAppletName());
            UserToken userToken = au().getSessionUserToken();
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
                FormContext fCtx = form.getCtx();
                if (fCtx.isSupportAudit()) {
                    asb.addSnapshot(fCtx.getEntityAudit(), auditEventType);
                }
            } else if (viewMode.isInAssignment()) {
                if (assignmentPage.isSupportAudit()) {
                    asb.addSnapshot(assignmentPage.getEntityAssignmentAudit(), auditEventType);
                }
            }

            AuditSnapshot auditSnapshot = asb.build();
            au().audit().log(auditSnapshot);
        }
    }

    protected AppletDef getAppletDef(String appletName) throws UnifyException {
        return au().getAppletDef(appletName);
    }

    protected AppletDef getAppletDef(FormRelatedListDef formRelatedListDef) throws UnifyException {
        return au().getAppletDef(formRelatedListDef.getApplet());
    }

    protected List<BreadCrumb> getBaseFormBreadCrumbs() {
        return Collections.emptyList();
    }

    protected EntityItem getEntitySearchItem(EntitySearch entitySearch, int index) throws UnifyException {
        Entity entity = entitySearch.getEntityTable().getDispItemList().get(index);
        return new EditEntityItem(entity);
    }

    protected Entity reloadEntity(Entity _inst, boolean maintainAct) throws UnifyException {
        if (maintainAct) {
            // TODO Fire on-maintain-action policy
        }

        if (currUniqueConstraintDef != null && currUniqueConstraintDef.isWithFields()) {
            Query<? extends Entity> query = Query.of((Class<? extends Entity>) _inst.getClass());
            for (String fieldName : currUniqueConstraintDef.getFieldList()) {
                query.addEquals(fieldName, ReflectUtils.getBeanProperty(_inst, fieldName));
            }

            if (currUniqueConstraintDef.isWithConditionList()) {
                for (UniqueConditionDef ucd : currUniqueConstraintDef.getConditionList()) {
                    query.addRestriction(ucd.getRestriction());
                }
            }

            return au().environment().listLean(query);
        }

        return au().environment().listLean((Class<? extends Entity>) _inst.getClass(), _inst.getId());
    }

    public static class ShowPopupInfo {

        public enum Type {
            SHOW_MULTISELECT,
            SHOW_TREEMULTISELECT
        }

        private Type type;

        private String reference;

        private ShowPopupInfo(Type type, String reference) {
            this.type = type;
            this.reference = reference;
        }

        public Type getType() {
            return type;
        }

        public String getReference() {
            return reference;
        }
    }

    protected class FormState {

        private final AppletDef appletDef;

        private final HeaderWithTabsForm form;

        private final FormRelatedListDef formRelatedListDef;

        private final FormTabDef formTabDef;

        private final FormTabDef readOnlyFormTabDef;

        private final EntityDef parentEntityDef;

        private final UniqueConstraintDef uniqueConstraintDef;

        private final Entity parentInst;

        private final ViewMode viewMode;

        public FormState(AppletDef appletDef, HeaderWithTabsForm form, FormRelatedListDef formRelatedListDef,
                FormTabDef formTabDef, FormTabDef readOnlyFormTabDef, EntityDef parentEntityDef,
                UniqueConstraintDef uniqueConstraintDef, Entity parentInst, ViewMode viewMode) {
            this.appletDef = appletDef;
            this.form = form;
            this.formRelatedListDef = formRelatedListDef;
            this.formTabDef = formTabDef;
            this.readOnlyFormTabDef = readOnlyFormTabDef;
            this.parentEntityDef = parentEntityDef;
            this.uniqueConstraintDef = uniqueConstraintDef;
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

        public FormTabDef getReadOnlyFormTabDef() {
            return readOnlyFormTabDef;
        }

        public EntityDef getParentEntityDef() {
            return parentEntityDef;
        }

        public UniqueConstraintDef getUniqueConstraintDef() {
            return uniqueConstraintDef;
        }

        public Entity getParentInst() {
            return this.parentInst;
        }

        public ViewMode getMode() {
            return this.viewMode;
        }

    }

    protected final void setCurrFormAppletDef(AppletDef currFormAppletDef) throws UnifyException {
        this.currFormAppletDef = currFormAppletDef;
        final String ucName = currFormAppletDef != null
                ? currFormAppletDef.getPropValue(String.class,
                        AppletPropertyConstants.SEARCH_TABLE_SELECT_BY_CONSTRAINT)
                : null;
        if (!StringUtils.isBlank(ucName)) {
            this.currUniqueConstraintDef = au().getEntityDef(currFormAppletDef.getEntity()).getUniqueConstraint(ucName);
        } else {
            this.currUniqueConstraintDef = null;
        }
    }

    protected final AppletDef getCurrFormAppletDef() {
        return currFormAppletDef;
    }

    @SuppressWarnings("unchecked")
    protected Entity loadEntity(Object entityInstId) throws UnifyException {
        final AppletDef _currentFormAppletDef = getFormAppletDef();
        final EntityClassDef entityClassDef = au().getEntityClassDef(_currentFormAppletDef.getEntity());
        return au().environment().listLean((Class<? extends Entity>) entityClassDef.getEntityClass(), entityInstId);
    }

    private boolean ensureSaveOnTabAction() throws UnifyException {
        if (!appletCtx().isStudioComponent() && isSaveHeaderFormOnTabAction()) {
            FormContext ctx = getResolvedForm().getCtx();
            if (ctx.isUpdateEnabled() && ctx.getFormDef().isInputForm()) {
                au().formContextEvaluator().evaluateFormContext(ctx,
                        new FormValidationContext(EvaluationMode.UPDATE_TABACTION));
            } else {
                ctx.clearValidationErrors();
            }

            if (ctx.isWithFormErrors()) {
                au().hintUser(MODE.ERROR, "$m{entityformapplet.formvalidation.error.hint}");
                return false;
            }

            if (ctx.isUpdateEnabled()) {
                final AppletDef _currFormAppletDef = getFormAppletDef();
                au().updateEntityInstByFormContext(_currFormAppletDef, form.getCtx(), this);
            }
        }

        return true;
    }

    private SectorIcon getSectorIcon() throws UnifyException {
        AppletDef _rootAppletDef = getRootAppletDef();
        return au().getPageSectorIconByApplication(_rootAppletDef.getApplicationName());
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

    private EntityActionResult saveNewInst(ActionMode actionMode, FormReviewContext rCtx) throws UnifyException {
        final FormContext formContext = form.getCtx();
        EntityActionResult entityActionResult = createInst();
        Long entityInstId = (Long) entityActionResult.getResult();
        takeAuditSnapshot(rCtx.auditEventType());

        // Review form
        ReviewResult reviewResult = reviewFormContext(formContext, new FormValidationContext(EvaluationMode.CREATE),
                rCtx);
        if (rCtx.isOnSave() || formContext.isWithReviewErrors()) {
            enterMaintainForm(formContext, entityInstId);
            entityActionResult.setReviewResult(reviewResult);
            if (rCtx.formClosedOrReplaced()) {
                setClosePage(entityActionResult);
            }
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

    private EntityActionResult submitInst(ActionMode actionMode, FormReviewContext rCtx) throws UnifyException {
        final FormContext formContext = form.getCtx();
        final Entity inst = (Entity) form.getFormBean();
        final EntityDef _entityDef = form.getFormDef().getEntityDef();
        EntityActionResult entityActionResult = null;
        Long entityInstId = (Long) inst.getId();
        if (viewMode.isCreateForm()) {
            final AppletDef _currFormAppletDef = getFormAppletDef();
            String channel = _currFormAppletDef.getPropValue(String.class,
                    AppletPropertyConstants.MAINTAIN_FORM_SUBMIT_WORKFLOW_CHANNEL);
            if (StringUtils.isBlank(channel)) {
                channel = _currFormAppletDef.getPropValue(String.class,
                        AppletPropertyConstants.CREATE_FORM_SUBMIT_WORKFLOW_CHANNEL);
            }
            
            au().workItemUtilities().checkSubmitToWorkflowChannel(channel, (WorkEntity) inst);

            entityActionResult = createInst();
            takeAuditSnapshot(rCtx.auditEventType());
            entityInstId = (Long) entityActionResult.getResult();
            if (_entityDef.delegated()) {
                ((AbstractSequencedEntity) inst).setId(entityInstId);
            }
        }

        ReviewResult reviewResult = reviewFormContext(formContext,
                new FormValidationContext(EvaluationMode.CREATE_SUBMIT), rCtx);
        if (formContext.isWithReviewErrors()) {
            enterMaintainForm(formContext, entityInstId);
            entityActionResult = new EntityActionResult(new EntityActionContext(_entityDef, inst, null));
            entityActionResult.setReviewResult(reviewResult);
            entityActionResult.setSubmitToWorkflow(true);
            entityActionResult.setActionMode(actionMode);
        } else {
            entityActionResult = submitCurrentInst(actionMode);
        }

        return entityActionResult;
    }

    private void setClosePage(EntityActionResult entityActionResult) throws UnifyException {
        if (isRootForm() && getRootAppletDef().getType().isFormInitial()) {
            entityActionResult.setClosePage(true);
        } else {
            entityActionResult.setCloseView(true);
        }
    }

    private EntityActionResult updateInst(FormReviewContext rCtx) throws UnifyException {
        final AppletDef _currFormAppletDef = getFormAppletDef();
        EntityActionResult entityActionResult = au().updateEntityInstByFormContext(_currFormAppletDef, form.getCtx(),
                this);
        takeAuditSnapshot(
                isWorkflowCopy() || form.isUpdateDraft() ? AuditEventType.UPDATE_DRAFT : rCtx.auditEventType());
        updateForm(HeaderWithTabsForm.UpdateType.UPDATE_INST, form, reloadEntity((Entity) form.getFormBean(), false));

        // Review form
        ReviewResult reviewResult = reviewFormContext(form.getCtx(), new FormValidationContext(EvaluationMode.UPDATE),
                rCtx);
        entityActionResult.setReviewResult(reviewResult);
        return entityActionResult;
    }

    private ReviewResult reviewFormContext(FormContext formContext, FormValidationContext vCtx, FormReviewContext rCtx)
            throws UnifyException {
        FormContextEvaluator formContextEvaluator = au().getComponent(FormContextEvaluator.class,
                ApplicationModuleNameConstants.FORMCONTEXT_EVALUATOR);
        return formContextEvaluator.reviewFormContext(formContext, vCtx, rCtx);
    }

    private EntityActionResult createInst() throws UnifyException {
        final AppletDef _appletDef = getFormAppletDef();
        return au().createEntityInstByFormContext(_appletDef, form.getCtx(), this);
    }

    private void enterNextForm() throws UnifyException {
        switch (viewMode) {
            case MAINTAIN_FORM:
                viewMode = ViewMode.NEW_FORM;
                break;
            case MAINTAIN_PRIMARY_FORM_NO_SCROLL:
                viewMode = ViewMode.NEW_PRIMARY_FORM;
                break;
            default:
        }

        if (viewMode == ViewMode.NEW_FORM || viewMode == ViewMode.NEW_HEADLESSLIST_FORM
                || viewMode == ViewMode.NEW_PRIMARY_FORM) {
            form = constructNewForm(FormMode.CREATE, null, false);
        } else if (viewMode == ViewMode.NEW_CHILD_FORM || viewMode == ViewMode.NEW_CHILDLIST_FORM) {
            String childFkFieldName = au().getChildFkFieldName(currParentEntityDef, currFormTabDef.getReference());
            form = constructNewForm(FormMode.CREATE, childFkFieldName, true);
        } else if (viewMode == ViewMode.NEW_RELATEDLIST_FORM) {
            String reference = au().getChildFkFieldName(currParentEntityDef.getLongName(),
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
                    String childFkFieldName = au().getChildFkFieldName(currParentEntityDef,
                            currFormTabDef.getReference());
                    Entity inst = loadEntity(entityInstId);
                    form = constructForm(inst, FormMode.MAINTAIN, childFkFieldName, true);
                    viewMode = ViewMode.MAINTAIN_CHILDLIST_FORM;
                }
                    break;
                case NEW_RELATEDLIST_FORM: {
                    String childFkFieldName = au().getChildFkFieldName(currParentEntityDef.getLongName(),
                            form.getFormDef().getEntityDef().getLongName());
                    Entity inst = loadEntity(entityInstId);
                    form = constructForm(inst, FormMode.MAINTAIN, childFkFieldName, false);
                    viewMode = ViewMode.MAINTAIN_RELATEDLIST_FORM;
                }
                    break;
                case NEW_HEADLESSLIST_FORM: {
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

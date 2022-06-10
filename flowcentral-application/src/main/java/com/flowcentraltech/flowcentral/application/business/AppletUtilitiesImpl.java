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
package com.flowcentraltech.flowcentral.application.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.AppletSetValuesDef;
import com.flowcentraltech.flowcentral.application.data.AssignmentPageDef;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.data.FieldSequenceDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormFieldDef;
import com.flowcentraltech.flowcentral.application.data.FormRelatedListDef;
import com.flowcentraltech.flowcentral.application.data.FormStatePolicyDef;
import com.flowcentraltech.flowcentral.application.data.FormTabDef;
import com.flowcentraltech.flowcentral.application.data.PropertyListItem;
import com.flowcentraltech.flowcentral.application.data.PropertyRuleDef;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.data.SetValuesDef;
import com.flowcentraltech.flowcentral.application.data.TabSheetDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.data.WidgetTypeDef;
import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.application.util.ApplicationCollaborationUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.flowcentraltech.flowcentral.application.web.panels.EntityChild;
import com.flowcentraltech.flowcentral.application.web.panels.EntityFieldSequence;
import com.flowcentraltech.flowcentral.application.web.panels.EntityFilter;
import com.flowcentraltech.flowcentral.application.web.panels.EntityParamValues;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySearch;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySelect;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySetValues;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySingleForm;
import com.flowcentraltech.flowcentral.application.web.panels.HeaderWithTabsForm;
import com.flowcentraltech.flowcentral.application.web.panels.HeadlessTabsForm;
import com.flowcentraltech.flowcentral.application.web.panels.ListingForm;
import com.flowcentraltech.flowcentral.application.web.panels.PropertySearch;
import com.flowcentraltech.flowcentral.application.web.panels.SingleFormBean;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractEntityFormApplet;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractEntitySingleFormApplet;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniForm;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniFormScope;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet.TabSheetItem;
import com.flowcentraltech.flowcentral.common.annotation.BeanBinding;
import com.flowcentraltech.flowcentral.common.business.CollaborationProvider;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.business.SequenceCodeGenerator;
import com.flowcentraltech.flowcentral.common.business.SpecialParamProvider;
import com.flowcentraltech.flowcentral.common.business.policies.ConsolidatedFormStatePolicy;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionContext;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.ParamConfigListProvider;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.constants.CollaborationType;
import com.flowcentraltech.flowcentral.common.constants.OwnershipType;
import com.flowcentraltech.flowcentral.common.data.ParamValuesDef;
import com.flowcentraltech.flowcentral.common.util.RestrictionUtils;
import com.flowcentraltech.flowcentral.configuration.constants.EntityChildCategoryType;
import com.flowcentraltech.flowcentral.configuration.constants.RecordActionType;
import com.flowcentraltech.flowcentral.configuration.constants.RendererType;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.convert.constants.EnumConst;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.criterion.AbstractRestrictionTranslatorMapper;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.criterion.RestrictionTranslator;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.data.MapValues;
import com.tcdng.unify.core.data.ParamConfig;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.filter.ObjectFilter;
import com.tcdng.unify.core.upl.UplComponent;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.font.FontSymbolManager;

/**
 * Applet utilities implementation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(ApplicationModuleNameConstants.APPLET_UTILITIES)
public class AppletUtilitiesImpl extends AbstractUnifyComponent implements AppletUtilities {
//
//    private static final Order ORDER_BY_ID = new Order().add("id");

    @Configurable
    private RestrictionTranslator restrictionTranslator;

    @Configurable
    private SpecialParamProvider specialParamProvider;

    @Configurable
    private SequenceCodeGenerator sequenceCodeGenerator;

    @Configurable
    private EnvironmentService environmentService;

    @Configurable
    private ApplicationModuleService applicationModuleService;

    @Configurable
    private SystemModuleService systemModuleService;

    @Configurable
    private ApplicationWorkItemUtilities applicationWorkItemUtil;

    @Configurable
    private FontSymbolManager fontSymbolManager;

    @Configurable
    private CollaborationProvider collaborationProvider;

    private final FactoryMap<String, Class<? extends SingleFormBean>> singleFormBeanClassByPanelName;

    public AppletUtilitiesImpl() {
        this.singleFormBeanClassByPanelName = new FactoryMap<String, Class<? extends SingleFormBean>>()
            {
                @SuppressWarnings("unchecked")
                @Override
                protected Class<? extends SingleFormBean> create(String panelName, Object... arg1) throws Exception {
                    try {
                        BeanBinding sa = getComponentType(panelName).getAnnotation(BeanBinding.class);
                        return (Class<? extends SingleFormBean>) sa.value();
                    } catch (Exception e) {
                        throw new RuntimeException("Get single form bean class error: panelName = " + panelName, e);
                    }
                }

            };
    }

    public final void setCollaborationProvider(CollaborationProvider collaborationProvider) {
        this.collaborationProvider = collaborationProvider;
    }

    public final void setRestrictionTranslator(RestrictionTranslator restrictionTranslator) {
        this.restrictionTranslator = restrictionTranslator;
    }

    public final void setSpecialParamProvider(SpecialParamProvider specialParamProvider) {
        this.specialParamProvider = specialParamProvider;
    }

    public final void setSequenceCodeGenerator(SequenceCodeGenerator sequenceCodeGenerator) {
        this.sequenceCodeGenerator = sequenceCodeGenerator;
    }

    public final void setEnvironmentService(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    public final void setApplicationModuleService(ApplicationModuleService applicationModuleService) {
        this.applicationModuleService = applicationModuleService;
    }

    public final void setSystemModuleService(SystemModuleService systemModuleService) {
        this.systemModuleService = systemModuleService;
    }

    public final void setApplicationWorkItemUtil(ApplicationWorkItemUtilities applicationWorkItemUtil) {
        this.applicationWorkItemUtil = applicationWorkItemUtil;
    }

    public final void setFontSymbolManager(FontSymbolManager fontSymbolManager) {
        this.fontSymbolManager = fontSymbolManager;
    }

    @Override
    public boolean isEntitySearchWidget(String widgetLongName) throws UnifyException {
        return applicationModuleService.isEntitySearchWidget(widgetLongName);
    }

    @Override
    public <T> T getSysParameterValue(Class<T> clazz, String code) throws UnifyException {
        return systemModuleService.getSysParameterValue(clazz, code);
    }

    @Override
    public SpecialParamProvider getSpecialParamProvider() {
        return specialParamProvider;
    }

    @Override
    public SequenceCodeGenerator getSequenceCodeGenerator() throws UnifyException {
        return sequenceCodeGenerator;
    }

    @Override
    public boolean isApplicationDevelopable(Long applicationId) throws UnifyException {
        return applicationModuleService.isApplicationDevelopable(applicationId);
    }

    @Override
    public String getSessionUserLoginId() throws UnifyException {
        return getSessionContext().getUserToken().getUserLoginId();
    }

    @Override
    public <T> T getSessionAttribute(Class<T> clazz, String attributeName) throws UnifyException {
        return DataUtils.convert(clazz, getSessionAttribute(attributeName));
    }

    @Override
    public Date getNow() throws UnifyException {
        return applicationModuleService.getNow();
    }

    @Override
    public Date getToday() throws UnifyException {
        return applicationModuleService.getToday();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends UnifyComponent> T getComponent(Class<T> componentClazz, String componentName)
            throws UnifyException {
        return (T) getComponent(componentName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends UplComponent> T getUplComponent(Class<T> componentClazz, String descriptor)
            throws UnifyException {
        return (T) getUplComponent(getSessionLocale(), descriptor, true);
    }

    @Override
    public String getSymbolUnicode(String symbolName) throws UnifyException {
        return fontSymbolManager.resolveSymbolUnicode(symbolName);
    }

    @Override
    public String resolveSessionMessage(String message, Object... params) throws UnifyException {
        return super.resolveSessionMessage(message, params);
    }

    @Override
    public Listable getListItemByKey(String listName, String itemKey) throws UnifyException {
        return getListItemByKey(LocaleType.SESSION, listName, itemKey);
    }

    @Override
    public EnvironmentService getEnvironment() {
        return environmentService;
    }

    @Override
    public ApplicationWorkItemUtilities getWorkItemUtilities() {
        return applicationWorkItemUtil;
    }

    @Override
    public String translate(Restriction restriction) throws UnifyException {
        return restrictionTranslator.translate(restriction);
    }

    @Override
    public String translate(Restriction restriction, EntityDef entityDef) throws UnifyException {
        return restrictionTranslator.translate(restriction, new AuRestrictionTranslatorMapper(entityDef));
    }

    @Override
    public AppletDef getAppletDef(String appletName) throws UnifyException {
        return applicationModuleService.getAppletDef(appletName);
    }

    @Override
    public WidgetTypeDef getWidgetTypeDef(String widgetName) throws UnifyException {
        return applicationModuleService.getWidgetTypeDef(widgetName);
    }

    @Override
    public TableDef getTableDef(String tableName) throws UnifyException {
        return applicationModuleService.getTableDef(tableName);
    }

    @Override
    public FormDef getFormDef(String formName) throws UnifyException {
        return applicationModuleService.getFormDef(formName);
    }

    @Override
    public EntityClassDef getEntityClassDef(String entityName) throws UnifyException {
        return applicationModuleService.getEntityClassDef(entityName);
    }

    @Override
    public EntityDef getEntityDef(String entityName) throws UnifyException {
        return applicationModuleService.getEntityDef(entityName);
    }

    @Override
    public RefDef getRefDef(String refName) throws UnifyException {
        return applicationModuleService.getRefDef(refName);
    }

    @Override
    public AssignmentPageDef getAssignmentPageDef(String assignPageName) throws UnifyException {
        return applicationModuleService.getAssignmentPageDef(assignPageName);
    }

    @Override
    public PropertyRuleDef getPropertyRuleDef(String propertyRuleName) throws UnifyException {
        return applicationModuleService.getPropertyRuleDef(propertyRuleName);
    }

    @Override
    public List<PropertyListItem> getPropertyListItems(Entity entityInst, String childFkFieldName,
            PropertyRuleDef propertyRuleDef) throws UnifyException {
        return applicationModuleService.getPropertyListItems(entityInst, childFkFieldName, propertyRuleDef);
    }

    @Override
    public MapValues getPropertyListValues(Entity entityInst, String childFkFieldName, PropertyRuleDef propertyRuleDef)
            throws UnifyException {
        return applicationModuleService.getPropertyListValues(entityInst, childFkFieldName, propertyRuleDef);
    }

    @Override
    public void savePropertyListValues(SweepingCommitPolicy sweepingCommitPolicy, Entity entityInst,
            String childFkFieldName, PropertyRuleDef propertyRuleDef, MapValues values) throws UnifyException {
        applicationModuleService.savePropertyListValues(sweepingCommitPolicy, entityInst, childFkFieldName,
                propertyRuleDef, values);
    }

    @Override
    public FilterDef retrieveFilterDef(String category, String ownerEntityName, Long ownerInstId)
            throws UnifyException {
        return applicationModuleService.retrieveFilterDef(category, ownerEntityName, ownerInstId);
    }

    @Override
    public void saveFilterDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, FilterDef filterDef) throws UnifyException {
        applicationModuleService.saveFilterDef(sweepingCommitPolicy, category, ownerEntityName, ownerInstId, filterDef);
    }

    @Override
    public void saveAppletQuickFilterDef(SweepingCommitPolicy sweepingCommitPolicy, Long appAppletId, String name,
            String description, OwnershipType ownershipType, FilterDef filterDef) throws UnifyException {
        applicationModuleService.saveAppletQuickFilterDef(sweepingCommitPolicy, appAppletId, name, description,
                ownershipType, filterDef);
    }

    @Override
    public FieldSequenceDef retrieveFieldSequenceDef(String category, String ownerEntityName, Long ownerInstId)
            throws UnifyException {
        return applicationModuleService.retrieveFieldSequenceDef(category, ownerEntityName, ownerInstId);
    }

    @Override
    public void saveFieldSequenceDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, FieldSequenceDef fieldSequenceDef) throws UnifyException {
        applicationModuleService.saveFieldSequenceDef(sweepingCommitPolicy, category, ownerEntityName, ownerInstId,
                fieldSequenceDef);
    }

    @Override
    public SetValuesDef retrieveSetValuesDef(String category, String ownerEntityName, Long ownerInstId)
            throws UnifyException {
        return applicationModuleService.retrieveSetValuesDef(category, ownerEntityName, ownerInstId);
    }

    @Override
    public void saveSetValuesDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, SetValuesDef setValuesDef) throws UnifyException {
        applicationModuleService.saveSetValuesDef(sweepingCommitPolicy, category, ownerEntityName, ownerInstId,
                setValuesDef);
    }

    @Override
    public ParamValuesDef retrieveParamValuesDef(String category, String ownerEntityName, Long ownerInstId,
            List<ParamConfig> paramConfigList) throws UnifyException {
        return applicationModuleService.retrieveParamValuesDef(category, ownerEntityName, ownerInstId, paramConfigList);
    }

    @Override
    public void saveParamValuesDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, ParamValuesDef paramValuesDef) throws UnifyException {
        applicationModuleService.saveParamValuesDef(sweepingCommitPolicy, category, ownerEntityName, ownerInstId,
                paramValuesDef);
    }

    @Override
    public ListingForm constructListingForm(AbstractEntityFormApplet applet, String rootTitle, String beanTitle,
            FormDef formDef, Entity inst, BreadCrumbs breadCrumbs) throws UnifyException {
        logDebug("Constructing listing form for bean [{0}] using form definition [{1}]...", beanTitle,
                formDef.getLongName());
        final AppletContext appletContext = applet != null ? applet.getCtx() : new AppletContext(applet, this);
        final FormContext formContext = new FormContext(appletContext, formDef, null, inst);
        final ListingForm form = new ListingForm(formContext, breadCrumbs);
        form.setBeanTitle(beanTitle);
        form.setFormMode(FormMode.LISTING);
        return form;
    }

    @Override
    public HeaderWithTabsForm constructHeaderWithTabsForm(AbstractEntityFormApplet applet, String rootTitle,
            String beanTitle, FormDef formDef, Entity inst, FormMode formMode, BreadCrumbs breadCrumbs,
            EntityFormEventHandlers formEventHandlers) throws UnifyException {
        logDebug("Constructing header with tabs form for bean [{0}] using form definition [{1}]...", beanTitle,
                formDef.getLongName());
        final AppletContext appletContext = applet != null ? applet.getCtx() : new AppletContext(applet, this);
        final SweepingCommitPolicy sweepingCommitPolicy = applet;
        final FormContext formContext = new FormContext(appletContext, formDef, formEventHandlers, inst);
        final HeaderWithTabsForm form = new HeaderWithTabsForm(formContext, breadCrumbs);
        form.setBeanTitle(beanTitle);
        form.setFormMode(formMode);
        if (applet != null && applet.isCollaboration() && applet.isNoForm()) {
            setCollaborationContext(form);
        }

        // Tabs
        final EntityDef entityDef = formDef.getEntityDef();
        final boolean isCreateMode = formMode.isCreate();
        if (formDef.getTabCount() > 1) {
            TabSheetDef.Builder tsdb = TabSheetDef.newBuilder(formContext, 1L);
            List<TabSheetItem> tabSheetItemList = new ArrayList<TabSheetItem>();
            final int len = formDef.getTabCount();
            formContext.evaluateTabStates();
            for (int tabIndex = 1; tabIndex < len; tabIndex++) {
                FormTabDef formTabDef = formDef.getFormTabDef(tabIndex);
                switch (formTabDef.getContentType()) {
                    case MINIFORM:
                    case MINIFORM_CHANGELOG: {
                        logDebug("Constructing mini form for tab [{0}]...", formTabDef.getName());
                        tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "!fc-miniform",
                                RendererType.SIMPLE_WIDGET);
                        tabSheetItemList.add(new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(),
                                new MiniForm(MiniFormScope.CHILD_FORM, formContext, formTabDef), tabIndex,
                                formContext.getFormTab(formTabDef.getName()).isVisible()));
                    }
                        break;
                    case PROPERTY_LIST: {
                        logDebug("Constructing property list tab [{0}] using applet [{1}]...", formTabDef.getName(),
                                formTabDef.getApplet());
                        AppletDef _appletDef = getAppletDef(formTabDef.getApplet());
                        String childFkFieldName = getChildFkFieldName(entityDef, formTabDef.getReference());
                        PropertySearch _propertySearch = constructPropertySearch(formContext, sweepingCommitPolicy,
                                formTabDef.getName(), inst, _appletDef);
                        _propertySearch.setChildTabIndex(tabIndex);
                        _propertySearch.applyEntityToSearch(inst, childFkFieldName);

                        tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "fc-propertylistpanel",
                                RendererType.STANDALONE_PANEL);
                        tabSheetItemList.add(new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(),
                                _propertySearch, tabIndex,
                                !isCreateMode && formContext.getFormTab(formTabDef.getName()).isVisible()));
                    }
                        break;
                    case CHILD: {
                        logDebug("Constructing child tab [{0}] using applet [{1}]...", formTabDef.getName(),
                                formTabDef.getApplet());
                        AppletDef _appletDef = getAppletDef(formTabDef.getApplet());
                        EntityChild _entityChild = constructEntityChild(formContext, sweepingCommitPolicy,
                                formTabDef.getName(), rootTitle, _appletDef);
                        final boolean canCreate = _appletDef.getPropValue(boolean.class,
                                AppletPropertyConstants.SEARCH_TABLE_NEW);
                        _entityChild.setCanCreate(canCreate);
                        Restriction childRestriction = getChildRestriction(entityDef, formTabDef.getReference(), inst);
                        _entityChild.setChildTabIndex(tabIndex);
                        _entityChild.load(formContext, childRestriction);
                        tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "fc-childpanel",
                                RendererType.STANDALONE_PANEL);
                        tabSheetItemList.add(
                                new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(), _entityChild, tabIndex,
                                        !isCreateMode && formContext.getFormTab(formTabDef.getName()).isVisible()));
                    }
                        break;
                    case CHILD_LIST: {
                        logDebug("Constructing child list tab [{0}] using applet [{1}]...", formTabDef.getName(),
                                formTabDef.getApplet());
                        AppletDef _appletDef = getAppletDef(formTabDef.getApplet());
                        final boolean newButtonVisible = !hideAddActionButton(form, applet.getFormAppletDef(),
                                formTabDef.getApplet());
                        final String editAction = formTabDef.getEditAction() == null ? "/assignToChildItem"
                                : formTabDef.getEditAction();
                        EntitySearch _entitySearch = constructEntitySearch(formContext, sweepingCommitPolicy,
                                formTabDef.getName(), rootTitle, _appletDef, editAction, EntitySearch.ENABLE_ALL);
                        _entitySearch.setNewButtonVisible(newButtonVisible);
                        if (_appletDef.isPropWithValue(AppletPropertyConstants.BASE_RESTRICTION)) {
                            _entitySearch.setBaseFilter(_appletDef.getFilterDef(
                                    _appletDef.getPropValue(String.class, AppletPropertyConstants.BASE_RESTRICTION)),
                                    specialParamProvider);
                        }

                        Restriction childRestriction = getChildRestriction(entityDef, formTabDef.getReference(), inst);
                        Restriction tabRestriction = formTabDef.getFilter() != null
                                ? _appletDef.getFilterDef(formTabDef.getFilter()).getRestriction(
                                        _entitySearch.getEntityDef(), specialParamProvider, getNow())
                                : null;
                        childRestriction = RestrictionUtils.and(childRestriction, tabRestriction);

                        _entitySearch.setChildTabIndex(tabIndex);
                        _entitySearch.setRelatedList(formTabDef.getApplet());
//                        _entitySearch.setOrder(ORDER_BY_ID);
                        _entitySearch.setBaseRestriction(childRestriction, specialParamProvider);
                        _entitySearch.applyFilterToSearch();

                        tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "fc-childlistpanel",
                                RendererType.STANDALONE_PANEL);
                        tabSheetItemList.add(
                                new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(), _entitySearch, tabIndex,
                                        !isCreateMode && formContext.getFormTab(formTabDef.getName()).isVisible()));
                    }
                        break;
                    case FILTER_CONDITION: {
                        logDebug("Constructing filter condition tab [{0}] using reference [{1}]...",
                                formTabDef.getName(), formTabDef.getReference());
                        EntityChildCategoryType categoryType = EntityChildCategoryType
                                .fromName(formTabDef.getReference());
                        EntityDef _entityDef = getEntityDef(appletContext.getReference(categoryType));
                        EntityFilter _entityFilter = constructEntityFilter(formContext, sweepingCommitPolicy,
                                formTabDef.getName(), formDef.getEntityDef(), EntityFilter.ENABLE_ALL);
                        _entityFilter.setListType(categoryType.listType());
                        _entityFilter.setParamList(categoryType.paramList());
                        _entityFilter.setCategory(categoryType.category());
                        _entityFilter.setOwnerInstId((Long) inst.getId());
                        _entityFilter.load(_entityDef);
                        tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "fc-entityfilterpanel",
                                RendererType.STANDALONE_PANEL);
                        tabSheetItemList.add(
                                new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(), _entityFilter, tabIndex,
                                        !isCreateMode && formContext.getFormTab(formTabDef.getName()).isVisible()));
                    }
                        break;
                    case PARAM_VALUES: {
                        logDebug("Constructing parameter values tab [{0}] using reference [{1}]...",
                                formTabDef.getName(), formTabDef.getReference());
                        ParamConfigListProvider pclProvider = (ParamConfigListProvider) getComponent(
                                formTabDef.getReference());
                        EntityParamValues _entityParamValues = constructEntityParamValues(formContext,
                                sweepingCommitPolicy, formTabDef.getName(), formDef.getEntityDef(),
                                EntityParamValues.ENABLE_ALL);
                        _entityParamValues.setCategory(pclProvider.getCategory(inst));
                        _entityParamValues.setOwnerInstId((Long) inst.getId());
                        _entityParamValues.load(pclProvider.getParamConfigList(inst));
                        tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "fc-entityparamvaluespanel",
                                RendererType.STANDALONE_PANEL);
                        tabSheetItemList.add(new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(),
                                _entityParamValues, tabIndex,
                                !isCreateMode && formContext.getFormTab(formTabDef.getName()).isVisible()));
                    }
                        break;
                    case FIELD_SEQUENCE: {
                        logDebug("Constructing field sequence tab [{0}] using reference [{1}]...", formTabDef.getName(),
                                formTabDef.getReference());
                        EntityChildCategoryType categoryType = EntityChildCategoryType
                                .fromName(formTabDef.getReference());
                        EntityDef _entityDef = getEntityDef(appletContext.getReference(categoryType));
                        EntityFieldSequence _entityFieldSequence = constructEntityFieldSequence(formContext,
                                sweepingCommitPolicy, formTabDef.getName(), formDef.getEntityDef(),
                                EntityFieldSequence.ENABLE_ALL);
                        _entityFieldSequence.setCategory(categoryType.category());
                        _entityFieldSequence.setOwnerInstId((Long) inst.getId());
                        _entityFieldSequence.load(_entityDef);
                        tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "fc-entityfieldsequencepanel",
                                RendererType.STANDALONE_PANEL);
                        tabSheetItemList.add(new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(),
                                _entityFieldSequence, tabIndex,
                                !isCreateMode && formContext.getFormTab(formTabDef.getName()).isVisible()));
                    }
                        break;
                    case SET_VALUES: {
                        logDebug("Constructing set values tab [{0}] using reference [{1}]...", formTabDef.getName(),
                                formTabDef.getReference());
                        EntityChildCategoryType categoryType = EntityChildCategoryType
                                .fromName(formTabDef.getReference());
                        EntityDef _entityDef = getEntityDef(appletContext.getReference(categoryType));
                        EntitySetValues _entitySetValues = constructEntitySetValues(formContext, sweepingCommitPolicy,
                                formTabDef.getName(), formDef.getEntityDef(), EntitySetValues.ENABLE_ALL);
                        _entitySetValues.setCategory(categoryType.category());
                        _entitySetValues.setOwnerInstId((Long) inst.getId());
                        _entitySetValues.load(_entityDef);
                        tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "fc-entitysetvaluespanel",
                                RendererType.STANDALONE_PANEL);
                        tabSheetItemList.add(new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(),
                                _entitySetValues, tabIndex,
                                !isCreateMode && formContext.getFormTab(formTabDef.getName()).isVisible()));
                    }
                        break;
                    default:
                        break;
                }
            }

            form.setTabSheet(new TabSheet(tsdb.build(), tabSheetItemList));
        }

        // Related lists
        List<FormRelatedListDef> formRelatedListDefList = formDef.getFormRelatedListDefList();
        if (!DataUtils.isBlank(formRelatedListDefList)) {
            TabSheetDef.Builder tsdb = TabSheetDef.newBuilder(null, 1L);
            List<TabSheetItem> tabSheetItemList = new ArrayList<TabSheetItem>();
            final int len = formRelatedListDefList.size();
            for (int i = 0; i < len; i++) {
                FormRelatedListDef formRelatedListDef = formRelatedListDefList.get(i);
                AppletDef _appletDef = getAppletDef(formRelatedListDef.getApplet());
                final String editAction = formRelatedListDef.getEditAction() == null ? "/assignToRelatedItem"
                        : formRelatedListDef.getEditAction();
                EntitySearch _entitySearch = constructEntitySearch(formContext, sweepingCommitPolicy,
                        formRelatedListDef.getName(), rootTitle, _appletDef, editAction, EntitySearch.ENABLE_ALL);
                if (_appletDef.isPropWithValue(AppletPropertyConstants.BASE_RESTRICTION)) {
                    _entitySearch.setBaseFilter(
                            _appletDef.getFilterDef(
                                    _appletDef.getPropValue(String.class, AppletPropertyConstants.BASE_RESTRICTION)),
                            specialParamProvider);
                }

                String fkFieldName = getChildFkFieldName(entityDef.getLongName(), _appletDef.getEntity());
                Restriction baseRestriction = new Equals(fkFieldName, inst.getId());
                _entitySearch.setRelatedList(formRelatedListDef.getName());
                _entitySearch.setBaseRestriction(baseRestriction, specialParamProvider);
                _entitySearch.applyFilterToSearch();

                tsdb.addTabDef(formRelatedListDef.getName(),
                        formRelatedListDef.getLabel() + " (" + _entitySearch.getTotalItemCount() + ")",
                        "fc-relatedlistpanel", RendererType.STANDALONE_PANEL);
                tabSheetItemList.add(new TabSheetItem(formRelatedListDef.getName(), formRelatedListDef.getApplet(),
                        _entitySearch, i, !isCreateMode));
            }

            form.setRelatedListTabSheet(new TabSheet(tsdb.build(), tabSheetItemList, !isCreateMode));
        }

        return form;
    }

    @Override
    public HeadlessTabsForm constructHeadlessTabsForm(AppletContext appletContext,
            SweepingCommitPolicy sweepingCommitPolicy, String rootTitle, AppletDef appletDef) throws UnifyException {
        logDebug("Constructing headerless tabs form for [{0}] using applet definition [{1}]...", rootTitle,
                appletDef.getLongName());
        TabSheetDef.Builder tsdb = TabSheetDef.newBuilder(null, 1L);
        List<TabSheetItem> tabSheetItemList = new ArrayList<TabSheetItem>();
        final List<String> appletList = appletDef.getPropDef(AppletPropertyConstants.HEADLESS_TABS_APPLETS)
                .getValueList();
        final int len = appletList.size();
        final boolean isCreateMode = false;
        for (int i = 0; i < len; i++) {
            final String appletName = appletList.get(i);
            AppletDef _appletDef = getAppletDef(appletName);
            final String editAction = null;
            EntitySearch _entitySearch = constructEntitySearch(new FormContext(appletContext), sweepingCommitPolicy,
                    _appletDef.getName(), rootTitle, _appletDef, editAction, EntitySearch.ENABLE_ALL);
            if (_appletDef.isPropWithValue(AppletPropertyConstants.BASE_RESTRICTION)) {
                _entitySearch.setBaseFilter(
                        _appletDef.getFilterDef(
                                _appletDef.getPropValue(String.class, AppletPropertyConstants.BASE_RESTRICTION)),
                        specialParamProvider);
            }

            _entitySearch.setChildTabIndex(i);
//            _entitySearch.setOrder(ORDER_BY_ID);
            _entitySearch.setHeadlessList(appletName);
            _entitySearch.applyFilterToSearch();

            tsdb.addTabDef(_appletDef.getName(), _appletDef.getLabel(), "fc-headlesslistpanel",
                    RendererType.STANDALONE_PANEL);
            tabSheetItemList.add(new TabSheetItem(_appletDef.getName(), appletName, _entitySearch, i, !isCreateMode));
        }

        return new HeadlessTabsForm(this, new TabSheet(tsdb.build(), tabSheetItemList, !isCreateMode));
    }

    @Override
    public EntitySingleForm constructEntitySingleForm(AbstractEntitySingleFormApplet applet, String rootTitle,
            String beanTitle, Entity inst, FormMode formMode, BreadCrumbs breadCrumbs) throws UnifyException {
        final AppletContext appletContext = applet != null ? applet.getCtx() : new AppletContext(applet, this);
        final FormContext formContext = new FormContext(appletContext, applet.getEntityDef(), inst);
        final String panelName = applet.getRootAppletDef().getPropValue(String.class,
                AppletPropertyConstants.SINGLE_FORM_PANEL);
        logDebug("Constructing entity single form for [{0}] using panel [{1}]...", rootTitle, panelName);
        final SingleFormBean bean = createSingleFormBeanForPanel(panelName);
        bean.init(this);

        final EntitySingleForm form = new EntitySingleForm(formContext, breadCrumbs, panelName, bean);
        form.setBeanTitle(beanTitle);
        form.setFormMode(formMode);
        return form;
    }

    @Override
    public void updateHeaderWithTabsForm(HeaderWithTabsForm form, Entity inst) throws UnifyException {
        final FormDef formDef = form.getFormDef();
        final EntityDef entityDef = formDef.getEntityDef();
        final FormContext formContext = form.getCtx();
        final AbstractEntityFormApplet applet = (AbstractEntityFormApplet) formContext.getAppletContext().getApplet();
        logDebug("Updating header with tabs form for [{0}] using form definition [{1}]...", inst.getId(),
                formDef.getLongName());
        boolean isCreateMode = form.getFormMode().isCreate();
        if (!isCreateMode) {
            String beanTitle = getEntityDescription(getEntityClassDef(entityDef.getLongName()), inst, null);
            form.setBeanTitle(beanTitle);
        }

        FormTabDef _formTabDef = formDef.getFormTabDef(0);
        // Clear unsatisfactory foreign key fields
        if (HeaderWithTabsForm.UpdateType.SWITCH_ON_CHANGE.equals(form.getUpdateType())
                && _formTabDef.isWithCondRefDefFormFields()) {
            clearUnsatisfactoryRefs(_formTabDef, entityDef, form.getCtx().getFormValueStore().getReader(), inst);
        }

        // Populate entity list-only fields
        populateListOnlyFields(entityDef, inst);

        form.setFormBean(inst);
        if (applet.isCollaboration() && applet.isRootForm()) {
            setCollaborationContext(form);
        }

        // Update tabs
        if (form.isWithTabSheet() && formDef.getTabCount() > 1) {
            formContext.evaluateTabStates();
            TabSheet tabSheet = form.getTabSheet();
            for (TabSheetItem tabSheetItem : tabSheet.getTabSheetItemList()) {
                FormTabDef formTabDef = formDef.getFormTabDef(tabSheetItem.getIndex());
                switch (formTabDef.getContentType()) {
                    case MINIFORM:
                    case MINIFORM_CHANGELOG: {
                        tabSheetItem.setVisible(formContext.getFormTab(tabSheetItem.getName()).isVisible());
                    }
                        break;
                    case PROPERTY_LIST: {
                        logDebug("Updating property list tab [{0}] using applet [{1}]...", formTabDef.getName(),
                                formTabDef.getApplet());
                        String childFkFieldName = getChildFkFieldName(entityDef, formTabDef.getReference());
                        PropertySearch _propertySearch = (PropertySearch) tabSheetItem.getValObject();
                        _propertySearch.applyEntityToSearch(inst, childFkFieldName);
                        tabSheetItem.setVisible(
                                !isCreateMode && formContext.getFormTab(tabSheetItem.getName()).isVisible());
                    }
                        break;
                    case CHILD: {
                        logDebug("Updating child tab [{0}] using reference [{1}]...", formTabDef.getName(),
                                formTabDef.getReference());
                        Restriction childRestriction = getChildRestriction(entityDef, formTabDef.getReference(), inst);
                        EntityChild _entityChild = (EntityChild) tabSheetItem.getValObject();
                        _entityChild.load(formContext, childRestriction);
                        tabSheetItem.setVisible(
                                !isCreateMode && formContext.getFormTab(tabSheetItem.getName()).isVisible());
                    }
                        break;
                    case CHILD_LIST: {
                        logDebug("Updating child list tab [{0}] using reference [{1}]...", formTabDef.getName(),
                                formTabDef.getReference());
                        final boolean newButtonVisible = !hideAddActionButton(form, applet.getFormAppletDef(),
                                formTabDef.getApplet());
                        EntitySearch _entitySearch = (EntitySearch) tabSheetItem.getValObject();
                        AppletDef _appletDef = getAppletDef(formTabDef.getApplet());
                        Restriction childRestriction = getChildRestriction(entityDef, formTabDef.getReference(), inst);
                        Restriction tabRestriction = formTabDef.getFilter() != null
                                ? _appletDef.getFilterDef(formTabDef.getFilter()).getRestriction(
                                        _entitySearch.getEntityDef(), specialParamProvider, getNow())
                                : null;
                        childRestriction = RestrictionUtils.and(childRestriction, tabRestriction);

                        _entitySearch.setNewButtonVisible(newButtonVisible);
                        _entitySearch.setBaseRestriction(childRestriction, specialParamProvider);
                        _entitySearch.applyFilterToSearch();
                        tabSheetItem.setVisible(
                                !isCreateMode && formContext.getFormTab(tabSheetItem.getName()).isVisible());
                    }
                        break;
                    case FILTER_CONDITION: {
                        logDebug("Updating filter condition tab [{0}] using reference [{1}]...", formTabDef.getName(),
                                formTabDef.getReference());
                        EntityChildCategoryType categoryType = EntityChildCategoryType
                                .fromName(formTabDef.getReference());
                        EntityDef _entityDef = getEntityDef(formContext.getAppletContext().getReference(categoryType));
                        EntityFilter _entityFilter = (EntityFilter) tabSheetItem.getValObject();
                        _entityFilter.setListType(categoryType.listType());
                        _entityFilter.setParamList(categoryType.paramList());
                        _entityFilter.setCategory(categoryType.category());
                        _entityFilter.setOwnerInstId((Long) inst.getId());
                        _entityFilter.load(_entityDef);
                        tabSheetItem.setVisible(
                                !isCreateMode && formContext.getFormTab(tabSheetItem.getName()).isVisible());
                    }
                        break;
                    case PARAM_VALUES: {
                        logDebug("Updating parameter values tab [{0}] using reference [{1}]...", formTabDef.getName(),
                                formTabDef.getReference());
                        ParamConfigListProvider pclProvider = getComponent(ParamConfigListProvider.class,
                                formTabDef.getReference());
                        EntityParamValues _entityParamValues = (EntityParamValues) tabSheetItem.getValObject();
                        _entityParamValues.setCategory(pclProvider.getCategory(inst));
                        _entityParamValues.setOwnerInstId((Long) inst.getId());
                        _entityParamValues.load(pclProvider.getParamConfigList(inst));
                        tabSheetItem.setVisible(
                                !isCreateMode && formContext.getFormTab(tabSheetItem.getName()).isVisible());
                    }
                        break;
                    case FIELD_SEQUENCE: {
                        logDebug("Updating field sequence tab [{0}] using reference [{1}]...", formTabDef.getName(),
                                formTabDef.getReference());
                        EntityChildCategoryType categoryType = EntityChildCategoryType
                                .fromName(formTabDef.getReference());
                        EntityDef _entityDef = getEntityDef(formContext.getAppletContext().getReference(categoryType));
                        EntityFieldSequence _entityFieldSequence = (EntityFieldSequence) tabSheetItem.getValObject();
                        _entityFieldSequence.setCategory(categoryType.category());
                        _entityFieldSequence.setOwnerInstId((Long) inst.getId());
                        _entityFieldSequence.load(_entityDef);
                        tabSheetItem.setVisible(
                                !isCreateMode && formContext.getFormTab(tabSheetItem.getName()).isVisible());
                    }
                        break;
                    case SET_VALUES: {
                        logDebug("Updating set values tab [{0}] using reference [{1}]...", formTabDef.getName(),
                                formTabDef.getReference());
                        EntityChildCategoryType categoryType = EntityChildCategoryType
                                .fromName(formTabDef.getReference());
                        EntityDef _entityDef = getEntityDef(formContext.getAppletContext().getReference(categoryType));
                        EntitySetValues _entitySetValues = (EntitySetValues) tabSheetItem.getValObject();
                        _entitySetValues.setCategory(categoryType.category());
                        _entitySetValues.setOwnerInstId((Long) inst.getId());
                        _entitySetValues.load(_entityDef);
                        tabSheetItem.setVisible(
                                !isCreateMode && formContext.getFormTab(tabSheetItem.getName()).isVisible());
                    }
                        break;
                    default:
                        break;

                }
            }

            tabSheet.pushTabIndexToActiveItem();
            tabSheet.setActive(true);
        }

        // Update related lists
        if (form.isWithRelatedListTabSheet()) {
            TabSheet tabSheet = form.getRelatedListTabSheet();
            if (!isCreateMode) {
                List<FormRelatedListDef> formRelatedListDefList = formDef.getFormRelatedListDefList();
                if (!DataUtils.isBlank(formRelatedListDefList)) {
                    for (TabSheetItem tabSheetItem : tabSheet.getTabSheetItemList()) {
                        FormRelatedListDef formRelatedListDef = formRelatedListDefList.get(tabSheetItem.getIndex());
                        AppletDef _appletDef = getAppletDef(formRelatedListDef.getApplet());
                        String fkFieldName = getChildFkFieldName(entityDef.getLongName(), _appletDef.getEntity());
                        EntitySearch _entitySearch = (EntitySearch) tabSheetItem.getValObject();
                        _entitySearch.setBaseRestriction(new Equals(fkFieldName, inst.getId()), specialParamProvider);
                        _entitySearch.applyFilterToSearch();

                        form.getRelatedListTabSheet().getTabDef(tabSheetItem.getIndex()).setTabLabel(
                                formRelatedListDef.getLabel() + " (" + _entitySearch.getTotalItemCount() + ")");
                    }
                }
            }

            tabSheet.setActive(!isCreateMode);
        }
    }

    @Override
    public void updateEntitySingleForm(AbstractEntitySingleFormApplet applet, EntitySingleForm form, Entity inst)
            throws UnifyException {
        if (!form.getFormMode().isCreate()) {
            form.setBeanTitle(inst.getDescription());
        }

        form.setFormBean(inst);
    }

    @Override
    public String getChildFkFieldName(String entity, String childEntity) throws UnifyException {
        return getEntityDef(childEntity).getRefEntityFieldDef(entity).getFieldName();
    }

    @Override
    public String getChildFkFieldName(EntityDef parentEntityDef, String childFieldName) throws UnifyException {
        EntityFieldDef childListFieldDef = parentEntityDef.getFieldDef(childFieldName);
        EntityDef _childEntityDef = getEntityDef(getRefDef(childListFieldDef.getReferences()).getEntity());
        EntityFieldDef refEntityFieldDef = _childEntityDef.getRefEntityFieldDef(parentEntityDef.getLongName());
        return refEntityFieldDef != null ? refEntityFieldDef.getFieldName() : null;
    }

    @Override
    public Restriction getChildRestriction(EntityDef parentEntityDef, String childFieldName, Entity parentInst)
            throws UnifyException {
        return applicationModuleService.getChildRestriction(parentEntityDef, childFieldName, parentInst);
    }

    @Override
    public void populateNewChildReferenceFields(EntityDef parentEntityDef, String childFieldName, Entity parentInst,
            Entity childInst) throws UnifyException {
        EntityFieldDef childListFieldDef = parentEntityDef.getFieldDef(childFieldName);
        EntityDef _childEntityDef = getEntityDef(getRefDef(childListFieldDef.getReferences()).getEntity());
        if (childListFieldDef.isWithCategory()) {
            DataUtils.setBeanProperty(childInst, _childEntityDef.getFosterParentIdDef().getFieldName(),
                    parentInst.getId());
            DataUtils.setBeanProperty(childInst, _childEntityDef.getFosterParentTypeDef().getFieldName(),
                    parentEntityDef.getTableName());
            DataUtils.setBeanProperty(childInst, _childEntityDef.getCategoryColumnDef().getFieldName(),
                    childListFieldDef.getCategory());
            return;
        }

        final String fkFieldName = _childEntityDef.getRefEntityFieldDef(parentEntityDef.getLongName()).getFieldName();
        DataUtils.setBeanProperty(childInst, fkFieldName, parentInst.getId());
        copyListOnlyFieldsFromForeignEntity(_childEntityDef, fkFieldName, childInst, parentInst);
    }

    @Override
    public PropertySearch constructPropertySearch(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy,
            String tabName, Entity inst, AppletDef _appletDef) throws UnifyException {
        PropertyRuleDef _propertyRuleDef = getPropertyRuleDef(
                _appletDef.getPropValue(String.class, AppletPropertyConstants.PROPERTY_LIST_RULE));
        int propertySearchMode = PropertySearch.ENABLE_ALL;
        if (!_appletDef.getPropValue(boolean.class, AppletPropertyConstants.PROPERTY_LIST_UPDATE, false)) {
            propertySearchMode = propertySearchMode & ~PropertySearch.SHOW_EDIT_BUTTON;
        }

        PropertySearch _propertySearch = new PropertySearch(ctx, sweepingCommitPolicy, tabName, _propertyRuleDef,
                propertySearchMode);
        _propertySearch.setEntitySubTitle(inst.getDescription());
        return _propertySearch;
    }

    @Override
    public EntitySearch constructEntitySearch(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy,
            String tabName, String rootTitle, AppletDef _appletDef, String editAction, int entitySearchMode)
            throws UnifyException {
        logDebug("Constructing entity search for [{0}] using applet definition [{1}]...", rootTitle,
                _appletDef.getLongName());
        TableDef _tableDef = getTableDef(_appletDef.getPropValue(String.class, AppletPropertyConstants.SEARCH_TABLE));
        if (!_appletDef.getPropValue(boolean.class, AppletPropertyConstants.SEARCH_TABLE_NEW, false)) {
            entitySearchMode = entitySearchMode & ~EntitySearch.SHOW_NEW_BUTTON;
        }

        if (!_appletDef.getPropValue(boolean.class, AppletPropertyConstants.SEARCH_TABLE_EDIT, false)) {
            entitySearchMode = entitySearchMode & ~EntitySearch.SHOW_EDIT_BUTTON;
        }

        if (!_appletDef.getPropValue(boolean.class, AppletPropertyConstants.SEARCH_TABLE_QUICKFILTER, false)) {
            entitySearchMode = entitySearchMode & ~EntitySearch.SHOW_QUICKFILTER;
        }

        if (!_appletDef.getPropValue(boolean.class, AppletPropertyConstants.SEARCH_TABLE_ACTIONFOOTER, false)) {
            entitySearchMode = entitySearchMode & ~EntitySearch.SHOW_ACTIONFOOTER;
        }

        final boolean basicSearchOnly = _appletDef.getPropValue(boolean.class,
                AppletPropertyConstants.SEARCH_TABLE_BASICSEARCHONLY, false);
        EntitySearch _entitySearch = new EntitySearch(ctx, sweepingCommitPolicy, tabName, _tableDef, _appletDef.getId(),
                editAction, entitySearchMode);
        _entitySearch.setPaginationLabel(resolveSessionMessage("$m{entitysearch.display.label}"));
        _entitySearch.setBasicSearchOnly(basicSearchOnly);
        if (_appletDef.isDescriptiveButtons()) {
            String addCaption = _appletDef.getPropValue(String.class, AppletPropertyConstants.SEARCH_TABLE_NEW_CAPTION);
            if (!StringUtils.isBlank(addCaption)) {
                _entitySearch.setEntityNewLabel(resolveSessionMessage(addCaption));
            } else {
                _entitySearch.setEntityNewLabel(
                        resolveSessionMessage("$m{entitysearch.button.new}", _tableDef.getEntityDef().getLabel()));
            }
        }

        _entitySearch.setEntitySubTitle(rootTitle);
        return _entitySearch;
    }

    @Override
    public EntitySelect constructEntitySelect(RefDef refDef, ValueStore valueStore, String filter, int limit)
            throws UnifyException {
        logDebug("Constructing entity select using reference definition [{0}]...", refDef.getLongName());
        TableDef tableDef = applicationModuleService.getTableDef(refDef.getSearchTable());
        EntitySelect entitySelect = new EntitySelect(this, tableDef, refDef.getSearchField(), valueStore,
                refDef.getSelectHandler(), limit);
        entitySelect.setEnableFilter(true);
        String label = tableDef.getEntityDef().getFieldDef(refDef.getSearchField()).getFieldLabel() + ":";
        entitySelect.setLabel(label);
        if (!StringUtils.isBlank(filter)) {
            entitySelect.setFilter(filter);
        }

        Restriction br = null;
        if (refDef.isWithFilterGenerator()) {
            br = ((EntityBasedFilterGenerator) getComponent(refDef.getFilterGenerator()))
                    .generate(valueStore.getReader(), refDef.getFilterGeneratorRule());
        } else {
            EntityClassDef entityClassDef = applicationModuleService.getEntityClassDef(refDef.getEntity());

            br = refDef.isWithFilter()
                    ? refDef.getFilter().getRestriction(entityClassDef.getEntityDef(), null,
                            applicationModuleService.getNow())
                    : null;
        }

        entitySelect.setBaseRestriction(br);
        return entitySelect;
    }

    @Override
    public EntityChild constructEntityChild(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy, String tabName,
            String rootTitle, AppletDef _appletDef) throws UnifyException {
        logDebug("Constructing entity child for [{0}] using applet definition [{1}]...", rootTitle,
                _appletDef.getLongName());
        FormDef childFormDef = getFormDef(_appletDef.getPropValue(String.class, AppletPropertyConstants.MAINTAIN_FORM));
        EntityChild _entityChild = new EntityChild(ctx, sweepingCommitPolicy, tabName, childFormDef);
        _entityChild.setEntitySubTitle(rootTitle);
        return _entityChild;
    }

    @Override
    public EntityFilter constructEntityFilter(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy,
            String tabName, EntityDef ownerEntityDef, int entityFilterMode) throws UnifyException {
        logDebug("Constructing entity filter for [{0}] using entity definition [{1}]...", tabName,
                ownerEntityDef.getLongName());
        return new EntityFilter(ctx, sweepingCommitPolicy, tabName, ownerEntityDef, entityFilterMode);
    }

    @Override
    public EntityFieldSequence constructEntityFieldSequence(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy,
            String tabName, EntityDef ownerEntityDef, int entityFieldSequenceMode) throws UnifyException {
        logDebug("Constructing entity field sequence for [{0}] using entity definition [{1}]...", tabName,
                ownerEntityDef.getLongName());
        return new EntityFieldSequence(ctx, sweepingCommitPolicy, tabName, ownerEntityDef, entityFieldSequenceMode);
    }

    @Override
    public EntitySetValues constructEntitySetValues(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy,
            String tabName, EntityDef ownerEntityDef, int entitySetValuesMode) throws UnifyException {
        logDebug("Constructing entity set values for [{0}] using entity definition [{1}]...", tabName,
                ownerEntityDef.getLongName());
        return new EntitySetValues(ctx, sweepingCommitPolicy, tabName, ownerEntityDef, entitySetValuesMode);
    }

    @Override
    public EntityParamValues constructEntityParamValues(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy,
            String tabName, EntityDef ownerEntityDef, int entityParamValuesMode) throws UnifyException {
        logDebug("Constructing entity parameter values for [{0}] using entity definition [{1}]...", tabName,
                ownerEntityDef.getLongName());
        return new EntityParamValues(ctx, sweepingCommitPolicy, tabName, ownerEntityDef, entityParamValuesMode);
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    private boolean hideAddActionButton(HeaderWithTabsForm form, AppletDef _currFormAppletDef, String childAppletName)
            throws UnifyException {
        List<FilterDef> filterList = _currFormAppletDef.getChildListFilterDefs(childAppletName);
        if (!filterList.isEmpty()) {
            EntityDef entityDef = form.getFormDef().getEntityDef();
            ValueStore formValueStore = form.getCtx().getFormValueStore();
            SpecialParamProvider specialParamProvider = form.getCtx().getAppletContext().getSpecialParamProvider();
            Date now = getNow();
            for (FilterDef filterDef : filterList) {
                if (filterDef.isHideAddWidgetChildListAction()) {
                    ObjectFilter filter = filterDef.getObjectFilter(entityDef, specialParamProvider, now);
                    if (filter.match(formValueStore)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private void clearUnsatisfactoryRefs(FormTabDef _formTabDef, EntityDef entityDef, ValueStoreReader _reader,
            Entity inst) throws UnifyException {
        int depth = 4;
        boolean change = true;
        while ((--depth >= 0) && change) {
            change = false;
            for (FormFieldDef _formFieldDef : _formTabDef.getCondRefDefFormFieldDefList()) {
                Long currVal = DataUtils.getBeanProperty(Long.class, inst, _formFieldDef.getFieldName());
                if (currVal != null) {
                    RefDef refDef = _formFieldDef.getInputRefDef();
                    EntityClassDef _entityClassDef = applicationModuleService.getEntityClassDef(refDef.getEntity());
                    Restriction br = null;
                    if (refDef.isWithFilterGenerator()) {
                        br = ((EntityBasedFilterGenerator) getComponent(refDef.getFilterGenerator())).generate(_reader,
                                refDef.getFilterGeneratorRule());
                    } else {
                        br = refDef.isWithFilter() ? refDef.getFilter().getRestriction(_entityClassDef.getEntityDef(),
                                getSpecialParamProvider(), getNow()) : null;
                    }

                    Query<? extends Entity> query = br != null
                            ? Query.ofDefaultingToAnd((Class<? extends Entity>) _entityClassDef.getEntityClass(), br)
                            : Query.of((Class<? extends Entity>) _entityClassDef.getEntityClass());
                    query.addEquals("id", currVal);
                    if (environmentService.countAll(query) == 0) {
                        DataUtils.setBeanProperty(inst, _formFieldDef.getFieldName(), null);
                        change = true;
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void populateListOnlyFields(EntityDef entityDef, Entity inst) throws UnifyException {
        for (EntityFieldDef entityFieldDef : entityDef.getFkFieldDefList()) {
            final String fkFieldName = entityFieldDef.getFieldName();
            if (entityFieldDef.isEnumDataType()) {
                String key = DataUtils.getBeanProperty(String.class, inst, fkFieldName);
                if (key == null) {
                    clearListOnlyFields(entityDef, fkFieldName, inst);
                } else {
                    Listable listable = getListItemByKey(entityFieldDef.getReferences(), key);
                    for (EntityFieldDef _entityFieldDef : entityDef.getListOnlyFieldDefList()) {
                        if (_entityFieldDef.getKey().equals(fkFieldName)) {
                            String val = "description".equals(_entityFieldDef.getProperty())
                                    ? listable.getListDescription()
                                    : listable.getListKey();
                            DataUtils.setBeanProperty(inst, _entityFieldDef.getFieldName(), val);
                        }
                    }
                }
            } else {
                Long id = DataUtils.getBeanProperty(Long.class, inst, fkFieldName);
                if (id == null) {
                    clearListOnlyFields(entityDef, fkFieldName, inst);
                } else {
                    EntityClassDef _parentEntityClassDef = getEntityClassDef(entityFieldDef.getRefDef().getEntity());
                    Entity parentInst = environmentService
                            .listLean((Class<? extends Entity>) _parentEntityClassDef.getEntityClass(), id);
                    copyListOnlyFieldsFromForeignEntity(entityDef, fkFieldName, inst, parentInst);
                }
            }
        }
    }

    @Override
    public String getEntityDescription(EntityClassDef entityClassDef, Entity inst, String fieldName)
            throws UnifyException {
        return applicationModuleService.getEntityDescription(entityClassDef, inst, fieldName);
    }

    @Override
    public EntityActionResult createEntityInstByFormContext(final AppletDef formAppletDef,
            final FormContext formContext, final SweepingCommitPolicy scp) throws UnifyException {
        final Entity inst = (Entity) formContext.getInst();
        final FormDef _formDef = formContext.getFormDef();
        final EntityDef _entityDef = _formDef.getEntityDef();
        String createPolicy = formAppletDef != null
                ? formAppletDef.getPropValue(String.class, AppletPropertyConstants.CREATE_FORM_NEW_POLICY)
                : formContext.getAttribute(String.class, AppletPropertyConstants.CREATE_FORM_NEW_POLICY);
        EntityActionContext eCtx = new EntityActionContext(_entityDef, inst, RecordActionType.CREATE, scp,
                createPolicy);
        eCtx.setAll(formContext);

        // Populate values for auto-format fields
        final SequenceCodeGenerator gen = getSequenceCodeGenerator();
        if (_entityDef.isWithAutoFormatFields()) {
            ValueStoreReader valueStoreReader = new ValueStoreReader(inst);
            for (EntityFieldDef entityFieldDef : _entityDef.getAutoFormatFieldDefList()) {
                if (entityFieldDef.isStringAutoFormat()) {
                    String skeleton = gen.getCodeSkeleton(entityFieldDef.getAutoFormat());
                    if (skeleton.equals(DataUtils.getBeanProperty(String.class, inst, entityFieldDef.getFieldName()))) {
                        DataUtils.setBeanProperty(inst, entityFieldDef.getFieldName(), gen.getNextSequenceCode(
                                _entityDef.getLongName(), entityFieldDef.getAutoFormat(), valueStoreReader));
                    }
                }
            }
        }

        // Apply delayed set values here since set values can depend on auto-format
        // fields
        applyDelayedSetValues(formContext);

        EntityActionResult entityActionResult;
        try {
            entityActionResult = getEnvironment().create(eCtx);
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

    @Override
    public EntityActionResult updateEntityInstByFormContext(AppletDef formAppletDef, FormContext formContext,
            SweepingCommitPolicy scp) throws UnifyException {
        Entity inst = (Entity) formContext.getInst();
        String updatePolicy = formAppletDef != null
                ? formAppletDef.getPropValue(String.class, AppletPropertyConstants.MAINTAIN_FORM_UPDATE_POLICY)
                : formContext.getAttribute(String.class, AppletPropertyConstants.MAINTAIN_FORM_UPDATE_POLICY);
        EntityActionContext eCtx = new EntityActionContext(formContext.getFormDef().getEntityDef(), inst,
                RecordActionType.UPDATE, scp, updatePolicy);
        eCtx.setAll(formContext);

        applyDelayedSetValues(formContext);

        EntityActionResult entityActionResult = getEnvironment().updateLean(eCtx);
        formContext.addValidationErrors(eCtx.getFormMessages());
        return entityActionResult;
    }

    @Override
    public EntityActionResult deleteEntityInstByFormContext(AppletDef formAppletDef, FormContext formContext,
            SweepingCommitPolicy scp) throws UnifyException {
        Entity inst = (Entity) formContext.getInst();
        final EntityDef _entityDef = formContext.getFormDef().getEntityDef();
        boolean pseudoDelete = formAppletDef.getPropValue(boolean.class,
                AppletPropertyConstants.MAINTAIN_FORM_DELETE_PSEUDO, false);
        EntityActionResult entityActionResult = null;
        String deletePolicy = formAppletDef != null
                ? formAppletDef.getPropValue(String.class, AppletPropertyConstants.MAINTAIN_FORM_DELETE_POLICY)
                : formContext.getAttribute(String.class, AppletPropertyConstants.MAINTAIN_FORM_DELETE_POLICY);
        if (pseudoDelete) {
            String pseudoDeleteSetValuesName = formAppletDef.getPropValue(String.class,
                    AppletPropertyConstants.MAINTAIN_FORM_DELETE_PSEUDO_SETVALUES);
            if (!StringUtils.isBlank(pseudoDeleteSetValuesName)) {
                AppletSetValuesDef appletSetValuesDef = formAppletDef.getSetValues(pseudoDeleteSetValuesName);
                appletSetValuesDef.getSetValuesDef().apply(this, _entityDef, getNow(), inst, Collections.emptyMap(),
                        null);
            }

            EntityActionContext eCtx = new EntityActionContext(_entityDef, inst, RecordActionType.UPDATE, scp,
                    deletePolicy);
            eCtx.setAll(formContext);
            entityActionResult = getEnvironment().updateLean(eCtx);
        } else {
            EntityActionContext eCtx = new EntityActionContext(_entityDef, inst, RecordActionType.DELETE, scp,
                    deletePolicy);
            eCtx.setAll(formContext);

            entityActionResult = getEnvironment().delete(eCtx);
        }

        return entityActionResult;
    }

    @Override
    public void onFormConstruct(AppletDef formAppletDef, FormContext formContext, String baseField, boolean create)
            throws UnifyException {
        final ValueStore formValueStore = formContext.getFormValueStore();
        final FormDef formDef = formContext.getFormDef();
        formContext.setFixedReference(baseField);
        logDebug("Executing on-form-construct [{0}] using applet [{1}] and base field [{2}]...", formDef.getLongName(),
                formAppletDef.getLongName(), baseField);

        final Map<String, Object> variables = Collections.emptyMap();
        final Entity inst = (Entity) formContext.getInst();
        if (create) {
            // Apply create state policy
            String onCreateStatePolicy = formAppletDef.getPropValue(String.class,
                    AppletPropertyConstants.CREATE_FORM_STATE_POLICY);
            if (!StringUtils.isBlank(onCreateStatePolicy)) {
                logDebug("Applying on-create state policy [{0}] on form [{1}]...", onCreateStatePolicy, inst.getId());
                FormStatePolicyDef onCreateFormStatePolicyDef = formDef
                        .getOnCreateFormStatePolicyDef(onCreateStatePolicy);
                if (onCreateFormStatePolicyDef.isSetValues()) {
                    onCreateFormStatePolicyDef.getSetValuesDef().apply(this, formDef.getEntityDef(), getNow(),
                            formValueStore, variables, null);
                }
            }

            // Populate skeleton for auto-format fields
            EntityDef _entityDef = formDef.getEntityDef();
            if (_entityDef.isWithAutoFormatFields()) {
                logDebug("Populating auto-format fields for form [{0}]...", inst.getId());
                final SequenceCodeGenerator gen = getSequenceCodeGenerator();
                for (EntityFieldDef entityFieldDef : _entityDef.getAutoFormatFieldDefList()) {
                    if (entityFieldDef.isStringAutoFormat()) {
                        DataUtils.setBeanProperty(inst, entityFieldDef.getFieldName(),
                                gen.getCodeSkeleton(entityFieldDef.getAutoFormat()));
                    }
                }
            }
        }

        if (formDef.isWithConsolidatedFormState()) {
            ConsolidatedFormStatePolicy policy = getComponent(ConsolidatedFormStatePolicy.class,
                    formDef.getConsolidatedFormState());
            logDebug("Applying consolidated form state policy [{0}] on form [{1}]...", policy.getName(), inst.getId());
            policy.onFormConstruct(formValueStore);
        }

        // Fire on form construct value generators
        logDebug("Populating on-form-construct set values for form [{0}]...", inst.getId());
        for (FormStatePolicyDef formStatePolicyDef : formDef.getOnFormConstructSetValuesFormStatePolicyDefList()) {
            if (formStatePolicyDef.isSetValues()) {
                formStatePolicyDef.getSetValuesDef().apply(this, formDef.getEntityDef(), getNow(), formValueStore,
                        variables, null);
            }
        }
    }

    private void applyDelayedSetValues(final FormContext formContext) throws UnifyException {
        final FormDef _formDef = formContext.getFormDef();
        final ValueStore _formValueStore = formContext.getFormValueStore();
        final Date now = getNow();
        logDebug("Applying delayed set values on [{0}] using form definition [{1}] ...", _formValueStore.retrieve("id"),
                _formDef.getLongName());
        // Execute delayed set values
        final Map<String, Object> variables = Collections.emptyMap();
        for (FormStatePolicyDef formStatePolicyDef : _formDef.getOnDelayedSetValuesFormStatePolicyDefList()) {
            ObjectFilter objectFilter = formStatePolicyDef.isWithCondition()
                    ? formStatePolicyDef.getOnCondition().getObjectFilter(_formDef.getEntityDef(),
                            getSpecialParamProvider(), now)
                    : null;
            if (objectFilter == null || objectFilter.match(_formValueStore)) {
                if (formStatePolicyDef.isSetValues()) {
                    formStatePolicyDef.getSetValuesDef().apply(this, _formDef.getEntityDef(), now, _formValueStore,
                            variables, null);
                }
            }
        }
    }

    private SingleFormBean createSingleFormBeanForPanel(String panelName) throws UnifyException {
        Class<? extends SingleFormBean> beanClass = singleFormBeanClassByPanelName.get(panelName);
        return ReflectUtils.newInstance(beanClass);
    }

    private void clearListOnlyFields(EntityDef entityDef, String fkFieldName, Entity inst) throws UnifyException {
        for (EntityFieldDef entityFieldDef : entityDef.getListOnlyFieldDefList()) {
            if (entityFieldDef.getKey().equals(fkFieldName)) {
                DataUtils.setBeanProperty(inst, entityFieldDef.getFieldName(), null);
            }
        }
    }

    private void copyListOnlyFieldsFromForeignEntity(EntityDef entityDef, String fkFieldName, Entity inst,
            Entity foreignInst) throws UnifyException {
        for (EntityFieldDef entityFieldDef : entityDef.getListOnlyFieldDefList()) {
            if (entityFieldDef.getKey().equals(fkFieldName)) {
                DataUtils.setBeanProperty(inst, entityFieldDef.getFieldName(),
                        DataUtils.getBeanProperty(Object.class, foreignInst, entityFieldDef.getProperty()));
            }
        }
    }

    private void setCollaborationContext(HeaderWithTabsForm form) throws UnifyException {
        boolean enterReadOnlyMode = false;
        if (collaborationProvider != null && collaborationProvider.isLicensedForCollaboration()) {
            final BaseApplicationEntity _appInst = (BaseApplicationEntity) ((AbstractForm) form).getCtx().getInst();
            final CollaborationType type = ApplicationCollaborationUtils.getCollaborationType(_appInst.getClass());
            if (type != null) {
                final String resourceName = ApplicationNameUtils
                        .getApplicationEntityLongName(_appInst.getApplicationName(), _appInst.getName());
                enterReadOnlyMode = !collaborationProvider.isLockedBy(type, resourceName,
                        getUserToken().getUserLoginId());
            }
        }

        form.getCtx().getAppletContext().setReadOnly(enterReadOnlyMode);
    }

    private class AuRestrictionTranslatorMapper extends AbstractRestrictionTranslatorMapper {

        private EntityDef entityDef;

        public AuRestrictionTranslatorMapper(EntityDef entityDef) {
            this.entityDef = entityDef;
        }

        @Override
        public String getLabelTranslation(String fieldName) throws UnifyException {
            return entityDef.getFieldLabelMap().get(fieldName);
        }

        @Override
        public String getValueTranslation(String fieldName, Object val) throws UnifyException {
            EntityFieldDef entityFieldDef = entityDef.getFieldDef(fieldName);
            switch (entityFieldDef.getDataType()) {
                case ENUM:
                case ENUM_REF:
                    String key = val instanceof String ? (String) val : null;
                    if (key == null) {
                        key = val instanceof EnumConst ? ((EnumConst) val).code() : null;
                    }

                    if (key != null) {
                        return getListItemByKey(LocaleType.SESSION, entityFieldDef.getReferences(), key)
                                .getListDescription();
                    }
                    break;
                case REF:
                case REF_UNLINKABLE:
                    RefDef refDef = applicationModuleService.getRefDef(entityFieldDef.getReferences());
                    val = val instanceof String ? Long.valueOf((String) val) : val;
                    return val != null ? applicationModuleService.getEntityDescription(
                            applicationModuleService.getEntityClassDef(refDef.getEntity()), (Long) val,
                            refDef.getSearchField()) : "";
                case LIST_ONLY:
                case BLOB:
                case BOOLEAN:
                case CHAR:
                case CHILD:
                case CHILD_LIST:
                case REF_FILEUPLOAD:
                case CLOB:
                case DATE:
                case DECIMAL:
                case DOUBLE:
                case FLOAT:
                case INTEGER:
                case LONG:
                case SCRATCH:
                case SHORT:
                case STRING:
                case TIMESTAMP:
                case TIMESTAMP_UTC:
                default:
                    break;

            }

            return null;
        }

    }
}

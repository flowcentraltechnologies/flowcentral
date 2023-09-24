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
package com.flowcentraltech.flowcentral.application.business;

import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.AppletWorkflowCopyInfo;
import com.flowcentraltech.flowcentral.application.data.ApplicationDef;
import com.flowcentraltech.flowcentral.application.data.ApplicationMenuDef;
import com.flowcentraltech.flowcentral.application.data.AssignmentPageDef;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntitySchema;
import com.flowcentraltech.flowcentral.application.data.DelegateEntityInfo;
import com.flowcentraltech.flowcentral.application.data.FieldSequenceDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.PropertyListDef;
import com.flowcentraltech.flowcentral.application.data.PropertyListItem;
import com.flowcentraltech.flowcentral.application.data.PropertyRuleDef;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.data.SearchInputsDef;
import com.flowcentraltech.flowcentral.application.data.SetValuesDef;
import com.flowcentraltech.flowcentral.application.data.SuggestionTypeDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.data.WidgetRulesDef;
import com.flowcentraltech.flowcentral.application.data.WidgetTypeDef;
import com.flowcentraltech.flowcentral.application.entities.AppApplet;
import com.flowcentraltech.flowcentral.application.entities.AppAppletAlert;
import com.flowcentraltech.flowcentral.application.entities.AppAppletFilter;
import com.flowcentraltech.flowcentral.application.entities.AppAppletFilterQuery;
import com.flowcentraltech.flowcentral.application.entities.AppAppletQuery;
import com.flowcentraltech.flowcentral.application.entities.AppAppletSetValues;
import com.flowcentraltech.flowcentral.application.entities.AppAssignmentPage;
import com.flowcentraltech.flowcentral.application.entities.AppAssignmentPageQuery;
import com.flowcentraltech.flowcentral.application.entities.AppEntity;
import com.flowcentraltech.flowcentral.application.entities.AppEntityField;
import com.flowcentraltech.flowcentral.application.entities.AppEntityFieldQuery;
import com.flowcentraltech.flowcentral.application.entities.AppEntityQuery;
import com.flowcentraltech.flowcentral.application.entities.AppEntityUpload;
import com.flowcentraltech.flowcentral.application.entities.AppEntityUploadQuery;
import com.flowcentraltech.flowcentral.application.entities.AppForm;
import com.flowcentraltech.flowcentral.application.entities.AppFormAnnotation;
import com.flowcentraltech.flowcentral.application.entities.AppFormAnnotationQuery;
import com.flowcentraltech.flowcentral.application.entities.AppFormElement;
import com.flowcentraltech.flowcentral.application.entities.AppFormFilter;
import com.flowcentraltech.flowcentral.application.entities.AppFormQuery;
import com.flowcentraltech.flowcentral.application.entities.AppPropertyList;
import com.flowcentraltech.flowcentral.application.entities.AppPropertyRule;
import com.flowcentraltech.flowcentral.application.entities.AppRef;
import com.flowcentraltech.flowcentral.application.entities.AppRefQuery;
import com.flowcentraltech.flowcentral.application.entities.AppSuggestionType;
import com.flowcentraltech.flowcentral.application.entities.AppTable;
import com.flowcentraltech.flowcentral.application.entities.AppTableAction;
import com.flowcentraltech.flowcentral.application.entities.AppTableActionQuery;
import com.flowcentraltech.flowcentral.application.entities.AppTableQuery;
import com.flowcentraltech.flowcentral.application.entities.AppWidgetType;
import com.flowcentraltech.flowcentral.application.entities.AppWidgetTypeQuery;
import com.flowcentraltech.flowcentral.application.entities.Application;
import com.flowcentraltech.flowcentral.application.entities.ApplicationQuery;
import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.common.business.FlowCentralService;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.constants.OwnershipType;
import com.flowcentraltech.flowcentral.common.data.ParamValuesDef;
import com.flowcentraltech.flowcentral.common.entities.EntityWrapper;
import com.flowcentraltech.flowcentral.configuration.constants.EntityBaseType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.FormElementType;
import com.flowcentraltech.flowcentral.system.entities.Module;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.data.MapValues;
import com.tcdng.unify.core.data.ParamConfig;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.data.ValueStoreWriter;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.database.dynamic.DynamicEntityInfo;

/**
 * Application service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface ApplicationModuleService extends FlowCentralService {

    /**
     * Update entity schema.
     * 
     * @param entitySchema
     *                     the entity schema
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean updateEntitySchema(EntitySchema entitySchema) throws UnifyException;

    /**
     * Creates a wrapper instance initialized with a new instance of wrapped entity
     * type.
     * 
     * @param wrapperType
     *                    the wrapper type
     * @return the wrapper instance
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends EntityWrapper> T wrapperOf(Class<T> wrapperType) throws UnifyException;

    /**
     * Creates a wrapper instance initialized with the supplied value store.
     * 
     * @param wrapperType
     *                    the wrapper type
     * @param valueStore
     *                    the value store
     * @return the wrapper instance
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends EntityWrapper> T wrapperOf(Class<T> wrapperType, ValueStore valueStore) throws UnifyException;

    /**
     * Creates a wrapper instance initialized with the supplied value store reader.
     * 
     * @param wrapperType
     *                         the wrapper type
     * @param valueStoreReader
     *                         the value store reader
     * @return the wrapper instance
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends EntityWrapper> T wrapperOf(Class<T> wrapperType, ValueStoreReader valueStoreReader)
            throws UnifyException;

    /**
     * Creates a wrapper instance initialized with the supplied value store writer.
     * 
     * @param wrapperType
     *                         the wrapper type
     * @param valueStoreWriter
     *                         the value store writer
     * @return the wrapper instance
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends EntityWrapper> T wrapperOf(Class<T> wrapperType, ValueStoreWriter valueStoreWriter)
            throws UnifyException;

    /**
     * Creates a wrapper instance initialized with the entity instance found by
     * supplied ID.
     * 
     * @param wrapperType
     *                    the wrapper type
     * @param id
     *                    the id
     * @return the wrapper instance
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends EntityWrapper> T wrapperOf(Class<T> wrapperType, Long Id) throws UnifyException;

    /**
     * Creates a wrapper instance initialized with the supplied entity instance.
     * 
     * @param wrapperType
     *                    the wrapper type
     * @param inst
     *                    the entity inst
     * @return the wrapper instance
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends EntityWrapper> T wrapperOf(Class<T> wrapperType, Entity inst) throws UnifyException;

    /**
     * Creates a wrapper instance initialized with the entity list found by supplied
     * query and list all method.
     * 
     * @param wrapperType
     *                    the wrapper type
     * @param query
     *                    the query
     * @return the wrapper instance
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends EntityWrapper> T wrapperOf(Class<T> wrapperType, Query<? extends Entity> query) throws UnifyException;

    /**
     * Creates a wrapper instance initialized with the supplied entity instance
     * list.
     * 
     * @param wrapperType
     *                    the wrapper type
     * @param instList
     *                    the entity inst list
     * @return the wrapper instance
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends EntityWrapper> T wrapperOf(Class<T> wrapperType, List<? extends Entity> instList) throws UnifyException;

    /**
     * Creates query of wrapper entity type.
     * 
     * @param entityName
     *                    the entity name
     * @return the query object
     * @throws UnifyException
     *                        if an error occurs
     */
    Query<? extends Entity> queryOf(String entityName) throws UnifyException;

    /**
     * Creates query of wrapper entity type.
     * 
     * @param wrapperType
     *                    the wrapper type
     * @return the query object
     * @throws UnifyException
     *                        if an error occurs
     */
    Query<? extends Entity> queryOf(Class<? extends EntityWrapper> wrapperType) throws UnifyException;

    /**
     * Gets filter group definition for applet.
     * 
     * @param appletName
     *                   the applet name
     * @param tabFilter
     *                   the tab filter
     * @return the filter group definition
     * @throws UnifyException
     *                        if an error occurs
     */
    FilterGroupDef getFilterGroupDef(String appletName, String tabFilter) throws UnifyException;

    /**
     * Checks if widget type is entity search widget.
     * 
     * @param widgetLongName
     *                       the widget long name
     * @return true if entity search widget otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean isEntitySearchWidget(String widgetLongName) throws UnifyException;

    /**
     * Checks if application is developable.
     * 
     * @param applicationId
     *                      the application ID
     * @return true if application is developable
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean isApplicationDevelopable(Long applicationId) throws UnifyException;

    /**
     * Checks if application is developable.
     * 
     * @param applicationName
     *                        the application name
     * @return true if application is developable
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean isApplicationDevelopable(String applicationName) throws UnifyException;

    /**
     * Gets an application entity entity.
     * 
     * @param appEntityId
     *                    the application entity ID
     * @return the entity name
     * @throws UnifyException
     *                        if an error occurs
     */
    String getAppEntity(Long appEntityId) throws UnifyException;

    /**
     * Finds application entity uploads.
     * 
     * @param query
     *              the entity upload query
     * @return list of application entity uploads
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppEntityUpload> findAppEntityUploads(AppEntityUploadQuery query) throws UnifyException;

    /**
     * Gets application entity fields for base type.
     * 
     * @param type
     *                   the base type
     * @param configType
     *                   the custom status
     * @return the entity filed list
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppEntityField> getEntityBaseTypeFieldList(EntityBaseType type, ConfigType configType) throws UnifyException;

    /**
     * Creates an application entity reference record.
     * 
     * @param appRef
     *               the application reference record
     * @return the created record ID
     * @throws UnifyException
     *                        if an error occurs
     */
    Long createAppRef(AppRef appRef) throws UnifyException;

    /**
     * Creates an application.
     * 
     * @param application
     *                    the application to create
     * @param module
     *                    optional module
     * @return the record ID of created application
     * @throws UnifyException
     *                        if application with same properties exists. If an
     *                        error occurs
     */
    Long createApplication(Application application, Module module) throws UnifyException;

    /**
     * Gets names of all applications in module.
     * 
     * @param moduleName
     *                   the module name
     * @return the application names
     * @throws UnifyException
     *                        if an error occurs
     */
    List<String> getApplicationNames(String moduleName) throws UnifyException;
    
    /**
     * Finds applications based on query.
     * 
     * @param query
     *              the search query
     * @return list of applications
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Application> findApplications(ApplicationQuery query) throws UnifyException;

    /**
     * Finds an application record by name.
     * 
     * @param applicationName
     *                        the application name
     * @return the application
     * @throws UnifyException
     *                        if an error occurs
     */
    Application findApplication(String applicationName) throws UnifyException;

    /**
     * Finds manage list applets.
     * 
     * @param entity
     *               the application entity
     * @return list of manage entity list applets
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppApplet> findManageEntityListApplets(String entity) throws UnifyException;

    /**
     * Finds create entity applets.
     * 
     * @param entity
     *               the application entity
     * @return list of create entity applets
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppApplet> findCreateEntityApplets(String entity) throws UnifyException;

    /**
     * Finds application entity unique constraints.
     * 
     * @param entity
     *               the application entity
     * @return list of entity unique constraints
     * @throws UnifyException
     *                        if an error occurs
     */
    List<? extends Listable> findAppEntityUniqueConstraints(String entity) throws UnifyException;

    /**
     * Finds a list of applets.
     * 
     * @param query
     *              the application applet query
     * @return list of applets
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppApplet> findAppApplets(AppAppletQuery query) throws UnifyException;

    /**
     * Finds related application applets for specified application form
     * 
     * @param formId
     *               the application form ID
     * @return list of application form related applets
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppApplet> findFormRelatedAppApplets(Long formId) throws UnifyException;

    /**
     * Finds an applet by ID.
     * 
     * @param appletId
     *                 the applet ID
     * @return the application applet record
     * @throws UnifyException
     *                        if applet with ID is not found. If an error occurs
     */
    AppApplet findAppApplet(Long appletId) throws UnifyException;

    /**
     * Checks if an applet is with workflow copy.
     * 
     * @param appletName
     *                   the applet name
     * @return true if applet is with workflow copy otherwise false.
     * @throws UnifyException
     *                        if an error occurs.
     */
    boolean isAppletWithWorkflowCopy(String appletName) throws UnifyException;

    /**
     * Gets applet workflow copy information.
     * 
     * @param appletName
     *                   the applet name
     * @return the applet workflow copy information
     * @throws UnifyException
     *                        if an error occurs.
     */
    AppletWorkflowCopyInfo getAppletWorkflowCopyInfo(String appletName) throws UnifyException;

    /**
     * Gets application name.
     * 
     * @param applicationId
     *                      the application ID
     * @return the application name
     * @throws UnifyException
     *                        if an error occurs
     */
    String getApplicationName(Long applicationId) throws UnifyException;

    /**
     * Gets application ID.
     * 
     * @param applicationName
     *                      the application name
     * @return the application ID
     * @throws UnifyException
     *                        if an error occurs
     */
    Long getApplicationId(String applicationName) throws UnifyException;

    /**
     * Gets an application applet entity.
     * 
     * @param appAppletId
     *                    the application applet ID
     * @return the entity name
     * @throws UnifyException
     *                        if an error occurs
     */
    String getAppAppletEntity(Long appAppletId) throws UnifyException;

    /**
     * Gets an application applet entity.
     * 
     * @param appletName
     *                    the applet name
     * @return the entity name
     * @throws UnifyException
     *                        if an error occurs
     */
    String getAppAppletEntity(String appletName) throws UnifyException;

    /**
     * Gets an application table entity.
     * 
     * @param appTableId
     *                   the application table ID
     * @return the entity name
     * @throws UnifyException
     *                        if an error occurs
     */
    String getAppTableEntity(Long appTableId) throws UnifyException;

    /**
     * Gets an application component entity.
     * 
     * @param componentEntityName
     *                            the component entity name
     * @param appComponentId
     *                            the application component ID
     * @return the entity name
     * @throws UnifyException
     *                        if an error occurs
     */
    String getAppComponentEntity(String componentEntityName, Long appComponentId) throws UnifyException;

    /**
     * Finds application applet set values.
     * 
     * @param appAppletId
     *                    the application applet ID
     * @return list of applet set values
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppAppletSetValues> findAppAppletSetValues(Long appAppletId) throws UnifyException;

    /**
     * Finds application applet alerts.
     * 
     * @param appAppletId
     *                    the application applet ID
     * @return list of applet alerts
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppAppletAlert> findAppAppletAlerts(Long appAppletId) throws UnifyException;

    /**
     * Finds application applet search inputs.
     * 
     * @param appAppletId
     *                    the application applet ID
     * @return list of applet search inputs
     * @throws UnifyException
     *                        if an error occurs
     */
    List<? extends Listable> findAppAppletSearchInputsListable(Long appAppletId) throws UnifyException;

    /**
     * Finds application applet filters.
     * 
     * @param appAppletId
     *                    the application applet ID
     * @return list of applet filters
     * @throws UnifyException
     *                        if an error occurs
     */
    List<? extends Listable> findAppAppletFiltersListable(Long appAppletId) throws UnifyException;

    /**
     * Finds application applet quick filters.
     * 
     * @param appAppletId
     *                    the application applet ID
     * @return list of applet filters
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppAppletFilter> findAppAppletQuickFilters(Long appAppletId) throws UnifyException;

    /**
     * Finds application applet filters.
     * 
     * @param appAppletName
     *                      the application applet name
     * @return list of applet filters
     * @throws UnifyException
     *                        if an error occurs
     */
    List<? extends Listable> findAppAppletFiltersListable(String appAppletName) throws UnifyException;

    /**
     * Finds application applet filters.
     * 
     * @param query
     *              the application applet query
     * @return list of applet filters
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppAppletFilter> findAppAppletFilters(AppAppletFilterQuery query) throws UnifyException;

    /**
     * Finds a reference by ID.
     * 
     * @param refId
     *              the reference ID
     * @return the application reference record
     * @throws UnifyException
     *                        if reference with ID is not found. If an error occurs
     */
    AppRef findAppRef(Long refId) throws UnifyException;

    /**
     * Finds application entities based on query.
     * 
     * @param query
     *              the search query
     * @return list of application entities
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppEntity> findAppEntities(AppEntityQuery query) throws UnifyException;

    /**
     * Finds a entity by ID.
     * 
     * @param entityId
     *                 the entity ID
     * @return the application entity record
     * @throws UnifyException
     *                        if entity with ID is not found. If an error occurs
     */
    AppEntity findAppEntity(Long entityId) throws UnifyException;

    /**
     * Finds application entity fields based on query.
     * 
     * @param query
     *              the search query
     * @return list of application entity fields
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppEntityField> findAppEntityFields(AppEntityFieldQuery query) throws UnifyException;

    /**
     * Finds application entity field based on query.
     * 
     * @param query
     *              the search query
     * @return the application entity field
     * @throws UnifyException
     *                        if an error occurs
     */
    AppEntityField findAppEntityField(AppEntityFieldQuery query) throws UnifyException;

    /**
     * Finds entity search inputs.
     * 
     * @param entity
     *               the entity name
     * @return list of entity search inputs
     * @throws UnifyException
     *                        if an error occurs
     */
    List<? extends Listable> findAppEntitySearchInputs(String entity) throws UnifyException;

    /**
     * Finds a table by ID.
     * 
     * @param tableId
     *                the table ID
     * @return the application table record
     * @throws UnifyException
     *                        if table with ID is not found. If an error occurs
     */
    AppTable findAppTable(Long tableId) throws UnifyException;

    /**
     * Finds all application tables associated with entity.
     * 
     * @param entity
     *               the entity
     * @return list of application tables
     * @throws UnifyException
     *                        if an error occurs
     */
    List<String> findEntityAppTables(String entity) throws UnifyException;

    /**
     * Finds related application entity fields for specified application form and
     * applet.
     * 
     * @param formId
     *                   the application form ID
     * @param appletName
     *                   the applet name
     * @return list of application form related entity fields
     * @throws UnifyException
     */
    List<AppEntityField> findFormRelatedAppEntityFields(Long formId, String appletName) throws UnifyException;

    /**
     * Finds related application entity fields for specified application form.
     * 
     * @param formId
     *               the application form ID
     * @return list of application form entity fields
     * @throws UnifyException
     */
    List<AppEntityField> findFormAppEntityFields(Long formId) throws UnifyException;

    /**
     * Finds a set of child application entities.
     * 
     * @param entityName
     *                   the entity name
     * @return set of child application entity long names
     * @throws UnifyException
     *                        if an error occurs
     */
    Set<String> findChildAppEntities(String entityName) throws UnifyException;

    /**
     * Finds a set of BLOB application entities.
     * 
     * @return set of BLOB application entity long names
     * @throws UnifyException
     *                        if an error occurs
     */
    Set<String> findBlobEntities(String entityName) throws UnifyException;

    /**
     * Resolves entity references.
     * 
     * @param inst
     *               the entity instance
     * @param entity
     *               the entity long name
     * @throws UnifyException
     *                        if an error occurs
     */
    void resolveEntityReferences(Entity inst, String entity) throws UnifyException;

    /**
     * Resolves the entity data type for application entity field.
     * 
     * @param appEntityField
     *                       the application entity field
     * @return the entity data type
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityFieldDataType resolveListOnlyEntityDataType(AppEntityField appEntityField) throws UnifyException;

    /**
     * Finds a form by ID.
     * 
     * @param formId
     *               the form ID
     * @return the application form record
     * @throws UnifyException
     *                        if form with ID is not found. If an error occurs
     */
    AppForm findAppForm(Long formId) throws UnifyException;

    /**
     * Finds all application forms associated with entity.
     * 
     * @param entity
     *               the entity
     * @return list of application forms
     * @throws UnifyException
     *                        if an error occurs
     */
    List<String> findEntityAppForms(String entity) throws UnifyException;

    /**
     * Creates an application form.
     * 
     * @param appTable
     *                 the application form
     * @return the ID of the created application form
     * @throws UnifyException
     *                        if an error occurs
     */
    Long createAppForm(AppForm appForm) throws UnifyException;

    /**
     * Finds application forms based on query.
     * 
     * @param query
     *              the search query
     * @return list of application forms
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppForm> findAppForms(AppFormQuery query) throws UnifyException;

    /**
     * Finds application form filters.
     * 
     * @param appFormName
     *                    the application form name
     * @return list of form filters
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppFormFilter> findAppFormFilters(String appFormName) throws UnifyException;

    /**
     * Finds application form annotations based on query.
     * 
     * @param query
     *              the search query
     * @return list of application form annotations
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppFormAnnotation> findAppFormAnnotations(AppFormAnnotationQuery query) throws UnifyException;

    /**
     * Finds related application form elements by form ID
     * 
     * @param appFormId
     *                  the application form ID
     * @param type
     *                  the element type
     * @return list of application form elements
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppFormElement> findAppFormElementsByFormId(Long appFormId, FormElementType type) throws UnifyException;

    /**
     * Finds application form entity name.
     * 
     * @param appFormId
     *                  the application form ID
     * @return the entity long name
     * @throws UnifyException
     *                        if an error occurs
     */
    String findAppFormEntityLongName(Long appFormId) throws UnifyException;

    /**
     * Finds related application form elements for specified application form state
     * policy.
     * 
     * @param appFormStatePolicyId
     *                             the application form state ID
     * @param type
     *                             the element type
     * @return list of application form elements
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppFormElement> findAppFormElementsByStatePolicy(Long appFormStatePolicyId, FormElementType type)
            throws UnifyException;

    /**
     * Creates an application assignment page.
     * 
     * @param appAssignmentPage
     *                          the application assignment page
     * @return the ID of the created application assignment page
     * @throws UnifyException
     *                        if an error occurs
     */
    Long createAppAssignmentPage(AppAssignmentPage appAssignmentPage) throws UnifyException;

    /**
     * Creates an application table.
     * 
     * @param appTable
     *                 the application table
     * @return the ID of the created application table
     * @throws UnifyException
     *                        if an error occurs
     */
    Long createAppTable(AppTable appTable) throws UnifyException;

    /**
     * Finds application tables based on query.
     * 
     * @param query
     *              the search query
     * @return list of application tables
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppTable> findAppTables(AppTableQuery query) throws UnifyException;

    /**
     * Finds table action based on query.
     * 
     * @param query
     *              the search query
     * @return list of table actions
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppTableAction> findTableActions(AppTableActionQuery query) throws UnifyException;

    /**
     * Finds application assignment pages on query.
     * 
     * @param query
     *              the search query
     * @return list of application assignment pages
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppAssignmentPage> findAppAssignmentPages(AppAssignmentPageQuery query) throws UnifyException;

    /**
     * Finds all application component IDs.
     * 
     * @param applicationName
     *                        the application name
     * @param componentClazz
     *                        the application component type
     * @return list of application component IDs
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends BaseApplicationEntity> List<Long> findAppComponentIdList(String applicationName, Class<T> componentClazz)
            throws UnifyException;

    /**
     * Finds all application component names.
     * 
     * @param applicationName
     *                        the application name
     * @param componentClazz
     *                        the application component type
     * @return list of application component names
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends BaseApplicationEntity> List<String> findAppComponentNames(String applicationName,
            Class<T> componentClazz) throws UnifyException;

    /**
     * Finds all custom application component IDs.
     * 
     * @param applicationName
     *                        the application name
     * @param componentClazz
     *                        the application component type
     * @return list of application component IDs
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends BaseApplicationEntity> List<Long> findCustomAppComponentIdList(String applicationName,
            Class<T> componentClazz) throws UnifyException;

    /**
     * Gets application definition.
     * 
     * @param applicationName
     *                        the application name
     * @return the application definition
     * @throws UnifyException
     *                        if an error occurs
     */
    ApplicationDef getApplicationDef(String applicationName) throws UnifyException;

    /**
     * Gets application menu definitions.
     * 
     * @param appletFilter
     *                     optional applet filter
     * @return the application menu definition list
     * @throws UnifyException
     *                        if an error occurs
     */
    List<ApplicationMenuDef> getApplicationMenuDefs(String appletFilter) throws UnifyException;

    /**
     * Gets application applet definitions.
     * 
     * @param applicationName
     *                        the application name
     * @return the applet definition list
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppletDef> getAppletDefs(String applicationName) throws UnifyException;

    /**
     * Gets a application applet definition.
     * 
     * @param appletName
     *                   the applet long name
     * @return the applet definition
     * @throws UnifyException
     *                        if an error occurs
     */
    AppletDef getAppletDef(String appletName) throws UnifyException;

    /**
     * Gets a application applet definition.
     * 
     * @param appAppletId
     *                    the applet ID
     * @return the applet definition
     * @throws UnifyException
     *                        if an error occurs
     */
    AppletDef getAppletDef(Long appAppletId) throws UnifyException;

    /**
     * Lists application widget types.
     * 
     * @param query
     *              the query object
     * @return list of widget types
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppWidgetType> findAppWidgetTypes(AppWidgetTypeQuery query) throws UnifyException;

    /**
     * Gets a application widget type.
     * 
     * @param widgetName
     *                   the widget long name
     * @return the widget types definition
     * @throws UnifyException
     *                        if an error occurs
     */
    WidgetTypeDef getWidgetTypeDef(String widgetName) throws UnifyException;

    /**
     * Finds a widget type by ID.
     * 
     * @param widgetTypeId
     *                     the widget type ID
     * @return the application widget type record
     * @throws UnifyException
     *                        if widget type with ID is not found. If an error
     *                        occurs
     */
    AppWidgetType findAppWidgetType(Long widgetTypeId) throws UnifyException;

    /**
     * Gets a application suggestion type.
     * 
     * @param suggestionTypeName
     *                           the suggestion type long name
     * @return the suggestion types definition
     * @throws UnifyException
     *                        if an error occurs
     */
    SuggestionTypeDef getSuggestionTypeDef(String suggestionTypeName) throws UnifyException;

    /**
     * Finds a suggestion type by ID.
     * 
     * @param suggestionTypeId
     *                         the suggestion type ID
     * @return the application suggestion type record
     * @throws UnifyException
     *                        if suggestion type with ID is not found. If an error
     *                        occurs
     */
    AppSuggestionType findAppSuggestionType(Long suggestionTypeId) throws UnifyException;

    /**
     * Finds application property list by ID
     * 
     * @param appPropertyId
     *                      the app property ID
     * @return the application property list
     * @throws UnifyException
     *                        if property list with ID is not found.
     */
    AppPropertyList findAppPropertyList(Long appPropertyId) throws UnifyException;

    /**
     * Finds application property rule by ID
     * 
     * @param appPropertyRuleId
     *                          the app property ID
     * @return the application property rule
     * @throws UnifyException
     *                        if property rule with ID is not found.
     */
    AppPropertyRule findAppPropertyRule(Long appPropertyRuleId) throws UnifyException;

    /**
     * Finds application assignment page by ID
     * 
     * @param appAssignmentPageId
     *                            the app assignment page ID
     * @return the application assignment page
     * @throws UnifyException
     *                        if assignment page with ID is not found.
     */
    AppAssignmentPage findAppAssignmentPage(Long appAssignmentPageId) throws UnifyException;

    /**
     * Gets a application related widget types.
     * 
     * @param applicationName
     *                        the application name
     * @return the widget types definition
     * @throws UnifyException
     *                        if an error occurs
     */
    List<? extends Listable> getRelatedWidgetTypes(String applicationName) throws UnifyException;

    /**
     * Gets delegate entities by entity name
     * 
     * @param entityLongNames
     *                       the entity long name
     * @return the entity list
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Class<?>> getDelegateEntities(List<String> entityLongNames) throws UnifyException;

    /**
     * Gets a application entity class definition.
     * 
     * @param entityName
     *                   the entity long name
     * @return the entity class definition
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityClassDef getEntityClassDef(String entityName) throws UnifyException;

    /**
     * Gets a application entity definition.
     * 
     * @param entityName
     *                   the entity long name
     * @return the entity definition
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityDef getEntityDef(String entityName) throws UnifyException;

    /**
     * Gets a application entity definition.
     * 
     * @param appletName
     *                   the applet name
     * @return the entity definition.
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityDef getAppletEntityDef(String appletName) throws UnifyException;

    /**
     * Gets a application entity class definition.
     * 
     * @param appletName
     *                   the applet name
     * @return the entity class definition.
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityClassDef getAppletEntityClassDef(String appletName) throws UnifyException;

    /**
     * Gets a application entity definition by class name.
     * 
     * @param entityClass
     *                    the entity class name
     * @return the entity definition
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityDef getEntityDefByClass(String entityClass) throws UnifyException;

    /**
     * Lists application references.
     * 
     * @param query
     *              the query object
     * @return list of references
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AppRef> findAppRefs(AppRefQuery query) throws UnifyException;

    /**
     * Gets a application reference definition.
     * 
     * @param refName
     *                the reference long name
     * @return the reference definition
     * @throws UnifyException
     *                        if an error occurs
     */
    RefDef getRefDef(String refName) throws UnifyException;

    /**
     * Gets a application table definition.
     * 
     * @param tableName
     *                  the table long name
     * @return the table definition
     * @throws UnifyException
     *                        if an error occurs
     */
    TableDef getTableDef(String tableName) throws UnifyException;

    /**
     * Gets a application form definition.
     * 
     * @param formName
     *                 the form long name
     * @return the form definition
     * @throws UnifyException
     *                        if an error occurs
     */
    FormDef getFormDef(String formName) throws UnifyException;

    /**
     * Gets a application assignment page definition.
     * 
     * @param assigmentPageName
     *                          the assignment page long name
     * @return the assignment page definition
     * @throws UnifyException
     *                        if an error occurs
     */
    AssignmentPageDef getAssignmentPageDef(String assigmentPageName) throws UnifyException;

    /**
     * Gets a application property list definition.
     * 
     * @param propertyListName
     *                         the property list long name
     * @return the property list definition
     * @throws UnifyException
     *                        if an error occurs
     */
    PropertyListDef getPropertyListDef(String propertyListName) throws UnifyException;

    /**
     * Gets a application property rule definition.
     * 
     * @param propertyRuleName
     *                         the property rule long name
     * @return the property rule definition
     * @throws UnifyException
     *                        if an error occurs
     */
    PropertyRuleDef getPropertyRuleDef(String propertyRuleName) throws UnifyException;

    /**
     * Gets the property list items for entity using supplied rules.
     * 
     * @param entityInst
     *                         the entity instance
     * @param childFkFieldName
     *                         the child foreign key field name
     * @param propertyRuleDef
     *                         the property list rule
     * @return list of property items
     * @throws UnifyException
     *                        if an error occurs
     */
    List<PropertyListItem> getPropertyListItems(Entity entityInst, String childFkFieldName,
            PropertyRuleDef propertyRuleDef) throws UnifyException;

    /**
     * Gets the list property values based on supplied parameters.
     * 
     * @param entityInst
     *                         the entity instance
     * @param childFkFieldName
     *                         the child foreign key field name
     * @param propertyRuleDef
     *                         the property list rule
     * @return the property list values
     * @throws UnifyException
     *                        if an error occurs
     */
    MapValues getPropertyListValues(Entity entityInst, String childFkFieldName, PropertyRuleDef propertyRuleDef)
            throws UnifyException;

    /**
     * Saves property list values using supplied parameters.
     * 
     * @param sweepingCommitPolicy
     *                             sweeping commit policy
     * @param entityInst
     *                             the entity instance
     * @param childFkFieldName
     *                             the child foreign key field name
     * @param propertyRuleDef
     *                             the property list rule
     * @param values
     *                             the values to save
     * @throws UnifyException
     *                        if an error occurs
     */
    void savePropertyListValues(SweepingCommitPolicy sweepingCommitPolicy, Entity entityInst, String childFkFieldName,
            PropertyRuleDef propertyRuleDef, MapValues values) throws UnifyException;

    /**
     * Retrieves application filter definition for an entity instance.
     * 
     * @param category
     *                        the filter category
     * @param ownerEntityName
     *                        the entity type long name
     * @param ownerInstId
     *                        the entity instance ID
     * @param filterGenerator
     *                        the filter generator
     * @return the filter definition if found otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    FilterDef retrieveFilterDef(String category, String ownerEntityName, Long ownerInstId, String filterGenerator)
            throws UnifyException;

    /**
     * Saves application filter definition for an entity instance.
     * 
     * @param sweepingCommitPolicy
     *                             sweeping commit policy
     * @param category
     *                             the filter category
     * @param ownerEntityName
     *                             the entity type long name
     * @param ownerInstId
     *                             the entity instance ID
     * @param filterDef
     *                             the filter definition to save
     * @throws UnifyException
     *                        if an error occurs
     */
    void saveFilterDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, FilterDef filterDef) throws UnifyException;

    /**
     * Saves quick filter application applet filter definition. Updates if existing
     * otherwise creates a new one.
     * 
     * @param sweepingCommitPolicy
     *                             sweeping commit policy
     * @param appAppletId
     *                             the application applet ID
     * @param name
     *                             the filter name
     * @param description
     *                             the filter description
     * @param ownershipType
     *                             the ownership type
     * @param filterDef
     *                             the filter definition
     * @throws UnifyException
     *                        if an error occurs if an error occurs
     */
    void saveAppletQuickFilterDef(SweepingCommitPolicy sweepingCommitPolicy, Long appAppletId, String name,
            String description, OwnershipType ownershipType, FilterDef filterDef) throws UnifyException;

    /**
     * Retrieves application field sequence definition for an entity instance.
     * 
     * @param category
     *                        the set values category
     * @param ownerEntityName
     *                        the entity type long name
     * @param ownerInstId
     *                        the entity instance ID
     * @return the set values definition if found otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    FieldSequenceDef retrieveFieldSequenceDef(String category, String ownerEntityName, Long ownerInstId)
            throws UnifyException;

    /**
     * Saves application field sequence definition for an entity instance.
     * 
     * @param sweepingCommitPolicy
     *                             sweeping commit policy
     * @param category
     *                             the set values category
     * @param ownerEntityName
     *                             the entity type long name
     * @param ownerInstId
     *                             the entity instance ID
     * @param fieldSequenceDef
     *                             the field sequence definition to save
     * @throws UnifyException
     *                        if an error occurs
     */
    void saveFieldSequenceDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, FieldSequenceDef fieldSequenceDef) throws UnifyException;

    /**
     * Retrieves application widget rules definition for an entity instance.
     * 
     * @param category
     *                        the widget rules category
     * @param ownerEntityName
     *                        the entity type long name
     * @param ownerInstId
     *                        the entity instance ID
     * @return the widget rules definition if found otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    WidgetRulesDef retrieveWidgetRulesDef(String category, String ownerEntityName, Long ownerInstId)
            throws UnifyException;

    /**
     * Saves application widget rules definition for an entity instance.
     * 
     * @param sweepingCommitPolicy
     *                             sweeping commit policy
     * @param category
     *                             the set values category
     * @param ownerEntityName
     *                             the entity type long name
     * @param ownerInstId
     *                             the entity instance ID
     * @param widgetRulesDef
     *                             the widget rules definition to save
     * @throws UnifyException
     *                        if an error occurs
     */
    void saveWidgetRulesDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, WidgetRulesDef widgetRulesDef) throws UnifyException;

    /**
     * Retrieves application set values definition for an entity instance.
     * 
     * @param category
     *                        the set values category
     * @param ownerEntityName
     *                        the entity type long name
     * @param ownerInstId
     *                        the entity instance ID
     * @return the set values definition if found otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    SetValuesDef retrieveSetValuesDef(String category, String ownerEntityName, Long ownerInstId) throws UnifyException;

    /**
     * Saves application set values definition for an entity instance.
     * 
     * @param sweepingCommitPolicy
     *                             sweeping commit policy
     * @param category
     *                             the set values category
     * @param ownerEntityName
     *                             the entity type long name
     * @param ownerInstId
     *                             the entity instance ID
     * @param setValuesDef
     *                             the set values definition to save
     * @throws UnifyException
     *                        if an error occurs
     */
    void saveSetValuesDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, SetValuesDef setValuesDef) throws UnifyException;

    /**
     * Retrieves application parameter values definition for an entity instance.
     * 
     * @param category
     *                        the set values category
     * @param ownerEntityName
     *                        the entity type long name
     * @param ownerInstId
     *                        the entity instance ID
     * @param paramConfigList
     *                        parameter configuration list
     * @return the parameter values definition if found otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    ParamValuesDef retrieveParamValuesDef(String category, String ownerEntityName, Long ownerInstId,
            List<ParamConfig> paramConfigList) throws UnifyException;

    /**
     * Saves application parameter values definition for an entity instance.
     * 
     * @param sweepingCommitPolicy
     *                             sweeping commit policy
     * @param category
     *                             the set values category
     * @param ownerEntityName
     *                             the entity type long name
     * @param ownerInstId
     *                             the entity instance ID
     * @param paramValuesDef
     *                             the parameter values definition to save
     * @throws UnifyException
     *                        if an error occurs
     */
    void saveParamValuesDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, ParamValuesDef paramValuesDef) throws UnifyException;

    /**
     * Retrieves application search inputs definition for an entity instance.
     * 
     * @param category
     *                        the search inputs category
     * @param ownerEntityName
     *                        the entity type long name
     * @param ownerInstId
     *                        the entity instance ID
     * @return the search inputs definition if found otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    SearchInputsDef retrieveSearchInputsDef(String category, String ownerEntityName, Long ownerInstId)
            throws UnifyException;

    /**
     * Saves application search inputs definition for an entity instance.
     * 
     * @param sweepingCommitPolicy
     *                             sweeping commit policy
     * @param category
     *                             the search inputs category
     * @param ownerEntityName
     *                             the entity type long name
     * @param ownerInstId
     *                             the entity instance ID
     * @param searchInputsDef
     *                             the search inputs definition to save
     * @throws UnifyException
     *                        if an error occurs
     */
    void saveSearchInputsDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, SearchInputsDef searchInputsDef) throws UnifyException;

    /**
     * Gets an entity description.
     * 
     * @param refLongName
     *                    the entity reference long name
     * @param entityId
     *                    the entity ID
     * @return the entity description
     * @throws UnifyException
     *                        if an error occurs
     */
    String getEntityDescriptionByRef(String refLongName, Long entityId) throws UnifyException;

    /**
     * Gets an entity description.
     * 
     * @param entityClassDef
     *                       the entity class definition
     * @param entityId
     *                       the entity ID
     * @param fieldName
     *                       the description field name
     * @return the entity description
     * @throws UnifyException
     *                        if an error occurs
     */
    String getEntityDescription(EntityClassDef entityClassDef, Long entityId, String fieldName) throws UnifyException;

    /**
     * Gets an entity description.
     * 
     * @param entityClassDef
     *                       the entity class definition
     * @param inst
     *                       the entity
     * @param fieldName
     *                       the description field name
     * @return the entity description
     * @throws UnifyException
     *                        if an error occurs
     */
    String getEntityDescription(EntityClassDef entityClassDef, Entity inst, String fieldName) throws UnifyException;

    /**
     * Gets an entity delegate.
     * 
     * @param entityName
     *                   the entity name
     * @return the entity delegate
     * @throws UnifyException
     *                        if an error occurs
     */
    DelegateEntityInfo getDelegateEntity(String entityName) throws UnifyException;

    /**
     * Gets all entities by delegate.
     * 
     * @param delegate
     *                 the delegate
     * @return the entity long names
     * @throws UnifyException
     *                        if an error occurs
     */
    List<DelegateEntityInfo> getDelegateEntitiesByDelegate(String delegate) throws UnifyException;

    /**
     * Gets all entities by data source name.
     * 
     * @param dataSourceName
     *                 the data source name
     * @return the entity long names
     * @throws UnifyException
     *                        if an error occurs
     */
    List<DelegateEntityInfo> getDelegateEntitiesByDataSource(String dataSourceName) throws UnifyException;

    /**
     * Gets all entities that have a delegate.
     * 
     * @return the entity long names
     * @throws UnifyException
     *                        if an error occurs
     */
    List<DelegateEntityInfo> getDelegateEntities() throws UnifyException;

    /**
     * Find foreign entity string fields including list-only strings.
     * 
     * @param entityName
     *                    the entity name
     * @param fkFieldName
     *                    the foreign key field name
     * @return the list of string field names
     * @throws UnifyException
     *                        if an error occurs
     */
    List<String> findForeignEntityStringFields(String entityName, String fkFieldName) throws UnifyException;

    /**
     * Find entity string fields including list-only strings.
     * 
     * @param entityName
     *                   the entity name
     * @return the list of string field names
     * @throws UnifyException
     *                        if an error occurs
     */
    List<String> findEntityStringFields(String entityName) throws UnifyException;

    /**
     * Generates dynamic entity information for a set of entities.
     * 
     * @param entityNames
     *                    the entity names
     * @param basePackage
     *                    the base package
     * @param extension
     *                    extension flag
     * @return list of dynamic entity information
     * @throws UnifyException
     *                        if an error occurs
     */
    List<DynamicEntityInfo> generateDynamicEntityInfos(List<String> entityNames, String basePackage, boolean extension)
            throws UnifyException;

    /**
     * Gets child restriction.
     * 
     * @param parentEntityDef
     *                        the parent entity definition
     * @param childFieldName
     *                        the child field name
     * @param parentInst
     *                        the parent
     * @return the child restriction
     * @throws UnifyException
     *                        if an error occurs
     */
    Restriction getChildRestriction(EntityDef parentEntityDef, String childFieldName, Entity parentInst)
            throws UnifyException;
    
    /**
     * Sets the reload on switch flag in current request context.
     * 
     * @throws UnifyException
     *                        if an error occurs
     */
    void setReloadOnSwitch() throws UnifyException;

    /**
     * Clears the reload on switch flag in current request context.
     * 
     * @return last state
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean clearReloadOnSwitch() throws UnifyException;

    /**
     * Checks if the reload on switch flag is set in current request context.
     * 
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean isReloadOnSwitch() throws UnifyException;
    
    /**
     * Gets work item category participation count by role.
     * 
     * @param role
     *             the role to get by
     * @return the participation count
     * @throws UnifyException
     *                        if an error occurs
     */
    int getWorkitemCategoryParticipationCount(String role) throws UnifyException;
}

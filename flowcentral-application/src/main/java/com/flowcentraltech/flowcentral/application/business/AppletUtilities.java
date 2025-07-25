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
package com.flowcentraltech.flowcentral.application.business;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.AssignmentPageDef;
import com.flowcentraltech.flowcentral.application.data.DelegateEntityInfo;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFormEventHandlers;
import com.flowcentraltech.flowcentral.application.data.FieldSequenceDef;
import com.flowcentraltech.flowcentral.application.data.FilterDef;
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormTabDef;
import com.flowcentraltech.flowcentral.application.data.PropertyListItem;
import com.flowcentraltech.flowcentral.application.data.PropertyRuleDef;
import com.flowcentraltech.flowcentral.application.data.PropertySequenceDef;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.data.SearchInputsDef;
import com.flowcentraltech.flowcentral.application.data.SetValuesDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.data.WidgetRulesDef;
import com.flowcentraltech.flowcentral.application.data.WidgetTypeDef;
import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.application.validation.FormContextEvaluator;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.application.web.data.DetailsCase;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.flowcentraltech.flowcentral.application.web.panels.EntityCRUD;
import com.flowcentraltech.flowcentral.application.web.panels.EntityChild;
import com.flowcentraltech.flowcentral.application.web.panels.EntityFieldSequence;
import com.flowcentraltech.flowcentral.application.web.panels.EntityFilter;
import com.flowcentraltech.flowcentral.application.web.panels.EntityParamValues;
import com.flowcentraltech.flowcentral.application.web.panels.EntityPropertySequence;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySearch;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySearchInput;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySelect;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySetValues;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySingleForm;
import com.flowcentraltech.flowcentral.application.web.panels.EntityWidgetRules;
import com.flowcentraltech.flowcentral.application.web.panels.FormWizard;
import com.flowcentraltech.flowcentral.application.web.panels.HeaderWithTabsForm;
import com.flowcentraltech.flowcentral.application.web.panels.HeadlessTabsForm;
import com.flowcentraltech.flowcentral.application.web.panels.ListingForm;
import com.flowcentraltech.flowcentral.application.web.panels.LoadingSearch;
import com.flowcentraltech.flowcentral.application.web.panels.PropertySearch;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractApplet;
import com.flowcentraltech.flowcentral.application.web.panels.applet.AbstractEntityFormApplet;
import com.flowcentraltech.flowcentral.application.web.widgets.BeanListTable;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniForm;
import com.flowcentraltech.flowcentral.application.web.widgets.SectorIcon;
import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.flowcentraltech.flowcentral.common.business.AuditLogger;
import com.flowcentraltech.flowcentral.common.business.CollaborationProvider;
import com.flowcentraltech.flowcentral.common.business.EnvironmentDelegateRegistrar;
import com.flowcentraltech.flowcentral.common.business.EnvironmentDelegateUtilities;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.business.ReportProvider;
import com.flowcentraltech.flowcentral.common.business.SequenceCodeGenerator;
import com.flowcentraltech.flowcentral.common.business.SpecialParamProvider;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.TableSummaryLine;
import com.flowcentraltech.flowcentral.common.constants.OwnershipType;
import com.flowcentraltech.flowcentral.common.data.AuditSnapshot;
import com.flowcentraltech.flowcentral.common.data.EntityAuditInfo;
import com.flowcentraltech.flowcentral.common.data.FormListingOptions;
import com.flowcentraltech.flowcentral.common.data.FormattedAudit;
import com.flowcentraltech.flowcentral.common.data.FormatterOptions;
import com.flowcentraltech.flowcentral.common.data.GenerateListingReportOptions;
import com.flowcentraltech.flowcentral.common.data.ParamValuesDef;
import com.flowcentraltech.flowcentral.configuration.constants.InputType;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.common.database.WorkEntity;
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UserToken;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.Formats;
import com.tcdng.unify.core.data.MapValues;
import com.tcdng.unify.core.data.ParamConfig;
import com.tcdng.unify.core.data.ParameterizedStringGenerator;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.database.Database;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.format.FormatHelper;
import com.tcdng.unify.core.report.Report;
import com.tcdng.unify.core.system.SequenceNumberService;
import com.tcdng.unify.core.task.TaskLauncher;
import com.tcdng.unify.core.upl.UplComponent;
import com.tcdng.unify.web.ui.widget.Panel;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;

/**
 * Applet utilities component.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface AppletUtilities extends FlowCentralComponent {

    /**
     * Gets formatted audit. Uses default formatter options.
     * 
     * @param auditSnapshot
     *                      the audit snapshot.
     * @return the formatted audit
     * @throws UnifyException
     *                        if an error occurs
     */
    FormattedAudit formatAudit(AuditSnapshot auditSnapshot) throws UnifyException;

    /**
     * Gets formatted audit using supplied formatter options.
     * 
     * @param auditSnapshot
     *                      the audit snapshot.
     * @param options
     *                      the formatter options
     * @return the formatted audit
     * @throws UnifyException
     *                        if an error occurs
     */
    FormattedAudit formatAudit(AuditSnapshot auditSnapshot, FormatterOptions options) throws UnifyException;

    /**
     * Get entity audit information.
     * 
     * @param entityName
     *                   the entity name
     * @return the entity audit information
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityAuditInfo getEntityAuditInfo(String entityName) throws UnifyException;

    /**
     * Gets application entities based on supplied query.
     * 
     * @param query
     *              the query to search with
     * @return list of application entities long names
     * @throws UnifyException
     *                        if an error occurs
     */
    List<String> getApplicationEntitiesLongNames(Query<? extends BaseApplicationEntity> query) throws UnifyException;

    /**
     * Gets application entities based on supplied query.
     * 
     * @param query
     *              the query to search with
     * @return list of application listables
     * @throws UnifyException
     *                        if an error occurs
     */
    List<? extends Listable> getApplicationEntityListables(Query<? extends BaseApplicationEntity> query)
            throws UnifyException;

    /**
     * Gets entity components list
     * 
     * @param componentType
     *                            the component type
     * @param entity
     *                            the entity name
     * @param acceptNonReferenced
     *                            accept non-reference components
     * @return the list of components
     * @throws UnifyException
     *                        if an error occurs
     */
    List<? extends Listable> getEntityComponents(Class<? extends UnifyComponent> componentType, String entity,
            boolean acceptNonReferenced) throws UnifyException;

    /**
     * Gets a generator instance.
     * 
     * @param paramReader
     *                    the parameter value store reader
     * @param tokenList
     *                    the token list
     * @return the generator instance
     * @throws UnifyException
     *                        if an error occurs
     */
    ParameterizedStringGenerator getStringGenerator(ValueStoreReader paramReader, List<StringToken> tokenList)
            throws UnifyException;

    /**
     * Gets a generator instance.
     * 
     * @param paramReader
     *                        the parameter value store reader
     * @param generatorReader
     *                        the generator value store reader
     * @param tokenList
     *                        the token list
     * @return the generator instance
     * @throws UnifyException
     *                        if an error occurs
     */
    ParameterizedStringGenerator getStringGenerator(ValueStoreReader paramReader, ValueStoreReader generatorReader,
            List<StringToken> tokenList) throws UnifyException;

    /**
     * Checks if request is low latency
     * 
     * @return true if low latency otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean isLowLatencyRequest() throws UnifyException;

    /**
     * Gets search minimum items per page.
     * 
     * @return the minimum items per page
     * @throws UnifyException
     *                        if an error occurs
     */
    int getSearchMinimumItemsPerPage() throws UnifyException;

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
     * Gets page sector icon by application.
     * 
     * @param applicationName
     *                        the application name
     * @return the sector icon if page section indication is enabled
     * @throws UnifyException
     *                        if an error occurs
     */
    SectorIcon getPageSectorIconByApplication(String applicationName) throws UnifyException;

    /**
     * Gets the current request trigger ID
     * 
     * @return the current trigger ID
     * @throws UnifyException
     *                        if an error occurs
     */
    String getTriggerWidgetId() throws UnifyException;

    /**
     * Hints user in current request with supplied message in INFO mode.
     * 
     * @param messageKey
     *                   the message key
     * @param params
     *                   the message parameters
     * @throws UnifyException
     *                        if an error occurs
     */
    void hintUser(String messageKey, Object... params) throws UnifyException;

    /**
     * Hints user in current request with supplied message.
     * 
     * @param mode
     *                   the hint mode
     * @param messageKey
     *                   the message key
     * @param params
     *                   the message parameters
     * @throws UnifyException
     *                        if an error occurs
     */
    void hintUser(MODE mode, String messageKey, Object... params) throws UnifyException;

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
     * Gets system parameter value and converts to the specified type.
     * 
     * @param clazz
     *              the type to convert to
     * @param code
     *              the system parameter code
     * @return the resulting value
     * @throws UnifyException
     *                        if parameter with name is unknown. if a value data
     *                        conversion error occurs
     */
    <T> T getSysParameterValue(Class<T> clazz, String code) throws UnifyException;

    /**
     * Gets the next sequence code for the supplied sequence definition and current
     * date.
     * 
     * @param ownerId
     *                          the sequence owner Id
     * @param sequenceDefintion
     *                          the sequence definition
     * @param valueStoreReader
     *                          the value store reader (optional)
     * @return the next sequence code
     * @throws UnifyException
     *                        if an error occurs
     */
    String getNextSequenceCode(String ownerId, String sequenceDefintion, ValueStoreReader valueStoreReader)
            throws UnifyException;

    /**
     * Gets next sequence number.
     * 
     * @param sequence
     *                 the sequence name
     * @return the next number
     * @throws UnifyException
     *                        if an error occurs
     */
    long getNextSequenceNumber(String sequence) throws UnifyException;

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
     * Gets current session user token.
     * 
     * @return the session user token
     * @throws UnifyException
     *                        if an error occurs
     */
    UserToken getSessionUserToken() throws UnifyException;

    /**
     * Gets current session user ID.
     * 
     * @return the session user ID
     * @throws UnifyException
     *                        if an error occurs
     */
    String getSessionUserLoginId() throws UnifyException;

    /**
     * Gets current session branch scope.
     * 
     * @return the branch scope
     * @throws UnifyException
     *                        if an error occurs.
     */
    List<Long> getSessionBranchScope() throws UnifyException;

    /**
     * Gets current session branch scope restriction.
     * 
     * @return the restriction otherwise null
     * @throws UnifyException
     *                        if an error occurs.
     */
    Restriction getSessionBranchScopeRestriction(EntityDef entityDef) throws UnifyException;

    /**
     * Gets a session attribute.
     * 
     * @param clazz
     *              the attribute class
     * @param name
     *              the attribute name
     * @return the session attribute
     * @throws UnifyException
     *                        if an error occurs
     */
    <T> T getSessionAttribute(Class<T> clazz, String name) throws UnifyException;

    /**
     * Sets session attribute.
     * 
     * @param name
     *             the session name
     * @param val
     *             the session value
     * @throws UnifyException
     *                        if an error occurs
     */
    void setSessionAttribute(String name, Object val) throws UnifyException;

    /**
     * Gets current system timestamp.
     * 
     * @return the current timestamp
     * @throws UnifyException
     *                        if an error occurs
     */
    Date getNow() throws UnifyException;

    /**
     * Gets today.
     * 
     * @return today
     * @throws UnifyException
     *                        if an error occurs
     */
    Date getToday() throws UnifyException;

    /**
     * Gets a component.
     * 
     * @param componentClazz
     *                       the component class.
     * 
     * @return the component instance
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends UnifyComponent> T getComponent(Class<T> componentClazz) throws UnifyException;

    /**
     * Gets a component.
     * 
     * @param componentClazz
     *                       the component class.
     * 
     * @param componentName
     *                       the component name
     * @return the component instance
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends UnifyComponent> T getComponent(Class<T> componentClazz, String componentName) throws UnifyException;

    /**
     * Gets a UPL component.
     * 
     * @param componentClazz
     *                       the UPL component class.
     * 
     * @param descriptor
     *                       the UPL descriptor
     * @return the component instance
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends UplComponent> T getUplComponent(Class<T> componentClazz, String descriptor) throws UnifyException;

    /**
     * Gets a symbol unicode.
     * 
     * @param symbolName
     *                   the symbol name
     * @return the unicode if found otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    String getSymbolUnicode(String symbolName) throws UnifyException;

    /**
     * Gets a resolves applet session message.
     * 
     * @param message
     *                the message
     * @param params
     *                the message parameters
     * @return the resolves session message
     * @throws UnifyException
     *                        if an error occurs
     */
    String resolveSessionMessage(String message, Object... params) throws UnifyException;

    /**
     * Gets a list item for session locale.
     * 
     * @param listName
     *                 the list name
     * @param itemKey
     *                 the item key
     * @return the list item
     * @throws UnifyException
     *                        if an error occurs
     */
    Listable getListItemByKey(String listName, String itemKey) throws UnifyException;

    /**
     * Translates restriction to text using current session locale.
     * 
     * @param restriction
     *                    the restriction to translate
     * @return the generated translation.
     * @throws UnifyException
     *                        if an error occurs
     */
    String translate(Restriction restriction) throws UnifyException;

    /**
     * Translates restriction to text using current session locale.
     * 
     * @param restriction
     *                    the restriction to translate
     * @param entityDef
     *                    the entity definition
     * @return the generated translation.
     * @throws UnifyException
     *                        if an error occurs
     */
    String translate(Restriction restriction, EntityDef entityDef) throws UnifyException;

    /**
     * Gets the application work item utilities.
     * 
     * @return the application work item utilities
     */
    ApplicationWorkItemUtilities workItemUtilities();

    /**
     * Gets the environment delegate utilities.
     * 
     * @return the environment delegate utilities
     */
    EnvironmentDelegateUtilities delegateUtilities();

    /**
     * Gets the environment delegate registrar.
     * 
     * @return the environment delegate registrar
     */
    EnvironmentDelegateRegistrar delegateRegistrar();

    /**
     * Gets audit logger.
     * 
     * @return the audit logger
     */
    AuditLogger audit();

    /**
     * Gets the application service.
     * 
     * @return the application service.
     */
    ApplicationModuleService application();

    /**
     * Gets dynamic enumeration provider.
     * 
     * @return the provider
     */
    DynamicEnumProvider enumProvider();
    
    /**
     * Gets the application privilege manager.
     * 
     * @return the application privilege manager.
     */
    ApplicationPrivilegeManager applicationPrivilegeManager();

    /**
     * Get the task launcher component.
     * 
     * @return the task launcher
     */
    TaskLauncher taskLauncher();

    /**
     * Gets the system service.
     * 
     * @return the system service.
     */
    SystemModuleService system();

    /**
     * Gets the environment service.
     * 
     * @return the environment service.
     */
    EnvironmentService environment();

    /**
     * Gets special parameter provider.
     * 
     * @return the special parameter provider
     */
    SpecialParamProvider specialParamProvider();

    /**
     * Gets collaboration provider.
     * 
     * @return the special parameter provider
     */
    CollaborationProvider collaborationProvider();

    /**
     * Gets format helper
     * 
     * @return the format helper
     */
    FormatHelper formatHelper();

    /**
     * Gets form context evaluator.
     * 
     * @return the evaluator
     */
    FormContextEvaluator formContextEvaluator();

    /**
     * Gets the sequence code generator
     * 
     * @return the sequence code generator
     */
    SequenceCodeGenerator sequenceCodeGenerator();

    /**
     * Gets the sequence number service
     * 
     * @return the sequence number service
     */
    SequenceNumberService sequence();

    /**
     * Gets the report provider.
     * 
     * @return the report provider
     */
    ReportProvider reportProvider();

    /**
     * Gets a application applet definition.
     * 
     * @param appletName
     *                   the applet name
     * @return the applet definition.
     * @throws UnifyException
     *                        if an error occurs
     */
    AppletDef getAppletDef(String appletName) throws UnifyException;

    /**
     * Gets a application applet definition.
     * 
     * @param appAppletId
     *                    the applet ID
     * @return the applet definition.
     * @throws UnifyException
     *                        if an error occurs
     */
    AppletDef getAppletDef(Long appAppletId) throws UnifyException;

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
     * Ensures workflows are defined for applet if applet has its workflow copy flag
     * set.
     * 
     * @param appletName
     *                    the applet name
     * @param forceUpdate
     *                    force workflow update
     * @throws UnifyException
     *                        if an error occurs.
     */
    void ensureWorkflowCopyWorkflows(String appletName, boolean forceUpdate) throws UnifyException;

    /**
     * Ensures workflow user interaction loading applets.
     * 
     * @param forceUpdate
     *                    force loading applets update
     * @throws UnifyException
     *                        if an error occurs.
     */
    void ensureWorkflowUserInteractionLoadingApplets(boolean forceUpdate) throws UnifyException;

    /**
     * Ensures workflow user interaction loading applet.
     * 
     * @param loadingTableName
     *                         the loading table name
     * @param forceUpdate
     *                         force loading applets update
     * @throws UnifyException
     *                        if an error occurs.
     */
    void ensureWorkflowUserInteractionLoadingApplet(String loadingTableName, boolean forceUpdate) throws UnifyException;

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
     * Checks if work entity is with pending draft
     * 
     * @param entityClass
     *                    the entity class
     * @param id
     *                    the entity ID
     * @return true if a draft exists otherwise false
     * @throws UnifyException
     *                        if an error occurs.
     */
    boolean isWorkEntityWithPendingDraft(Class<? extends WorkEntity> entityClass, Long id) throws UnifyException;;

    /**
     * Gets a application entity class definition.
     * 
     * @param entityName
     *                   the entity name
     * @return the entity class definition.
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityClassDef getEntityClassDef(String entityName) throws UnifyException;

    /**
     * Checks if application entity class is defined.
     * 
     * @param entityName
     *                   the entity long name
     * @return true if define otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean isEntityDef(String entityName) throws UnifyException;

    /**
     * Gets a application entity definition.
     * 
     * @param entityName
     *                   the entity name
     * @return the entity definition.
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityDef getEntityDef(String entityName) throws UnifyException;

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
     *                  the table name
     * @return the table definition.
     * @throws UnifyException
     *                        if an error occurs
     */
    TableDef getTableDef(String tableName) throws UnifyException;

    /**
     * Gets a application form definition.
     * 
     * @param formName
     *                 the form name
     * @return the form definition.
     * @throws UnifyException
     *                        if an error occurs
     */
    FormDef getFormDef(String formName) throws UnifyException;

    /**
     * Gets a application assignment page definition.
     * 
     * @param assignPageName
     *                       the assignment page name
     * @return the form definition.
     * @throws UnifyException
     *                        if an error occurs
     */
    AssignmentPageDef getAssignmentPageDef(String assignPageName) throws UnifyException;

    /**
     * Gets a application widget type definition.
     * 
     * @param widgetName
     *                   the widget name
     * @return the widget definition.
     * @throws UnifyException
     *                        if an error occurs
     */
    WidgetTypeDef getWidgetTypeDef(String widgetName) throws UnifyException;

    /**
     * Sets an adhoc widget type.
     * 
     * @param dataType
     *                    the data type
     * @param type
     *                    the input type
     * @param longName
     *                    the long name
     * @param description
     *                    the description
     * @param editor
     *                    the editor
     * @param renderer
     *                    the renderer
     * @return the widget long name
     * @throws UnifyException
     *                        if an error occurs
     */
    String setAdhocWidgetTypeDef(DataType dataType, InputType type, String longName, String description, String editor,
            String renderer) throws UnifyException;

    /**
     * Gets a application property rule definition.
     * 
     * @param propertyRuleName
     *                         the property rule name
     * @return the property rule definition.
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
     *                        filter generator
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
     *                             the sweeping commit policy
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
     * Saves a quick filter for application applet definition. Updates if existing
     * otherwise creates a new one.
     * 
     * @param sweepingCommitPolicy
     *                             the sweeping commit policy
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
     * Sets up show popup command result in current request.
     * 
     * @param panel
     *              the popup panel
     * @throws UnifyException
     *                        if an error occurs
     */
    void commandShowPopup(Panel panel) throws UnifyException;

    /**
     * Sets up show popup command result in current request.
     * 
     * @param panelName
     *                  the popup panel long name
     * @throws UnifyException
     *                        if an error occurs
     */
    void commandShowPopup(String panelName) throws UnifyException;

    /**
     * Hide popup on command request response
     * 
     * @throws UnifyException
     *                        if an error occurs
     */
    void commandHidePopup() throws UnifyException;

    /**
     * Refresh panels on command request response
     * 
     * @param panelLongName
     *                      the panel long names
     * @throws UnifyException
     *                        if an error occurs
     */
    void commandRefreshPanels(String... panelLongName) throws UnifyException;

    /**
     * Refresh panels on command request response
     * 
     * @param panels
     *               the panels
     * @throws UnifyException
     *                        if an error occurs
     */
    void commandRefreshPanels(Panel... panels) throws UnifyException;

    /**
     * Refresh panels and hide popup on command request response
     * 
     * @param panelLongName
     *                      the panel long names
     * @throws UnifyException
     *                        if an error occurs
     */
    void commandRefreshPanelsAndHidePopup(String... panelLongName) throws UnifyException;

    /**
     * Refresh panels and hide popup on command request response
     * 
     * @param panelLongName
     *                      the panels to refresh
     * @throws UnifyException
     *                        if an error occurs
     */
    void commandRefreshPanelsAndHidePopup(Panel... panels) throws UnifyException;

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
     * Retrieves application sequence definition for an entity instance.
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
    PropertySequenceDef retrieveSequenceDef(String category, String ownerEntityName, Long ownerInstId)
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
     * Saves application sequence definition for an entity instance.
     * 
     * @param sweepingCommitPolicy
     *                             sweeping commit policy
     * @param category
     *                             the set values category
     * @param ownerEntityName
     *                             the entity type long name
     * @param ownerInstId
     *                             the entity instance ID
     * @param propertySequenceDef
     *                             the sequence definition to save
     * @throws UnifyException
     *                        if an error occurs
     */
    void saveSequenceDef(SweepingCommitPolicy sweepingCommitPolicy, String category, String ownerEntityName,
            Long ownerInstId, PropertySequenceDef propertySequenceDef) throws UnifyException;

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
     * Constructs a listing form.
     * 
     * @param applet
     *                    the applet
     * @param rootTitle
     *                    the root applet title
     * @param beanTitle
     *                    the bean title
     * @param formDef
     *                    the form definition
     * @param inst
     *                    the entity instance
     * @param breadCrumbs
     *                    optional bread crumbs
     * @return constructed form
     * @throws UnifyException
     *                        if an error occurs
     */
    ListingForm constructListingForm(AbstractApplet applet, String rootTitle, String beanTitle, FormDef formDef,
            Entity inst, BreadCrumbs breadCrumbs) throws UnifyException;

    /**
     * Constructs a form wizard.
     * 
     * @param applet
     *                    the applet
     * @param formDef
     *                    the form definition
     * @param inst
     *                    the backing entity bean
     * @param rootTitle
     *                    the root applet title
     * @param beanTitle
     *                    the bean title
     * @param breadCrumbs
     *                    optional bread crumbs
     * @return constructed form wizard
     * @throws UnifyException
     *                        if an error occurs
     */
    FormWizard constructFormWizard(AbstractApplet applet, FormDef formDef, Entity inst, String rootTitle,
            String beanTitle, BreadCrumbs breadCrumbs) throws UnifyException;

    /**
     * Constructs a header with tabs form.
     * 
     * @param applet
     *                          the applet
     * @param rootTitle
     *                          the root applet title
     * @param beanTitle
     *                          the bean title
     * @param formDef
     *                          the form definition
     * @param inst
     *                          the entity instance
     * @param formMode
     *                          the form mode
     * @param breadCrumbs
     *                          optional bread crumbs
     * @param formEventHandlers
     *                          optional form switch on change handler
     * @return constructed form
     * @throws UnifyException
     *                        if an error occurs
     */
    HeaderWithTabsForm constructHeaderWithTabsForm(AbstractEntityFormApplet applet, String rootTitle, String beanTitle,
            FormDef formDef, Entity inst, FormMode formMode, BreadCrumbs breadCrumbs,
            EntityFormEventHandlers formEventHandlers) throws UnifyException;

    /**
     * Constructs a headless tab form.
     * 
     * @param appletContext
     *                             the applet context
     * @param sweepingCommitPolicy
     *                             the sweeping commit policy
     * @param rootTitle
     *                             the root applet title
     * @param appletDef
     *                             the applet definition
     * @return the headless tabs form
     * @throws UnifyException
     *                        if an error occurs
     */
    HeadlessTabsForm constructHeadlessTabsForm(AppletContext appletContext, SweepingCommitPolicy sweepingCommitPolicy,
            String rootTitle, AppletDef appletDef) throws UnifyException;

    /**
     * Constructs a entity single form.
     * 
     * @param applet
     *                    the applet
     * @param rootTitle
     *                    the root applet title
     * @param beanTitle
     *                    the bean title
     * @param inst
     *                    the entity instance
     * @param formMode
     *                    the form mode
     * @param breadCrumbs
     *                    optional bread crumbs
     * @return the entity single form
     * @throws UnifyException
     *                        if an error occurs
     */
    EntitySingleForm constructEntitySingleForm(AbstractApplet applet, String rootTitle, String beanTitle, Entity inst,
            FormMode formMode, BreadCrumbs breadCrumbs) throws UnifyException;

    /**
     * Updates a header with tabs form.
     * 
     * @param form
     *             the form to update
     * @param inst
     *             the entity instance
     * @throws UnifyException
     *                        if an error occurs
     */
    void updateHeaderWithTabsForm(HeaderWithTabsForm form, Entity inst) throws UnifyException;

    /**
     * Updates an entity single form.
     * 
     * @param applet
     *               the applet
     * @param form
     *               the form to update
     * @param inst
     *               the entity instance
     * @throws UnifyException
     *                        if an error occurs
     */
    void updateEntitySingleForm(AbstractApplet applet, EntitySingleForm form, Entity inst) throws UnifyException;

    /**
     * Constructs property search.
     * 
     * @param ctx
     *                                the form context
     * @param sweepingCommitPolicy
     *                                the sweepingCommitPolicy (optional)
     * @param tabName
     *                                the tab name (optional)
     * @param inst
     *                                the entity instance
     * @param _appletDef
     *                                the applet definition
     * @param isIgnoreParentCondition
     *                                ignore parent condition flag
     * @return the property search
     * @throws UnifyException
     *                        if an error occurs
     */
    PropertySearch constructPropertySearch(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy, String tabName,
            Entity inst, AppletDef _appletDef, boolean isIgnoreParentCondition) throws UnifyException;

    /**
     * Constructs entity search.
     * 
     * @param ctx
     *                                the form context
     * @param sweepingCommitPolicy
     *                                the sweepingCommitPolicy (optional)
     * @param tabName
     *                                the tab name (optional)
     * @param rootTitle
     *                                the root applet title
     * @param _appletDef
     *                                the applet definition
     * @param editAction
     *                                the edit action
     * @param entitySearchMode
     *                                the entity search mode
     * @param isIgnoreReport
     *                                ignore report flag
     * @param isIgnoreParentCondition
     *                                ignore parent condition flag
     * @return the entity search
     * @throws UnifyException
     *                        if an error occurs
     */
    EntitySearch constructEntitySearch(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy, String tabName,
            String rootTitle, AppletDef _appletDef, String editAction, int entitySearchMode, boolean isIgnoreReport,
            boolean isIgnoreParentCondition) throws UnifyException;

    /**
     * Constructs loading search.
     * 
     * @param ctx
     *                          applet context
     * @param loadingSearchMode
     *                          the loading search mode
     * @return the loading search
     * @throws UnifyException
     *                        if an error occurs
     */
    LoadingSearch constructLoadingSearch(AppletContext ctx, int loadingSearchMode) throws UnifyException;

    /**
     * Constructs an entity select object.
     * 
     * @param refDef
     *                   the reference definition
     * @param valueStore
     *                   the value store
     * @param fieldNameA
     *                   optional field name A
     * @param fieldNameB
     *                   optional field name B
     * @param filter
     *                   the initial filter (optional)
     * @param limit
     *                   the limit (optional)
     * @return the entity select object
     * @throws UnifyException
     *                        if an error occurs
     */
    EntitySelect constructEntitySelect(RefDef refDef, ValueStore valueStore, String fieldNameA, String fieldNameB,
            String filter, int limit) throws UnifyException;

    /**
     * Constructs entity child.
     * 
     * @param ctx
     *                                the form context
     * @param sweepingCommitPolicy
     *                                the sweepingCommitPolicy (optional)
     * @param tabName
     *                                the tab name (optional)
     * @param rootTitle
     *                                the root applet title
     * @param _appletDef
     *                                the applet definition
     * @param quickEdit
     *                                enable quick edit flag
     * @param isIgnoreParentCondition
     *                                ignore parent condition flag
     * @return the entity child
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityChild constructEntityChild(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy, String tabName,
            String rootTitle, AppletDef _appletDef, boolean quickEdit, boolean isIgnoreParentCondition)
            throws UnifyException;

    /**
     * Constructs entity filter.
     * 
     * @param ctx
     *                                the form context
     * @param sweepingCommitPolicy
     *                                the sweepingCommitPolicy (optional)
     * @param tabName
     *                                the tab name (optional)
     * @param ownerEntityDef
     *                                the owner entity definition
     * @param entityFilterMode
     *                                the entity filter mode
     * @param isIgnoreParentCondition
     *                                ignore parent condition flag
     * @param includeSysParam
     *                                include system parameter filters
     * @return the entity filter
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityFilter constructEntityFilter(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy, String tabName,
            EntityDef ownerEntityDef, int entityFilterMode, boolean isIgnoreParentCondition, boolean includeSysParam)
            throws UnifyException;

    /**
     * Constructs entity search input.
     * 
     * @param ctx
     *                                the form context
     * @param sweepingCommitPolicy
     *                                the sweepingCommitPolicy (optional)
     * @param tabName
     *                                the tab name (optional)
     * @param ownerEntityDef
     *                                the owner entity definition
     * @param entitySearchInputMode
     *                                the entity search input mode
     * @param isIgnoreParentCondition
     *                                ignore parent condition flag
     * @return the entity filter
     * @throws UnifyException
     *                        if an error occurs
     */
    EntitySearchInput constructEntitySearchInput(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy,
            String tabName, EntityDef ownerEntityDef, int entitySearchInputMode, boolean isIgnoreParentCondition)
            throws UnifyException;

    /**
     * Constructs entity field sequence.
     * 
     * @param ctx
     *                                the form context
     * @param sweepingCommitPolicy
     *                                the sweepingCommitPolicy (optional)
     * @param tabName
     *                                the tab name (optional)
     * @param ownerEntityDef
     *                                the owner entity definition
     * @param entityFieldSequenceMode
     *                                the entity field sequence mode
     * @param isIgnoreParentCondition
     *                                ignore parent condition flag
     * @return the entity set values
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityFieldSequence constructEntityFieldSequence(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy,
            String tabName, EntityDef ownerEntityDef, int entityFieldSequenceMode, boolean isIgnoreParentCondition)
            throws UnifyException;

    /**
     * Constructs entity property sequence.
     * 
     * @param ctx
     *                                the form context
     * @param sweepingCommitPolicy
     *                                the sweepingCommitPolicy (optional)
     * @param tabName
     *                                the tab name (optional)
     * @param ownerEntityDef
     *                                the owner entity definition
     * @param entityFieldSequenceMode
     *                                the entity field sequence mode
     * @param isIgnoreParentCondition
     *                                ignore parent condition flag
     * @return the entity set values
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityPropertySequence constructEntityPropertySequence(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy,
            String tabName, EntityDef ownerEntityDef, int entityPropertySequenceMode, boolean isIgnoreParentCondition)
            throws UnifyException;

    /**
     * Constructs entity widget rules.
     * 
     * @param ctx
     *                                the form context
     * @param sweepingCommitPolicy
     *                                the sweepingCommitPolicy (optional)
     * @param tabName
     *                                the tab name (optional)
     * @param ownerEntityDef
     *                                the owner entity definition
     * @param mode
     *                                the entity widget rules mode
     * @param isIgnoreParentCondition
     *                                ignore parent condition flag
     * @return the entity widget rules
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityWidgetRules constructEntityWidgetRules(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy,
            String tabName, EntityDef ownerEntityDef, int mode, boolean isIgnoreParentCondition) throws UnifyException;

    /**
     * Constructs entity set values.
     * 
     * @param ctx
     *                                the form context
     * @param sweepingCommitPolicy
     *                                the sweepingCommitPolicy (optional)
     * @param tabName
     *                                the tab name (optional)
     * @param ownerEntityDef
     *                                the owner entity definition
     * @param entitySetValuesMode
     *                                the entity set values mode
     * @param isIgnoreParentCondition
     *                                ignore parent condition flag
     * @return the entity set values
     * @throws UnifyException
     *                        if an error occurs
     */
    EntitySetValues constructEntitySetValues(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy, String tabName,
            EntityDef ownerEntityDef, int entitySetValuesMode, boolean isIgnoreParentCondition) throws UnifyException;

    /**
     * Constructs entity parameter values.
     * 
     * @param ctx
     *                                the form context
     * @param sweepingCommitPolicy
     *                                the sweepingCommitPolicy (optional)
     * @param tabName
     *                                the tab name (optional)
     * @param ownerEntityDef
     *                                the owner entity definition
     * @param entityParamValuesMode
     *                                the entity parameter values mode
     * @param isIgnoreParentCondition
     *                                ignore parent condition flag
     * @return the entity parameter values
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityParamValues constructEntityParamValues(FormContext ctx, SweepingCommitPolicy sweepingCommitPolicy,
            String tabName, EntityDef ownerEntityDef, int entityParamValuesMode, boolean isIgnoreParentCondition)
            throws UnifyException;

    /**
     * Constructs an entry bean table,
     * 
     * @param tableName
     *                        the entry table name
     * @param entryEditPolicy
     *                        the entry edit policy
     * @return the entry bean table
     * @throws UnifyException
     *                        if an error occurs
     */
    BeanListTable constructEntryBeanTable(String tableName, String entryEditPolicy) throws UnifyException;

    /**
     * Constructs an entry bean table,
     * 
     * @param tableName
     *                        the entry table name
     * @param filterGroupDef
     *                        optional filter group definition
     * @param entryEditPolicy
     *                        the entry edit policy
     * @return the entry bean table
     * @throws UnifyException
     *                        if an error occurs
     */
    BeanListTable constructEntryBeanTable(String tableName, FilterGroupDef filterGroupDef, String entryEditPolicy)
            throws UnifyException;

    /**
     * Constructs an entity CRUD object
     * 
     * @param ctx
     *                          the applet context
     * @param appletName
     *                          the applet name
     * @param formEventHandlers
     *                          the for event handler
     * @param baseField
     *                          the base field
     * @param baseId
     *                          the base ID
     * @return the entity CRUD object
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityCRUD constructEntityCRUD(AppletContext ctx, String appletName, EntityFormEventHandlers formEventHandlers,
            String baseField, Object baseId) throws UnifyException;

    /**
     * Constructs an entity CRUD object
     * 
     * @param ctx
     *                             the applet context
     * @param appletName
     *                             the applet name
     * @param formEventHandlers
     *                             the for event handler
     * @param sweepingCommitPolicy
     *                             the sweeping commit policy
     * @param parentEntityDef
     *                             the parent entity definition
     * @param parentInst
     *                             the parent instance
     * @param baseField
     *                             the base field
     * @param baseId
     *                             the base ID
     * @param childListName
     *                             the child list name
     * @param filterGroupDef
     *                             the filter group definition
     * @param viewOnly
     *                             the view only constant
     * @param fixedRows
     *                             the fixed rows
     * @return the entity CRUD object
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityCRUD constructEntityCRUD(AppletContext ctx, String appletName, EntityFormEventHandlers formEventHandlers,
            SweepingCommitPolicy sweepingCommitPolicy, EntityDef parentEntityDef, Entity parentInst, String baseField,
            Object baseId, String childListName, FilterGroupDef filterGroupDef, boolean viewOnly, boolean allowAddition,
            boolean fixedRows) throws UnifyException;

    /**
     * Matches a form bean with applet condition property
     * 
     * @param appletDef
     *                          the applet definition
     * @param form
     *                          the form
     * @param conditionPropName
     *                          the condition applet property name
     * @return true if matched or on no condition otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean formBeanMatchAppletPropertyCondition(AppletDef appletDef, AbstractForm form, String conditionPropName)
            throws UnifyException;

    /**
     * Bumps entity version
     * 
     * @param db
     *                  the database
     * @param entityDef
     *                  the entity definition
     * @param inst
     *                  the entity instance
     * @throws UnifyException
     *                        if an error occurs
     */
    void bumpVersion(Database db, EntityDef entityDef, Entity inst) throws UnifyException;

    /**
     * Bumps entity version
     * 
     * @param entityDef
     *                  the entity definition
     * @param id
     *                  the entity ID
     * @throws UnifyException
     *                        if an error occurs
     */
    void bumpVersion(EntityDef entityDef, Long id) throws UnifyException;

    /**
     * Gets child entity foreign key field name
     * 
     * @param entity
     *                    the entity name
     * @param childEntity
     *                    the child entity name
     * @return the child foreign key field name
     * @throws UnifyException
     *                        if an error occurs
     */
    String getChildFkFieldName(String entity, String childEntity) throws UnifyException;

    /**
     * Gets form tab foreign key field name
     * 
     * @param parentEntityDef
     *                        the parent entity definition
     * @param childFieldName
     *                        the child field name
     * @return the child foreign key field name
     * @throws UnifyException
     *                        if an error occurs
     */
    String getChildFkFieldName(EntityDef parentEntityDef, String childFieldName) throws UnifyException;

    /**
     * Gets child restriction.
     * 
     * @param parentEntityDef
     *                        the parent entity definition
     * @param childFieldName
     *                        the child field name
     * @param parentInst
     *                        parent entity class instance
     * @return the child restriction
     * @throws UnifyException
     *                        if an error occurs
     */
    Restriction getChildRestriction(EntityDef parentEntityDef, String childFieldName, Entity parentInst)
            throws UnifyException;

    /**
     * Populates new child reference field.
     * 
     * @param parentEntityDef
     *                        the parent entity definition
     * @param childFieldName
     *                        the child field name
     * @param parentInst
     *                        parent entity instance
     * @param childInst
     *                        child entity instance
     * @return the child restriction
     * @throws UnifyException
     *                        if an error occurs
     */
    void populateNewChildReferenceFields(EntityDef parentEntityDef, String childFieldName, Entity parentInst,
            Entity childInst) throws UnifyException;

    /**
     * Gets override tenant ID
     * 
     * @param parentEntityDef
     *                        the parent entity definition
     * @param parentInst
     *                        the parent instance
     * @return the override tenant ID otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    Long getOverrideTenantId(EntityDef parentEntityDef, Entity parentInst) throws UnifyException;

    /**
     * Populate entity list-only fields.
     * 
     * @param entityDef
     *                  the entity definition
     * @param inst
     *                  the entity instance
     * @throws UnifyException
     *                        if an error occurs
     */
    void populateListOnlyFields(EntityDef entityDef, Entity inst) throws UnifyException;

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
    DelegateEntityInfo getEntityDelegate(String entityName) throws UnifyException;

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
     *                       the data source name
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
     * Creates an entity by form context.
     * 
     * @param formAppletDef
     *                      the form applet definition
     * @param formContext
     *                      the form context
     * @param scp
     *                      the sweeping commit policy
     * @return the entity action result
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityActionResult createEntityInstByFormContext(AppletDef formAppletDef, FormContext formContext,
            SweepingCommitPolicy scp) throws UnifyException;

    /**
     * Populates entity auto format fields.
     * 
     * @param entityDef
     *                  the entity definition
     * @param inst
     *                  the entity instance
     * @throws UnifyException
     *                        if an error occurs
     */
    void populateAutoFormatFields(EntityDef entityDef, Entity inst) throws UnifyException;

    /**
     * Reverts entity auto format fields.
     * 
     * @param entityDef
     *                  the entity definition
     * @param inst
     *                  the entity instance
     * @throws UnifyException
     *                        if an error occurs
     */
    void revertAutoFormatFields(EntityDef entityDef, Entity inst) throws UnifyException;

    /**
     * Creates an entity instance workflow draft.
     * 
     * @param formAppletDef
     *                      the form applet definition
     * @param formContext
     *                      the form context
     * @param scp
     *                      the sweeping commit policy
     * @return the entity action result
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityActionResult createEntityInstWorkflowDraftByFormContext(AppletDef formAppletDef, FormContext formContext,
            SweepingCommitPolicy scp) throws UnifyException;

    /**
     * Updates an entity by form context.
     * 
     * @param formAppletDef
     *                      the form applet definition
     * @param formContext
     *                      the form context
     * @param scp
     *                      the sweeping commit policy
     * @return the entity action result
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityActionResult updateEntityInstByFormContext(AppletDef formAppletDef, FormContext formContext,
            SweepingCommitPolicy scp) throws UnifyException;

    /**
     * Deletes an entity by form context.
     * 
     * @param formAppletDef
     *                      the form applet definition
     * @param formContext
     *                      the form context
     * @param scp
     *                      the sweeping commit policy
     * @return the entity action result
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityActionResult deleteEntityInstByFormContext(AppletDef formAppletDef, FormContext formContext,
            SweepingCommitPolicy scp) throws UnifyException;

    /**
     * Updates an entity type.
     * 
     * @param entityClass
     *                    the entity class
     * @param id
     *                    the entity ID
     * @param update
     *                    the update object
     * @return number of records updated
     * @throws UnifyException
     *                        if an error occurs
     */
    int updateEntity(Class<? extends Entity> entityClass, Long id, Update update) throws UnifyException;
    
    /**
     * Performs on form construction.
     * 
     * @param formAppletDef
     *                      the form applet definition
     * @param formContext
     *                      the form context
     * @param baseField
     *                      the base field
     * @param create
     *                      the create mode
     * @throws UnifyException
     *                        if an error occurs
     */
    void onFormConstruct(final AppletDef formAppletDef, final FormContext formContext, final String baseField,
            final boolean create) throws UnifyException;

    /**
     * Clears unsatisfactory references.
     * 
     * @param formTabDef
     *                   the form tab definition
     * @param entityDef
     *                   the entity definition
     * @param reader
     *                   the instance reader
     * @param inst
     *                   the entity instance
     * @throws UnifyException
     *                        if an error occurs
     */
    void clearUnsatisfactoryRefs(FormTabDef formTabDef, EntityDef entityDef, ValueStoreReader reader, Entity inst)
            throws UnifyException;

    /**
     * Performs operations on mini-form switch on-change
     * 
     * @param form
     *             the mini-form
     * @throws UnifyException
     *                        if an error occurs
     */
    void onMiniformSwitchOnChange(MiniForm form) throws UnifyException;

    /**
     * Get mapped tenant ID.
     * 
     * @param srcTenantId
     *                    the source tenant ID
     * @return the mapped tenant ID if found otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    Long getMappedDestTenantId(Long srcTenantId) throws UnifyException;

    /**
     * Get unmapped source tenant ID.
     * 
     * @param destTenantId
     *                     the destination tenant ID
     * @return the unmapped source tenant ID otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    Long getUnmappedSrcTenantId(Long destTenantId) throws UnifyException;

    /**
     * Check if mapping provider is available for query entity type
     * 
     * @param query
     *              the query
     * @return true if present otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean isMappingProviderPresent(Query<? extends Entity> query) throws UnifyException;

    /**
     * Check if mapping provider is available for entity type.
     * 
     * @param entityClass
     *                    the entity type
     * @return true if present otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean isMappingProviderPresent(Class<? extends Entity> entityClass) throws UnifyException;

    /**
     * Gets mapping provider for query entity type.
     * 
     * @param query
     *              the query
     * @return the mapping provider
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends BaseMappedEntityProviderContext> MappedEntityProvider<T> getMappingProvider(
            Query<? extends Entity> query) throws UnifyException;

    /**
     * Gets mapping provider for entity type.
     * 
     * @param entityClass
     *                    the entity type
     * @return the mapping provider
     * @throws UnifyException
     *                        if an error occurs
     */
    MappedEntityProvider<? extends BaseMappedEntityProviderContext> getMappingProvider(
            Class<? extends Entity> entityClass) throws UnifyException;

    /**
     * Gets mapping provider source entity.
     * 
     * @param entityClass
     *                    the entity type
     * @return the source entity long name
     * @throws UnifyException
     *                        if an error occurs
     */
    String getProviderSrcEntity(String destEntity);

    /**
     * Consumes an exception and generates an equivalent silent error
     * 
     * @param e
     *                    the exception to consume
     * @param tableResult
     *                    indicates table result
     */
    void consumeExceptionAndGenerateSilentError(Exception e, boolean tableResult);

    /**
     * Consumes an exception and generates an equivalent silent hint
     * 
     * @param e
     *               the exception to consume
     * @param hint
     *               message
     * @param params
     *               optional message parameters
     */
    void consumeExceptionAndGenerateHint(Exception e, String hint, Object... params);

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
     * @return the last state
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
     * Generates a listing report as byte array.
     * 
     * @param reader
     *                the source reader
     * @param options
     *                the listing options
     * @return the generated report object
     * @throws UnifyException
     *                        if an error occurs
     */
    byte[] generateViewListingReportAsByteArray(ValueStoreReader reader, List<GenerateListingReportOptions> options)
            throws UnifyException;

    /**
     * Generates a listing report as byte array.
     * 
     * @param reader
     *                  the source reader
     * @param generator
     *                  the listing generator
     * @param options
     *                  the listing options
     * @return the generated report object
     * @throws UnifyException
     *                        if an error occurs
     */
    byte[] generateViewListingReportAsByteArray(ValueStoreReader reader, String generator, FormListingOptions options)
            throws UnifyException;

    /**
     * Generates a view listing report as byte array.
     * 
     * @param reader
     *                      the source reader
     * @param tableName
     *                      the table name
     * @param dataList
     *                      the data list
     * @param generator
     *                      the listing generator
     * @param properties
     *                      the report properties
     * @param formats
     *                      the formats
     * @param asSpreadSheet
     *                      indicates spread sheet
     * @return the generated report object
     * @throws UnifyException
     *                        if an error occurs
     */
    byte[] generateDetailListingReportAsByteArray(ValueStoreReader reader, String tableName,
            List<? extends Entity> dataList, String generator, Map<String, Object> properties, Formats formats,
            boolean asSpreadSheet) throws UnifyException;

    /**
     * Generates a listing report as byte array.
     * 
     * @param reader
     *                           the source reader
     * @param tableName
     *                           the table name
     * @param dataList
     *                           the data list
     * @param generator
     *                           the listing generator
     * @param properties
     *                           the report properties
     * @param formats
     *                           the formats
     * @param asSpreadSheet
     *                           indicates spread sheet
     * @param preSummaryLines
     *                           pre-table summary lines
     * @param postSummaryLines
     *                           post-table summary lines
     * @param summaryTitleColumn
     *                           summary title column
     * @return the generated report object
     * @throws UnifyException
     *                        if an error occurs
     */
    byte[] generateDetailListingReportAsByteArray(ValueStoreReader reader, String tableName,
            List<? extends Entity> dataList, String generator, Map<String, Object> properties, Formats formats,
            boolean asSpreadSheet, List<TableSummaryLine> preSummaryLines, List<TableSummaryLine> postSummaryLines,
            int summaryTitleColumn) throws UnifyException;

    /**
     * Generates a listing report as byte array.
     * 
     * @param reader
     *                      the source reader
     * @param caseList
     *                      the details case list
     * @param generator
     *                      the listing generator
     * @param properties
     *                      the report properties
     * @param columns
     *                      the number of columns
     * @param asSpreadSheet
     *                      indicates spread sheet
     * @return the generated report object
     * @throws UnifyException
     *                        if an error occurs
     */
    byte[] generateDetailListingReportAsByteArray(ValueStoreReader reader, List<DetailsCase> caseList, String generator,
            Map<String, Object> properties, int columns, boolean asSpreadSheet) throws UnifyException;

    /**
     * Generates a listing report as byte array.
     * 
     * @param reader
     *                        the source reader
     * @param letterGenerator
     *                        the letter generator
     * @throws UnifyException
     *                        if an error occurs
     */
    byte[] generateLetterListingReportAsByteArray(ValueStoreReader reader, String letterGenerator)
            throws UnifyException;

    /**
     * Generates a listing report.
     * 
     * @param reader
     *                the source reader
     * @param options
     *                the report options
     * @return the generated report object
     * @throws UnifyException
     *                        if an error occurs
     */
    Report generateViewListingReport(ValueStoreReader reader, List<GenerateListingReportOptions> options)
            throws UnifyException;

    /**
     * Generates a listing report.
     * 
     * @param reader
     *                  the source reader
     * @param generator
     *                  the listing generator
     * @param options
     *                  the listing options
     * @return the generated report object
     * @throws UnifyException
     *                        if an error occurs
     */
    Report generateViewListingReport(ValueStoreReader reader, String generator, FormListingOptions options)
            throws UnifyException;

    /**
     * Generates a listing report.
     * 
     * @param reader
     *                      the source reader
     * @param tableName
     *                      the table name
     * @param dataList
     *                      the data list
     * @param generator
     *                      the listing generator
     * @param properties
     *                      the report properties
     * @param formats
     *                      the formats
     * @param asSpreadSheet
     *                      indicates spread sheet
     * @return the generated report object
     * @throws UnifyException
     *                        if an error occurs
     */
    Report generateDetailListingReport(ValueStoreReader reader, String tableName, List<? extends Entity> dataList,
            String generator, Map<String, Object> properties, Formats formats, boolean asSpreadSheet)
            throws UnifyException;

    /**
     * Generates a listing report.
     * 
     * @param reader
     *                           the source reader
     * @param tableName
     *                           the table name
     * @param dataList
     *                           the data list
     * @param generator
     *                           the listing generator
     * @param properties
     *                           the report properties
     * @param formats
     *                           the formats
     * @param asSpreadSheet
     *                           indicates spread sheet
     * @param preSummaryLines
     *                           pre-table summary lines
     * @param postSummaryLines
     *                           post-table summary lines
     * @param summaryTitleColumn
     *                           summary title column
     * @return the generated report object
     * @throws UnifyException
     *                        if an error occurs
     */
    Report generateDetailListingReport(ValueStoreReader reader, String tableName, List<? extends Entity> dataList,
            String generator, Map<String, Object> properties, Formats formats, boolean asSpreadSheet,
            List<TableSummaryLine> preSummaryLines, List<TableSummaryLine> postSummaryLines, int summaryTitleColumn)
            throws UnifyException;

    /**
     * Generates a listing report.
     * 
     * @param reader
     *                      the source reader
     * @param caseList
     *                      the details case list
     * @param generator
     *                      the listing generator
     * @param properties
     *                      the report properties
     * @param columns
     *                      the number of columns
     * @param asSpreadSheet
     *                      indicates spread sheet
     * @return the generated report object
     * @throws UnifyException
     *                        if an error occurs
     */
    Report generateDetailListingReport(ValueStoreReader reader, List<DetailsCase> caseList, String generator,
            Map<String, Object> properties, int columns, boolean asSpreadSheet) throws UnifyException;

    /**
     * Generates a listing report as byte array.
     * 
     * @param reader
     *                        the source reader
     * @param letterGenerator
     *                        the letter generator
     * @throws UnifyException
     *                        if an error occurs
     */
    Report generateLetterListingReport(ValueStoreReader reader, String letterGenerator) throws UnifyException;

    /**
     * Generates a listing report.
     * 
     * @param outputStream
     *                     the output stream
     * @param reader
     *                     the source reader
     * @param options
     *                     the listing options
     * @return the generated report object
     * @throws UnifyException
     *                        if an error occurs
     */
    void generateViewListingReportToOutputStream(OutputStream outputStream, ValueStoreReader reader,
            List<GenerateListingReportOptions> options) throws UnifyException;

    /**
     * Generates a listing report.
     * 
     * @param outputStream
     *                     the output stream
     * @param reader
     *                     the source reader
     * @param generator
     *                     the listing generator
     * @param options
     *                     the listing options
     * @return the generated report object
     * @throws UnifyException
     *                        if an error occurs
     */
    void generateViewListingReportToOutputStream(OutputStream outputStream, ValueStoreReader reader, String generator,
            FormListingOptions options) throws UnifyException;

    /**
     * Generates a listing report.
     * 
     * @param outputStream
     *                      the output stream
     * @param reader
     *                      the source reader
     * @param tableName
     *                      the table name
     * @param dataList
     *                      the data list
     * @param generator
     *                      the listing generator
     * @param properties
     *                      the report properties
     * @param formats
     *                      the formats
     * @param asSpreadSheet
     *                      indicates spread sheet
     * @throws UnifyException
     *                        if an error occurs
     */
    void generateDetailListingReportToOutputStream(OutputStream outputStream, ValueStoreReader reader, String tableName,
            List<? extends Entity> dataList, String generator, Map<String, Object> properties, Formats formats,
            boolean asSpreadSheet) throws UnifyException;

    /**
     * Generates a listing report.
     * 
     * @param outputStream
     *                           the output stream
     * @param reader
     *                           the source reader
     * @param tableName
     *                           the table name
     * @param dataList
     *                           the data list
     * @param generator
     *                           the listing generator
     * @param properties
     *                           the report properties
     * @param formats
     *                           the formats
     * @param asSpreadSheet
     *                           indicates spread sheet
     * @param preSummaryLines
     *                           pre-table summary lines
     * @param postSummaryLines
     *                           post-table summary lines
     * @param summaryTitleColumn
     *                           summary title column
     * @throws UnifyException
     *                        if an error occurs
     */
    void generateDetailListingReportToOutputStream(OutputStream outputStream, ValueStoreReader reader, String tableName,
            List<? extends Entity> dataList, String generator, Map<String, Object> properties, Formats formats,
            boolean asSpreadSheet, List<TableSummaryLine> preSummaryLines, List<TableSummaryLine> postSummaryLines,
            int summaryTitleColumn) throws UnifyException;

    /**
     * Generates a listing report.
     * 
     * @param outputStream
     *                      the output stream
     * @param reader
     *                      the source reader
     * @param caseList
     *                      the details case list
     * @param generator
     *                      the listing generator
     * @param properties
     *                      the report properties
     * @param columns
     *                      the number of columns
     * @param asSpreadSheet
     *                      indicates spread sheet
     * @throws UnifyException
     *                        if an error occurs
     */
    void generateDetailListingReportToOutputStream(OutputStream outputStream, ValueStoreReader reader,
            List<DetailsCase> caseList, String generator, Map<String, Object> properties, int columns,
            boolean asSpreadSheet) throws UnifyException;

    /**
     * Generates a listing report as byte array.
     * 
     * @param outputStream
     *                     the output stream
     * @param reader
     *                     the source reader
     * @param letterName
     *                     the letter name
     * @throws UnifyException
     *                        if an error occurs
     */
    void generateLetterListingReportToOutputStream(OutputStream outputStream, ValueStoreReader reader,
            String letterGenerator) throws UnifyException;
}

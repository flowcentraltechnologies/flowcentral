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
package com.flowcentraltech.flowcentral.system.business;

import java.util.List;
import java.util.Optional;

import com.flowcentraltech.flowcentral.common.business.FlowCentralService;
import com.flowcentraltech.flowcentral.common.constants.SecuredLinkType;
import com.flowcentraltech.flowcentral.common.data.SecuredLinkInfo;
import com.flowcentraltech.flowcentral.system.data.CredentialDef;
import com.flowcentraltech.flowcentral.system.data.LicenseDef;
import com.flowcentraltech.flowcentral.system.entities.Credential;
import com.flowcentraltech.flowcentral.system.entities.CredentialQuery;
import com.flowcentraltech.flowcentral.system.entities.DownloadLog;
import com.flowcentraltech.flowcentral.system.entities.MappedTenant;
import com.flowcentraltech.flowcentral.system.entities.MappedTenantQuery;
import com.flowcentraltech.flowcentral.system.entities.Module;
import com.flowcentraltech.flowcentral.system.entities.ModuleQuery;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;

/**
 * System module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface SystemModuleService extends FlowCentralService {

    /**
     * Creates a new open link.
     * 
     * @param title
     *                        the title (optional)
     * @param openUrl
     *                        the open URL
     * @param entityId
     *                        the target entity ID
     * @param validityMinutes
     *                        the validity minutes
     * @return the secured link information
     * @throws UnifyException
     *                        if an error occurs
     */
    SecuredLinkInfo getNewOpenLink(String title, String openUrl, Long entityId, int validityMinutes)
            throws UnifyException;

    /**
     * Creates a new a secured link.
     * 
     * @param type
     *                    secured link type
     * @param title
     *                    the title
     * @param contentPath
     *                    the content path
     * @return the secured link information
     * @throws UnifyException
     *                        if an error occurs
     */
    SecuredLinkInfo getNewSecuredLink(SecuredLinkType type, String title, String contentPath) throws UnifyException;

    /**
     * Creates a new secured link.
     * 
     * @param type
     *                        secured link type
     * @param title
     *                        the title
     * @param contentPath
     *                        the content path
     * @param assignedLoginId
     *                        the assigned login ID
     * @return the secured link information
     * @throws UnifyException
     *                        if an error occurs
     */
    SecuredLinkInfo getNewSecuredLink(SecuredLinkType type, String title, String contentPath, String assignedLoginId)
            throws UnifyException;

    /**
     * Creates a new secured link.
     * 
     * @param type
     *                        secured link type
     * @param title
     *                        the title
     * @param contentPath
     *                        the content path
     * @param assignedLoginId
     *                        the assigned login ID
     * @param assignedRole
     *                        the assigned role
     * @return the secured link information
     * @throws UnifyException
     *                        if an error occurs
     */
    SecuredLinkInfo getNewSecuredLink(SecuredLinkType type, String title, String contentPath, String assignedLoginId,
            String assignedRole) throws UnifyException;

    /**
     * Gets tenant IDs with primary tenant ID mapped.
     * 
     * @return the list of tenant IDs
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Long> getPrimaryMappedTenantIds() throws UnifyException;

    /**
     * Get mapped tenant ID.
     * 
     * @param srcTenantId
     *                 the source tenant ID
     * @return the mapped tenant ID otherwise null
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
     * Get mapped tenant.
     * 
     * @param tenantId
     *                 the tenant ID
     * @return the mapped tenant
     * @throws UnifyException
     *                        if an error occurs
     */
    MappedTenant findPrimaryMappedTenant(Long tenantId) throws UnifyException;
   
    /**
     * Gets a count of tenants defined in the system.
     * 
     * @return the tenant count
     * @throws UnifyException
     *                        if an error occurs
     */
    int getTenantCount() throws UnifyException;
    
    /**
     * Find tenants by supplied query.
     * 
     * @param query
     *              the search query
     * @return the list of tenants
     * @throws UnifyException
     *                        if an error occurs
     */
    List<MappedTenant> findTenants(MappedTenantQuery query) throws UnifyException;

    /**
     * Gets instance licensing.
     * 
     * @return the instance licensing
     * @throws UnifyException
     *                        if an error occurs
     */
    LicenseDef getInstanceLicensing() throws UnifyException;
    
    /**
     * Creates a download log.
     * 
     * @param downloadLog
     *                    the log to create
     * @return the created download log ID
     * @throws UnifyException
     *                        if an error occurs
     */
    Long createDownloadLog(DownloadLog downloadLog) throws UnifyException;
    
    /**
     * Finds credentials based on supplied query.
     * 
     * @param query
     *              the credential query
     * @return the list of credential
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Credential> findCredentials(CredentialQuery query) throws UnifyException;

    /**
     * Gets an credential definition.
     * 
     * @param credName
     *                 the credential name
     * @return the credential definition
     * @throws UnifyException
     *                        if an error occurs
     */
    CredentialDef getCredentialDef(String credName) throws UnifyException;

    /**
     * Creates a new module.
     * 
     * @param module
     *               the module data
     * @return the created module ID
     * @throws UnifyException
     *                        if an error occurs
     */
    Long createModule(Module module) throws UnifyException;

    /**
     * Find modules by supplied query.
     * 
     * @param query
     *              the search query
     * @return the list of module
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Module> findModules(ModuleQuery query) throws UnifyException;

    /**
     * Find module by supplied name.
     * 
     * @param moduleName
     *              the module name
     * @return the module
     * @throws UnifyException
     *                        if an error occurs
     */
    Module findModule(String moduleName) throws UnifyException;

    /**
     * Gets the unique ID of a module.
     * 
     * @param moduleName
     *                   the module name
     * @return the module ID
     * @throws UnifyException
     *                        if an error occurs
     */
    Optional<Long> getModuleId(String moduleName) throws UnifyException;

    /**
     * Gets the module name for supplied ID.
     * 
     * @param moduleId
     *                   the module ID
     * @return the module name
     * @throws UnifyException
     *                        if an error occurs
     */
    String getModuleName(Long moduleId) throws UnifyException;

    /**
     * Gets all the module names in this application.
     * 
     * @return the list of module names
     * @throws UnifyException
     *                        if an error occurs
     */
    List<String> getAllModuleNames() throws UnifyException;

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
     * Updates the value field of a system parameter.
     * 
     * @param code
     *              the system parameter code
     * @param value
     *              the value to set
     * @return the number of records updated
     * @throws UnifyException
     *                        if an error occurs
     */
    int setSysParameterValue(String code, Object value) throws UnifyException;

    /**
     * Get names system parameters.
     * 
     * @return list of name system parameters
     * @throws UnifyException
     *                        if an error occurs
     */
    List<? extends Listable> getNamesSystemParameters() throws UnifyException;

    /**
     * Gets contact system parameters.
     * 
     * @return list of contact system parameters
     * @throws UnifyException
     *                        if an error occurs
     */
    List<? extends Listable> getContactSystemParameters() throws UnifyException;
    
    /**
     * Gets filter system parameters.
     * 
     * @return list of filter system parameters
     * @throws UnifyException
     *                        if an error occurs
     */
    List<? extends Listable> getFilterSystemParameters() throws UnifyException;
}

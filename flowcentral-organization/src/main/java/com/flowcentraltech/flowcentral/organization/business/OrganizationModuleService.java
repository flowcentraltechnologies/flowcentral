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
package com.flowcentraltech.flowcentral.organization.business;

import java.util.List;
import java.util.Optional;

import com.flowcentraltech.flowcentral.common.business.FlowCentralService;
import com.flowcentraltech.flowcentral.organization.entities.Branch;
import com.flowcentraltech.flowcentral.organization.entities.BranchQuery;
import com.flowcentraltech.flowcentral.organization.entities.Department;
import com.flowcentraltech.flowcentral.organization.entities.DepartmentQuery;
import com.flowcentraltech.flowcentral.organization.entities.MappedBranch;
import com.flowcentraltech.flowcentral.organization.entities.MappedBranchQuery;
import com.flowcentraltech.flowcentral.organization.entities.MappedDepartment;
import com.flowcentraltech.flowcentral.organization.entities.MappedDepartmentQuery;
import com.flowcentraltech.flowcentral.organization.entities.Privilege;
import com.flowcentraltech.flowcentral.organization.entities.PrivilegeCategory;
import com.flowcentraltech.flowcentral.organization.entities.PrivilegeCategoryQuery;
import com.flowcentraltech.flowcentral.organization.entities.PrivilegeQuery;
import com.flowcentraltech.flowcentral.organization.entities.Role;
import com.flowcentraltech.flowcentral.organization.entities.RoleQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.FactoryMap;

/**
 * Organization module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface OrganizationModuleService extends FlowCentralService {

    /**
     * Finds roles using supplied query.
     * 
     * @param query
     *              the query to use
     * @return the role list
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Role> findRoles(RoleQuery query) throws UnifyException;

    /**
     * Gets role code for supplied role ID.
     * 
     * @param roleId
     *               the role ID
     * @return the role code
     * @throws UnifyException
     *                        if role with ID does not exist. if an error occurs
     */
    String getRoleCode(Long roleId) throws UnifyException;

    /**
     * Finds privilege categories by query.
     * 
     * @param query
     *              the search query
     * @return the privilege categories
     * @throws UnifyException
     *                        if an error occurs
     */
    List<PrivilegeCategory> findPrivilegeCategories(PrivilegeCategoryQuery query) throws UnifyException;

    /**
     * Finds privileges using supplied query.
     * 
     * @param query
     *              the query to use
     * @return the privilege list
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Privilege> findPrivileges(PrivilegeQuery query) throws UnifyException;

    /**
     * Gets branch by ID
     * 
     * @param branchId
     *                 the branch ID
     * @return the branch
     * @throws UnifyException
     *                        if not found. If an error occurs
     */
    Branch getBranch(Long branchId) throws UnifyException;
    
    /**
     * Gets branch by criteria.
     * 
     * @param query
     *              the criteria object
     * @return the branch
     * @throws UnifyException
     *                        if an error occurs
     */
    Branch findBranch(BranchQuery query) throws UnifyException;
    
    /**
     * Gets branches by criteria.
     * 
     * @param query
     *              the criteria object
     * @return the list of branches
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Branch> findBranches(BranchQuery query) throws UnifyException;
    
    /**
     * Get branch ID.
     * 
     * @param branchCode
     *                   the branch code
     * @return the branch ID if found otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    Long getBranchId(String branchCode) throws UnifyException;

    /**
     * Get branch ID.
     * 
     * @param query
     *                   the branch query
     * @return the branch ID if found otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    Optional<Long> getBranchId(BranchQuery query) throws UnifyException;

    /**
     * Gets associated branch IDs by hub with branch itself included.
     * 
     * @param branchId
     *                 the branch ID
     * @return the associated branch IDs
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Long> getAssociatedBranchIds(Long branchId) throws UnifyException;

    /**
     * Get department ID..
     * 
     * @param departmentCode
     *                       the department code
     * @return the department ID if found otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    Long getDepartmentId(String departmentCode) throws UnifyException;

    /**
     * Invalidates role privileges cache.
     * 
     * @param roleCodes
     *                  the role codes
     * @throws UnifyException
     *                        if an error occurs
     */
    void invalidateRolePrivilegesCache(String... roleCodes) throws UnifyException;

    /**
     * Retrieves a new instance of a mapped branch code factory map.
     * 
     * @return the factory map
     * @throws UnifyException
     *                        if an error occurs
     */
    FactoryMap<Long, String> getMappedBranchCodeFactoryMap() throws UnifyException;

    /**
     * Retrieves a new instance of a mapped department code factory map.
     * 
     * @return the factory map
     * @throws UnifyException
     *                        if an error occurs
     */
    FactoryMap<Long, String> getMappedDepartmentCodeFactoryMap() throws UnifyException;

    /**
     * Gets mapped department code.
     * 
     * @param departmentId
     *                     the department ID
     * @return the department code
     * @throws UnifyException
     *                        if an error occurs
     */
    String getMappedDepartmentCode(Long departmentId) throws UnifyException;

    /**
     * Gets mapped branch code.
     * 
     * @param branchId
     *                 the branch ID
     * @return the branch code
     * @throws UnifyException
     *                        if an error occurs
     */
    String getMappedBranchCode(Long branchId) throws UnifyException;

    /**
     * Finds mapped departments using supplied query.
     * 
     * @param query
     *              the query to use
     * @return list of mapped departments
     * @throws UnifyException
     *                        if an error occurs
     */
    List<MappedDepartment> findMappedDepartments(MappedDepartmentQuery query) throws UnifyException;

    /**
     * Finds mapped departments using supplied query.
     * 
     * @param query
     *              the query to use
     * @return list of mapped branches
     * @throws UnifyException
     *                        if an error occurs
     */
    List<MappedBranch> findMappedBranches(MappedBranchQuery query) throws UnifyException;

    /**
     * Finds department by criteria.
     * 
     * @param query
     *              the department query
     * @return the department
     * @throws UnifyException
     *                        if an error occurs
     */
    Department findDepartment(DepartmentQuery query) throws UnifyException;

}

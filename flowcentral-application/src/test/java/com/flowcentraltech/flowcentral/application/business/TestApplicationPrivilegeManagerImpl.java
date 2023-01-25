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

import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.common.business.ApplicationPrivilegeManager;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Test Appliaction Privilege Manager.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("test-applicationprivilegemanager")
public class TestApplicationPrivilegeManagerImpl extends AbstractUnifyComponent implements ApplicationPrivilegeManager {

    @Override
    public void registerPrivilege(Long applicationId, String privilegeCategoryCode, String privilegeCode,
            String privilegeDesc) throws UnifyException {

    }

    @Override
    public boolean isRegisteredPrivilege(String privilegeCategoryCode, String privilegeCode) throws UnifyException {
        return false;
    }

    @Override
    public boolean unregisterPrivilege(Long applicationId, String privilegeCategoryCode, String privilegeCode)
            throws UnifyException {
        return false;
    }

    @Override
    public void unregisterApplicationPrivileges(Long applicationId) throws UnifyException {
        
    }

    @Override
    public List<String> findRolePrivileges(String privilegeCategoryCode, String roleCode) throws UnifyException {
        return Collections.emptyList();
    }

    @Override
    public boolean assignPrivilegeToRole(String roleCode, String privilegeCode) throws UnifyException {
        return true;
    }

    @Override
    public boolean isRoleWithPrivilege(String roleCode, String privilegeCode) throws UnifyException {
        return false;
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

}

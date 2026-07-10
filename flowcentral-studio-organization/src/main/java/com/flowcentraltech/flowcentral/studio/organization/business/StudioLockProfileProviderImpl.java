/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.studio.organization.business;

import com.flowcentraltech.flowcentral.organization.business.OrganizationModuleService;
import com.flowcentraltech.flowcentral.organization.entities.MappedBranch;
import com.flowcentraltech.flowcentral.security.business.SecurityModuleService;
import com.flowcentraltech.flowcentral.security.entities.User;
import com.flowcentraltech.flowcentral.studio.business.StudioLockProfileProvider;
import com.flowcentraltech.flowcentral.studio.business.data.LockProfile;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Studio lock profile provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("studiolock-profileprovider")
public class StudioLockProfileProviderImpl extends AbstractUnifyComponent implements StudioLockProfileProvider {

    @Configurable
    private SecurityModuleService securityModuleService;

    @Configurable
    private OrganizationModuleService organizationModuleService;

    @Override
    public LockProfile provide(String userLoginId) throws UnifyException {
        final User user = securityModuleService.findUser(userLoginId);
        final MappedBranch userBranch = user.getBranchId() != null
                ? organizationModuleService.findMappedBranch(user.getBranchId())
                : null;
        String branchDesc = userBranch != null ? userBranch.getDescription() : null;
        branchDesc = branchDesc == null ? getSessionMessage("application.no.branch") : branchDesc;
        return new LockProfile(user.getFullName(), branchDesc);
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

}

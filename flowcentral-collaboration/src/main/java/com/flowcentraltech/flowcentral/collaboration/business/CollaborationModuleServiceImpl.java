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
package com.flowcentraltech.flowcentral.collaboration.business;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.collaboration.constants.CollaborationModuleNameConstants;
import com.flowcentraltech.flowcentral.collaboration.entities.CollaborationFreeze;
import com.flowcentraltech.flowcentral.collaboration.entities.CollaborationFreezeQuery;
import com.flowcentraltech.flowcentral.collaboration.entities.CollaborationLock;
import com.flowcentraltech.flowcentral.collaboration.entities.CollaborationLockHistory;
import com.flowcentraltech.flowcentral.collaboration.entities.CollaborationLockQuery;
import com.flowcentraltech.flowcentral.collaboration.entities.FreezeUnfreeze;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.common.business.CollaborationProvider;
import com.flowcentraltech.flowcentral.common.constants.CollaborationType;
import com.flowcentraltech.flowcentral.common.data.CollaborationLockInfo;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Synchronized;
import com.tcdng.unify.core.annotation.Transactional;

/**
 * Implementation of collaboration module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(CollaborationModuleNameConstants.COLLABORATION_MODULE_SERVICE)
public class CollaborationModuleServiceImpl extends AbstractFlowCentralService
        implements CollaborationModuleService, CollaborationProvider {

    private static final List<String> collaborationAppletList = Collections.unmodifiableList(
            Arrays.asList("collaboration.manageHeldCollaborationLock", "collaboration.manageFreezeUnfreeze"));

    @Configurable
    private SystemModuleService systemModuleService;

    @Override
    public int releaseAllLocks(CollaborationLockQuery query) throws UnifyException {
        return environment().deleteAll(query);
    }

	@Synchronized("clb:freezeunfreezecomponents")
	@Override
	public int freezeComponents(List<FreezeUnfreeze> freezeUnfreezeList) throws UnifyException {
		int result = 0;
		CollaborationFreeze collaborationFreeze = new CollaborationFreeze();
		for (FreezeUnfreeze freeze : freezeUnfreezeList) {
			if (environment().countAll(new CollaborationFreezeQuery().type(freeze.getType())
					.applicationName(freeze.getApplicationName()).resourceName(freeze.getResourceName())) == 0) {
				collaborationFreeze.setType(freeze.getType());
				collaborationFreeze.setApplicationName(freeze.getApplicationName());
				collaborationFreeze.setResourceName(freeze.getResourceName());
				collaborationFreeze.setResourceDesc(freeze.getResourceDesc());
				environment().create(collaborationFreeze);
				result++;
			}
		}

		return result;
	}

	@Synchronized("clb:freezeunfreezecomponents")
	@Override
	public int unfreezeComponents(List<FreezeUnfreeze> freezeUnfreezeList) throws UnifyException {
		int result = 0;
		for (FreezeUnfreeze freeze : freezeUnfreezeList) {
			result += environment().deleteAll(new CollaborationFreezeQuery().type(freeze.getType())
					.applicationName(freeze.getApplicationName()).resourceName(freeze.getResourceName()));
		}

		return result;
	}

	@Override
    public List<String> getCollaborationApplets() throws UnifyException {
        return collaborationAppletList;
    }

    @Override
    public CollaborationLockInfo getLockInfo(CollaborationType type, String resourceName) throws UnifyException {
        CollaborationLock collaborationLock = environment()
                .find(new CollaborationLockQuery().type(type).resourceName(resourceName));
        if (collaborationLock != null) {
            return new CollaborationLockInfo(collaborationLock.getType(), collaborationLock.getResourceName(),
                    collaborationLock.getCreatedBy(), collaborationLock.getCreateDt());
        }

        return null;
    }

    @Override
    public boolean isLocked(CollaborationType type, String resourceName) throws UnifyException {
        return environment().find(new CollaborationLockQuery().type(type).resourceName(resourceName)) != null;
    }

    @Override
	public boolean isFrozen(CollaborationType type, String resourceName) throws UnifyException {
        return environment().countAll(new CollaborationFreezeQuery().type(type).resourceName(resourceName)) > 0;
	}

	@Override
    public boolean isLockedBy(CollaborationType type, String resourceName, String userLoginId) throws UnifyException {
        CollaborationLock collaborationLock = environment()
                .find(new CollaborationLockQuery().type(type).resourceName(resourceName));
        if (collaborationLock != null) {
            return collaborationLock.getCreatedBy().equals(userLoginId);
        }

        return false;
    }

    @Synchronized("clb:resourcelock")
    @Override
    public boolean lock(CollaborationType type, String resourceName, String userLoginId) throws UnifyException {
        CollaborationLock collaborationLock = environment()
                .find(new CollaborationLockQuery().type(type).resourceName(resourceName));
        if (collaborationLock != null) {
            return collaborationLock.getCreatedBy().equals(userLoginId);
        }

        collaborationLock = new CollaborationLock(type, resourceName);
        collaborationLock.setCreatedBy(userLoginId);
        environment().create(collaborationLock);

        CollaborationLockHistory collaborationLockHistory = new CollaborationLockHistory(type, resourceName);
        collaborationLockHistory.setCreatedBy(userLoginId);
        environment().create(collaborationLockHistory);

        return true;
    }

    @Synchronized("clb:resourcelock")
    @Override
    public boolean unlock(CollaborationType type, String resourceName, String userLoginId) throws UnifyException {
        return environment().deleteAll(
                new CollaborationLockQuery().type(type).resourceName(resourceName).createdBy(userLoginId)) > 0;
    }

    @Synchronized("clb:resourcelock")
    @Override
    public boolean unlock(CollaborationType type, String resourceName) throws UnifyException {
        return environment().deleteAll(new CollaborationLockQuery().type(type).resourceName(resourceName)) > 0;
    }

    @Synchronized("clb:resourcelock")
    @Override
    public boolean grabLock(CollaborationType type, String resourceName, String userLoginId) throws UnifyException {
        unlock(type, resourceName);
        return lock(type, resourceName, userLoginId);
    }

    @Override
    protected void doInstallModuleFeatures(final ModuleInstall moduleInstall) throws UnifyException {

    }

}

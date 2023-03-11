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
package com.flowcentraltech.flowcentral.collaboration.business;

import java.util.List;

import com.flowcentraltech.flowcentral.collaboration.entities.CollaborationLockQuery;
import com.flowcentraltech.flowcentral.collaboration.entities.FreezeUnfreeze;
import com.flowcentraltech.flowcentral.common.business.FlowCentralService;
import com.tcdng.unify.core.UnifyException;

/**
 * Collaboration module service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface CollaborationModuleService extends FlowCentralService {

	/**
	 * Releases all collaboration locks.
	 * 
	 * @param query the release criteria
	 * @return the number of records deleted
	 * @throws UnifyException if an error occurs
	 */
	int releaseAllLocks(CollaborationLockQuery query) throws UnifyException;

	/**
	 * Freeze components.
	 * 
	 * @param freezeUnfreezeList the freeze list
	 * @return the number of components frozen.
	 * @throws UnifyException if an error occurs
	 */
	int freezeComponents(List<FreezeUnfreeze> freezeUnfreezeList) throws UnifyException;

	/**
	 * Unfreeze components.
	 * 
	 * @param freezeUnfreezeList the unfreeze list
	 * @return the number of components unfrozen.
	 * @throws UnifyException if an error occurs
	 */
	int unfreezeComponents(List<FreezeUnfreeze> freezeUnfreezeList) throws UnifyException;
}

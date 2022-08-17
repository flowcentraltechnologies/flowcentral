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
package com.flowcentraltech.flowcentral.collaboration.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.collaboration.constants.FrozenStatus;
import com.flowcentraltech.flowcentral.common.constants.CollaborationType;
import com.flowcentraltech.flowcentral.common.entities.BaseEntity;

/**
 * Represents freeze and unfreeze pseudo entity.
 * 
 * @author Lateef
 *
 */
public class FreezeUnfreeze extends BaseEntity {

    private CollaborationType type;

    private FrozenStatus status;

    private String applicationName;

    private String resourceName;

    private String resourceDesc;

    private String typeDesc;

    private String statusDesc;

    private String frozenBy;

    private Date frozenOn;

	@Override
	public String getDescription() {
		return resourceDesc;
	}

	public CollaborationType getType() {
		return type;
	}

	public void setType(CollaborationType type) {
		this.type = type;
	}

	public FrozenStatus getStatus() {
		return status;
	}

	public void setStatus(FrozenStatus status) {
		this.status = status;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getResourceDesc() {
		return resourceDesc;
	}

	public void setResourceDesc(String resourceDesc) {
		this.resourceDesc = resourceDesc;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public String getFrozenBy() {
		return frozenBy;
	}

	public void setFrozenBy(String frozenBy) {
		this.frozenBy = frozenBy;
	}

	public Date getFrozenOn() {
		return frozenOn;
	}

	public void setFrozenOn(Date frozenOn) {
		this.frozenOn = frozenOn;
	}

}

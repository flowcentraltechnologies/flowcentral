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

package com.flowcentraltech.flowcentral.dashboard.entities;

import java.util.List;

import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Dashboard entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_DASHBOARD")
public class Dashboard extends BaseApplicationEntity {

	@ForeignKey
	private RecordStatus status;
	
    @Column
    private int sections;

    @Column
    private boolean allowSecondaryTenants;

    @ListOnly(key = "status", property = "description")
    private String statusDesc;
    
    @ChildList
    private List<DashboardTile> tileList;

    public Dashboard() {
		this.status = RecordStatus.ACTIVE;
	}

	public int getSections() {
        return sections;
    }

    public void setSections(int sections) {
        this.sections = sections;
    }

    public boolean isAllowSecondaryTenants() {
        return allowSecondaryTenants;
    }

    public void setAllowSecondaryTenants(boolean allowSecondaryTenants) {
        this.allowSecondaryTenants = allowSecondaryTenants;
    }

    public RecordStatus getStatus() {
		return status;
	}

	public void setStatus(RecordStatus status) {
		this.status = status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public List<DashboardTile> getTileList() {
        return tileList;
    }

    public void setTileList(List<DashboardTile> tileList) {
        this.tileList = tileList;
    }

}

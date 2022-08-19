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

package com.flowcentraltech.flowcentral.dashboard.data;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.data.BaseApplicationEntityDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.configuration.constants.DashboardTileType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Dashboard definition object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class DashboardDef extends BaseApplicationEntityDef {

	private int sections;

	private RecordStatus status;

	private List<List<DashboardTileDef>> tileList;

	private DashboardDef(int sections, RecordStatus status, List<List<DashboardTileDef>> tileList,
			ApplicationEntityNameParts nameParts, String description, Long id, long version) {
		super(nameParts, description, id, version);
		this.sections = sections;
		this.status = status;
		this.tileList = tileList;
	}

	public int getSections() {
		return sections;
	}

	public RecordStatus getStatus() {
		return status;
	}

	public boolean isActive() {
		return RecordStatus.ACTIVE.equals(status);
	}

	public List<DashboardTileDef> getTileList(int section) {
		return tileList.get(section);
	}

	public static Builder newBuilder(int sections, RecordStatus status, String longName, String description, Long id,
			long version) {
		return new Builder(sections, status, longName, description, id, version);
	}

	public static class Builder {

		private int sections;

		private RecordStatus status;

		private List<DashboardTileDef> tileList;

		private String longName;

		private String description;

		private Long id;

		private long version;

		public Builder(int sections, RecordStatus status, String longName, String description, Long id, long version) {
			this.sections = sections <= 0 ? 1 : sections;
			this.status = status;
			this.longName = longName;
			this.description = description;
			this.id = id;
			this.version = version;
			this.tileList = new ArrayList<DashboardTileDef>();
		}

		public Builder addTile(DashboardTileType type, String name, String description, String chart, int section,
				int index) {
			if (section < 0 || section >= sections) {
				throw new RuntimeException(
						"Can not add tile to section [" + section + "] for dashboard with [" + sections + "] sections");
			}

			tileList.add(new DashboardTileDef(type, name, description, chart, section, index));
			return this;
		}

		@SuppressWarnings("unchecked")
		public DashboardDef build() throws UnifyException {
			Object[] tileListArr = new Object[sections];
			for (int i = 0; i < sections; i++) {
				tileListArr[i] = new ArrayList<DashboardTileDef>();
			}

			for (DashboardTileDef dashboardTileDef : tileList) {
				((List<DashboardTileDef>) tileListArr[dashboardTileDef.getSection()]).add(dashboardTileDef);
			}

			List<List<DashboardTileDef>> finalTileList = new ArrayList<List<DashboardTileDef>>();
			for (Object obj : tileListArr) {
				List<DashboardTileDef> tileList = (List<DashboardTileDef>) obj;
				DataUtils.sortAscending(tileList, DashboardTileDef.class, "index");
				finalTileList.add(DataUtils.unmodifiableList(tileList));
			}

			return new DashboardDef(sections, status, DataUtils.unmodifiableList(finalTileList),
					ApplicationNameUtils.getApplicationEntityNameParts(longName), description, id, version);
		}
	}

	@Override
	public String toString() {
		return "DashboardDef [sections=" + sections + ", tileList=" + tileList + "]";
	}

}

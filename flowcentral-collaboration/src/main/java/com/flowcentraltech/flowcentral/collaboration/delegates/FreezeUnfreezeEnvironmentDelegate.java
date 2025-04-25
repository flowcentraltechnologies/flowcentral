/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.collaboration.delegates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.entities.BaseApplicationEntity;
import com.flowcentraltech.flowcentral.application.util.ApplicationCollaborationUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.collaboration.constants.FrozenStatus;
import com.flowcentraltech.flowcentral.collaboration.entities.CollaborationFreeze;
import com.flowcentraltech.flowcentral.collaboration.entities.CollaborationFreezeQuery;
import com.flowcentraltech.flowcentral.collaboration.entities.FreezeUnfreeze;
import com.flowcentraltech.flowcentral.common.constants.CollaborationType;
import com.flowcentraltech.flowcentral.connect.common.data.DataSourceRequest;
import com.flowcentraltech.flowcentral.delegate.business.AbstractPseudoEntityEnvironmentDelegate;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.core.criterion.Order;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Freeze/unfreeze environment delegate.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(name = "freezeunfreeze-environmentdelegate", description = "$m{collaboration.freezeunfreeze.delegate}")
public class FreezeUnfreezeEnvironmentDelegate extends AbstractPseudoEntityEnvironmentDelegate<FreezeUnfreeze> {

    @Override
	protected long countAll(DataSourceRequest req) throws UnifyException {
		QueryInfo info = getQueryInfo(req);
		if (info.validQuery()) {
			if (info.frozen()) {
				CollaborationFreezeQuery query = new CollaborationFreezeQuery()
						.applicationName(info.getApplicationName()).type(info.getType());
				return environment().countAll(query);
			} else if (info.unfrozen()) {
				Query<? extends Entity> query = new CollaborationFreezeQuery()
						.applicationName(info.getApplicationName()).type(info.getType());
				int frozen = environment().countAll(query);
				query = Query.of(ApplicationCollaborationUtils.getEntityClass(info.getType()))
						.addEquals("applicationName", info.getApplicationName());
				return environment().countAll(query) - frozen;
			} else {
				Query<? extends Entity> query = Query.of(ApplicationCollaborationUtils.getEntityClass(info.getType()))
						.addEquals("applicationName", info.getApplicationName());
				return environment().countAll(query);
			}
		}

		return 0;
	}

	@Override
	protected List<FreezeUnfreeze> findAll(DataSourceRequest req) throws UnifyException {
		QueryInfo info = getQueryInfo(req);
		if (info.validQuery()) {
			List<FreezeUnfreeze> list = new ArrayList<FreezeUnfreeze>();
			if (info.frozen()) {
				polulateFrozen(info, req, list);
			} else if (info.unfrozen()) {
				polulateUnfrozen(info, req, list);
			} else {
				polulateFrozen(info, req, list);
				polulateUnfrozen(info, req, list);
			}

			Order order = InputWidgetUtils.getOrder(req.getOrder());
			if (order != null) {
				for(Order.Part part: order.getParts()) {
					if (part.isAscending()) {
						DataUtils.sortAscending(list, FreezeUnfreeze.class, part.getField());
					} else {
						DataUtils.sortDescending(list, FreezeUnfreeze.class, part.getField());
					}
				}
			}

			return list;
		}

		return Collections.emptyList();
	}

	@Override
	protected List<FreezeUnfreeze> listAll(DataSourceRequest req) throws UnifyException {
		return findAll(req);
	}

	private List<String> getFrozenResources(QueryInfo info) throws UnifyException {
		CollaborationFreezeQuery query = new CollaborationFreezeQuery().applicationName(info.getApplicationName())
				.type(info.getType());
		return environment().valueList(String.class, "resourceName", query);
	}

	private void polulateFrozen(QueryInfo info, DataSourceRequest req, List<FreezeUnfreeze> list)
			throws UnifyException {
		CollaborationFreezeQuery query = new CollaborationFreezeQuery().applicationName(info.getApplicationName())
				.type(info.getType());
		query.setOffset(req.getOffset());
		query.setLimit(req.getLimit());
		for (CollaborationFreeze collaborationFreeze : environment().findAll(query)) {
			FreezeUnfreeze freezeUnfreeze = new FreezeUnfreeze();
			freezeUnfreeze.setApplicationName(collaborationFreeze.getApplicationName());
			freezeUnfreeze.setFrozenBy(collaborationFreeze.getCreatedBy());
			freezeUnfreeze.setFrozenOn(collaborationFreeze.getCreateDt());
			freezeUnfreeze.setResourceName(collaborationFreeze.getResourceName());
			freezeUnfreeze.setResourceDesc(collaborationFreeze.getResourceDesc());
			freezeUnfreeze.setStatus(FrozenStatus.FROZEN);
			freezeUnfreeze.setType(collaborationFreeze.getType());
			freezeUnfreeze.setStatusDesc(
					getListItemByKey(LocaleType.SESSION, "frozenstatuslist", FrozenStatus.FROZEN.code())
							.getListDescription());
			freezeUnfreeze
					.setTypeDesc(getListItemByKey(LocaleType.SESSION, "collaborationtypelist", info.getType().code())
							.getListDescription());
			list.add(freezeUnfreeze);
		}
	}

	private void polulateUnfrozen(QueryInfo info, DataSourceRequest req, List<FreezeUnfreeze> list)
			throws UnifyException {
		Query<? extends BaseApplicationEntity> query = Query
				.of(ApplicationCollaborationUtils.getEntityClass(info.getType()))
				.addEquals("applicationName", info.getApplicationName());
		query.setOffset(req.getOffset());
		query.setLimit(req.getLimit());
		List<String> frozenenResourceList = getFrozenResources(info);
		if (!frozenenResourceList.isEmpty()) {
			query.addNotAmongst("name", frozenenResourceList);
		}

		for (BaseApplicationEntity baseApplicationEntity : environment().listAll(query)) {
			FreezeUnfreeze freezeUnfreeze = new FreezeUnfreeze();
			freezeUnfreeze.setApplicationName(baseApplicationEntity.getApplicationName());
			freezeUnfreeze.setResourceName(baseApplicationEntity.getName());
			freezeUnfreeze.setResourceDesc(baseApplicationEntity.getDescription());
			freezeUnfreeze.setStatus(FrozenStatus.UNFROZEN);
			freezeUnfreeze.setType(info.getType());
			freezeUnfreeze.setStatusDesc(
					getListItemByKey(LocaleType.SESSION, "frozenstatuslist", FrozenStatus.UNFROZEN.code())
							.getListDescription());
			freezeUnfreeze
					.setTypeDesc(getListItemByKey(LocaleType.SESSION, "collaborationtypelist", info.getType().code())
							.getListDescription());
			list.add(freezeUnfreeze);
		}
	}

	private QueryInfo getQueryInfo(DataSourceRequest req) {
		String applicationName = getQueryPart(req.getQuery(), "applicationName");
		CollaborationType type = CollaborationType.fromCode(getQueryPart(req.getQuery(), "type"));
		FrozenStatus status = FrozenStatus.fromCode(getQueryPart(req.getQuery(), "status"));
		return new QueryInfo(type, status, applicationName);
	}

	private String getQueryPart(String query, String filterName) {
		int fromIndex = query.indexOf(filterName);
		if (fromIndex >= 0) {
			fromIndex += filterName.length() + 1;
			int toIndex = query.indexOf(']', fromIndex);
			return query.substring(fromIndex, toIndex);
		}

		return null;
	}

	private class QueryInfo {

		private final CollaborationType type;

		private final FrozenStatus status;

		private final String applicationName;

		public QueryInfo(CollaborationType type, FrozenStatus status, String applicationName) {
			this.type = type;
			this.status = status;
			this.applicationName = applicationName;
		}

		public CollaborationType getType() {
			return type;
		}

		public String getApplicationName() {
			return applicationName;
		}

		public boolean validQuery() {
			return type != null && !StringUtils.isBlank(applicationName);
		}

		public boolean frozen() {
			return FrozenStatus.FROZEN.equals(status);
		}

		public boolean unfrozen() {
			return FrozenStatus.UNFROZEN.equals(status);
		}
	}
}

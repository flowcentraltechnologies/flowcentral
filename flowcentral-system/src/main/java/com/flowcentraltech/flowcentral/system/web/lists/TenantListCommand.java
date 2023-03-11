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

package com.flowcentraltech.flowcentral.system.web.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.system.constants.SystemModuleSysParamConstants;
import com.flowcentraltech.flowcentral.system.entities.MappedTenant;
import com.flowcentraltech.flowcentral.system.entities.MappedTenantQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.list.ZeroParams;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Tenant list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("tenantlist")
public class TenantListCommand extends AbstractSystemListCommand<ZeroParams> {

    public TenantListCommand() {
        super(ZeroParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, ZeroParams params) throws UnifyException {
        List<MappedTenant> tenantList = system().findTenants((MappedTenantQuery) new MappedTenantQuery().ignoreEmptyCriteria(true));
        if (!DataUtils.isBlank(tenantList)) {
            List<Listable> resultList = new ArrayList<Listable>();
            Long actualPrimaryTenantId = system().getSysParameterValue(Long.class,
                    SystemModuleSysParamConstants.SYSTEM_ACTUAL_PRIMARY_TENANT_ID);
            for (MappedTenant mappedTenant : tenantList) {
                Long tenantId = mappedTenant.getId();
                String label = null;
                if (mappedTenant.getId().equals(actualPrimaryTenantId) && Boolean.TRUE.equals(mappedTenant.getPrimary())) {
                    tenantId = Entity.PRIMARY_TENANT_ID;
                    label = "[P] " + mappedTenant.getName();
                } else {
                    label = "[S] " + mappedTenant.getName();
                }

                resultList.add(new ListData(String.valueOf(tenantId), label));
            }

            DataUtils.sortAscending(resultList, Listable.class, "listDescription");
            return resultList;
        }

        return Collections.emptyList();
    }

}

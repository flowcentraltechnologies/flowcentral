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

package com.flowcentraltech.flowcentral.system.web.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.system.constants.SystemModuleSysParamConstants;
import com.flowcentraltech.flowcentral.system.entities.Tenant;
import com.flowcentraltech.flowcentral.system.entities.TenantQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.list.ZeroParams;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Login tenant list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("logintenantlist")
public class LoginTenantListCommand extends AbstractSystemListCommand<ZeroParams> {

    public LoginTenantListCommand() {
        super(ZeroParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, ZeroParams params) throws UnifyException {
        List<Tenant> tenantList = system().findTenants((TenantQuery) new TenantQuery().ignoreEmptyCriteria(true));
        if (!DataUtils.isBlank(tenantList)) {
            List<Listable> resultList = new ArrayList<Listable>();
            Long actualPrimaryTenantId = system().getSysParameterValue(Long.class,
                    SystemModuleSysParamConstants.SYSTEM_ACTUAL_PRIMARY_TENANT_ID);
            for (Tenant tenant : tenantList) {
                Long tenantId = tenant.getId();
                if (tenant.getId().equals(actualPrimaryTenantId) && Boolean.TRUE.equals(tenant.getPrimary())) {
                    tenantId = Entity.PRIMARY_TENANT_ID;
                }

                resultList.add(new ListData(String.valueOf(tenantId), tenant.getName()));
            }

            DataUtils.sortAscending(resultList, Listable.class, "listDescription");
            return resultList;
        }

        return Collections.emptyList();
    }

}

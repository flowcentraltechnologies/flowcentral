/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.list.ZeroParams;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Login tenant list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("logintenantlist")
public class LoginTenantListCommand extends AbstractSystemListCommand<ZeroParams> {

    public LoginTenantListCommand() {
        super(ZeroParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, ZeroParams params) throws UnifyException {
        List<MappedTenant> tenantList = system().findTenants((MappedTenantQuery) new MappedTenantQuery().ignoreEmptyCriteria(true));
        if (!DataUtils.isBlank(tenantList)) {
            List<Listable> resultList = new ArrayList<Listable>();
            Long actualPrimaryTenantId = system().getSysParameterValue(Long.class,
                    SystemModuleSysParamConstants.SYSTEM_ACTUAL_PRIMARY_TENANT_ID);
            ListData primary = null;
            for (MappedTenant mappedTenant : tenantList) {
                Long tenantId = mappedTenant.getId();
                if (mappedTenant.getId().equals(actualPrimaryTenantId) && Boolean.TRUE.equals(mappedTenant.getPrimary())) {
                    tenantId = Entity.PRIMARY_TENANT_ID;
                    primary = new ListData(String.valueOf(tenantId), mappedTenant.getName());
                } else {
                    resultList.add(new ListData(String.valueOf(tenantId), mappedTenant.getName()));
                }
            }

            DataUtils.sortAscending(resultList, Listable.class, "listDescription");
            if (primary != null) {
                resultList.add(0, primary);
            }
            
            return resultList;
        }

        return Collections.emptyList();
    }

}

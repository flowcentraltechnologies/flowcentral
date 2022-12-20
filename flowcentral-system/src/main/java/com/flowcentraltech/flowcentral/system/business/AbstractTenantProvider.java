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
package com.flowcentraltech.flowcentral.system.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.system.entities.Tenant;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;

/**
 * Convenient abstract base class for tenant providers..
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractTenantProvider<T extends Entity> extends AbstractUnifyComponent
        implements TenantProvider {

    @Configurable
    private EnvironmentService environmentService;

    private final Class<T> tenantClass;
    
    private final Map<String, String> queryFieldMap;
    
    public AbstractTenantProvider(Class<T> tenantClass, Map<String, String> queryFieldMap) {
        if (Tenant.class.equals(tenantClass)) {
            throw new IllegalArgumentException("Can use tenant class for provider.");
        }

        this.tenantClass = tenantClass;
        this.queryFieldMap = queryFieldMap;
    }

    public final void setEnvironmentService(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    @Override
    public Tenant find(Long id) throws UnifyException {
        T record = environmentService.find(tenantClass, id);
        return createTenant(record);
    }

    @Override
    public Tenant find(Long id, Object versionNo) throws UnifyException {
        T record = environmentService.find(tenantClass, id);
        return createTenant(record);
    }

    @Override
    public Tenant find(Query<Tenant> query) throws UnifyException {
        T record = environmentService.find(tenantClass, convertQuery(query));
        return createTenant(record);
    }

    @Override
    public Tenant findLean(Long id) throws UnifyException {
        T record = environmentService.findLean(tenantClass, id);
        return createTenant(record);
    }

    @Override
    public Tenant findLean(Long id, Object versionNo) throws UnifyException {
        T record = environmentService.findLean(tenantClass, id);
        return createTenant(record);
    }

    @Override
    public Tenant findLean(Query<Tenant> query) throws UnifyException {
        T record = environmentService.findLean(tenantClass, convertQuery(query));
        return createTenant(record);
    }

    @Override
    public List<Tenant> findAll(Query<Tenant> query) throws UnifyException {
        List<T> instList = environmentService.findAll(convertQuery(query));
        return createTenantList(instList);
    }

    @Override
    public Tenant list(Long id) throws UnifyException {
        T record = environmentService.list(tenantClass, id);
        return createTenant(record);
    }

    @Override
    public Tenant list(Long id, Object versionNo) throws UnifyException {
        T record = environmentService.list(tenantClass, id);
        return createTenant(record);
    }

    @Override
    public Tenant list(Query<Tenant> query) throws UnifyException {
        T record = environmentService.list(tenantClass,  convertQuery(query));
        return createTenant(record);
    }

    @Override
    public Tenant listLean(Long id) throws UnifyException {
        T record = environmentService.listLean(tenantClass, id);
        return createTenant(record);
    }

    @Override
    public Tenant listLean(Long id, Object versionNo) throws UnifyException {
        T record = environmentService.listLean(tenantClass, id);
        return createTenant(record);
    }

    @Override
    public Tenant listLean(Query<Tenant> query) throws UnifyException {
        T record = environmentService.listLean(tenantClass,  convertQuery(query));
        return createTenant(record);
    }

    @Override
    public List<Tenant> listAll(Query<Tenant> query) throws UnifyException {
        List<T> instList = environmentService.listAll(convertQuery(query));
        return createTenantList(instList);
     }

    @Override
    public int countAll(Query<Tenant> query) throws UnifyException {
        return environmentService.countAll(convertQuery(query));
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    protected abstract Tenant doCreateTenant(T inst) throws UnifyException;

    private Query<T> convertQuery(Query<Tenant> query) throws UnifyException {
        Query<T> _query = Query.of(tenantClass);
        if (!query.isEmptyCriteria()) {
            Restriction restriction = query.getRestrictions();
            restriction.fieldSwap(queryFieldMap);
            _query.addRestriction(restriction);
        }
        
        return _query;
    }
    
    private List<Tenant> createTenantList(List<T> instList) throws UnifyException {
        List<Tenant> tenantList = new ArrayList<Tenant>();
        for (T inst: instList) {
            Tenant tenant = createTenant(inst);
            tenantList.add(tenant);
        }
        
        return tenantList;
    }
    
    private Tenant createTenant(T inst) throws UnifyException {
        if (inst != null) {
            Tenant tenant = doCreateTenant(inst);
            tenant.setId((Long) inst.getId());
            return tenant;
        }
        
        return null;
    }
}

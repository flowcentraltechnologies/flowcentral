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
package com.flowcentraltech.flowcentral.application.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.system.entities.Tenant;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for tenant providers..
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractTenantProvider extends AbstractUnifyComponent implements TenantProvider {

    @Configurable
    private ApplicationModuleService applicationModuleService;

    @Configurable
    private EnvironmentService environmentService;

    private Class<? extends Entity> tenantClass;

    private final String tenantEntityName;

    private final ProviderInfo providerInfo;

    private final Map<String, String> queryFieldMap;

    protected AbstractTenantProvider(String tenantEntityName, ProviderInfo providerInfo) {
        this.tenantEntityName = tenantEntityName;
        this.providerInfo = providerInfo;
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", providerInfo.getNameField());
        map.put("primary", providerInfo.getPrimaryFlagField());
        this.queryFieldMap = Collections.unmodifiableMap(map);
    }

    public final void setApplicationModuleService(ApplicationModuleService applicationModuleService) {
        this.applicationModuleService = applicationModuleService;
    }

    public final void setEnvironmentService(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    @Override
    public Tenant find(Long id) throws UnifyException {
        Entity record = environmentService.find(tenantClass, id);
        return createTenant(record);
    }

    @Override
    public Tenant find(Long id, Object versionNo) throws UnifyException {
        Entity record = environmentService.find(tenantClass, id);
        return createTenant(record);
    }

    @Override
    public Tenant find(Query<Tenant> query) throws UnifyException {
        Entity record = environmentService.find(tenantClass, convertQuery(query));
        return createTenant(record);
    }

    @Override
    public Tenant findLean(Long id) throws UnifyException {
        Entity record = environmentService.findLean(tenantClass, id);
        return createTenant(record);
    }

    @Override
    public Tenant findLean(Long id, Object versionNo) throws UnifyException {
        Entity record = environmentService.findLean(tenantClass, id);
        return createTenant(record);
    }

    @Override
    public Tenant findLean(Query<Tenant> query) throws UnifyException {
        Entity record = environmentService.findLean(tenantClass, convertQuery(query));
        return createTenant(record);
    }

    @Override
    public List<Tenant> findAll(Query<Tenant> query) throws UnifyException {
        List<? extends Entity> instList = environmentService.findAll(convertQuery(query));
        return createTenantList(instList);
    }

    @Override
    public Tenant list(Long id) throws UnifyException {
        Entity record = environmentService.list(tenantClass, id);
        return createTenant(record);
    }

    @Override
    public Tenant list(Long id, Object versionNo) throws UnifyException {
        Entity record = environmentService.list(tenantClass, id);
        return createTenant(record);
    }

    @Override
    public Tenant list(Query<Tenant> query) throws UnifyException {
        Entity record = environmentService.list(tenantClass, convertQuery(query));
        return createTenant(record);
    }

    @Override
    public Tenant listLean(Long id) throws UnifyException {
        Entity record = environmentService.listLean(tenantClass, id);
        return createTenant(record);
    }

    @Override
    public Tenant listLean(Long id, Object versionNo) throws UnifyException {
        Entity record = environmentService.listLean(tenantClass, id);
        return createTenant(record);
    }

    @Override
    public Tenant listLean(Query<Tenant> query) throws UnifyException {
        Entity record = environmentService.listLean(tenantClass, convertQuery(query));
        return createTenant(record);
    }

    @Override
    public List<Tenant> listAll(Query<Tenant> query) throws UnifyException {
        List<? extends Entity> instList = environmentService.listAll(convertQuery(query));
        return createTenantList(instList);
    }

    @Override
    public int countAll(Query<Tenant> query) throws UnifyException {
        return environmentService.countAll(convertQuery(query));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onInitialize() throws UnifyException {
        EntityClassDef entityClassDef = applicationModuleService.getEntityClassDef(tenantEntityName);
        tenantClass = (Class<? extends Entity>) entityClassDef.getEntityClass();
        if (Tenant.class.equals(tenantClass)) {
            throw new IllegalArgumentException("Can not use tenant class for provider.");
        }
    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    protected ApplicationModuleService application() {
        return applicationModuleService;
    }

    protected EnvironmentService environment() {
        return environmentService;
    }

    private Query<? extends Entity> convertQuery(Query<Tenant> query) throws UnifyException {
        Query<? extends Entity> _query = Query.of(tenantClass);
        if (!query.isEmptyCriteria()) {
            Restriction restriction = query.getRestrictions();
            restriction.fieldSwap(queryFieldMap);
            _query.addRestriction(restriction);
        }

        return _query;
    }

    private List<Tenant> createTenantList(List<? extends Entity> instList) throws UnifyException {
        List<Tenant> tenantList = new ArrayList<Tenant>();
        for (Entity inst : instList) {
            Tenant tenant = createTenant(inst);
            tenantList.add(tenant);
        }

        return tenantList;
    }

    private Tenant createTenant(Entity inst) throws UnifyException {
        if (inst != null) {
            final String name = DataUtils.getBeanProperty(String.class, inst, providerInfo.getNameField());
            final boolean primary = DataUtils.getBeanProperty(boolean.class, inst, providerInfo.getPrimaryFlagField());
            return new Tenant((Long) inst.getId(), name, primary);
        }

        return null;
    }

    protected static class ProviderInfo {

        private final String nameField;

        private final String primaryFlagField;

        public ProviderInfo(String nameField, String primaryFlagField) {
            this.nameField = nameField;
            this.primaryFlagField = primaryFlagField;
        }

        public String getNameField() {
            return nameField;
        }

        public String getPrimaryFlagField() {
            return primaryFlagField;
        }
    }

}

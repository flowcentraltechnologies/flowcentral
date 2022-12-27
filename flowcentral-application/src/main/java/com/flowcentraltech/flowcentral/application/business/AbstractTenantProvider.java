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

import java.util.HashMap;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for tenant providers..
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractTenantProvider extends AbstractMappedEntityProvider<MappedTenantProviderContext> {

    private final ProviderInfo providerInfo;

    @SuppressWarnings("serial")
    protected AbstractTenantProvider(String destTenantEntityName, String srcTenantEntityName,
            ProviderInfo providerInfo) {
        super(MappedTenantProviderContext.class, destTenantEntityName, srcTenantEntityName,
                new HashMap<String, String>()
                    {
                        {
                            put("name", providerInfo.getNameField());
                            put("primary", providerInfo.getPrimaryFlagField());
                        }
                    });
        this.providerInfo = providerInfo;
    }

    @Override
    protected void doMappedCopy(MappedTenantProviderContext context, Entity destInst, Entity srcInst)
            throws UnifyException {
        final Long id = DataUtils.getBeanProperty(Long.class, srcInst, "id");
        final String name = DataUtils.getBeanProperty(String.class, srcInst, providerInfo.getNameField());
        final boolean primary = DataUtils.getBeanProperty(boolean.class, srcInst, providerInfo.getPrimaryFlagField());
        DataUtils.setBeanProperty(destInst, "id", id);
        DataUtils.setBeanProperty(destInst, "name", name);
        DataUtils.setBeanProperty(destInst, "primary", primary);
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

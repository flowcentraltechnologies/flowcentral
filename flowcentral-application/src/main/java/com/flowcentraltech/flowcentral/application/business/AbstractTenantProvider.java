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
package com.flowcentraltech.flowcentral.application.business;

import java.util.HashMap;

import com.flowcentraltech.flowcentral.common.constants.FlowCentralApplicationAttributeConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;

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
                            put("dateFormat", providerInfo.getDateFormatField());
                            put("primary", providerInfo.getPrimaryFlagField());
                        }
                    });
        this.providerInfo = providerInfo;
    }

    @Override
    protected void doMappedCopyFromDestToSrc(MappedTenantProviderContext context, ValueStore srcValueStore,
            ValueStore destValueStore) throws UnifyException {
        throwUnsupportedOperationException();
    }

    @Override
    protected void doMappedCopyFromSrcToDest(MappedTenantProviderContext context, ValueStore destValueStore, ValueStore srcValueStore)
            throws UnifyException {
        destValueStore.store("id", srcValueStore.retrieve("id"));
        destValueStore.store("name", srcValueStore.retrieve(providerInfo.getNameField()));
        destValueStore.store("dateFormat", resolveDateFormat(srcValueStore, providerInfo.getDateFormatField()));
        destValueStore.store("primary", srcValueStore.retrieve(providerInfo.getPrimaryFlagField()));
    }

    @Override
    protected void onInitialize() throws UnifyException {
        super.onInitialize();
        setApplicationAttribute(FlowCentralApplicationAttributeConstants.TENANT_SOURCE_ENTITY, srcEntity());
    }

    protected abstract String resolveDateFormat(ValueStore srcValueStore, String dateFormatField) throws UnifyException;
    
    protected static class ProviderInfo {

        private final String nameField;

        private final String dateFormatField;

        private final String primaryFlagField;

        public ProviderInfo(String nameField, String dateFormatField, String primaryFlagField) {
            this.nameField = nameField;
            this.dateFormatField = dateFormatField;
            this.primaryFlagField = primaryFlagField;
        }

        public String getNameField() {
            return nameField;
        }

        public String getDateFormatField() {
            return dateFormatField;
        }

        public String getPrimaryFlagField() {
            return primaryFlagField;
        }
    }

}

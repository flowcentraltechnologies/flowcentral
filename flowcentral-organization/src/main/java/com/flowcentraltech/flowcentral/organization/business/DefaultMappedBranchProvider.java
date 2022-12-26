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
package com.flowcentraltech.flowcentral.organization.business;

import java.util.Collections;

import com.flowcentraltech.flowcentral.application.business.AbstractMappedEntityProvider;
import com.flowcentraltech.flowcentral.organization.entities.MappedBranch;
import com.flowcentraltech.flowcentral.organization.entities.Zone;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Entity;

/**
 * Default mapped branch provider
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("default-mappedbranchprovider")
public class DefaultMappedBranchProvider
        extends AbstractMappedEntityProvider<MappedBranch, DefaultMappedBranchProviderContext> {

    public DefaultMappedBranchProvider() {
        super(MappedBranch.class, DefaultMappedBranchProviderContext.class, "organization.branch",
                Collections.emptyMap());
    }

    @Override
    protected MappedBranch doCreate(DefaultMappedBranchProviderContext context, Entity inst) throws UnifyException {
        MappedBranch mappedBranch = new MappedBranch();
        ValueStore mappedBranchValueStore = new BeanValueStore(mappedBranch);
        mappedBranchValueStore.copy(new BeanValueStore(inst));

        final Long zoneId = mappedBranchValueStore.retrieve(Long.class, "zoneId");
        if (zoneId != null) {
            ValueStore zoneValueStore = null;
            if (context.isMultiple()) {
                if (context.isZonePresent(zoneId)) {
                    zoneValueStore = context.getZone(zoneId);
                } else {
                    Zone zone = environment().list(Zone.class, zoneId);
                    zoneValueStore = new BeanValueStore(zone);
                    context.saveZone(zoneId, zoneValueStore);
                }
            } else {
                Zone zone = environment().list(Zone.class, zoneId);
                zoneValueStore = new BeanValueStore(zone);
            }

            mappedBranchValueStore.store("zoneCode", zoneValueStore.retrieve("code"));
            mappedBranchValueStore.store("zoneDesc", zoneValueStore.retrieve("description"));
            mappedBranchValueStore.store("languageTag", zoneValueStore.retrieve("languageTag"));
            mappedBranchValueStore.store("timeZone", zoneValueStore.retrieve("timeZone"));
        }

        return mappedBranch;
    }

}

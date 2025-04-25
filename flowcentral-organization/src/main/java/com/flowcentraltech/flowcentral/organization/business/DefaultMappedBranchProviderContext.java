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
package com.flowcentraltech.flowcentral.organization.business;

import java.util.HashMap;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.BaseMappedEntityProviderContext;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Default mapped branch provider context.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class DefaultMappedBranchProviderContext extends BaseMappedEntityProviderContext {

    private Map<Long, ValueStore> zones;

    public boolean isZonePresent(Long zoneId) {
        return zones != null && zones.containsKey(zoneId);
    }

    public void saveZone(Long zoneId, ValueStore valueStore) {
        if (zones == null) {
            zones = new HashMap<Long, ValueStore>();
        }

        zones.put(zoneId, valueStore);
    }

    public ValueStore getZone(Long zoneId) {
        return zones.get(zoneId);
    }
}

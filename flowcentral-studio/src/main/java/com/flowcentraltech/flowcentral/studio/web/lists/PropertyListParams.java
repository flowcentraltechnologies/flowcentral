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

package com.flowcentraltech.flowcentral.studio.web.lists;

import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.tcdng.unify.core.list.AbstractListParam;

/**
 * Property list parameters.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class PropertyListParams extends AbstractListParam {

    private EntityFieldDataType dataType;

    private Long appEntityId;

    private String key;

    public PropertyListParams(EntityFieldDataType dataType, Long appEntityId, String key) {
        this.dataType = dataType;
        this.appEntityId = appEntityId;
        this.key = key;
    }

    public EntityFieldDataType getDataType() {
        return dataType;
    }

    public Long getAppEntityId() {
        return appEntityId;
    }

    public String getKey() {
        return key;
    }

    @Override
    public boolean isPresent() {
        return dataType != null && appEntityId != null && key != null;
    }

    @Override
    public String toString() {
        return "PropertyListParams [dataType=" + dataType + ", appEntityId=" + appEntityId + ", key=" + key + "]";
    }
}

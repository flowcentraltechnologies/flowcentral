/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.application.data;

import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.common.data.VersionedEntityDef;
import com.tcdng.unify.core.data.Listable;

/**
 * Base class for application entity definition
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class BaseApplicationEntityDef implements Listable, VersionedEntityDef {

    private ApplicationEntityNameParts nameParts;

    private String description;

    private Long id;

    private long version;

    public BaseApplicationEntityDef(ApplicationEntityNameParts nameParts, String description, Long id, long version) {
        this.nameParts = nameParts;
        this.description = description;
        this.id = id;
        this.version = version;
    }

    @Override
    public String getListDescription() {
        return description;
    }

    @Override
    public String getListKey() {
        return nameParts.getLongName();
    }

    public ApplicationEntityNameParts getNameParts() {
        return nameParts;
    }

    public String getLongName() {
        return nameParts.getLongName();
    }

    public String getApplicationName() {
        return nameParts.getApplicationName();
    }

    public String getName() {
        return nameParts.getEntityName();
    }

    public String getMinorName() {
        return nameParts.getMinorName();
    }

    public String getDescription() {
        return description;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public long getVersion() {
        return version;
    }
}

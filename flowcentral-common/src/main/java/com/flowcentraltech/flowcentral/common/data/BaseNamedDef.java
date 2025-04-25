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
package com.flowcentraltech.flowcentral.common.data;

import com.tcdng.unify.common.data.Listable;

/**
 * Convenient abstract base class for named definitions.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class BaseNamedDef implements Listable {

    private String name;

    private String description;

    private Long id;

    private long version;

    protected BaseNamedDef(String name, String description, Long id, long version) {
        this.name = name;
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
        return name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

}

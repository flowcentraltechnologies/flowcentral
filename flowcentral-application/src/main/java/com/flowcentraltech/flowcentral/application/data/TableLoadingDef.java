/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

import com.tcdng.unify.core.data.Listable;

/**
 * Table loading definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class TableLoadingDef implements Listable {

    private final String name;

    private final String description;

    private final String label;

    private final String provider;

    private final Object parameter;

    private final int orderIndex;

    public TableLoadingDef(String name, String description, String label, String provider, int orderIndex) {
        this.name = name;
        this.description = description;
        this.label = label;
        this.provider = provider;
        this.parameter = null;
        this.orderIndex = orderIndex;
    }

    public TableLoadingDef(String name, String description, String label, String provider, Object parameter,
            int orderIndex) {
        this.name = name;
        this.description = description;
        this.label = label;
        this.provider = provider;
        this.parameter = parameter;
        this.orderIndex = orderIndex;
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

    public String getLabel() {
        return label;
    }

    public String getProvider() {
        return provider;
    }

    public Object getParameter() {
        return parameter;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

}

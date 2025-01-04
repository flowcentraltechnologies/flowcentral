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

import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Application property sequence entry definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class PropertySequenceEntryDef implements Listable {

    private String property;

    private String label;

    public PropertySequenceEntryDef(String property, String label) {
        this.property = property;
        this.label = label;
    }

    @Override
    public String getListKey() {
        return property;
    }

    @Override
    public String getListDescription() {
        return StringUtils.isBlank(label) ? property : label;
    }

    public String getProperty() {
        return property;
    }

    public String getLabel() {
        return label;
    }

}

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
package com.flowcentraltech.flowcentral.application.data;

import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.constant.ColorScheme;

/**
 * Enumeration item definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EnumerationItemDef implements Listable {

    private String code;
    
    private String label;
    
    private ColorScheme color;
    
    public EnumerationItemDef(String code, String label, ColorScheme color) {
        this.code = code;
        this.label = label;
        this.color = color;
    }

    @Override
    public String getListDescription() {
        return label;
    }

    @Override
    public String getListKey() {
        return code;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public ColorScheme getColor() {
        return color;
    }

    public boolean isWithColor() {
        return color != null;
    }
}

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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;

/**
 * Enumeration definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EnumerationDef extends BaseApplicationEntityDef {

    private String label;
    
    private Map<String, EnumerationItemDef> items;

    private List<EnumerationItemDef> itemsList;

    public EnumerationDef(ApplicationEntityNameParts nameParts, String description, Long id, long version,
            String label, List<EnumerationItemDef> itemsList) {
        super(nameParts, description, id, version);
        this.label = label;
        this.itemsList = Collections.unmodifiableList(itemsList);
        this.items = new LinkedHashMap<String, EnumerationItemDef>();
        for (EnumerationItemDef item: itemsList) {
            this.items.put(item.getCode(), item);
        }
        
        this.items = Collections.unmodifiableMap(this.items);
    }

    @Override
    public String getListDescription() {
        return label;
    }

    public String getLabel() {
        return label;
    }

    public Map<String, EnumerationItemDef> getItems() {
        return items;
    }

    public List<EnumerationItemDef> getItemsList() {
        return itemsList;
    }
    
    public EnumerationItemDef getItem(String code) {
        return items.get(code);
    }

}

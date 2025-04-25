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

package com.flowcentraltech.flowcentral.application.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.configuration.constants.FormColumnsType;
import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Property list definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class PropertyListDef extends BaseApplicationEntityDef {

    private List<FormTabDef> formTabDefs;

    private List<PropertyListItemDef> itemDefList;

    private PropertyListDef(List<FormTabDef> formTabDefs, List<PropertyListItemDef> itemDefList,
            ApplicationEntityNameParts nameParts, String description, Long id, long version) {
        super(nameParts, description, id, version);
        this.formTabDefs = formTabDefs;
        this.itemDefList = itemDefList;
    }

    public List<FormTabDef> getFormTabDefs() {
        return formTabDefs;
    }

    public List<PropertyListItemDef> getItemDefList() {
        return itemDefList;
    }

    public static Builder newBuilder(String longName, String description, Long id, long version) {
        return new Builder(longName, description, id, version);
    }

    public static class Builder {

        private Map<String, List<PropertyListItemDef>> itemDefMap;

        private Set<String> itemNames;

        private String longName;

        private String description;

        private Long id;

        private long version;

        public Builder(String longName, String description, Long id, long version) {
            this.longName = longName;
            this.description = description;
            this.id = id;
            this.version = version;
            this.itemDefMap = new LinkedHashMap<String, List<PropertyListItemDef>>();
            this.itemNames = new HashSet<String>();
        }

        public Builder addSetDef(String label) {
            if (itemDefMap.containsKey(label)) {
                throw new RuntimeException(
                        "Property set with label [" + label + "] already exists in this definition.");
            }

            itemDefMap.put(label, new ArrayList<PropertyListItemDef>());
            return this;
        }

        public Builder addItemDef(EntityFieldDef entityFieldDef, WidgetTypeDef widgetTypeDef, String setLabel,
                String description, String renderer, String defaultVal, boolean required, boolean mask,
                boolean encrypt) {
            List<PropertyListItemDef> itemList = itemDefMap.get(setLabel);
            if (itemList == null) {
                throw new RuntimeException("Property set with label [" + setLabel + "] is unknown in this definition.");
            }

            if (itemNames.contains(entityFieldDef.getFieldName())) {
                throw new RuntimeException("Property list item with name [" + entityFieldDef.getFieldName()
                        + "] already exists in this definition.");
            }

            itemNames.add(entityFieldDef.getFieldName());
            itemList.add(new PropertyListItemDef(entityFieldDef, widgetTypeDef, description, renderer, defaultVal,
                    required, mask, encrypt));
            return this;
        }

        public PropertyListDef build() throws UnifyException {
            List<PropertyListItemDef> itemList = new ArrayList<PropertyListItemDef>();
            List<FormTabDef> formTabDefs = new ArrayList<FormTabDef>();
            for (Map.Entry<String, List<PropertyListItemDef>> entry : itemDefMap.entrySet()) {
                List<FormFieldDef> formFieldDefList = new ArrayList<FormFieldDef>();
                for (PropertyListItemDef propertyListItemDef : entry.getValue()) {
                    formFieldDefList.add(new FormFieldDef(propertyListItemDef.getEntityFieldDef(),
                            propertyListItemDef.getWidgetTypeDef(), null, null, propertyListItemDef.getDescription(),
                            propertyListItemDef.getRenderer(), 0, false, false, propertyListItemDef.isRequired(), true,
                            true, false));
                    itemList.add(propertyListItemDef);
                }

                FormTabDef formTabDef = new FormTabDef(TabContentType.MINIFORM, null, entry.getKey(), entry.getKey(),
                        null, null, null, null, null, null, null, null, null,
                        Arrays.asList(new FormSectionDef(formFieldDefList, entry.getKey(), entry.getKey(),
                                FormColumnsType.TYPE_1, null, null, true, true, false)),
                        false, false, false, false, false, true, true, false);
                formTabDefs.add(formTabDef);
            }

            ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
            return new PropertyListDef(formTabDefs, DataUtils.unmodifiableList(itemList), nameParts, description, id,
                    version);
        }
    }

}

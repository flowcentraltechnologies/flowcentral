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

import java.util.ArrayList;
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
 * @since 1.0
 */
public class PropertyListDef extends BaseApplicationEntityDef {

    private FormTabDef formTabDef;

    private List<PropertyListItemDef> itemDefList;

    private PropertyListDef(FormTabDef formTabDef, List<PropertyListItemDef> itemDefList,
            ApplicationEntityNameParts nameParts, String description, Long id, long version) {
        super(nameParts, description, id, version);
        this.formTabDef = formTabDef;
        this.itemDefList = itemDefList;
    }

    public FormTabDef getFormTabDef() {
        return formTabDef;
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
            List<FormSectionDef> formSectionDefList = new ArrayList<FormSectionDef>();
            for (Map.Entry<String, List<PropertyListItemDef>> entry : itemDefMap.entrySet()) {
                List<FormFieldDef> formFieldDefList = new ArrayList<FormFieldDef>();
                for (PropertyListItemDef propertyListItemDef : entry.getValue()) {
                    formFieldDefList.add(new FormFieldDef(propertyListItemDef.getEntityFieldDef(),
                            propertyListItemDef.getWidgetTypeDef(), null, propertyListItemDef.getDescription(),
                            propertyListItemDef.getRenderer(), 0, false, false, propertyListItemDef.isRequired(), true,
                            true, false));
                    itemList.add(propertyListItemDef);
                }

                formSectionDefList.add(new FormSectionDef(formFieldDefList, entry.getKey(), entry.getKey(),
                        FormColumnsType.TYPE_1, true, true, false));
            }

            FormTabDef formTabDef = new FormTabDef(TabContentType.MINIFORM, null, "details", description, null, null,
                    null, null, null, null, null, formSectionDefList, false, false, false, true, true, false);

            ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
            return new PropertyListDef(formTabDef, DataUtils.unmodifiableList(itemList), nameParts, description, id,
                    version);
        }
    }

}

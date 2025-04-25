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

/**
 * Property list item definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class PropertyListItemDef {

    private EntityFieldDef entityFieldDef;

    private WidgetTypeDef widgetTypeDef;
    
    private String description;

    private String renderer;

    private String defaultVal;

    private boolean required;

    private boolean mask;

    private boolean encrypt;

    public PropertyListItemDef(EntityFieldDef entityFieldDef, WidgetTypeDef widgetTypeDef, String description, String renderer,
            String defaultVal, boolean required, boolean mask, boolean encrypt) {
        this.entityFieldDef = entityFieldDef;
        this.widgetTypeDef = widgetTypeDef;
        this.description = description;
        this.renderer = renderer;
        this.defaultVal = defaultVal;
        this.required = required;
        this.mask = mask;
        this.encrypt = encrypt;
    }

    public EntityFieldDef getEntityFieldDef() {
        return entityFieldDef;
    }

    public WidgetTypeDef getWidgetTypeDef() {
        return widgetTypeDef;
    }

    public String getName() {
        return entityFieldDef.getFieldName();
    }

    public String getDescription() {
        return description;
    }

    public String getReferences() {
        return entityFieldDef.getReferences();
    }

    public String getRenderer() {
        return renderer;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isMask() {
        return mask;
    }

    public boolean isEncrypt() {
        return encrypt;
    }
}

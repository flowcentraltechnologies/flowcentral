/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.web.widgets;

/**
 * Entity search and view information.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EntitySearchViewInfo {

    private Object key;

    private String label;

    private String table;

    private String form;

    public EntitySearchViewInfo(Object key, String label, String table, String form) {
        this.key = key;
        this.label = label;
        this.table = table;
        this.form = form;
    }

    public Object getKey() {
        return key;
    }

    public String getLabel() {
        return label;
    }

    public String getTable() {
        return table;
    }

    public String getForm() {
        return form;
    }

    public boolean isWithKey() {
        return key != null;
    }

    public boolean isWithLabel() {
        return label != null;
    }
}

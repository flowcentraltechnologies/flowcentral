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
package com.flowcentraltech.flowcentral.common.data;

import java.util.List;

import com.tcdng.unify.core.database.Entity;

/**
 * Loading items.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class LoadingItems {
    
    private List<? extends Entity> items;

    private String source;
    
    public LoadingItems(List<? extends Entity> items) {
        this.items = items;
    }

    public List<? extends Entity> getItems() {
        return items;
    }

    public boolean isItem(Entity inst) {
        return items.contains(inst);
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}

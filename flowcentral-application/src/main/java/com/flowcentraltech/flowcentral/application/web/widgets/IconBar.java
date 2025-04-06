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
package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Icon bar.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class IconBar {

    private List<Item> items;

    private int selected;
    
    private IconBar(List<Item> items) {
        this.items = items;
    }
    
    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        if (selected < 0) {
            this.selected = 0;
            return;
        }
        
        if (!items.isEmpty() && selected >= items.size()) {
            this.selected = items.size() - 1;
            return;
        }
        
        this.selected = selected;
    }

    public List<Item> getItems() {
        return items;
    }

    public Item getSelectedItem() {
        return items.get(selected);
    }
    
    public static Builder newBuilder() {
        return new Builder();
    }
    
    public static class Builder{

        private List<Item> items;
        
        public Builder() {
            this.items = new ArrayList<Item>();
        }
        
        public Builder addItem(String icon, String label) {
            items.add(new Item(icon, label));
            return this;
        }
        
        public IconBar build() {
            return new IconBar(Collections.unmodifiableList(items));
        }
    }
    
    public static class Item {
        
        private String icon;
        
        private String label;

        public Item(String icon, String label) {
            this.icon = icon;
            this.label = label;
        }

        public String getIcon() {
            return icon;
        }

        public String getLabel() {
            return label;
        }
    }
}

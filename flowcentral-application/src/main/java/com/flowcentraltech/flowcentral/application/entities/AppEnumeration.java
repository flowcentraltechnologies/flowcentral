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
package com.flowcentraltech.flowcentral.application.entities;

import java.util.List;

import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;

/**
 * Application enumeration entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_ENUMERATION")
public class AppEnumeration extends BaseApplicationEntity {

    @Column(name = "ENUM_LABEL", length = 128)
    private String label;
    
    @ChildList
    private List<AppEnumerationItem> itemList;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<AppEnumerationItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<AppEnumerationItem> itemList) {
        this.itemList = itemList;
    }
}

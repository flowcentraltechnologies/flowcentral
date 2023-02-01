/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Application property set entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_PROPSET", uniqueConstraints = { @UniqueConstraint({ "appPropertyListId", "label" }) })
public class AppPropertySet extends BaseEntity {

    @ForeignKey(AppPropertyList.class)
    private Long appPropertyListId;

    @Column(name = "PROPSETLIST_LABEL", length = 128)
    private String label;

    @ListOnly(key = "appPropertyListId", property = "name")
    private String appPropertyListName;

    @ListOnly(key = "appPropertyListId", property = "description")
    private String appPropertyListDesc;

    @ChildList
    private List<AppPropertyListItem> itemList;

    @Override
    public String getDescription() {
        return label;
    }

    public Long getAppPropertyListId() {
        return appPropertyListId;
    }

    public void setAppPropertyListId(Long appPropertyListId) {
        this.appPropertyListId = appPropertyListId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getAppPropertyListName() {
        return appPropertyListName;
    }

    public void setAppPropertyListName(String appPropertyListName) {
        this.appPropertyListName = appPropertyListName;
    }

    public String getAppPropertyListDesc() {
        return appPropertyListDesc;
    }

    public void setAppPropertyListDesc(String appPropertyListDesc) {
        this.appPropertyListDesc = appPropertyListDesc;
    }

    public List<AppPropertyListItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<AppPropertyListItem> itemList) {
        this.itemList = itemList;
    }

}

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

import com.flowcentraltech.flowcentral.common.entities.BaseEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Application property rule choice entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_PROPLISTITEM")
public class AppPropertyListItem extends BaseEntity {

    @ForeignKey(AppPropertySet.class)
    private Long appPropertySetId;

    @Column(name = "PROPLISTITEM_NM", length = 64)
    private String name;

    @Column(name = "PROPLISTITEM_DESC", length = 128)
    private String description;

    @Column(name = "ITEM_REFERENCE", length = 128, nullable = true)
    private String references;

    @Column(length = 128)
    private String inputWidget;

    @Column(length = 128, nullable = true)
    private String defaultVal;

    @Column
    private boolean required;

    @Column
    private boolean mask;

    @Column
    private boolean encrypt;

    @Column
    private boolean disabled;

    @ListOnly(key = "appPropertySetId", property = "label")
    private String appPropertySetLabel;

    @Override
    public String getDescription() {
        return description;
    }

    public Long getAppPropertySetId() {
        return appPropertySetId;
    }

    public void setAppPropertySetId(Long appPropertySetId) {
        this.appPropertySetId = appPropertySetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getInputWidget() {
        return inputWidget;
    }

    public void setInputWidget(String inputWidget) {
        this.inputWidget = inputWidget;
    }

    public String getDefaultVal() {
        return defaultVal;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isMask() {
        return mask;
    }

    public void setMask(boolean mask) {
        this.mask = mask;
    }

    public boolean isEncrypt() {
        return encrypt;
    }

    public void setEncrypt(boolean encrypt) {
        this.encrypt = encrypt;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getAppPropertySetLabel() {
        return appPropertySetLabel;
    }

    public void setAppPropertySetLabel(String appPropertySetLabel) {
        this.appPropertySetLabel = appPropertySetLabel;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

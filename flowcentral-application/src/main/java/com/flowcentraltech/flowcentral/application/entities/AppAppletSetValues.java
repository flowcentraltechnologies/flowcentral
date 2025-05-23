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

import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Child;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;

/**
 * Application applet set values entity;
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_APPLETSETVALUES", uniqueConstraints = { @UniqueConstraint({ "appAppletId", "name" }),
        @UniqueConstraint({ "appAppletId", "description" }) })
public class AppAppletSetValues extends BaseConfigNamedEntity {

    @ForeignKey(AppApplet.class)
    private Long appAppletId;

    @Column(length = 128, nullable = true)  
    private String valueGenerator;

    @Child(category = "applet")
    private AppSetValues setValues;

    public Long getAppAppletId() {
        return appAppletId;
    }

    public void setAppAppletId(Long appAppletId) {
        this.appAppletId = appAppletId;
    }

    public String getValueGenerator() {
        return valueGenerator;
    }

    public void setValueGenerator(String valueGenerator) {
        this.valueGenerator = valueGenerator;
    }

    public AppSetValues getSetValues() {
        return setValues;
    }

    public void setSetValues(AppSetValues setValues) {
        this.setValues = setValues;
    }

}

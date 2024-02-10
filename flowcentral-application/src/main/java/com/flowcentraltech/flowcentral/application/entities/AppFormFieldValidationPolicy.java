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
package com.flowcentraltech.flowcentral.application.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseConfigNamedEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.Table;

/**
 * Application form field validation policy entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_FORMFLDVALIDPOLICY")
public class AppFormFieldValidationPolicy extends BaseConfigNamedEntity {

    @ForeignKey(AppForm.class)
    private Long appFormId;

    @Column(length = 64)
    private String fieldName;

    @Column(length = 64)
    private String validation;

    @Column(length = 64, nullable = true)
    private String rule;

    public Long getAppFormId() {
        return appFormId;
    }

    public void setAppFormId(Long appFormId) {
        this.appFormId = appFormId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

}

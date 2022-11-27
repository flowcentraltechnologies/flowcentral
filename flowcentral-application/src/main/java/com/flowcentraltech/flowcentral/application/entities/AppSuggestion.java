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

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;

/**
 * Application suggestions.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_SUGGESTION")
public class AppSuggestion extends BaseAuditEntity {

    @ForeignKey(AppSuggestionType.class)
    private Long suggestionTypeId;
    
    @Column(length = 512)
    private String suggestion;
    
    @Column(name = "FIELD_NM", length = 32, nullable = true)
    private String fieldName;

    @ListOnly(key = "suggestionTypeId", property = "applicationName")
    private String applicationName;

    @ListOnly(key = "suggestionTypeId", property = "applicationDesc")
    private String applicationDesc;

    @ListOnly(key = "suggestionTypeId", property = "name")
    private String suggestionTypeName;

    @ListOnly(key = "suggestionTypeId", property = "description")
    private String suggestionTypeDesc;
    
    @Override
    public String getDescription() {
        return suggestion;
    }

    public Long getSuggestionTypeId() {
        return suggestionTypeId;
    }

    public void setSuggestionTypeId(Long suggestionTypeId) {
        this.suggestionTypeId = suggestionTypeId;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationDesc() {
        return applicationDesc;
    }

    public void setApplicationDesc(String applicationDesc) {
        this.applicationDesc = applicationDesc;
    }

    public String getSuggestionTypeName() {
        return suggestionTypeName;
    }

    public void setSuggestionTypeName(String suggestionTypeName) {
        this.suggestionTypeName = suggestionTypeName;
    }

    public String getSuggestionTypeDesc() {
        return suggestionTypeDesc;
    }

    public void setSuggestionTypeDesc(String suggestionTypeDesc) {
        this.suggestionTypeDesc = suggestionTypeDesc;
    }

}

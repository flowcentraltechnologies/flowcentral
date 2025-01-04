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

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.common.annotation.ColumnType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Application help entry entity;
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_HELPENTRY",
    uniqueConstraints = {
        @UniqueConstraint({ "appHelpSheetId", "fieldName" }) })
public class AppHelpEntry extends BaseAuditEntity {

    @ForeignKey(AppHelpSheet.class)
    private Long appHelpSheetId;

    @Column(name = "FIELD_NM", length = 64)
    private String fieldName;

    @Column(name = "HELP_CONTENT", type = ColumnType.CLOB)
    private String helpContent;

    @ListOnly(key = "appHelpSheetId", property="applicationId")
    private Long applicationId;

    @ListOnly(key = "appHelpSheetId", property="applicationName")
    private String applicationName;

    @ListOnly(key = "appHelpSheetId", property="name")
    private String sheetName;

    @ListOnly(key = "appHelpSheetId", property="entity")
    private String entity;

    @Override
    public String getDescription() {
        return fieldName;
    }

    public Long getAppHelpSheetId() {
        return appHelpSheetId;
    }

    public void setAppHelpSheetId(Long appHelpSheetId) {
        this.appHelpSheetId = appHelpSheetId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getHelpContent() {
        return helpContent;
    }

    public void setHelpContent(String helpContent) {
        this.helpContent = helpContent;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }
}

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

package com.flowcentraltech.flowcentral.studio.entities;

import com.flowcentraltech.flowcentral.application.entities.Application;
import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.flowcentraltech.flowcentral.configuration.constants.EntityBaseType;
import com.tcdng.unify.common.annotation.ColumnType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Studio JSON entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_STUDIOJSONENTITY")
public class StudioJsonEntity extends BaseAuditEntity {

    @ForeignKey(Application.class)
    private Long applicationId;

    @ForeignKey(name = "ENTITY_BASE_TY")
    private EntityBaseType baseType;

    @Column(length = 32)
    private String entityName;
    
    @Column(name = "DATE_FORMATTER", length = 64, nullable = true)
    private String dateFormatter;

    @Column(name = "DATETIME_FORMATTER", length = 64, nullable = true)
    private String dateTimeFormatter;

    @Column(length = 64, nullable = true)
    private String delegate;
    
    @Column(name = "SOURCE_JSON", type = ColumnType.CLOB)
    private String sourceJson;
    
    @Column(name = "REFINE_STRUCTURE", type = ColumnType.CLOB)
    private String refinedStructure;

    @Column(nullable = true)
    private Boolean generateEntity;

    @Column(nullable = true)
    private Boolean loadSourceJSON;
    
    @Column(nullable = true)
    private Boolean generateApplet;

    @Column(nullable = true)
    private Boolean generateRest;

    @Column(nullable = true)
    private Boolean generateImport;
    
    @ListOnly(key = "applicationId", property = "name")
    private String applicationName;

    @ListOnly(key = "applicationId", property = "description")
    private String applicationDesc;

    @ListOnly(key = "applicationId", property = "moduleId")
    private Long moduleId;

    @ListOnly(key = "applicationId", property = "moduleName")
    private String moduleName;

    @ListOnly(key = "applicationId", property = "moduleDesc")
    private String moduleDesc;

    @Override
    public String getDescription() {
        return null;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public EntityBaseType getBaseType() {
        return baseType;
    }

    public void setBaseType(EntityBaseType baseType) {
        this.baseType = baseType;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getDateFormatter() {
        return dateFormatter;
    }

    public void setDateFormatter(String dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    public String getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    public void setDateTimeFormatter(String dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public String getDelegate() {
        return delegate;
    }

    public void setDelegate(String delegate) {
        this.delegate = delegate;
    }

    public String getSourceJson() {
        return sourceJson;
    }

    public void setSourceJson(String sourceJson) {
        this.sourceJson = sourceJson;
    }

    public String getRefinedStructure() {
        return refinedStructure;
    }

    public void setRefinedStructure(String refinedStructure) {
        this.refinedStructure = refinedStructure;
    }

    public Boolean getGenerateEntity() {
        return generateEntity;
    }

    public void setGenerateEntity(Boolean generateEntity) {
        this.generateEntity = generateEntity;
    }

    public Boolean getLoadSourceJSON() {
        return loadSourceJSON;
    }

    public void setLoadSourceJSON(Boolean loadSourceJSON) {
        this.loadSourceJSON = loadSourceJSON;
    }

    public Boolean getGenerateApplet() {
        return generateApplet;
    }

    public void setGenerateApplet(Boolean generateApplet) {
        this.generateApplet = generateApplet;
    }

    public Boolean getGenerateRest() {
        return generateRest;
    }

    public void setGenerateRest(Boolean generateRest) {
        this.generateRest = generateRest;
    }

    public Boolean getGenerateImport() {
        return generateImport;
    }

    public void setGenerateImport(Boolean generateImport) {
        this.generateImport = generateImport;
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

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleDesc() {
        return moduleDesc;
    }

    public void setModuleDesc(String moduleDesc) {
        this.moduleDesc = moduleDesc;
    }

}

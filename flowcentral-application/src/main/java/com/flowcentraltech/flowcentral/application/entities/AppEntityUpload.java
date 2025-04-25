/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
import com.tcdng.unify.core.annotation.Child;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.batch.ConstraintAction;

/**
 * Application entity upload configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_ENTITYUPLOAD")
public class AppEntityUpload extends BaseConfigNamedEntity {

    @ForeignKey(AppEntity.class)
    private Long appEntityId;

    @ForeignKey
    private ConstraintAction constraintAction;

    @ListOnly(key = "constraintAction", property = "description")
    private String constraintActionDesc;

    @ListOnly(key = "appEntityId", property = "applicationId")
    private Long applicationId;

    @ListOnly(key = "appEntityId", property = "applicationName")
    private String applicationName;

    @ListOnly(key = "appEntityId", property = "name")
    private String entityName;

    @Child(category = "entity-upload")
    private AppFieldSequence fieldSequence;

    public Long getAppEntityId() {
        return appEntityId;
    }

    public void setAppEntityId(Long appEntityId) {
        this.appEntityId = appEntityId;
    }

    public ConstraintAction getConstraintAction() {
        return constraintAction;
    }

    public void setConstraintAction(ConstraintAction constraintAction) {
        this.constraintAction = constraintAction;
    }

    public String getConstraintActionDesc() {
        return constraintActionDesc;
    }

    public void setConstraintActionDesc(String constraintActionDesc) {
        this.constraintActionDesc = constraintActionDesc;
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

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public AppFieldSequence getFieldSequence() {
        return fieldSequence;
    }

    public void setFieldSequence(AppFieldSequence fieldSequence) {
        this.fieldSequence = fieldSequence;
    }

}

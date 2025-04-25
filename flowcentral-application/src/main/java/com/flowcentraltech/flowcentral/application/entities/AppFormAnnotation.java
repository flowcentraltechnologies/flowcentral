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
import com.flowcentraltech.flowcentral.configuration.constants.FormAnnotationType;
import com.flowcentraltech.flowcentral.configuration.constants.VisibilityType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Child;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Application form annotation entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_FORMANNOTATION", uniqueConstraints = { @UniqueConstraint({ "appFormId", "name" }),
        @UniqueConstraint({ "appFormId", "description" }) })
public class AppFormAnnotation extends BaseConfigNamedEntity {

    @ForeignKey(AppForm.class)
    private Long appFormId;

    @ForeignKey(name = "FORMANNOTATION_TY")
    private FormAnnotationType type;
    
    @ForeignKey(name = "VISIBILITY", nullable = true)
    private VisibilityType visibility;

    @Column(length = 512)
    private String message;

    @Column
    private boolean html;

    @Column
    private boolean directPlacement;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    @ListOnly(key = "visibility", property = "description")
    private String visibilityDesc;
    
    @Child(category = "formannotation")
    private AppFilter onCondition;

    public Long getAppFormId() {
        return appFormId;
    }

    public void setAppFormId(Long appFormId) {
        this.appFormId = appFormId;
    }

    public FormAnnotationType getType() {
        return type;
    }

    public void setType(FormAnnotationType type) {
        this.type = type;
    }

    public VisibilityType getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityType visibility) {
        this.visibility = visibility;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isHtml() {
        return html;
    }

    public void setHtml(boolean html) {
        this.html = html;
    }

    public boolean isDirectPlacement() {
        return directPlacement;
    }

    public void setDirectPlacement(boolean directPlacement) {
        this.directPlacement = directPlacement;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getVisibilityDesc() {
        return visibilityDesc;
    }

    public void setVisibilityDesc(String visibilityDesc) {
        this.visibilityDesc = visibilityDesc;
    }

    public AppFilter getOnCondition() {
        return onCondition;
    }

    public void setOnCondition(AppFilter onCondition) {
        this.onCondition = onCondition;
    }

}

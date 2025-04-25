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
package com.flowcentraltech.flowcentral.system.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.flowcentraltech.flowcentral.system.constants.DelegateRedirectType;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;

/**
 * Delegate redirect entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_DELEGATEREDIRECT", uniqueConstraints = {
        @UniqueConstraint({ "delegateId", "name" }),
        @UniqueConstraint({ "delegateId", "description" }) })
public class DelegateRedirect extends BaseAuditEntity {

    @ForeignKey(Delegate.class)
    private Long delegateId;

    @ForeignKey(name = "REDIRECT_TYPE")
    private DelegateRedirectType type;
    
    @Column(name = "REDIRECT_NM", length = 64)
    private String name;

    @Column(name = "REDIRECT_DESC", length = 128)
    private String description;

    @Column(name = "REDIRECT_URL", length = 1024)
    private String redirectUrl;

    @Column(name = "SUPPORTED_OPERATIONS", length = 2048, nullable = true)
    private String supportedOperations;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    @ListOnly(key = "delegateId", property = "name")
    private String delegateName;

    @ListOnly(key = "delegateId", property = "description")
    private String delegateDesc;

    @Override
    public String getDescription() {
        return description;
    }

    public Long getDelegateId() {
        return delegateId;
    }

    public void setDelegateId(Long delegateId) {
        this.delegateId = delegateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public DelegateRedirectType getType() {
        return type;
    }

    public void setType(DelegateRedirectType type) {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getSupportedOperations() {
        return supportedOperations;
    }

    public void setSupportedOperations(String supportedOperations) {
        this.supportedOperations = supportedOperations;
    }

    public String getDelegateName() {
        return delegateName;
    }

    public void setDelegateName(String delegateName) {
        this.delegateName = delegateName;
    }

    public String getDelegateDesc() {
        return delegateDesc;
    }

    public void setDelegateDesc(String delegateDesc) {
        this.delegateDesc = delegateDesc;
    }

}

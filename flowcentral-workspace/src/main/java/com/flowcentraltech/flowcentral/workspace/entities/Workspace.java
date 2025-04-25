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
package com.flowcentraltech.flowcentral.workspace.entities;

import java.util.List;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.ChildList;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.Policy;

/**
 * Workspace entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Policy("workspace-entitypolicy")
@Table(name = "FC_WORKSPACE", uniqueConstraints = { @UniqueConstraint({ "code" }), @UniqueConstraint({ "name" }),
        @UniqueConstraint({ "description" }) })
public class Workspace extends BaseStatusEntity {

    @Column(name = "WORKSPACE_CD", length = 32)
    private String code;

    @Column(name = "WORKSPACE_NM", length = 64)
    private String name;

    @Column(name = "WORKSPACE_DESC", length = 96)
    private String description;

    @ChildList
    private List<WorkspacePrivilege> privilegeList;

    public Workspace(Long id, String code, String name, String description) {
        this.setId(id);
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public Workspace() {

    }

    @Override
    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public List<WorkspacePrivilege> getPrivilegeList() {
        return privilegeList;
    }

    public void setPrivilegeList(List<WorkspacePrivilege> privilegeList) {
        this.privilegeList = privilegeList;
    }

}

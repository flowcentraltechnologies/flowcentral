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

package com.flowcentraltech.flowcentral.repository.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntity;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;

/**
 * Remote repository branch entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table(name = "FC_REMOTEREPOBRANCH",
        uniqueConstraints = {
                @UniqueConstraint({ "remoteRepoConfigId", "name" }),
                @UniqueConstraint({ "remoteRepoConfigId", "description" }) })
public class RemoteRepoBranch extends BaseStatusEntity {

    @ForeignKey(RemoteRepoConfig.class)
    private Long remoteRepoConfigId;
    
    @Column(name = "BRANCH_NM", length = 96)
    private String name;

    @Column(name = "BRANCH_DESC", length = 128)
    private String description;

    @ListOnly(key = "remoteRepoConfigId", property ="name")
    private String repoName;

    @ListOnly(key = "remoteRepoConfigId", property ="description")
    private String repoDesc;
    
    @Override
    public String getDescription() {
        return description;
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

    public Long getRemoteRepoConfigId() {
        return remoteRepoConfigId;
    }

    public void setRemoteRepoConfigId(Long remoteRepoConfigId) {
        this.remoteRepoConfigId = remoteRepoConfigId;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getRepoDesc() {
        return repoDesc;
    }

    public void setRepoDesc(String repoDesc) {
        this.repoDesc = repoDesc;
    }


}

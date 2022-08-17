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
package com.flowcentraltech.flowcentral.workspace.entities;

import java.util.Collection;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntityQuery;
import com.flowcentraltech.flowcentral.configuration.constants.DefaultApplicationConstants;

/**
 * Workspace query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WorkspaceQuery extends BaseStatusEntityQuery<Workspace> {

    public WorkspaceQuery() {
        super(Workspace.class);
    }

    public WorkspaceQuery code(String code) {
        return (WorkspaceQuery) addEquals("code", code);
    }

    public WorkspaceQuery codeNotMaster() {
        return (WorkspaceQuery) addNotEquals("code", DefaultApplicationConstants.ROOT_WORKSPACE_CODE);
    }

    public WorkspaceQuery codeIn(Collection<String> codeList) {
        return (WorkspaceQuery) addAmongst("code", codeList);
    }

    public WorkspaceQuery name(String name) {
        return (WorkspaceQuery) addEquals("name", name);
    }

    public WorkspaceQuery nameLike(String name) {
        return (WorkspaceQuery) addLike("name", name);
    }

}

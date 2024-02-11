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

package com.flowcentraltech.flowcentral.system.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntityQuery;

/**
 * Credential query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class CredentialQuery extends BaseStatusEntityQuery<Credential> {

    public CredentialQuery() {
        super(Credential.class);
    }

    public CredentialQuery name(String name) {
        return (CredentialQuery) addEquals("name", name);
    }

    public CredentialQuery nameLike(String name) {
        return (CredentialQuery) addLike("name", name);
    }

    public CredentialQuery descriptionLike(String description) {
        return (CredentialQuery) addLike("description", description);
    }
}

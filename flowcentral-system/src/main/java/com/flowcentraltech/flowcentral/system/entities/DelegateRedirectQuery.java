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

import java.util.Collection;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntityQuery;
import com.flowcentraltech.flowcentral.configuration.constants.SysParamType;

/**
 * Module application query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class DelegateRedirectQuery extends BaseAuditEntityQuery<DelegateRedirect> {

    public DelegateRedirectQuery() {
        super(DelegateRedirect.class);
    }

    public DelegateRedirectQuery delegateId(Long delegateId) {
        return (DelegateRedirectQuery) addEquals("delegateId", delegateId);
    }

    public DelegateRedirectQuery delegateIdNotIn(Collection<Long> delegateId) {
        return (DelegateRedirectQuery) addNotAmongst("delegateId", delegateId);
    }

    public DelegateRedirectQuery delegateName(String delegateName) {
        return (DelegateRedirectQuery) addEquals("delegateName", delegateName);
    }

    public DelegateRedirectQuery type(SysParamType type) {
        return (DelegateRedirectQuery) addEquals("type", type);
    }

    public DelegateRedirectQuery name(String name) {
        return (DelegateRedirectQuery) addEquals("name", name);
    }

    public DelegateRedirectQuery nameLike(String name) {
        return (DelegateRedirectQuery) addLike("name", name);
    }

    public DelegateRedirectQuery shortDescriptionLike(String shortDescription) {
        return (DelegateRedirectQuery) addLike("shortDescription", shortDescription);
    }

}

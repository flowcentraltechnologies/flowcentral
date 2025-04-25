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
package com.flowcentraltech.flowcentral.system.entities;

import java.util.Collection;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntityQuery;
import com.flowcentraltech.flowcentral.configuration.constants.SysParamType;

/**
 * System parameter query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SystemParameterQuery extends BaseAuditEntityQuery<SystemParameter> {

    public SystemParameterQuery() {
        super(SystemParameter.class);
    }

    public SystemParameterQuery moduleId(Long moduleId) {
        return (SystemParameterQuery) addEquals("moduleId", moduleId);
    }

    public SystemParameterQuery moduleIdNotIn(Collection<Long> moduleId) {
        return (SystemParameterQuery) addNotAmongst("moduleId", moduleId);
    }

    public SystemParameterQuery moduleCode(String moduleCode) {
        return (SystemParameterQuery) addEquals("moduleCode", moduleCode);
    }

    public SystemParameterQuery type(SysParamType type) {
        return (SystemParameterQuery) addEquals("type", type);
    }

    public SystemParameterQuery code(String code) {
        return (SystemParameterQuery) addEquals("code", code);
    }

    public SystemParameterQuery codeLike(String code) {
        return (SystemParameterQuery) addLike("code", code);
    }

    public SystemParameterQuery codeBeginsWith(String code) {
        return (SystemParameterQuery) addBeginsWith("code", code);
    }

    public SystemParameterQuery codeNotIn(Collection<String> codes) {
        return (SystemParameterQuery) addNotAmongst("code", codes);
    }

}

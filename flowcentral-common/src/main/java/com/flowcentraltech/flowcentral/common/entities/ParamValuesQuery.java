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
package com.flowcentraltech.flowcentral.common.entities;

/**
 * Common parameter values query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ParamValuesQuery extends BaseAuditEntityQuery<ParamValues> {

    public ParamValuesQuery() {
        super(ParamValues.class);
    }

    public ParamValuesQuery entityInstId(Long entityInstId) {
        return (ParamValuesQuery) addEquals("entityInstId", entityInstId);
    }

    public ParamValuesQuery entity(String entity) {
        return (ParamValuesQuery) addEquals("entity", entity);
    }

    public ParamValuesQuery category(String category) {
        return (ParamValuesQuery) addEquals("category", category);
    }

}

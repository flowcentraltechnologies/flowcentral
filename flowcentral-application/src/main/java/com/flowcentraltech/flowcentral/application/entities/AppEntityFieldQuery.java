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
package com.flowcentraltech.flowcentral.application.entities;

import java.util.Arrays;
import java.util.Collection;

import com.flowcentraltech.flowcentral.common.entities.BaseConfigEntityQuery;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldType;

/**
 * Application entity field query.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class AppEntityFieldQuery extends BaseConfigEntityQuery<AppEntityField> {

    public AppEntityFieldQuery() {
        super(AppEntityField.class);
    }

    public AppEntityFieldQuery appEntityId(Long appEntityId) {
        return (AppEntityFieldQuery) addEquals("appEntityId", appEntityId);
    }

    public AppEntityFieldQuery appEntityName(String appEntityName) {
        return (AppEntityFieldQuery) addEquals("appEntityName", appEntityName);
    }

    public AppEntityFieldQuery name(String name) {
        return (AppEntityFieldQuery) addEquals("name", name);
    }

    public AppEntityFieldQuery nameNotIn(String... name) {
        return (AppEntityFieldQuery) addNotAmongst("name", Arrays.asList(name));
    }

    public AppEntityFieldQuery applicationName(String applicationName) {
        return (AppEntityFieldQuery) addEquals("applicationName", applicationName);
    }

    public AppEntityFieldQuery applicationNameNot(String applicationName) {
        return (AppEntityFieldQuery) addNotEquals("applicationName", applicationName);
    }

    public AppEntityFieldQuery dataType(EntityFieldDataType type) {
        return (AppEntityFieldQuery) addEquals("dataType", type);
    }

    public AppEntityFieldQuery dataTypeIn(EntityFieldDataType... type) {
        return (AppEntityFieldQuery) addAmongst("dataType", Arrays.asList(type));
    }

    public AppEntityFieldQuery dataTypeNotIn(EntityFieldDataType... type) {
        return (AppEntityFieldQuery) addNotAmongst("dataType", Arrays.asList(type));
    }

    public AppEntityFieldQuery type(EntityFieldType type) {
        return (AppEntityFieldQuery) addEquals("type", type);
    }

    public AppEntityFieldQuery referencesIn(Collection<String> references) {
        return (AppEntityFieldQuery) addAmongst("references", references);
    }

    public AppEntityFieldQuery referencesBeginsWith(String references) {
        return (AppEntityFieldQuery) addBeginsWith("references", references);
    }

    public AppEntityFieldQuery inputWidgetBeginsWith(String inputWidget) {
        return (AppEntityFieldQuery) addBeginsWith("inputWidget", inputWidget);
    }

    public AppEntityFieldQuery suggestionTypeBeginsWith(String suggestionType) {
        return (AppEntityFieldQuery) addBeginsWith("suggestionType", suggestionType);
    }

    public AppEntityFieldQuery lingualWidgetBeginsWith(String lingualWidget) {
        return (AppEntityFieldQuery) addBeginsWith("lingualWidget", lingualWidget);
    }

    public AppEntityFieldQuery isEntityField() {
        return (AppEntityFieldQuery) addIsNull("key");
    }

    public AppEntityFieldQuery isListField() {
        return (AppEntityFieldQuery) addIsNotNull("key").addIsNotNull("property");
    }

}

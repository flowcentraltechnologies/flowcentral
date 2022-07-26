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

package com.flowcentraltech.flowcentral.configuration.constants;

import com.tcdng.unify.convert.constants.EnumConst;
import com.tcdng.unify.core.criterion.FilterConditionListType;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Entity child category type
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public enum EntityChildCategoryType implements EnumConst {

    APP_REF(
            "REF",
            "application.appRef",
            "entity",
            "ref",
            FilterConditionListType.IMMEDIATE_FIELD,
            null),
    APP_APPLET(
            "APPLET",
            "application.appApplet",
            "entity",
            "applet",
            FilterConditionListType.IMMEDIATE_FIELD,
            null),
    APP_TABLE(
            "TABLE",
            "application.appTable",
            "entity",
            "table",
            FilterConditionListType.IMMEDIATE_FIELD,
            null),
    APP_ENTITYUPLOAD(
            "ENTITYUPLOAD",
            "application.appEntity",
            "name",
            "entity-upload",
            FilterConditionListType.IMMEDIATE_FIELD,
            null),
    APP_FORMSTATEPOLICY(
            "FORMSTATEPOLICY",
            "application.appForm",
            "entity",
            "formstatepolicy",
            FilterConditionListType.IMMEDIATE_FIELD,
            null),
    APP_FORMWIDGETRULES(
            "FORMWIDGETRULES",
            "application.appForm",
            "entity",
            "formwidgetrulespolicy",
            FilterConditionListType.IMMEDIATE_FIELD,
            null),
    APP_FORMACTION(
            "FORMACTION",
            "application.appForm",
            "entity",
            "formaction",
            FilterConditionListType.IMMEDIATE_FIELD,
            null),
    APP_FORMVALIDATIONPOLICY(
            "FORMVALIDATIONPOLICY",
            "application.appForm",
            "entity",
            "formvalidationpolicy",
            FilterConditionListType.IMMEDIATE_FIELD,
            null),
    APP_FORMREVIEWPOLICY(
            "FORMREVIEWPOLICY",
            "application.appForm",
            "entity",
            "formreviewpolicy",
            FilterConditionListType.IMMEDIATE_FIELD,
            null),
    REPORT_CONFIG(
            "REPORTCONFIG",
            "report.reportConfig",
            "reportable",
            "report-config",
            FilterConditionListType.IMMEDIATE_PARAM,
            "reportparameterlist"),
    WORKFLOW(
            "WORKFLOW",
            "workflow.workflow",
            "entity",
            "workflow",
            FilterConditionListType.IMMEDIATE_FIELD,
            null);

    private final String code;

    private final String readEntity;

    private final String readField;

    private final String category;

    private final FilterConditionListType listType;

    private final String paramList;

    private EntityChildCategoryType(String code, String readEntity, String readField, String category,
            FilterConditionListType listType, String paramList) {
        this.code = code;
        this.readEntity = readEntity;
        this.readField = readField;
        this.category = category;
        this.listType = listType;
        this.paramList = paramList;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String defaultCode() {
        return APP_REF.code;
    }

    public String readEntity() {
        return readEntity;
    }

    public String readField() {
        return readField;
    }

    public String category() {
        return category;
    }

    public FilterConditionListType listType() {
        return listType;
    }

    public String paramList() {
        return paramList;
    }

    public static EntityChildCategoryType fromCode(String code) {
        return EnumUtils.fromCode(EntityChildCategoryType.class, code);
    }

    public static EntityChildCategoryType fromName(String name) {
        return EnumUtils.fromName(EntityChildCategoryType.class, name);
    }
}

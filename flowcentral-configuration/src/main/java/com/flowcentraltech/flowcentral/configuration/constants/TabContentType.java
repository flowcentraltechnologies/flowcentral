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

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Tab content type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_TABCONTENTTYPE")
@StaticList(name = "tabcontenttypelist", description = "$m{staticlist.tabcontenttypelist}")
public enum TabContentType implements EnumConst {

    MINIFORM(
            "MNF"),
    MINIFORM_CHANGELOG(
            "MNC"),
    MINIFORM_MAPPED(
            "MNM"),
    CHILD(
            "CHD"),
    CHILD_LIST(
            "CHL"),
    FILTER_CONDITION(
            "FIL"),
    FIELD_SEQUENCE(
            "FSQ"),
    PARAM_VALUES(
            "PMV"),
    PROPERTY_LIST(
            "PRL"),
    USAGE_LIST(
            "USL"),
    SEARCH_INPUT(
            "SCH"),
    SET_VALUES(
            "STV"),
    WIDGET_RULES(
            "WGR");

    private final String code;

    private TabContentType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return MINIFORM.code;
    }

    public boolean isChildOrChildList() {
        return this.equals(CHILD) || this.equals(CHILD_LIST);
    }

    public boolean isMappped() {
        return this.equals(MINIFORM_MAPPED);
    }

    public boolean isMiniForm() {
        return this.equals(MINIFORM) || this.equals(MINIFORM_CHANGELOG) || this.equals(MINIFORM_MAPPED);
    }

    public static TabContentType fromCode(String code) {
        return EnumUtils.fromCode(TabContentType.class, code);
    }

    public static TabContentType fromName(String name) {
        return EnumUtils.fromName(TabContentType.class, name);
    }

}

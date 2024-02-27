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
package com.flowcentraltech.flowcentral.configuration.constants;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.report.ReportLayoutType;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Report configuration type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_REPORTCONFIGTYPE")
@StaticList(name = "reportconfigtypelist", description = "$m{staticlist.reportconfigtypelist}")
public enum ReportConfigType implements EnumConst {

    TABULAR(
            "TBL",
            ReportLayoutType.TABULAR),
    CHART_SUMMARY(
            "CSM",
            ReportLayoutType.TABULAR),
    PLACEMENT(
            "PLC",
            ReportLayoutType.PLACEMENT_PDF),
    LETTER(
            "LTR",
            ReportLayoutType.HTML_PDF);

    private final String code;

    private final ReportLayoutType layout;

    private ReportConfigType(String code, ReportLayoutType layout) {
        this.code = code;
        this.layout = layout;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return TABULAR.code;
    }

    public ReportLayoutType layout() {
        return layout;
    }

    public boolean isTabular() {
        return TABULAR.equals(this);
    }

    public boolean isPlacement() {
        return PLACEMENT.equals(this);
    }

    public boolean isLetter() {
        return LETTER.equals(this);
    }

    public static ReportConfigType fromCode(String code) {
        return EnumUtils.fromCode(ReportConfigType.class, code);
    }

    public static ReportConfigType fromName(String name) {
        return EnumUtils.fromName(ReportConfigType.class, name);
    }
}

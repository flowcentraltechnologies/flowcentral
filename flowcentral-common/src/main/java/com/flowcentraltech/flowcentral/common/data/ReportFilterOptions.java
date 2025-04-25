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

package com.flowcentraltech.flowcentral.common.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tcdng.unify.core.criterion.RestrictionType;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Report filter option.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ReportFilterOptions {

    private RestrictionType op;

    private String tableName;

    private String columnName;

    private Object param1;

    private Object param2;

    private List<ReportFilterOptions> subFilterOptionList;

    public ReportFilterOptions(RestrictionType op, String tableName, String columnName, Object param1, Object param2) {
        this.op = op;
        if (op.isCompound()) {
            this.subFilterOptionList = new ArrayList<ReportFilterOptions>();
        } else {
            this.tableName = tableName;
            this.columnName = columnName;
            this.param1 = param1;
            this.param2 = param2;
            this.subFilterOptionList = Collections.emptyList();
        }
    }

    public ReportFilterOptions(RestrictionType op) {
        this(op, null, null, null, null);
    }

    public boolean isIdEqualsRestricted(String idColumnName) {
        if (RestrictionType.EQUALS.equals(op) && columnName.equals(idColumnName)) {
            return true;
        }

        if (subFilterOptionList != null) {
            for (ReportFilterOptions reportFilterOptions : subFilterOptionList) {
                if (reportFilterOptions.isIdEqualsRestricted(idColumnName)) {
                    return true;
                }
            }
        }

        return false;
    }

    public RestrictionType getOp() {
        return op;
    }

    public boolean isCompound() {
        return op.isCompound();
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public Object getParam1() {
        return param1;
    }

    public Object getParam2() {
        return param2;
    }

    public ReportFilterOptions addReportFilterOptions(ReportFilterOptions reportFilterOptions) {
        subFilterOptionList.add(reportFilterOptions);
        return this;
    }

    public List<ReportFilterOptions> getSubFilterOptionList() {
        return subFilterOptionList;
    }

    public String toString() {
        return StringUtils.toXmlString(this);
    }
}

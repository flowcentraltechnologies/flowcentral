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

package com.flowcentraltech.flowcentral.common.data;

import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.constant.VAlignType;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Report column options.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ReportColumnOptions {

    private static final int DEFAULT_WIDTH = 10;

    private String tableName;

    private String columnName;

    private String description;

    private String type;

    private String formatter;

    private HAlignType hAlignType;

    private VAlignType vAlignType;

    private int width;

    private OrderType orderType;

    private boolean bold;

    private boolean group;

    private boolean groupOnNewPage;

    private boolean sum;

    private boolean included;

    public ReportColumnOptions(String columnName, String description, String type, String formatter,
            HAlignType hAlignType, int width, boolean included) {
        this.columnName = columnName;
        this.description = description;
        this.type = type;
        this.formatter = formatter;
        this.included = included;
        this.vAlignType = VAlignType.TOP;

        setHAlignType(hAlignType);
        setWidth(width);
    }

    public ReportColumnOptions() {
        this.setWidth(0);
        this.hAlignType = HAlignType.LEFT;
        this.vAlignType = VAlignType.TOP;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormatter() {
        return formatter;
    }

    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }

    public HAlignType getHAlignType() {
        return hAlignType;
    }

    public void setHAlignType(HAlignType hAlignType) {
        this.hAlignType = hAlignType;
        if (this.hAlignType == null) {
            this.hAlignType = HAlignType.LEFT;
        }
    }

    public VAlignType getVAlignType() {
        return vAlignType;
    }

    public void setVAlignType(VAlignType vAlignType) {
        this.vAlignType = vAlignType;
        if (this.vAlignType == null) {
            this.vAlignType = VAlignType.TOP;
        }
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        if (this.width <= 0) {
            this.width = DEFAULT_WIDTH;
        }
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public boolean isGroupOnNewPage() {
        return groupOnNewPage;
    }

    public void setGroupOnNewPage(boolean groupOnNewPage) {
        this.groupOnNewPage = groupOnNewPage;
    }

    public boolean isSum() {
        return sum;
    }

    public void setSum(boolean sum) {
        this.sum = sum;
    }

    public boolean isIncluded() {
        return included;
    }

    public void setIncluded(boolean included) {
        this.included = included;
    }

    @Override
    public String toString() {
        return StringUtils.toXmlString(this);
    }

}

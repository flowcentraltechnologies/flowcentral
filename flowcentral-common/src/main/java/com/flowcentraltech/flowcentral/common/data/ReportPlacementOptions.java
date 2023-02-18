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

package com.flowcentraltech.flowcentral.common.data;

import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.constant.VAlignType;
import com.tcdng.unify.core.constant.XOffsetType;
import com.tcdng.unify.core.constant.YOffsetType;
import com.tcdng.unify.core.report.ReportPlacementType;

/**
 * Report placement options.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ReportPlacementOptions {

    private final ReportPlacementType type;

    private String tableName;

    private String columnName;

    private String dataType;

    private String text;

    private String formatter;

    private HAlignType hAlignType;

    private VAlignType vAlignType;
    
    private XOffsetType xOffsetType;

    private YOffsetType yOffsetType;

    private int x;

    private int y;

    private int width;

    private int height;

    private boolean bold;

    public ReportPlacementOptions(ReportPlacementType type) {
        this.type = type;
        this.hAlignType = HAlignType.LEFT;
        this.vAlignType = VAlignType.TOP;
        this.xOffsetType = XOffsetType.LEFT;
        this.yOffsetType = YOffsetType.TOP;
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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
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

    public XOffsetType getXOffsetType() {
        return xOffsetType;
    }

    public void setXOffsetType(XOffsetType xOffsetType) {
        this.xOffsetType = xOffsetType;
    }

    public YOffsetType getYOffsetType() {
        return yOffsetType;
    }

    public void setYOffsetType(YOffsetType yOffsetType) {
        this.yOffsetType = yOffsetType;
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
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFormatter() {
        return formatter;
    }

    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ReportPlacementType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "[type=" + type + ", tableName=" + tableName + ", columnName=" + columnName
                + ", dataType=" + dataType + ", text=" + text + ", formatter=" + formatter + ", hAlignType="
                + hAlignType + ", vAlignType=" + vAlignType + ", x=" + x + ", y=" + y + ", width=" + width + ", height="
                + height + ", bold=" + bold + "]";
    }

}

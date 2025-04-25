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
package com.flowcentraltech.flowcentral.configuration.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.HAlignTypeXmlAdapter;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.ReportPlacementTypeXmlAdapter;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.VAlignTypeXmlAdapter;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.XOffsetTypeXmlAdapter;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.YOffsetTypeXmlAdapter;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.constant.VAlignType;
import com.tcdng.unify.core.constant.XOffsetType;
import com.tcdng.unify.core.constant.YOffsetType;
import com.tcdng.unify.core.report.ReportPlacementType;

/**
 * Placement configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @version 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class ReportPlacementConfig extends BaseConfig {

    @JsonSerialize(using = ReportPlacementTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = ReportPlacementTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "type")
    private ReportPlacementType type;

    @JsonSerialize(using = HAlignTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = HAlignTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "halign")
    private HAlignType horizAlignType;

    @JsonSerialize(using = VAlignTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = VAlignTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "valign")
    private VAlignType vertAlignType;

    @JsonSerialize(using = XOffsetTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = XOffsetTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "x-offset")
    private XOffsetType xOffsetType;

    @JsonSerialize(using = YOffsetTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = YOffsetTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "y-offset")
    private YOffsetType yOffsetType;
    
    @JacksonXmlProperty(isAttribute = true, localName="field")
    private String fieldName;

    @JacksonXmlProperty(isAttribute = true, localName = "text")
    private String text;

    @JacksonXmlProperty(isAttribute = true)
    private String formatter;

    @JacksonXmlProperty(isAttribute = true)
    private int x;

    @JacksonXmlProperty(isAttribute = true)
    private int y;

    @JacksonXmlProperty(isAttribute = true)
    private int width;

    @JacksonXmlProperty(isAttribute = true)
    private int height;

    @JacksonXmlProperty(isAttribute = true)
    private boolean bold;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ReportPlacementType getType() {
        return type;
    }

    public void setType(ReportPlacementType type) {
        this.type = type;
    }

    public HAlignType getHorizAlignType() {
        return horizAlignType;
    }

    public void setHorizAlignType(HAlignType horizAlignType) {
        this.horizAlignType = horizAlignType;
    }

    public VAlignType getVertAlignType() {
        return vertAlignType;
    }

    public void setVertAlignType(VAlignType vertAlignType) {
        this.vertAlignType = vertAlignType;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isBold() {
        return bold;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

}

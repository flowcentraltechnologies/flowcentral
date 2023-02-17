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
package com.flowcentraltech.flowcentral.configuration.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

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
public class ReportPlacementConfig {

    private ReportPlacementType type;

    private HAlignType horizAlignType;

    private VAlignType vertAlignType;

    private XOffsetType xOffsetType;

    private YOffsetType yOffsetType;
    
    private String fieldName;

    private String text;

    private String formatter;

    private int x;

    private int y;

    private int width;

    private int height;

    private boolean bold;

    public String getFieldName() {
        return fieldName;
    }

    @XmlAttribute(name = "field")
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getText() {
        return text;
    }

    @XmlAttribute(name = "text")
    public void setText(String text) {
        this.text = text;
    }

    public ReportPlacementType getType() {
        return type;
    }

    @XmlJavaTypeAdapter(ReportPlacementTypeXmlAdapter.class)
    @XmlAttribute(name = "type", required = true)
    public void setType(ReportPlacementType type) {
        this.type = type;
    }

    public HAlignType getHorizAlignType() {
        return horizAlignType;
    }

    @XmlJavaTypeAdapter(HAlignTypeXmlAdapter.class)
    @XmlAttribute(name = "halign")
    public void setHorizAlignType(HAlignType horizAlignType) {
        this.horizAlignType = horizAlignType;
    }

    public VAlignType getVertAlignType() {
        return vertAlignType;
    }

    @XmlJavaTypeAdapter(VAlignTypeXmlAdapter.class)
    @XmlAttribute(name = "valign")
    public void setVertAlignType(VAlignType vertAlignType) {
        this.vertAlignType = vertAlignType;
    }

    public XOffsetType getXOffsetType() {
        return xOffsetType;
    }

    @XmlJavaTypeAdapter(XOffsetTypeXmlAdapter.class)
    @XmlAttribute(name = "x-offset")
    public void setXOffsetType(XOffsetType xOffsetType) {
        this.xOffsetType = xOffsetType;
    }

    public YOffsetType getYOffsetType() {
        return yOffsetType;
    }

    @XmlJavaTypeAdapter(YOffsetTypeXmlAdapter.class)
    @XmlAttribute(name = "y-offset")
    public void setYOffsetType(YOffsetType yOffsetType) {
        this.yOffsetType = yOffsetType;
    }

    public String getFormatter() {
        return formatter;
    }

    @XmlAttribute
    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }

    public int getX() {
        return x;
    }

    @XmlAttribute
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    @XmlAttribute
    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    @XmlAttribute
    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    @XmlAttribute
    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isBold() {
        return bold;
    }

    @XmlAttribute
    public void setBold(boolean bold) {
        this.bold = bold;
    }

}

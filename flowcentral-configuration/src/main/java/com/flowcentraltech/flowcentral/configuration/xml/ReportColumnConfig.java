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
package com.flowcentraltech.flowcentral.configuration.xml;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.HAlignTypeXmlAdapter;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.OrderTypeXmlAdapter;
import com.flowcentraltech.flowcentral.configuration.xml.adapter.VAlignTypeXmlAdapter;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.constant.VAlignType;

/**
 * Column configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @version 1.0
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class ReportColumnConfig extends BaseConfig {

    @JacksonXmlProperty(isAttribute = true, localName = "field")
    private String fieldName;

    @JsonSerialize(using = OrderTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = OrderTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "order")
    private OrderType columnOrder;

    @JsonSerialize(using = HAlignTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = HAlignTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "halign")
    private HAlignType horizAlignType;

    @JsonSerialize(using = VAlignTypeXmlAdapter.Serializer.class)
    @JsonDeserialize(using = VAlignTypeXmlAdapter.Deserializer.class)
    @JacksonXmlProperty(isAttribute = true, localName = "valign")
    private VAlignType vertAlignType;

    @JacksonXmlProperty(isAttribute = true)
    private String renderWidget;

    @JacksonXmlProperty(isAttribute = true)
    private String description;

    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JacksonXmlProperty(isAttribute = true)
    private String formatter;

    @JacksonXmlProperty(isAttribute = true)
    private int width;

    @JacksonXmlProperty(isAttribute = true)
    private boolean bold;

    @JacksonXmlProperty(isAttribute = true)
    private boolean group;

    @JacksonXmlProperty(isAttribute = true)
    private boolean groupOnNewPage;

    @JacksonXmlProperty(isAttribute = true)
    private boolean sum;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public OrderType getColumnOrder() {
        return columnOrder;
    }

    public void setColumnOrder(OrderType columnOrder) {
        this.columnOrder = columnOrder;
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

    public String getRenderWidget() {
        return renderWidget;
    }

    public void setRenderWidget(String renderWidget) {
        this.renderWidget = renderWidget;
    }

    public String getFormatter() {
        return formatter;
    }

    public void setFormatter(String formatter) {
        this.formatter = formatter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

}

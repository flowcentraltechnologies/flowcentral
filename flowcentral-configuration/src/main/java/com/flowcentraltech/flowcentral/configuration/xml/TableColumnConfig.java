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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

/**
 * Table column configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@JsonInclude(value = Include.NON_NULL, content = Include.NON_EMPTY)
public class TableColumnConfig extends BaseConfig {

    @JacksonXmlProperty(isAttribute = true)
    private String field;

    @JacksonXmlProperty(isAttribute = true)
    private String label;

    @JacksonXmlProperty(isAttribute = true)
    private String renderWidget;

    @JacksonXmlProperty(isAttribute = true)
    private String linkAct;

    @JacksonXmlProperty(isAttribute = true)
    private String symbol;

    @JacksonXmlProperty(isAttribute = true)
    private String order;

    @JacksonXmlProperty(isAttribute = true)
    private int widthRatio;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean switchOnChange;
    
    @JacksonXmlProperty(isAttribute = true)
    private Boolean hiddenOnNull;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean hidden;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean disabled;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean editable;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean sortable;

    @JacksonXmlProperty(isAttribute = true)
    private Boolean summary;

    public TableColumnConfig() {
        this.switchOnChange = Boolean.FALSE;
        this.hiddenOnNull = Boolean.FALSE;
        this.hidden = Boolean.FALSE;
        this.disabled = Boolean.FALSE;
        this.editable = Boolean.FALSE;
        this.sortable = Boolean.TRUE;
        this.summary = Boolean.FALSE;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getRenderWidget() {
        return renderWidget;
    }

    public void setRenderWidget(String renderWidget) {
        this.renderWidget = renderWidget;
    }

    public String getLinkAct() {
        return linkAct;
    }

    public void setLinkAct(String linkAct) {
        this.linkAct = linkAct;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getWidthRatio() {
        return widthRatio;
    }

    public void setWidthRatio(int widthRatio) {
        this.widthRatio = widthRatio;
    }

    public Boolean getSwitchOnChange() {
        return switchOnChange;
    }

    public void setSwitchOnChange(Boolean switchOnChange) {
        this.switchOnChange = switchOnChange;
    }

    public Boolean getHiddenOnNull() {
        return hiddenOnNull;
    }

    public void setHiddenOnNull(Boolean hiddenOnNull) {
        this.hiddenOnNull = hiddenOnNull;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getSortable() {
        return sortable;
    }

    public void setSortable(Boolean sortable) {
        this.sortable = sortable;
    }

    public Boolean getSummary() {
        return summary;
    }

    public void setSummary(Boolean summary) {
        this.summary = summary;
    }

}

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
package com.flowcentraltech.flowcentral.configuration.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.unify.core.util.xml.MarshalFalseToNullXmlAdapter;
import com.tcdng.unify.core.util.xml.MarshalTrueToNullXmlAdapter;

/**
 * Table column configuration.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class TableColumnConfig {

    private String field;

    private String label;

    private String renderWidget;

    private String linkAct;

    private String symbol;

    private String order;

    private int widthRatio;

    private Boolean switchOnChange;
    
    private Boolean hiddenOnNull;

    private Boolean hidden;

    private Boolean disabled;

    private Boolean editable;

    private Boolean sortable;

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

    @XmlAttribute(required = true)
    public void setField(String field) {
        this.field = field;
    }

    public String getLabel() {
        return label;
    }

    @XmlAttribute
    public void setLabel(String label) {
        this.label = label;
    }

    public String getRenderWidget() {
        return renderWidget;
    }

    @XmlAttribute(required = true)
    public void setRenderWidget(String renderWidget) {
        this.renderWidget = renderWidget;
    }

    public String getLinkAct() {
        return linkAct;
    }

    @XmlAttribute
    public void setLinkAct(String linkAct) {
        this.linkAct = linkAct;
    }

    public String getSymbol() {
        return symbol;
    }

    @XmlAttribute
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getOrder() {
        return order;
    }

    @XmlAttribute
    public void setOrder(String order) {
        this.order = order;
    }

    public int getWidthRatio() {
        return widthRatio;
    }

    @XmlAttribute
    public void setWidthRatio(int widthRatio) {
        this.widthRatio = widthRatio;
    }

    public Boolean getSwitchOnChange() {
        return switchOnChange;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setSwitchOnChange(Boolean switchOnChange) {
        this.switchOnChange = switchOnChange;
    }

    public Boolean getHiddenOnNull() {
        return hiddenOnNull;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setHiddenOnNull(Boolean hiddenOnNull) {
        this.hiddenOnNull = hiddenOnNull;
    }

    public Boolean getHidden() {
        return hidden;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
   @XmlAttribute
    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getEditable() {
        return editable;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getSortable() {
        return sortable;
    }

    @XmlJavaTypeAdapter(MarshalTrueToNullXmlAdapter.class)
    @XmlAttribute
    public void setSortable(Boolean sortable) {
        this.sortable = sortable;
    }

    public Boolean getSummary() {
        return summary;
    }

    @XmlJavaTypeAdapter(MarshalFalseToNullXmlAdapter.class)
    @XmlAttribute
    public void setSummary(Boolean summary) {
        this.summary = summary;
    }

}

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

    private String order;

    private int widthRatio;

    private boolean switchOnChange;

    private boolean hidden;

    private boolean disabled;

    private boolean editable;

    private boolean sortable;

    private boolean summary;

    public TableColumnConfig() {
        this.sortable = true;
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

    public boolean isSwitchOnChange() {
        return switchOnChange;
    }

    @XmlAttribute
    public void setSwitchOnChange(boolean switchOnChange) {
        this.switchOnChange = switchOnChange;
    }

    public boolean isHidden() {
        return hidden;
    }

    @XmlAttribute
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isDisabled() {
        return disabled;
    }

    @XmlAttribute
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isEditable() {
        return editable;
    }

    @XmlAttribute
    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isSortable() {
        return sortable;
    }

    @XmlAttribute
    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    public boolean isSummary() {
        return summary;
    }

    @XmlAttribute
    public void setSummary(boolean summary) {
        this.summary = summary;
    }

}

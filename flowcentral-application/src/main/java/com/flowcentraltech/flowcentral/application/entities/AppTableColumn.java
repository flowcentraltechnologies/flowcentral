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
package com.flowcentraltech.flowcentral.application.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseConfigEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;

/**
 * Application table column entity;
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_TABLECOLUMN", uniqueConstraints = { @UniqueConstraint({ "appTableId", "field" }) })
public class AppTableColumn extends BaseConfigEntity {

    @ForeignKey(AppTable.class)
    private Long appTableId;

    @Column(length = 64)
    private String field;

    @Column(length = 128)
    private String renderWidget;

    @Column(length = 128, nullable = true)
    private String label;

    @Column(length = 64, nullable = true)
    private String linkAct;

    @Column(name = "LINK_SYMBOL", length = 32, nullable = true)
    private String symbol;

    @Column(name = "default_order", length = 32, nullable = true)
    private String order;

    @Column
    private int widthRatio;

    @Column
    private boolean switchOnChange;

    @Column
    private boolean hiddenOnNull;

    @Column
    private boolean hidden;

    @Column
    private boolean disabled;

    @Column
    private boolean editable;

    @Column
    private boolean sortable;

    @Column
    private boolean summary;

    @Override
    public String getDescription() {
        return field;
    }

    public Long getAppTableId() {
        return appTableId;
    }

    public void setAppTableId(Long appTableId) {
        this.appTableId = appTableId;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getRenderWidget() {
        return renderWidget;
    }

    public void setRenderWidget(String renderWidget) {
        this.renderWidget = renderWidget;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public boolean isSwitchOnChange() {
        return switchOnChange;
    }

    public void setSwitchOnChange(boolean switchOnChange) {
        this.switchOnChange = switchOnChange;
    }

    public boolean isHiddenOnNull() {
        return hiddenOnNull;
    }

    public void setHiddenOnNull(boolean hiddenOnNull) {
        this.hiddenOnNull = hiddenOnNull;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    public boolean isSummary() {
        return summary;
    }

    public void setSummary(boolean summary) {
        this.summary = summary;
    }

}

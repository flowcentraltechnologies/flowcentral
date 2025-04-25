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
package com.flowcentraltech.flowcentral.application.data;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Table column definition;
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class TableColumnDef {

    private String label;

    private String fieldName;

    private String headerStyle;

    private String cellRenderer;

    private String cellEditor;

    private String renderer;

    private String editor;

    private String linkAct;

    private String symbol;

    private int widthRatio;

    private OrderType order;

    private int width;

    private boolean switchOnChange;

    private boolean hiddenOnNull;

    private boolean hidden;

    private boolean disabled;

    private boolean editable;

    private boolean sortable;

    private boolean summary;

    public TableColumnDef(String label, String fieldName, String headerStyle, String cellRenderer, String cellEditor,
            String renderer, String editor, String linkAct, String symbol, OrderType order, int widthRatio, int width,
            boolean switchOnChange, boolean hiddenOnNull, boolean hidden, boolean disabled, boolean editable, boolean sortable,
            boolean summary) {
        this.label = label;
        this.fieldName = fieldName;
        this.headerStyle = headerStyle;
        this.cellRenderer = cellRenderer;
        this.cellEditor = cellEditor;
        this.renderer = renderer;
        this.editor = editor;
        this.linkAct = linkAct;
        this.symbol = symbol;
        this.order = order;
        this.widthRatio = widthRatio;
        this.width = width;
        this.switchOnChange = switchOnChange;
        this.hiddenOnNull = hiddenOnNull;
        this.hidden = hidden;
        this.disabled = disabled;
        this.editable = editable;
        this.sortable = sortable;
        this.summary = summary;
    }

    private TableColumnDef(String label, String fieldName, String renderer, String editor, String linkAct,
            String symbol, OrderType order, int widthRatio, boolean switchOnChange, boolean hiddenOnNull, boolean hidden, boolean disabled,
            boolean editable, boolean sortable, boolean summary) {
        this.label = label;
        this.fieldName = fieldName;
        this.renderer = renderer;
        this.editor = editor;
        this.linkAct = linkAct;
        this.symbol = symbol;
        this.order = order;
        this.widthRatio = widthRatio;
        this.switchOnChange = switchOnChange;
        this.hiddenOnNull = hiddenOnNull;
        this.hidden = hidden;
        this.disabled = disabled;
        this.editable = editable;
        this.sortable = sortable;
        this.summary = summary;
    }

    public String getLabel() {
        return label;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getHeaderStyle() {
        return headerStyle;
    }

    public String getCellRenderer() {
        return cellRenderer;
    }

    public String getCellEditor() {
        return cellEditor;
    }

    public OrderType getOrder() {
        return order;
    }

    public String getRenderer() {
        return renderer;
    }

    public String getEditor() {
        return editor;
    }

    public String getLinkAct() {
        return linkAct;
    }

    public boolean isWithLinkAct() {
        return !StringUtils.isBlank(linkAct);
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isWithSymbol() {
        return !StringUtils.isBlank(symbol);
    }

    public int getWidthRatio() {
        return widthRatio;
    }

    public int getWidth() {
        return width;
    }

    public boolean isWithCellEditor() {
        return cellEditor != null;
    }

    public boolean isWithLabel() {
        return !StringUtils.isBlank(label);
    }

    public boolean isSwitchOnChange() {
        return switchOnChange;
    }

    public boolean isHiddenOnNull() {
        return hiddenOnNull;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public boolean isEditable() {
        return editable;
    }

    public boolean isSortable() {
        return sortable;
    }

    public boolean isSummary() {
        return summary;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(AppletUtilities au, EntityFieldDef entityFieldDef) throws UnifyException {
        String renderWidget = InputWidgetUtils.resolveEntityFieldWidget(entityFieldDef);
        String renderer = InputWidgetUtils.constructRenderer(au.getWidgetTypeDef(renderWidget), entityFieldDef);
        String editor = !entityFieldDef.isListOnly()
                ? InputWidgetUtils.constructEditor(au.getWidgetTypeDef(renderWidget), entityFieldDef)
                : null;
        return new Builder().label(entityFieldDef.getFieldLabel()).fieldName(entityFieldDef.getFieldName())
                .renderer(renderer).editor(editor);
    }

    public static class Builder {

        private String label;

        private String fieldName;

        private String renderer;

        private String editor;

        private String linkAct;

        private String symbol;

        private OrderType order;

        private int widthRatio;

        private boolean switchOnChange;

        private boolean hiddenOnNull;

        private boolean hidden;

        private boolean disabled;

        private boolean editable;

        private boolean sortable;

        private boolean summary;

        public Builder() {
            this.widthRatio = 1;
        }

        public Builder label(String label) {
            this.label = label;
            return this;
        }

        public Builder fieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        public Builder renderer(String renderer) {
            this.renderer = renderer;
            return this;
        }

        public Builder editor(String editor) {
            this.editor = editor;
            return this;
        }

        public Builder linkAct(String linkAct) {
            this.linkAct = linkAct;
            return this;
        }

        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder widthRatio(int widthRatio) {
            this.widthRatio = widthRatio > 0 ? widthRatio : 1;
            return this;
        }

        public Builder switchOnChange(boolean switchOnChange) {
            this.switchOnChange = switchOnChange;
            return this;
        }

        public Builder hiddenOnNull(boolean hiddenOnNull) {
            this.hiddenOnNull = hiddenOnNull;
            return this;
        }

        public Builder hidden(boolean hidden) {
            this.hidden = hidden;
            return this;
        }

        public Builder disabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }

        public Builder editable(boolean editable) {
            this.editable = editable;
            return this;
        }

        public Builder sortable(boolean sortable) {
            this.sortable = sortable;
            return this;
        }

        public Builder summary(boolean summary) {
            this.summary = summary;
            return this;
        }

        public Builder order(OrderType order) {
            this.order = order;
            return this;
        }

        public TableColumnDef build() throws UnifyException {
            return new TableColumnDef(label, fieldName, renderer, editor, linkAct, symbol, order, widthRatio,
                    switchOnChange, hiddenOnNull, hidden, disabled, editable, sortable, summary);
        }
    }

    @Override
    public String toString() {
        return StringUtils.toXmlString(this);
    }

}

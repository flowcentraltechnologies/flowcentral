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

package com.flowcentraltech.flowcentral.studio.web.widgets;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.tcdng.unify.core.UnifyException;

/**
 * Table editor object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class TableEditor {

    private EntityDef entityDef;

    private Design design;

    private String editColumnId;

    private boolean readOnly;

    private TableEditor(EntityDef entityDef, Design design) {
        this.entityDef = entityDef;
        this.design = design;
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public String getEditColumnId() {
        return editColumnId;
    }

    public void setEditColumnId(String editColumnId) {
        this.editColumnId = editColumnId;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Design getDesign() {
        return design;
    }

    public void setDesign(Design design) {
        this.design = design;
    }

    public static Builder newBuilder(AppletUtilities au, EntityDef entityDef) {
        return new Builder(au, entityDef);
    }

    public static class Builder {

        private EntityDef entityDef;

        private List<TableColumn> columnList;

        private final AppletUtilities au;

        public Builder(AppletUtilities au, EntityDef entityDef) {
            this.au = au;
            this.entityDef = entityDef;
            this.columnList = new ArrayList<TableColumn>();
        }

        public Builder addColumn(String fieldName, String renderWidget, String label, String linkAct, String symbol,
                String order, int widthRatio, boolean switchOnChange, boolean hiddenOnNull, boolean hidden,
                boolean disabled, boolean editable, boolean sortable, boolean summary) throws UnifyException {
            if (entityDef.isWithFieldDef(fieldName)) {
                columnList.add(new TableColumn(
                        au.resolveSessionMessage(entityDef.getFieldDef(fieldName).getFieldLabel()), fieldName,
                        renderWidget, au.resolveSessionMessage(label), linkAct, symbol, order, widthRatio,
                        switchOnChange, hiddenOnNull, hidden, disabled, editable, sortable, summary));
            }

            return this;
        }

        public TableEditor build() {
            return new TableEditor(entityDef, new Design(columnList));
        }
    }

    public static class Design {

        private List<TableColumn> columns;

        public Design(List<TableColumn> columns) {
            this.columns = columns;
        }

        public Design() {

        }

        public List<TableColumn> getColumns() {
            return columns;
        }

        public void setColumns(List<TableColumn> columns) {
            this.columns = columns;
        }
    }

    public static class TableColumn {

        private String fldLabel;

        private String fldNm;

        private String widget;

        private String label;

        private String link;

        private String symbol;

        private String order;

        private int width;

        private boolean switchOnChange;

        private boolean hiddenOnNull;

        private boolean hidden;

        private boolean disabled;

        private boolean editable;

        private boolean sort;

        private boolean summary;

        public TableColumn(String fldLabel, String fldNm, String widget, String label, String link, String symbol,
                String order, int width, boolean switchOnChange, boolean hiddenOnNull, boolean hidden, boolean disabled,
                boolean editable, boolean sort, boolean summary) {
            this.fldLabel = fldLabel;
            this.fldNm = fldNm;
            this.widget = widget;
            this.label = label;
            this.link = link;
            this.symbol = symbol;
            this.order = order;
            this.width = width;
            this.switchOnChange = switchOnChange;
            this.hiddenOnNull = hiddenOnNull;
            this.hidden = hidden;
            this.disabled = disabled;
            this.editable = editable;
            this.sort = sort;
            this.summary = summary;
        }

        public TableColumn() {

        }

        public String getFldLabel() {
            return fldLabel;
        }

        public void setFldLabel(String fldLabel) {
            this.fldLabel = fldLabel;
        }

        public String getFldNm() {
            return fldNm;
        }

        public void setFldNm(String fldNm) {
            this.fldNm = fldNm;
        }

        public String getWidget() {
            return widget;
        }

        public void setWidget(String widget) {
            this.widget = widget;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public boolean isSummary() {
            return summary;
        }

        public void setSummary(boolean summary) {
            this.summary = summary;
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

        public boolean isSort() {
            return sort;
        }

        public void setSort(boolean sort) {
            this.sort = sort;
        }
    }
}

/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.application.data.EntityDef;

/**
 * Report editor object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ReportEditor {

    private EntityDef entityDef;

    private Design design;

    private String editColumnId;

    private boolean readOnly;

    private ReportEditor(EntityDef entityDef, Design design) {
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

    public static Builder newBuilder(EntityDef entityDef) {
        return new Builder(entityDef);
    }

    public static class Builder {

        private EntityDef entityDef;

        private List<ReportColumn> columnList;

        public Builder(EntityDef entityDef) {
            this.entityDef = entityDef;
            this.columnList = new ArrayList<ReportColumn>();
        }

        public Builder addColumn(String fieldName, String widget, String order, String horizAlign, String vertAlign,
                String description, String formatter, int width, boolean bold, boolean group,
                boolean groupOnNewPage, boolean sum) {
            if (entityDef.isWithFieldDef(fieldName)) {
                columnList.add(new ReportColumn(entityDef.getFieldDef(fieldName).getFieldLabel(), fieldName, widget,
                        order, horizAlign, vertAlign, description, formatter, width, bold, group, groupOnNewPage,
                        sum));
            }

            return this;
        }

        public ReportEditor build() {
            return new ReportEditor(entityDef, new Design(columnList));
        }
    }

    public static class Design {

        private List<ReportColumn> columns;

        public Design(List<ReportColumn> columns) {
            this.columns = columns;
        }

        public Design() {

        }

        public List<ReportColumn> getColumns() {
            return columns;
        }

        public void setColumns(List<ReportColumn> columns) {
            this.columns = columns;
        }
    }

    public static class ReportColumn {

        private String fldLabel;

        private String fldNm;

        private String widget;

        private String order;

        private String horizAlign;

        private String vertAlign;

        private String description;

        private String formatter;

        private int width;

        private boolean bold;

        private boolean group;

        private boolean groupOnNewPage;

        private boolean sum;

        public ReportColumn(String fldLabel, String fldNm, String widget, String order, String horizAlign,
                String vertAlign, String description, String formatter, int width, boolean bold,
                boolean group, boolean groupOnNewPage, boolean sum) {
            this.fldLabel = fldLabel;
            this.fldNm = fldNm;
            this.widget = widget;
            this.order = order;
            this.horizAlign = horizAlign;
            this.vertAlign = vertAlign;
            this.description = description;
            this.formatter = formatter;
            this.width = width;
            this.bold = bold;
            this.group = group;
            this.groupOnNewPage = groupOnNewPage;
            this.sum = sum;
        }

        public ReportColumn() {

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

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public String getHorizAlign() {
            return horizAlign;
        }

        public void setHorizAlign(String horizAlign) {
            this.horizAlign = horizAlign;
        }

        public String getVertAlign() {
            return vertAlign;
        }

        public void setVertAlign(String vertAlign) {
            this.vertAlign = vertAlign;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFormatter() {
            return formatter;
        }

        public void setFormatter(String formatter) {
            this.formatter = formatter;
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
}

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
package com.flowcentraltech.flowcentral.application.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.DynamicEnumProvider;
import com.flowcentraltech.flowcentral.application.constants.LinkActConstants;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.util.WidgetCalculationUtils;
import com.flowcentraltech.flowcentral.common.data.DefaultReportColumn;
import com.flowcentraltech.flowcentral.configuration.constants.EntityBaseType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.criterion.Select;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.data.BadgeInfo;
import com.tcdng.unify.web.ui.widget.data.ButtonInfo;
import com.tcdng.unify.web.ui.widget.data.ColorLegendInfo;

/**
 * Table definition;
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class TableDef extends BaseApplicationEntityDef {

    private EntityDef entityDef;

    private List<TableLoadingDef> loadingDefList;

    private List<TableColumnDef> columnDefList;

    private List<TableColumnDef> visibleColumnDefList;

    private List<DefaultReportColumn> defaultReportColumnList;

    private LabelSuggestionDef labelSuggestionDef;

    private List<ButtonInfo> actionBtnInfos;

    private Select select;

    private String label;

    private String detailsPanelName;

    private String loadingSearchInput;

    private String loadingFilterGen;

    private int sortHistory;

    private int itemsPerPage;

    private int summaryTitleColumns;

    private boolean serialNo;

    private boolean sortable;

    private boolean showLabelHeader;

    private boolean headerToUpperCase;

    private boolean headerCenterAlign;

    private boolean limitSelectToColumns;

    private boolean basicSearch;

    private boolean totalSummary;

    private boolean headerless;

    private boolean multiSelect;

    private boolean nonConforming;

    private boolean fixedRows;

    private Map<String, TableFilterDef> filterDefMap;

    private List<TableFilterDef> rowColorFilterList;

    private ColorLegendInfo colorLegendInfo;

    private Set<String> summaryFields;

    private TableDef(EntityDef entityDef, List<TableLoadingDef> loadingDefList, List<TableColumnDef> columnDefList,
            List<TableColumnDef> visibleColumnDefList, List<ButtonInfo> actionBtnInfos,
            Map<String, TableFilterDef> filterDefMap, String label, String detailsPanelName, String loadingSearchInput,
            String loadingFilterGen, int sortHistory, int itemsPerPage, int summaryTitleColumns, boolean serialNo,
            boolean sortable, boolean showLabelHeader, boolean headerToUpperCase, boolean headerCenterAlign,
            boolean basicSearch, boolean totalSummary, boolean headerless, boolean multiSelect, boolean nonConforming,
            boolean fixedRows, boolean limitSelectToColumns, ApplicationEntityNameParts nameParts, String description,
            Long id, long version) {
        super(nameParts, description, id, version);
        this.entityDef = entityDef;
        this.loadingDefList = loadingDefList;
        this.columnDefList = columnDefList;
        this.visibleColumnDefList = visibleColumnDefList;
        this.actionBtnInfos = actionBtnInfos;
        this.label = label;
        this.detailsPanelName = detailsPanelName;
        this.loadingSearchInput = loadingSearchInput;
        this.loadingFilterGen = loadingFilterGen;
        this.sortHistory = sortHistory;
        this.itemsPerPage = itemsPerPage;
        this.summaryTitleColumns = summaryTitleColumns;
        this.serialNo = serialNo;
        this.sortable = sortable;
        this.showLabelHeader = showLabelHeader;
        this.headerToUpperCase = headerToUpperCase;
        this.headerCenterAlign = headerCenterAlign;
        this.basicSearch = basicSearch;
        this.totalSummary = totalSummary;
        this.headerless = headerless;
        this.multiSelect = multiSelect;
        this.nonConforming = nonConforming;
        this.fixedRows = fixedRows;
        this.limitSelectToColumns = limitSelectToColumns;
        this.summaryFields = new HashSet<String>();

        if (!DataUtils.isBlank(loadingDefList)) {
            this.actionBtnInfos = InputWidgetUtils.getButtonInfos(loadingDefList);
        }

        List<TableFilterDef> rowColorFilterList = new ArrayList<TableFilterDef>();
        for (TableFilterDef filterDef : filterDefMap.values()) {
            if (filterDef.isWithRowColor()) {
                rowColorFilterList.add(filterDef);
            }
        }

        if (this.limitSelectToColumns) {
            this.select = new Select().add("id");
            if (!EntityBaseType.BASE_ENTITY.equals(entityDef.getBaseType())) {
                select.add("versionNo");
            }

            for (TableColumnDef tableColumnDef : columnDefList) {
                this.select.add(tableColumnDef.getFieldName());
            }
        }

        this.defaultReportColumnList = new ArrayList<DefaultReportColumn>();
        final int len = visibleColumnDefList.size();
        for (int i = 0; i < len; i++) {
            TableColumnDef tableColumnDef = visibleColumnDefList.get(i);
            this.defaultReportColumnList.add(new DefaultReportColumn(tableColumnDef.getFieldName(), getFieldLabel(i)));
            if (tableColumnDef.isSummary()) {
                this.summaryFields.add(tableColumnDef.getFieldName());
            }
        }

        this.rowColorFilterList = DataUtils.unmodifiableList(rowColorFilterList);
        this.defaultReportColumnList = DataUtils.unmodifiableList(this.defaultReportColumnList);
        this.summaryFields = DataUtils.unmodifiableSet(this.summaryFields);
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public List<TableLoadingDef> getLoadingDefList() {
        return loadingDefList;
    }

    public boolean isWithLoading() {
        return !loadingDefList.isEmpty();
    }

    public TableLoadingDef getTableLoadingDef(int index) {
        return loadingDefList.get(index);
    }

    public int getLoadingDefCount() {
        return loadingDefList.size();
    }

    public List<TableFilterDef> getRowColorFilterList() {
        return rowColorFilterList;
    }

    public List<ButtonInfo> getActionBtnInfos() {
        return actionBtnInfos;
    }

    public Set<String> getSummaryFields() {
        return summaryFields;
    }

    public boolean isSummaryField(String fieldName) {
        return summaryFields.contains(fieldName);
    }

    public boolean isWithSummaryFields() {
        return !summaryFields.isEmpty();
    }

    public boolean isRowColorFilters() {
        return !rowColorFilterList.isEmpty();
    }

    public ColorLegendInfo getColorLegendInfo() {
        if (colorLegendInfo == null && isRowColorFilters()) {
            synchronized (this) {
                if (colorLegendInfo == null) {
                    ColorLegendInfo.Builder clib = ColorLegendInfo.newBuilder();
                    for (TableFilterDef tableFilterDef : rowColorFilterList) {
                        String label = tableFilterDef.isWithLegendLabel() ? tableFilterDef.getLegendLabel()
                                : tableFilterDef.getListDescription();
                        clib.addItem(tableFilterDef.getRowColor(), label);
                    }

                    colorLegendInfo = clib.build();
                }
            }
        }

        return colorLegendInfo;
    }

    public TableFilterDef getFilterDef(String name) {
        TableFilterDef filterDef = filterDefMap.get(name);
        if (filterDef == null) {
            throw new RuntimeException(
                    "Filter with name [" + name + "] is unknown for applet definition [" + getName() + "].");
        }

        return filterDef;
    }

    public String getFieldLabel(int index) {
        TableColumnDef columnDef = getVisibleColumnDef(index);
        if (columnDef.isWithLabel()) {
            return columnDef.getLabel();
        }

        EntityFieldDef entityFieldDef = entityDef.getFieldDef(columnDef.getFieldName());
        return entityFieldDef.isWithInputLabel() ? entityFieldDef.getInputLabel() : entityFieldDef.getFieldLabel();
    }

    public EntityFieldDef getFieldDef(String name) {
        return entityDef.getFieldDef(name);
    }

    public List<TableColumnDef> getColumnDefList() {
        return columnDefList;
    }

    public List<TableColumnDef> getVisibleColumnDefList() {
        return visibleColumnDefList;
    }

    public LabelSuggestionDef getLabelSuggestionDef() {
        if (labelSuggestionDef == null) {
            final int len = visibleColumnDefList.size();
            Map<String, String> labelByFieldNames = new HashMap<String, String>();
            for (int i = 0; i < len; i++) {
                labelByFieldNames.put(visibleColumnDefList.get(i).getFieldName(), getFieldLabel(i));
            }

            labelSuggestionDef = new LabelSuggestionDef(labelByFieldNames);
        }

        return labelSuggestionDef;
    }

    public List<DefaultReportColumn> getDefaultReportColumnList() {
        return defaultReportColumnList;
    }

    public Select getSelect() {
        return select;
    }

    public String getLabel() {
        return label;
    }

    public String getDetailsPanelName() {
        return detailsPanelName;
    }

    public boolean isWithDetailsPanelName() {
        return !StringUtils.isBlank(detailsPanelName);
    }

    public String getLoadingSearchInput() {
        return loadingSearchInput;
    }

    public boolean isWithLoadingSearchInput() {
        return !StringUtils.isBlank(loadingSearchInput);
    }

    public String getLoadingFilterGen() {
        return loadingFilterGen;
    }

    public boolean isWithLoadingFilterGen() {
        return !StringUtils.isBlank(loadingFilterGen);
    }

    public int getSortHistory() {
        return sortHistory;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public int getSummaryTitleColumns() {
        return summaryTitleColumns;
    }

    public int getColumnCount() {
        return columnDefList.size();
    }

    public int getVisibleColumnCount() {
        return visibleColumnDefList.size();
    }

    public boolean isSerialNo() {
        return serialNo;
    }

    public boolean isSortable() {
        return sortable;
    }

    public boolean isShowLabelHeader() {
        return showLabelHeader;
    }

    public boolean isHeaderToUpperCase() {
        return headerToUpperCase;
    }

    public boolean isHeaderCenterAlign() {
        return headerCenterAlign;
    }

    public boolean isBasicSearch() {
        return basicSearch;
    }

    public boolean isTotalSummary() {
        return totalSummary;
    }

    public boolean isHeaderless() {
        return headerless;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public boolean isNonConforming() {
        return nonConforming;
    }

    public boolean isFixedRows() {
        return fixedRows;
    }

    public boolean isLimitSelectToColumns() {
        return limitSelectToColumns;
    }

    public boolean isUseSortHistory() {
        return sortHistory > 0;
    }

    public boolean isPagination() {
        return itemsPerPage > 0;
    }

    public TableColumnDef getVisibleColumnDef(int index) {
        return visibleColumnDefList.get(index);
    }

    public TableColumnDef getVisibleColumnDef(String fieldName) {
        for (TableColumnDef tableColumnDef : visibleColumnDefList) {
            if (tableColumnDef.getFieldName().equals(fieldName)) {
                return tableColumnDef;
            }
        }

        throw new RuntimeException(
                "Field with name [" + fieldName + "] is unknown for table definition [" + getLongName() + "].");
    }

    public int getVisibleColumnIndex(String fieldName) {
        int index = 0;
        for (TableColumnDef tableColumnDef : visibleColumnDefList) {
            if (tableColumnDef.getFieldName().equals(fieldName)) {
                return index;
            }

            index++;
        }

        throw new RuntimeException(
                "Field with name [" + fieldName + "] is unknown for table definition [" + getLongName() + "].");
    }

    public static Builder newBuilder(EntityDef entityDef) {
        return new Builder(entityDef);
    }

    public static Builder newBuilder(EntityDef entityDef, String label, boolean serialNo, boolean sortable,
            String longName, String description, Long id, long version) {
        return new Builder(entityDef, label, serialNo, sortable, longName, description, id, version);
    }

    public static class Builder {

        private EntityDef entityDef;

        private Map<String, TableFilterDef> filterDefMap;

        private List<TableLoadingDef> loadingDefList;

        private List<TableColumnDef> columnDefList;

        private List<TableColumnDef> visibleColumnDefList;

        private List<ButtonInfo> actionBtnInfos;

        private List<Integer> widthRatios;

        private String label;

        private String detailsPanelName;

        private String loadingSearchInput;

        private String loadingFilterGen;

        private int sortHistory;

        private int itemsPerPage;

        private int summaryTitleColumns;

        private boolean classLink;

        private boolean serialNo;

        private boolean sortable;

        private boolean showLabelHeader;

        private boolean headerToUpperCase;

        private boolean headerCenterAlign;

        private boolean basicSearch;

        private boolean totalSummary;

        private boolean headerless;

        private boolean multiSelect;

        private boolean nonConforming;

        private boolean fixedRows;

        private boolean limitSelectToColumns;

        private String longName;

        private String description;

        private Long id;

        private long version;

        public Builder(EntityDef entityDef, String label, boolean serialNo, boolean sortable, String longName,
                String description, Long id, long version) {
            this.entityDef = entityDef;
            this.label = label;
            this.serialNo = serialNo;
            this.sortable = sortable;
            this.longName = longName;
            this.id = id;
            this.description = description;
            this.version = version;
            this.widthRatios = new ArrayList<Integer>();
            this.filterDefMap = new LinkedHashMap<String, TableFilterDef>();
            this.loadingDefList = new ArrayList<TableLoadingDef>();
            this.columnDefList = new ArrayList<TableColumnDef>();
            this.visibleColumnDefList = new ArrayList<TableColumnDef>();
            this.actionBtnInfos = new ArrayList<ButtonInfo>();
        }

        public Builder(EntityDef entityDef) {
            this.entityDef = entityDef;
            this.widthRatios = new ArrayList<Integer>();
            this.filterDefMap = new LinkedHashMap<String, TableFilterDef>();
            this.loadingDefList = new ArrayList<TableLoadingDef>();
            this.columnDefList = new ArrayList<TableColumnDef>();
            this.visibleColumnDefList = new ArrayList<TableColumnDef>();
            this.actionBtnInfos = new ArrayList<ButtonInfo>();
        }

        public Builder label(String label) {
            this.label = label;
            return this;
        }

        public Builder detailsPanelName(String detailsPanelName) {
            this.detailsPanelName = detailsPanelName;
            return this;
        }

        public Builder loadingSearchInput(String loadingSearchInput) {
            this.loadingSearchInput = loadingSearchInput;
            return this;
        }

        public Builder loadingFilterGen(String loadingFilterGen) {
            this.loadingFilterGen = loadingFilterGen;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder longName(String longName) {
            this.longName = longName;
            return this;
        }

        public Builder classicLink(boolean classLink) {
            this.classLink = classLink;
            return this;
        }

        public Builder serialNo(boolean serialNo) {
            this.serialNo = serialNo;
            return this;
        }

        public Builder sortable(boolean sortable) {
            this.sortable = sortable;
            return this;
        }

        public Builder sortHistory(int sortHistory) {
            this.sortHistory = sortHistory;
            return this;
        }

        public Builder itemsPerPage(int itemsPerPage) {
            this.itemsPerPage = itemsPerPage;
            return this;
        }

        public Builder summaryTitleColumns(int summaryTitleColumns) {
            this.summaryTitleColumns = summaryTitleColumns;
            return this;
        }

        public Builder showLabelHeader(boolean showLabelHeader) {
            this.showLabelHeader = showLabelHeader;
            return this;
        }

        public Builder headerToUpperCase(boolean headerToUpperCase) {
            this.headerToUpperCase = headerToUpperCase;
            return this;
        }

        public Builder headerCenterAlign(boolean headerCenterAlign) {
            this.headerCenterAlign = headerCenterAlign;
            return this;
        }

        public Builder basicSearch(boolean basicSearch) {
            this.basicSearch = basicSearch;
            return this;
        }

        public Builder totalSummary(boolean totalSummary) {
            this.totalSummary = totalSummary;
            return this;
        }

        public Builder headerless(boolean headerless) {
            this.headerless = headerless;
            return this;
        }

        public Builder multiSelect(boolean multiSelect) {
            this.multiSelect = multiSelect;
            return this;
        }

        public Builder nonConforming(boolean nonConforming) {
            this.nonConforming = nonConforming;
            return this;
        }

        public Builder fixedRows(boolean fixedRows) {
            this.fixedRows = fixedRows;
            return this;
        }

        public Builder limitSelectToColumns(boolean limitSelectToColumns) {
            this.limitSelectToColumns = limitSelectToColumns;
            return this;
        }

        public Builder addTableAction(String actionPolicy, String actionLabel) {
            this.actionBtnInfos.add(new ButtonInfo(actionPolicy, actionLabel));
            return this;
        }

        public Builder addColumnDef(String fieldName, String renderer, int widthRatio, boolean sortable)
                throws UnifyException {
            return addColumnDef(fieldName, renderer, null, widthRatio, false, false, false, false, true, sortable,
                    false);
        }

        public Builder addColumnDef(String fieldName, String renderer, OrderType order, int widthRatio,
                boolean switchOnChange, boolean hiddenOnNull, boolean hidden, boolean disabled, boolean editable,
                boolean sortable, boolean summary) throws UnifyException {
            return addColumnDef(null, fieldName, renderer, null, order, widthRatio, switchOnChange, hiddenOnNull,
                    hidden, disabled, editable, sortable, summary);
        }

        public Builder addColumnDef(String label, String fieldName, String renderer, int widthRatio, boolean sortable)
                throws UnifyException {
            return addColumnDef(label, fieldName, renderer, null, null, widthRatio, false, false, false, false, true,
                    sortable, false);
        }

        public Builder addColumnDef(String label, String fieldName, String renderer, String editor, OrderType order,
                int widthRatio, boolean switchOnChange, boolean hiddenOnNull, boolean hidden, boolean disabled,
                boolean editable, boolean sortable, boolean summary) throws UnifyException {
            return addColumnDef(label, fieldName, renderer, editor, null, null, order, widthRatio, switchOnChange,
                    hiddenOnNull, hidden, disabled, editable, sortable, summary);
        }

        public Builder addColumnDef(String label, String fieldName, String renderer, String editor, String linkAct,
                String symbol, OrderType order, int widthRatio, boolean switchOnChange, boolean hiddenOnNull,
                boolean hidden, boolean disabled, boolean editable, boolean sortable, boolean summary)
                throws UnifyException {
            TableColumnDef tableColumnDef = TableColumnDef.newBuilder().label(label).fieldName(fieldName)
                    .renderer(renderer).editor(editor).linkAct(linkAct).symbol(symbol).order(order)
                    .widthRatio(widthRatio).switchOnChange(switchOnChange).hiddenOnNull(hiddenOnNull).hidden(hidden)
                    .disabled(disabled).editable(editable).sortable(sortable).summary(summary).build();
            return addColumnDef(tableColumnDef);
        }

        public Builder addColumnDef(TableColumnDef tableColumnDef) {
            if (tableColumnDef.isHidden()) {
                columnDefList.add(tableColumnDef);
            } else {
                widthRatios.add(tableColumnDef.getWidthRatio());
                visibleColumnDefList.add(tableColumnDef);
            }

            return this;
        }

        public Builder addTableLoadingDef(TableLoadingDef tableLoadingDef) {
            loadingDefList.add(tableLoadingDef);
            return this;
        }

        public Builder addFilterDef(TableFilterDef filterDef) {
            if (filterDef != null) {
                if (filterDefMap.containsKey(filterDef.getName())) {
                    throw new RuntimeException(
                            "Filter with name [" + filterDef.getName() + "] already exists in this definition.");
                }

                filterDefMap.put(filterDef.getName(), filterDef);
            }

            return this;
        }

        public TableDef build(DynamicEnumProvider dynamicEnumProvider) throws UnifyException {
            List<TableColumnDef> _visibleColumnDefList = new ArrayList<TableColumnDef>();
            int usedPercent = 0;
            final int len = visibleColumnDefList.size();
            final int[] _widthRatios = DataUtils.convert(int[].class, widthRatios);
            final int[] widths = WidgetCalculationUtils.shareAsEqualAsPossible(100, _widthRatios);
            for (int i = 0; i < len; i++) {
                TableColumnDef tempColumnDef = visibleColumnDefList.get(i);
                TableColumnDef tableColumnDef = null;
                BadgeInfo badgeInfo = null;
                String fieldName = tempColumnDef.getFieldName();
                String renderer = tempColumnDef.getRenderer();
                String editor = !StringUtils.isBlank(tempColumnDef.getEditor())
                        ? tempColumnDef.getEditor() + " binding:" + fieldName
                        : null;
                String linkAct = tempColumnDef.getLinkAct();
                String symbol = tempColumnDef.getSymbol();
                final boolean toggle = LinkActConstants.TOGGLE_ACTION.equals(linkAct);
                if (tempColumnDef.isWithLinkAct()) {
                    String formatter = "";
                    int fromIndex = renderer.indexOf("formatter");
                    if (fromIndex > 0) {
                        int toIndex = renderer.indexOf('}', fromIndex);
                        if (toIndex > 0) {
                            formatter = renderer.substring(fromIndex - 1, toIndex + 1);
                        }
                    }

                    EntityFieldDef entityFieldDef = entityDef.getFieldDef(fieldName);
                    entityFieldDef = entityFieldDef.isWithResolvedTypeFieldDef()
                            ? entityFieldDef.getResolvedTypeFieldDef()
                            : entityFieldDef;
                    if (LinkActConstants.BUTTON_ACTION.equals(linkAct)) {
                        final String symbolAttr = tempColumnDef.isWithSymbol() ? " symbol:$s{" + symbol + "}" : "";
                        renderer = "!ui-button" + symbolAttr + "  binding:" + fieldName
                                + " alwaysValueIndex:true copyEventHandlers:true eventHandler:$d{!ui-event event:onclick action:$c{"
                                + linkAct + "}}";
                    } else {
                        if (entityFieldDef.isEnumDynamic()) {
                            badgeInfo = dynamicEnumProvider.getEnumerationDef(entityFieldDef.getReferences())
                                    .getBadgeInfo();
                            renderer = "!ui-badge badgeInfoBinding:$s{__badgeInfo} alwaysValueIndex:true binding:$s{"
                                    + fieldName + "}";
                        } else {
                            String styleClass = classLink
                                    ? (entityFieldDef.getDataType().isNumber()
                                            ? " styleClass:$e{link-right ui-link-classic}"
                                            : " styleClass:$e{ui-link-classic}")
                                    : (entityFieldDef.getDataType().isNumber() ? " styleClass:$e{link-right}" : "");
                            renderer = "!ui-link debounce:true preferredCaptionBinding:" + fieldName + formatter
                                    + " binding:" + fieldName + styleClass
                                    + " alwaysValueIndex:true copyEventHandlers:true eventHandler:$d{!ui-event event:onclick action:$c{"
                                    + linkAct + "}}";
                        }
                    }
                } else {
                    renderer = renderer + " binding:" + fieldName;
                }

                tableColumnDef = new TableColumnDef(badgeInfo, tempColumnDef.getLabel(), fieldName,
                        "width:" + widths[i] + "%;", renderer, editor, tempColumnDef.getRenderer(),
                        tempColumnDef.getEditor(), linkAct, symbol, tempColumnDef.getOrder(),
                        tempColumnDef.getWidthRatio(), (100 - usedPercent), tempColumnDef.isSwitchOnChange(),
                        tempColumnDef.isHiddenOnNull(), tempColumnDef.isHidden(), tempColumnDef.isDisabled(),
                        tempColumnDef.isEditable(), tempColumnDef.isSortable(), tempColumnDef.isSummary(), toggle);

                _visibleColumnDefList.add(tableColumnDef);
                columnDefList.add(tableColumnDef);
            }

            ApplicationEntityNameParts nameParts = ApplicationNameUtils.getApplicationEntityNameParts(longName);
            return new TableDef(entityDef, DataUtils.unmodifiableList(loadingDefList),
                    DataUtils.unmodifiableList(columnDefList), DataUtils.unmodifiableList(_visibleColumnDefList),
                    DataUtils.unmodifiableList(actionBtnInfos), DataUtils.unmodifiableMap(filterDefMap), label,
                    detailsPanelName, loadingSearchInput, loadingFilterGen, sortHistory, itemsPerPage,
                    summaryTitleColumns, serialNo, sortable, showLabelHeader, headerToUpperCase, headerCenterAlign,
                    basicSearch, totalSummary, headerless, multiSelect, nonConforming, fixedRows, limitSelectToColumns,
                    nameParts, description, id, version);
        }
    }

    @Override
    public String toString() {
        return StringUtils.toXmlString(this);
    }

}

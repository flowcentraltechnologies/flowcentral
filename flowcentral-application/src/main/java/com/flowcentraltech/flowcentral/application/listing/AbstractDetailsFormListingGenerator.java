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
package com.flowcentraltech.flowcentral.application.listing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.constants.ListingColorType;
import com.flowcentraltech.flowcentral.application.data.ListingProperties;
import com.flowcentraltech.flowcentral.application.data.TableColumnDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.web.data.DetailsCase;
import com.flowcentraltech.flowcentral.application.web.data.DetailsFormListing;
import com.flowcentraltech.flowcentral.application.web.data.Summary;
import com.flowcentraltech.flowcentral.common.data.FontSetting;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.data.Formats;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Convenient abstract base class for details form listing generators.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractDetailsFormListingGenerator extends AbstractFormListingGenerator
        implements DetailsFormListingGenerator {

    @Override
    protected Set<ListingColorType> getPausePrintColors() throws UnifyException {
        return Collections.emptySet();
    }

    @Override
    protected String additionalStyleClass() {
        return "fc-detailsformlisting";
    }

    @Override
    protected FontSetting getFontSetting(ValueStoreReader reader) throws UnifyException {
        return FontSetting.DETAULT_FONT_SETTING;
    }

    @Override
    protected final void doWriteBody(ValueStoreReader reader, ListingProperties listingProperties,
            ListingGeneratorWriter writer) throws UnifyException {
        doGenerate(reader, listingProperties, writer);
    }

    protected final void doGenerate(ValueStoreReader reader,  ListingProperties listingProperties,
            ListingGeneratorWriter writer) throws UnifyException {
        final DetailsFormListing detailsFormListing = writer.getFormListing(DetailsFormListing.class);
        final List<DetailsCase> detailsCaseList = detailsFormListing.getCaseList();
        final int columns = detailsFormListing.getColumns();
        final int[] sectionColumnWidth = new int[columns];
        Arrays.fill(sectionColumnWidth, 100 / columns);

        final int numberOfCases = detailsCaseList.size();
        final int rows = (numberOfCases / columns) + (numberOfCases % columns > 0 ? 1 : 0);

        int caseIndex = 0;
        for (int i = 0; i < rows; i++) {
            writer.beginSection(sectionColumnWidth, 100, HAlignType.CENTER, false, ListingCell.BORDER_NONE);
            int j = 0;
            for (; j < columns && caseIndex < numberOfCases; j++, caseIndex++) {
                doGenerate(detailsCaseList.get(caseIndex), listingProperties, writer);
            }

            while (j < columns) {
                writer.beginClassicTable(new ListingColumn[0]);
                writer.endTable();
                j++;
            }
            writer.endSectionWithSpacing();
        }
    }

     private void doGenerate(DetailsCase detailsCase, ListingProperties listingProperties, ListingGeneratorWriter writer)
            throws UnifyException {
        final TableDef tableDef = application().getTableDef(detailsCase.getTableName());
        final boolean isSerialNo = tableDef.isSerialNo();
        final boolean isHeaderToUpperCase = tableDef.isHeaderToUpperCase();
        final Formats.Instance formatsInstance = detailsCase.getFormatsInstance();
        final List<TableColumnDef> tableColumns = tableDef.getVisibleColumnDefList();
        final boolean summaries = detailsCase.isWithSummaries();
        final int summaryTitleColumns = tableDef.getSummaryTitleColumns() > 0 ? tableDef.getSummaryTitleColumns()
                : detailsCase.getSummaryTitleColumn() + 1;
        final int columns = tableColumns.size();
        final int columnWidth = 100 / columns;
        List<ListingColumn> summaryColumns = new ArrayList<ListingColumn>();
        List<ListingCell> summaryCells = new ArrayList<ListingCell>();
        List<ListingColumn> headerListingColumns = new ArrayList<ListingColumn>();
        List<ListingCell> headerRowCells = new ArrayList<ListingCell>();
        List<ListingColumn> listingColumns = new ArrayList<ListingColumn>();
        List<ListingCell> rowCells = new ArrayList<ListingCell>();
        if (isSerialNo) {
            headerListingColumns.add(new ListingColumn(HAlignType.CENTER, 42, ListingColumn.WidthType.PIXELS));
            headerRowCells.add(new ListingCell(ListingCellType.BOLD_TEXT, "S/N", ListingCell.BORDER_ALL));

            ListingColumn column = new ListingColumn(HAlignType.LEFT, 42, ListingColumn.WidthType.PIXELS);
            listingColumns.add(column);

            ListingCell cell = new ListingCell(ListingCellType.TEXT, "", ListingCell.BORDER_ALL);
            rowCells.add(cell);
        }

        if (summaries) {
            ListingColumn column = new ListingColumn(HAlignType.RIGHT,
                    isSerialNo ? summaryTitleColumns + 1 : summaryTitleColumns, ListingColumn.WidthType.COLUMNS);
            summaryColumns.add(column);

            ListingCell cell = new ListingCell(ListingCellType.TEXT, "", ListingCell.BORDER_ALL);
            summaryCells.add(cell);
        }

        for (int i = 0; i < columns; i++) {
            headerListingColumns.add(new ListingColumn(HAlignType.CENTER, columnWidth));
            headerRowCells.add(new ListingCell(ListingCellType.BOLD_TEXT,
                    isHeaderToUpperCase ? tableDef.getFieldLabel(i).toUpperCase() : tableDef.getFieldLabel(i),
                    ListingCell.BORDER_ALL));

            final EntityFieldDataType dataType = tableDef.getEntityDef().getFieldDef(tableColumns.get(i).getFieldName())
                    .getResolvedDataType();
            ListingColumn column = new ListingColumn(dataType.alignType(), columnWidth);
            listingColumns.add(column);

            ListingCell cell = new ListingCell(ListingCellType.TEXT, "", ListingCell.BORDER_ALL);
            if (dataType.isDecimal()) {
                cell.setFormat(formatsInstance.getDecimalFormat());
            } else if (dataType.isDate()) {
                cell.setFormat(formatsInstance.getDateFormat());
            } else if (dataType.isTimestamp()) {
                cell.setFormat(formatsInstance.getTimestampFormat());
            }
            rowCells.add(cell);

            if (summaries && i >= summaryTitleColumns) {
                summaryColumns.add(column);
                summaryCells.add(cell);
            }
        }

        writer.beginClassicTable(headerListingColumns);
        // Header Label
        if (tableDef.isShowLabelHeader()) {
            ListingColumn column = new ListingColumn(HAlignType.CENTER, isSerialNo ? columns + 1 : columns,
                    ListingColumn.WidthType.COLUMNS);
            List<ListingColumn> headerColumns = Arrays.asList(column);

            ListingCell cell = new ListingCell(ListingCellType.BOLD_TEXT,
                    tableDef.isHeaderToUpperCase() ? tableDef.getLabel().toUpperCase() : tableDef.getLabel(),
                    ListingCell.BORDER_ALL);
            List<ListingCell> headerCells = Arrays.asList(cell);

            writer.replaceTableColumns(headerColumns);
            writer.writeRow(headerCells);
        }

        // Pre-summaries
        if (detailsCase.isWithPreSummaries()) {
            writer.replaceTableColumns(summaryColumns);
            for (Summary summary : detailsCase.getPreSummaries()) {
                summaryCells.get(0).setContent(summary.getLabel());
                for (int i = summaryTitleColumns, j = 1; i < columns; i++, j++) {
                    summaryCells.get(j).setContent(summary.getContent(tableColumns.get(i).getFieldName()));
                }

                writer.writeRow(summaryCells);
            }
        }

        // Header
        writer.replaceTableColumns(headerListingColumns);
        writer.writeRow(headerRowCells);

        // Rows
        writer.replaceTableColumns(listingColumns);

        final ValueStore detailsValueStore = new BeanValueListStore(detailsCase.getContent());
        final int len = detailsValueStore.size();
        for (int k = 0; k < len;) {
            detailsValueStore.setDataIndex(k++);

            int j = 0;
            if (isSerialNo) {
                rowCells.get(j).setContent(String.valueOf(k));
                j++;
            }

            for (int i = 0; i < columns; i++, j++) {
                rowCells.get(j).setContent(detailsValueStore.retrieve(tableColumns.get(i).getFieldName()));
            }

            writer.writeRow(rowCells);
        }

        // Post-summaries
        if (detailsCase.isWithPostSummaries()) {
            writer.replaceTableColumns(summaryColumns);
            for (Summary summary : detailsCase.getPostSummaries()) {
                summaryCells.get(0).setContent(summary.getLabel());
                for (int i = summaryTitleColumns, j = 1; i < columns; i++, j++) {
                    summaryCells.get(j).setContent(summary.getContent(tableColumns.get(i).getFieldName()));
                }
                writer.writeRow(summaryCells);
            }
        }

        writer.endTable();
    }
}

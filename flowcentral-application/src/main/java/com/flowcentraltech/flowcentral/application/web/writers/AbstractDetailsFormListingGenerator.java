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
package com.flowcentraltech.flowcentral.application.web.writers;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.constants.ListingColorType;
import com.flowcentraltech.flowcentral.application.data.ListingProperties;
import com.flowcentraltech.flowcentral.application.data.ListingReportProperties;
import com.flowcentraltech.flowcentral.application.data.TableColumnDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.web.data.DetailsFormListing;
import com.flowcentraltech.flowcentral.application.web.data.Formats;
import com.flowcentraltech.flowcentral.application.web.data.Summary;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Convenient abstract base class for details form listing generators.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractDetailsFormListingGenerator extends AbstractFormListingGenerator
        implements DetailsFormListingGenerator {

    @Override
    public final int getOptionFlagsOverride(ValueStore formBeanValueStore) throws UnifyException {
        return getOptionFlagsOverride((DetailsFormListing) formBeanValueStore.getValueObject());
    }

    @Override
    protected Set<ListingColorType> getPausePrintColors() throws UnifyException {
        return Collections.emptySet();
    }

    @Override
    protected String additionalStyleClass() {
        return "fc-detailsformlisting";
    }

    @Override
    protected final void doGenerate(ValueStore formBeanValueStore, ListingProperties listingProperties,
            ListingGeneratorWriter writer) throws UnifyException {
        doGenerate((DetailsFormListing) formBeanValueStore.getValueObject(), listingProperties, writer);
    }

    @Override
    protected final void generateReportHeader(ValueStore formBeanValueStore, ListingReportProperties properties,
            ListingGeneratorWriter writer) throws UnifyException {
        generateReportHeader((DetailsFormListing) formBeanValueStore.getValueObject(), properties, writer);
    }

    @Override
    protected final void generateReportAddendum(ValueStore formBeanValueStore, ListingReportProperties properties,
            ListingGeneratorWriter writer) throws UnifyException {
        generateReportAddendum((DetailsFormListing) formBeanValueStore.getValueObject(), properties, writer);
    }

    @Override
    protected final void generateReportFooter(ValueStore formBeanValueStore, ListingReportProperties properties,
            ListingGeneratorWriter writer) throws UnifyException {
        generateReportFooter((DetailsFormListing) formBeanValueStore.getValueObject(), properties, writer);
    }

    protected void doGenerate(DetailsFormListing detailsFormListing, ListingProperties listingProperties,
            ListingGeneratorWriter writer) throws UnifyException {
        final TableDef tableDef = detailsFormListing.getTableDef();
        final boolean isSerialNo = tableDef.isSerialNo();
        final boolean isHeaderToUpperCase = tableDef.isHeaderToUpperCase();
        final Formats formats = detailsFormListing.getFormats();
        final DecimalFormat amountFormat = new DecimalFormat(formats.getDecimalFormat());
        final SimpleDateFormat dateFormat = new SimpleDateFormat(formats.getDateFormat());
        final SimpleDateFormat timestampFormat = new SimpleDateFormat(formats.getTimestampFormat());
        final List<TableColumnDef> tableColumns = tableDef.getVisibleColumnDefList();
        final boolean summaries = detailsFormListing.isWithSummaries();
        final int summaryTitleColumns = tableDef.getSummaryTitleColumns() > 0 ? tableDef.getSummaryTitleColumns()
                : detailsFormListing.getSummaryTitleColumn() + 1;
        final int columns = tableColumns.size();
        final int columnWidth = 100 / columns;
        writer.beginSection(1, 100, HAlignType.CENTER, false, ListingCell.BORDER_NONE);
        List<ListingColumn> summaryColumns = new ArrayList<ListingColumn>();
        List<ListingCell> summaryCells = new ArrayList<ListingCell>();
        List<ListingColumn> headerListingColumns = new ArrayList<ListingColumn>();
        List<ListingCell> headerRowCells = new ArrayList<ListingCell>();
        List<ListingColumn> listingColumns = new ArrayList<ListingColumn>();
        List<ListingCell> rowCells = new ArrayList<ListingCell>();
        if (isSerialNo) {
            headerListingColumns.add(new ListingColumn(HAlignType.CENTER, 56, ListingColumn.WidthType.PIXELS));
            headerRowCells.add(new ListingCell(ListingCellType.BOLD_TEXT, "S/N", ListingCell.BORDER_ALL));

            ListingColumn column = new ListingColumn(HAlignType.LEFT, 56, ListingColumn.WidthType.PIXELS);
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
                cell.setFormat(formats.getDecimalFormat(), amountFormat);
            } else if (dataType.isDate()) {
                cell.setFormat(formats.getDateFormat(), dateFormat);
            } else if (dataType.isTimestamp()) {
                cell.setFormat(formats.getTimestampFormat(), timestampFormat);
            }
            rowCells.add(cell);

            if (summaries && i >= summaryTitleColumns) {
                summaryColumns.add(column);
                summaryCells.add(cell);
            }
        }

        writer.beginClassicTable(headerListingColumns);
        // Pre-summaries
        if (detailsFormListing.isWithPreSummaries()) {
            writer.replaceTableColumns(summaryColumns);
            for (Summary summary : detailsFormListing.getPreSummaries()) {
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

        final ValueStore detailsValueStore = new BeanValueListStore(detailsFormListing.getDetails());
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
        if (detailsFormListing.isWithPostSummaries()) {
            writer.replaceTableColumns(summaryColumns);
            for (Summary summary : detailsFormListing.getPostSummaries()) {
                summaryCells.get(0).setContent(summary.getLabel());
                for (int i = summaryTitleColumns, j = 1; i < columns; i++, j++) {
                    summaryCells.get(j).setContent(summary.getContent(tableColumns.get(i).getFieldName()));
                }
                writer.writeRow(summaryCells);
            }
        }

        writer.endTable();
        writer.endSection();
    }

    protected abstract int getOptionFlagsOverride(DetailsFormListing detailsFormListing) throws UnifyException;

    protected abstract void generateReportHeader(DetailsFormListing detailsFormListing,
            ListingReportProperties properties, ListingGeneratorWriter writer) throws UnifyException;

    protected abstract void generateReportAddendum(DetailsFormListing detailsFormListing,
            ListingReportProperties properties, ListingGeneratorWriter writer) throws UnifyException;

    protected abstract void generateReportFooter(DetailsFormListing detailsFormListing,
            ListingReportProperties properties, ListingGeneratorWriter writer) throws UnifyException;
}

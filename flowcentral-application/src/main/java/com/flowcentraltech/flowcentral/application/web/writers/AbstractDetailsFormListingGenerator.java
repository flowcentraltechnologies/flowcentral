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
        final int columns = tableColumns.size();
        final int columnWidth = 100 / columns;
        writer.beginSection(1, 100, HAlignType.CENTER, false, ListingCell.BORDER_NONE);
        List<ListingColumn> listingColumns = new ArrayList<ListingColumn>();
        List<ListingCell> rowCells = new ArrayList<ListingCell>();
        if (isSerialNo) {
            listingColumns.add(new ListingColumn(HAlignType.CENTER, 56, true));
            rowCells.add(new ListingCell(ListingCellType.BOLD_TEXT, "S/N", ListingCell.BORDER_ALL)); // TODO Get from
                                                                                                     // messages
        }

        for (int i = 0; i < columns; i++) {
            listingColumns.add(new ListingColumn(HAlignType.CENTER, columnWidth));
            rowCells.add(new ListingCell(ListingCellType.BOLD_TEXT,
                    isHeaderToUpperCase ? tableDef.getFieldLabel(i).toUpperCase() : tableDef.getFieldLabel(i),
                    ListingCell.BORDER_ALL));
        }

        writer.beginClassicTable(listingColumns);
        // Header
        writer.writeRow(rowCells);

        // Rows
        int j = 0;
        if (isSerialNo) {
            listingColumns.get(j).setAlign(HAlignType.LEFT);
            rowCells.get(j).setType(ListingCellType.TEXT);
            j++;
        }

        for (int i = 0; i < columns; i++, j++) {
            final EntityFieldDataType dataType = tableDef.getEntityDef().getFieldDef(tableColumns.get(i).getFieldName())
                    .getResolvedDataType();
            listingColumns.get(j).setAlign(dataType.alignType());
            rowCells.get(j).setType(ListingCellType.TEXT);
            if (dataType.isDecimal()) {
                rowCells.get(j).setFormat(amountFormat);
            } else if (dataType.isDate()) {
                rowCells.get(j).setFormat(dateFormat);
            } else if (dataType.isTimestamp()) {
                rowCells.get(j).setFormat(timestampFormat);
            }
        }

        final ValueStore detailsValueStore = new BeanValueListStore(detailsFormListing.getDetails());
        final int len = detailsValueStore.size();
        for (int k = 0; k < len;) {
            detailsValueStore.setDataIndex(k++);

            j = 0;
            if (isSerialNo) {
                rowCells.get(j).setContent(String.valueOf(k));
                j++;
            }

            for (int i = 0; i < columns; i++, j++) {
                rowCells.get(j).setContent(detailsValueStore.retrieve(tableColumns.get(i).getFieldName()));
            }

            writer.writeRow(rowCells);
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

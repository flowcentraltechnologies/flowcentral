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
package com.flowcentraltech.flowcentral.application.listing;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.constants.ListingColorType;
import com.flowcentraltech.flowcentral.application.data.ListingProperties;
import com.flowcentraltech.flowcentral.application.data.ListingReportProperties;
import com.flowcentraltech.flowcentral.application.util.HtmlUtils;
import com.flowcentraltech.flowcentral.application.web.data.LetterFormListing;
import com.flowcentraltech.flowcentral.common.data.FormListingOptions;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.report.Report;

/**
 * Convenient abstract base class for letter form listing generators.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractLetterFormListingGenerator extends AbstractFormListingGenerator
        implements LetterFormListingGenerator {

    @Override
    public final int getOptionFlagsOverride(ValueStoreReader reader) throws UnifyException {
        return -1;
    }

    @Override
    public final Report generateExcelReport(ValueStoreReader reader, FormListingOptions listingOptions)
            throws UnifyException {
        throwUnsupportedOperationException();
        return null;
    }

    @Override
    protected final Set<ListingColorType> getPausePrintColors() throws UnifyException {
        return Collections.emptySet();
    }

    @Override
    protected String additionalStyleClass() {
        return "fc-letterformlisting";
    }

    @Override
    protected final void doWriteBody(ValueStoreReader reader, ListingProperties listingProperties,
            ListingGeneratorWriter writer) throws UnifyException {
        final LetterFormListing letterFormListing = (LetterFormListing) reader.getValueObject();
        ListingColumn[] columns = new ListingColumn[] { new ListingColumn(HAlignType.LEFT, 100) };
        final String body = getLetterBody(letterFormListing);
        final String fmtBody = HtmlUtils.formatHTML(body);
        final ListingCell cell = new ListingCell(ListingCellType.TEXT, fmtBody, ListingCell.BORDER_NONE);
        cell.setNoHtmlEscape(true);
        writer.beginSection(1, 100, HAlignType.LEFT, false, ListingCell.BORDER_NONE);
        writer.beginClassicTable(columns);
        writer.replaceTableColumns(Arrays.asList(columns));
        writer.writeRow(Arrays.asList(cell));
        writer.endTable();
        writer.endSection();
    }

    @Override
    protected final void writeReportHeader(ValueStoreReader reader, ListingReportProperties properties,
            ListingGeneratorWriter writer) throws UnifyException {
        writeLetterHeader((LetterFormListing) reader.getValueObject(), properties, writer);
    }

    @Override
    protected final void writeReportAddendum(ValueStoreReader reader, ListingReportProperties properties,
            ListingGeneratorWriter writer) throws UnifyException {
        writeLetterAddendum((LetterFormListing) reader.getValueObject(), properties, writer);
    }

    @Override
    protected final void writeReportFooter(ValueStoreReader reader, ListingReportProperties properties,
            ListingGeneratorWriter writer) throws UnifyException {
        writeLetterFooter((LetterFormListing) reader.getValueObject(), properties, writer);
    }

    protected abstract String getLetterBody(LetterFormListing letterFormListing) throws UnifyException;

    protected abstract void writeLetterHeader(LetterFormListing letterFormListing, ListingReportProperties properties,
            ListingGeneratorWriter writer) throws UnifyException;

    protected abstract void writeLetterAddendum(LetterFormListing letterFormListing, ListingReportProperties properties,
            ListingGeneratorWriter writer) throws UnifyException;

    protected abstract void writeLetterFooter(LetterFormListing letterFormListing, ListingReportProperties properties,
            ListingGeneratorWriter writer) throws UnifyException;
}

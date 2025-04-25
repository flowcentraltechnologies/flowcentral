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
package com.flowcentraltech.flowcentral.application.listing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.constants.ListingColorType;
import com.flowcentraltech.flowcentral.application.data.ListingProperties;
import com.flowcentraltech.flowcentral.application.data.ListingReportGeneratorProperties;
import com.flowcentraltech.flowcentral.application.data.ListingReportProperties;
import com.flowcentraltech.flowcentral.application.web.data.LetterFormListing;
import com.flowcentraltech.flowcentral.common.data.FontSetting;
import com.flowcentraltech.flowcentral.common.data.FormListingOptions;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.report.Report;
import com.tcdng.unify.core.report.ReportPageProperties;

/**
 * Convenient abstract base class for letter form listing generators.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
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

    protected ListingReportGeneratorProperties getReportProperties(ValueStoreReader reader,
            FormListingOptions listingOptions) throws UnifyException {
        final int numberOfParts = getNumberOfParts(reader);
        final List<ListingReportProperties> listingReportProperties = new ArrayList<ListingReportProperties>();
        for (int i = 0; i < numberOfParts; i++) {
            listingReportProperties.add(new ListingReportProperties("default_prop_" + i));
        }

        return new ListingReportGeneratorProperties(
                ReportPageProperties.newBuilder().resourceBaseUri(getSessionContext().getUriBase()).build(),
                listingReportProperties);
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
    protected FontSetting getFontSetting(ValueStoreReader reader) throws UnifyException {
        return new FontSetting(12);
    }

    @Override
    protected final void writeReportAddendum(ValueStoreReader reader, ListingReportProperties listingProperties,
            ListingGeneratorWriter writer) throws UnifyException {
        writeLetterAddendum(reader, writer.getFormListing(LetterFormListing.class), listingProperties, writer);

    }

    @Override
    protected final void writeReportFooter(ValueStoreReader reader, ListingReportProperties listingProperties,
            ListingGeneratorWriter writer) throws UnifyException {
        writeLetterFooter(reader, writer.getFormListing(LetterFormListing.class), listingProperties, writer);
    }

    @Override
    protected final void writeReportHeader(ValueStoreReader reader, ListingReportProperties listingProperties,
            ListingGeneratorWriter writer) throws UnifyException {
        writeLetterHeader(reader, writer.getFormListing(LetterFormListing.class), listingProperties, writer);
    }

    @Override
    protected final void doWriteBody(ValueStoreReader reader, ListingProperties listingProperties,
            ListingGeneratorWriter writer) throws UnifyException {
        ListingColumn[] columns = new ListingColumn[] { new ListingColumn(HAlignType.LEFT, 100) };
        final LetterFormListing letterFormListing = writer.getFormListing(LetterFormListing.class);
        final FontSetting fontSetting = letterFormListing.getWorkingFontSetting();
        final String body = getLetterBody(reader, letterFormListing);
        StringBuilder sb = new StringBuilder();
        sb.append("<pre");
        if (fontSetting != null) {
            sb.append(" style=\"");
            sb.append(fontSetting.getFontStyle());
            sb.append("\"");
        }
        sb.append(">");
        sb.append(body);
        sb.append("</pre>");

        final ListingCell cell = new ListingCell(ListingCellType.TEXT, sb.toString(), ListingCell.BORDER_NONE);
        cell.setNoHtmlEscape(true);
        writer.beginSection(1, 100, HAlignType.LEFT, false, ListingCell.BORDER_NONE);
        writer.beginClassicTable(columns);
        writer.replaceTableColumns(Arrays.asList(columns));
        writer.writeRow(Arrays.asList(cell));
        writer.endTable();
        writer.endSection();
    }

    protected abstract int getNumberOfParts(ValueStoreReader reader) throws UnifyException;

    protected abstract String getLetterBody(ValueStoreReader reader, LetterFormListing letterFormListing)
            throws UnifyException;

    protected abstract void writeLetterAddendum(ValueStoreReader reader, LetterFormListing letterFormListing,
            ListingReportProperties listingProperties, ListingGeneratorWriter writer) throws UnifyException;

    protected abstract void writeLetterFooter(ValueStoreReader reader, LetterFormListing letterFormListing,
            ListingReportProperties listingProperties, ListingGeneratorWriter writer) throws UnifyException;

    protected abstract void writeLetterHeader(ValueStoreReader reader, LetterFormListing letterFormListing,
            ListingReportProperties listingProperties, ListingGeneratorWriter writer) throws UnifyException;

}

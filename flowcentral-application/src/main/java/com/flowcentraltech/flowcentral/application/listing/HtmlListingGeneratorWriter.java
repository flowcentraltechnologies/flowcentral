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

import java.util.Set;

import com.flowcentraltech.flowcentral.application.constants.ListingColorType;
import com.flowcentraltech.flowcentral.application.util.ListingUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.resource.ImageProvider;
import com.tcdng.unify.web.ThemeManager;
import com.tcdng.unify.web.ui.widget.ResponseWriter;

/**
 * HTML Listing generator writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class HtmlListingGeneratorWriter extends AbstractListingGeneratorWriter {

    private final ResponseWriter writer;

    public HtmlListingGeneratorWriter(ThemeManager themeManager, ImageProvider entityImageProvider, String listingType,
            ResponseWriter writer, Set<ListingColorType> pausePrintColors, boolean highlighting) {
        super(themeManager, entityImageProvider, listingType, pausePrintColors, highlighting);
        this.writer = writer;
    }

    @Override
    public void close() {

    }

    @Override
    protected void doBeginSection(ListingSectionHeader header, int[] sectionColumnWidth, int widthPercent,
            HAlignType horizontalAlign, boolean alternatingColumn, int borders) throws UnifyException {
        // Begin section
        writer.write("<div class=\"flsection").write(ListingUtils.getBorderStyle(borders)).write("\">");
        if (header != null) {
            writer.write("<div class=\"fltable flsectionheader\">");
            doWriteRow(header.getColumns(), false, header.getCells());
            writer.write("</div>");
        }

        int leftMargin = 0;
        if (HAlignType.CENTER.equals(horizontalAlign)) {
            leftMargin = (100 - widthPercent) / 2;
        } else if (HAlignType.RIGHT.equals(horizontalAlign)) {
            leftMargin = 100 - widthPercent;
        }

        writer.write("<div class=\"flsectionbody\" style=\"width:").write(widthPercent).write("%;margin-left:")
                .write(leftMargin).write("%;\">"); // Begin section body
    }

    @Override
    protected void doBeginTable(boolean classicTable, ListingColumn... _columns) throws UnifyException {
        if (currentSectionColumn == 0) {
            writer.write("<div class=\"flsectionbodyrow\">"); // Begin section row
        }

        int width = sectionColumnWidth[currentSectionColumn];
        currentSectionColumn++;

        writer.write("<div class=\"flsectionbodycell");
        if (alternatingColumn && (currentSectionColumn % 2) == 0) {
            writer.write(" flgray");
        }
        writer.write("\" style=\"width:").write(width).write("%;\">");
        if (classicTable) {
            writer.write("<table class=\"flctable\">");
        } else {
            writer.write("<div class=\"fltable\">");
        }
    }

    @Override
    protected void doWriteRow(ListingColumn[] columns, ListingCell... cells) throws UnifyException {
        doWriteRow(columns, true, cells);
    }

    private void doWriteRow(ListingColumn[] columns, boolean spaceOnEmptyContent, ListingCell... cells)
            throws UnifyException {
        if (classicTable) {
            writer.write("<tr");
        } else {
            writer.write("<div class=\"flrow\"");
        }

        if (isWithRowColor()) {
            writer.write(" style=\"background-color:");
            writer.write(rowColor.backgroundColor());
            writer.write(";\"");
        }
        writer.write(">");

        for (int cellIndex = 0; cellIndex < cells.length; cellIndex++) {
            ListingColumn column = columns[cellIndex];
            ListingCell cell = cells[cellIndex];
            if (classicTable) {
                writer.write("<td");
                if (column.isColumnsWidth()) {
                    writer.write(" colspan=\"").write(column.getWidth()).write("\"");
                }
            } else {
                writer.write("<div class=\"flcell").write(ListingUtils.getBorderStyle(cell.getBorders())).write("\"");
            }

            writer.write(" style=\"");
            if (column.isStyleWidth()) {
                writer.write("width:").write(column.getWidth());
                if (column.isPixelsWidth()) {
                    writer.write("px;");
                } else if (column.isPercentWidth()) {
                    writer.write("%;");
                }
            }

            if (highlighting && cell.isWithCellColor()) {
                writer.write("background-color:");
                writer.write(cell.getCellColor().backgroundColor());
                writer.write(";");
            }
            writer.write("\">");
            writer.write("<span class=\"flcontent ").write(cell.getType().styleClass()).write(" ")
                    .write(column.getAlign().styleClass()).write("\">");
            if (cell.isWithContent()) {
                if (cell.isFileImage() || cell.isEntityProviderImage()) {
                    writer.write("<img src=\"");
                    if (cell.isFileImage()) {
                        writer.writeFileImageContextURL(cell.getContent());
                    } else {
                        writer.write("data:image/*;base64,");
                        writer.write(entityImageProvider.provideAsBase64String(cell.getContent()));
                    }

                    writer.write("\"");
                    if (cell.isWithContentStyle()) {
                        writer.write(" style=\"");
                        writer.write(cell.getContentStyle());
                        writer.write("\"");
                    }
                    writer.write(">");
                    writer.write("</img>");
                } else if (cell.isScopeImage()) {
                    writer.writeScopeImageContextURL(null); // TODO
                } else {
                    if (cell.isNoHtmlEscape()) {
                        writer.write(cell.getContent());
                    } else {
                        writer.writeResolvedSessionMessage(cell.getContent());
                    }
                }
            } else {
                if (spaceOnEmptyContent) {
                    writer.write("&#160;"); // SAX failure on &nbsp;
                }
            }
            writer.write("</span>");

            if (classicTable) {
                writer.write("</td>");
            } else {
                writer.write("</div>");
            }
        }

        if (classicTable) {
            writer.write("</tr>");
        } else {
            writer.write("</div>");
        }
    }

    @Override
    protected void doEndTable() throws UnifyException {
        if (classicTable) {
            writer.write("</table>");
        } else {
            writer.write("</div>");
        }
        writer.write("</div>");

        if (currentSectionColumn == sectionColumnWidth.length) {
            currentSectionColumn = 0;
            writer.write("</div>"); // End section row
        }
    }

    @Override
    protected void doEndSection(boolean addSectionSpacing) throws UnifyException {
        if (currentSectionColumn > 0 && currentSectionColumn < sectionColumnWidth.length) {
            for (; currentSectionColumn < sectionColumnWidth.length; currentSectionColumn++) {
                writer.write("<div class=\"flsectionbodycell\" style=\"width:")
                        .write(sectionColumnWidth[currentSectionColumn]).write("%;\"></div>");
            }

            currentSectionColumn = 0;
            writer.write("</div>");// End section row
        }

        writer.write("</div>"); // End section body
        writer.write("</div>"); // End section

        if (addSectionSpacing) {
            writer.write("<div class=\"flsectionspacing\"></div>");
        }
    }
}

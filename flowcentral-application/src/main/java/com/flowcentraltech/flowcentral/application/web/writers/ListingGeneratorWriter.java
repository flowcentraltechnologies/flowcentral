/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import java.util.Arrays;
import java.util.List;

import com.flowcentraltech.flowcentral.application.constants.ListingRowColorType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.ResponseWriter;

/**
 * Listing generator writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ListingGeneratorWriter {

    private ResponseWriter writer;

    private List<ListingColumn> tableColumnList;

    private ListingRowColorType rowColor;
    
    private int sectionColumns;

    private int sectionColumnWidth;

    private int currentSectionColumn;

    private boolean inSection;

    private boolean alternatingColumn;

    public ListingGeneratorWriter(ResponseWriter writer) {
        this.writer = writer;
    }

    public ListingRowColorType getRowColor() {
        return rowColor;
    }

    public void setRowColor(ListingRowColorType rowColor) {
        this.rowColor = rowColor;
    }

    public boolean isWithRowColor() {
        return rowColor != null;
    }
    
    public void clearRowColor() {
        rowColor = null;
    }
    
    public void sectionHeader(String header) throws UnifyException {
        writer.write("<div class=\"flsection\"><span>").writeResolvedSessionMessage(header).write("</span></div>");
    }
    
    public void beginSection(int sectionColumns, int widthPercent, HAlignType horizontalAlign,
            boolean alternatingColumn) throws UnifyException {
        beginSection(null, sectionColumns, widthPercent, horizontalAlign, alternatingColumn);
    }
    
    public void beginSection(String header, int sectionColumns, int widthPercent, HAlignType horizontalAlign,
            boolean alternatingColumn) throws UnifyException {
        if (sectionColumns <= 0) {
            throw new RuntimeException("Section columns must be greater than zero.");
        }

        if (inSection) {
            throw new RuntimeException("Section already begun.");
        }

        this.sectionColumns = sectionColumns;
        this.sectionColumnWidth = 100 / sectionColumns;
        this.currentSectionColumn = 0;
        this.alternatingColumn = alternatingColumn;
        if (!StringUtils.isBlank(header)) {
            sectionHeader(header);
        }
        
        int leftMargin = 0;
        if (HAlignType.CENTER.equals(horizontalAlign)) {
            leftMargin = (100 - widthPercent) / 2;
        } else if (HAlignType.RIGHT.equals(horizontalAlign)) {
            leftMargin = 100 - widthPercent;
        }

        writer.write("<div class=\"flsectionbody\" style=\"width:").write(widthPercent).write("%;margin-left:")
                .write(leftMargin).write("%;\">"); // Begin section body
        inSection = true;
    }

    public void beginTable(ListingColumn... columns) throws UnifyException {
        if (!inSection) {
            throw new RuntimeException("No section started.");
        }

        if (tableColumnList != null) {
            throw new RuntimeException("Table already begun.");
        }

        if (currentSectionColumn == 0) {
            writer.write("<div class=\"flsectionbodyrow\">"); // Begin section row
        }

        currentSectionColumn++;

        tableColumnList = Arrays.asList(columns);
        writer.write("<div class=\"flsectionbodycell");
        if (alternatingColumn && (currentSectionColumn % 2) == 0) {
            writer.write(" flgray");
        }
        writer.write("\" style=\"width:").write(sectionColumnWidth).write("%;\">");
        writer.write("<div class=\"fltable\">");
    }

    public void writeRow(ListingCell... cells) throws UnifyException {
        if (tableColumnList == null) {
            throw new RuntimeException("No table is started.");
        }

        if (cells.length != tableColumnList.size()) {
            throw new IllegalArgumentException(
                    "Length of supplied cells does not match current section number of columns.");
        }

        writer.write("<div class=\"flrow\"");
        if (isWithRowColor()) {
            writer.write(" style=\"background-color:");
            writer.write(rowColor.backgroundColor());
            writer.write(";\"");
        }
        writer.write(">");
        
        for (int cellIndex = 0; cellIndex < cells.length; cellIndex++) {
            ListingColumn column = tableColumnList.get(cellIndex);
            ListingCell cell = cells[cellIndex];
            writer.write("<div class=\"flcell\" style=\"width:").write(column.getWidthPercent()).write("%;\">");
            writer.write("<span class=\"flcontent ").write(cell.getType().styleClass()).write(" ")
                    .write(column.getAlign().styleClass()).write("\">");
            if (cell.isWithContent()) {
                writer.writeResolvedSessionMessage(cell.getContent());
            }
            writer.write("</span>");
            writer.write("</div>");
        }

        writer.write("</div>");
    }

    public void endTable() throws UnifyException {
        if (tableColumnList == null) {
            throw new RuntimeException("No table is started.");
        }

        tableColumnList = null;
        writer.write("</div></div>");

        if (currentSectionColumn == sectionColumns) {
            currentSectionColumn = 0;
            writer.write("</div>"); // End section row
        }
    }

    public void endSection() throws UnifyException {
        if (tableColumnList != null) {
            throw new RuntimeException("Current table is not ended.");
        }

        if (!inSection) {
            throw new RuntimeException("No section started.");
        }

        if (currentSectionColumn > 0 && currentSectionColumn < sectionColumns) {
            for (; currentSectionColumn < sectionColumns; currentSectionColumn++) {
                writer.write("<div class=\"flsectionbodycell\" style=\"width:").write(sectionColumnWidth)
                        .write("%;\"></div>");
            }

            currentSectionColumn = 0;
            writer.write("</div>");// End section row
        }

        writer.write("</div>"); // End section body
        inSection = false;
    }
}

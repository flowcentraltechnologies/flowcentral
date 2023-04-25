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

import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.flowcentraltech.flowcentral.application.constants.ListingColorType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.resource.ImageProvider;

/**
 * Excel Listing generator writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ExcelListingGeneratorWriter extends AbstractListingGeneratorWriter {

    final private Sheet sheet;
    
    private int writeRow;
    
    private int writeColumn;
    
    private int tableStartRow;
    
    private int nextSectionStartRow;
    
    private int nextTableStartColumn;
    
    public ExcelListingGeneratorWriter(ImageProvider entityImageProvider, String listingType, Sheet sheet,
            Set<ListingColorType> pausePrintColors, boolean highlighting) {
        super(entityImageProvider, listingType, pausePrintColors, highlighting);
        this.sheet = sheet;
    }

    @Override
    protected void doBeginSection(ListingSectionHeader header, int[] sectionColumnWidth, int widthPercent,
            HAlignType horizontalAlign, boolean alternatingColumn, int borders) throws UnifyException {
        // Begin section
        writeRow = tableStartRow = nextSectionStartRow;
        if (header != null) {
            doWriteRow(header.getColumns(), header.getCells());
            tableStartRow++;
        }
        
        nextTableStartColumn = 0;
    }

    @Override
    protected void doBeginTable(boolean classicTable, ListingColumn... _columns) throws UnifyException {
        currentSectionColumn++;

        if (alternatingColumn && (currentSectionColumn % 2) == 0) {
            //TODO Set background for all cells in table
        }
        
        writeRow = tableStartRow; 
    }

    @Override
    protected void doWriteRow(ListingColumn[] columns, ListingCell... cells) throws UnifyException {
        Row row = getSheetWriteRow();
        if (isWithRowColor()) {
            // TODO Set row color
        }
        
        writeColumn = nextTableStartColumn;
        for (int cellIndex = 0; cellIndex < cells.length; cellIndex++) {
            ListingColumn _column = columns[cellIndex];
            ListingCell _cell = cells[cellIndex];
            // TODO Merge columns based on column width
            if (_cell.isWithContent()) {
                Cell cell = row.createCell(writeColumn);
                if (_cell.isDate()) {
                    cell.setCellValue(_cell.getDateContent());
                } else if (_cell.isNumber()) {
                    cell.setCellValue(_cell.getNumberContent().doubleValue());
                } else {
                    cell.setCellValue(_cell.getContent());
                }
            }
            
            writeColumn++;
        }
        
        writeRow++;
    }

    @Override
    protected void doEndTable() throws UnifyException {
        if (currentSectionColumn == sectionColumnWidth.length) {
            currentSectionColumn = 0;
        }
        
        if (nextTableStartColumn < writeColumn) {
            nextTableStartColumn = writeColumn;
        }
    }

    @Override
    protected void doEndSection() throws UnifyException {
        if (nextSectionStartRow < writeRow) {
            nextSectionStartRow = writeRow;
        }
    }
    
    private Row getSheetWriteRow() {
        Row row = sheet.getRow(writeRow);
        return row == null ? sheet.createRow(writeRow) : row;
    }
}

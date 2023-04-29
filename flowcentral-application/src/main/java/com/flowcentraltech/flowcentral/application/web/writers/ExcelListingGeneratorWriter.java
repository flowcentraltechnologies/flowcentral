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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.flowcentraltech.flowcentral.application.constants.ListingColorType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.resource.ImageProvider;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.ImageUtils;

/**
 * Excel Listing generator writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ExcelListingGeneratorWriter extends AbstractListingGeneratorWriter {

    private final Sheet sheet;

    @SuppressWarnings("rawtypes")
    private Drawing drawing;

    @SuppressWarnings("serial")
    private static final Map<HAlignType, HorizontalAlignment> alignment = Collections
            .unmodifiableMap(new HashMap<HAlignType, HorizontalAlignment>()
                {
                    {
                        put(HAlignType.LEFT, HorizontalAlignment.LEFT);
                        put(HAlignType.CENTER, HorizontalAlignment.CENTER);
                        put(HAlignType.RIGHT, HorizontalAlignment.RIGHT);
                        put(HAlignType.JUSTIFIED, HorizontalAlignment.JUSTIFY);
                    }
                });

    private Map<String, CellStyle> cellStyles;

    private int writeRow;

    private int writeColumn;

    private int tableStartRow;

    private int nextSectionStartRow;

    private int nextTableStartColumn;

    private int maxSheetColumns;

    private List<Merge> mergeList;

    public ExcelListingGeneratorWriter(ImageProvider entityImageProvider, String listingType, Sheet sheet,
            Set<ListingColorType> pausePrintColors, boolean highlighting) {
        super(entityImageProvider, listingType, pausePrintColors, highlighting);
        this.sheet = sheet;
        this.cellStyles = new HashMap<String, CellStyle>();
        this.mergeList = new ArrayList<Merge>();
    }

    @Override
    public void close() {
        autoSizeColumns();
        mergeRegions();
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
            // TODO Set background for all cells in table
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
        CreationHelper ch = sheet.getWorkbook().getCreationHelper();
        for (int cellIndex = 0; cellIndex < cells.length; cellIndex++) {
            ListingColumn _column = columns[cellIndex];
            ListingCell _cell = cells[cellIndex];
            Cell cell = row.createCell(writeColumn);
            ClientAnchor imgAnchor = null;
            int imgIndex = -1;
            if (_cell.isWithContent()) {
                if (_cell.isDate()) {
                    cell.setCellValue(_cell.getDateContent());
                } else if (_cell.isNumber()) {
                    cell.setCellValue(_cell.getNumberContent().doubleValue());
                } else {
                    cell.setCellValue(_cell.getContent());
                }
            } else if (_cell.isFileImage() || _cell.isEntityProviderImage()) {
                final byte[] image = _cell.isFileImage() ? IOUtils.readFileResourceInputStream(_cell.getContent())
                        : entityImageProvider.provideAsByteArray(_cell.getContent());
                int imgTypeIndex = detectWorkbookImageType(image);
                if (imgTypeIndex >= 0) {
                    imgIndex = sheet.getWorkbook().addPicture(image, imgTypeIndex);
                    imgAnchor = ch.createClientAnchor();
                    imgAnchor.setCol1(writeColumn);
                    imgAnchor.setCol2(writeColumn);
                    imgAnchor.setRow1(writeRow);
                    imgAnchor.setRow2(writeRow);
                }
            } else {
                cell.setCellValue("");
            }

            CellStyle style = getCellStyle(_column, _cell);
            cell.setCellStyle(style);

            final int mergeColumns = _column.getMergeColumns();
            if (mergeColumns > 1) {
                mergeList.add(new Merge(writeRow, writeRow, writeColumn, writeColumn + mergeColumns - 1));
                if (imgAnchor != null) {
                    imgAnchor.setCol2(writeColumn + mergeColumns - 1);
                }
            }

            if (imgIndex >= 0 && imgAnchor != null) {
                getDrawing().createPicture(imgAnchor, imgIndex);
            }

            writeColumn += mergeColumns;
        }

        writeRow++;
    }

    @SuppressWarnings("rawtypes")
    private Drawing getDrawing() {
        if (drawing == null) {
            synchronized (this) {
                if (drawing == null) {
                    drawing = sheet.createDrawingPatriarch();
                }
            }
        }

        return drawing;
    }

    private int detectWorkbookImageType(byte[] image) {
        ImageUtils.ImageType type = ImageUtils.detectImageType(image);
        if (type != null) {
            switch (type) {
                case BMP:
                    return Workbook.PICTURE_TYPE_DIB;
                case GIF:
                    break;
                case JPEG:
                    return Workbook.PICTURE_TYPE_JPEG;
                case PNG:
                    return Workbook.PICTURE_TYPE_PNG;
                default:
                    break;

            }
        }

        return -1;
    }

    @Override
    protected void doEndTable() throws UnifyException {
        if (currentSectionColumn == sectionColumnWidth.length) {
            currentSectionColumn = 0;
        }

        if (nextTableStartColumn < writeColumn) {
            nextTableStartColumn = writeColumn;
        }

        if (nextSectionStartRow < writeRow) {
            nextSectionStartRow = writeRow;
        }
    }

    @Override
    protected void doEndSection() throws UnifyException {
        if (maxSheetColumns < nextTableStartColumn) {
            maxSheetColumns = nextTableStartColumn;
        }
    }

    private class Merge {
        final int row1;
        final int row2;
        final int column1;
        final int column2;

        public Merge(int row1, int row2, int column1, int column2) {
            this.row1 = row1;
            this.row2 = row2;
            this.column1 = column1;
            this.column2 = column2;
        }

        public int getRow1() {
            return row1;
        }

        public int getRow2() {
            return row2;
        }

        public int getColumn1() {
            return column1;
        }

        public int getColumn2() {
            return column2;
        }
    }

    private void mergeRegions() {
        for (Merge merge : mergeList) {
            sheet.addMergedRegion(
                    new CellRangeAddress(merge.getRow1(), merge.getRow2(), merge.getColumn1(), merge.getColumn2()));
        }
    }

    private void autoSizeColumns() {
        for (int i = 0; i < maxSheetColumns; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private Row getSheetWriteRow() {
        Row row = sheet.getRow(writeRow);
        return row == null ? sheet.createRow(writeRow) : row;
    }

    private CellStyle getCellStyle(ListingColumn column, ListingCell cell) {
        final String key = cell.getFormatPattern() != null
                ? column.getAlign().toString() + cell.isBold() + cell.getFormatPattern()
                : column.getAlign().toString() + cell.isBold();
        CellStyle style = cellStyles.get(key);
        if (style == null) {
            style = sheet.getWorkbook().createCellStyle();
            style.setAlignment(alignment.get(column.getAlign()));
            if (cell.isBold()) {
                Font font = sheet.getWorkbook().createFont();
                font.setBold(true);
                style.setFont(font);
            }

            if (cell.getFormatPattern() != null) {
                CreationHelper ch = sheet.getWorkbook().getCreationHelper();
                style.setDataFormat(ch.createDataFormat().getFormat(cell.getFormatPattern()));
            }

            cellStyles.put(key, style);
        }

        return style;
    }
}

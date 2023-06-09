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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.constants.ListingColorType;
import com.flowcentraltech.flowcentral.common.data.FormListing;
import com.tcdng.unify.core.ThemeManager;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.filter.ObjectFilter;
import com.tcdng.unify.core.resource.ImageProvider;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Convenient abstract base class for listing generator writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractListingGeneratorWriter implements ListingGeneratorWriter {

    private ListingColumn[] columns;

    private final String listingType;

    protected final ImageProvider entityImageProvider;

    protected final ThemeManager themeManager;

    protected final boolean highlighting;

    protected final FormListing formListing;
    
    protected ListingColorType rowColor;

    protected int[] sectionColumnWidth;

    protected int currentSectionColumn;

    protected boolean classicTable;

    protected boolean alternatingColumn;

    private boolean inSection;

    private boolean pauseRowPrinting;

    private List<ItemColorRule> itemColorRules;

    private Set<ListingColorType> pauseRowPrintColors;

    public AbstractListingGeneratorWriter(FormListing formListing, ThemeManager themeManager, ImageProvider entityImageProvider,
            String listingType, Set<ListingColorType> pausePrintColors, boolean highlighting) {
        this.formListing = formListing;
        this.themeManager = themeManager;
        this.entityImageProvider = entityImageProvider;
        this.listingType = listingType;
        this.highlighting = highlighting;
        this.pauseRowPrinting = false;
        this.pauseRowPrintColors = pausePrintColors;
        this.itemColorRules = new ArrayList<ItemColorRule>();
    }

    @Override
    public <T extends FormListing> T getFormListing(Class<T> formListingType) {
        return (T) formListing;
    }

    @Override
    public String getListingType() {
        return listingType;
    }

    @Override
    public ListingColorType getRowColor() {
        return rowColor;
    }

    @Override
    public void addItemColorRule(Restriction restriction, ListingColorType color) {
        itemColorRules.add(new ItemColorRule(new ObjectFilter(restriction), color));
    }

    @Override
    public void clearItemColorRules() {
        itemColorRules.clear();
    }

    @Override
    public ListingColorType getItemColor(ValueStoreReader reader) throws UnifyException {
        for (ItemColorRule rule : itemColorRules) {
            if (rule.filter.matchReader(reader)) {
                return rule.color;
            }
        }

        return null;
    }

    @Override
    public ListingColorType getItemColor(Object bean) throws UnifyException {
        for (ItemColorRule rule : itemColorRules) {
            if (rule.filter.matchObject(bean)) {
                return rule.color;
            }
        }

        return null;
    }

    @Override
    public void setRowColor(ListingColorType rowColor) {
        pauseRowPrinting = pauseRowPrintColors.contains(rowColor);
        if (highlighting) {
            this.rowColor = rowColor;
        }
    }

    @Override
    public boolean isPausePrint(ValueStoreReader reader) throws UnifyException {
        ListingColorType color = getItemColor(reader);
        return color != null && pauseRowPrintColors.contains(color);
    }

    @Override
    public boolean isPausePrint(Object bean) throws UnifyException {
        ListingColorType color = getItemColor(bean);
        return color != null && pauseRowPrintColors.contains(color);
    }

    @Override
    public boolean isWithRowColor() {
        return rowColor != null;
    }

    @Override
    public void clearRowColor() {
        pauseRowPrinting = false;
        rowColor = null;
    }

    @Override
    public void beginSection(int sectionColumns, int widthPercent, HAlignType horizontalAlign,
            boolean alternatingColumn, int borders) throws UnifyException {
        beginSection(null, sectionColumns, widthPercent, horizontalAlign, alternatingColumn, borders);
    }

    @Override
    public void beginSection(String header, int sectionColumns, int widthPercent, HAlignType horizontalAlign,
            boolean alternatingColumn, int borders) throws UnifyException {
        if (sectionColumns <= 0) {
            throw new RuntimeException("Section columns must be greater than zero.");
        }

        int width = 100 / sectionColumns;
        int[] sectionColumnWidth = new int[sectionColumns];
        for (int i = 0; i < sectionColumns; i++) {
            sectionColumnWidth[i] = width;
        }

        ListingSectionHeader _header = header != null
                ? ListingSectionHeader.newBuilder()
                        .addColumn(HAlignType.LEFT, 100, ListingCellType.BOLD_TEXT, header, 0).build()
                : null;
        internalBeginSection(_header, sectionColumnWidth, widthPercent, horizontalAlign, alternatingColumn, borders);
    }

    @Override
    public void beginSection(int[] sectionColumnWidth, int widthPercent, HAlignType horizontalAlign,
            boolean alternatingColumn, int borders) throws UnifyException {
        beginSection(null, sectionColumnWidth, widthPercent, horizontalAlign, alternatingColumn, borders);
    }

    @Override
    public void beginSection(ListingSectionHeader header, int[] sectionColumnWidth, int widthPercent,
            HAlignType horizontalAlign, boolean alternatingColumn, int borders) throws UnifyException {
        int totalWidth = 0;
        for (int i = 0; i < sectionColumnWidth.length; i++) {
            int width = sectionColumnWidth[i];
            if (width <= 0) {
                throw new RuntimeException("Column width must be greater than zero.");
            }

            totalWidth += width;
        }

        int[] _sectionColumnWidth = new int[sectionColumnWidth.length];
        for (int i = 0; i < sectionColumnWidth.length; i++) {
            _sectionColumnWidth[i] = (sectionColumnWidth[i] * 100) / totalWidth;
        }

        internalBeginSection(header, _sectionColumnWidth, widthPercent, horizontalAlign, alternatingColumn, borders);
    }

    @Override
    public void replaceTableColumns(List<ListingColumn> _columns) throws UnifyException {
        replaceTableColumns(DataUtils.toArray(ListingColumn.class, _columns));
    }

    @Override
    public void replaceTableColumns(ListingColumn... _columns) throws UnifyException {
        if (columns == null) {
            throw new RuntimeException("No table is started.");
        }

        columns = _columns;
    }

    @Override
    public void beginClassicTable(List<ListingColumn> _columns) throws UnifyException {
        beginClassicTable(DataUtils.toArray(ListingColumn.class, _columns));
    }

    @Override
    public void beginClassicTable(ListingColumn... _columns) throws UnifyException {
        beginTable(true, _columns);
    }

    @Override
    public void beginTable(List<ListingColumn> _columns) throws UnifyException {
        beginTable(DataUtils.toArray(ListingColumn.class, _columns));
    }

    @Override
    public void beginTable(ListingColumn... _columns) throws UnifyException {
        beginTable(false, _columns);
    }

    @Override
    public void writeRow(List<ListingCell> cells) throws UnifyException {
        writeRow(DataUtils.toArray(ListingCell.class, cells));
    }

    @Override
    public void writeRow(ListingCell... cells) throws UnifyException {
        if (columns == null) {
            throw new RuntimeException("No table is started.");
        }

        if (cells.length != columns.length) {
            throw new IllegalArgumentException(
                    "Length of supplied cells does not match current section number of columns.");
        }

        if (!pauseRowPrinting) {
            doWriteRow(columns, cells);
        }
    }

    @Override
    public void endTable() throws UnifyException {
        if (columns == null) {
            throw new RuntimeException("No table is started.");
        }

        columns = null;
        doEndTable();
    }

    @Override
    public void endSection() throws UnifyException {
        if (columns != null) {
            throw new RuntimeException("Current table is not ended.");
        }

        if (!inSection) {
            throw new RuntimeException("No section started.");
        }

        doEndSection(false);
        inSection = false;
    }

    @Override
    public void endSectionWithSpacing() throws UnifyException {
        if (columns != null) {
            throw new RuntimeException("Current table is not ended.");
        }

        if (!inSection) {
            throw new RuntimeException("No section started.");
        }

        doEndSection(true);
        inSection = false;
    }

    protected class ItemColorRule {

        private final ObjectFilter filter;

        private final ListingColorType color;

        public ItemColorRule(ObjectFilter filter, ListingColorType color) {
            this.filter = filter;
            this.color = color;
        }

    }

    private void internalBeginSection(ListingSectionHeader header, int[] sectionColumnWidth, int widthPercent,
            HAlignType horizontalAlign, boolean alternatingColumn, int borders) throws UnifyException {
        if (sectionColumnWidth == null || sectionColumnWidth.length == 0) {
            throw new RuntimeException("Section columns must be greater than zero.");
        }

        if (inSection) {
            throw new RuntimeException("Section already begun.");
        }

        this.sectionColumnWidth = sectionColumnWidth;
        this.currentSectionColumn = 0;
        this.alternatingColumn = alternatingColumn;

        doBeginSection(header, sectionColumnWidth, widthPercent, horizontalAlign, alternatingColumn, borders);
        inSection = true;
    }

    private void beginTable(boolean classicTable, ListingColumn... _columns) throws UnifyException {
        if (!inSection) {
            throw new RuntimeException("No section started.");
        }

        if (columns != null) {
            throw new RuntimeException("Table already begun.");
        }

        doBeginTable(classicTable, _columns);
        this.classicTable = classicTable;
        this.columns = _columns;
    }

    protected abstract void doBeginSection(ListingSectionHeader header, int[] sectionColumnWidth, int widthPercent,
            HAlignType horizontalAlign, boolean alternatingColumn, int borders) throws UnifyException;

    protected abstract void doBeginTable(boolean classicTable, ListingColumn... _columns) throws UnifyException;

    protected abstract void doWriteRow(ListingColumn[] columns, ListingCell... cells) throws UnifyException;

    protected abstract void doEndTable() throws UnifyException;

    protected abstract void doEndSection(boolean addSectionSpacing) throws UnifyException;
}

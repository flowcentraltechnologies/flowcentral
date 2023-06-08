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

import java.util.List;

import com.flowcentraltech.flowcentral.application.constants.ListingColorType;
import com.flowcentraltech.flowcentral.common.data.FormListing;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Listing generator writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface ListingGeneratorWriter {

    <T extends FormListing> T getFormListing(Class<T> formListingType);

    String getListingType();

    ListingColorType getRowColor();

    void addItemColorRule(Restriction restriction, ListingColorType color);

    void clearItemColorRules();

    ListingColorType getItemColor(ValueStoreReader reader) throws UnifyException;

    ListingColorType getItemColor(Object bean) throws UnifyException;

    void setRowColor(ListingColorType rowColor);

    boolean isPausePrint(ValueStoreReader reader) throws UnifyException;

    boolean isPausePrint(Object bean) throws UnifyException;

    boolean isWithRowColor();

    void clearRowColor();

    void beginSection(int sectionColumns, int widthPercent, HAlignType horizontalAlign,
            boolean alternatingColumn, int borders) throws UnifyException;

    void beginSection(String header, int sectionColumns, int widthPercent, HAlignType horizontalAlign,
            boolean alternatingColumn, int borders) throws UnifyException;

    void beginSection(int[] sectionColumnWidth, int widthPercent, HAlignType horizontalAlign,
            boolean alternatingColumn, int borders) throws UnifyException;

    void beginSection(ListingSectionHeader header, int[] sectionColumnWidth, int widthPercent,
            HAlignType horizontalAlign, boolean alternatingColumn, int borders) throws UnifyException ;

    void replaceTableColumns(List<ListingColumn> _columns) throws UnifyException;

    void replaceTableColumns(ListingColumn... _columns) throws UnifyException;

    void beginClassicTable(List<ListingColumn> _columns) throws UnifyException;

    void beginClassicTable(ListingColumn... _columns) throws UnifyException;

    void beginTable(List<ListingColumn> _columns) throws UnifyException;

    void beginTable(ListingColumn... _columns) throws UnifyException;

    void writeRow(List<ListingCell> cells) throws UnifyException ;

    void writeRow(ListingCell... cells) throws UnifyException;

    void endTable() throws UnifyException;

    void endSection() throws UnifyException;

    void endSectionWithSpacing() throws UnifyException;
    
    void close();
}

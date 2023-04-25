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

import java.text.Format;
import java.util.Date;

import com.flowcentraltech.flowcentral.application.constants.ListingColorType;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Listing column.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ListingCell {

    public static final int BORDER_TOP = 0x00000001;

    public static final int BORDER_RIGHT = 0x00000002;

    public static final int BORDER_BOTTOM = 0x00000004;

    public static final int BORDER_LEFT = 0x00000008;

    public static final int BORDER_NONE = 0;

    public static final int BORDER_ALL = BORDER_TOP | BORDER_RIGHT | BORDER_BOTTOM | BORDER_LEFT;

    private ListingCellType type;

    private ListingColorType cellColor;

    private Object rawContent;

    private String content;

    private String contentStyle;

    private Format format;

    private int borders;

    public ListingCell() {
        this.type = ListingCellType.TEXT;
        this.borders = BORDER_ALL;
    }

    public ListingCell(String content) {
        this.type = ListingCellType.TEXT;
        this.rawContent = this.content = content;
        this.borders = BORDER_ALL;
    }

    public ListingCell(ListingCellType type, String content, int borders) {
        this.type = type;
        this.rawContent = this.content = content;
        this.borders = borders;
    }

    public ListingCell(ListingCellType type, String content, String contentStyle, int borders) {
        this.type = type;
        this.rawContent = this.content = content;
        this.contentStyle = contentStyle;
        this.borders = borders;
    }

    public ListingCell(String content, String contentStyle, int borders) {
        this.type = ListingCellType.TEXT;
        this.rawContent = this.content = content;
        this.contentStyle = contentStyle;
        this.borders = borders;
    }

    public ListingCell(ListingCellType type, String content) {
        this.type = type;
        this.rawContent = this.content = content;
    }

    public ListingCell(ListingCellType type, String content, String contentStyle) {
        this.type = type;
        this.rawContent = this.content = content;
        this.contentStyle = contentStyle;
    }

    public ListingCell(String content, String contentStyle) {
        this.type = ListingCellType.TEXT;
        this.rawContent = this.content = content;
        this.contentStyle = contentStyle;
    }

    public ListingCell(ListingCellType type, ListingColorType cellColor, String content, int borders) {
        this.type = type;
        this.cellColor = cellColor;
        this.rawContent = this.content = content;
        this.borders = borders;
    }

    public ListingCell(ListingCellType type, ListingColorType cellColor, String content, String contentStyle,
            int borders) {
        this.type = type;
        this.cellColor = cellColor;
        this.rawContent = this.content = content;
        this.contentStyle = contentStyle;
        this.borders = borders;
    }

    public ListingCell(ListingColorType cellColor, String content, String contentStyle, int borders) {
        this.type = ListingCellType.TEXT;
        this.cellColor = cellColor;
        this.rawContent = this.content = content;
        this.contentStyle = contentStyle;
        this.borders = borders;
    }

    public ListingCell(ListingCellType type, ListingColorType cellColor, String content) {
        this.type = type;
        this.cellColor = cellColor;
        this.rawContent = this.content = content;
    }

    public ListingCell(ListingCellType type, ListingColorType cellColor, String content, String contentStyle) {
        this.type = type;
        this.cellColor = cellColor;
        this.rawContent = this.content = content;
        this.contentStyle = contentStyle;
    }

    public ListingCell(ListingColorType cellColor, String content, String contentStyle) {
        this.type = ListingCellType.TEXT;
        this.cellColor = cellColor;
        this.rawContent = this.content = content;
        this.contentStyle = contentStyle;
    }

    public ListingCellType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getContentStyle() {
        return contentStyle;
    }

    public void setType(ListingCellType type) {
        this.type = type;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public void setContent(Object content) {
        this.rawContent = content;
        this.content = content != null ? (format != null ? format.format(content) : String.valueOf(content)) : null;
    }

    public Object getRawContent() {
        return rawContent;
    }

    public boolean isDate() {
        return rawContent instanceof Date;
    }

    public boolean isNumber() {
        return rawContent instanceof Number;
    }
    
    public Date getDateContent() {
        return (Date) rawContent;
    }
    
    public Number getNumberContent() {
        return (Number) rawContent;
    }
    
    public int getBorders() {
        return borders;
    }

    public boolean isWithContent() {
        return !StringUtils.isBlank(content);
    }

    public boolean isWithContentStyle() {
        return !StringUtils.isBlank(contentStyle);
    }

    public ListingColorType getCellColor() {
        return cellColor;
    }

    public boolean isWithCellColor() {
        return cellColor != null;
    }

    public boolean isFileImage() {
        return type.isFileImage();
    }

    public boolean isEntityProviderImage() {
        return type.isEntityProviderImage();
    }

    public boolean isScopeImage() {
        return type.isScopeImage();
    }
}

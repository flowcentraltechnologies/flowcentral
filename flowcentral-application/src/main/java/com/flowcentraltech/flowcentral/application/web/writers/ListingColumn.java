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

import com.tcdng.unify.core.constant.HAlignType;

/**
 * Listing column.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ListingColumn {

    public enum WidthType {
        PERCENT,
        PIXELS,
        COLUMNS
    };

    private HAlignType align;

    private int width;

    private WidthType widthType;

    public ListingColumn(int width) {
        this.align = HAlignType.LEFT;
        this.width = width;
        this.widthType = WidthType.PERCENT;
    }

    public ListingColumn(HAlignType align, int width) {
        this.align = align;
        this.width = width;
    }

    public ListingColumn(int width, WidthType widthType) {
        this.align = HAlignType.LEFT;
        this.width = width;
        this.widthType = widthType;
    }

    public ListingColumn(HAlignType align, int width, WidthType widthType) {
        this.align = align;
        this.width = width;
        this.widthType = widthType;
    }

    public HAlignType getAlign() {
        return align;
    }

    public void setAlign(HAlignType align) {
        this.align = align;
    }

    public int getWidth() {
        return width;
    }

    public boolean isPercentWidth() {
        return WidthType.PERCENT.equals(widthType);
    }

    public boolean isPixelsWidth() {
        return WidthType.PIXELS.equals(widthType);
    }

    public boolean isColumnsWidth() {
        return WidthType.COLUMNS.equals(widthType);
    }
    
    public boolean isStyleWidth() {
        return isPercentWidth() || isPixelsWidth();
    }
}

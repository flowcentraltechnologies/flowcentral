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

    private  HAlignType align;
    
    private int widthPercent;

    private boolean widthInPixels;

    public ListingColumn(int widthPercent) {
        this.align = HAlignType.LEFT;
        this.widthPercent = widthPercent;
        this.widthInPixels = false;
    }

    public ListingColumn(HAlignType align, int widthPercent) {
        this.align = align;
        this.widthPercent = widthPercent;
    }

    public ListingColumn(int widthPercent, boolean widthInPixels) {
        this.align = HAlignType.LEFT;
        this.widthPercent = widthPercent;
        this.widthInPixels = widthInPixels;
    }

    public ListingColumn(HAlignType align, int widthPercent, boolean widthInPixels) {
        this.align = align;
        this.widthPercent = widthPercent;
        this.widthInPixels = widthInPixels;
    }

    public HAlignType getAlign() {
        return align;
    }

    public void setAlign(HAlignType align) {
        this.align = align;
    }

    public int getWidthPercent() {
        return widthPercent;
    }

    public boolean isWidthInPixels() {
        return widthInPixels;
    }
}

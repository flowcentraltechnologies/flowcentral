/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.chart.data;

import com.flowcentraltech.flowcentral.configuration.constants.ChartCategoryDataType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Chart details.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ChartDetails {

    private String title;

    private String subTitle;

    private int titleOffsetX;

    private int subTitleOffsetX;

    private ChartDetails(String title, String subTitle, int titleOffsetX, int subTitleOffsetX) {
        this.title = title;
        this.subTitle = subTitle;
        this.titleOffsetX = titleOffsetX;
        this.subTitleOffsetX = subTitleOffsetX;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public boolean isWithTitle() {
        return !StringUtils.isBlank(title);
    }

    public boolean isWithSubtitle() {
        return !StringUtils.isBlank(subTitle);
    }

    public int getTitleOffsetX() {
        return titleOffsetX;
    }

    public int getSubTitleOffsetX() {
        return subTitleOffsetX;
    }

    public static Builder newBuilder(ChartCategoryDataType categoryType) {
        return new Builder(categoryType);
    }

    public static class Builder {

        private String title;

        private String subTitle;

        private int titleOffsetX;

        private int subTitleOffsetX;

        private ChartCategoryDataType categoryType;

        public Builder(ChartCategoryDataType categoryType) {
            this.categoryType = categoryType;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder titleOffsetX(int titleOffsetX) {
            this.titleOffsetX = titleOffsetX;
            return this;
        }

        public Builder subTitle(String subTitle) {
            this.subTitle = subTitle;
            return this;
        }

        public Builder subTitleOffsetX(int subTitleOffsetX) {
            this.subTitleOffsetX = subTitleOffsetX;
            return this;
        }

        public ChartCategoryDataType getCategoryType() {
            return categoryType;
        }

        public ChartDetails build() throws UnifyException {
            return new ChartDetails(title, subTitle, titleOffsetX, subTitleOffsetX);
        }

    }
}

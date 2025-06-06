/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.web.data;

import com.flowcentraltech.flowcentral.common.data.FontSetting;
import com.flowcentraltech.flowcentral.common.data.FormListing;

/**
 * Convenient abstract base class for form listings.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractFormListing implements FormListing {

    private int listingIndex;

    private FontSetting workingFontSetting;

    @Override
    public void nextIndex() {
        listingIndex++;
    }

    @Override
    public int getListingIndex() {
        return listingIndex;
    }

    @Override
    public FontSetting getWorkingFontSetting() {
        return workingFontSetting;
    }

    @Override
    public void setWorkingFontSetting(FontSetting workingFontSetting) {
        this.workingFontSetting = workingFontSetting;
    }

}

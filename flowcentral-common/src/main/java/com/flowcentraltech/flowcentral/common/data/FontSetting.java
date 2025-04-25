/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.common.data;

import com.flowcentraltech.flowcentral.configuration.constants.FontFamilyType;

/**
 * Form setting.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class FontSetting {

    public static final FontSetting DETAULT_FONT_SETTING = new FontSetting(10);

    private final FontFamilyType fontFamily;

    private final int fontSizeInPixels;

    public FontSetting(FontSetting fontSetting, int fontSizeInPixels) {
        this.fontFamily = fontSetting.fontFamily;
        this.fontSizeInPixels = fontSizeInPixels;
    }

    public FontSetting(FontFamilyType fontFamily, int fontSizeInPixels) {
        this.fontFamily = fontFamily;
        this.fontSizeInPixels = fontSizeInPixels;
    }

    public FontSetting(int fontSizeInPixels) {
        this.fontFamily = FontFamilyType.SERIF;
        this.fontSizeInPixels = fontSizeInPixels;
    }

    public boolean isWithFontFamily() {
        return fontFamily != null;
    }

    public FontFamilyType getFontFamily() {
        return fontFamily;
    }

    public int getFontSizeInPixels() {
        return fontSizeInPixels;
    }

    public String getFontStyle() {
        StringBuilder sb = new StringBuilder();
        sb.append("font-size:");
        sb.append(fontSizeInPixels);
        sb.append("px;");

        if (isWithFontFamily()) {
            sb.append("font-family:");
            sb.append(fontFamily.code());
            sb.append(";");
        }

        return sb.toString();
    }
}
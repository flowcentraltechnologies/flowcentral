/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.application.constants;

/**
 * Listing color type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public enum ListingColorType {

    BLACK("#d3d3d3", "#000000"),
    RED("#ffbeb2", "#641e16"),
    YELLOW("#fff5b0", "#7d6608"),
    GREEN("#b7ffdd", "#0e6251"),
    BLUE("#c3e9ff", "#154360"),
    VIOLET("#e5baff", "#4a235a");

    private final String backgroundColor;

    private final String textColor;
    
    private ListingColorType(String backgroundColor, String textColor) {
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }

    public String backgroundColor() {
        return this.backgroundColor;
    }

    public String textColor() {
        return this.textColor;
    }

}

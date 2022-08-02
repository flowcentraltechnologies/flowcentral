/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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
 * Listing row color type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public enum ListingRowColorType {

    RED("#ffbeb2"),
    YELLOW("#fff5b0"),
    GREEN("#b7ffdd"),
    BLUE("#c3e9ff"),
    VIOLET("#e5baff");

    private final String backgroundColor;
    
    private ListingRowColorType(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String backgroundColor() {
        return this.backgroundColor;
    }

}

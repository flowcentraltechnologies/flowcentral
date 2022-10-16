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

package com.flowcentraltech.flowcentral.application.web.writers;

import com.tcdng.unify.core.util.StringUtils;

/**
 * Listing column.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ListingCell {

    private  ListingCellType type;
    
    private String content;

    public ListingCell(ListingCellType type, String content) {
        this.type = type;
        this.content = content;
    }

    public ListingCell(String content) {
        this.type = ListingCellType.TEXT;
        this.content = content;
    }

    public ListingCellType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public boolean isWithContent() {
        return !StringUtils.isBlank(content);
    }
    
    public boolean isFileImage() {
        return type.isFileImage();
    }
    
    public boolean isScopeImage() {
        return type.isScopeImage();
    }
}

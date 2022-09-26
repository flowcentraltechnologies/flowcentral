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

package com.flowcentraltech.flowcentral.application.web.widgets;

import com.flowcentraltech.flowcentral.application.data.EntityFieldAttributes;

/**
 * Entry attributes
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntryAttributes implements EntityFieldAttributes {

    private String suggestionType;

    private String references;

    private int minLen;

    private int maxLen;

    private int precision;

    private int scale;

    private boolean allowNegative;

    public EntryAttributes(String suggestionType, String references, int minLen, int maxLen, int precision, int scale, boolean allowNegative) {
        this.suggestionType = suggestionType;
        this.references = references;
        this.minLen = minLen;
        this.maxLen = maxLen;
        this.precision = precision;
        this.scale = scale;
        this.allowNegative = allowNegative;
    }

    public EntryAttributes(int minLen, int maxLen, int precision, int scale, boolean allowNegative) {
        this.minLen = minLen;
        this.maxLen = maxLen;
        this.precision = precision;
        this.scale = scale;
        this.allowNegative = allowNegative;
    }

    public EntryAttributes() {

    }

    @Override
    public String getSuggestionType() {
        return suggestionType;
    }

    @Override
    public String getReferences() {
        return references;
    }

    @Override
    public int getMinLen() {
        return minLen;
    }

    @Override
    public int getMaxLen() {
        return maxLen;
    }

    @Override
    public int getPrecision() {
        return precision;
    }

    @Override
    public int getScale() {
        return scale;
    }

    @Override
    public boolean isAllowNegative() {
        return allowNegative;
    }

}

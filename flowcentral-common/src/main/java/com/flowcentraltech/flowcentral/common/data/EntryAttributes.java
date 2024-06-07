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

package com.flowcentraltech.flowcentral.common.data;

/**
 * Entry attributes
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntryAttributes implements EntityFieldAttributes {

    public static final EntryAttributes BLANK = new EntryAttributes();

    private String suggestionType;

    private String references;

    private int minLen;

    private int maxLen;

    private int precision;

    private int scale;

    private boolean trim;

    private boolean allowNegative;

    private EntryAttributes(String suggestionType, String references, int minLen, int maxLen, int precision, int scale,
            boolean trim, boolean allowNegative) {
        this.suggestionType = suggestionType;
        this.references = references;
        this.minLen = minLen;
        this.maxLen = maxLen;
        this.precision = precision;
        this.scale = scale;
        this.trim = trim;
        this.allowNegative = allowNegative;
    }

    private EntryAttributes() {

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
    public boolean isTrim() {
        return trim;
    }

    @Override
    public boolean isAllowNegative() {
        return allowNegative;
    }

    public static class Builder {

        private String suggestionType;

        private String references;

        private int minLen;

        private int maxLen;

        private int precision;

        private int scale;

        private boolean trim;

        private boolean allowNegative;

        public Builder suggestionType(String suggestionType) {
            this.suggestionType = suggestionType;
            return this;
        }

        public Builder references(String references) {
            this.references = references;
            return this;
        }

        public Builder minLen(int minLen) {
            this.minLen = minLen;
            return this;
        }

        public Builder maxLen(int maxLen) {
            this.maxLen = maxLen;
            return this;
        }

        public Builder precision(int precision) {
            this.precision = precision;
            return this;
        }

        public Builder scale(int scale) {
            this.scale = scale;
            return this;
        }

        public Builder trim(boolean trim) {
            this.trim = trim;
            return this;
        }

        public Builder allowNegative(boolean allowNegative) {
            this.allowNegative = allowNegative;
            return this;
        }

        public EntryAttributes build() {
            return new EntryAttributes(suggestionType, references, minLen, maxLen, precision, scale, trim,
                    allowNegative);
        }
    }
}

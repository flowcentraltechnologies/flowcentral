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
package com.flowcentraltech.flowcentral.security.business.data;

/**
 * Password complexity settings.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class PasswordComplexitySettings {

    private Integer minimumPasswordLen;

    private Integer minimumAlphabets;

    private Integer minimumNumbers;

    private Integer minimumSpecial;

    private Integer minimumUppercase;

    private Integer minimumLowercase;

    public Integer getMinimumPasswordLen() {
        return minimumPasswordLen;
    }

    public void setMinimumPasswordLen(Integer minimumPasswordLen) {
        this.minimumPasswordLen = minimumPasswordLen;
    }

    public Integer getMinimumAlphabets() {
        return minimumAlphabets;
    }

    public void setMinimumAlphabets(Integer minimumAlphabets) {
        this.minimumAlphabets = minimumAlphabets;
    }

    public Integer getMinimumNumbers() {
        return minimumNumbers;
    }

    public void setMinimumNumbers(Integer minimumNumbers) {
        this.minimumNumbers = minimumNumbers;
    }

    public Integer getMinimumSpecial() {
        return minimumSpecial;
    }

    public void setMinimumSpecial(Integer minimumSpecial) {
        this.minimumSpecial = minimumSpecial;
    }

    public Integer getMinimumUppercase() {
        return minimumUppercase;
    }

    public void setMinimumUppercase(Integer minimumUppercase) {
        this.minimumUppercase = minimumUppercase;
    }

    public Integer getMinimumLowercase() {
        return minimumLowercase;
    }

    public void setMinimumLowercase(Integer minimumLowercase) {
        this.minimumLowercase = minimumLowercase;
    }

    public void clear() {
        minimumPasswordLen = null;
        minimumAlphabets = null;
        minimumNumbers = null;
        minimumSpecial = null;
        minimumUppercase = null;
        minimumLowercase = null;
    }
}

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
 * Password complexity check.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class PasswordComplexityCheck {

    private PasswordComplexitySettings settings;

    private boolean minimumPasswordLen;

    private boolean minimumAlphabets;

    private boolean minimumNumbers;

    private boolean minimumSpecial;

    private boolean minimumUppercase;

    private boolean minimumLowercase;

    public PasswordComplexityCheck(PasswordComplexitySettings settings, boolean minimumPasswordLen,
            boolean minimumAlphabets, boolean minimumNumbers, boolean minimumSpecial, boolean minimumUppercase,
            boolean minimumLowercase) {
        this.settings = settings;
        this.minimumPasswordLen = minimumPasswordLen;
        this.minimumAlphabets = minimumAlphabets;
        this.minimumNumbers = minimumNumbers;
        this.minimumSpecial = minimumSpecial;
        this.minimumUppercase = minimumUppercase;
        this.minimumLowercase = minimumLowercase;
    }

    public PasswordComplexitySettings getSettings() {
        return settings;
    }

    public boolean isMinimumPasswordLen() {
        return minimumPasswordLen;
    }

    public boolean isMinimumAlphabets() {
        return minimumAlphabets;
    }

    public boolean isMinimumNumbers() {
        return minimumNumbers;
    }

    public boolean isMinimumSpecial() {
        return minimumSpecial;
    }

    public boolean isMinimumUppercase() {
        return minimumUppercase;
    }

    public boolean isMinimumLowercase() {
        return minimumLowercase;
    }

    public boolean pass() {
        return minimumPasswordLen && minimumAlphabets && minimumNumbers && minimumSpecial && minimumUppercase
                && minimumLowercase;
    }
}

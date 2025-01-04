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
package com.flowcentraltech.flowcentral.security.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.core.annotation.Column;

/**
 * Entity for storing password complexity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_PASSWORDCOMPLEXITY")
public class PasswordComplexity extends BaseAuditEntity {

    @Column(name = "minimum_password_len", length = 2)
    private Integer minimumPasswordLen;

    @Column(name = "minimum_alphabets", length = 2, nullable = true)
    private Integer minimumAlphabets;

    @Column(name = "minimum_numbers", length = 2, nullable = true)
    private Integer minimumNumbers;

    @Column(name = "minimum_special", length = 2, nullable = true)
    private Integer minimumSpecial;

    @Column(name = "minimum_uppercase", length = 2, nullable = true)
    private Integer minimumUppercase;

    @Column(name = "minimum_lowercase", length = 2, nullable = true)
    private Integer minimumLowercase;

    @Override
    public String getDescription() {
        return null;
    }

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


}

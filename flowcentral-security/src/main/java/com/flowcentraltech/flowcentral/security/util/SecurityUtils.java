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

package com.flowcentraltech.flowcentral.security.util;

import com.flowcentraltech.flowcentral.security.business.data.PasswordComplexityCheck;
import com.flowcentraltech.flowcentral.security.business.data.PasswordComplexitySettings;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.message.MessageResolver;

/**
 * Security utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public final class SecurityUtils {

    private SecurityUtils() {

    }

    public static String getPasswordComplexityCheckFailureMessage(PasswordComplexityCheck check,
            MessageResolver messageResolver) throws UnifyException {
        if (!check.pass()) {
            PasswordComplexitySettings settings = check.getSettings();
            StringBuilder sb = new StringBuilder();
            sb.append(messageResolver.resolveSessionMessage("$m{security.passwordcomplexity.checkfail}"));
            
            if (settings.getMinimumPasswordLen() != null) {
                sb.append("<br>");
                sb.append(messageResolver.resolveSessionMessage("$m{security.passwordcomplexity.checkfail.passwordlen}",
                        settings.getMinimumPasswordLen()));
            }

            if (settings.getMinimumAlphabets() != null) {
                sb.append("<br>");
                sb.append(messageResolver.resolveSessionMessage("$m{security.passwordcomplexity.checkfail.alphabets}",
                        settings.getMinimumAlphabets()));
            }

            if (settings.getMinimumNumbers() != null) {
                sb.append("<br>");
                sb.append(messageResolver.resolveSessionMessage("$m{security.passwordcomplexity.checkfail.numbers}",
                        settings.getMinimumNumbers()));
            }

            if (settings.getMinimumSpecial() != null) {
                sb.append("<br>");
                sb.append(messageResolver.resolveSessionMessage("$m{security.passwordcomplexity.checkfail.special}",
                        settings.getMinimumSpecial()));
            }

            if (settings.getMinimumUppercase() != null) {
                sb.append("<br>");
                sb.append(messageResolver.resolveSessionMessage("$m{security.passwordcomplexity.checkfail.uppercase}",
                        settings.getMinimumUppercase()));
            }

            if (settings.getMinimumLowercase() != null) {
                sb.append("<br>");
                sb.append(messageResolver.resolveSessionMessage("$m{security.passwordcomplexity.checkfail.lowercase}",
                        settings.getMinimumLowercase()));
            }

            return sb.toString();
        }

        return null;
    }
}

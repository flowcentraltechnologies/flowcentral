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

package com.flowcentraltech.flowcentral.security.util;

import com.flowcentraltech.flowcentral.security.business.data.PasswordComplexityCheck;
import com.flowcentraltech.flowcentral.security.business.data.PasswordComplexitySettings;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.message.MessageResolver;

/**
 * Security utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class SecurityUtils {

    private SecurityUtils() {

    }

    public static String getPasswordComplexityCheckFailureMessage(PasswordComplexityCheck check,
            MessageResolver messageResolver) throws UnifyException {
        if (!check.pass()) {
            PasswordComplexitySettings settings = check.getSettings();
            StringBuilder sb = new StringBuilder();
            boolean appendSym = false;
            if (!check.isMinimumPasswordLen()) {
                sb.append(messageResolver.resolveSessionMessage("$m{security.passwordcomplexity.checkfail.passwordlen}",
                        settings.getMinimumPasswordLen()));
                appendSym = true;
            }

            if (!check.isMinimumAlphabets()) {
                if (appendSym) {
                    sb.append(' ');
                }

                sb.append(messageResolver.resolveSessionMessage("$m{security.passwordcomplexity.checkfail.alphabets}",
                        settings.getMinimumAlphabets()));
                appendSym = true;
            }

            if (!check.isMinimumNumbers()) {
                if (appendSym) {
                    sb.append(' ');
                }

                sb.append(messageResolver.resolveSessionMessage("$m{security.passwordcomplexity.checkfail.numbers}",
                        settings.getMinimumNumbers()));
                appendSym = true;
            }

            if (!check.isMinimumSpecial()) {
                if (appendSym) {
                    sb.append(' ');
                }

                sb.append(messageResolver.resolveSessionMessage("$m{security.passwordcomplexity.checkfail.special}",
                        settings.getMinimumSpecial()));
                appendSym = true;
            }

            if (!check.isMinimumUppercase()) {
                if (appendSym) {
                    sb.append(' ');
                }

                sb.append(messageResolver.resolveSessionMessage("$m{security.passwordcomplexity.checkfail.uppercase}",
                        settings.getMinimumUppercase()));
                appendSym = true;
            }

            if (!check.isMinimumLowercase()) {
                if (appendSym) {
                    sb.append(' ');
                }

                sb.append(messageResolver.resolveSessionMessage("$m{security.passwordcomplexity.checkfail.lowercase}",
                        settings.getMinimumLowercase()));
            }

            return sb.toString();
        }

        return null;
    }
}

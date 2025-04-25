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

package com.flowcentraltech.flowcentral.common.util;

/**
 * Validation utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class ValidationUtils {

    private ValidationUtils() {

    }

    public static boolean isBlank(Object val) {
        if (val instanceof String) {
            return ((String) val).trim().isEmpty();
        }

        return val == null;
    }

    public static boolean isLessThanMinLen(String val, int minLen) {
        return minLen > 0 && (val != null && (val.trim().length() < minLen));
    }

    public static boolean isGreaterThanMaxLen(String val, int maxLen) {
        return val != null && maxLen > 0 && val.trim().length() > maxLen;
    }
}

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
package com.flowcentraltech.flowcentral.messaging.os.util;

import com.flowcentraltech.flowcentral.messaging.os.data.OSCredentials;
import com.tcdng.unify.core.util.EncodingUtils;

/**
 * OS Messaging Utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public final class OSMessagingUtils {

    private static final String BASIC_AUTH_PREFIX = "Basic ";
    
    private OSMessagingUtils() {
        
    }
    
    public static boolean isBasicAuthorization(String authorization) {
        return authorization != null && authorization.startsWith(BASIC_AUTH_PREFIX);
    }
    
    public static String getAuthorization(String source, String processor, String password) {
        return BASIC_AUTH_PREFIX + EncodingUtils
                .getBase64String(source + "." + processor + ":" + password);
    }
    
    public static OSCredentials getOSCredentials(String authorization) {
        final String credentials = EncodingUtils
                .decodeBase64String(authorization.substring(BASIC_AUTH_PREFIX.length()));
        String[] parts = credentials.split(":", 2);
        String[] nparts = parts[0].split("\\.", 2);
        return new OSCredentials(nparts[0], nparts[1], parts[1]);
    }

}

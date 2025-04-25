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

package com.flowcentraltech.flowcentral.application.util;

/**
 * Reference encoding utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class RefEncodingUtils {

    public static String encodeRefValue(int index, String refLongName, String value) {
        return new StringBuilder().append(index).append(':').append(refLongName).append(':').append(value).toString();
    }
    
    public static RefDecodedValue decodeRefValue(Object refValue) {
        if (refValue != null) {
            String[] parts = String.valueOf(refValue).split(":");
            if (parts.length == 3) {
                return new RefDecodedValue(Integer.valueOf(parts[0]), parts[1], parts[2]);
            }
            
            return new RefDecodedValue(refValue);
        }
        
        return null;
    }
}

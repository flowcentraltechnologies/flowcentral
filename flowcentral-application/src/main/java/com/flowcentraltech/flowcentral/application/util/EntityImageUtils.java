/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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
 * Entity image utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class EntityImageUtils {

    private EntityImageUtils() {

    }

    public static String encode(EntityImage image) {
        return image.getEntity() + ":" + image.getInstId() + ":" + image.getFieldName();
    }
    
    public static EntityImage decode(String encoded) {
        try {
            String[] parts = encoded.split(":");
            if (parts.length == 3) {
                return new EntityImage(parts[0], Long.valueOf(parts[1]), parts[2]);
            }
        } catch (Exception e) {
        }
        
        return EntityImage.BLANK;
    }
}

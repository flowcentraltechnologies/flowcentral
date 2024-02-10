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
package com.flowcentraltech.flowcentral.application.util;

import com.flowcentraltech.flowcentral.application.listing.ListingCell;

/**
 * Listing utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ListingUtils {

    public static boolean isBorderTop(int borders) {
        return (borders & ListingCell.BORDER_TOP)  > 0;
    }

    public static boolean isBorderRight(int borders) {
        return (borders & ListingCell.BORDER_RIGHT)  > 0;
    }

    public static boolean isBorderBottom(int borders) {
        return (borders & ListingCell.BORDER_BOTTOM)  > 0;
    }

    public static boolean isBorderLeft(int borders) {
        return (borders & ListingCell.BORDER_LEFT)  > 0;
    }

    public static String getBorderStyle(int borders) {
        StringBuilder sb = new StringBuilder();
        if (isBorderTop(borders)) {
            sb.append(" flbtop");
        }
        
        if (isBorderRight(borders)) {
            sb.append(" flbright");
        }
        
        if (isBorderBottom(borders)) {
            sb.append(" flbbottom");
        }
        
        if (isBorderLeft(borders)) {
            sb.append(" flbleft");
        }
        
        return sb.toString();
    }

}

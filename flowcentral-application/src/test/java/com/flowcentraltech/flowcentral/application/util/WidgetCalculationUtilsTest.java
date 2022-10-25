/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests widget calculation utilities class.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WidgetCalculationUtilsTest {

    @Test
    public void shareAsEqualAsPossible() throws Exception {
        int [] shared = WidgetCalculationUtils.shareAsEqualAsPossible(0, 1);
        assertEquals(1, shared.length);
        assertEquals(0, shared[0]);
        
        shared = WidgetCalculationUtils.shareAsEqualAsPossible(0, 2);
        assertEquals(2, shared.length);
        assertEquals(0, shared[0]);
        assertEquals(0, shared[1]);
        
        shared = WidgetCalculationUtils.shareAsEqualAsPossible(25, 1);
        assertEquals(1, shared.length);
        assertEquals(25, shared[0]);
        
        shared = WidgetCalculationUtils.shareAsEqualAsPossible(24, 2);
        assertEquals(2, shared.length);
        assertEquals(12, shared[0]);
        assertEquals(12, shared[1]);
        
        shared = WidgetCalculationUtils.shareAsEqualAsPossible(25, 2);
        assertEquals(2, shared.length);
        assertEquals(13, shared[0]);
        assertEquals(12, shared[1]);
        
        shared = WidgetCalculationUtils.shareAsEqualAsPossible(26, 3);
        assertEquals(3, shared.length);
        assertEquals(9, shared[0]);
        assertEquals(9, shared[1]);
        assertEquals(8, shared[2]);        
    }

    @Test
    public void shareAsEqualAsPossibleWithRatios() throws Exception {
        int [] shared = WidgetCalculationUtils.shareAsEqualAsPossible(0, new int[] {1});
        assertEquals(1, shared.length);
        assertEquals(0, shared[0]);
        
        shared = WidgetCalculationUtils.shareAsEqualAsPossible(0, new int[] {1, 1});
        assertEquals(2, shared.length);
        assertEquals(0, shared[0]);
        assertEquals(0, shared[1]);
        
        shared = WidgetCalculationUtils.shareAsEqualAsPossible(25, new int[] {1});
        assertEquals(1, shared.length);
        assertEquals(25, shared[0]);
        
        shared = WidgetCalculationUtils.shareAsEqualAsPossible(24, new int[] {1, 1});
        assertEquals(2, shared.length);
        assertEquals(12, shared[0]);
        assertEquals(12, shared[1]);
        
        shared = WidgetCalculationUtils.shareAsEqualAsPossible(25, new int[] {3, 2});
        assertEquals(2, shared.length);
        assertEquals(15, shared[0]);
        assertEquals(10, shared[1]);
        
        shared = WidgetCalculationUtils.shareAsEqualAsPossible(26, new int[] {1, 3, 2});
        assertEquals(3, shared.length);
        assertEquals(5, shared[0]);
        assertEquals(13, shared[1]);
        assertEquals(8, shared[2]);        
    }
}

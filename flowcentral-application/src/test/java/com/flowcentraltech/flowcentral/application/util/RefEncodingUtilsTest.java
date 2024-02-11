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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests reference encoding utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class RefEncodingUtilsTest {

    @Test
    public void encodeRefValue() throws Exception {
        String encoded = RefEncodingUtils.encodeRefValue(0, "master.customer", "1001");
        assertEquals("0:master.customer:1001", encoded);
    }

    @Test
    public void decodeRefValue() throws Exception {
        RefDecodedValue value = RefEncodingUtils.decodeRefValue(null);
        assertNull(value);
        
        value = RefEncodingUtils.decodeRefValue("");
        assertNotNull(value);
        assertEquals(0, value.getIndex());
        assertNull(value.getRefLongName());
        assertEquals("", value.getValue());
        assertFalse(value.isLongNameRef());
       
        value = RefEncodingUtils.decodeRefValue("1:master.customer");
        assertNotNull(value);
        assertEquals(0, value.getIndex());
        assertNull(value.getRefLongName());
        assertEquals("1:master.customer", value.getValue());
        assertFalse(value.isLongNameRef());
        
        value = RefEncodingUtils.decodeRefValue("0:master.customer:1001");
        assertNotNull(value);
        assertEquals(0, value.getIndex());
        assertEquals("master.customer", value.getRefLongName());
        assertEquals("1001", value.getValue());
        assertTrue(value.isLongNameRef());
        
        value = RefEncodingUtils.decodeRefValue("4:account.ledger:2001");
        assertNotNull(value);
        assertEquals(4, value.getIndex());
        assertEquals("account.ledger", value.getRefLongName());
        assertEquals("2001", value.getValue());
        assertTrue(value.isLongNameRef());
    }
}

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

package com.flowcentraltech.flowcentral.studio.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.flowcentraltech.flowcentral.studio.constants.StudioAppComponentType;
import com.flowcentraltech.flowcentral.studio.util.StudioNameUtils.StudioAppletNameParts;

/**
 * Studio name utilities tests.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class StudioNameUtilsTest {

    @Test
    public void testGetStudioAppletName() throws Exception {
        assertEquals("security.apl_45",
                StudioNameUtils.getStudioAppletName("security", StudioAppComponentType.APPLET, 45L));
    }

    @Test
    public void testGetStudioAppletNameParts() throws Exception {
        StudioAppletNameParts nameParts = StudioNameUtils.getStudioAppletNameParts("security.apl_45");
        assertNotNull(nameParts);
        assertEquals("security", nameParts.getApplicationName());
        assertEquals(StudioAppComponentType.APPLET, nameParts.getType());
        assertEquals(Long.valueOf(45L), nameParts.getInstId());
    }
}

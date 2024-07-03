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
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.tcdng.unify.core.UnifyException;

/**
 * Tests HTML utilities class.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class HtmlUtilsTest {

    @Test
    public void testFormatReportHTML() throws UnifyException {
        assertNull(HtmlUtils.formatReportHTML(null));
        assertEquals("", HtmlUtils.formatReportHTML(""));
        assertEquals("&#160;", HtmlUtils.formatReportHTML(" "));
        assertEquals("&#160;&#160;&#160;", HtmlUtils.formatReportHTML("   "));
        assertEquals("&#160;", HtmlUtils.formatReportHTML("&nbsp;"));
   }

    @Test
    public void testFormatEmailHTML() throws UnifyException {
        assertNull(HtmlUtils.formatEmailHTML(null));
        assertEquals("", HtmlUtils.formatEmailHTML(""));
        assertEquals("&", HtmlUtils.formatEmailHTML("&"));
        assertEquals("&amp;", HtmlUtils.formatEmailHTML("&amp;"));
        assertEquals("&lt;", HtmlUtils.formatEmailHTML("&lt;"));
        assertEquals("&gt;", HtmlUtils.formatEmailHTML("&gt;"));
        assertEquals("&nbsp;", HtmlUtils.formatEmailHTML("&nbsp;"));
        assertEquals(" ", HtmlUtils.formatEmailHTML(" "));
        assertEquals("   ", HtmlUtils.formatEmailHTML("   "));
        assertEquals("\n", HtmlUtils.formatEmailHTML("\n"));
        assertEquals("&nbsp;&nbsp;&nbsp;&nbsp;", HtmlUtils.formatEmailHTML("\t"));
    }

}

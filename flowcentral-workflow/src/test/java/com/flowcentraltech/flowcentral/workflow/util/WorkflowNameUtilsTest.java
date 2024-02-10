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

package com.flowcentraltech.flowcentral.workflow.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.flowcentraltech.flowcentral.workflow.util.WorkflowNameUtils.WfAppletNameParts;

/**
 * Workflow name utilities tests.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class WorkflowNameUtilsTest {

    @Test
    public void testGetWfAppletName() throws Exception {
        assertEquals("__wf_security.newUserFlow_approval",
                WorkflowNameUtils.getWfAppletName("security.newUserFlow", "approval"));
    }

    @Test
    public void testGetWfAppletNameParts() throws Exception {
        WfAppletNameParts nameParts = WorkflowNameUtils.getWfAppletNameParts("__wf_security.newUserFlow_approval");
        assertNotNull(nameParts);
        assertEquals("security.newUserFlow", nameParts.getWorkflow());
        assertEquals("approval", nameParts.getStepName());
    }
}

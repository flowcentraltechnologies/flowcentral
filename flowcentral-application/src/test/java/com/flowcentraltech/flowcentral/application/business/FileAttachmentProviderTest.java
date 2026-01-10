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
package com.flowcentraltech.flowcentral.application.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.common.business.FileAttachmentProvider;
import com.flowcentraltech.flowcentral.common.constants.FileAttachmentCategoryType;
import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.entities.FileAttachment;
import com.flowcentraltech.flowcentral.test.AbstractFlowCentralTest;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.data.UploadedFile;

/**
 * Application file attachment provider tests.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class FileAttachmentProviderTest extends AbstractFlowCentralTest {

    private FileAttachmentProvider fileAttachmentProvider;

    @Test
    public void testSychFileAttachments() throws Exception {
        Attachment attachment = Attachment.newBuilder(FileAttachmentType.IMAGE, false).name("disk").title("Disk")
                .file(UploadedFile.create("disk", new byte[] { (byte) 0xbe, (byte) 0xba })).build();
        fileAttachmentProvider.saveFileAttachment(FileAttachmentCategoryType.FORM_CATEGORY, "application.application",
                1L, attachment);
        boolean synched = fileAttachmentProvider.sychFileAttachments(FileAttachmentCategoryType.FORM_CATEGORY,
                "application.application", 2L, 1L);
        assertTrue(synched);
        Attachment synchAttachment = fileAttachmentProvider.retrieveFileAttachment(
                FileAttachmentCategoryType.FORM_CATEGORY, "application.application", 2L, "disk");
        assertNotNull(synchAttachment);
        assertEquals("disk", synchAttachment.getName());
        assertEquals("Disk", synchAttachment.getTitle());
        assertNull(synchAttachment.getFileName());
        final byte[] data = synchAttachment.getFile().getDataAndInvalidate();
        assertNotNull(data);
        assertEquals((byte) 0xbe, data[0]);
        assertEquals((byte) 0xba, data[1]);
    }

    @Test
    public void testSychFileAttachmentsSameInst() throws Exception {
        boolean synched = fileAttachmentProvider.sychFileAttachments(FileAttachmentCategoryType.FORM_CATEGORY,
                "application.application", 1L, 1L);
        assertFalse(synched);
    }

    @Override
    protected void onSetup() throws Exception {
        fileAttachmentProvider = (FileAttachmentProvider) getComponent(
                ApplicationModuleNameConstants.APPLICATION_MODULE_SERVICE);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onTearDown() throws Exception {
        deleteAll(FileAttachment.class);
    }

}

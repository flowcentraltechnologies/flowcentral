/*
 * Copyright (c) 2019, 2021, FlowCentral Technologies.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
 */
package com.flowcentraltech.flowcentral.application.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.common.AbstractFlowCentralTest;
import com.flowcentraltech.flowcentral.common.business.FileAttachmentProvider;
import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.entities.FileAttachment;
import com.tcdng.unify.core.constant.FileAttachmentType;

/**
 * Application file attachment provider tests.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class FileAttachmentProviderTest extends AbstractFlowCentralTest {

    private FileAttachmentProvider fileAttachmentProvider;

    @Test
    public void testSychFileAttachments() throws Exception {
        Attachment attachment = Attachment.newBuilder(FileAttachmentType.IMAGE).name("disk").title("Disk")
                .data(new byte[] { (byte) 0xbe, (byte) 0xba }).build();
        fileAttachmentProvider.saveFileAttachment("work", "application.application", 1L, attachment);
        boolean synched = fileAttachmentProvider.sychFileAttachments("work", "application.application", 2L, 1L);
        assertTrue(synched);
        Attachment synchAttachment = fileAttachmentProvider.retrieveFileAttachment("work", "application.application",
                2L, "disk");
        assertNotNull(synchAttachment);
        assertEquals("disk", synchAttachment.getName());
        assertEquals("Disk", synchAttachment.getTitle());
        assertNull(synchAttachment.getFileName());
        byte[] data = synchAttachment.getData();
        assertNotNull(data);
        assertEquals((byte) 0xbe, data[0]);
        assertEquals((byte) 0xba, data[1]);
    }

    @Test
    public void testSychFileAttachmentsSameInst() throws Exception {
        boolean synched = fileAttachmentProvider.sychFileAttachments("work", "application.application", 1L, 1L);
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

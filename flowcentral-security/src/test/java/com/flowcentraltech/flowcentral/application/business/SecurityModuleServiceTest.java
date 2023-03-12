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
package com.flowcentraltech.flowcentral.application.business;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.flowcentraltech.flowcentral.application.entities.Leave;
import com.flowcentraltech.flowcentral.common.AbstractFlowCentralTest;
import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.NotifRecipientType;
import com.flowcentraltech.flowcentral.notification.business.NotificationModuleService;
import com.flowcentraltech.flowcentral.notification.constants.NotificationModuleNameConstants;
import com.flowcentraltech.flowcentral.notification.data.NotifMessage;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.database.Entity;

/**
 * Security module service tests.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SecurityModuleServiceTest extends AbstractFlowCentralTest {

    private NotificationModuleService nms;

    @Test
    public void testConstructUserCreationApprovalNotificationChannelMessage() throws Exception {
        NotifMessage ncm = nms.constructNotificationChannelMessage(Entity.PRIMARY_TENANT_ID,
                "security.userCreationApproval", new BeanValueStore(new Leave()),
                new Recipient(NotifRecipientType.TO, "Albert Einstien",
                        "albert.einstien@flowcentraltechnologies.com"));
        assertNotNull(ncm);
    }

    @Test
    public void testConstructUserWelcomeNotificationChannelMessage() throws Exception {
        NotifMessage ncm = nms.constructNotificationChannelMessage(Entity.PRIMARY_TENANT_ID,
                "security.userWelcome", new BeanValueStore(new Leave()),
                new Recipient(NotifRecipientType.TO, "Albert Einstien",
                        "albert.einstien@flowcentraltechnologies.com"));
        assertNotNull(ncm);
    }

    @Override
    protected void onSetup() throws Exception {
        nms = (NotificationModuleService) getComponent(NotificationModuleNameConstants.NOTIFICATION_MODULE_SERVICE);
    }

    @Override
    protected void onTearDown() throws Exception {

    }

}

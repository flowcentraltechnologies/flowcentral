/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.notification.jobs;

import com.flowcentraltech.flowcentral.notification.constants.NotificationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.notification.entities.NotificationChannel;
import com.flowcentraltech.flowcentral.notification.entities.NotificationChannelQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.JobFetchInfo;

/**
 * Send notification job handler.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Transactional
@Component("sendnotification-jobhandler")
public class SendNotificationJobHandler extends AbstractNotificationJobHandler {

    @Override
    public void performWork(Long notificationChannelId) throws UnifyException {
        notification().sendNotifications(notificationChannelId);
    }

    @Override
    public void notifyFail(Long notificationChannelId) throws UnifyException {
        environment().updateById(NotificationChannel.class, notificationChannelId,
                new Update().set("sendCheck", false));
    }

    @Override
    protected boolean ready() throws UnifyException {
        return super.ready() && system().getSysParameterValue(boolean.class,
                NotificationModuleSysParamConstants.NOTIFICATION_ENABLED);
    }

    @Override
    protected JobFetchInfo getJobFetchInfo() throws UnifyException {
        return JobFetchInfo.newBuilder()
                .title("Send notifications")
                .claimQuery(new NotificationChannelQuery().readyToSend())
                .claimUpdate(new Update().set("sendCheck", true))
                .fetchBatchSize(4)
                .jobMaxAttempts(4)
                .build();
    }

}

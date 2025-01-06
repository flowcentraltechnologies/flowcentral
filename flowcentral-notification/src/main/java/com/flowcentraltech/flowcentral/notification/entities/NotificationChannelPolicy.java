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

package com.flowcentraltech.flowcentral.notification.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseStatusEntityPolicy;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.constant.FrequencyUnit;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Notification channel policy
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("notificationchannel-entitypolicy")
public class NotificationChannelPolicy extends BaseStatusEntityPolicy {

    @Override
    public Object preCreate(Entity record, Date now) throws UnifyException {
        NotificationChannel notificationChannel = (NotificationChannel) record;
        notificationChannel.setNextTransmissionOn(getNextTransmissionOn(notificationChannel, now));
        return super.preCreate(record, now);
    }

    @Override
    public void preUpdate(Entity record, Date now) throws UnifyException {
        NotificationChannel notificationChannel = (NotificationChannel) record;
        notificationChannel.setNextTransmissionOn(getNextTransmissionOn(notificationChannel, now));
        super.preUpdate(record, now);
    }

    private Date getNextTransmissionOn(NotificationChannel notificationChannel, Date now) throws UnifyException {
        if (DataUtils.convert(int.class, notificationChannel.getMessagesPerMinute()) > 0) {
            return CalendarUtils.getNowWithFrequencyOffset(now, FrequencyUnit.MINUTE, 1);
        }

        return null;
    }
}

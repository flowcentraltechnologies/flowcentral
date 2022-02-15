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

package com.flowcentraltech.flowcentral.common.business;

import java.util.Collection;
import java.util.List;

import com.flowcentraltech.flowcentral.common.data.Recipient;
import com.flowcentraltech.flowcentral.configuration.constants.NotificationType;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Notification recipient provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface NotificationRecipientProvider extends UnifyComponent {

    /**
     * Gets recipient by user login ID.
     * 
     * @param type
     *                    the notification type
     * @param userLoginId
     *                    the user login ID
     * @return the recipient
     * @throws UnifyException
     *                        if an error occurs
     */
    Recipient getRecipientByLoginId(NotificationType type, String userLoginId) throws UnifyException;

    /**
     * Gets notification recipients by role.
     * 
     * @param type
     *                 the notification type
     * @param roleCode
     *                 the role code
     * @return a list of recipients
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Recipient> getRecipientsByRole(NotificationType type, String roleCode) throws UnifyException;

    /**
     * Gets notification recipients by roles.
     * 
     * @param type
     *              the notification type
     * @param roles
     *              the role codes
     * @return a list of recipients
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Recipient> getRecipientsByRole(NotificationType type, Collection<String> roles) throws UnifyException;
}

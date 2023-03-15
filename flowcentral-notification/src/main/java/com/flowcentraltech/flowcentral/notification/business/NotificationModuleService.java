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
package com.flowcentraltech.flowcentral.notification.business;

import java.util.List;

import com.flowcentraltech.flowcentral.common.business.FlowCentralService;
import com.flowcentraltech.flowcentral.notification.data.NotifMessage;
import com.flowcentraltech.flowcentral.notification.data.NotifTemplateDef;
import com.flowcentraltech.flowcentral.notification.data.NotifTemplateWrapper;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplate;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplateQuery;
import com.flowcentraltech.flowcentral.notification.util.DynamicNotifTemplateInfo;
import com.tcdng.unify.core.UnifyException;

/**
 * Notification business service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface NotificationModuleService extends FlowCentralService {

    /**
     * Creates a wrapper instance initialized with a new instance of wrapped
     * template type.
     * 
     * @param wrapperType
     *                    the wrapper type
     * @return the wrapper instance
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends NotifTemplateWrapper> T wrapperOf(Class<T> wrapperType) throws UnifyException;

    /**
     * Generates dynamic notification template information.
     * 
     * @param basePackage
     *                    the base package
     * @param moduleName
     *                    the module name
     * @return list of template infos
     * @throws UnifyException
     *                        if an error occurs
     */
    List<DynamicNotifTemplateInfo> generateNotifTemplateInfos(String basePackage, String moduleName)
            throws UnifyException;
    
    /**
     * Finds notification templates.
     * 
     * @param query
     *              the notification template query
     * @return list of notification templates
     * @throws UnifyException
     *                        if an error occurs
     */
    List<NotificationTemplate> findNotificationTemplates(NotificationTemplateQuery query) throws UnifyException;

    /**
     * Finds notification template by ID.
     * 
     * @param notifTemplateId
     *                the notification template ID
     * @return the notification template
     * @throws UnifyException
     *                        if notification template with ID is not found. If an error occurs
     */
    NotificationTemplate findNotificationTemplate(Long notifTemplateId) throws UnifyException;

    /**
     * Finds notification template ID list for application.
     * 
     * @param applicationName
     *                        the application name
     * @return list of application notification template IDs
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Long> findNotificationTemplateIdList(String applicationName) throws UnifyException;

    /**
     * Get notification template definition.
     * 
     * @param templateName
     *                     the template name
     * @return the template definition
     * @throws UnifyException
     *                        if an error occurs
     */
    NotifTemplateDef getNotifTemplateDef(String templateName) throws UnifyException;
    
    /**
     * Sends a notification. This is an asynchronous call where message is pushed
     * into communication system that is left to do actual notification
     * transmission.
     * 
     * @param notifMessage
     *                            the message to send
     * @throws UnifyException
     *                        if an error occurs
     */
    void sendNotification(NotifMessage notifMessage) throws UnifyException;
}

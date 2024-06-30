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
package com.flowcentraltech.flowcentral.notification.business;

import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.common.business.FlowCentralService;
import com.flowcentraltech.flowcentral.notification.data.NotifLargeTextDef;
import com.flowcentraltech.flowcentral.notification.data.NotifLargeTextWrapper;
import com.flowcentraltech.flowcentral.notification.data.NotifMessage;
import com.flowcentraltech.flowcentral.notification.data.NotifTemplateDef;
import com.flowcentraltech.flowcentral.notification.data.NotifTemplateWrapper;
import com.flowcentraltech.flowcentral.notification.entities.NotificationLargeText;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplate;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplateQuery;
import com.flowcentraltech.flowcentral.notification.util.DynamicNotifLargeTextInfo;
import com.flowcentraltech.flowcentral.notification.util.DynamicNotifTemplateInfo;
import com.flowcentraltech.flowcentral.notification.util.NotifLargeTextInfo;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.Listable;

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
    <T extends NotifTemplateWrapper> T wrapperOfNotifTemplate(Class<T> wrapperType) throws UnifyException;

    /**
     * Creates a wrapper instance initialized with a new instance of wrapped large
     * text type.
     * 
     * @param wrapperType
     *                    the wrapper type
     * @return the wrapper instance
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends NotifLargeTextWrapper> T wrapperOfNotifLargeText(Class<T> wrapperType) throws UnifyException;

    /**
     * Creates a wrapper instance initialized with a new instance of wrapped large
     * text type.
     * 
     * @param wrapperType
     *                    the wrapper type
     * @param parameters
     *                    the parameters
     * @return the wrapper instance
     * @throws UnifyException
     *                        if an error occurs
     */
    <T extends NotifLargeTextWrapper> T wrapperOfNotifLargeText(Class<T> wrapperType, Map<String, Object> parameters)
            throws UnifyException;

    /**
     * Counts notification templates by module
     * 
     * @param moduleName
     *                   the module name
     * @return the template count
     * @throws UnifyException
     *                        if an error occurs
     */
    int countNotifTemplatesByModule(String moduleName) throws UnifyException;

    /**
     * Counts notification large texts by module
     * 
     * @param moduleName
     *                   the module name
     * @return the large text count
     * @throws UnifyException
     *                        if an error occurs
     */
    int countNotifLargeTextsByModule(String moduleName) throws UnifyException;

    /**
     * Gets entity notification large texts.
     * 
     * @param entityName
     *                   the entity name
     * @return the information list
     * @throws UnifyException
     *                        if an error occurs
     */
    List<NotifLargeTextInfo> getEntityNotifLargeTexts(String entityName) throws UnifyException;

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
     * Generates dynamic notification large text information.
     * 
     * @param basePackage
     *                    the base package
     * @param moduleName
     *                    the module name
     * @return list of large text infos
     * @throws UnifyException
     *                        if an error occurs
     */
    List<DynamicNotifLargeTextInfo> generateNotifLargeTextInfos(String basePackage, String moduleName)
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
     * Finds notification templates by application ID.
     * 
     * @param applicationId
     *                      the application ID
     * @return the list of notification templates
     * @throws UnifyException
     *                        if an error occurs
     */
    List<? extends Listable> findNotificationTemplatesByApplicationId(Long applicationId) throws UnifyException;

    /**
     * Finds notification template by ID.
     * 
     * @param notifTemplateId
     *                        the notification template ID
     * @return the notification template
     * @throws UnifyException
     *                        if notification template with ID is not found. If an
     *                        error occurs
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
    List<Long> findCustomNotificationTemplateIdList(String applicationName) throws UnifyException;

    /**
     * Finds notification large text by ID.
     * 
     * @param notifTemplateId
     *                        the notification large text ID
     * @return the notification large text
     * @throws UnifyException
     *                        if notification large text with ID is not found. If an
     *                        error occurs
     */
    NotificationLargeText findNotificationLargeText(Long notifLargeTextId) throws UnifyException;

    /**
     * Finds notification large text ID list for application.
     * 
     * @param applicationName
     *                        the application name
     * @return list of application notification template IDs
     * @throws UnifyException
     *                        if an error occurs
     */
    List<Long> findCustomNotificationLargeTextIdList(String applicationName) throws UnifyException;

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
     * Get notification large text definition.
     * 
     * @param largeTextName
     *                      the large text name
     * @return the large text definition
     * @throws UnifyException
     *                        if an error occurs
     */
    NotifLargeTextDef getNotifLargeTextDef(String largeTextName) throws UnifyException;

    /**
     * Sends a notification. This is an asynchronous call where message is pushed
     * into communication system that is left to do actual notification
     * transmission.
     * 
     * @param notifMessage
     *                     the message to send
     * @throws UnifyException
     *                        if an error occurs
     */
    void sendNotification(NotifMessage notifMessage) throws UnifyException;

}

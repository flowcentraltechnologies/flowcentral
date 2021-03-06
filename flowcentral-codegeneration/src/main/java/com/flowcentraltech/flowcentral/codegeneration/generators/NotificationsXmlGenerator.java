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

package com.flowcentraltech.flowcentral.codegeneration.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import com.flowcentraltech.flowcentral.configuration.xml.AppNotifTemplateConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppNotifTemplatesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.NotifTemplateConfig;
import com.flowcentraltech.flowcentral.configuration.xml.util.ConfigurationUtils;
import com.flowcentraltech.flowcentral.notification.business.NotificationModuleService;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplate;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.NameUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Notifications XML Generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("notifications-xml-generator")
public class NotificationsXmlGenerator extends AbstractStaticArtifactGenerator {

    private static final String NOTIFICATION_FOLDER = "apps/notification/";

    @Configurable
    private NotificationModuleService notificationModuleService;

    public NotificationsXmlGenerator() {
        super("src/main/resources/apps/notification/");
    }

    public void setNotificationModuleService(NotificationModuleService notificationModuleService) {
        this.notificationModuleService = notificationModuleService;
    }

    @Override
    protected void doGenerate(ExtensionModuleStaticFileBuilderContext ctx, String applicationName, ZipOutputStream zos)
            throws UnifyException {
        List<Long> notifTemplateIdList = notificationModuleService.findNotificationTemplateIdList(applicationName);
        if (!DataUtils.isBlank(notifTemplateIdList)) {
            final String lowerCaseApplicationName = applicationName.toLowerCase();

            AppNotifTemplatesConfig notifTemplatesConfig = new AppNotifTemplatesConfig();
            List<AppNotifTemplateConfig> notifTemplateList = new ArrayList<AppNotifTemplateConfig>();
            for (Long notifTemplateId : notifTemplateIdList) {
                AppNotifTemplateConfig appNotifTemplateConfig = new AppNotifTemplateConfig();
                NotificationTemplate notifTemplate = notificationModuleService.findNotificationTemplate(notifTemplateId);
                final String filename = StringUtils.dashen(NameUtils.describeName(notifTemplate.getName())) + ".xml";
                openEntry(filename, zos);
                
                NotifTemplateConfig notifTemplateConfig = new NotifTemplateConfig();
                String descKey = getDescriptionKey(lowerCaseApplicationName, "notification", notifTemplate.getName());
                String subjectKey = descKey + ".subject";
                String bodyKey = descKey + ".body";
                ctx.addMessage(StaticMessageCategoryType.NOTIFICATION, descKey, notifTemplate.getDescription());
                ctx.addMessage(StaticMessageCategoryType.NOTIFICATION, subjectKey, notifTemplate.getSubject());
                ctx.addMessage(StaticMessageCategoryType.NOTIFICATION, bodyKey, notifTemplate.getTemplate());

                notifTemplateConfig.setNotificationType(notifTemplate.getNotificationType());
                notifTemplateConfig.setMessageFormat(notifTemplate.getMessageFormat());
                notifTemplateConfig.setName(notifTemplate.getName());
                notifTemplateConfig.setDescription("$m{" + descKey + "}");
                notifTemplateConfig.setEntity(notifTemplate.getEntity());
                notifTemplateConfig.setSubject("$m{" + subjectKey + "}");
                notifTemplateConfig.setBody("$m{" + bodyKey + "}");
                notifTemplateConfig.setAttachmentGenerator(notifTemplate.getAttachmentGenerator());

                ConfigurationUtils.writeConfigNoEscape(notifTemplateConfig, zos);
                closeEntry(zos);
  
                appNotifTemplateConfig.setConfigFile(NOTIFICATION_FOLDER + filename);
                notifTemplateList.add(appNotifTemplateConfig);
            }

            notifTemplatesConfig.setNotifTemplateList(notifTemplateList);
            ctx.setNotifTemplatesConfig(notifTemplatesConfig);
        }

    }

}

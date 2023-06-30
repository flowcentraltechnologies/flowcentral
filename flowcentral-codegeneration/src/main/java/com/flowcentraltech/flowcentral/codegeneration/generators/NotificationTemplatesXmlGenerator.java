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

package com.flowcentraltech.flowcentral.codegeneration.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import com.flowcentraltech.flowcentral.configuration.xml.AppNotifTemplateConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppNotifTemplatesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.NotifTemplateConfig;
import com.flowcentraltech.flowcentral.configuration.xml.NotifTemplateParamConfig;
import com.flowcentraltech.flowcentral.configuration.xml.util.ConfigurationUtils;
import com.flowcentraltech.flowcentral.notification.business.NotificationModuleService;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplate;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplateParam;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.NameUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Notification templates XML Generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("notification-templates-xml-generator")
public class NotificationTemplatesXmlGenerator extends AbstractStaticArtifactGenerator {

    private static final String NOTIFICATION_TEMPLATE_FOLDER = "apps/notification/template/";

    @Configurable
    private NotificationModuleService notificationModuleService;

    public NotificationTemplatesXmlGenerator() {
        super("src/main/resources/apps/notification/template/");
    }

    public void setNotificationModuleService(NotificationModuleService notificationModuleService) {
        this.notificationModuleService = notificationModuleService;
    }

    @Override
    protected void doGenerate(ExtensionModuleStaticFileBuilderContext ctx, String applicationName, ZipOutputStream zos)
            throws UnifyException {
        // Templates
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
                ctx.addMessage(StaticMessageCategoryType.NOTIFICATION, descKey, notifTemplate.getDescription());

                notifTemplateConfig.setNotifType(notifTemplate.getNotificationType());
                notifTemplateConfig.setMessageFormat(notifTemplate.getMessageFormat());
                notifTemplateConfig.setName(notifTemplate.getName());
                notifTemplateConfig.setDescription("$m{" + descKey + "}");
                notifTemplateConfig.setEntity(notifTemplate.getEntity());
                notifTemplateConfig.setSubject(notifTemplate.getSubject());
                notifTemplateConfig.setBody(notifTemplate.getTemplate());

                List<NotifTemplateParamConfig> paramList = new ArrayList<NotifTemplateParamConfig>();
                for (NotificationTemplateParam param: notifTemplate.getParamList()) {
                    NotifTemplateParamConfig pConfig = new NotifTemplateParamConfig();
                    pConfig.setName(param.getName());
                    pConfig.setLabel(param.getLabel());
                    paramList.add(pConfig);
                }
                
                notifTemplateConfig.setParamList(paramList);
                
                ConfigurationUtils.writeConfigNoEscape(notifTemplateConfig, zos);
                closeEntry(zos);
  
                appNotifTemplateConfig.setConfigFile(NOTIFICATION_TEMPLATE_FOLDER + filename);
                notifTemplateList.add(appNotifTemplateConfig);
            }

            notifTemplatesConfig.setNotifTemplateList(notifTemplateList);
            ctx.setNotifTemplatesConfig(notifTemplatesConfig);
        }
     }

}

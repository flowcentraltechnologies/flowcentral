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

package com.flowcentraltech.flowcentral.codegeneration.generators;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipOutputStream;

import com.flowcentraltech.flowcentral.configuration.xml.AppNotifLargeTextConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppNotifLargeTextsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.NotifLargeTextConfig;
import com.flowcentraltech.flowcentral.configuration.xml.NotifLargeTextParamConfig;
import com.flowcentraltech.flowcentral.configuration.xml.util.ConfigurationUtils;
import com.flowcentraltech.flowcentral.notification.business.NotificationModuleService;
import com.flowcentraltech.flowcentral.notification.entities.NotificationLargeText;
import com.flowcentraltech.flowcentral.notification.entities.NotificationLargeTextParam;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.NameUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Notification large texts XML Generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("notification-largetexts-xml-generator")
public class NotificationLargeTextsXmlGenerator extends AbstractResourcesArtifactGenerator {

    private static final String NOTIFICATION_LARGETEXT_FOLDER = "apps/notification/largetext/";

    @Configurable
    private NotificationModuleService notificationModuleService;

    public NotificationLargeTextsXmlGenerator() { 
        super(NOTIFICATION_LARGETEXT_FOLDER);
    }

    @Override
    protected void doGenerate(ExtensionModuleStaticFileBuilderContext ctx, String applicationName, ZipOutputStream zos)
            throws UnifyException {
        // Large texts
        List<Long> notifLargeTextIdList = notificationModuleService.findCustomNotificationLargeTextIdList(applicationName);
        if (!DataUtils.isBlank(notifLargeTextIdList)) {
            final String lowerCaseApplicationName = applicationName.toLowerCase();

            AppNotifLargeTextsConfig notifLargeTextsConfig = new AppNotifLargeTextsConfig();
            List<AppNotifLargeTextConfig> notifLargeTextList = new ArrayList<AppNotifLargeTextConfig>();
            for (Long notifLargeTextId : notifLargeTextIdList) {
                AppNotifLargeTextConfig appNotifLargeTextConfig = new AppNotifLargeTextConfig();
                NotificationLargeText notifLargeText = notificationModuleService
                        .findNotificationLargeText(notifLargeTextId);
                final String filename = StringUtils.dashen(NameUtils.describeName(notifLargeText.getName())) + ".xml";
                openEntry(ctx, filename, zos);

                NotifLargeTextConfig notifLargeTextConfig = new NotifLargeTextConfig();
                String descKey = getDescriptionKey(lowerCaseApplicationName, "notification", notifLargeText.getName());
                ctx.addMessage(StaticMessageCategoryType.NOTIFICATION, descKey, notifLargeText.getDescription());

                notifLargeTextConfig.setName(notifLargeText.getName());
                notifLargeTextConfig.setDescription("$m{" + descKey + "}");
                notifLargeTextConfig.setEntity(notifLargeText.getEntity());
                notifLargeTextConfig.setFontFamily(notifLargeText.getFontFamily());
                notifLargeTextConfig.setFontSizeInPixels(notifLargeText.getFontSizeInPixels());
                notifLargeTextConfig.setBody(notifLargeText.getBody());

                List<NotifLargeTextParamConfig> paramList = new ArrayList<NotifLargeTextParamConfig>();
                for (NotificationLargeTextParam param : notifLargeText.getParamList()) {
                    NotifLargeTextParamConfig pConfig = new NotifLargeTextParamConfig();
                    pConfig.setName(param.getName());
                    pConfig.setLabel(param.getLabel());
                    paramList.add(pConfig);
                }

                notifLargeTextConfig.setParamList(paramList);

                ConfigurationUtils.writeConfigNoEscape(notifLargeTextConfig, zos);
                closeEntry(zos);

                appNotifLargeTextConfig.setConfigFile(NOTIFICATION_LARGETEXT_FOLDER + filename);
                notifLargeTextList.add(appNotifLargeTextConfig);
            }

            notifLargeTextsConfig.setNotifLargeTextList(notifLargeTextList);
            ctx.setNotifLargeTextsConfig(notifLargeTextsConfig);
        }
    }

}

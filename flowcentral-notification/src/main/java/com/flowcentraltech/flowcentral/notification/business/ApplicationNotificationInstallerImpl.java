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

package com.flowcentraltech.flowcentral.notification.business;

import java.util.Arrays;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AbstractApplicationArtifactInstaller;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationReplicationContext;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.common.util.ConfigUtils;
import com.flowcentraltech.flowcentral.configuration.data.ApplicationInstall;
import com.flowcentraltech.flowcentral.configuration.data.NotifTemplateInstall;
import com.flowcentraltech.flowcentral.configuration.xml.AppConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppNotifTemplateConfig;
import com.flowcentraltech.flowcentral.configuration.xml.NotifTemplateConfig;
import com.flowcentraltech.flowcentral.notification.constants.NotificationModuleNameConstants;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplate;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplateQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Application notification installer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(NotificationModuleNameConstants.APPLICATION_NOTIFICATION_INSTALLER)
public class ApplicationNotificationInstallerImpl extends AbstractApplicationArtifactInstaller {

    @Override
    public void installApplicationArtifacts(final TaskMonitor taskMonitor, final ApplicationInstall applicationInstall)
            throws UnifyException {
        final AppConfig applicationConfig = applicationInstall.getApplicationConfig();
        final Long applicationId = applicationInstall.getApplicationId();

        logDebug(taskMonitor, "Executing notification installer...");
        // Install configured notification templates
        if (applicationConfig.getNotifTemplatesConfig() != null
                && !DataUtils.isBlank(applicationConfig.getNotifTemplatesConfig().getNotifTemplateList())) {
            for (AppNotifTemplateConfig applicationNotifTemplateConfig : applicationConfig.getNotifTemplatesConfig()
                    .getNotifTemplateList()) {
                NotifTemplateInstall notifTemplateInstall = getConfigurationLoader()
                        .loadNotifTemplateInstallation(applicationNotifTemplateConfig.getConfigFile());
                NotifTemplateConfig notifTemplateConfig = notifTemplateInstall.getNotifTemplateConfig();
                String description = resolveApplicationMessage(notifTemplateConfig.getDescription());
                String entity = ApplicationNameUtils.ensureLongNameReference(applicationConfig.getName(),
                        notifTemplateConfig.getEntity());
                String subject = resolveApplicationMessage(notifTemplateConfig.getSubject());
                String body = resolveApplicationMessage(notifTemplateConfig.getBody());
                logDebug(taskMonitor, "Installing configured notification template [{0}]...", description);

                NotificationTemplate oldNotificationTemplate = environment().findLean(new NotificationTemplateQuery()
                        .applicationId(applicationId).name(notifTemplateConfig.getName()));

                if (oldNotificationTemplate == null) {
                    NotificationTemplate notificationTemplate = new NotificationTemplate();
                    notificationTemplate.setApplicationId(applicationId);
                    notificationTemplate.setNotificationType(notifTemplateConfig.getNotificationType());
                    notificationTemplate.setMessageFormat(notifTemplateConfig.getMessageFormat());
                    notificationTemplate.setName(notifTemplateConfig.getName());
                    notificationTemplate.setDescription(description);
                    notificationTemplate.setEntity(entity);
                    notificationTemplate.setSubject(subject);
                    notificationTemplate.setTemplate(body);
                    notificationTemplate.setAttachmentGenerator(notifTemplateConfig.getAttachmentGenerator());
                    notificationTemplate.setConfigType(ConfigType.MUTABLE_INSTALL);
                    environment().create(notificationTemplate);
                } else {
                    if (ConfigUtils.isSetInstall(oldNotificationTemplate)) {
                        oldNotificationTemplate.setNotificationType(notifTemplateConfig.getNotificationType());
                        oldNotificationTemplate.setMessageFormat(notifTemplateConfig.getMessageFormat());
                        oldNotificationTemplate.setDescription(description);
                        oldNotificationTemplate.setEntity(entity);
                        oldNotificationTemplate.setSubject(subject);
                        oldNotificationTemplate.setTemplate(body);
                        oldNotificationTemplate.setAttachmentGenerator(notifTemplateConfig.getAttachmentGenerator());
                        environment().updateByIdVersion(oldNotificationTemplate);
                    }
                }
            }
        }
    }

    @Override
    public void replicateApplicationArtifacts(TaskMonitor taskMonitor, Long srcApplicationId, Long destApplicationId,
            ApplicationReplicationContext ctx) throws UnifyException {
        // Notification Templates
        logDebug(taskMonitor, "Replicating notification templates...");
        List<Long> templateIdList = environment().valueList(Long.class, "id",
                new NotificationTemplateQuery().applicationId(srcApplicationId));
        for (Long templateId : templateIdList) {
            NotificationTemplate srcNotificationTemplate = environment().find(NotificationTemplate.class, templateId);
            String oldDescription = srcNotificationTemplate.getDescription();
            srcNotificationTemplate.setApplicationId(destApplicationId);
            srcNotificationTemplate.setDescription(ctx.messageSwap(srcNotificationTemplate.getDescription()));
            srcNotificationTemplate.setEntity(ctx.entitySwap(srcNotificationTemplate.getEntity()));
            srcNotificationTemplate.setAttachmentGenerator(ctx.componentSwap(srcNotificationTemplate.getAttachmentGenerator()));

            environment().create(srcNotificationTemplate);
            logDebug(taskMonitor, "Notification template [{0}] -> [{1}]...", oldDescription, srcNotificationTemplate.getDescription());
        }
    }

    @Override
    protected List<DeletionParams> getDeletionParams() throws UnifyException {
        return Arrays.asList(new DeletionParams("notification templates", new NotificationTemplateQuery()));
    }

}

/*
 * Copyright (c) 2019, 2021, FlowCentral Technologies.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
 */

package com.flowcentraltech.flowcentral.notification.business;

import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.common.business.AbstractApplicationArtifactInstaller;
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
 * @author Lateef Ojulari
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

}

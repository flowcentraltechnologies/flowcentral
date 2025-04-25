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

package com.flowcentraltech.flowcentral.notification.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AbstractApplicationArtifactInstaller;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationReplicationContext;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.configuration.data.ApplicationInstall;
import com.flowcentraltech.flowcentral.configuration.data.ApplicationRestore;
import com.flowcentraltech.flowcentral.configuration.data.NotifLargeTextInstall;
import com.flowcentraltech.flowcentral.configuration.data.NotifLargeTextRestore;
import com.flowcentraltech.flowcentral.configuration.data.NotifTemplateInstall;
import com.flowcentraltech.flowcentral.configuration.data.NotifTemplateRestore;
import com.flowcentraltech.flowcentral.configuration.xml.AppConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppNotifLargeTextConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppNotifTemplateConfig;
import com.flowcentraltech.flowcentral.configuration.xml.NotifLargeTextConfig;
import com.flowcentraltech.flowcentral.configuration.xml.NotifLargeTextParamConfig;
import com.flowcentraltech.flowcentral.configuration.xml.NotifTemplateConfig;
import com.flowcentraltech.flowcentral.configuration.xml.NotifTemplateParamConfig;
import com.flowcentraltech.flowcentral.notification.constants.NotificationModuleNameConstants;
import com.flowcentraltech.flowcentral.notification.entities.NotificationLargeText;
import com.flowcentraltech.flowcentral.notification.entities.NotificationLargeTextParam;
import com.flowcentraltech.flowcentral.notification.entities.NotificationLargeTextQuery;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplate;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplateParam;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplateQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Application notification installer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
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
        environment().updateAll(new NotificationTemplateQuery().applicationId(applicationId).isStatic(),
                new Update().add("deprecated", Boolean.TRUE));
        if (applicationConfig.getNotifTemplatesConfig() != null
                && !DataUtils.isBlank(applicationConfig.getNotifTemplatesConfig().getNotifTemplateList())) {
            for (AppNotifTemplateConfig applicationNotifTemplateConfig : applicationConfig.getNotifTemplatesConfig()
                    .getNotifTemplateList()) {
                NotifTemplateInstall notifTemplateInstall = getConfigurationLoader()
                        .loadNotifTemplateInstallation(applicationNotifTemplateConfig.getConfigFile());
                // Template
                NotifTemplateConfig notifTemplateConfig = notifTemplateInstall.getNotifTemplateConfig();
                String description = resolveApplicationMessage(notifTemplateConfig.getDescription());
                String entity = ApplicationNameUtils.ensureLongNameReference(applicationConfig.getName(),
                        notifTemplateConfig.getEntity());
                logDebug(taskMonitor, "Installing configured notification template [{0}]...", description);

                NotificationTemplate oldNotificationTemplate = environment().findLean(new NotificationTemplateQuery()
                        .applicationId(applicationId).name(notifTemplateConfig.getName()));

                if (oldNotificationTemplate == null) {
                    NotificationTemplate notificationTemplate = new NotificationTemplate();
                    notificationTemplate.setApplicationId(applicationId);
                    notificationTemplate.setNotificationType(notifTemplateConfig.getNotifType());
                    notificationTemplate.setMessageFormat(notifTemplateConfig.getMessageFormat());
                    notificationTemplate.setName(notifTemplateConfig.getName());
                    notificationTemplate.setDescription(description);
                    notificationTemplate.setEntity(entity);
                    notificationTemplate.setSubject(notifTemplateConfig.getSubject());
                    notificationTemplate.setTemplate(notifTemplateConfig.getBody());
                    notificationTemplate.setDeprecated(false);
                    notificationTemplate.setConfigType(ConfigType.STATIC);
                    populateChildList(notificationTemplate, notifTemplateConfig);
                    environment().create(notificationTemplate);
                } else {
                    oldNotificationTemplate.setNotificationType(notifTemplateConfig.getNotifType());
                    oldNotificationTemplate.setMessageFormat(notifTemplateConfig.getMessageFormat());
                    oldNotificationTemplate.setDescription(description);
                    oldNotificationTemplate.setEntity(entity);
                    oldNotificationTemplate.setSubject(notifTemplateConfig.getSubject());
                    oldNotificationTemplate.setTemplate(notifTemplateConfig.getBody());
                    oldNotificationTemplate.setDeprecated(false);
                    oldNotificationTemplate.setConfigType(ConfigType.STATIC);
                    populateChildList(oldNotificationTemplate, notifTemplateConfig);
                    environment().updateByIdVersion(oldNotificationTemplate);
                }
            }
        }

        // Install configured notification large texts
        environment().updateAll(new NotificationLargeTextQuery().applicationId(applicationId).isStatic(),
                new Update().add("deprecated", Boolean.TRUE));
        if (applicationConfig.getNotifLargeTextsConfig() != null
                && !DataUtils.isBlank(applicationConfig.getNotifLargeTextsConfig().getNotifLargeTextList())) {
            for (AppNotifLargeTextConfig applicationNotifLargeTextConfig : applicationConfig.getNotifLargeTextsConfig()
                    .getNotifLargeTextList()) {
                logDebug(taskMonitor, "Reading notification configuration file [{0}]...",
                        applicationNotifLargeTextConfig.getConfigFile());
                NotifLargeTextInstall notifLargeTextInstall = getConfigurationLoader()
                        .loadNotifLargeTextInstallation(applicationNotifLargeTextConfig.getConfigFile());
                // Large Text
                NotifLargeTextConfig notifLargeTextConfig = notifLargeTextInstall.getNotifLargeTextConfig();
                String description = resolveApplicationMessage(notifLargeTextConfig.getDescription());
                String entity = ApplicationNameUtils.ensureLongNameReference(applicationConfig.getName(),
                        notifLargeTextConfig.getEntity());
                logDebug(taskMonitor, "Installing configured notification large text [{0}]...", description);

                NotificationLargeText oldNotificationLargeText = environment().findLean(new NotificationLargeTextQuery()
                        .applicationId(applicationId).name(notifLargeTextConfig.getName()));
                if (oldNotificationLargeText == null) {
                    NotificationLargeText notificationLargeText = new NotificationLargeText();
                    notificationLargeText.setApplicationId(applicationId);
                    notificationLargeText.setName(notifLargeTextConfig.getName());
                    notificationLargeText.setDescription(description);
                    notificationLargeText.setEntity(entity);
                    notificationLargeText.setFontFamily(notifLargeTextConfig.getFontFamily());
                    notificationLargeText.setFontSizeInPixels(notifLargeTextConfig.getFontSizeInPixels());
                    notificationLargeText.setBody(notifLargeTextConfig.getBody());
                    notificationLargeText.setDeprecated(false);
                    notificationLargeText.setConfigType(ConfigType.STATIC);
                    populateChildList(notificationLargeText, notifLargeTextConfig);
                    environment().create(notificationLargeText);
                } else {
                    oldNotificationLargeText.setDescription(description);
                    oldNotificationLargeText.setEntity(entity);
                    oldNotificationLargeText.setFontFamily(notifLargeTextConfig.getFontFamily());
                    oldNotificationLargeText.setFontSizeInPixels(notifLargeTextConfig.getFontSizeInPixels());
                    oldNotificationLargeText.setBody(notifLargeTextConfig.getBody());
                    oldNotificationLargeText.setDeprecated(false);
                    oldNotificationLargeText.setConfigType(ConfigType.STATIC);
                    populateChildList(oldNotificationLargeText, notifLargeTextConfig);
                    environment().updateByIdVersion(oldNotificationLargeText);
                }
            }
        }

    }

    @Override
    public void restoreCustomApplicationArtifacts(TaskMonitor taskMonitor, ApplicationRestore applicationRestore)
            throws UnifyException {
        final AppConfig applicationConfig = applicationRestore.getApplicationConfig();
        final Long applicationId = applicationRestore.getApplicationId();

        // Notification templates
        logDebug(taskMonitor, "Executing notification restore...");
        if (!DataUtils.isBlank(applicationRestore.getNotifTemplateList())) {
            for (NotifTemplateRestore notifTemplateRestore : applicationRestore.getNotifTemplateList()) {
                // Template
                NotifTemplateConfig notifTemplateConfig = notifTemplateRestore.getNotifTemplateConfig();
                String description = resolveApplicationMessage(notifTemplateConfig.getDescription());
                String entity = ApplicationNameUtils.ensureLongNameReference(applicationConfig.getName(),
                        notifTemplateConfig.getEntity());
                logDebug(taskMonitor, "Restoring configured notification template [{0}]...", description);

                NotificationTemplate notificationTemplate = new NotificationTemplate();
                notificationTemplate.setApplicationId(applicationId);
                notificationTemplate.setNotificationType(notifTemplateConfig.getNotifType());
                notificationTemplate.setMessageFormat(notifTemplateConfig.getMessageFormat());
                notificationTemplate.setName(notifTemplateConfig.getName());
                notificationTemplate.setDescription(description);
                notificationTemplate.setEntity(entity);
                notificationTemplate.setSubject(notifTemplateConfig.getSubject());
                notificationTemplate.setTemplate(notifTemplateConfig.getBody());
                notificationTemplate.setDeprecated(false);
                notificationTemplate.setConfigType(ConfigType.CUSTOM);
                populateChildList(notificationTemplate, notifTemplateConfig);
                environment().create(notificationTemplate);
            }
        }

        // Notification large texts
        if (!DataUtils.isBlank(applicationRestore.getNotifLargeTextList())) {
            for (NotifLargeTextRestore notifLargeTextRestore : applicationRestore.getNotifLargeTextList()) {
                // Large Text
                NotifLargeTextConfig notifLargeTextConfig = notifLargeTextRestore.getNotifLargeTextConfig();
                String description = resolveApplicationMessage(notifLargeTextConfig.getDescription());
                String entity = ApplicationNameUtils.ensureLongNameReference(applicationConfig.getName(),
                        notifLargeTextConfig.getEntity());
                logDebug(taskMonitor, "Restoring notification large text [{0}]...", description);

                NotificationLargeText notificationLargeText = new NotificationLargeText();
                notificationLargeText.setApplicationId(applicationId);
                notificationLargeText.setName(notifLargeTextConfig.getName());
                notificationLargeText.setDescription(description);
                notificationLargeText.setEntity(entity);
                notificationLargeText.setFontFamily(notifLargeTextConfig.getFontFamily());
                notificationLargeText.setFontSizeInPixels(notifLargeTextConfig.getFontSizeInPixels());
                notificationLargeText.setBody(notifLargeTextConfig.getBody());
                notificationLargeText.setDeprecated(false);
                notificationLargeText.setConfigType(ConfigType.CUSTOM);
                populateChildList(notificationLargeText, notifLargeTextConfig);
                environment().create(notificationLargeText);
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
            srcNotificationTemplate.setId(null);
            srcNotificationTemplate.setApplicationId(destApplicationId);
            srcNotificationTemplate.setName(ctx.nameSwap(srcNotificationTemplate.getName()));
            srcNotificationTemplate.setDescription(ctx.messageSwap(srcNotificationTemplate.getDescription()));
            srcNotificationTemplate.setEntity(ctx.entitySwap(srcNotificationTemplate.getEntity()));
            srcNotificationTemplate.setDeprecated(false);
            srcNotificationTemplate.setConfigType(ConfigType.CUSTOM);
            environment().create(srcNotificationTemplate);
            logDebug(taskMonitor, "Notification template [{0}] -> [{1}]...", oldDescription,
                    srcNotificationTemplate.getDescription());
        }
    }

    @Override
    protected List<DeletionParams> getDeletionParams() throws UnifyException {
        return Arrays.asList(new DeletionParams("notification templates", new NotificationTemplateQuery()),
                new DeletionParams("notification large texts", new NotificationLargeTextQuery()));
    }

    private void populateChildList(NotificationTemplate notificationTemplate, NotifTemplateConfig notifTemplateConfig)
            throws UnifyException {
        List<NotificationTemplateParam> paramList = null;
        if (!DataUtils.isBlank(notifTemplateConfig.getParamList())) {
            paramList = new ArrayList<NotificationTemplateParam>();
            for (NotifTemplateParamConfig paramConfig : notifTemplateConfig.getParamList()) {
                NotificationTemplateParam param = new NotificationTemplateParam();
                param.setName(paramConfig.getName());
                param.setLabel(paramConfig.getLabel());
                paramList.add(param);
            }
        }

        notificationTemplate.setParamList(paramList);
    }

    private void populateChildList(NotificationLargeText notificationLargeText,
            NotifLargeTextConfig notifLargeTextConfig) throws UnifyException {
        List<NotificationLargeTextParam> paramList = null;
        if (!DataUtils.isBlank(notifLargeTextConfig.getParamList())) {
            paramList = new ArrayList<NotificationLargeTextParam>();
            for (NotifLargeTextParamConfig paramConfig : notifLargeTextConfig.getParamList()) {
                NotificationLargeTextParam param = new NotificationLargeTextParam();
                param.setName(paramConfig.getName());
                param.setLabel(paramConfig.getLabel());
                paramList.add(param);
            }
        }

        notificationLargeText.setParamList(paramList);
    }

}

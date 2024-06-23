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

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.UsageProvider;
import com.flowcentraltech.flowcentral.application.data.Usage;
import com.flowcentraltech.flowcentral.application.data.UsageType;
import com.flowcentraltech.flowcentral.common.business.AbstractFlowCentralService;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.notification.constants.NotificationModuleNameConstants;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplate;
import com.flowcentraltech.flowcentral.notification.entities.NotificationTemplateQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Transactional;

/**
 * Default implementation of notification usage service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Transactional
@Component(NotificationModuleNameConstants.NOTIFICATION_USAGE_SERVICE)
public class NotificationUsageServiceImpl extends AbstractFlowCentralService implements UsageProvider {
    
    @Override
    public void clearDefinitionsCache() throws UnifyException {

    }

    @Override
    public List<Usage> findApplicationUsagesByOtherApplications(String applicationName, UsageType usageType)
            throws UnifyException {
        final String applicationNameBase = applicationName + '.';
        List<Usage> usageList = new ArrayList<Usage>();
        // Notification template
        if (UsageType.isQualifiesEntity(usageType)) {
            List<NotificationTemplate> notificationTemplateList = environment()
                    .listAll(new NotificationTemplateQuery().entityBeginsWith(applicationNameBase)
                            .applicationNameNot(applicationName).addSelect("applicationName", "name", "entity"));
            for (NotificationTemplate notificationTemplate : notificationTemplateList) {
                Usage usage = new Usage(UsageType.ENTITY, "NotificationTemplate",
                        notificationTemplate.getApplicationName() + "." + notificationTemplate.getName(), "entity",
                        notificationTemplate.getEntity());
                usageList.add(usage);
            }
        }

        return usageList;
    }

    @Override
    public long countApplicationUsagesByOtherApplications(String applicationName, UsageType usageType)
            throws UnifyException {
        final String applicationNameBase = applicationName + '.';
        long usages = 0L;
        // Notification template
        if (UsageType.isQualifiesEntity(usageType)) {
            usages += environment().countAll(new NotificationTemplateQuery().entityBeginsWith(applicationNameBase)
                    .applicationNameNot(applicationName).addSelect("applicationName", "name", "entity"));
        }

        return usages;
    }

    @Override
    public List<Usage> findEntityUsages(String entity, UsageType usageType) throws UnifyException {
        List<Usage> usageList = new ArrayList<Usage>();
        // Notification template
        if (UsageType.isQualifiesEntity(usageType)) {
            List<NotificationTemplate> notificationTemplateList = environment().listAll(
                    new NotificationTemplateQuery().entity(entity).addSelect("applicationName", "name", "entity"));
            for (NotificationTemplate notificationTemplate : notificationTemplateList) {
                Usage usage = new Usage(UsageType.ENTITY, "NotificationTemplate",
                        notificationTemplate.getApplicationName() + "." + notificationTemplate.getName(), "entity",
                        notificationTemplate.getEntity());
                usageList.add(usage);
            }
        }

        return usageList;
    }

    @Override
    public long countEntityUsages(String entity, UsageType usageType) throws UnifyException {
        long usages = 0L;
        // Notification template
        if (UsageType.isQualifiesEntity(usageType)) {
            usages += environment().countAll(
                    new NotificationTemplateQuery().entity(entity).addSelect("applicationName", "name", "entity"));
        }

        return usages;
    }

    @Override
    protected void doInstallModuleFeatures(ModuleInstall moduleInstall) throws UnifyException {

    }
}

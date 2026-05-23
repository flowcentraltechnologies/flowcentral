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

package com.flowcentraltech.flowcentral.application.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.entities.AppHelpEntry;
import com.flowcentraltech.flowcentral.application.entities.AppHelpSheet;
import com.flowcentraltech.flowcentral.application.entities.AppHelpSheetQuery;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.ApplicationReplicationContext;
import com.flowcentraltech.flowcentral.common.constants.ConfigType;
import com.flowcentraltech.flowcentral.configuration.data.ApplicationInstall;
import com.flowcentraltech.flowcentral.configuration.data.ApplicationRestore;
import com.flowcentraltech.flowcentral.configuration.data.HelpSheetInstall;
import com.flowcentraltech.flowcentral.configuration.data.HelpSheetRestore;
import com.flowcentraltech.flowcentral.configuration.xml.AppConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppHelpSheetConfig;
import com.flowcentraltech.flowcentral.configuration.xml.HelpEntryConfig;
import com.flowcentraltech.flowcentral.configuration.xml.HelpSheetConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.application.InstallationContext;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.task.TaskMonitor;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Application help sheet installer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component(ApplicationModuleNameConstants.APPLICATION_HELPSHEET_INSTALLER)
public class ApplicationHelpSheetInstallerImpl extends AbstractApplicationArtifactInstaller {

    @Override
    public void installApplicationArtifacts(final TaskMonitor taskMonitor, final InstallationContext ctx,
            final ApplicationInstall applicationInstall) throws UnifyException {
        final AppConfig applicationConfig = applicationInstall.getApplicationConfig();
        final Long applicationId = applicationInstall.getApplicationId();

        logDebug(taskMonitor, "Executing help sheet installer...");
        final boolean deprecate = ctx.install(applicationConfig.getName());
        // Install configured help sheets
        if (deprecate) {
            environment().updateAll(new AppHelpSheetQuery().applicationId(applicationId).isStatic(),
                    new Update().add("deprecated", Boolean.TRUE));
        }

        if (applicationConfig.getHelpSheetsConfig() != null
                && !DataUtils.isBlank(applicationConfig.getHelpSheetsConfig().getHelpSheetList())) {
            for (AppHelpSheetConfig appHelpSheetConfig : applicationConfig.getHelpSheetsConfig().getHelpSheetList()) {
                HelpSheetInstall helpSheetInstall = getConfigurationLoader()
                        .loadHelpSheetInstallation(appHelpSheetConfig.getConfigFile());
                // Template
                HelpSheetConfig helpSheetConfig = helpSheetInstall.getHelpSheetConfig();
                String description = resolveApplicationMessage(helpSheetConfig.getDescription());
                String entity = ApplicationNameUtils.ensureLongNameReference(applicationConfig.getName(),
                        helpSheetConfig.getEntity());
                logDebug(taskMonitor, "Installing configured help sheet [{0}]...", description);

                AppHelpSheet oldAppHelpSheet = environment()
                        .findLean(new AppHelpSheetQuery().applicationId(applicationId).name(helpSheetConfig.getName()));

                if (oldAppHelpSheet == null) {
                    AppHelpSheet appHelpSheet = new AppHelpSheet();
                    appHelpSheet.setApplicationId(applicationId);
                    appHelpSheet.setName(helpSheetConfig.getName());
                    appHelpSheet.setDescription(description);
                    appHelpSheet.setEntity(entity);
                    appHelpSheet.setLabel(helpSheetConfig.getLabel());
                    appHelpSheet.setHelpOverview(helpSheetConfig.getHelpOverview());
                    appHelpSheet.setClassified(helpSheetConfig.getClassified());
                    appHelpSheet.setDeprecated(false);
                    appHelpSheet.setConfigType(ConfigType.STATIC);
                    populateChildList(appHelpSheet, helpSheetConfig);
                    environment().create(appHelpSheet);
                } else {
                    oldAppHelpSheet.setDescription(description);
                    oldAppHelpSheet.setEntity(entity);
                    oldAppHelpSheet.setLabel(helpSheetConfig.getLabel());
                    oldAppHelpSheet.setHelpOverview(helpSheetConfig.getHelpOverview());
                    oldAppHelpSheet.setClassified(helpSheetConfig.getClassified());
                    oldAppHelpSheet.setDeprecated(false);
                    oldAppHelpSheet.setConfigType(ConfigType.STATIC);
                    populateChildList(oldAppHelpSheet, helpSheetConfig);
                    environment().updateByIdVersion(oldAppHelpSheet);
                }
            }
        }

    }

    @Override
    public void restoreCustomApplicationArtifacts(TaskMonitor taskMonitor, ApplicationRestore applicationRestore)
            throws UnifyException {
        final AppConfig applicationConfig = applicationRestore.getApplicationConfig();
        final Long applicationId = applicationRestore.getApplicationId();

        // Help sheets
        logDebug(taskMonitor, "Executing help sheet restore...");
        if (!DataUtils.isBlank(applicationRestore.getNotifTemplateList())) {
            for (HelpSheetRestore helpSheetRestore : applicationRestore.getHelpSheetList()) {
                // Help sheet
                HelpSheetConfig helpSheetConfig = helpSheetRestore.getHelpSheetConfig();
                String description = resolveApplicationMessage(helpSheetConfig.getDescription());
                String entity = ApplicationNameUtils.ensureLongNameReference(applicationConfig.getName(),
                        helpSheetConfig.getEntity());
                logDebug(taskMonitor, "Restoring configured help sheet [{0}]...", description);

                AppHelpSheet appHelpSheet = new AppHelpSheet();
                appHelpSheet.setApplicationId(applicationId);
                appHelpSheet.setName(helpSheetConfig.getName());
                appHelpSheet.setDescription(description);
                appHelpSheet.setEntity(entity);
                appHelpSheet.setLabel(helpSheetConfig.getLabel());
                appHelpSheet.setHelpOverview(helpSheetConfig.getHelpOverview());
                appHelpSheet.setClassified(helpSheetConfig.getClassified());
                appHelpSheet.setDeprecated(false);
                appHelpSheet.setConfigType(ConfigType.CUSTOM);
                populateChildList(appHelpSheet, helpSheetConfig);
                environment().create(appHelpSheet);
            }
        }

    }

    @Override
    public void replicateApplicationArtifacts(TaskMonitor taskMonitor, Long srcApplicationId, Long destApplicationId,
            ApplicationReplicationContext ctx) throws UnifyException {
        // Help sheets
        logDebug(taskMonitor, "Replicating help sheets...");
        List<Long> sheetIdList = environment().valueList(Long.class, "id",
                new AppHelpSheetQuery().applicationId(srcApplicationId));
        for (Long sheetId : sheetIdList) {
            AppHelpSheet srcHelpSheet = environment().find(AppHelpSheet.class, sheetId);
            String oldDescription = srcHelpSheet.getDescription();
            srcHelpSheet.setId(null);
            srcHelpSheet.setApplicationId(destApplicationId);
            srcHelpSheet.setName(ctx.nameSwap(srcHelpSheet.getName()));
            srcHelpSheet.setDescription(ctx.messageSwap(srcHelpSheet.getDescription()));
            srcHelpSheet.setEntity(ctx.entitySwap(srcHelpSheet.getEntity()));
            srcHelpSheet.setDeprecated(false);
            srcHelpSheet.setConfigType(ConfigType.CUSTOM);
            environment().create(srcHelpSheet);
            logDebug(taskMonitor, "Help sheet [{0}] -> [{1}]...", oldDescription, srcHelpSheet.getDescription());
        }
    }

    @Override
    protected List<DeletionParams> getDeletionParams() throws UnifyException {
        return Arrays.asList(new DeletionParams("help sheets", new AppHelpSheetQuery()));
    }

    private void populateChildList(AppHelpSheet appHelpSheet, HelpSheetConfig helpSheetConfig) throws UnifyException {
        List<AppHelpEntry> entryList = null;
        if (helpSheetConfig.getEntries() != null && !DataUtils.isBlank(helpSheetConfig.getEntries().getEntryList())) {
            entryList = new ArrayList<AppHelpEntry>();
            for (HelpEntryConfig entryConfig : helpSheetConfig.getEntries().getEntryList()) {
                AppHelpEntry entry = new AppHelpEntry();
                entry.setFieldName(entryConfig.getFieldName());
                entry.setHelpContent(entryConfig.getHelpContent());
                entryList.add(entry);
            }
        }

        appHelpSheet.setEntryList(entryList);
    }

}

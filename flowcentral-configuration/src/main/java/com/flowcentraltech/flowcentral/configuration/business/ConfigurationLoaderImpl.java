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
package com.flowcentraltech.flowcentral.configuration.business;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.configuration.constants.ConfigurationModuleErrorConstants;
import com.flowcentraltech.flowcentral.configuration.constants.ConfigurationModuleNameConstants;
import com.flowcentraltech.flowcentral.configuration.constants.FlowCentralStaticSettings;
import com.flowcentraltech.flowcentral.configuration.data.ApplicationInstall;
import com.flowcentraltech.flowcentral.configuration.data.FlowCentralInstall;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.flowcentraltech.flowcentral.configuration.data.NotifTemplateInstall;
import com.flowcentraltech.flowcentral.configuration.data.ReportInstall;
import com.flowcentraltech.flowcentral.configuration.data.WorkflowInstall;
import com.flowcentraltech.flowcentral.configuration.data.WorkflowWizardInstall;
import com.flowcentraltech.flowcentral.configuration.xml.AppConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ModuleConfig;
import com.flowcentraltech.flowcentral.configuration.xml.NotifLargeTextConfig;
import com.flowcentraltech.flowcentral.configuration.xml.NotifTemplateConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ReportConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WfConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WfWizardConfig;
import com.flowcentraltech.flowcentral.configuration.xml.util.ConfigurationUtils;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyError;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.UnifyStaticSettings;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Default implementation of configuration loader.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(ConfigurationModuleNameConstants.CONFIGURATION_LOADER)
public class ConfigurationLoaderImpl extends AbstractUnifyComponent implements ConfigurationLoader {

    @Override
    public FlowCentralInstall loadMasterModuleInstallation() throws UnifyException {
        List<String> installerList = new ArrayList<String>();
        List<String> moduleXmlList = new ArrayList<String>();
        for (UnifyStaticSettings unifyStaticSettings : getStaticSettings()) {
            if (unifyStaticSettings instanceof FlowCentralStaticSettings) {
                FlowCentralStaticSettings fcStaticSettings = (FlowCentralStaticSettings) unifyStaticSettings;
                if (!StringUtils.isBlank(fcStaticSettings.moduleConfigName())) {
                    moduleXmlList.add(fcStaticSettings.moduleConfigName());

                    String installerName = fcStaticSettings.installerName();
                    if (!StringUtils.isBlank(installerName)) {
                        logDebug("Identified feature installer [{0}]...", installerName);
                        installerList.add(installerName);
                    }
                }
            }
        }

        List<ModuleInstall> moduleInstallList = getModuleInstallList(moduleXmlList);
        return new FlowCentralInstall(installerList, moduleInstallList);
    }

    @Override
    public ApplicationInstall loadApplicationInstallation(String configFile) throws UnifyException {
        logDebug("Loading application feature definitions from [{0}]...", configFile);
        AppConfig applicationConfig = ConfigurationUtils.readConfig(AppConfig.class, configFile,
                getUnifyComponentContext().getWorkingPath());
        processConfigErrors(ConfigurationUtils.validateApplicationConfig(applicationConfig),
                ConfigurationModuleErrorConstants.APPLICATIONCONFIG_VALIDATION_ERROR, configFile);

        logDebug("Loaded application feature definitions from [{0}] successfully.", configFile);
        return new ApplicationInstall(applicationConfig);
    }

    @Override
    public ReportInstall loadReportInstallation(String configFile) throws UnifyException {
        logDebug("Loading report definition from [{0}]...", configFile);
        ReportConfig reportConfig = ConfigurationUtils.readConfig(ReportConfig.class, configFile,
                getUnifyComponentContext().getWorkingPath());
        processConfigErrors(ConfigurationUtils.validateReportConfig(reportConfig),
                ConfigurationModuleErrorConstants.REPORTCONFIG_VALIDATION_ERROR, configFile);

        logDebug("Loaded report definition from [{0}] successfully.", configFile);
        return new ReportInstall(reportConfig);
    }

    @Override
    public NotifTemplateInstall loadNotifTemplateInstallation(String configFile) throws UnifyException {
        logDebug("Loading notification template definition from [{0}]...", configFile);
        NotifTemplateConfig notifTemplateConfig = ConfigurationUtils.readConfig(NotifTemplateConfig.class, configFile,
                getUnifyComponentContext().getWorkingPath());
        NotifLargeTextConfig notifLargeTextConfig = ConfigurationUtils.readConfig(NotifLargeTextConfig.class,
                configFile, getUnifyComponentContext().getWorkingPath());
        processConfigErrors(ConfigurationUtils.validateNotifTemplateConfig(notifTemplateConfig),
                ConfigurationModuleErrorConstants.NOTIFTEMPLATECONFIG_VALIDATION_ERROR, configFile);

        logDebug("Loaded notification template definition from [{0}] successfully.", configFile);
        return new NotifTemplateInstall(notifTemplateConfig, notifLargeTextConfig);
    }

    @Override
    public WorkflowInstall loadWorkflowInstallation(String configFile) throws UnifyException {
        logDebug("Loading workflow definition from [{0}]...", configFile);
        WfConfig wfConfig = ConfigurationUtils.readConfig(WfConfig.class, configFile,
                getUnifyComponentContext().getWorkingPath());
        processConfigErrors(ConfigurationUtils.validateWfConfig(wfConfig),
                ConfigurationModuleErrorConstants.WFCONFIG_VALIDATION_ERROR, configFile);

        logDebug("Loaded workflow definition from [{0}] successfully.", configFile);
        return new WorkflowInstall(wfConfig);
    }

    @Override
    public WorkflowWizardInstall loadWorkflowWizardInstallation(String configFile) throws UnifyException {
        logDebug("Loading workflow definition from [{0}]...", configFile);
        WfWizardConfig wfWizardConfig = ConfigurationUtils.readConfig(WfWizardConfig.class, configFile,
                getUnifyComponentContext().getWorkingPath());
        processConfigErrors(ConfigurationUtils.validateWfWizardConfig(wfWizardConfig),
                ConfigurationModuleErrorConstants.WFWIZARDCONFIG_VALIDATION_ERROR, configFile);

        logDebug("Loaded workflow definition from [{0}] successfully.", configFile);
        return new WorkflowWizardInstall(wfWizardConfig);
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    private List<ModuleInstall> getModuleInstallList(final List<String> moduleXmlList) throws UnifyException {
        List<ModuleInstall> list = new ArrayList<ModuleInstall>();
        for (String configFile : moduleXmlList) {
            logDebug("Loading module feature definitions from [{0}]...", configFile);
            ModuleConfig moduleConfig = ConfigurationUtils.readConfig(ModuleConfig.class, configFile,
                    getUnifyComponentContext().getWorkingPath());
            processConfigErrors(ConfigurationUtils.validateModuleConfig(moduleConfig),
                    ConfigurationModuleErrorConstants.MODULECONFIG_VALIDATION_ERROR, configFile);

            list.add(new ModuleInstall(moduleConfig));
            logDebug("Loaded module feature definitions from [{0}] successfully.", configFile);
        }

        return list;
    }

    private void processConfigErrors(List<UnifyError> errorList, String errorCode, String configFile)
            throws UnifyException {
        if (!DataUtils.isBlank(errorList)) {
            for (UnifyError ue : errorList) {
                logError(ue);
            }

            throw new UnifyException(errorCode, configFile);
        }
    }

}

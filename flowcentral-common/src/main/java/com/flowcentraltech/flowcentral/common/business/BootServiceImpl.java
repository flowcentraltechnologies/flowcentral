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
package com.flowcentraltech.flowcentral.common.business;

import java.util.List;

import com.flowcentraltech.flowcentral.common.constants.CommonModuleNameConstants;
import com.flowcentraltech.flowcentral.common.web.util.WidgetWriterUtils;
import com.flowcentraltech.flowcentral.configuration.business.ConfigurationLoader;
import com.flowcentraltech.flowcentral.configuration.data.FlowCentralInstall;
import com.flowcentraltech.flowcentral.configuration.data.ModuleInstall;
import com.tcdng.unify.core.UnifyComponentConfig;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.application.AbstractBootService;
import com.tcdng.unify.core.application.BootInstallationInfo;
import com.tcdng.unify.core.application.StartupShutdownHook;

/**
 * Application boot service implementation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(CommonModuleNameConstants.APPLICATION_BOOTSERVICE)
public class BootServiceImpl extends AbstractBootService<ModuleInstall> {

    @Configurable
    private ConfigurationLoader configurationLoader;

    public void setConfigurationLoader(ConfigurationLoader configurationLoader) {
        this.configurationLoader = configurationLoader;
    }

    @Override
    protected BootInstallationInfo<ModuleInstall> prepareBootInstallation() throws UnifyException {
        FlowCentralInstall flowCentralInstall = configurationLoader.loadMasterModuleInstallation();
        return new BootInstallationInfo<ModuleInstall>(flowCentralInstall.getInstallerList(),
                flowCentralInstall.getModuleInstallList());
    }

    @Override
    protected List<StartupShutdownHook> getStartupShutdownHooks() throws UnifyException {
        return null;
    }

    @Override
    protected void onStartup() throws UnifyException {
        WidgetWriterUtils.registerJSAliases();
        for (UnifyComponentConfig config : getComponentConfigs(PostBootSetup.class)) {
            PostBootSetup postBootSetup = (PostBootSetup) getComponent(config.getName());
            postBootSetup.performPostBootSetup();
        }
    }

    @Override
    protected void onShutdown() throws UnifyException {

    }
}

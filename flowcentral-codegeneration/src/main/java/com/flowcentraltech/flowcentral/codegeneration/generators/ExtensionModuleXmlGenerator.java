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

package com.flowcentraltech.flowcentral.codegeneration.generators;

import java.util.zip.ZipOutputStream;

import com.flowcentraltech.flowcentral.configuration.xml.ModuleConfig;
import com.flowcentraltech.flowcentral.configuration.xml.util.ConfigurationUtils;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.flowcentraltech.flowcentral.system.entities.Module;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Extension module XML generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("extension-module-xml-generator")
public class ExtensionModuleXmlGenerator extends AbstractResourcesArtifactGenerator {

    private static final String CONFIG_FOLDER = "config/";

    @Configurable
    private SystemModuleService systemModuleService;

    public ExtensionModuleXmlGenerator() {
        super(CONFIG_FOLDER); 
    }

    @Override
    protected void doGenerate(ExtensionModuleStaticFileBuilderContext ctx, String moduleName, ZipOutputStream zos)
            throws UnifyException {
        final String lowerCaseModuleName = moduleName.toLowerCase();
        final String filename =  "extension-" + lowerCaseModuleName + "-module.xml";
        openEntry(ctx, filename, zos);
        
        final ModuleConfig moduleConfig = new ModuleConfig();
        Module module = systemModuleService.findModule(moduleName);
        String descKey = "extension." + lowerCaseModuleName + ".description";
        String labelKey = "extension." + lowerCaseModuleName + ".label";
        ctx.addMessage(StaticMessageCategoryType.HEADER, descKey, module.getDescription());
        ctx.addMessage(StaticMessageCategoryType.HEADER, labelKey, module.getLabel());
        moduleConfig.setName(moduleName);
        moduleConfig.setDescription("$m{" + descKey + "}");
        moduleConfig.setLabel("$m{" + labelKey + "}");
        moduleConfig.setPrincipal(false); // Extension
        moduleConfig.setShortCode(module.getShortCode());
        moduleConfig.setModuleAppsConfig(ctx.getModuleAppsConfig());

        ConfigurationUtils.writeConfig(moduleConfig, zos);
        closeEntry(zos);
    }
}

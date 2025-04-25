/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.entities.AppHelpEntry;
import com.flowcentraltech.flowcentral.application.entities.AppHelpSheet;
import com.flowcentraltech.flowcentral.configuration.xml.AppHelpSheetConfig;
import com.flowcentraltech.flowcentral.configuration.xml.AppHelpSheetsConfig;
import com.flowcentraltech.flowcentral.configuration.xml.HelpEntriesConfig;
import com.flowcentraltech.flowcentral.configuration.xml.HelpEntryConfig;
import com.flowcentraltech.flowcentral.configuration.xml.HelpSheetConfig;
import com.flowcentraltech.flowcentral.configuration.xml.util.ConfigurationUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.NameUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Help sheets XML Generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("help-sheets-xml-generator")
public class HelpSheetsXmlGenerator extends AbstractResourcesArtifactGenerator {

    private static final String HELP_SHEET_FOLDER = "apps/help/sheet/";

    @Configurable
    private ApplicationModuleService applicationModuleService;

    public HelpSheetsXmlGenerator() {
        super(HELP_SHEET_FOLDER); 
    }

    @Override
    protected void doGenerate(ExtensionModuleStaticFileBuilderContext ctx, String applicationName, ZipOutputStream zos)
            throws UnifyException {
        // Sheets
        List<Long> helpSheetIdList = applicationModuleService.findCustomHelpSheetIdList(applicationName);
        if (!DataUtils.isBlank(helpSheetIdList)) {
            final String lowerCaseApplicationName = applicationName.toLowerCase();
            AppHelpSheetsConfig appHelpSheetsConfig = new AppHelpSheetsConfig();
            List<AppHelpSheetConfig> helpSheetList = new ArrayList<AppHelpSheetConfig>();
            for (Long helpSheetId : helpSheetIdList) {
                AppHelpSheetConfig appHelpSheetConfig = new AppHelpSheetConfig();
                AppHelpSheet appHelpSheet = applicationModuleService.findHelpSheet(helpSheetId);
                final String filename = StringUtils.dashen(NameUtils.describeName(appHelpSheet.getName())) + ".xml";
                openEntry(ctx, filename, zos);
                
                HelpSheetConfig helpSheetConfig = new HelpSheetConfig();
                String descKey = getDescriptionKey(lowerCaseApplicationName, "help", appHelpSheet.getName());
                ctx.addMessage(StaticMessageCategoryType.HELP, descKey, appHelpSheet.getDescription());

                helpSheetConfig.setName(appHelpSheet.getName());
                helpSheetConfig.setDescription("$m{" + descKey + "}");
                helpSheetConfig.setEntity(appHelpSheet.getEntity());
                helpSheetConfig.setLabel(appHelpSheet.getLabel());
                helpSheetConfig.setHelpOverview(appHelpSheet.getHelpOverview());

                List<HelpEntryConfig> entryList = new ArrayList<HelpEntryConfig>();
                for (AppHelpEntry entry: appHelpSheet.getEntryList()) {
                    HelpEntryConfig eConfig = new HelpEntryConfig();
                    eConfig.setFieldName(entry.getFieldName());
                    eConfig.setHelpContent(entry.getHelpContent());
                    entryList.add(eConfig);
                }
                
                helpSheetConfig.setEntries(new HelpEntriesConfig(entryList));
                
                ConfigurationUtils.writeConfigNoEscape(helpSheetConfig, zos);
                closeEntry(zos);
  
                appHelpSheetConfig.setConfigFile(HELP_SHEET_FOLDER + filename);
                helpSheetList.add(appHelpSheetConfig);
            }

            appHelpSheetsConfig.setHelpSheetList(helpSheetList);
            ctx.setAppHelpSheetsConfig(appHelpSheetsConfig);
        }
     }

}
